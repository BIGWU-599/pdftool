package com.sfccn.pdftool.main;

import com.sfccn.pdftool.utils.ToolUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.fit.pdfdom.PDFDomTree;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

public class Pdf2Html {
    public final Logger log = LogManager.getLogger(Pdf2Html.class);
    public void create(String pdffile, String html){
        log.info("====create");
        try{
            PDDocument pdf = PDDocument.load(new File(pdffile));
            Writer output = new PrintWriter(html, "utf-8");
            new PDFDomTree().writeText(pdf, output);
            output.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void generateHTMLFromPDF(String filename) throws IOException, ParserConfigurationException {
        PDDocument pdf = PDDocument.load(new File(filename));
        Writer output = new PrintWriter("pdf.html", "utf-8");
        new PDFDomTree().writeText(pdf, output);
        output.close();
    }
    public static void main(String[] args) {
        ToolUtil.setLogFile("logs/app_" + Pdf2Html.class.getSimpleName());
        Pdf2Html pdf2Html = new Pdf2Html();
//        String pdffile = "doc/test.pdf";
//        String htmlfile = "doc/test.html";
//        String pdffile = "doc/template/南凌科技股份有限公司.pdf";
//        String htmlfile = "doc/template/南凌科技股份有限公司.html";
//        String pdffile = "doc/甘肃省2021年第二批认定报备高新技术企业名单.pdf";
//        String htmlfile = "doc/甘肃省2021年第二批认定报备高新技术企业名单.html";

        String pdffile = "doc/tddd1.pdf";
        String htmlfile = "doc/tddd1.html";
        pdf2Html.create(pdffile, htmlfile);
    }
}
