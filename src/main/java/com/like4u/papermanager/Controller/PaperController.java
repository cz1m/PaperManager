package com.like4u.papermanager.Controller;

import com.like4u.papermanager.Service.PaperService;
import com.like4u.papermanager.pojo.Pages;
import com.like4u.papermanager.pojo.Paper;

import com.like4u.papermanager.pojo.PaperDownload;
import com.like4u.papermanager.pojo.TongJi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Controller
@Api("论文管理")
public class PaperController {
    @Autowired
    private PaperService paperService;
    @Autowired
    private RedisTemplate redisTemplate;
    @RequestMapping("/")
    public String index(){
        return "index";
    }


    //测试用注解
    //测试能否成功hook连接
    /**
     * 查询全部
     * */
    @PreAuthorize("hasAuthority('system:paper:query')")
    @GetMapping("/paper")
    @ApiOperation("查询全部论文")
    public String getPaper(Model model){
        List<Paper> papers= paperService.getAllPaper();
        //Pages pages = paperService.getPaperPage();
        model.addAttribute("papers",papers);
        return "papers";
        //return "papers";
    }


    //分页查询
    @ResponseBody
    @GetMapping("/paper/page")
    @ApiOperation("分页查询所有论文")
    public Pages getPaperByPage(@RequestParam(required = false) Integer page) {
        List<Paper> papers;
        if(page==null){
            papers = paperService.getPaperByPage(0);
        }else {
            papers=paperService.getPaperByPage(page);//获取到page页 的论文信息
             }
        Long Num = paperService.getTotalBNum();
        Pages pages=new Pages(papers,Num);
        return pages;
    }






    @RequestMapping("/papers/add")
    public String toAdd(){
        return "paper_add";
    }
    //添加paper
    @PostMapping("/paper")
    @ApiOperation("添加论文")
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
    //点击修改按钮跳转到用户的论文
    @GetMapping("/paper/update")
    //测试用注解，
    @ResponseBody
    @ApiOperation("修改指定论文")
    public List<Paper> toUpdate(Model model,
                           @ApiParam(required = true) @CookieValue("username")String uid){
        List<Paper> papers= paperService.getUserByUid(uid);
        model.addAttribute("papers",papers);
        return papers;
    }



    //按照id查询
    @GetMapping("/paper/{id}")
    @ApiOperation("按照id查询论文")
    public String getPaperById(@PathVariable("id") Integer id,
                               Model model){
        Paper paper= paperService.getPaperById(id);
        model.addAttribute("paper",paper);

            return "paper_update";

    }
    //修改paper
    @PutMapping("/paper")
    @ApiOperation("修改论文")
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
    @PutMapping("/papers")
    @ResponseBody
    public String updatePaper(Paper paper){
        paperService.updatePaper(paper);
        return "success";
    }





    //删除paper
    @DeleteMapping("/paper/{id}")
    @ApiOperation("根据id删除论文")
    public String deletePaper(@PathVariable("id") Integer id){
        paperService.deletePaper(id);

        return "redirect:/paper";

    }
/**
 * 在线阅读
 * */
    @GetMapping("/readonline/{id}")
    @ResponseBody
    @ApiOperation("根据id在线阅读论文")
    public String  test(@PathVariable("id")Integer id) throws IOException{

        String src = paperService.getSrcByID(id);
        String finalsrc="./thieise/"+src;
        File file=new File(finalsrc);
        System.out.println(file.exists()+"文件名"+file.getName());

        FileInputStream fileInputStream = new FileInputStream(file);
        XWPFDocument xwpfDocument=new XWPFDocument(fileInputStream);
        XWPFWordExtractor xwpfWordExtractor=new XWPFWordExtractor(xwpfDocument);
        String text = xwpfWordExtractor.getText();
        System.out.println(text);
        return text;

    }


