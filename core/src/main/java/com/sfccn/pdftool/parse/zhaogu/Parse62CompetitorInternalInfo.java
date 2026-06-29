package com.sfccn.pdftool.parse.zhaogu;

import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.parse.ParseValue;
import com.sfccn.pdftool.utils.ToolUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 国内主要竞争者数目（<=5）(国内：中国大陆)
 */
public class Parse62CompetitorInternalInfo extends ParseValue {
    boolean isExist = false;

    public Parse62CompetitorInternalInfo() {
        label = "国内主要竞争者简称";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false){
            isExist = true;
            addElementValue(null,"","参考国内主要竞争者");
        }
    }
}
