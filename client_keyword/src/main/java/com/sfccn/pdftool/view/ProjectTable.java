package com.sfccn.pdftool.view;


import com.sfccn.pdftool.client.MainKeyWord;
import com.sfccn.pdftool.utils.ImgUtil;
import com.sfccn.pdftool.utils.ToolUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.table.ColumnFactory;
import org.jdesktop.swingx.table.TableColumnExt;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;


public class ProjectTable extends DfTreeTable {
    private static Logger log = LogManager.getLogger(ProjectTable.class);
    private ProjectModel model;
    private JPopupMenu popupMenu = new ProjectPopupMenu();
    public static TransferHandler handler = null;
    public ProjectTable() {
        root = new ProjectNode("根节点", null, null, ProjectNode.ProjectType.NONE);
        this.model = new ProjectModel(root);
        ColumnFactory factory = new ColumnFactory() {
            String[] columnNameKeys = { "名称(N)"};
            @Override
            public void configureTableColumn(TableModel model,
                                             TableColumnExt columnExt) {
                super.configureTableColumn(model, columnExt);
                if (columnExt.getModelIndex() < columnNameKeys.length) {
                    columnExt.setTitle(columnNameKeys[columnExt.getModelIndex()]);
                }
            }
        };

        setTableHeader(null);
        this.setColumnFactory(factory);
        this.setExpandsSelectedPaths(true);
        //setTransferHandler(MainKeyWord.handler);
        setTreeCellRenderer(new DefaultTreeRenderer(new ProjectIconValue(), new ProjectStringValue()));
        this.addMouseListener(new ProjectMouseAdapter());
        this.addMouseMotionListener(new ProjectMouseMotionListener());
        this.addTreeExpansionListener(new ProjectTreeExpansionListener());
    }

    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {

        Component component = super.prepareRenderer(renderer, row, column);
        Object obj = this.getValueAt(row, 0);

        if(obj instanceof ProjectNode projectNode){
            if(projectNode.isOpened() == true){
                component.setForeground(new Color(128,0,0));
            }
            else{
                component.setForeground(Color.BLACK);
            }

        }

        return this.applyRenderer(component, this.getComponentAdapter(row, column));
    }

    class ProjectPopupMenu extends JPopupMenu{
        private JMenuItem popupExplorer;
        private JMenuItem shellExplorer;
        private JMenuItem popupRemove;
        private JMenuItem popupNewFile;
        private JMenuItem popupNewFolder;
        private JMenuItem popupRename;
        private JMenuItem popupDelete;
        public ProjectPopupMenu(){
            final String explorerCommand = "在资源管理器打开";
            final String shellCommand = "在命令行打开";
            final String removeCommand = "关闭目录";
            final String newFileCommand = "新建文件";
            final String newFolderCommand = "新建目录";
            final String renameCommand = "重命名";
            final String deleteCommand = "删除";

            ActionListener action = e -> {
                int row = getSelectedRow();
                ProjectNode node = (ProjectNode)getValueAt(row, 0);
                ProjectNode parentNode = (ProjectNode)ProjectTable.this.getPathForRow(row).getParentPath().getLastPathComponent();

//                if(node.getType().equals(ProjectNode.ProjectType.FOLDER)){
                    String command = e.getActionCommand();

                    switch (command){
                        case explorerCommand:
                            explorer(node);
                            break;
                        case shellCommand:
                            shellExplorer(node);
                            break;
                        case removeCommand:
                            clearSelection();
                            removeFolder(node);
                            break;
                        case newFileCommand:
                            //newFile(node, parentNode, false);
                            break;
                        case newFolderCommand:
                            //newFile(node, parentNode, true);
                            break;
                        case renameCommand:
                            //rename(node);
                            break;
                        case deleteCommand:
                            delete(node, parentNode);
                            break;
                    }
//                }
            };

            popupExplorer = new JMenuItem(explorerCommand);
            popupExplorer.addActionListener(action);

            shellExplorer = new JMenuItem(shellCommand);
            shellExplorer.addActionListener(action);

            popupRemove = new JMenuItem(removeCommand);
            popupRemove.addActionListener(action);

            popupNewFile = new JMenuItem(newFileCommand);
            popupNewFile.setIcon(ImgUtil.IMAGE_FILE_NEW);
            popupNewFile.addActionListener(action);

            popupNewFolder = new JMenuItem(newFolderCommand);
            popupNewFolder.setIcon(ImgUtil.IMAGE_FOLDER_NEW);
            popupNewFolder.addActionListener(action);

            popupRename = new JMenuItem(renameCommand);
//            popupFolder.setIcon(ImgUtil.IMAGE_FOLDER_NEW);
            popupRename.addActionListener(action);

            popupDelete = new JMenuItem(deleteCommand);
            popupDelete.addActionListener(action);

            add(popupExplorer);
            if(ToolUtil.isWindow() == true){
                add(shellExplorer);
            }

            add(popupRemove);
            addSeparator();
            add(popupNewFile);
            add(popupNewFolder);
            add(popupRename);
            addSeparator();
            add(popupDelete);

        }
        public void show(Component invoker, int x, int y, boolean isHide, ProjectNode node){
            // 第一层
            if(isHide == true){
                popupExplorer.setEnabled(true);
                shellExplorer.setEnabled(true);
                popupRemove.setEnabled(true);
                if(node.getType().equals(ProjectNode.ProjectType.FOLDER)){
                    popupNewFile.setEnabled(true);
                    popupNewFolder.setEnabled(true);
                    popupRename.setEnabled(true);
                }
                else{
                    popupNewFile.setEnabled(false);
                    popupNewFolder.setEnabled(false);
                    popupRename.setEnabled(false);
                }
                popupDelete.setEnabled(false);
            }
            else{
                popupExplorer.setEnabled(true);
                shellExplorer.setEnabled(true);
                popupRemove.setEnabled(false);
                popupNewFile.setEnabled(true);
                popupNewFolder.setEnabled(true);
                popupRename.setEnabled(true);
                popupDelete.setEnabled(true);
            }
            show(invoker,x,y);
        }
        //public void hide

