package com.zzb.conf.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.cninsure.core.utils.LogUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.QueryBean;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCDeptService;
import com.common.PagingParams;
import com.zzb.conf.dao.INSBPrvaccountmanagerDao;
import com.zzb.conf.entity.INSBPrvaccountkey;
import com.zzb.conf.entity.INSBPrvaccountmanager;
import com.zzb.conf.service.INSBPrvaccountkeyService;
import com.zzb.conf.service.INSBPrvaccountmanagerService;

@Controller
@RequestMapping("/prvacc/*") 
public class INSBPrvAccountManagerController extends BaseController {
	@Resource
	private INSBPrvaccountmanagerService service;
	@Resource
	private INSBPrvaccountkeyService prvaccountkeyService;
	@Resource
	private INSCDeptService deptService;
	@Resource
	private INSBPrvaccountmanagerDao managerDao;

	/**
	 * 跳转页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "menu2list", method = RequestMethod.GET)
	public ModelAndView menu2List(String deptId,String managerid) {
		ModelAndView result = new ModelAndView("zzbconf/prvaccountmanager");
		
		if(managerid!=null &&!"".equals(managerid)){
			deptId =managerDao.selectById(managerid).getDeptid(); 
		}
		result.addObject("deptId", deptId);
		result.addObject("managerid", managerid);
		return result;
	}

	@RequestMapping(value = "initdatalistpage", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> initDataListPage(HttpSession session,
			@ModelAttribute PagingParams para,
			String usetype,String deptid,String providerid,
			@ModelAttribute QueryBean querybean) throws ParseException {
		if("0".equals(usetype)){
			usetype=null;
		}
		if("".equals(providerid)){
			providerid=null;
		}
		Map<String, Object> map = BeanUtils.toMap( querybean, para,usetype);
		map.put("deptid", deptid);
		map.put("providerid", providerid);
		map.put("usetype", usetype);
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = service.getDataListPage(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	@RequestMapping(value = "initkeylistpage", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> initKeyListPage(HttpSession session,@ModelAttribute PagingParams para,
			@RequestParam(required=false) String deptId,
			@RequestParam(required=false) String usetype,
			@RequestParam(required=false) String providerid,
			@ModelAttribute QueryBean querybean) throws ParseException {
		if("".equals(deptId)||"".equals(providerid)||"".equals(usetype)){
			return null;
		}
		Map<String, Object> map = BeanUtils.toMap(para);
		map.put("deptId", deptId);
		map.put("providerid", providerid);
		map.put("usetype", usetype);
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = service.getKeyDataListPage(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	@RequestMapping(value = "inscdeptlist",method= RequestMethod.POST)
	@ResponseBody
	public List<Map<String,String>>  institutionTreeList(HttpSession session, @RequestParam(value="root", required=false)   String parentcode)throws ControllerException{
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		
//		List<Map<String,String>> resultinstitutionList = deptService.queryTreeList4PrvAccount(parentcode,operator.getUserorganization());
//		return resultinstitutionList;
		if (parentcode != null) {
			return deptService.queryTreeList4Data(parentcode,operator.getUserorganization());
		}
		return deptService.dept4Tree(operator.getDeptinnercode(), null, null);
	}

	 /**
	 * 转到编辑页面
	 *
	 * 初始化编辑页面信息
	 *
	 * @param agentId
	 * @return
	 */
	 @RequestMapping(value = "mian2edit", method = RequestMethod.GET)
	 @ResponseBody
	 public Map<String,Object> mian2edit(String deptid,String id) {
	
		// ModelAndView result = new ModelAndView("zzbconf/prvaccountmanageredite");
		
//		 Map<String,Object> resultTemp = resultTemp = 
		 
//		 if(!resultTemp.isEmpty()){
//			 result.addObject("data", resultTemp.get("model"));
//			 result.addObject("pmodel", resultTemp.get("pmodel"));
//			 result.addObject("prvName", resultTemp.get("prvName"));
//			 result.addObject("prvId", resultTemp.get("prvId"));
//			 result.addObject("deptid", resultTemp.get("deptid"));
//		 }else{
//			 result.addObject("deptid", deptid);
//		 }
		
		
//		 result.addObject("deptname", resultTemp.get("deptname"));
	 return service.main2edit(id,deptid);
	 }
	 
