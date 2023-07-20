package com.like4u.papermanager.common.filter;

import com.alibaba.druid.util.StringUtils;
import com.like4u.papermanager.Service.TokenService;
import com.like4u.papermanager.common.RedisCache;
import com.like4u.papermanager.domain.LoginUser;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;


/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/11 14:54
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    RedisCache redisCache;
    @Autowired
    TokenService tokenService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        /**
         * 从token里解析出uid，去redis把用户的登录信息拿出来
         * */
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (!Objects.isNull(loginUser)){
            /**
             * 如果能拿到说明用户是已登录状态，如果用户令牌还有20分钟以上才过期，就刷新令牌
             * */
            tokenService.verifyTokenTime(loginUser);
            UsernamePasswordAuthenticationToken authenticationToken=
                    new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        }
        filterChain.doFilter(request,response);

    }
}
