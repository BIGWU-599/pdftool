package com.sfccn.pdftool.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/2/10.
 */
public class ToolUtil {




    /**
     * 去除字符串中所包含的空格（包括:空格(全角，半角)、制表符、换页符等）
     * @param s
     * @return
     */
    public static String removeAllBlank(String s, String replace){
        String result = "";
        if(null!=s && !"".equals(s)){
            result = s.replaceAll("[　*| *| *|//s*]*", replace);
        }
        return result;
    }
    /**
     * 去除字符串中所包含的空格（包括:空格(全角，半角)、制表符、换页符等）
     * @param s
     * @return
     */
    public static String removeAllBlank(String s){
        String result = "";
        if(null!=s && !"".equals(s)){
            result = s.replaceAll("[　*| *| *|//s*]*", "");
        }
        return result;
    }

    /**
     * 去除字符串中头部和尾部所包含的空格（包括:空格(全角，半角)、制表符、换页符等）
     * @param s
     * @return
     */
    public static String trim(String s){
        String result = "";
        if(null!=s && !"".equals(s)){
            result = s.replaceAll("^[　*| *| *|//s*]*", "").replaceAll("[　*| *| *|//s*]*$", "");
        }
        return result;
    }

    /**
     * 去掉所有特殊符号
     * @return
     */
    public static String filterTitle(String title){
        String regEx="[\\s~·`!！@#￥$%^……&*（()）\\-——\\-_=+【\\[\\]】｛{}｝\\|、\\\\；;：:‘'“”\"，,《<。.》>、/？?]";
        String tempTitle = title.replaceAll(regEx, "");
        return tempTitle;
    }

    /**
     * 获取字符串
     * @param obj
     * @param defStr
     * @return
     */
    public static String getString(Object obj, String defStr){
        String str = defStr;
        if(obj != null){
            str = obj.toString();
        }
        return str;
    }

    public static String getHost(String url){
        try{
            java.net.URL  urlHost = new  java.net.URL(url);
            String host = urlHost.getHost();
            return host;
        }
        catch(Exception e){

        }
        return "";
    }
    public static String listToStr(Collection<Object> list, String cchar){
//        StringUtils.
        StringBuilder sr = new StringBuilder();
        if(list != null && list.size() > 0){
            for (Object obj:list){
                if(obj instanceof String){
                    sr.append("'").append(obj).append("',");
                }
                else{
                    sr.append(obj).append(",");
                }
            }
            sr = sr.deleteCharAt(sr.length() - 1);
        }
        return sr.toString();
    }

    /**
     * 与php兼容的md5
     * @param str
     * @return
     */
    public static String md5(String str){

        byte[] strbyte=str.getBytes();

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        md.update(strbyte, 0, strbyte.length);
//        md.update(secretbyte, 0, secretbyte.length);
        return String.valueOf(Hex.encodeHex(md.digest()));
    }
    public static void setLogFile(String name){
        if(!name.endsWith(".log")){
            name = name + ".log";
        }
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        org.apache.logging.log4j.core.config.Configuration config = ctx.getConfiguration();

        if (config.getAppender("appFile") != null) {
            RollingFileAppender rollingFileAppender = (RollingFileAppender)config.getAppender("appFile");
            config.getLoggerConfig("").removeAppender("appFile");
            rollingFileAppender.stop();
            Appender appender = RollingFileAppender.newBuilder()
                    .setName("appFile2")
                    .withFileName(name)
                    .withFilePattern(rollingFileAppender.getFilePattern())
                    .setLayout(rollingFileAppender.getLayout())
                    .withPolicy(rollingFileAppender.getTriggeringPolicy())
                    .build();
            appender.start();
            config.getLoggerConfig("").addAppender(appender, config.getRootLogger().getLevel(), null);
            ctx.updateLoggers(config);
        }
    }

    /**
     * 获取文章第一个句子
     * @return
     */
    public static String getFirstSent(String para, String sym){
        String sent = "";
        for(int i = 0; i < para.length(); i++){
            char c = para.charAt(i);
            int index = sym.indexOf(c);
            if(index > - 1){
                sent = para.substring(0, sym.indexOf(c) + 1);
                break;
            }
        }
        return sent;
    }
    /**
     * 获取文章最后一个句子
     * @return
     */
    public static String getLastSent(String para, String sym){
        String sent = "";
        for(int i = para.length() - 1 ; i >= 0; i--){
            char c = para.charAt(i);
            int index = sym.indexOf(c);
            if(index > - 1){
                sent = para.substring(sym.indexOf(c),  para.length());
                break;
            }
        }
        return sent;
    }
    public static String toJson(Object obj){
        String json = null;
        try{
            ObjectMapper mapper=new ObjectMapper();
            json = mapper.writeValueAsString(obj);
        }
        catch(Exception e){

        }
        return json;
    }
    public static Map<String, Object> strToMap(String jsonStr){
        Map<String, Object> json = null;
        try{
            ObjectMapper mapper=new ObjectMapper();
            json = mapper.readValue(jsonStr, Map.class);
        }
        catch(Exception e){
            System.out.println("strToMap error======" + e.getMessage());
        }
        return json;
    }
    public static JsonNode strToJsonNode(String jsonStr){
        Map<String, Object> json = null;
        JsonNode node = null;
        try{
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
//            mapper.configure(JsonParser.Feature., true);
            node = mapper.readTree(jsonStr);
            //json = mapper.readValue(jsonStr, Map.class);
        }
        catch(Exception e){
            System.out.println("strToMap error======" + e.getMessage());
        }
        return node;
    }

