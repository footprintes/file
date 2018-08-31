package com.nick.file.utils;

import com.nick.file.po.Attachment;
import com.nick.file.service.AttachmentService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 项目名称：
 * 类描述：
 * 类名称：
 * 创建人：huangbangjing
 * 创建时间：2018/7/26
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version V1.0
 */
public class PdfToImgThread implements Runnable {

    private String rootPath;

    private static final String DOT = ".";

    Attachment attachment;

    AttachmentService attachmentService;

    @Override
    public void run() {
        pdfToImgPdfBox(attachment);
    }

    public PdfToImgThread(Attachment attachment, AttachmentService attachmentService, String rootPath){
        this.attachment = attachment;
        this.attachmentService = attachmentService;
        this.rootPath = rootPath;
    }

    /**
     *
     * @Title: pdf转图片(png)
     * @Description: 1,
     * @param:
     *            description
     * @return:
     * @auther: hbj
     * @date: 2018/7/26 15:27
     */
    public void pdfToImgPdfBox(Attachment pdf){
        List<Attachment> imgList = new ArrayList<>();
        //pdf的路径
        String pdfPath = rootPath + pdf.getPath();
        String pdfPathInData = pdf.getPath();
        //生成的图片路径属于跟pdf同一级目录下的文件夹
        String basePath = pdfPath.replace(".pdf","/");
        pdfPathInData = pdfPathInData.replace(".pdf","/");
        System.out.println(basePath);
        if (!FileTransferUtils.insertPath(basePath)) {

        }


        Date start = new Date();
        PDDocument doc;
        try{
            doc = PDDocument.load(pdfPath);
            int pdfCount = doc.getNumberOfPages();

            List pages = doc.getDocumentCatalog().getAllPages();
            for (int i = 0;i < pages.size();i++){
                String fileUuid = Random.getUUid();
                PDPage page = (PDPage) pages.get(i);
                int width = new Float(page.getTrimBox().getWidth()).intValue();
                int height = new Float(page.getTrimBox().getHeight()).intValue();

                BufferedImage image = page.convertToImage();
                ImageIO.write(image,"png",new File(basePath + File.separator + "pdf" +"-" + (i + 1) + ".png"));
                System.out.println("image in the page ---- " + (i + 1));

                Attachment commonAttachFile = new Attachment();
                commonAttachFile.setId(fileUuid);
                commonAttachFile.setName(fileUuid + DOT + "png");
                commonAttachFile.setExt("png");
                commonAttachFile.setPath(pdfPathInData  + "pdf" + "-" + (i + 1) + ".png");
                commonAttachFile.setCreateBy(pdf.getCreateBy());
                commonAttachFile.setUploadFileName(fileUuid + DOT + "png");
                commonAttachFile.setFileCount(i + 1);
                commonAttachFile.setParentId(pdf.getId());

                imgList.add(commonAttachFile);
            }
            doc.close();
            attachmentService.insertFileList(imgList);
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }

        Date end = new Date();
        System.out.println("花费的时间为 : " + (end.getTime() - start.getTime()));
    }

}
