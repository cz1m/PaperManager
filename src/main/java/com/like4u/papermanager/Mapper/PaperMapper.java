package com.like4u.papermanager.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.like4u.papermanager.pojo.Paper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper

public interface PaperMapper extends BaseMapper<Paper> {
    void countDownload(@Param("id") Integer id);
}
