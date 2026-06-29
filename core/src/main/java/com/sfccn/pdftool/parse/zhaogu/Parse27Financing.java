package com.sfccn.pdftool.parse.zhaogu;

import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.parse.ParseValue;
import com.sfccn.pdftool.utils.BoolUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 是否完成过股权融资
 * 关键字”股权融资“、“股权变动”、“增资”，看是否存在股权融资
 */
public class Parse27Financing extends ParseValue {
    boolean isExist = false;
    int maxIndex = 0;
    public Parse27Financing() {
        label = "是否完成过股权融资";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false && rowindex > maxIndex){
            StringBuffer valueTemp = new StringBuffer();
            String temp = getRowContent(row);
            if(BoolUtil.in(temp, Arrays.asList("股权融资","股权变动","增发股份","新增注册资本","定向增发"))){
                valueTemp.append(getRowContent(rowList.get(rowindex-1)));
                valueTemp.append(getRowContent(rowList.get(rowindex)));
                valueTemp.append(getRowContent(rowList.get(rowindex+1)));
                marks.addAll(rowList.get(rowindex-1));
                marks.addAll(rowList.get(rowindex));
                marks.addAll(rowList.get(rowindex+1));
                //value.add(valueTemp.toString());
                addElementValue(marks, "", valueTemp.toString());
                marks.clear();
            }
            else if(temp.indexOf("增资") > -1){
                valueTemp.append(getRowContent(rowList.get(rowindex-1)));
                valueTemp.append(getRowContent(rowList.get(rowindex)));
                valueTemp.append(getRowContent(rowList.get(rowindex + 1)));
                if(valueTemp.indexOf("注册资本") > -1){
//                    value.add(valueTemp.toString());
                    marks.addAll(rowList.get(rowindex-1));
                    marks.addAll(rowList.get(rowindex));
                    marks.addAll(rowList.get(rowindex+1));
                    addElementValue(marks, "",valueTemp.toString());
                    marks.clear();
                }
            }
            else if(temp.indexOf("注册资本") > -1 && temp.indexOf("增至") > -1){
                valueTemp.append(getRowContent(rowList.get(rowindex-1)));
                valueTemp.append(getRowContent(rowList.get(rowindex)));
                valueTemp.append(getRowContent(rowList.get(rowindex + 1)));

                marks.addAll(rowList.get(rowindex-1));
                marks.addAll(rowList.get(rowindex));
                marks.addAll(rowList.get(rowindex+1));
                addElementValue(marks, "", valueTemp.toString());
                marks.clear();
            }
            maxIndex = rowindex + 1;
        }
    }
}
