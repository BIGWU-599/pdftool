package com.sfccn.pdftool.webview;


import com.sfccn.pdftool.util.GB;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefStringVisitor;
import org.cef.handler.CefAppHandlerAdapter;
import org.cef.handler.CefDisplayHandlerAdapter;
import org.cef.handler.CefLoadHandlerAdapter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CefDriver extends JPanel {
    private static CefApp cefApp_ = null;
    private CefClient client_;
    private CefBrowser browser_;
    private Component browerUI_;
    private JTextField urlField;
    private JPopupMenu popupMenu;
    private CefLoadHandlerAdapter cefLoadHandlerAdapter;
    private CefDisplayHandlerAdapter cefDisplayHandlerAdapter;
    private String htmlSource = "";
    public CefDriver(){
        this(null, null);
    }
    public CefDriver(CefLoadHandlerAdapter cefLoadHandlerAdapter, CefDisplayHandlerAdapter cefDisplayHandlerAdapter){
        this.cefLoadHandlerAdapter = cefLoadHandlerAdapter != null ? cefLoadHandlerAdapter : null;
        this.cefDisplayHandlerAdapter = cefDisplayHandlerAdapter != null ? cefDisplayHandlerAdapter : null;
        this.setLayout(new GridBagLayout());
        initBack();
        initForward();
        initReload();
        initUrlField();
        initGo();
        initSetting();
        initPopupMenu();
        initCef();
        this.add(browerUI_, new GB(0, 1, 6,1).setFill(GB.BOTH).setWeight(100, 100));
    }
    private void initBack(){
        JButton back = new JButton("←");
        back.addActionListener(e->{
            browser_.goBack();
        });
        this.add(back, new GB(0, 0, 1,1));

    }
    private void initForward(){
        JButton forward = new JButton("→");
        forward.addActionListener(e->{
            browser_.goForward();
        });
        this.add(forward, new GB(1, 0, 1,1));
    }
    private void initReload(){
        JButton reload = new JButton("⊂");
        reload.addActionListener(e->{
            browser_.reload();
        });
        this.add(reload, new GB(2, 0, 1,1));
    }
    private void initUrlField(){
        urlField = new JTextField();
        urlField.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyChar()==KeyEvent.VK_ENTER )   //按回车键执行相应操作;
                {
                    browser_.loadURL(urlField.getText());
                }
            }
        });
        this.add(urlField, new GB(3, 0, 1,1).setFill(GB.BOTH).setWeight(100, 0));

    }
    private void initGo(){
        JButton go = new JButton("go");
        go.addActionListener(new  ActionListener(){
            public void actionPerformed(ActionEvent e){
                browser_.loadURL(urlField.getText());
            }});
        this.add(go, new GB(4, 0, 1,1));
    }
    private void initSetting(){
        JButton setting = new JButton("≡");
        setting.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                popupMenu.show(e.getComponent(), e.getX(),
                        e.getY());
            }
        });
        this.add(setting, new GB(5, 0, 1,1));
    }
    private void initPopupMenu(){
        popupMenu = new JPopupMenu();
        JMenuItem item = new JMenuItem("查看源码");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //browser_.viewSource();
            }
        });
        popupMenu.add(item);
    }
    private void initCef(){
        if (CefApp.getState() != CefApp.CefAppState.INITIALIZED) {
            CefSettings settings = new CefSettings();
            settings.windowless_rendering_enabled = false;
            cefApp_ = CefApp.getInstance(new String[]{
            }, settings);

            CefApp.CefVersion version = cefApp_.getVersion();
            System.out.println("===version:" + version);
            CefApp.addAppHandler(new CefAppHandlerAdapter(null) {
                @Override
                public void stateHasChanged(CefApp.CefAppState state) {
                    if (state == CefApp.CefAppState.TERMINATED) System.exit(0);
                }
            });
        } else {
            cefApp_ = CefApp.getInstance();
        }

        client_ = cefApp_.createClient();
        browser_ = client_.createBrowser("", false, false);
        browerUI_ = browser_.getUIComponent();

        client_.addLoadHandler(new WebCefLoadHandlerAdapter());
        client_.addDisplayHandler(new WebCefDisplayHandlerAdapter());

    }
    class WebCefLoadHandlerAdapter extends CefLoadHandlerAdapter{
        @Override
        public void onLoadingStateChange(CefBrowser browser, boolean isLoading,
        boolean canGoBack, boolean canGoForward) {
            if(cefLoadHandlerAdapter != null){
                cefLoadHandlerAdapter.onLoadingStateChange(browser, isLoading, canGoBack, canGoForward);
            }
        }
        @Override
        public void onLoadError(CefBrowser browser, CefFrame frame, ErrorCode errorCode,
                String errorText, String failedUrl) {
//                if (errorCode != ErrorCode.ERR_NONE && errorCode != ErrorCode.ERR_ABORTED) {
//                    browser.stopLoad();
//                }
            if(cefLoadHandlerAdapter != null){
                cefLoadHandlerAdapter.onLoadError(browser, frame, errorCode, errorText, failedUrl);
            }
        }
        public void onLoadEnd(CefBrowser browser, CefFrame frame, int httpStatusCode) {
            browser.getSource(new CefStringVisitor(){
                public void visit(String source){
                    htmlSource = source;
                    //System.out.println("onLoadEnd=============html\n" + var1);
                }
            });
            if(cefLoadHandlerAdapter != null){
                cefLoadHandlerAdapter.onLoadEnd(browser, frame, httpStatusCode);
            }
        }
    }
    class WebCefDisplayHandlerAdapter extends CefDisplayHandlerAdapter{
        @Override
        public void onAddressChange(CefBrowser browser, CefFrame frame, String url) {
            htmlSource = "";
            urlField.setText(url);
            if(cefDisplayHandlerAdapter != null){
                cefDisplayHandlerAdapter.onAddressChange(browser, frame, url);
            }
        }
        @Override
        public void onTitleChange(CefBrowser browser, String title) {
            if(cefDisplayHandlerAdapter != null){
                cefDisplayHandlerAdapter.onTitleChange(browser, title);
            }
        }
        @Override
        public void onStatusMessage(CefBrowser browser, String value) {
            //System.out.println("onTitleChange======" + value);
            if(cefDisplayHandlerAdapter != null){
                cefDisplayHandlerAdapter.onStatusMessage(browser, value);
            }
        }
    }
    public void addLoadHandler(CefLoadHandlerAdapter adapter){
        this.cefLoadHandlerAdapter = adapter;

    }
    public void addDisplayHandler(CefDisplayHandlerAdapter adapter){
        this.cefDisplayHandlerAdapter = adapter;
    }

    public void dispose(){
        CefApp.getInstance().dispose();
    }
    public void get(String url){
        browser_.loadURL(url);
    }
    public String getHtmlSource(){
        return htmlSource;
    }
    public void getUrl(){
        browser_.getURL();
    }
    public Elements findElements(String cssSelector){
        int time = 60;
        Elements elements = null;
        try{
            while(time > 0){
                Document  document = Jsoup.parse(htmlSource);
                elements = document.select(cssSelector);
                if(elements != null && elements.size() > 0){
                    break;
                }
                time--;
                Thread.sleep(500);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return elements;
    }
    public Element findElement(String cssSelector){
        int time = 60;
        Element element = null;
        try{
            while(time > 0){
                Document  document = Jsoup.parse(htmlSource);
                element = document.selectFirst(cssSelector);
                if(element != null){
                    break;
                }
                time--;
                Thread.sleep(500);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return element;
    }


}
