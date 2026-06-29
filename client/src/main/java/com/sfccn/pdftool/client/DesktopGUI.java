package com.sfccn.pdftool.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.main.ZhaoGuShuMain;
import com.sfccn.pdftool.ui.AddressMenu;
import com.sfccn.pdftool.ui.SettingMenu;
import com.sfccn.pdftool.util.GB;
import com.sfccn.pdftool.utils.ToolUtil;
import com.sfccn.pdftool.webview.ContextMenuHandler;
import com.sfccn.pdftool.webview.ExcelDialog;
import com.sfccn.pdftool.webview.WebDialog;
import me.friwi.jcefmaven.CefAppBuilder;
import me.friwi.jcefmaven.MavenCefAppHandlerAdapter;
import me.friwi.jcefmaven.impl.progress.ConsoleProgressHandler;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefMessageRouter;
import org.cef.handler.CefFocusHandlerAdapter;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Random;
//import java.util.logging.Level;
//import java.util.logging.Logger;

/**
 * -Djava.library.path=libs\win64
 */
public class DesktopGUI extends JFrame {
    public final Logger log = LogManager.getLogger(DesktopGUI.class);
    private CefApp cefApp_;
    private CefClient client;
    private CefBrowser browser;
    private final String initFile = "logs/init.txt";
    private File initFilePath = null;
    private JTextField addressField;
    private final int identifier_ = new Random().nextInt(1000);//.(int) Math.random();
    private boolean browserFocus_ = true;
    private SettingMenu settingMenu = null;
    private ZhaoGuShuMain zhaoGuShuMain = new ZhaoGuShuMain();
    public DesktopGUI(){
        initMenu();
        initToolPanl();
        initWebView();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenshot = Toolkit.getDefaultToolkit().getScreenSize();
        setTitle("PDF文件数据自动化提取软件V1.0");
        pack();
        setSize((int)(screenshot.width*0.8), (int)(screenshot.height*0.8));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initMenu(){
        final String str_exit = "退出";
        final String str_pdf = "解析招股说明书(PDF)";
        final String str_pdfs = "解析招股说明书(PDF)_批量";
        final String str_img = "提取图片到本地硬盘";
        final String str_excel = "提取内容到excel";
        Action action = new AbstractAction("name") {
            final FileDialog pdfDialog = new FileDialog();
            public void actionPerformed(ActionEvent event) {
                String command = event.getActionCommand();
                System.out.println("action====" + command);
                switch (command){
                    case str_exit:
                        System.exit(0);
                        break;
                    case str_pdf:
                        openPdf();
                        break;
                    case str_pdfs:
                        pdfDialog.setVisible(true);
                        break;
                    case str_img:
                        WebDialog webDialog = new WebDialog(client, zhaoGuShuMain);
                        webDialog.setVisible(true);
                        break;
                    case str_excel:
                        ExcelDialog excelDialog = new ExcelDialog(client, zhaoGuShuMain);
                        excelDialog.setVisible(true);
                        break;
                }
            }
        };
        /////////////////////// 文件菜单
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);//关键一行。
        JMenuBar bar = new JMenuBar();

        JMenu fileMenu = new JMenu("文件");
        bar.add(fileMenu);
        JMenuItem openFileItem = new JMenuItem(str_exit);
        openFileItem.addActionListener(action);
        fileMenu.add(openFileItem);

        /////////////////////// 解析菜单
        JMenu parseMenu = new JMenu("提取");
        bar.add(parseMenu);


        JMenuItem zhaoguItem = new JMenuItem(str_pdf);
        zhaoguItem.addActionListener(action);
        parseMenu.add(zhaoguItem);

        JMenuItem selectParseItem = new JMenuItem(str_pdfs);
        selectParseItem.addActionListener(action);
        parseMenu.add(selectParseItem);

//        JMenuItem tableItem = new JMenuItem("提取所有表格");
//        parseMenu.add(tableItem);

        JMenuItem imageItem = new JMenuItem(str_img);
        imageItem.addActionListener(action);
        parseMenu.add(imageItem);

        JMenuItem excelItem = new JMenuItem(str_excel);
        excelItem.addActionListener(action);
        parseMenu.add(excelItem);

        this.setJMenuBar(bar);
    }

