/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wind.ibroker.common.windstream;

/**
 * WINDSTREAM支持的基础数据类型
 * @author nbwu
 */
public class WindStreamDataType {
    public static final byte BOOL = 0;
    
    public static final byte BYTE = 1;
    
    public static final byte INT16 = 2;
    
    public static final byte INT32 = 3;
    
    public static final byte INT64 = 4;
    
    public static final byte FLOAT = 5;
    
    public static final byte DOUBLE = 6;
    
    public static final byte STRING = 7;
    
    public static final byte HUGESTRING = 8;
    
    public static final byte ARRAY = 9;
    
    public static final byte HUGEARRAY = 10;
    
    public static final byte WINDSTREAM = 11;
    
    public static final byte NONE = 12;
    
    public static final byte BYTEARRY = 13;
    
    /**
     * 根据名称获取数据类型
     * @param name 类型名称
     * */
    public static byte getDataTypeByName(String name){
    	if(name == null)
    		return NONE;
    	
    	switch(name){
	        case "BOOL":
	        	return WindStreamDataType.BOOL;
	        
	        case "BYTE": 
	        	return WindStreamDataType.BYTE;
	        
	        case "INT16": 
	        	return WindStreamDataType.INT16;
	        
	        case "INT32": 
	        	return WindStreamDataType.INT32;
	        
	        case "INT64": 
	        	return WindStreamDataType.INT64;
	        
	        case "FLOAT": 
	        	return WindStreamDataType.FLOAT;
	        
	        case "DOUBLE": 
	        	return WindStreamDataType.DOUBLE;
	        
	        case "STRING": 
	        	return WindStreamDataType.STRING;
	        
	        case "HUGESTRING": 
	        	return WindStreamDataType.HUGESTRING;
	        
	        case "ARRAY": 
	        	return WindStreamDataType.ARRAY;
	        
	        case "HUGEARRAY": 
	        	return WindStreamDataType.HUGEARRAY;
	        
	        case "WINDSTREAM": 
	        	return WindStreamDataType.WINDSTREAM;
	        
	        case "NONE": 
	        	return WindStreamDataType.NONE;
	        
	        default:
	        	return WindStreamDataType.NONE;
    	}
    }
}
