package com.sfccn.pdftool.parse.zhaogu;

import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.parse.ParseValue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 拥有已经授权的电路布图数量
 */
public class Parse42CircuitDiagram extends ParseValue {
    boolean isExist = false;

    public Parse42CircuitDiagram() {
        label = "拥有已经授权的电路布图数量";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false){
            //            集成电路布图
            String temp = getRowContent(row);
            if(temp.indexOf("集成电路布图") > -1 && temp.length() < 20){
                if(isTable(rowList, rowindex, 8)) {
                    addElementValue(row, "", temp);
                    // Map<y1,Map<x1,String>>
                    Map<Integer, Map<Integer, String>> gridMap = findGrid(pageLineMap, rowList, rowindex, 10, 5);
                    if(gridMap.size() > 0) {
                        for (Map.Entry<Integer, Map<Integer, String>> rowEntry : gridMap.entrySet()) {
                            int y = rowEntry.getKey();
                            List<String> columns = rowEntry.getValue().values().stream().collect(Collectors.toList());
                            String strTemp = String.join(",", columns);
                            if (strTemp.indexOf("设计") > -1 && strTemp.indexOf("证书") > -1) {

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
