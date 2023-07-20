package com.like4u.papermanager.Controller;

import com.like4u.papermanager.Service.LoginService;
import com.like4u.papermanager.common.AjaxResult;
import com.like4u.papermanager.config.LoginConfig;
import com.like4u.papermanager.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class LoginController {
    @Autowired
    LoginService loginService;

    @PostMapping("/login")

    public AjaxResult login(@Validated @RequestBody User user){ //RedirectAttributes
        AjaxResult ajax = AjaxResult.success();
        String token = loginService.login(user);
        ajax.put("token",token);
        return ajax;

    }
    @GetMapping("/logout")

    public AjaxResult logout(){
        return loginService.logout();
    }

}
