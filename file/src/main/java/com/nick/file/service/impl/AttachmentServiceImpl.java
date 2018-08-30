package com.nick.file.service.impl;

import com.nick.file.bo.FileInfoBO;
import com.nick.file.constant.UploadTypeEnum;
import com.nick.file.po.Attachment;
import com.nick.file.repositories.FileRepositories;
import com.nick.file.service.AttachmentService;
import com.nick.file.utils.DateUtil;
import com.nick.file.utils.FileTransferUtils;
import com.nick.file.utils.ResponseResult;
import com.nick.file.utils.ZipFileUtil;
import com.nick.file.vo.AddFileVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipOutputStream;

/**
 * @version V1.0
 * @ClassName：AttachmentServiceImpl
 * @author: hbj
 * @CreateDate：2018/8/27 19:34
 */
@Service
public class AttachmentServiceImpl implements AttachmentService {

    @Autowired
    private FileRepositories fileRepositories;

    @Autowired
    private MessageSource messageSource;


    @Value("${file.truepath}")
    private String fileTruePath;
    @Value("${file.virtualpath}")
    private String fileVirtualPath;
    @Value("${file.namemaxlength}")
    private Integer fileNameMax;
    @Value("${file.maxsize}")
    private Integer fileSizeMax;
    @Value("${file.whitelist.images}")
    private String imgWhiteList;
    @Value("${file.whitelist.words}")
    private String wordWhiteList;
    @Value("${file.whitelist.default}")
    private String defaultWhiteList;


    private static final Integer TYPR_ERROR = 1;
    private static final Integer SIZE_ERROR = 2;
    private static final Integer NAME_ERROR = 3;
    private static final Integer FILE_OK = 4;
    private static final String DOT = ".";
    private static final String SLASH = "/";
    private static final String ZIP_PREFIX = "zip";


