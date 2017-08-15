package com.zzb.mobile.service;

import java.util.List;
import java.util.Map;

import com.zzb.mobile.model.CommonModel;


public interface AppBaseDataMsgCenterService {
	/**
	 * 获取个人所有的消息列表
	 * @param map -参数对象
	 * @return
	 */
	public CommonModel getAllMessages(Map map);
	/**
	 * 获取个人所有的消息列表
	 * @param map -参数对象
	 * @return
	 */
	public CommonModel getAllNotReadMessages(Map map);
	/**
	 * 更新消息状态
	 * @param messageid -消息id
	 * @param status -消息状态，0-未读，1-已读；
	 * @return
	 */
	public CommonModel updateMessageStatus(String messageid,String status);
	/**
	 * 标记消息已读
	 * @param messageid
	 * @return
	 */
	public CommonModel markReaded(String messageid);
	/**
	 * 标记消息未读
	 * @param messageid
	 * @return
	 */
	public CommonModel markNotReaded(String messageid);
	/**
	 * 删除消息
	 * @param messageid -消息id
	 */
	public CommonModel deleteMessage(List<String> messageids);
	/**
	 * 获取未读消息数量
	 * @param receiver -接受者
	 */
	public CommonModel getNotReadMessageCount(String receiver);
	
	/**
	 * 获取未读消息数量
	 * @param receiver -接受者
	 */
	public CommonModel getMessageById(String messageid);


    CommonModel getNotReadMessageCountPush(String receiver);
}
