package com.like4u.papermanager.Service.impl;

import com.like4u.papermanager.Service.ConfigService;
import com.like4u.papermanager.common.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/26 10:48
 */
@Service
public class ConfigServiceImpl implements ConfigService {
    public static final String CAPTCHA_ENABLE_KEY="sys.account.captchaEnabled";
    public static final String MAIL_CAPTCHA_ENABLE_KEY="sys.account.mailCaptchaEnable";

    @Autowired
    private RedisCache redisCache;

    @Override
    public boolean selectCaptchaEnabled() {
        return selectConfigEnable(CAPTCHA_ENABLE_KEY);
    }

    @Override
    public boolean selectMailCaptchaEnable() {
        return selectConfigEnable(MAIL_CAPTCHA_ENABLE_KEY);
    }

    public boolean selectConfigEnable(String configKey){
        String configEnable = redisCache.getCacheObject(configKey);

        if (configEnable==null){

            //默认开启配置项
            redisCache.setCacheObject(configKey,"true");
            return true;
        }else {
            return configEnable.equals("true");
        }

    }


    @Override
    public boolean changeCaptchaEnable(String change) {
        return changeEnable(change,CAPTCHA_ENABLE_KEY);
    }

    @Override
    public boolean changeMailCaptchaEnable(String change) {
       return changeEnable(change,MAIL_CAPTCHA_ENABLE_KEY);
    }


    public boolean changeEnable(String change,String configKey) {
        switch (change){
            case "true":
            case"OK":
            case "1":
            case "yes":
                redisCache.setCacheObject(configKey,"true");
                break;
            case "false":
            case "0":
            case "no":
                redisCache.setCacheObject(configKey,"false");
                break;
            default:return false;
        }
        return true;
    }

}
