package test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.imageio.ImageIO;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSFloat;
import org.apache.pdfbox.cos.COSInteger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionGoTo;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageFitWidthDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineNode;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFBoxNewDemo {
    static final float INCH = 72;
    private static String path = "D:\\pdf\\work.pdf";
    private static String dest = "D:/logs";
    private static String path_test = "D:\\pdf\\test.pdf";
    private static String path_test2 = "D:\\pdf\\test2.pdf";



    public static void main(String[] args) throws Exception {
        PDFBoxNewDemo.writeToPdf("hello world", path_test);
        // PDFBoxNewDemo.getText(path);
        // PDFBoxNewDemo.readPdfToImage(path, dest);
        //PDFBoxNewDemo.addOutlineToPDF(path, path2);
        PDFBoxNewDemo.printOnline(path_test);
    }

    private static void writeToPdf(String contentStr, String pdfPath) throws IOException {
        try {
            File file = new File(pdfPath);
            if (!file.exists()) {
                file.createNewFile();
            }
            PDDocument document = new PDDocument();
            PDPage pag1 = new PDPage();
            document.addPage(pag1);

            PDDocumentOutline outline = new PDDocumentOutline();
            document.getDocumentCatalog().setDocumentOutline(outline);

            PDPageContentStream content = new PDPageContentStream(document, pag1);
            PDType0Font font = PDType0Font.load(document, new File("C:\\Windows\\Fonts\\SIMSUNB.TTF"));
            // PDFont font = PDType1Font.HELVETICA_BOLD;
            content.setFont(font, 12);
            content.beginText();
            content.newLineAtOffset(80, 750);
            for(int i=0;i<30;i++){
                content.showText(contentStr+"        iiiiii = "+i);
                content.newLineAtOffset(0, -(INCH / 5));
            }

            for(int i=0;i<5;i++){
                PDPageFitWidthDestination dest = new PDPageFitWidthDestination();
                dest.setPageNumber(-1);
                //dest.setTop(5*i);
                dest.setTop(0);
                dest.setFitBoundingBox(true);

                COSArray cosArray = dest.getCOSObject();
                COSArray base = new COSArray();

                base.growToSize(5);
                base.setName(1, "XYZ" );
                base.set(2,COSInteger.get("0"));
                base.set(3,COSFloat.get("771.967059"));
                dest.create(base);

                base.getCOSObject();

                PDOutlineItem bookmark = new PDOutlineItem();
                bookmark.setDestination(dest);
                bookmark.setTitle("title  "+i);
                //outline.appendChild(bookmark);
                outline.addLast(bookmark);
            }

            content.setNonStrokingColor(Color.blue);
            content.endText();
            content.close();
            document.save(pdfPath);
            System.out.println("-----写入PDF数据完成-----------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印pdf文档文本内容
     *
     * @param strFile
     *            pdf文件
     * @throws Exception
     */
    private static void getText(String strFile) throws Exception {
        boolean sort = false;
        int startPage = 1;
        int endPage = Integer.MAX_VALUE;

        InputStream inputStream = null;
        Writer outWriter = null;
        PDFTextStripper textStripper = null;
        PDDocument document = null;
        try {
            inputStream = new FileInputStream(strFile);
            outWriter = new OutputStreamWriter(System.out);

            textStripper = new PDFTextStripper();
            document = PDDocument.load(inputStream);

            textStripper.setSortByPosition(sort);
            textStripper.setStartPage(startPage);
            textStripper.setEndPage(endPage);

            textStripper.writeText(document, outWriter);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            inputStream.close();
            outWriter.flush();
            outWriter.close();
        }
    }

    /**
     * <p>
     * 读取PDF转IMAGE
     * </p>
     *
     * @date 2017年9月19日下午2:19:43
     * @param path
     * @param dest
     * @throws IOException
     */
    public static void readPdfToImage(String path, String dest) throws IOException {
        PDDocument doc = null;
        try {
            doc = PDDocument.load(new File(path));
            PDFRenderer render = new PDFRenderer(doc);
            int count = doc.getNumberOfPages();
            for (int i = 0; i < count; i++) {
                // 设置图片的分辨率
                BufferedImage image = render.renderImageWithDPI(i, 296);
                // 如果是PNG图片想要背景透明的话使用下面这个
                // BufferedImage image = render.renderImageWithDPI(i, 296,
                // ImageType.ARGB);
                ImageIO.write(image, "PNG", new File(dest + File.separator + i + ".png"));

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (doc != null) {
                doc.close();
            }
        }
    }


    private static String addOutlineToPDF(String filePath, String outputFilePath) throws Exception {
        PDDocument document = null;
        PDDocument document2 = null;
        String outFilePath = "";
        try {
            document = PDDocument.load(new File(filePath));
            if (document.isEncrypted()) {
                System.err.println("Error: Cannot add bookmarks to encrypted document.");
                System.exit(1);
            }
            PDFTextStripper stripper = new PDFTextStripper();
            PDDocumentOutline outline = new PDDocumentOutline();
            document.getDocumentCatalog().setDocumentOutline(outline);

            PDPageTree pages = document.getDocumentCatalog().getPages();
            for (int i = 0; i < pages.getCount(); i++) {
                PDPage page = (PDPage) pages.get(i);
                stripper.setStartPage(i + 1);
                stripper.setEndPage(i + 1);
                String content = stripper.getText(document);

                addBookmark(outline,content,i,"title1",1);
                addBookmark(outline,content,i,"title2",1);
                addBookmark(outline,content,i,"title3",1);
            }

            document.save(outputFilePath);
            System.out.println("-------------- 写放目录完成------------------------");
        } catch (Exception e) {
            throw e;
        } finally {
            if (document != null) {
                document.close();
            }
        }
        return outFilePath;
    }

    private static void addBookmark(PDDocumentOutline outline, String content, int i, String title, int level) {
        String indexStr = "spinach_"+level+"_"+title;
        if ((content.indexOf(indexStr) != -1 || content.indexOf("spinach_1 DEPARTURE") != -1)) {
            PDPageFitWidthDestination dest = new PDPageFitWidthDestination();

            String[] array = content.split("\\r\\n");
            int number = -1;
            for(String str : array){
                if(str.equals(".") || str.startsWith("★") || str.startsWith("B)")){
                }else if(str.equals(indexStr)||str.startsWith(indexStr)){
                    title = str;
                    break;
                }else{
                    number++;
                }
            }

            dest.setPageNumber(i);
            dest.setTop(number);
            PDOutlineItem bookmark = new PDOutlineItem();
            bookmark.setDestination(dest);
            bookmark.setTitle(title);
            //bookmark.getStructureElement();
            outline.addLast(bookmark);
        }
    }

    private static int getNumberByTitle(String content, String title) {
        String[] array = content.split("\\r\\n");
        int number = -1;
        for(String str : array){
            if(str.equals(".") || str.startsWith("★") || str.startsWith("B)")){

            }else if(str.equals(title)||str.startsWith(title)){
                break;
            }else{
                number++;
            }
        }
        return number;
    }

    public static void printOnline(String path_test) throws InvalidPasswordException, IOException {
        PDDocument document = PDDocument.load(new File(path_test));
        PDDocument document2 = PDDocument.load(new File(path_test2));
        PDDocumentOutline bookmarks1 = document.getDocumentCatalog().getDocumentOutline();
        PDDocumentOutline bookmarks2 = document2.getDocumentCatalog().getDocumentOutline();
		/*if (bookmarks1 != null) {
			printBookmark(document, bookmarks1, "");
		} else {
			System.out.println("This document does not contain any bookmarks");
		}*/
        if (bookmarks2 != null) {
            printBookmark(document, bookmarks2, "");
        } else {
            System.out.println("This document does not contain any bookmarks");
        }
    }

    /**
     * <p>
     * 打印BOOKMARKS
     * </p>
     *
     * @author wanghuihui
     * @date 2017年10月23日下午1:38:26
     * @param document
     * @param bookmark
     * @param indentation
     * @throws IOException
     */
    public static void printBookmark(PDDocument document, PDOutlineNode bookmark, String indentation) throws IOException {
        PDOutlineItem current = bookmark.getFirstChild();
        while (current != null) {
            System.out.println(indentation + current.getTitle());
            if (current.getDestination() instanceof PDPageDestination) {
                PDPageDestination pd = (PDPageDestination) current.getDestination();
                System.out.println(" number = "+pd.getPageNumber());
                System.out.println(" cosobject = "+pd.getCOSObject());
                System.out.println(indentation + "Destination page: " + (pd.retrievePageNumber() + 1));
                COSArray array = pd.getCOSObject();
                array.size();
                array.get(1);
                for(int i=0;i<array.size();i++){
                    COSBase obj = array.get(i);
                    System.out.println(obj);
                }
            }
            if (current.getAction() instanceof PDActionGoTo) {
                PDActionGoTo gta = (PDActionGoTo) current.getAction();
                if (gta.getDestination() instanceof PDPageDestination) {
                    PDPageDestination pd = (PDPageDestination) gta.getDestination();
                    //System.out.println(indentation + "Destination page: " + (pd.retrievePageNumber() + 1));
                }
            }
            System.out.println("------------------------"+current.getOpenCount()+"   ---------------------- ");


            //Iterable<PDOutlineItem>  children = current.children();
            //System.out.println(JSONObject.fromObject(children).toString());
            printBookmark(document, current, indentation + "    ");
            current = current.getNextSibling();
        }
    }
}

