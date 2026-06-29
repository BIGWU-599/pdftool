package com.sfccn.pdftool.parse.zhaogu;

import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.parse.ParseValue;
import com.sfccn.pdftool.utils.BoolUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 员工总数
 */
public class Parse07Employee extends ParseValue {
    boolean isExist = false;
    StringBuffer employCount = new StringBuffer();

    public Parse07Employee() {
        label = "员工总数";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false){
            String temp = getRowContent(row);
            if(temp.indexOf("员工人数") > -1){
                // 遍历后边10行数据，查找研发人员、员工总数
                boolean isIndex = false;
                for(int r = 1; r < 30; r++){
                    List<PdfObject> rowTemp = rowList.get(rowindex + r);
                    String temp2 = getRowContent(rowTemp);
                    if(BoolUtil.in(temp2,Arrays.asList("员工总数", "员工人数"))){
//                        System.out.println(r+",Parse07Employee======"+temp2);
                        if(isIndex == false){
                            isIndex = true;
                            addElementValue(rowTemp, "", temp2);
                        }
                        for (int c = 0; c < rowTemp.size(); c++){
                            String data = rowTemp.get(c).getData();
                            if(BoolUtil.in(data,Arrays.asList("员工总数", "公司员工人数", "员工人数"))
                                && (c + 1) < rowTemp.size()){
                                employCount.append(rowTemp.get(c + 1).getData());
                                //value.add(employCount.toString());
                                addElementValue(rowTemp, employCount.toString());
                                isExist = true;
                                break;
                            }
                        }
                    }
                    if(employCount.length() > 0){
                        break;
                    }
                }
            }
        }

    }
}
