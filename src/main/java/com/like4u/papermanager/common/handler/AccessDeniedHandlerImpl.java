package com.like4u.papermanager.common.handler;

import com.alibaba.fastjson.JSON;
import com.like4u.papermanager.common.AjaxResult;
import com.like4u.papermanager.common.HttpStatus;
import com.like4u.papermanager.common.ServletUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/14 10:01
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        int code= HttpStatus.FORBIDDEN;
        String msg= JSON.toJSONString(AjaxResult.error(code,"您的账号权限不足，请联系管理员咨询详情"));
        ServletUtils.renderString(response,msg);
    }
}
