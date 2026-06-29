package com.sfccn.pdftool.webview;


import com.sfccn.pdftool.client.DesktopPdf;
import com.sfccn.pdftool.util.GB;
import com.sfccn.pdftool.webview.cef.*;
import com.sfccn.pdftool.webview.selenium.CefWebElement;
import me.friwi.jcefmaven.CefAppBuilder;
import me.friwi.jcefmaven.MavenCefAppHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.browser.CefMessageRouter;
import org.cef.callback.CefQueryCallback;
import org.cef.handler.CefDisplayHandlerAdapter;
import org.cef.handler.CefFocusHandlerAdapter;
import org.cef.handler.CefLoadHandlerAdapter;
import org.cef.handler.CefMessageRouterHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import tests.detailed.util.DataUri;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URLDecoder;
import java.util.List;
import java.util.*;

public class WebViewCef extends JPanel implements WebDriver, JavascriptExecutor {
    public final Logger log = LogManager.getLogger(WebViewCef.class);
    private static CefApp cefApp_ = null;
    private static CefClient client_ = null;
    public CefBrowser browser_ = null;
    private Component browerUI_ = null;
    private boolean browserFocus_ = true;
    private JTextField urlField = null;
    private ClosableTabbedPane tabbedPane = null;
    private int tabIndex = -1;
    private String errorMsg_ = "";
    private DesktopPdf cefFrame = null;
    private MenuBarCef menuBar = null;
    private StatusPanel statusPanel = null;
    private String defaultUrl = "";
    private StringBuilder htmlSource = new StringBuilder();
    private String title = "";
    private static Map<Integer, Integer> identifierMap = null;
    static{
        //System.out.println("addLibraryDir===");
//        NativeLoader.addLibraryDir("libs/win64");
        identifierMap = new HashMap<>();
    }
    public WebViewCef(DesktopPdf cefFrame, ClosableTabbedPane tabbedPane, int index, String url){
        this.tabbedPane = tabbedPane;
        this.cefFrame = cefFrame;
        this.defaultUrl = url;
        this.tabIndex = index;
        System.out.println("WebViewCef=====tabIndex:" + tabIndex);
        this.setLayout(new GridBagLayout());
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        initBack();
        initForward();
        initReload();
        initUrlField();
        initGo();
        initSetting();
        initCef();
        initPopupMenu();
        this.add(browerUI_, new GB(0, 1, 6,1).setFill(GB.BOTH).setWeight(100, 100));
        statusPanel = new StatusPanel();
        this.add(statusPanel, new GB(0, 2, 6,1).setFill(GB.BOTH).setWeight(100, 0));
    }


