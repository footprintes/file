package com.nick.file.service;

import com.nick.file.bo.FileInfoBO;
import com.nick.file.utils.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @version V1.0
 * @ClassName：AttachmentService
 * @author: hbj
 * @CreateDate：2018/8/27 19:34
 */
public interface AttachmentService {
    ResponseResult uploadFileSingle(FileInfoBO fileInfoBO,Integer type);
    ResponseResult uploadFileZip(MultipartFile[] files,Integer userId,Integer type);
}
