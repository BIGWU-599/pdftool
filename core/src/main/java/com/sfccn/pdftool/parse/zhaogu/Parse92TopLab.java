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
 * 是否拥有或掌握一流的研发设施或实验室
 * 如企业被评为xxx技术中心，则表示这个企业拥有一流的实验室。
 */
public class Parse92TopLab extends ParseValue {
    boolean isExist = false;
//    List value = new ArrayList();

    public Parse92TopLab() {
        label = "是否拥有或掌握一流的研发设施或实验室";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false){
//            isExist = true;
            String contentTemp = getRowContent(row);
            if(contentTemp.indexOf("技术中心") > -1 || contentTemp.indexOf("开发中心") > -1 || contentTemp.indexOf("技术研究中心") > -1){

                StringBuffer sff = new StringBuffer();
                sff.append(getRowContent(rowList.get(rowindex)));
                sff.append(getRowContent(rowList.get(rowindex + 1)));
                sff.append(getRowContent(rowList.get(rowindex + 2)));

                List<PdfObject> markList = new ArrayList<>();
                markList.addAll(rowList.get(rowindex));
                markList.addAll(rowList.get(rowindex + 1));
                markList.addAll(rowList.get(rowindex + 2));
                addElementValue(markList, "", sff.toString());

                if(contentTemp.indexOf("评为") > -1 || contentTemp.indexOf("拥有") > -1 || contentTemp.indexOf("评定") > -1
                        || contentTemp.indexOf("认定") > -1 || contentTemp.indexOf("作为") > -1 || contentTemp.indexOf("拥有") > -1
                        || contentTemp.indexOf("获") > -1){
                    addElementValue(null, "是", "");
                }
            }
        }
    }
}
