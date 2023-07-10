package com.like4u.papermanager.Service;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.like4u.papermanager.common.RedisCache;
import com.like4u.papermanager.config.LoginConfig;
import com.like4u.papermanager.pojo.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
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
    @Value("${token.secret}")
    private String secret;

    // 令牌有效期（默认30分钟）
    @Value("${token.expireTime}")
    private int expireTime;

    @Autowired
    RedisCache redisCache;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;


    public String createToken(User user){
        String token= UUID.randomUUID().toString();
        user.setToken(token);
        Map<String, Object> claims = new HashMap<>();
        claims.put(LoginConfig.LOGIN_USER_KEY, token);
        claims.put("username",user.getUsername());
        refreshToken(user);
        return createToken(claims);
    }

    /**
     * 通过claims生成jwt令牌
     * */

    private String createToken(Map<String, Object> claims)
    {
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
        return token;
    }


    /**
     * 刷新令牌有效期
     *
     * @param user 登录信息
     */
    public void refreshToken(User user)
    {
        user.setLoginTime(System.currentTimeMillis());
        user.setExpireTime(user.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(user.getToken());
        redisCache.setCacheObjectAndTTL(userKey, user, expireTime, TimeUnit.MINUTES);
    }


    public void delLoginUser(String token)
    {
        if (StringUtils.isNotEmpty(token))
        {
            String userKey = getTokenKey(token);
            redisCache.deleteObject(userKey);
        }
    }


    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims parseToken(String token)
    {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }


    private String getTokenKey(String uuid)
    {
        return LoginConfig.LOGIN_USER_KEY + uuid;
    }

}
