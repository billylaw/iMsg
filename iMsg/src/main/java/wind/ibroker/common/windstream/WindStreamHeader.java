package wind.ibroker.common.windstream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * WindStream 头部
 * */
public class WindStreamHeader implements IWindStreamSerilize{
    /**
     * WindStream包名
     * */
    private String packName;

    /**
     * 是否压缩
     * */
    private boolean isCompress;

    /**
     * 大小
     * */
    private int size;

    /**
     * 版本
     * */
    private short version;

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public void setIsCompress(boolean isCompress) {
        this.isCompress = isCompress;
    }

    public void setSize(int size) {
        this.size = size;
    }

    /**
     * 序列化
     * */
    @Override
    public void serialize(DataOutputStream writer) throws IOException {
        Serializer.writeInt16(writer, this.version);
        Serializer.writeBool(writer, this.isCompress);
        Serializer.writeInt32(writer, this.size);
        Serializer.writeString(writer, this.packName);
    }

    /**
     * 设置版本号
     * */
    public void setVersion(short version) {
        this.version = version;
    }

    public String getPackName() {
        return this.packName;
    }

    /**
     * 反序列化WindStream头部
     */
    void deserialize(DataInputStream br) throws IOException {
        this.version = Serializer.readInt16(br);
        this.isCompress = Serializer.readBool(br);
        this.size = Serializer.readInt32(br);
        this.packName = Serializer.readString(br);
    }

    /**
     * 返回大小
     */
    int getSize() {
        return this.size;
    }

    /**
     * 返回是否压缩
     */
    boolean getIsCompress() {
        return this.isCompress;
    }
}
