package wind.ibroker.common;


import org.apache.commons.lang3.StringUtils;
import wind.ibroker.comm.DataRow;
import wind.ibroker.comm.Helper;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 存储结构
 * */
public class StoreBase implements Cloneable, Serializable {
    //Key: PropertyName, Value: Object
    private Map<String, Object> _coll = null;

    public StoreBase(){
        this._coll = new HashMap<String, Object>();
    }

    public StoreBase(Map<String, Object> coll){
        this._coll = coll;
    }

    public void set(String fieldName, Object value){
        if(StringUtils.isEmpty(fieldName)){
            return;
        }

        String fd = fieldName.trim().toUpperCase();
        if(fd.length() == 0){
            return;
        }

        _coll.put(fd, value);
    }

    public Object get(String fieldName){
        return getFieldValue(fieldName);
    }

    /**
     * 初始化
     * */
    public void init(Map<String, Object> coll){
        this._coll = coll;
    }


    /**
     * 合并一个StoreBase对象
     * */
    public void addStoreBase(StoreBase sb){
        if(sb == null || sb._coll == null || sb._coll.size() == 0)
            return;
        for(String key : sb._coll.keySet()){
        if(StringUtils.isEmpty(key))
        {
            continue;
        }
        this.set(key, sb._coll.get(key));
    }
}

    /**
     * 合并一个sb对象
     * */
    public void addStoreBase(StoreBase sb, List<DataField> lsDataField){
        if(sb == null || sb._coll == null || sb._coll.size() == 0 || lsDataField == null || lsDataField.size() < 1)
            return;

        for(String key : sb._coll.keySet()){
            DataField dataField = null;
            for(DataField field : lsDataField){
                if(field.getFieldName().toUpperCase().equals(key.toUpperCase())){
                    dataField = field;
                    break;
                }
            }

            if(dataField != null){
                this.set(key, sb._coll.get(key));
            }
        }
    }

    public Object getFieldValue(String fieldName){
        if(StringUtils.isEmpty(fieldName)){
            return null;
        }

        String fd = fieldName.trim().toUpperCase();
        if(fd.length() == 0){
            return null;
        }

        Object obj = null;
        if(_coll.containsKey(fd)){
            obj = _coll.get(fd);
        }

        return obj;
    }

    public void addCollData(String key, Object value){
        this.set(key, value);
    }

    /**
     * 删除记录
     * */
    public void remove(String fieldName){
        if(StringUtils.isEmpty(fieldName))
            return;

        String fd = fieldName.trim().toUpperCase();
        if(fd.length() == 0)
            return;

        if(_coll.containsKey(fd)){
            _coll.remove(fd);
        }
    }

    /**
     * 获取数据集合
     */
    public Map<String, Object> getCollection(){
        return _coll;
    }

