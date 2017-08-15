package com.zzb.conf.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCCodeService;
import com.cninsure.system.service.INSCDeptService;
import com.common.PagingParams;
import com.zzb.cm.service.INSBFilelibraryService;
import com.zzb.conf.dao.INSBAgreementDao;
import com.zzb.conf.dao.INSBProviderDao;
import com.zzb.conf.entity.INSBAutoconfigshow;
import com.zzb.conf.entity.INSBEdiconfiguration;
import com.zzb.conf.entity.INSBEdidescription;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.service.INSBAutoconfigService;
import com.zzb.conf.service.INSBAutoconfigshowService;
import com.zzb.conf.service.INSBEdiconfigurationService;
import com.zzb.conf.service.INSBElfconfService;
import com.zzb.conf.service.INSBProviderService;
import com.zzb.conf.service.INSBProviderandediService;
import com.zzb.model.EDIConfModel;
import com.zzb.model.elfAbilityModel;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/edi/*")
public class INSBEDIDescriptionController extends BaseController{
	@Resource
	private INSBElfconfService insbelfconfservice;
	@Resource
	private INSBEdiconfigurationService ediService;
	
	@Resource
	private INSBFilelibraryService inscFilelibraryService;
	
	@Resource
	private INSBProviderService insbProviderService;
	
	@Resource
	private INSBProviderandediService insbProviderandediService;
	
	@Resource
	private INSCCodeService inscCodeService;
	
	@Resource
	private INSBAutoconfigService insbAutoconfigService;
	
	@Resource
	private INSCDeptService inscDeptService;
	
	@Resource
    private HttpServletRequest request;
	@Resource
	private INSCDeptDao deptDao;
	@Resource
	private INSBProviderDao providerDao;
	@Resource
	private INSBAgreementDao agreementDao;
	
	@Resource
	private INSBAutoconfigshowService insbAutoconfigshowService;
	
	/**
	 * 跳转到列表页面
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list() throws ControllerException{
		ModelAndView model = new ModelAndView("zzbconf/ediconflist");
		return model;
	}
	
	/**
	 * EDI列表
	 * @param para
	 * @param edi
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "initedilist", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> initUserList(@ModelAttribute PagingParams para, @ModelAttribute INSBEdiconfiguration edi) throws ControllerException{
		Map<String, Object> map = BeanUtils.toMap(edi,para);
		return ediService.initEDIList(map);
	}
	
	/**
	 * 跳转新增页面
	 * @param edi
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "addedi", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView addedi(@RequestParam(value="id",required=false) String id)throws ControllerException{
		ModelAndView model = new ModelAndView("zzbconf/edisaveorupdate");
//		List<INSCCode> capacityconf = inscCodeService.queryINSCCodeByCode("capacityconf","capacityconf");
//		model.addObject("capacityconf",capacityconf);
		return model;
	}
	
	/**
	 * 添加或修改edi配置
	 * @param session
	 * @param edi
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "saveorupdate", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView  saveorupdate(HttpSession session,
			@ModelAttribute EDIConfModel edimodel)throws ControllerException{
		ModelAndView model = new ModelAndView("zzbconf/ediconflist");
		String operator = ((INSCUser)session.getAttribute("insc_user")).getUsercode();
		Date date = new Date();
//		String uuid = UUIDUtils.random();
//		String currentDate = DateUtil.getCurrentDate();
		
//		List<INSBProviderandedi> proediList = edimodel.getInsbproviderandedi();
//		INSBProviderandedi proandedibean  = new INSBProviderandedi();
		if(StringUtil.isEmpty(edimodel.getEdiconfid())){
			edimodel.setEdiconfid(UUIDUtils.random());
			edimodel.setOperator(operator);
			edimodel.setCreatetime(date);
			edimodel.setModifytime(date);
//			if (!file.isEmpty()) {
//				inscFilelibraryService.upload(request,file, "ediconf", "ediconf", operator);
//			}
//			if(!StringUtil.isEmpty(file)){
//				edimodel.setEdipath("http://"+ValidateUtil.getConfigValue("localhost.ip")+":"+ValidateUtil.getConfigValue("localhost.port") + "/"+
//						ValidateUtil.getConfigValue("localhost.projectName")+"/static/upload/" + currentDate + "/" + uuid +"."+ "ediconf");
//			}
			ediService.addedi(edimodel);
//			添加关系表信息
//			if(proediList!=null){
//				for (INSBProviderandedi list :proediList) {
//					proandedibean.setId(UUIDUtils.random());
//					proandedibean.setEdiid(edimodel.getEdiconfid());
//					proandedibean.setProviderid(list.getProviderid());
//					proandedibean.setOperator(operator);
//					proandedibean.setCreatetime(date);
//					proandedibean.setModifytime(date);
//					insbProviderandediService.addproandedi(proandedibean);
//				}
//			}
		}else{
			edimodel.setOperator(operator);
			edimodel.setModifytime(date);
//			if (!file.isEmpty()) {
//				inscFilelibraryService.upload(request,file, "ediconf", "ediconf", operator);
//			}
//			if(!file.isEmpty()&&file.getOriginalFilename()!=null){
//				edimodel.setEdipath("http://"+ValidateUtil.getConfigValue("localhost.ip")+":"+ValidateUtil.getConfigValue("localhost.port") + "/"+
//						ValidateUtil.getConfigValue("localhost.projectName")+"/static/upload/" + currentDate + "/" + uuid +"."+ "ediconf");
//			}
			ediService.updateedi(edimodel);
//			删除关系
//			insbProviderandediService.deleteRelation(edimodel.getEdiconfid());
////			修改关系表
//			if(proediList!=null){
//				for (INSBProviderandedi list :proediList) {
//					proandedibean.setId(UUIDUtils.random());
//					proandedibean.setEdiid(edimodel.getEdiconfid());
//					proandedibean.setProviderid(list.getProviderid());
//					proandedibean.setOperator(operator);
//					proandedibean.setCreatetime(date);
//					proandedibean.setModifytime(date);
//					insbProviderandediService.addproandedi(proandedibean);
//				}
//			}
		}
		return model;
	}
	
	/**
	 * 查看
	 * @param id
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "ediedit", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView  ediedit( @RequestParam("id") String id)throws ControllerException{
		ModelAndView model = new ModelAndView("zzbconf/ediedit");
		List<INSBEdidescription> edidesc = ediService.queryediinfoByid(id);
		model.addObject("edidesc", edidesc);
		model.addObject("ediconf", ediService.queryById(id));
		model.addObject("prolist",insbProviderService.queryListPro(id));
		List<INSCCode> conftype = inscCodeService.queryINSCCodeByCode("conftype","conftype");
		model.addObject("conftype",conftype);//字典表配置类型（01：报价配置 :02：核保配置:03：续保配置.04:承保配置）
		return model;
	}
	
	
	/**
	 * 根据edi的id删除配置信息
	 * @param session
	 * @param id
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "deletebyid", method = RequestMethod.GET)
	@ResponseBody
	public String deleteById(HttpSession session, @RequestParam(value="id") String id) throws ControllerException{
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("EDI进入配置信息删除id为%s,操作人:%s", id, operator.getUsercode());
//		删除配置表信息
		int confcount = ediService.deleteById(id);
//		删除关系表信息
		insbProviderandediService.deleteRelation(id);
//		删除edi详情表信息
		ediService.deleteDetail(id);
//		根据EDI id删除相关的自动化配置表
//		insbAutoconfigService.deleteByeElfId(id);
		insbAutoconfigService.deleteByeElfId(id, "01");
		JSONObject jsonObject = new JSONObject();
		jsonObject.accumulate("confcount", confcount);
		return jsonObject.toString();
	}
	
	/**
	 * 保险公司树
	 * @param parentcode
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="queryprotree",method=RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> queryProList(HttpSession session,@RequestParam(value="id",required=false) String parentcode) throws ControllerException{
		if(null != parentcode && !"".equals(parentcode)){
			return insbProviderService.queryTreeList(parentcode);
		}else{
			String userdept = ((INSCUser)session.getAttribute("insc_user")).getUserorganization();
			INSCDept dept = deptDao.selectById(userdept);
			if("".equals(dept.getUpcomcode()) || "1200000000".equals(userdept) ||null == dept.getUpcomcode()){
				List<Map<String, String>> list = new ArrayList<Map<String,String>>();
				List<Map<String,String>> providerIdList = providerDao.selectEdiAllProvider(userdept);
				for(Map<String, String> pro : providerIdList){
					Map<String, String> map = new HashMap<String, String>();
					map.put("id", pro.get("id"));
					map.put("prvcode", pro.get("prvcode"));
					map.put("pId", pro.get("pId"));
					map.put("name",pro.get("name"));
					map.put("isParent", "1".equals(pro.get("isParent"))? "true" : "false");
					list.add(map);
				}
				return list;
			}else{
				List<Map<String,String>> providerIdList = providerDao.selectEdiProvider(userdept);
				List<Map<String, String>> list = new ArrayList<Map<String,String>>();
				for(Map<String, String> pro : providerIdList){
					Map<String, String> map = new HashMap<String, String>();
					map.put("id", pro.get("id"));
					map.put("prvcode", pro.get("prvcode"));
					map.put("pId", pro.get("pId"));
					map.put("name",pro.get("name"));
					map.put("isParent", "1".equals(pro.get("isParent"))? "true" : "false");
					list.add(map);
				}
				return list;
			}
			
		}
	}
	
//	public List<Map<String,String>>
	
	/**
	 * 选择机构
	 * @param parentcode
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="querydepttree",method=RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> queryDeptList(HttpSession session,@RequestParam(value="id",required=false) String parentcode) throws ControllerException{
        INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		
		List<Map<String,String>> resultinstitutionList = inscDeptService.queryTreeList4Data(parentcode,operator.getUserorganization());
		return resultinstitutionList;
	}
	
	/**
	 * 添加自动化配置信息
	 * @param session
	 * @param providerid
	 * @param deptid
	 * @param conftype
	 * @param confid
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "addautoconf",method = RequestMethod.GET)
	@ResponseBody
	public String addautoconf(HttpSession session,@RequestParam(required=false) String providerid,
			@RequestParam(required=false) String deptid,@RequestParam(required=false) String conftype,@RequestParam(required=false) String confid,@RequestParam(required=false) String ediid)throws ControllerException{
		JSONObject jsonObject = new JSONObject();
		INSCUser user = (INSCUser) session.getAttribute("insc_user");
		INSCDept dept = inscDeptService.queryById(deptid);
		String msg = "";
		if(confid==null||"".equals(confid)){
			INSBAutoconfigshow cn = new INSBAutoconfigshow();
			cn.setProviderid(providerid);
			cn.setDeptid(deptid);
			cn.setContentid(ediid);
			cn.setQuotetype("01");
			List<INSBAutoconfigshow> list =  insbAutoconfigshowService.queryList(cn);
			if(list!=null&&list.size()>0){
				INSBProvider p = insbProviderService.queryById(list.get(0).getProviderid());
				String proname = "";
				INSCDept d = inscDeptService.queryById(list.get(0).getDeptid());
				String deptname = "";
				if(p!=null){
					proname = p.getPrvname();
					deptname = d.getComname();
				}
				msg = "所选:"+proname+"供应商和"+deptname+"已存在";
				jsonObject.accumulate("msg", msg);
				return jsonObject.toString();
			}else{
				INSBAutoconfigshow autoconf = new INSBAutoconfigshow();//添加信息到自动化配置表(show表)
				autoconf.setProviderid(providerid);
				autoconf.setDeptid(deptid);
				autoconf.setCreatetime(new Date());
				autoconf.setConftype(conftype);
				autoconf.setOperator(user.getUsercode());
				autoconf.setContentid(ediid);
				autoconf.setQuotetype("01");
				insbAutoconfigshowService.insert(autoconf);//自动化配置表（show显示表）
//				删除自动化配置表(根据show表id删除)
//				insbAutoconfigService.deleteautobyshowid(autoconf.getId(),dept.getParentcodes(),dept.getId());
//				insbAutoconfigService.deleteautobyshowid(autoconf.getId(),"01");
//				添加自动化配置表
//				insbAutoconfigService.saveEdiConfig("01",autoconf.getId(),providerid,deptid,ediid,conftype,user);
				
//				如果添加的show表中含有子节点，则删除
				LogUtil.info("EDIshow表删除parentcodes:%s,providerid:%s,comcode:%s,操作人:%s", dept.getParentcodes(), providerid, dept.getComcode(), user.getUsercode());
				insbAutoconfigshowService.deleterepetitionautoshow(dept.getParentcodes(),providerid,dept.getComcode());
				msg = "添加成功";
				jsonObject.accumulate("msg", msg);
				return jsonObject.toString();
			}
		}else{
			INSBAutoconfigshow autoconf = new INSBAutoconfigshow();//修改信息到自动化配置表(show表)
			autoconf.setProviderid(providerid);
			autoconf.setDeptid(deptid);
//			autoconf.setCreatetime(new Date());
			autoconf.setModifytime(new Date());
			autoconf.setConftype(conftype);
			autoconf.setOperator(user.getUsercode());
			autoconf.setContentid(ediid);
			autoconf.setQuotetype("01");
			autoconf.setId(confid);
			insbAutoconfigshowService.updateById(autoconf);
//			删除自动化配置表
//			insbAutoconfigService.deleteautobyshowid(autoconf.getId(),dept.getParentcodes(),dept.getId());
//			insbAutoconfigService.deleteautobyshowid(autoconf.getId(),"01");
//			添加自动化配置表
//			insbAutoconfigService.saveEdiConfig("01",autoconf.getId(),providerid,deptid,ediid,conftype,user);
			LogUtil.info("EDIshow表删除parentcodes:%s,providerid:%s,comcode:%s,操作人:%s", dept.getParentcodes(), providerid, dept.getComcode(), user.getUsercode());
			insbAutoconfigshowService.deleterepetitionautoshow(dept.getParentcodes(),providerid,dept.getComcode());
			msg = "修改成功";
			jsonObject.accumulate("msg", msg);
			return jsonObject.toString();
		}
	}
//	查询功能
	@RequestMapping(value = "queryautoconfig",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> queryautoconfig(
			@ModelAttribute PagingParams para,@RequestParam(required=false) String ediid,@ModelAttribute elfAbilityModel queryModel
			)throws ControllerException{
		Map<String, Object> map = BeanUtils.toMap(queryModel,para);
		return insbelfconfservice.abilityListByelfid(map,ediid);
	}
	
	/**
	 * 根据自动化配置id删除一条信息
	 * @param id
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "delautoconfbyid",method = RequestMethod.GET)
	@ResponseBody
	public String delautoconfById(HttpSession session, @RequestParam(required=false) String id)throws ControllerException{
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("EDI自动化配置删除id为%s,操作人:%s", id, operator.getUsercode());
		int count = insbAutoconfigshowService.deleteById(id);
//		删除自动化配置表信息
		insbAutoconfigService.deleteautobyshowid(id,"01");
		JSONObject jsonObject = new JSONObject();
		jsonObject.accumulate("count", count);
		return jsonObject.toString();
	}
	/**
	 * 根据精灵id获取能力列表(show)
	 * @param para
	 * @param elfconf
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "abilitylist", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> abilityList(@ModelAttribute PagingParams para, @ModelAttribute elfAbilityModel model,@RequestParam(required=false) String ediid) throws ControllerException{
		Map<String, Object> map = BeanUtils.toMap(model,para);
		return insbelfconfservice.abilityListByelfid(map,ediid);
	}
	
	/**
	 * 根据id查询自动化配置信息(show)
	 * @param id
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "selectautobyid", method = RequestMethod.GET)
	@ResponseBody
	public elfAbilityModel selectautobyid(@RequestParam(required=false) String id) throws ControllerException{
		elfAbilityModel model = new elfAbilityModel();
		INSBAutoconfigshow auto =  insbAutoconfigshowService.queryById(id);
		if(auto!=null){
			INSBProvider pro = insbProviderService.queryById(auto.getProviderid());
			INSCDept dept = inscDeptService.queryById(auto.getDeptid());
			if(pro!=null){
				model.setProvidername(pro.getPrvname());
			}
			if(dept!=null){
				model.setDeptname(dept.getComname());
			}
			model.setProviderid(auto.getProviderid());
			model.setDeptid(auto.getDeptid());
			model.setAbility(auto.getConftype());
		}
		return model;
	}
	
}