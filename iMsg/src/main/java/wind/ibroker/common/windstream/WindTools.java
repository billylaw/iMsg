package wind.ibroker.common.windstream;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Wind工具类
 * */
public class WindTools {
    /**
     * 日志工具
     * */
    static Logger _logger = LogManager.getLogger(WindTools.class);

    /**
     * 使用gzip算法压缩
     * */
    public static byte[] gzip(byte[] data) throws IOException {
        try(ByteArrayOutputStream is = new ByteArrayOutputStream()){
            try(GZIPOutputStream gzipInputStream = new GZIPOutputStream(is)){
                gzipInputStream.write(data);
                gzipInputStream.flush();
            }
            
            return is.toByteArray();
        }
    }

    public static void log(Object sender, String msg) {
        if(_logger.isInfoEnabled()){
            _logger.info(msg);
        }
    }

    /**
     * 使用gizp算法解压
     */
    static byte[] ungzip(byte[] data) throws IOException {
        byte[] buf = new byte[1024];
        byte[] output = null;
        
        try(ByteArrayOutputStream os = new ByteArrayOutputStream()){
            try(ByteArrayInputStream is = new ByteArrayInputStream(data)){
                try(GZIPInputStream gzipInputStream = new GZIPInputStream(is)){
                    int len = gzipInputStream.read(buf);
                    while(len != -1){
                        os.write(buf, 0, len);
                        os.flush();
                        
                        len = gzipInputStream.read(buf);
                    }
                }
            }
            
            output = os.toByteArray();
        }
        
        return output;
    }
}
