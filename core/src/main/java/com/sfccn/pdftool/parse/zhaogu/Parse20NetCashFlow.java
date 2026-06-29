package com.sfccn.pdftool.parse.zhaogu;

import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.libary.CustomPDFDomTree;
import com.sfccn.pdftool.libary.IntTreeMap;
import com.sfccn.pdftool.parse.ParseValue;
import com.sfccn.pdftool.utils.ToolUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.fit.pdfdom.TextMetrics;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 近一财年经营活动现金流净额
 */
public class Parse20NetCashFlow extends ParseValue {
    boolean isExist = false;

    public Parse20NetCashFlow() {
        label = "近一财年经营活动现金流净额";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap,List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false){
            String temp = getRowContent(row);
            if((temp.indexOf("现金流量表") > -1)
                    && temp.indexOf("主要数据") == -1 && temp.indexOf("母公司") == -1
                    && temp.indexOf("现金流量表主要财务数据") == -1 && temp.indexOf("现金流量表主要财务数据") == -1){
//                System.out.println("合并现金流量表＝＝＝＝＝＝＝＝＝＝" + temp);
                if(isTable(rowList, rowindex, 6)){
                    addElementValue(row, "", temp);
                    // Map<y1,Map<x1,String>>
                    Map<Integer, Map<Integer, String>> gridMap = findGrid(pageLineMap, rowList, rowindex);
                    if(gridMap.size() > 0){
                        for(Map.Entry<Integer, Map<Integer, String>> rowEntry:gridMap.entrySet()){
                            int x = rowEntry.getKey();
                            List<String> columns = rowEntry.getValue().values().stream().collect(Collectors.toList());
                            if(columns != null && columns.get(0).indexOf("经营活动产生的现金流量净额") > -1 && columns.size() >= 4){

                                String valueString = columns.get(columns.size() - 3).replace(",", "");
                                if(StringUtils.isEmpty(valueString) == false && !valueString.equals("-") ) {
                                    Double value1 = ToolUtil.string2Double(Double.valueOf(valueString), 10000);
                                    addElementValue(null, String.valueOf(value1), "");
                                }
                                isExist = true;
                                break;
                            }
                        }
                    }
//                    System.out.println("gridMap========" + gridMap);
                }
            }
        }
    }

}
