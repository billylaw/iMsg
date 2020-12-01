package wind.ibroker.impl;

/**
 * @Author jinxu
 * @Date 2020/11/24
 */
import org.apache.commons.lang3.StringUtils;
import wind.ibroker.protocol.Consts;
import wind.ibroker.socket.ClientSocket;
import wind.ibroker.util.BaseLog;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * 暂时不用
 * 任务线程
 */
public class TaskThread implements Runnable{


	private static TaskThread instance;

	private static Object obj = new Object();

	public static TaskThread getInstance(){
		synchronized (obj){
			if(instance == null) instance = new TaskThread();
			return instance;
		}
	}

	private Queue<String> queue =new ArrayDeque<>();

	public void enQueue(String msg) throws Exception{
		queue.add(msg);
	}

	public String deQueue() throws Exception{
		return queue.poll();
	}

	@Override
	public void run() {
		while(true){
			try{
				Thread.sleep(200);
				String msg = deQueue();
				if(StringUtils.isEmpty(msg))  continue;
				MsgHandler.getInstance().doSend(Consts.CMD_SENDMSG, msg);
			}catch (Exception ex){
				BaseLog.logError(this.getClass(), ex.getMessage());
			}
		}

	}
}
