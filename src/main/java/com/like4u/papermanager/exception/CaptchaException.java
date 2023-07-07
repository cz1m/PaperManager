package com.like4u.papermanager.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/7 10:40
 */
@ResponseStatus(reason = "验证码错误")
public class CaptchaException extends RuntimeException{

    public CaptchaException() {
    }

    public CaptchaException(String message) {
        super(message);
    }

}
