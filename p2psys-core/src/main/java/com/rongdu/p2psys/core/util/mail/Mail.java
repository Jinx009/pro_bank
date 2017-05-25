package com.rongdu.p2psys.core.util.mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.log4j.Logger;

import com.rongdu.common.util.StringUtil;
import com.rongdu.common.util.code.BASE64Encoder;
import com.rongdu.common.util.code.MD5;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.model.SystemConfigModel;
import com.rongdu.p2psys.user.domain.User;

/**
 * 邮件实体类
 * 
 * @author xx
 * @version 2.0
 * @since 2014年2月27日
 */
public class Mail {

	private static Logger logger = Logger.getLogger(Mail.class);

	// 发送邮件的服务器的IP
	private String host;
	// 邮件发送者的地址
	private String from;
	// 发件人昵称
	private String nick;
	// 邮件接收者的地址
	private String to;
	// 邮件主题
	private String subject;
	// 邮件的文本内容
	private String body;
	// 发送邮件的服务器的端口
	private String port;
	private EmailAutherticator auth;

	private Mail() {
		init();
	}

	private void init() {
		SystemConfigModel info = Global.SYSTEMINFO;
		EmailAutherticator auth = new EmailAutherticator(info.getValue("email_email"), info.getValue("email_pwd"));
		host = info.getValue("email_host");
		//from = auth.getUsername();
		from = info.getValue("email_from");
		logger.debug("From:" + from);
		nick = info.getValue("email_from_name");
		port = info.getValue("email_port");
		this.setAuth(auth);
		this.setHost(host);
		this.setNick(nick);
		this.setFrom(from);
		this.setPort(port);
	}

	public static Mail getInstance() {
		Mail mail = new Mail();
		SystemConfigModel info = Global.SYSTEMINFO;
		EmailAutherticator auth = new EmailAutherticator(info.getValue("email_email"), info.getValue("email_pwd"));
		String host = info.getValue("email_host");
		String from = auth.getUsername();
		String nick = info.getValue("email_from_name");
		String port = info.getValue("email_port");
		mail.setAuth(auth);
		mail.setHost(host);
		mail.setNick(nick);
		mail.setFrom(from);
		mail.setPort(port);
		return mail;
	}

	public String transferChinese(String strText) {
		try {
			strText = MimeUtility.encodeText(new String(strText.getBytes(), "utf-8"), "utf-8", "B");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strText;
	}

	public void sendMail() throws Exception {
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true");

		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.smtp.port", port);
		props.setProperty("mail.smtp.socketFactory.port", port);

		Session session = Session.getDefaultInstance(props, auth);
		MimeMessage message = new MimeMessage(session);
		message.setContent("Hello", "text/plain");
		logger.debug(subject);
		message.setSubject(subject, "utf-8");// 设置邮件主题
		message.setSentDate(new Date());// 设置邮件发送时期
		Address address = new InternetAddress(from, nick, "utf-8");

		message.setFrom(address);// 设置邮件发送者的地址
		Address toaddress = new InternetAddress(to);// 设置邮件接收者的地址
		message.addRecipient(Message.RecipientType.TO, toaddress);
		// 创建一个包含HTML内容的MimeBodyPart
		Multipart mainPart = new MimeMultipart();
		BodyPart html = new MimeBodyPart();
		html.setContent(body, "text/html; charset=utf-8");
		mainPart.addBodyPart(html);
		// 将MiniMultipart对象设置为邮件内容
		message.setContent(mainPart);
		try {
			Transport.send(message);
			logger.error(to+"-----"+from+"-------------"+body);
		} catch (MessagingException e) {
			logger.error("Send Email founds error!",e);
		}
	}

	@SuppressWarnings("unused")
	private void saveEmailToSentMailFolder(Folder sentFolder, Message message) {
		Store store = null;
		try {
			sentFolder.appendMessages(new Message[] { message });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			// 判断发件文件夹是否打开如果打开则将其关闭
			if (sentFolder != null && sentFolder.isOpen()) {
				try {
					sentFolder.close(true);
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
			// 判断邮箱存储是否打开如果打开则将其关闭
			if (store != null && store.isConnected()) {
				try {
					store.close();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void sendMail(String to, String subject, String content) throws Exception {
		this.setTo(to);
		this.setSubject(subject);
		this.setBody(content);
		this.sendMail();
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public EmailAutherticator getAuth() {
		return auth;
	}

	public void setAuth(EmailAutherticator auth) {
		this.auth = auth;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void readActiveMailMsg() {
		this.readMsg("/res/mail.msg");
	}

	private void readMsg(String filename) {
		StringBuffer sb = new StringBuffer();
		InputStream fis = Mail.class.getResourceAsStream(filename);
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(fis, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			logger.error("读取文件遇见不正确的文件编码！");
		}
		BufferedReader br = new BufferedReader(isr);
		String line = "";
		try {
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		String msg = sb.toString();
		logger.info(msg);
		setBody(msg);
	}

	public String getdecodeIdStr(User user) {
		String chars = "0123456789qwertyuiopasdfghjklmnbvcxz-=~!@#$%^*+-._/*<>|";
		int length = chars.length();
		StringBuffer url = new StringBuffer();
		StringBuffer rancode = new StringBuffer();
		String timeStr = System.currentTimeMillis() / 1000 + "";
		String userIDAddtime = user.getUserId() + StringUtil.isNull(user.getAddTime());
		userIDAddtime = MD5.getMD5ofStr(userIDAddtime);
		url.append(user.getUserId()).append(",").append(userIDAddtime).append(",").append(timeStr).append(",");
		for (int i = 0; i < 10; i++) {
			int num = (int) (Math.random() * (length - 2)) + 1;
			rancode.append(chars.charAt(num));
		}
		url.append(rancode);
		String idurl = url.toString();
		BASE64Encoder encoder = new BASE64Encoder();
		String s = encoder.encode(idurl.getBytes());
		return s;
	}

	public void replace(String webname, String host, String username, String email, String url) {
		String msg = this.getBody();
		msg = msg.replace("$webname$", webname).replace("$email$", email).replace("$host$", host)
				.replace("$username$", username).replace("$url$", host + url);
		this.setBody(msg);
	}
}
