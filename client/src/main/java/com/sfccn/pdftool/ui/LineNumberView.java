package com.sfccn.pdftool.ui;

import javax.swing.*;
import java.awt.*;

public class LineNumberView extends JTextArea {
    public LineNumberView(){
        super();
        StringBuffer sff = new StringBuffer();
        for(int i = 1; i < 9999; i++){
            sff.append(String.format("%4s", i)).append("\n");
        }
        this.setText(sff.toString());
        this.setEnabled(false);
        this.setEditable(false);
        this.setBackground(new Color(228, 228, 228));
    }
}
