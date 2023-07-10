package com.like4u.papermanager.Controller;

import cn.hutool.core.codec.Base64;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.like4u.papermanager.common.AjaxResult;

import com.like4u.papermanager.common.RedisCache;
import com.like4u.papermanager.config.LoginConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/5 17:14
 */
@RestController
public class CaptchaController {
    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;
    @Autowired
    private RedisCache redisCache;


    @GetMapping("/captchaImage")
    public AjaxResult getCode(){
        AjaxResult ajax = AjaxResult.success();

        UUID uuid = UUID.randomUUID();

        String verifyKey = LoginConfig.CAPTCHA_CODE_KEY + uuid;


        String capStr = null, code = null;
        BufferedImage image = null;
        /*
        * 实现了Producer接口的 验证码生成器可以直接调用createText生成验证码
        * 验证码的格式由配置文件确定，这个实现类内部会自己去读取属性里的配置*/

        String capText = captchaProducerMath.createText();

        capStr = capText.substring(0, capText.lastIndexOf("@"));//算式

        code = capText.substring(capText.lastIndexOf("@") + 1);
        redisCache.setCacheObjectAndTTL(verifyKey,code,1, TimeUnit.MINUTES);
        image = captchaProducerMath.createImage(capStr);
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try
        {
            ImageIO.write(image, "jpg", os);
        }
        catch (IOException e)
        {
            return AjaxResult.error(e.getMessage());
        }


        ajax.put("uuid", uuid);

        ajax.put("img", Base64.encode(os.toByteArray()));
        return ajax;

    }





}
