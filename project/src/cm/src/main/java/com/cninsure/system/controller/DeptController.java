package com.cninsure.system.controller;
 
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.manager.scm.INSCSyncService;
import com.cninsure.system.service.INSCDeptService;
import com.zzb.conf.dao.INSBPermissionsetDao;
import com.zzb.conf.dao.INSCDeptpermissionsetDao;
import com.zzb.conf.entity.INSBPermissionset;
import com.zzb.conf.entity.INSBRegion;
import com.zzb.conf.entity.INSCDeptpermissionset;
import com.zzb.conf.service.INSBPermissionsetService;
import com.zzb.conf.service.INSCDeptpermissionsetService;

@Controller
@RequestMapping("/dept/*")
public class DeptController extends BaseController {
	
	@Resource
	private INSCDeptService service;
	@Resource
	private INSCSyncService syncService;
	@Resource
	private INSCDeptpermissionsetService  inscdeptpermissionsetService;
	@Resource
	private INSCDeptpermissionsetDao  inscdeptpermissionsetDao;
	@Resource
	private INSBPermissionsetService insbPermissionsetService;
	@Resource
	private INSBPermissionsetDao insbPermissionsetDao;
	
	@RequestMapping(value = "list" ,method =RequestMethod.GET )
	public ModelAndView returnInstitution()throws ControllerException{
		ModelAndView model = new ModelAndView("system/inscdeptlist");
		return model;
	}
	
	@RequestMapping(value = "inscdeptlist",method= RequestMethod.POST,produces = {"text/json;charset=UTF-8"})
	@ResponseBody
	public String institutionTreeList(@RequestParam(value="root", required=false)   String parentcode)throws ControllerException{
		List<Map<Object,Object>> resultinstitutionList = new ArrayList<Map<Object,Object>>();
		resultinstitutionList = service.queryDeptList(parentcode);
		System.out.println(JSONArray.fromObject(resultinstitutionList).toString()+"qiuxiaolin");
		return JSONArray.fromObject(resultinstitutionList).toString();
	}

