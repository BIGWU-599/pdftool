package com.sfccn.pdftool.libary;

import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.utils.ToolUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.fit.pdfdom.PDFDomTree;
import org.fit.pdfdom.PathSegment;
import org.fit.pdfdom.TextMetrics;
import org.fit.pdfdom.resource.ImageResource;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomPDFDomTree extends PDFDomTree {
    public boolean isMark = false; // 是否需要em标记
    //  key:page_x_y,PdfObject
    public Map<String,PdfObject> markMap = null;
    private List<Map<Integer,List<PdfObject>>> pageList = new ArrayList<>();
    private Map<Integer,List<PdfObject>> rows = null;
    public CustomPDFDomTree() throws IOException {
        super();
    }
    protected String colorString(PDColor pdcolor)
    {
        String color = null;
        try
        {
            float[] rgb = pdcolor.getColorSpace().toRGB(pdcolor.getComponents());
            color = colorString(rgb[0], rgb[1], rgb[2]);
        } catch (IOException e) {
//            log.error("colorString: IOException: {}", e.getMessage());
        } catch (UnsupportedOperationException e) {
//            log.error("colorString: UnsupportedOperationException: {}", e.getMessage());
        }
        return color;
    }
    protected void startNewPage(){
        if(pageList.size() <= pagecnt){
            rows = new IntTreeMap<>();
            pageList.add(rows);
        }
        super.startNewPage();
    }
    protected void renderText(String data, TextMetrics metrics){
        int y = (int)metrics.getTop();
        int x = (int)metrics.getX();
        if(markMap == null){
//            int y = (int)(metrics.getTop() + metrics.getAscent());
            PdfObject pdfObject = new PdfObject(x,y,PdfObject.STYLE_STRING, data, new Integer[]{x,y,(int)metrics.getWidth(), (int)metrics.getHeight()}, pagecnt);
            List<PdfObject> columns = null;
            int tempy = y;
            columns = rows.get(y);
            if(columns == null){
                columns = new ArrayList();
            }
            columns.add(pdfObject);
            rows.put(tempy, columns);
//            System.out.println("renderText============:" + data + ",定位y轴:" + y + ",page:" + pagecnt + ",x:" + (int)metrics.getX() + ",y:" + (int)metrics.getTop() + ",width:" + (int)metrics.getWidth() + ",height:" + (int)metrics.getHeight() + ",ascent:" + (int)metrics.getAscent() + ",descent" + (int)metrics.getDescent() + "," + ToolUtil.toJson(metrics));

        }
        else{
            String key = pagecnt + "_" + x + "_" + y + "_" + data.length();
            if(markMap != null && markMap.get(key) != null){
                data = "@@" + key + "##" + data + "@@";
            }
        }
        super.renderText(data, metrics);
    }
    /**
     * Creates an element that represents a single positioned box containing the specified text string.
     * @param data the text string to be contained in the created box.
     * @return the resulting DOM element
     */
    protected Element createTextElement(String data, float width)
    {
        Element el = super.createTextElement(data, width);
        el.setAttribute("data-type", "text");
        return el;
    }
    protected Element createLineElement(float x1, float y1, float x2, float y2){
        if(markMap == null){
            List columns = rows.get((int)y1);
//            PdfObject column = new PdfObject((int)x1,(int)y1,PdfObject.STYLE_LINE, "", new Integer[]{(int)x1, (int)y1, (int)x2, (int)y2}, pagecnt);
//            if(columns == null){
//                columns = new ArrayList();
//            }
            if((x1 + 1) < x2 || (y1 + 1) < y2){
                PdfObject column = new PdfObject((int)x1,(int)y1,PdfObject.STYLE_LINE, "", new Integer[]{(int)x1, (int)y1, (int)x2, (int)y2}, pagecnt);
                if(columns == null){
                    columns = new ArrayList();
                }
                columns.add(column);
                rows.put((int)y1, columns);
            }
            //x=158, y=371
//            if(x1 == 158 && y1 == 371){
//                System.out.println("createLineElement=============x1:"+(int)x1 + ",y1:" +(int)y1 + ",x2:" + (int)x2 + ",y2:" + (int)y2 + ",");
//            }
//        System.out.println("createLineElement=============x1:"+(int)x1 + ",y1:" +(int)y1 + ",x2:" + (int)x2 + ",y2:" + (int)y2);
        }
        return super.createLineElement(x1, y1, x2, y2);
    }
    protected Element createRectangleElement(float x, float y, float width, float height, boolean stroke, boolean fill) {
//            System.out.println("createRectangleElement=========y:" + (int)y + ",    x:" + x + ", y:" + y + ",width:" + width + ", height:" + height + ", stroke:" + stroke + ", fill:" + fill);
        if(markMap == null){
            // #ffffff
            String bg = colorString(getGraphicsState().getNonStrokingColor());

            List columns = rows.get((int)y);
            int x1 = (int)x;
            int y1 = (int)y;
            int x2 = x1;
            int y2 = y1;
            if(width > height){
                x2 = x1 + (int)width;
            }
            else{
                y2 = y1 + (int)height;
            }
            if(((x1 + 1) < x2 || (y1 + 1) < y2) && (bg == null || bg.equals("#ffffff") == false)
                && (10 > height && y1 == y2)){
                PdfObject column = new PdfObject(x1,y1,PdfObject.STYLE_LINE, "", new Integer[]{x1, y1, x2, y2}, pagecnt);
                if(columns == null){
                    columns = new ArrayList();
                }
                columns.add(column);
                rows.put(y1, columns);
            }
            // x=303, y=350
//            if(x1 == 303 && y1 == 350){
//                System.out.println("createRectangleElement=========y:" + (int)y + ",    x:" + x + ", y:" + y + ",width:" + width + ", height:" + height + ", stroke:" + stroke + ", fill:" + fill + ",fcolor:" + colorString(getGraphicsState().getNonStrokingColor()));
//            }
        }
        return super.createRectangleElement(x, y, width, height, stroke, fill);
    }
    protected Element createPathImage(List<PathSegment> path) throws IOException{
//            System.out.println("createPathImage=============");
        return super.createPathImage(path);
    }
    protected Element createImageElement(float x, float y, float width, float height, ImageResource resource) throws IOException {
        if(markMap == null){
            //            System.out.println("createImageElement========x:" + (int)x + ",y:" + (int)y + ",width:" + width + ",height:" + height);
            String imgSrc = config.getImageHandler().handleResource(resource);
            // Map.of("x",x,"y",y,"width",width,"height",height)
            PdfObject column = new PdfObject((int)x,(int)y,PdfObject.STYLE_IMAGE, imgSrc, new Integer[]{(int)x,(int)y,(int)width,(int)height}, pagecnt);
            List columns = rows.get((int)y);
            if(columns == null){
                columns = new ArrayList();
            }
            columns.add(column);
            rows.put((int)y, columns);
        }
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
