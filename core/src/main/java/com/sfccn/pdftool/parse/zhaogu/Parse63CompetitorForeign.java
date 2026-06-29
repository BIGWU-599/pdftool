package com.sfccn.pdftool.parse.zhaogu;

import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.parse.ParseValue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 国际主要竞争者数目(勾选）（国际：港、澳、台、国外）
 */
public class Parse63CompetitorForeign extends ParseValue {
    boolean isExist = false;

    public Parse63CompetitorForeign() {
        label = "国际主要竞争者数目(勾选）";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false){
            isExist = true;
//            addValue(Arrays.asList("国际主要竞争者数目(勾选）", ""));
            addElementValue(null,"","参考国内主要竞争者");
        }
    }

}
