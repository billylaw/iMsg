/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wind.ibroker.common.windstream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author nbwu
 */
public class WindStreamDataField implements IWindStreamSerilize{
    //字段名
    private String fieldName;
    
    //数据类型
    private byte dataType;
    
    public WindStreamDataField(){}
    
    public WindStreamDataField(String fieldName, byte dataType){
        this.fieldName = fieldName;
        this.dataType = dataType;
    }
    
    //设置字段名
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    //设置字段类型
    public void setDataType(byte dataType) {
        this.dataType = dataType;
    }

    //获取字段名
    public String getFieldName() {
        return this.fieldName;
    }
    
    /**
     * 获取字段类型
     * */
    public byte getDataType(){
    	return this.dataType;
    }

    /**
     * 写入数据字段
     * */
    @Override
    public void serialize(DataOutputStream writer) throws IOException {
        Serializer.writeString(writer, fieldName);
        Serializer.writeByte(writer, dataType);
    }

    /**
     * 反序列化DataField
     */
    void deserialize(DataInputStream br) throws IOException {
        this.fieldName = Serializer.readString(br);
        this.dataType = Serializer.readByte(br);
    }
}
