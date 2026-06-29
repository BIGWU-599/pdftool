package test;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.tools.PDFText2HTML;
import org.fit.pdfdom.PDFDomTree;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;

/**
 * https://blog.csdn.net/loongshawn/article/details/51542309
 * https://blog.csdn.net/loongshawn/article/details/51550383
 */
public class PdfReaderTest {
    public final Logger log = LogManager.getLogger(PdfReaderTest.class);
    public void readText(){
        try{
//            File pdfFile = new File("doc/test.pdf");
//            File pdfFile = new File("doc/tddd1.pdf");
            File pdfFile = new File("doc\\zhaogu\\057-云南贝泰妮生物科技集团股份有限公司_new.pdf");
            PDDocument document = PDDocument.load(pdfFile);
            int pages = document.getNumberOfPages();
            log.info("pages====" + pages);
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            stripper.setStartPage(1);
            stripper.setEndPage(pages);
            String content = stripper.getText(document);
            System.out.println(content);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public void readImage(){
        try{
//            File pdfFile = new File("doc/test.pdf");

//            File pdfFile = new File("doc/甘肃省2021年第二批认定报备高新技术企业名单.pdf");
//            File pdfFile = new File("doc/tddd1.pdf");
            File pdfFile = new File("D:\\project\\idea\\datasfc\\pdftool\\doc\\grid.pdf");
            File pdfFile_out = new File("doc/test_out.pdf");

            PDDocument document = PDDocument.load(pdfFile);
//            PDDocument document_out = PDDocument.load(pdfFile_out);
            PDDocument document_out = new PDDocument();
            PDPage pagea = new PDPage(PDRectangle.A4);
            document_out.addPage(pagea);
            int pages_size = document.getNumberOfPages();
            System.out.println("getAllPages=====" + pages_size);
            int j = 0;
            for(int i = 0; i < pages_size; i++){
                PDPage page = document.getPage(i);
                PDPage page1 = document_out.getPage(0);
                PDResources resources = page.getResources();
                Iterable xobjects = resources.getXObjectNames();

                if(xobjects != null){
                    Iterator imageIter = xobjects.iterator();
                    while(imageIter.hasNext()){
                        COSName key = (COSName)imageIter.next();
                        System.out.println(i + ",key=====" + key);
                        if(resources.isImageXObject(key)){
                            PDImageXObject image = (PDImageXObject)resources.getXObject(key);
                            PDPageContentStream contentStream = new PDPageContentStream(document_out,page1, PDPageContentStream.AppendMode.APPEND,true);
                            System.out.println("img=========" + image.getWidth() + "," + image.getWidth());
                            float scale = 1f;
                            contentStream.drawImage(image, 20,20,image.getWidth()*scale,image.getHeight()*scale);
                            contentStream.close();
                            document_out.save("doc/test_img/test_img_"+j+".pdf");
                            System.out.println(image.getSuffix() + ","+image.getHeight() +"," + image.getWidth());
                            j++;
                        }
                        else{
                            PDXObject object = resources.getXObject(key);
                            System.out.println(i + ",object=======" + object);
                        }
                    }
                }
            }
            System.out.println(j);
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    public void read2Html(){

        try{
//            String outputPath = "test.html";
//            File pdfFile = new File("doc/甘肃省2021年第二批认定报备高新技术企业名单.pdf");
//            String outputPath = "doc/甘肃省2021年第二批认定报备高新技术企业名单.html";
//            File pdfFile = new File("doc/tddd1.pdf");
//            String outputPath = "doc/tddd1.html";
//            File pdfFile = new File("D:\\project\\idea\\datasfc\\pdftool\\doc\\grid2.pdf");
//            String outputPath = "D:\\project\\idea\\datasfc\\pdftool\\doc\\grid2_dom.html";
            File pdfFile = new File("doc\\zhaogu\\307-天津久日新材料股份有限公司.pdf");
            File outputPath =  new File("doc\\zhaogu\\307-天津久日新材料股份有限公司.pdf.html");
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPath),"UTF-8"));
//            File pdfFile = new File("doc/test.pdf");


            PDDocument document = PDDocument.load(pdfFile);
            PDFDomTree pdfDomTree = new PDFDomTree();
            pdfDomTree.writeText(document,out);
            out.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * pdfbox工具自带转html
     */
    public void read2Html2(){
        try{
            File pdfFile = new File("doc/test.pdf");
            String outputPath = "doc/test2.html";
            PDDocument document = PDDocument.load(pdfFile);
            PDFTextStripper stripper = new PDFText2HTML();
            String extractedText = stripper.getText(document);
            FileUtils.write(new File(outputPath), extractedText, "UTF-8");
        }
        catch(Exception e){
           e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        PdfReaderTest pdfReaderTest = new PdfReaderTest();
//        pdfReaderTest.readText();
//        pdfReaderTest.readImage();
        pdfReaderTest.read2Html();
//        pdfReaderTest.read2Html2();
    }
}
