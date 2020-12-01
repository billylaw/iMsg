package wind.ibroker.comm;


import wind.ibroker.common.DataType;

/**
 * 统一序列化返回对象
 */
public  class SerializeItem {
    public short dataType;
    public Object object;

    /**
     * SerializeItem
     * @param dataType
     * @see DataType
     * @param object
     */
    public SerializeItem(short dataType, Object object) {
        this.dataType = dataType;
        this.object = object;
    }

    public static SerializeItem createSerializeItem(int val)
    {
        return new SerializeItem(DataType._INT,val);
    }

    public static SerializeItem createSerializeItem(long val)
    {
        return new SerializeItem(DataType._INT64,val);
    }

    public static SerializeItem createSerializeItem(short val)
    {
        return new SerializeItem(DataType._SHORT,val);
    }

    public static SerializeItem createSerializeItem(byte val)
    {
        return new SerializeItem(DataType._BYTE,val);
    }

    public static SerializeItem createSerializeItem(boolean val)
    {
        return new SerializeItem(DataType._BOOL,val);
    }

    public static SerializeItem createSerializeItem(boolean[] val)
    {
        return new SerializeItem(DataType._BOOLEANARRAY,val);
    }

    public static SerializeItem createSerializeItem(String val)
    {
        return new SerializeItem(DataType._STRING,val);
    }

    public static SerializeItem createSerializeItem(String[] val)
    {
        return new SerializeItem(DataType._STRINGARRAY,val);
    }

    public static SerializeItem createSerializeItem(String[][] val)
    {
        return new SerializeItem(DataType._STRINGARRAY2,val);
    }

    public static SerializeItem createSerializeItem(int[] val)
    {
        return new SerializeItem(DataType._INTARRAY,val);
    }


    public static SerializeItem createSerializeItem(byte[] val)
    {
        return new SerializeItem(DataType._BYTEARRAY,val);
    }

    public static SerializeItem createSerializeItem(double val)
    {
        return new SerializeItem(DataType._DOUBLE,val);
    }

    public static SerializeItem createSerializeItem(float val)
    {
        return new SerializeItem(DataType._FLOAT,val);
    }

    public static SerializeItem createSerializeItem(double[] val)
    {
        return new SerializeItem(DataType._DOUBLEARRAY,val);
    }

    public static SerializeItem createSerializeItem(float[] val)
    {
        return new SerializeItem(DataType._FLOATARRAY,val);
    }
}