package com.lzgapi.yzt.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.exception.ServiceException;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.service.INSCDeptService;
import com.lzgapi.yzt.bean.RegisterOrBecomeAgentBean;
import com.lzgapi.yzt.model.RegisterOrBecomeAgentModel;
import com.lzgapi.yzt.service.RegisterOrBecomeAgentService;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBCertification;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.conf.service.INSBCertificationService;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.CommonModelforlzglogin;
import com.zzb.mobile.service.AppLoginService;
import com.zzb.mobile.service.AppRegisteredService;

import net.sf.json.JSONObject;

@Service
@Transactional
public class RegisterOrBecomeAgentServiceImpl implements
		RegisterOrBecomeAgentService {
	@Resource
	private AppLoginService appLoginService;
	@Resource
	private INSBAgentService insbAgentService;
	@Resource
	private AppRegisteredService appRegisteredService;
	@Resource
	private INSBAgentDao insbAgentDao;
	@Resource
	private INSCDeptService inscDeptService;
	@Resource
	private INSBCertificationService insbCertificationService;

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lzgapi.yzt.service.RegisterOrBecomeAgentService#getAgentInfo(java
	 * .lang.String)
	 */
	@Override
	public RegisterOrBecomeAgentBean getAgentInfo(String model)
			throws ServiceException {
		RegisterOrBecomeAgentBean bean = new RegisterOrBecomeAgentBean();
		if (StringUtil.isNotEmpty(model)) {
			RegisterOrBecomeAgentModel agentModel = (RegisterOrBecomeAgentModel) JSONObject
					.toBean(JSONObject.fromObject(model),
							RegisterOrBecomeAgentModel.class);
			CommonModelforlzglogin lzgLogin = appLoginService.lzgLogin(
					agentModel.getToken(), null);
			if (lzgLogin != null && lzgLogin.getStatus() != null
					&& lzgLogin.getStatus().equals("success")) {
				if (agentModel != null
						&& StringUtil.isEmpty(agentModel.getUserid())) {
					bean = getRegisterAgent(agentModel);
				} 
				else if (agentModel != null
						&& StringUtil.isNotEmpty(agentModel.getUserid())) {
					INSBAgent agent = insbAgentDao.selectById(agentModel.getUserid());
					if(agent != null){
						bean = getBecomeAgent(agentModel,agent.getDeptid(),agent.getTempjobnum());
					}else{
						bean.setStatus("FAILED");
						bean.setMessage("未找到【第三方平台用户id】为【" + agentModel.getUserid()+"】的普通用户");
					}
				}
			} else if (lzgLogin != null && lzgLogin.getStatus() != null
					&& lzgLogin.getStatus().equals("fail")) {
				bean.setStatus("FAILED");
				bean.setMessage(lzgLogin.getMessage());
			}
		}
		return bean;
	}
	
	public RegisterOrBecomeAgentBean getCertificationInfo(
			RegisterOrBecomeAgentModel agentModel,String deptId,String tempjobnum
			) {
		RegisterOrBecomeAgentBean bean = new RegisterOrBecomeAgentBean();
		/*
		INSBAgent insbAgent = new INSBAgent();
		insbAgent.setJobnum(agentModel.getAgentnum());
		insbAgent = insbAgentService.queryOne(insbAgent);
		if (insbAgent == null) {
			bean.setStatus("FAILED");
			bean.setMessage("代理人不存在");
			return bean;
		}
		*/
		INSBCertification insbCertification = new INSBCertification();
		insbCertification.setId(UUIDUtils.random());
		insbCertification.setOperator(tempjobnum);
		insbCertification.setCreatetime(new Date());
		insbCertification.setNoti(agentModel.getNoti());
		insbCertification.setAgentnum(tempjobnum);
		insbCertification.setIdcardpositive(agentModel.getIdcardpositive());
		insbCertification.setIdcardopposite(agentModel.getIdcardopposite());
		insbCertification.setBankcardpositive(agentModel.getBankcardpositive());
		insbCertification.setQualificationpositive(agentModel.getQualificationpositive());
		insbCertification.setQualificationpage(agentModel.getQualificationpage());
		
		/*insbCertification.setDeptid(inscDeptService.getOrgDept(
				insbAgent.getDeptid()).getId());*/
		insbCertification.setDeptid(deptId);
		
		insbCertification.setAgree(Integer.parseInt(agentModel.getAgree()));
		insbCertification.setMainbiz(agentModel.getMainbiz());
		insbCertificationService.insert(insbCertification);
		/*insbAgent.setApprovesstate(2);// 认证状态改为认证中
		insbAgentService.updateById(insbAgent);*/
		bean.setStatus("OK");
		bean.setMessage("保存认证信息成功");
		return bean;
	}

	/**
	 * 注册成为代理人
	 * 
	 * @param agentModel
	 * @return
	 * @throws ServiceException
	 */
	public RegisterOrBecomeAgentBean getRegisterAgent(
			RegisterOrBecomeAgentModel agentModel) throws ServiceException {
		RegisterOrBecomeAgentBean bean = new RegisterOrBecomeAgentBean();
		INSBAgent user = new INSBAgent();
		INSBAgent iNSBAgent = new INSBAgent();
		String name = agentModel.getName();
		String phone = agentModel.getPhone();
		String passWord = agentModel.getPassWord();
		String referrerJobNum = agentModel.getRefNum();

		String openid = null;
		if (StringUtil.isNotEmpty(agentModel.getOpenid()))
			openid = agentModel.getOpenid();
		iNSBAgent.setPhone(phone);
		iNSBAgent.setMobile(phone);
		INSBAgent temp = insbAgentService.queryOne(iNSBAgent);
		// 判断手机号是否已存在
		if (temp != null) {
			bean.setStatus("FAILED");
			bean.setMessage("Phone number already exists");
			return bean;
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
		 * commonModel.setStatus("fail");
		 * commonModel.setMessage("Registration failed");
		 * commonModel.setBody("Referrer does not exist"); return commonModel; }
		 */// 100000031 123456
		user.setChannelid(referrerJobNum);
		user.setJobnumtype(1);
		user.setAgentstatus(1);
		user.setApprovesstate(2);// 认证状态改为认证中
		// 根据省、市、区 ，查询代理机构
		INSCDept dept = new INSCDept();
		CommonModel commonModel = new CommonModel();

		if (StringUtil.isNotEmpty(agentModel.getArea())) {
			String[] areaArray = agentModel.getArea().split("-");
			commonModel = appRegisteredService.getDeptByArea(areaArray[0],
					areaArray[1], areaArray[2]);
		}

		if ("fail".equals(commonModel.getStatus())) {
			bean.setStatus("FAILED");
			bean.setMessage(commonModel.getMessage());
			return bean;
		} else {
			dept = (INSCDept) commonModel.getBody();
		}

		user.setDeptid(dept.getComcode());
		user.setRegistertime(new Date());

		// 生成临时工号
		String tempjobnum = insbAgentService.updateAgentTempJobNo() + "";
		user.setTempjobnum(tempjobnum);
		user.setJobnum(tempjobnum);
		user.setCreatetime(new Date());
		user.setAgentcode(tempjobnum);
		user.setIstest(Integer.valueOf(2));//普通账户

		// 2 保存注册信息
		try {
			insbAgentDao.insertReturnId(user);
		} catch (Exception e) {
			e.printStackTrace();
			bean.setStatus("FAILED");
			bean.setMessage("添加用户失败！");
			return bean;
		}
		try {
			// 3分配权限
			appRegisteredService.initTestAgentPermission(dept.getDeptid(),
					tempjobnum);
		} catch (Exception e) {
			e.printStackTrace();
			//ldapMgr.deleteEntity(model.getEmployeeNumber());
			insbAgentDao.deleteById(user.getId());
			bean.setStatus("FAILED");
			bean.setMessage("添加用户失败！");
			return bean;
		}
		bean= getBecomeAgent(agentModel,dept.getDeptid(),tempjobnum);
		return bean;
	}

	public RegisterOrBecomeAgentBean getBecomeAgent(
			RegisterOrBecomeAgentModel agentModel,String deptId,String tempjobnum) throws ServiceException {
		RegisterOrBecomeAgentBean bean = new RegisterOrBecomeAgentBean();
		RegisterOrBecomeAgentBean certification = getCertificationInfo(agentModel,deptId,tempjobnum);
		if(certification!=null){
			LogUtil.info(certification.getMessage());
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("phone", agentModel.getPhone());
		map.put("pwd", StringUtil.md5Base64(agentModel.getPassWord()));
		Map<String, Object> userMap = insbAgentDao
				.selectBindingUserByAccountInfo(map);
		if (userMap != null && !userMap.isEmpty()) {
			bean.setStatus("OK");
			bean.setUserid((String) userMap.get("id"));
			bean.setMessage("注册成功");
			bean.setUsername((String) userMap.get("name"));
			bean.setAccount(agentModel.getPhone());
			if ((Integer) userMap.get("agentkind") != null
					&& ((Integer) userMap.get("agentkind")).intValue() == 2) {
				bean.setIsmanager("1");
			} else if ((Integer) userMap.get("agentkind") != null
					&& ((Integer) userMap.get("agentkind")).intValue() == 1) {
				bean.setIsmanager("0");
			}
			Map<String, Object> agentMap = new HashMap<String, Object>();
			agentMap.put("lzgid", agentModel.getManagerid());
			agentMap.put("id", (String) userMap.get("id"));
			insbAgentDao.updateAgentLzgidById(agentMap);
		}
		
		return bean;
	}
	
}
