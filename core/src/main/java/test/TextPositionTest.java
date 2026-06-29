package test;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;
import java.io.File;
import java.util.List;

public class TextPositionTest {

    public static void main(String[] args) throws Exception {
        String pdfPath = "doc/grid2.pdf";
        File file = new File(pdfPath);
        PDDocument doc = PDDocument.load(file);
        String keyWords = "系统挂牌";
//        PDImageXObject pdImage = PDImageXObject.createFromFile("doc/Clock.png", doc);
        PdfBoxKeyWordPosition pdf = new PdfBoxKeyWordPosition(keyWords, pdfPath);
        PDPageContentStream contentStream = null;
        List<float[]> list = pdf.getCoordinate();
        // 多页pdf的处理
        for (float[] fs : list) {
            PDPage page = doc.getPage((int)fs[2]-1);
            float x = fs[0];
            float y = fs[1];
            System.out.println("drawLine=======, x:"+x+" y:"+y);
            contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true);
//            contentStream.drawImage(pdImage, x, y);

            contentStream.setStrokingColor(Color.RED);
            contentStream.setLineWidth(2);
            contentStream.addRect(x,y - 15, 48,20);
            contentStream.stroke();
//            contentStream.drawLine(x,y, x+200,y+200);
            contentStream.close();
        }
        doc.save("doc/grid_sign.pdf");
        doc.close();
    }

}
