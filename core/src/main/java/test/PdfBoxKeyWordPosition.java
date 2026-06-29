package test;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PdfBoxKeyWordPosition extends PDFTextStripper {
    // 关键字字符数组
    private char[] key;
    // PDF文件路径
    private String pdfPath;
    // 坐标信息集合
    private List<float[]> list = new ArrayList<float[]>();
    // 当前页信息集合
    private List<float[]> pagelist = new ArrayList<float[]>();
    // 有参构造方法
    public PdfBoxKeyWordPosition(String keyWords, String pdfPath) throws IOException {
        super();
        super.setSortByPosition(true);
        this.pdfPath = pdfPath;
        char[] key = new char[keyWords.length()];
        for (int i = 0; i < keyWords.length(); i++) {
            key[i] = keyWords.charAt(i);
        }
        this.key = key;
    }
    public char[] getKey() {
        return key;
    }
    public void setKey(char[] key) {
        this.key = key;
    }
    public String getPdfPath() {
        return pdfPath;
    }
    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }
    // 获取坐标信息
    public List<float[]> getCoordinate() throws IOException {
        try {
            document = PDDocument.load(new File(pdfPath));
            int pages = document.getNumberOfPages();
            for (int i = 1; i <= pages; i++) {
                pagelist.clear();
                super.setSortByPosition(true);
                super.setStartPage(i);
                super.setEndPage(i);
                Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
                super.writeText(document, dummy);
                for (float[] li : pagelist) {
                    li[2] = i;
                }
                list.addAll(pagelist);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (document != null) {
                document.close();
            }
        }
        return list;
    }

    // 获取坐标信息
    @Override
    protected void writeString(String string, List<TextPosition> textPositions) throws IOException {

        float width = 0;
        for (int i = 0; i < textPositions.size(); i++) {
            width = width + textPositions.get(i).getWidth();
            // text得到pdf这一行中的汉字，同时下面有判断这一行字的长度，防止关键字在文中多次出现
            String text = textPositions.toString();//.replaceAll("[^\u4E00-\u9FA5]", "");
            String str = textPositions.get(i).getUnicode();
            System.out.println("writeString======text=" + text + ",str=" + str + ",x=" + (int)textPositions.get(i).getX() + ",y=" + (int)textPositions.get(i).getY());
//            String text = textPositions;
            if (str.equals(key[0] + "") && text.length() < 100) {
                int count = 0;
                for (int j = 0; j < key.length; j++) {
                    String s = "";
                    try {
                        s = textPositions.get(i + j).getUnicode();
//                        textPositions.get(i + j)
                    } catch (Exception e) {
                        s = "";
                    }
                    if (s.equals(key[j] + "")) {
                        count++;
                    }
                }
                System.out.println("1 count=" + count);
                if (count == key.length) {
                    System.out.println("2 count=" + count);
                    float[] idx = new float[3];
                    // 需要进行一些调整 使得章盖在字体上
                    // X坐标 在这里加上了字体的长度，也可以直接 idx[0] = textPositions.get(i).getX()
//                    idx[0] = textPositions.get(i).getX()+textPositions.get(i).getFontSize();
                    idx[0] = textPositions.get(i).getX();
                    // Y坐标 在这里减去的字体的长度，也可以直接 idx[1] = textPositions.get(i).getPageHeight()-textPositions.get(i).getY()
//                    idx[1] = textPositions.get(i).getPageHeight()-textPositions.get(i).getY()-4*textPositions.get(i).getFontSize();
                    idx[1] = textPositions.get(i).getY();
                    System.out.println("x=" + idx[0] + ",y=" + idx[1] + "," + textPositions.get(i).getWidth());
                    pagelist.add(idx);
                }
            }
        }
        System.out.println("writeString======" + string + ",width:" + (int)width);
//        System.out.println("width:" + (int)width);
    }
}