    @Override
    public Object clone(){
        StoreBase sb = new StoreBase();
        for(String key : this._coll.keySet()){
            sb.addCollData(key, this._coll.get(key));
        }
        return sb;
    }


    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        if(this._coll != null && this._coll.size() > 0)
        {
            for(String key : this._coll.keySet()) {
                sb.append(key);
                sb.append(":");
                sb.append(this.get(key));
                sb.append("\r\n");
            }
            return sb.toString();
        }
        return StringUtils.EMPTY;
    }

    /**
     * 获取双精度浮点型的字段值
     * @param name 字段名称
     * @param defaultVal 默认值
     * @return 如果字段值不存在或者为null则返回提供的默认值，如果是String类型或Double类型则返回表示的Double值，否则返回默认值
     * */
	public double getDouble(String name, double defaultVal) {
		Object value = getFieldValue(name);
		if(value == null){
			return defaultVal;
		}
        if(value instanceof Byte){
            return (byte)value;
        }else if(value instanceof Short){
            return (short)value;
        }else if(value instanceof Integer){
            return (int)value;
        }else if(value instanceof Long){
            return (long)value;
        }else if(value instanceof Float){
            return (float)value;
        }else if(value instanceof Double){
			return (double)value;
		}else if(value instanceof String){
            if(StringUtils.EMPTY.equals(value))
                return defaultVal;

			return Double.parseDouble((String) value);
		}
		
		return defaultVal;
	}

	/**
	 * 获取字符串类型的字段值
	 * @param name 字段名称
	 * @result 如果不存在或者字段值为null，则返回空字符串
         * @return 字段的字符串值
	 * */
	public String getString(String name) {
		Object value = getFieldValue(name);
		if(value == null)
			return StringUtils.EMPTY;
		
		if(value instanceof String){
			return (String)value;
		}else{
			return value.toString();
		}
	}

	/**
	 * 获取byte类型字段值
         * @param name 字段名
         * @param defaultVal 默认值
         * @return 如果字段存在且格式正确则返回字段的byte值，否则返回defaultVal
	 * */
	public byte getByte(String name, byte defaultVal) {
		Object value = getFieldValue(name);
		if(value == null)
            return defaultVal;
		
		if(value instanceof Byte){
            return (Byte)value;
		}else if(value instanceof Short){
            Short val = (Short)value;
            if(val >= Byte.MIN_VALUE && val <= Byte.MAX_VALUE){
                return val.byteValue();
            }else{
                return defaultVal;
            }
        }else if(value instanceof Integer){
            Integer val = (Integer)value;
            if(val >= Byte.MIN_VALUE && val <= Byte.MAX_VALUE){
                return val.byteValue();
            }else{
                return defaultVal;
            }
        }else if(value instanceof Long){
            Long val = (Long)value;
            if(val >= Byte.MIN_VALUE && val <= Byte.MAX_VALUE){
                return val.byteValue();
            }else{
                return defaultVal;
            }
        }else if(value instanceof Float){
            Float val = (Float)value;
            if(val >= Byte.MIN_VALUE && val <= Byte.MAX_VALUE){
                return val.byteValue();
            }else{
                return defaultVal;
            }
        }else if(value instanceof Double){
            Double val = (Double)value;
            if(val >= Byte.MIN_VALUE && val <= Byte.MAX_VALUE){
                return val.byteValue();
            }else{
                return defaultVal;
            }
        }else if(value instanceof String){
            if(StringUtils.EMPTY.equals(value))
                return defaultVal;
            return Byte.parseByte((String)value);
        }
		
		return defaultVal;
	}

	/**
	 * 获取int类型字段值
         * @param name 字段名
         * @param defaultVal 值不存在或格式不正确时返回的默认值
         * @return 如果值存在且格式正确返回该字段的整型值，否则返回默认值
	 * */
	public int getInt(String name, int defaultVal) {
            Object value = getFieldValue(name);
            if(value == null)
                    return defaultVal;

            if(value instanceof Byte){
                return (Byte)value;
            }else if(value instanceof Short){
                return (Short)value;
            }else if(value instanceof Integer){
                return (Integer)value;
            }else if(value instanceof Long){
                Long val = (Long)value;
                if(val >= Integer.MIN_VALUE && val <= Integer.MAX_VALUE){
                    return val.intValue();
                }else{
                    return defaultVal;
                }
            }else if(value instanceof Double){
                Double val = (Double)value;
                if(val >= Integer.MIN_VALUE && val <= Integer.MAX_VALUE){
                    return val.intValue();
                }else{
                    return defaultVal;
                }
            }else if(value instanceof Float){
                Float val = (Float)value;
                if(val >= Integer.MIN_VALUE && val <= Integer.MAX_VALUE){
                    return val.intValue();
                }else{
                    return defaultVal;
                }
            }else if(value instanceof String){
                if(StringUtils.EMPTY.equals(value))
                    return defaultVal;

                return Integer.parseInt((String)value);
            }

            return defaultVal;
	}

	/**
     * 获取short类型字段值
     * @param name 字段名
     * @param defaultVal 默认值
     * @return 如果值存在且格式正确返回该字段的short值，否则返回默认值
     * */
    public short getShort(String name, short defaultVal){
        Object value = getFieldValue(name);
        if(value == null)
            return defaultVal;

        if(value instanceof Byte){
            return (byte)value;
        }else if(value instanceof Short){
            return (short)value;
        }else if(value instanceof Integer){
            Integer val = (Integer)value;
            if(val >= Short.MIN_VALUE && val <= Short.MAX_VALUE){
                return val.shortValue();
            }else{
                return defaultVal;
            }
        }else if(value instanceof Long){
            Long val = (Long)value;
            if(val >= Short.MIN_VALUE && val <= Short.MAX_VALUE){
                return val.shortValue();
            }else{
                return defaultVal;
            }
        }else if(value instanceof Double){
            Double val = (Double)value;
            if(val >= Short.MIN_VALUE && val <= Short.MAX_VALUE){
                return val.shortValue();
            }else{
                return defaultVal;
            }
        }else if(value instanceof Float){
            Float val = (Float)value;
            if(val >= Short.MIN_VALUE && val <= Short.MAX_VALUE){
                return val.shortValue();
            }else{
                return defaultVal;
            }
        }else if(value instanceof String){
            if(StringUtils.EMPTY.equals(value))
                return defaultVal;

            return Short.parseShort((String)value);
        }

        return defaultVal;
    }

    /**
     * 获取long类型字段值
     * @param name 字段名
     * @param defaultVal 默认值
     * @return 如果值存在且格式正确，返回该字段的long值，否则返回默认值
     * */
    public long getLong(String name, long defaultVal){
        Object value = getFieldValue(name);
        if(value == null)
            return defaultVal;

        if(value instanceof Byte){
            return (Byte)value;
        }else if(value instanceof Short){
            return (Short)value;
        }else if(value instanceof Integer){
            return (Integer)value;
        }else if(value instanceof Long){
            return (Long)value;
        }else if(value instanceof Double){
            Double val = (Double)value;
            return val.longValue();
        }else if(value instanceof Float){
            Float val = (Float)value;
            return val.longValue();
        }else if(value instanceof String){
            if(StringUtils.EMPTY.equals(value))
                return defaultVal;

            return Long.parseLong((String)value);
        }

        return defaultVal;
    }

    /**
     * 获得输出项
     * @return
     */
    public List<DataField> getDataFields(){
        List<DataField> dataFields = new ArrayList<DataField>();
        DataField dataField;
        Field[] fields =   this.getClass().getDeclaredFields();
        for(Field field : fields){
            dataField = new DataField();
            dataField.setFieldName(field.getName());
            dataField.setFieldCName(field.getName());
            dataField.setDataType(Helper.getDataType(field.getGenericType().toString()));
            dataFields.add(dataField);
        }
        return  dataFields;
    }

    /**
     * 返回DataRow对象
     * @return
     */
    public DataRow toDataRow(){
        List<DataField> fields = getDataFields();
        if(fields == null || fields.size() == 0)
            return  null;
        Object[] objs = new Object[fields.size()];
        for (int i=0;i<fields.size(); ++i){
            objs[i] = this.get(fields.get(i).getFieldName());
        }
        return  new DataRow(objs);
    }

}
