package wind.ibroker.impl;

import wind.ibroker.comm.ExpoSerializer;
import wind.ibroker.protocol.Consts;
import wind.ibroker.protocol.Message;
import wind.ibroker.socket.ClientSocket;
import wind.ibroker.util.BaseLog;
import wind.ibroker.util.ConfigUtils;
import wind.ibroker.util.MD5Utils;
import wind.ibroker.util.MsgUtils;

import java.io.ByteArrayOutputStream;

/**
 * 发送器
 */
public class MsgHandler {

    private static MsgHandler instance;

    public static MsgHandler getInstance(){
        if(instance == null) instance = new MsgHandler();
        return instance;
    }

    public void doSend(int commandid, String msg){
        switch (commandid){
            case Consts.CMD_LOGIN:
                login(commandid);
                break;
            case Consts.CMD_QUERYWITHOPTIONBench:
                subscribe(commandid);
                break;
            case Consts.CMD_SENDMSG:
                sendData(commandid, msg);
                break;
            case  Consts.CMD_HEART_BEAT:
                sendData(commandid,msg);
        }
    }

    /**
     * 登录后台服务验证
     */
    private  void  login(int commandid){
        byte[] msgBytes = null;
        try{
            //开始发送登录请求
            BaseLog.logInfo("开始发送登录请求信息");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Message bm = new Message();
            String loginName = ConfigUtils.getInstance().getProperty("loginname");
            String pwd = ConfigUtils.getInstance().getProperty("password");
            String password = MD5Utils.toMD5(pwd);
            ExpoSerializer.writeBytes(bos, loginName.getBytes("UTF-8"));
            ExpoSerializer.writeBytes(bos, password.getBytes("UTF-8"));
            bm.getHeader().setCommandId(commandid);
            bm.setMessageBody(bos.toByteArray());
            bos.close();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            bm.serialize(os);
            msgBytes = os.toByteArray();
            ClientSocket.getInstance().send(msgBytes);
        }catch (Exception ex){
            BaseLog.logError(MsgUtils.class, "发送登录消息失败,ex "+  ex.getMessage());
        }
    }

    /**
     * 向服务发送订阅信息
     */
    private  void subscribe(int commandid){
        byte[] msgBytes = null;
        try{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Message bm = new Message();
            String[] strings = new String[]{
                    "SUBSCRIBETYPE","SUBSCRIBEID","OPTION","WORKBENCH",
                    "WINDCODE","DIRECTION","DURATION","QUOTESTATE"
            };
            String[][] stringArrays = {{
                    "SUBSCRIBETYPE","SUBSCRIBEID","OPTION","WORKBENCH",
                    "WINDCODE","DIRECTION","DURATION","QUOTESTATE"
            },{
                    "99","0","0","0","0","0","0","0"
            }};
            ExpoSerializer.writeStringShortArray2(bos, stringArrays);
            bm.getHeader().setCommandId(commandid);
            bm.setMessageBody(bos.toByteArray());
            bos.close();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            bm.serialize(os);
            msgBytes = os.toByteArray();
            ClientSocket.getInstance().send(msgBytes);
        }catch (Exception ex){
            BaseLog.logError(MsgUtils.class, "发送登录消息失败,ex "+  ex.getMessage());
        }
    }

    /**
     * 发送消息
     * @param msg
     */
    private  void sendData(int commandid, String msg){
        byte[] msgBytes = null;
        try{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Message bm = new Message();
            ExpoSerializer.writeString(bos, msg);
            bm.getHeader().setCommandId(commandid);
            bm.setMessageBody(bos.toByteArray());
            bos.close();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            bm.serialize(os);
            msgBytes = os.toByteArray();
            os.close();
            ClientSocket.getInstance().send(msgBytes);
        }catch (Exception ex){
            BaseLog.logError(MsgUtils.class, "转化为字节流失败,ex "+  ex.getMessage());
        }
    }

    public void doRecivData(byte[] arrs){

    }
}
