package com.sfccn.pdftool.parse;

import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.libary.CustomPDFDomTree;
import com.sfccn.pdftool.libary.IntTreeMap;
import com.sfccn.pdftool.utils.ToolUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public abstract  class ParseValue {
    protected String label = "";
    protected ArrayList value = new ArrayList();
    protected ArrayList indexes = new ArrayList();
    protected ArrayList<PdfObject> marks = new ArrayList();
    protected List<List<Object>> valueList = null;
    public static Map<String,PdfObject> markMap = null;

    public abstract void runRow(Map<Integer,List<Axis>> pageLineMap,List<List<PdfObject>> rowList, List<PdfObject> row,int rowindex);

    public void runEnd(){
        addValue(Arrays.asList(label, value, indexes));
    }
    public static Map<String,PdfObject> getMarkMap(){
        return markMap;
    }
    public static List<List<List<Object>>> parse(Map<Integer,List<Axis>> pageLineMap,List<List<PdfObject>> rowList, List<Class<?>> parseList){
        markMap = new HashMap<>();
        List<List<List<Object>>> list = new ArrayList<>();
        List<ParseValue> parseArray = new ArrayList<>();
        try{
            for(Class<?> parseClass:parseList){
                ParseValue parseValue = (ParseValue)parseClass.getDeclaredConstructor().newInstance();
                parseArray.add(parseValue);
            }
            //        List<List<String>> valueList = getValueList();
            for(int r = 0; r < rowList.size(); r++){
                List<PdfObject> row = rowList.get(r);      // 每行可能包含多列数据
                for(ParseValue parseValue:parseArray){
                    parseValue.runRow(pageLineMap, rowList, row, r);
                    if(r == (rowList.size() - 1)){
                        parseValue.runEnd();
                    }
                }
            }

            for(ParseValue parseValue:parseArray){
                //parseValue.runRow(pageList, row, r);
                list.add(parseValue.getValueList());
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
    protected List<List<Object>> getValueList(){
        if(valueList == null){
            valueList = new ArrayList<>();
        }
        return valueList;
    }
    protected void addValue(List<Object> value){
        getValueList().add(value);
//        list.add()
    }
    protected String getRowContent(List<PdfObject> mrow){
        StringBuilder value = new StringBuilder();
        for (PdfObject nColumn : mrow) {
            if (nColumn.getType().equals(PdfObject.STYLE_STRING)) {
                value.append(nColumn.getData());
            }
        }
        return value.toString();
    }
    protected List<String> getRowContents(List<PdfObject> mrow){
        List<String> value = new ArrayList();
        for (PdfObject nColumn : mrow) {
            if (nColumn.getType().equals(PdfObject.STYLE_STRING)) {
                value.add(nColumn.getData());
            }
        }
        return value;
    }

    /**
     * 把文本按句子切分
     * @param content
     * @return
     */
    public List<String> getSentences(String content){
        int start = 0;
        int end;
        List<String> list = new ArrayList<>();
        for(int r = 0; r < content.length();r++){
            char c = content.charAt(r);
            if("。！？；".indexOf(c) > -1){
                end = r + 1;
                list.add(content.substring(start, end));
                start = end;
            }
        }
        return list;
    }

    public void addElementValue(String index){
        addElementValue(marks, "", index);
    }

    /**
     * 创建元素定位ID,接记录ID
     * @param row
     * @param str
     */
    public void addElementValue(List<PdfObject> row, String str){
        this.addElementValue(row, str, str);
    }

    /**
     * 创建元素定位ID,接记录ID
     * @param row
     * @param str
     */
    public void addElementValue(List<PdfObject> row, String str, String index){
        String id = null;
        if(row != null){
            for(PdfObject pdfObject:row){
                if(pdfObject.getData().length() > 0){
                    String key = new StringBuffer().append(pdfObject.getPagenum())
                            .append("_").append(pdfObject.getX()).append("_").append(pdfObject.getY())
                            .append("_").append(pdfObject.getData().length()).toString();
                    if(id == null){
                        id = key;
                    }
                    markMap.put(key, pdfObject);
                }

            }
        }
        value.add(str);
        if(StringUtils.isEmpty(index) == false){
            if(id != null){
                indexes.add("<a href=\"#"+id+"\">" + index + "</a>");
            }
            else{
                indexes.add(index);
            }
        }
    }
    /**
     * 通过线条数量来判断是否属于表格
     * @param rowList
     * @param rowindex
     * @param lentgh
     * @param linecount
     * @return
     */
    public boolean isTable(List<List<PdfObject>> rowList, int rowindex, int lentgh, int linecount){
        boolean isTable = false;
        int endIndex = rowindex + lentgh;
        int lineSize = 0;
        for(int startIndex = rowindex + 1; startIndex <= endIndex; startIndex++) {
            List<PdfObject> rowTemp = rowList.get(startIndex);
//            if(rowindex == 1092){
//                System.out.println("isTable=======" + rowTemp);
//            }
            for(PdfObject row:rowTemp){
                if(row.getType().equals(PdfObject.STYLE_LINE)){
                    lineSize++;
                    break;
                }
            }
            if(lineSize >= linecount){
                isTable = true;
                break;
            }
        }
        return isTable;
    }
    public boolean isTable(List<List<PdfObject>> rowList, int rowindex, int lentgh){
        return this.isTable(rowList, rowindex, lentgh, 2);
    }

    public Integer[] findEdge(Map<Integer,List<Axis>> pageLineMap, PdfObject pdfObject){
        int x = pdfObject.getX();
        int y = pdfObject.getY();
        int page = pdfObject.getPagenum();
        int page_index = page * 10000;
        //int start_key = page_index + y;

//        if(!(pdfObject.getTypeInfo() instanceof Integer[])){
//            System.out.println("findEdge=====" + pdfObject.getTypeInfo() + "," + ToolUtil.toJson(pdfObject));
//        }
        Integer[] info = (Integer[])pdfObject.getTypeInfo();
        int width = info[2];
        int height = info[3];
        int edgex1 = 0;
        int edgey1 = 0;
        int edgex2 = 0;
        int edgey2 = 0;
        for(int i = (page_index + y); i > page_index; i--){
            List<Axis> axiss = pageLineMap.get(i);
            if(axiss != null){
                for(Axis axis:axiss){
                    if(axis.getX() <= x && x <= axis.getHorizontalX() ){
                        edgex1 = axis.getX();
                        edgey1 = i % 10000;
                        break;
                    }
                }
                if(edgey1 > 0){
                    break;
                }
            }
        }
        for(int i = (page_index + y); i <= (page_index + y + height * 5); i++){
            List<Axis> axiss = pageLineMap.get(i);
            if(axiss != null){
                for(Axis axis:axiss){
                    if(axis.getX() <= x && x <= axis.getHorizontalX()){
                        edgex2 = axis.getHorizontalX();
                        edgey2 = i % 10000;
                        break;
                    }
                }
                if(edgey2 > 0){
                    break;
                }
            }
        }
        return new Integer[]{edgex1, edgey1, edgex2, edgey2, x, y};
    }

    public Map<Integer, Map<Integer, String>> findGrid(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, int rowindex){
        return findGrid(pageLineMap, rowList, rowindex, 60,20);
    }

    public Map<Integer, Map<Integer, String>> findGrid(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, int rowindex, int indexcount, int selectcount){
        Map<Integer, Map<Integer, String>> gridMap = new IntTreeMap<>();
        for(int r =  1; r <= indexcount; r++){
            List<PdfObject> rowTemp = rowList.get(rowindex + r);

            if(rowindex == 14741){
                System.out.println(r + ",findGrid＝＝＝＝＝＝rowTemp:" + rowTemp);
            }
            if(rowTemp.get(0).getType().equals(PdfObject.STYLE_STRING)){
//                if(indexcount == 71){
//                    System.out.println(r + ",findGrid＝＝＝＝＝＝rowTemp:" + rowTemp);
//                }
                for(PdfObject pdfObject:rowTemp){
                    Integer[] edges = findEdge(pageLineMap, pdfObject);// x1,y1,x2,y2
                    if(rowindex == 14741){
                        System.out.println(r + "  ＝＝＝＝＝＝edges:" + ToolUtil.toJson(edges) + "," + pdfObject.getData());
                    }
                    int x1 = edges[0];
                    int y1 = edges[1];
                    int key = pdfObject.getPagenum() * 10000 + y1;
                    if(x1 > 0 && y1 > 0){
                        Map<Integer, String> gridRow = gridMap.get(key);
                        if(gridRow == null){
                            gridRow = new IntTreeMap<>();
                            gridMap.put(key, gridRow);
                        }
                        String cell = gridRow.getOrDefault(x1, "") + pdfObject.getData();
                        gridRow.put(x1, cell);
                    }
                }
            }

            if(gridMap.size() >= selectcount){
                break;
            }
        }
        return gridMap;
    }
}
