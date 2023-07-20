package com.like4u.papermanager.Service;


import com.like4u.papermanager.common.RedisCache;
import com.like4u.papermanager.config.LoginConfig;
import com.like4u.papermanager.domain.LoginUser;
import com.like4u.papermanager.pojo.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/7 13:41
 */
@Service
public class TokenService {
    @Value("${token.header}")
    private String header;

    // 令牌秘钥
    public static final String JWT_KEY = "secret";
    // redis缓存有效期（默认30分钟）
    @Value("${token.expireTime}")
    private int expireTime;
    @Value("${token.soloLogin}")
    private boolean soloLogin;

    @Autowired
    RedisCache redisCache;
    //令牌有效期

    private static final long EXPIRATION_TIME = 30 * 60 * 1000;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;


    public String createToken(LoginUser loginUser) {
        String uid = UUID.randomUUID().toString().replace("-", "");
        loginUser.setUid(uid);
        refreshToken(loginUser);
        Map<String, Object> claims = new HashMap<>();
        claims.put(LoginConfig.LOGIN_USER_KEY, uid);


        return createToken(claims);
    }

    /**
     * 通过claims生成jwt令牌
     */

    private String createToken(Map<String, Object> claims) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        long expMillis = nowMillis + EXPIRATION_TIME;
        Date expDate = new Date(expMillis);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expDate)
                .setIssuer("sg")
                .signWith(SignatureAlgorithm.HS512, secretKey()).compact();
        return token;
    }


    public void verifyTokenTime(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
            refreshToken(loginUser);
        }
    }


    /**
     * 刷新令牌有效期
     *
     * @param user 登录信息
     */
    public void refreshToken(LoginUser user) {


        user.setLoginTime(System.currentTimeMillis());
        user.setExpireTime(user.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(user.getUid());
        redisCache.setCacheObjectAndTTL(userKey, user, expireTime, TimeUnit.MINUTES);

        if (soloLogin) {
            // 缓存用户唯一标识，防止同一帐号，同时登录
            //key 用户数据库id  value redis 保存用户信息的key（uuid）
            String userIdKey = getUserIdKey(user.getUser().getUserId());
            redisCache.setCacheObjectAndTTL(userIdKey, userKey, expireTime, TimeUnit.MINUTES);
        }

    }



    /***
     *
     * @param token 用户唯一标识uid
     *       根据uid删除redis里面此用户的信息
     *
     * @param userid 用户id
     *               根据id删除用户的登录状态
     */

    public void delLoginUser(String token,Long userid) {
        if (StringUtils.hasText(token)) {
            String userKey = getTokenKey(token);
            redisCache.deleteObject(userKey);
        }
        if(soloLogin&&userid!=null){
            redisCache.deleteObject(getUserIdKey(userid));
        }
    }


    /**
     * 生成加密后的秘钥 secretKey
     *
     * @return
     */
    public static SecretKey secretKey() {
        byte[] encodedKey = Base64.getDecoder().decode(JWT_KEY);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey())
                .parseClaimsJws(token)
                .getBody();
    }

    /***
     *解析token获得用户
     * @param request 请求
     * @return 从请求中获取登录用户
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = getToken(request);
        return getLoginUserFromRedis(token);
    }

    private LoginUser getLoginUserFromRedis(String token) {
        if (StringUtils.hasText(token)) {
            Claims claims = parseToken(token);
            String uuid = (String) claims.get(LoginConfig.LOGIN_USER_KEY);
            String tokenKey = getTokenKey(uuid);
            return redisCache.getCacheObject(tokenKey);

        }
        return null;

    }

    /***
     * 获取请求头（Authorization）中的token信息
     * @param request 请求
     * @return 请求头携带的token
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(header);
        return token;
    }

    //通过uuid得到redis key
    private String getTokenKey(String uuid) {
        return LoginConfig.CAPTCHA_CODE_KEY + uuid;
    }

    private String getUserIdKey(Long userId) {
        return LoginConfig.LOGIN_USERID_KEY + userId;
    }
}
