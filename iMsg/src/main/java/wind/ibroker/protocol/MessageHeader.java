package wind.ibroker.protocol;




import wind.ibroker.util.BaseLog;
import wind.ibroker.comm.ExpoSerializer;

import java.io.*;

public class MessageHeader {
    private short version;

    private byte sign;

    private  int commandId;

    private int messageLen;

    private int headerCRC;

    public int getSerialNO() {
        return serialNO;
    }

    public void setSerialNO(int serialNO) {
        this.serialNO = serialNO;
    }

    private int serialNO;

    public short getVersion() {
        return version;
    }

    public void setVersion(short version) {
        this.version = version;
    }

    public byte getSign() {
        return sign;
    }

    public void setSign(byte sign) {
        this.sign = sign;
    }

    public int getCommandId() {
        return commandId;
    }

    public void setCommandId(int commandId) {
        this.commandId = commandId;
    }

    public int getMessageLen() {
        return messageLen;
    }

    public void setMessageLen(int messageLen) {
        this.messageLen = messageLen;
    }

    public int getHeaderCRC() {
        return headerCRC;
    }

    public void setHeaderCRC(int headerCRC) {
        this.headerCRC = headerCRC;
    }

    public MessageHeader(){
        setVersion((short) 1);
    }

    //序列化
    public void serialize(OutputStream os) throws IOException {
        ExpoSerializer.writeShort(os, this.version);
        ExpoSerializer.writeInt(os, this.serialNO);
        ExpoSerializer.writeByte(os, this.sign);
        ExpoSerializer.writeInt(os, this.commandId);
        ExpoSerializer.writeInt(os, this.messageLen);
        ExpoSerializer.writeInt(os, this.headerCRC);
    }


    //反序列化
    public void deSerialize(InputStream is) throws IOException {
        this.version = ExpoSerializer.readShort(is);
        this.serialNO = ExpoSerializer.readInt(is);
        this.sign = ExpoSerializer.readByte(is);
        this.commandId = ExpoSerializer.readInt(is);
        this.messageLen = ExpoSerializer.readInt(is);
        this.headerCRC = ExpoSerializer.readInt(is);
    }

    //不用考虑
    public int calcCRC(int crc){
        int crcTemp = crc;
        ByteArrayOutputStream os = null;
        try{
             os = new ByteArrayOutputStream();
            ExpoSerializer.writeShort(os, this.version);
            crcTemp = calcAddCRC(os.toByteArray(), crcTemp);
            os.reset();

            ExpoSerializer.writeInt(os, this.serialNO);
            crcTemp = calcAddCRC(os.toByteArray(), crcTemp);
            os.reset();

            byte[] bytes3 = new byte[]{ this.sign };
            crcTemp = calcAddCRC(bytes3, crcTemp);

            ExpoSerializer.writeInt(os, this.commandId);
            crcTemp = calcAddCRC(os.toByteArray(), crcTemp);
            os.reset();

            ExpoSerializer.writeInt(os, this.messageLen);
            crcTemp = calcAddCRC(os.toByteArray(), crcTemp);
            return crcTemp;
        }catch (Exception ex){
            return crcTemp;
        }finally {
            if(os != null)
            {
                try {
                    os.close();
                } catch (IOException e) {
                    BaseLog.logError(this.getClass(),"close stream failed",e);
                }
            }
        }
    }

    //计算方法
    private int calcAddCRC(byte[] arr, int crc)
    {
        if(arr == null || arr.length == 0)
            return crc;
        for(int i=0; i<arr.length; ++i){
            crc += Byte.toUnsignedInt(arr[i]);
        }
        return crc;
    }


}
