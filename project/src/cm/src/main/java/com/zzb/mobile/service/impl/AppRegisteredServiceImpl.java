package com.zzb.mobile.service.impl;

import com.cninsure.core.utils.DateUtil;
import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCUser;
import com.common.JsonUtil;
import com.common.redis.Constants;
import com.common.redis.IRedisClient;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zzb.conf.entity.*;
import com.zzb.conf.service.*;
import com.zzb.mobile.model.usercenter.UserCenterReturnModel;
import com.zzb.mobile.service.*;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cninsure.core.tools.util.ValidateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.jobpool.Task;
import com.cninsure.jobpool.dispatch.DispatchTaskService;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.service.INSCCodeService;
import com.cninsure.system.service.INSCDeptService;
import com.common.ConfigUtil;
import com.common.HttpClientUtil;
import com.zzb.cm.dao.INSBFilebusinessDao;
import com.zzb.cm.dao.INSBFilelibraryDao;
import com.zzb.cm.entity.INSBCorecodemap;
import com.zzb.cm.entity.INSBFilebusiness;
import com.zzb.cm.service.INSBCorecodemapService;
import com.zzb.cm.service.INSBFilelibraryService;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.dao.OFUserDao;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.util.EncodeUtils;

import net.sf.json.JSONObject;



@Service
@Transactional
public class AppRegisteredServiceImpl implements AppRegisteredService {

	public static final String MODULE = "cm:zzb:registered:validate_code_image";
	@Resource
	private INSBAgentService insbAgentService;
	@Resource
	private INSCDeptService inscDeptService;
	@Resource
	private INSBPermissionsetService INSBPermissionsetService;
	@Resource
	private INSBFilelibraryService insbFilelibraryService;
	@Resource
	private INSBCertificationService insbCertificationService;
	@Resource
	private INSCDeptDao inscDeptDao;
	@Resource
	private INSBAgentDao insbAgentDao;
	@Resource
	private OFUserDao ofUserDao;

	@Resource
	private AppLoginService appLoginService;

	@Resource
	private INSBFilebusinessDao insbFilebusinessDao;
	@Resource
	private INSBCorecodemapService insbCorecodemapService;
	@Resource
	private SMSClientService smsClientService;
	@Resource
	private INSBRegionService insbRegionService;
	@Resource
	private INSBAgentpermissionService insbAgentpermissionService;

	@Resource
	private IRedisClient redisClient;

	private String imageActionPath;

	@Resource
	private INSCCodeDao inscCodeDao;

	@Resource
	private UserCenterService userCenterService;

	@Override // 68310051b96928a88a86a384f0584952 uuid
	// f2311b927e15f91729960907faa1db8 2772
	public CommonModel sendValidateCode(HttpServletRequest request, String phoneNo, String uuid, String code) {
		CommonModel commonModel = new CommonModel();
		Map<Object, Object> responseMap = new HashMap<Object, Object>();
		// 验证图片验证码
		CommonModel model = validateCodeImg(request, uuid, code);
		responseMap.put("validateCodeImg", model);
		// 如果图片验证码失败就不发送手机验证码
		if (model.getStatus().equals("fail")) {
			commonModel.setStatus("fail");
			commonModel.setMessage("请输入正确的图形验证码");
			return commonModel;
		}
		// 发送手机验证码
		// LdapMgr ldap =new LdapMgr();
		// LdapAgentModel agentModel =ldap.searchAgent(phoneNo);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("phone",phoneNo);
		map.put("mobile",phoneNo);
		List<INSBAgent> tempAgentList = insbAgentDao.selectNotminiAgentByMap(map);

		// 判断手机号是否已存在
		if (tempAgentList!= null && tempAgentList.size() == 1) {
			String token = request.getHeader("token");
			if (StringUtil.isNotEmpty(token)) {
				Map<String, Object> obj = redisClient.get(Constants.TOKEN, token, Map.class);
				if (obj != null) {
					String phone = (String)obj.get("phone");
					LogUtil.info("sendValidateCode phone" + phone);
					if (StringUtil.isNotEmpty(phoneNo) && !phoneNo.equals(phone)) {
						commonModel.setStatus("fail");
						commonModel.setMessage("手机号已存在");
						return commonModel;
					}
				} else {
					LogUtil.info("sendValidateCode redisClient token is null");
					commonModel.setStatus("fail");
					commonModel.setMessage("手机号已存在");
					return commonModel;
				}
			} else {
				LogUtil.info("sendValidateCode token is null");
				commonModel.setStatus("fail");
				commonModel.setMessage("手机号已存在");
				return commonModel;
			}
		} else if (tempAgentList.size() > 1){
			commonModel.setStatus("fail");
			commonModel.setMessage("手机号已存在");
			return commonModel;
		}

		String validateCode = EncodeUtils.generateValidateCode(6);
		String createTime = String.valueOf(System.currentTimeMillis());
		try {
			redisClient.set(Constants.PHONE, phoneNo, validateCode + "_" + createTime,5*60); // 验证码放入redis
		} catch (Exception e) {
			e.printStackTrace();
		}
			/*
			 * 给phone发送验证码validateCode
			 */
		try {
			smsClientService.sendMobileValidateCode(phoneNo, validateCode);

			commonModel.setStatus("success");
			commonModel.setMessage("验证码发送成功");
			//responseMap.put("code", validateCode);
			responseMap.put("phone", phoneNo);
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("手机短信验证码发送失败");
		}
		commonModel.setBody(responseMap);

		return commonModel;

	}

