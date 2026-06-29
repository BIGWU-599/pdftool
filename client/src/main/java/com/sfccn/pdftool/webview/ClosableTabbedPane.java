package com.sfccn.pdftool.webview;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *自定义带关闭按钮的TabbedPane
 * @author  XiaoQuan
 */
public class ClosableTabbedPane extends JTabbedPane implements MouseListener {

    private static final long serialVersionUID = -5154435928212725041L;

    public ClosableTabbedPane() {
        super();
        addMouseListener(this);
    }

    @Override
    public void addTab(String title, Component component) {
        super.addTab(title, null, component);
    }

    public void addTab(String title, Component component, Icon extraIcon) {
        super.addTab(title, new CloseTabIcon(), component);
    }
    public void insertTab(String title, Component component, int index){
        super.insertTab(title, new CloseTabIcon(), component, "", index);
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        int tabNumber = getUI().tabForCoordinate(this, e.getX(), e.getY());
        System.out.println("tabNumber=====" + tabNumber);
        if (tabNumber < 0) {
            return;
        }
        if (((CloseTabIcon) getIconAt(tabNumber)) != null) {
            String title = this.getTitleAt(tabNumber);
            Rectangle rect = ((CloseTabIcon) getIconAt(tabNumber)).getBounds();
            if (rect.contains(e.getX(), e.getY()) && !title.equals("+")) {
                this.removeTabAt(tabNumber);
            }
        }
        e.consume();

    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    class CloseTabIcon implements Icon {

        private int x_pos;
        private int y_pos;
        private int width;
        private int height;

        public CloseTabIcon() {
            width = 16;
            height = 16;
        }
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            this.x_pos = x;
            this.y_pos = y;
            Color col = g.getColor();
            g.setColor(Color.black);
            int y_p = y + 2;
            g.drawLine(x + 1, y_p, x + 12, y_p);
            g.drawLine(x + 1, y_p + 13, x + 12, y_p + 13);
            g.drawLine(x, y_p + 1, x, y_p + 12);
            g.drawLine(x + 13, y_p + 1, x + 13, y_p + 12);
            g.drawLine(x + 3, y_p + 3, x + 10, y_p + 10);
            g.drawLine(x + 3, y_p + 4, x + 9, y_p + 10);
            g.drawLine(x + 4, y_p + 3, x + 10, y_p + 9);
            g.drawLine(x + 10, y_p + 3, x + 3, y_p + 10);
            g.drawLine(x + 10, y_p + 4, x + 4, y_p + 10);
            g.drawLine(x + 9, y_p + 3, x + 3, y_p + 9);
            g.setColor(col);
        }

        @Override
        public int getIconWidth() {
            return width;
        }

        @Override
        public int getIconHeight() {
            return height;
        }

        public Rectangle getBounds() {
            return new Rectangle(x_pos, y_pos, width, height);
        }
    }
}



