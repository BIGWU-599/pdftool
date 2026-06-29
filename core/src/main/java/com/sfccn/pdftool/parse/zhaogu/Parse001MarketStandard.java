package com.sfccn.pdftool.parse.zhaogu;

import com.sfccn.pdftool.bean.Axis;
import com.sfccn.pdftool.bean.PdfObject;
import com.sfccn.pdftool.parse.ParseValue;

import java.util.List;
import java.util.Map;

/**
 * 上市标准数据提取
 * 科创板企业上市五个标准是什么？哪套标准最受欢迎？
 * 发行人申请在本所科创板上市，市值及财务指标应当至少符合下列标准中的一项：
 * （一）预计市值不低于人民币10亿元，最近两年净利润均为正且累计净利润不低于人民币5000万元，或者预计市值不低于人民币10亿元，最近一年净利润为正且营业收入不低于人民币1亿元;
 * （二）预计市值不低于人民币15亿元，最近一年营业收入不低于人民币2亿元，且最近三年累计研发投入占最近三年累计营业收入的比例不低于15%;
 * （三）预计市值不低于人民币20亿元，最近一年营业收入不低于人民币3亿元，且最近三年经营活动产生的现金流量净额累计不低于人民币1亿元;
 * （四）预计市值不低于人民币30亿元，且最近一年营业收入不低于人民币3亿元;
 * （五）预计市值不低于人民币40亿元，主要业务或产品需经国家有关部门批准，市场空间大，目前已取得阶段性成果。医药行业企业需至少有一项核心产品获准开展二期临床试验，其他符合科创板定位的企业需具备明显的技术优势并满足相应条件。
 */
public class Parse001MarketStandard extends ParseValue {
    boolean isExist = false;
    public Parse001MarketStandard(){
        label = "上市标准";
    }
    @Override
    public void runRow(Map<Integer, List<Axis>> pageLineMap, List<List<PdfObject>> rowList, List<PdfObject> row, int rowindex) {
        if(isExist == false && rowindex < (rowList.size() - 20)){
            StringBuffer temp = new StringBuffer();
            temp.append(getRowContent(rowList.get(rowindex)));
            temp.append(getRowContent(rowList.get(rowindex + 1)));
            // 发行人选择的上市标准 发行人选择的具体上市标准
            //上海证券交易所科创板股票发行上市审核规则  上海证券交易所科创板股票上市规则
            if((temp.indexOf("上市标准") > - 1) && temp.indexOf("......") == - 1){
                StringBuffer buff = new StringBuffer();
                int index = 0;
                for(int r = 0; r < 20; r++){
                    buff.append(getRowContent(rowList.get(rowindex + r)));
                    if(buff.indexOf("预计市值") > -1){
                        index = r;
                        break;
                    }
                }
                StringBuffer buff2 = new StringBuffer();
                buff2.append(getRowContent(rowList.get(rowindex + index -1)));
                buff2.append(getRowContent(rowList.get(rowindex + index)));
                buff2.append(getRowContent(rowList.get(rowindex + index + 1)));
                buff2.append(getRowContent(rowList.get(rowindex + index + 2)));
                buff2.append(getRowContent(rowList.get(rowindex + index + 3)));
                String value = "";
                if(buff2.indexOf("预计市值不低于人民币10亿元") > -1){
                    value = "预计市值不低于人民币10亿元";
                }
                else if(buff2.indexOf("预计市值不低于人民币15亿元") > -1){
                    value = "预计市值不低于人民币15亿元";
                }
                else if(buff2.indexOf("预计市值不低于人民币20亿元") > -1){
                    value = "预计市值不低于人民币20亿元";
                }
                else if(buff2.indexOf("预计市值不低于人民币30亿元") > -1){
                    value = "预计市值不低于人民币30亿元";
                }
                else if(buff2.indexOf("预计市值不低于人民币40亿元") > -1) {
                    value = "预计市值不低于人民币40亿元";
                }
                if(!value.equals("")){
                    addElementValue(null, value, buff2.toString());
                    isExist = true;
                }

            }
        }
    }
}
