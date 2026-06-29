package com.sfccn.pdftool.parse.zhaogu;

import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.parse.ParseValue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 担心可能被诉讼的对方专利号
 */
public class Parse74Lawsuit extends ParseValue {
    boolean isExist = false;

    public Parse74Lawsuit() {
        label = "担心可能被诉讼的对方专利号";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false){
            isExist = true;
//            addValue(Arrays.asList("担心可能被诉讼的对方专利号", ""));
        }
    }
}
