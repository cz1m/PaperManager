package com.like4u.papermanager.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.like4u.papermanager.pojo.Paper;

import java.util.List;

public interface PaperService extends IService<Paper> {
    List<Paper> getAllPaper();
}
