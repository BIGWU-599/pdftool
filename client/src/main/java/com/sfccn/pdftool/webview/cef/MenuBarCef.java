package com.sfccn.pdftool.webview.cef;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sfccn.pdftool.main.ZhaoGuShuMain;
import com.sfccn.pdftool.utils.ToolUtil;
import com.sfccn.pdftool.webview.ScriptDialog;
import com.sfccn.pdftool.webview.WebViewCef;
import org.apache.commons.io.FileUtils;
import org.cef.browser.CefBrowser;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.io.File;

public class MenuBarCef {
    public AddressMenu addressMenu = null;
    public CefBrowser browser = null;
    public SettingMenu settingMenu = null;
    public JFrame frame = null;
    public WebViewCef webViewCef = null;
    private String initFile = "logs/init.txt";
    private File initFilePath = null;
    public MenuBarCef(JFrame frame, WebViewCef webViewCef){//CefBrowser browser
        this.frame = frame;
        this.browser = webViewCef.browser_;
        this.webViewCef = webViewCef;
        initPdfPath();
    }
    public void initSettingMenu(){
        settingMenu = new SettingMenu();
    }
    public void initAddressMenu(JTextField urlField){
        addressMenu = new AddressMenu(urlField);
    }
    public void initPdfPath(){
        File file = new File(initFile);
        try{
            if(file.exists() == true){
                String content = FileUtils.readFileToString(file, "UTF-8");
                JsonNode jsonNode = ToolUtil.strToJsonNode(content);
                if(jsonNode != null){
                    String pdfPath = jsonNode.get("pdf_path").asText();
                    initFilePath = new File(pdfPath);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public void savePdfPath(){
        File file = new File(initFile);
        try{
            if(file.getParentFile().exists() == false){
                file.mkdir();
            }
            JsonNode jsonNode = null;
            if(file.exists()){
                String content = FileUtils.readFileToString(file, "UTF-8");
                jsonNode = ToolUtil.strToJsonNode(content);
            }
            if(jsonNode == null){
                ObjectMapper mapper = new ObjectMapper();
                jsonNode = mapper.createObjectNode();
            }
            ((ObjectNode)jsonNode).put("pdf_path", initFilePath.getAbsolutePath());
            FileUtils.write(file, jsonNode.toString(), "UTF-8");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public class SettingMenu{
        public JPopupMenu popupMenu = null;
        public SettingMenu(){
            // 设置菜单选项
            popupMenu = new JPopupMenu();
            JMenuItem zhaogupdf = new JMenuItem("选择招股说明书PDF");
            zhaogupdf.addActionListener(e->{
                JFileChooser fileChooser = new JFileChooser("");
//            System.out.println("fileChooser＝===" + initFilePath);
                fileChooser.setSelectedFile(initFilePath);
                FileNameExtensionFilter filter = new FileNameExtensionFilter( "TXT files (*.pdf)", "pdf", "PDF");
                fileChooser.setFileFilter(filter);
                int result = fileChooser.showOpenDialog(null);
                if(result == JFileChooser.APPROVE_OPTION){
                    File emptyHtml = new File("data/thymeleaf/empty.html");
//                    browser_.loadURL(emptyHtml.getAbsolutePath());
                    browser.loadURL(emptyHtml.getAbsolutePath());
                    File selectFile = fileChooser.getSelectedFile();
                    initFilePath = selectFile;

//                    addressField.setText(selectFile.getAbsolutePath());
                    savePdfPath();
                    new Thread(() -> {
                        ZhaoGuShuMain zhaoGuShuMain = new ZhaoGuShuMain();
                        String file = zhaoGuShuMain.run(selectFile);
                        System.out.println("zhaoGuShuMain.run(selectFile)======" + file);
                        SwingUtilities.invokeLater(() -> {
                            browser.loadURL(file);
                        });
                    }).start();
                }
            });
            popupMenu.add(zhaogupdf);

            JMenuItem viewSource = new JMenuItem("查看源码");
            viewSource.addActionListener(e->{
                ShowTextDialog visitor = new ShowTextDialog(
                        frame, "Source of \"" + browser.getURL() + "\"");
                browser.getSource(visitor);
            });
            popupMenu.add(viewSource);
            JMenuItem runjs = new JMenuItem("执行脚本");
            runjs.addActionListener(e->{
                ScriptDialog scriptDialog = new ScriptDialog(webViewCef, browser);
                scriptDialog.setVisible(true);
            });
            popupMenu.add(runjs);
            JMenuItem shwodevtoole = new JMenuItem("显示开发者工具");
            shwodevtoole.setAccelerator(KeyStroke.getKeyStroke('D', InputEvent.CTRL_MASK));
            shwodevtoole.addActionListener(e->{
                DevToolsDialog devToolsDlg = new DevToolsDialog(frame, "DEV Tools", browser);
                devToolsDlg.addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentHidden(ComponentEvent e) {
                        shwodevtoole.setEnabled(true);
                    }
                });
                devToolsDlg.setVisible(true);
                shwodevtoole.setEnabled(false);
            });
            popupMenu.add(shwodevtoole);
        }
        public void show(Component invoker, int x, int y){
            int width = popupMenu.getWidth();
            System.out.println("width======" + width);
            popupMenu.show(invoker, x - width, y);
            if(width == 0){
                width = popupMenu.getWidth();
                popupMenu.show(invoker, x - width, y);
            }

        }
    }

    public class AddressMenu{
        private JPopupMenu popupMenu = null;
        public JMenuItem copy = null;
        public JMenuItem paste = null;
        public JMenuItem cut = null;
        private JTextField urlField = null;
        public AddressMenu(JTextField urlField){
            // 地址栏弹出菜单
            this.urlField = urlField;
            popupMenu = new JPopupMenu();
//            popupMenu.setLightWeightPopupEnabled(false);

            cut = new JMenuItem("剪切");
            cut.setAccelerator(KeyStroke.getKeyStroke('X', InputEvent.CTRL_MASK));
            cut.addActionListener(e->{
                urlField.cut();
            });
            popupMenu.add(cut);

            copy = new JMenuItem("复制");
            copy.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.CTRL_MASK));
            copy.addActionListener(e->{
                urlField.copy();
            });

            popupMenu.add(copy);
            paste = new JMenuItem("粘贴");
            paste.setAccelerator(KeyStroke.getKeyStroke('V', InputEvent.CTRL_MASK));
            paste.addActionListener(e->{
                urlField.paste();
            });
            popupMenu.add(paste);

        }

        public void show(Component invoker, int x, int y){
            cut.setEnabled(isCanCopy());
            copy.setEnabled(isCanCopy());
            paste.setEnabled(isClipboardString());
            popupMenu.show(invoker, x, y);
        }

        public boolean isCanCopy() {
            boolean b = false;
            int start = urlField.getSelectionStart();
            int end = urlField.getSelectionEnd();
            //log.info("========start:" + start + ", end:" + end);
            if (start != end)
                b = true;
            return b;
        }

        public boolean isClipboardString() {
            boolean b = false;
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable content = clipboard.getContents(this);
            try {
                if (content.getTransferData(DataFlavor.stringFlavor) instanceof String) {
                    b = true;
                }
            } catch (Exception e) {
            }
            return b;
        }
    }
}
