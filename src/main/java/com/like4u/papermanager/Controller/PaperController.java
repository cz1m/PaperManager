package com.like4u.papermanager.Controller;

import com.like4u.papermanager.Service.PaperService;
import com.like4u.papermanager.pojo.Paper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@ResponseBody
public class PaperController {
    @Autowired
    private PaperService paperService;
    @RequestMapping("/user")
    public List<Paper> getUser(){
        List<Paper> allPaper=paperService.getAllPaper();

        return allPaper;
    }


}
