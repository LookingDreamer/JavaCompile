package com.cninsure.system.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.cninsure.system.entity.INSCMessage;

public interface INSCMessageService  extends BaseService<INSCMessage>{
	/**
	 * 获取未读消息数量
	 * @param receiver -接收人
	 * @return
	 */
	public int getNotReadMsgCount(String receiver);
	
	/**
	 * 获取所有消息数量
	 * @param receiver
	 * @return
	 */
	public int getMsgCount(String receiver);
	/**
	 * 获取发送消息列表
	 * @param receiver -接收人
	 * @return
	 */
	public List<INSCMessage> getAllReceiveMessages(String receiver);
	/**
	 * 分页获取发送消息列表
	 * @return
	 */
	public Map<String, Object> getReceiveMessagesPaging(Map<String, Object> map,String receiver);
	
	/**
	 * 分页获取发送消息列表
	 * @return
	 */
	public Map<String, Object> getReceiveMessagesPaging(Map<String, Object> map);
	
	/**
	 * 分页获取未读消息列表
	 * @return
	 */
	public Map<String, Object> getNotReadMessagesPaging(Map<String, Object> map);
	
	/**
	 * 获取发送消息列表
	 * @param sender -发送人
	 * @return
	 */
	public List<INSCMessage> getAllSenderMessages(String sender);
	
	/**
	 * 查看消息详情
	 * @param sender -发送人
	 * @return
	 */
	public INSCMessage viewMessage(String id);
	/**
	 * 标记为已读
	 * @param id -消息ID
	 */
	public void markedReaded(String id);
	/**
	 * 标记未未读
	 * @param id —消息ID
	 */
	public void markedNotReaded(String id);
	/*
	 * 
	 */
	public String deleteMessage(String msgid);
}
