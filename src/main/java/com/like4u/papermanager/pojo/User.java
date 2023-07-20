package com.like4u.papermanager.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("sys_user")

public class User {

    /**
     * 所有属性都应该在数据库中
     */

    @TableId(value = "id",type = IdType.AUTO)
    private Long userId;
    @Size(max = 10,min = 2,message = "用户名长度为2到10位")
    @TableField(value = "user_name")
    private String username;

    /**
     * 昵称
     */
    @TableField(value = "nick_name")
    private String nickName;

    @Size(max = 20,min = 6,message = "密码长度为6到20位")
    private String password;

    /**
     * 账号状态（0正常 1停用）
     */
    private String status;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String phonenumber;
    /**
     * 用户性别（0男，1女，2未知）
     */
    private String sex;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 用户类型（0管理员，1普通用户）
     */
    private String userType;
    /**
     * 创建人的用户id
     */
    private Long createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新人
     */
    private Long updateBy;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    @TableLogic(value = "0",delval = "1")
    private Integer delFlag;
    /**
     * 验证码标识
     * */
    @TableField(exist = false)
    private String uuid;

    /**
     * 验证码
     * */
    @TableField(exist = false)
    private String code;






}
