package wind.ibroker.comm;



import wind.ibroker.util.BaseLog;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RecordSetHeader {

    ///头对象版本，注意 原来是ushort 变成了short
    private short version;
    /// <summary>
    /// 0：文本，1：报表，3：Excel，4：多报表
    /// </summary>
    private byte format;

    /// <summary>
    /// 是否压缩
    /// </summary>
    private boolean isCompressed;

    //返回信息
    private boolean isOk;

    /// <summary>
    ///返回数据是否文本，0：非文本，1：文本
    ///如果为文本则直接显示出来，一般为错误信息或异常情况才会用文本显示
    /// </summary>
    private boolean isText;

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    /// <summary>
    /// 返回信息
    /// </summary>
    private String information;

    /// <summary>
    /// 数据包的大小，不包含该Header的字节数据
    /// </summary>
    private int size;

    public short getVersion() {
        return version;
    }

    public void setVersion(short version) {
        this.version = version;
    }

    public byte getFormat() {
        return format;
    }

    public void setFormat(byte format) {
        this.format = format;
    }

    public boolean isCompressed() {
        return isCompressed;
    }

    public void setCompressed(boolean compressed) {
        isCompressed = compressed;
    }

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }

    public boolean isText() {
        return isText;
    }

    public void setText(boolean text) {
        isText = text;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public RecordSetHeader() {
        this.version = 1;
        this.format = 1;
        this.isOk = true;
        this.isCompressed = false;
    }

    /// <summary>
    /// 序列化该对象
    /// </summary>
    /// <param name="bw">写入器</param>
    /// <param name="header">RecordHeader对象</param>
    public static void serialize(DataOutputStream writer, RecordSetHeader header) throws IOException {
        if (header == null)
            return;
        ReportSerializer.writeShort(writer, header.version);
        ReportSerializer.writeByte(writer, header.format);
        ReportSerializer.writeBool(writer, header.isCompressed);
        ReportSerializer.writeBool(writer, header.isOk);
        if (header.isOk) {
            ReportSerializer.writeBool(writer, header.isText);
            ReportSerializer.writeInt(writer, header.size);
        } else {
            header.isText = true;
            ReportSerializer.writeBool(writer, header.isText);
            ReportSerializer.writeInt(writer, header.size);
            ReportSerializer.writeString(writer, header.information);
        }
    }

    /// <summary>
    /// 序列化该对象
    /// </summary>
    /// <param name="bw">写入器</param>
    /// <param name="header">RecordHeader对象</param>
    public byte[] serializeSelf() {
        ByteArrayOutputStream byteOS = new ByteArrayOutputStream();
        //DataOutputStream writer = new DataOutputStream(byteOS);
        try {
            ReportSerializer.writeShort(byteOS, this.version);
            ReportSerializer.writeByte(byteOS, this.format);
            ReportSerializer.writeBool(byteOS, this.isCompressed);
            ReportSerializer.writeBool(byteOS, this.isOk);
            if (this.isOk) {
                ReportSerializer.writeBool(byteOS, this.isText);
                ReportSerializer.writeInt(byteOS, this.size);
            } else {
                this.isText = true;
                ReportSerializer.writeBool(byteOS, this.isText);
                ReportSerializer.writeInt(byteOS, this.size);
                ReportSerializer.writeString(byteOS, this.information);
            }
            byte[] rt = byteOS.toByteArray();
            return rt;
        } catch (IOException ex) {
            BaseLog.logError(this.getClass(),"RecordSetHeader serializeSelf failed", ex);
            return null;
        } finally {
            try {
                byteOS.close();
            } catch (IOException e) {
               BaseLog.logError(RecordSetHeader.class,"close stream failed",e);
            }
        }
    }
}

