package com.sfccn.pdftool.webview.cef;


import com.sfccn.pdftool.util.GB;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class StatusPanel extends JPanel {
    private final JProgressBar progressBar_;
    private final JLabel status_field_;

    public StatusPanel() {
//        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
//
//        add(Box.createHorizontalStrut(5));
//        add(Box.createHorizontalStrut(5));
        setLayout(new GridBagLayout());
        progressBar_ = new JProgressBar();
        Dimension progressBarSize = progressBar_.getMaximumSize();
        progressBarSize.width = 100;
        progressBar_.setMinimumSize(progressBarSize);
        progressBar_.setMaximumSize(progressBarSize);
        add(progressBar_, new GB(0, 0, 1,1));
        //add(Box.createHorizontalStrut(5));

        status_field_ = new JLabel("Info");
        status_field_.setAlignmentX(LEFT_ALIGNMENT);
        add(status_field_, new GB(1, 0, 1,1));
        add(new JPanel(), new GB(2, 0, 1, 1).setFill(GB.BOTH).setWeight(100, 0));
        //        add(Box.createHorizontalStrut(5));
//        add(Box.createVerticalStrut(21));
    }

    public void setIsInProgress(boolean inProgress) {
        progressBar_.setIndeterminate(inProgress);
    }

    public void setStatusText(String text) {
        status_field_.setText(text);
    }
}
