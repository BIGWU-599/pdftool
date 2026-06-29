package com.sfccn.pdftool.webview.cef;

import org.cef.callback.CefAuthCallback;
import org.cef.callback.CefURLRequestClient;
import org.cef.network.CefURLRequest;

import java.io.ByteArrayOutputStream;

public abstract class CefURLRequestClientAdapter implements CefURLRequestClient {
    public ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
    @Override
    public void onRequestComplete(CefURLRequest cefURLRequest) {

    }

    @Override
    public void onUploadProgress(CefURLRequest cefURLRequest, int i, int i1) {

    }

    @Override
    public void onDownloadProgress(CefURLRequest cefURLRequest, int i, int i1) {

    }

    @Override
    public void onDownloadData(CefURLRequest cefURLRequest, byte[] bytes, int i) {
        byteStream.write(bytes, 0, i);
    }

    @Override
    public boolean getAuthCredentials(boolean b, String s, int i, String s1, String s2, CefAuthCallback cefAuthCallback) {
        return false;
    }


    @Override
    public void setNativeRef(String s, long l) {

    }

    @Override
    public long getNativeRef(String s) {
        return 0;
    }
}
