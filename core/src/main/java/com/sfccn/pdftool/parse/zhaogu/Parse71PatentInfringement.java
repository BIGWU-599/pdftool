package com.sfccn.pdftool.parse.zhaogu;

import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.parse.ParseValue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 是否有过专利侵权诉讼或接到过专利侵权警告律师函
 */
public class Parse71PatentInfringement extends ParseValue {
    boolean isExist = false;
    private int maxIndex = 0;
    public Parse71PatentInfringement() {
        label = "是否有过专利侵权诉讼或接到过专利侵权警告律师函";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false && rowindex > maxIndex){
            String temp = getRowContent(row);
            if(temp.indexOf("侵犯") > -1 || temp.indexOf("侵权") > -1){
                StringBuffer sff = new StringBuffer();
                sff.append(getRowContent(rowList.get(rowindex -1)));
                sff.append(getRowContent(rowList.get(rowindex)));
                sff.append(getRowContent(rowList.get(rowindex + 1)));
                if(sff.indexOf("专利") > -1){
//                    value.add(sff.toString());
                    marks.addAll(rowList.get(rowindex -1));
                    marks.addAll(rowList.get(rowindex));
                    marks.addAll(rowList.get(rowindex + 1));
                    addElementValue(marks, "",sff.toString());
                    marks.clear();
                    maxIndex = maxIndex + 1;
//                    isExist = true;
                }
            }

        }
    }
}
