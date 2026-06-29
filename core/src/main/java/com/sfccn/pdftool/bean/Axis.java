package com.sfccn.pdftool.bean;

public class Axis {
    private int x;
    private int y;
    private int page;

    // 横线
    private int horizontalX;
    private int horizontalY;

    // 竖线
    private int verticalX;
    private int verticalY;

    /**
     *
     * @param x
     * @param y
     * @param page
     * @param horizontalX 横线
     * @param horizontalY 横线
     * @param verticalX 竖线
     * @param verticalY 竖线
     */
    public Axis(int page, int x, int y, int horizontalX, int horizontalY, int verticalX, int verticalY) {
        this.x = x;
        this.y = y;
        this.page = page;
        this.horizontalX = horizontalX;
        this.horizontalY = horizontalY;
        this.verticalX = verticalX;
        this.verticalY = verticalY;
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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getHorizontalX() {
        return horizontalX;
    }

    public void setHorizontalX(int horizontalX) {
        this.horizontalX = horizontalX;
    }

    public int getHorizontalY() {
        return horizontalY;
    }

    public void setHorizontalY(int horizontalY) {
        this.horizontalY = horizontalY;
    }

    public int getVerticalX() {
        return verticalX;
    }

    public void setVerticalX(int verticalX) {
        this.verticalX = verticalX;
    }

    public int getVerticalY() {
        return verticalY;
    }

    public void setVerticalY(int verticalY) {
        this.verticalY = verticalY;
    }

    @Override
    public String toString() {
        return "{" +
                "page:" + page +
                ", x:" + x +
                ", y:" + y +
                ", horizontalX:" + horizontalX +
                ", horizontalY:" + horizontalY +
                ", verticalX:" + verticalX +
                ", verticalY:" + verticalY +
                '}';
    }
}
