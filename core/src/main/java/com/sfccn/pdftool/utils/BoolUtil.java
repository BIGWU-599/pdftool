package com.sfccn.pdftool.utils;

import java.util.List;

public class BoolUtil {

    /**
     * 如果content包含strs数组内的任一字符串，返回ture,
     * 等同于 content.indexOf(strs[0]) > -1 || content.indexOf(strs[1]) > -1
     * @param content
     * @param strs
     * @return
     */
    public static boolean in(String content, List<String> strs){
        boolean isExist = false;
        for(String str:strs){
            if(content.indexOf(str) > -1){
                isExist = true;
                break;
            }
        }
        return isExist;
    }

    /**
     *  如果content不包含strs中的任一字符串，返回true
     *  等同于 content.indexOf(strs[0]) == -1 && content.indexOf(strs[1]) == -1
     * @param content
     * @param strs
     * @return
     */
    public static boolean notIn(String content, List<String> strs){
        boolean isExist = true;
        for(String str:strs){
            if(content.indexOf(str) > -1){
                isExist = false;
                break;
            }
        }
        return isExist;
    }
}
