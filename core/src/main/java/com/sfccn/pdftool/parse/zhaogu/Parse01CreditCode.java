package com.sfccn.pdftool.parse.zhaogu;

import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.libary.CustomPDFDomTree;
import com.sfccn.pdftool.parse.ParseValue;
import com.sfccn.pdftool.utils.ToolUtil;
import org.apache.commons.lang3.StringUtils;
import org.fit.pdfdom.TextMetrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 统一社会信用代码
 */
public class Parse01CreditCode extends ParseValue {
    boolean isExist = false;
    public Parse01CreditCode() {
        label = "统一社会信用代码";
    }

    public void runRow(Map<Integer,List<Axis>> pageLineMap,List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex){
        if(isExist == false){
            String content = getRowContent(row);
            if(content.indexOf("统一社会信用代码") > -1){
                StringBuffer sff = new StringBuffer();
                sff.append(getRowContent(rowList.get(rowindex)));
                sff.append(getRowContent(rowList.get(rowindex + 1)));
                String code = ToolUtil.findText(sff.toString(), "([0-9A-Z]{16,18})");
                if(StringUtils.isEmpty(code) == false){
                    marks.addAll(row);
//                    value.add(code);
                    addElementValue(code);
                    isExist = true;
                }
            }
        }
    }
}
