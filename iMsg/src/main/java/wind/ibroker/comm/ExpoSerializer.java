package wind.ibroker.comm;



import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ExpoSerializer {

    //缺省的字节编码
    private static final String DEFAULT_ENCODING = "UTF-8";

    //读取一个字节流
    public static byte readByte(InputStream is) throws IOException {
        byte[] bytes = new byte[1];
        is.read(bytes, 0, 1);

        return bytes[0];
    }
    //写入一个字节
    public static void writeByte(OutputStream os, byte value) throws IOException{
        os.write(new byte[]{ value });
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

    //读取字节数组
    private static  byte[] readBytes(InputStream is, int count) throws IOException{
        byte[] bytes = new byte[count];
        is.read(bytes, 0, count);
        return bytes;
    }

    //写入一个字节数组
    public static void writeBytes(OutputStream os, byte[] values) throws IOException{
        os.write(values,0,values.length);
    }

    //读取short（按照网络字节序Big-Endian 大端）
    //先转成大端存储，未测试。
    public static short readShort(InputStream is) throws IOException {
        byte[] bytes = new byte[2];
        is.read(bytes, 0, 2);
//        ByteBuffer bb = ByteBuffer.wrap(bytes);
//        bb.order(ByteOrder.LITTLE_ENDIAN);
//        return Conversion.byteArrayToShort(bb.array(), 0, (short)0, 0, 2);
        int result = (bytes[0] & 0xFF) << 8;
        result |= (bytes[1]) & 0xFF;
        return  (short) result;
    }

    //写入short （按照网络字节序Big-Endian）
    public static void writeShort(OutputStream os, short value) throws IOException {
        byte[] bytes = new byte[2];
//        Conversion.shortToByteArray(value, 0, bytes, 0, 2);
//        bytes[0] = (byte)(value & 0xFF);
//        os.write(bb.array());
        bytes[1] = (byte)(value & 0xFF);
        bytes[0] = (byte)((value & 0xFF00) >> 8);

        os.write(bytes);

    }

    /// 读取Int32（按照网络字节序Big-Endian）
    public static int readInt(InputStream is) throws IOException {
        byte[] bytes = new byte[4];
        is.read(bytes);
//        ByteBuffer bb = ByteBuffer.wrap(bytes);
//        bb.order(ByteOrder.LITTLE_ENDIAN);
//        Conversion.byteArrayToInt(bb.array(), 0, 0, 0, 4);
        int result = (bytes[0]& 0xFF) << 24;
        result |= (bytes[1] & 0xFF) << 16;
        result |= (bytes[2] & 0xFF) << 8;
        result |= bytes[3] & 0xFF;
        //result = (bytes[0] <<24 ) + (bytes[1] <<16) + (bytes[2] <<8) + bytes[3];
        return result;
    }

    //写入int （按照网络字节序Big-Endian）
    public static void writeInt(OutputStream os, int value) throws IOException {
        byte[] bytes = new byte[4];
//        Conversion.intToByteArray(value, 0, bytes, 0, 4);
//        ByteBuffer bb = ByteBuffer.wrap(bytes);
//        bb.order(ByteOrder.LITTLE_ENDIAN);
//        os.write(bb.array());
        bytes[3] = (byte) (value& 0xFF);
        bytes[2] = (byte) ((value& 0xFF00)>>8);
        bytes[1] = (byte) ((value& 0xFF0000)>>16);
        bytes[0] = (byte) ((value& 0xFF000000)>>24);
        os.write(bytes);
    }


    /**
     * 读取int数组
     * @param is
     * @return
     * @throws IOException
     */
    public static int[] readIntShortArray(InputStream is) throws  IOException{
        int count = ExpoSerializer.readShort(is);
        int[] data = new int[count];
        for(int i=0;i<count;++i){
            data[i] = readInt(is);
        }
        return  data;
    }


    //读取一个long
    public static long readLong(InputStream is) throws IOException{
        byte[] bytes = new byte[8];
        is.read(bytes, 0, 8);
//        ByteBuffer bb = ByteBuffer.wrap(bytes);
//        bb.order(ByteOrder.LITTLE_ENDIAN);
//        return Conversion.byteArrayToLong(bb.array(), 0, 0L, 0, 8);
        long result = (((long)(bytes[0] & 0xFF)) << 56);
        result |= (((long)(bytes[1] & 0xFF)) << 48) ;
        result |=  (((long)(bytes[2] & 0xFF)) << 40);
        result |=  (((long)(bytes[3] & 0xFF))<< 32);
        result |=  (((long)(bytes[4] & 0xFF)) << 24);
        result |=  (((long)(bytes[5] & 0xFF)) << 16);
        result |=  (((long)(bytes[6] & 0xFF)) << 8);
        result |=  (((long)(bytes[7]) & 0xFF));
        return result;
    }

    //写入一个long
    public static void writeLong(OutputStream os, long value) throws IOException{
        byte[] bytes = new byte[8];
//        Conversion.longToByteArray(value, 0, bytes, 0, 8);
//        ByteBuffer bb = ByteBuffer.wrap(bytes);
//        bb.order(ByteOrder.LITTLE_ENDIAN);
//        os.write(bb.array());
        bytes[7] = (byte) (value& 0xFF);
        bytes[6] = (byte) ((value& 0xFF00)>>8);
        bytes[5] = (byte) ((value& 0xFF0000)>>16);
        bytes[4] = (byte) ((value& 0xFF000000)>>24);
        bytes[3] = (byte) ((value& 0xFF00000000L)>>32);
        bytes[2] = (byte) ((value& 0xFF0000000000L)>>40);
        bytes[1] = (byte) ((value& 0xFF000000000000L)>>48);
        bytes[0] = (byte) ((value& 0xFF00000000000000L)>>56);
        os.write(bytes);
    }


    /// <summary>
    /// 读取动态字节数组
    /// </summary>
    /// <param name="reader">BinaryReader类实例</param>
    /// <returns></returns>
    public static byte[] readByteArray(InputStream is) throws IOException {
        int length = readInt(is);
        if(length == 0)
            return null;
        else{
            byte[] bytes = new byte[length];
            is.read(bytes, 0, length);
            return bytes;
        }
    }

    /// <summary>
    /// 写入动态字节数组
    /// </summary>
    /// <param name="writer">BinaryWriter类实例</param>
    /// <param name="v"></param>
    public static void writeByteArray(OutputStream os, byte[] values) throws IOException {
        if(values == null || values.length == 0)
            writeInt(os, 0);
        else{
            writeInt(os, values.length);
            os.write(values,0,values.length);
        }
    }

    /// <summary>
    /// 读取字符串
    /// </summary>
    /// <param name="reader">BinaryReader类实例</param>
    /// <returns></returns>
    public static String readString(InputStream is) throws IOException {
        short len = readShort(is);
        if(len == 0) return null;
        byte[] bytes = new byte[len];
        is.read(bytes, 0, len);
        return new String(bytes, DEFAULT_ENCODING);
    }

    /// <summary>
    /// 写入字符串
    /// </summary>
    /// <param name="writer">BinaryWriter类实例</param>
    /// <param name="s">字符串</param>
    public static  void  writeString(OutputStream os, String value) throws  IOException{
        if(!StringUtils.isEmpty(value)){
            byte[] bytes = value.getBytes(DEFAULT_ENCODING);
            writeShort(os, (short) bytes.length);
            if(bytes.length > 0){
                os.write(bytes);
            }
        }else{
            writeShort(os, (short) 0);
        }
    }


    /// <summary>
    /// 读取固定长度字符串
    /// </summary>
    /// <param name="reader">BinaryReader类实例</param>
    /// <param name="len">字符串长度</param>
    /// <returns></returns>
    public static String readFixedString(InputStream is, int len) throws  IOException{
        if(len == 0) return null;
        byte[] bytes = new byte[len];
        is.read(bytes, 0, len);
        String temp =  new String(bytes, DEFAULT_ENCODING);
        //return temp.substring(0, temp.length()-1); //去掉末尾的'\0'？
        return temp;
    }

    /// <summary>
    /// 按固定长度写入字符串
    /// </summary>
    /// <param name="writer">BinaryWriter类实例</param>
    /// <param name="s">字符串</param>
    /// <param name="len">长度</param>
    public static void writeFixedString(OutputStream writer, String value, int len) throws IOException {
        if(StringUtils.isEmpty(value)) return;
        byte[] bytes = new byte[len];
        byte[] byteSrc = value.getBytes(DEFAULT_ENCODING);
        int lenTemp = byteSrc.length > len ? len: byteSrc.length;
        for(int i=0;i<lenTemp; ++i){
            bytes[i] = byteSrc[i];
        }
        writer.write(bytes);
    }

    /// <summary>
    /// 读取字符串数组
    /// </summary>
    /// <param name="reader">BinaryReader类实例</param>
    /// <returns></returns>
    public static String[] readStringShortArray(InputStream is) throws IOException{
        int count = readShort(is);
        String[] rt = new String[count];
        for(int i=0; i<count; i++){
            rt[i] = readString(is);
        }
        return rt;
    }

    /// <summary>
    /// 写入字符串数组
    /// </summary>
    /// <param name="writer">BinaryWriter类实例</param>
    /// <param name="data">字符串数组</param>
    public static void writeStringShortArray(OutputStream os, String[] data) throws IOException {
        if (null != data && data.length > 0) {
            writeShort(os,(short)data.length );
            for(String s : data){
                writeString(os, s);
            }
        }
        else
            os.write((short)0);
    }

    /// <summary>
    /// 读取字符串数组
    /// </summary>
    /// <param name="reader">BinaryReader类实例</param>
    /// <returns></returns>
    public static boolean[] readBooleanShortArray(InputStream is) throws IOException {
        int count = readShort(is);
        boolean[] data = new boolean[count];
        for(int i=0; i<count; i++){
            data[i] = readBool(is);
        }
        return data;
    }

    /// <summary>
    /// 写入bool数组
    /// </summary>
    /// <param name="writer">BinaryWriter类实例</param>
    /// <param name="data">字符串数组</param>
    public static void writeBoolShortArray(OutputStream os, boolean[] data) throws IOException {
        if (null != data && data.length > 0){
           writeShort(os, (short)data.length);
            for(boolean s : data){
                writeBool(os, s);
            }
        }
        else
            os.write((short)0);
    }

    /// <summary>
    /// 读取int数组
    /// </summary>
    /// <param name="reader">BinaryReader类实例</param>
    /// <returns></returns>
    public static int[] ReadIntShortArray(InputStream is) throws IOException {
        int count = readShort(is);
        int[] data = new int[count];
        for(int i=0; i<count; i++){
            data[i] = readInt(is);
        }
        return data;
    }

    /// <summary>
    /// 写入int数组
    /// </summary>
    /// <param name="writer">BinaryWriter类实例</param>
    /// <param name="data">字符串数组</param>
    public static void writeIntShortArray(OutputStream os, int[] data) throws IOException {
        if (null != data && data.length > 0) {
            writeShort(os, (short)data.length);
            for(int s : data){
                writeInt(os, s);
            }
        }
        else
            os.write((short)0);
    }

    /// <summary>
    /// 序列化string[][]
    /// </summary>
    /// <param name="bw">写入器</param>
    /// <param name="arr"></param>
    public static void writeStringShortArray2(OutputStream os, String[][] value) throws IOException {
        if(value ==null || value.length == 0){
            writeShort(os, (short)0);
            return;
        }
        writeShort(os, (short)value.length);
        for (String[] strArr: value) {
            writeStringShortArray(os, strArr);
        }
    }

    /// <summary>
    /// 反序列化String[][]
    /// </summary>
    /// <param name="br">读取器</param>
    /// <returns></returns>
    public static String[][] readStringShortArray2(InputStream is) throws IOException {
        short len = readShort(is);
        if (len == 0)
            return null;
        String[][] rt = new String[len][];
        for (short i = 0; i < len; i++) {
            rt[i] = readStringShortArray(is);
        }
        return rt;
    }


    public static void writeDouble(OutputStream os,double val) throws IOException {
        long lval = Double.doubleToLongBits(val);
        ExpoSerializer.writeLong(os,lval);
    }

    public static double readDouble(InputStream is) throws IOException {
        long lval = ExpoSerializer.readLong(is);
        return Double.longBitsToDouble(lval);
    }

    public static void  writeFloat(OutputStream os,float val) throws IOException {
        int lval = Float.floatToIntBits(val);
        ExpoSerializer.writeInt(os,lval);
    }

    public static float readFloat(InputStream is) throws IOException
    {
        int ret = ExpoSerializer.readInt(is);
        return Float.intBitsToFloat(ret);
    }


    public static double[]  readDoubleArray(InputStream is) throws IOException {
        short len = (short)readShort(is);
        double[] val = new double[len];
        for(int i=0;i<len;i++)
        {
            val[i] = readDouble(is);
        }
        return val;
    }

    public static void writeDoubleArray(OutputStream os,double[] val) throws IOException
    {
        if(val == null || val.length ==0)
        {
            writeShort(os,(short)0);
        }else
        {
            short len = (short)val.length;
            writeShort(os,len);
            for(int i=0;i<len;i++)
            {
                writeDouble(os,val[i]);
            }
        }
    }

    public static float[]  readFloatArray(InputStream is) throws IOException {
        short len = (short)readShort(is);
        float[] val = new float[len];
        for(int i=0;i<len;i++)
        {
            val[i] = readFloat(is);
        }
        return val;
    }

    public static void writeFloatArray(OutputStream os,float[] val) throws IOException
    {
        if(val == null || val.length ==0)
        {
            writeInt(os,0);
        }else
        {
            short len = (short)val.length;
            writeShort(os,len);
            for(int i=0;i<len;i++)
            {
                writeFloat(os,val[i]);
            }
        }
    }

}
