package com.like4u.papermanager.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.like4u.papermanager.pojo.Pages;
import com.like4u.papermanager.pojo.Paper;
import com.like4u.papermanager.pojo.PaperDownload;
import com.like4u.papermanager.pojo.TongJi;

import java.time.LocalDateTime;
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

    void saveDownload(String username, LocalDateTime downloadTime, Integer id);

    void saveDownload(PaperDownload paperDownload);

    List<PaperDownload> selectDownload(Integer id);

    Pages searchByMajor(String major,Integer page);

    Pages rankByDownload(Integer page);
    public TongJi tongJi();
}
