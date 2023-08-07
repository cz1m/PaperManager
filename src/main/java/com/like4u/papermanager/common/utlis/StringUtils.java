package com.like4u.papermanager.common.utlis;

import java.util.Random;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/26 13:41
 */
public class StringUtils {

    public static boolean isNull(Object object)
    {
        return object == null;
    }

    /**
     * * 判断一个对象是否非空
     *
     * @param object Object
     * @return true：非空 false：空
     */
    public static boolean isNotNull(Object object)
    {
        return !isNull(object);
    }

    /**
     * * 判断一个字符串是否为空串
     *
     * @param str String
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(String str)
    {
        return isNull(str) ||"".equals(str.trim());
    }

    /**
     * * 判断一个字符串是否为非空串
     *
     * @param str String
     * @return true：非空串 false：空串
     */
    public static boolean isNotEmpty(String str)
    {
        return !isEmpty(str);
    }

    /**
     * 生成任意位数字验证码
     */
    public static String generateVerificationCode(int length) {
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < length; i++) {
            code.append(random.nextInt(10));
        }

        return code.toString();
    }
}
