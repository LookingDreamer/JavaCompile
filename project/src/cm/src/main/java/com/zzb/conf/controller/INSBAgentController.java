package com.zzb.conf.controller;
 
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cninsure.core.tools.util.ValidateUtil;
import com.cninsure.core.utils.*;


import com.zzb.conf.controller.vo.TreeVo;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.service.*;
import com.zzb.mobile.dao.INSBAgentMarketingNewDao;
import com.zzb.mobile.dao.INSBCoreAgentDao;
import com.zzb.mobile.entity.INSBCoreAgent;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.service.AppMarketingActivitiesService;
import com.zzb.mobile.service.UserCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.manager.scm.INSASyncService;
import com.cninsure.system.service.INSCDeptService;
import com.common.ExcelUtil;
import com.common.PagingParams;
import com.zzb.cm.service.INSBCarinfoService;
import com.zzb.conf.controller.vo.BaseVo;
import com.zzb.conf.controller.vo.ExportPermissionInfoVo;
import com.zzb.conf.dao.INSBPermissionsetDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBAgentpermission;
import com.zzb.conf.entity.INSBCertification;
import com.zzb.conf.entity.INSBPermission;
import com.zzb.conf.entity.INSBPermissionset;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.model.INSBAgentQueryModel;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/agent/*")
public class INSBAgentController extends BaseController {
	@Resource 
	private INSBAgentService service; 
	@Resource
	private INSASyncService agtService;
	@Resource
	private INSBPermissionsetService insbPermissionsetService;
	@Resource
	private INSBPermissionsetDao insbPermissionsetDao;
	@Resource
	private INSCDeptDao deptDao;
	@Resource
	private INSCDeptService deptService;	
	@Resource
	private INSBChannelService insbChannelService;	
	@Resource
	private INSBCarinfoService insbCarinfoService;
	@Resource
	private INSBPolicyitemService insbPolicyitemService;
	@Resource
	private INSBCertificationService insbCertificationService;
	@Resource
	private INSBPermissionallotService insbPermissionallotService;
	@Resource
	private UserCenterService userCenterService;

	@Autowired
	private AppMarketingActivitiesService appMarketingActivitiesService;

	private String agentid = "";

	/**
	 * 初始化代理人状态
	 * 
	 * @return
	 */
	@RequestMapping(value = "menu2list", method = RequestMethod.GET)
	public ModelAndView menu2List() {
		ModelAndView result = new ModelAndView("zzbconf/agentlist");
		List<Map<String,Object>> approve =  service.getQueryPageData();
		result.addObject("approveData", approve);
		return result;
	}

	@RequestMapping(value = "initagentlistpage", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> initAgentListPage(HttpSession session,
			@ModelAttribute PagingParams para,
			@ModelAttribute INSBAgentQueryModel qm, 
			@ModelAttribute INSCDept dept, @ModelAttribute QueryBean querybean)
			throws ParseException {
		if(para.getSort()!=null){
			if("agentkindstr".equals(para.getSort())){
				para.setSort("agentkind");
			}
			if("agentstatusstr".equals(para.getSort())){
				para.setSort("agentstatus");
			}
		}		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		// 处理时间字段
		// 1:为空不处理
		if (!"".equals(qm.getTesttimeendstr())
				&& qm.getTesttimeendstr() != null) {
			qm.setTesttimeend(format.parse(qm.getTesttimeendstr().toString()));
		}
		if (!"".equals(qm.getTesttimestr()) && qm.getTesttimestr() != null) {
			qm.setTesttime(format.parse(qm.getTesttimestr().toString()));
		}
		if (!"".equals(qm.getRegistertimestr())
				&& qm.getRegistertimestr() != null) {
			qm.setRegistertime(format.parse(qm.getRegistertimestr().toString()));
		}
		if (!"".equals(qm.getRegistertimeendstr())
				&& qm.getRegistertimeendstr() != null) {
			qm.setRegistertimeend(format.parse(qm.getRegistertimeendstr()
					.toString()));
		}

		String deptid=qm.getDeptid();
		if(StringUtil.isEmpty(deptid)){
			INSCUser user = (INSCUser) session.getAttribute("insc_user");
			deptid = user.getUserorganization();
		}

		Map<String, Object> map = BeanUtils.toMap(qm, dept, querybean, para);
		if(StringUtil.isNotEmpty(deptid)){
			map.put("deptid", deptid);
		} else {
            map.put("deptid", null);
        }

		return service.getAgentListPage(map);
	}
	
	
	/**
	 *  跳转保单列表页面
	 * 
	 * @param agentId
	 * @return
	 */
	@RequestMapping(value = "querybuttonlist", method = RequestMethod.GET)
	public ModelAndView querybuttonlist(@RequestParam(required = false, defaultValue = "") String agentId) {
		agentid = agentId;
		return new ModelAndView("zzbconf/policyitemlist");
	}
	/**
	 * 根据代理人id查询该代理人id下面所有的保单
	 * @param para
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "policcyitembyagentid", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> policcyitembyagentid(@ModelAttribute PagingParams para, @ModelAttribute INSBPolicyitem policyitem) throws ControllerException{
		policyitem.setId(agentid);
		Map<String, Object> map = BeanUtils.toMap(policyitem,para);
		return insbPolicyitemService.policcyitembyagentid(map);
	}
	
	@RequestMapping(value="getAgentData" ,method=RequestMethod.POST)
	@ResponseBody
	public String getAgentData(HttpSession session) throws ControllerException{
		String operator = ((INSCUser)session.getAttribute("insc_user")).getUsercode();
		Map<String, Object> mapResult = agtService.getAgentDataorOrg(operator, null);
		return com.alibaba.fastjson.JSONObject.toJSONString(mapResult);
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
	public ModelAndView mian2edit(@RequestParam(required = false, defaultValue = "") String agentId) {
		ModelAndView result = new ModelAndView("zzbconf/agentedit");
		List<INSBPermissionset> setList = new ArrayList<INSBPermissionset>();
//				insbPermissionsetService.queryOnUseSet();
		INSBAgent insbAgent = service.queryById(agentId);
		if(insbAgent != null){
			String deptid = insbAgent.getDeptid();
			Integer agentkind = insbAgent.getAgentkind();
			if(agentkind == null){
				agentkind = 1;//1为试用类型
			}
			setList = insbPermissionsetService.selectByDeptAgentkindAndOnUseSet(deptid, agentkind);
		}
		
		Map<String,Object> resultTemp = service.main2edit(agentId);
		result.addObject("agent", resultTemp.get("agent"));
		result.addObject("setlist", setList);
		result.addObject("approve", resultTemp.get("approve"));
		result.addObject("commonUseBranchNames", resultTemp.get("commonUseBranchNames"));
		result.addObject("certKinds", resultTemp.get("certKinds"));//证件类型
		result.addObject("agentlevelvalue", resultTemp.get("agentlevelvalue"));//代理人级别
		return result;
	}

	/**
	 * 获取权限包
	 * @param deptid
	 * @param agentkind
	 * @return
	 */
	@RequestMapping(value = "getAreaByDeptid", method = RequestMethod.GET)
	@ResponseBody
	public List<INSBPermissionset> getAreaByDeptid( String deptid,String agentkind) {
		Map<String,Object> param = new HashMap<String,Object>();
		List<INSBPermissionset> setList = new ArrayList<INSBPermissionset>();
		if(agentkind!=null){
			param.put("agentkind", agentkind);
		}		
		//根据所属机构平台或集团、用户类型、功能启用来显示功能包
		INSCDept ps = new INSCDept();
		ps.setComcode(deptid);
		ps = deptDao.selectOne(ps);
		if(ps!=null && ps.getParentcodes()!=null){
			String [] dept = ps.getParentcodes().split("[+]");
			LogUtil.info("权限包dept"+dept);
			for(int i = 1 ; i < dept.length ; i++){
				param.put("deptid", dept[i]);
				LogUtil.info("权限包deptid"+dept[i]);
				if(i<=3){
					List<INSBPermissionset> pss = insbPermissionsetDao.selectByDeptAndAgentkind(param);
					LogUtil.info("权限包pss"+pss);
					if(pss!=null){
						for (INSBPermissionset insbPermissionset : pss) {
							setList.add(insbPermissionset);
						}
					}
				}	
			}
		}
		LogUtil.info("权限包"+setList);
		return setList;	
	}
	
	/**
	 * 1：有功能包（新增后选择了功能包。修改时有功能包） 1.1： 初始化 修改 保存 走allot
	 * 
	 * 2:没有功能包 2.1：新增修改走代理人权限关系表
	 * 
	 * @param session
	 * @param permissionsetId
	 *            当前功能包id 新增时为空
	 * @param para
	 *            分页信息
	 * @param permission
	 *            （列表查询 留得参数）
	 * @param dept
	 *            组织机构
	 * @param querybean
	 *            （没有源码 暂且不用）
	 * @return
	 */
	@RequestMapping(value = "initpermissionlistpage", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> initpermissionListPage(HttpSession session,
			String permissionsetId, String agentId,
			@ModelAttribute PagingParams para,
			@ModelAttribute INSBPermission permission,
			@ModelAttribute INSCDept dept, @ModelAttribute QueryBean querybean) {

		Map<String, Object> map = BeanUtils.toMap(permission, dept, querybean,
				para);
		INSBPermissionset permissionset = insbPermissionallotService.getPermissionsetByAgentId(agentId);
		if (StringUtil.isEmpty(permissionsetId)) {
			permissionsetId = permissionset == null ? null : permissionset.getId();
		}
		return service.getPermissionListByAgentidAndPermissionsetid(permissionsetId, agentId);
//		return service.getpermissionListByPage(map, permissionsetId, agentId);
	}
	
	/**
	 * 清空已使用次数
	 * @param agentPermissionId
	 * @return
	 */
	@RequestMapping(value = "cleanSurplusnum", method = RequestMethod.GET, produces="text/html;charset=UTF-8")
	@ResponseBody
	public String cleanSurplusnum(String agentPermissionId,HttpSession session) {
		INSCUser user = (INSCUser) session.getAttribute("insc_user");
		String name=user.getName();
		return service.cleanSurplusnum(agentPermissionId,name);
	}
	
	@RequestMapping(value = "phoneverify", method = RequestMethod.GET)
	@ResponseBody
	public String phoneverify(String phonenum, String agentid) {
		
		int num = service.phoneverify(phonenum,agentid);
		if(num>0){
			return JSONObject.fromObject("{'msg':'该手机号已存在'}").toString();
		}else{
			return JSONObject.fromObject("{'msg':'手机号可以使用'}").toString();
		}
	}
	
	@RequestMapping(value = "deptverify", method = RequestMethod.GET)
	@ResponseBody
	public String deptverify(String deptid) {
		INSCDept dept = deptService.getOrgDeptByDeptCode(deptid);
		if(dept != null){
			if("05".equals(dept.getComtype())){
				return JSONObject.fromObject("{'msg':'true'}").toString();
			}else{
				return JSONObject.fromObject("{'msg':'false'}").toString();
			}
		}else{
			return JSONObject.fromObject("{'msg':'false'}").toString();
		}
	}
	
	/**
	 * 初始化当前功能包供应商
	 * 
	 * A：代理人没有选择功能包
	 * B：代理人选择功能包
	 * C：代理人关联协议
	 * D：代理人没有关联协议
	 * 
	 * 下面组合方式否能够编辑，有功能包编辑时默认修改代理人基本信息功能包字段值置为空
	 * 
	 * AD:不显示供应商结构
	 * AC:显示协议供应商
	 * 
	 * BD:显示功能包供应商
	 * BC:显示功能包、协议交集
	 * 
	 * 
	 * @param agentId 初始化非功能包代理人供应商时需要
	 * @param setId  下拉框更换功能包时需要
	 *            
	 *            
	 * @return
	 */
	@RequestMapping(value = "initprovidertree", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> initProviderTree(HttpSession session,String agentId,String setId) {
		INSBPermissionset permissionset = insbPermissionallotService.getPermissionsetByAgentId(agentId);
		if (StringUtil.isEmpty(setId)) {
			setId = permissionset == null ? null : permissionset.getId();
		}
		return service.getProviderTreeList(agentId,setId);
	}

	/**
	 * 查询条件 机构树（异步）
	 * 
	 * @return
	 */
	@RequestMapping(value = "initdepttree", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> initDeptTree(HttpSession session,
			@RequestParam(value = "id", required = false) String parentcode) {
		return deptService.queryTreeList(parentcode);
	}
	
	/**
	 * 认证任务所需机构树
	 * @param session
	 * @param parentcode
	 * @return
	 */
	@RequestMapping(value = "initdepttree2", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> initDeptTree2(HttpSession session,
			@RequestParam(value = "id", required = false) String parentcode) {
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		if (parentcode != null) {
			return deptService.queryTreeList4Data(parentcode,operator.getUserorganization());
		}
		return deptService.dept4Tree2(operator.getDeptinnercode(), null, null);
	}
	
	/**
	 * 根据登陆代理人所在机构查询条件 机构树（异步）
	 * 
	 * @return
	 */
	@RequestMapping(value = "initDeptTreeByAgent", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> initDeptTreeByAgent(HttpSession session,
			@RequestParam(value = "id", required = false) String parentcode) {
		INSCUser user = (INSCUser) session.getAttribute("insc_user");
		if(parentcode == null || "".equals(parentcode)){
			parentcode = user.getUserorganization();
			INSCDept dept = deptService.getOrgDept(parentcode);
			if(dept != null){
				parentcode = dept.getUpcomcode();
			}
		}
		return deptService.queryTreeList4Data(parentcode,user.getUserorganization());
	}

	/**
	 * 查询渠道
	 * @param session
	 * @param parentcode
	 * @return
	 */
	@RequestMapping(value = "initchanneltree", method = RequestMethod.POST)
	@ResponseBody
	public List<TreeVo> initChannelTree(HttpSession session,
										@RequestParam(value = "id", required = false) String parentcode) {
		return insbChannelService.queryTreeListByPid(parentcode);
	}
	/**
	 * 保存或者修改代理人基本信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "saveorupdateagent", method = RequestMethod.POST)
	@ResponseBody
	public String saveOrUpdateAgent(HttpSession session,@ModelAttribute INSBAgent agent) {
		INSCUser user = (INSCUser) session.getAttribute("insc_user");
		return service.saveOrUpdateAgent(user,agent);
	}
	@RequestMapping(value = "checkusercode", method = RequestMethod.POST)
	@ResponseBody
	public boolean checkUserCode(String agentcode,String id){
		int agentCount =  service.selectcountByAgentCode(agentcode,id);
		if(agentCount == 0){
			return  true ;
		}else{
			return false;
		}		
	}
	
	/**
	 * 修改关系表
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "agent2agentpermission", method = RequestMethod.GET)
	public ModelAndView agent2AgentPermission(String id) {
		INSBAgentpermission ap = service.agentMian2edit(id);
		ModelAndView result = new ModelAndView("zzbconf/agentpermissionedit");
		result.addObject("apData", ap);
		return result;
	}

	/**
	 * 新增或者修改代理人权限关系表
	 * 
	 * @param ap
	 * @return
	 */
	@RequestMapping(value = "updateagentpermission", method = RequestMethod.POST)
	@ResponseBody
	public BaseVo updateAgentPermission(HttpSession session,@ModelAttribute INSBAgentpermission ap) {
		BaseVo bv = new BaseVo();
		INSCUser user = (INSCUser) session.getAttribute("insc_user");
		try {
			ap.setOperator(user.getUsercode());
			ap.setModifytime(new Date());
			service.updateAgentPermission(ap);
			
			bv.setStatus("1");
			bv.setMessage("操作成功！");
		} catch (ParseException e) {
			bv.setStatus("2");
			bv.setMessage("操作失败！");
			e.printStackTrace();
		}

		return bv;
	}

	/**
	 * 删除代理人权限关系
	 * 
	 * @param ap
	 * @return
	 */
	@RequestMapping(value = "deleteagentpermission", method = RequestMethod.POST)
	@ResponseBody
	public BaseVo deleteagentpermission(HttpSession session, @ModelAttribute INSBAgentpermission ap) {
		BaseVo bv = new BaseVo();
		try {
			INSCUser operator = (INSCUser) session.getAttribute("insc_user");
			LogUtil.info("删除代理人权限关系agentid:%s,操作人:%s", ap.getAgentid(), operator.getUsercode());
			service.removeAgentPermission(ap);
			bv.setStatus("1");
			bv.setMessage("操作成功！");
		} catch (Exception e) {
			bv.setStatus("2");
			bv.setMessage("操作失败！");
			e.printStackTrace();
		}

		return bv;
	}

	@RequestMapping(value = "saveagentproviderdata", method = RequestMethod.POST)
	@ResponseBody
	public BaseVo saveagentproviderdata(HttpSession session,String providerIds,
			String agentId) {
		BaseVo bv = new BaseVo();
		 try {
//			INSCUser user = (INSCUser) session.getAttribute("insc_user");
			service.saveAgentProvider(agentId, providerIds);
			bv.setStatus("1");
			bv.setMessage("操作成功！");
		} catch (Exception e) {
			bv.setStatus("2");
			bv.setMessage("操作失败！");
			e.printStackTrace();
		}
		 return bv;
	}

	/**
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "batchdeleteagentbyid", method = RequestMethod.POST)
	@ResponseBody
	public BaseVo batchDeleteAgentById(HttpSession session, String ids) {
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("批量删除代理人ids为%s,操作人:%s", ids, operator.getUsercode());
		BaseVo bv = new BaseVo();
		 try {
			String str = service.deleteAgentBatch(ids);
			if(!"".equals(str) && str != null){
				bv.setStatus("2");
				bv.setMessage(str);
			} else{
				bv.setStatus("1");
				bv.setMessage("操作成功！");
			}
		} catch (Exception e) {
			bv.setStatus("2");
			bv.setMessage("操作失败！");
			e.printStackTrace();
		}
		 return bv;
	}
	/*@RequestMapping(value = "batchdeleteagentbyid", method = RequestMethod.POST)
	@ResponseBody
	public BaseVo batchDeleteAgentById(String ids) {
		BaseVo bv = new BaseVo();
		try {
			service.deleteAgentBatch(ids);
			bv.setStatus("1");
			bv.setMessage("操作成功！");
		} catch (Exception e) {
			bv.setStatus("2");
			bv.setMessage("操作失败！");
			e.printStackTrace();
		}
		return bv;
	}*/

	@RequestMapping(value = "main2detail", method = RequestMethod.GET)
	public ModelAndView main2detail(String id) {
		ModelAndView result = new ModelAndView("zzbconf/agentdetailinfo");
		INSBAgent agent = service.queryById(id);
		//根据代理人工号获取代理人的备注信息
		result.addObject("comment","");
		if( agent != null && agent.getJobnum() != null ) {
			INSBCertification insbCertification = new INSBCertification();
			insbCertification.setAgentnum(agent.getJobnum());
			insbCertification = insbCertificationService.queryOne(insbCertification);
			if(insbCertification != null){
				result.addObject("comment",insbCertification.getNoti());
			}
		}
		String stationdeptname="";
		if(!"".equals(agent.getStationid()) && agent.getStationid()!=null ){
			INSCDept dept = deptService.queryById(agent.getStationid());
			stationdeptname = dept.getShortname();
			result.addObject("stationdeptname", stationdeptname);
		}
		result.addObject("agent", agent);
		return result;
	}
	
	/**
	 * 批量重置密码
	 * 
	 * @return
	 */
	@RequestMapping(value = "resetpwd", method = RequestMethod.GET)
	public Map<String, String> resetPwd(String Ids,HttpSession session) {
		INSCUser user = (INSCUser) session.getAttribute("insc_user");
		return service.updateResetPwd(Ids, user.getUsercode());
	}
	/**
	 * 绑定工号
	 * 
	 * @return
	 */
	@RequestMapping(value = "bandjobno", method = RequestMethod.GET)
	public Map<String, String> bandjobno(HttpSession session, String jobno, String idno, String phone) {
		LogUtil.info("bandjobno jobno: " + jobno + " phone: " + phone);
		INSCUser user = (INSCUser) session.getAttribute("insc_user");
		Map<String, String> resultMap = new HashMap<String, String>();
		INSBAgent  agent =  new INSBAgent();
		List<INSBAgent> agents = null;
		agent.setPhone(phone);
		agents = service.queryList(agent);
		if(null==agents||agents.size()<=0){
			resultMap.put("statu", "0");
		}else{
			String tempjobno = "";
			//Date firstLogintime = null;
			String referrer = "";
			Date regTime = null;
			Date firstOderSuccesstime = null;
			String pw = null;
			for(INSBAgent aget:agents){
				if("1".equals(String.valueOf(aget.getAgentkind()))){
					tempjobno = aget.getJobnum();
					//firstLogintime = aget.getFirstlogintime();
					referrer = aget.getReferrer();
					regTime = aget.getRegistertime();
					firstOderSuccesstime = aget.getFirstOderSuccesstime();
					pw = aget.getPwd();
				}
			}
			if (StringUtil.isEmpty(tempjobno)) {
				resultMap.put("statu", "3");
				return resultMap;
			}
			if(StringUtil.isNotEmpty(jobno)){
				agent.setPhone(null);
				agent.setAgentcode(jobno);
				agent.setAgentkind(2);
				agents = service.queryList(agent);
				if(null==agents||agents.size()<=0){
					resultMap.put("statu", "0");
					return resultMap;
				}

				for(INSBAgent agt:agents){
					agt.setOperator(user.getUsercode());
					if(jobno.equals(agt.getAgentcode())){
						agent=agt;
						String oldPwd = agent.getPwd();
						agent.setPhone(phone);
						agent.setMobile(phone);
						agent.setJobnum(jobno);
						agent.setAgentcode(jobno);
						agent.setTempjobnum(tempjobno);
						agent.setJobnumtype(2);
						agent.setAgentkind(2);
						agent.setModifytime(new Date());

						if (agent.getFirstlogintime() == null) {
							agent.setFirstlogintime(new Date());
						}

						agent.setRegistertime(regTime);
						agent.setFirstOderSuccesstime(firstOderSuccesstime);
						if (agent.getFirstlogintime() == null) {
							agent.setFirstlogintime(new Date());
						}
						agent.setReferrer(referrer);
						agent.setPwd(pw);
//						service.updateById(agent);
						service.updateAgentPwd(agent, oldPwd);//修改密码并发短信通知代理人
						LogUtil.info("绑定正式工号，修改代理人密码 代理人："+agent.toString()+" 操作人:"+user.getUsercode()+" 操作时间:"+ DateUtil.getCurrentDateTime());
						//修改order，保单信息
						service.updateOrderInsuranceInfo(jobno, tempjobno);

						LogUtil.info("bandjobno绑定正式工号,代理人工号：" + jobno + ", 推荐人工号：" +referrer +",参加营销活动 ,临时工号注册时间：" + regTime);
						//appMarketingActivitiesService.isCanParticipateMarketing(jobno, referrer,agent.getRegistertime());

						//20170601 hwc 实名认证通过后通知集团统一中心
						LogUtil.info("bandjobno绑定正式工号调用updateUserDetail"+ com.common.JsonUtil.serialize(agent)+" agentcode="+jobno);
						userCenterService.updateUserDetail(agent,jobno,referrer);
						//20170601 hwc end
					}else{
						agt.setAgentkind(1);
						agt.setAgentstatus(2);
						agt.setApprovesstate(4);
						agt.setModifytime(new Date());
						//agent.setFirstlogintime(firstLogintime);
						if (agt.getFirstlogintime() == null) {
							agt.setFirstlogintime(new Date());
						}
						service.updateById(agt);
					}
				}
				agent =  new INSBAgent();
				agent.setJobnum(tempjobno);
				agents = service.queryList(agent);
				for(INSBAgent agt:agents){
//					LogUtil.info("bandjobno delete record jobnum:" + agt.getJobnum());
					LogUtil.info("bandjobno delete record jobnum:%s,操作人:%s", agt.getJobnum(), user.getUsercode());
					service.deleteById(agt.getId());
				}
				INSBCertification query = new INSBCertification();
				query.setAgentnum(tempjobno);
				List<INSBCertification> certs = insbCertificationService.queryList(query);
				for(INSBCertification cert:certs){
					cert.setOperator(user.getUsercode());
					cert.setStatus(1);//绑定之后则认证成功
					cert.setModifytime(new Date());
					insbCertificationService.updateById(cert);
				}
				resultMap.put("statu", "1");
			}else{
				resultMap.put("statu", "2");
			}
		}
		return resultMap;
	}
	/**
	 * 处理跟踪跳转
	 * 
	 * @return
	 */
	@RequestMapping(value = "showcartasklist", method = RequestMethod.GET)
	public ModelAndView taskturn(@RequestParam(value = "agentId", required = false) String agentId) {
		ModelAndView result = new ModelAndView("zzbconf/agentCarTaskManage");
		INSBAgent ag = service.queryById(agentId);
		String agentNum = ag.getAgentcode();
		result.addObject("agentNum",agentNum);
		return result;
	}
	
	
	
	/**
	 * 处理跟踪
	 * 
	 * @return
	 */
	@RequestMapping(value = "showcartaskmanagelist", method = RequestMethod.GET)
	@ResponseBody
	public String showcartaskmanagelist(@ModelAttribute PagingParams para,@RequestParam(value = "agentNum", required = false) String agentNum) {
//		PagingParams pagingParams = new PagingParams();
		Map<String, Object> paramMap = BeanUtils.toMap(para);
		String taskstatus = null;
		
		paramMap.put("agentNum", agentNum); //代理人工号
		List<String> tasktypeList = null;//任务类型集合
		paramMap.put("tasktype", tasktypeList);
	    return service.getJSONOfCarTaskListByMap(paramMap, taskstatus);
		
	}

	@RequestMapping(value = "getSetList", method = RequestMethod.GET)
	@ResponseBody
	public List<INSBPermissionset> getSetList(HttpSession session) throws ControllerException {
		INSCUser user = (INSCUser) session.getAttribute("insc_user");
		String deptid = user.getUserorganization();
		LogUtil.info("getSetList deptid:" + deptid);
		return insbPermissionsetService.selectPermissionset(deptid);
	}

	@RequestMapping(value = "addfuncs", method = RequestMethod.POST)
	@ResponseBody
	public boolean funcsOK(String setid, String deptid, HttpSession session) throws ControllerException {
		INSCUser user = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("funcsOK setid:" + setid+ "deptid:" + deptid);
		return insbPermissionsetService.saveFuncs(setid, deptid, user.getUsercode());
	}
	
	/**
	 * 批量关联权限包
	 * @param setcode
	 * @param deptid
	 * @param session
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "addfuncsAll", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> funcsOKAll(String setcode,String deptid, HttpSession session) {
		Map<String,Object> map=new HashMap<String,Object>();
		INSCUser user = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("funcsOK1 setcode:" + setcode+ "deptid:" + deptid);
		String[] arr=deptid.split(",");
		System.out.println(arr.length);
		List<String> list=new ArrayList<String>();
		Collections.addAll(list,arr);
		try {
			List<ExportPermissionInfoVo> infovoList=insbPermissionsetService.importAgentRelationPermission(setcode,list,user.getName());
			JSONArray jsonstr=JSONArray.fromObject(infovoList);
			for(ExportPermissionInfoVo vo:infovoList){
				if(!"成功".equals(vo.getReason())){
					//ResponseEntity<byte[]> seq=insbPermissionsetService.exPortResult(jsonstr.toString());
//					ModelAndView view=new ModelAndView("zzbconf/resultDefault");
//					view.addObject(seq);
//					return view;
					map.put("status",2);
					map.put("result", jsonstr.toString());
					session.setAttribute("result", jsonstr.toString());
					return map;
				}
			}
//			return new ModelAndView("zzbconf/resultOk");
			map.put("status", 1);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status","3");
			map.put("message",e.getMessage());
			return map;
		}
	}
	
	     @RequestMapping(value="download")
	     public String download(HttpServletRequest request,HttpServletResponse response) throws IOException{
	    	 String str = (String) request.getSession().getAttribute("result");
	    	 request.getSession().removeAttribute("result");
	    	 String fileName="关联报错Excel";
	      //将json型字符串转换为ExportPermissionInfoVo对象集合
	         LogUtil.info("str========:"+str);
	 		 JSONArray json=JSONArray.fromObject(str);
			 List<ExportPermissionInfoVo> list=(List<ExportPermissionInfoVo>)JSONArray.toCollection(json,ExportPermissionInfoVo.class);
	 		 LogUtil.info("List<ExportPermissionInfoVo>:"+list);
	 		 String columnNames[]={"序号","工号","手机号","关联结果","原因"};//列名
	         String keys[] =  {"id","jobnum","mobile","result","reason"};//map中的key
	         ByteArrayOutputStream os = new ByteArrayOutputStream();
	         try {
	             ExcelUtil.createWorkBook(list,keys,columnNames).write(os);
	         } catch (IOException e) {
	             e.printStackTrace();
	         }
	         byte[] content = os.toByteArray();
	         InputStream is = new ByteArrayInputStream(content);
	         // 设置response参数，可以打开下载页面
	         response.reset();
	         response.setContentType("application/vnd.ms-excel;charset=utf-8");
	         response.setHeader("Content-Disposition", "attachment;filename="+ new String((fileName + ".xls").getBytes(), "iso-8859-1"));
	         ServletOutputStream out = response.getOutputStream();
	         LogUtil.info("ServletOutputStream:"+out);
	         BufferedInputStream bis = null;
	         BufferedOutputStream bos = null;
	         try {
	             bis = new BufferedInputStream(is);
	             bos = new BufferedOutputStream(out);
	             byte[] buff = new byte[2048];
	             int bytesRead;
	             // Simple read/write loop.
	             while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
	                 bos.write(buff, 0, bytesRead);
	             }
	         } catch (final IOException e) {
	             throw e;
	         } finally {
	             if (bis != null)
	                 bis.close();
	             if (bos != null)
	                 bos.close();
	         }
	         return null;
	     }
}
