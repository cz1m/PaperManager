package com.like4u.papermanager.common.handler;

import com.alibaba.fastjson.JSON;
import com.like4u.papermanager.common.AjaxResult;
import com.like4u.papermanager.common.HttpStatus;
import com.like4u.papermanager.common.ServletUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/14 9:41
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        int code=HttpStatus.UNAUTHORIZED;
        String msg= JSON.toJSONString(AjaxResult.error(code,"请求访问"+request.getRequestURI()+"认证失败,无法访问系统资源"));
        ServletUtils.renderString(response,msg);
    }
}
