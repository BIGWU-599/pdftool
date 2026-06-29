package test;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;

public class PdfMergeExample {

    public static void merger1() throws IOException {
        PDFMergerUtility pdfMerger = new PDFMergerUtility();
        File file = new File("C:\\Users\\Administrator\\Desktop\\新建文件夹\\石岗新村2座502房终稿20241022\\石岗新村2座502房终稿20241022");
        for (File listFile : file.listFiles()) {
            pdfMerger.addSource(listFile);
        }
        pdfMerger.setDestinationFileName("C:\\Users\\Administrator\\Desktop\\新建文件夹\\石岗新村2座502房终稿20241022\\merged.pdf");
        pdfMerger.mergeDocuments(null);
    }


    public static void main(String[] args) throws IOException {
        merger1();
//        File file = new File("C:\\Users\\admin\\石岗新村2座502房终稿\\石岗新村2座502房终稿");
//
//        File outputFile = new File("C:\\Users\\admin\\石岗新村2座502房终稿\\merged.pdf");
//
//        try {
//            File[] files = file.listFiles();
//            // 创建一个新的PDF作为输出
//            PDDocument destination = new PDDocument();
//            for(File f : files) {
//                PDDocument doc1 = PDDocument.load(f);
//
//                // 合并文档
//                for (int i = 0; i < doc1.getNumberOfPages(); i++) {
//                    destination.addPage(doc1.getPage(i));
//                }
//
//                // 关闭输入文档
//                doc1.close();
//
//            }
//
//            // 保存合并后的文档
//            destination.save(outputFile);
//            destination.close();
//
//            System.out.println("PDFs merged successfully.");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}