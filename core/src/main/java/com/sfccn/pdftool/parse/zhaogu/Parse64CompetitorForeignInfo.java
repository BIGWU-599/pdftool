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
 *
 * 国际主要竞争者简称（英文逗号分隔，如没有，填“无”）
 */
public class Parse64CompetitorForeignInfo extends ParseValue {
    boolean isExist = false;

    public Parse64CompetitorForeignInfo() {
        label = "国际主要竞争者简称";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false){
            isExist = true;
//            addValue(Arrays.asList("国际主要竞争者简称", ""));
            addElementValue(null,"","参考国内主要竞争者");
        }
    }
}
