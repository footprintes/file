package com.nick.file.constant;

/**
 * @version V1.0
 * @ClassName：UploadTypeEnum
 * @author: hbj
 * @CreateDate：2018/8/27 19:34
 */
public enum UploadTypeEnum {
    USER_ICON_IMAGE(1,"用户头像上传"),
    MOVIE_IMAGE(2,"电影图片上传");

    private Integer code;

    private String msg;
    UploadTypeEnum(final Integer code,final String msg){
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }
}
