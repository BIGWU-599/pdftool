package com.sfccn.pdftool.webview;

import com.sfccn.pdftool.client.DesktopPdf;
import com.sfccn.pdftool.main.ZhaoGuShuMain;
import com.sfccn.pdftool.util.GB;
import org.cef.CefClient;
import org.cef.browser.CefBrowser;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class WebDialog extends JDialog {
    public WebDialog(CefClient client, ZhaoGuShuMain zhaoGuShuMain){
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setModal(true);
        this.setTitle("提取图片到本地硬盘");
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        String file = zhaoGuShuMain.viewImageHtml();
        System.out.println("imgHtml====" + file);
        CefBrowser browser = client.createBrowser(file, false, false);

//        contentPane.add(browser.getUIComponent(), new GB(0,0,1,1).setFill(GB.BOTH).setWeight(100,100));
//        contentPane.add(new, BorderLayout.CENTER);
        JPanel topPanel = new JPanel(new GridBagLayout());
        EtchedBorder border = (EtchedBorder)BorderFactory.createEtchedBorder();

        topPanel.setBorder(border);
        contentPane.add(topPanel, BorderLayout.NORTH);
        JButton saveBtn = new JButton("保存图片");
        saveBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser(new File(file));
//            System.out.println("fileChooser＝===" + initFilePath);
            fileChooser.setApproveButtonText("选择");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showOpenDialog(null);
            if(result == JFileChooser.APPROVE_OPTION){
                File selectFile = fileChooser.getSelectedFile();
                zhaoGuShuMain.saveImageHtml(selectFile);
            }
        });
        topPanel.add(saveBtn,new GB(1,0,1,1).setInsets(5,5,5,5));
        contentPane.add(browser.getUIComponent(), BorderLayout.CENTER);

        Dimension screenshot = Toolkit.getDefaultToolkit().getScreenSize();

        setSize((int)(screenshot.width*0.7), (int)(screenshot.height*0.7));
        this.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                browser.close(true);
            }
        });
        this.setLocationRelativeTo(null);
    }
}
