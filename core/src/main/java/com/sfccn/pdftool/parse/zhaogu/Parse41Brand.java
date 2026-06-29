package com.sfccn.pdftool.parse.zhaogu;

import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.parse.ParseValue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 拥有已经授权的商标数量
 */
public class Parse41Brand extends ParseValue {
    boolean isExist = false;
    int maxIndex = 0;
    public Parse41Brand() {
        label = "拥有已经授权的商标数量";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false && rowindex > maxIndex){
            String temp = getRowContent(row);
            if(temp.indexOf("商标") > -1 && temp.length() < 7){
                if(isTable(rowList, rowindex, 8)) {
                    // Map<y1,Map<x1,String>>
                    Map<Integer, Map<Integer, String>> gridMap = findGrid(pageLineMap, rowList, rowindex, 10, 5);
                    if(gridMap.size() > 0) {
                        for (Map.Entry<Integer, Map<Integer, String>> rowEntry : gridMap.entrySet()) {
                            int y = rowEntry.getKey();
                            List<String> columns = rowEntry.getValue().values().stream().collect(Collectors.toList());
                            String strTemp = String.join(",", columns);
                            if (strTemp.indexOf("商标") > -1 && (strTemp.indexOf("注册") > -1 || strTemp.indexOf("类别") > -1 || strTemp.indexOf("分类") > -1)) {

                                StringBuffer sff = new StringBuffer();
                                sff.append(getRowContent(rowList.get(rowindex)));
                                sff.append(getRowContent(rowList.get(rowindex + 1)));
                                sff.append(getRowContent(rowList.get(rowindex + 2)));
                                sff.append(getRowContent(rowList.get(rowindex + 1)));

                                marks.addAll(rowList.get(rowindex));
                                marks.addAll(rowList.get(rowindex + 1));
                                marks.addAll(rowList.get(rowindex + 2));
                                marks.addAll(rowList.get(rowindex + 3));
                                addElementValue(marks, "",sff.toString());
                                marks.clear();
                                maxIndex = rowindex + 3;
                                isExist = true;
                                break;
                            }
                        }
                    }
                    else{
                        addElementValue(row, "", temp);
                    }
                }
            }
        }
    }
}
