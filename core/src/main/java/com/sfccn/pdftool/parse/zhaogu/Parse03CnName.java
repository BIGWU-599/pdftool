package com.sfccn.pdftool.parse.zhaogu;

import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.libary.CustomPDFDomTree;
import com.sfccn.pdftool.parse.ParseValue;
import org.apache.commons.lang3.StringUtils;
import org.fit.pdfdom.TextMetrics;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 企业名称
 */
public class Parse03CnName extends ParseValue {
    boolean isExist = false;

    public Parse03CnName() {
        label = "企业名称";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false){
            String temp = getRowContent(row);
            if(temp.indexOf("发行人基本情况") > -1){
                if(isTable(rowList, rowindex, 6)){
                    Map<Integer, Map<Integer, String>> gridMap = findGrid(pageLineMap, rowList, rowindex);
                    if(gridMap.size() > 0){
                        for(Map.Entry<Integer, Map<Integer, String>> rowEntry:gridMap.entrySet()){
                            List<String> columns = rowEntry.getValue().values().stream().collect(Collectors.toList());
//                            System.out.println("Parse03CnName====" + columns);
                            for(int r = 0; r < columns.size() - 1; r++){
                                String data = columns.get(r);
                                if(data.indexOf("中文名称") > -1 || data.indexOf("发行人名称") > -1 || data.indexOf("公司名称") > -1
                                        || data.indexOf("发行人名称（中文）") > -1){
                                    addElementValue(row,"", temp);
                                    System.out.println("");
                                    if((r + 1) < columns.size()){
                                        addElementValue(null,columns.get(r + 1), "");
                                    }
                                    isExist = true;
                                    break;
                                }
                            }
                            if(isExist == true){
                                break;
                            }
                        }
                    }
                }
//                for(int r = 1; r < 20; r++){
//                    List<PdfObject> rowTemp = rowList.get(rowindex + r);
//                    for (int c = 0; c < rowTemp.size() - 1; c++){
//                        String data = rowTemp.get(c).getData();
//                        if(data.indexOf("中文名称") > -1 || data.indexOf("发行人名称") > -1 || data.indexOf("公司名称") > -1
//                                || data.indexOf("发行人名称（中文）") > -1){
//                            cnname = rowTemp.get(c + 1).getData();
//                            marks.add(rowTemp.get(c + 1));
//                            addElementValue(row, "", temp);
//                            break;
//                        }
//                    }
//                    String tempStr = getRowContent(rowTemp);
//                    if(tempStr.indexOf("中介机构") > -1){
//                        cnname = null;
//                        marks.clear();
//                        break;
//                    }
//                }
//                if(cnname != null){
//                    addElementValue(cnname);
//                    isExist = true;
//                }
            }
        }

    }
}
