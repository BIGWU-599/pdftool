package com.sfccn.pdftool.parse.zhaogu;

import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.parse.ParseValue;
import com.sfccn.pdftool.utils.BoolUtil;
import com.sfccn.pdftool.utils.ToolUtil;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 近三个财年净利润
 */
public class Parse22Profit extends ParseValue {
    boolean isExist = false;

    public Parse22Profit() {
        label = "近三个财年净利润";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false){
            StringBuffer sff = new StringBuffer();
            String temp = getRowContent(row);

            if(temp.indexOf("利润表") > -1 && BoolUtil.notIn(temp, Arrays.asList("合并利润表主要数据","简要合并利润表","母公司","财务数据","简要","影响"))){
                if(isTable(rowList, rowindex, 6) == true){
                    Map<Integer, Map<Integer, String>> gridMap = findGrid(pageLineMap, rowList, rowindex, 200, 30);
//                    System.out.println("净利润gridMap=====" + gridMap);
                    if(gridMap.size() > 0){
                        for(Map.Entry<Integer, Map<Integer, String>> rowEntry:gridMap.entrySet()){
                            int x = rowEntry.getKey();
                            List<String> columns = rowEntry.getValue().values().stream().collect(Collectors.toList());
//                            System.out.println("净利润columns=====" + columns);
                            String temp2 = columns.get(0);
                            if(temp2.indexOf("净利润") > -1 && temp2.indexOf("归属于母公司") == -1){
//                                System.out.println("净利润=====" + columns);
                                if(columns.size() >= 4){
                                    String valueString1 = columns.get(columns.size() - 3).replace(",", "");
                                    String valueString2 = columns.get(columns.size() - 2).replace(",", "");
                                    String valueString3 = columns.get(columns.size() - 1).replace(",", "");
                                    if(NumberUtils.isCreatable(valueString1) && NumberUtils.isCreatable(valueString2) && NumberUtils.isCreatable(valueString3)
                                            && !valueString1.equals("-") ){
                                        Double value1 = ToolUtil.string2Double(Double.valueOf(valueString1), 10000);
                                        Double value2 = ToolUtil.string2Double(Double.valueOf(valueString2), 10000);
                                        Double value3 = ToolUtil.string2Double(Double.valueOf(valueString3), 10000);

                                        addElementValue(null, String.valueOf(value1), "");
                                        addElementValue(null, String.valueOf(value2), "");
                                        addElementValue(null, String.valueOf(value3), "");

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
    }

    private Double[] getValue(List<PdfObject> row){
        PdfObject columnTemp1 = row.get(row.size() - 3);
        PdfObject columnTemp2 = row.get(row.size() - 2);
        PdfObject columnTemp3 = row.get(row.size() - 1);

        String valueString1 = columnTemp1.getData().replace(",", "");
        String valueString2 = columnTemp2.getData().toString().replace(",", "");
        String valueString3 = columnTemp3.getData().toString().replace(",", "");

        if(NumberUtils.isCreatable(valueString1) == false || NumberUtils.isCreatable(valueString2) == false || NumberUtils.isCreatable(valueString3) == false){
            return null;
        }
        else{
            Double value1 = ToolUtil.string2Double(Double.valueOf(valueString1), 10000);
            Double value2 = ToolUtil.string2Double(Double.valueOf(valueString2), 10000);
            Double value3 = ToolUtil.string2Double(Double.valueOf(valueString3), 10000);
            return new Double[]{value1, value2, value3};
        }
    }
}
