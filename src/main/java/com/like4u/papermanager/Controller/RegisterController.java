package com.like4u.papermanager.Controller;

import com.like4u.papermanager.Service.ConfigService;
import com.like4u.papermanager.Service.RegisterService;
import com.like4u.papermanager.common.AjaxResult;
import com.like4u.papermanager.common.utlis.StringUtils;
import com.like4u.papermanager.pojo.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/26 13:30
 */
@Api("注册接口")
@RestController
public class RegisterController {

    @Autowired
    private RegisterService registerService;


    @ApiOperation("用户注册接口，需要上传验证码")
    @PostMapping("/user/register")
    public AjaxResult userRegister(@Validated @RequestBody User user){
        String msg= registerService.userRegister(user);
        if (StringUtils.isEmpty(msg)){
            return AjaxResult.success("注册成功");
        }else return AjaxResult.error(msg);

    }

}
