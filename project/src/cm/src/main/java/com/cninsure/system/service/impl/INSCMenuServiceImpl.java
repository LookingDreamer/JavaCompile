package com.cninsure.system.service.impl;

import java.util.*;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.jobpool.DispatchService;
import com.cninsure.jobpool.dispatch.DispatchTaskService;
import com.cninsure.system.dao.INSCMenuDao;
import com.cninsure.system.dao.INSCRoleMenuDao;
import com.cninsure.system.dao.INSCUserDao;
import com.cninsure.system.dao.INSCUserRoleDao;
import com.cninsure.system.entity.INSCMenu;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCMenuService;
import com.common.RedisException;
import com.common.WorkFlowException;
import com.zzb.cm.dao.INSBQuoteinfoDao;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.conf.controller.vo.MenuVo;
import com.zzb.conf.dao.INSBGroupmembersDao;
import com.zzb.conf.dao.INSBWorkflowmainDao;
import com.zzb.conf.dao.INSBWorkflowsubDao;
import com.zzb.conf.entity.INSBWorkflowmain;
import com.zzb.conf.entity.INSBWorkflowsub;

@Service
@Transactional
public class INSCMenuServiceImpl extends BaseServiceImpl<INSCMenu> implements INSCMenuService {
	@Resource
	private INSCMenuDao inscMenuDao;
	@Resource
	private INSCUserRoleDao userRoleDao;
	@Resource
	private INSCUserDao userDao;
	@Resource
	private INSCRoleMenuDao roleMenuDao;
	@Resource
	private INSBGroupmembersDao groupmembersDao;
	@Resource
	private INSBWorkflowsubDao workflowsubDao;
	@Resource
	private DispatchService dispatchService;
	@Resource
	private INSBQuoteinfoDao quoteinfoDao;
	@Resource
	private INSBWorkflowmainDao workflowmainDao;
	@Resource
	private DispatchTaskService dispatchTaskService;
	private static String SERVER_SYSTEM = "";
	static {
		// 读取相关的配置
		ResourceBundle resourceBundle = ResourceBundle
				.getBundle("config/config");
		SERVER_SYSTEM = resourceBundle.getString("server.system");
	}
	@Override
	protected BaseDao<INSCMenu> getBaseDao() {
		return inscMenuDao;
	}
//	@Cacheable(value="menuCache")
	public String queryMenusFtl(String usercode, String parentnodecode){
		List<String> myMenuIds = new ArrayList<String>();
		
		String userId = userDao.selectIdByCode4Menu(usercode);
		if(userId!=null){
			//得到当前用户所属角色
			List<String> roleIds = userRoleDao.selectRoleidByUserid(userId);
			if(roleIds!=null){
				List<String>  menuIds= roleMenuDao.selectMenuIdsByRoleIds4Menu(roleIds);
				if(menuIds !=null){
					myMenuIds = inscMenuDao.selectCodeByIds4Menu(menuIds);
				}
			}
			
		}
		
		List<Map<Object, Object>> resultList = this.queryMenusByUserCode(usercode, parentnodecode,myMenuIds);

		
		StringBuffer tbody = new StringBuffer();
		tbody.append("<ul class=\"nav navbar-nav\">");
		for (int i = 0; i < resultList.size(); i++) {
			Map<Object, Object> map1 = (Map<Object, Object>) resultList.get(i);
			if(!"".equals(map1.get("children"))){
				INSCMenu tempInscMenu1 = (INSCMenu) map1.get("menu");
				tbody.append("<li class=\"dropdown\"><a class=\"dropdown-toggle\" data-toggle=\"collapse\" data-target=\"#sub-" + i + "\"><span class=\"" + tempInscMenu1.getIconurl() + " cus-icon\"></span>" + tempInscMenu1.getNodename() + "<b class=\"caret\"></b></a>");
				@SuppressWarnings("unchecked")
				List<Map<Object, Object>> list1 = (List<Map<Object, Object>>) map1.get("children");
				tbody.append("<div id=\"sub-" + i + "\" class=\"collapse navbar-collapse\"><ul class=\"nav navbar-nav\">");
				for (int j = 0; j < list1.size(); j++) {
					
	            	Map<Object, Object> map2 = (Map<Object, Object>) list1.get(j);
	            	INSCMenu tempInscMenu2 = (INSCMenu) map2.get("menu");
	            	if(!"".equals(map2.get("children"))){
	            		@SuppressWarnings("unchecked")
						List<Map<Object, Object>> list2 = (List<Map<Object, Object>>) map2.get("children");
	            		tbody.append("<li class=\"dropdown\"><a class=\"dropdown-toggle\" data-toggle=\"collapse\" data-target=\"#sub-" + i + "_" + j + "\"><span class=\"" + tempInscMenu2.getIconurl() + " cus-icon\"></span>" + tempInscMenu2.getNodename() + "<b class=\"caret\"></b></a>");
	            		tbody.append("<div id=\"sub-" + i + "_" + j + "\" class=\"collapse navbar-collapse\"><ul class=\"nav navbar-nav\">");
	            		for (int k = 0; k < list2.size(); k++) {
	            			Map<Object, Object> map3 = (Map<Object, Object>) list2.get(k);
							INSCMenu tempInscMenu3 = (INSCMenu) map3.get("menu");
            				tbody.append("<li><a data-bind=\"" + tempInscMenu3.getActiveurl() + "\" target=\"fra_content\"><span class=\"" + tempInscMenu3.getIconurl() + " cus-icon\"></span>" + tempInscMenu3.getNodename() + "</a></li>");
						}
	            		tbody.append("</ul></div></li>");
	            	}else{
						tbody.append("<li><a data-bind=\"" +tempInscMenu2.getActiveurl() + "\" target=\"fra_content\"><span class=\"" + tempInscMenu2.getIconurl() + " cus-icon\"></span>" + tempInscMenu2.getNodename() + "</a></li>");
	            	}
	            
				}

				tbody.append("</ul></div></li>");
			}else{
				INSCMenu tempInscMenu4 = (INSCMenu) map1.get("menu");
				tbody.append("<li><a data-bind=\"" + tempInscMenu4.getActiveurl()+ "\" target=\"fra_content\"><span class=\"" + tempInscMenu4.getIconurl() + " cus-icon\"></span>" + tempInscMenu4.getNodename() + "</a></li>");
			}
		    
		}
		tbody.append("</ul>");
	    return tbody.toString();
	}
	
