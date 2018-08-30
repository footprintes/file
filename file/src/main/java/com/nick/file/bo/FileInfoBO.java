package com.nick.file.bo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @version V1.0
 * @ClassName：FileInfoBO
 * @author: hbj
 * @CreateDate：2018/8/27 19:34
 */
@Data
public class FileInfoBO {
    private MultipartFile file;
    private Integer createBy;
    private Integer fileMaxSize;
    private String whiteList;
}
