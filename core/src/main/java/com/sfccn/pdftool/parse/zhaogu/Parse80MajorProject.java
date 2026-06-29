package com.sfccn.pdftool.parse.zhaogu;

import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.parse.ParseValue;
import com.sfccn.pdftool.utils.BoolUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 是否获得国家重大立项
 * 关键字”国家重大“、”重大立项“、“重大专项”。范围是企业和个人，如果是个人，则要看个人是否是在公司在职的情况下（不包括外聘和顾问）
 */
public class Parse80MajorProject extends ParseValue {
    boolean isExist = false;

    public Parse80MajorProject() {
        label = "是否获得国家重大立项";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false){
            String temp = getRowContent(row);
            if(BoolUtil.in(temp, Arrays.asList("重大立项", "重大专项"))
                && BoolUtil.notIn(temp, Arrays.asList("半导体协会", "国务院","将集成电路装备等列为"))){
                StringBuffer sff = new StringBuffer();
                sff.append(getRowContent(rowList.get(rowindex)));
                sff.append(getRowContent(rowList.get(rowindex + 1)));
                sff.append(getRowContent(rowList.get(rowindex + 2)));

                marks.addAll(rowList.get(rowindex));
                marks.addAll(rowList.get(rowindex + 1));
                marks.addAll(rowList.get(rowindex + 2));
                addElementValue(marks, "",sff.toString());
                marks.clear();
            }
        }
    }
}
