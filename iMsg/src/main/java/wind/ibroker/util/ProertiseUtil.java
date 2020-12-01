package wind.ibroker.util;

import java.io.*;
import java.util.Properties;

/**
 * @Date 2020/11/19
 * @Author jinxu
 */
public class ProertiseUtil {
	private static Properties properties = new Properties();

	public static String getUsername() {
//		FileInputStream inputStream = (FileInputStream) Object.class.getResourceAsStream("/user.properties");
		try {
			FileInputStream inputStream = new FileInputStream("/user.properties");
			properties.load(inputStream);
			if (!(properties.getProperty("username").equals(""))) {
				String username=properties.getProperty("username");
				return username;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	public static String getPassword() {
//		FileInputStream inputStream = (FileInputStream) Object.class.getResourceAsStream("/user.properties");
		try {
			FileInputStream inputStream = new FileInputStream("/user.properties");
			properties.load(inputStream);
			String pass=properties.getProperty("password");
			return pass;
			} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return " ";
	}
	public static String getEmai() {
//		FileInputStream inputStream = (FileInputStream) Object.class.getResourceAsStream("/user.properties");
		try {
			FileInputStream inputStream = new FileInputStream("/user.properties");
			properties.load(inputStream);
			String pass=properties.getProperty("email");
			return pass;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return " ";
	}
	public static void setEmai(String emai) {
		// TODO add your code here
		try {
			FileInputStream inputStream = new FileInputStream(new File("/user.properties"));
			properties.load(inputStream);
			FileOutputStream oFile = new FileOutputStream("/user.properties", true);
			properties.setProperty("email", emai);
			properties.store(oFile, "test");
			oFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	public static void setProperties(String username, String password) {
		// TODO add your code here
		try {
			FileInputStream inputStream = new FileInputStream(new File("/user.properties"));
			properties.load(inputStream);
			FileOutputStream oFile = new FileOutputStream("/user.properties", true);
			properties.setProperty("username", username);
			properties.setProperty("password", password);
			properties.store(oFile, "test");
			oFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
