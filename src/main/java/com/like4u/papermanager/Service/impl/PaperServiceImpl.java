package com.like4u.papermanager.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.like4u.papermanager.Mapper.PaperMapper;
import com.like4u.papermanager.Service.PaperService;
import com.like4u.papermanager.pojo.Page;
import com.like4u.papermanager.pojo.Paper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    public Page getPaperPage() {
        LambdaQueryWrapper<Paper> wrapper=new LambdaQueryWrapper<>();

        List<Paper> papers = paperMapper.selectList(null);
        Long num = paperMapper.selectCount(null);
        Page page =new Page(papers,num);

        return page;
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

    @Override
    public Long getPaperNum() {
        LambdaQueryWrapper<Paper>wrapper=new LambdaQueryWrapper<>();
        wrapper.select(Paper::getSrc);
        Long paperNum = paperMapper.selectCount(wrapper);
        return paperNum;
    }

    @Override
    public List<Paper> searchPaper(String title, String author, String teacher) {
        LambdaQueryWrapper<Paper> wrapper =new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasLength(title),Paper::getTitle,title)
                .eq(StringUtils.hasLength(author),Paper::getAuthor,author)
                .eq(StringUtils.hasLength(teacher),Paper::getAdvisor,teacher);
        List<Paper> papers = paperMapper.selectList(wrapper);
        return papers;
    }

    //根据uid查papers


    @Override
    public List<Paper> getUserByUid(String uid) {
        LambdaQueryWrapper<Paper> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Paper::getUid,uid);
        List<Paper> papers = paperMapper.selectList(wrapper);
        return papers;
    }


}
