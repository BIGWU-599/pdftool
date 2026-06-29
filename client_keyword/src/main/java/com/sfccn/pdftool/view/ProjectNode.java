package com.sfccn.pdftool.view;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;


public class ProjectNode extends DataNode<ProjectNode>{
    private final static Logger log = LogManager.getLogger(ProjectNode.class);

    public enum ProjectType {
        NONE,
        FILE, // 文件编辑
        FOLDER, // 目录
        FILE_DIFF, // 文件对比
        FOLDER_DIFF // 目录对比
    }

    private String title;
    private File pathLeft;
    private File pathRight;
    private ProjectType type;
    private boolean opened = false;

//    private List<ProjectNode> children;

    public ProjectNode(String title, File pathLeft, File pathRight, ProjectType type) {
        this.title = title;
        this.pathLeft = pathLeft;
        this.pathRight = pathRight;
        this.type = type;
//        children = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public File getPathLeft() {
        return pathLeft;
    }

    public void setPathLeft(File pathLeft) {
        this.pathLeft = pathLeft;
    }

    public File getPathRight() {
        return pathRight;
    }

    public void setPathRight(File pathRight) {
        this.pathRight = pathRight;
    }

    public ProjectType getType() {
        return type;
    }

    public void setType(ProjectType type) {
        this.type = type;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

}
