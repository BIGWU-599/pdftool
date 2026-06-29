package com.sfccn.pdftool.view;

import org.jdesktop.swingx.JXTreeTable;

public class DfTreeTable extends JXTreeTable{
    protected DataNode root = null;
    public DataNode getRootNode(){
        return root;
    }

    public void refreshUI(){
        updateUI();
        repaint();
    }
}
