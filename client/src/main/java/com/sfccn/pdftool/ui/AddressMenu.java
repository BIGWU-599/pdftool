package com.sfccn.pdftool.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;

public class AddressMenu{
        private JPopupMenu popupMenu = null;
        public JMenuItem copy = null;
        public JMenuItem paste = null;
        public JMenuItem cut = null;
        private JTextField urlField = null;
        public AddressMenu(JTextField urlField){
            // 地址栏弹出菜单
            this.urlField = urlField;
            popupMenu = new JPopupMenu();
//            popupMenu.setLightWeightPopupEnabled(false);

            cut = new JMenuItem("剪切");
            cut.setAccelerator(KeyStroke.getKeyStroke('X', InputEvent.CTRL_MASK));
            cut.addActionListener(e->{
                urlField.cut();
            });
            popupMenu.add(cut);

            copy = new JMenuItem("复制");
            copy.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.CTRL_MASK));
            copy.addActionListener(e->{
                urlField.copy();
            });

            popupMenu.add(copy);
            paste = new JMenuItem("粘贴");
            paste.setAccelerator(KeyStroke.getKeyStroke('V', InputEvent.CTRL_MASK));
            paste.addActionListener(e->{
                urlField.paste();
            });
            popupMenu.add(paste);

        }

        public void show(Component invoker, int x, int y){
            cut.setEnabled(isCanCopy());
            copy.setEnabled(isCanCopy());
            paste.setEnabled(isClipboardString());
            popupMenu.show(invoker, x, y);
        }

        public boolean isCanCopy() {
            boolean b = false;
            int start = urlField.getSelectionStart();
            int end = urlField.getSelectionEnd();
            //log.info("========start:" + start + ", end:" + end);
            if (start != end)
                b = true;
            return b;
        }

        public boolean isClipboardString() {
            boolean b = false;
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable content = clipboard.getContents(this);
            try {
                if (content.getTransferData(DataFlavor.stringFlavor) instanceof String) {
                    b = true;
                }
            } catch (Exception e) {
            }
            return b;
        }
    }