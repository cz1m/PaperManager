package com.like4u.papermanager.Service;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/26 10:48
 */
public interface ConfigService {
    boolean selectCaptchaEnabled();
    boolean selectMailCaptchaEnable();

    boolean changeCaptchaEnable(String change);

    boolean changeMailCaptchaEnable(String change);
}
