package com.sfccn.pdftool.parse.zhaogu;

import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.libary.CustomPDFDomTree;
import com.sfccn.pdftool.parse.ParseValue;
import com.sfccn.pdftool.utils.ToolUtil;
import org.apache.commons.lang3.StringUtils;
import org.fit.pdfdom.TextMetrics;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 招股说明书签署日期
 */
public class Parse02SignDate extends ParseValue {
    boolean isExist = false;
    public Parse02SignDate() {
        label = "招股说明书签署日期";
    }

    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex){
        if(isExist == false){
            String temp = getRowContent(row);  // 招股说明书签署日期
            if(temp.indexOf("发行概况") > -1){
                if(isTable(rowList, rowindex, 6)){

                    Map<Integer, Map<Integer, String>> gridMap = findGrid(pageLineMap, rowList, rowindex);
                    if(gridMap.size() > 0){
                        for(Map.Entry<Integer, Map<Integer, String>> rowEntry:gridMap.entrySet()){
                            List<String> columns = rowEntry.getValue().values().stream().collect(Collectors.toList());
//                            System.out.println(rowindex + ",招股说明书签署日===" + ToolUtil.toJson(columns));
                            for(int r = 0; r < columns.size(); r++){
                                String temp2 = columns.get(r);
                                if(((r+1) < columns.size()) && (temp2.indexOf("招股说明书签署日期") > -1 || temp2.indexOf("招股意向书签署日期") > -1
                                        || temp2.indexOf("招股说明书签署日") > -1 || temp2.indexOf("签署日期") > -1)){
                                    addElementValue(row,"", temp);
                                    addElementValue(null,columns.get(r + 1), "");
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