	private List<Map<Object, Object>> queryMenusByUserCode(String usercode, String parentnodecode,List<String> myMenuIds) {
			List<Map<Object, Object>> resultList = new ArrayList<>();
			List<INSCMenu> tempListMenu = new ArrayList<INSCMenu>();
			if("0".equals(parentnodecode)){
				List<INSCMenu> myTempListMenu = inscMenuDao.selectMenuByParentNodeCode(parentnodecode);
				
				for(INSCMenu model:myTempListMenu){
					if(myMenuIds.contains(model.getNodecode())){
						if ("cm".equals(SERVER_SYSTEM) && !model.getNodecode().startsWith("r01")) {
							tempListMenu.add(model);
						} else if ("report".equals(SERVER_SYSTEM) && model.getNodecode().startsWith("r01")) {
							tempListMenu.add(model);
						}

					}
				}
			}else if(myMenuIds.contains(parentnodecode)){
				List<INSCMenu> myTempListMenu = inscMenuDao.selectMenuByParentNodeCode(parentnodecode);
				for(INSCMenu model:myTempListMenu){
					if(myMenuIds.contains(model.getNodecode())){
						if ("cm".equals(SERVER_SYSTEM) && !model.getNodecode().startsWith("r01")) {
							tempListMenu.add(model);
						} else if ("report".equals(SERVER_SYSTEM) && model.getNodecode().startsWith("r01")) {
							tempListMenu.add(model);
						}
					}
				}
			}
//			tempListMenu = inscMenuDao.selectMenuByParentNodeCode(parentnodecode);
		
			if(!tempListMenu.isEmpty()){
				for(int i=0;i<tempListMenu.size();i++){
					
					INSCMenu tempInscMenu = new INSCMenu();
					Map<Object,Object> tempMap = new HashMap<Object,Object>();
					tempInscMenu = tempListMenu.get(i);
					tempMap.put("menu", tempInscMenu);
					tempMap.put("children", "1".equals(tempInscMenu.getChildflag())?this.queryMenusByUserCode(usercode, tempInscMenu.getNodecode(),myMenuIds):"");
					resultList.add(tempMap);
				}
			}
			return resultList;
	}
	
