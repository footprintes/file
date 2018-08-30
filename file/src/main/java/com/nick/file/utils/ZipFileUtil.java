package com.nick.file.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @version V1.0
 * @ClassName：ZipFileUtil
 * @author: hbj
 * @CreateDate：2018/8/29 18:10
 */
public class ZipFileUtil {
    public static void zipFile(List<File> files, ZipOutputStream outputStream) throws IOException {

        try {
            int size = files.size();
            // 压缩列表中的文件
            for (int i = 0; i < size; i++) {
                File file = files.get(i);
                zipFile(file, outputStream);
            }
        } catch (IOException e) {
            throw e;
        }
    }

    public static void zipFile(File inputFile, ZipOutputStream outputstream) throws IOException {
        try {
            if (inputFile.exists()) {
                if (inputFile.isFile()) {
                    FileInputStream inStream = new FileInputStream(inputFile);
                    BufferedInputStream bInStream = new BufferedInputStream(inStream);
                    ZipEntry entry = new ZipEntry(inputFile.getName());
                    outputstream.putNextEntry(entry);

                    // 最大的流为10M
                    final int MAX_BYTE = 10 * 1024 * 1024;
                    // 接受流的容量
                    long streamTotal = 0;
                    // 流需要分开的数量
                    int streamNum = 0;
                    // 文件剩下的字符数
                    int leaveByte = 0;
                    // byte数组接受文件的数据
                    byte[] inOutbyte;

                    // 通过available方法取得流的最大字符数
                    streamTotal = bInStream.available();
                    // 取得流文件需要分开的数量
                    streamNum = (int) Math.floor(streamTotal / MAX_BYTE);
                    // 分开文件之后,剩余的数量
                    leaveByte = (int) streamTotal % MAX_BYTE;

                    if (streamNum > 0) {
                        for (int j = 0; j < streamNum; ++j) {
                            inOutbyte = new byte[MAX_BYTE];
                            // 读入流,保存在byte数组
                            bInStream.read(inOutbyte, 0, MAX_BYTE);
                            // 写出流
                            outputstream.write(inOutbyte, 0, MAX_BYTE);
                        }
                    }
                    // 写出剩下的流数据
                    inOutbyte = new byte[leaveByte];
                    bInStream.read(inOutbyte, 0, leaveByte);
                    outputstream.write(inOutbyte);
                    outputstream.closeEntry(); // Closes the current ZIP entry
                    // and positions the stream for
                    // writing the next entry
                    bInStream.close(); // 关闭
                    inStream.close();
                }
            } else {
                System.out.println("文件不存在！！");
            }
        } catch (IOException e) {
            throw e;
        }
    }
}
