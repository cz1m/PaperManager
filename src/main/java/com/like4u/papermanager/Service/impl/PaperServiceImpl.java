package com.like4u.papermanager.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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

    @Override
    public void addPaper(Paper paper) {
        paperMapper.insert(paper);
    }

    @Override
    public Paper getPaperById(Integer id) {
        Paper paper=paperMapper.selectById(id);
        return paper;
    }

    @Override
    public void updatePaper(Paper paper) {
        paperMapper.updateById(paper);
    }

    @Override
    public void deletePaper(Integer id) {
        paperMapper.deleteById(id);
    }

    @Override
    public String getSrcByID(Integer id) {
        LambdaQueryWrapper<Paper>wrapper=new LambdaQueryWrapper<>();
        wrapper.select(Paper::getSrc).eq(Paper::getPaperID,id);
        Paper paper = paperMapper.selectOne(wrapper);

        return paper.getSrc();
    }
    //记录下载记录

    @Override
    public void countDownload(Integer id) {
        paperMapper.countDownload(id);

    }

    @Override
    public Long getCPaper() {
        LambdaQueryWrapper<Paper>wrapper=new LambdaQueryWrapper<>();
        wrapper.le(Paper::getScore,60).gt(Paper::getScore,85);
        Long CPaper = paperMapper.selectCount(wrapper);

        return CPaper ;
    }
}