    private void initToolPanl(){
        JPanel panel = new JPanel(new GridBagLayout());
        JLabel label1 = new JLabel("文件");
        panel.add(label1, new GB(0,0,1,1).setInsets(5));

        addressField = new JTextField();
        addressField.setEnabled(false);
        panel.add(addressField, new GB(1,0,1,1).setInsets(5, 5, 5,0).setFill(GB.BOTH).setWeight(75,0));

        JButton selectButton = new JButton("选择");
        selectButton.setMargin(new Insets(2,2,2,2));
        panel.add(selectButton, new GB(2,0,1,1).setInsets(5, 5, 5, 0));
        selectButton.addActionListener(l->{
            openPdf();
        });

        panel.add(new JPanel(), new GB(3,0,1,1).setInsets(5, 5, 5, 0).setFill(GB.BOTH).setWeight(10,0));

        JTextField searchField = new JTextField();
        searchField.setToolTipText("请输入关键词");
        AddressMenu addressMenu = new AddressMenu(searchField);
        searchField.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton()==MouseEvent.BUTTON3) {
                    addressMenu.show(e.getComponent(), e.getX(),
                            e.getY());
                }
            }
        });
        panel.add(searchField, new GB(4,0,1,1).setInsets(5, 5, 5, 0).setFill(GB.BOTH).setWeight(15,0));

        JButton searchButton = new JButton("查找");
        searchButton.setMargin(new Insets(2,2,2,2));
        searchButton.addActionListener(l->browser.find(identifier_, searchField.getText(), true, false, true));
        panel.add(searchButton, new GB(5,0,1,1).setInsets(5, 5, 5, 0));

        JButton prevButton = new JButton("上一个");
        prevButton.setMargin(new Insets(2,2,2,2));
        panel.add(prevButton, new GB(6,0,1,1).setInsets(5, 5, 5, 0));
        prevButton.addActionListener(l-> browser.find(identifier_, searchField.getText(), false, false, true));

        JButton nextButton = new JButton("下一个");
        nextButton.setMargin(new Insets(2,2,2,2));
        panel.add(nextButton, new GB(7,0,1,1).setInsets(5, 5, 5, 5));
        nextButton.addActionListener(l-> browser.find(identifier_, searchField.getText(), true, false, true));

        JButton setting = new JButton("≡");

        setting.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton()==MouseEvent.BUTTON1) {
                    super.mouseClicked(e);
                    settingMenu.show(e.getComponent(), e.getX(),
                            e.getY());
                }
            }
        });
        panel.add(setting, new GB(8, 0, 1,1));

        getContentPane().add(panel, BorderLayout.NORTH);
    }

    private void openPdf(){
        JFileChooser fileChooser = new JFileChooser("");
//            System.out.println("fileChooser＝===" + initFilePath);
        fileChooser.setSelectedFile(initFilePath);
        FileNameExtensionFilter filter = new FileNameExtensionFilter( "TXT files (*.pdf)", "pdf", "PDF");
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(null);
        if(result == JFileChooser.APPROVE_OPTION){
            File emptyHtml = new File("data/thymeleaf/empty.html");
            browser.loadURL(emptyHtml.getAbsolutePath());
            File selectFile = fileChooser.getSelectedFile();
            initFilePath = selectFile;
            addressField.setText(selectFile.getAbsolutePath());
            savePdfPath();
            new Thread(() -> {
//                ZhaoGuShuMain zhaoGuShuMain = new ZhaoGuShuMain();
                String file = zhaoGuShuMain.run(selectFile);
                log.info("zhaoGuShuMain.run(selectFile)======" + file);
                SwingUtilities.invokeLater(() -> browser.loadURL(file));
            }).start();
        }
    }

    public void initWebView(){
        ////////////////////////////////////////
        CefAppBuilder builder = new CefAppBuilder();
        builder.getCefSettings().windowless_rendering_enabled = false;
        builder.setAppHandler(new MavenCefAppHandlerAdapter() {
            public void stateHasChanged(CefApp.CefAppState state) {
                if (state == CefApp.CefAppState.TERMINATED) {
                    System.exit(0);
                }
            }
        });
        try{
            cefApp_ = builder.build();
            CefApp.CefVersion version = cefApp_.getVersion();
            System.out.println(version);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        client = cefApp_.createClient();
        CefMessageRouter msgRouter = CefMessageRouter.create();
        client.addMessageRouter(msgRouter);
        client.addContextMenuHandler(new ContextMenuHandler(DesktopGUI.this));
        // https://sfccn.com/
        browser = client.createBrowser("", false, false);

        Component browerUI_ = browser.getUIComponent();
        getContentPane().add(browerUI_, BorderLayout.CENTER);
//        client_.addDisplayHandler(new CefDisplayHandlerAdapter() {
//            @Override
//            public void onAddressChange(CefBrowser browser, CefFrame frame, String url) {
//            }
//        });

        // Clear focus from the address field when the browser gains focus.
        client.addFocusHandler(new CefFocusHandlerAdapter() {
            @Override
            public void onGotFocus(CefBrowser browser) {
                if (browserFocus_)
                    return;
                browserFocus_ = true;
                KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
                browser.setFocus(true);
            }

            @Override
            public void onTakeFocus(CefBrowser browser, boolean next) {
                browserFocus_ = false;
            }
        });
        settingMenu = new SettingMenu(DesktopGUI.this, browser);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                CefApp.getInstance().dispose();
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        java.util.logging.Logger log= java.util.logging.Logger.getLogger(ConsoleProgressHandler.class.getName());
        log.setLevel(java.util.logging.Level.WARNING);
        try {
            UIManager
                    .setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            new DesktopGUI();
//            SwingUtilities.invokeLater(() -> new DesktopGUI());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class FileDialog extends JDialog {
        private final JTextField pdfField;
        private JTextField htmlField;
        public FileDialog() {
            initPdfPath();
            this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            this.setLocationRelativeTo(null);
            this.setTitle("选择PDF所在目录_批量提取");
            this.setModal(true);
            this.setSize(500,200);
            Container contentPane = this.getContentPane();
            contentPane.setLayout(new GridBagLayout());
            ///////////////一行
            JLabel label1 = new JLabel("选择目录");
            contentPane.add(label1, new GB(0,0,1,1).setInsets(0,5,0,0));
            pdfField = new JTextField();
            contentPane.add(pdfField, new GB(1,0,1,1).setFill(GB.BOTH).setWeight(100,0).setInsets(0,5,0,5));

            JButton pdfButton1 = new JButton("选择");
            pdfButton1.addActionListener(l->{
                JFileChooser fileChooser = new JFileChooser("");
                fileChooser.setApproveButtonText("选择");
                fileChooser.setSelectedFile(initFilePath);
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fileChooser.showOpenDialog(null);
                if(result == JFileChooser.APPROVE_OPTION){
                    initFilePath = fileChooser.getSelectedFile();
                    pdfField.setText(initFilePath.getAbsolutePath());
                    htmlField.setText(initFilePath.getAbsolutePath());
                }
            });
            contentPane.add(pdfButton1, new GB(2,0,1,1).setInsets(0,0,0,5));
            ///////////////二行
            JLabel label2 = new JLabel("生成目录");
            contentPane.add(label2, new GB(0,1,1,1).setInsets(5,5,0,0));
            htmlField = new JTextField();
            contentPane.add(htmlField, new GB(1,1,1,1).setFill(GB.BOTH).setWeight(100,0).setInsets(5,5,0,5));

            JButton pdfButton2 = new JButton("选择");
            pdfButton2.addActionListener(l->{
                JFileChooser fileChooser = new JFileChooser("");
                fileChooser.setApproveButtonText("选择");
                fileChooser.setSelectedFile(initFilePath);
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fileChooser.showOpenDialog(null);
                if(result == JFileChooser.APPROVE_OPTION){
                    initFilePath = fileChooser.getSelectedFile();
                    htmlField.setText(initFilePath.getAbsolutePath());
                }
            });
            contentPane.add(pdfButton2, new GB(2,1,1,1).setInsets(5,0,0,5));

            /////////////////////////////////////
            JLabel label3 = new JLabel("生成中..");
            label3.setVisible(false);
            contentPane.add(label3, new GB(2,1,1,1).setInsets(5,5,0,5).setAnchor(GB.WEST));


            JButton parseButton = new JButton("提取");
            parseButton.addActionListener(l->{
                label3.setVisible(true);
                new Thread(() -> {
                    String path = pdfField.getText();
                    File pdfPath = new File(path);
                    File[] pdfs = pdfPath.listFiles();
                    if(pdfs != null && pdfs.length > 0){
//                        ZhaoGuShuMain zhaoGuShuMain = new ZhaoGuShuMain();
                        for(File pdf:pdfs){
                            if(pdf.getName().toLowerCase().endsWith(".pdf")){
                                log.info("正在解析:" + pdf.getAbsolutePath());
                                String file = zhaoGuShuMain.run(pdf);
                                SwingUtilities.invokeLater(() -> {
                                    htmlField.setText(file);
                                    label3.setVisible(false);
                                }
                                );
                            }
                        }
                    }
                }).start();
            });
//            JPanel bottomPanel = new JPanel(new GridBagLayout());
//            bottomPanel.add(parseButton, new GB(2,0,1,1).setInsets(0,0,0,10));

            contentPane.add(parseButton, new GB(2,2,1,1).setInsets(50,0,0,5));

//            JButton openButton = new JButton("打开文件");
//            openButton.addActionListener(l->{
//                browser_.loadURL(htmlField.getText());
//                FileDialog.this.setVisible(false);
//            });
//            bottomPanel.add(openButton, new GB(1,0,1,1).setInsets(0,10,0,0));

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

}
