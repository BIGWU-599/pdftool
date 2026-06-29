package com.sfccn.pdftool.parse.zhaogu;

import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.parse.ParseValue;
import com.sfccn.pdftool.utils.BoolUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 是否参与制定国家标准
 */
public class Parse81NationalStandard extends ParseValue {
    private boolean isExist = false;
//    List value = new ArrayList();

    private int maxIndex = 0;

    public Parse81NationalStandard() {
        label = "是否参与制定国家标准";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false){
//            isExist = true;
            String temp = getRowContent(row);
            // 同时还参与了 10 多个分子诊断相关国家标准物质制定
            if(temp.indexOf("标准") > -1 && rowindex >= (maxIndex + 2)){
                StringBuffer sff = new StringBuffer();
                sff.append(getRowContent(rowList.get(rowindex -1)));
                sff.append(getRowContent(rowList.get(rowindex)));
                sff.append(getRowContent(rowList.get(rowindex + 1)));

                if(BoolUtil.in(sff.toString(),Arrays.asList("参与","主编","参编","起草","修订"))
                        && sff.indexOf("国家") > -1){
                    marks.addAll(rowList.get(rowindex -1));
                    marks.addAll(rowList.get(rowindex));
                    marks.addAll(rowList.get(rowindex + 1));
                    addElementValue(marks, "",sff.toString());
                    maxIndex = rowindex;
                }

            }
        }

    }

    public static void main(String[] args) {
//        String content = "公司作为起草单位之一，深度参与了核酸提取、质控品研制、扩增试剂盒、 分子诊断产品性能评价等近 10 项分子诊断行业标准建立；作为国内分子诊断优 秀企业代表，代表中国参与了 WHO 乙型肝炎病毒国际标准物质协助定标工作； 同时还参与了 10 多个分子诊断相关国家标准物质制定。公司共拥有包含 51 项发 明专利在内的 75 项境内专利，3 项境外专利。";
//        int start = 0;
//        int end = 0;
//        List<String> list = new ArrayList<>();
//        for(int r = 0; r < content.length();r++){
//            char c = content.charAt(r);
//            if("。！？；".indexOf(c) > -1){
//                System.out.println("r===" + r);
//                end = r + 1;
//                list.add(content.substring(start, end));
//                start = end;
//            }
//        }
//        System.out.println(list);
//        list.forEach(System.out::println);
//        System.out.println("。！？；".indexOf('！'));
    }
}