        public void explorer(ProjectNode node){
            try{
                File file = node.getPathLeft();
                String os = System.getProperty("os.name");
                if(os.startsWith("Windows")){
                    // 打开window文件夹
                    Runtime.getRuntime().exec("explorer /select," + file.getAbsolutePath());
                }
                else if(os.startsWith("Mac")){
                    // 打开finder文件夹
                    Runtime.getRuntime().exec("open -R " + file.getAbsolutePath());
                }
                else{
                    Desktop.getDesktop().open(file.getParentFile());
                }
            }
            catch(Exception e){
                log.info("explorer error===" + node.getPathLeft(), e);
            }
        }

        public void shellExplorer(ProjectNode node){
            try{
                File file = node.getPathLeft();
                String os = System.getProperty("os.name");
                if(os.startsWith("Windows")){
                    // 打开window文件夹
                    if(file.isDirectory()){
                        Runtime.getRuntime().exec("cmd /k start /d \"" + file.getAbsolutePath() + "\"");
                    }
                    else{
                        Runtime.getRuntime().exec("cmd /k start /d \"" + file.getParentFile().getAbsolutePath() + "\"");
                    }
                }
                else if(os.startsWith("Mac")){
                    // 打开finder文件夹
                    Runtime.getRuntime().exec("open -R " + file.getAbsolutePath());
                }
                else{
                    Desktop.getDesktop().open(file.getParentFile());
                }
            }
            catch(Exception e){
                log.error("shellExplorer error===" + node.getPathLeft(), e);
            }
        }

        public void removeFolder(ProjectNode node){
                removeFor(node);
//                MainKeyWord.projectTable.getRootNode().getChildren().remove(node);
//                DFMain.projectTable.refreshUI();

        }

        public void removeFor(ProjectNode node){
            List<ProjectNode> nodes = node.getChildren();
            nodes.forEach(n->{
                removeFor(n);
            });

        }

//        public void rename(ProjectNode node){
//            File file = node.getPathLeft();
//            String name = JOptionPane.showInputDialog(null, "重命名：" + file.getName(), file.getName());
//            File newFile = new File(file.getParentFile(), name);
//            if(newFile.exists()){
//                JOptionPane.showMessageDialog(null, "重命名失败，名称：" + newFile.getName() + "已经存在！","错误", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//            boolean isSuccess = file.renameTo(newFile);
//            if(isSuccess == false){
//                JOptionPane.showMessageDialog(null, file.getName() + "重命名" + newFile.getName() + "失败！","错误", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//            node.setTitle(newFile.getName());
//            node.setPathLeft(newFile);
//            // 选中效果
//            MainKeyWord.desktopPane.selectFrame(node, newFile.getName());
//            refreshUI();
//        }

        public void delete(ProjectNode node, ProjectNode parentNode){
            int option = JOptionPane.showConfirmDialog(null, "是否要永久删除\"" + node.getPathLeft().getAbsolutePath() +"\"?", "删除文件", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                // 物理删除
                boolean d = node.getPathLeft().delete();
                parentNode.getChildren().remove(node);
                log.info("删除=====" + d + "," + node.getPathLeft().getAbsolutePath());
                refreshUI();
            } else if (option == JOptionPane.NO_OPTION) {
                System.out.println("取消删除");
            } else if (option == JOptionPane.CANCEL_OPTION) {
                System.out.println("操作被取消");
            }
        }
    }

    class ProjectTreeExpansionListener implements TreeExpansionListener{

        public void treeExpanded(TreeExpansionEvent event) {
//            log.info("treeExpanded=======" + event.getPath().getLastPathComponent());
            Object obj = event.getPath().getLastPathComponent();
            if(obj instanceof ProjectNode node){
                if(node.getType().equals(ProjectNode.ProjectType.FOLDER)){
                    List<ProjectNode> children = node.getChildren();
                    children.forEach(child->{
                        File file = child.getPathLeft();
                        File[] files = file.listFiles();
                        if(files != null && files.length > 0){
                            for(File f:files){
                                child.addChildren(new ProjectNode(f.getName(), f, null, ProjectNode.ProjectType.FOLDER));
                            }

                        }
                    });
                }

            }
        }

