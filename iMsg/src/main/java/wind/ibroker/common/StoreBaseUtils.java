package wind.ibroker.common;


import org.apache.commons.lang3.StringUtils;
import wind.ibroker.util.BaseLog;
import wind.ibroker.comm.DataRow;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * storebase 工具类
 */
public class StoreBaseUtils {

    /**
     *  生成字符串表示
     * @param storeBase
     * @return
     */
    public static String toString(StoreBase storeBase){
        if (storeBase == null || storeBase.getCollection() == null)
            return null;
        StringBuilder builder = new StringBuilder();
        for(Map.Entry<String, Object> item : storeBase.getCollection().entrySet()) {
            if(item.getValue() == null){
                builder.append(String.format("%s=null", item.getKey()));
            }else {
                builder.append(String.format("%s=%s", item.getKey(), item.getValue()));
            }
        }
        return builder.toString().trim();
    }

    /**
     * Storebase 转换为 实体对象
     * 转化成该对象实体
     */
    public static boolean storeBasetoEntity(StoreBase sb, Object entity)  {
        try{

            if(sb == null || entity == null)
            {
                BaseLog.logError(StoreBase.class,"storebase or entity is null");
                return false;
            }

            //获取当前类的字段
            Field[] fields = entity.getClass().getDeclaredFields();
            for (Field field : fields){
                //设置是否允许访问
             //   BaseLog.logInfo(field.getName());
                field.setAccessible(true);
                Object value = getReflectValue(field,sb.get(field.getName().toString()));
                field.set(entity, value);
            }
        }catch (Exception ex){
            BaseLog.logError(StoreBase.class,"StoreBase转实体失败！ ", ex);
            return false;
        }

        return true;
    }

    /**
     *  直接通过storebase转化成DataRow
     * @param sb
     * @return
     */
    public DataRow toDataRow(StoreBase sb){
        List<DataField> fields = sb.getDataFields();
        if(fields == null || fields.size() == 0)
            return  null;
        Object[] objs = new Object[fields.size()];
        for (int i=0;i<fields.size(); ++i){
            objs[i] = sb.get(fields.get(i).getFieldName());
        }
        return  new DataRow(objs);
    }

    /**
     * Entity to Object
     * @param sb storebase
     * @param entity entity
     */
    public  static void entityToStoreBase(Object entity,StoreBase sb) {
        if(entity == null || sb == null)
        {
            BaseLog.logError(StoreBaseUtils.class,"entity or storebase is null");
            return;
        }
        Field[] fields = entity.getClass().getDeclaredFields();
        if(fields == null || fields.length == 0)
        {
            BaseLog.logError(StoreBaseUtils.class,"entity fields count is 0");
            return;
        }
        try
        {
            for (Field field : fields){
                //设置是否允许访问
                field.setAccessible(true);
                Object value  = field.get(entity);
                sb.set(field.getName(),value);
            }
        }catch(Exception ex)
        {

        }
    }

    /**
     * Entity to Object
     * @param srcEntity storebase
     * @param dstEntity entity
     */
    public  static void entityToEntity(Object srcEntity,Object dstEntity) {
        if(srcEntity == null || dstEntity == null)
        {
            BaseLog.logError(StoreBaseUtils.class,"srcEntity or dstEntity is null");
            return;
        }
        if(srcEntity.getClass() != dstEntity.getClass())
        {
            BaseLog.logInfo(StoreBaseUtils.class,"diffrent class cant do entityToEntity");
            return;
        }
        Field[] fields = srcEntity.getClass().getDeclaredFields();
        if(fields == null || fields.length == 0)
        {
            BaseLog.logError(StoreBaseUtils.class,"entity fields count is 0");
            return;
        }
        try
        {
            for (Field field : fields){
                //设置是否允许访问
                field.setAccessible(true);
                Object value  = field.get(srcEntity);
               field.set(dstEntity,value);
            }
        }catch(Exception ex)
        {

        }
    }


    /**
     * 或者反射的返回值
     * @param value
     * @return
     */
    public  static Object getReflectValue(Field filed,Object value)
    {
        boolean isNull = value == null?true:false;

        Type type = filed.getGenericType();
        String typeName = type.getTypeName();

        if(typeName.equals("double") || typeName.equals("java.lang.Double"))
        {
            if(isNull)
            {
                return 0d;
            }

            try {
                return    Double.parseDouble(value.toString());
            }catch(Exception ex)
            {
                return 0d;
            }
        }
        else if(typeName.equals("float") || typeName.equals("java.lang.Float"))
        {
            if(isNull)
            {
                return 0f;
            }

            try {
                return    Float.parseFloat(value.toString());
            }catch(Exception ex)
            {
                return 0f;
            }
        }
        else if(typeName.equals("long") || typeName.equals("java.lang.Long"))
        {
            if(isNull)
            {
                return (long)0;
            }

            try
            {
                return Long.parseLong(value.toString());
            }catch(Exception ex)
            {
                return (long)0;
            }
        }
        else if(typeName.equals("int") ||  typeName.equals("java.lang.Integer"))
        {

            if(isNull)
            {
                return (int)0;
            }

            try
            {
                return Integer.parseInt(value.toString());
            }catch(Exception ex)
            {
                return (int)0;
            }
        }else if(typeName.equals("short") || typeName.equals("java.lang.Short"))
        {
            if(isNull)
            {
                return (short)0;
            }

            try
            {
                return Short.parseShort(value.toString());
            }catch(Exception ex)
            {
                return (short)0;
            }

        }else if(typeName.equals("byte") || typeName.equals("java.lang.Byte"))
        {
            if(isNull)
            {
                return (byte)0;
            }

            try
            {
                return Byte.parseByte(value.toString());
            }catch(Exception ex)
            {
                return (byte)0;
            }
        }
        else if(typeName.equals("boolean") || typeName.equals("java.lang.Short"))
        {
            if(isNull)
            {
                return false;
            }

            try
            {
                return Boolean.parseBoolean(value.toString());
            }catch(Exception ex)
            {
                return  false;
            }
        }
        else if(typeName.equals("java.lang.String"))
        {
            if(isNull)
            {
                return StringUtils.EMPTY;
            }
            else
            {
                return   value.toString();
            }
        }else
        {
            if(isNull)
            {
                return null;
            }

            BaseLog.logError(StoreBase.class,"not support class");
            return value;
        }
    }
}
