package com.like4u.papermanager.exception;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/27 10:01
 */
public class SendMailFailException extends RuntimeException{
    public SendMailFailException() {
        super("发送邮件失败");
    }

    public SendMailFailException(String message) {
        super(message);
    }
}
