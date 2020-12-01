/*
 * Created by JFormDesigner on Wed Nov 18 11:10:39 CST 2020
 */
package wind.ibroker.view;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.event.*;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.ContactList;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.Events;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import org.jetbrains.annotations.NotNull;
import wind.ibroker.impl.TaskThread;
import wind.ibroker.util.BaseLog;
import wind.ibroker.util.ConfigUtils;
import wind.ibroker.util.MsgUtils;
import wind.ibroker.util.QQMail;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
/**
 * @author unknown
 */
public class BrokerClient extends JFrame {

	private  String path="src/main/resources/group.txt";
	private  DefaultMutableTreeNode selectedNodeDelete;
	private  DefaultMutableTreeNode selectedNodeAdd;
	private List<String>groupNumberList=new ArrayList<>();
	private Bot bot; //机器人对象
	private  DefaultMutableTreeNode root;
	//维持节点的集合
	private ContactList<Group> groupsList;
	private static BrokerClient instance;

	/**
	 * 单例
	 * @return
	 */
	public static BrokerClient getInstance(){
		if(instance == null) instance = new BrokerClient();
		return instance;
	}
	public void start(Bot bot){
		this.bot = bot;
		this.groupsList=bot.getGroups();
		this.register(bot);
		initComponents();
		this.setVisible(true);
		this.setTitle("iBrokerMsg");
		this.setDefaultCloseOperation(0);
	}

	private void register(Bot bot) {
		Events.registerEvents(bot, new SimpleListenerHost() {
			//EventHandler可以指定多个属性，包括处理方式、优先级、是否忽略已取消的事件
			//其默认值请见EventHandler注解类
			//因为默认处理的类型为Listener.ConcurrencyKind.CONCURRENT
			//需要考虑并发安全
			@EventHandler
			public ListeningStatus onGroupMessage(GroupMessageEvent event) {
				String groupNickname=event.getGroup().getName();
				String groupNumber=String.valueOf(event.getGroup().getId());
				String group=groupNickname+"("+groupNumber+")";
				try {
					if (!groupNumberList.contains(group)){
						BaseLog.logError(this.getClass(),String.format("QQ群%s不在配置列表中，请重新设置!",
								event.getGroup()));
						return ListeningStatus.LISTENING;
					}
//					if (event.getMessage().contains("reply")) {
//						// 引用回复
//						final QuoteReply quote = new QuoteReply(event.getSource());
//						event.getGroup().sendMessage(quote.plus("引用回复"));
//					}
					long timeLong = (long)event.getTime() * 1000;
					SimpleDateFormat simFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String time  = simFormatter.format(timeLong);
					String groupName = event.getGroup().getName();
					long qqid = event.getSender().getId();
					String nickname =event.getSenderName();
					String msgString = BrokerClient.toString(event.getMessage());
					String msgNew = MsgUtils.getMsgStr(msgString,time, groupNumber, groupName,String.valueOf(qqid), nickname);
					TaskThread.getInstance().enQueue(msgNew);

					ChatRecordText.append(msgNew);
					ChatRecordText.append("\r\n");
					return ListeningStatus.LISTENING;
				} catch (Exception e) {
					BaseLog.logError(this.getClass(),"接收群消息异常:" + e.toString());
				}
				return null;
			}
			//处理在处理事件中发生的未捕获异常
			@Override
			public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
				JOptionPane.showMessageDialog(
						null,
						"信息异常，请及时联系管理人员，错误信息已经发送至你的邮箱了，请及时查收!",
						"消息",
						JOptionPane.ERROR_MESSAGE
				);
				if(!ConfigUtils.getInstance().getProperty("email").isEmpty()){
					try {
						QQMail.sendMail(ConfigUtils.getInstance().getProperty("email"),exception.toString());
					} catch (Exception e) {
						JOptionPane.showMessageDialog(
								null,
								"请检查你的QQ邮箱格式和账号是否输入正常!",
								"消息",
								JOptionPane.ERROR_MESSAGE
						);
						e.printStackTrace();
					}
				}
			}
		});
	}

	private static String toString(MessageChain chain) {
		return chain.contentToString();
	}
	private void write(){
		clearInfoForFile(path);
		Enumeration e = root.breadthFirstEnumeration();
		while(e.hasMoreElements())
		{
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)e.nextElement();
			if(!node.toString().equals("监控群列表")){
				writeFileGroups(node.toString());
			}
		}
	}
	private void logoutActionPerformed(ActionEvent e) {
		//关闭和服务端的连接，关闭socker
		try {
//			ClientSocket.getInstance().stop();
//			BrokerLogin.getInstance().taskThread.interrupt();
//			BrokerLogin.getInstance().connectThread.interrupt();
//			BrokerLogin.getInstance().heartThread.interrupt();
			write();
			System.exit(0);
		} //catch (IOException e1) {
			//BaseLog.logError(this.getClass(),"socket连接关闭出现异常状况。。。");
		 catch (Exception e1) {
			e1.printStackTrace();
		}

	}
	//时间显示
	private void getTimelabel() {

			Timer times = new Timer(1000, new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					time.setText(new SimpleDateFormat("yyyy/MM/dd/ EEEE ").format(new Date()));
				}
			});
			times.start();
	}
	private void resetActionPerformed(ActionEvent e) {
		this.emailtextField.setText("");
	}

	private void timeAncestorAdded(AncestorEvent e) {
		// TODO add your code here
		getTimelabel();
	}
	private void configEmailActionPerformed(ActionEvent e) {
		String email=this.emailtextField.getText();
		String format ="^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";
		boolean matches = email.matches(format);
		if(matches){
		// TODO add your code here
		 int flag=JOptionPane.showConfirmDialog(null, "确认是否配置你的客户端，我们会及时监控客户端，将错误消息发送至你的邮箱",
				"提示!", JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE);
		if(JOptionPane.YES_OPTION == flag){
			ConfigUtils.getInstance().setProperty("email",this.emailtextField.getText());
			this.emailtextField.setText(" ");
		}else{
			return;
		}
		}else{
			JOptionPane.showMessageDialog(
					null,
					"请检查你的邮箱格式是否正确",
					"消息",
					JOptionPane.ERROR_MESSAGE
			);
		}

	}
	private  DefaultMutableTreeNode InitgroupTree(){
		List<DefaultMutableTreeNode> list=new ArrayList<>();
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("群列表");
		for (Group group:groupsList) {
			 DefaultMutableTreeNode node = new DefaultMutableTreeNode(group.getName()+"("+group.getId()+")");
			list.add(node);
		}
		for (int i = 0; i < list.size(); i++) {
			root.insert(list.get(i),i);
		}
		return root;
	}

	private void tree1ValueChanged(TreeSelectionEvent e) {
		// TODO add your code here
		 selectedNodeAdd = (DefaultMutableTreeNode) tree1.getLastSelectedPathComponent();
	}

	private void groupListValueChanged(TreeSelectionEvent e) {
		// TODO add your code here
		 this.selectedNodeDelete = (DefaultMutableTreeNode) groupList.getLastSelectedPathComponent();
	}
