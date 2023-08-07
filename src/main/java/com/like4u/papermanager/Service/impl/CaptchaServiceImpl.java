package com.like4u.papermanager.Service.impl;

import com.like4u.papermanager.Service.CaptchaService;
import com.like4u.papermanager.Service.ConfigService;
import com.like4u.papermanager.common.RedisCache;
import com.like4u.papermanager.config.LoginConfig;
import com.like4u.papermanager.exception.CaptchaException;
import com.like4u.papermanager.exception.CaptchaExpireException;
import com.microsoft.schemas.vml.STTrueFalse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/27 11:12
 */
@Service
public class CaptchaServiceImpl implements CaptchaService {
    @Autowired
    private ConfigService configService;
    @Autowired
    private RedisCache redisCache;

    public void validateCaptcha( String code, String uuid) {
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if(captchaEnabled){
            String verifyKey= LoginConfig.CAPTCHA_CODE_KEY+uuid;
            String captcha = redisCache.getCacheObject(verifyKey);
            if (captcha==null){
                throw new CaptchaExpireException();
            }
            if (!code.equalsIgnoreCase(captcha))
            {
                throw new CaptchaException();
            }else redisCache.deleteObject(verifyKey);
        }

    }

    /**
     *
     * @param code 邮箱验证码
     * @param email 用户邮箱
     */

    @Override
    public void validateMailCode(String code, String email) {
        boolean mailCaptchaEnable = configService.selectMailCaptchaEnable();
        if (mailCaptchaEnable){
            String verifyKey=LoginConfig.MAIL_CODE_KEY+email;
            String captcha=redisCache.getCacheObject(verifyKey);
            if (captcha==null){
                throw new CaptchaException("邮件已过期");
            }if (!code.equals(captcha)){
                throw new CaptchaException("验证码不正确");
            }else redisCache.deleteObject(verifyKey);
        }

    }


}
