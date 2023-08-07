package com.like4u.papermanager.Controller;

import com.like4u.papermanager.common.annotation.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/25 15:39
 */
@Component
@Slf4j
public class TestController {
    @Log(name = "张三",age = 18)
    public int sayHello(){
        try {
            Class<?> aClass = Class.forName("com.like4u.papermanager.Controller.TestController");
            Method sayHello = aClass.getMethod("sayHello");


            if (sayHello.isAnnotationPresent(Log.class)){
                Log annotation = sayHello.getAnnotation(Log.class);
                log.warn("{}说：hello,我今年{}岁",annotation.name(),annotation.age());
                return annotation.age();
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
       return 400;
    }
}
