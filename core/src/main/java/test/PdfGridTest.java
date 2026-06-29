package test;


import java.awt.Rectangle;
import java.io.File;
import java.util.List;

import com.sfccn.pdftool.utils.ToolUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.text.PDFTextStripperByArea;


public class PdfGridTest {
    public static void main(String[] args) {
//        fillTemplete();
        readGrid();
    }
    //pdf模板处理
    private static void fillTemplete(){
//        String templetePath = "D:\\project\\idea\\datasfc\\pdftool\\doc\\grid.pdf";
        String templetePath = "C:\\Users\\Administrator\\Documents\\WeChat Files\\penngo\\FileStorage\\File\\2021-11\\中国诚通控股集团有限公司2021年度主体跟踪评级.pdf";

        String data = "";
        try {
            PDDocument document = PDDocument.load(new File(templetePath));
            PDFTextStripperByArea stripper = new PDFTextStripperByArea();
            stripper.setSortByPosition(true);
            stripper.setWordSeparator("|");
//	      stripper.setLineSeparator("#");
            //划定区域
            Rectangle rect= new Rectangle(0, 0, 10000, 10000);
            stripper.addRegion("area", rect);
            PDPageTree allPages = document.getPages();//.getDocumentCatalog().getPages();
            int i = 0;
            for(PDPage page : allPages){
                stripper.extractRegions(page);
                i++;
                System.out.println("i===========" + i);
                //获取区域的text
                data = stripper.getTextForRegion("area");
//	         data = data.trim();
                String[] datas = data.split("\r\n");
                //对文本进行分行处理
                for( i = 0; i<datas.length; ++i){
                    String[] str = datas[i].split(" ");
                    System.out.println(i + "," + ToolUtil.toJson(str));
                }
            }
            document.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void readGrid()
    {
//        String templetePath = "D:\\project\\idea\\datasfc\\pdftool\\doc\\grid.pdf";
        String templetePath = "D:\\Users\\Administrator\\Documents\\WeChat Files\\penngo\\FileStorage\\File\\2021-11\\中国诚通控股集团有限公司2021年度主体跟踪评级.pdf";

        PDDocument document = null;
        try
        {
            document = PDDocument.load( new File(templetePath) );
            PDFTextStripperByArea stripper = new PDFTextStripperByArea();
            stripper.setSortByPosition( true );
            Rectangle rect = new Rectangle( 0, 0, 10000, 10000 );
            stripper.addRegion( "class1", rect );
            PDPage firstPage = document.getPage(0);
            stripper.extractRegions( firstPage );
            System.out.println( "Text in the area:" + rect );
            System.out.println( stripper.getTextForRegion( "class1" ) );
        }
        catch(Exception e){
           e.printStackTrace();
        }
    }

}
