/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wind.ibroker.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nbwu
 */
public class SectionData {
    //cache定时更新时间
    public int runAt;
    
    //自选股分组定时更新时间
    public int bondGroupAutoUpdate;
    
    //cache定时更新时间间隔
    public int cacheInterval;
    
    //cache加载时间
    public String cacheLoadTime;
    
    //是否入mongo库
    public boolean isMongo;
    
    //是否是撤销报价的主机
    public boolean isRefQuote;
    
    //是否为更新自选债分组的主机
    public boolean isUpdateBondGroup;
    
    //项目列表
    public List<SectionItem> items = new ArrayList<>();

    //类型字典
    private Map<Short, SectionItem> dict;

    /**
     * 获取字段列表
     * @param type 数据类型
     * */
    public List<DataField> getFieldList(short type){
        if(dict == null){
            synchronized (this){
                if(dict == null){
                    dict = new HashMap<Short, SectionItem>();

                    for(SectionItem item : items){
                        dict.put(item.getItemType(), item);
                    }
                }
            }
        }

        if(dict.containsKey(type)){
            return dict.get(type).getList();
        }

        return null;
    }

    public enum PushSubscribeType
    {
        QuoteList((short)1),
        BestQuote((short)2),
        FocusQuote((short)3),
        QuoteDone((short)4),
        RefQuote((short)5),
        SourceModify((short)6),
        QuoteHis((short)7),
        QuoteDoneHis((short)8),
        QuoteDoneVolumnHis((short)9),
        BestQuoteRecord((short)10),
        BlackList((short)11),
        BondGroup((short)12),
        BondGroupList((short)13),

        QQQuote((short)15),

        IForexQuotePush((short)20),
        IForexBest((short)21),
        IForexDeal((short)22),
        ExternalQuoteDone((short)30),
        BondCalendar ((short)31),
        BondDeptTrace((short)32),
        User((short)33),
        BestQuoteValid((short)34);

        private final short value;
        PushSubscribeType(short value) {
            this.value = value;
        }

        public short getValue(){
            return value;
        }
    }
}
