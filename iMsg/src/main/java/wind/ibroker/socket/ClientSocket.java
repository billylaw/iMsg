package wind.ibroker.socket;


import org.apache.commons.lang3.ObjectUtils;
import wind.ibroker.impl.MsgHandler;
import wind.ibroker.util.BaseLog;
import wind.ibroker.util.ConfigUtils;
import wind.ibroker.protocol.Consts;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 客户端连接信息
 */
public class ClientSocket implements Runnable{

    /**
     * ip port信息
     */
    private String ip;
    private int port ;

    /**
     * 连接状态和连接异常
     */
    public boolean isConnected = false;
    public boolean isConnectError = false;

    /**
     *  Selector和SocketChannel
     */
    private Selector selector = null;
    private SocketChannel sc;

    /**
     * 分配读数据缓冲区 8k
     */
    private ByteBuffer receiveBuffer;
    /**
     * 发送缓冲区
     */
    private ByteBuffer sendBuffer;

    /**
     * 单例
     */
    private static ClientSocket instance;
    private static Object obj = new Object();
    public static ClientSocket getInstance(){
        synchronized (obj){
            if(instance == null) instance = new ClientSocket();
            return instance;
        }
    }

    public ClientSocket(){
        try{
            if(ObjectUtils.isEmpty(ConfigUtils.getInstance().getProperty("serverip"))){
                ConfigUtils.getInstance().setProperty("serverip", "10.100.2.65");
            }
            if(ObjectUtils.isEmpty(ConfigUtils.getInstance().getProperty("serverport"))){
                ConfigUtils.getInstance().setProperty("serverport", "9100");
            }
            ip = ConfigUtils.getInstance().getProperty("serverip").toString();
            port = Integer.parseInt(ConfigUtils.getInstance().getProperty("serverport").toString());
            receiveBuffer = ByteBuffer.allocate(1024 * 1024 * 2);
            sendBuffer = ByteBuffer.allocate(1024 * 1024 * 2);
        }catch (Exception ex){
            BaseLog.logError(this.getClass(), "初始化CientSocket失败，请检查配置文件是否正确...");
        }
    }

    /**
     * 新建线程启动连接
     */
    @Override
    public void run() {
        try{
            BaseLog.logInfo("开始与"+ip+":"+port + "服务建立连接...");
            selector = Selector.open();
            sc = SocketChannel.open(new InetSocketAddress(ip, port));
            sc.configureBlocking(false);
            sc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_CONNECT);
            while(!sc.finishConnect()){
                BaseLog.logInfo("正在与"+ip+":"+port + "服务器建立连接，请稍等...");
                Thread.sleep(10);
            }
            BaseLog.logInfo("与"+ip+":"+port + "与后台服务器建立连接成功...");
            isConnected = true;
            MsgHandler.getInstance().doSend(Consts.CMD_QUERYWITHOPTIONBench,null);
            while(true){
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                //只考虑读数据
                while(iterator.hasNext()){
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    if(selectionKey.isReadable()){
                        SocketChannel sc1 = (SocketChannel)selectionKey.channel();
                        readMsg(sc1);
                    }
                }
            }
        }catch (Exception ex){
                BaseLog.logError(this.getClass(),"连接异常， 请检查与服务器的连接...");
                isConnectError  = true;
                isConnected = false;
                try{
                    sc.close();
                }catch (Exception e){
                    isConnectError  = true;
                    BaseLog.logError(this.getClass(),"关闭端口发生异常..");
                }
        }
    }

    /**
     *  读数据
     * @param sc
     * @throws IOException
     */
    private void readMsg(SocketChannel sc) throws IOException {
        sc.read(receiveBuffer);
        BaseLog.logInfo("接收到消息:" + receiveBuffer.toString());
        receiveBuffer.flip();
        //接收到服务器数据后续操作
        MsgHandler.getInstance().doRecivData(receiveBuffer.array());
    }

    /**
     * 发送消息到服务器
     * @param content
     */
    public void send(byte[] content){
        if(content == null || content.length == 0) return;
        try{
            sendBuffer.clear();
            sendBuffer.put(content);
            sendBuffer.flip();
            sc.write(sendBuffer);
        }catch (Exception ex){
            BaseLog.logError(this.getClass(), "发送数据出现异常ex："+ ex.getMessage());
        }
    }



    /**
     * 停止端口
     * @throws Exception
     */
    public void stop() throws Exception{
        if(!ObjectUtils.isEmpty(sc)){
            sc.close();
        }
    }

}
