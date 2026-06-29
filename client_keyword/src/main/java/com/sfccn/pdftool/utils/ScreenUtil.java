package com.sfccn.pdftool.utils;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class ScreenUtil {
    /**
     * 显示尺寸
     * @return
     */
    public static Dimension getScreen(){
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    public static int getScreenWidth(){
        return getScreen().width;
    }
    public static int getScreenHeight(){
        Dimension screenshot = Toolkit.getDefaultToolkit().getScreenSize();
        return screenshot.height;
    }

    /**
     * 显示器当前分辨率
     * @return
     */
    public static DisplayMode getDisplay(){
        GraphicsDevice graphDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode disMode = graphDevice.getDisplayMode();
        return disMode;
    }

    public static int getDisplayWidth(){
        return getDisplay().getWidth();
    }

    public static int getDisplayHeight(){
        return getDisplay().getHeight();
    }
    /**
     * 显示器分辨率缩放比例
     */
    public static AffineTransform getScale(){
        GraphicsConfiguration gc = GraphicsEnvironment
        .getLocalGraphicsEnvironment()
        .getDefaultScreenDevice().
        getDefaultConfiguration();
        AffineTransform at = gc.getDefaultTransform();
        return at;
    }

    public static double getScaleX(){
        return getScale().getScaleX();
    }
    public static double getScaleY(){
        return getScale().getScaleY();
    }
}
