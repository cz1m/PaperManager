package com.like4u.papermanager.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@TableName("paper")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Paper {
    @TableId(value = "paperID",type = IdType.AUTO)
    private Integer paperID;
    private String title;
    private String Abstract;
    private String type;
    private String major;
    private Integer pages;
    private Float score;
    private String author;
    private String advisor;
    private String src;
    private Integer downloadTime;
    private String uid;

}
