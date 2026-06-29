package com.sfccn.pdftool.parse.zhaogu;


import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.parse.ParseValue;
import com.sfccn.pdftool.utils.BoolUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 是否属于新模式
 * 关键字”新模式“、”新商业模式“，看是否包含这些关键字
 */
public class Parse25NewPattern extends ParseValue {
    boolean isExist = false;
    private int maxIndex = 0;
    public Parse25NewPattern() {
        label = "是否属于新模式";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
//        if(isExist == false){
        if(rowindex >= (maxIndex + 2) && rowindex < (rowList.size() - 2)){
            String temp = getRowContent(row);
            if(BoolUtil.in(temp,Arrays.asList("新模式","新商业模式"))
                    && BoolUtil.notIn(temp, Arrays.asList("以旧换新模式","新产业园","创新模式","更新模式","工信部"))){
                StringBuffer valueSff = new StringBuffer();
                valueSff.append(getRowContent(rowList.get(rowindex-1)));
                valueSff.append(getRowContent(rowList.get(rowindex)));
                valueSff.append(getRowContent(rowList.get(rowindex+1)));
//                List<Term> list = NLPTokenizer.segment(valueSff.toString());
                ArrayList<PdfObject> markTemp = new ArrayList();
                markTemp.addAll(rowList.get(rowindex-1));
                markTemp.addAll(rowList.get(rowindex));
                markTemp.addAll(rowList.get(rowindex + 1));
                addElementValue(markTemp, "", valueSff.toString());

                maxIndex = rowindex;
            }
        }
    }

    public static void main(String[] args) throws Exception{
//        String text = "公司所处业务领域属于新产业、新业态和新商业模式";
        String text = "相结合的创新模式。鼓励企业积极申请知识产权，";
//        System.out.println("NLPTokenizer==========" + NLPTokenizer.segment(text));
//        System.out.println("NLPTokenizer==========" + NLPTokenizer.analyze(text));
//        CRFLexicalAnalyzer analyzer = new CRFLexicalAnalyzer();
//        System.out.println("CRFLexicalAnalyzer====" + analyzer.analyze(text));
//        System.out.println("SpeedTokenizer========" + SpeedTokenizer.segment(text));
//        System.out.println("HanLP=================" + HanLP.segment(text));

        // 自定义词典
        // 动态增加
//        CustomDictionary.add("攻城狮");
//        // 强行插入
//        CustomDictionary.insert("白富美", "nz 1024");
//        // 删除词语（注释掉试试）
////        CustomDictionary.remove("攻城狮");
//        System.out.println(CustomDictionary.add("单身狗", "nz 1024 n 1"));
//        System.out.println(CustomDictionary.get("单身狗"));
    }
}
