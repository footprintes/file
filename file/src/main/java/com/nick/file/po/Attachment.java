package com.nick.file.po;

import lombok.Data;

import javax.persistence.*;

/**
 * @version V1.0
 * @ClassName：Attachment
 * @author: hbj
 * @CreateDate：2018/8/27 19:34
 */
@Entity
@Data
public class Attachment {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "ext")
    private String ext;
    @Column(name = "path")
    private String path;
    @Column(name = "upload_file_name")
    private String uploadFileName;
    @Column(name = "size")
    private Integer size;
    @Column(name = "parent_id")
    private String parentId;
    @Column(name = "file_count")
    private Integer fileCount;
    @Column(name = "create_by")
    private Integer createBy;
    @Column(name = "create_date")
    private Long createDate;
}
