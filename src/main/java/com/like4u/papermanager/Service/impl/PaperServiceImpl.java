package com.like4u.papermanager.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.like4u.papermanager.Mapper.PaperDownloadMapper;
import com.like4u.papermanager.Mapper.PaperMapper;
import com.like4u.papermanager.Service.PaperService;
import com.like4u.papermanager.pojo.Pages;
import com.like4u.papermanager.pojo.Paper;
import com.like4u.papermanager.pojo.PaperDownload;
import com.like4u.papermanager.pojo.TongJi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaperServiceImpl extends ServiceImpl<PaperMapper,Paper> implements PaperService {
    @Autowired
private PaperMapper paperMapper;
    @Autowired
    private PaperDownloadMapper paperDownloadMapper;
    @Override
    public List<Paper> getAllPaper() {
      //  LambdaQueryWrapper<Paper> wrapper=new LambdaQueryWrapper<>();
        List<Paper> papers = paperMapper.selectList(null);

        return papers;
    }
    public Pages getPaperPage() {
        LambdaQueryWrapper<Paper> wrapper=new LambdaQueryWrapper<>();

        List<Paper> papers = paperMapper.selectList(null);
        Long num = paperMapper.selectCount(null);
        Pages pages =new Pages(papers,num);

        return pages;
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
        wrapper.le(Paper::getScore,85).gt(Paper::getScore,60);
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
    public Pages searchPaper(String title, String author, String teacher,Integer page) {
        Page<Paper> searchPage=new Page<>(page,10);
        LambdaQueryWrapper<Paper> wrapper =new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasLength(title),Paper::getTitle,title)
                .eq(StringUtils.hasLength(author),Paper::getAuthor,author)
                .eq(StringUtils.hasLength(teacher),Paper::getAdvisor,teacher);
        //List<Paper> papers = paperMapper.selectList(wrapper);
        Page<Paper> paperPage = paperMapper.selectPage(searchPage, wrapper);
        List<Paper> papers=paperPage.getRecords();//当前页的用户信息
        Long num = paperMapper.selectCount(wrapper);//总共根据条件能查出多少数据
        Pages pages =new Pages(papers,num);
        return pages;
    }

    //根据uid查papers


    @Override
    public List<Paper> getUserByUid(String uid) {
        LambdaQueryWrapper<Paper> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Paper::getUid,uid);
        List<Paper> papers = paperMapper.selectList(wrapper);
        return papers;
    }


    /**
     * 分页查询
     * */
    @Override
    public List<Paper> getPaperByPage(Integer pagenum) {
        Page<Paper> page=new Page<>(pagenum,10);
        LambdaQueryWrapper<Paper> wrapper=new LambdaQueryWrapper<>();
        Page<Paper> page1 = paperMapper.selectPage(page, wrapper);
        List<Paper> papers=page1.getRecords();
        return papers;
    }

    @Override
    public Long getTotalBNum() {
        Long num = paperMapper.selectCount(null);
        return num;

    }


    @Override
    public void saveDownload(String username, LocalDateTime downloadTime, Integer id) {

    }

    @Override
    public void saveDownload(PaperDownload paperDownload) {
        paperDownloadMapper.insert(paperDownload);
    }

    @Override
    public List<PaperDownload> selectDownload(Integer id) {
        List<PaperDownload> downloads = paperDownloadMapper.selectList(null);
        return downloads;
    }
    /**
     *
     * 根据专业查询
     * */

    @Override
    public Pages searchByMajor(String major,Integer num) {
        Page<Paper> paperPage=new Page<>(num,10);

        LambdaQueryWrapper<Paper> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Paper::getMajor,major);
        Page<Paper> paperPage1 = paperMapper.selectPage(paperPage, wrapper);

        List<Paper> papers = paperPage1.getRecords();
        long total = paperPage1.getTotal();
        Pages pages = new Pages(papers, total);

        return pages;
    }

    /**
     *
     * 下载次数排行*/

    @Override
    public Pages rankByDownload(Integer page) {
        List<Paper> papers= paperMapper.rankByDownload(page);
        Long num = paperMapper.selectCount(null);
        Pages pages = new Pages(papers, num);

        return pages;
    }

    public TongJi tongJi(){

        //文法
        Float count1 = paperMapper.getCount("文法学院");
        Float youXiu1 = paperMapper.getYouXiu("文法学院");
        Float jiGe1 = paperMapper.getJiGe("文法学院");
        Float a1=youXiu1/count1;
        Float b1=jiGe1/count1;

        //理学院
        Float count2 = paperMapper.getCount("理学院");
        Float youXiu2 = paperMapper.getYouXiu("理学院");
        Float jiGe2 = paperMapper.getJiGe("理学院");
        Float a2=youXiu2/count2;
        Float b2=jiGe2/count2;
        //工程学院
        Float count3 = paperMapper.getCount("工程学院");
        Float youXiu3 = paperMapper.getYouXiu("工程学院");
        Float jiGe3 = paperMapper.getJiGe("工程学院");
        Float a3=youXiu3/count3;
        Float b3=jiGe3/count3;
        //信息学院
        Float count4 = paperMapper.getCount("信息学院");
        Float youXiu4 = paperMapper.getYouXiu("信息学院");
        Float jiGe4 = paperMapper.getJiGe("信息学院");
        Float a4=youXiu4/count4;
        Float b4=jiGe4/count4;
        TongJi tongJi = new TongJi(a1, a2, a3, a4, b1, b2, b3, b4);
        return tongJi;


    }

}
