package com.pinyougou.manager.controller;

import entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import util.FastDFSClient;

@RestController
@RequestMapping("/upload")
public class UploadController {

    //获取properties配置文件的内容 spel
    @Value("${FILE_SERVER_URL}")
    private String FILE_SERVER_URL;

    /**
     * 接收页面提交的文件对象，完成文件上传操作
     */
    @RequestMapping("/uploadFile")
    public Result uploadFile(MultipartFile file){
        try {
            //获取文件源名称 "1.jpg"
            String originalFilename = file.getOriginalFilename();
            String extName =originalFilename.substring(originalFilename.lastIndexOf(".")+1);
            //基于FastDFS工具类实现文件上传操作
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:config/fdfs_client.conf");
            //参数一：文件对象内容 参数二：文件扩展名
            //path=/M00/00/00/wKgZhVyQVOGAEbRoAABanUWeaGY033.jpg
            String path = fastDFSClient.uploadFile(file.getBytes(), extName);
            System.out.println(path);
            String url =FILE_SERVER_URL+path;
            return new Result(true,url);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"上传失败");
        }
    }
}
