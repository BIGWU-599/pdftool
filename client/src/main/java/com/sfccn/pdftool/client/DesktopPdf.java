package com.sfccn.pdftool.client;

import com.sfccn.pdftool.webview.ClosableTabbedPane;
import com.sfccn.pdftool.webview.WebViewCef;
import me.friwi.jcefmaven.impl.progress.ConsoleProgressHandler;
import org.cef.CefApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * jdk9后版本需要配置vm参数:-Djava.library.path=libs\win64
 */
public class DesktopPdf extends JFrame {
    private final ClosableTabbedPane tabbedPane = new ClosableTabbedPane();
    public DesktopPdf(){
        initCef();
        initPanel();
        this.setTitle("PDF文件数据自动化提取软件V1.0");
        this.setSize(getScreenSize());
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    private Dimension getScreenSize(){
        Dimension screenshot = Toolkit.getDefaultToolkit().getScreenSize();
        int w = screenshot.width;
        int h = screenshot.height;
        double size = 0.8;
        return new Dimension((int)(w*size), (int)(h*size));
    }
    private void initCef(){
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                CefApp.getInstance().dispose();
                dispose();
            }
        });
    }
    private void initPanel(){
        tabbedPane.addTab(" ", new WebViewCef(DesktopPdf.this, tabbedPane, 0, ""), null);
        tabbedPane.add("+", new JPanel());
        tabbedPane.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                int index = tabbedPane.getSelectedIndex();
                int tabNumber = tabbedPane.getTabCount() - 1;
                if(index == tabNumber){
                    tabbedPane.insertTab(" ", new WebViewCef(DesktopPdf.this, tabbedPane, tabNumber, ""), tabNumber);
                    tabbedPane.setSelectedIndex(tabNumber);
                }
                e.consume();
            }
        });
        this.add(tabbedPane);

    }
    public void addBrowserTab(String url){
        int tabNumber = tabbedPane.getTabCount();
        tabbedPane.insertTab(" ", new WebViewCef(DesktopPdf.this, tabbedPane, tabNumber - 1, url), tabNumber - 1);
        tabbedPane.setSelectedIndex(tabNumber - 1);

    }
    public static void main(String[] args) {
        Logger log= Logger.getLogger(ConsoleProgressHandler.class.getName());
        log.setLevel(Level.WARNING);
        try {
            UIManager
                    .setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            EventQueue.invokeLater(() -> {
                try {
                    DesktopPdf frame = new DesktopPdf();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
