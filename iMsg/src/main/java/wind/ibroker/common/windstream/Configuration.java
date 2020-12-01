/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wind.ibroker.common.windstream;

import java.util.HashMap;
import java.util.Map;

/**
 * WindStream配置
 */
public class Configuration {
    //字典
    private Map<String, WindStreamConfiguration> _dict = new HashMap<String, WindStreamConfiguration>();
    
    /**
     * 设定WindStream配置
     * @param packName WindStream包名
     * @param wsc WindStream配置
     * */
    void set(String packName, WindStreamConfiguration wsc) {
        _dict.put(packName, wsc);
    }
    
    /**
     * 获取WindStream配置
     * @param packName WindStream包名
     * */
    public WindStreamConfiguration get(String packName){
    	if(_dict.containsKey(packName)){
    		return _dict.get(packName);
    	}
    	
    	return null;
    }
    
    /**
     * 获取配置数量
     * */
    public int size(){
    	return _dict.size();
    }
    
}
