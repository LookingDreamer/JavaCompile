package com.zzb.app.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.LogUtil;
import com.zzb.mobile.service.UserCenterService;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCDeptService;
import com.common.redis.Constants;
import com.common.redis.IRedisClient;
import com.zzb.app.model.AppRegistModel;
import com.zzb.app.service.AppRegistService;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.mobile.service.SMSClientService;

@Service
@Transactional
public class AppRegistServiceImpl implements AppRegistService {

    public static final String AGENTUSERID = "cm:zzb:register:agent_user_id";
    public static final String TOKENID = "cm:zzb:register:token_id";
    @Resource
	private INSBAgentService insbAgentService;
	@Resource
	private INSCDeptService inscDeptService;
	@Resource
	private INSCDeptDao inscDeptDao;
	@Resource
	private SMSClientService smsClientService;

	@Resource
	private IRedisClient redisClient;

	@Resource
	private UserCenterService userCenterService;

	/**
	 * 找回密码--发送验证码
	 */
	@Override
	public String findpws(String account) {
		Map<Object, Object> responseMap = new HashMap<Object, Object>();
		try {
			INSBAgent user1 = new INSBAgent();// 工号登陆 验证参数
			user1.setJobnum(account);
			INSBAgent user2 = new INSBAgent();// 手机号登陆 验证参数
			user2.setPhone(account);
			user2.setMobile(account);
			INSBAgent agent1 = insbAgentService.queryOne(user1);// 工号登陆 验证
			INSBAgent agent2 = insbAgentService.queryOne(user2);// 手机号登陆 验证

			if (agent1 != null || agent2 != null) {
				INSBAgent agentuser = new INSBAgent();

				if (agent1 != null) {
					agentuser = agent1;
				} else if (agent2 != null) {
					agentuser = agent2;
				}

				// 生成6位随机数 并发送 （目前是 123456）
				String phone=agentuser.getPhone(); //代理人用户 手机
				String validateCode = AppRegistServiceImpl.getStr(); // 生成6位随机数

				/* 给phone发送验证码random */

				smsClientService.sendMobileValidateCode(phone, validateCode);

				/* 生成token 放入redis */
				String userId = String.valueOf(agentuser.getId());
				String token = userId + "_" + String.valueOf(new Date());

				try {
					redisClient.set(AGENTUSERID, agentuser.getId(), agentuser,24*60*60); // key:
																			// 代理人id
																			// value:代理人对象
					redisClient.set(TOKENID, agentuser.getId(), token,24*60*60); // key: tokenid+代理人id value：token
					redisClient.set(Constants.VALIDATE_CODE, agentuser.getId(), validateCode,24*60*60); // 6位随机数
																				// 放入redis
				} catch (Exception e) {
					e.printStackTrace();
				}

				responseMap.put("status", "success");
				responseMap.put("Message",
						"Modify password verification code verification");
				responseMap.put("jobnum", agentuser.getJobnum());
				responseMap.put("userid", agentuser.getId());
				responseMap.put("token", token);
			} else {
				responseMap.put("status", "fail");
				responseMap
						.put("Message",
								"Modify password verification code verification failed");
			}
		} catch (Exception e) {
			e.printStackTrace();
			responseMap.put("status", "fail");
			responseMap.put("Message", "Program exception!");
		}

		JSONObject jsonObject = JSONObject.fromObject(responseMap);
		return jsonObject.toString();
	}

