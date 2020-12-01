/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wind.ibroker.comm;

import org.apache.commons.lang3.Conversion;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import wind.ibroker.common.DataField;
import wind.ibroker.common.DataType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Wind IMarket通用的序列化器
 * @author nbwu
 */
public class ReportSerializer {
    //缺省的字节编码
    private static final String DEFAULT_ENCODING = "UTF-8";
    
    //序列化字符串数组
    public static void writeStringArray(OutputStream os, String[] values) throws UnsupportedEncodingException, IOException{
        if(values == null || values.length == 0){
            writeInt(os, 0);
        }else{
            writeInt(os, values.length);
            for(int i=0; i<values.length; i++){
                writeString(os, values[i]);
            }
        }
    }
    
    //读取一个字符串数组
    public static String[] readStringArray(InputStream is) throws UnsupportedEncodingException, IOException{
        int len = readInt(is);
        if(len <= 0)
            return null;
        
        String[] rt = new String[len];
        for(int i=0; i<len; i++){
            rt[i] = readString(is);
        }
        
        return rt;
    }

    //写入一个double数组
    public static void writeDoubleArray(OutputStream os, double[] values) throws IOException{
        if(values == null || values.length == 0){
            writeInt(os, 0);
        }else{
            writeInt(os, values.length);
            for(int i=0; i<values.length; i++){
                writeDouble(os, values[i]);
            }
        }
    }
    
    //读取一个double数组
    public static double[] readDoubleArray(InputStream is) throws IOException{
        int length = readInt(is);
        if(length == 0)
            return null;
        else{
            double[] rt = new double[length];
            for(int i=0; i<length; i++){
                rt[i] = readDouble(is);
            }
            
            return rt;
        }
    }
    
    //写入一个字节数组
    public static void writeByteArray(OutputStream os, byte[] values) throws IOException{
        if(values == null || values.length == 0)
            writeInt(os, 0);
        else{
            writeInt(os, values.length);
            os.write(values);
        }
    }

    //读取一个字节数组
    public static byte[] readByteArray(InputStream is) throws IOException{
        int length = readInt(is);
        if(length == 0)
            return null;
        else{
            byte[] bytes = new byte[length];
            is.read(bytes, 0, length);
            
            return bytes;
        }
    }
    
    //写入一个字节
    public static void writeByte(OutputStream os, byte value) throws IOException{
        os.write(new byte[]{ value });
    }
    
    //写入一个字节数组
    public static void writeBytes(OutputStream os, byte[] values) throws IOException{
        if(values == null)
            writeInt(os, 0);
        else{
            writeInt(os, values.length);
            os.write(values);
        }
    }
    
    //读取一个字节流
    public static byte readByte(InputStream is) throws IOException{
        byte[] bytes = new byte[1];
        is.read(bytes, 0, 1);
        
        return bytes[0];
    }
    
    //写入一个short
    public static void writeShort(OutputStream os, short value) throws IOException{
        byte[] bytes = new byte[2];
        Conversion.shortToByteArray(value, 0, bytes, 0, 2);
        
        os.write(bytes);
    }
    
    //读取一个short
    public static short readShort(InputStream is) throws IOException{
        byte[] bytes = new byte[2];
        is.read(bytes, 0, 2);
        
        return Conversion.byteArrayToShort(bytes, 0, (short)0, 0, 2);
    }
    
    //写入一个int32
    public static void writeInt(OutputStream os, int value) throws IOException{
        byte[] bytes = new byte[4];
        Conversion.intToByteArray(value, 0, bytes, 0, 4);
        os.write(bytes, 0, 4);
    }
    
    //读取一个int
    public static int readInt(InputStream is) throws IOException{
        byte[] bytes = new byte[4];
        is.read(bytes);
        
        return Conversion.byteArrayToInt(bytes, 0, 0, 0, 4);
    }
    
    //写入一个long
    public static void writeLong(OutputStream os, long value) throws IOException{
        byte[] bytes = new byte[8];
        Conversion.longToByteArray(value, 0, bytes, 0, 8);
        
        os.write(bytes);
    }
    
