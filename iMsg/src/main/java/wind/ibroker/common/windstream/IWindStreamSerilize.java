package wind.ibroker.common.windstream;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * WindStream序列化接口
 * */
public interface IWindStreamSerilize {
    /**
     * 序列化
     * @param writer 数据输出流
     * */
    void serialize(DataOutputStream writer) throws IOException;
}
