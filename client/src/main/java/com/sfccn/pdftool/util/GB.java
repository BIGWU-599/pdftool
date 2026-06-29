package com.sfccn.pdftool.util;

import java.awt.*;

public class GB extends GridBagConstraints {
    private static final long serialVersionUID = 1L;

    public GB(int gridx, int gridy) {
        this.gridx = gridx;
        this.gridy = gridy;
    }

    public GB(int gridx, int gridy, int gridwidth, int gridheight) {
        this.gridx = gridx;
        this.gridy = gridy;
        this.gridwidth = gridwidth;
        this.gridheight = gridheight;
    }

    public GB setAnchor(int anchor) {
        this.anchor = anchor;
        return this;
    }

    public GB setFill(int fill) {
        this.fill = fill;
        return this;
    }

    public GB setWeight(double weigthx, double weighty) {
        this.weightx = weigthx;
        this.weighty = weighty;
        return this;

    }

    public GB setInsets(int distance) {
        this.insets = new Insets(distance, distance, distance, distance);
        return this;
    }

    public GB setInsets(int top, int left, int bottom, int right) {
        this.insets = new Insets(top, left, bottom, right);
        return this;
    }

    public GB setIpad(int ipadx, int ipady) {
        this.ipadx = ipadx;
        this.ipady = ipady;
        return this;
    }

}
