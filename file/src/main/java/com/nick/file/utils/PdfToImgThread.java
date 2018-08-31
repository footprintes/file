package cnzsqh.supplychain.common.util;

import cnzsqh.supplychain.common.po.CommonAttachFile;
import cnzsqh.supplychain.common.service.IUploadFileService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    CommonAttachFile commonAttachFile;

    IUploadFileService uploadFileService;

    @Override
    public void run() {
        pdfToImgPdfBox(commonAttachFile);
    }

    public PdfToImgThread(CommonAttachFile commonAttachFile, IUploadFileService uploadFileService,String rootPath){
        this.commonAttachFile = commonAttachFile;
        this.uploadFileService = uploadFileService;
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
    public void pdfToImgPdfBox(CommonAttachFile pdf){
        List<CommonAttachFile> imgList = new ArrayList<>();
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

                CommonAttachFile commonAttachFile = new CommonAttachFile();
                commonAttachFile.setId(fileUuid);
                commonAttachFile.setName(fileUuid + DOT + "png");
                commonAttachFile.setExt("png");
//                commonAttachFile.setPath(pdfPathInData + File.separator + "pdf" + "-" + (i + 1) + ".png");
                commonAttachFile.setPath(pdfPathInData  + "pdf" + "-" + (i + 1) + ".png");
                commonAttachFile.setCreateBy(pdf.getCreateBy());
                commonAttachFile.setContractName(pdf.getContractName());
                commonAttachFile.setUploadFileName(fileUuid + DOT + "png");
                commonAttachFile.setPdfCount(i + 1);
                commonAttachFile.setPdfId(pdf.getId());

                imgList.add(commonAttachFile);
            }
            doc.close();
            uploadFileService.insertFileList(imgList);
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }

        Date end = new Date();
        System.out.println("花费的时间为 : " + (end.getTime() - start.getTime()));
    }

}
