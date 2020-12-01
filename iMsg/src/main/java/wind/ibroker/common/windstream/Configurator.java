/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wind.ibroker.common.windstream;


import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import wind.ibroker.common.DataField;
import wind.ibroker.common.DataType;
import wind.ibroker.common.SectionData;
import wind.ibroker.common.SectionItem;

/**
 * windstream配置文件读取
 * @author nbwu
 */
public class Configurator {
    //serialNum字段
    private static final WindStreamDataField SERIAL_NUM_FIELD = new WindStreamDataField("SerialNum", WindStreamDataType.INT64);
    
    //配置项
    private static Configuration _config;
    
    /**
     * 从SectionData生成WindStream配置
     * @param sectionData SectionData配置
     * @param enableSerialNumField 是否启用SerialNum字段
     * */
    public static void init(SectionData sectionData, boolean enableSerialNumField)
    {
        Configuration configuration = new Configuration();
        for(SectionItem si : sectionData.items){
            WindStreamConfiguration wsc = new WindStreamConfiguration();
            wsc.setKeyFieldName(si.getKeyFieldName());
            wsc.setPackName(StringUtils.isEmpty(si.getPackName()) ? String.format("PackName_ItemType_%d", si.getItemType()) : si.getPackName());
            for(DataField df : si.getList()){
                WindStreamDataField wsdf = new WindStreamDataField();
                wsdf.setFieldName(df.getFieldName());
                wsdf.setDataType(getDataType(df.getDataType()));
                
                wsc.add(wsdf);
            }
            
            if(enableSerialNumField){
                wsc.add(SERIAL_NUM_FIELD);
            }
            
            configuration.set(wsc.getPackName(), wsc);
        }
        
        _config = configuration;
    }
    
    /**
     * 从xml配置文件中读取WindStream配置
     * @param elem xml配置节 
     * */
    public static void load(Element elem){
    	Configuration configuration = new Configuration();
    	
    	NodeList nodeList = elem.getChildNodes();
    	for(int i=0; i<nodeList.getLength(); i++){
    		Node node = nodeList.item(i);
    		if(node instanceof Element){
    			Element child = (Element)node;
    			if("WindStream".equals(child.getTagName())){
    				WindStreamConfiguration wsConfig = new WindStreamConfiguration();
    				wsConfig.setPackName(child.getAttribute("PackName"));
    				wsConfig.setKeyFieldName(child.getAttribute("KeyField"));
    				loadFieldList(child, wsConfig);  
    				
    				configuration.set(wsConfig.getPackName(), wsConfig);
    			}
    		}
    	}
    	
    	_config = configuration;
    }
    
    /**
     * 加载字段配置
     * @param elem WindStream配置节
     * @param config WindStream配置
     * */
    public static void loadFieldList(Element elem, WindStreamConfiguration config){
    	NodeList nodeList = elem.getChildNodes();
    	for(int i=0; i<nodeList.getLength(); i++){
    		Node node = nodeList.item(i);
    		if(node instanceof Element){
    			Element child = (Element)node;
    			if("Field".equals(child.getTagName())){
    				WindStreamDataField field = new WindStreamDataField();
    				field.setFieldName(child.getAttribute("FieldName"));    				
    				field.setDataType(WindStreamDataType.getDataTypeByName(child.getAttribute("DataType")));
    				
    				config.add(field);
    			}
    		}
    	}
    }
    
    /**
     * 获取WindStream配置
     * @param packName WindStream包名
     * */
    public static WindStreamConfiguration getItem(String packName){
    	return _config.get(packName);
    }
    
    /**
     * 获取配置数量
     * */
    public static int size(){
    	return _config.size();
    }
    
    //获取字段类型
    private static byte getDataType(byte dataType){
        switch(dataType){
            case DataType._BOOL:
                return WindStreamDataType.BOOL;
                
            case DataType._BYTE:
                return WindStreamDataType.BYTE;
                
            case DataType._SHORT:
                return WindStreamDataType.INT16;
                
            case DataType._INT:
                return WindStreamDataType.INT32;
                
            case DataType._INT64:
                return WindStreamDataType.INT64;
                
            case DataType._FLOAT:
                return WindStreamDataType.FLOAT;
                
            case DataType._DOUBLE:
                return WindStreamDataType.DOUBLE;
                
            case DataType._STRING:
                return WindStreamDataType.STRING;
                
            case DataType._BYTEARRAY:
                return WindStreamDataType.ARRAY;
                
            default:
                return WindStreamDataType.NONE;
        }
    }
}
