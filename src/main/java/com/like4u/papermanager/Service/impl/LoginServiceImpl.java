package com.like4u.papermanager.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.like4u.papermanager.Mapper.LoginMapper;
import com.like4u.papermanager.Service.LoginService;
import com.like4u.papermanager.Service.TokenService;
import com.like4u.papermanager.common.AjaxResult;
import com.like4u.papermanager.common.HttpStatus;
import com.like4u.papermanager.common.RedisCache;
import com.like4u.papermanager.config.LoginConfig;
import com.like4u.papermanager.domain.LoginUser;
import com.like4u.papermanager.exception.CaptchaException;
import com.like4u.papermanager.exception.CaptchaExpireException;
import com.like4u.papermanager.exception.UserPasswordNotMatchException;
import com.like4u.papermanager.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/5 14:21
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Value("${token.soloLogin}")
    private boolean soloLogin;
    @Autowired
    LoginMapper loginMapper;
    @Autowired
    RedisCache redisCache;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Override
    public String login(User user) {
        //验证码校验

        validateCaptcha(user.getUsername(),user.getCode(),user.getUuid());
        Authentication authentication = null;

        try {
            //登录校验用户名和密码
            UsernamePasswordAuthenticationToken authenticationToken=
                    new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword());
            //authenticationManager会调用我们自己定义的userDetailsServiceImpl去查询数据库然后将返回信息封装到UserDetail返回
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            if(e instanceof BadCredentialsException){
                throw new UserPasswordNotMatchException();
            }
            else {
                throw new RuntimeException(e.getMessage());
            }

        }
        //从userDetail是我们自己的实现类loginUser
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        if (soloLogin) {
            // 如果用户不允许多终端同时登录，清除缓存信息
            String userIdKey = LoginConfig.LOGIN_USERID_KEY + loginUser.getUser().getUserId();
            String userKey = redisCache.getCacheObject(userIdKey);

            //如果 查出来的不是空的，就说明用户已经登录了，要踢人下线了
            if (userKey!=null) {
                redisCache.deleteObject(userIdKey);
                redisCache.deleteObject(userKey);
            }
        }


        //创建token，给loginUser创建uid，并把loginUser里面的权限信息等保存在redis
        String token = tokenService.createToken(loginUser);

        return token;

    }

    @Override
    public AjaxResult logout() {

        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser =(LoginUser) authentication.getPrincipal();
        tokenService.delLoginUser(loginUser.getUid(),loginUser.getUser().getUserId());

        return new AjaxResult(200,"注销成功");
    }

    /***
     *
     * @param username 用户名
     * @param code 验证码
     * @param uuid 验证码的标识
     */

    private void validateCaptcha(String username, String code, String uuid) {

        String verifyKey= LoginConfig.CAPTCHA_CODE_KEY+uuid;
        String captcha = redisCache.getCacheObject(verifyKey);
        if (captcha==null){
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha))
        {
            throw new CaptchaException();
        }

    }


}