    //读取一个long
    public static long readLong(InputStream is) throws IOException{
        byte[] bytes = new byte[8];
        is.read(bytes, 0, 8);
        
        return Conversion.byteArrayToLong(bytes, 0, 0L, 0, 8);
    }
    
    //读取一个单精度浮点值
    public static float readFloat(InputStream is) throws IOException{
        byte[] bytes = new byte[4];
        is.read(bytes, 0, 4);
        int data = Conversion.byteArrayToInt(bytes, 0, 0, 0, 4);
        
        return Float.intBitsToFloat(data);
    }
    
    //读取一个float数组
    public static float[] readFloatArray(InputStream is) throws IOException{
        int len = readInt(is);
        if(len <= 0)
            return null;
        
        float[] rt = new float[len];
        for(int i=0; i<len; i++){
            rt[i] = readFloat(is);
        }
        
        return rt;
    }
    
    //写入一个单精度浮点数
    public static void writeFloat(OutputStream os, float value) throws IOException{
        int data = Float.floatToIntBits(value);
        byte[] bytes = new byte[4];
        Conversion.intToByteArray(data, 0, bytes, 0, 4);
        
        os.write(bytes);
    }
    
    //读取一个bool
    public static boolean readBool(InputStream is) throws IOException{
        byte[] bytes = new byte[1];
        is.read(bytes, 0, 1);
        
        return bytes[0] != 0;
    }
    
    //写入一个bool值
    public static void writeBool(OutputStream os, boolean value) throws IOException{
        if(value){
            os.write(new byte[]{ 1 });
        }else{
            os.write(new byte[]{ 0 });
        }
    }
    
    //序列化字符串
    public static void writeString(OutputStream os, String value) throws UnsupportedEncodingException, IOException {
        int len = 0;
        if(value == null || value.length() == 0){
            writeInt(os, len);
            return;
        }
        
        byte[] bytes = value.getBytes(DEFAULT_ENCODING);
        len = bytes.length;
        writeInt(os, len);
        os.write(bytes);
    }

    //读取一个字符串
    public static String readString(InputStream is) throws UnsupportedEncodingException, IOException {
        int len = readInt(is);
        if(len == 0)
            return Strings.EMPTY;
        else{
            byte[] bytes = new byte[len];
            is.read(bytes, 0, len);
            return new String(bytes, DEFAULT_ENCODING);
        }
    }

    //写入一个double
    public static void writeDouble(OutputStream os, double value) throws IOException {
        long data = Double.doubleToLongBits(value);
        
        byte[] bytes = new byte[8];
        Conversion.longToByteArray(data, 0, bytes, 0, 8);
        os.write(bytes);
    }

    //读取一个double
    public static double readDouble(InputStream is) throws IOException {
        byte[] bytes = new byte[8];
        is.read(bytes, 0, 8);
        
        long data = Conversion.byteArrayToLong(bytes, 0, 0, 0, 8);
        return Double.longBitsToDouble(data);
    }

    //动态读取流
    public static void readObject(InputStream is, Object[] values) throws UnsupportedEncodingException, IOException{
        if(values == null)
            return;
        
        Object value;
        for(int i=0; i<values.length; i++){
            value = values[i];
            
            if(value instanceof String){
                value = readString(is);
            }else if(value instanceof Integer){
                value = readInt(is);
            }else if(value instanceof Float){
                value = readFloat(is);
            }else if(value instanceof Double){
                value = readDouble(is);
            }else if(value instanceof Byte){
                value = readByte(is);
            }else if(value instanceof byte[]){
                value = readByteArray(is);
            }else if(value instanceof Boolean){
                value = readBool(is);
            }else if(value instanceof Short){
                value = readShort(is);
            }else if(value instanceof Long){
                value = readLong(is);
            }
            
            continue;
        }
    }

    //动态读取流
    public static Object wudsReadObject(InputStream is, DataField field) throws UnsupportedEncodingException, IOException{
        switch(field.getDataType()){
            case DataType._BOOL:
                return readBool(is);
                
            case DataType._BYTE:
                return readByte(is);
                
            case DataType._SHORT:
                return readShort(is);
                
            case DataType._INT:
                return readInt(is);
                
            case DataType._INT64:
                return readLong(is);
                
            case DataType._FLOAT:
                return readFloat(is);
                
            case DataType._DOUBLE:
                return readDouble(is);
                
            case DataType._STRING:
                return readString(is);
            case DataType._BYTEARRAY:
                return readByteArray(is);
                
            case DataType._STOP:
            case DataType._CHAR8DATE:
            case DataType._CURRENCY:
            case DataType._CUSTOM:
            case DataType._MEMO:
            case DataType._MEMOURL:
            case DataType._NULL:
            case DataType._RECORDSET:
                return new Object();
            default:
                return new Object();
        }
    }

