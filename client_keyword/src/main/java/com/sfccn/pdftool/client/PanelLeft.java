package com.sfccn.pdftool.client;

import com.sfccn.pdftool.utils.GB;
import com.sfccn.pdftool.view.ProjectTable;

import javax.swing.*;
import java.awt.*;

public class PanelLeft extends JPanel {
    private JTextField pathField;
    private JButton selectBtn;
    public static ProjectTable projectTableLeft;

    public PanelLeft() {
        super();
        this.setLayout(new GridBagLayout());

        JLabel label = new JLabel("第一步:");
        this.add(label, new GB(0, 0, 1, 1).setFill(GB.BOTH).setInsets(5));

        pathField = new JTextField();
        this.add(pathField, new GB(1, 0, 1, 1).setWeight(100, 0).setFill(GB.BOTH).setInsets(5));

        selectBtn = new JButton("选择");
        selectBtn.addActionListener(l->{
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fileChooser.showOpenDialog(this);
            pathField.setText(fileChooser.getSelectedFile().getAbsolutePath());
        });
        this.add(selectBtn, new GB(2, 0, 1, 1).setInsets(5));
        projectTableLeft = new ProjectTable();
        this.add(projectTableLeft, new GB(0, 1, 3, 1).setFill(GB.BOTH).setWeight(100,100).setInsets(5));
    }
}
