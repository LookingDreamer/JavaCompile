package com.zzb.mobile.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.MsgPram;
import com.zzb.mobile.service.AppBaseDataMsgCenterService;

@Controller
@RequestMapping("/mobile/basedata/msgcenter/*")
public class AppBaseDataMsgCenterController extends BaseController {

	@Resource
	private AppBaseDataMsgCenterService msgCenterService;
	/**
	 * 获取个人所有的消息列表
	 * @param userid -用户id
	 * @return 
	 * @throws ControllerException
	 */
	@RequestMapping(value="/getMessages",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel getMessages(@RequestBody MsgPram msgPram) throws ControllerException{
		Map<Object, Object> hashMap =new HashMap<Object, Object>();
		hashMap.put("receiver",msgPram.getReceiver());
		hashMap.put("offset",msgPram.getOffset());
		hashMap.put("limit",msgPram.getLimit());
		//推送消息标识
		hashMap.put("pushFlag", "1");
		return msgCenterService.getAllMessages(hashMap);
	}
	/**
	 * 删除消息
	 * @param messageID -消息id
	 * @throws ControllerException
	 */
	@RequestMapping(value="/deleteMessage",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel deleteMessage(@RequestBody List<String> messageids) throws ControllerException{
		return msgCenterService.deleteMessage(messageids);
	}
	
	/**
	 * 根据消息ID获取消息对象
	 * @param messageID -消息id
	 * @throws ControllerException
	 */
	@RequestMapping(value="/getMessageById",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel getMessageById(@RequestBody String messageid) throws ControllerException{
		return msgCenterService.getMessageById(messageid);
	}
	/**
	 * 更新消息状态,标记为已读
	 * @param messageID -消息id
	 * @param messageStatus -消息状态，0-未读，1-已读；
	 * @throws ControllerException
	 */
	@RequestMapping(value="/markReaded",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel markReaded(@RequestBody String messageid) throws ControllerException{
		return msgCenterService.markReaded(messageid);
	}
	/**
	 * 更新消息状态，标记为未读
	 * @param messageID -消息id
	 * @param messageStatus -消息状态，0-未读，1-已读；
	 * @throws ControllerException
	 */
	@RequestMapping(value="/markNotReaded",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel markNotReaded(@RequestBody String messageid) throws ControllerException{
		return msgCenterService.markNotReaded(messageid);
	}
	/**
	 * 获取消息数量
	 * @param receiver -用户id
	 * @throws ControllerException
	 */
	@RequestMapping(value="/getNotReadMessageCount",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel getNotReadMessageCount(@RequestBody String receiver) throws ControllerException{
		return msgCenterService.getNotReadMessageCountPush(receiver);
	}
	/**
	 * 获取消息数量
	 * @param receiver -用户id
	 * @throws ControllerException
	 */
	@RequestMapping(value="/getNotReadMessages",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel getNotReadMessages(@RequestBody MsgPram msgPram) throws ControllerException{
		Map<Object, Object> hashMap =new HashMap<Object, Object>();
		hashMap.put("receiver",msgPram.getReceiver());
		hashMap.put("offset",msgPram.getOffset());
		hashMap.put("limit",msgPram.getLimit());
		//推送消息标识
		hashMap.put("pushFlag", "1");
		return msgCenterService.getAllNotReadMessages(hashMap);
	}
}
