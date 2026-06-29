package com.sfccn.pdftool.bean;

import com.sfccn.pdftool.utils.ToolUtil;

public class PdfObject {
    public final static String STYLE_STRING = "string";
    public final static String STYLE_LINE = "line";
    public final static String STYLE_IMAGE = "image";
    public int x;
    public int y;
    private String type;
    private String data;
    private Object typeInfo;
    private int pagenum;

    public PdfObject() {
    }

    public PdfObject(int x, int y, String type, String data, Object typeinfo, int pagenum) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.data = data;
        this.typeInfo = typeinfo;
        this.pagenum = pagenum;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Object getTypeInfo() {
        return typeInfo;
    }

    public void setTypeInfo(Object typeInfo) {
        this.typeInfo = typeInfo;
    }

    public int getPagenum() {
        return pagenum;
    }

    public void setPagenum(int pagenum) {
        this.pagenum = pagenum;
    }

    public String toString(){
        StringBuffer sff = new StringBuffer()
                .append(type).append(",").append(data).append(",x=").append(x).append(", y=")
                .append(y).append(",").append(ToolUtil.toJson(typeInfo)).append(",page=").append(pagenum);
        return sff.toString();
//        return type + ","+ data + ",x=" + x + ", y=" + y + "," + ToolUtil.toJson(typeInfo);
    }
}
