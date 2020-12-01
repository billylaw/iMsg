/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wind.ibroker.common;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author nbwu
 */
public class SectionItem implements Serializable{

    //表类型
    private short itemType;
    
    //是否缓存
    private boolean isCache;
    
    //订阅类型
    private short subscribeType;
    
    //唯一标识字段
    private String keyFieldName;
    
    //PacketName
    private String packName;
    
    //查询语句
    private String searchSQL;
    
    //属性的数据字段描述
    private List<DataField> list;
    
    /**
     * @return the itemType
     */
    public short getItemType() {
        return itemType;
    }

    /**
     * @param itemType the itemType to set
     */
    public void setItemType(short itemType) {
        this.itemType = itemType;
    }

    /**
     * @return the isCache
     */
    public boolean isCache() {
        return isCache;
    }

    /**
     * @param isCache the isCache to set
     */
    public void setIsCache(boolean isCache) {
        this.isCache = isCache;
    }

    /**
     * @return the subscribeType
     */
    public short getSubscribeType() {
        return subscribeType;
    }

    /**
     * @param subscribeType the subscribeType to set
     */
    public void setSubscribeType(short subscribeType) {
        this.subscribeType = subscribeType;
    }

    /**
     * @return the keyFieldName
     */
    public String getKeyFieldName() {
        return keyFieldName;
    }

    /**
     * @param keyFieldName the keyFieldName to set
     */
    public void setKeyFieldName(String keyFieldName) {
        this.keyFieldName = keyFieldName;
    }

    /**
     * @return the packName
     */
    public String getPackName() {
        return packName;
    }

    /**
     * @param packName the packName to set
     */
    public void setPackName(String packName) {
        this.packName = packName;
    }

    /**
     * @return the searchSQL
     */
    public String getSearchSQL() {
        return searchSQL;
    }

    /**
     * @param searchSQL the searchSQL to set
     */
    public void setSearchSQL(String searchSQL) {
        this.searchSQL = searchSQL;
    }

    /**
     * @return the list
     */
    public List<DataField> getList() {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(List<DataField> list) {
        this.list = list;
    }
}
