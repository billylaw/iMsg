/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wind.ibroker.common;

/**
 * 数据类型
 * @author nbwu
 */
public class DataType {
    //bool
    public static final byte _BOOL = 0;
    
    //string
    public static final byte _STRING = 1;
    
    //byte
    public static final byte _BYTE = 2;
    
    //short
    public static final byte _SHORT = 3;
    
    //ushort
    public static final byte _USHORT = 4;
    
    //int
    public static final byte _INT = 5;
    
    //uint
    public static final byte _UINT = 6;
    
    //int64
    public static final byte _INT64 = 7;
    
    //float
    public static final byte _FLOAT = 8;
    
    //double
    public static final byte _DOUBLE = 9;
    
    //char8date
    public static final byte _CHAR8DATE = 10;
    
    //memo
    public static final byte _MEMO = 11;
    
    //memourl
    public static final byte _MEMOURL = 12;
    
    //custom
    public static final byte _CUSTOM = 13;
    
    //datetime
    public static final byte _DATETIME = 14;
    
    //分隔符，结束符
    public static final byte _STOP = 15;
    
    //currency
    public static final byte _CURRENCY = 16;
    
    //byte[]
    public static final byte _BYTEARRAY = 17;
    
    //RecordSet
    public static final byte _RECORDSET = 18;
    
    //8字节无符号的长整型
    public static final byte _UINT64 = 19;
    
    //空值
    public static final byte _NULL = -1;

    /*辅助exposerializer 扩展字段*/
    //字符串1维数组
    public static final byte _STRINGARRAY = 90;

    //字符串2维数组
    public static final byte _STRINGARRAY2 = 91;

    //boolean 1维数组
    public static final byte _BOOLEANARRAY = 92;

    //short1维数组
    public static final byte _SHORTARRAY = 93;

    //short2维数组
    public static final byte _SHORTARRAY2 = 94;

    //int 1维数组
    public static final byte _INTARRAY = 95;

    //float 1维数组
    public static final byte _FLOATARRAY = 96;

    //double 1维数组
    public static final byte _DOUBLEARRAY = 97;
}
