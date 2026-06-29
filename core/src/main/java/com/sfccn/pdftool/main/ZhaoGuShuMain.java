package com.sfccn.pdftool.main;

import com.google.common.collect.Lists;
import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.libary.CustomPDFDomTree;
import com.sfccn.pdftool.libary.IntTreeMap;
import com.sfccn.pdftool.parse.ParseValue;
import com.sfccn.pdftool.parse.zhaogu.*;
import com.sfccn.pdftool.utils.ExcelTool;
import com.sfccn.pdftool.utils.ThymeleafUtil;
import com.sfccn.pdftool.utils.ToolUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.List;

/**
 * 招股说明书内容提取
 */
public class ZhaoGuShuMain {
    public final Logger log = LogManager.getLogger(ZhaoGuShuMain.class);
    private List<List<PdfObject>> pageRowList = null;
    private Map<Integer,List<Axis>> pageLineMap = null;
    private List<List<List<Object>>> valueList;
    private File pdfFile = null;
    public ZhaoGuShuMain(){
        //initSeg();
    }

    public List<List<List<Object>>> parsePdf(PDDocument document) throws Exception{
        CustomPDFDomTree pdfDomTree = new CustomPDFDomTree();
        pdfDomTree.writeText(document);
        List<Map<Integer,List<PdfObject>>> pageList = pdfDomTree.getPageList();
        pageRowList = new ArrayList<>();
        float xLeft = 100;
        float xRight = 100;

        // Map<page*10000+y,List<Axis>>
        pageLineMap = new IntTreeMap<>();
        for(int page = 0; page < pageList.size(); page++){
            Map<Integer,List<PdfObject>> rows = pageList.get(page);
            rows.forEach((key,pdfObjectList)->{
                if(rows.get(key - 1) != null){
                    List<PdfObject> prePdfObjectList = pageRowList.remove(pageRowList.size() - 1);
                    pdfObjectList.addAll(prePdfObjectList);
                }
                Collections.sort(pdfObjectList, (axis1, axis2) -> axis1.getX() > axis2.getX() ? 1 : (axis1.getX() == axis2.getX() ? 0 : -1));
                pageRowList.add(pdfObjectList);
                addLine(pdfObjectList,pageLineMap);
            });
        }

        valueList = ParseValue.parse(pageLineMap, pageRowList,
                Arrays.asList(Parse01CreditCode.class, Parse02SignDate.class, Parse03CnName.class,
                        Parse04Address.class, Parse05Industry.class, Parse06Segmentation.class,
                        Parse07Employee.class, Parse08EmployeeTech.class, Parse20NetCashFlow.class,
                        Parse21Income.class, Parse22Profit.class, Parse23Research.class,
                        Parse24Consumer.class, Parse25NewPattern.class, Parse26NewBusiness.class,
                        Parse27Financing.class, Parse28Consumer30.class, Parse40SoftRight.class,
                        Parse41Brand.class, Parse42CircuitDiagram.class, Parse43Patent.class,
                        Parse44PatentInvention.class, Parse45PatentOwn.class, Parse46PatentOwnBusiness.class,
                        Parse47PatentBuy.class, Parse61CompetitorInternal.class, Parse62CompetitorInternalInfo.class,
                        Parse63CompetitorForeign.class, Parse64CompetitorForeignInfo.class, Parse65NonCompetition.class,
                        Parse70FTO.class, Parse71PatentInfringement.class, Parse72WarningAnalysis.class,
                        Parse73AvoidInfringement.class, Parse74Lawsuit.class, Parse75PatentValueMax.class,
                        Parse80MajorProject.class, Parse81NationalStandard.class, Parse82TechPrize.class,
                        Parse83IndustryFirst.class, Parse84BigSupplier.class, Parse85ReplaceTech.class,
                        Parse90TechProject.class, Parse91TechAuthentication.class, Parse92TopLab.class,
                        Parse93IndustryProduct.class, Parse94TechLeader.class, Parse95TalentLeader.class
                ));
        return valueList;
    }

    private void addLine(List<PdfObject> pdfObjectList,Map<Integer,List<Axis>> pageLineMap){
        for(int r = 0; r < pdfObjectList.size(); r++){
            PdfObject pdfObject = pdfObjectList.get(r);
            if(pdfObject.getType().equals(PdfObject.STYLE_LINE)){
                Integer[] xy = (Integer[])pdfObject.getTypeInfo();
                int x1 = xy[0];
                int y1 = xy[1];
                int x2 = xy[2];
                int y2 = xy[3];
                int pagenum = pdfObject.getPagenum();
                int key = pagenum * 10000 + y1;
                Axis axis;
                if(x1 == x2){ //  竖线
                    axis = new Axis(pdfObject.getPagenum(), x1,y1,0,0,x2,y2);
                }
                else{
                    axis = new Axis(pdfObject.getPagenum(), x1, y1, x2, y2,0,0);
                }
                List<Axis> lines = pageLineMap.get(key);
                if(lines == null){
                    lines = new ArrayList();
                    pageLineMap.put(key, lines);
                }
                lines.add(axis);
            }
        }
    }

