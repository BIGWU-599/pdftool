// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license that
// can be found in the LICENSE file.

package com.sfccn.pdftool.webview.cef;

import org.apache.commons.lang3.StringUtils;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefAuthCallback;
import org.cef.callback.CefRequestCallback;
import org.cef.handler.CefLoadHandler.ErrorCode;
import org.cef.handler.CefRequestHandler;
import org.cef.handler.CefResourceHandler;
import org.cef.handler.CefResourceRequestHandler;
import org.cef.handler.CefResourceRequestHandlerAdapter;
import org.cef.misc.BoolRef;
import org.cef.network.CefRequest;
import org.cef.network.CefResponse;
import org.cef.network.CefURLRequest;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class RequestHandler extends CefResourceRequestHandlerAdapter implements CefRequestHandler {
    private final Frame owner_;

    public RequestHandler(Frame owner) {
        owner_ = owner;
    }
    public void onResourceLoadComplete(CefBrowser browser, CefFrame frame, CefRequest request, CefResponse response, CefURLRequest.Status status, long var6) {
        String url = request.getURL();
        String code = readTxt("monitor.js");

        if(StringUtils.isEmpty(code) == false){
            runcode(request, code);
        }

//        System.out.println("RequestHandler onResourceLoadComplete===" + url);
//        if(url.indexOf("https://my.oschina.net/penngo/radar/getUserPortraitRadarMap") > -1 ){
//            System.out.println("获取内容:"  + url);
//            new NMoneyCefURLRequestClient(request);
//        }

    }
    @Override
    public boolean onBeforeBrowse(CefBrowser browser, CefFrame frame, CefRequest request,
                                  boolean user_gesture, boolean is_redirect) {
        //System.out.println("onBeforeBrowse=====request.getURL:"  + request.getURL());
//        CefPostData postData = request.getPostData();
//        if (postData != null) {
//            Vector<CefPostDataElement> elements = new Vector<CefPostDataElement>();
//            postData.getElements(elements);
//            for (CefPostDataElement el : elements) {
//                int numBytes = el.getBytesCount();
//                if (numBytes <= 0) continue;
//
//                byte[] readBytes = new byte[numBytes];
//                if (el.getBytes(numBytes, readBytes) <= 0) continue;
//
//                String readString = new String(readBytes);
//                if (readString.indexOf("ignore") > -1) {
//                    SwingUtilities.invokeLater(new Runnable() {
//                        @Override
//                        public void run() {
//                            JOptionPane.showMessageDialog(owner_,
//                                    "The request was rejected because you've entered \"ignore\" into the form.");
//                        }
//                    });
//                    return true;
//                }
//            }
//        }
//        System.out.println("onBeforeBrowse====" + request.getURL());
//                                  //https://static.oschina.net/uploads/user/58/117179_200.gif?t=1380194723000
//        if(request.getURL().equals("https://static.oschina.net/uploads/user/58/117179_200.gif?t=1380194723000")){
//            System.out.println("======url exist");
//            return true;
//        }
        return false;
//        return true;
    }

    @Override
    public CefResourceRequestHandler getResourceRequestHandler(CefBrowser browser, CefFrame frame,
                                                               CefRequest request, boolean isNavigation, boolean isDownload, String requestInitiator,
                                                               BoolRef disableDefaultHandling) {
        return this;
    }

    @Override
    public boolean onBeforeResourceLoad(CefBrowser browser, CefFrame frame, CefRequest request) {
//        System.out.println("onBeforeResourceLoad====" + request.getURL());
//        if (request.getMethod().equalsIgnoreCase("POST")
//                && request.getURL().equals("http://www.google.com/")) {
//            String forwardTo = "http://www.google.com/#q=";
//            CefPostData postData = request.getPostData();
//            boolean sendAsGet = false;
//            if (postData != null) {
//                Vector<CefPostDataElement> elements = new Vector<CefPostDataElement>();
//                postData.getElements(elements);
//                for (CefPostDataElement el : elements) {
//                    int numBytes = el.getBytesCount();
//                    if (numBytes <= 0) continue;
//
//                    byte[] readBytes = new byte[numBytes];
//                    if (el.getBytes(numBytes, readBytes) <= 0) continue;
//
//                    String readString = new String(readBytes).trim();
//                    String[] stringPairs = readString.split("&");
//                    for (String s : stringPairs) {
//                        int startPos = s.indexOf('=');
//                        if (s.startsWith("searchFor"))
//                            forwardTo += s.substring(startPos + 1);
//                        else if (s.startsWith("sendAsGet")) {
//                            sendAsGet = true;
//                        }
//                    }
//                }
//                if (sendAsGet) postData.removeElements();
//            }
//            if (sendAsGet) {
//                request.setFlags(0);
//                request.setMethod("GET");
//                request.setURL(forwardTo);
//                request.setFirstPartyForCookies(forwardTo);
//                HashMap<String, String> headerMap = new HashMap<>();
//                request.getHeaderMap(headerMap);
//                headerMap.remove("Content-Type");
//                headerMap.remove("Origin");
//                request.setHeaderMap(headerMap);
//            }
//        }
//        if(request.getURL().equals("https://static.oschina.net/uploads/user/58/117179_200.gif?t=1380194723000")){
//            System.out.println("======url exist");
//            return true;
//        }
        String code = readTxt("filter.js");
        if(code != null){
            return runFilter(request, code);
        }
        else{
            return false;
        }
    }

    @Override
    public CefResourceHandler getResourceHandler(
            CefBrowser browser, CefFrame frame, CefRequest request) {
//        if(request.getURL().indexOf("http://push2.eastmoney.com/api/qt/kamt.rtmin/get") > -1){
//            return new ResourceEastmoneyHandle();
//        }
        return null;
    }

    @Override
    public boolean getAuthCredentials(CefBrowser browser, String origin_url, boolean isProxy,
                                      String host, int port, String realm, String scheme, CefAuthCallback callback) {
        SwingUtilities.invokeLater(new PasswordDialog(owner_, callback));
        return true;
    }

    @Override
    public boolean onQuotaRequest(
            CefBrowser browser, String origin_url, long new_size, CefRequestCallback callback) {
        return false;
    }

    @Override
    public boolean onCertificateError(CefBrowser browser, ErrorCode cert_error, String request_url,
                                      CefRequestCallback callback) {
        SwingUtilities.invokeLater(new CertErrorDialog(owner_, cert_error, request_url, callback));
        return true;
    }

    @Override
    public void onPluginCrashed(CefBrowser browser, String pluginPath) {
        System.out.println("Plugin " + pluginPath + "CRASHED");
    }

    @Override
    public void onRenderProcessTerminated(CefBrowser browser, TerminationStatus status) {
        System.out.println("render process terminated: " + status);
    }
    public boolean onOpenURLFromTab(CefBrowser var1, CefFrame var2, String var3, boolean var4){
        return true;
    }

    public void runcode(CefRequest request, String code){
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        try{
            engine.put("request", request);
            engine.put("CefURLRequestClientAdapter", CefURLRequestClientAdapter.class);
            engine.put("CefURLRequest", CefURLRequest.class);
            // 执行这个脚本
            engine.eval(code);
        }
        catch(Exception ex){
            ex.printStackTrace();
//            log.error("脚本执行出错======", ex);
        }
    }
    public boolean runFilter(CefRequest request, String code){
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        try{
            engine.put("request", request);
            // 执行这个脚本
            engine.eval(code);
            Invocable invocable = (Invocable) engine;

            Object result = invocable.invokeFunction("filter", request);
            return (boolean)result;
        }
        catch(Exception ex){
            ex.printStackTrace();
//            log.error("脚本执行出错======", ex);
        }
        return false;
    }
    public String readTxt(String txtPath) {
        File file = new File(txtPath);
        if(file.isFile() && file.exists()){
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuffer sb = new StringBuffer();
                String text = null;
                while((text = bufferedReader.readLine()) != null){
                    sb.append(text);
                }
                return sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
