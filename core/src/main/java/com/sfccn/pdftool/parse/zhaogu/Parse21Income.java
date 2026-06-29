package com.sfccn.pdftool.parse.zhaogu;

import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.libary.CustomPDFDomTree;
import com.sfccn.pdftool.parse.ParseValue;
import com.sfccn.pdftool.utils.ToolUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.fit.pdfdom.TextMetrics;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 合并利润表
 */
public class Parse21Income extends ParseValue {
    boolean isExist = false;

    public Parse21Income() {
        label = "近三个财年营收";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false){
            String temp = getRowContent(row);
            if((temp.indexOf("合并利润表") > -1 || temp.indexOf("利润表") > -1)
                    && temp.indexOf("主要数据") == -1 && temp.indexOf("母公司") == -1 && temp.indexOf("财务数据") == -1
                    && temp.indexOf("简要") == -1){
                if(isTable(rowList, rowindex, 6)){
                    Map<Integer, Map<Integer, String>> gridMap = findGrid(pageLineMap, rowList, rowindex);
//                    for(Map.Entry<Integer, Map<Integer, String>> rowEntry:gridMap.entrySet()){
//                        List<String> columns = rowEntry.getValue().values().stream().collect(Collectors.toList());
//                        System.out.println("Parse21Income===利润表:" + columns);
//                    }
                    if(gridMap.size() > 0){
                        for(Map.Entry<Integer, Map<Integer, String>> rowEntry:gridMap.entrySet()){
                            int x = rowEntry.getKey();
                            List<String> columns = rowEntry.getValue().values().stream().collect(Collectors.toList());
                            String temp2 = columns.get(0);
                            if(temp2.indexOf("营业总收入") > -1 || temp2.indexOf("营业收入") > -1){
                                if(columns.size() >= 4){
                                    String valueString1 = columns.get(columns.size() - 3).replace(",", "");
                                    String valueString2 = columns.get(columns.size() - 2).replace(",", "");
                                    String valueString3 = columns.get(columns.size() - 1).replace(",", "");


                                    if(NumberUtils.isCreatable(valueString1) && NumberUtils.isCreatable(valueString2) && NumberUtils.isCreatable(valueString3)
                                            && !valueString1.equals("-") && !valueString2.equals("-") && !valueString3.equals("-")){
                                        Double value1 = ToolUtil.string2Double(Double.valueOf(valueString1), 10000);
                                        Double value2 = ToolUtil.string2Double(Double.valueOf(valueString2), 10000);
                                        Double value3 = ToolUtil.string2Double(Double.valueOf(valueString3), 10000);

                                        addElementValue(null, String.valueOf(value1), "");
                                        addElementValue(null, String.valueOf(value2), "");
                                        addElementValue(null, String.valueOf(value3), "");

                                        addElementValue(row,"", temp);
                                        isExist = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
