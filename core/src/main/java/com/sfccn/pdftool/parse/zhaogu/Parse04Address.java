package com.sfccn.pdftool.parse.zhaogu;

import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.libary.CustomPDFDomTree;
import com.sfccn.pdftool.parse.ParseValue;
import com.sfccn.pdftool.utils.ToolUtil;
import org.fit.pdfdom.TextMetrics;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 企业地址
 */
public class Parse04Address extends ParseValue {
    boolean isExist = false;
    public Parse04Address() {
        label = "企业所在地";
    }
    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false){
            String temp = getRowContent(row);
            if(temp.indexOf("发行人基本情况") > -1){
                if(isTable(rowList, rowindex, 6)){
                    Map<Integer, Map<Integer, String>> gridMap = findGrid(pageLineMap, rowList, rowindex);
                    if(gridMap.size() > 0){
                        for(Map.Entry<Integer, Map<Integer, String>> rowEntry:gridMap.entrySet()){
                            List<String> columns = rowEntry.getValue().values().stream().collect(Collectors.toList());
                            for(int r = 0; r < columns.size(); r++){
                                String data = columns.get(r);
                                if(data.indexOf("注册地址") > -1 || data.indexOf("住所") > -1){
                                    addElementValue(row,"", temp);
                                    if((r + 1) < columns.size()){
                                        addElementValue(null,columns.get(r + 1), "");
                                    }
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


//                String address = null;
//                String index = null;
//                for(int r = 1; r < 30; r++){
//                    List<PdfObject> rowTemp = rowList.get(rowindex + r);
//                    for (int c = 0; c < rowTemp.size(); c++){
//                        String data = rowTemp.get(c).getData();
//                        if(data.indexOf("注册地址") > -1 || data.indexOf("住所") > -1){
////                            address = rowTemp.get(c + 1).getData();
//                            StringBuffer temp2 = new StringBuffer();
//                            for(int m = c; m < rowTemp.size() - 1; m++){
//                                PdfObject columnTemp = rowTemp.get(m + 1);
//                                temp2.append(columnTemp.getData());
//                            }
//                            marks.addAll(rowTemp);
//                            index = getRowContent(rowTemp);
//                            address = temp2.toString();
//                            break;
//                        }
//                    }
//                    String tempStr = getRowContent(rowTemp);
//                    if(tempStr.indexOf("中介机构") > -1){
//                        address = null;
//                        marks.clear();
//                        indexes.clear();
//                        break;
//                    }
//                }
//                if(address != null){
//                    addElementValue(marks, address, index);
//                    isExist = true;
//                }
            }
        }



//        boolean isAddress = false;
//        StringBuffer sff = new StringBuffer();
//        for(int c = 0; c < row.size(); c++){
//            PdfObject column = row.get(c);
//            if(column.getType().equals(CustomPDFDomTree.STYLE_STRING)){
//                TextMetrics metrics = (TextMetrics)column.getTypeInfo();
//                String content = (String)column.getData();
//                if(content.indexOf("发行人基本情况") > -1 ){
//                    isExist = true;
//                }
//
//                if(isExist == true && content.indexOf("注册地址") > -1){
//                    isAddress = true;
//                    isExist = false;
//                }
//                if(isAddress == true){
//                    if(c < row.size() - 1){
//                        StringBuffer temp = new StringBuffer();
////                        System.out.println("ParseAddress=====" + c + "," + row.size());
//                        for(int m = c; m < row.size() - 1; m++){
//                            PdfObject columnTemp = row.get(m + 1);
//                            temp.append(columnTemp.getData());
//                        }
//                        value.add(temp.toString());
//                        isExist = false;
//                        isAddress = false;
//                        break;
//                    }
//                }
//            }
//        }
    }


}
