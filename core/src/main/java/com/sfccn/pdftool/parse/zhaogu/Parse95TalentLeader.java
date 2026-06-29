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
 * 聘请院士/国千（国家“千人计划”）/省千（省“千人计划”）/长江学者 等领军人物数量
 * 关键字“院士”“国家千人计划”“省千人计划”“国千”“省千”“长江学者”“行业领军人物”
 */
public class Parse95TalentLeader extends ParseValue {
    boolean isExist = false;
    private int maxIndex = 0;
    public Parse95TalentLeader() {
        label = "聘请院士/国千（国家“千人计划”）/省千（省“千人计划”）/长江学者 等领军人物数量";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false && rowindex > maxIndex){
//            isExist = true;
            String temp = getRowContent(row);
            // 我国千伏
            if(temp.indexOf("院士") > -1 || temp.indexOf("千人计划") > -1 || temp.indexOf("国千") > -1
                || temp.indexOf("省千") > -1 || temp.indexOf("长江学者") > -1 || temp.indexOf("领军人才") > -1
                || temp.indexOf("领军人物") > -1){
                StringBuffer sff = new StringBuffer();
                sff.append(getRowContent(rowList.get(rowindex -1)));
                sff.append(getRowContent(rowList.get(rowindex)));
                sff.append(getRowContent(rowList.get(rowindex + 1)));
//                value.add(sff.toString());
                marks.addAll(rowList.get(rowindex -1));
                marks.addAll(rowList.get(rowindex));
                marks.addAll(rowList.get(rowindex + 1));
                maxIndex = rowindex + 1;
                addElementValue(marks, "", sff.toString());
                marks.clear();
            }
        }

    }
}
