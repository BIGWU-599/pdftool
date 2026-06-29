package com.sfccn.pdftool.webview.cef;

import org.cef.callback.CefCallback;
import org.cef.handler.CefResourceHandlerAdapter;
import org.cef.misc.IntRef;
import org.cef.misc.StringRef;
import org.cef.network.CefRequest;
import org.cef.network.CefResponse;
import org.cef.network.CefURLRequest;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

/**
 * 东方财富
 */
public class ResourceEastmoneyHandle extends CefResourceHandlerAdapter {
    private ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
    private String html = "";
    private int startPos = 0;
    public ResourceEastmoneyHandle(){

    }
    public boolean processRequest(CefRequest cefRequest, CefCallback callback) {
        System.out.println("ResourceTest====processRequest,cefRequest.getURL:" + cefRequest.getURL());
        CefURLRequest urlRequest_ = CefURLRequest.create(cefRequest, new CefURLRequestClientAdapter() {
            @Override
            public void onRequestComplete(CefURLRequest cefURLRequest) {
                try{
                    html = byteStream.toString("UTF-8");
                    System.out.println("ResourceTest====processRequest,onRequestComplete:" + html);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                callback.Continue();
            }
            @Override
            public void onDownloadData(CefURLRequest request, byte[] data, int data_length) {
                byteStream.write(data, 0, data_length);
            }
        });
        startPos = 0;
        return true;
    }
    @Override
    public void getResponseHeaders(
            CefResponse response, IntRef response_length, StringRef redirectUrl) {
        //System.out.println("ResourceTest====getResponseHeaders: " + response);

        response_length.set(html.length());
        response.setMimeType("text/html");
        response.setStatus(200);
    }
    @Override
    public boolean readResponse(
            byte[] data_out, int bytes_to_read, IntRef bytes_read, CefCallback callback) {
        //System.out.println("ResourceTest====readResponse,bytes_to_read: " + bytes_to_read);
        int length = html.length();
        if (startPos >= length) return false;
        int endPos = startPos + bytes_to_read;
        String dataToSend =
                (endPos > length) ? html.substring(startPos) : html.substring(startPos, endPos);

        ByteBuffer result = ByteBuffer.wrap(data_out);
        result.put(dataToSend.getBytes());
        bytes_read.set(dataToSend.length());
        startPos = endPos;
        return true;
    }
    @Override
    public void cancel() {
        //System.out.println("cancel");
        startPos = 0;
    }
}
