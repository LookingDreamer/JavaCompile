package com.zzb.conf.component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.dao.INSCMessageDao;
import com.cninsure.system.entity.INSCMessage;
import com.common.XMPPUtils;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.dao.INSBWorkflowsubDao;
import com.zzb.mobile.dao.INSBAgentstandbyinfoDao;

/**
 * cm向前端后端推送消息
 */
@Service
@PropertySource(value = { "classpath:config/config.properties" })
public class MessageManager {
	@Resource
	private INSCMessageDao messageDao;
	@Resource
	private INSBQuotetotalinfoDao quotetotalinfoDao;
	@Resource
	private INSBWorkflowsubDao workflowsubDao;
	@Resource
	private INSBAgentstandbyinfoDao insbAgentstandbyinfoDao;
	@Resource
	private INSBAgentDao insbAgentDao;
	@Resource
	private INSCCodeDao codeDao;
	
	@Value("${fairy.serviceName}")
	private String fairyServiceName;

//	public void sendMessage4Agent4Refuse(String mainInsatnceId,String subInstanceId,String taskName){
//		Map<String,String> param = new HashMap<String,String>();
//		param.put("codetype", "workflowNodelName");
//		param.put("codename", taskName);
//		String taskCode= codeDao.selectCodeValueByCodeName(param);
//		this.sendMessage4Agent("admin", mainInsatnceId, subInstanceId, taskCode);
//	}
	
	public void sendMessage4Agent(String fromUser, String mainInsatnceId,String subInstanceId,String taskcode) {
		LogUtil.info("向代理人发送消息-===START=====");
		// 查询当前代理人 拿到车牌号
		Map<String, String> agentMap = new HashMap<String,String>();
		Map<String,String> param = new HashMap<String,String>();
		if(subInstanceId!=null){
			
			//优化成一次查询  TODO 
			param.put("subInstanceId", subInstanceId);
			agentMap = quotetotalinfoDao.selectAgentDataByTaskIdAndSub(param);
		}else{
			agentMap = quotetotalinfoDao.selectAgentDataByTaskId(mainInsatnceId);
		}
		

		if (agentMap != null&&!agentMap.isEmpty()) {
			LogUtil.info("向代理人发送消息-===代理人信息agentMap："+agentMap.toString());
			String agentStr = "";
			String plateStr = "";
			String prvnameStr="";
			if (agentMap.get("agentnum") != null) {
				agentStr = agentMap.get("agentnum");
			}
			if (agentMap.get("platenumber") != null) {
				plateStr = agentMap.get("platenumber");
			}
			if (agentMap.get("prvname") != null) {
				prvnameStr = agentMap.get("prvname");
			}

			if (taskcode.equals("13")) {
					saveMessageData(fromUser, agentStr, plateStr+" "+prvnameStr+" 报价退回", plateStr+" "+prvnameStr+" 报价退回,请查收");
			}else if (taskcode.equals("14")) {
					saveMessageData(fromUser, agentStr, plateStr+" "+prvnameStr+" 报价已更新", plateStr+" "+prvnameStr+" 报价已更新,请查收");
			} else if (taskcode.equals("19")) {
				saveMessageData(fromUser, agentStr, plateStr+" "+prvnameStr+" 核保退回", plateStr+" "+prvnameStr+" 核保退回,请查收");
			} else if (taskcode.equals("20")) {
					saveMessageData(fromUser, agentStr, plateStr+" "+prvnameStr+" 核保已更新", plateStr+" "+prvnameStr+" 核保已更新,请查收");
			}  else if (taskcode.equals("21")) {
					saveMessageData(fromUser, agentStr, plateStr+" "+prvnameStr+" 支付已更新", plateStr+" "+prvnameStr+" 支付已更新,请查收");
			} else if (taskcode.equals("23")) {
					saveMessageData(fromUser, agentStr, plateStr+" "+prvnameStr+" 承保已更新", plateStr+" "+prvnameStr+" 承保已更新,请查收");
			} else if (taskcode.equals("24")) {
				saveMessageData(fromUser, agentStr, plateStr+" "+prvnameStr+" 承保打单已更新", plateStr+" "+prvnameStr+" 承保打单已更新,请查收");
			}else if (taskcode.equals("28")) {
					saveMessageData(fromUser, agentStr, plateStr+" "+prvnameStr+" 承保退回",  plateStr+" "+prvnameStr+" 承保退回,请查收");
			}else if (taskcode.equals("30")) {
				saveMessageData(fromUser, agentStr, plateStr+" "+prvnameStr+" 订单取消",  plateStr+" "+prvnameStr+" 订单取消,请查看");
			}
			
			//int num = messageDao.getNotReadMsgCount(agentStr);
			
//			sendXmppMessage4User(agentStr,num+"");
		}
		
		
		
	}

	private void saveMessageData(String fromUser, String toUser,String content, String title) {
		LogUtil.info("向代理人发送消息-===保存消息内容  fromUser："+fromUser+"  toUser:"+toUser+" title："+title+" content："+content );

		//TODO 杨
		//		String message = (String) com.common.RedisClient.get("msgAcceptingStatus:"+toUser);
//		INSBAgent agent = insbAgentDao.selectByJobnum(toUser);
//		if(agent != null){
//			INSBAgentstandbyinfo info = new INSBAgentstandbyinfo();
//			info.setAgentid(agent.getId());
//			info.setNoti("isacceptmessage");
//			info = insbAgentstandbyinfoDao.selectOne(info);
//			String message = null;
//			if(info != null){
//				message = info.getIsacceptmessage();
//			}
//			if("1".equals(message)||message==null||"".equals(message)){
//				INSCMessage messageModel = new INSCMessage();
//				messageModel.setCreatetime(new Date());
//				messageModel.setMsgcontent(content);
//				messageModel.setMsgtitle(title);
//				messageModel.setOperator("admin");
//				messageModel.setReceiver(toUser);
//				messageModel.setSender(fromUser);
//				messageModel.setSendtime(getNowDate());
//				messageModel.setStatus(0);
//				messageModel.setState(1);
//
//				messageDao.insert(messageModel);
//			}
//		}
	}

	private String getNowDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	
	private void sendXmppMessage4User(String toUser,String num) {
		
		try {
//			String xmppUserName =  (String) com.common.RedisClient.get(toUser.toLowerCase()+"_online");
//			XMPPUtils xmppInstance = XMPPUtils.getInstance();
//			xmppInstance.sendMessage(xmppUserName, num);
		} catch (Exception e) {
			//e.printStackTrace();
			//TODO 用户不在线   不再打印错误信息
			LogUtil.warn("xmpp向"+toUser+",发送消息错误="+new Date());
		}
	}
}
