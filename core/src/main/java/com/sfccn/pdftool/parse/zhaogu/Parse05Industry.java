package com.sfccn.pdftool.parse.zhaogu;

import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.libary.CustomPDFDomTree;
import com.sfccn.pdftool.parse.ParseValue;
import com.sfccn.pdftool.utils.ToolUtil;
import org.fit.pdfdom.TextMetrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 行业分类
 */
public class Parse05Industry extends ParseValue {
    boolean isExist = false;

    public Parse05Industry() {
        label = "所属行业";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false){
            String temp = getRowContent(row);
                          // 发行人基本情况
            if(temp.indexOf("发行人基本情况") > -1){
//                System.out.println("1发行人基本情况=====row=" + rowindex + "," +  ToolUtil.toJson(row));
                if(isTable(rowList, rowindex, 7)){
//                    System.out.println("2发行人基本情况=====row=" + ToolUtil.toJson(row));
                    Map<Integer, Map<Integer, String>> gridMap = findGrid(pageLineMap, rowList, rowindex, 71, 20);

                    if(gridMap.size() > 0){
                        for(Map.Entry<Integer, Map<Integer, String>> rowEntry:gridMap.entrySet()){
//                            System.out.println("所属行业======" + ToolUtil.toJson(rowEntry));
                            List<String> columns = rowEntry.getValue().values().stream().collect(Collectors.toList());
                            for(int r = 0; r < columns.size(); r++){
                                if(columns.get(r).indexOf("行业分类") > -1){
                                    addElementValue(row,"", temp);
                                    if(((r + 1) < columns.size())){
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
            }
        }

    }
}
