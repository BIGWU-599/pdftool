package com.sfccn.pdftool.webview;

import org.cef.browser.CefBrowser;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class SearchDialog extends JDialog {
    private final CefBrowser browser_;
    private final JTextField searchField_ = new JTextField(30);
    private final JCheckBox caseCheckBox_ = new JCheckBox("区分大小写");
    private final int identifier_ = (int) Math.random();
    private final JButton prevButton_ = new JButton("上一个"); // Prev
    private final JButton nextButton_ = new JButton("下一个"); // Next

    public SearchDialog(Frame owner, CefBrowser browser) {
        super(owner, "查找...", false);
        browser_ = browser;

        setLayout(new BorderLayout());
        setSize(400, 100);
        setLocationRelativeTo(null);
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
        searchPanel.add(Box.createHorizontalStrut(5));
        searchPanel.add(new JLabel("查找内容:"));
        searchPanel.add(searchField_);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
        controlPanel.add(Box.createHorizontalStrut(5));

        JButton searchButton = new JButton("查找");
        searchButton.addActionListener(e -> {
            if (searchField_.getText() == null || searchField_.getText().isEmpty()) return;

            setTitle("Find \"" + searchField_.getText() + "\"");
            boolean matchCase = caseCheckBox_.isSelected();
            browser_.find(identifier_, searchField_.getText(), true, matchCase, false);
            prevButton_.setEnabled(true);
            nextButton_.setEnabled(true);
        });
        controlPanel.add(searchButton);

        prevButton_.addActionListener(e -> {
            boolean matchCase = caseCheckBox_.isSelected();
            setTitle("Find \"" + searchField_.getText() + "\"");
            browser_.find(identifier_, searchField_.getText(), false, matchCase, true);
        });
        prevButton_.setEnabled(false);
        controlPanel.add(prevButton_);

        nextButton_.addActionListener(e -> {
            boolean matchCase = caseCheckBox_.isSelected();
            setTitle("Find \"" + searchField_.getText() + "\"");
            browser_.find(identifier_, searchField_.getText(), true, matchCase, true);
        });
        nextButton_.setEnabled(false);
        controlPanel.add(nextButton_);

        controlPanel.add(Box.createHorizontalStrut(50));

        JButton doneButton = new JButton("关闭");
        doneButton.addActionListener(e -> {
            setVisible(false);
            dispose();
        });
        controlPanel.add(doneButton);

        add(searchPanel, BorderLayout.NORTH);
        add(caseCheckBox_);
        add(controlPanel, BorderLayout.SOUTH);
    }

    @Override
    public void dispose() {
        browser_.stopFinding(true);
        super.dispose();
    }
}
