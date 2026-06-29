package test;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageTree;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class SplitPages {

    public void splitPage(){
        try{
            //Loading an existing PDF document
//            File file = new File("doc\\zhaogu\\303-圣湘生物科技股份有限公司.pdf");
//            File file = new File("doc\\zhaogu\\057-云南贝泰妮生物科技集团股份有限公司.pdf");
//            File file = new File("F:\\work\\2021.11.03_企业测评_pdf数据提取\\未校验\\招股说明书结果20211126\\049-中兰环保科技股份有限公司.pdf");
//            File file = new File("F:\\work\\2021.11.03_企业测评_pdf数据提取\\未校验\\招股说明书结果20211221\\",
//                    "444-深圳市亚辉龙生物科技股份有限公司.pdf"
//            );
            File file = new File("F:\\work\\2021.11.03_企业测评_pdf数据提取\\未校验\\招股说明书结果20220104",
                    "228-上海硅产业集团股份有限公司.pdf"
            );
            PDDocument document = PDDocument.load(file);
            PDPageTree pdPageTree = document.getPages();

            int pageSize = pdPageTree.getCount();
            System.out.println(pageSize);
//            System.out.println("pdPageTree.get(1).getCropBox().getWidth()======" + pdPageTree.get(1).getCropBox().getWidth());
            PDDocument documentNew = new PDDocument();
            documentNew.addPage(pdPageTree.get(273));
//            documentNew.addPage(pdPageTree.get(35));
//            documentNew.addPage(pdPageTree.get(2));
//            documentNew.save("doc\\zhaogu\\303-圣湘生物科技股份有限公司_new.pdf");
            documentNew.save("doc\\zhaogu\\228-上海硅产业集团股份有限公司_new.pdf");
            documentNew.close();
            document.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void split(){
        try{
            //Loading an existing PDF document
            File file = new File("doc\\zhaogu\\303-圣湘生物科技股份有限公司.pdf");
            PDDocument document = PDDocument.load(file);
            //Instantiating Splitter class
            Splitter splitter = new Splitter();
            //splitting the pages of a PDF document
            List<PDDocument> Pages = splitter.split(document);
            //Creating an iterator
            Iterator<PDDocument> iterator = Pages.listIterator();
            //Saving each page as an individual document
            int i = 1;
            while(iterator.hasNext()) {
                PDDocument pd = iterator.next();
                pd.save("doc\\zhaogu\\303-圣湘生物科技股份有限公司_"+ i++ +".pdf");
                if(i >= 5){
                    break;
                }
            }
            System.out.println("Multiple PDF’s created");
            document.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        SplitPages splitPages = new SplitPages();
//        splitPages.split();
        splitPages.splitPage();
    }
}