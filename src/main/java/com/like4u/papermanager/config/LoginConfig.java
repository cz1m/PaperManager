package com.like4u.papermanager.config;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/5 14:35
 */
public class LoginConfig {


    /**
     * 用户名长度限制
     */
    public static final int USERNAME_MIN_LENGTH = 2;
    public static final int USERNAME_MAX_LENGTH = 20;

    /**
     * 密码长度限制
     */
    public static final int PASSWORD_MIN_LENGTH = 5;
    public static final int PASSWORD_MAX_LENGTH = 20;
    public static final String CAPTCHA_CODE_KEY="verifyCode:";

}
