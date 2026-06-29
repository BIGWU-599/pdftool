package com.sfccn.pdftool.webview.cef;


import com.sfccn.pdftool.client.DesktopPdf;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.handler.CefLifeSpanHandlerAdapter;

public class LifeSpanHandler extends CefLifeSpanHandlerAdapter {
    private DesktopPdf cefFrame = null;

    public LifeSpanHandler(DesktopPdf cefFrame){
        this.cefFrame = cefFrame;
    }
    @Override
    public void onAfterCreated(CefBrowser browser) {
        //System.out.println("BrowserFrame.onAfterCreated id=" + browser.getIdentifier());
//        browserCount_++;
    }

    @Override
    public void onAfterParentChanged(CefBrowser browser) {
        System.out.println(
                "BrowserFrame.onAfterParentChanged id=" + browser.getIdentifier());
//        if (afterParentChangedAction_ != null) {
//            SwingUtilities.invokeLater(afterParentChangedAction_);
//            afterParentChangedAction_ = null;
//        }
    }

    @Override
    public boolean doClose(CefBrowser browser) {
        boolean result = browser.doClose();
        System.out.println("BrowserFrame.doClose id=" + browser.getIdentifier()
                + " CefBrowser.doClose=" + result);
        return result;
    }

    @Override
    public void onBeforeClose(CefBrowser browser) {
        System.out.println("BrowserFrame.onBeforeClose id=" + browser.getIdentifier());
//        if (--browserCount_ == 0) {
//            System.out.println("BrowserFrame.onBeforeClose CefApp.dispose");
//            CefApp.getInstance().dispose();
//        }
    }

//    @Override
    public boolean onBeforePopup(CefBrowser browser, CefFrame frame, String target_url, String target_frame_name) {
        cefFrame.addBrowserTab(target_url);
        return true;
    }
}
