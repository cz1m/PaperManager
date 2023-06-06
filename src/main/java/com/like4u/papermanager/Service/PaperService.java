package com.like4u.papermanager.Service;

import com.baomidou.mybatisplus.extension.service.IService;
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
}
