package com.sfccn.pdftool.parse.zhaogu;

import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.parse.ParseValue;
import com.sfccn.pdftool.utils.BoolUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 是否获得国家最高科技奖励（1级或2级）
 * 国家最高科技奖励包括：国家最高科学技术奖、国家自然科学家、国家技术发明家、国家科学技术进步奖、中华人民共和国国际科学技术合作奖
 * 关键字”科技“、”奖励“。范围是企业和个人，如果是个人，则要看个人是否是在公司在职的情况下（不包括外聘和顾问）
 */
public class Parse82TechPrize extends ParseValue {
    boolean isExist = false;
//    List value = new ArrayList();

    public Parse82TechPrize() {
        label = "是否获得国家最高科技奖励（1级或2级）";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false){
//            isExist = true;
            String temp = getRowContent(row);
            if(BoolUtil.in(temp,Arrays.asList("奖励","荣获","获得"))){
                StringBuffer sff = new StringBuffer();
                sff.append(getRowContent(rowList.get(rowindex -1)));
                sff.append(getRowContent(rowList.get(rowindex)));
                sff.append(getRowContent(rowList.get(rowindex + 1)));
                if(BoolUtil.in(sff.toString(), Arrays.asList("国家科学技术进步奖","国家最高科学技术奖","国家自然科学家","国家技术发明家","国家科学技术进步奖","中华人民共和国国际科学技术合作奖","国家科技进步"))){
//                    value.add(sff.toString());
                    marks.addAll(rowList.get(rowindex -1));
                    marks.addAll(rowList.get(rowindex));
                    marks.addAll(rowList.get(rowindex + 1));
                    addElementValue(marks, "",sff.toString());
                    marks.clear();
                }
            }
        }

    }
}
