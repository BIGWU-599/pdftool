package com.sfccn.pdftool.parse.zhaogu;

import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.parse.ParseValue;
import com.sfccn.pdftool.utils.BoolUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 是否有自主研发已经产业化科技产品
 * “产业化”、“份额”
 */
public class Parse93IndustryProduct extends ParseValue {
    boolean isExist = false;
    int maxIndex = 0;
    public Parse93IndustryProduct() {
        label = "是否有自主研发已经产业化科技产品";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false && rowindex > maxIndex){
            String temp = getRowContent(row);
            if(temp.indexOf("产业化") > -1) {
                StringBuffer sff = new StringBuffer();
                sff.append(getRowContent(rowList.get(rowindex)));
                sff.append(getRowContent(rowList.get(rowindex + 1)));
                if (BoolUtil.notIn(sff.toString(),Arrays.asList("...","国务院发布","发改委")) && sff.length() > 40) {
                    marks.addAll(rowList.get(rowindex));
                    marks.addAll(rowList.get(rowindex + 1));
                    maxIndex = rowindex + 1;
                    addElementValue(marks, "", sff.toString());
                    marks.clear();

                    if(value.size() >= 20){
                        isExist = true;
                    }
                }


            }
        }
    }
}
