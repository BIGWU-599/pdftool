package com.sfccn.pdftool.client;

import com.sfccn.pdftool.utils.*;
import com.sfccn.pdftool.view.ProjectTable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Arrays;

public class MainKeyWord extends JFrame {
    public static final Logger log = LogManager.getLogger(MainKeyWord.class);
//    public static ProjectTable projectTableLeft;
    public static ProjectTable projectTableRight;
    public MainKeyWord() {
        setTitle(AppInfo.Name);
        setIconImage(ImgUtil.IMAGE_LOGO_APP.getImage());
        setJMenuBar(new MainMenu());

        int width = (int)(ScreenUtil.getScreenWidth() * 0.6);
        JPanel PanelLeft = new PanelLeft();
//        projectTableLeft = new ProjectTable();
        projectTableRight = new ProjectTable();
        JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(PanelLeft),
                projectTableRight);
        sp.setDividerLocation(width/2);
        getContentPane().add(sp);
        setSize(width, (int)(ScreenUtil.getScreenHeight() * 0.6));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //handler = new FileTransferHandler();

        setLocationRelativeTo(null);

    }

    class MainMenu extends JMenuBar {
        public MainMenu() {
            JPopupMenu.setDefaultLightWeightPopupEnabled(false);//关键一行。
            /////////////////////// 文件菜单
            add(new FileMenu());
        }
        class FileMenu extends JMenu {
            private final String str_newfile = "新建文件";
            private final String str_openfile = "打开文件";
            private final String str_openFolder = "打开文件夹";
            private final String str_savefile = "保存文件";
            private final String str_saveAsfile = "另存文件";
            private final String str_exit = "退出";

            public FileMenu() {
                super("文件");
                AbstractAction action = new FileAction();

                JMenuItem openFileItem = new JMenuItem(str_openfile);
                openFileItem.setIcon(ImgUtil.IMAGE_FILE);
                openFileItem.addActionListener(action);
                add(openFileItem);

                JMenuItem openFolderItem = new JMenuItem(str_openFolder);
                openFolderItem.setIcon(ImgUtil.IMAGE_FOLDER);
                openFolderItem.addActionListener(action);
                add(openFolderItem);

//                JMenuItem saveFileItem = new JMenuItem(str_savefile);
//                saveFileItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S,
//                        java.awt.Event.CTRL_MASK));
//                saveFileItem.addActionListener(action);
//                add(saveFileItem);
//
//                JMenuItem saveAsFileItem = new JMenuItem(str_saveAsfile);
//                saveAsFileItem.addActionListener(action);
//                add(saveAsFileItem);

                addSeparator();

                JMenuItem exitItem = new JMenuItem(str_exit);
                exitItem.addActionListener(action);
                add(exitItem);
            }

            class FileAction extends AbstractAction {

                @Override
                public void actionPerformed(ActionEvent event) {
                    String command = event.getActionCommand();
                    switch (command) {
                        case str_openfile:
//                            openFile(null);
                            break;
                        case str_openFolder:
//                            openFolder(null);
                            break;
                        case str_exit:
                            System.exit(0);
                            break;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
//            if(args.length > 0){
//                Arrays.asList(args).forEach(arg->{
//                    log.info("args======" + arg);
//                    File file = new File(arg);
//                    if(file.exists() && fileArgs.size() < 2){
//                        fileArgs.add(new File(arg));
//                    }
//                });
//
//            }
            System.setProperty("file.encoding","UTF-8");
            System.setProperty("sun.java2d.dpiaware", "true");

//            System.setProperty("sun.java2d.uiScale", "1");

            // 抗锯齿
//            System.setProperty("awt.useSystemAAFontSettings","on");
//            System.setProperty("swing.aatext", "true");

            ToolUtil.initLookAndFeel();

            FontUtil.initGobalFont();

            SwingUtilities.invokeLater(() ->  {
                MainKeyWord dfMain = new MainKeyWord();
                dfMain.setVisible(true);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}