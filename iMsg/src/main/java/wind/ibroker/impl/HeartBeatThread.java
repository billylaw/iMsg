package wind.ibroker.impl;
import wind.ibroker.protocol.Consts;
import wind.ibroker.util.BaseLog;


/**
 * @Author jinxu
 * @Date 2020/11/26
 */
public class HeartBeatThread implements Runnable {
	private static HeartBeatThread instance;

	private static Object obj = new Object();

	public static HeartBeatThread getInstance(){
		synchronized (obj){
			if(instance == null) instance = new HeartBeatThread();
			return instance;
		}
	}
	@Override
	public void run() {
		while(true){
			try{
				MsgHandler.getInstance().doSend(Consts.CMD_HEART_BEAT, "client heart beat running");
				Thread.sleep(5000);
			}catch (Exception ex){
				BaseLog.logError(this.getClass(), ex.getMessage());
			}
		}

	}
}
