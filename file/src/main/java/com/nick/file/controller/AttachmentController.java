package com.nick.file.controller;

import com.nick.file.bo.FileInfoBO;
import com.nick.file.service.AttachmentService;
import com.nick.file.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @version V1.0
 * @ClassName：AttachmentController
 * @author: hbj
 * @CreateDate：2018/8/27 19:34
 */
@RestController
@RequestMapping(value = "/attachment/file")
public class AttachmentController {
    @Autowired
    private AttachmentService attachmentService;

    @PostMapping("/upload/single")
    public ResponseResult uploadFileSingle(@RequestPart("file") MultipartFile multipartFile, @RequestParam("userId") Integer userId, @RequestParam("type") Integer type){
        FileInfoBO fileInfoBO = new FileInfoBO();
        fileInfoBO.setCreateBy(userId);
        fileInfoBO.setFile(multipartFile);
        ResponseResult responseResult = attachmentService.uploadFileSingle(fileInfoBO,type);
        return responseResult;
    }

    @PostMapping("/upload/zip")
    public ResponseResult uploadFileZip(@RequestPart("files") MultipartFile[] multipartFiles, @RequestParam("userId") Integer userId, @RequestParam("type") Integer type){
        ResponseResult responseResult = attachmentService.uploadFileZip(multipartFiles,userId,type);
        return responseResult;
    }
}
