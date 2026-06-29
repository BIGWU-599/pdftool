package com.sfccn.pdftool.main;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.libary.CustomPDFDomTree;
import com.sfccn.pdftool.libary.IntTreeMap;
import com.sfccn.pdftool.parse.ParseValue;
import com.sfccn.pdftool.parse.zhaogu.*;
import com.sfccn.pdftool.utils.ThymeleafUtil;
import com.sfccn.pdftool.utils.ToolUtil;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.*;

/**
 * 统计科创板上市公司上市标准依据
 */
public class MarketStandardMain {
    public final Logger log = LogManager.getLogger(ZhaoGuShuMain.class);
    public MarketStandardMain(){

    }

    public List<List<List<Object>>> parsePdf(PDDocument document) throws Exception{
        CustomPDFDomTree pdfDomTree = new CustomPDFDomTree();
        pdfDomTree.writeText(document);
        List<Map<Integer,List<PdfObject>>> pageList = pdfDomTree.getPageList();
        List<List<PdfObject>> pageList2 = new ArrayList<>();
        // Map<page*10000+y,List<Axis>>
        Map<Integer,List<Axis>> pageLineMap = new IntTreeMap<>();
        for(int page = 0; page < pageList.size(); page++){
            Map<Integer,List<PdfObject>> rows = pageList.get(page);// 取出每页行数据
            rows.forEach((key,pdfObjectList)->{
                if(rows.get(key - 2) != null){
                    List<PdfObject> prePdfObjectList = pageList2.remove(pageList2.size() - 1);
                    pdfObjectList.addAll(prePdfObjectList);
                }
                else if(rows.get(key - 1) != null){
                    List<PdfObject> prePdfObjectList = pageList2.remove(pageList2.size() - 1);
                    pdfObjectList.addAll(prePdfObjectList);
                }
                Collections.sort(pdfObjectList, (axis1, axis2) -> axis1.getX() > axis2.getX() ? 1 : (axis1.getX() < axis2.getX() ? -1 : 0));
                pageList2.add(pdfObjectList);

            });
        }
        List<List<List<Object>>> valueList = ParseValue.parse(pageLineMap, pageList2,
                Arrays.asList(Parse001MarketStandard.class));
        return valueList;
    }

    public String run(File file){
        String filepath = null;
        try{
            File[] pdfFiles = file.listFiles();
            int row = 1;
            File marketFile = new File("logs/market.json");
            String content = "[]";
            if(marketFile.exists() == true){
                content = FileUtils.readFileToString(marketFile, "UTF-8");
            }
            ArrayNode arrayNode = (ArrayNode)ToolUtil.strToJsonNode(content);
            if(arrayNode == null){
                arrayNode = ToolUtil.createArrayNode();
            }
            Map<String,Boolean> map = new HashMap<>();
            for(int i = 0; i < arrayNode.size(); i++){
                JsonNode json = arrayNode.get(i);
                String filename = json.get("file").asText();
                map.put(filename, true);
            }
            for(File pdfFile:pdfFiles){
                String filename = pdfFile.getName();
                if(map.get(filename) == null){
                    log.info("filename=======" + filename);
                    PDDocument document = PDDocument.load(pdfFile);
                    List<List<List<Object>>> valueList = parsePdf(document);
                    log.info("row:" + (row++) + "," + pdfFile.getName() + "=====" + ToolUtil.toJson(valueList));
                    document.close();
                    ObjectNode fileNode = ToolUtil.createObjectNode();
                    fileNode.put("file", filename);
                    String value = "";
                    if(valueList.get(0).get(0).get(1) != null && ((List)valueList.get(0).get(0).get(1)).size() > 0){
                        value = ((List)valueList.get(0).get(0).get(1)).get(0).toString();
                    }
                    String indexString = "";
                    if(valueList.get(0).get(0).get(2) != null && ((List)valueList.get(0).get(0).get(2)).size() > 0){
                        indexString = ((List)valueList.get(0).get(0).get(2)).get(0).toString();
                    }
                    fileNode.put("value", value);
                    fileNode.put("index", indexString);
                    System.out.println(fileNode);
                    arrayNode.add(fileNode);
                    FileUtils.writeStringToFile(marketFile, arrayNode.toString(), "UTF-8");
//                    break;
                }
//                break;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return filepath;
    }

    public static void main(String[] args) {
        MarketStandardMain marketStandardMain = new MarketStandardMain();
        File pdfFile = new File("F:\\work\\2021.11.03_企业测评_pdf数据提取\\上市企业招股说明书\\ipo成功企业\\科创板"
        );
        marketStandardMain.run(pdfFile);
    }
}