    private Document filter(Document docZhaogu){
        Elements divs = docZhaogu.select("div");
        for(org.jsoup.nodes.Element div:divs){
            String data_type = div.attr("data-type") != null ? div.attr("data-type") : "";
            String styleString = div.attr("style");
            String[] styles = styleString.split(";");
            StringBuffer styleBuffer = new StringBuffer();
            for(String style:styles){
                String[] strs = style.split("\\:");
//                        || strs[0].equals("-webkit-text-stroke") == true
                if(strs[0].equals("letter-spacing") == true || strs[0].equals("text-shadow") == true){
                    continue;
                }
                if(strs[0].equals("font-family") == true && strs[1].startsWith("A")){
                    continue;
                }
                if(strs[0].equals("width") == true && data_type.equals("text") == true){
                    String width = ToolUtil.findText(strs[1], "([0-9\\.]+)");
                    if(StringUtils.isEmpty(width) == false){
                        double dwidth = new BigDecimal(width).multiply(new BigDecimal(1.3)).setScale(2, RoundingMode.HALF_UP).doubleValue();
                        width = dwidth + "pt;";
//                        width = Double.valueOf(width) * 1.3 + "pt;";
                    }
                    styleBuffer.append(strs[0]).append(":").append(width);
                }
                else{
                    styleBuffer.append(style).append(";");
                }
            }
            div.attr("style", styleBuffer.toString());
            String text = div.text();
            if(text.trim().equals("")){
                div.text("");
            }
//            <div class="p" data-type="text" id="p80" style="top:330.09894pt;left:90.024pt;line-height:10.559998pt;font-family:ABCDEE ËÎÌå;font-size:10.56pt;-webkit-text-stroke: #000000 1px ;width:164.433932pt;">
        }
        return docZhaogu;
    }

