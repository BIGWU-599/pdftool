package com.sfccn.pdftool.parse.zhaogu;

import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.parse.ParseValue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 是否存在单一客户销售占比超过总额的30%
 */
public class Parse28Consumer30 extends ParseValue {
    boolean isExist = false;

    public Parse28Consumer30() {
        label = "是否存在单一客户销售占比超过总额的30%";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false){
            isExist = true;
//            addValue(Arrays.asList("是否存在单一客户销售占比超过总额的30%", ""));
            addElementValue(null, "", "参考\"13、主要客户类型\"");
        }

    }
}
