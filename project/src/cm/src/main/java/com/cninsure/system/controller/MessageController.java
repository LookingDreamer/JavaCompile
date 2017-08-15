package com.cninsure.system.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.system.entity.INSCMessage;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCMessageService;
import com.common.PagingParams;
import com.zzb.cm.controller.vo.MediumPaymentVo;
import com.zzb.cm.controller.vo.NotReadMessageVo;
import com.zzb.mobile.model.CommonModel;

@Controller
@RequestMapping("/message/*")

public class MessageController  extends BaseController {
	@Resource
	private INSCMessageService inscMessageServicve;
	
	@RequestMapping(value = "getNotReadCount",method= RequestMethod.GET)
	@ResponseBody
	public String getNotReadMsgCount(@RequestParam(value = "receiver", defaultValue = "") String receiver){
		int count = this.inscMessageServicve.getNotReadMsgCount(receiver);
		return JSONArray.fromObject(count).toString();
	}
	
	@RequestMapping(value = "getMsgCount",method= RequestMethod.GET)
	@ResponseBody
	public String getMsgCount(@RequestParam(value = "receiver", defaultValue = "") String receiver){
		int count = this.inscMessageServicve.getMsgCount(receiver);
		return JSONArray.fromObject(count).toString();
	}
	
	@RequestMapping(value = "listNotReadMessages",method= RequestMethod.GET)
	public ModelAndView listNotReadMessages(@RequestParam(value = "receiver", defaultValue = "") String receiver){
		List<INSCMessage> msgs = this.inscMessageServicve.getAllReceiveMessages(receiver);
		ModelAndView mav = new ModelAndView("system/usermessage");
		mav.addObject("receiver", receiver);
		return mav;
	}
	
	@RequestMapping(value = "viewMessage",method= RequestMethod.GET)
	public ModelAndView viewMessage(@RequestParam("msgId") String msgId){
		INSCMessage messageDetail = inscMessageServicve.viewMessage(msgId);
		ModelAndView mav = new ModelAndView("system/usermessagedetail");
		mav.addObject("messageDetail", messageDetail);
		return mav;
	}
	
	@RequestMapping(value = "listNotReadMessagesPaging", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> listNotReadMessagesPaging(@ModelAttribute PagingParams para,
			@RequestParam(value = "receiver", defaultValue = "") String receiver,HttpSession session)
			throws ControllerException {
		receiver=((INSCUser)session.getAttribute("insc_user")).getUsercode(); 
		Map<String, Object> map = BeanUtils.toMap(receiver,para);
		map.put("receiver", receiver);
		map.put("status", 0);
		map = inscMessageServicve.getReceiveMessagesPaging(map,receiver);
		return map;
	}
	
	@RequestMapping(value = "listAllMessagesPaging", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> listAllMessagesPaging(@ModelAttribute PagingParams para,
			@RequestParam(value = "receiver", defaultValue = "") String receiver,HttpSession session)
			throws ControllerException {
		receiver=((INSCUser)session.getAttribute("insc_user")).getUsercode(); 
		Map<String, Object> map = BeanUtils.toMap(receiver,para);
		map.put("receiver", receiver);
		map.put("status", null);
		map = inscMessageServicve.getReceiveMessagesPaging(map,receiver);
		int count = this.inscMessageServicve.getMsgCount(receiver);
		map.put("nocount", count);
		return map;
	}
	
	
	/*
	 * 保存未读信息，从openfire服务器端获取的数据
	 */
	@RequestMapping(value = "saveNewNotReadMessage", method = RequestMethod.POST)
	@ResponseBody
	public String saveNewNotReadMessage(@RequestBody NotReadMessageVo notReadMessageVo){
		System.out.println("saveNewNotReadMessage--------"+notReadMessageVo.getContent()+","+notReadMessageVo.getReciever()+","+notReadMessageVo.getFromPeople()+","+notReadMessageVo.getDateTime());
		try{
			INSCMessage message = new INSCMessage();
			message.setMsgcontent(notReadMessageVo.getContent());  //内容
			message.setMsgtitle(notReadMessageVo.getTitle());      //标题
			String receiver = notReadMessageVo.getReciever();
			String[] str = receiver.split("@");
			message.setReceiver(str[0]);                           //接收者
			String sender = notReadMessageVo.getFromPeople();
			str = sender.split("@");
			message.setSender(str[0]);   //发送者
			//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			message.setSendtime(notReadMessageVo.getDateTime());   //发送时间
			message.setOperator("");                               //操作员
			message.setStatus(0);                                   //状态0-未读，1-已读，默认设置为未读
			message.setState(1);                                   //接收-1，发送-0; 代表接收的这条消息
			inscMessageServicve.insert(message);
			return "success";
		}catch(Exception e){
			e.printStackTrace();
			return "fail";
		}
		
	}
	
	@RequestMapping(value="/deleteMessage",method=RequestMethod.GET)
	@ResponseBody
	public String deleteMessage(String messageid) throws ControllerException{
		return inscMessageServicve.deleteMessage(messageid);
	}
	
}
