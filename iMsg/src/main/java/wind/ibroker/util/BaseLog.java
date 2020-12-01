package wind.ibroker.util;


import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BaseLog {
    //日志
    public static void logInfo(Class clazz, String message) {
        Logger logger= LogManager.getLogger(clazz);
        logger.log(Level.INFO, message);
    }

    //info
    public static void logInfo(String message) {
        Logger logger= LogManager.getLogger();
        logger.log(Level.INFO, message);
    }
    /**
     * 记录错误日志
     * @param clazz 类型
     * @param message 消息
     * @param ex 异常信息
     * */
    public static void logError(Class clazz, String message, Exception ex) {
        Logger logger= LogManager.getLogger(clazz);
        logger.log(Level.ERROR,message);
        //logger.error("errorMsg:"+message+" Exception:"+ExceptionUtils.getStackTrace(ex));
    }

    /**
     * 记录错误日志
     * @param clazz 类型
     * @param message 消息
     * */
    public static void logError(Class clazz, String message) {
        Logger logger= LogManager.getLogger(clazz);
        logger.log(Level.ERROR,message);
        //logger.error(message);
    }

    /**
     * 记录错误日志
     * @param clazz 类型
     * @param ex 消息
     * */
    public static void logError(Class clazz, Exception ex) {
        Logger logger= LogManager.getLogger(clazz);
        logger.log(Level.ERROR,ExceptionUtils.getStackTrace(ex));
    }

    //根据订阅类型获取订阅名称
    public static String GetSubscribeName(short subscribeType){
        switch(subscribeType){
            case 1:
                return "报价流水";
            case 2:
                return "最优报价";
            case 3:
                return "重要报价";
            case 4:
                return "成交报价";
            case 5:
                return "撤销报价";
            case 6:
                return "机构等缓存数据";
            case 7:
                return "报价流水历史";
            case 8:
                return "成交报价历史";
            case 9:
                return "成交量历史";
            default:
                return "";
        }
    }
}
