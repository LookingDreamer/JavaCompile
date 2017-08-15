package com.zzb.conf.component;

import com.common.redis.CMRedisClient;
import com.common.redis.IRedisClient;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cninsure.system.dao.INSCMessageDao;
import com.cninsure.system.entity.INSCMessage;
import com.common.XMPPUtils;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.mobile.dao.INSBAgentstandbyinfoDao;
import com.zzb.mobile.entity.INSBAgentstandbyinfo;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class MessageManagerTest {
	@Resource
	private INSCMessageDao messageDao;
	@Resource
	private INSBAgentstandbyinfoDao insbAgentstandbyinfoDao;
	@Resource
	private INSBAgentDao insbAgentDao;
	@Resource
	private IRedisClient redisClient;

	@Test
	public void saveMessageData() {
		String message = (String) redisClient.get("cm:test", "msgAcceptingStatus:610425783");
		if("1".equals(message)){
			INSCMessage messageModel = new INSCMessage();

			messageModel.setCreatetime(new Date());
			messageModel.setMsgcontent("测试消息设置");
			messageModel.setMsgtitle("测试消息设置");
			messageModel.setOperator("admin");
			messageModel.setReceiver("620429536");
			messageModel.setSender("aaaaaaaaaaaaa");
			messageModel.setSendtime(getNowDate());
			messageModel.setStatus(0);
			messageModel.setState(1);
			
			messageDao.insert(messageModel);
		}
	}

	@Test
	public void saveMessageDataTest() {
		String toUser = "610195437",fromUser = "620077557",content = "content",title = "title";
		INSBAgent agent = insbAgentDao.selectByJobnum(toUser);
		if(agent != null){
			INSBAgentstandbyinfo info = new INSBAgentstandbyinfo();
			info.setAgentid(agent.getId());
			info.setNoti("isacceptmessage");
			info = insbAgentstandbyinfoDao.selectOne(info);
			String message = null;
			if(info != null){
				message = info.getIsacceptmessage();
			}
			if("1".equals(message)||message==null||"".equals(message)){
				INSCMessage messageModel = new INSCMessage();
				messageModel.setCreatetime(new Date());
				messageModel.setMsgcontent(content);
				messageModel.setMsgtitle(title);
				messageModel.setOperator("admin");
				messageModel.setReceiver(toUser);
				messageModel.setSender(fromUser);
				messageModel.setSendtime(getNowDate());
				messageModel.setStatus(0);
				messageModel.setState(1);
				
				messageDao.insert(messageModel);
			}
		}
	}
	
	private String getNowDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	
	private void sendXmppMessage4User(String toUser,String num) {
		
		String xmppUserName =  (String) redisClient.get("cm:test", toUser+"_online");

		
		XMPPUtils xmppInstance = XMPPUtils.getInstance();
		try {
			xmppInstance.sendMessage(xmppUserName, num);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
