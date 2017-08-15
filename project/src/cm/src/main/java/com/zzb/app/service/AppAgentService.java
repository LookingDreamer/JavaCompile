package com.zzb.app.service;

import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBAgent;

public interface AppAgentService extends BaseService<INSBAgent>{
	
	/**
	 * 
	 * TODO  部分字段未确定
	 * 通过id查询代理人信息
	 * 
	 * @param idType
	 * @param uuid
	 * @return
	 * 
	 * { insure_with_common_device": true/false, 是否为普通设备
	 * "user_auth_code": "授权码",
	 * "Attributes": { "uid": "4008528528ABCDEFGHIJKLMNOPQRSTUV",
	 * 					"cn": "4008528528",工号 
	 * 					"employeeNumber": "4008528528",工号 
	 * 					"sn": "\u6d4b\u8bd5\u4e13\u7528\u7528\u6237",名 
	 * 					"gn": "\u6d4b\u8bd5\u4e13\u7528\u7528\u6237",姓 
	 * 					"displayName": "\u6d4b\u8bd5\u4e13\u7528\u7528\u6237",全名 
	 * 					"mobile": "15914250367",电话号码
	 * 					"mail": "",邮箱 
	 * 					"initials": "1",性别
	 * 					"businessCategory": "1",证件类型
	 * 					"registeredAddress": "44010119900101977X",证件号码 
	 * 					"destinationIndicator": "1",是否允许所有的设备都能登录 
	 * 					"labeledURI": "serialNumber=863371010183938,o=473C0F6A75A842959143BDFAE106AB4D,o=C203F2B43C67411E99E4E24A04FD410C,o=organizations,dc=baoxian,dc=com",
	 * 					"title": ["PropertyIns"],//可以使用的模块 "l": "0",//是否允许手机登录 
	 * 					"st": "0"//是否允许网页登录 
	 * 				}, 
	 * "region": "440100",//城市编码 
	 * "org": { "platform": "1244000000",//所属平台编码 
	 * 			"businessOffice": "1244191004"//所属网点编码 
	 * 		} 
	 * }
	 */
	public Map<String,Object> queryByAgentId(String idType,String uuid);

}
