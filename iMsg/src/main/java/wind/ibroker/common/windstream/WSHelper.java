package wind.ibroker.common.windstream;


import org.apache.commons.lang3.StringUtils;
import wind.ibroker.comm.DataRow;
import wind.ibroker.common.StoreBase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * WindStream工具类
 * */
public class WSHelper {
    /**
     * 将StoreBase数组转为WindStream
     * */
    public static WindStream storeBaseToWindStream(StoreBase[] sbs, WindStreamDataField[] fields, String packName) {
        Object[] objs;
        WindStream _stream = new WindStream();
        _stream.getHeader().setPackName(packName);
        _stream.setFields(fields);

        if(sbs == null){
            return _stream;
        }

        for(StoreBase sb : sbs){
            if(sb == null)
                continue;

            objs = new Object[fields.length];
            for(int i=0; i<fields.length; i++){
                if(sb.get(fields[i].getFieldName()) != null){
                    objs[i] = sb.get(fields[i].getFieldName());
                }else{
                    objs[i] = getDefaultValue(fields[i].getDataType());
                }
            }

            _stream.getRows().add(new DataRow(objs));
        }

        return _stream;
    }

    private static Object getDefaultValue(byte dataType){
        switch(dataType){
            case WindStreamDataType.BOOL:
                return false;
            case WindStreamDataType.BYTE:
                return (byte)0;

            case WindStreamDataType.INT16:
                return (short)0;

            case WindStreamDataType.INT32:
                return 0;

            case WindStreamDataType.INT64:
                return 0L;

            case WindStreamDataType.FLOAT:
                return 0.0f;

            case WindStreamDataType.DOUBLE:
                return 0.0;

            case WindStreamDataType.STRING:
                return StringUtils.EMPTY;

            default:
                return null;
        }
    }

    /**
     * 将StoreBase数组转为WindStream
     * @param sbList StoreBase数组
     * @return WindStream
     */
    public static WindStream storeBaseToWindStream(StoreBase[] sbList) {
        Object[] objs;
        WindStream _stream = new WindStream();
        
        if(sbList == null || sbList.length == 0)
            return _stream;
        
        Iterator<String> iterator = sbList[0].getCollection().keySet().iterator();
        List<WindStreamDataField> lsField = new ArrayList<WindStreamDataField>();
        while(iterator.hasNext()){
            String f = iterator.next();
            lsField.add(new WindStreamDataField(f, getDataType(sbList[0].get(f))));
        }
        
        WindStreamDataField[] fields = new WindStreamDataField[lsField.size()];
        lsField.toArray(fields);
        _stream.setFields(fields);
        
        for(StoreBase sb : sbList){
            if(sb == null)
                continue;
            
            objs = new Object[lsField.size()];
            for(int i=0; i<lsField.size(); i++){
                if(sb.get(lsField.get(i).getFieldName()) != null){
                    objs[i] = sb.get(lsField.get(i).getFieldName());
                }else{
                    objs[i] = getDefaultValue(lsField.get(i).getDataType());
                }
            }
            
            _stream.getRows().add(new DataRow(objs));
        }
        
        return _stream;
    }

    /**
     * 获取数据类型
     */
    private static byte getDataType(Object obj) {
        if(obj == null)
            return WindStreamDataType.NONE;
        
        if(obj instanceof Integer)
            return WindStreamDataType.INT32;
        if(obj instanceof Long)
            return WindStreamDataType.INT64;
        if(obj instanceof Short)
            return WindStreamDataType.INT16;
        if(obj instanceof String)
            return WindStreamDataType.STRING;
        if(obj instanceof Float)
            return WindStreamDataType.FLOAT;
        if(obj instanceof Double)
            return WindStreamDataType.DOUBLE;
        if(obj instanceof Byte)
            return WindStreamDataType.BYTE;
        if(obj instanceof Boolean)
            return WindStreamDataType.BOOL;
        
        return WindStreamDataType.NONE;
    }

    /**
     * 从WindStream生成StoreBase数组
     * @param stream WindStream
     * @return StoreBase数组
     */
    public static StoreBase[] windStreamToStoreBase(WindStream stream) {
        List<StoreBase> sbList = new ArrayList<>();
        
        if(stream.getFields() != null && stream.getRows() != null && stream.getRows().size() > 0){
            StoreBase sb;
            for(DataRow item : stream.getRows()){
                Object[] rowData = (Object[])item.getData().toArray();
                sb = new StoreBase();
                
                WindStreamDataField[] fields = stream.getFields();
                for(int i=0; i<fields.length; i++){
                    if(fields[i].getDataType() == WindStreamDataType.STRING && rowData[i] == null){
                        sb.set(fields[i].getFieldName(), StringUtils.EMPTY);
                        continue;
                    }
                    
                    sb.set(fields[i].getFieldName(), rowData[i]);
                }
                
                sbList.add(sb);
            }
            
            StoreBase[] sbArray = new StoreBase[sbList.size()];
            sbList.toArray(sbArray);
            return sbArray;
        }
        
        return null;
    }
}
