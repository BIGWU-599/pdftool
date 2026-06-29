package com.sfccn.pdftool.parse.zhaogu;

import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.parse.ParseValue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 拥有已经授权的软著数量
 */
public class Parse40SoftRight extends ParseValue {
    boolean isExist = false;

    public Parse40SoftRight() {
        label = "拥有已经授权的软著数量";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false){
            String content = getRowContent(row);
            if((content.indexOf("软件著作权") > -1 && content.length() <= 10)
                || (content.indexOf("计算机软件著作权") > -1 && content.length() <= 15) ){
                if(isTable(rowList, rowindex, 7)){
//                    for(int r = 1; r < 5; r++){
                    StringBuffer sff = new StringBuffer();
                    sff.append(getRowContent(rowList.get(rowindex)));
                    sff.append(getRowContent(rowList.get(rowindex + 1)));
                    sff.append(getRowContent(rowList.get(rowindex + 2)));
                    marks.addAll(rowList.get(rowindex));
                    marks.addAll(rowList.get(rowindex + 1));
                    marks.addAll(rowList.get(rowindex + 2));
                    addElementValue(marks, "",sff.toString());
                    isExist = true;
                }
            }
        }
    }
}
