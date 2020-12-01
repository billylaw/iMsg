package wind.ibroker.util;


import wind.ibroker.comm.ExpoSerializer;
import wind.ibroker.protocol.Message;

import java.io.ByteArrayOutputStream;

public class MsgUtils {

    /**
     * 拼裝發送消息格式
     * @param msg
     * @param time
     * @param groupID
     * @param groupName
     * @param qqID
     * @param nickName
     * @return
     */
    public static String getMsgStr(String msg,
                                   String time,
                                   String groupID,
                                   String groupName,
                                   String qqID,
                                   String nickName){
        //拼裝消息
        StringBuilder sb = new StringBuilder();
        sb.append("*****($$$)*****");
        sb.append("\n");
        sb.append(String.format("[%s][%s][%s][%s][%s]", time,groupID,groupName,qqID,nickName));
        sb.append("\n");
        sb.append(msg);
        sb.append("\n");
        sb.append("-----(@@@)-----");
        return sb.toString();
    }

    public static  byte[] getMessageBytes(String msg, int commandid){
        byte[] msgBytes = null;
        try{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Message bm = new Message();
            ExpoSerializer.writeBytes(bos, msg.getBytes("UTF-8"));
            bm.getHeader().setCommandId(commandid);
            bm.setMessageBody(bos.toByteArray());
            bos.close();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            bm.serialize(os);
            msgBytes = os.toByteArray();
            return msgBytes;
        }catch (Exception ex){
            BaseLog.logError(MsgUtils.class, "轉化字節流失敗,ex "+  ex.getMessage());
        }
        return msgBytes;
    }
}