	@Override
	public List<Map<Object, Object>> queryMenusList(String id,String NoParentnodecode) {
		List<Map<Object, Object>> resultList = new ArrayList<>();
		List<INSCMenu> tempListMenu = new ArrayList<INSCMenu>();
		INSCMenu inscmenu = new INSCMenu();
		String parentnodecode = null;
		if(id!=null && !"".equals(id)){
			inscmenu = inscMenuDao.selectById(id);
			parentnodecode = inscmenu.getNodecode();
		}else if(id == null || StringUtil.isEmpty(parentnodecode) || "source".equalsIgnoreCase(parentnodecode)){
			parentnodecode = "0";
		}
		/*if(StringUtil.isEmpty(parentnodecode) || "source".equalsIgnoreCase(parentnodecode)){
			parentnodecode = "0";
		}*/
		tempListMenu = inscMenuDao.selectMenuByParentNodeCode(parentnodecode);
		for(int i=0;i<tempListMenu.size();i++){
			INSCMenu tempInscMenu = new INSCMenu();
			Map<Object,Object> tempMap = new HashMap<Object,Object>();
			tempInscMenu = tempListMenu.get(i);
			/**
			 * ztree数据
			 */
			tempMap.put("name", tempInscMenu.getNodename());
			tempMap.put("id", tempInscMenu.getId());
			tempMap.put("pid", tempInscMenu.getParentnodecode());
			tempMap.put("isParent", "1".equals(tempInscMenu.getChildflag())? "true" : "false");
			
			resultList.add(tempMap);
		}
		return resultList;
	}
	
