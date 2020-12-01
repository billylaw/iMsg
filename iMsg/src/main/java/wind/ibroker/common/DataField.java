/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wind.ibroker.common;


import org.apache.commons.lang3.StringUtils;
import wind.ibroker.comm.ReportSerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * 数据类型定义对象
 * @author nbwu
 */
public class DataField implements Serializable{
    //字段名称
    private String fieldName;
    
    //字段中文名称
    private String fieldCName;
    
    //数据类型
    private byte dataType;
    
    //是否是插入数据（本表数据）
    private boolean isInsert;
    
    //不允许为空
    private boolean isNotNull;
    
    //最大长度
    private int limitLen;
    
    //索引名称
    private String indexName;
    
    //是否存储
    private boolean isStore;
    
    //分词
    private byte splitType;
    
    /**
     * @return the fieldName
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * @param fieldName the fieldName to set
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * @return the fieldCName
     */
    public String getFieldCName() {
        return fieldCName;
    }

    /**
     * @param fieldCName the fieldCName to set
     */
    public void setFieldCName(String fieldCName) {
        this.fieldCName = fieldCName;
    }

    /**
     * @return the dataType
     */
    public byte getDataType() {
        return dataType;
    }

    /**
     * @param dataType the dataType to set
     */
    public void setDataType(byte dataType) {
        this.dataType = dataType;
    }

    /**
     * @return the isInsert
     */
    public boolean isIsInsert() {
        return isInsert;
    }

    /**
     * @param isInsert the isInsert to set
     */
    public void setIsInsert(boolean isInsert) {
        this.isInsert = isInsert;
    }

    /**
     * @return the isNotNull
     */
    public boolean isIsNotNull() {
        return isNotNull;
    }

    /**
     * @param isNotNull the isNotNull to set
     */
    public void setIsNotNull(boolean isNotNull) {
        this.isNotNull = isNotNull;
    }

    /**
     * @return the limitLen
     */
    public int getLimitLen() {
        return limitLen;
    }

    /**
     * @param limitLen the limitLen to set
     */
    public void setLimitLen(int limitLen) {
        this.limitLen = limitLen;
    }

    /**
     * @return the indexName
     */
    public String getIndexName() {
        return indexName;
    }

    /**
     * @param indexName the indexName to set
     */
    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    /**
     * @return the isStore
     */
    public boolean isIsStore() {
        return isStore;
    }

    /**
     * @param isStore the isStore to set
     */
    public void setIsStore(boolean isStore) {
        this.isStore = isStore;
    }

    /**
     * @return the splitType
     */
    public byte getSplitType() {
        return splitType;
    }

    /**
     * @param splitType the splitType to set
     */
    public void setSplitType(byte splitType) {
        this.splitType = splitType;
    }
    
    //序列化
    public void serialize(ByteArrayOutputStream os) throws IOException{
        ReportSerializer.writeString(os, this.getFieldName());
        ReportSerializer.writeByte(os, this.getDataType());
    }
    
    //是否是索引
    public boolean isIndex(){
        return !StringUtils.isEmpty(this.getIndexName());
    }
}