    private void initBack(){
        JButton back = new JButton("\uD83E\uDC68"); // 🡸 ←  🡰   🡨
        back.addActionListener(e->{
            browser_.goBack();
        });
        this.add(back, new GB(0, 0, 1,1));

    }
    private void initForward(){
        JButton forward = new JButton("\uD83E\uDC6A");  // → 🠊 🡪
        forward.addActionListener(e->{
            browser_.goForward();
        });
        this.add(forward, new GB(1, 0, 1,1));
    }
    private void initReload(){
        JButton reload = new JButton("\uD83D\uDDD8");
        reload.addActionListener(e->{
            browser_.reload();
        });
        this.add(reload, new GB(2, 0, 1,1));
    }
    private void initUrlField(){
        urlField = new JTextField(defaultUrl);
        urlField.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyChar()==KeyEvent.VK_ENTER )   //按回车键执行相应操作;
                {
                    browser_.loadURL(urlField.getText());
                }
            }
        });

        urlField.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton()==MouseEvent.BUTTON3) {
                    menuBar.addressMenu.show(e.getComponent(), e.getX(),
                            e.getY());
                }
            }
        });
        this.add(urlField, new GB(3, 0, 1,1).setFill(GB.BOTH).setWeight(100, 0));

    }
    private void initGo(){
        JButton go = new JButton("\u21b2");  //go ↲
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
                if (e.getButton()==MouseEvent.BUTTON1) {
                    super.mouseClicked(e);
                    menuBar.settingMenu.show(e.getComponent(), e.getX(),
                            e.getY());
                }
            }
        });
        this.add(setting, new GB(5, 0, 1,1));
    }
    private void initPopupMenu(){
        menuBar = new MenuBarCef(cefFrame, WebViewCef.this);
        menuBar.initSettingMenu();
        menuBar.initAddressMenu(urlField);
    }
    private void initCef(){
        if (CefApp.getState() != CefApp.CefAppState.INITIALIZED) {
//            CefSettings settings = new CefSettings();
//            File cachePath = new File("logs/cef");
//            settings.cache_path = cachePath.getAbsolutePath();
//            settings.windowless_rendering_enabled = false;
//            cefApp_ = CefApp.getInstance(new String[]{
//            }, settings);
            CefAppBuilder builder = new CefAppBuilder();
            File cachePath = new File("logs/cef");
            builder.getCefSettings().cache_path = cachePath.getAbsolutePath();
            builder.getCefSettings().windowless_rendering_enabled = false;
            builder.setAppHandler(new MavenCefAppHandlerAdapter() {
                @Override
                public void stateHasChanged(CefApp.CefAppState state) {
                    // Shutdown the app if the native CEF part is terminated
                    if (state == CefApp.CefAppState.TERMINATED) System.exit(0);
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
//            CefApp.CefVersion version = cefApp_.getVersion();
//            System.out.println("===version:" + version);
//            CefApp.addAppHandler(new CefAppHandlerAdapter(null) {
//                @Override
//                public void stateHasChanged(CefApp.CefAppState state) {
//                    if (state == CefApp.CefAppState.TERMINATED) System.exit(0);
//                }
////                public void onContextInitialized() {
////                }
//            });
        } else {
            cefApp_ = CefApp.getInstance();
        }
        if(client_ == null){
            client_ = cefApp_.createClient();
            CefMessageRouter msgRouter = CefMessageRouter.create();
            client_.addMessageRouter(msgRouter);
            client_.addContextMenuHandler(new ContextMenuHandler(cefFrame));
            client_.addDownloadHandler(new DownloadDialog(cefFrame));
            client_.addDragHandler(new DragHandler());
            client_.addJSDialogHandler(new JSDialogHandler());
            client_.addKeyboardHandler(new KeyboardHandler());
            client_.addRequestHandler(new RequestHandler(cefFrame));
            client_.addLifeSpanHandler(new LifeSpanHandler(cefFrame));

            client_.addFocusHandler(new CefFocusHandlerAdapter() {
                @Override
                public void onGotFocus(CefBrowser browser) {
                    if (browserFocus_) return;
                    browserFocus_ = true;
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
                    browser.setFocus(true);
                }

                @Override
                public void onTakeFocus(CefBrowser browser, boolean next) {
                    browserFocus_ = false;
                }
            });
            client_.addDisplayHandler(new CefDisplayHandlerAdapter() {
                @Override
                public void onAddressChange(CefBrowser browser, CefFrame frame, String url) {
                    if(url.indexOf("%") > -1){
                        try {
                            url = URLDecoder.decode(url, "UTF-8");
                        }
                        catch(Exception e){
                        }
                    }
                    urlField.setText(url);
                }
                @Override
                public void onTitleChange(CefBrowser browser, String title) {
                    title = title.length() < 10 ? title : title.substring(0, 10);
                    Integer index = identifierMap.get(browser.hashCode());
                    //System.out.println("onTitleChange=====" + identifierMap + ", index:" + index + ", browser.hashCode:" + browser.hashCode());
                    if(index != null){
                        tabbedPane.setTitleAt(index, title);
                        Component component = tabbedPane.getComponent(index);
                        if(component instanceof WebViewCef){
                            ((WebViewCef)component).title = title;
                        }
                    }
                }
                @Override
                public void onStatusMessage(CefBrowser browser, String value) {
                    statusPanel.setStatusText(value);
                }
            });

            client_.addLoadHandler(new MyCefLoadHandlerAdapter());
        }
        urlField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (!browserFocus_) return;
                browserFocus_ = false;
                KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
                urlField.requestFocus();
            }
        });
        browser_ = client_.createBrowser(defaultUrl, false, false);
        browerUI_ = browser_.getUIComponent();
        identifierMap.put(browser_.hashCode(), tabIndex);
    }

    class MyCefLoadHandlerAdapter extends CefLoadHandlerAdapter {
        @Override
        public void onLoadingStateChange(CefBrowser browser, boolean isLoading,
        boolean canGoBack, boolean canGoForward) {
            statusPanel.setIsInProgress(isLoading);
            if (!isLoading && !errorMsg_.isEmpty()) {
                browser.loadURL(DataUri.create("text/html", errorMsg_));
                errorMsg_ = "";

            }
        }

        @Override
        public void onLoadError(CefBrowser browser, CefFrame frame, ErrorCode errorCode,
                String errorText, String failedUrl) {
            if (errorCode != ErrorCode.ERR_NONE && errorCode != ErrorCode.ERR_ABORTED) {
                errorMsg_ = "<html><head>";
                errorMsg_ += "<title>Error while loading</title>";
                errorMsg_ += "</head><body>";
                errorMsg_ += "<h1>" + errorCode + "</h1>";
                errorMsg_ += "<h3>Failed to load " + failedUrl + "</h3>";
                errorMsg_ += "<p>" + (errorText == null ? "" : errorText) + "</p>";
                errorMsg_ += "</body></html>";
                browser.stopLoad();
            }
        }

        public void onLoadEnd(CefBrowser browser, CefFrame frame, int httpStatusCode) {
            browser.getSource(html -> {
                Integer index = identifierMap.get(browser.hashCode());
//                System.out.println("onLoadEnd=============index:" + index + ",hashCode=" + browser.hashCode() + ",url=" + browser.getURL());
                if(index != null){
                    Component component = tabbedPane.getComponent(index);
                    if(component instanceof WebViewCef){
//                        System.out.println("onLoadEnd=============html:" + html.length());
                        WebViewCef webViewCef = (WebViewCef)component;
                        webViewCef.setHtmlSource(html);
//                        System.out.println("onLoadEnd=============PageSource:" + webViewCef.getPageSource().length() + ",hashCode:" + webViewCef.hashCode());
                    }
                }
            });
        }
    }
    /**
     * 添加js交互
     */
    public void jsActive(CefClient client) {
        //配置一个查询路由,html页面可使用 window.java({}) 和 window.javaCancel({}) 来调用此方法
        CefMessageRouter.CefMessageRouterConfig cmrc=new CefMessageRouter.CefMessageRouterConfig("java","javaCancel");
        //创建查询路由
        CefMessageRouter cmr=CefMessageRouter.create(cmrc);
        cmr.addHandler(new CefMessageRouterHandler() {

            @Override
            public void setNativeRef(String str, long val) {
                System.out.println(str+"  "+val);
            }

            @Override
            public long getNativeRef(String str) {
                System.out.println(str);
                return 0;
            }

            @Override
            public void onQueryCanceled(CefBrowser browser, CefFrame frame, long query_id) {
                System.out.println("取消查询:"+query_id);
            }

            @Override
            public boolean onQuery(CefBrowser browser, CefFrame frame, long query_id, String request, boolean persistent,
                                   CefQueryCallback callback) {
                System.out.println("request:"+request+"\nquery_id:"+query_id+"\npersistent:"+persistent);

                callback.success("Java后台处理了数据");
                return true;
            }
        }, true);
        client.addMessageRouter(cmr);
    }

    @Override
    public void get(String s) {
        browser_.loadURL(s);
    }

    @Override
    public String getCurrentUrl() {
        return browser_.getURL();
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public List<WebElement> findElements(By by) {
        String selector = by.toString().split(":")[1].trim();
        Document document = Jsoup.parse(htmlSource.toString());
        Elements elements = document.select(selector);
        List<WebElement> list = new ArrayList<>();
        for(Element e:elements){
            list.add(new CefWebElement(e, this));
        }
        return list;
    }

    @Override
    public WebElement findElement(By by) {
        String selector = by.toString().split(":")[1].trim();
        Document document = Jsoup.parse(htmlSource.toString());
        Element element = document.selectFirst(selector);
        return new CefWebElement(element, this);
    }

    @Override
    public String getPageSource() {
        return this.htmlSource.toString();
    }

    @Override
    public void close() {

    }

    @Override
    public void quit() {

    }

    @Override
    public Set<String> getWindowHandles() {
        return null;
    }

    @Override
    public String getWindowHandle() {
        return null;
    }

    @Override
    public WebDriver.TargetLocator switchTo() {
        return null;
    }

    @Override
    public Navigation navigate() {
        return null;
    }

    public Options manage() {
        return null;
    }

    public void setHtmlSource(String htmlSource) {
        this.htmlSource.delete(0, this.htmlSource.length());
        this.htmlSource.append(htmlSource);
    }

    public void setTitle(String title) {
        this.title = title;
    }


    /**
     * 返回值为null
     */
    @Override
    public Object executeScript(String s, Object... objects) {
        browser_.executeJavaScript(s, "", 0);
        return null;
    }

    /**
     * 返回值为null
     */
    public Object executeAsyncScript(String s, Object... objects) {
        browser_.executeJavaScript(s, "", 0);
        return null;
    }
}
