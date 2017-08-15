package com.lzgapi.yzt.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.service.INSCDeptService;
import com.common.LZGUtil;
import com.lzgapi.yzt.model.AgentLzgModel;
import com.lzgapi.yzt.model.UptomanagerModel;
import com.lzgapi.yzt.model.UserRegisterModel;
import com.lzgapi.yzt.service.UserAPIService;
import com.zzb.cm.service.INSBFilelibraryService;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.dao.OFUserDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.OFUser;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.conf.service.INSBCertificationService;
import com.zzb.conf.service.INSBPermissionsetService;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.CommonModelforlzglogin;
import com.zzb.mobile.service.AppLoginService;
import com.zzb.mobile.service.AppRegisteredService;
import com.zzb.mobile.service.SMSClientService;
import com.zzb.mobile.util.EncodeUtils.Md5Encodes;

import net.sf.json.JSONObject;

@Service
@Transactional
public class UserAPIServiceImpl implements UserAPIService {

	
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
	private SMSClientService smsClientService;
	@Resource
	private AppRegisteredService appRegisteredService;
	
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public UserRegisterModel register(String regInfoJSON) {
		UserRegisterModel userModel = new UserRegisterModel();
		if(StringUtils.isEmpty(regInfoJSON)){
			userModel.setStatus("FAILED");
			userModel.setMessage("参数不能为空");
			return userModel;
		}
		INSBAgent user = new INSBAgent();    
		INSBAgent iNSBAgent = new INSBAgent();
		JSONObject jsonObject = JSONObject.fromObject(regInfoJSON);
		String name = "";
		String province = "",city = "",country = "";
		if(!jsonObject.containsKey("token") || StringUtils.isEmpty(jsonObject.getString("token"))){
			userModel.setStatus("FAILED");
			userModel.setMessage("token不能为空");
			return userModel;
		}else{
			String token = jsonObject.getString("token");
			CommonModelforlzglogin model = appLoginService.lzgLogin(token, null);
			if("fail".equals(model.getStatus())){
				userModel.setStatus("FAILED");
				userModel.setMessage(model.getMessage());
				return userModel;
			}
			JSONObject resultJSN = (JSONObject) model.getValidateData();
			if(resultJSN.containsKey("name") && !StringUtils.isEmpty(resultJSN.getString("name"))){
				name = resultJSN.getString("name");
			}
			if(!resultJSN.containsKey("province") || StringUtils.isEmpty(resultJSN.getString("province"))){
				userModel.setStatus("FAILED");
				userModel.setMessage("用户所在省不能为空");
				return userModel;
			}else{
				province = resultJSN.getString("province");
			}
			if(!resultJSN.containsKey("city") || StringUtils.isEmpty(resultJSN.getString("city"))){
				userModel.setStatus("FAILED");
				userModel.setMessage("用户所在市不能为空");
				return userModel;
			}else{
				city = resultJSN.getString("city");
			}
			if(!resultJSN.containsKey("county") || StringUtils.isEmpty(resultJSN.getString("county"))){
				userModel.setStatus("FAILED");
				userModel.setMessage("用户所在区不能为空");
				return userModel;
			}else{
				country = resultJSN.getString("county");
			}
		}
		
		String phone = null;
		if(!jsonObject.containsKey("phone") || StringUtils.isEmpty(jsonObject.getString("phone"))){
			userModel.setStatus("FAILED");
			userModel.setMessage("手机号不能为空");
			return userModel;
		}else{
			phone = jsonObject.getString("phone");
		}
		String passWord = null;
		if(!jsonObject.containsKey("password") || StringUtils.isEmpty(jsonObject.getString("password"))){
			userModel.setStatus("FAILED");
			userModel.setMessage("密码不能为空");
			return userModel;
		}else{
			passWord = jsonObject.getString("password");
		}
		String lzgid = null;
		if(!jsonObject.containsKey("managerid") || StringUtils.isEmpty(jsonObject.getString("managerid"))){
			userModel.setStatus("FAILED");
			userModel.setMessage("懒掌柜ID不能为空");
			return userModel;
		}else{
			lzgid = jsonObject.getString("managerid");
		}
		//String referrerJobNum = jsonObject.getString("refNum");
		String openid = null;
		if(jsonObject.containsKey("openid") && !StringUtils.isEmpty(jsonObject.getString("openid"))){
			openid = jsonObject.getString("openid");
		}
		iNSBAgent.setPhone(phone);
		iNSBAgent.setMobile(phone);
		INSBAgent temp = insbAgentService.queryOne(iNSBAgent);
		//判断手机号是否已存在
		if(temp!=null){
			userModel.setStatus("FAILED");
			userModel.setMessage("手机号码已经存在");
			return userModel;
		}
		user.setName(name);
		user.setPhone(phone);
		user.setMobile(phone);
		user.setPwd(StringUtil.md5Base64(passWord));
		user.setOpenid(openid);
		user.setLzgid(lzgid);
		//user.setChannelid(referrerJobNum);
		user.setJobnumtype(1);
		user.setAgentstatus(1);
		user.setApprovesstate(1);
		user.setAgentkind(1);
		user.setIstest(Integer.valueOf(2));
		//根据省、市、区 ，查询代理机构
		INSCDept dept = new INSCDept();
		userModel = this.getDeptByArea(province, city, country);
		if("fail".equals(userModel.getStatus())){
			return userModel;
		}else{
			dept = (INSCDept) userModel.getBody();
			userModel.setBody(null);
		}
		
		user.setDeptid(dept.getComcode());
		user.setRegistertime(new Date());
		
		//生成临时工号
		String tempjobnum=insbAgentService.updateAgentTempJobNo() + "";
		user.setTempjobnum(tempjobnum);
		user.setJobnum(tempjobnum);
		user.setCreatetime(new Date());
		user.setAgentcode(tempjobnum);

		//1插入到ofuser表中
		OFUser ofuser = new OFUser();
		ofuser.setUsername(tempjobnum);
//		ofuser.setPlainPassword(passWord);
		ofuser.setPlainPassword(StringUtil.md5Base64(passWord));
		ofuser.setName(name);
		ofuser.setCreationDate(new Date().getTime());
		ofuser.setModificationDate(new Date().getTime());
		ofUserDao.insert(ofuser);
		
		//2 保存注册信息
    	try{
    		insbAgentDao.insertReturnId(user);
		}catch(Exception e){
    		e.printStackTrace();
    		ofUserDao.deleteByUserName(ofuser.getUsername());
    		userModel.setStatus("fail");
    		userModel.setMessage("添加用户失败！");
			return userModel;
		 }
    	try{
    		//3分配权限
    		initTestAgentPermission(dept.getDeptid(), tempjobnum);
    	}catch(Exception e){
    		e.printStackTrace();
    		ofUserDao.deleteByUserName(ofuser.getUsername());
		    insbAgentDao.deleteById(user.getId());
		    userModel.setStatus("fail");
		    userModel.setMessage("添加用户失败！");
			return userModel;
		}
    	userModel.setStatus("OK");
    	userModel.setMessage("注册成功");
    	userModel.setIsmanager("0");
    	userModel.setUserid(user.getId());
    	userModel.setUsername(name);
		
		return userModel;
	}
	
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public UserRegisterModel registerAuto(String token) {
		UserRegisterModel userModel = new UserRegisterModel();
		if(StringUtils.isEmpty(token)){
			userModel.setStatus("FAILED");
			userModel.setMessage("参数不能为空");
			return userModel;
		}
		INSBAgent user = new INSBAgent();    
		INSBAgent iNSBAgent = new INSBAgent();
		String name = "";
		String province = "",city = "",country = "";
		CommonModelforlzglogin model = appLoginService.lzgLogin(token, null);
		if("fail".equals(model.getStatus())){
			userModel.setStatus("FAILED");
			userModel.setMessage(model.getMessage());
			return userModel;
		}
		JSONObject resultJSN = (JSONObject) model.getValidateData();
		if(resultJSN.containsKey("name") && !StringUtils.isEmpty(resultJSN.getString("name"))){
			name = resultJSN.getString("name");
		}
		if(!resultJSN.containsKey("province") || StringUtils.isEmpty(resultJSN.getString("province"))){
			userModel.setStatus("FAILED");
			userModel.setMessage("用户所在省不能为空");
			return userModel;
		}else{
			province = resultJSN.getString("province");
		}
		if(!resultJSN.containsKey("city") || StringUtils.isEmpty(resultJSN.getString("city"))){
			userModel.setStatus("FAILED");
			userModel.setMessage("用户所在市不能为空");
			return userModel;
		}else{
			city = resultJSN.getString("city");
		}
		if(!resultJSN.containsKey("county") || StringUtils.isEmpty(resultJSN.getString("county"))){
			userModel.setStatus("FAILED");
			userModel.setMessage("用户所在区不能为空");
			return userModel;
		}else{
			country = resultJSN.getString("county");
		}
		
		String phone = null;
		if(resultJSN.containsKey("mobile") && !StringUtils.isEmpty(resultJSN.getString("mobile"))){
			phone = resultJSN.getString("mobile");
		}
		String passWord = "123456";
		String lzgid = null;
		if(!resultJSN.containsKey("id") || StringUtils.isEmpty(resultJSN.getString("id"))){
			userModel.setStatus("FAILED");
			userModel.setMessage("懒掌柜ID不能为空");
			return userModel;
		}else{
			lzgid = resultJSN.getString("id");
		}
		//String referrerJobNum = jsonObject.getString("refNum");
		iNSBAgent.setPhone(phone);
		iNSBAgent.setMobile(phone);
		INSBAgent temp = insbAgentService.queryOne(iNSBAgent);
		//判断手机号是否已存在
		if(temp!=null){
			userModel.setStatus("FAILED");
			userModel.setMessage("手机号码已经存在");
			return userModel;
		}
		user.setName(name);
		user.setPhone(phone);
		user.setMobile(phone);
		user.setPwd(StringUtil.md5Base64(passWord));
		user.setLzgid(lzgid);
		//user.setChannelid(referrerJobNum);
		user.setJobnumtype(1);
		user.setAgentstatus(1);
		user.setApprovesstate(1);
		user.setAgentkind(1);
		user.setIstest(Integer.valueOf(2));
		//根据省、市、区 ，查询代理机构
		INSCDept dept = new INSCDept();
		userModel = this.getDeptByArea(province, city, country);
		if("fail".equals(userModel.getStatus())){
			return userModel;
		}else{
			dept = (INSCDept) userModel.getBody();
			userModel.setBody(null);
		}
		
		user.setDeptid(dept.getComcode());
		user.setRegistertime(new Date());
		
		//生成临时工号
		String tempjobnum=insbAgentService.updateAgentTempJobNo() + "";
		user.setTempjobnum(tempjobnum);
		user.setJobnum(tempjobnum);
		user.setCreatetime(new Date());
		user.setAgentcode(tempjobnum);

		//1插入到ofuser表中
		OFUser ofuser = new OFUser();
		ofuser.setUsername(tempjobnum);
//		ofuser.setPlainPassword(passWord);
		ofuser.setPlainPassword(StringUtil.md5Base64(passWord));
		ofuser.setName(name);
		ofuser.setCreationDate(new Date().getTime());
		ofuser.setModificationDate(new Date().getTime());
		ofUserDao.insert(ofuser);
		
		//2 保存注册信息
    	try{
    		insbAgentDao.insertReturnId(user);
		}catch(Exception e){
    		e.printStackTrace();
    		ofUserDao.deleteByUserName(ofuser.getUsername());
    		userModel.setStatus("fail");
    		userModel.setMessage("添加用户失败！");
			return userModel;
		 }
    	try{
    		//3分配权限
    		initTestAgentPermission(dept.getDeptid(), tempjobnum);
    	}catch(Exception e){
    		e.printStackTrace();
    		ofUserDao.deleteByUserName(ofuser.getUsername());
		    insbAgentDao.deleteById(user.getId());
		    userModel.setStatus("fail");
		    userModel.setMessage("添加用户失败！");
			return userModel;
		}
    	userModel.setStatus("OK");
    	userModel.setMessage("注册成功");
    	userModel.setIsmanager("0");
    	userModel.setUserid(user.getId());
    	userModel.setUsername(name);
    	userModel.setLzgid(lzgid);
    	if(phone != null){
    		userModel.setLoginame(phone);
    		//向用户发送注册成功的提示短信
    		try {
				smsClientService.sendRegistSuccessMessage(phone, phone, passWord);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}else{
    		userModel.setLoginame(tempjobnum);
    	}
    	userModel.setPassword(passWord);
    	//调用懒掌柜绑定一账通接口,在懒掌柜系统将掌中保与懒掌柜账号绑定
    	Map<String, String> map = new HashMap<String, String>();
		map.put("userid", user.getId());
		map.put("managerid", lzgid);
		map.put("requirementid", "");
		map.put("account", userModel.getLoginame());
		map.put("username", name);
		map.put("ismanager", "0");
		map.put("agentcode", user.getAgentcode());
		map.put("idno", user.getIdno());
		map.put("organization", user.getIdno());
    	appRegisteredService.bindingLzg(map);
		
		return userModel;
	}
	
	private UserRegisterModel getDeptByArea(String province, String city, String country){
		UserRegisterModel commonModel = new UserRegisterModel();
		INSCDept dept = new INSCDept();
		dept.setProvince(province);
		dept.setCity(city);
		dept.setCounty(country);
		List<INSCDept> deptList = inscDeptService.queryList(dept);
		dept = null;
		
		for (INSCDept inscDept : deptList) {
			if("05".equals(inscDept.getComtype())){
				dept = inscDept;
				break;
			}
			dept = new INSCDept();
			dept.setUpcomcode(inscDept.getComcode());
			dept.setComtype("05");
			if(inscDeptService.queryCount(dept) != 0 && !StringUtils.isEmpty(inscDept.getNoti())){
				dept = inscDeptService.queryList(dept).get(0);
				break;
			}else{
				dept = null;
			}
		}

		if(dept == null){
			dept = new INSCDept();
			dept.setProvince(province);
			dept.setCity(city);
			deptList = inscDeptService.queryList(dept);
			dept = null;
			for (INSCDept inscDept : deptList) {
				if("05".equals(inscDept.getComtype())){
					dept = inscDept;
					break;
				}
				dept = new INSCDept();
				dept.setUpcomcode(inscDept.getComcode());
				dept.setComtype("05");
				if(inscDeptService.queryCount(dept) != 0 && !StringUtils.isEmpty(inscDept.getNoti())){
					dept = inscDeptService.queryList(dept).get(0);
					break;
				}else{
					dept = null;
				}
			}
		}
		if(dept == null){
			commonModel.setStatus("fail");
			commonModel.setMessage("没有找到地区对应的机构网点");
			return commonModel;
		}
		if("1".equals(dept.getStatus())){
			commonModel.setStatus("fail");
			commonModel.setMessage("该区域未启用");
			return commonModel;
		}
		commonModel.setBody(dept);
		return commonModel;
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	private int initTestAgentPermission(String deptId, String tempjobnum) {
		return INSBPermissionsetService.initTestAgentPermission(deptId, tempjobnum);
	}
	
//	private synchronized String getJobNumber(){
//		String tempjobnum =(String) com.common.RedisClient.get("tempjobnum");
//		if(tempjobnum==null || tempjobnum.length()==0){
//			tempjobnum="10000000";
//		}
//		List<INSBAgent> selectList;
//		INSBAgent insbAgent = new INSBAgent();
//		do{
//			tempjobnum = String.valueOf(Long.parseLong(tempjobnum)+1);
//			insbAgent.setJobnum(tempjobnum);
//			selectList = insbAgentDao.selectList(insbAgent);
//		}while(selectList.size()!=0);
//		try {
//			com.common.RedisClient.set("tempjobnum",tempjobnum);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return tempjobnum;
//	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public CommonModel getIndex(String token, String lzgOtherUserId, String lzgManagerId) {
		CommonModel model = new CommonModel();
		if(token == null || "".equals(token)){
			model.setStatus("fail");
			model.setMessage("token不能为空");
			return model;
		}
		if(lzgOtherUserId == null || "".equals(lzgOtherUserId)){
			model.setStatus("fail");
			model.setMessage("懒掌柜未绑定掌中保账户");
			return model;
		}
		
		return (CommonModel) appLoginService.lzgLogin(token,lzgOtherUserId);
	}

	@Override
	public Map<String, String> up2LZGManager(String  agentId) {
		
		if(null==agentId||"".equals(agentId)){
			return new HashMap<String,String>();
		}
		
		INSBAgent agentModel = insbAgentService.queryById(agentId);
		
		if(agentModel.getLzgid()==null){
			return new HashMap<String,String>();
		}
		Map<String, String> result = new HashMap<String,String>();
		
		AgentLzgModel alModle = new AgentLzgModel();
		alModle.setOtheruserid(agentModel.getId());
		alModle.setIsmanager("1");
		alModle.setAgentcode(agentModel.getAgentcode());
		alModle.setIdno(agentModel.getIdno());
		alModle.setOrganization(agentModel.getDeptid());
		List<AgentLzgModel> otheruseridlist  = new ArrayList<AgentLzgModel>();
		otheruseridlist.add(alModle);
		
		UptomanagerModel model = new  UptomanagerModel();
		model.setPlatform("21");
		model.setToken("4ee6f3b3adc6ed202c9462cfb26f206d");
		model.setOtheruseridlist(otheruseridlist);
		
		try {
			String lzg = LZGUtil.agentTurnToLZGManager(model);
			Map<String,Object> mapResult = JSONObject.fromObject(lzg);
			if(mapResult==null){
				result.put("status", "FAILED");
				result.put("message", "返回值为空");
			}else{
				result.put("status", mapResult.get("status").toString());
				result.put("message", mapResult.get("message").toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "FAILED");
			result.put("message", e.getMessage());
		}
		
		return result;
	}
	
	public static void main(String[] args) {
		String aa = Md5Encodes.encodeMd5("cmlzg123");	
		System.out.println(aa);
	}


}
