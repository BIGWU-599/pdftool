package com.sfccn.pdftool.view;

import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

public class ProjectModel extends AbstractTreeTableModel {

    private DataNode root;
    public ProjectModel(DataNode node){
        this.root = node;
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueAt(Object o, int column) {
        ProjectNode node = (ProjectNode)o;

        switch (column) {
            case 0:
                return node;
            default:
                return "Unknown";
        }
    }

    @Override
    public Object getChild(Object parent, int index) {

        ProjectNode treenode = (ProjectNode) parent;

        return treenode.getChildren().get(index);
    }

    @Override
    public int getChildCount(Object parent) {
        ProjectNode treenode = (ProjectNode) parent;

        return treenode.getChildren().size();
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        ProjectNode treenode = (ProjectNode) parent;
        for (int i = 0; i < treenode.getChildren().size(); i++) {
            if (treenode.getChildren().get(i) == child) {
                return i;
            }
        }
        return 0;
    }

    public boolean isLeaf(Object node) {

        ProjectNode treenode = (ProjectNode) node;
        return treenode.getChildren().size() == 0;
    }

    @Override
    public Object getRoot() {
        return root;
    }
}
