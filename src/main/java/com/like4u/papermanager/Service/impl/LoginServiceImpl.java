package com.like4u.papermanager.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.like4u.papermanager.Mapper.LoginMapper;
import com.like4u.papermanager.Service.LoginService;
import com.like4u.papermanager.Service.TokenService;
import com.like4u.papermanager.common.AjaxResult;
import com.like4u.papermanager.common.HttpStatus;
import com.like4u.papermanager.common.RedisCache;
import com.like4u.papermanager.config.LoginConfig;
import com.like4u.papermanager.exception.CaptchaException;
import com.like4u.papermanager.exception.CaptchaExpireException;
import com.like4u.papermanager.exception.UserPasswordNotMatchException;
import com.like4u.papermanager.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/5 14:21
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    LoginMapper loginMapper;
    @Autowired
    RedisCache redisCache;
    @Autowired
    private TokenService tokenService;
    @Override
    public String login(User user) {
        //验证码校验

        validateCaptcha(user.getUsername(),user.getCode(),user.getUuid());

        //登录校验用户名和密码

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,user.getUsername())
                        .eq(User::getPassword,user.getPassword());

        if ( loginMapper.selectOne(wrapper)==null){
            throw new UserPasswordNotMatchException();
        }

        String token = tokenService.createToken(user);

        return token;

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
