package com.sfccn.pdftool.utils;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fife.ui.rsyntaxtextarea.modes.*;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.*;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToolUtil {
    private static Logger log = LogManager.getLogger(ToolUtil.class);
    private static List<Object> styleList;
    /**
     * 选在当前目录查找，再在包内查找。
     *
     * @return
     */
    public static InputStream getFileInputStream(String filename) throws FileNotFoundException {
        InputStream is = null;

        File file = new File(filename);
        if (file.exists() == true) {
            is = new FileInputStream(file);
            System.out.println("log.properties:" + file.getAbsolutePath());
        } else if (is == null) {
            URL url = ToolUtil.class.getProtectionDomain().getCodeSource().getLocation();
            String filePath = null;
            try {
                filePath = URLDecoder.decode(url.getPath(), "utf-8");// 转化为utf-8编码，支持中文
                File fileTemp = new File(filePath);
                if (fileTemp.isFile() == true) {
                    fileTemp = fileTemp.getParentFile();
                }
                file = new File(fileTemp, filename);
                is = new FileInputStream(file);
                System.out.println("log.properties=" + file.getAbsolutePath());
            } catch (Exception e) {
                System.out.println("warning " + filename + " not exist.");
            }

        }
        if (is == null) {
            // 保证nate-image下被编译
            System.out.println("log.properties@" + ToolUtil.class.getClassLoader().getResource("."));
            InputStream is2 = ToolUtil.class.getClassLoader().getResourceAsStream(filename);
            is = is2;
        }
        return is;
    }

    public static String getRunPath() {
        try {
            File path = new File(".");
            URL url = ToolUtil.class.getProtectionDomain().getCodeSource().getLocation();
            String filePath = URLDecoder.decode(url.getPath(), "utf-8");
            File fileTemp = new File(filePath);
            if (fileTemp.isFile() == true) {
                fileTemp = fileTemp.getParentFile();
            }
            return fileTemp.getAbsolutePath();
        } catch (Exception e) {
            System.out.println("RunPath path error=====:" + e.getMessage());
            e.printStackTrace();
        }
        return ".";
    }


    public Font getFont(){
        Font font1 = null;
        try{
            String fontname = "D:\\project\\idea\\datagui\\DFDiff\\logs\\zhenhei.TTF";
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(fontname));
            font1 = font.deriveFont(Font.BOLD, 16);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return font1;
    }

    public static boolean isWindow(){
        String os = System.getProperty("os.name");
        //Windows操作系统
        if (os != null && os.toLowerCase().startsWith("windows")) {
            return true;
        }
        return false;
    }
    public static boolean isMac(){
        String os = System.getProperty("os.name");
        //Mac操作系统
        if (os != null && os.toLowerCase().startsWith("mac")) {
            return true;
        }
        return false;
    }
    public static boolean isLinux(){
        String os = System.getProperty("os.name");
        //Linux操作系统
        if (os != null && os.toLowerCase().startsWith("linux")) {
            return true;
        }
        return false;
    }

    public static void initLookAndFeel() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {

        //Windows操作系统
        if (ToolUtil.isWindow()) {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } else {
            //Linux或其它操作系统
            // linux中文输入兼容
            System.setProperty("jdk.gtk.version", "2");
            String lookAndFeel =UIManager.getCrossPlatformLookAndFeelClassName();
            UIManager.setLookAndFeel(lookAndFeel);
//                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //UIManager.setLookAndFeel(NimbusLookAndFeel.class.getName());

            //1、Metal风格 (默认)
//                String lookAndFeel = "javax.swing.plaf.metal.MetalLookAndFeel";
//                UIManager.setLookAndFeel(lookAndFeel);

            //2、Windows风格
//                String lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
//                UIManager.setLookAndFeel(lookAndFeel);

            //Windows Classic风格
//                String lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel";
//                UIManager.setLookAndFeel(lookAndFeel);

//                4、Motif风格
//                String lookAndFeel4 = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
//                UIManager.setLookAndFeel(lookAndFeel4);

//                5、Mac风格 (需要在相关的操作系统上方可实现)
//            String lookAndFeel5 = "com.sun.java.swing.plaf.mac.MacLookAndFeel";
//            UIManager.setLookAndFeel(lookAndFeel5);

//                6、GTK风格 (需要在相关的操作系统上方可实现)
//            String lookAndFeel6 = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
//            UIManager.setLookAndFeel(lookAndFeel6);

//                7、可跨平台的默认风格
//            String lookAndFeel7 = UIManager.getCrossPlatformLookAndFeelClassName();
//            UIManager.setLookAndFeel(lookAndFeel7);

//                8、当前系统的风格
//            String lookAndFeel8 = UIManager.getSystemLookAndFeelClassName();
//            UIManager.setLookAndFeel(lookAndFeel8);

        }
    }

    public static Icon getSystemIcon(File file){
        //ShellFolder shellFolder = ShellFolder.getShellFolder(file);
        Icon icon = FileSystemView.getFileSystemView().getSystemIcon(file);
        if(icon == null){
            icon = ImgUtil.IMAGE_FILE;
        }
        return icon;
    }


    public static void main(String[] args) throws Exception{


    }

}
