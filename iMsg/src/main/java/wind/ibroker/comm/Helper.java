package wind.ibroker.comm;



import wind.ibroker.util.BaseLog;
import wind.ibroker.common.DataType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Helper {

    /**
     * 日期转str
     *
     * @param date
     * @param format
     * @return
     */
    public static String dateToStr(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 返回指定格式的日期格式
     *
     * @param date
     * @param format
     * @return
     */
    public static String dateToStr(LocalDateTime date, String format) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return date.format(df);
    }

    /* 返回当前日期yyyymmdd日期
     */
    public static String getCurrentDate() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.now().format(df);
    }

    /* 返回指定日期yyyymmdd日期
     */
    public static String getDateStr(LocalDate date) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
        return date.format(df);
    }

    /**
     * string 转成date
     *
     * @param currentTime
     * @param format
     * @return
     * @throws Exception
     */
    public static Date strToDate(String currentTime, String format) throws Exception {
        SimpleDateFormat formats = new SimpleDateFormat(format);
        Date date = null;
        date = formats.parse(currentTime);
        return date;
    }

    /**
     * 转成 yyyyMMddHHmmssfff这种格式
     *
     * @param date
     * @param format
     * @return
     */
    public static long dateToLong(Date date, String format) throws Exception {
        String dateStr = dateToStr(date, format);
        return Long.parseLong(dateStr);
    }

    /**
     * 返回类型
     *
     * @param type
     * @return
     */
    public static byte getDataType(String type) {
        type = type.replaceFirst("class ", "");
        switch (type) {
            case "java.lang.Boolean":
            case "boolean":
                return DataType._BOOL;
            case "java.lang.String":
                return DataType._STRING;
            case "java.lang.Byte":
            case "byte":
                return DataType._STRING;
            case "java.lang.Short":
            case "short":
                return DataType._SHORT;
            case "ushort":
                return DataType._USHORT;
            case "java.lang.Integer":
            case "int":
                return DataType._INT;
            case "uint":
                return DataType._UINT;
            case "java.lang.Long":
            case "long":
                return DataType._INT64;
            case "java.lang.Float":
            case "float":
                return DataType._FLOAT;
            case "java.lang.Double":
            case "double":
                return DataType._DOUBLE;
            //    case "[Ljava.lang.Byte;":
            case "[B":
                return DataType._BYTEARRAY;
            case "null":
                return DataType._NULL;
            case "Selector":
            case "cn.com.wind.ibroker.bondweb.entities.Selector":
                return 10;
            case "SearchSelector":
            case "cn.com.wind.ibroker.bondweb.entities.SearchSelector":
                return 11;
            case "TableSelector":
            case "cn.com.wind.ibroker.bondweb.entities.TableSelector":
                return 12;
            default:
                return DataType._NULL;
        }
    }

    /**
     * 根据item 获得类型
     */
    public static Class getClassByItemType(short itemType) {
        switch (itemType) {
            case EnumItemType.User:
                return null;
//            case  EnumItemType.Quote:
//            case  EnumItemType.QuoteBest:
//            case  EnumItemType.QuoteDone:
//            case  EnumItemType.Dealer:
//            case  EnumItemType.Bond:
//            case  EnumItemType.Institution :
//            case  EnumItemType.User:
//            case  EnumItemType.User:
//            case  EnumItemType.User:
//            case  EnumItemType.User:
//            case  EnumItemType.User:
//            case  EnumItemType.User:
//            case  EnumItemType.User:
//            case  EnumItemType.User:
//            case  EnumItemType.User:

        }
        return null;
    }

    /**
     * 你懂的
     *
     * @param arguments
     * @return
     */
    public static HashMap<String, String> getHashMap(String[][] arguments) {
        if (arguments == null || arguments.length < 1) {
            return null;
        }
        HashMap<String, String> dictParam = new HashMap<String, String>();
        for (String[] item : arguments) {
            if (item == null || item.length != 2 || item[0] == null)
                continue;
            dictParam.put(item[0].toUpperCase(), item[1]);
        }
        return dictParam;
    }

    /**
     * 时间转化成DateTime
     *
     * @param day yyyyMMddhhmmss
     * @return localDatetime
     */
    public static LocalDateTime longToDateTime(long day) {
        long lc = 0;
        int year = (int) (day / 10000000000L);
        lc = day - year * 10000000000L;
        int month = (int) (lc / 100000000);
        lc = lc - month * 100000000;
        int dy = (int) (lc / 1000000);
        lc = lc - dy * 1000000;
        int hour = (int) (lc / 10000);
        lc = lc - hour * 10000;
        int minute = (int) (lc / 100);
        int second = (int) (lc - minute * 100);
        return LocalDateTime.of(year, month, dy, hour, minute, second);
    }

    /**
     * 时间转化成DateTime
     *
     * @param day yyyyMMdd
     * @return localDatetime
     */
    public static LocalDate IntToLocalDate(int day) {
        long lc = 0;
        int year = (int) (day / 10000000);
        lc = day - year * 10000000;
        int month = (int) (lc / 1000000);
        lc = lc - month * 1000000;
        int dy = (int) (lc / 1000);
        return LocalDate.of(year, month, dy);
    }

    /**
     * 将时间转化成yyyyMMdd的整型
     *
     * @param dt localdatetime
     * @return yyyyMMdd的整数
     */
    public static int LocalDateToInt(LocalDate dt) {
        return dt.getYear() * 1000 + dt.getMonthValue() * 10 + dt.getDayOfMonth();
    }

    /**
     * 将时间转化成yyyyMMddhhmmss的long整数
     *
     * @param dt localdatetime
     * @return yyyyMMddhhmmss的long整数
     */
    public static long dateTimeToLong(LocalDateTime dt) {
        return dt.getYear() * 10000000000L + dt.getMonthValue() * 100000000 + dt.getDayOfMonth() * 1000000 + dt.getHour() * 10000 + dt.getMinute() * 100 + dt.getSecond();
    }


    /**
     * 将时间转化成yyyyMMddhhmmssfff的long整数
     *
     * @param dt localdatetime
     * @return yyyyMMddhhmmssfff的long整数
     */
    public static long dateTimetoLongEx3(LocalDateTime dt) {
        return dateTimeToLong(dt) * 1000 + dt.getNano() / 1000000;
    }


    /**
     * 序列化数据成字节流
     *
     * @param list
     * @return
     */
    public static byte[] serializeList(List<SerializeItem> list) {
        if (list != null && list.size() > 0) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                int len = list.size();
                for (int i = 0; i < len; i++) {
                    SerializeItem val = list.get(i);

                    if (val.dataType == DataType._STRING) {
                        ExpoSerializer.writeString(os, (String) val.object);
                    } else if (val.dataType == DataType._BYTE) {
                        ExpoSerializer.writeByte(os, (byte) val.object);
                    } else if (val.dataType == DataType._SHORT) {
                        ExpoSerializer.writeShort(os, (short) val.object);
                    } else if (val.dataType == DataType._INT) {
                        ExpoSerializer.writeInt(os, (int) val.object);
                    } else if (val.dataType == DataType._INT64) {
                        ExpoSerializer.writeLong(os, (long) val.object);
                    } else if (val.dataType == DataType._DOUBLE) {
                        ExpoSerializer.writeDouble(os, (double) val.object);
                    } else if (val.dataType == DataType._FLOAT) {
                        ExpoSerializer.writeFloat(os, (float) val.object);
                    } else if (val.dataType == DataType._BYTEARRAY) {
                        ExpoSerializer.writeByteArray(os, (byte[]) val.object);
                    } else if (val.dataType == DataType._STRINGARRAY) {
                        ExpoSerializer.writeStringShortArray(os, (String[]) val.object);
                    } else if (val.dataType == DataType._INTARRAY) {
                        ExpoSerializer.writeIntShortArray(os, (int[]) val.object);
                    } else if (val.dataType == DataType._STRINGARRAY2) {
                        ExpoSerializer.writeStringShortArray2(os, (String[][]) val.object);
                    } else if (val.dataType == DataType._BOOLEANARRAY) {
                        ExpoSerializer.writeBoolShortArray(os, (boolean[]) val.object);
                    } else if (val.dataType == DataType._FLOATARRAY) {
                        ExpoSerializer.writeFloatArray(os, (float[]) val.object);
                    } else if (val.dataType == DataType._DOUBLEARRAY) {
                        ExpoSerializer.writeDoubleArray(os, (double[]) val.object);
                    } else {
                        throw new Exception("不支持的序列化类型");
                    }
                }

                byte[] content = os.toByteArray();
                return content;
            } catch (Exception ex) {
                BaseLog.logError(Helper.class, "serializeList failed", ex);
            } finally {
                try {
                    os.close();
                } catch (IOException e) {
                    BaseLog.logError(Helper.class, "close stream failed", e);
                }
            }

        }

        return null;
    }

 /**
     * 从字节流反序列化出数据
     * @param data
     * @param list
     */
    public static void deSerializeList(byte[] data, List<SerializeItem> list)  {
        if(list != null && list.size() > 0)
        {
            ByteArrayInputStream is = new ByteArrayInputStream(data);
            try
            {
                int len = list.size();
                for(int i=0;i<len;i++)
                {
                    SerializeItem val = list.get(i);

                    if(val.dataType == DataType._STRING)
                    {
                        val.object = ExpoSerializer.readString(is);
                    }else if(val.dataType == DataType._BYTE)
                    {
                        val.object = ExpoSerializer.readByte(is);
                    }else if(val.dataType == DataType._SHORT)
                    {
                        val.object = ExpoSerializer.readShort(is);
                    }
                    else if(val.dataType == DataType._INT)
                    {
                        val.object = ExpoSerializer.readInt(is);
                    }
                    else if(val.dataType == DataType._INT64)
                    {
                        val.object = ExpoSerializer.readLong(is);
                    }
                    else if(val.dataType == DataType._DOUBLE)
                    {
                        val.object = ExpoSerializer.readDouble(is);
                    }else if(val.dataType == DataType._FLOAT)
                    {
                        val.object = ExpoSerializer.readFloat(is);
                    }
                    else if(val.dataType == DataType._BYTEARRAY)
                    {
                        val.object = ExpoSerializer.readByteArray(is);
                    }else if(val.dataType == DataType._STRINGARRAY)
                    {
                        val.object = ExpoSerializer.readStringShortArray(is);
                    }else if(val.dataType == DataType._INTARRAY)
                    {
                        val.object = ExpoSerializer.readIntShortArray(is);
                    }else if(val.dataType == DataType._STRINGARRAY2)
                    {
                        val.object = ExpoSerializer.readStringShortArray2(is);
                    }else if(val.dataType == DataType._BOOLEANARRAY)
                    {
                        val.object = ExpoSerializer.readBooleanShortArray(is);
                    }else if(val.dataType == DataType._FLOATARRAY)
                    {
                        val.object = ExpoSerializer.readFloatArray(is);
                    }else if(val.dataType == DataType._DOUBLEARRAY)
                    {
                        val.object = ExpoSerializer.readDoubleArray(is);
                    }
                    else
                    {
                        throw new Exception("不支持的序列化类型");
                    }
                }

            }catch (Exception ex)
            {
                BaseLog.logError(Helper.class,"deSerializeList failed",ex);
            }finally {
                try {
                    is.close();
                } catch (IOException e) {
                    BaseLog.logError(Helper.class,"close stream failed",e);
                }
            }

        }
    }

    /**
     *  转换time的形式
     * @param time
     * @param format
     * @return
     */
    public static String getTime2Time(long time, String format){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return longToDateTime(time).format(formatter);
    }

    /**
     * 从流中序列化出要的数据，返回该流，可以此持续调用此函数获取需要的值，调用者负责关闭流
     *
     * @param is
     * @param list
     */
    public static ByteArrayInputStream deSerializeList(ByteArrayInputStream is, List<SerializeItem> list) throws Exception {
        if (list != null && list.size() > 0) {
            int len = list.size();
            for (int i = 0; i < len; i++) {
                SerializeItem val = list.get(i);

                if (val.dataType == DataType._STRING) {
                    val.object = ExpoSerializer.readString(is);
                } else if (val.dataType == DataType._BYTE) {
                    val.object = ExpoSerializer.readByte(is);
                } else if (val.dataType == DataType._SHORT) {
                    val.object = ExpoSerializer.readShort(is);
                } else if (val.dataType == DataType._INT) {
                    val.object = ExpoSerializer.readInt(is);
                } else if (val.dataType == DataType._INT64) {
                    val.object = ExpoSerializer.readLong(is);
                } else if (val.dataType == DataType._DOUBLE) {
                    val.object = ExpoSerializer.readDouble(is);
                } else if (val.dataType == DataType._FLOAT) {
                    val.object = ExpoSerializer.readFloat(is);
                } else if (val.dataType == DataType._BYTEARRAY) {
                    val.object = ExpoSerializer.readByteArray(is);
                } else if (val.dataType == DataType._STRINGARRAY) {
                    val.object = ExpoSerializer.readStringShortArray(is);
                } else if (val.dataType == DataType._INTARRAY) {
                    val.object = ExpoSerializer.readIntShortArray(is);
                } else if (val.dataType == DataType._STRINGARRAY2) {
                    val.object = ExpoSerializer.readStringShortArray2(is);
                } else if (val.dataType == DataType._BOOLEANARRAY) {
                    val.object = ExpoSerializer.readBooleanShortArray(is);
                } else if (val.dataType == DataType._FLOATARRAY) {
                    val.object = ExpoSerializer.readFloatArray(is);
                } else if (val.dataType == DataType._DOUBLEARRAY) {
                    val.object = ExpoSerializer.readDoubleArray(is);
                } else {
                    throw new Exception("不支持的序列化类型");
                }
            }
        }
        return is;
    }



}
