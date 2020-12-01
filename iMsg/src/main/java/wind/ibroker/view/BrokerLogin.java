/*
 * Created by JFormDesigner on Wed Nov 18 14:13:58 CST 2020
 */

package wind.ibroker.view;

import net.mamoe.mirai.Bot;

import org.apache.commons.lang3.ObjectUtils;

import wind.ibroker.impl.TaskThread;

import wind.ibroker.socket.ClientSocket;
import wind.ibroker.socket.MsgConnect;
import wind.ibroker.util.BaseLog;
import wind.ibroker.util.ConfigUtils;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author jinxu
 */
public class BrokerLogin extends JFrame {
	private Bot bot; //机器人对象
	//启动连接
	public Thread connectThread;
	public Thread taskThread;
	public Thread heartThread;
	private static BrokerLogin instance;

	/**
	 * 单例
	 *
	 * @return
	 */
	public static BrokerLogin getInstance() {
		if (instance == null) instance = new BrokerLogin();
		return instance;
	}

	/**
	 * 启动方法
	 */
	public void start() {
		initComponents();

		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setTitle("Client");
		if (!ObjectUtils.isEmpty(ConfigUtils.getInstance().getProperty("qqnum")) && !ObjectUtils.isEmpty(ConfigUtils.getInstance().getProperty("qqpwd"))) {
			this.username.setText(ConfigUtils.getInstance().getProperty("qqnum").toString());
			this.password.setText(ConfigUtils.getInstance().getProperty("qqpwd").toString());
		}
	}


	private void loginButtonActionPerformed(ActionEvent e) {
		try {
			//启动连接服务端的线程
			//startConnectWind();
			//waitToLogin();
			//发送心跳线程
			//heartThread = new Thread(HeartBeatThread.getInstance());
			//heartThread.start();
			BaseLog.logInfo("开始登录QQ，请稍等...");
			String qqNum = username.getText().trim();
			String qqPwd = String.valueOf(password.getPassword());
			String cacheNum = ConfigUtils.getInstance().getProperty("qqnum");
			if (this.checkBox1.isSelected() && !cacheNum.equals(qqNum)) {
				ConfigUtils.getInstance().setProperty("qqnum", qqNum);
				ConfigUtils.getInstance().setProperty("qqpwd", qqPwd);
			}
			MsgConnect.qqNum = qqNum;
			MsgConnect.qqPwd = qqPwd;
			MsgConnect.isLogin = true;
			this.setVisible(false);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(
					null,
					"登陆异常，请先检查登陆账号和密码或联系管理员!",
					"消息",
					JOptionPane.ERROR_MESSAGE
			);
		}
	}

	private void waitToLogin() throws Exception {
		while (ClientSocket.getInstance().isConnectError) {
			Thread.sleep(100);
		}
	}
	//和后台服务端进行连接

	/**
	 * 启动连接
	 */
	private void startConnectWind() throws Exception {
		connectThread = new Thread(ClientSocket.getInstance());
		connectThread.start();
		taskThread = new Thread(TaskThread.getInstance());
		taskThread.start();
		while (!ClientSocket.getInstance().isConnected) {
			if (ClientSocket.getInstance().isConnectError) {
				int flag = JOptionPane.showConfirmDialog(null, "连接wind后台服务器失败，请联系管理员...",
						"提示!", JOptionPane.YES_NO_OPTION,
						JOptionPane.INFORMATION_MESSAGE);
				if (JOptionPane.YES_OPTION == flag) {
					System.exit(0);
				} else {
					return;
				}
				return;
			}
			Thread.sleep(200);
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		label1 = new JLabel();
		label2 = new JLabel();
		username = new JTextField();
		password = new JPasswordField();
		loginButton = new JButton();
		checkBox1 = new JCheckBox();

		//======== this ========
		setBackground(new Color(255, 255, 51));
		Container contentPane = getContentPane();
		contentPane.setLayout(null);

		//---- label1 ----
		label1.setText("\u7528\u6237\u540d\uff1a");
		label1.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
		contentPane.add(label1);
		label1.setBounds(new Rectangle(new Point(50, 30), label1.getPreferredSize()));

		//---- label2 ----
		label2.setText("\u5bc6\u7801\uff1a");
		label2.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
		contentPane.add(label2);
		label2.setBounds(50, 90, 45, 30);
		contentPane.add(username);
		username.setBounds(115, 30, 120, 25);
		contentPane.add(password);
		password.setBounds(115, 90, 120, password.getPreferredSize().height);

		//---- loginButton ----
		loginButton.setText("\u767b\u5f55");
		loginButton.setFont(loginButton.getFont().deriveFont(loginButton.getFont().getSize() + 3f));
		loginButton.addActionListener(e -> loginButtonActionPerformed(e));
		contentPane.add(loginButton);
		loginButton.setBounds(new Rectangle(new Point(60, 165), loginButton.getPreferredSize()));

		//---- checkBox1 ----
		checkBox1.setText("\u8bb0\u4f4f\u5bc6\u7801");
		checkBox1.setFont(checkBox1.getFont().deriveFont(checkBox1.getFont().getSize() + 4f));
		contentPane.add(checkBox1);
		checkBox1.setBounds(new Rectangle(new Point(175, 165), checkBox1.getPreferredSize()));

		contentPane.setPreferredSize(new Dimension(330, 305));
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JLabel label1;
	private JLabel label2;
	private JTextField username;
	private JPasswordField password;
	private JButton loginButton;
	private JCheckBox checkBox1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
