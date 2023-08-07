package com.like4u.papermanager.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.like4u.papermanager.Mapper.LoginMapper;
import com.like4u.papermanager.Mapper.MenuMapper;
import com.like4u.papermanager.Service.CaptchaService;
import com.like4u.papermanager.Service.ConfigService;
import com.like4u.papermanager.Service.RegisterService;
import com.like4u.papermanager.common.RedisCache;
import com.like4u.papermanager.common.utlis.StringUtils;
import com.like4u.papermanager.common.utlis.TimeUtils;
import com.like4u.papermanager.config.LoginConfig;
import com.like4u.papermanager.exception.CaptchaException;
import com.like4u.papermanager.exception.CaptchaExpireException;
import com.like4u.papermanager.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/26 13:31
 */
@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private LoginMapper loginMapper;
    @Autowired
    private ConfigService configService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private CaptchaService captchaService;

    @Override
    public String userRegister(User user) {
        String msg="";

       /* //如果开启了验证码验证
        captchaService.validateCaptcha(user.getCode(),user.getUuid());*/
        captchaService.validateMailCode(user.getCode(),user.getEmail());


        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        User registerUser = loginMapper.selectOne(wrapper);

        if (registerUser != null) {
            msg="用户已存在";
        }else{
            String encodePassword = bCryptPasswordEncoder.encode(user.getPassword());

            user.setCreateTime(TimeUtils.nowTime());
            user.setPassword(encodePassword);

            int insert = loginMapper.insert(user);
            Long userId = user.getUserId();
            //默认给个游客权限
            menuMapper.addUserWithVisitRole(userId);

            if (insert!=1) msg="注册时出现错误，请联系管理员";
        }
        return msg;
    }




}
