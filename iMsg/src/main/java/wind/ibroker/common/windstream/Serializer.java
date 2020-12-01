package wind.ibroker.common.windstream;



import org.apache.commons.lang3.StringUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * WindStream序列化工具，使用网络字节序（BIG-ENDIAN）
 * */
public class Serializer {
    /**
     * 默认字符编码
     * */
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    /**
     * 写入一个int16数据
     * */
    public static void writeInt16(DataOutputStream writer, short value) throws IOException {
        byte[] data = new byte[2];
        data[0] = (byte)(value >> 8);
        data[1] = (byte)value;

        writer.write(data);
    }

    /**
     * 写入一个string
     * */
    public static void writeString(DataOutputStream writer, String value) throws IOException {
        byte[] data = new byte[0];
        if(!StringUtils.isEmpty(value)){
            data = value.getBytes(DEFAULT_CHARSET);
        }

        Serializer.writeInt16(writer, (short)data.length);
        if(data.length > 0){
            writer.write(data);
        }
    }

    /**
     * 写入一个byte
     * */
    public static void writeByte(DataOutputStream writer, byte value) throws IOException {
        byte[] data = new byte[1];
        data[0] = value;
        writer.write(data);
    }

    /**
     * 写入一个int32
     * */
    public static void writeInt32(DataOutputStream writer, int value) throws IOException {
        byte[] data = new byte[4];
        data[0] = (byte)(value >> 24);
        data[1] = (byte)(value >> 16);
        data[2] = (byte)(value >> 8);
        data[3] = (byte)value;

        writer.write(data);
    }

    /**
     * 写入bool值
     * */
    public static void writeBool(DataOutputStream writer, boolean boolVal) throws IOException {
        if(boolVal){
            writer.writeByte(1);
        }else{
            writer.writeByte(0);
        }
    }

    /**
     * 写入double值
     * */
    public static void writeDouble(DataOutputStream writer, double doubleVal) throws IOException {
        long value = Double.doubleToLongBits(doubleVal);
        byte[] data = new byte[8];
        data[0] = (byte)(value >> 56);
        data[1] = (byte)(value >> 48);
        data[2] = (byte)(value >> 40);
        data[3] = (byte)(value >> 32);
        data[4] = (byte)(value >> 24);
        data[5] = (byte)(value >> 16);
        data[6] = (byte)(value >> 8);
        data[7] = (byte)value;

        writer.write(data);
    }

    /**
     * 写入float值
     * */
    public static void writeSingle(DataOutputStream writer, float floatVal) throws IOException {
        int value = Float.floatToIntBits(floatVal);
        byte[] data = new byte[4];
        data[0] = (byte)(value >> 24);
        data[1] = (byte)(value >> 16);
        data[2] = (byte)(value >> 8);
        data[3] = (byte)value;

        writer.write(data);
    }

    /**
     * 写入长字符串类型
     * */
    public static void writeHugeString(DataOutputStream writer, String hugeStringVal) throws IOException {
        if(StringUtils.isEmpty(hugeStringVal)){
            Serializer.writeInt32(writer, 0);
        }else{
            byte[] data = hugeStringVal.getBytes(DEFAULT_CHARSET);
            Serializer.writeInt32(writer, data.length);
            writer.write(data);
        }
    }

    /**
     * 写入长整型值
     * */
    public static void writeInt64(DataOutputStream writer, long value) throws IOException {
        byte[] data = new byte[8];
        data[0] = (byte)(value >> 56);
        data[1] = (byte)(value >> 48);
        data[2] = (byte)(value >> 40);
        data[3] = (byte)(value >> 32);
        data[4] = (byte)(value >> 24);
        data[5] = (byte)(value >> 16);
        data[6] = (byte)(value >> 8);
        data[7] = (byte)value;

        writer.write(data);
    }

