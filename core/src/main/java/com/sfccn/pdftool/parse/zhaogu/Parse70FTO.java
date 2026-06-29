package com.sfccn.pdftool.parse.zhaogu;

import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.parse.ParseValue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 是否有可实施检索（FTO）报告
 */
public class Parse70FTO extends ParseValue {
    boolean isExist = false;

    public Parse70FTO() {
        label = "是否有可实施检索（FTO）报告";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false){
            isExist = true;
//            addValue(Arrays.asList("是否有可实施检索（FTO）报告", ""));
        }
    }
}
