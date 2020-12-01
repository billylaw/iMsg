package wind.ibroker.common.windstream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Wind数据对象
 * */
public class WindDataItem implements IWindStreamSerilize{
    /**
     * 数据类型
     * */
    private byte dataType;

    /**
     * 值
     * */
    private Object value;

    public void setDataType(byte dataType) {
        this.dataType = dataType;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public void serialize(DataOutputStream writer) throws IOException {
        Serializer.writeByte(writer, dataType);
        WindStreamSerializer.windStreamWriteObject(value, dataType, writer);
    }

    public Object deserialize(DataInputStream br) throws IOException {
        this.dataType = Serializer.readByte(br);
        this.value = WindStreamSerializer.windStreamReadObject(dataType, br);
        return this;
    }
}
