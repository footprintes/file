package com.nick.file.service.impl;

import org.junit.Test;

import java.io.File;

/**
 * @version V1.0
 * @ClassName：AttachmentServiceImplTest
 * @author: hbj
 * @CreateDate：2018/8/28 14:31
 */
public class AttachmentServiceImplTest {

    @Test
    public void uploadFileSingle() {
    }

    @Test
    public void uploadFile() {
    }

    @Test
    public void insertPath() {
        String newPath = "C://user";
        File dir = new File(newPath);
        if (dir.exists()) {
            System.out.println("创建目录" + newPath + "失败，目标目录已经存在");
        }
        if (!newPath.endsWith(File.separator)) {
            newPath = newPath + File.separator;
        }
        // 创建目录
        if (dir.mkdirs()) {
            System.out.println("创建目录" + newPath + "成功！");
        } else {
            System.out.println("创建目录" + newPath + "失败！");
        }
    }
}