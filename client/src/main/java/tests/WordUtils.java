package tests;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import fr.opensagres.poi.xwpf.converter.core.utils.StringUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.xwpf.usermodel.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



/**
 * @description: 工具类
 */

public class WordUtils {

    /**
     * @Date:   2023/2/20 17:42
     * @param path 模版路径
     * @param outPath 输出路径
     * @param dict 需要替换的信息集合
     * @Return: boolean
     * @description: 根据dict编译模版中的文本和表格
     */
    public static void compile(String path, String outPath, Map<String, Object> dict) throws Exception{
        FileInputStream is = new FileInputStream(path);
        XWPFDocument document = new XWPFDocument(is);
        if (dict != null) {
            // 替换掉表格之外的文本(仅限文本)
            WordUtils.compileText(document, dict);
            // 替换表格内的文本对象
            WordUtils.compileTable(document, dict);
        }
        File f = new File(outPath.substring(0, outPath.lastIndexOf(File.separator)));
        if(!f.exists()){
            f.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(outPath);
        document.write(out);


    }

    /***
     * @Description :替换段落文本
     * @param document docx解析对象
     * @param dict 需要替换的信息集合
     * @return void
     * @Date 2022/11/17 17:22
     */
    public static void compileText(XWPFDocument document, Map<String, Object> dict) {
        // 获取段落集合
        Iterator<XWPFParagraph> iterator = document.getParagraphsIterator();
        XWPFParagraph paragraph = null;
        while (iterator.hasNext()) {
            paragraph = iterator.next();
            // 判断此段落是否需要替换
            if (checkText(paragraph.getText())) {
                replaceValue(paragraph, dict);
            }
        }
    }

    /***
     * @Description :替换表格内的文字
     * @param document
     * @param dict 需要替换的信息集合
     * @return void
     * @Date 2022/11/18 11:29
     */
    public static void compileTable(XWPFDocument document, Map<String, Object> dict) {
        // 获取文件的表格
        Iterator<XWPFTable> tableList = document.getTablesIterator();
        XWPFTable table;
        List<XWPFTableRow> rows;
        List<XWPFTableCell> cells;
        // 循环所有需要进行替换的文本，进行替换
        while (tableList.hasNext()) {
            table = tableList.next();
            if (checkText(table.getText())) {
                rows = table.getRows();
                // 遍历表格，并替换模板
                for (XWPFTableRow row : rows) {
                    cells = row.getTableCells();
                    for (XWPFTableCell cell : cells) {
                        // 判断单元格是否需要替换
                        if (checkText(cell.getText())) {
                            List<XWPFParagraph> paragraphs = cell.getParagraphs();
                            for (XWPFParagraph paragraph : paragraphs) {
                                replaceValue(paragraph, dict);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @Date:   2023/2/20 17:31
     * @param paragraph word文本
     * @param dict 需要替换的信息集合
     * @description: 替换字符串
     */
    private static void replaceValue(XWPFParagraph paragraph, Map<String, Object> dict) {
        String nextLine;
        List<XWPFRun> runs = paragraph.getRuns();
        for (int i = 0; i < runs.size(); i++) {
            // 读取当前行
            String readLine = runs.get(i).text();
//            System.out.println("readLine:" + readLine);
            // 如果为空 或 不包含目标字符串 则跳过
            if(StringUtils.isEmpty(readLine) || !readLine.contains("$"))    continue;
            // 初始化结果集
            StringBuffer sb = new StringBuffer();
            // 循环处理当前行的模版串
            while (readLine.contains("$")){
                // 获取模版串左侧字符串
                int left;
                if(readLine.contains("${")){
                    left = readLine.indexOf("${");
                }else {
                    if(runs.size() < i+1){
                        break;
                    }
                    nextLine = runs.get(i+1).text();
                    if(!nextLine.startsWith("{"))   break;
                    readLine += nextLine;
                    paragraph.removeRun(i+1);
                    left = readLine.indexOf("${");
                }
                sb.append(readLine.substring(0, left));
                // 获取模版串右侧
                while(runs.size() >= i+1 && !readLine.contains("}")){
                    nextLine = runs.get(i+1).text();
                    readLine += nextLine;
                    paragraph.removeRun(i+1);
                }
                int right = readLine.indexOf("}");
                if(right == -1) break;
                // 替换模版串 [如果字典中不存在 则替换为空串]
                sb.append(dict.getOrDefault(readLine.substring(left, right+1), ""));
                if(right + 1 < readLine.length()){
                    sb.append(readLine.substring(right + 1));
                }
                readLine = sb.toString();
            }
            runs.get(i).setText(sb.toString(), 0);
        }
    }


    /***
     * @Description :检查文本中是否包含指定的字符(此处为“$”)
     * @param text
     * @return boolean
     * @Date 2022/11/17 17:22
     */
    private static boolean checkText(String text) {
        return text.contains("$");
    }



    /**
     * 通过documents4j 实现word转pdf
     *
     * @param sourcePath 源文件地址 如 /root/example.doc
     * @param targetPath 目标文件地址 如 /root/example.pdf
     */
    public static void documents4jWordToPdf(String sourcePath, String targetPath) {
        File inputWord = new File(sourcePath);
        File outputFile = new File(targetPath);
        System.out.println("转换开");
        try {
            InputStream docxInputStream = new FileInputStream(inputWord);
            OutputStream outputStream = new FileOutputStream(outputFile);
            IConverter converter = LocalConverter.builder().build();
            boolean execute = converter.convert(docxInputStream)
                    .as(DocumentType.DOCX)
                    .to(outputStream)
                    .as(DocumentType.PDF).schedule().get();
            outputStream.close();
            docxInputStream.close();
            System.out.println("转换完毕 targetPath = " + outputFile.getAbsolutePath());
            converter.shutDown();
            return;
        } catch (Exception e) {
        }
    }

    /**
     * 将生成的 Word 文档转换为 PDF 格式
     * @param wordPath Word 文档路径
     * @param pdfPath  生成的 PDF 路径
     */
    public static void convertToPDF(String wordPath , String pdfPath ) {
        documents4jWordToPdf(wordPath, pdfPath);
    }


    /**
     * PDF文件转图片
     * @param sourcePath PDF文件地址
     */
    public void execute(String sourcePath) {
        File file = new File(sourcePath);
        String path = file.getAbsolutePath();
        String targetPathNoExt = path.substring(0, path.lastIndexOf("."));
        try {
            PDDocument doc = Loader.loadPDF(file);
            PDFRenderer renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
            for (int i = 0; i < pageCount; i++) {
//                System.out.println("当前页面" + (i + 1));
                BufferedImage image = renderer.renderImageWithDPI(i, 296);
                //          BufferedImage image = renderer.renderImage(i, 2.5f);
                ImageIO.write(image, "PNG", new File(targetPathNoExt + "_" + i + ".png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