	@Override
	public INSCMenu queryByNodeCode(String nodecode) {
		INSCMenu menu=(INSCMenu) inscMenuDao.selectByNodeCode(nodecode);
		return menu;
	}
	@Override
	public Map<String, String> getTaskManageDataByUserCode(String usercode) {
		
		Map<String,String> result = new HashMap<String,String>();
		String userId = userDao.selectIdByCode4Menu(usercode);
		if(userId!=null){
			//得到当前用户所属角色
			List<String> roleIds = userRoleDao.selectRoleidByUserid(userId);
			if(roleIds!=null){
				List<String>  menuIds= roleMenuDao.selectMenuIdsByRoleIds4Menu(roleIds);
				//判断当前菜单是否包含
				List<String> myIds = new ArrayList<String>();
				myIds.add("17");
				myIds.add("18");
				myIds.add("55");
				
				String myData="";
				if(menuIds.contains("17")){
						myData +="m0017,";
				}
				if(menuIds.contains("18")){
					myData +="m0018,";
				}
				if(menuIds.contains("55")){
						myData +="m0025,";
				}
				if(myData.length()>0){
					myData.substring(0, myData.length()-1);
					result.put("myMenu", myData);
				}
			}
		}
		return result;
	}
	@Override
	public void loginUserDispatchWork(String userCode) {
		LogUtil.info("业管登录分配任务--登陆人userCode="+userCode);
		
		List<String> usercodes = new ArrayList<String>();
		usercodes.add(userCode);
		List<String> result = groupmembersDao.selectGroupIdsByUserCodes4Login(usercodes);
		LogUtil.info("业管登录分配任务--登陆人所属业管群组="+result);
		
		if(null!=result&&!result.isEmpty()){
			
			//主流程任务自动分配
			List<INSBWorkflowmain> mainWork = workflowmainDao.getDataByGroupId4UserLogin(result);
			LogUtil.info("业管登录分配任务--当前群组未分配任-主流程-务集合="+mainWork);
			
			
			if(null!=mainWork&&!mainWork.isEmpty()){
				//查当前任务的供应商
				for(INSBWorkflowmain model:mainWork){
					if(null!=model.getInstanceid()){
						List<String> subInstanceList = workflowsubDao.selectSubInstanceIdByEnd(model.getInstanceid());
						LogUtil.info("业管登录分配任务--得到正常结束的子流程="+subInstanceList);
						if(null!=subInstanceList&&subInstanceList.size()==1){
								INSBQuoteinfo quoteModel = quoteinfoDao.queryQuoteinfoByWorkflowinstanceid(subInstanceList.get(0));
								LogUtil.info("业管登录分配任务--未分配任务报价信息="+quoteModel);
								if(quoteModel==null){
									continue;
								}
								if(null!=quoteModel.getInscomcode()){
									INSCUser userModel = userDao.selectByUserCode(userCode);
									LogUtil.info("业管登录分配任务--当前未分配任务进行认领="+userModel);
									try {
										//dispatchService.getTask(model.getInstanceid(), quoteModel.getInscomcode(), 1, userModel, userModel);
										//修改为业管上线，通知调度可以分配任务给该业管
										dispatchTaskService.userLoginForTask(userCode);
									} catch (WorkFlowException e) {
										LogUtil.info("业管登录分配任务--分配任务出错");
										e.printStackTrace();
										
									} catch (RedisException e) {
										LogUtil.info("业管登录分配任务--分配任务出错");
										e.printStackTrace();
									}
								}
						}
					}
				}
			}
			
			
			
			
			//当前为子流程自动分配任务
			List<INSBWorkflowsub> todoWork = workflowsubDao.getDataByGroupId4UserLogin(result);
			LogUtil.info("业管登录分配任务--当前群组未分配-子流程-任务集合="+todoWork);
			
			if(null!=todoWork&&!todoWork.isEmpty()){
				//查当前任务的供应商
				for(INSBWorkflowsub model:todoWork){
					
					if(null!=model.getInstanceid()){
						INSBQuoteinfo quoteModel = quoteinfoDao.queryQuoteinfoByWorkflowinstanceid(model.getInstanceid());
						if(quoteModel==null){
							continue;
						}
						LogUtil.info("业管登录分配任务--未分配任务报价信息="+quoteModel);
						if(null!=quoteModel.getInscomcode()){
							INSCUser userModel = userDao.selectByUserCode(userCode);
							
							LogUtil.info("业管登录分配任务--当前未分配任务进行认领="+userModel);
							try {
								//dispatchService.getTask(model.getInstanceid(), quoteModel.getInscomcode(), 2, userModel, userModel);
								//修改为业管上线，通知调度可以分配任务给该业管
								dispatchTaskService.userLoginForTask(userCode);
							} catch (WorkFlowException e) {
								LogUtil.info("业管登录分配任务--分配任务出错");
								e.printStackTrace();
							} catch (RedisException e) {
								LogUtil.info("业管登录分配任务--分配任务出错");
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}
	@Override
	public List<INSCMenu> queryAll() {
		return inscMenuDao.selectAll();
	}
	@Override
	public List<MenuVo> selectListMap(int offset,int limit) {
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("offset", offset);
		params.put("limit", limit);
		return inscMenuDao.selectListMap(params);
	}
}
