/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wind.ibroker.common.windstream;



import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nbwu
 */
public class WindStreamConfiguration {
    //主键列名
    private String keyFieldName;
    
    //PackName
    private String packName;
    
    //字段列表
    private List<WindStreamDataField> fields;
    
    //主键
    private WindStreamDataField keyField;
    
    public WindStreamConfiguration(){
        fields = new ArrayList<WindStreamDataField>();
        keyFieldName = StringUtils.EMPTY;
    }
    
    //设置主键列名
    public void setKeyFieldName(String keyFieldName) {
        this.keyFieldName = keyFieldName;
    }
    
    //设置PackName
    public void setPackName(String packName){
        this.packName = packName;
    }
    
    //获取packName
    public String getPackName(){
        return this.packName;
    }
    
    public void add(WindStreamDataField field){
        this.fields.add(field);
        if(this.keyFieldName.equals(field.getFieldName())){
            this.keyField = field;
        }
    }
    
    /**
     * 获取字段数量
     * */
    public int size(){
    	return fields.size();
    }
    
    /**
     * 获取字段列表
     * */
    public WindStreamDataField[] getFields(){
    	WindStreamDataField[] fieldList = new WindStreamDataField[fields.size()];
    	fields.toArray(fieldList);
    	
    	return fieldList;
    }
    
}
