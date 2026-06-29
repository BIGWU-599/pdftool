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
 * 质量最高或价值最大的”已授权自主研发发明专利号(英文逗号分隔，<=5)
 */
public class Parse75PatentValueMax extends ParseValue {
    boolean isExist = false;

    public Parse75PatentValueMax() {
        label = "质量最高或价值最大的”已授权自主研发发明专利号(英文逗号分隔，<=5)";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false){
            String temp = getRowContent(row);
            if((temp.indexOf("专利名称") > -1 || temp.indexOf("专利权人") > -1) && temp.indexOf("专利号") > -1){
                for(int r2 = rowindex + 1; r2 < (rowindex + 300); r2++){
                    List<PdfObject> row2 = rowList.get(r2);
                    String patentnum = null;
                    PdfObject patentObj = null;
                    boolean isInvention = false;
                    for(PdfObject pdfObject:row2){
                        String content = pdfObject.getData().trim();
                        String patentnumTemp = ToolUtil.findText(content,"([a-zA-Z0-9.]{14,16})");
                        if(patentnum == null){
                            patentnum = patentnumTemp;
                            patentObj = pdfObject;
                        }
                        if(content.equals("发明")){
                            isInvention = true;
//                            patentObj = null;
                        }
                    }
                    if(patentnum != null && isInvention == true && value.size() < 5){
                        marks.add(patentObj);
//                        value.add(patentnum);
                        addElementValue(marks, "",patentnum);
                        marks.clear();
                    }
                    if(value.size() >= 5){
                        isExist = true;
                        break;
                    }
                }
            }
        }
    }

}
