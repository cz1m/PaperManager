package com.like4u.papermanager.common.Aspect;

import com.like4u.papermanager.common.annotation.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/25 16:11
 */
@Aspect
@Component
public class AspectDev {

    @Pointcut("@annotation(com.like4u.papermanager.common.annotation.Log)")
    public void pointcut(){};


    @Before(value = "@annotation(log)")
    public void before(JoinPoint joinPoint, Log log){
        System.out.println(log);
        System.out.println("记录年龄"+log.age());

    }
    @After(value = "@annotation(log)")
    public void after(JoinPoint joinPoint, Log log){

        System.out.println("方法执行完成");
    }
    @AfterReturning(pointcut = "@annotation(log)",returning = "result")
    public void afterReturn(JoinPoint joinPoint, Log log, Integer result){
        System.out.println("方法名"+joinPoint.getSignature().getName());
        System.out.println("返回值"+result);

    }
    @AfterThrowing(value = "@annotation(log)",throwing = "e")
    public void afterThrow(JoinPoint joinPoint, Log log, Exception e){
        System.out.println("出现异常"+ e);

    }



}
