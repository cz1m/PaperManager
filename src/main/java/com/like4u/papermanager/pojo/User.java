package com.like4u.papermanager.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("t_user")

public class User {

    /**
     * 所有属性都应该在数据库中
     */

    @TableId(value = "user_id",type = IdType.AUTO)
    private Integer userId;
    @Size(max = 10,min = 2,message = "用户名长度为2到10位")
    private String username;
    @Size(max = 20,min = 6,message = "密码长度为6到20位")
    private String password;
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
    @TableField(exist = false)
    private String token;




}