	/**
	 * 校验忘记密码验证码(身份证)
	 */
	@Override
	public String validatorFindPws(String phone, String code, String idno) {
		Map<Object, Object> responseMap = new HashMap<Object, Object>();
		try {
			INSBAgent user = new INSBAgent();// 手机号登陆 验证参数
			user.setPhone(phone);
			user.setMobile(phone);
			INSBAgent agent = insbAgentService.queryOne(user);// 手机号登陆 验证

			String userIdno = agent.getIdno(); // 代理人用户 身份证号

			String userid = agent.getId();
			String validateCode = (String) redisClient
					.get(Constants.VALIDATE_CODE, userid);

			if (validateCode.equals(code) && userIdno.equals(idno)) {
				responseMap.put("status", "success");
				responseMap
						.put("Message",
								"Forget password verification code verification successful");
			} else {
				responseMap.put("status", "fail");
				responseMap.put("Message",
						"Verification code or identity card is not correct!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			responseMap.put("status", "fail");
			responseMap.put("Message", "Program exception!");
		}

		JSONObject jsonObject = JSONObject.fromObject(responseMap);
		return jsonObject.toString();
	}

	/**
	 * 修改代理人登录密码
	 */
	@Override
	public String updatepwd(String phone, String newpwd) {
		Map<Object, Object> responseMap = new HashMap<Object, Object>();

		try {
			INSBAgent user = new INSBAgent();// 手机号登陆 验证参数
			user.setPhone(phone);
			user.setMobile(phone);
			INSBAgent agent = insbAgentService.queryOne(user);// 手机号登陆 验证
			String oldPwd = agent.getPwd();
			agent.setPwd(newpwd);
			LogUtil.info("updatepwd 修改代理人密码 代理人："+agent.toString()+" 操作人:"+phone+" 操作时间:"+ DateUtil.getCurrentDateTime());
//			int i = insbAgentService.updateById(agent);
			int i = insbAgentService.updateAgentPwd(agent, oldPwd);//修改密码并发短信通知代理人
			if (i > 0) {
				responseMap.put("status", "success");
				responseMap.put("Message", "Password modification success");
				/**
				 * 通知集团统一用户中心 hwc 添加 20170602
				 */
				try {
					Map<String, String> params = new HashMap<>();
					params.put("omobile", phone);
					params.put("nmobile","");
					params.put("cardno", agent.getIdno());
					params.put("agentcode", agent.getAgentcode());
					params.put("password",newpwd);
					params.put("passwordflag","1");
					userCenterService.updateUserAccount(params);
				}catch (Exception e){
					LogUtil.info("修改密码通知集团统一用户中心失败"+e.getMessage());
					e.printStackTrace();
				}
			} else {
				responseMap.put("status", "fail");
				responseMap.put("Message", "Password change failed");
			}
		} catch (Exception e) {
			e.printStackTrace();
			responseMap.put("status", "fail");
			responseMap.put("Message", "Program exception!");
		}

		JSONObject jsonObject = JSONObject.fromObject(responseMap);
		return jsonObject.toString();
	}

	/**
	 * 注册--发送手机验证码 同时手机号重复性校验
	 */
	@Override
	public String RegistvalidationCode(String phoneNum) {
		Map<Object, Object> responseMap = new HashMap<Object, Object>();

		INSBAgent user = new INSBAgent();// 手机号验证参数
		user.setPhone(phoneNum);
		user.setMobile(phoneNum);
		INSBAgent agent = insbAgentService.queryOne(user);

		// 判断手机号是否已存在
		if (agent == null) {
			String validateCode = AppRegistServiceImpl.getStr(); // 生成6位随机数
			try {
				redisClient.set(Constants.VALIDATE_CODE, user.getId(), validateCode, 24*60*60); // 验证码放入redis
			} catch (Exception e) {
				e.printStackTrace();
			}

			/*
			 * 给phone发送验证码validateCode
			 */
			try {
				smsClientService.sendMobileValidateCode(phoneNum, validateCode);
				
				responseMap.put("status", "success");
				responseMap.put("userId", null);
				responseMap.put("err", validateCode); // 返回的验证码
				responseMap.put("Message",
						"Registration verification code sent successfully");
			} catch (Exception e1) {
				e1.printStackTrace();
				responseMap.put("status", "fail");
				responseMap.put("userId", null);
				responseMap.put("Message",
						"Registration verification code send failed");
			}
		} else {
			responseMap.put("status", "fail");
			responseMap.put("userId", null);
			responseMap.put("Message",
					"Registration verification code send failed");
		}

		JSONObject jsonObject = JSONObject.fromObject(responseMap);
		return jsonObject.toString();
	}

	/**
	 * 注册--提交注册信息
	 */
	@Override
	public String register(AppRegistModel registModel) {
		Map<Object, Object> responseMap = new HashMap<Object, Object>();

		/* 手机号重复性校验 */
		INSBAgent agentVphone = new INSBAgent();
		agentVphone.setPhone(registModel.getPhone());
		agentVphone.setMobile(registModel.getPhone());
		INSBAgent phoneVagent = insbAgentService.queryOne(agentVphone); // 手机号是否已存在
		if (phoneVagent == null) {
			/*
			 * 判断 注册输入的地区，是否有使用机构。有：注册；无：终止注册
			 */
			INSCDept inscDeptTemp = new INSCDept();
			inscDeptTemp.setTrydept("0"); // "0" 表示是使用地区
			inscDeptTemp.setCounty(registModel.getCountyCode());
			List<INSCDept> tryDeptList = inscDeptDao.selectList(inscDeptTemp);
			if (tryDeptList != null) {
				INSCUser user = new INSCUser();
				INSBAgent insbAgent = new INSBAgent();
				insbAgent.setPhone(registModel.getPhone()); // 电话
				insbAgent.setMobile(registModel.getPhone()); 
				insbAgent.setReferrer(registModel.getReadNum()); // 推荐人工号
				insbAgent.setLivingcityid(registModel.getCountyCode()); // 所在区域区级code
				insbAgent.setPwd(registModel.getPassWord()); // 密码
				insbAgent.setNoti(registModel.getRemark()); // 备注
				insbAgent.setTempjobnum("0"); // 设置是否是使用账号 "0":是
				String id = insbAgentService.saveOrUpdateAgent(user, insbAgent);
				if (id != null) {
					String tempjobnum = insbAgentService.queryById(id)
							.getTempjobnum();
					responseMap.put("status", "success");
					responseMap.put("Message","Registration success, generate trial jobnum");
					responseMap.put("tempjobnum", tempjobnum);
				} else {
					responseMap.put("status", "fail");
					responseMap.put("Message", "Registration failure");
				}
			} else if (tryDeptList == null) {
				responseMap.put("status", "fail");
				responseMap.put("Message", "The area is not a trial area");
			}
		} else {
			responseMap.put("status", "fail");
			responseMap.put("Message", "Phone number has been registered");
		}

		JSONObject jsonObject = JSONObject.fromObject(responseMap);
		return jsonObject.toString();
	}

	/**
	 * 绑定工号
	 */
	@Override
	public String BindingJobNum(String jobNumOrIdCard, String jobNumPassword) {
//		Map<Object, Object> responseMap = new HashMap<Object, Object>();
//
//		INSBAgent agent1 = new INSBAgent();
//		agent1.setJobnum(jobNumOrIdCard);
//		agent1.setPwd(jobNumPassword);
//		INSBAgent agent2 = new INSBAgent();
//		agent2.setIdno(jobNumOrIdCard);
//		agent2.setPwd(jobNumPassword);
//		INSBAgent insbAgent1 = insbAgentService.queryOne(agent1);
//		INSBAgent insbAgent2 = insbAgentService.queryOne(agent2);
//
//		if (insbAgent1 != null || insbAgent2 != null) { // cm数据库中存在已有工号
//
//			responseMap.put("status", "fail");
//			responseMap.put("message", "jobnum already exists");
//			
//			/*
//			 * 问题：在cm数据库中查到 已有工号，做什么处理。 
//			 * 是否需要把数据库中代理人的两条工号记录关联起来， 
//			 * 表示是同一个代理人的两个账号
//			 */
//			/*INSBAgent agent = new INSBAgent();
//			if (insbAgent1 != null) {
//				agent = insbAgent1;
//			} else if (insbAgent2 != null) {
//				agent = insbAgent2;
//			}
//
//			if (agent.getJobnumtype() != 1) {
//				agent.setJobnumtype(1); // "1" 表示正式工号
//				responseMap.put("status", "success");
//				responseMap.put("Message", "Binding success");
//			} else if (agent.getJobnumtype() == 1) {
//				responseMap.put("status", "fail");
//				responseMap.put("Message", "Binding failure");
//			}*/
//
//		} else if (insbAgent1 == null && insbAgent2 == null) { // cm数据库中没有已有工号
//			String jobNum = "jobnum=" + jobNumOrIdCard;
//			String idno = "idno=" + jobNumOrIdCard;
//			List<Map<String, String>> list1 = LdapNode.getLdapDlr(jobNum); // cm库不存在已有工号，到LDAP中查找
//			List<Map<String, String>> list2 = LdapNode.getLdapDlr(idno);
//
//			List<Map<String, String>> tempAgent = new ArrayList<Map<String, String>>();
//			if (list1 != null) {
//				tempAgent = list1;
//			} else if (list2 != null) {
//				tempAgent = list2;
//			} else {
//				tempAgent = null;
//			}
//
//			if (tempAgent != null) {
//				String pwd = tempAgent.get(0).get("pwd");
//				if (pwd.equals(jobNumPassword)) {
//
//					INSBAgent agentPhone = new INSBAgent();
//					agentPhone.setPhone(tempAgent.get(0).get("mobile"));
//					INSBAgent agentid=insbAgentService.queryOne(agentPhone);
//					
//					
//					INSBAgent insertAgent = new INSBAgent();
//					insertAgent.setId(agentid.getId()); //获取临时工号的id，根据id更新
//					insertAgent.setName(tempAgent.get(0).get("name"));
//					insertAgent.setJobnum(tempAgent.get(0).get("jobnum"));
//					insertAgent.setAgentcode(tempAgent.get(0).get("agentcode"));
//					insertAgent.setAgentlevel(Integer.valueOf(tempAgent.get(0).get("agentlevel")));
//					insertAgent.setPwd(tempAgent.get(0).get("pwd"));
//					insertAgent.setIdno(tempAgent.get(0).get("idno"));
//					insertAgent.setIdnotype(tempAgent.get(0).get("idnotype"));
////					insertAgent.setMobile(tempAgent.get(0).get("mobile"));
//					insertAgent.setTempjobnum("1"); // 设置是否是使用账号 "0":使用,"1":正式
//					INSCUser user = new INSCUser();
//					insbAgentService.saveOrUpdateAgent(user,insertAgent);
//
//					responseMap.put("status", "success");
//					responseMap.put("message", "Copy LDAP to CM");
//				} else {
//					responseMap.put("status", "fail");
//					responseMap.put("message", "password error");
//				}
//			} else {
//				responseMap.put("status", "fail");
//				responseMap.put("message", "LDAP can't find the number.");
//			}
//		}

		return null;
	}

	/**
	 * 首页 欢迎姓名
	 */
	@Override
	public String getAgentName(String token) {
		Map<Object, Object> responseMap = new HashMap<Object, Object>();

		String name;
		try {
			String userid = AppRegistServiceImpl.analysisToken(token);
			INSBAgent agent = new INSBAgent();
			agent.setId(userid);
			INSBAgent insbAgent = insbAgentService.queryOne(agent);
			if (insbAgent != null) {
				name = insbAgent.getName();
				responseMap.put("status", "success");
				responseMap.put("message", "success");
				responseMap.put("name", name);
			} else {
				responseMap.put("status", "fail");
				responseMap.put("message", "agent is not found");
			}
		} catch (Exception e) {
			responseMap.put("status", "fail");
			responseMap.put("message", "Program exception!");
			e.printStackTrace();
		}

		JSONObject jsonObject = JSONObject.fromObject(responseMap);
		return jsonObject.toString();
	}

	/**
	 * 随机生成6位数
	 */
	// private final static String[] str = { "0", "1", "2", "3", "4", "5", "6",
	// "7", "8","9"};
	public static String getStr() {
		String s = "123456";
		/*
		 * String s = ""; for (int i = 0; i < 6; i++) { int
		 * a=(int)(Math.random()*10); s+=str[a]; }
		 */
		return s;
	}

	/**
	 * 分解token 获取userid
	 */
	public static String analysisToken(String token) throws Exception {
		String userId = token.split("_")[0];
		return userId;
	}

}