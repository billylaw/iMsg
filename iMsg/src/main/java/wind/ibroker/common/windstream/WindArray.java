package wind.ibroker.common.windstream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * 数组数据
 * */
public class WindArray implements IWindStreamSerilize{
    /**
     * 数据类型
     * */
    private byte _dataType;

    /**
     * 数据列表
     * */
    private List<WindDataItem> data = new ArrayList<WindDataItem>();

    /**
     * 构造WindArray
     * @param isHuge 是否为大数组
     * */
    public WindArray(boolean isHuge) {
        this._dataType = isHuge ? WindStreamDataType.HUGEARRAY : WindStreamDataType.ARRAY;
    }

    /**
     * 初始化数组数据
     * */
    public void init(Object data) throws Exception {
        if(data == null || !data.getClass().isArray())
            return;

        byte dataType;
        WindDataItem dataItem;
        for(int i=0; i<Array.getLength(data); i++){
            Object obj = Array.get(data, i);
            dataType = getDataType(obj);
            if(dataType == WindStreamDataType.NONE)
                throw new Exception("unkown data type");

            dataItem = new WindDataItem();
            dataItem.setDataType(dataType);
            dataItem.setValue(obj);

            this.data.add(dataItem);
        }
    }

    private byte getDataType(Object obj) {
        if(obj == null)
            return WindStreamDataType.NONE;

        if(obj instanceof Boolean){
            return WindStreamDataType.BOOL;
        }else if(obj instanceof Byte){
            return WindStreamDataType.BYTE;
        }else if(obj instanceof Short){
            return WindStreamDataType.INT16;
        }else if(obj instanceof Integer){
            return WindStreamDataType.INT32;
        }else if(obj instanceof Long){
            return WindStreamDataType.INT64;
        }else if(obj instanceof Float){
            return WindStreamDataType.FLOAT;
        }else if(obj instanceof Double){
            return WindStreamDataType.DOUBLE;
        }else if(obj instanceof String){
            return WindStreamDataType.STRING;
        }

        return WindStreamDataType.NONE;
    }

    /**
     * 序列化
     * */
    @Override
    public void serialize(DataOutputStream writer) throws IOException {
        Serializer.writeByte(writer, this._dataType);
        if(this.data == null || this.data.size() == 0){
            if(this._dataType == WindStreamDataType.ARRAY){
                Serializer.writeInt16(writer, (short)0);
            }else{
                Serializer.writeInt32(writer, 0);
            }
        }else{
            if(this._dataType == WindStreamDataType.ARRAY){
                Serializer.writeInt16(writer, (short)this.data.size());
            }else{
                Serializer.writeInt32(writer, this.data.size());
            }

            for(WindDataItem item : this.data){
                item.serialize(writer);
            }
        }
    }

    Object deserialize(DataInputStream br) throws IOException {
        this._dataType = Serializer.readByte(br);
        int len;
        if(this._dataType == WindStreamDataType.ARRAY)
            len = Serializer.readInt16(br);
        else
            len = Serializer.readInt32(br);
        
        WindDataItem it;
        if(len > 0){
            for(short i=0; i<len; i++){
                it = new WindDataItem();
                it.deserialize(br);
                this.data.add(it);
            }
        }else{
            this.data = null;
        }
        
        return this;
    }
}
