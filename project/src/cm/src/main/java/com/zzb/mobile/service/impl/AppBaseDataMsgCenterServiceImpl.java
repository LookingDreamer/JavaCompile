package com.zzb.mobile.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.dao.INSCMessageDao;
import com.cninsure.system.entity.INSCMessage;
import com.cninsure.system.service.INSCMessageService;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.service.AppBaseDataMsgCenterService;
@Service
@Transactional
public class AppBaseDataMsgCenterServiceImpl implements
		AppBaseDataMsgCenterService {
	@Resource
	private INSCMessageService inscMessageServicve;
	
	@Resource
	private INSCMessageDao messageDao;

	@Override
	public CommonModel getAllMessages(Map map) {
		CommonModel commonModel = new CommonModel();
		//20160804 添加判断 如果没有用户数据 不进行查询
		if(StringUtil.isEmpty(map.get("receiver"))){
			commonModel.setStatus("fail");
			commonModel.setMessage("Get all messages fail");
		}else{
			Map<String,Object> map2 = inscMessageServicve.getReceiveMessagesPaging(map);
			if(map2==null){
				commonModel.setStatus("fail");
				commonModel.setMessage("Get all messages fail");
			}else{
				commonModel.setStatus("success");
				commonModel.setMessage("Get all messages success");
				commonModel.setBody(map2);
			}
		}
		return commonModel;
	}

	@Override
	public CommonModel updateMessageStatus(String messageid, String status) {
		CommonModel commonModel = new CommonModel();
		String success="0";
		if("0".equals(status)){
			messageDao.updateNotReadById(messageid);
			success="1";
		}
		if("1".equals(status)){
			Map map =new HashMap();
			map.put("id",messageid );
			map.put("readtime", DateUtil.getCurrentDateTime());
			messageDao.updateReadedById(map);
			success="1";
		}
		if(success.equals("0")){
			commonModel.setStatus("fail");
			commonModel.setMessage("Update messages fail");
		}else{
			commonModel.setStatus("success");
			commonModel.setMessage("Update messages success");
			
		}
		commonModel.setBody(success);
		return commonModel;
	}

	@Override
	public CommonModel deleteMessage(List<String> messageids) {
		CommonModel commonModel = new CommonModel();
		for(String messageid:messageids){
			messageDao.deleteById(messageid);
		}
		commonModel.setStatus("success");
		commonModel.setMessage("delete messages success");
		return commonModel;
	}

	@Override
	public CommonModel markReaded(String messageid) {
		CommonModel commonModel = new CommonModel();
		inscMessageServicve.markedReaded(messageid);
		commonModel.setStatus("success");
		commonModel.setMessage("marked messages success");
		return commonModel;
	}

	@Override
	public CommonModel markNotReaded(String messageid) {
		CommonModel commonModel = new CommonModel();
		inscMessageServicve.markedNotReaded(messageid);
		commonModel.setStatus("success");
		commonModel.setMessage("marked messages success");
		return commonModel;
	}

	@Override
	public CommonModel getNotReadMessageCount(String receiver) {
		CommonModel commonModel = new CommonModel();
		int count = messageDao.getNotReadMsgCount(receiver);
		commonModel.setStatus("success");
		commonModel.setMessage("get count success");
		HashMap<Object,Object> map =new HashMap<Object,Object>();
		map.put("count", count);
		commonModel.setBody(map);
		return commonModel;
	}

	@Override
	public CommonModel getAllNotReadMessages(Map map) {
		CommonModel commonModel = new CommonModel();
		Map<String,Object> map2 = inscMessageServicve.getNotReadMessagesPaging(map);
		//List<Map<Object,Object>> list = messageDao.getAllByReceiverMap(userid);
		if(map2==null){
			commonModel.setStatus("fail");
			commonModel.setMessage("Get all messages fail");
		}else{
			commonModel.setStatus("success");
			commonModel.setMessage("Get all messages success");
			commonModel.setBody(map2);
		}
		return commonModel;
	}

	@Override
	public CommonModel getMessageById(String messageid) {
		CommonModel commonModel = new CommonModel();
		INSCMessage message = inscMessageServicve.queryById(messageid);
		//List<Map<Object,Object>> list = messageDao.getAllByReceiverMap(userid);
		if(message==null){
			commonModel.setStatus("fail");
			commonModel.setMessage("Get all messages fail");
		}else{
			commonModel.setStatus("success");
			commonModel.setMessage("Get all messages success");
			commonModel.setBody(message);
		}
		return commonModel;
	}

	@Override
	public CommonModel getNotReadMessageCountPush(String receiver) {
		CommonModel commonModel = new CommonModel();
		int count = messageDao.getNotReadMsgCountPush(receiver);
		commonModel.setStatus("success");
		commonModel.setMessage("get count success");
		HashMap<Object,Object> map =new HashMap<Object,Object>();
		map.put("count", count);
		commonModel.setBody(map);
		return commonModel;
	}
}
