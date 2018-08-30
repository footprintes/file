package com.nick.file.utils;

import org.springframework.web.multipart.MultipartFile;


/**
 * @version V1.0
 * @ClassName：FileTransferUtils
 * @author: hbj
 * @CreateDate：2018/8/27 19:34
 */
public class FileTransferUtils {
    private static final String DOT = ".";

    /**
     *
     * @Title:  20180725  校验文件类型是否符合要求
     * @Description: 1,
     * @param: [file, whiteList]
     *            description
     * @return: boolean
     * @auther: hbj
     * @date: 2018/7/25 21:04
     */
    public static final boolean checkFileType(MultipartFile file, String whiteList){
        String prefix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(DOT) + 1);
        String[] imgWhiteList = whiteList.split(",");
        boolean result = false;
        for (String imgType : imgWhiteList) {
            if ((imgType).equalsIgnoreCase(prefix)) {
                result = true;
            }
        }
        if (false == result) {
            return false;
        }
        return true;
    }

    /**
     *
     * @Title:  20180725  校验文件类型是否为PDF
     * @Description: 1,
     * @param: [file, whiteList]
     *            description
     * @return: boolean
     * @auther: hbj
     * @date: 2018/7/25 21:04
     */
    public static final boolean isPDF(MultipartFile file){
        String prefix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(DOT) + 1);
        String pdf = "pdf";
        if (!prefix.equals(pdf)){
            return false;
        }
        return true;
    }

    /**
     *
     * @Title:  20180725 校验文件大小是否超过规定值
     * @Description: 校验文件大小是否超过规定值
     * @param file 需要检测的文件，maxSize 上传文件允许的最大大小
     * @return 文件是否超过最大值
     */
    public static final boolean checkFileSize(MultipartFile file,int maxSize){
        if(file.getSize() > maxSize) {
            return false;
        }
        return true;
    }


    /**
     *
     * @Title:  20180725 校验文件名长度是否超过规定值
     * @Description: 校验文件名长度是否超过规定值
     * @param file 需要检测的文件，length 上传文件名允许的最大大小
     * @return 文件名长度是否超过规定值
     */
    public static final boolean checkFileNameLength(MultipartFile file,int length){
        if(file.getOriginalFilename().length()>length) {
            return false;
        }
        return true;
    }
}
