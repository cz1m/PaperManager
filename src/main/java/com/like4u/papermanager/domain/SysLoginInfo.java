package com.like4u.papermanager.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/28 13:12
 */
@Data
@TableName("sys_logininfor")
public class SysLoginInfo {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(value = "info_id",type = IdType.AUTO)
    private Long infoId;

    /** 用户账号 */
    private String username;

    /** 登录状态 0成功 1失败 */
    private String status;

    /** 登录IP地址 */
    @TableField(value = "ip_address")
    private String ipAddress;

    /** 登录地点 */
    @TableField(value = "login_location")
    private String loginLocation;

    /** 浏览器类型 */
    private String browser;

    /** 操作系统 */
    private String os;

    /** 提示消息 */
    private String msg;

    /** 访问时间 */
    private Date loginTime;
}
