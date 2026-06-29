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
 * 研发费用
 */
public class Parse23Research extends ParseValue {
    boolean isExist = false;

    public Parse23Research() {
        label = "研发费用";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false){
            String content = getRowContent(row);
            if(content.indexOf("利润表") > -1
                && content.indexOf("合并利润表主要数据") == -1 && content.indexOf("母公司") == -1
                && content.indexOf("简要") == -1){
                Map<Integer, Map<Integer, String>> gridMap = findGrid(pageLineMap, rowList, rowindex);
                if(gridMap.size() > 0){
                    for(Map.Entry<Integer, Map<Integer, String>> rowEntry:gridMap.entrySet()){
                        int x = rowEntry.getKey();
                        List<String> columns = rowEntry.getValue().values().stream().collect(Collectors.toList());
                        String temp2 = columns.get(0);
                        if(temp2.indexOf("研发费用") > -1){
                            if(columns.size() >= 4){
                                String valueString1 = columns.get(columns.size() - 3).replace(",", "");
                                String valueString2 = columns.get(columns.size() - 2).replace(",", "");
                                String valueString3 = columns.get(columns.size() - 1).replace(",", "");

                                if(StringUtils.isEmpty(valueString1) == false && !valueString1.equals("-") && !valueString2.equals("-") && !valueString3.equals("-") ){
                                    Double value1 = ToolUtil.string2Double(Double.valueOf(valueString1), 10000);
                                    Double value2 = ToolUtil.string2Double(Double.valueOf(valueString2), 10000);
                                    Double value3 = ToolUtil.string2Double(Double.valueOf(valueString3), 10000);

                                    addElementValue(null, String.valueOf(value1), "");
                                    addElementValue(null, String.valueOf(value2), "");
                                    addElementValue(null, String.valueOf(value3), "");
                                    addElementValue(row,"", content);
                                    isExist = true;
                                    break;
                                }
                            }
                        }
                    }
                }

//                boolean isTable = false;
//                for(int r = 1; r < 50; r++){
//                    List<PdfObject> rowTemp = rowList.get(rowindex + r);
//                    String temp = getRowContent(rowTemp);
//                    if(isTable == true){
//                        if(temp.indexOf("研发费用") > -1){
//                            if((rowTemp.size()) >=3){
//                                Double[] values = getValue(rowTemp);
//                                if(values == null && rowList.get(rowindex + r + 1).size() >= 3){
//                                    rowTemp = rowList.get(rowindex + r + 1);
//                                    values = getValue(rowTemp);
//                                }
//                                if(values != null){
////                                value.add(Arrays.asList(values[0], values[1], values[2]));
//                                    //addElementValue(rowTemp, ToolUtil.toJson(Arrays.asList(values[0], values[1], values[2])));
//                                    addElementValue(Arrays.asList(rowTemp.get(rowTemp.size() - 3)), ToolUtil.toJson(values[0]));
//                                    addElementValue(Arrays.asList(rowTemp.get(rowTemp.size() - 2)), ToolUtil.toJson(values[1]));
//                                    addElementValue(Arrays.asList(rowTemp.get(rowTemp.size() - 1)), ToolUtil.toJson(values[2]));
//                                    isExist = true;
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                    else if(rowTemp.get(0).getType().equals(PdfObject.STYLE_LINE) && r < 4){
//                        isTable = true;
//                    }
//                    else if(r > 5){
//                        break;
//                    }
//                }

            }
        }
    }

    private Double[] getValue(List<PdfObject> row){
        PdfObject columnTemp1 = row.get(row.size() - 3);
        PdfObject columnTemp2 = row.get(row.size() - 2);
        PdfObject columnTemp3 = row.get(row.size() - 1);

        String valueString1 = columnTemp1.getData().replace(",", "");
        String valueString2 = columnTemp2.getData().replace(",", "");
        String valueString3 = columnTemp3.getData().toString().replace(",", "");

        if(NumberUtils.isNumber(valueString1) == false || NumberUtils.isNumber(valueString2) == false || NumberUtils.isNumber(valueString3) == false){
            if(valueString1.equals("-") && valueString2.equals("-") && valueString3.equals("-")){
                return new Double[]{0D,0D,0D};
            }
            else{
                return null;
            }
        }
        else{
//            String valueString1 = columnTemp1.get(1).toString().replace(",", "");
//            String valueString2 = columnTemp2.get(1).toString().replace(",", "");
//            String valueString3 = columnTemp3.get(1).toString().replace(",", "");
            Double value1 = ToolUtil.string2Double(Double.valueOf(valueString1), 10000);
            Double value2 = ToolUtil.string2Double(Double.valueOf(valueString2), 10000);
            Double value3 = ToolUtil.string2Double(Double.valueOf(valueString3), 10000);
            return new Double[]{value1, value2, value3};
        }
    }
}