	 @RequestMapping(value = "mian2keyedit", method = RequestMethod.GET)
	 @ResponseBody
	 public Map<String,Object> mian2keyEdit(String id) {
		 Map<String,Object> resultTemp = new HashMap<String,Object>();
		 resultTemp = service.mian2keyEdit(id);
		 return resultTemp;
	 }
	 
	
	 @RequestMapping(value = "saveorupdate", method = RequestMethod.POST)
	 @ResponseBody
	 public Map<String,String> saveOrUpdate(HttpSession session,@ModelAttribute INSBPrvaccountmanager model) {
		 Map<String,String> result = new HashMap<String,String>();
		 INSCUser user = (INSCUser) session.getAttribute("insc_user");
		 try {
			service.saveOrUpdate(user,model);
			result.put("message", "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("message", "保存失败");
		}
		 return result;
				
	 }
	 @RequestMapping(value = "saveorupdatekey", method = RequestMethod.POST)
	 @ResponseBody
	 public Map<String,String> saveOrUpdateKey(HttpSession session,@ModelAttribute INSBPrvaccountkey model,String deptId,String manageridmain) {
		 Map<String,String> result = new HashMap<String,String>();
		 INSCUser user = (INSCUser) session.getAttribute("insc_user");
		 try {
			 service.saveOrUpdateKey(user,model,deptId,manageridmain);
			 result.put("message", "保存成功");
		 } catch (Exception e) {
			 e.printStackTrace();
			 result.put("message", "保存失败");
		 }
		 return result;
		 
	 }
	 @RequestMapping(value = "deletebyid", method = RequestMethod.GET)
	 @ResponseBody
	 public Map<String,String> deleteById(HttpSession session, String id){
		 INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		 LogUtil.info("供应商账号配置删除id为%s,操作人:%s", id, operator.getUsercode());
		 Map<String,String>  result = new HashMap<String,String>();
		 try {
			service.deleteById(id);
			result.put("message", "删除成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.put("message", "删除失败");
		}
		 return result;
	 }
	 @RequestMapping(value = "deletekeybyid", method = RequestMethod.GET)
	 @ResponseBody
	 public Map<String,String> deleteKeyById(HttpSession session, String id){
		 INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		 LogUtil.info("供应商账号配置prvaccountkey删除id为%s,操作人:%s", id, operator.getUsercode());
		 Map<String,String>  result = new HashMap<String,String>();
		 try {
			 prvaccountkeyService.deleteById(id);
			 result.put("message", "删除成功");
		 } catch (Exception e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
			 result.put("message", "删除失败");
		 }
		 return result;
	 }
	
	 /**
	 * 修改关系表
	 * @param id
	 * @return
	 */
	 @RequestMapping(value = "initprvtree", method =RequestMethod.GET)
	 public ModelAndView agent2AgentPermission(String id) {
//		 INSBAgentpermission ap = service.agentMian2edit(id);
		 ModelAndView result = new ModelAndView("zzbconf/agentpermissionedit");
//		 result.addObject("apData", ap);
		 return result;
	 }
	//
	// /**
	// * 新增或者修改代理人权限关系表
	// *
	// * @param ap
	// * @return
	// */
	// @RequestMapping(value = "updateagentpermission", method =
	// RequestMethod.POST)
	// @ResponseBody
	// public BaseVo updateAgentPermission(HttpSession session,@ModelAttribute
	// INSBAgentpermission ap) {
	// BaseVo bv = new BaseVo();
	// INSCUser user = (INSCUser) session.getAttribute("insc_user");
	// try {
	// ap.setOperator(user.getUsercode());
	// ap.setModifytime(new Date());
	// service.updateAgentPermission(ap);
	//
	// bv.setStatus("1");
	// bv.setMessage("操作成功！");
	// } catch (ParseException e) {
	// bv.setStatus("2");
	// bv.setMessage("操作失败！");
	// e.printStackTrace();
	// }
	//
	// return bv;
	// }
	//
	// /**
	// * 删除代理人权限关系
	// *
	// * @param ap
	// * @return
	// */
	// @RequestMapping(value = "deleteagentpermission", method =
	// RequestMethod.POST)
	// @ResponseBody
	// public BaseVo deleteagentpermission(@ModelAttribute INSBAgentpermission
	// ap) {
	// BaseVo bv = new BaseVo();
	// try {
	//
	// service.removeAgentPermission(ap);
	// bv.setStatus("1");
	// bv.setMessage("操作成功！");
	// } catch (Exception e) {
	// bv.setStatus("2");
	// bv.setMessage("操作失败！");
	// e.printStackTrace();
	// }
	//
	// return bv;
	// }
	//
	// @RequestMapping(value = "saveagentproviderdata", method =
	// RequestMethod.POST)
	// @ResponseBody
	// public BaseVo saveagentproviderdata(HttpSession session,String
	// providerIds,
	// String agentId) {
	// BaseVo bv = new BaseVo();
	// try {
	// // INSCUser user = (INSCUser) session.getAttribute("insc_user");
	// service.saveAgentProvider(agentId, providerIds);
	// bv.setStatus("1");
	// bv.setMessage("操作成功！");
	// } catch (Exception e) {
	// bv.setStatus("2");
	// bv.setMessage("操作失败！");
	// e.printStackTrace();
	// }
	// return bv;
	// }
	//
	// /**
	// * @param ids
	// * @return
	// */
	// @RequestMapping(value = "batchdeleteagentbyid", method =
	// RequestMethod.POST)
	// @ResponseBody
	// public BaseVo batchDeleteAgentById(String ids) {
	// BaseVo bv = new BaseVo();
	// try {
	// String str = service.deleteAgentBatch(ids);
	// if(!"".equals(str) && str != null){
	// bv.setStatus("2");
	// bv.setMessage(str);
	// } else{
	// bv.setStatus("1");
	// bv.setMessage("操作成功！");
	// }
	// } catch (Exception e) {
	// bv.setStatus("2");
	// bv.setMessage("操作失败！");
	// e.printStackTrace();
	// }
	// return bv;
	// }
	// /*@RequestMapping(value = "batchdeleteagentbyid", method =
	// RequestMethod.POST)
	// @ResponseBody
	// public BaseVo batchDeleteAgentById(String ids) {
	// BaseVo bv = new BaseVo();
	// try {
	// service.deleteAgentBatch(ids);
	// bv.setStatus("1");
	// bv.setMessage("操作成功！");
	// } catch (Exception e) {
	// bv.setStatus("2");
	// bv.setMessage("操作失败！");
	// e.printStackTrace();
	// }
	// return bv;
	// }*/
	//
	// @RequestMapping(value = "main2detail", method = RequestMethod.GET)
	// public ModelAndView main2detail(String id) {
	// ModelAndView result = new ModelAndView("zzbconf/agentdetailinfo");
	// INSBAgent agent = service.queryById(id);
	// String stationdeptname="";
	// if(!"".equals(agent.getStationid()) && agent.getStationid()!=null ){
	// INSCDept dept = deptService.queryById(agent.getStationid());
	// stationdeptname = dept.getShortname();
	// result.addObject("stationdeptname", stationdeptname);
	// }
	// result.addObject("agent", agent);
	// return result;
	// }
	//
	// /**
	// * 批量重置密码
	// *
	// * @param userIds
	// * @return
	// */
	// @RequestMapping(value = "resetpwd", method = RequestMethod.GET)
	// public Map<String, String> resetPwd(String Ids) {
	// return service.updateResetPwd(Ids);
	// }
	//
	// /**
	// * 处理跟踪跳转
	// *
	// * @param userIds
	// * @return
	// */
	// @RequestMapping(value = "showcartasklist", method = RequestMethod.GET)
	// public ModelAndView taskturn(@RequestParam(value = "agentId", required =
	// false) String agentId) {
	// ModelAndView result = new ModelAndView("zzbconf/agentCarTaskManage");
	// INSBAgent ag = service.queryById(agentId);
	// String agentNum = ag.getAgentcode();
	// result.addObject("agentNum",agentNum);
	// return result;
	// }
	//
	//
	//
	// /**
	// * 处理跟踪
	// *
	// * @param userIds
	// * @return
	// */
	// @RequestMapping(value = "showcartaskmanagelist", method =
	// RequestMethod.GET)
	// @ResponseBody
	// public String showcartaskmanagelist(@ModelAttribute PagingParams
	// para,@RequestParam(value = "agentNum", required = false) String agentNum)
	// {
	// // PagingParams pagingParams = new PagingParams();
	// Map<String, Object> paramMap = BeanUtils.toMap(para);
	// String taskstatus = null;
	//
	// paramMap.put("agentNum", agentNum); //代理人工号
	// List<String> tasktypeList = null;//任务类型集合
	// paramMap.put("tasktype", tasktypeList);
	// return service.getJSONOfCarTaskListByMap(paramMap, taskstatus);
	//
	// }
	//
	// // @RequestMapping(value="aa",method=RequestMethod.POST)
	// // @ResponseBody
	// // public Map<String,String> Test(String cifRiskStr){
	// // System.out.println("========="+cifRiskStr);
	// // new JSONObject();
	// // JSONObject obj=JSONObject.fromObject(cifRiskStr);
	// // System.out.println(obj);
	// // return null;
	// // }
}