	@RequestMapping(value = "updatedept",method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView updateOrg(HttpSession session ,@ModelAttribute INSCDept dept)throws ControllerException{
		LogUtil.info("INSCDept dept = " + dept);
		ModelAndView mav = new ModelAndView();
		Date now = new Date();
		String uuid = UUIDUtils.random();

		if(StringUtil.isEmpty(dept.getId())){
			dept.setId(dept.getComcode());
			//dept.setComcode(uuid);
			dept.setCreatetime(now);
			dept.setModifytime(now);
			dept.setChildflag("0");
			//dept.setStatus("0");
			dept.setOperator(((INSCUser)session.getAttribute("insc_user")).getUsercode());
			dept.setParentcodes(dept.getParentcodes() + "+" + dept.getUpcomcode());
			service.addDeptData(dept);
			if(dept.getUpcomcode()!=null){
				service.updateDeptById(dept.getUpcomcode());
			}
		}else{
			dept.setModifytime(new Date());
			dept.setOperator(((INSCUser)session.getAttribute("insc_user")).getUsercode());
			service.updateById(dept);	
		}
		if(dept != null && dept.getComcode()!=null){
			 String tryset= dept.getTryset();
			 String formalset = dept.getFormalset();
			 String channelset = dept.getChannelset();
			 String comcode = dept.getComcode();
			 Map<String, String> map = inscdeptpermissionsetService.queryByComcode(comcode);
			 Map<String, String> mapid = queryBySetcode(tryset,formalset,channelset);  
			 INSCDeptpermissionset ps = new INSCDeptpermissionset();
		     ps.setChannelsetid(mapid.get("channelsetid"));
			 ps.setFormalsetid(mapid.get("formalsetid"));
			 ps.setTrysetid(mapid.get("trysetid"));
			 ps.setComcode(comcode);	
			 ps.setOperator(((INSCUser)session.getAttribute("insc_user")).getUsercode());
			 if(map!=null){
					ps.setModifytime(now);
					inscdeptpermissionsetDao.updateBycomcode(ps);
			}else{
					ps.setCreatetime(now);
					inscdeptpermissionsetDao.insert(ps);
			}
		}
		mav.addObject("dept", dept);
		mav.addObject("flag", "success");
		return mav;
	}
	
	private Map<String,String> queryBySetcode(String tryset,String formalset,String channelset){	
		Map<String,String> map = new HashMap<>();
		String trysetid = insbPermissionsetDao.queryBytrysetcode(tryset);
		String formalsetid = insbPermissionsetDao.queryByformalsetcode(formalset);
		String channelsetid = insbPermissionsetDao.queryBychannelsetcode(channelset);
		map.put("trysetid", trysetid);
		map.put("formalsetid", formalsetid);
		map.put("channelsetid", channelsetid);
		return map;
	}
	
	
	@RequestMapping(value = "deletbyid",method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView deleteById(HttpSession session,String id)throws ControllerException{
		INSCUser user = (INSCUser) session.getAttribute("insc_user");
		//user.getUsercode();
		ModelAndView model = new ModelAndView("system/inscdeptlist");
		String upcode = service.queryById(id).getUpcomcode();
		int count = service.deleteById(id);
		model.addObject("count", count);

		//查询父节点的子节点数量
		if(service.queryDeptList(upcode).size()<1){
			//更新父节点无子节点
			service.updateDeptByIddel(upcode);
		}
		return model;
	}

	/**
	 * 删除检查当前节点是否存在或者存在子节点
	 * @param id
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "checkDept",method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView checkDept(String id)throws ControllerException{
		ModelAndView model = new ModelAndView();
		if (StringUtil.isEmpty(id)) return model;
		INSCDept dept = service.queryById(id);
		model.addObject("dept", dept);
		//查询父节点的子节点数量
		model.addObject("subNodeCount", service.queryDeptList(id).size());
		return model;
	}

	/**
	 * 新增/修改检查 comcode或者机构内部编码是否已存在
	 * @param id
	 * @param comcode 机构代码
	 * @param deptinnercode 机构内部编码
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "checkNewDept",method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView checkNewDept(String id, String comcode, String deptinnercode, String upcomcode)throws ControllerException{
		ModelAndView model = new ModelAndView();
		if (StringUtil.isNotEmpty(id)) {
			return model;
		}
		if (StringUtil.isEmpty(comcode)) {
			model.addObject("success", false);
			model.addObject("msg", "机构代码不能为空！");
			return model;
		}
		INSCDept dept = new INSCDept();
		dept.setComcode(comcode);
		long l = service.queryCount(dept);
		if (l > 0) {
			model.addObject("success", false);
			model.addObject("msg", "已存在相同的机构代码，请重新填写！");
			return model;
		}
		if (StringUtil.isEmpty(deptinnercode)) {
			model.addObject("success", false);
			model.addObject("msg", "机构内部编码不能为空！");
			return model;
		}

		dept.setComcode(null);
		dept.setDeptinnercode(deptinnercode);
		l = service.queryCount(dept);
		if (l > 0) {
			model.addObject("success", false);
			model.addObject("msg", "已存在相同的机构内部编码，请重新填写！");
			return model;
		}
		if (StringUtil.isEmpty(upcomcode)) {
			model.addObject("success", false);
			model.addObject("msg", "机构上级代码不能为空！");
			return model;
		}
		dept.setDeptinnercode(null);
		dept.setComcode(upcomcode);//查找上级机构
		try {
			dept = service.queryOne(dept);
		} catch (Exception e) {
			e.printStackTrace();
			model.addObject("success", false);
			model.addObject("msg", "查询上级机构信息出错，请联系管理员！");
			return model;
		}
		if (dept == null) {
			model.addObject("success", false);
			model.addObject("msg", "上级机构信息不存在！");
			return model;
		}
		String upDeptinnercode = dept.getDeptinnercode();//上级机构的内部编码
		if (upDeptinnercode != null && !deptinnercode.startsWith(upDeptinnercode)) {
			model.addObject("success", false);
			model.addObject("msg", "上级机构内部编码为" + upDeptinnercode + "，内部编码格式必须为" + upDeptinnercode + "xxx形式，请重新填写！");
			return model;
		}
		model.addObject("success", true);
		return model;
	}

	@RequestMapping(value = "querybyid",method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView queryById(String id)throws ControllerException{
		LogUtil.info("id:" + id);
		ModelAndView model = new ModelAndView("system/inscdeptlist");
		INSCDept s = service.queryById(id);		
		String comcode = s.getComcode();
		Map<String,String> ss = inscdeptpermissionsetService.queryByComcode(comcode);
		if(ss !=null){
			s.setTryset(ss.get("tryset"));
			s.setFormalset(ss.get("formalset"));
			s.setChannelset(ss.get("channelset"));
		}
		model.addObject("orgobject", s);
		return model;
	}
	
	@RequestMapping(value = "querybyparentcodes",method = RequestMethod.GET)
	@ResponseBody
	public List<INSBPermissionset> querybyparentcodes(String deptid, String agentkind)throws ControllerException{
		LogUtil.info("deptid:" + deptid,"agentkind:"+agentkind);
		List<INSBPermissionset> list = insbPermissionsetService.querybyparentcodes(deptid,agentkind);
		LogUtil.info("list:" + list);
		return list;
	}
	
	@RequestMapping(value="getSyncDeptData" ,method=RequestMethod.POST)
	@ResponseBody
	public String  getSyncDeptData(HttpSession session) throws ControllerException{
		Map<String,Object> map = new HashMap<String,Object>();
		String operator = ((INSCUser)session.getAttribute("insc_user")).getUsercode();
		Map<String, Object> mapResult = syncService.getSyncDeptData(operator,null);
		JSONObject jobj = JSONObject.fromObject(mapResult);
		return jobj.toString();
	}
	
	@RequestMapping(value="defaultSetId",method=RequestMethod.GET)
	@ResponseBody
	public INSCDeptpermissionset defaultSetId(String deptid,String agentKind){
		LogUtil.info("DeptController--INSCDeptpermissionset方法参数：deptid="+deptid+";agentKind="+agentKind);
		return inscdeptpermissionsetService.getSetid(deptid, agentKind);
	}
}
