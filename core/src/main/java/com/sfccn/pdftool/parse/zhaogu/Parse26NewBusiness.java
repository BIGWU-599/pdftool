package com.sfccn.pdftool.parse.zhaogu;

import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.libary.CustomPDFDomTree;
import com.sfccn.pdftool.parse.ParseValue;
import com.sfccn.pdftool.utils.ToolUtil;
import org.lionsoul.jcseg.IWord;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 是否属于新业态
 * 关键字”新产业“、”新行业“、”新形态“、”新业态“，看是否包含这些关键字
 */
public class Parse26NewBusiness extends ParseValue {
    boolean isExist = false;
    int maxIndex = 0;
    public Parse26NewBusiness() {
        label = "是否属于新业态";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(rowindex > maxIndex && rowindex < (rowList.size() - 2)){
            String temp = getRowContent(row);
            if((temp.indexOf("新产业") > -1 || temp.indexOf("新行业") > -1 ||
                    temp.indexOf("新形态") > -1 || temp.indexOf("新业态") > -1
                )
                && (temp.indexOf("创新产业") == -1 && temp.indexOf("更新产业") == -1 && temp.indexOf("高新产业") == -1
                    && temp.indexOf("最新行业") == -1)
                    ){
                //System.out.println("Parse26NewBusiness======" + rowindex + "," +rowList.size() + ",temp:" + temp);
                StringBuffer valueSff = new StringBuffer();
                valueSff.append(getRowContent(rowList.get(rowindex-1)));
                valueSff.append(getRowContent(rowList.get(rowindex)));
                valueSff.append(getRowContent(rowList.get(rowindex+1)));
                maxIndex = rowindex + 1;
                addElementValue(row, "", temp);

            }
        }

    }



}
