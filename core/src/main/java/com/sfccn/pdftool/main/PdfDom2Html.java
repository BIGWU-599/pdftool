package com.sfccn.pdftool.main;

import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.libary.CustomPDFDomTree;
import com.sfccn.pdftool.utils.ToolUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.fit.pdfdom.PDFDomTree;
import org.fit.pdfdom.TextMetrics;

import java.io.*;
import java.util.*;

/**
 * https://blog.csdn.net/loongshawn/article/details/51542309
 * https://blog.csdn.net/loongshawn/article/details/51550383
 */
public class PdfDom2Html {
    public final Logger log = LogManager.getLogger(PdfDom2Html.class);

    public void read2Html(){
        try{
//            File pdfFile = new File("doc\\grid2.pdf");
//            File pdfFile = new File("doc\\tddd1.pdf");
            File pdfFile = new File("doc\\zhaogu\\303-圣湘生物科技股份有限公司_new.pdf");

            PDDocument document = PDDocument.load(pdfFile);
            CustomPDFDomTree pdfDomTree = new CustomPDFDomTree();
            pdfDomTree.writeText(document);
//            System.out.println("rows====" + pdfDomTree.getRows());

            System.out.println("XXXXXXXXXXXXXXXXXXXXXXXX");
            System.out.println("getWidth=" + document.getPage(0).getCropBox().getWidth());
            List<Map<Integer,List<PdfObject>>> pageList = pdfDomTree.getPageList();
            List<List<List>> pageList2 = new ArrayList<>();
            float xLeft = 100;
            float xRight = 100;
            for(int page = 0; page < pageList.size(); page++){
                System.out.println("page========================" + page);
                List<List> rowList = new ArrayList<>();
                pageList2.add(rowList);
                Map<Integer,List<PdfObject>> rows = pageList.get(page);
                rows.forEach((key,list)->{
                    rowList.add(list);
                    System.out.println("row y=" + key + ",=====");
                    list.forEach(column->{
                        if(column.getType().equals(PdfObject.STYLE_STRING)){
                            System.out.println("      cloumn=" + ToolUtil.toJson(column));
                        }
                        else if(column.getType().equals(PdfObject.STYLE_LINE)){
                            System.out.println("      cloumn=" + ToolUtil.toJson(column));
                        }
                        else{
                            System.out.println("      cloumn=   image base64," + ToolUtil.toJson(column.getData()));
                        }
                    });
                });
            }

            StringBuilder html = new StringBuilder();
            for(int page = 0; page < pageList2.size(); page++){
                List<List> rowList = pageList2.get(page);
                for(int r = 0; r < rowList.size(); r++){
                    List<List> row = rowList.get(r);
                    System.out.println("pageList2=====" + row);
                    StringBuilder rowBuilder = new StringBuilder();
                    for(int c = 0; c < row.size(); c++){
                        List column = row.get(c);
                        if(column.get(0).toString().equals(PdfObject.STYLE_STRING)){
                            TextMetrics metrics = (TextMetrics)column.get(2);
                            rowBuilder.append(column.get(1));
                        }
                        if(c == 0){
//                        rowBuilder
                        }
                    }
                    html.append(rowBuilder);
                }
            }

            System.out.println("html=======\n" + html);
            System.out.println("==========xLeft:" + xLeft + ",xRight:" + xRight);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public void read2Html2(){
        try{
            File pdfFile = new File("doc\\grid2.pdf");
            String outputPath =  "doc\\grid2_dom.html";
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outputPath)),"UTF-8"));

            PDDocument document = PDDocument.load(pdfFile);
            CustomPDFDomTree pdfDomTree = new CustomPDFDomTree();
//            pdfDomTree.writeText(document);
            pdfDomTree.writeText(document,out);
            out.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 开源库pdf2dom，原始数据
     */
    public void pdf2dom(){
        try{
            File pdfFile = new File("doc\\2022-01-18_国海证券_科锐国际.pdf");
            File outputPath =  new File("doc\\2022-01-18_国海证券_科锐国际.pdf.html");
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
    public static void main(String[] args) {
//        java.util.logging.Logger.getLogger("org.mabb.fontverter.opentype.TtfInstructions.TtfInstructionParser").setLevel(Level.WARNING);
//        System.setProperty("org.mabb.fontverter.opentype.TtfInstructions.TtfInstructionParser","org.apache.commons.logging.impl.NoOpLog");
        PdfDom2Html pdfReaderTest = new PdfDom2Html();
//        pdfReaderTest.readText();
//        pdfReaderTest.readImage();
//        pdfReaderTest.read2Html();
//        pdfReaderTest.read2Html2();
        pdfReaderTest.pdf2dom();
    }
}

