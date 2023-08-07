package com.like4u.papermanager.Controller;

import com.like4u.papermanager.Service.CaptchaService;
import com.like4u.papermanager.Service.LoginService;
import com.like4u.papermanager.common.AjaxResult;
import com.like4u.papermanager.config.LoginConfig;
import com.like4u.papermanager.pojo.User;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class LoginController {
    @Autowired
    LoginService loginService;
    @Autowired
    CaptchaService captchaService;

    @PostMapping("/login")
    @ApiOperation(value = "通过账号密码登录")
    public AjaxResult loginByPassword(@Validated @RequestBody User user){ //RedirectAttributes
        AjaxResult ajax = AjaxResult.success();
        String token = loginService.login(user);
        ajax.put("token",token);
        return ajax;

    }

    @PostMapping("/login/mail")
    @ApiOperation(value = "通过邮箱登录")
    public AjaxResult loginByMail(@RequestBody User user){

        AjaxResult ajax = AjaxResult.success();
        //先校验邮箱验证码
        captchaService.validateMailCode(user.getCode(), user.getEmail());
        String token = loginService.loginByEmail(user);
        ajax.put("token",token);
        return ajax;
    }

    @GetMapping("/logout")
    @ApiOperation(value = "登出接口")
    public AjaxResult logout(){
        return loginService.logout();
    }

}
