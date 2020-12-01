package wind.ibroker.socket;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wind.ibroker.util.BaseLog;
import wind.ibroker.util.MD5Utils;
import wind.ibroker.util.MsgUtils;
import wind.ibroker.comm.ExpoSerializer;
import wind.ibroker.protocol.Consts;
import wind.ibroker.protocol.Message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class QQClient implements Runnable{
    //可配置，暂时写死
    public final static String ip = "10.100.2.65";
    public final static int port = 9100;

    private Selector selector = null;
    private SocketChannel sc;
    public boolean isConnected = false;
    public boolean isConnectError = false;
    private Logger logger= LogManager.getLogger(this.getClass());
    public static QQClient instance;

    private static Object obj = new Object();
    //分配读数据缓冲区 8k
    private ByteBuffer receiveBuffer = ByteBuffer.allocate(1024 * 8);
    //发送缓冲区
    private ByteBuffer sendBuffer = ByteBuffer.allocate(1024 * 8);

    public static QQClient getInstance(){
        synchronized (obj){
            if(instance == null) instance = new QQClient();
            return instance;
        }
    }
    /**
     * 新建个连接服务器的线程
     */
    @Override
    public void run() {
        try{
            logger.log(Level.INFO,"开始与"+ip+":"+port + "服务器建立连接...");
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
            subscribe();
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
    }

    /**
     * 发送消息到服务器
     * @param content
     */
    public void sendMsg(byte[] content){
        if(content == null || content.length == 0) return;
        try{
            sendBuffer.clear();
            sendBuffer.put(content);
            sendBuffer.flip();
            sc.write(sendBuffer);
        }catch (Exception ex){
            logger.log(Level.ERROR, "发送数据出现异常ex："+ ex.getMessage());
        }
    }
    public void send(String msg){
        byte[] msgBytes = null;
        try{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Message bm = new Message();
            ExpoSerializer.writeString(bos, msg);
            bm.getHeader().setCommandId(Consts.CMD_SENDMSG);
            bm.setMessageBody(bos.toByteArray());
            bos.close();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            bm.serialize(os);
            msgBytes = os.toByteArray();
            os.close();
            sendMsg(msgBytes);
        }catch (Exception ex){
            BaseLog.logError(MsgUtils.class, "转化为字节流失败,ex "+  ex.getMessage());
        }
    }

    /**
     * 登录后台服务验证
     */
    public void login(){
        byte[] msgBytes = null;
        try{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Message bm = new Message();
            String loginName = "456";
            String password = MD5Utils.toMD5("123456");
            ExpoSerializer.writeBytes(bos, loginName.getBytes("UTF-8"));
            ExpoSerializer.writeBytes(bos, password.getBytes("UTF-8"));
            bm.getHeader().setCommandId(Consts.CMD_LOGIN);
            bm.setMessageBody(bos.toByteArray());
            bos.close();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            bm.serialize(os);
            msgBytes = os.toByteArray();
            sendMsg(msgBytes);
        }catch (Exception ex){
            BaseLog.logError(MsgUtils.class, "发送登录消息失败,ex "+  ex.getMessage());
        }
    }
    //关闭线程
    public void stop(){
        if(this.instance!=null){

        }
    }

    /**
     * 向服务发送订阅信息
     */
    public void subscribe(){
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
            bm.getHeader().setCommandId(Consts.CMD_QUERYWITHOPTIONBench);
            bm.setMessageBody(bos.toByteArray());
            bos.close();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            bm.serialize(os);
            msgBytes = os.toByteArray();
            sendMsg(msgBytes);
        }catch (Exception ex){
            BaseLog.logError(MsgUtils.class, "发送登录消息失败,ex "+  ex.getMessage());
        }
    }
}
