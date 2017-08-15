package com.cninsure.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.jobpool.dispatch.DispatchTaskService;
import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.dao.INSCRoleDao;
import com.cninsure.system.dao.INSCUserDao;
import com.cninsure.system.dao.INSCUserRoleDao;
import com.cninsure.system.dao.impl.INSBServiceUtil;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCRole;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.entity.INSCUserRole;
import com.cninsure.system.service.INSCUserService;
import com.common.HttpSender;
import com.zzb.conf.dao.INSBGroupmembersDao;
import com.zzb.conf.dao.OFUserDao;
import com.zzb.conf.entity.OFUser;
import com.zzb.conf.service.INSBBusinessmanagegroupService;

@Service
@Transactional
public class INSCUserServiceImpl extends BaseServiceImpl<INSCUser> implements
		INSCUserService {
	@Resource
	private INSCUserDao inscUserDao;
	@Resource
	private INSCUserRoleDao userRoleDao;
	@Resource
	private INSCRoleDao roleDao;
	@Resource
	private INSCCodeDao codeDao;
	@Resource
	private INSCDeptDao deptDao;
	@Resource
	private INSBServiceUtil serviceUtil;
	@Resource
	private OFUserDao ofuserDao;
	@Resource
	private INSBGroupmembersDao groupmembersDao;
	@Resource
	private INSBBusinessmanagegroupService  businessmanagegroupService;
	@Resource
	private DispatchTaskService dispatchTaskService;
	@Resource
	private ThreadPoolTaskExecutor taskthreadPool4workflow;
	
	
	private static String DISPATCH_HOST = "";
	static {
		// 读取相关的配置  
		ResourceBundle resourceBundle = ResourceBundle
				.getBundle("config/config");
		DISPATCH_HOST = resourceBundle.getString("dispatch.hostName");
	}
	
	@Override
	public  INSCUser queryByUserCode(String usercode) {
		return inscUserDao.selectByUserCode(usercode);
	}

	@Override
	public boolean userCodeCheck(String usercode) {
		INSCUser user = inscUserDao.selectByUserCode(usercode);
		if (user == null) {
			return true;
		}
		return false;
	}

	@Override
	/**
	 * TODO 批量删除
	 */
	public void benchDeleteByIds(List<String> arrayid) {
		if(arrayid == null){
			return;
		}
		String path = DISPATCH_HOST + "/worker/offline";
		List<INSCUser> users = inscUserDao.selectByIds(arrayid);
		if (users != null && !users.isEmpty()) {
			LogUtil.info("批量删除用户开始");
			List<String>groups = new ArrayList<String>();
			for (INSCUser inscUser : users) {
				String result = "";
				HashMap<String, Object> map = new HashMap<String, Object>();
				
				map.put("groups", groups);
				map.put("workerId", inscUser.getUsercode());
				map.put("workerName", inscUser.getUsercode());
				try {
					result = HttpSender.doPost(path, JSONObject.fromObject(map).toString());
				} catch (Exception e) {
					LogUtil.info("业管权限调度通知参数异常失败usercode=" + inscUser.getUsercode(), e);
				}
				LogUtil.info("业管权限调度通知参数结果=" + result + " usercode=" + inscUser.getUsercode());
			}
		}
		LogUtil.info("已通知所有业管下线, 开始删除用户");
		//用户表批量删除用户
		inscUserDao.deleteByIdInBatch(arrayid);
		
		//更新所有业管组
		List<String> groupIds = groupmembersDao.selectGroupIdsByUserId4UserDelete(arrayid);
		String ids ="";
		for(String id:arrayid){
			ids += id + ",";
		}
		if(groupIds!=null&&!groupIds.isEmpty()){
			for(String gIds:groupIds){
				businessmanagegroupService.removeGroupMember( ids,  gIds);
			}
		}
		LogUtil.info("更新业管组完成, 批量删除用户完成");
	}

	@Override
	protected BaseDao<INSCUser> getBaseDao() {
		return inscUserDao;
	}

	@Override
	public boolean changePassword(String usercode, String oldpwd, String newpwd) {
		INSCUser user = inscUserDao.selectByUserCode(usercode);
		if (user.getPassword().equals(StringUtil.md5Base64(oldpwd))) {
			user.setPassword(StringUtil.md5Base64(newpwd));
			user.setPwsmodifytime(new Date());
			inscUserDao.updateById(user);
			changeOfuser(usercode, newpwd);
			return true;
		}
		return false;
	}
	

	@Override
	public Map<String, Object> initUserList(Map<String, Object> data) {
		Map<String, Object> map = new HashMap<String, Object>();
		long total = inscUserDao.selectPagingCount(data);
		List<Map<Object, Object>> infoList = inscUserDao.selectUserListPaging(data);
		if (infoList != null) {
			for (Map<Object, Object> mapModel : infoList) {
				if ("1".equals(mapModel.get("status"))) {
					mapModel.put("statusStr", "启用");
				} else if ("0".equals(mapModel.get("status"))) {
					mapModel.put("statusStr", "停用");
				}
			}
		}

		map.put("total", total);
		map.put("rows", infoList);
		return map;
	}

	@Override
	public Map<String, String> updateResetPwd(String userIds) {
		Map<String, String> resultMap = new HashMap<String, String>();
		try {
			String[] userIdArray = userIds.split(",");
			for (String userId : userIdArray) {
				INSCUser tempUser = new INSCUser();
				tempUser.setId(userId);
//				tempUser.setPassword(StringUtil.md5Base64("123456"));
				tempUser.setPassword(StringUtil.md5Base64("password01!"));
				tempUser.setPwsmodifytime(new Date());
				inscUserDao.updatePWDById(tempUser);
				INSCUser u = inscUserDao.selectById(userId);
				if(u!=null){
//					String result = updatePassWord4Ldap(u.getUsercode(),"123456");
//					String result =changeOfuser(u.getUsercode(), "123456");
					String result =changeOfuser(u.getUsercode(), "password01!");
					if("fail".equals(result)){
						throw  new Exception();
					}
				}
			}
			resultMap.put("code", "0");
			resultMap.put("message", "操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("code", "1");
			resultMap.put("message", "操作失败，请稍候重试");
		}
		return resultMap;
	}
	
	/**
	 * hxx 更新ofuser
	 * @param usercode
	 * @param newPassword
	 */
	private String changeOfuser(String usercode,String newPassword){
		try {
			OFUser ofuser = ofuserDao.queryByUserName(usercode) ;
			if(ofuser==null){
				 ofuser = new OFUser();
				 ofuser.setUsername(usercode);
//				 ofuser.setPlainPassword(newPassword);
				 ofuser.setPlainPassword(StringUtil.md5Base64(newPassword));
				 ofuserDao.insert(ofuser);
			}else{
				 ofuser.setUsername(usercode);
//				 ofuser.setPlainPassword(newPassword);
				 ofuser.setPlainPassword(StringUtil.md5Base64(newPassword));
				 ofuserDao.updateByUserName(ofuser);
			}
			return "sucess";
		} catch (Exception e) {
			return "fail"; 
		}
		
	}
	
	/**
	 * hxx 更新ofuser
	 * @param usercode
	 * @param newPassword
	 */
	private String changeOfuser(String usercode,String newPassword,String name){
		try {
			OFUser ofuser = ofuserDao.queryByUserName(usercode) ;
			if(ofuser==null){
				 ofuser = new OFUser();
				 ofuser.setUsername(usercode);
				 ofuser.setName(name);
				 ofuser.setPlainPassword(newPassword);
				 ofuserDao.insert(ofuser);
			}else{
				 ofuser.setUsername(usercode);
				 ofuser.setName(name);
				 ofuser.setPlainPassword(newPassword);
				 ofuserDao.updateByUserName(ofuser);
			}
			return "sucess";
		} catch (Exception e) {
			return "fail";
		}
		
	}
	
	@Override
	public Map<String, String> updateResetUserSataus(String userIds,int type) {
		Map<String, String> resultMap = new HashMap<String, String>();
		try {
			String[] userIdArray = userIds.split(",");
			for (String userId : userIdArray) {
				INSCUser tempUser = new INSCUser();
				tempUser.setId(userId);
				tempUser.setStatus("0");
				if(type==1){
					tempUser.setStatus("0");
					inscUserDao.updateUserStatusById(tempUser);
				}else if(type==2){
					tempUser.setStatus("1");
					inscUserDao.updateUserStatus2OnById(tempUser);
				}
			}
			resultMap.put("code", "0");
			resultMap.put("message", "操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("code", "1");
			resultMap.put("message", "操作失败，请稍候重试");
		}
		return resultMap;
	}

	@Override
	public List<String> findUserByGroupid(String groupid) {
		List<String> list = null;
		if (StringUtils.isNotBlank(groupid)) {
			list = inscUserDao.selectUserByGroupid(groupid);
		}
		return list;
	}

	@Override
	public Map<String, Object> getEditeData(String id) {
		Map<String, Object> result = new HashMap<String, Object>();

		// 初始化角色信息
		INSCUser user = new INSCUser();
		List<String> roleIds = new ArrayList<String>();
		List<INSCRole> roleModelList = new ArrayList<INSCRole>();
		
		
		if (id!=null) {
			user = inscUserDao.selectById(id);				
		    user.setMaturitydata(user.getMaturitydata().substring(0, 10));
			INSCDept deptModel = deptDao.selectById(user.getUserorganization());

			if (deptModel != null) {
				user.setComname(deptModel.getComname());
				roleIds = userRoleDao.selectRoleidByUserid(id);
			}
		}
		result.put("user", user);
		// role
		List<Map<String,Object>> roleList = roleDao.selectAllList();
		
		if(!roleIds.isEmpty()){
			for(String roleid:roleIds){
				roleModelList.add(roleDao.selectById(roleid));
			}
		}
		result.put("roleList", roleList);
		result.put("addList", roleModelList);

		LogUtil.info(user.getUsercode()+"角色："+roleModelList);
		return result;
	}

	@Override
	public void saveOrUpdate(INSCUser operator, INSCUser user, String roleIds) {
		String onlineuser = operator.getUsercode();
		List<String> newRoleIdList = new ArrayList<String>();
		String[] tempArray = null;
		if(roleIds != null && roleIds != ""){
			try {
				tempArray = roleIds.split(",");
			} catch (Exception e) {
				tempArray[0] = roleIds;
				e.printStackTrace();
			}
			if (tempArray != null) {
				for (String str : tempArray) {
					newRoleIdList.add(str);
				}
			}
		}		
		
		// 新增
		if (StringUtil.isEmpty(user.getId()) || user.getId() == null) {
			// 新增用户默认属于groupid为1的群组
			String pass =user.getPassword();
			Date date = new Date();
			user.setGroupid("1");
			user.setCreatetime(date);
			user.setOperator(onlineuser);
			user.setPassword(StringUtil.md5Base64(pass));
			String userId = inscUserDao.insertReturnId(user);
//			INSCUser u = inscUserDao.selectById(userId);
			
			changeOfuser(user.getUsercode(), StringUtil.md5Base64(pass), user.getName());
//			try {
//				inscUserDao.updateById(u);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			
			if(tempArray!=null){
				List<INSCUserRole> addList = new ArrayList<INSCUserRole>();
				for(String id:newRoleIdList){
					INSCUserRole model = new INSCUserRole();
					model.setCreatetime(new Date());
					model.setOperator(onlineuser);
					model.setRoleid(id);
					model.setUserid(userId);
					model.setStatus("1");
					addList.add(model);
				}
				userRoleDao.insertInBatch(addList);
				
			}
		} else {
			INSCUser olduser = inscUserDao.selectById(user.getId());
			
			if(!StringUtils.isEmpty(user.getUsercode())&&!StringUtils.isEmpty(olduser.getUsercode())){
				try {
					cascadUpdateUserCode(user.getUsercode(),olduser.getUsercode());
				} catch (Exception e) {
					LogUtil.error("业管工号级联更新出错");
				}
			}
			String pass =user.getPassword();
			//判断当前两个密码是否相同 不相同 需要加密
			if(!olduser.getPassword().equals(pass)){
				user.setPassword(StringUtil.md5Base64(pass));
			}
			// 修改
			user.setModifytime(new Date());
			user.setOperator(onlineuser);
			
//			
			inscUserDao.updateById(user);

			// 已经保存的角色id
			List<String> oldRoleIds = userRoleDao.selectRoleidByUserid(user.getId());
			
			Map<String, List<String>> resultMap = serviceUtil.updateUtil(
					newRoleIdList, oldRoleIds);
			
			List<String> deleteRoleIds = resultMap.get("delete");
			for (String id : deleteRoleIds) {
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("roleId", id);
				paramMap.put("userId", user.getId());
				userRoleDao.deleteByUserIdRoleId(paramMap);
			}

			List<String> addRoleIds = resultMap.get("add");
			
			List<INSCUserRole> addList = new ArrayList<INSCUserRole>();
			for(String id:addRoleIds){
				INSCUserRole model = new INSCUserRole();
				model.setCreatetime(new Date());
				model.setOperator(onlineuser);
				model.setRoleid(id);
				model.setUserid(user.getId());
				model.setStatus("1");
				addList.add(model);
			}
			userRoleDao.insertInBatch(addList);
			
			if(!olduser.getUsercode().equals(user.getUsercode())){
//				changeOfuser(user.getUsercode(), StringUtil.md5Base64(pass),user.getName());
				//如果修改密码则需加密
				changeOfuser(user.getUsercode(), !olduser.getPassword().equals(user.getPassword()) ? StringUtil.md5Base64(pass) : pass, user.getName());
				ofuserDao.deleteByUserName(olduser.getUsercode());
			}else{
//				changeOfuser(user.getUsercode(), StringUtil.md5Base64(pass),user.getName());
				changeOfuser(user.getUsercode(), !olduser.getPassword().equals(user.getPassword()) ? StringUtil.md5Base64(pass) : pass, user.getName());
			}
			
		}
	}
	
	/**
	 * 
	 * 级联更新cm中所有usercode
	 * @param newUserCode ,oldUserCode
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	private int cascadUpdateUserCode(String newUserCode,String oldUserCode) {
		if(newUserCode.equals(oldUserCode)){
			return 0;
		}else{
			Map<String,String> param = new HashMap<String, String>();
			param.put("newUserCode", newUserCode);
			param.put("oldUserCode", oldUserCode);
			
			return groupmembersDao.updateMembersUserCode(param);
		}
	}

	@Override
	public INSCUser getByUsercode(String code) {
		
		return inscUserDao.selectByUserCode(code);
	}

	@Override
	public Map<String, Object> initonlineList(Map<String, Object> data) {
		Map<String, Object> map = new HashMap<String, Object>();
		long total = inscUserDao.selectonlineuserPagingCount(data);
		List<Map<Object, Object>> infoList = inscUserDao.selectonlineListPaging(data);

		if (infoList != null) {
			for (Map<Object, Object> mapModel : infoList) {
				String usercode = mapModel.get("usercode").toString();
				//每个业管所在的业管组数量
				long count = inscUserDao.selectGroupnameCountByUsercode(usercode);
				//得到一个业管所属的多个业管组
				if(count>1){
					List<String> groupnames = inscUserDao.selectGroupnameByUsercode(usercode);
					if(groupnames != null && !"".equals(groupnames)){
						String[] groupidArr = groupnames.toArray(new String[groupnames.size()]);					
						String groupname=new String();
						for(String name : groupidArr) {      												
							groupname +=name+",";	
						}
						mapModel.put("groupname", groupname.substring(0, (groupname.length()-1)));
					}				
				}
				//该业管未完成的任务数量
				long unfinishedtasknum = inscUserDao.selectunfinishedtasknumCount(usercode);
				mapModel.put("unfinishedtasknum", unfinishedtasknum);
				
				if(StringUtil.isEmpty(mapModel.get("usercode"))){
					mapModel.put("usercode", "-");
				}
				if(StringUtil.isEmpty(mapModel.get("name"))){
					mapModel.put("name", "-");
				}
				if(StringUtil.isEmpty(mapModel.get("userorganization"))){
					mapModel.put("userorganization", "-");
				}
				if(StringUtil.isEmpty(mapModel.get("comname"))){
					mapModel.put("comname", "-");
				}
				if(StringUtil.isEmpty(mapModel.get("onlinestatus"))){
					mapModel.put("onlinestatus", "-");
				}else{
					if("1".equals(mapModel.get("onlinestatus").toString())){
						mapModel.put("onlinestatus", "在线");
					}else if("2".equals(mapModel.get("onlinestatus").toString())){
						mapModel.put("onlinestatus", "忙碌");
					}else{
						mapModel.put("onlinestatus", "离线");
					}
				}
				if(StringUtil.isEmpty(mapModel.get("status"))){
					mapModel.put("status", "-");
				}
				else{
					if("1".equals(mapModel.get("status").toString())){
						mapModel.put("status", "启用");
					}else{
						mapModel.put("status", "停用");
					}
				}
				if(StringUtil.isEmpty(mapModel.get("modifytime"))){
					mapModel.put("modifytime", "-");
				}
			}
		}		
		map.put("total", total);
		map.put("rows", infoList);
		return map;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int cascadDelete(String id) {
		//TODO 删除用户前通知调度系统用户下线
		INSCUser inscUser = inscUserDao.selectById(id);
		if(inscUser != null){
			String result = "";
			Map<String, Object> map = new HashMap<String, Object>();
			List<String>groups = new ArrayList<String>();
			map.put("groups", groups);
			map.put("workerId", inscUser.getUsercode());
			map.put("workerName", inscUser.getUsercode());
			try {
				String path = DISPATCH_HOST + "/worker/offline";
				result = HttpSender.doPost(path, JSONObject.fromObject(map).toString());
			} catch (Exception e) {
				LogUtil.info("业管权限调度通知参数异常失败usercode=" + inscUser.getUsercode(), e);
			}
			LogUtil.info("业管权限调度通知参数结果=" + result + " usercode=" + inscUser.getUsercode());
		}
		
		int deleteResult = inscUserDao.deleteById(id);
		if(deleteResult ==1){
			List<String> usercodesList = new ArrayList<String>();
			usercodesList.add(id);
			List<String> groupIds = groupmembersDao.selectGroupIdsByUserId4UserDelete(usercodesList);
			
			//更新所有业管组
			if(groupIds!=null&&!groupIds.isEmpty()){
				for(String gIds:groupIds){
					businessmanagegroupService.removeGroupMember( id,  gIds);
				}
			}
		
		}
		return deleteResult;
	}

	@Override
	public boolean changePooltasks(String usercode, String taskpool) {
		INSCUser user = inscUserDao.selectByUserCode(usercode);
		if (user.getTaskpool()!=Integer.valueOf(taskpool)) {
			user.setTaskpool(Integer.valueOf(taskpool));
			inscUserDao.updateById(user);
			taskthreadPool4workflow.execute(new Runnable() {
				@Override
				public void run() {
					try {
						dispatchTaskService.userLoginForTask(usercode);//通知调度中心修改业管最大任务数
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			});
			return true;
		}
		return false;
	}
}