    /**
     *
     * @Title: 上传单个文件
     * @Description: 1,
     * @param: [fileInfoBO, type]
     *            description
     * @return: com.nick.file.utils.ResponseResult
     * @auther: hbj
     * @date: 2018/8/29 19:00
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseResult uploadFileSingle(FileInfoBO fileInfoBO,Integer type) {
        //获取当前的系统语言
        Locale locale = LocaleContextHolder.getLocale();

        //对上传的文件进行校验
        if (!(UploadTypeEnum.MOVIE_IMAGE.getCode().equals(type) || UploadTypeEnum.USER_ICON_IMAGE.getCode().equals(type))){
            return new ResponseResult().code(Integer.parseInt(messageSource.getMessage("input.type.error.code",null,locale)))
                    .fail(messageSource.getMessage("input.type.error.message",null,locale));
        }

        this.setFileInfo(fileInfoBO,type);
        Integer result = this.checkFile(fileInfoBO);
        if (TYPR_ERROR.equals(result)){
            return new ResponseResult().code(Integer.parseInt(messageSource.getMessage("file.type.error.code",null,locale)))
                    .fail(messageSource.getMessage("file.type.error.message",null,locale));
        }
        if (SIZE_ERROR.equals(result)){
            return new ResponseResult().code(Integer.parseInt(messageSource.getMessage("file.size.error.code",null,locale)))
                    .fail(messageSource.getMessage("file.size.error.message",null,locale));
        }
        if (NAME_ERROR.equals(result)){
            return new ResponseResult().code(Integer.parseInt(messageSource.getMessage("file.name.error.code",null,locale)))
                    .fail(messageSource.getMessage("file.name.error.message",null,locale));
        }
        if (null == result || !FILE_OK.equals(result)){
            return new ResponseResult().code(Integer.parseInt(messageSource.getMessage("file.error.code",null,locale)))
                    .fail(messageSource.getMessage("file.error.message",null,locale));
        }

        //上传文件
        Attachment attachment = this.uploadFile(fileInfoBO.getCreateBy(),fileInfoBO.getFile());
        if (null == attachment){
            return new ResponseResult().code(Integer.parseInt(messageSource.getMessage("upload.fail.code",null,locale)))
                    .fail(messageSource.getMessage("upload.fail.message",null,locale));
        }

        //将文件信息存储到数据库中
        try{
            fileRepositories.save(attachment);
        }catch (Exception e){
            throw new RuntimeException("上传文件失败");
        }

        AddFileVO returnResult = new AddFileVO();
        BeanUtils.copyProperties(attachment,returnResult);
        return new ResponseResult().code(Integer.parseInt(messageSource.getMessage("upload.success.code",null,locale))).success(messageSource.getMessage("upload.success.message",null,locale)).data(returnResult);
    }

    /**
     *
     * @Title: 上传多个文件，并打成zip包
     * @Description: 1,
     * @param: [files, userId, type]
     *            description
     * @return: com.nick.file.utils.ResponseResult
     * @auther: hbj
     * @date: 2018/8/29 19:00
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseResult uploadFileZip(MultipartFile[] files, Integer userId, Integer type) {
        //获取当前系统的语言
        Locale locale = LocaleContextHolder.getLocale();

        //对上传的文件进行校验
        if (!(UploadTypeEnum.MOVIE_IMAGE.getCode().equals(type) || UploadTypeEnum.USER_ICON_IMAGE.getCode().equals(type))){
            return new ResponseResult().code(500).fail("请填写正确的上传文件类型编码");
        }

        //校验上传的文件的属性
        FileInfoBO fileInfoBO = new FileInfoBO();
        fileInfoBO.setCreateBy(userId);
        this.setFileInfo(fileInfoBO,type);
        for (MultipartFile file : files){
            fileInfoBO.setFile(file);
            Integer result = this.checkFile(fileInfoBO);
            if (TYPR_ERROR.equals(result)){
                return new ResponseResult().code(500).fail("文件类型不符合要求");
            }
            if (SIZE_ERROR.equals(result)){
                return new ResponseResult().code(500).fail("文件大小不符合要求");
            }
            if (NAME_ERROR.equals(result)){
                return new ResponseResult().code(500).fail("文件名不符合要求");
            }
            if (null == result || !FILE_OK.equals(result)){
                return new ResponseResult().code(500).fail("文件不符合要求");
            }
        }

        //上传文件并打成zip包
        List<Attachment>  fileList = this.uploadFiles(userId,files);

        //将文件信息存储到数据库中
        List<Attachment> upload;
        try {
            upload = fileRepositories.saveAll(fileList);
        }catch (Exception e){
            throw new RuntimeException("上传文件失败");
        }


        return new ResponseResult().code(200).success(messageSource.getMessage("upload.success.message",null,locale)).data(upload);
    }

    private Integer checkFile(FileInfoBO fileInfoBO){
        //文件类型校验
        boolean typeCheck = FileTransferUtils.checkFileType(fileInfoBO.getFile(),fileInfoBO.getWhiteList());
        if(typeCheck == false){
            return TYPR_ERROR;
        }

        //文件大小校验
        boolean sizeCheck = FileTransferUtils.checkFileSize(fileInfoBO.getFile(),fileInfoBO.getFileMaxSize());
        if(sizeCheck == false){
            return SIZE_ERROR;
        }

        //文件名长度校验
        boolean lengthCheck = FileTransferUtils.checkFileNameLength(fileInfoBO.getFile(),fileNameMax);
        if (lengthCheck==false){
            return NAME_ERROR;
        }

        return FILE_OK;
    }

    /**
     *
     * @Title: 上传单个文件
     * @Description: 1, 前端传来用户编号 (用户编号唯一)，文件
     *                2, 生成随机的文件编号
     *                3，存储路径为项目部署的服务器路径下的uploadfile/文件编号/  文件夹下
     *                4，文件名称为：文件编号+文件原名称
     * @param userId（用户编号），file，httpRequest
     *            上传文件
     * @return 上传的文件信息
     */
    @Transactional(rollbackOn = Exception.class)
    public Attachment uploadFile(Integer userId, MultipartFile file){

        Attachment attachment = new Attachment();
        String fileUuid = UUID.randomUUID().toString().replace("-", "");
        String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(DOT) + 1);
        int fileSize = (int)file.getSize();

        String path = fileTruePath;
        String time = DateUtil.getThisDay();
        path = path + SLASH + time + SLASH + fileUuid;
        if (!insertPath(path)) {

        }

        //设置文件名
        String fileName = path  + SLASH + fileUuid + DOT + ext;

        //上传文件
        File uploadFile = new File(fileName);
        try {
            file.transferTo(uploadFile);
        } catch (IOException e) {
            return null;
        }

