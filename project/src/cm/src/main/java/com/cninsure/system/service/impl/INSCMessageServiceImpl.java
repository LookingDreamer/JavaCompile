package com.cninsure.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.DateUtil;
import com.cninsure.system.dao.INSCMessageDao;
import com.cninsure.system.dao.INSCUserDao;
import com.cninsure.system.entity.INSCMessage;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCMessageService;

@Service
@Transactional
public class INSCMessageServiceImpl extends BaseServiceImpl<INSCMessage> implements INSCMessageService {

	@Resource
	private INSCMessageDao  inscMessageDao;
	@Resource
	private INSCUserDao  inscUserDao;
	@Override
	protected BaseDao<INSCMessage> getBaseDao() {
		// TODO Auto-generated method stub
		return inscMessageDao;
	}
	public int getNotReadMsgCount(String receiver){
		return this.inscMessageDao.getNotReadMsgCount(receiver);
	}
	public List<INSCMessage> getAllReceiveMessages(String receiver){
		return this.inscMessageDao.getAllByReceiver(receiver);
	}
	@Override
	public Map<String, Object> getReceiveMessagesPaging(Map<String, Object> data,String receiver) {
		Map<String, Object> map = new HashMap<String, Object>();
		long total = inscMessageDao.getMsgCount(receiver);
		List<Map<Object, Object>> infoList = inscMessageDao
				.getReceiveMessagesPaging(data);

		map.put("total", total);
		map.put("rows", infoList);
		return map;
	}
	public List<INSCMessage> getAllSenderMessages(String sender){
		return this.inscMessageDao.getAllBySender(sender);
	}
	@Override
	public void markedReaded(String id) {
		Map map =new HashMap();
		map.put("id", id);
		map.put("readtime", DateUtil.getCurrentDateTime());
		this.inscMessageDao.updateReadedById(map);
	}
	@Override
	public void markedNotReaded(String id) {
		this.inscMessageDao.updateNotReadById(id);
	}
	@Override
	public Map<String, Object> getReceiveMessagesPaging(Map<String, Object> map) {
		Map<String, Object> map2 = new HashMap<String, Object>();
		String receiver =(String)map.get("receiver");
		long total = inscMessageDao.getMsgCountByReceiverPush(receiver);
		List<Map<Object, Object>> infoList = inscMessageDao
				.getReceiveMessagesPaging(map);

		map.put("total", total);
		map.put("rows", infoList);
		return map;
	}
	@Override
	public Map<String, Object> getNotReadMessagesPaging(Map<String, Object> map) {
		// TODO Auto-generated method stub
		Map<String, Object> map2 = new HashMap<String, Object>();
		String reciver =(String)map.get("receiver");
		long total = inscMessageDao.getNotReadMsgCountPush(reciver);
		map.put("status", 0);
		List<Map<Object, Object>> infoList = inscMessageDao
				.getReceiveMessagesPaging(map);

		map.put("total", total);
		map.put("rows", infoList);
		return map;
	}
	@Override
	public INSCMessage viewMessage(String id) {
		INSCMessage inscMessage = inscMessageDao.selectById(id);
		INSCUser inscUser = new INSCUser();
		inscUser.setUsercode(inscMessage.getSender());
		inscUser = inscUserDao.selectOne(inscUser);
		inscMessage.setSender(inscUser.getName());
		 
		if(inscMessage==null)return inscMessage;
		inscMessage.setStatus(1);
		markedReaded(id);
		return inscMessage;
	}
	@Override
	public String deleteMessage(String msgid) {
		try{
			inscMessageDao.deleteById(msgid);
			return "success";
		}catch(Exception e){
			e.printStackTrace();
			return "fail";
		}
	}
	@Override
	public int getMsgCount(String receiver) {
		return this.inscMessageDao.getMsgCount(receiver);
	}
}
