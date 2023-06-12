package com.like4u.papermanager.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.like4u.papermanager.pojo.Paper;
import com.like4u.papermanager.pojo.School;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Scanner;

@Mapper

public interface PaperMapper extends BaseMapper<Paper> {
    void countDownload(@Param("id") Integer id);

    List<Paper> rankByDownload(@Param("page") Integer page);

    List<School> searchSchool(@Param("school") String school);

    Float getCount(@Param("school") String school);

    Float getYouXiu(@Param("school") String school);

    Float getJiGe(@Param("school") String school);


}
