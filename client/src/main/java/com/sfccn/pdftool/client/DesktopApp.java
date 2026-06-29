package com.sfccn.pdftool.client;

import java.io.File;

public class DesktopApp {

    public static void jar(){
        File file = new File("D:\\project\\idea\\datasfc\\pdftool\\client\\lib");
        StringBuffer sff = new StringBuffer();
        File[] files = file.listFiles();
        for(File f:files){
            sff.append("lib\\").append(f.getName()).append(";");
        }
        System.out.println(sff);
    }

    public static void main(String[] args) {
//        Desktop.main(args);
        jar();
    }
}
