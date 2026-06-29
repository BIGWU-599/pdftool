package com.sfccn.pdftool.main;

import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.libary.IntTreeMap;
import com.sfccn.pdftool.parse.ParseValue;
import com.sfccn.pdftool.parse.zhaogu.*;
import com.sfccn.pdftool.utils.ThymeleafUtil;
import com.sfccn.pdftool.utils.ToolUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.fit.pdfdom.PDFDomTree;
import org.fit.pdfdom.PathSegment;
import org.fit.pdfdom.TextMetrics;
import org.fit.pdfdom.resource.ImageResource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.w3c.dom.Element;
import java.io.*;
import java.util.*;

/**
 * 识别pdf表格数据
 */
public class PdfTableMain {
    public final Logger log = LogManager.getLogger(PdfTableMain.class);

    public void run(){
        try{
//            File pdfFile = new File("doc\\zhaogu\\303-圣湘生物科技股份有限公司.pdf");
//            File outputPath =  new File("doc\\zhaogu\\303-圣湘生物科技股份有限公司.pdf.html");
//            File pdfFile = new File("doc\\zhaogu\\049-中兰环保科技股份有限公司2_new.pdf");
//            File outputPath =  new File("doc\\zhaogu\\049-中兰环保科技股份有限公司2_new.html");
            File pdfFile = new File("doc\\zhaogu\\228-上海硅产业集团股份有限公司_new.pdf");
            File outputPath =  new File("doc\\zhaogu\\228-上海硅产业集团股份有限公司_new.pdf.html");
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPath),"UTF-8"));

            PDDocument document = PDDocument.load(pdfFile);
            CustomPDFDomTree pdfDomTree = new CustomPDFDomTree();
            pdfDomTree.writeText(document);

            List<Map<Integer,List<PdfObject>>> pageList = pdfDomTree.getPageList();
            List<List<PdfObject>> pageList2 = new ArrayList<>();
            for(int page = 0; page < pageList.size(); page++){
                Map<Integer,List<PdfObject>> rows = pageList.get(page);
                rows.forEach((key,pdfObject)->{
                    pageList2.add(pdfObject);
                });
            }
            List<List<List<Object>>> valueList2 = ParseValue.parse(null,pageList2,
                    Arrays.asList(
                            Parse01CreditCode.class, Parse02SignDate.class, Parse03CnName.class,
                            Parse04Address.class, Parse05Industry.class, Parse06Segmentation.class,
                            Parse07Employee.class, Parse08EmployeeTech.class, Parse20NetCashFlow.class,
                            Parse21Income.class,Parse22Profit.class,Parse23Research.class,
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

            Map root = new HashMap();
            root.put("valueList", valueList2);
            root.put("title", pdfFile.getName());
//            System.out.println("valueList=====" + valueList2);
//            String html = FreeMarkerUtil.getInstance().process(root, "zhaogu.ftl");
            String html = ThymeleafUtil.getInstance().process(root, "zhaogu");
            //FileUtils.write(new File(pdfFile.getAbsolutePath() + ".html"), html, "UTF-8");
            Document docZhaogu = Jsoup.parse(html);

            CustomPDFDomTree pdfDomTree2 = new CustomPDFDomTree();
            pdfDomTree2.writeText(document, out, ParseValue.getMarkMap());
//            System.out.println("html=======" + html);
            System.out.println("valueList=====" + valueList2);
            System.out.println("MarkMap======" + ParseValue.getMarkMap());
            String content = FileUtils.readFileToString(outputPath, "UTF-8");
//            content = content.replace("letter-spacing:1.8pt;", "");
            content = content.replaceAll("@@(.+?)##(.+?)@@", "<em id=\"$1\">$2</em>");

            Document doc = Jsoup.parse(content);
            doc.select("style").remove();
            doc.head().append(docZhaogu.select("style").outerHtml());
            doc.head().append("");
            doc.body().select("div").first().before(docZhaogu.body().html());
            Elements divs = doc.select("div");
            for(org.jsoup.nodes.Element div:divs){
                String data_type = div.attr("data-type") != null ? div.attr("data-type") : "";
                String styleString = div.attr("style");
                String[] styles = styleString.split(";");
                StringBuffer styleBuffer = new StringBuffer();
                for(String style:styles){
                    String[] strs = style.split("\\:");
//                            || strs[0].equals("-webkit-text-stroke") == true
                    if(strs[0].equals("letter-spacing") == true || strs[0].equals("text-shadow") == true){
                        continue;
                    }
                    if(strs[0].equals("width") == true && data_type.equals("text")){
                        String width = ToolUtil.findText(strs[1], "([0-9\\.]+)");
                        if(StringUtils.isEmpty(width) == false){
                            width = Double.valueOf(width) * 1.3 + "pt;";
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
            }
            String htmlPdf = doc.outerHtml();
            htmlPdf = htmlPdf.replaceAll("\\s{2,}", " ");
            FileUtils.write(outputPath, htmlPdf, "UTF-8");
            document.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    class CustomPDFDomTree extends PDFDomTree {
        public boolean isMark = false; // 是否需要em标记
        //  key:page_x_y,PdfObject
        public Map<String,PdfObject> markMap = null;//new HashMap<>();
        private List<Map<Integer,List<PdfObject>>> pageList = new ArrayList<>();
        private Map<Integer,List<PdfObject>> rows = null;//new IntTreeMap<>();
        private Map<Integer,List> grids = new IntTreeMap<>();
        public CustomPDFDomTree() throws IOException {
            super();
        }

        protected void startNewPage(){
            System.out.println("pagecnt＝＝＝＝" + pagecnt);
            if(pageList.size() <= pagecnt){
                rows = new IntTreeMap<>();
                pageList.add(rows);
            }
            super.startNewPage();
        }
        protected void renderText(String data, TextMetrics metrics){
            int y = (int)(metrics.getTop() + metrics.getAscent());
            int x = (int)metrics.getX();
            String key = pagecnt + "_" + x + "_" + y;
            // 2_220_165
            if(markMap != null && markMap.get(key) != null){
                PdfObject pdfObject = markMap.get(key);
                data = "@@" + key + "##" + data + "@@";
                System.err.println("renderText XXXXXXXXXXX==========" + data);
            }
//        int x, int y, String type, String data, Object typeinfo, int pagenum
            PdfObject pdfObject = new PdfObject(x,y,PdfObject.STYLE_STRING, data, new Integer[]{x,y,(int)metrics.getWidth(), (int)metrics.getHeight()}, pagecnt);
            List<PdfObject> columns = rows.get(y);
            if(columns == null){
                columns = new ArrayList();
            }
            columns.add(pdfObject);
            rows.put(y, columns);
            //                  createRectangleElement=========y:
            System.out.println("renderText=====================y:" + y + ",    " + data + ",page:" + pagecnt+ ",x:" + (int)metrics.getX() + ",y:" + (int)metrics.getTop() + ",width:" + (int)metrics.getWidth() + ",height:" + (int)metrics.getHeight() + ",ascent:" + (int)metrics.getAscent() + ",descent" + (int)metrics.getDescent());
            super.renderText(data, metrics);
        }

        protected Element createTextElement(String data, float width)
        {
            Element el = super.createTextElement(data, width);
            el.setAttribute("data-type", "text");
            return el;
        }

        protected Element createLineElement(float x1, float y1, float x2, float y2){
            List columns = rows.get((int)y1);
            PdfObject column = new PdfObject((int)x1,(int)y1,PdfObject.STYLE_LINE, "", new Integer[]{(int)x1, (int)y1, (int)x2, (int)y2}, pagecnt);
            if(columns == null){
                columns = new ArrayList();
            }
            columns.add(column);
            rows.put((int)y1, columns);
            System.out.println("createLineElement=============y:" + (int)y1 + ",    " +(int)x1 + ",y1:" +(int)y1 + ",x2:" + (int)x2 + ",y2:" + (int)y2);
            return super.createLineElement(x1, y1, x2, y2);
        }
        protected Element createRectangleElement(float x, float y, float width, float height, boolean stroke, boolean fill) {
            System.out.println("createRectangleElement=========y:" + (int)y + ",    x:" + x + ", y:" + y + ",width:" + width + ", height:" + height + ", stroke:" + stroke + ", fill:" + fill + ",fcolor:" + colorString(getGraphicsState().getNonStrokingColor()));
//            float lineWidth = transformWidth(getGraphicsState().getLineWidth());
//            float wcor = stroke ? lineWidth : 0.0f;
//            float strokeOffset = wcor == 0 ? 0 : wcor / 2;
////            fload width1 = width - wcor < 0 ? 1 : width - wcor;
////            int height2 = height - wcor < 0 ? 1 : height - wcor;
//            System.out.println("2createRectangleElement=========y:" + (int)y + ",    x:" + x + ", y:" + y + ",width:" + width + ", height:" + height + ", stroke:" + stroke + ", fill:" + fill + ",fcolor:" + colorString(getGraphicsState().getNonStrokingColor()));

            return super.createRectangleElement(x, y, width, height, stroke, fill);

        }
        protected Element createPathImage(List<PathSegment> path) throws IOException{
            System.out.println("createPathImage=============" + path);
            return super.createPathImage(path);
        }
        protected Element createImageElement(float x, float y, float width, float height, ImageResource resource) throws IOException {
            System.out.println("createImageElement========x:" + (int)x + ",y:" + (int)y + ",width:" + width + ",height:" + height);
            String imgSrc = config.getImageHandler().handleResource(resource);
            PdfObject column = new PdfObject((int)x,(int)y,PdfObject.STYLE_IMAGE, imgSrc, Map.of("x",x,"y",y,"width",width,"height",height), pagecnt);
            List columns = rows.get((int)y);
            if(columns == null){
                columns = new ArrayList();
            }
            columns.add(column);
            rows.put((int)y, columns);
            return super.createImageElement(x, y, width, height,resource);
        }

        public void writeText(PDDocument doc) throws IOException{
            try
            {
                DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
                DOMImplementationLS impl = (DOMImplementationLS)registry.getDOMImplementation("LS");
                LSSerializer writer = impl.createLSSerializer();
                LSOutput output = impl.createLSOutput();
                writer.getDomConfig().setParameter("format-pretty-print", true);
                createDOM(doc);
            } catch (ClassCastException e) {
                throw new IOException("Error: cannot initialize the DOM serializer", e);
            } catch (ClassNotFoundException e) {
                throw new IOException("Error: cannot initialize the DOM serializer", e);
            } catch (InstantiationException e) {
                throw new IOException("Error: cannot initialize the DOM serializer", e);
            } catch (IllegalAccessException e) {
                throw new IOException("Error: cannot initialize the DOM serializer", e);
            }
        }
        public void writeText(PDDocument doc, Writer outputStream, Map<String,PdfObject> markMap) throws IOException
        {
            try
            {
                this.markMap = markMap;
                DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
                DOMImplementationLS impl = (DOMImplementationLS)registry.getDOMImplementation("LS");
                LSSerializer writer = impl.createLSSerializer();
                LSOutput output = impl.createLSOutput();
                writer.getDomConfig().setParameter("format-pretty-print", true);
                output.setCharacterStream(outputStream);
                createDOM(doc);
                writer.write(getDocument(), output);
            } catch (ClassCastException e) {
                throw new IOException("Error: cannot initialize the DOM serializer", e);
            } catch (ClassNotFoundException e) {
                throw new IOException("Error: cannot initialize the DOM serializer", e);
            } catch (InstantiationException e) {
                throw new IOException("Error: cannot initialize the DOM serializer", e);
            } catch (IllegalAccessException e) {
                throw new IOException("Error: cannot initialize the DOM serializer", e);
            }
        }
        public void setMarkMap(Map<String, PdfObject> markMap) {
            this.markMap = markMap;
        }

        public List<Map<Integer,List<PdfObject>>> getPageList(){
            return pageList;
        }
    }
    public static void main(String[] args) {
        PdfTableMain pdfTableMain = new PdfTableMain();
        pdfTableMain.run();

//        String str="China12345America678922England342343434Mexica";
//        System.out.println(str.replaceAll("\\d+", " "));
//
//        str="China|||||America::::::England&&&&&&&Mexica";
//        System.out.println(str.replaceAll("(.)\\1+","$1"));
//
//        String content = "@@1_1_100##中兰环保科技股份有限公司@@";
//        System.out.println(content.replaceAll("@@(.+?)##(.+?)@@", "<em id=\"$1\">$2</em>"));
//        System.out.println(ToolUtil.findText("width:81.27994pt;", "([0-9\\.]+)"));
    }
}
