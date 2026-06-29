package com.sfccn.pdftool.parse.zhaogu;

import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.parse.ParseValue;
import com.sfccn.pdftool.utils.BoolUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 是否有望取代西方技术产品
 * 是否有“进口替代”
 */
public class Parse85ReplaceTech extends ParseValue {
    boolean isExist = false;
//    List value = new ArrayList();
    private int maxIndex = 0;

    public Parse85ReplaceTech() {
        label = "是否有望取代西方技术产品";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false && (rowindex + 1) < rowList.size() && maxIndex < rowindex){
            String temp = getRowContent(row) + "," + getRowContents(rowList.get(rowindex + 1));
            if(BoolUtil.in(temp, Arrays.asList("进口替代", "替代国外","填补了国内空白"))
                    || (temp.indexOf("进口产品") > -1 && temp.indexOf("替代") > -1)){
                StringBuffer sff = new StringBuffer();
                sff.append(getRowContent(rowList.get(rowindex -1)));
                sff.append(getRowContent(rowList.get(rowindex)));
                sff.append(getRowContent(rowList.get(rowindex + 1)));
//                value.add(sff.toString());
                marks.addAll(rowList.get(rowindex - 1));
                marks.addAll(rowList.get(rowindex));
                marks.addAll(rowList.get(rowindex + 1));
                maxIndex = rowindex + 1;
                addElementValue(marks, "",sff.toString());
                marks.clear();
                if(value.size() >=10){
                    isExist = true;
                }
            }
            //addValue(Arrays.asList("是否有望取代西方技术产品", ""));
        }

    }
}
