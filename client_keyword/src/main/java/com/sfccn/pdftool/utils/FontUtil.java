package com.sfccn.pdftool.utils;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

public class FontUtil {

    private static String[] fonts = null;

    public static String[] getFonts(){
        if(fonts == null){
//            fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
//            fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames(Locale.forLanguageTag("zh_CN"));
            Font[] fontList = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
            fonts = new String[fontList.length];
            int i = 0;
            for(Font font:fontList){
                fonts[i++] = font.getFamily(Locale.CHINA);
            }
            Arrays.sort(fonts);
        }
        return fonts;
    }

    public static void initGobalFont() {
        String[] fonts = getFonts();
        List<String> fontList = Arrays.asList("微软雅黑", "Microsoft Yahei UI", "宋体", "SimSun");
        String defaultFont = null;
        String simSun = null;
        for (String font : fonts) {
            //String fontName = font.getFontName();
            if(font.equals("宋体")){
                simSun = font;
                defaultFont = font;
            }
            else if (defaultFont == null && fontList.contains(font)) {
                defaultFont = font;
                //break;
            }
        }
        if (defaultFont != null) {
            defaultFont = simSun != null ? simSun : defaultFont;
//            System.out.println("=====defaultFont:" + defaultFont.getFontName());
            Font font = new Font(defaultFont, Font.PLAIN, 14);
            for (Enumeration keys = UIManager.getDefaults().keys(); keys.hasMoreElements(); ) {
                Object key = keys.nextElement();
                if(key != null && key.toString().contains("font")){
                    Font fontTemp = UIManager.getDefaults().getFont(key);
                    if (fontTemp != null) {
                        font = font.deriveFont((float)14);
                        UIManager.put(key,font);
                    }
                }
            }
            UIManager.put("defaultFont", font);
        }
    }

    public static void test(){
        for (Enumeration keys = UIManager.getDefaults().keys(); keys.hasMoreElements(); ) {
            Object key = keys.nextElement();
            if(key != null && key.toString().contains("font")){
                Font fontTemp = UIManager.getDefaults().getFont(key);
                if (fontTemp != null) {
                    System.out.println("font=====" + key + ":" + fontTemp);
                }
            }
        }
    }
//    public static Font getDefaultFont(){
//        File dir = ToolUtil.getConfigDir();
//
//        return defaultFont;
//    }
//
//    public static void setDefaultFont(Font font){
//        defaultFont = font;
//    }
}
