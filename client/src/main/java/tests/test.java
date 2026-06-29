package tests;

import com.aspose.words.*;
import javassist.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author wvy
 * @date 2025/2/7
 * apiNote
 */
public class test {
    public static void main(String[] args) throws NotFoundException, CannotCompileException, IOException {
        // ClassPool.getDefault().insertClassPath("E:/Download/aspose-words-20.12-jdk17-crack.jar");
        // CtClass zzZJJClass = ClassPool.getDefault().getCtClass("com.aspose.words.zzZDZ");//找到指定类
        // //找到指定方法
        // CtMethod zzZ4u = zzZJJClass.getDeclaredMethod("zzZ4n");
        // CtMethod zzZ4t = zzZJJClass.getDeclaredMethod("zzZ4m");
        // //修改返回值
        // zzZ4u.setBody("{return 1;}");
        // zzZ4t.setBody("{return 1;}");
        // //输出到指定路径
        // zzZJJClass.writeFile("E:/Download/aspose-words-20.12.0-java/");

        // docxToPdf("E:/360Downloads/海洋数据应用系统问题1.docx","E:/360Downloads/outpdf.pdf");
        WordUtils wordUtils=new WordUtils();
        wordUtils.convertToPDF("E:/360Downloads/海洋数据应用系统问题1.docx","E:/360Downloads/test.pdf");
    }

    public static boolean docxToPdf(String inPath, String outPath) {
        // if (!getLicense()) { // 验证License 若不验证则转化出的pdf文档会有水印产生,破解版不需要验证
        //     return false;
        // }
        FileOutputStream os = null;
        try {
            long old = System.currentTimeMillis();
            File file = new File(outPath); // 新建一个空白pdf文档
            os = new FileOutputStream(file);
            Document document= new Document(inPath); // Address是将要被转化的word文档
            TableCollection tables = document.getFirstSection().getBody().getTables();
            for (Table table : tables) {
                RowCollection rows = table.getRows();
                table.setAllowAutoFit(false);
                for (Row row : rows) {
                    CellCollection cells = row.getCells();
                    for (Cell cell : cells) {
                        CellFormat cellFormat = cell.getCellFormat();
                        cellFormat.setFitText(false);
                        cellFormat.setWrapText(true);
                    }
                }
            }
            document.save(os, SaveFormat.PDF);// 全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF,
// EPUB, XPS, SWF 相互转换
            long now = System.currentTimeMillis();
            System.out.println("pdf转换成功，共耗时：" + ((now - old) / 1000.0) + "秒"); // 转化用时
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally {
            if (os != null) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}
