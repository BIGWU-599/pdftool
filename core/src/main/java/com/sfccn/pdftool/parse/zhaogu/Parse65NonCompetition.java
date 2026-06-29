package com.sfccn.pdftool.parse.zhaogu;

import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.parse.ParseValue;
import com.sfccn.pdftool.utils.BoolUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 是否有技术秘密保护和竞业限制或竞业禁止法律文件
 */
public class Parse65NonCompetition extends ParseValue {
    boolean isExist = false;

    public Parse65NonCompetition() {
        label = "是否有技术秘密保护和竞业限制或竞业禁止法律文件";
    }

    @Override
    public void runRow(Map<Integer,List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false){

            // 公司在职的董事、监事、高级管理人员及核心技术人员与公司签订了服务协 议或劳动合同，公司高级管理人员及核心技术人员与公司签订了保密协议或竞业 禁止协议。
            // 公司已与所有高级管理人员及参与技术保密的员工签署《员工保密协议书》 和《竞业限制协议》，协议对保密信息的内容与范围、保密义务、违约责任及竞 业禁止等内容进行了明确约定，以确保公司的核心技术与合法权益受到法律保护。
            String temp = getRowContent(row);
            if(BoolUtil.in(temp, Arrays.asList("签订","签署","签定")) && BoolUtil.in(temp, Arrays.asList("员工","人员"))){
                StringBuffer content = new StringBuffer();
                content.append(getRowContent(rowList.get(rowindex)));
                content.append(getRowContent(rowList.get(rowindex + 1)));
                content.append(getRowContent(rowList.get(rowindex + 2)));
//                content.append(getRowContent(rowList.get(rowindex + 3)));
                if(content.indexOf("保密协议") > -1 || content.indexOf("保密义务") > -1 || content.indexOf("竞业禁止协议") > -1
                    || content.indexOf("员工保密协议书") > -1 || content.indexOf("竞业限制") > -1 || content.indexOf("竞业禁止") > -1){
//                    value.add(content.toString());
                    marks.addAll(rowList.get(rowindex));
                    marks.addAll(rowList.get(rowindex + 1));
                    marks.addAll(rowList.get(rowindex + 2));
                    addElementValue(marks, "",content.toString());
                    isExist = true;
                }
            }

        }

    }
}
