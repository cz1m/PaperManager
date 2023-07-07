package com.like4u.papermanager.Controller;

import com.like4u.papermanager.Service.LoginService;
import com.like4u.papermanager.common.AjaxResult;
import com.like4u.papermanager.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class LoginController {
    @Autowired
    LoginService loginService;
    @GetMapping("/login")
    public String login(){
        return "/login";
    }
    @PostMapping("/login")

    public AjaxResult login(@Validated @RequestBody User user){ //RedirectAttributes
        return loginService.login(user);
    }



}
