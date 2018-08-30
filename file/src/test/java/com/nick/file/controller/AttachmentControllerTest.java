package com.nick.file.controller;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * @version V1.0
 * @ClassName：AttachmentControllerTest
 * @author: hbj
 * @CreateDate：2018/8/28 14:28
 */

public class AttachmentControllerTest {

    @Test
    public void uploadFileSingle() throws IOException {
        //创建写入流对象
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("D:\\pngtotxt/test.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //输入流对象
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream("D:\\pngtotxt/test.jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int b = -1;
        while((b = inputStream.read()) !=-1){
            byte[] bytes =( b+"").getBytes();
            outputStream.write(bytes);
            outputStream.write(new byte[]{'\r','\n'});
        }
        outputStream.close();
        inputStream.close();
    }
}