package com.sfccn.pdftool.parse.zhaogu;

import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.parse.ParseValue;
import com.sfccn.pdftool.utils.ToolUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 拥有已经授权的专利数量
 */
public class Parse43Patent extends ParseValue {
    boolean isExist = false;

    public Parse43Patent() {
        label = "拥有已经授权的专利数量";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false){
            String temp = getRowContent(row);

            if((temp.indexOf("专利") > -1 && temp.length() < 7) || (temp.indexOf("自有专利") > -1 && temp.length() < 10)){
                if(isTable(rowList, rowindex, 7)){
                    Map<Integer, Map<Integer, String>> gridMap = findGrid(pageLineMap, rowList, rowindex,20,10);
//                    System.out.println("gridMap=======gridMap:" + gridMap);
                    if(gridMap.size() > 0){
                        for(Map.Entry<Integer, Map<Integer, String>> rowEntry:gridMap.entrySet()){
                            Integer y = rowEntry.getKey();
//                            System.out.println("gridMap=======rowEntry:" + rowEntry + ",y:" +y);

                            List<String> columns = rowEntry.getValue().values().stream().collect(Collectors.toList());
                            String strTemp = String.join(",", columns);
//                            System.out.println("gridMap=======strTemp:" + strTemp);
                            if((strTemp.indexOf("专利号") > -1 || strTemp.indexOf("名称") > -1)
                                    && (strTemp.indexOf("日期") > -1 || strTemp.indexOf("申请日") > -1 || strTemp.indexOf("取得方式") > -1)){
                               //addElementValue(row,"", temp);
                               StringBuffer sff = new StringBuffer();
                               ArrayList<PdfObject> markTemp = new ArrayList();
                               for(int r = rowindex; r < (rowindex + 4);r++){
                                   List<PdfObject> rowTemp = rowList.get(r);
                                   int y2 = rowTemp.get(0).getPagenum() * 10000 + rowTemp.get(0).getY();

                                   if(y2 < y){
                                       sff.append(getRowContent(rowTemp));
                                       markTemp.addAll(rowTemp);
                                   }
                               }
                               addElementValue(markTemp, "", sff.toString());
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