    public String run(File pdfFile){
        if(pageRowList != null && pageLineMap != null){
            pageRowList.clear();
            pageLineMap.clear();
        }
        String filepath = null;
        this.pdfFile = pdfFile;
        try{
            File outputPath =  new File(pdfFile.getAbsolutePath() + ".html");
            PDDocument document = PDDocument.load(pdfFile);

            List<List<List<Object>>> valueList = parsePdf(document);
//            for(List<List<Object>> value:valueList){
//                System.out.println("value====" + ToolUtil.toJson(value));
//            }
            // ToolUtil.isString()
            Dimension screenshot = Toolkit.getDefaultToolkit().getScreenSize();
            int width = screenshot.width;
            width = width/2 > 500 ? (width/2) : 500;
            Map root = new HashMap();
            root.put("valueList", valueList);
            root.put("title", pdfFile.getName());
            root.put("toolUtil", new ToolUtil());
            root.put("width", width);
            String html = ThymeleafUtil.getInstance().process(root,"zhaogu2");
            // 测试
            //FileUtils.write(outputPath, html, "UTF-8");

            Document docZhaogu = Jsoup.parse(html);
            CustomPDFDomTree pdfDomTree2 = new CustomPDFDomTree();
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPath),"UTF-8"));
            pdfDomTree2.writeText(document, out, ParseValue.getMarkMap());
            out.close();
            String content = FileUtils.readFileToString(outputPath, "UTF-8");
            content = content.replaceAll("@@(.+?)##(.+?)@@", "<em id=\"$1\">$2</em>");
            Document doc = Jsoup.parse(content);
            Element element = docZhaogu.selectFirst(".right");
            if(element != null){
                element.html(doc.body().html());
            }

            docZhaogu = filter(docZhaogu);
            String htmlPdf = docZhaogu.outerHtml();
            htmlPdf = htmlPdf.replaceAll("\\s{2,}", " ");
            FileUtils.write(outputPath, htmlPdf, "UTF-8");
            document.close();
            log.info("文件生成成功：" + outputPath.getAbsolutePath());
            filepath = outputPath.getAbsolutePath();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return filepath;
    }

    public List<List<PdfObject>> getPageRowList() {
        return pageRowList;
    }

    public Map<Integer, List<Axis>> getPageLineMap() {
        return pageLineMap;
    }

    /**
     * 提取所有图片
     */
    public String viewImageHtml(){
        String imgHtml = pdfFile.getAbsolutePath() + ".img.html";
        try{
            List<PdfObject> imgList = new ArrayList<>();
            for(List<PdfObject> row:pageRowList){
                for(PdfObject obj:row){
                    if(obj.getType().equals(PdfObject.STYLE_IMAGE)){
                        imgList.add(obj);
                    }
                }
            }
            Map root = new HashMap();
            root.put("imgList", imgList);
            String html = ThymeleafUtil.getInstance().process(root,"img");
            FileUtils.write(new File(imgHtml), html, "UTF-8");
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return imgHtml;
    }
    public void saveImageHtml(File imgPath){
//        String imgHtml = pdfFile.getAbsolutePath() + ".img.html";
        try{
            List<PdfObject> imgList = new ArrayList<>();
            String path = imgPath.getAbsolutePath();
            for(int r= 0; r < pageRowList.size(); r++){
                List<PdfObject> row = pageRowList.get(r);
                for(int c = 0; c < row.size(); c++){
                    PdfObject obj = row.get(c);
                    if(obj.getType().equals(PdfObject.STYLE_IMAGE)){
                        String data = obj.getData();
                        String imgFile = path + String.format("/%03d_%02d", r, c);
                        imgFile = data.indexOf("image/png") > -1 ? imgFile + ".png" : imgFile + ".jpg";
                        data = data.substring(data.indexOf(","));
                        System.out.println("============" + obj.getData());
                        ToolUtil.base642Img(data, imgFile);
                    }
                }
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
//        return imgHtml;
    }

    public String viewExcelHtml(){
        String excelHtml = pdfFile.getAbsolutePath() + ".excel.html";
        try{

            Map root = new HashMap();
            root.put("valueList", valueList);
            root.put("title", pdfFile.getName());
            root.put("toolUtil", new ToolUtil());
            String html = ThymeleafUtil.getInstance().process(root,"excel");
            FileUtils.write(new File(excelHtml), html, "UTF-8");
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return excelHtml;
    }

    public String saveExcelHtml(File selectFile){
        String filename = selectFile.getAbsolutePath();
        if(!filename.toLowerCase().endsWith(".xlsx")){
            filename = filename + ".xlsx";
        }
        String excelHtml = filename;//pdfFile.getAbsolutePath() + ".excel.xlsx";
        log.info("excelHtml=====" + excelHtml);
        try{

            List<String> titleList = Arrays.asList("数据字段","提取结果","结果所在文字段落/图表");
            List<List<String>> rowList = Lists.newArrayList();
            for(List<List<Object>> values:valueList){
                StringBuilder column1 = new StringBuilder(values.get(0).get(0).toString());
                StringBuilder column2 = new StringBuilder();
                if(values.get(0).get(1) instanceof List && ((List)values.get(0).get(1)).size() > 0){
                    for(Object obj:(List)values.get(0).get(1)){
                        if(StringUtils.isEmpty(obj.toString()) == false){
                            column2.append(obj).append("\r\n");
                        }
                    }
                }
                StringBuilder column3 = new StringBuilder();
                if(values.get(0).get(2) instanceof List && ((List)values.get(0).get(2)).size() > 0){
                    for(Object obj:(List)values.get(0).get(2)){
                        if(StringUtils.isEmpty(obj.toString()) == false){
                            String c3 = ToolUtil.findText(obj.toString(), ">(.+)<");
                            column3.append(c3).append("\r\n");
                        }
                    }
                }


                List<String> row = Arrays.asList(
                        column1.toString(),
                        column2.toString(),
                        column3.toString());
                rowList.add(row);
            }
            ExcelTool.exportExcelX(titleList, rowList, excelHtml);
//            String html = ThymeleafUtil.getInstance().process(root,"excel");
//            FileUtils.write(new File(excelHtml), html, "UTF-8");
        }
        catch(Exception e){
            log.error("保存excel出错====",e);
        }
        return excelHtml;
    }

    public void makeTableHtml(){

    }
    public static void main(String[] args) {
        ZhaoGuShuMain zhaoGuShuMain = new ZhaoGuShuMain();

//        File pdfFile = new File("F:\\work\\2021.11.03_企业测评_pdf数据提取\\未校验\\招股说明书结果20211221\\",
//              "444-深圳市亚辉龙生物科技股份有限公司.pdf"
//        );
        File pdfFile = new File("D:\\project\\idea\\datasfc\\pdftool\\doc",
                "grid.pdf"
        );
        zhaoGuShuMain.run(pdfFile);
//        zhaoGuShuMain.run2();
//        System.out.println(231.18896 + 9.071041);
//        System.out.println(231.18896 + 10.559998);
    }
}