    //动态写入流
    public static void writeObject(OutputStream os, Object[] values) throws IOException{
        if(values == null)
            return;
        
        Object value;
        for(int i=0; i<values.length; i++){
            value = values[i];
            if(value instanceof String){
                writeString(os, value.toString());
            }else if(value instanceof Integer){
                writeInt(os, (int)value);
            }else if(value instanceof Double){
                writeDouble(os, (double)value);
            }else if(value instanceof Byte){
                writeByte(os, (byte)value);
            }else if(value instanceof byte[]){
                writeBytes(os, (byte[])value);
            }else if(value instanceof Boolean){
                writeBool(os, (boolean)value);
            }else if(value instanceof Short){
                writeShort(os, (short)value);
            }else if(value instanceof Long){
                writeLong(os, (long)value);
            }else if(value instanceof Float){
                writeFloat(os, (float)value);
            }
            
            continue;
        }
    }

    //动态写入流
    public static void writeObjectEx(OutputStream os, List<?> values) throws IOException{
        if(values == null)
            return;
        
        Object value;
        for(int i=0; i<values.size(); i++){
            value = values.get(i);
            if(value instanceof String){
                writeString(os, value.toString());
            }else if(value instanceof Integer){
                writeInt(os, (int)value);
            }else if(value instanceof Double){
                writeDouble(os, (double)value);
            }else if(value instanceof Byte){
                writeByte(os, (byte)value);
            }else if(value instanceof byte[]){
                writeBytes(os, (byte[])value);
            }else if(value instanceof Boolean){
                writeBool(os, (boolean)value);
            }else if(value instanceof Short){
                writeShort(os, (short)value);
            }else if(value instanceof Long){
                writeLong(os, (long)value);
            }else if(value instanceof Float){
                writeFloat(os, (float)value);
            }
        }
    }

    //动态写入流
    public static void wudsWriteObject(OutputStream os, List<?> values, List<DataField> fields) throws IOException{
        if(values == null || fields == null)
            return;
        
        if(values.size() != fields.size())
            return;
        
        for(int i=0; i<values.size(); i++){
            Object value = values.get(i);
            switch(fields.get(i).getDataType()){
                case DataType._BOOL:
                    if(value == null){
                        writeBool(os, false);
                    }else{
                        writeBool(os, (boolean)value);
                    }
                    break;
                    
                case DataType._BYTE:
                    if(value == null){
                        writeByte(os, (byte)0);
                    }else{
                        writeByte(os, (byte)value);
                    }
                    break;
                    
                case DataType._SHORT:
                    if(value == null){
                        writeShort(os, (short)0);
                    }else{
                        writeShort(os, (short)value);
                    }
                    break;
                    
                case DataType._INT:
                    if(value == null){
                        writeInt(os, 0);
                    }else{
                        writeInt(os, (int)value);
                    }
                    break;
                    
                case DataType._INT64:
                    if(value == null){
                        writeLong(os, 0L);
                    }else{
                        writeLong(os, (long)value);
                    }
                    break;
                    
                case DataType._FLOAT:
                    if(value == null){
                        writeFloat(os, 0.0f);
                    }else{
                        writeFloat(os, (float)value);
                    }
                    break;
                    
                case DataType._DOUBLE:
                    if(value == null){
                        writeDouble(os, 0.0f);
                    }else{
                        writeDouble(os, (double)value);
                    }
                    break;
                    
                case DataType._STRING:
                    if(value == null){
                        writeString(os, StringUtils.EMPTY);
                    }else{
                        writeString(os, value.toString());
                    }
                    break;
                case DataType._BYTEARRAY:
                   writeByteArray(os,(byte[])value);
                    break;
                default:
                    break;
            }
        }
    }
}
