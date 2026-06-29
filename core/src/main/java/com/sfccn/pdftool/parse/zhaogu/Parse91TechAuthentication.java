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
 * 是否有国家就技术/产品国际领先或国家战略的官方认证
 * “鉴定”、“科技成果”、“技术成果”、“认证”
 */
public class Parse91TechAuthentication extends ParseValue {
    boolean isExist = false;

    public Parse91TechAuthentication() {
        label = "是否有国家就技术/产品国际领先或国家战略的官方认证";
    }

    //    List value = new ArrayList();
    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false){
            String contentTemp = getRowContent(row);
            if((contentTemp.indexOf("鉴定") > -1 || contentTemp.indexOf("认证") > -1 || contentTemp.indexOf("取得") > -1)
                    && (contentTemp.indexOf("科技成果") > -1 || contentTemp.indexOf("技术成果") > -1)){
                StringBuffer sff = new StringBuffer();
                sff.append(getRowContent(rowList.get(rowindex)));
                sff.append(getRowContent(rowList.get(rowindex + 1)));
                sff.append(getRowContent(rowList.get(rowindex + 2)));
//                value.add(sff.toString());
                marks.addAll(rowList.get(rowindex));
                marks.addAll(rowList.get(rowindex + 1));
                marks.addAll(rowList.get(rowindex + 2));
                addElementValue(marks, "",sff.toString());
                marks.clear();
            }

        }

    }
}
