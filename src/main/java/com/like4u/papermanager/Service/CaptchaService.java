package com.like4u.papermanager.Service;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/27 11:11
 */
public interface CaptchaService {
     void validateCaptcha(String code, String uuid);
     void validateMailCode(String code,String email);
}
