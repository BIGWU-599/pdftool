package com.sfccn.pdftool.parse.zhaogu;

import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.parse.ParseValue;
import com.sfccn.pdftool.utils.BoolUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 主要客户类型
 */
public class Parse24Consumer extends ParseValue {
    public final Logger log = LogManager.getLogger(Parse24Consumer.class);
    boolean isExist = false;
    private int maxIndex = 0;
    public Parse24Consumer() {
        label = "主要客户类型";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false && rowindex > maxIndex){
            String temp = getRowContent(row);
            /////// 前五大客户
            if(BoolUtil.in(temp, Arrays.asList("前五大客户","前五名客户","前十大客户","前十名客户","主要客户","前五名销售客户"))
                && temp.indexOf("账款") == -1){
//                System.out.println(rowindex + "主要客户类型======" + temp);
                if(isTable(rowList, rowindex, 6) == true){
//                    System.out.println("主要客户类型======table");
                    Map<Integer, Map<Integer, String>> gridMap = findGrid(pageLineMap, rowList, rowindex);
//                    for(Map.Entry<Integer, Map<Integer, String>> rowEntry:gridMap.entrySet()){
//                        List<String> columns = rowEntry.getValue().values().stream().collect(Collectors.toList());
//                        System.out.println("主要客户类型======columns:" + columns);
//                    }
                    if(gridMap.size() > 0){
                        maxIndex = rowindex + 20;
                        int r = 0;
                        for(Map.Entry<Integer, Map<Integer, String>> rowEntry:gridMap.entrySet()){
                            r++;
                            if(r > 3 && isExist == false){
                                break;
                            }
                            List<String> columns = rowEntry.getValue().values().stream().collect(Collectors.toList());
//                            System.out.println("主要客户类型======columns:" + columns);
                            String strTemp = String.join(",", columns);
                            if(BoolUtil.in(strTemp, Arrays.asList("客户", "名称"))
                                && BoolUtil.in(strTemp, Arrays.asList("金额","销售","收入"))
                                && (strTemp.indexOf("供应商") == -1) && isExist == false){
                                isExist = true;
//                                System.out.println("主要客户类型====rowindex:" + rowindex + ",maxIndex:" + maxIndex + "," + temp);
                                addElementValue(row,"", temp);
                            }
                            if(isExist == true){
                                addElementValue(null,"", strTemp);
                            }
                            if(value.size() > 5){
                                break;
                            }
                        }
                    }
                }

            }
        }
    }
}
