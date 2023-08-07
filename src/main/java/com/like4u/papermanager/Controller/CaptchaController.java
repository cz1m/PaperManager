package com.like4u.papermanager.Controller;

import cn.hutool.core.codec.Base64;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.like4u.papermanager.Service.CaptchaService;
import com.like4u.papermanager.Service.ConfigService;
import com.like4u.papermanager.Service.MailService;
import com.like4u.papermanager.Service.RegisterService;
import com.like4u.papermanager.common.AjaxResult;

import com.like4u.papermanager.common.RedisCache;
import com.like4u.papermanager.config.LoginConfig;
import com.like4u.papermanager.pojo.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

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
@Api("验证码接口")
public class CaptchaController {
    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    RegisterService registerService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private MailService mailService;

    @ApiOperation("验证码的开关")
    @PreAuthorize("hasAuthority('system')")
    @GetMapping("/config/captchaEnable")
    public AjaxResult changeCaptchaEnable(@RequestParam String change){

        if (configService.changeCaptchaEnable(change)){
            return AjaxResult.success("成功修改验证码状态，目前状态为："+change);
        }else return AjaxResult.error("修改验证码状态失败");

    }
    @ApiOperation("邮箱验证码的开关")
    @PreAuthorize("hasAuthority('system')")
    @GetMapping("/config/MailCaptchaEnable")
    public AjaxResult changeMailCaptchaEnable(@RequestParam String change){

        if (configService.changeMailCaptchaEnable(change)){
            return AjaxResult.success("成功修改验证码状态，目前状态为："+change);
        }else return AjaxResult.error("修改验证码状态失败");

    }


    @ApiOperation("发送验证码")
    @GetMapping("/captchaImage")
    public AjaxResult getCode(){
        AjaxResult ajax = AjaxResult.success();

        String uuid = UUID.randomUUID().toString().replace("-","");

        String verifyKey = LoginConfig.CAPTCHA_CODE_KEY + uuid;


        String capStr = null, code = null;
        BufferedImage image = null;
        /*
        * 实现了Producer接口的 验证码生成器可以直接调用createText生成验证码
        * 验证码的格式由配置文件确定，这个实现类内部会自己去读取属性里的配置*/

        String capText = captchaProducerMath.createText();

        capStr = capText.substring(0, capText.lastIndexOf("@"));//算式

        code = capText.substring(capText.lastIndexOf("@") + 1);
        redisCache.setCacheObjectAndTTL(verifyKey,code,5, TimeUnit.MINUTES);
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

    /**
     *
     * 发邮件之前应该先检测验证码
     */
    @ApiOperation("注册登录邮件验证码")
    @PostMapping("/captchaMail")
    public void sendCaptchaByMail(@RequestBody User user){

        captchaService.validateCaptcha(user.getCode(), user.getUuid());

        //发送验证码
        mailService.sendRegisterMail(user);


    }





}
