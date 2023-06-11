package com.like4u.papermanager.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.like4u.papermanager.pojo.Pages;
import com.like4u.papermanager.pojo.Paper;

import java.util.List;

public interface PaperService extends IService<Paper> {
    List<Paper> getAllPaper();

    void addPaper(Paper paper);

    Paper getPaperById(Integer id);

    void updatePaper(Paper paper);

    void deletePaper(Integer id);

    String getSrcByID(Integer id);

    void countDownload(Integer id);

    Long getCPaper();

    Long getPaperNum();


    Pages searchPaper(String title, String author, String teacher,Integer page);


    List<Paper> getUserByUid(String uid);

    public Pages getPaperPage();

    List<Paper> getPaperByPage(Integer page);

    Long getTotalBNum();
}
