package com.like4u.papermanager.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/7 10:21
 */
public class CaptchaExpireException extends RuntimeException{

    public CaptchaExpireException() {
        super("验证码不存在");
    }

    public CaptchaExpireException(String message) {
        super(message);
    }
}
