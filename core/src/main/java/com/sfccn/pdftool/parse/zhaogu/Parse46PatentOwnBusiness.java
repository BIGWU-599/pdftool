package com.sfccn.pdftool.parse.zhaogu;

import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.parse.ParseValue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 与主营相关的自主研发的已授权发明专利数量
 */
public class Parse46PatentOwnBusiness extends ParseValue {
    boolean isExist = false;

    public Parse46PatentOwnBusiness() {
        label = "与主营相关的自主研发的已授权发明专利数量";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false){
            isExist = true;
            addElementValue(null, "", "参考\"21、拥有已经授权的专利数量\"");
        }
    }
}
