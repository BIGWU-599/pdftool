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
 * 是否是行业的第一梯队
 * 关键字”行业领先“、”第一梯队“
 */
public class Parse83IndustryFirst extends ParseValue {
    boolean isExist = false;
//    List value = new ArrayList();

    public Parse83IndustryFirst() {
        label = "是否是行业的第一梯队";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false){
//            isExist = true;
            // 发行人是国内DCS市场龙头企业
            String temp = getRowContent(row);
            if(temp.indexOf("行业领先") > -1 || temp.indexOf("第一梯队") > -1 || temp.indexOf("市场领先") > -1
                || temp.indexOf("行业的领先地位") > -1){
                StringBuffer sff = new StringBuffer();
                sff.append(getRowContent(rowList.get(rowindex -1)));
                sff.append(getRowContent(rowList.get(rowindex)));
                sff.append(getRowContent(rowList.get(rowindex + 1)));
                if(sff.indexOf("行业领先的技术") == -1 && sff.indexOf("行业领先的工程") == -1 &&
                    sff.indexOf("项目经验行业领先") == -1){
//                    value.add(sff.toString());
                    marks.addAll(rowList.get(rowindex -1));
                    marks.addAll(rowList.get(rowindex));
                    marks.addAll(rowList.get(rowindex + 1));
                    addElementValue(marks, "",sff.toString());
                    marks.clear();
                }

//                isExist = true;
            }
        }

    }
}
