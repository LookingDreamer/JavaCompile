package com.lzgapi.yzt.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.exception.ServiceException;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.lzgapi.yzt.bean.CanBindUserValidateBean;
import com.lzgapi.yzt.bean.UptoManagerBean;
import com.lzgapi.yzt.bean.UserBindingValidateBean;
import com.lzgapi.yzt.model.CanBindUserValidateModel;
import com.lzgapi.yzt.model.UserBindingValidateModel;
import com.lzgapi.yzt.service.UserBindingValidateService;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.mobile.model.CommonModelforlzglogin;
import com.zzb.mobile.service.AppLoginService;

import net.sf.json.JSONObject;

@Service
@Transactional
public class UserBindingValidateServiceImpl implements
		UserBindingValidateService {
	@Resource
	private INSBAgentDao insbAgentDao;

	@Resource
	private AppLoginService appLoginService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lzgapi.yzt.service.UserBindingValidateService#getBindingUserList(
	 * java.lang.String)
	 */
	@Override
	public UserBindingValidateBean getBindingUserList(String model)
			throws ServiceException {
		UserBindingValidateBean resultBean = new UserBindingValidateBean();
		if (StringUtil.isNotEmpty(model)) {
			UserBindingValidateModel userModel = (UserBindingValidateModel) JSONObject
					.toBean(JSONObject.fromObject(model),
							UserBindingValidateModel.class);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("openid", userModel.getOpenid());
			map.put("mobile", userModel.getCellphone());
			map.put("idno", userModel.getIdnumber());
			map.put("jobnum", userModel.getUsername());
			map.put("email", userModel.getEmail());

			CommonModelforlzglogin lzgLogin = appLoginService.lzgLogin(
					userModel.getToken(), null);
			if (lzgLogin != null && lzgLogin.getStatus() != null
					&& lzgLogin.getStatus().equals("success")) {

				List<Map<String, Object>> list = insbAgentDao
						.selectListByBindingUser(map);
				LogUtil.info("可绑定的用户:" + list);
				if (list != null && !list.isEmpty() && list.size() > 0) {
					resultBean.setStatus("1");
					List<UserBindingValidateBean.UserValidateBean> userList = new ArrayList<UserBindingValidateBean.UserValidateBean>();
					for (Map<String, Object> agentMap : list) {
						UserBindingValidateBean.UserValidateBean userBean = resultBean.new UserValidateBean();
						for (String key : agentMap.keySet()) {
							if (StringUtil.isNotEmpty(key)
									&& "phone".equals(key)) {
								userBean.setAccount((String) agentMap.get(key));
							} else if (StringUtil.isNotEmpty(key)
									&& "name".equals(key)) {
								userBean.setUsername((String) agentMap.get(key));
							} else if (StringUtil.isNotEmpty(key)
									&& "id".equals(key)) {
								userBean.setUserid((String) agentMap.get(key));
							} else if (StringUtil.isNotEmpty(key)
									&& "agentkind".equals(key)) {
								if((Integer)agentMap.get("agentkind") !=null  && ((Integer)agentMap.get("agentkind")).intValue() == 2){
									userBean.setIsmanager("1");
								}else if((Integer)agentMap.get("agentkind") !=null  && ((Integer)agentMap.get("agentkind")).intValue() == 1){
									userBean.setIsmanager("0");
								}
							} else if (StringUtil.isNotEmpty(key) && "agentcode".equals(key)) {
								userBean.setAgentcode((String) agentMap.get(key));
							} else if (StringUtil.isNotEmpty(key) && "idno".equals(key)) {
								userBean.setIdno((String) agentMap.get(key));
							} else if (StringUtil.isNotEmpty(key) && "deptid".equals(key)) {
								userBean.setOrganization((String) agentMap.get(key));
							}
						}
						userList.add(userBean);
						userBean = null;
					}
					resultBean.setAccounts(userList);
				} else {
					resultBean.setStatus("0");
				}
			} else if (lzgLogin != null && lzgLogin.getStatus() != null
					&& lzgLogin.getStatus().equals("fail")) {
				resultBean.setStatus("9");
				resultBean.setMessage(lzgLogin.getMessage());
			}
		} else {
			resultBean.setStatus("9");
			resultBean.setMessage("传入的字符串为空，null或空字符串");
		}
		return resultBean;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lzgapi.yzt.service.UserBindingValidateService#getUserValidateResult
	 * (java.lang.String)
	 */
	@Override
	public CanBindUserValidateBean getUserValidateResult(String model)
			throws ServiceException {
		CanBindUserValidateBean bean = new CanBindUserValidateBean();
		if (StringUtil.isNotEmpty(model)) {
			CanBindUserValidateModel userModel = (CanBindUserValidateModel) JSONObject
					.toBean(JSONObject.fromObject(model),
							CanBindUserValidateModel.class);
			CommonModelforlzglogin lzgLogin = appLoginService.lzgLogin(
					userModel.getToken(), null);
			if (lzgLogin != null && lzgLogin.getStatus() != null
					&& lzgLogin.getStatus().equals("success")) {

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("jobnum", userModel.getAccount());
				map.put("pwd", StringUtil.md5Base64(userModel.getPassword()));
				Map<String, Object> userMap = insbAgentDao.selectBindingUserByJobnumInfo(map);
				if (userMap == null || userMap.isEmpty()) {
					map.remove("jobnum");
					map.put("phone", userModel.getAccount());
					userMap = insbAgentDao.selectBindingUserByAccountInfo(map);
				}
				LogUtil.info("查询到的用户:" + userMap);
				if (userMap != null && !userMap.isEmpty()) {
					bean.setUserid((String) userMap.get("id"));
					bean.setUsername((String) userMap.get("name"));
					bean.setStatus("OK");
					bean.setMessage("用户验证成功");
					if((Integer)userMap.get("agentkind") !=null  && ((Integer)userMap.get("agentkind")).intValue() == 2){
						bean.setIsmanager("1");
					}else if((Integer)userMap.get("agentkind") !=null  && ((Integer)userMap.get("agentkind")).intValue() == 1){
						bean.setIsmanager("0");
					}
					bean.setAgentcode((String) userMap.get("agentcode"));
					bean.setIdno((String) userMap.get("idno"));
					bean.setOrganization((String) userMap.get("deptid"));
					Map<String, Object> amap = new HashMap<String, Object>();
					amap.put("lzgid", userModel.getManagerid());
					insbAgentDao.updateAgentLzgid(amap);
					Map<String, Object> agentMap = new HashMap<String, Object>();
					agentMap.put("lzgid", userModel.getManagerid());
					agentMap.put("id", (String) userMap.get("id"));
					insbAgentDao.updateAgentLzgidById(agentMap);
				} else {
					bean.setStatus("FAILED");
					bean.setMessage("用户验证失败，账号或密码不正确");
				}
			} else if (lzgLogin != null && lzgLogin.getStatus() != null
					&& lzgLogin.getStatus().equals("fail")) {
				bean.setStatus("FAILED");
				bean.setMessage(lzgLogin.getMessage());
			}
		}
		return bean;
	}

	/**
	 * 懒掌柜升级
	 */
	@Override
	public UptoManagerBean uptoManager(String model) throws ServiceException {
		UptoManagerBean bean = new UptoManagerBean();
		if (StringUtil.isNotEmpty(model)) {
			UptoManagerBean userModel = (UptoManagerBean) JSONObject.toBean(JSONObject.fromObject(model),
							UptoManagerBean.class);
			CommonModelforlzglogin lzgLogin = appLoginService.lzgLogin(
					userModel.getToken(), null);
			if (lzgLogin != null && lzgLogin.getStatus() != null && lzgLogin.getStatus().equals("success")) {
				INSBAgent agent = insbAgentDao.selectById(userModel.getOtheruserid());
				if(agent == null){
					bean.setStatus("FAILED");
					bean.setMessage("用户不存在");
					return bean;
				}
				agent.setJobnum(userModel.getAgentcode());
				agent.setAgentcode(userModel.getAgentcode());
				agent.setEmail(userModel.getEmail());
				agent.setPhone(userModel.getMobile());
				agent.setMobile(userModel.getMobile());
				agent.setIdno(userModel.getIdno());
				agent.setDeptid(userModel.getOrganization());
				agent.setName(userModel.getUsername());
				agent.setJobnumtype(2);
				agent.setAgentstatus(1);
				agent.setAgentkind(2);
				agent.setIstest(2);
				agent.setApprovesstate(3);
				agent.setModifytime(new Date());
				insbAgentDao.updateById(agent);
				bean.setUserid(userModel.getOtheruserid());
				bean.setStatus("OK");
				bean.setMessage("成功");
			}
		}
		return bean;
	}
}
