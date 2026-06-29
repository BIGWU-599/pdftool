package com.sfccn.pdftool.ui;

import org.cef.browser.CefBrowser;

import javax.swing.*;

public class ScriptDialog extends CodeBaseDialog {
    private JTextArea codeText = null;
    public ScriptDialog(CefBrowser browser){ // CefBrowser browser
        super();
        this.browser = browser;
    }


    /**
     * 浏览器缓存路径
     * @return
     */
    protected String getTempPath(){
        return "logs\\Chrome\\toutiao";
    }

    /**
     * 默认首页
     * @return
     */
    protected String getBaseUrl(){
        return "";
    }

    /**
     *
     * @return
     */
    protected String getAppTitle(){
        return "执行脚本";
    }

    protected String getCodeFile(){
        return "";
    }
    private void test(){
//        browser.loadURL();
    }
}
