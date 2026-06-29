package com.sfccn.pdftool.parse.zhaogu;

import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.parse.ParseValue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 是否是领域巨头的供应商
 * 看客户是否是领域巨头
 */
public class Parse84BigSupplier extends ParseValue {
    boolean isExist = false;

    public Parse84BigSupplier() {
        label = "是否是领域巨头的供应商";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false){
            isExist = true;
            addElementValue("参考前五大客户信息");
//            value.add("参考前五大客户信息");
//            addValue(Arrays.asList("是否是领域巨头的供应商", ""));
        }
    }

}
