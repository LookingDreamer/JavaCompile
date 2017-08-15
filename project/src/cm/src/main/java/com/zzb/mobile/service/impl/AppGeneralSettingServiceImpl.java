package com.zzb.mobile.service.impl;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.common.redis.Constants;
import com.common.redis.IRedisClient;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import com.zzb.mobile.model.usercenter.UserCenterReturnModel;
import com.zzb.mobile.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.service.INSCCodeService;
import com.cninsure.system.service.INSCDeptService;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.dao.INSBAgentpermissionDao;
import com.zzb.conf.dao.INSBHolidayareaDao;
import com.zzb.conf.dao.INSBWorktimeDao;
import com.zzb.conf.dao.INSBWorktimeareaDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBAgentpermission;
import com.zzb.conf.entity.INSBHolidayarea;
import com.zzb.conf.entity.INSBRegion;
import com.zzb.conf.entity.INSBWorktime;
import com.zzb.conf.entity.INSBWorktimearea;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.conf.service.INSBPermissionsetService;
import com.zzb.conf.service.INSBRegionService;
import com.zzb.mobile.dao.INSBAgentstandbyinfoDao;
import com.zzb.mobile.entity.INSBAgentstandbyinfo;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.util.EncodeUtils;
@Service
@Transactional
public class AppGeneralSettingServiceImpl implements AppGeneralSettingService {
	@Resource
	private INSBAgentService insbAgentService;
	@Resource
	private INSBPermissionsetService permissionsetService;
	@Resource
	private AppRegisteredService appRegisteredService;
	@Resource
	private INSCDeptService inscDeptService;
	@Resource
	private INSCCodeService inscCodeService;
	@Resource
	private AppLoginService loginService;
	@Resource
	private INSBRegionService insbRegionService;
	@Resource
	private SMSClientService smsClientService;
	@Resource
	private INSBAgentstandbyinfoDao insbAgentstandbyinfoDao;
	@Resource
	private INSBAgentDao insbAgentDao;
	@Resource
	private INSBWorktimeDao insbWorktimeDao;
	@Resource
	private INSBWorktimeareaDao insbWorktimeareaDao;
	@Resource
	private INSBHolidayareaDao insbHolidayareaDao;
	@Resource
	private INSCDeptDao inscDeptDao;
	@Resource
	private INSBAgentpermissionDao agentpermissionDao;

	@Resource
	private IRedisClient redisClient;

	@Resource
	private UserCenterService userCenterService;

	public CommonModel updateMsgAcceptingStatus(String agentJobnum, String message) {
		CommonModel commonModel = new CommonModel();
		try {
			//redisClient.set("msgAcceptingStatus:"+agentJobnum,message);
			INSBAgent agent = new INSBAgent();
			agent.setJobnum(agentJobnum);
			agent = insbAgentService.queryOne(agent);
			INSBAgentstandbyinfo info = new INSBAgentstandbyinfo();
			if(agent != null){
				info.setAgentid(agent.getId());
				info.setNoti("isacceptmessage");
				info = insbAgentstandbyinfoDao.selectOne(info);

				if(info != null){
					//修改
					info.setModifytime(new Date());
					info.setOperator(agent.getName());
					info.setIsacceptmessage(message);
					insbAgentstandbyinfoDao.updateById(info);
				}else{
					//插入
					info = new INSBAgentstandbyinfo();
					info.setId(UUID.randomUUID().toString().replace("-", ""));
					info.setCreatetime(new Date());
					info.setModifytime(new Date());
					info.setOperator(agent.getName());
					info.setNoti("isacceptmessage");
					info.setAgentid(agent.getId());
					info.setIsacceptmessage(message);
					insbAgentstandbyinfoDao.insert(info);
				}
			}else{
				commonModel.setStatus("fail");
				commonModel.setMessage("代理人信息不存在");
				return commonModel;
			}
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("消息接受状态修改失败");
			return commonModel;
		}
		commonModel.setStatus("success");
		commonModel.setMessage("消息接受状态修改成功");
		return commonModel;
	}

	public CommonModel updatePictureUploadQuality(String agentJobnum, String imageCompression) {
		CommonModel commonModel = new CommonModel();
		INSBAgent insbAgent = new INSBAgent();
		insbAgent.setJobnum(agentJobnum);
		insbAgent = insbAgentService.queryOne(insbAgent);
		insbAgent.setCompression(Integer.parseInt(imageCompression));
		insbAgentService.updateById(insbAgent);
		commonModel.setStatus("success");
		commonModel.setMessage("图片上传质量修改成功");
		return commonModel;
	}


