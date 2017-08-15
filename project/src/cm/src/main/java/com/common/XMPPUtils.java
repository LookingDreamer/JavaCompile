package com.common;

import java.util.List;
import java.util.Random;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import com.cninsure.core.tools.util.ValidateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;

public class XMPPUtils {

	private String userName = ValidateUtil.getConfigValue("fairy.userName")+ "@" + ValidateUtil.getConfigValue("fairy.serviceName");
	private String passWord = ValidateUtil.getConfigValue("fairy.passWord");
	private String serviceName = ValidateUtil.getConfigValue("fairy.serviceName");
	private int port = Integer.parseInt(ValidateUtil.getConfigValue("fairy.port"));
	private String host = ValidateUtil.getConfigValue("fairy.host");
	private String resource = ValidateUtil.getConfigValue("fairy.resource");
	public static XMPPUtils getInstance() {
		return new XMPPUtils() ;
	}
	public static XMPPUtils getInstance(String userName, String pwd) {
		XMPPUtils xmpp= new XMPPUtils() ;
		xmpp.setUserName(userName);
		xmpp.setPassWord(pwd);
		return xmpp;
	}
	@Deprecated
	public void logOut(){
		
	}
	/*public static void main(String[] args) {
		System.out.println(new Random().nextInt(1000));
	}*/
	/**
	 * 默认构造函数
	 */
	public void sendMessage(List<String> toUsers, String msg) {
		XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration
				.builder().setUsernameAndPassword(userName, passWord)
				.setServiceName(serviceName).setPort(port)
				.setSecurityMode(SecurityMode.disabled).setHost(host)
				.setConnectTimeout(100000).setCompressionEnabled(false)
				.setResource(resource).build();
		AbstractXMPPConnection connection = new XMPPTCPConnection(config);
		connection.addConnectionListener(new XMPPConnectionListener());
		try {
			connection.connect();
			connection.login();
			ChatManager chatmanager=ChatManager.getInstanceFor(connection);
            chatmanager.addChatListener(new MyChatManagerListener());

			for (String user : toUsers) {
				Chat newChat = chatmanager.createChat(user);
				newChat.sendMessage(msg);
			}
		} catch (Exception e) {
			//SmackException | IOException | XMPPException
			LogUtil.error(e.getLocalizedMessage()+" openfire消息发送消息错误！exception="+e.getMessage());
		} finally {
			connection.disconnect();
		}
	}

	/**
	 * 通过XMPP协议发送消息
	 * 
	 * @param toUser
	 *            -消息接收方 ,ldap -cn用户名域
	 * @param msg
	 *            -消息体
	 * @throws NotConnectedException
	 */
	public void sendMessage(String toUser, String msg) throws Exception {
		XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration
				.builder().setUsernameAndPassword(userName, passWord)
				.setServiceName(serviceName).setPort(port)
				.setSecurityMode(SecurityMode.disabled).setHost(host)
				.setConnectTimeout(100000).setCompressionEnabled(false)
				.setResource(resource+new Random().nextInt(10000)).build();
		AbstractXMPPConnection connection = new XMPPTCPConnection(config);
		connection.addConnectionListener(new XMPPConnectionListener());
		try {
			connection.connect();
			connection.login();
			ChatManager chatmanager=ChatManager.getInstanceFor(connection);
            chatmanager.addChatListener(new MyChatManagerListener());
			Chat newChat = chatmanager.createChat(toUser);
			newChat.sendMessage(msg);
		} catch (Exception e) {
			LogUtil.error(e.getLocalizedMessage()+" openfire消息发送消息错误！exception="+e.getMessage()); 
		} finally {
			connection.disconnect();
		}
	}
	
	/**
	 * 修改用户状态
	 * @param toUsers
	 * @param status(参考Presence.Type): available-在线, unavailable-不在线
	 */
	public void updateStatus(String toUsers, String status) {
		if(StringUtil.isEmpty(status) || StringUtil.isEmpty(toUsers)){
			return;
		}
		XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration
				.builder().setUsernameAndPassword(userName, passWord)
				.setServiceName(serviceName).setPort(port)
				.setSecurityMode(SecurityMode.disabled).setHost(host)
				.setConnectTimeout(100000).setCompressionEnabled(false)
				.setResource(resource).build();
		AbstractXMPPConnection connection = new XMPPTCPConnection(config);
		connection.addConnectionListener(new XMPPConnectionListener());
		try {
			connection.connect();
			connection.login();
			Presence presence = null;
			 if (status.equals("unavailable")){
				presence = new Presence(Presence.Type.unavailable);
			} else {
				presence = new Presence(Presence.Type.available);
			}
			presence.setTo(toUsers);
			connection.sendStanza(presence);
			LogUtil.info("通知openfire用户下线，usercode=" + toUsers);
		} catch (Exception e) {
			LogUtil.error(e.getLocalizedMessage()+" openfire消息发送消息错误！exception="+e.getMessage());
		} finally {
			connection.disconnect();
		}
	}
	
	private class XMPPConnectionListener implements ConnectionListener {

		@Override
		public void reconnectionSuccessful() {
			//state = XMPPState.CONNECTED;
		}

		@Override
		public void reconnectionFailed(Exception arg0) {
			//state = XMPPState.NOT_CONNECTED;
			getInstance();
		}

		@Override
		public void reconnectingIn(int arg0) {
			//state = XMPPState.CONNECTED;
		}

		@Override
		public void connectionClosedOnError(Exception arg0) {
			//state = XMPPState.CONNECTED;
		}

		@Override
		public void connectionClosed() {
			//state = XMPPState.NOT_CONNECTED;
			getInstance();
		}

		@Override
		public void connected(XMPPConnection arg0) {
			//state = XMPPState.CONNECTED;
		}

		@Override
		public void authenticated(XMPPConnection arg0, boolean arg1) {
			//state = XMPPState.AUTHENTICATED;
		}
	}
	
	private class MyChatManagerListener implements ChatManagerListener{
		@Override
		public void chatCreated(Chat chat, boolean createdLocally) {
			chat.addMessageListener(new ChatMessageListener() {
				@Override
				public void processMessage(Chat chat, Message message) {
					if(message.getBody()!=null && !message.getBody().equals("null")){
//						queue.add(message.getBody());
					}
					
				}
			});
		}
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

}