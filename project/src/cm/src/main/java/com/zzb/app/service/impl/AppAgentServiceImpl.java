package com.zzb.app.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.app.service.AppAgentService;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.dao.INSBAgentpermissionDao;
import com.zzb.conf.dao.INSBPermissionDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBAgentpermission;

@Service
@Transactional
public class AppAgentServiceImpl extends BaseServiceImpl<INSBAgent> implements
		AppAgentService {
	@Resource
	private INSBAgentDao agentDao;

	@Resource
	private INSBAgentpermissionDao agentpermissionDao;

	@Resource
	private INSBPermissionDao insbpermissionDao;

	@Override
	protected BaseDao<INSBAgent> getBaseDao() {
		return agentDao;
	}

	/*
	 * " { insure_with_common_device": true/false, 是否为普通设备 "user_auth_code":
	 * "授权码", "Attributes": { "uid": "4008528528ABCDEFGHIJKLMNOPQRSTUV", "cn":
	 * "4008528528",工号 "employeeNumber": "4008528528",工号 "sn":
	 * "\u6d4b\u8bd5\u4e13\u7528\u7528\u6237",名 "gn":
	 * "\u6d4b\u8bd5\u4e13\u7528\u7528\u6237",姓 "displayName":
	 * "\u6d4b\u8bd5\u4e13\u7528\u7528\u6237",全名 "mobile": "15914250367",电话号码
	 * "mail": "",邮箱 "initials": "1",性别 "businessCategory": "1",证件类型
	 * "registeredAddress": "44010119900101977X",证件号码 "destinationIndicator":
	 * "1",是否允许所有的设备都能登录 "labeledURI":
	 * "serialNumber=863371010183938,o=473C0F6A75A842959143BDFAE106AB4D,o=C203F2B43C67411E99E4E24A04FD410C,o=organizations,dc=baoxian,dc=com"
	 * , "title": ["PropertyIns"],//可以使用的模块 "l": "0",//是否允许手机登录 "st":
	 * "0"//是否允许网页登录 }, "region": "440100",//城市编码 "org": { "platform":
	 * "1244000000",//所属平台编码 "businessOffice": "1244191004"//所属网点编码 } }
	 */

	@Override
	public Map<String, Object> queryByAgentId(String idType, String uuid) {
		Map<String, Object> result = new HashMap<String, Object>();
		INSBAgent agentModel = agentDao.selectById(uuid);
		List<INSBAgentpermission> apModelList = agentpermissionDao
				.selectByAgentId(uuid);

		Map<String, Object> attributesMap = new HashMap<String, Object>();
		Map<String, Object> orgMap = new HashMap<String, Object>();

		orgMap.put("platform", agentModel.getDeptid());
		orgMap.put("businessOffice", agentModel.getStationid());

		attributesMap.put("uid", agentModel.getId());
		attributesMap.put("cn", agentModel.getAgentcode());
		attributesMap.put("employeeNumber", agentModel.getJobnum());
		attributesMap.put("displayName", agentModel.getName());
		attributesMap.put("mobile", agentModel.getMobile());
		attributesMap.put("mail", "");
		attributesMap.put("initials", agentModel.getSex());
		attributesMap.put("businessCategory", agentModel.getIdnotype());
		attributesMap.put("registeredAddress", agentModel.getIdno());

		// 是否允许所有的设备都能登录
		// attributesMap.put("destinationIndicator",apModel.getDeviceslogin() );

		// 是否允许手机登录
		// attributesMap.put("title", apModel.getPhonelogin());

		// 是否允许网页登录
		// attributesMap.put("st", apModel.getWeblogin());
		// attributesMap.put("st", "");
		/**
		 * 001 所有设备登录 002 续保 003 手机登录 004 投保 005 核保 006 网页登录
		 */
		// 暂定“功能状态”作为判定条件 1为启用，0为停用
		for (INSBAgentpermission model : apModelList) {
			if (model.getFunctionstate() == 1) {
				// 找到权限表id
				try {
					String perId = model.getPermissionid();
					String code = insbpermissionDao.selectById(perId)
							.getPercode();
					if (code.equals("003")) {
						attributesMap.put("title", "1");
					} else {
						attributesMap.put("title", "0");
					}

					if (code.equals("001")) {
						attributesMap.put("destinationIndicator", "1");
					} else {
						attributesMap.put("destinationIndicator", "0");
					}

					if (code.equals("006")) {
						attributesMap.put("st", "1");
					} else {
						attributesMap.put("st", "0");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		result.put("insure_with_common_device", true);
		result.put("user_auth_code", "AAAAAAAAAAAAAAAAA");
		result.put("region", agentModel.getLivingcityid());
		result.put("Attributes", attributesMap);
		result.put("org", orgMap);

		return result;
	}

}