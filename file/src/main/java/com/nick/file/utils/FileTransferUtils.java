package com.nick.file.utils;

import com.nick.file.bo.FileInfoBO;
import com.nick.file.constant.UploadTypeEnum;
import com.nick.file.utils.spring.SpringContextHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;


/**
 * @version V1.0
 * @ClassName：FileTransferUtils
 * @author: hbj
 * @CreateDate：2018/8/27 19:34
 */
@Component
public class FileTransferUtils {
    private static final String DOT = ".";

//    @Value("${file.truepath}")
//    private String fileTruePath;
//    @Value("${file.virtualpath}")
//    private String fileVirtualPath;
//    @Value("${file.namemaxlength}")
//    private Integer fileNameMax;
//    @Value("${file.maxsize}")
//    private Integer fileSizeMax;
//    @Value("${file.whitelist.images}")
//    private String imgWhiteList;
//    @Value("${file.whitelist.words}")
//    private String wordWhiteList;
//    @Value("${file.whitelist.default}")
//    private String defaultWhiteList;

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

    /**
     *
     * @Title: 创建目录
     * @Description: 创建目录
     * @param newPath
     *            创建要上传的文件的目录
     * @return 是否创建成功
     */
    public static boolean insertPath(String newPath) {
        File dir = new File(newPath);
        if (dir.exists()) {
            System.out.println("创建目录" + newPath + "失败，目标目录已经存在");
            return false;
        }
        if (!newPath.endsWith(File.separator)) {
            newPath = newPath + File.separator;
        }
        // 创建目录
        if (dir.mkdirs()) {
            System.out.println("创建目录" + newPath + "成功！");
            return true;
        } else {
            System.out.println("创建目录" + newPath + "失败！");
            return false;
        }
    }

    public FileInfoBO setFileInfo(FileInfoBO fileInfoBO, Integer type){
        Environment env = SpringContextHolder.getBean(Environment.class);
        String imgWhiteList = env.getProperty("file.whitelist.images");
        String defaultWhiteList = env.getProperty("file.whitelist.default");
        Integer fileSizeMax = Integer.parseInt(env.getProperty("file.maxsize"));

        if (UploadTypeEnum.USER_ICON_IMAGE.equals(type)){
            fileInfoBO.setFileMaxSize(fileSizeMax);
            fileInfoBO.setWhiteList(imgWhiteList);
            return fileInfoBO;
        }
        if (UploadTypeEnum.MOVIE_IMAGE.equals(type)){
            fileInfoBO.setFileMaxSize(fileSizeMax);
            fileInfoBO.setWhiteList(imgWhiteList);
            return fileInfoBO;
        }
        fileInfoBO.setFileMaxSize(fileSizeMax);
        fileInfoBO.setWhiteList(defaultWhiteList);
        return fileInfoBO;

    }
}
