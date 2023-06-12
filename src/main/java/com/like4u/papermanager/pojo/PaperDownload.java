package com.like4u.papermanager.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

@TableName("paperdownload")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaperDownload {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer paperID;
    private String username;
    private Date time;

}
