package com.like4u.papermanager.exception;

import com.like4u.papermanager.common.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ArithmeticException.class, NullPointerException.class})
    public String handleArithException(Exception e) {
        log.error("异常是：{}", e);
        return "index";

    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public AjaxResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        if (log.isErrorEnabled()) {
            log.warn(e.getMessage(), e);
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (ObjectError error : e.getAllErrors()) {
            stringBuilder.append("[");
            stringBuilder.append(((FieldError) error).getField());
            stringBuilder.append(" ");
            stringBuilder.append(error.getDefaultMessage());
            stringBuilder.append("]");

        }  return AjaxResult.warn(stringBuilder.toString());

    }

    @ExceptionHandler(value = {CaptchaExpireException.class})
    public AjaxResult handleCaptchaExpireException(CaptchaExpireException e){
        if (log.isErrorEnabled()) {
            log.warn(e.getMessage(), e);
        }
        return AjaxResult.error(e.getMessage());
    }

    @ExceptionHandler(value = {CaptchaException.class})
    public AjaxResult handleCaptchaException(CaptchaException e){
        if (log.isErrorEnabled()) {
            log.warn(e.getMessage(), e);
        }
        return AjaxResult.error(e.getMessage());
    }

}