	public CommonModel getCommonOutDept(String agentJobnum) {
		CommonModel commonModel = new CommonModel();
		INSBAgent agent = new INSBAgent();
		agent.setJobnum(agentJobnum);
		agent = insbAgentService.queryOne(agent);
		if(agent==null){
			commonModel.setStatus("fail");
			commonModel.setMessage("代理人不存在");
			return commonModel;
		}
//		INSCDept legalPersonDept = inscDeptService.getPlatformDept(agent.getDeptid());//法人
		INSCDept dept = inscDeptDao.selectByComcode(agent.getDeptid());
		String Platform=dept.getParentcodes().substring(24, 34);
		List<Map<String,String>> deptList=inscDeptService.getDeptListByCity(dept.getId(),dept.getCity(),Platform);

		if(deptList.size()==0){
			commonModel.setStatus("fail");
			commonModel.setMessage("获取常用出单网点失败");
		}else{
			commonModel.setStatus("success");
			commonModel.setMessage("获取常用出单网点成功");
			commonModel.setBody(deptList);
		}
		return commonModel;
	}

	private List<Map<String, String>> getDeptListByParentcodes(String code) {
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		INSCDept dept = inscDeptDao.selectByComcode(code);
		List<Map<String, String>> queryTreeListByAgr = inscDeptService.queryTreeList(dept.getUpcomcode());
		for (Map<String, String> map : queryTreeListByAgr) {
			if(dept.getUpcomcode().equals(map.get("id"))){
				list.add(map);
			}
		}
		for (Map<String, String> map : queryTreeListByAgr) {
			if(dept.getUpcomcode().equals(map.get("id"))){
			}else {
				if("05".equals(map.get("comtype"))){
					list.add(map);
				}else{
					list.addAll(this.getDeptListByParentcode(map.get("id")));
				}
			}
		}
		List<Map<String, String>> queryTreeListByCity= inscDeptService.queryTreeListByCity(dept.getUpcomcode(),dept.getCity());
		for (Map<String, String> map : queryTreeListByCity) {
			list.add(map);
		}
		return list;
	}
	private List<Map<String, String>> getDeptListByParentcode(String code) {
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		List<Map<String, String>> queryTreeListByAgr = inscDeptService.queryTreeList(code);
		for (Map<String, String> map : queryTreeListByAgr) {
			if("05".equals(map.get("comtype"))){
				list.add(map);
			}else{
				list.addAll(this.getDeptListByParentcode(map.get("id")));
			}

		}
		return list;
	}

	public CommonModel getCurrCommonOutDept(String agentJobnum) {
		CommonModel commonModel = new CommonModel();
		INSBAgent agent = new INSBAgent();
		agent.setJobnum(agentJobnum);
		agent = insbAgentService.queryOne(agent);
		if(agent==null){
			commonModel.setStatus("fail");
			commonModel.setMessage("代理人不存在");
			return commonModel;
		}
		if(agent.getDeptid()==null){
			commonModel.setStatus("fail");
			commonModel.setMessage("尚未设置常用出单网点");
			return commonModel;
		}
		INSCDept dept = inscDeptService.queryById(agent.getDeptid());
		Map<String, Object> map=new HashMap<String,Object>();
		map.put("deptName", dept.getShortname());
		map.put("deptAddress", dept.getAddress());
		commonModel.setStatus("success");
		commonModel.setMessage("获取当前常用出单网点成功");
		commonModel.setBody(map);
		return commonModel;
	}

