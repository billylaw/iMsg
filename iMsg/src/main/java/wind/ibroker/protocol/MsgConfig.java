package wind.ibroker.protocol;

public class MsgConfig {
    /// <summary>
    /// 消息头开始标识
    /// </summary>
    public  static final byte MSG_START_TAG = 0x77;
    /// <summary>
    /// 消息的最短字节长度
    /// </summary>
    public static final int MSG_MIN_LEN = 24;
    /// <summary>
    /// 开始标识+MSG_HEADER_LEN = 20
    /// </summary>
    public static final int MSG_HEADER_LENGTH = 20;
}
