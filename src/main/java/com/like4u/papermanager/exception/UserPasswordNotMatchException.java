package com.like4u.papermanager.exception;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/7 14:02
 */
public class UserPasswordNotMatchException extends RuntimeException {
    public UserPasswordNotMatchException() {

        super("用户名或密码不正确");
    }

    public UserPasswordNotMatchException(String message) {
        super(message);
    }
}
