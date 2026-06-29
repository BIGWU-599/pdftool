package com.sfccn.pdftool.parse.zhaogu;

import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.parse.ParseValue;
import com.sfccn.pdftool.utils.BoolUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 研发人员总数
 */
public class Parse08EmployeeTech extends ParseValue {
    boolean isExist = false;

    public Parse08EmployeeTech() {
        label = "研发人员总数";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false){
            String temp = getRowContent(row);
            if(BoolUtil.in(temp, Arrays.asList("员工专业结构", "员工专业构成", "员工人数", "员工结构"))){
                // 遍历后边10行数据，查找研发人员、员工总数
//                System.out.println("temp=======" + temp);
                if(isTable(rowList, rowindex, 6) == true){
                    Map<Integer, Map<Integer, String>> gridMap = findGrid(pageLineMap, rowList, rowindex);
                    if(gridMap.size() > 0){
                        for(Map.Entry<Integer, Map<Integer, String>> rowEntry:gridMap.entrySet()){
                            int x = rowEntry.getKey();
                            List<String> columns = rowEntry.getValue().values().stream().collect(Collectors.toList());
                            String temp2 = columns.get(0);
                            if(BoolUtil.in(temp2, Arrays.asList("研发类","研发人员","研发技术人员","技术人员","研发与技术")) && columns.size() >= 2){
                                String valueString = columns.get(1);
                                addElementValue(null, valueString, "");
                                addElementValue(row, "", temp);
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