        //文件记录在数据库中的信息【地址存储的格式是  /uploadfile/20180726/8ebb4a1a0a204eb3ba7d44f4eb71a9a5/8ebb4a1a0a204eb3ba7d44f4eb71a9a5.pdf】
        String fileNameInDatebase = fileVirtualPath +  time + SLASH + fileUuid + SLASH + fileUuid + DOT + ext;
        attachment.setId(fileUuid);
        attachment.setName(fileUuid + DOT + ext);
        attachment.setExt(ext);
        attachment.setPath(fileNameInDatebase);
        attachment.setSize(fileSize);
        attachment.setCreateBy(userId);
        attachment.setUploadFileName(file.getOriginalFilename());
        attachment.setCreateDate(DateUtil.getTimeOfEastEight());
        return attachment;
    }

    /**
     *
     * @Title: 上传多个文件
     * @Description: 1, 前端传来用户编号 (用户编号唯一)，文件
     *                2, 生成随机的文件编号
     *                3，存储路径为项目部署的服务器路径下的uploadfile/文件编号/  文件夹下
     *                4，文件名称为：文件编号+文件原名称
     * @param userId（用户编号），file，httpRequest
     *            上传文件
     * @return 上传的文件信息
     */
    @Transactional(rollbackOn = Exception.class)
    public List<Attachment> uploadFiles(Integer userId, MultipartFile[] files){
        //设置此次上传的文件夹信息
        String fileUuid = UUID.randomUUID().toString().replace("-", "");
        String path = fileTruePath;
        String time = DateUtil.getThisDay();
        path = path + SLASH + time + SLASH + fileUuid;
        if (!insertPath(path)) {
        }

        List<Attachment> filesInfo = new ArrayList<>();

        //设置zip包基本信息
        List<File> zipFiles = new ArrayList<>();
        int zipSize = 0;
        String filePathZip = path + SLASH + fileUuid + DOT + ZIP_PREFIX;

        for (int i = 0;i < files.length;i++){
            String fileUuidSingle = UUID.randomUUID().toString().replace("-", "");
            String ext = files[i].getOriginalFilename().substring(files[i].getOriginalFilename().lastIndexOf(DOT) + 1);
            int fileSize = (int)files[i].getSize();

            //设置文件名
            String fileName = path  + SLASH + fileUuidSingle + DOT + ext;

            //上传文件
            File uploadFile = new File(fileName);
            try {
                files[i].transferTo(uploadFile);
            } catch (IOException e) {
                return null;
            }

            //设置本次上传文件的属性
            String fileNameInDatebase = fileVirtualPath +  time + SLASH + fileUuid + SLASH + fileUuidSingle + DOT + ext;
            Attachment attachment = new Attachment();
            attachment.setId(fileUuidSingle);
            attachment.setName(fileUuidSingle + DOT + ext);
            attachment.setExt(ext);
            attachment.setPath(fileNameInDatebase);
            attachment.setSize(fileSize);
            attachment.setCreateBy(userId);
            attachment.setUploadFileName(files[i].getOriginalFilename());
            attachment.setCreateDate(DateUtil.getTimeOfEastEight());
            attachment.setParentId(fileUuid);
            attachment.setFileCount(i+1);

            //将此次上传的文件放入压缩包中
            zipFiles.add(uploadFile);
            zipSize += fileSize;

            filesInfo.add(attachment);
        }

        //生成zip包
        try {
            FileOutputStream outStream;
            outStream = new FileOutputStream(filePathZip);
            ZipOutputStream toClient = new ZipOutputStream(outStream);
            ZipFileUtil.zipFile(zipFiles, toClient);
            toClient.close();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //设置zip包的基本属性
        Attachment attachmentZip = new Attachment();
        attachmentZip.setId(fileUuid);
        attachmentZip.setName(fileUuid + DOT + ZIP_PREFIX);
        attachmentZip.setExt(ZIP_PREFIX);
        String zipNameInDatebase = fileVirtualPath +  time + SLASH + fileUuid + SLASH + fileUuid + DOT + ZIP_PREFIX;
        attachmentZip.setPath(zipNameInDatebase);
        attachmentZip.setSize(zipSize);
        attachmentZip.setCreateBy(userId);
        attachmentZip.setUploadFileName(fileUuid + DOT + ZIP_PREFIX);
        attachmentZip.setCreateDate(DateUtil.getTimeOfEastEight());
        filesInfo.add(attachmentZip);

        return filesInfo;
    }


    /**
     *
     * @Title: 创建目录
     * @Description: 创建目录
     * @param newPath
     *            创建要上传的文件的目录
     * @return 是否创建成功
     */
    public boolean insertPath(String newPath) {
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

    public void setFileInfo(FileInfoBO fileInfoBO,Integer type){
        if (UploadTypeEnum.USER_ICON_IMAGE.equals(type)){
            fileInfoBO.setFileMaxSize(fileSizeMax);
            fileInfoBO.setWhiteList(imgWhiteList);
        } else if (UploadTypeEnum.MOVIE_IMAGE.equals(type)){
            fileInfoBO.setFileMaxSize(fileSizeMax);
            fileInfoBO.setWhiteList(imgWhiteList);
        } else {
            fileInfoBO.setFileMaxSize(fileSizeMax);
            fileInfoBO.setWhiteList(defaultWhiteList);
        }

    }



}
