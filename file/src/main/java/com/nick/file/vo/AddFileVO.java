package com.nick.file.vo;


import lombok.Data;

/**
 * @version V1.0
 * @ClassName：AddFileVO
 * @author: hbj
 * @CreateDate：2018/8/29 17:01
 */
@Data
public class AddFileVO {
    private String id;
    private String name;
    private String ext;
    private String path;
    private String uploadFileName;
    private Integer size;
    private String parentId;
    private Integer fileCount;
    private Integer createBy;
    private Long createDate;
}
