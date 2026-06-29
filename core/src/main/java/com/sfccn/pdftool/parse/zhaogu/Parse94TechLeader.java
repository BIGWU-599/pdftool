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
 * 核心技术是否国际领先或国内领先
 * 关键字“国内领先”“国际领先”
 */
public class Parse94TechLeader extends ParseValue {
    boolean isExist = false;
    private int maxIndex = 0;
    public Parse94TechLeader() {
        label = "核心技术是否国际领先或国内领先";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false && rowindex > maxIndex){
            String temp = getRowContent(row);
            if(temp.indexOf("国内领先") > -1 || temp.indexOf("国际领先") > -1 || temp.indexOf("国内处于领先地位") > -1
                || temp.indexOf("领先水平") > -1 || temp.indexOf("国际先进水平") > -1){
                StringBuffer sff = new StringBuffer();
                sff.append(getRowContent(rowList.get(rowindex -1)));
                sff.append(getRowContent(rowList.get(rowindex)));
                sff.append(getRowContent(rowList.get(rowindex + 1)));

                if(sff.length() > 60){
                    marks.addAll(rowList.get(rowindex -1));
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