	public CommonModel updateAgentPhone(String phone, String validatecode,
										String agentJobnum) {
		CommonModel model =new CommonModel();
		String[] codeStr = null; // 从redis中取出验证码
		try{
			String codeStrs=(String)redisClient.get(Constants.PHONE, phone);
			codeStr = codeStrs.split("_");
		}catch(Exception e){
			model.setStatus("fail");
			model.setMessage("验证码失效！");
			return model;
		}
		long timeMills =  Long.parseLong(codeStr[1]);
		long time = (System.currentTimeMillis()-timeMills)/1000;

		INSCCode code = new INSCCode();
		code.setCodetype("validateCodeFailureTime");
		code.setCodename("验证码失效时间");
		long ditime=Long.parseLong(inscCodeService.queryOne(code).getCodevalue());
		if(time>ditime){
			model.setStatus("fail");
			model.setMessage("验证码失效！");
		}else{
			if(validatecode.equals(codeStr[0])){
				INSBAgent agent = new INSBAgent();
				agent = new INSBAgent();
				agent.setJobnum(agentJobnum);
				agent = insbAgentService.queryOne(agent);
				if(agent==null){
					model.setStatus("fail");
					model.setMessage("用户不存在！");
				}else{
					String oldMobile = StringUtil.isEmpty(agent.getMobile())?agent.getPhone():agent.getMobile();//hwc 20170602 add
					agent.setMobile(phone);
					agent.setPhone(phone);
					insbAgentService.updateById(agent);
					//LdapMgr mgr =new LdapMgr();
					//mgr.modifyMobile(agent.getJobnum(), phone); 
					redisClient.del(Constants.PHONE, phone);
					model.setStatus("success");
					model.setMessage("手机号修改成功！");
					/**
					 * 通知集团统一用户中心 hwc 添加 20170602
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
				}

			}else{
				model.setStatus("fail");
				model.setMessage("验证失败！");
			}
		}

		return model;
	}

	public CommonModel sendValidateCode(String phone, String agentJobnum) {
		CommonModel model =new CommonModel();
		INSBAgent agent = new INSBAgent();
		agent.setPhone(phone);
		List<INSBAgent> list = insbAgentDao.selectNotminiAgent(phone);
		//判断手机号是否重复
		if(list==null||list.size()==0){

			/**
			 * 调用集团用户中心验证手机号是否注册 hwc add 20170601
			 */
			/*Map<String,String> params = new HashMap<String ,String>();
			params.put("phoneno",phone);
			UserCenterReturnModel userCenterReturnModel =userCenterService.validateMobileNo(params);
			if("fail".equals(userCenterReturnModel.getStatus())){
				model.setStatus(CommonModel.STATUS_FAIL);
				model.setMessage("该手机已注册！");
				return model;
			}*/
			//end 20170601 hwc

			//生成六位验证码
			String validatecode = EncodeUtils.generateValidateCode(6);
			// 将手机号、验证码、验证码生成时间保存到数据库
			try {
				redisClient.set(Constants.PHONE, phone, validatecode+"_"+String.valueOf(System.currentTimeMillis()),5*60); // 验证码放入redis
				/*
				 * 给phone发送验证码validateCode
				 */
				smsClientService.sendMobileValidateCode(phone, validatecode);

				Map<Object, Object> responseMap = new HashMap<Object, Object>();
				responseMap.put("validatecode", validatecode);
				model.setStatus("success");
				model.setMessage("验证码发送成功！");
				model.setBody(responseMap);
			} catch (Exception e) {
				e.printStackTrace();
				model.setStatus("fail");
				model.setMessage("验证码发送失败！");
			}
		}else{
			model.setStatus("fail");
			model.setMessage("手机号重复，请更换其他号码！");
		}
		return model;
	}

	@Override
	public CommonModel getAllProvince() {
		CommonModel commonModel = new CommonModel();
		Map list = insbRegionService.initList("0");
		commonModel.setStatus("success");
		commonModel.setMessage("获取所有省成功");
		commonModel.setBody(list);
		return commonModel;
	}

	@Override
	public CommonModel getAllCity(String parentcode) {
		CommonModel commonModel = new CommonModel();
		Map<String, Object> list = insbRegionService.getAllCity(parentcode);
		commonModel.setStatus("success");
		commonModel.setMessage("根据省代码获取市列表成功");
		commonModel.setBody(list);
		return commonModel;
	}

	@Override
	public CommonModel getAllCountry(String parentcode) {
		CommonModel commonModel = new CommonModel();
		Map<String, Object> list = insbRegionService.getAllCountry(parentcode);
		commonModel.setStatus("success");
		commonModel.setMessage("根据市代码获取区列表成功");
		commonModel.setBody(list);
		return commonModel;
	}

	@Override
	public CommonModel getAllCityForPS(String parentcode) {
		CommonModel commonModel = new CommonModel();
		Map list = insbRegionService.initList(parentcode);
		commonModel.setStatus("success");
		commonModel.setMessage("获取所有地市");
		commonModel.setBody(list);
		return commonModel;
	}
	@Override
	public CommonModel getAllCountryForPS(String parentcode) {
		CommonModel commonModel = new CommonModel();
		Map list = insbRegionService.initList(parentcode);
		commonModel.setStatus("success");
		commonModel.setMessage("获取所有区县");
		commonModel.setBody(list);
		return commonModel;
	}

	@Override
	public CommonModel getAllProvincefromSD() {
		CommonModel commonModel = new CommonModel();
		Map<String, String> para = new HashMap<String, String>();
		para.put("parentcode", "0");
		/*para.put("comcode", "370000");*///山东省
		Map list = insbRegionService.initListfromSD(para);
		/*List result = (List)list.get("result");
		para.put("comcode", "440000");//广东省
		result.addAll((List)insbRegionService.initListfromSD(para).get("result"));
		para.put("comcode", "510000");//四川
		result.addAll((List)insbRegionService.initListfromSD(para).get("result"));
		para.put("comcode", "610000");//陕西
		result.addAll((List)insbRegionService.initListfromSD(para).get("result"));
		para.put("comcode", "420000");//湖北
		result.addAll((List)insbRegionService.initListfromSD(para).get("result"));
		para.put("comcode", "430000");//湖南
		result.addAll((List)insbRegionService.initListfromSD(para).get("result"));
		para.put("comcode", "410000");//河南
		result.addAll((List)insbRegionService.initListfromSD(para).get("result"));
		para.put("comcode", "500000");//重庆
		result.addAll((List)insbRegionService.initListfromSD(para).get("result"));*/

		commonModel.setStatus("success");
		commonModel.setMessage("获取所有省成功");
		commonModel.setBody(list);
		return commonModel;
	}

	@Override
	public CommonModel getRegionByCode(String code) {
		CommonModel commonModel = new CommonModel();
		INSBRegion insbRegion = new INSBRegion();
		insbRegion.setComcode(code);
		INSBRegion region = insbRegionService.queryOne(insbRegion);
		if(region==null){
			commonModel.setStatus("fail");
			commonModel.setMessage("区域不存在");
			return commonModel;
		}
		commonModel.setStatus("success");
		commonModel.setMessage("根据代码获取区域成功");
		Map<String, String> map = new HashMap<String, String>();
		map.put("comcodename", region.getComcodename());
		map.put("comcode", region.getComcode());
		commonModel.setBody(map);
		return commonModel;
	}

	@Override
	public CommonModel getRegionByParentCode(String parentcode) {
		CommonModel commonModel = new CommonModel();
		Map list = insbRegionService.initList(parentcode);
		commonModel.setStatus("success");
		commonModel.setMessage("根据上级代码获取区域列表成功");
		commonModel.setBody(list);
		return commonModel;
	}

	@Override
	public CommonModel queryGeneralSetting(String jobnum) {
		CommonModel commonModel = new CommonModel();
		Map<String,Object> map = new HashMap<String,Object>();
		INSBAgent insbAgent = new INSBAgent();
		insbAgent.setJobnum(jobnum);
		insbAgent = insbAgentService.queryOne(insbAgent);
		if(insbAgent==null){
			commonModel.setStatus("fail");
			commonModel.setMessage("代理人不存在");
			return commonModel;
		}
		//查询信息接受状态
		String msgAcceptingStatus=null;
		try {
//			msgAcceptingStatus = (String)redisClient.get("msgAcceptingStatus:"+jobnum);
			INSBAgentstandbyinfo info = new INSBAgentstandbyinfo();
			info.setAgentid(insbAgent.getId());
			info.setNoti("isacceptmessage");
			info = insbAgentstandbyinfoDao.selectOne(info);
			if(info != null){
				msgAcceptingStatus = info.getIsacceptmessage();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		INSCDept inscDept = new INSCDept();
		if(!StringUtils.isEmpty(insbAgent.getCommonusebranch())){
			inscDept.setComcode(insbAgent.getCommonusebranch());
			inscDept = inscDeptService.queryOne(inscDept);
			map.put("branchCode", inscDept.getComcode());
			map.put("branchName", inscDept.getShortname());
			map.put("branchAddress", inscDept.getAddress());
		}else{
			map.put("branchCode", null);
			map.put("branchName", null);
			map.put("branchAddress", null);
		}
		map.put("renewaltime", (insbAgent.getRenewaltime()!=null?insbAgent.getRenewaltime()+"":null));
		map.put("msgAcceptingStatus", (msgAcceptingStatus==null?"1":msgAcceptingStatus));
		Integer pictureUploadQuality = insbAgent.getCompression();
		map.put("pictureUploadQuality", (pictureUploadQuality==null?"1":pictureUploadQuality+""));
		commonModel.setBody(map);
		commonModel.setStatus("success");
		commonModel.setMessage("查询通用设置成功");
		return commonModel;
	}

	@Override
	public CommonModel queryWorkTime(String agentJobnum) {
		CommonModel model = new CommonModel();
		INSBAgent agent = insbAgentDao.selectByJobnum(agentJobnum);
		if(agent != null){
			INSCDept dept = inscDeptDao.selectByComcode(agent.getDeptid());
			if(dept != null){
				INSBWorktime worktime = insbWorktimeDao.selectOneBydeptId(dept.getId());
				if(worktime != null){
					Map<Object, Object> wtmap = new HashMap<Object, Object>();
					String insbworktimeid = worktime.getId();
					Map<String, String> map1 = new HashMap<String, String>(); // 查询工作时间参数
					Map<String, String> map2 = new HashMap<String, String>(); // 查询工作时间提醒时间参数
					Map<String, String> map3 = new HashMap<String, String>(); // 查询节假日时间参数
					Map<String, String> map4 = new HashMap<String, String>(); // 查询节假日值班时间参数
					if (insbworktimeid != null) {
						map1.put("insbworktimeid", insbworktimeid);
						map1.put("isremindtime", "0");
						map2.put("insbworktimeid", insbworktimeid);
						map2.put("isremindtime", "1");
						map3.put("insbworktimeid", insbworktimeid);
						map3.put("isonduty", "0");
						map4.put("insbworktimeid", insbworktimeid);
						map4.put("isonduty", "1");
					}
					List<INSBWorktimearea> worktimeAreaList = insbWorktimeareaDao
							.selectByWorktimeId(map1); // 工作时间list
					List<INSBWorktimearea> remindWorkTimeAreaList = insbWorktimeareaDao
							.selectByWorktimeId(map2); // 工作时间提醒时间list
					List<INSBHolidayarea> holidaylist = insbHolidayareaDao
							.selectByWorktimeId(map3); // 节假日list
					List<INSBHolidayarea> holidayworklist = insbHolidayareaDao
							.selectByWorktimeId(map4); // 节假日值班list
					if (worktimeAreaList != null) {
						wtmap.put("workdaylist", worktimeAreaList); // 工作时间list
					}
					if (remindWorkTimeAreaList != null) {
						wtmap.put("workdayremindList", remindWorkTimeAreaList); // 工作时间提醒时间list
					}
					if (holidaylist != null) {
						wtmap.put("holidaylist", holidaylist); // 节假日list
					}
					if (holidayworklist != null) {
						wtmap.put("holidayworklist", holidayworklist); // 节假日值班时间段list
					}
					model.setBody(wtmap);
				}else{
					model.setBody(null);
				}
				model.setStatus("success");
				model.setMessage("查询成功");
			}else{
				model.setStatus("fail");
				model.setMessage("代理人所属机构不存在");
			}
		}else{
			model.setStatus("fail");
			model.setMessage("代理人不存在");
		}
		return model;
	}

	@Override
	public CommonModel updateRegioncode(String jobNum, String provinceCode,
										String cityCode, String countyCode) {
		CommonModel model = new CommonModel();
		INSBAgent insbAgent = insbAgentService.getAgent(jobNum);
		if(insbAgent==null){
			model.setStatus("fail");
			model.setMessage("代理人不存在");
		}else{
			INSBAgentpermission agentpermission = new INSBAgentpermission();
			agentpermission.setAgentid(insbAgent.getId());
			if(agentpermissionDao.selectList(agentpermission).size()>0){//如果已经分配权限，不能再次修改
				model.setStatus("fail");
				model.setMessage("已经分配权限，不能修改地区");
				return model;
			}
			INSCDept dept=null;
			CommonModel commonModel = appRegisteredService.getDeptByArea(provinceCode, cityCode, countyCode);
			if("fail".equals(commonModel.getStatus())){
				return commonModel;
			}else{
				dept = (INSCDept) commonModel.getBody();
			}
			insbAgent.setDeptid(dept.getComcode());
			insbAgent.setModifytime(new Date());
			insbAgentDao.updateById(insbAgent);

			try {
				permissionsetService.initTestAgentPermission(dept.getDeptid(), insbAgent.getTempjobnum());
			} catch (Exception e) {
				e.printStackTrace();
				model.setStatus("fail");
				model.setMessage("修改地区失败");
				return model;
			}
		}
		Map<String,String> map = new HashMap<String, String>();
		map.put("provinceName", insbRegionService.queryById(provinceCode).getComcodename());
		map.put("cityName", insbRegionService.queryById(cityCode).getComcodename());
		map.put("countyName", insbRegionService.queryById(countyCode).getComcodename());
		model.setBody(map);
		model.setStatus("success");
		model.setMessage("修改地区成功");
		return model;
	}

	@Override
	public CommonModel getRegisterProvince() {
		CommonModel commonModel = new CommonModel();
		List<Map<String, String>> list = insbRegionService.getRegisterProvince();
		commonModel.setStatus("success");
		commonModel.setMessage("获取所有省成功");
		commonModel.setBody(list);
		return commonModel;
	}

}
