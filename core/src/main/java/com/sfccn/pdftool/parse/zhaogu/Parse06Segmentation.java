package com.sfccn.pdftool.parse.zhaogu;

import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.parse.ParseValue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 所属细分领域
 */
public class Parse06Segmentation extends ParseValue {
    public boolean isExist = false;

    public Parse06Segmentation() {
        label = "所属细分领域";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false){
            String temp = getRowContent(row);
            if(temp.indexOf("发行人主营业务经营情况") > -1 || temp.indexOf("主营业务情况") > -1
                 || temp.indexOf("发行人的主营业务经营情况") > -1 || temp.indexOf("发行人的主营业务经营情况") > -1
                 || temp.indexOf("发行人主营业务经营情况") > -1){

                StringBuffer sff = new StringBuffer();
                sff.append(getRowContent(rowList.get(rowindex + 1)));
                sff.append(getRowContent(rowList.get(rowindex + 2)));
                sff.append(getRowContent(rowList.get(rowindex + 3)));
//                sff.append(getRowContent(rowList.get(rowindex + 4)));

//                System.out.println("所属细分领域1====" + temp);
//                System.out.println("所属细分领域2====" + sff.toString());
                if(sff.indexOf("一家") > -1 || sff.indexOf("从事") > -1 || sff.indexOf("主营业务为") > -1
                        || sff.indexOf("主营业务是") > -1 || sff.indexOf("面向") > -1){
//                    value.add(sff.toString());
                    addElementValue(row, "", sff.toString());
                }
            }

        }
    }
}
