package com.like4u.papermanager.common.annotation;

import java.lang.annotation.*;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/25 15:36
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.METHOD})

public @interface Log {
    String name();
    int age();

}