    //下载
    @GetMapping("/download/d")
    @ApiOperation("论文下载")
    public ResponseEntity<byte[]> testResponseEntity(HttpSession session,
                                                     @RequestBody Paper paper,
                                                     @CookieValue("username") String username) throws
            IOException {
        /*
         *查看论文下载用户与时间
         * 1.获取sessionId(获取下载的用户)
         * 2.下载数量+1
         * 3.获取下载时间
         * 4.在Paper表中添加数据
         *
         *
         * */
        Integer id = paper.getPaperID();

        //获取当前时间作为下载时间
        LocalDateTime downloadTime=LocalDateTime.now();
        Date date = Date.from(downloadTime.atZone(ZoneId.systemDefault()).toInstant());

        //String sessionId = session.getId();

        //记录下载次数
        paperService.countDownload(id);

        //获取下载用户 username


        //将信息加入userdownload表;username,time,paperID
        PaperDownload paperDownload=new PaperDownload(null,id,username,date);
       // paperService.saveDownload(username,downloadTime,id);
        paperService.saveDownload(paperDownload);

/**
 * 下载
 * */

//获取ServletContext对象
        ServletContext servletContext = session.getServletContext();
        //获取服务器文件
        String src = paperService.getSrcByID(id);
        String finalsrc="thesis/"+src;
        String name = new File(finalsrc).getName();


//创建输入流
        InputStream is = new FileInputStream(finalsrc);
//创建字节数组
        byte[] bytes = new byte[is.available()];
//将流读到字节数组中
        is.read(bytes);
//创建HttpHeaders对象设置响应头信息
        MultiValueMap<String, String> headers = new HttpHeaders();
//设置要下载方式以及下载文件的名字
        headers.add("Content-Disposition", "attachment;filename="+name);
//设置响应状态码
        HttpStatus statusCode = HttpStatus.OK;
//创建ResponseEntity对象
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(bytes, headers,
                statusCode);
//关闭输入流
        is.close();
        return responseEntity;
    }

    @GetMapping("/download")
    @ResponseBody
    public List<PaperDownload>  selectDownloadMsg(@RequestBody Paper paper){
        Integer paperID = paper.getPaperID();
        List<PaperDownload>downloads= paperService.selectDownload(paperID);
        return downloads;
    }


    /**
     *
     * 下载次数排行*/
    @GetMapping("/rank")
    @ResponseBody
    public Pages rank(@RequestParam(required = false)Integer page){
        Pages pages;
        if (page==null){
            pages= paperService.rankByDownload(0);
        }
        else { int num=(page-1)*10;
            pages= paperService.rankByDownload(num);
        }

        return pages;
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
    @PreAuthorize("hasAuthority('system')")
    @GetMapping("/paper/info")
    @ApiOperation("论文的优秀率、合格率统计")
    public String paperInfo(Model model){
        //todo:获取及格的（60-85）的论文数量，优秀 的论文数量>=85,论文总数
        Long CPaper=paperService.getCPaper();
        Long PaperNum= paperService.getPaperNum();
        model.addAttribute("CPaper",CPaper);

        return "PaperInfo";
    }

    //根据//title,author,teacher进行条件查询
    @ResponseBody
    @PostMapping("/paper/search")
    @ApiOperation("根据title,author,teacher进行条件查询")
    public Pages searchPaper(@RequestBody Paper paper){
        Pages pages;
        System.out.println(paper);

        if (paper.getPage()==null){
             pages = paperService.searchPaper(paper.getTitle(), paper.getType(),paper.getAdvisor(), 0);
            System.out.println("错误的方法");

        }else {
             pages = paperService.searchPaper(paper.getTitle(), paper.getType(),paper.getAdvisor(), paper.getPage());
        }

        //List<Paper> papers= paperService.searchPaper(title,author,teacher,page);


        return pages;

    }


    /**
     * 分专业查询
     * */
    @GetMapping("/searchByMajor")
    @ResponseBody
    public Pages searchByMajor(@RequestParam()String major,
                               @RequestParam(required = false) Integer page ){
        Pages pages;
        if (page!=null){
            pages= paperService.searchByMajor(major,page);
        }else { pages= paperService.searchByMajor(major,1);}
        return pages;
    }

    @GetMapping("/school")
    @ResponseBody
    public TongJi searchBySchool(){

        TongJi tongJi = paperService.tongJi();
        return tongJi;

    }


    /**
     *
     * 测试redis
     * */
    @GetMapping("/redis")
    @ResponseBody
    public String testRedis(){
        return (String) redisTemplate.opsForValue().get("ord:790");

    }

}
