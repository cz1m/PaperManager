package com.like4u.papermanager.Controller;

import com.like4u.papermanager.Service.PaperService;
import com.like4u.papermanager.pojo.Paper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PaperController {
    @Autowired
    private PaperService paperService;
    @RequestMapping("/")
    public String index(){
        return "index";
    }
    @GetMapping("/paper")
    public String getPaper(Model model){
        List<Paper> papers= paperService.getAllPaper();
        model.addAttribute("papers",papers);
        return "papers";
    }

    @RequestMapping("/papers/add")
    public String toAdd(){
        return "paper_add";
    }
    //添加paper
    @PostMapping("/paper")
    public String addPaper(Paper paper,
                           @RequestParam("file") MultipartFile multipartFile,
                           HttpSession httpSession) throws IOException {
        ServletContext servletContext = httpSession.getServletContext();
        String filename = multipartFile.getOriginalFilename();
        String paperPath = servletContext.getRealPath("paper");
        File file=new File(paperPath);
        if (!file.exists()){
            file.mkdir();
        }

        //src
        String finalPath =paperPath+File.separator+filename;
        multipartFile.transferTo(new File(finalPath));
        paper.setSrc(finalPath);

        paperService.addPaper(paper);
        return "redirect:/paper";

    }


    //按照id查询
    @GetMapping("/paper/{id}")
    public String getPaperById(@PathVariable("id") Integer id,Model model){
        Paper paper= paperService.getPaperById(id);
        model.addAttribute("paper",paper);

        return "paper_update";
    }
    //修改paper
    @PutMapping("/paper")
    public String updatePaper(Paper paper,
                              @RequestParam("file") MultipartFile multipartFile,
                              HttpSession httpSession ) throws IOException {

        ServletContext servletContext = httpSession.getServletContext();
        String filename = multipartFile.getOriginalFilename();
        String paperPath = servletContext.getRealPath("paper");
        File file=new File(paperPath);
        if (!file.exists()){
            file.mkdir();
        }

        //src
        String finalPath =paperPath+File.separator+filename;
        multipartFile.transferTo(new File(finalPath));
        paper.setSrc(finalPath);
        paperService.updatePaper(paper);
        return "redirect:/paper";
    }
    //删除paper
    @DeleteMapping("/paper/{id}")
    public String deletePaper(@PathVariable("id") Integer id){
        paperService.deletePaper(id);

        return "redirect:/paper";

    }

    //在线阅读
    @GetMapping("/read/{id}")
    public String readPaper(@PathVariable("id")Integer id,Model model){
        String src = paperService.getSrcByID(id);
        if (src != null) {
            String fileContent = readFileContent(src);
            model.addAttribute("fileContent", fileContent);
        }

        return "readPaper";
    }
    private String readFileContent(String filePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    //下载
    @RequestMapping("/download/{id}")
    public ResponseEntity<byte[]> testResponseEntity(HttpSession session, @PathVariable("id")Integer id) throws
            IOException {
        /*
         *查看论文下载用户与时间
         * 1.获取sessionId(获取下载的用户)
         * 2.下载数量+1
         * 3.获取下载时间
         * 4.在userPaper表中添加数据
         *
         *
         * */
        //获取当前时间作为下载时间
        //LocalDateTime downloadTime=LocalDateTime.now();

        String sessionId = session.getId();

        //记录下载次数
        paperService.countDownload(id);

        //获取下载用户
        //Integer userID=UserService.getUserID();

        //将信息加入userPaper表
        //userPaperService.insert(downloadTime,userID,id);


//获取ServletContext对象
        ServletContext servletContext = session.getServletContext();
        //获取服务器文件
        String src = paperService.getSrcByID(id);



//创建输入流
        InputStream is = new FileInputStream(src);
//创建字节数组
        byte[] bytes = new byte[is.available()];
//将流读到字节数组中
        is.read(bytes);
//创建HttpHeaders对象设置响应头信息
        MultiValueMap<String, String> headers = new HttpHeaders();
//设置要下载方式以及下载文件的名字
        headers.add("Content-Disposition", "attachment;filename=1.txt");
//设置响应状态码
        HttpStatus statusCode = HttpStatus.OK;
//创建ResponseEntity对象
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(bytes, headers,
                statusCode);
//关闭输入流
        is.close();
        return responseEntity;
    }


    //上传文件
    @RequestMapping("/paper/upload/")
    public String testUpload(MultipartFile multipartFile,
                             HttpSession httpSession) throws IOException {
        ServletContext servletContext = httpSession.getServletContext();
        String filename = multipartFile.getOriginalFilename();
        String photoPath = servletContext.getRealPath("paper");
        File file=new File(photoPath);
        if (!file.exists()){
            file.mkdir();
        }

        //src
        String finalPath =photoPath+File.separator+filename;

        multipartFile.transferTo(new File(finalPath));
        return "success";


    }
    @RequestMapping("/paper/info")
    public String paperInfo(Model model){
        //todo:获取及格的（60-85）的论文数量，优秀 的论文数量>=85,论文总数
        Long CPaper=paperService.getCPaper();
        return "PaperInfo";
    }


}
