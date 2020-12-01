package wind.ibroker.protocol;

import wind.ibroker.util.BaseLog;
import wind.ibroker.comm.ExpoSerializer;

import java.io.*;

public class Message {
    private byte startTag;

    public byte getStartTag() {
        return startTag;
    }

    public void setStartTag(byte startTag) {
        this.startTag = startTag;
    }

    public MessageHeader getHeader() {
        return header;
    }

    public void setHeader(MessageHeader header) {
        this.header = header;
    }

    public byte[] getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(byte[] messageBody) {
        this.messageBody = messageBody;
    }

    private MessageHeader header;

    private byte[] messageBody;

    /// <summary>
    /// 克隆
    /// </summary>
    /// <returns></returns>
    public Message clone(){
        Message rt = new Message();
        rt.header = this.header;
        rt.messageBody = this.messageBody;
        return rt;
    }

    /// <summary>
    /// 空消息，或无效消息
    /// </summary>
    /// <returns></returns>
    public Message() {
        this.startTag = MsgConfig.MSG_START_TAG;
        this.messageBody = null;
        this.header = new MessageHeader();
    }

    /// <summary>
    /// 计算该Message的长度
    /// </summary>
    /// <returns></returns>
    public int getLength() {
        if (this.messageBody == null || this.messageBody.length == 0)
            return 24;
        else
            return (int)(24 + this.messageBody.length);
    }

    /// <summary>
    /// 序列化
    /// </summary>
    /// <param name="bw"></param>
    public void serialize(OutputStream os) throws Exception {
        this.header.setMessageLen(getLength());
        this.header.setHeaderCRC(calcCRC());
        ExpoSerializer.writeByte(os, this.startTag);
        this.header.serialize(os);
        ExpoSerializer.writeByteArray(os,this.messageBody);
    }

    /// <summary>
    /// 反序列化
    /// </summary>
    /// <returns>Byte Stream</returns>
    public byte[] deSerialize() throws Exception {
        ByteArrayOutputStream byteOS = new ByteArrayOutputStream();
        this.header.setMessageLen(getLength());
        this.header.setHeaderCRC(calcCRC());
        ExpoSerializer.writeByte(byteOS, this.startTag);
        this.header.serialize(byteOS);
        ExpoSerializer.writeByteArray(byteOS, this.messageBody);
        return byteOS.toByteArray();
    }

    /// <summary>
    /// 反序列化
    /// </summary>
    /// <param name="br"></param>
    public void deSerialize(InputStream is) throws  IOException{
        this.startTag = ExpoSerializer.readByte(is);
        this.header.deSerialize(is);
        this.messageBody = ExpoSerializer.readByteArray(is);
    }

    /**
     * 反序列化
     * @param bytes
     * @return
     */
    public  Message deSerialize(byte[] bytes){
        try{
            Message message = new Message();
            if (bytes == null || bytes.length < MsgConfig.MSG_MIN_LEN)
                return null;
            ByteArrayInputStream is  = new ByteArrayInputStream(bytes);
            message.setStartTag(ExpoSerializer.readByte(is));
            message.getHeader().deSerialize(is);
            if(bytes.length < message.getHeader().getMessageLen())
                return  null;
            message.setMessageBody(ExpoSerializer.readByteArray(is));
            return message;
        }catch (Exception ex){
            BaseLog.logError(this.getClass(), "反序列化错误:" + ex.toString());
        }
        return null;
    }


    /// <summary>
    /// 计算CRC 暂时不用
    /// </summary>
    /// <returns></returns>
    public int calcCRC() throws Exception{
        int crc = 0;
        crc += this.startTag;
        crc = this.header.calcCRC(crc);
        if(this.messageBody == null || this.messageBody.length == 0)
            return crc;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ExpoSerializer.writeInt(os, this.messageBody.length);
        crc = calcAddCRC(os.toByteArray(), crc);
        os.close();

        crc = calcAddCRC(this.messageBody, crc);
        return crc;
    }

    private int calcAddCRC(byte[] arr, int crc){
        if(arr ==null || arr.length == 0)
            return crc;
        for(int i=0;i<arr.length; ++i){
            crc += Byte.toUnsignedInt(arr[i]);
        }
        return crc;
    }
}
