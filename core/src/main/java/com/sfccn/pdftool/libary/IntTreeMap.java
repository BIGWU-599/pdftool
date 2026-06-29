package com.sfccn.pdftool.libary;

import java.util.TreeMap;

public class IntTreeMap<K,V> extends TreeMap<K,V> {
    public IntTreeMap(){
        super((str1, str2) -> ((Integer)str2).intValue() == ((Integer)str1).intValue() ? 0 : ((Integer)str2).intValue() < ((Integer)str1).intValue() ? 1 : -1);
    }
}