    /**
     * 读取一个字节
     */
    public static byte readByte(DataInputStream br) throws IOException {
        byte[] data = new byte[1];
        br.read(data);
        
        return data[0];
    }

    /**
     * 读取字符串
     */
    public static String readString(DataInputStream br) throws IOException {
        short len = Serializer.readInt16(br);
        if(len == 0)
            return StringUtils.EMPTY;
        else if(len < 0){
            int length = 0xFFFF & len;            
            byte[] data = new byte[length];
            br.read(data);
            
            return new String(data, DEFAULT_CHARSET);
        }else{
            byte[] data = new byte[len];
            br.read(data);

            return new String(data, DEFAULT_CHARSET);
        }
    }

    /**
     * 读取short
     */
    public static short readInt16(DataInputStream br) throws IOException {
        byte[] data = new byte[2];
        br.read(data);
        
        return (short)(((0xFF & data[0]) << 8) + (0xFF & data[1]));
    }

    /**
     * 读取bool
     */
    static boolean readBool(DataInputStream br) throws IOException {
        byte[] data = new byte[1];
        br.read(data);
        
        return data[0] > 0;
    }

    /**
     * 读取int
     */
    static int readInt32(DataInputStream br) throws IOException {
        byte[] data = new byte[4];
        br.read(data);
        
        return ((0xFF & data[0]) << 24) + ((0xFF & data[1]) << 16) + ((0xFF & data[2]) << 8) + (0xFF & data[3]);
    }

    /**
     * 读取double
     */
    static double readDouble(DataInputStream br) throws IOException {
        byte[] data = new byte[8];
        br.read(data);
        
        long value = ((0xFFL & data[0]) << 56) + ((0xFFL & data[1]) << 48) + ((0xFFL & data[2]) << 40) + ((0xFFL & data[3]) << 32) + ((0xFFL & data[4]) << 24) + ((0xFFL & data[5]) << 16) + ((0xFFL & data[6]) << 8) + (0xFFL & data[7]);
        return Double.longBitsToDouble(value);
    }

    /**
     * 读取float
     */
    static float readSingle(DataInputStream br) throws IOException {
        byte[] data = new byte[4];
        br.read(data);
        
        int value = ((0xFF & data[0]) << 24) + ((0xFF & data[1]) << 16) + ((0xFF & data[2]) << 8) + (0xFF & data[3]);
        return Float.intBitsToFloat(value);
    }

    /**
     * 读取超长字符串
     */
    static String readHugeString(DataInputStream br) throws IOException {
        int len = Serializer.readInt32(br);
        if(len == 0)
            return null;
        
        byte[] data = new byte[len];
        br.read(data);
        return new String(data, DEFAULT_CHARSET);
    }

    static long readInt64(DataInputStream br) throws IOException {
        byte[] data = new byte[8];
        br.read(data);
        
        return ((0xFFL & data[0]) << 56) + ((0xFFL & data[1]) << 48) + ((0xFFL & data[2]) << 40) + ((0xFFL & data[3]) << 32) + ((0xFFL & data[4]) << 24) + ((0xFFL & data[5]) << 16) + ((0xFFL & data[6]) << 8) + (0xFFL & data[7]);
    }

    static void writeByteArray(DataOutputStream writer, byte[] b) throws IOException {
        if(null == b || b.length == 0){
            Serializer.writeInt32(writer, 0);
            return;
        }
        
        Serializer.writeInt32(writer, b.length);
        Serializer.writeBytes(writer, b);
    }

    static void writeBytes(DataOutputStream writer, byte[] b) throws IOException {
        writer.write(b, 0, b.length);
    }

    static byte[] readByteArray(DataInputStream br) throws IOException {
        int len = Serializer.readInt32(br);
        if(len > 0)
            return Serializer.readBytes(br, len);
        
        return null;
    }

    static byte[] readBytes(DataInputStream br, int len) throws IOException {
        byte[] buf = new byte[len];
        br.read(buf);
        
        return buf;
    }
}
