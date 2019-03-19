package com.pinyougou.shop.controller;

import entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import util.FastDFSClient;

/**
 * @ClassName: UploadController
 * @Description: 文件上传表现层
 * @Author: XuZhao
 * @CreateDate: 19/03/19$ 下午 04:55$
 */
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Value("${FILE_SERVER_URL}")
    private String FILE_SERVER_URL;

    /**
    * @Description: 接收页面提交的文件对象,完成文件上传
    * @Author:      XuZhao
    * @CreateDate:  19/03/19 下午 04:56
    */
    @RequestMapping("/uploadFile")
    public Result uploadFile(MultipartFile file){
        try {
            //获取文件原名称
            String originalFilename = file.getOriginalFilename();
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            //基于FastDFS工具类实现文件上传操作
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:config/fdfs_client.conf");

            String path = fastDFSClient.uploadFile(file.getBytes(), extName);
            System.out.println(path);
            String url = FILE_SERVER_URL+path;
            return new Result(true,url);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"上传失败!!");
        }
    }

}
