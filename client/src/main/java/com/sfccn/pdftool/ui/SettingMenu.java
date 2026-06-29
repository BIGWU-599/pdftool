package com.sfccn.pdftool.ui;

import org.cef.browser.CefBrowser;
import tests.detailed.dialog.DevToolsDialog;
import tests.detailed.dialog.ShowTextDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;

public class SettingMenu{
        public JPopupMenu popupMenu = null;
        public JFrame frame;
        private CefBrowser browser;
        public SettingMenu(JFrame frame, CefBrowser browser){
            this.frame = frame;
            this.browser = browser;
            // 设置菜单选项
            popupMenu = new JPopupMenu();
            JMenuItem viewSource = new JMenuItem("查看源码");
            viewSource.addActionListener(e->{
                ShowTextDialog visitor = new ShowTextDialog(
                        frame, "Source of \"" + browser.getURL() + "\"");
                browser.getSource(visitor);
            });
            popupMenu.add(viewSource);
            JMenuItem runjs = new JMenuItem("执行脚本");
            runjs.addActionListener(e->{
                ScriptDialog scriptDialog = new ScriptDialog(browser);
                scriptDialog.setVisible(true);
            });
            popupMenu.add(runjs);
            JMenuItem shwodevtoole = new JMenuItem("显示开发者工具");
            shwodevtoole.setAccelerator(KeyStroke.getKeyStroke('D', InputEvent.CTRL_DOWN_MASK));
            shwodevtoole.addActionListener(e->{
                DevToolsDialog devToolsDlg = new DevToolsDialog(frame, "DEV Tools", browser);
                devToolsDlg.addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentHidden(ComponentEvent e) {
                        shwodevtoole.setEnabled(true);
                    }
                });
                devToolsDlg.setVisible(true);
                shwodevtoole.setEnabled(false);
            });
            popupMenu.add(shwodevtoole);
        }
        public void show(Component invoker, int x, int y){
            int width = popupMenu.getWidth();
            System.out.println("width======" + width);
            popupMenu.show(invoker, x - width, y);
            if(width == 0){
                width = popupMenu.getWidth();
                popupMenu.show(invoker, x - width, y);
            }

        }
    }