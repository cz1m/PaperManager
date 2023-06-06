package com.like4u.papermanager.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.like4u.papermanager.Mapper.PaperMapper;
import com.like4u.papermanager.Service.PaperService;
import com.like4u.papermanager.pojo.Paper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaperServiceImpl extends ServiceImpl<PaperMapper,Paper> implements PaperService {
    @Autowired
private PaperMapper paperMapper;
    @Override
    public List<Paper> getAllPaper() {
      //  LambdaQueryWrapper<Paper> wrapper=new LambdaQueryWrapper<>();
        List<Paper> papers = paperMapper.selectList(null);

        return papers;
    }
}
