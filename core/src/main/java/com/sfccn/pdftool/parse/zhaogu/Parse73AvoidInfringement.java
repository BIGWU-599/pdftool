package com.sfccn.pdftool.parse.zhaogu;

import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.parse.ParseValue;
import com.sfccn.pdftool.utils.ToolUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 是否有任何规避专利侵权风险的措施
 */
public class Parse73AvoidInfringement extends ParseValue {
    boolean isExist = false;

    public Parse73AvoidInfringement() {
        label = "是否有任何规避专利侵权风险的措施";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false){
            isExist = true;

        }
    }



}
