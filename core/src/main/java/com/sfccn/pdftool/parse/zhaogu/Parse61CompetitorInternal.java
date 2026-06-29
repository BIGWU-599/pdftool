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
 * 国内主要竞争者数目（<=5）(国内：中国大陆)
 */
public class Parse61CompetitorInternal extends ParseValue {
    boolean isExist = false;
    private int maxIndex = 0;
    public Parse61CompetitorInternal() {
        label = "国内主要竞争者数目（<=5）";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false && rowindex > maxIndex){
            String temp = getRowContent(row);
            // 公司的竞争对手主要是国内外中高端注塑机生产商和服务

            if(BoolUtil.in(temp,Arrays.asList("主要竞争对手","行业内主要企业","国内主要企业","竞争对手主要是","行业内的主要企业","行业主要企业","同行业可比公司","发行人的竞争对手"))){
                StringBuffer sff = new StringBuffer();
//                    System.out.println("主要竞争对手1==========" + temp);
                sff.append(getRowContent(rowList.get(rowindex)));
                sff.append(getRowContent(rowList.get(rowindex + 1)));
                sff.append(getRowContent(rowList.get(rowindex + 2)));
                sff.append(getRowContent(rowList.get(rowindex + 3)));
//                isExist = true;
//                value.add(sff.toString());
                if(BoolUtil.notIn(sff.toString(), Arrays.asList("...","营业收入","净利润","营业收入","毛利率","薪资","薪酬","年度报告","招股说明书","流动比率","速动比率",
                        "周转率","技术迭代","负债率","偿债能力","注：","费用率"))){
                    List<PdfObject> markList = new ArrayList<>();
                    markList.addAll(rowList.get(rowindex));
                    markList.addAll(rowList.get(rowindex + 1));
                    markList.addAll(rowList.get(rowindex + 2));
                    markList.addAll(rowList.get(rowindex + 3));
                    addElementValue(markList, "",sff.toString());
                    maxIndex = rowindex + 3;
                }
            }
        }
    }
}
