package wind.ibroker.util;

/**
 * @Author jinxu
 * @Date 2020/11/20
 */
import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.util.MailSSLSocketFactory;


public class QQMail {
	public static void sendMail(String emailAddress,String content) throws AddressException, MessagingException, GeneralSecurityException {
		// 0.1 确定邮箱服务器地址
		Properties props= new Properties();
		// 获取163邮箱的smtp服务器地址
		props.setProperty("mail.host", "smtp.qq.com");
		props.setProperty("mail.smtp.auth", "true");
		//QQ邮箱的SSL加密。
		MailSSLSocketFactory sf = new MailSSLSocketFactory();
		sf.setTrustAllHosts(true);
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.ssl.socketFactory", sf);

		// 0.2  确定权限(账号密码)
		Authenticator authenticator = new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				// 填写自己的QQ邮箱的账号密码
				return new PasswordAuthentication("2360930659@qq.com","bpwxssquemdxebjb");
			}

		};

		// 1.获得链接
		Session session= Session.getDefaultInstance(props, authenticator);

		// 2.创建消息
		Message message= new MimeMessage(session);
//		2.1 发件人
		message.setFrom(new InternetAddress("2360930659@qq.com"));
//		2.2 收件人
		message.setRecipient(RecipientType.TO, new InternetAddress(emailAddress));
//		2.3主题（标题）
		message.setSubject("客户端异常信息");
//		2.4正文
		String str=content;

		message.setContent(str, "text/html;charset=utf-8");

//		3.发送消息
			Transport.send(message);
	}
}