	@Autowired
	private AppMarketingActivitiesService appMarketingActivitiesService;

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public CommonModel submitRegInfo(String regInfoJSON) {

		CommonModel commonModel = new CommonModel();
		INSBAgent user = new INSBAgent();
		JSONObject jsonObject = JSONObject.fromObject(regInfoJSON);
		String name = jsonObject.getString("name");
		String phone = jsonObject.getString("phone");
		String passWord = jsonObject.getString("passWord");
		String referrerJobNum = "";
		if (jsonObject.containsKey("refNum") && !StringUtils.isEmpty(jsonObject.getString("refNum"))) {
			referrerJobNum = jsonObject.getString("refNum");
			Map<String, Object> referrermap = new  HashMap<String, Object>();
			referrermap.put("agentcode", referrerJobNum);
			referrermap.put("agentkind", 2);//用户种类 1-试用  2-正式  3-渠道
			List<INSBAgent> referrers = insbAgentDao.selectReferrer(referrermap);
			referrermap.put("agentkind", 1);//用户种类 1-试用  2-正式  3-渠道
			List<INSBAgent> referrers1 = insbAgentDao.selectReferrer(referrermap);
			if (referrers.isEmpty() && referrers1.isEmpty()) {
				commonModel.setStatus(CommonModel.STATUS_FAIL);
				commonModel.setMessage("推荐人不存在！");
				return commonModel;
			}
			if (!referrers.isEmpty()) {
				referrerJobNum = referrers.get(0).getJobnum();
			}
			if (referrers.isEmpty() && !referrers1.isEmpty()) {
				referrerJobNum = referrers1.get(0).getJobnum();
			}
		}

		String openid = null;
		if (jsonObject.containsKey("openid") && !StringUtils.isEmpty(jsonObject.getString("openid")))
			openid = jsonObject.getString("openid");

		Map<String, Object> querymap = new HashMap<String, Object>();
		querymap.put("phone",phone);
		querymap.put("mobile",phone);
		List<INSBAgent> tempAgentList = insbAgentDao.selectNotminiAgentByMap(querymap);

		// 判断手机号是否已存在
		if (tempAgentList.size() > 0) {
			commonModel.setStatus("fail");
			commonModel.setMessage("手机号已存在");
			return commonModel;
		}
		if (jsonObject.containsKey("usersource") && StringUtil.isNotEmpty(jsonObject.getString("usersource"))) {
			user.setUsersource(jsonObject.getString("usersource"));
		} else {
			commonModel.setStatus(CommonModel.STATUS_FAIL);
			commonModel.setMessage("非法注册用户来源！");
			return commonModel;
		}
		user.setName(name);
		user.setPhone(phone);
		user.setMobile(phone);
		user.setPwd(StringUtil.md5Base64(passWord));
		user.setOpenid(openid);

		/*
		 * //根据工号查询推荐人 iNSBAgent = new INSBAgent();
		 * iNSBAgent.setJobnum(referrerJobNum); INSBAgent referrer =
		 * insbAgentDao.selectByJobnum(referrerJobNum); if(referrer==null){
		 * commonModel.setStatus("fail"); commonModel.setMessage(
		 * "Registration failed"); commonModel.setBody("Referrer does not exist"
		 * ); return commonModel; }
		 */ // 100000031 123456
		user.setChannelid(referrerJobNum);
		user.setReferrer(referrerJobNum);
		;
		user.setJobnumtype(1);
		user.setAgentstatus(1);
		user.setApprovesstate(1);
		user.setAgentkind(1);
		user.setIstest(Integer.valueOf(2));
		user.setSex("0");
		// 根据省、市、区 ，查询代理机构
		INSCDept dept = new INSCDept();
		// 如果推荐人工号不为空，则注册用户的代理机构为推荐人所在的代理机构，
		// 否则根据注册人的地区编码查询代理机构
		if (StringUtil.isNotEmpty(referrerJobNum)) {
			INSBAgent referAgent = insbAgentDao.selectByJobnum(referrerJobNum);
			if (referAgent != null && StringUtil.isNotEmpty(referAgent.getDeptid())) {
				// /mobile/registered/submitRegInfo   注册接口加个参数 regionSameAsReferrer 1 一致 / 0或者空 不一致
				//若客户确认（点选“是”），则按照代理人填写的地市，进行默认网点和默认团队的分配。 ==== 0或者空 不一致
				//若客户点击否，则将数据恢复成推荐人的省市区。===== 1 一致
				if (jsonObject.containsKey("regionSameAsReferrer") && StringUtil.isNotEmpty(jsonObject.getString("regionSameAsReferrer"))) {
					String regionSameAsReferrer = jsonObject.getString("regionSameAsReferrer");
					if ("1".equals(regionSameAsReferrer)) {
						user.setDeptid(referAgent.getDeptid());
						user.setTeamcode(referAgent.getTeamcode());
						user.setTeamname(referAgent.getTeamname());
					} else {
						setUserDept(user, jsonObject);
					}
				} else {
					setUserDept(user, jsonObject);
				}
			} else {
				setUserDept(user, jsonObject);
			}
		} else {
			setUserDept(user, jsonObject);
		}


		if (StringUtil.isEmpty(user.getDeptid())) {
			commonModel.setStatus("fail");
			commonModel.setMessage("请配置好该地区默认注册网点");
			return commonModel;
		}
		user.setRegistertime(new Date());
		user.setFirstlogintime(new Date());
		user.setLsatlogintime(new Date());
		// 生成临时工号
		LogUtil.info("生成临时工号开始");
		String tempjobnum = insbAgentService.updateAgentTempJobNo();
		LogUtil.info("生成临时工号：" + tempjobnum);
		user.setTempjobnum(tempjobnum);
		user.setJobnum(tempjobnum);
		user.setCreatetime(new Date());
		user.setAgentcode(tempjobnum);

		// 1插入到LdapAgentModel
		// LdapMgr ldapMgr = new LdapMgr();
		// LdapAgentModel model = new LdapAgentModel();
		// model.setBusinessCategory("01");
		// model.setCn(tempjobnum);
		// model.setMobile(phone);
		// model.setEmployeeNumber(tempjobnum);
		// model.setUserPassword(LdapMd5.Md5Encode(passWord));
		// model.setObjectClass("inetOrgPerson");
		// model.setDisplayName(name);
		// model.setGivenName(name);
		// model.setSn(name);
		// model.setUid(UUIDUtils.random());
		// model.setParentDN(dept.getNoti());
		// try{
		// model = ldapMgr.addAgent(model);
		// }catch(Exception e){
		// e.printStackTrace();
		// commonModel.setStatus("fail");
		// commonModel.setMessage("添加用户失败！");
		// return commonModel;
		// }
		// 1插入到ofuser表中
		OFUser ofuser = new OFUser();
		ofuser.setUsername(tempjobnum);
//		ofuser.setPlainPassword(passWord);	//以前没加密
		ofuser.setPlainPassword(StringUtil.md5Base64(passWord));  //密码加密
		ofuser.setName(name);
		ofuser.setCreationDate(new Date().getTime());
		ofuser.setModificationDate(new Date().getTime());
		ofUserDao.insert(ofuser);

		// 2 保存注册信息
		try {
			// user.setNoti(model.getDN());
			insbAgentDao.insertReturnId(user);
		} catch (Exception e) {
			e.printStackTrace();
			// ldapMgr.deleteEntity(model.getEmployeeNumber());
			ofUserDao.deleteByUserName(ofuser.getUsername());
			commonModel.setStatus("fail");
			commonModel.setMessage("添加用户失败！");
			return commonModel;
		}
		try {
			// 3分配权限
			initTestAgentPermission(dept.getDeptid(), tempjobnum);
		} catch (Exception e) {
			e.printStackTrace();
			// ldapMgr.deleteEntity(model.getEmployeeNumber());
			ofUserDao.deleteByUserName(ofuser.getUsername());
			insbAgentDao.deleteById(user.getId());
			commonModel.setStatus("fail");
			commonModel.setMessage("添加用户失败！");
			return commonModel;
		}

		CommonModel model2 = appLoginService.login(tempjobnum, passWord, openid, "");

		commonModel.setStatus("success");
		commonModel.setMessage("注册成功");
		commonModel.setBody(model2.getBody());
		//如果从懒掌柜引流过来注册成功，则调用懒掌柜绑定一账通接口
		if(jsonObject.containsKey("lzgid") && !StringUtils.isEmpty(jsonObject.getString("lzgid"))){
			Map<String, String> map = new HashMap<String, String>();
			map.put("userid", user.getId());
			map.put("managerid", jsonObject.getString("lzgid"));
			map.put("requirementid", "");
			map.put("account", phone);
			map.put("username", name);
			map.put("ismanager", "0");
			map.put("agentcode", user.getAgentcode());
			map.put("idno", user.getIdno());
			map.put("organization", user.getIdno());
			bindingLzg(map);
		}




		return commonModel;
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public CommonModel bindPhone(String bindPhoneJSON) {
		LogUtil.info("bindPhone:" + bindPhoneJSON);
		CommonModel commonModel = new CommonModel();
		try {
			JSONObject jsonObject = JSONObject.fromObject(bindPhoneJSON);
			String phone = jsonObject.getString("phone");
			if (StringUtil.isEmpty(phone)) {
				commonModel.setStatus(CommonModel.STATUS_FAIL);
				commonModel.setMessage("手机号不能为空。");
				return commonModel;
			}
			List<INSBAgent> tempAgentList = insbAgentDao.selectNotminiAgent(phone);

			// 判断手机号是否已存在
			if (tempAgentList.size() > 0) {
				commonModel.setStatus(CommonModel.STATUS_FAIL);
				commonModel.setMessage("该手机已绑定其他工号，请确认后再试。");
				return commonModel;
			}

			String jobnum = jsonObject.getString("jobnum");
			if (StringUtil.isEmpty(jobnum)) {
				commonModel.setStatus(CommonModel.STATUS_FAIL);
				commonModel.setMessage("工号不能为空。");
				return commonModel;
			}
			INSBAgent agent = insbAgentDao.selectAgentByAgentCode(jobnum);
			String oldMobile = agent.getMobile()==null?agent.getPhone():agent.getMobile();//add by hwc 20170621
			if (agent == null || agent.getAgentkind() ==null)  {
				commonModel.setStatus(CommonModel.STATUS_FAIL);
				commonModel.setMessage("非法代理人");
				return commonModel;
			}
			if (agent.getAgentkind() == 3)  {
				commonModel.setStatus(CommonModel.STATUS_FAIL);
				commonModel.setMessage("渠道用户不能绑定手机号");
				return commonModel;
			}
			agent.setFirstlogintime(new Date());
			agent.setLsatlogintime(new Date());
			agent.setPhone(phone);
			agent.setMobile(phone);
			agent.setMobile2(phone);
			insbAgentDao.updateById(agent);

			/**
			 * 通知集团统一用户中心 hwc 添加 20170621
			 */
			/*try {
				Map<String, String> params = new HashMap<>();
				params.put("omobile", oldMobile);
				params.put("nmobile", phone);
				params.put("cardno", agent.getIdno());
				params.put("agentcode", agent.getAgentcode());
				userCenterService.updateUserAccount(params);
			}catch (Exception e){
				LogUtil.info("修改手机号通知集团统一用户中心失败"+e.getMessage());
				e.printStackTrace();
			}*/

		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus(CommonModel.STATUS_FAIL);
			commonModel.setMessage("绑定失败，请再次尝试。");
			return commonModel;
		}

		commonModel.setStatus("success");
		commonModel.setMessage("绑定成功");
		commonModel.setBody(null);
		return commonModel;
	}

	private void setUserDept(INSBAgent user, JSONObject jsonObject) {
		INSBRegion insbRegion = new INSBRegion();
		String countyCode = jsonObject.getString("countyCode");
		String cityCode = jsonObject.getString("cityCode");
		String provinceCode = jsonObject.getString("provinceCode");
		if (StringUtil.isNotEmpty(countyCode) && StringUtil.isEmpty(user.getDeptid())) {
			insbRegion.setComcode(countyCode);
			INSBRegion region = insbRegionService.queryOne(insbRegion);
			user.setDeptid(region == null ? "" : region.getDeptid());
		}
		if (StringUtil.isNotEmpty(cityCode) && StringUtil.isEmpty(user.getDeptid())) {
			insbRegion.setComcode(cityCode);
			INSBRegion region = insbRegionService.queryOne(insbRegion);
			user.setDeptid(region == null ? "" : region.getDeptid());
		}
		if (StringUtil.isNotEmpty(provinceCode) && StringUtil.isEmpty(user.getDeptid())) {
			insbRegion.setComcode(provinceCode);
			INSBRegion region = insbRegionService.queryOne(insbRegion);
			user.setDeptid(region == null ? "" : region.getDeptid());
		}
	}

	@Override
	public void bindingLzg(Map<String,String> map){
		String platform = ConfigUtil.getPropString("lzg.zzb.platform.code");
		map.put("platform", platform);
		//懒掌柜用户注册后自动绑定一账通
		String url = ConfigUtil.getPropString("lzg.api.service.url")+"/lm/otherconfig/bindthird";
		try {
			String result = HttpClientUtil.doPostJson(url, map, null);
			JSONObject resultJSN = JSONObject.fromObject(result);
			if (resultJSN.containsKey("status") && "OK".equals(resultJSN.getString("status"))) {// 成功
				// 懒掌柜绑定成功后使掌中保代理人账号绑定懒掌柜账号
				INSBAgent agent = insbAgentDao.selectByJobnum(map.get("userid"));
				if(agent != null && !map.get("managerid").equals(agent.getLzgid())){
					agent.setLzgid(map.get("managerid"));
					insbAgentDao.updateById(agent);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public CommonModel getDeptByArea(String province, String city, String country) {
		CommonModel commonModel = new CommonModel();
		Map<String, String> param = new HashMap<String, String>();
		param.put("province", province);
		param.put("city", city);
		param.put("county", country);
		param.put("comtype", "05");
		param.put("status", "1");
		List<INSCDept> deptList = inscDeptDao.selectAgreedDeptByRegionCode(param);
		if (deptList.size() == 0) {
			param = new HashMap<String, String>();
			param.put("province", province);
			param.put("city", city);
			param.put("comtype", "05");
			param.put("status", "1");
			deptList = inscDeptDao.selectAgreedDeptByRegionCode(param);
		}

		if (deptList.size() == 0) {
			commonModel.setStatus("fail");
			commonModel.setMessage("没有找到地区对应的已配置协议的机构网点");
			return commonModel;
		}
		commonModel.setStatus("success");
		commonModel.setBody(deptList.get(0));
		return commonModel;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public int initTestAgentPermission(String deptId, String tempjobnum) {
		return INSBPermissionsetService.initTestAgentPermission(deptId, tempjobnum);
	}

	// public synchronized String getJobNumber(){
	// String tempjobnum =(String) redisClient.get("tempjobnum");
	// if(tempjobnum==null || tempjobnum.length()==0){
	// tempjobnum="10000000";
	// }
	// Integer agentNum=null;
	// INSBAgent insbAgent = new INSBAgent();
	// //LdapMgr ldap = new LdapMgr();
	// //LdapAgentModel ldapAgentModel = null;
	// OFUser user = null;
	// do{
	// tempjobnum = String.valueOf(Long.parseLong(tempjobnum)+1);
	// insbAgent.setTempjobnum(tempjobnum);
	// agentNum = insbAgentDao.selectList(insbAgent).size();
	// if(agentNum==0){
	// user = ofUserDao.queryByUserName(tempjobnum);
	// if(user != null) agentNum++;
	// }
	// }while(agentNum!=0);
	// try {
	// redisClient.set("tempjobnum",tempjobnum);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// return tempjobnum;
	// }

	@Override
	public CommonModel bindWorkCode1(String JobNumOrIdCard, String jobNumPassword) {
		LogUtil.info("进入绑定工号接口,  JobNumOrIdCard=" + JobNumOrIdCard + ",jobNumPassword=" + jobNumPassword);
		CommonModel cmodel = new CommonModel();
		// 查询核心推过来的代理人
		INSBAgent coreAgent = new INSBAgent();
		coreAgent.setAgentcode(JobNumOrIdCard);
		coreAgent = insbAgentService.queryOne(coreAgent);
		if (coreAgent == null) {
			coreAgent = new INSBAgent();
			coreAgent.setIdno(JobNumOrIdCard);
			coreAgent = insbAgentService.queryOne(coreAgent);
		}
		if (coreAgent != null) {
			if (!StringUtil.isEmpty(coreAgent.getTempjobnum()) || coreAgent.getMobile().equals(coreAgent.getPhone())) {
				cmodel.setMessage("此工号已经被绑定，不可重复绑定");
				cmodel.setStatus("fail");
				return cmodel;
			}
			if (!StringUtil.md5Base64(jobNumPassword).equals(coreAgent.getPwd())) {
				cmodel.setMessage("密码错误");
				cmodel.setStatus("fail");
				return cmodel;
			}
			INSBAgent user = new INSBAgent();// 工号登陆 查询参数
			INSBAgent agent = coreAgent;
			String tempjobnum = null;
			user.setMobile(coreAgent.getMobile());
			List<INSBAgent> list = insbAgentService.queryList(user);// 工号登陆 查询
			for (INSBAgent insbAgent2 : list) {
				if (!insbAgent2.getAgentcode().equals(JobNumOrIdCard)) {
					insbAgentService.deleteById(insbAgent2.getId());
					tempjobnum = insbAgent2.getTempjobnum();
				}
			}
			agent.setPhone(agent.getMobile());
			agent.setTempjobnum(tempjobnum);
			agent.setJobnumtype(1);
			insbAgentService.updateById(agent);
			/**
			 * 授权
			 */
			LogUtil.info("绑定工号接口,  绑定权限Deptid=" + agent.getDeptid() + ",Jobnum=" + agent.getJobnum());
			INSBPermissionsetService.initAgentPermission(agent.getDeptid(), agent.getJobnum());
			cmodel.setStatus("success");
			cmodel.setMessage("绑定工号成功！");
		} else {
			cmodel.setStatus("fail");
			cmodel.setMessage("身份证号或者工号不存在！");
		}
		LogUtil.info("关闭绑定工号接口,  JobNumOrIdCard=" + JobNumOrIdCard + ",jobNumPassword=" + jobNumPassword);
		return cmodel;
	}

	@Override
	public CommonModel bindWorkCode(String tempJobNum, String JobNumOrIdCard, String jobNumPassword) {
		LogUtil.info("进入绑定工号接口, tempJobNum=" + tempJobNum + ",JobNumOrIdCard=" + JobNumOrIdCard + ",jobNumPassword="
				+ jobNumPassword);
		CommonModel cmodel = new CommonModel();
		// 查询核心推过来的代理人
		INSBAgent coreAgent = new INSBAgent();
		coreAgent.setAgentcode(JobNumOrIdCard);
		coreAgent = insbAgentService.queryOne(coreAgent);
		if (coreAgent == null) {
			coreAgent = new INSBAgent();
			coreAgent.setIdno(JobNumOrIdCard);
			coreAgent = insbAgentService.queryOne(coreAgent);
		}
		if (coreAgent != null) {
			if (!StringUtil.isEmpty(coreAgent.getTempjobnum())) {
				cmodel.setMessage("此工号已经被绑定，不可重复绑定");
				cmodel.setStatus("fail");
				return cmodel;
			}
			if (!StringUtil.md5Base64(jobNumPassword).equals(coreAgent.getPwd())) {
				cmodel.setMessage("密码错误");
				cmodel.setStatus("fail");
				return cmodel;
			}
			String oldPwd = coreAgent.getPwd();
			INSBAgent tempAgent = new INSBAgent();// 工号登陆 查询参数
			tempAgent.setTempjobnum(tempJobNum);
			tempAgent = insbAgentService.queryOne(tempAgent);
			coreAgent.setPhone(tempAgent.getPhone());
			coreAgent.setTempjobnum(tempJobNum);
			//coreAgent.setAgentcode(tempAgent.getAgentcode());
			coreAgent.setReferrer(tempAgent.getReferrer());
			coreAgent.setJobnumtype(2);
			coreAgent.setAgentkind(2);
			coreAgent.setApprovesstate(3);
			coreAgent.setPwd(tempAgent.getPwd());
			//bug5659 【生产环境】【代理人数据】后台试用工号绑定正式工号导致首登时间变化 前后台绑定需求一致
			if (coreAgent.getFirstlogintime() == null) {
				coreAgent.setFirstlogintime(new Date());
			}
			coreAgent.setFirstOderSuccesstime(tempAgent.getFirstOderSuccesstime());
			coreAgent.setRegistertime(tempAgent.getRegistertime());
			LogUtil.info("前端绑定工号，修改代理人密码 代理人："+coreAgent.toString()+" 操作人:"+tempJobNum+" 操作时间:"+ DateUtil.getCurrentDateTime());
			OFUser tempUser = ofUserDao.queryByUserName(tempJobNum);
			OFUser coreUser = ofUserDao.queryByUserName(coreAgent.getJobnum());
//			coreUser.setPlainPassword(tempUser.getPlainPassword()); //以前没加密的密码
			coreUser.setPlainPassword(tempAgent.getPwd());//从agent表中取出加密的密码
			coreUser.setModificationDate(new Date().getTime());
			ofUserDao.updateByUserName(coreUser);
			ofUserDao.deleteByUserName(tempJobNum);

//			insbAgentService.updateById(coreAgent);
			insbAgentService.updateAgentPwd(coreAgent, oldPwd);//修改密码并发短信通知代理人
			insbAgentService.deleteById(tempAgent.getId());
			INSBAgentpermission agentpermission = new INSBAgentpermission();
			agentpermission.setAgentid(tempAgent.getId());
			List<INSBAgentpermission> list = insbAgentpermissionService.queryList(agentpermission);
			for (INSBAgentpermission insbAgentpermission : list) {
				insbAgentpermissionService.deleteById(insbAgentpermission.getId());
			}
			/**
			 * 授权
			 */
			agentpermission = new INSBAgentpermission();
			agentpermission.setAgentid(coreAgent.getId());
			if (insbAgentpermissionService.queryCount(agentpermission) == 0) {// 如果已经分配权限，不能再次绑定
				LogUtil.info("绑定工号接口,  绑定权限Deptid=" + coreAgent.getDeptid() + ",Jobnum=" + coreAgent.getJobnum());
				INSBPermissionsetService.initAgentPermission(coreAgent.getDeptid(), coreAgent.getJobnum());
			}
			//修改order，保单信息
			insbAgentService.updateOrderInsuranceInfo(coreAgent.getJobnum(), tempJobNum);

			//20170601 hwc 实名认证通过后通知集团统一中心
			LogUtil.info("前端bindWorkCode调用updateUserDetail"+ JsonUtil.serialize(coreAgent));
			userCenterService.updateUserDetail(coreAgent,coreAgent.getJobnum(),coreAgent.getReferrer());
			//20170601 hwc end

			//appMarketingActivitiesService.isCanParticipateMarketing(JobNumOrIdCard,tempAgent.getReferrer(),tempAgent.getRegistertime());
			cmodel.setStatus("success");
			cmodel.setMessage("绑定工号成功！");
		} else {
			cmodel.setStatus("fail");
			cmodel.setMessage("身份证号或者工号不存在！");
		}
		LogUtil.info("关闭绑定工号接口,  JobNumOrIdCard=" + JobNumOrIdCard + ",jobNumPassword=" + jobNumPassword);
		return cmodel;
	}

	@Override
	public CommonModel fileUpLoad(HttpServletRequest request, MultipartFile file, String fileType, String fileDescribes,
								  String jobNum) {
		CommonModel model = new CommonModel();
		INSBAgent insbAgent = new INSBAgent();
		insbAgent.setJobnum(jobNum);
		insbAgent = insbAgentService.queryOne(insbAgent);
		if (insbAgent == null) {
			model.setStatus("fail");
			model.setMessage("代理人不存在");
			return model;
		}
		Map<String, Object> map = insbFilelibraryService.uploadOneFile(request, file, fileType, fileDescribes,
				insbAgent.getAgentcode());
		model.setBody(JSONObject.fromObject(map).toString());
		if (map.get("status").equals("error")) {
			model.setStatus("fail");
			model.setMessage("上传失败");
		} else {
			model.setStatus("success");
			model.setMessage("上传成功");
		}
		return model;
	}

	@Override
	public CommonModel getValidateCodeImg(HttpServletRequest request) {
		CommonModel model = new CommonModel();
		String uuid = UUIDUtils.random();
		String code = EncodeUtils.generateValidateCode(4);
		try {
			redisClient.set(MODULE, uuid, code, 600);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uuid", uuid);
		if (imageActionPath == null) {
			imageActionPath = "http://" + ValidateUtil.getConfigValue("localhost.ip") + ":"
					+ ValidateUtil.getConfigValue("localhost.port") + "/"
					+ ValidateUtil.getConfigValue("localhost.projectName") + "/mobile/registered/validateCodeImg?uuid=";
		}
		map.put("imgSrc", imageActionPath + uuid);
		model.setStatus("success");
		model.setMessage("生成验证码图片成功");
		model.setBody(map);
		return model;
	}

	@Override
	public void validateCodeImg(HttpServletRequest request, HttpServletResponse response) {
		String uuid = request.getParameter("uuid");
		String code = (String) redisClient.get(MODULE, uuid);
		Random random = new Random();
		int width = 120;
		int height = 44; // 验证图片的宽度，高度
		Color back = new Color(198, 198, 198);
		Color front = new Color(51, 51, 51);
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
		Graphics2D g = bi.createGraphics(); // 得到画布
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 44)); // 设置字体
		g.setColor(back);
		g.fillRect(0, 0, width, height); // 画背景
		g.setColor(front);
		g.drawString(code, 3, 37); // 画字符
		for (int i = 0; i < 51; i++) {
			g.fillRect(random.nextInt(width), random.nextInt(height), 1, 1);
		} // 产生至多20个噪点
		try {
			response.setContentType("image/jpeg");
			ImageIO.write(bi, "JPEG", response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 统计出该代理人成功推荐的人数、被推荐人中的首单人数、这些人的姓名、所属平台、注册时间、首单时间
	 *
	 * @param jobNum
	 * @return
	 */
	@Override
	public CommonModel myRecommend(String jobNum, int limit, long offset) {
		CommonModel model = new CommonModel();
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> countRecommendMap = new HashMap<String, Object>();
		Map<String,Object> countAuthMap = new HashMap<>();
		if (StringUtil.isEmpty(jobNum)) {
			model.setStatus("fail");
			model.setMessage("代理人工号不能为空！");
			return model;
		}
		countRecommendMap.put("referrer", jobNum);
		countAuthMap.put("referrer", jobNum);
		int count = insbAgentDao.countRecommend(countRecommendMap);
		countRecommendMap.put("limit", limit == 0 ? 5 : limit);
		countRecommendMap.put("offset", offset);
		List<Map<String, Object>> list = insbAgentDao.recommendPerson(countRecommendMap);
		map.put("regCount",count);
		map.put("total",count);
		map.put("list", list);

		countRecommendMap.put("firstOderSuccess", true);

		map.put("firstOderSuccessCount",insbAgentDao.countRecommend(countRecommendMap));
		// 计算推荐的人中认证成功的人数
		map.put("authenticateSuccessCount",insbAgentDao.getAuthenticatedCount(countAuthMap));

		model.setBody(map);
		model.setStatus("success");
		model.setMessage("统计成功！");
		return model;
	}

	@Override
	public CommonModel validatePhone(String phone) {
		CommonModel model = new CommonModel();
		if (StringUtil.isEmpty(phone)) {
			model.setBody(null);
			model.setStatus(CommonModel.STATUS_FAIL);
			model.setMessage("phone is null");
			return model;
		}
		List<INSBAgent> list = insbAgentDao.selectNotminiAgent(phone);
		if (!list.isEmpty()) {
			model.setStatus(CommonModel.STATUS_FAIL);
			model.setMessage("该手机号已注册");
			return model;
		}
		/**
		 * 调用集团用户中心验证手机号是否注册 hwc add 20170601
		 */
		Map<String,String> params = new HashMap<String ,String>();
		params.put("phoneno",phone);
		UserCenterReturnModel userCenterReturnModel =userCenterService.validateMobileNo(params);
		if("fail".equals(userCenterReturnModel.getStatus())){
			model.setStatus(CommonModel.STATUS_FAIL);
			model.setMessage("该手机已注册");
			return model;
		}
		//end 20170601 hwc
		model.setStatus(CommonModel.STATUS_SUCCESS);
		model.setMessage("");
		return model;
	}

	@Override
	public CommonModel validateReferrer(JSONObject object) {
		CommonModel model = new CommonModel();
		if (object == null || !object.containsKey("jobnum") || StringUtil.isEmpty(object.getString("jobnum"))) {
			model.setBody(null);
			model.setStatus(CommonModel.STATUS_FAIL);
			model.setMessage("jobnum is null");
			return model;
		}
		String jobnum = object.getString("jobnum");
		Map<String, Object> referrermap = new  HashMap<String, Object>();
		referrermap.put("agentcode", jobnum);
		referrermap.put("agentkind", 2);//用户种类 1-试用  2-正式  3-渠道
		List<INSBAgent> referrers = insbAgentDao.selectReferrer(referrermap);
		referrermap.put("agentkind", 1);//用户种类 1-试用  2-正式  3-渠道
		List<INSBAgent> referrers1 = insbAgentDao.selectReferrer(referrermap);
		if (referrers.isEmpty() && referrers1.isEmpty()) {
			model.setStatus(CommonModel.STATUS_FAIL);
			model.setMessage("推荐人不存在！");
			return model;
		}
		model.setStatus(CommonModel.STATUS_SUCCESS);
		model.setMessage("");
		return model;
	}

	@Override
	public CommonModel agentRegion(JSONObject object) {
		CommonModel model = new CommonModel();
		if (object == null || !object.containsKey("jobnum") || StringUtil.isEmpty(object.getString("jobnum"))) {
			model.setBody(null);
			model.setStatus(CommonModel.STATUS_SUCCESS);
			model.setMessage("代理人工号为空！");
			return model;
		}
		String jobnum = object.getString("jobnum");

		Map<String, Object> referrermap = new  HashMap<String, Object>();
		referrermap.put("agentcode", jobnum);
		List<INSBAgent> referrers = insbAgentDao.selectReferrer(referrermap);
		if (referrers.isEmpty()) {
			model.setStatus(CommonModel.STATUS_FAIL);
			model.setMessage("推荐人不存在！");
			return model;
		}
		INSBAgent agent = referrers.get(0);
		if (agent == null || StringUtil.isEmpty(agent.getDeptid())) {
			model.setBody(null);
			model.setStatus(CommonModel.STATUS_FAIL);
			model.setMessage("代理人不存在或代理人所属机构为空！");
			return model;
		}
		INSCDept dept = inscDeptService.queryById(agent.getDeptid());
		if (dept == null) {
			model.setBody(null);
			model.setStatus(CommonModel.STATUS_FAIL);
			model.setMessage("代理人所属机构不存在！");
			return model;
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("provinceCode", dept.getProvince());
		map.put("cityCode", dept.getCity());
		map.put("countyCode", dept.getCounty());
		model.setBody(map);
		model.setStatus(CommonModel.STATUS_SUCCESS);
		model.setMessage("查询成功！");
		return model;
	}

	@Override
	public CommonModel bank(JSONObject object) {
		CommonModel model = new CommonModel();
		Map<String, String> map = new HashMap<String, String>();
		if (object == null || !object.containsKey("codename") || StringUtil.isEmpty(object.getString("codename"))) {

		} else {
			String codename = object.getString("codename");
			map.put("codename", codename);
		}

		List<INSCCode> list = inscCodeDao.selectBank(map);
		model.setBody(list);
		model.setStatus(CommonModel.STATUS_SUCCESS);
		model.setMessage("查询成功！");
		return model;
	}

	@Override
	public CommonModel removeOpenid(JSONObject object, String token) {
		CommonModel model = new CommonModel();
		Map<String, String> map = new HashMap<String, String>();
		if (object == null || !object.containsKey("openid") || StringUtil.isEmpty(object.getString("openid"))
				|| !object.containsKey("jobnum") || StringUtil.isEmpty(object.getString("jobnum"))
				|| StringUtil.isEmpty(token)) {
			model.setBody("参数不能为空");
			model.setStatus(CommonModel.STATUS_FAIL);
			model.setMessage("操作失败！");
			return model;
		}
		String jobnum = object.getString("jobnum");
		String openid = object.getString("openid");
		INSBAgent agent = new INSBAgent();
		agent.setJobnum(jobnum);
		agent.setOpenid(openid);
		agent = insbAgentDao.selectOne(agent);
		if (agent == null) {
			model.setBody("非法代理人！");
			model.setStatus(CommonModel.STATUS_FAIL);
			model.setMessage("操作失败！");
			return model;
		} else {
			agent.setOpenid("");
			insbAgentDao.updateById(agent);
			redisClient.del(Constants.TOKEN, token);
		}

		model.setStatus(CommonModel.STATUS_SUCCESS);
		model.setMessage("操作成功！");
		return model;
	}

	@Override
	public CommonModel validateCodeImg(HttpServletRequest request, String uuid, String code) {
		CommonModel model = new CommonModel();
		String codeStr = null; // 从redis中取出验证码
		try {
			codeStr = (String) redisClient.get(MODULE, uuid);
		} catch (Exception e) {
			model.setStatus("fail");
			model.setMessage("验证码失效！");
			return model;
		}

		if (code.equals(codeStr)) {
			model.setStatus("success");
			model.setMessage("验证通过！");
		} else {
			model.setStatus("fail");
			model.setMessage("请输入正确的图形验证码！");
		}
		return model;
	}

	@Override
	public CommonModel certification(String jobNumOrIdCard, String mainBiz, String idCardPhotoA, String idCardPhotoB,
									 String bankCardA, String qualificationA, String qualificationPage, String noti, String agree, String idno, String name,String bank) {
		CommonModel model = new CommonModel();
		INSBAgent insbAgent = new INSBAgent();
		insbAgent.setJobnum(jobNumOrIdCard);
		insbAgent = insbAgentService.queryOne(insbAgent);
		if (insbAgent == null) {
			model.setStatus("fail");
			model.setMessage("代理人不存在");
			return model;
		} else if ("2".equals(insbAgent.getJobnumtype()) || "2".equals(insbAgent.getApprovesstate())
				|| "3".equals(insbAgent.getApprovesstate())) {
			model.setStatus("fail");
			model.setMessage("认证失败或未认证的正式工号才可以认证");
			return model;
		}
		if (StringUtil.isEmpty(idno) || idno.length() > 20) {
			model.setStatus("fail");
			model.setMessage("请使用正确的身份证号码");
			return model;
		}

		INSBAgent query = new INSBAgent();
		query.setIdno(idno);
		List<INSBAgent> ids = insbAgentService.queryList(query);
		if (ids != null && !ids.isEmpty()) {
			for (INSBAgent a : ids) {
				if (StringUtil.isNotEmpty(a.getJobnum()) && !a.getJobnum().equals(jobNumOrIdCard) && a.getAgentstatus() != null && a.getAgentstatus() == 1) {
					if (a.getDeptid() == null || insbAgent.getDeptid() == null) {
						continue;
					}
					if (a.getDeptid().equals(insbAgent.getDeptid())) {
						model.setStatus(CommonModel.STATUS_FAIL);
						model.setMessage("该证件号已注册，请使用正式工号登陆如需查询请致电4008-008-111");
						return model;
					}
					INSCDept aDept = inscDeptDao.selectById(a.getDeptid());
					INSCDept insbAgentdept = inscDeptDao.selectById(insbAgent.getDeptid());
					if (aDept ==null || insbAgentdept == null || aDept.getDeptinnercode() == null || insbAgentdept.getDeptinnercode() == null) {
						continue;
					}
					if (aDept.getDeptinnercode().length() < 5  || insbAgentdept.getDeptinnercode().length() < 5) {
						continue;
					}
					if (aDept.getDeptinnercode().substring(0,5).equals(insbAgentdept.getDeptinnercode().substring(0,5))) {
						model.setStatus(CommonModel.STATUS_FAIL);
						model.setMessage("该证件号已注册，请使用正式工号登陆如需查询请致电4008-008-111");
						return model;
					}
				}
			}
		}

		INSBCertification queryCertification = new INSBCertification();
		queryCertification.setAgentnum(jobNumOrIdCard);
		queryCertification = insbCertificationService.queryOne(queryCertification);
		INSBCertification insbCertification = null;
		if (null == queryCertification) {
			insbCertification = new INSBCertification();
			insbCertification.setId(UUIDUtils.random());
			insbCertification.setCreatetime(new Date());
		} else {
			insbCertification = queryCertification;
		}
		insbCertification.setOperator(insbAgent.getJobnum());
		insbCertification.setNoti(noti);
		insbCertification.setAgentnum(jobNumOrIdCard);
		insbCertification.setIdcardpositive(idCardPhotoA);
		insbCertification.setIdcardopposite(idCardPhotoB);
		insbCertification.setBankcardpositive(bankCardA);
		insbCertification.setQualificationpositive(qualificationA);
		insbCertification.setQualificationpage(qualificationPage);
		insbCertification.setDeptid(insbAgent.getDeptid());
		insbCertification.setAgree(Integer.parseInt(agree));
		insbCertification.setMainbiz(mainBiz);// 主营业务
		insbCertification.setStatus(0);
		insbCertification.setDesignatedoperator(null);
		insbCertification.setBelongs2bank(bank);//开户行
		if (null == queryCertification) {
			insbCertificationService.insert(insbCertification);
		} else {
			insbCertification.setDesignatedoperator(queryCertification.getDesignatedoperator());
			insbCertification.setModifytime(new Date());
			insbCertificationService.updateById(insbCertification);
			insbCertificationService.updateDesignatedoperator(insbCertification);
		}
		insbAgent.setApprovesstate(2);// 认证状态改为认证中
		insbAgent.setMainbusiness(mainBiz);// 主营业务
		insbAgent.setIdno(idno);
		//insbAgent.setBelongs2bank(bank);//开户行
		insbAgentService.updateById(insbAgent);

		//LogUtil.info(StringUtil.isEmpty(insbCertification.getDesignatedoperator())+"=flag,oprator="+queryCertification.getDesignatedoperator()+"是否认证任务需要自动分配insbCertificationid="+insbCertification.getId());
		if(StringUtil.isEmpty(insbCertification.getDesignatedoperator())){//如果已经有处理人则不用再次调度。
			Task task = new Task();
			task.setProInstanceId(insbCertification.getId());
			task.setSonProInstanceId(insbCertification.getId());
			task.setPrvcode(insbCertification.getDeptid());
			task.setTaskTracks(insbCertification.getDesignatedoperator());
			task.setTaskName("认证任务");
			LogUtil.info("认证任务自动分配insbCertificationid="+insbCertification.getId());
			dispatchService.dispatchTask(task);
		}
		model.setStatus("success");
		model.setMessage("保存认证信息成功");
		return model;
	}
	@Resource
	private DispatchTaskService dispatchService;
	@Override
	public CommonModel filesUpLoad(HttpServletRequest request, MultipartFile[] file, String[] fileType,
								   String[] fileDescribes, String jobNum) {
		CommonModel model = new CommonModel();
		INSBAgent insbAgent = new INSBAgent();
		insbAgent.setJobnum(jobNum);
		insbAgent = insbAgentService.queryOne(insbAgent);
		if (insbAgent == null) {
			model.setStatus("fail");
			model.setMessage("代理人不存在");
			return model;
		}
		String[] filenames = new String[file.length];
		for (int i = 0; i < filenames.length; i++) {
			filenames[i] = UUIDUtils.random();
		}
		Map<String, Object> map = null;
		try {
			map = insbFilelibraryService.uploadFiles(request, file, "img", insbAgent.getAgentcode(), filenames,
					fileDescribes, fileType);
		} catch (Exception e) {
			e.printStackTrace();
			model.setStatus("fail");
			model.setMessage("上传失败");
			return model;
		}
		model.setBody(JSONObject.fromObject(map).toString());
		List<String> statuslist = (List<String>) map.get("statuslist");
		for (String string : statuslist) {
			if (string.equals("fail")) {
				model.setStatus("fail");
				model.setMessage("上传失败");
				break;
			}
			model.setStatus("success");
			model.setMessage("上传成功");
		}
		return model;
	}

	@Override
	public CommonModel fileUpLoadBase64(HttpServletRequest request, String file, String fileName, String fileType,
										String fileDescribes, String jobNum, String taskId) {
		LogUtil.info("==========上传接收到的参数==，fileName：" + fileName +" fileType = " +fileType+" jobNum = "+jobNum+" taskId = "+taskId);
		CommonModel model = new CommonModel();
		INSBAgent insbAgent = new INSBAgent();
		insbAgent.setJobnum(jobNum);
		insbAgent = insbAgentService.queryOne(insbAgent);
		if (insbAgent == null) {
			model.setStatus("fail");
			model.setMessage("代理人不存在");
			return model;
		}
		// 2015-11-25日修改,可以不用上传流程id
		// if(StringUtil.isEmpty(taskId)){
		// model.setStatus("fail");
		// model.setMessage("任务流水号不能为空");
		// return model;
		// }
		Map<String, Object> map = insbFilelibraryService.uploadOneFile(request, file, fileName, fileType, fileDescribes,
				insbAgent.getAgentcode());
		if (map.get("status").equals("success")) {
			model.setStatus("success");
			model.setMessage("上传成功");
			model.setBody(map);

			INSBFilebusiness insbFilebusiness = new INSBFilebusiness();
			insbFilebusiness.setCreatetime(new Date());
			insbFilebusiness.setOperator(null == jobNum ? "" : jobNum);
			insbFilebusiness.setType(fileType);
			insbFilebusiness.setFilelibraryid((String) map.get("fileid"));
			if (!StringUtils.isEmpty(taskId))
				insbFilebusiness.setCode(taskId);
			else
				insbFilebusiness.setCode("");
			insbFilebusinessDao.insert(insbFilebusiness);
		} else {
			model.setStatus("fail");
			model.setMessage((String) map.get("msg"));
		}
		return model;
	}

	public CommonModel fileUpLoadWeChat(HttpServletRequest request, String mediaId, String fileName, String fileType,
										String fileDescribes, String jobNum, String taskId) {
		CommonModel model = new CommonModel();
		INSBAgent insbAgent = new INSBAgent();
		insbAgent.setJobnum(jobNum);
		insbAgent = insbAgentService.queryOne(insbAgent);
		if (insbAgent == null) {
			model.setStatus("fail");
			model.setMessage("代理人不存在");
			return model;
		}
		Map<String, Object> map = insbFilelibraryService.saveFileInfo(request, mediaId, fileName, fileType,
				fileDescribes, insbAgent.getAgentcode());
		if (map.get("status").equals("success")) {
			model.setStatus("success");
			model.setMessage("上传成功");
			model.setBody(map);

			INSBFilebusiness insbFilebusiness = new INSBFilebusiness();
			insbFilebusiness.setCreatetime(new Date());
			insbFilebusiness.setOperator(null == jobNum ? "" : jobNum);
			insbFilebusiness.setType(fileType);
			insbFilebusiness.setFilelibraryid((String) map.get("fileid"));
			if (!StringUtils.isEmpty(taskId))
				insbFilebusiness.setCode(taskId);
			else
				insbFilebusiness.setCode("");
			insbFilebusinessDao.insert(insbFilebusiness);
		} else {
			model.setStatus("fail");
			model.setMessage((String) map.get("msg"));
		}
		return model;
	}

	@Override
	public CommonModel coreAgentDock(String param) {
		LogUtil.info("==进入核心推送代理人接口==，jsonObject：" + param);
		CommonModel commonModel = new CommonModel();

		if (StringUtil.isEmpty(param)) {
			commonModel.setStatus("fail");
			commonModel.setMessage("报文不可为空");
			LogUtil.info("==结束核心推送代理人接口==，同步失败，" + commonModel.getMessage());
			return commonModel;
		}
		JSONObject jsonObject = null;
		try {
			// 如果是json，则是代理人注册，或代理人修改
			jsonObject = JSONObject.fromObject(param);
		} catch (Exception e1) {
			// 如果不是json，则是代理人离职
			String jobnum = param;
			INSBAgent agent = insbAgentDao.selectAgentByAgentCode(jobnum);
			agent.setAgentstatus(2);
			insbAgentDao.updateById(agent);
			commonModel.setStatus("success");
			commonModel.setMessage("离职成功");
			LogUtil.info("==结束核心推送代理人接口==，离职成功");
			return commonModel;
		}
		if (StringUtil.isEmpty(jsonObject.get("Jobnum"))
				|| StringUtil.isEmpty(jsonObject.get("deptid"))
				|| StringUtil.isEmpty(jsonObject.get("teamcode"))) {
			commonModel.setStatus("fail");
			commonModel.setMessage("Jobnum、deptid、teamcode为必录项，不可为空");
			LogUtil.info("==结束核心推送代理人接口==，注册失败，" + commonModel.getMessage());
			return commonModel;
		}
		INSBAgent user = new INSBAgent();
		String name = !jsonObject.containsKey("name") ? null : jsonObject.getString("name");
		String sex = !jsonObject.containsKey("sex") ? null : jsonObject.getString("sex");
		String jobnum = jsonObject.getString("Jobnum");
		String cgfns = !jsonObject.containsKey("cgfns") ? null : jsonObject.getString("cgfns");
		String licenseno = !jsonObject.containsKey("licenseno") ? null : jsonObject.getString("licenseno");
		String email = !jsonObject.containsKey("email") ? null : jsonObject.getString("email");
		String idnotype = !jsonObject.containsKey("idnotype") ? null : jsonObject.getString("idnotype");

		INSBCorecodemap insbCorecodemap = new INSBCorecodemap();
		insbCorecodemap.setType("idcardtypecorepush");
		insbCorecodemap.setCorecode(idnotype);
		insbCorecodemap = insbCorecodemapService.queryOne(insbCorecodemap);
		idnotype = insbCorecodemap.getCmcode();
		if (!StringUtil.isEmpty(sex)) {
			insbCorecodemap = new INSBCorecodemap();
			insbCorecodemap.setType("sextype");
			insbCorecodemap.setCorecode(sex);
			insbCorecodemap = insbCorecodemapService.queryOne(insbCorecodemap);
			sex = insbCorecodemap.getCmcode();
		}

		String idno = !jsonObject.containsKey("idno") ? null : jsonObject.getString("idno");
		String passWord = null;
		if (StringUtil.isEmpty(idno) || idno.trim().length() < 6) {
			passWord = "123456";
		} else {
			passWord = idno.substring(idno.trim().length() - 6, idno.trim().length());
		}
		String deptid = jsonObject.getString("deptid");
		String livingcityid = !jsonObject.containsKey("livingcityid") ? null : jsonObject.getString("livingcityid");
		String address = !jsonObject.containsKey("address") ? null : jsonObject.getString("address");
		String teamcode = jsonObject.getString("teamcode");
		String teamname = !jsonObject.containsKey("teamname") ? null : jsonObject.getString("teamname");

		INSBAgent temp = new INSBAgent();
		temp.setJobnum(jobnum);
		temp = insbAgentService.queryOne(temp);
		String oldPwd = null;
		// 判断工号是否已存在
		if (temp != null) {
			user = temp;
			oldPwd = temp.getPwd();
		}
		user.setName(name);
		user.setPwd(StringUtil.md5Base64(passWord));
		user.setJobnumtype(2);
		user.setAgentstatus(1);
		user.setApprovesstate(3);
		user.setAgentkind(2);
		user.setIstest(2);//默认普通账户
		user.setSex(sex);
		user.setEmail(email);
		user.setIdnotype(idnotype);
		user.setIdno(idno);
		user.setLivingcityid(livingcityid);
		user.setAddress(address);
		user.setTeamcode(teamcode);
		user.setTeamname(teamname);
		user.setCgfns(cgfns);
		user.setLicenseno(licenseno);
		user.setMainbusiness("0");
		// 根据省、市、区 ，查询代理机构
		INSCDept dept = new INSCDept();
		dept = inscDeptService.queryById(deptid);

		user.setDeptid(dept.getComcode());

		user.setJobnum(jobnum);
		user.setCreatetime(new Date());
		user.setAgentcode(jobnum);
		OFUser ofuser = new OFUser();
		if (temp != null) {
			ofuser = ofUserDao.queryByUserName(jobnum);
		}
		// 1插入到ofuser表中
		ofuser.setUsername(jobnum);
//		ofuser.setPlainPassword(passWord);//未加密的密码
		ofuser.setPlainPassword(StringUtil.md5Base64(passWord));//加密的密码
		ofuser.setName(name);
		if (temp != null) {
			ofuser.setModificationDate(new Date().getTime());
			ofUserDao.updateByUserName(ofuser);
		} else {
			ofuser.setCreationDate(new Date().getTime());
			ofUserDao.insert(ofuser);
		}

		if (temp != null) {
			user.setPwd(null);
			LogUtil.info("核心推送，修改代理人密码 代理人："+user.toString()+" 操作人:"+user.getJobnum()+" 操作时间:"+ DateUtil.getCurrentDateTime());
//			insbAgentDao.updateById(user);
			insbAgentService.updateAgentPwd(user, oldPwd);//修改密码并发短信通知代理人
		}else{
			//2 保存注册信息
			try{
				user.setRegistertime(new Date());
				insbAgentDao.insertReturnId(user);
			}catch(Exception e){
				e.printStackTrace();
				ofUserDao.deleteByUserName(ofuser.getUsername());
				commonModel.setStatus("fail");
				commonModel.setMessage("添加用户失败！");
				LogUtil.info("==结束核心推送代理人接口==，注册失败，" + commonModel.getMessage());
				return commonModel;
			}
			try {
				// 3分配权限

				LogUtil.info("核心推送代理人接口,绑定权限Deptid=" + dept.getDeptid() + ",Jobnum=" + jobnum);
				INSBPermissionsetService.initAgentPermission(dept.getDeptid(), jobnum);
			} catch (Exception e) {
				e.printStackTrace();
				ofUserDao.deleteByUserName(ofuser.getUsername());
				insbAgentDao.deleteById(user.getId());
				commonModel.setStatus("fail");
				commonModel.setMessage("添加用户失败！");
				LogUtil.info("==结束核心推送代理人接口==，注册失败，" + commonModel.getMessage());
				return commonModel;
			}
			if(jsonObject.containsKey("lzgid") && !StringUtils.isEmpty(jsonObject.getString("lzgid"))){
				Map<String, String> map = new HashMap<String, String>();
				map.put("userid", user.getId());
				map.put("managerid", jsonObject.getString("lzgid"));
				map.put("requirementid", "");
				map.put("account", jobnum);
				map.put("username", name);
				map.put("ismanager", "0");
				map.put("agentcode", user.getAgentcode());
				map.put("idno", user.getIdno());
				map.put("organization", user.getIdno());
				bindingLzg(map);
			}

		}

		// CommonModel model2 = appLoginService.login(jobnum, passWord,null);

		commonModel.setStatus("success");
		commonModel.setMessage("同步成功");
		// commonModel.setBody(model2.getBody());
		// 如果从懒掌柜引流过来注册成功，则调用懒掌柜绑定一账通接口

		LogUtil.info("==结束核心推送代理人接口==，同步成功");
		return commonModel;
	}
	@Override
	public CommonModel oauthRegist(String regInfoJSON) {

		CommonModel commonModel = new CommonModel();
		INSBAgent user = new INSBAgent();
		INSBAgent iNSBAgent = new INSBAgent();
		JSONObject jsonObject = JSONObject.fromObject(regInfoJSON);
		String name = jsonObject.getString("name");
		String phone = jsonObject.getString("phone");
		String referrerJobNum = "";
		if (jsonObject.containsKey("refNum") && !StringUtils.isEmpty(jsonObject.getString("refNum")))
			referrerJobNum = jsonObject.getString("refNum");
		String openid = null;
		if (jsonObject.containsKey("openid") && !StringUtils.isEmpty(jsonObject.getString("openid")))
			openid = jsonObject.getString("openid");

		iNSBAgent.setPhone(phone);
		iNSBAgent.setMobile(phone);
		// 判断手机号是否已存在
		if (insbAgentService.queryList(iNSBAgent).size() > 0) {
			commonModel.setStatus("fail");
			commonModel.setMessage("手机号已存在");
			return commonModel;
		}
		user.setName(name);
		user.setPhone(phone);
		user.setMobile(phone);
		user.setOpenid(openid);

		/*
		 * //根据工号查询推荐人 iNSBAgent = new INSBAgent();
		 * iNSBAgent.setJobnum(referrerJobNum); INSBAgent referrer =
		 * insbAgentDao.selectByJobnum(referrerJobNum); if(referrer==null){
		 * commonModel.setStatus("fail"); commonModel.setMessage(
		 * "Registration failed"); commonModel.setBody("Referrer does not exist"
		 * ); return commonModel; }
		 */ // 100000031 123456
		user.setChannelid(referrerJobNum);
		user.setReferrer(referrerJobNum);
		;
		user.setJobnumtype(1);
		user.setAgentstatus(1);
		user.setApprovesstate(1);
		user.setAgentkind(1);
		user.setIstest(Integer.valueOf(2));
		user.setDeptid("");
		// 根据省、市、区 ，查询代理机构
		INSCDept dept = new INSCDept();
		// 如果推荐人工号不为空，则注册用户的代理机构为推荐人所在的代理机构，
		// 否则根据注册人的地区编码查询代理机构
		String provinceCode = jsonObject.getString("provinceCode");
		String cityCode = jsonObject.getString("cityCode");
		String countyCode = jsonObject.getString("countyCode");
		if (!"".equals(referrerJobNum)) {
			INSBAgent referAgent = insbAgentDao.selectByJobnum(referrerJobNum);
			if (referAgent != null && StringUtil.isNotEmpty(referAgent.getDeptid())) {
				user.setDeptid(referAgent.getDeptid());
			} else if (!StringUtil.isEmpty(provinceCode) && !StringUtil.isEmpty(cityCode)
					&& !StringUtil.isEmpty(countyCode)) {
				commonModel = this.getDeptByArea(provinceCode, cityCode, countyCode);
				if ("fail".equals(commonModel.getStatus())) {
					return commonModel;
				} else {
					dept = (INSCDept) commonModel.getBody();
				}

				user.setDeptid(dept.getComcode());
			}
		} else if (!StringUtil.isEmpty(provinceCode) && !StringUtil.isEmpty(cityCode)
				&& !StringUtil.isEmpty(countyCode)) {
			commonModel = this.getDeptByArea(provinceCode, cityCode, countyCode);
			if ("fail".equals(commonModel.getStatus())) {
				return commonModel;
			} else {
				dept = (INSCDept) commonModel.getBody();
			}

			user.setDeptid(dept.getComcode());
		}

		user.setRegistertime(new Date());

		// 生成临时工号
		String tempjobnum = insbAgentService.updateAgentTempJobNo() + "";
		user.setTempjobnum(tempjobnum);
		user.setJobnum(tempjobnum);
		user.setCreatetime(new Date());
		user.setAgentcode(tempjobnum);

		// 1插入到ofuser表中
		OFUser ofuser = new OFUser();
		ofuser.setUsername(tempjobnum);
		ofuser.setName(name);
		ofuser.setCreationDate(new Date().getTime());
		ofuser.setModificationDate(new Date().getTime());
		ofUserDao.insert(ofuser);

		// 2 保存注册信息
		try {
			insbAgentDao.insertReturnId(user);
		} catch (Exception e) {
			e.printStackTrace();
			ofUserDao.deleteByUserName(ofuser.getUsername());
			commonModel.setStatus("fail");
			commonModel.setMessage("添加用户失败！");
			return commonModel;
		}
		if (!StringUtil.isEmpty(provinceCode) && !StringUtil.isEmpty(cityCode) && !StringUtil.isEmpty(countyCode)) {
			try {
				// 3分配权限
				initTestAgentPermission(dept.getDeptid(), tempjobnum);
			} catch (Exception e) {
				e.printStackTrace();
				ofUserDao.deleteByUserName(ofuser.getUsername());
				insbAgentDao.deleteById(user.getId());
				commonModel.setStatus("fail");
				commonModel.setMessage("添加用户失败！");
				return commonModel;
			}
		}

		CommonModel model2 = appLoginService.loginWithOutPwd(tempjobnum);

		commonModel.setStatus("success");
		commonModel.setMessage("注册成功");
		commonModel.setBody(model2.getBody());

		return commonModel;
	}

	@Override
	public CommonModel fileUpLoadBase64ByCos(HttpServletRequest request,
											 String file, String fileName, String fileType,
											 String fileDescribes, String jobNum, String taskId) {
		// TODO Auto-generated method stub

		CommonModel model = new CommonModel();
		INSBAgent insbAgent = new INSBAgent();
		insbAgent.setJobnum(jobNum);
		insbAgent = insbAgentService.queryOne(insbAgent);
		if (insbAgent == null) {
			model.setStatus("fail");
			model.setMessage("代理人不存在");
			return model;
		}

		// 2015-11-25日修改,可以不用上传流程id
		// if(StringUtil.isEmpty(taskId)){
		// model.setStatus("fail");
		// model.setMessage("任务流水号不能为空");
		// return model;
		// }
		//先清除同一个任务同一个图片类型的记录
		/*if(StringUtil.isNotEmpty(taskId) &&StringUtil.isNotEmpty(fileType)) {
			Map<String,String> params = new HashMap<>();
			params.put("type",fileType);
			params.put("code",taskId);
			insbFilebusinessDao.deleteByFilelibraryByTadkIdImageType(params);
		}*/
		Map<String, Object> map = insbFilelibraryService.uploadOneFileByCos(request, file, fileName, fileType, fileDescribes,
				insbAgent.getAgentcode(),taskId);
		if (map.get("status").equals("success")) {
			model.setStatus("success");
			model.setMessage("上传成功");
			model.setBody(map);

			INSBFilebusiness insbFilebusiness = new INSBFilebusiness();
			insbFilebusiness.setCreatetime(new Date());
			insbFilebusiness.setOperator(null == jobNum ? "" : jobNum);
			insbFilebusiness.setType(fileType);
			insbFilebusiness.setFilelibraryid((String) map.get("fileid"));
			if (!StringUtils.isEmpty(taskId))
				insbFilebusiness.setCode(taskId);
			else
				insbFilebusiness.setCode("");
			try {
				insbFilebusinessDao.insert(insbFilebusiness);
			}catch(Exception ex){
				LogUtil.info("上传,增加 INSBFilebusiness 异常，taskId = "+taskId + " ,ex = " + ex +" msg="+ex.getMessage());
			}
			LogUtil.info("上传保存成功，taskId = "+taskId + " ,jobNum = " + jobNum);
		} else {
			model.setStatus("fail");
			model.setMessage((String) map.get("msg"));
		}
		return model;
	}

}
