package wind.ibroker.util;

import java.io.*;
import java.util.Properties;

/**
 * @Date 2020/11/19
 * @Author jinxu
 */
public class ConfigUtils {
	private static  final  String path = "src/main/resources/config.properties";
	private Properties properties = new Properties();

	private static ConfigUtils instance;
	public static ConfigUtils getInstance(){
		if(instance == null) instance = new ConfigUtils();
		return instance;
	}

	public String getProperty(String key){
		if(properties.getProperty(key)==null){
			return "";
		}else{
			return properties.getProperty(key);
		}
	}
	/**
	 * get
	 */
	public ConfigUtils(){
		try{
			FileInputStream inputStream = new FileInputStream(new File(path));
			properties.load(inputStream);
		}catch (Exception ex){
			BaseLog.logError(this.getClass(), "配置文件加载出错...", ex);
		}
	}
	/**
	 *  set
	 * @param key
	 * @param value
	 */
	public void setProperty(String key , String value){
		try {
			FileOutputStream oFile = new FileOutputStream(path, false);
			properties.setProperty(key, value);
			properties.store(oFile, key + "-" + value);
			oFile.close();
		} catch (IOException e) {
			BaseLog.logError(this.getClass(), "写入配置文件出错..." , e);
		}
	}
}
