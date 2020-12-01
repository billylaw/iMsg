package wind.ibroker.common.windstream;



import wind.ibroker.comm.DataRow;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;

/**
 * WindStream序列化器
 * */
public class WindStreamSerializer {
    /**
     * 写入数据行
     * @param row 数据行
     * @param dataFields 字段列表
     * @param writer 输出流
     * */
    public static void windStreamWriteObject(DataRow row, List<WindStreamDataField> dataFields, DataOutputStream writer) {
        if(row == null || dataFields == null){
            return;
        }

        if(row.size() != dataFields.size()){
            return;
        }

        for(int i=0; i<row.size(); i++){
            windStreamWriteObject(row.get(i), dataFields.get(i).getDataType(), writer);
        }
    }

    /**
     * 写入数据
     * */
    public static void windStreamWriteObject(Object o, byte dataType, DataOutputStream writer) {
        try{
            switch(dataType){
                case WindStreamDataType.BOOL:
                    boolean boolVal = (boolean)o;
                    Serializer.writeBool(writer, boolVal);
                    break;

                case WindStreamDataType.BYTE:
                    byte byteVal = (byte)o;
                    Serializer.writeByte(writer, byteVal);
                    break;

                case WindStreamDataType.DOUBLE:
                    double doubleVal = (double)o;
                    Serializer.writeDouble(writer, doubleVal);
                    break;

                case WindStreamDataType.FLOAT:
                    float floatVal = (float)o;
                    Serializer.writeSingle(writer, floatVal);
                    break;

                case WindStreamDataType.ARRAY:
                    WindArray arrayVal = new WindArray(false);
                    arrayVal.init((Array)o);
                    break;

                case WindStreamDataType.HUGEARRAY:
                    if(o instanceof WindArray){
                        WindArray hugeArrayVal = (WindArray)o;
                        hugeArrayVal.serialize(writer);
                    }
                    break;

                case WindStreamDataType.HUGESTRING:
                    String hugeStringVal = o instanceof String ? (String)o : null;
                    Serializer.writeHugeString(writer, hugeStringVal);
                    break;

                case WindStreamDataType.INT16:
                    short shortVal = (short)o;
                    Serializer.writeInt16(writer, shortVal);
                    break;

                case WindStreamDataType.INT32:
                    int intVal = (int)o;
                    Serializer.writeInt32(writer, intVal);
                    break;

                case WindStreamDataType.INT64:
                    long longVal = (long)o;
                    Serializer.writeInt64(writer, longVal);
                    break;

                case WindStreamDataType.STRING:
                    String stringVal = o instanceof String ? (String)o : null;
                    Serializer.writeString(writer, stringVal);
                    break;

                case WindStreamDataType.WINDSTREAM:
                    if(o instanceof WindStream){
                        WindStream windStreamVal = (WindStream)o;
                        windStreamVal.serialize(writer);
                    }
                    break;
                    
                case WindStreamDataType.BYTEARRY:
                    Serializer.writeByteArray(writer, (byte[])o);
                    break;

                default:
                    break;
            }
        }catch(Exception ex){}
    }

    /**
     * 读取WindStream字段值
     */
    static Object windStreamReadObject(byte dataType, DataInputStream br) throws IOException {
        switch(dataType){
            case WindStreamDataType.BOOL:
                return Serializer.readBool(br);
                
            case WindStreamDataType.BYTE:
                return Serializer.readByte(br);
                
            case WindStreamDataType.DOUBLE:
                return Serializer.readDouble(br);
                
            case WindStreamDataType.FLOAT:
                return Serializer.readSingle(br);
                
            case WindStreamDataType.ARRAY:
                WindArray bl = new WindArray(false);
                return bl.deserialize(br);
                
            case WindStreamDataType.HUGEARRAY:
                WindArray wa = new WindArray(true);
                return wa.deserialize(br);
                
            case WindStreamDataType.HUGESTRING:
                return Serializer.readHugeString(br);
                
            case WindStreamDataType.INT16:
                return Serializer.readInt16(br);
                
            case WindStreamDataType.INT32:
                return Serializer.readInt32(br);
                
            case WindStreamDataType.INT64:
                return Serializer.readInt64(br);
                
            case WindStreamDataType.STRING:
                return Serializer.readString(br);
                
            case WindStreamDataType.WINDSTREAM:
                WindStream ws = new WindStream();
                return ws.deserialize(br);
                
            case WindStreamDataType.BYTEARRY:
                return Serializer.readByteArray(br);
                
            default:
                break;
        }
        
        return null;
    }
}
