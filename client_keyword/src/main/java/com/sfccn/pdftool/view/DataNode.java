package com.sfccn.pdftool.view;

import java.util.ArrayList;
import java.util.List;

public class DataNode<T extends DataNode> implements Cloneable{
    protected String title;
    protected List<T> children;

    private String uuid = java.util.UUID.randomUUID().toString();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<T> getChildren() {
        if(children == null){
            children = new ArrayList<>();
        }
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }

    public void addChildren(T node) {
        getChildren().add(node);
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public T clone() {
        T clone = null;
        try {
            clone = (T)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }
    public String toString(){
        return title;
    }
}
