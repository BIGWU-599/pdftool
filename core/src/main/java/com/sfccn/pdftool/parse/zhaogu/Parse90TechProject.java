package com.sfccn.pdftool.parse.zhaogu;

import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.parse.ParseValue;
import com.sfccn.pdftool.utils.BoolUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 是否承担国家重大科技专项
 * 范围是企业和个人，如果是个人，则要看个人是否是在公司在职的情况下（不包括外聘和顾问）
 */
public class Parse90TechProject extends ParseValue {
    boolean isExist = false;
//    List value = new ArrayList();
    private int maxIndex = 0;

    public Parse90TechProject() {
        label = "是否承担国家重大科技专项";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false && rowindex > maxIndex){
            if(rowindex >= (rowList.size() - 1)){
                return;
            }
            StringBuffer temp = new StringBuffer()
                    .append(getRowContent(row)).append(getRowContent(rowList.get(rowindex + 1)));
            String contentTemp = temp.toString();
            // 重大科研项目

            if(BoolUtil.in(contentTemp, Arrays.asList("参与","承担","负责")) &&
                    contentTemp.indexOf("国家") > -1 &&
                    BoolUtil.notIn(contentTemp, Arrays.asList("国家标准","国家和行业")) &&
                    BoolUtil.in(contentTemp, Arrays.asList("计划","项目","专项"))){
                StringBuffer sff = new StringBuffer();
                sff.append(getRowContent(rowList.get(rowindex)));
                sff.append(getRowContent(rowList.get(rowindex + 1)));
                sff.append(getRowContent(rowList.get(rowindex + 2)));
//                value.add(sff.toString());
                marks.addAll(rowList.get(rowindex));
                marks.addAll(rowList.get(rowindex + 1));
                marks.addAll(rowList.get(rowindex + 2));
                maxIndex = rowindex + 2;
                addElementValue(marks, "", sff.toString());
                marks.clear();
            }
        }

    }
}
