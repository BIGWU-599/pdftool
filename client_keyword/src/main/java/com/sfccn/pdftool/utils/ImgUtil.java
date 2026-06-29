package com.sfccn.pdftool.utils;


import com.sfccn.pdftool.client.MainKeyWord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;


public class ImgUtil {
    private Logger log = LogManager.getLogger(ImgUtil.class);
    public final static int img_width = 16;
    public final static int img_height = 16;
    public final static ImageIcon IMAGE_FILE = getIconAuto(getIcon("/images/file.png"),img_width,img_height);
    public final static ImageIcon IMAGE_FILE_NEW = getIconAuto(getIcon("/images/file-new.png"),img_width,img_height);
    public final static ImageIcon IMAGE_FOLDER = getIconAuto(getIcon("/images/folder.png"),img_width,img_height);
    public final static ImageIcon IMAGE_FOLDER_NEW = getIconAuto(getIcon("/images/folder-new.png"),img_width,img_height);
//    public final static ImageIcon IMAGE_FOLDER_EDIT = getIconAuto(getIcon("/images/folder-edit.png"),18,18);
    public final static ImageIcon IMAGE_FILE_DIFF = getIconAuto(getIcon("/images/file-diff.png"),img_width,img_height);
    public final static ImageIcon IMAGE_FOLDER_DIFF = getIconAuto(getIcon("/images/folder-diff.png"),img_width,img_height);
    public final static ImageIcon IMAGE_LOGO = getIconAuto(getIcon("/images/logo.png"),img_width,img_height);
    public final static ImageIcon IMAGE_CLOSE = getIconAuto(getIcon("/images/close-line.png"),img_width,img_height);
    public final static ImageIcon IMAGE_LOGO_APP = getIcon("/images/logo.png");
    public static ImageIcon getIcon(String imgUrl){
        ImageIcon icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(
                MainKeyWord.class.getResource(imgUrl)));
        return icon;
    }
    public static ImageIcon getIcon(String imgUrl, int picWidth, int pinHeight){
        ImageIcon icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(
                MainKeyWord.class.getResource(imgUrl)));
        icon.setImage(icon.getImage().getScaledInstance(picWidth,pinHeight, Image.SCALE_SMOOTH));
        return icon;
    }
    public static ImageIcon getIconAuto(String imgUrl, int picWidth, int pinHeight){
        AutoImageIcon icon = new AutoImageIcon(Toolkit.getDefaultToolkit().getImage(MainKeyWord.class.getResource(imgUrl)), picWidth, pinHeight);
        return icon;
    }
    public static ImageIcon getIconAuto(ImageIcon image, int picWidth, int pinHeight){
        AutoImageIcon icon = new AutoImageIcon(image.getImage(), picWidth, pinHeight);
        return icon;
    }
}
class AutoImageIcon extends ImageIcon{
    private Logger log = LogManager.getLogger(AutoImageIcon.class);
    public AutoImageIcon(Image image, int width, int height) {
        double scaleX = ScreenUtil.getScaleX();
        if(scaleX > 1.0){
            width = (int)(width * ScreenUtil.getScaleX());
            height= (int)(height * ScreenUtil.getScaleY());
        }

        image = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        setImage(image);
        loadImage(image);
    }

    public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();
        AffineTransform at = g2d.getTransform();
        if(at.getScaleX() > 1){
            x = (int)(x * at.getScaleX());
            y = (int)(y * at.getScaleY());
            var scaled = AffineTransform.getScaleInstance(1.0 / at.getScaleX(), 1.0 / at.getScaleY());
            at.concatenate(scaled);
            g2d.setTransform(at);
        }
        super.paintIcon(c,g2d,x,y);
    }
}