        public void treeCollapsed(TreeExpansionEvent event) {
            //log.info("treeCollapsed=======");
        }
    }

    // 鼠标在上时文本提示
    class ProjectMouseMotionListener implements MouseMotionListener {
        @Override
        public void mouseDragged(MouseEvent e) {

        }

        public void mouseMoved(MouseEvent event){
            int row = rowAtPoint(event.getPoint());
            if(row > -1){
                Object obj = getValueAt(row, 0);
                Component component = ProjectTable.this.prepareRenderer(row, 0);
                if(component instanceof JComponent label){
                    ProjectNode node = (ProjectNode)obj;
                    if((node.getType().equals(ProjectNode.ProjectType.FILE) || node.getType().equals(ProjectNode.ProjectType.FOLDER))
                            && node.getPathLeft() != null){
                        label.setToolTipText(node.getPathLeft().getAbsolutePath());
                    }
                    else{
                        label.setToolTipText(node.getTitle());
                    }
                }
            }
        }
    }

    class ProjectMouseAdapter extends MouseAdapter{

        public void mouseClicked(MouseEvent event) {
            super.mouseClicked(event);
            if (event.getButton() == MouseEvent.BUTTON3){
                int row = rowAtPoint(event.getPoint());
                if(row > -1){
                    Object obj = getValueAt(row, 0);
                    if(obj instanceof ProjectNode node){
//                        if(node.getType().equals(ProjectNode.ProjectType.FOLDER)){
                            setRowSelectionInterval(row, row);
                            int pathcount = ProjectTable.this.getPathForRow(row).getPathCount();
                            if(pathcount == 2){
                                ((ProjectPopupMenu)popupMenu).show(event.getComponent(), event.getX(), event.getY(), true, node);
                            }
                            else if(pathcount > 2){
                                ((ProjectPopupMenu)popupMenu).show(event.getComponent(), event.getX(), event.getY(), false, node);
                            }
//                        }
                    }
                }
            }
            else if(event.getClickCount() == 2){

                ProjectNode node = (ProjectNode)ProjectTable.this.getValueAt(ProjectTable.this.getSelectedRow(),0);
                if(node.getType().equals(ProjectNode.ProjectType.FOLDER) ){
                    File file = node.getPathLeft();
                    if(file.isFile() && file.exists()){
                        // 检查是否已经打开
//                        if(DFMain.desktopPane.isFrameOpen(node) == false){
//                            DesktopFrame frame = new DesktopFrameEditor(node);
//                            DFMain.desktopPane.add(frame);
//                            frame.setVisible(true);
//                            node.setOpened(true);
//                        }
                    }
                }
                // 选中效果
                //DFMain.desktopPane.selectFrame(node);
            }
        }
    }



    class ProjectIconValue implements IconValue{
        public Icon getIcon(Object o) {
            ProjectNode node = (ProjectNode)o;
            Icon icon = null;
            switch (node.getType()){
                case FILE:
                    File file = node.getPathLeft();
                    if(file != null && file.exists()){
                        icon = ToolUtil.getSystemIcon(file);
                    }
                    if (icon == null){
                        icon = ImgUtil.IMAGE_FILE;
                    }
                    break;
                case FOLDER:
                    if(node.getPathLeft().isDirectory()){
                        icon = ImgUtil.IMAGE_FOLDER;
                    }
                    else{
                        File file2 = node.getPathLeft();
                        if(file2.exists()){
                            icon = ToolUtil.getSystemIcon(file2);
                        }
                        if (icon == null){
                            icon = ImgUtil.IMAGE_FILE;
                        }
//                        icon = ImgUtil.IMAGE_FILE;
                    }
                    break;
                case FILE_DIFF:
                    icon = ImgUtil.IMAGE_FILE_DIFF;
                    break;
                case FOLDER_DIFF:
                    icon = ImgUtil.IMAGE_FOLDER_DIFF;
                    break;
                default:
                    icon = null;
            }
            return icon;
        }
    }

    class ProjectStringValue implements StringValue{
        public String getString(Object o) {
            ProjectNode node = (ProjectNode)o;
            return node.getTitle();
        }
    }

    // 单行选中
    class ProjectListSelectionListener implements ListSelectionListener{

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting()) {
                return;
            }
            int row = ProjectTable.this.getSelectedRow();
            Object node = ProjectTable.this.getValueAt(row, 0);

//            Component[] components = DFMain.desktopPane.getComponents();
//            for(Component component:components){
//                if(component instanceof DesktopFrame frame){
//                    ProjectNode nodeTemp = frame.getProjectNode();
//                    if(node == nodeTemp){
//                        frame.setFrameSelected(true);
//                        frame.toFront();
//                    }
//
//                }
//
//            }
        }
    }

    public void addNode(ProjectNode node){
        root.addChildren(node);
        if(root.getChildren().size() == 1){
            setTreeTableModel(model);
        }
        refreshUI();
    }
}