//增加群列表
	private void addActionPerformed(ActionEvent e) {
		// TODO add your code here

		if(!groupNumberList.contains(selectedNodeAdd.toString()))
		{
			groupNumberList.add(selectedNodeAdd.toString());
			root.insert(selectedNodeAdd,0);
			groupList.updateUI();
		}
	}
//删除群列表
	private void deleteActionPerformed(ActionEvent e) {
		// TODO add your code here
		DefaultTreeModel model=(DefaultTreeModel)groupList.getModel();
		if (selectedNodeDelete != null && selectedNodeDelete.getParent() != null)
		{
			//删除指定节点
			int flag = JOptionPane.showConfirmDialog(null, "是否确认移除本监控群",
					"提示!", JOptionPane.YES_NO_OPTION,
					JOptionPane.INFORMATION_MESSAGE);
			if(JOptionPane.YES_OPTION == flag){
				groupNumberList.remove(selectedNodeDelete.toString());
				model.removeNodeFromParent(selectedNodeDelete);
			}else{
				return;
			}
		}
	}
	public  void writeFileGroups(String content)  {
		try {
			//打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			FileWriter writer = new FileWriter(path, true);
			writer.write(content+"\r\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// 清空已有的文件内容，以便下次重新写入新的内容
	public static void clearInfoForFile(String path) {
		File file =new File(path);
		try {
			if(!file.exists()) {
				file.createNewFile();
			}
			FileWriter fileWriter =new FileWriter(file);
			fileWriter.write("");
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public  void readFileGroups(DefaultMutableTreeNode root)  {
		BufferedReader reader = null;
		try {
			File file = new File(path);
			if(!file.exists())
				file.createNewFile();
			FileInputStream out=new FileInputStream(file);
			System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				DefaultMutableTreeNode group=new DefaultMutableTreeNode(tempString);
				root.insert(group,0);
				groupNumberList.add(tempString);
				groupList.updateUI();
				System.out.println("line " + line + ": " + tempString);
				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}


	private void initComponents() {

		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		ResourceBundle bundle = ResourceBundle.getBundle("form");
		loginPanel = new JPanel();
		label5 = new JLabel();
		scrollPane1 = new JScrollPane();
		ChatRecordText = new JTextArea();
		label6 = new JLabel();
		label8 = new JLabel();
		scrollPane3 = new JScrollPane();
		tree1 = new JTree();
		label9 = new JLabel();
		scrollPane4 = new JScrollPane();
		groupList = new JTree();
		configEmail = new JButton();
		label1 = new JLabel();
		emailtextField = new JTextField();
		reset = new JButton();
		logout = new JButton();
		label3 = new JLabel();
		label4 = new JLabel();
		time = new JLabel();
		add = new JButton();
		delete = new JButton();

		//======== this ========
		Container contentPane = getContentPane();
		contentPane.setLayout(null);

		//======== loginPanel ========
		{
			loginPanel.setMinimumSize(new Dimension(20, 20));
			loginPanel.setLayout(null);

			//---- label5 ----
			label5.setText("QQ\u7fa4\u5217\u8868");
			label5.setFont(label5.getFont().deriveFont(label5.getFont().getSize() + 3f));
			loginPanel.add(label5);
			label5.setBounds(new Rectangle(new Point(20, 40), label5.getPreferredSize()));

			//======== scrollPane1 ========
			{
				//---- ChatRecordText ----
				ChatRecordText.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 16));
				scrollPane1.setViewportView(ChatRecordText);
			}
			loginPanel.add(scrollPane1);
			scrollPane1.setBounds(10, 325, 495, 245);

			//---- label6 ----
			label6.setText("\u76d1\u63a7\u6d88\u606f\u8bb0\u5f55\uff1a");
			label6.setFont(label6.getFont().deriveFont(label6.getFont().getSize() + 3f));
			loginPanel.add(label6);
			label6.setBounds(new Rectangle(new Point(10, 295), label6.getPreferredSize()));

			//---- label8 ----
			label8.setText("\u5f53\u524d\u65f6\u95f4:");
			label8.setFont(label8.getFont().deriveFont(label8.getFont().getSize() + 5f));
			loginPanel.add(label8);
			label8.setBounds(490, 10, 85, 20);

			//======== scrollPane3 ========
			{
				DefaultMutableTreeNode root=InitgroupTree();
				tree1=new JTree(root);
				//---- tree1 ----
				tree1.setFont(tree1.getFont().deriveFont(tree1.getFont().getSize() + 5f));
				tree1.addTreeSelectionListener(e -> tree1ValueChanged(e));
				scrollPane3.setViewportView(tree1);
			}
			loginPanel.add(scrollPane3);
			scrollPane3.setBounds(15, 65, 300, 230);

			//---- label9 ----
			label9.setText("\u76d1\u63a7QQ\u7fa4\u5217\u8868");
			label9.setFont(label9.getFont().deriveFont(label9.getFont().getSize() + 3f));
			loginPanel.add(label9);
			label9.setBounds(420, 40, 106, 20);

			//======== scrollPane4 ========
			{
				root = new DefaultMutableTreeNode("监控群列表");
				this.readFileGroups(root);
				groupList=new JTree(root);
				//---- groupList ----
				groupList.setFont(groupList.getFont().deriveFont(groupList.getFont().getSize() + 5f));
				groupList.addTreeSelectionListener(e -> groupListValueChanged(e));
				scrollPane4.setViewportView(groupList);
			}
			loginPanel.add(scrollPane4);
			scrollPane4.setBounds(430, 70, 285, 235);

			//---- configEmail ----
			configEmail.setText("\u914d\u7f6e\u90ae\u7bb1");
			configEmail.setActionCommand("Logout");
			configEmail.setFont(configEmail.getFont().deriveFont(configEmail.getFont().getSize() + 5f));
			configEmail.addActionListener(e -> configEmailActionPerformed(e));
			loginPanel.add(configEmail);
			configEmail.setBounds(680, 460, 115, 30);

			//---- label1 ----
			label1.setText(bundle.getString("label1.text"));
			label1.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 17));
			loginPanel.add(label1);
			label1.setBounds(545, 355, 85, label1.getPreferredSize().height);
			loginPanel.add(emailtextField);
			emailtextField.setBounds(635, 350, 145, emailtextField.getPreferredSize().height);

			//---- reset ----
			reset.setText("\u91cd\u7f6e");
			reset.setActionCommand("Logout");
			reset.setFont(reset.getFont().deriveFont(reset.getFont().getSize() + 5f));
			reset.addActionListener(e -> resetActionPerformed(e));
			loginPanel.add(reset);
			reset.setBounds(555, 460, 85, 30);

			//---- logout ----
			logout.setText("\u9000\u51fa\u767b\u5f55");
			logout.setFont(logout.getFont().deriveFont(logout.getFont().getSize() + 3f));
			logout.addActionListener(e -> logoutActionPerformed(e));
			loginPanel.add(logout);
			logout.setBounds(750, 15, logout.getPreferredSize().width, 25);

			//---- label3 ----
			label3.setText("\u5f53\u524d\u7528\u6237\u72b6\u6001\uff1a");
			label3.setFont(new Font("Microsoft YaHei UI Light", Font.BOLD, 15));
			loginPanel.add(label3);
			label3.setBounds(new Rectangle(new Point(25, 10), label3.getPreferredSize()));

			//---- label4 ----
			label4.setText("\u5728\u7ebf");
			label4.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 15));
			label4.setForeground(Color.darkGray);
			loginPanel.add(label4);
			label4.setBounds(new Rectangle(new Point(130, 10), label4.getPreferredSize()));

			//---- time ----
			time.setText(bundle.getString("time.text"));
			time.setFont(time.getFont().deriveFont(time.getFont().getSize() + 4f));
			time.addAncestorListener(new AncestorListener() {
				@Override
				public void ancestorAdded(AncestorEvent e) {
					timeAncestorAdded(e);
				}
				@Override
				public void ancestorMoved(AncestorEvent e) {}
				@Override
				public void ancestorRemoved(AncestorEvent e) {}
			});
			loginPanel.add(time);
			time.setBounds(570, 10, 180, time.getPreferredSize().height);

			//---- add ----
			add.setText("+");
			add.setFont(add.getFont().deriveFont(add.getFont().getSize() + 11f));
			add.addActionListener(e -> addActionPerformed(e));
			loginPanel.add(add);
			add.setBounds(new Rectangle(new Point(345, 85), add.getPreferredSize()));

			//---- delete ----
			delete.setText("-");
			delete.setFont(delete.getFont().deriveFont(delete.getFont().getSize() + 9f));
			delete.addActionListener(e -> deleteActionPerformed(e));
			loginPanel.add(delete);
			delete.setBounds(new Rectangle(new Point(345, 185), delete.getPreferredSize()));
			{
				// compute preferred size
				Dimension preferredSize = new Dimension();
				for(int i = 0; i < loginPanel.getComponentCount(); i++) {
					Rectangle bounds = loginPanel.getComponent(i).getBounds();
					preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
					preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
				}
				Insets insets = loginPanel.getInsets();
				preferredSize.width += insets.right;
				preferredSize.height += insets.bottom;
				loginPanel.setMinimumSize(preferredSize);
				loginPanel.setPreferredSize(preferredSize);
			}
		}
		contentPane.add(loginPanel);
		loginPanel.setBounds(0, -5, 855, 575);

		{
			// compute preferred size
			Dimension preferredSize = new Dimension();
			for(int i = 0; i < contentPane.getComponentCount(); i++) {
				Rectangle bounds = contentPane.getComponent(i).getBounds();
				preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
				preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
			}
			Insets insets = contentPane.getInsets();
			preferredSize.width += insets.right;
			preferredSize.height += insets.bottom;
			contentPane.setMinimumSize(preferredSize);
			contentPane.setPreferredSize(preferredSize);
		}
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JPanel loginPanel;
	private JLabel label5;
	private JScrollPane scrollPane1;
	private JTextArea ChatRecordText;
	private JLabel label6;
	private JLabel label8;
	private JScrollPane scrollPane3;
	private JTree tree1;
	private JLabel label9;
	private JScrollPane scrollPane4;
	private JTree groupList;
	private JButton configEmail;
	private JLabel label1;
	private JTextField emailtextField;
	private JButton reset;
	private JButton logout;
	private JLabel label3;
	private JLabel label4;
	private JLabel time;
	private JButton add;
	private JButton delete;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