    public static ArrayNode createArrayNode(){
        ObjectMapper mapper = new ObjectMapper();
        return mapper.createArrayNode();
    }
    public static ObjectNode createObjectNode(){
        ObjectMapper mapper = new ObjectMapper();
        return mapper.createObjectNode();
    }
    public static <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef) throws IllegalArgumentException {
        try{
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            return mapper.convertValue(fromValue,toValueTypeRef );
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 数值转成亿或万
     * @param value
     * @return
     */
    public static String string2Money(double value){
        String v = "";
        double absValue = Math.abs(value);
        if(absValue > 100000000){
            v = new BigDecimal(value).divide(new BigDecimal(100000000), 2, RoundingMode.HALF_UP).toString() + "亿";
        }
        else{
            v = new BigDecimal(value).divide(new BigDecimal(10000), 2, RoundingMode.HALF_UP).toString() + "万";
        }
        return v;
    }
    /**
     * 数据值转为万元亿元,保留2位小数
     */
    public static double string2Double(double value, int div){
        BigDecimal divValue = new BigDecimal(value);
        BigDecimal digDiv = new BigDecimal(div);
        return divValue.divide(digDiv).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
    /**
     * 数据值转为万元亿元,保留2位小数
     */
    public static double string2Double(String value, int div){
        BigDecimal divValue = new BigDecimal(value);
        BigDecimal digDiv = new BigDecimal(div);
        return divValue.divide(digDiv).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
    public static String readFile(File file, String encode){
        String content = null;
        try{
            content = FileUtils.readFileToString(file, encode);
        }
        catch(Exception e){
            System.out.println("readFil error=======file:" +file);
        }
        return content;
    }

    public static String img2base64(File file){
        String imgStr = "";
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            Base64 encoder = new Base64();
            imgStr = new String(encoder.encode(buffer), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "data:image/png;base64,"+imgStr;
    }

    public static boolean base642Img(String imgData, String imgFilePath){
        boolean result = true;
        if (imgData == null) {
            return false;
        }
        Base64 decoder = new Base64();
        try(OutputStream out = new FileOutputStream(imgFilePath);) {
            byte[] b = decoder.decode(imgData);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            out.write(b);
            out.flush();
        } catch (Exception e) {
            result = true;
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 程序暂停/睡眠毫秒数
     * @param millis
     */
    public static void sleep(long millis){
        try{
            Thread.sleep(millis);
        }
        catch(Exception e){
        }
    }
    public static int nextInt(){
        Random random = new java.util.Random();
        return random.nextInt(100);
    }
    /**
     * 通过正则查找单个匹配的文本，String match = findText(txt, "(\\d{4,4}年\\d{1,2}月\\d{1,2}日)");
     * @param text
     * @param p
     * @return
     */
    public static String findText(String text, String p){
        Pattern pattern = Pattern.compile(p);
        Matcher matcher = pattern.matcher(text);
        String matchStr = null;
        if (matcher.find()) {
            matchStr = matcher.group(1);
        }
        return matchStr;
    }

    /**
     *
     * @param str
     * @param startTag
     * @param endTag
     * @param isIncludeTag 是否包含开始和结束标签
     * @return
     */
    public static String cutString(String str, String startTag, String endTag, boolean isIncludeTag){
        int start = str.indexOf(startTag);
        int end = str.lastIndexOf(endTag);
        if(isIncludeTag == true){
            str = str.substring(start, end + 1);
        }
        else{
            str = str.substring(start + 1, end);
        }
        return str;
    }

    public static boolean isLocal(){
        boolean isLocal = false;
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            NetworkInterface networkInterface;
            Enumeration<InetAddress> inetAddresses;
            InetAddress inetAddress;
            String ip;
            String hostName;
            while (networkInterfaces.hasMoreElements()) {
                networkInterface = networkInterfaces.nextElement();
                inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    inetAddress = inetAddresses.nextElement();
                    if (inetAddress != null && inetAddress instanceof Inet4Address) { // IPV4
                        ip = inetAddress.getHostAddress();
                        hostName = inetAddress.getHostName();
//                        ipList.add(ip);
//                        System.out.println(ip + "," + hostName);
                        if(hostName.indexOf("DESKTOP-") > -1){
                            isLocal = true;
                            break;
                        }
                    }
                }
                if(isLocal == true){
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("=======ToolUtil.isLocal error" + e.getMessage());
        }
        return isLocal;
    }

    public static boolean isString(Object obj){
        return obj instanceof String ? true : false;
    }



    public static void main(String[] args) {

    }
}
