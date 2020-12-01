package wind.ibroker.socket;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactoryJvm;
import net.mamoe.mirai.utils.BotConfiguration;
import wind.ibroker.util.BaseLog;
import wind.ibroker.view.BrokerClient;

import javax.swing.*;

public class MsgConnect {

    private static Bot bot;
    public static String qqNum;
    public static String qqPwd;
    public static boolean isLogin = false;

    public static void login(){
        try{
            waitToLogin();
            bot = BotFactoryJvm.newBot(Long.parseLong(qqNum), qqPwd, new BotConfiguration() {
                {
                    //保存设备信息到文件
                    fileBasedDeviceInfo("deviceInfo.json");
                }
            });
            BaseLog.logInfo("登录QQ配置完成，请稍等...");
            bot.login();
            BaseLog.logInfo("登录QQ成功...");
            //启动主界面
            BrokerClient.getInstance().start(bot);
        }catch (Exception ex){
            BaseLog.logError(MsgConnect.class, "客户端启动失败...");
            JOptionPane.showMessageDialog(
                    null,
                    "客户端启动失败，请检查日志或联系管理员!",
                    "消息",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    private static void waitToLogin() throws Exception{
        while(!isLogin){
            Thread.sleep(100);
        }
    }
}
