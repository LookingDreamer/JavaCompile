package com.zzb.conf.controller;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCCodeService;
import com.cninsure.system.service.INSCDeptService;
import com.common.PagingParams;
import com.zzb.cm.service.INSBFilelibraryService;
import com.zzb.conf.dao.INSBProviderDao;
import com.zzb.conf.entity.INSBAutoconfigshow;
import com.zzb.conf.entity.INSBElfconf;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.entity.INSBSkill;
import com.zzb.conf.service.INSBAutoconfigshowService;
import com.zzb.conf.service.INSBElfconfService;
import com.zzb.conf.service.INSBProviderService;
import com.zzb.conf.service.INSBSkillService;
import com.zzb.conf.service.INSBAutoconfigService;
import com.zzb.model.INSBelfconfSkillModel;
import com.zzb.model.elfAbilityModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 精灵配置
 */
@Controller
@RequestMapping("/elfconf/*")
public class INSBElfconfController extends BaseController{

    public static final String MSG = "msg";
    @Resource
	private INSBElfconfService service;
	
	@Resource
	private INSBFilelibraryService inscFilelibraryService;
	
	@Resource
	private INSCCodeService inscCodeService;
	
	@Resource
	private INSBSkillService insbSkillService;
	@Resource
	private INSBProviderService insbProviderService;
	
	@Resource
	private INSCDeptService inscDeptService;

	@Resource
	private INSBProviderDao providerDao;
	@Resource
	private INSCDeptDao deptDao;
	
	@Resource
	private INSBAutoconfigshowService insbAutoConfigShowService;

	@Resource
	private INSBAutoconfigService insbAutoconfigService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list() throws ControllerException{
		ModelAndView model = new ModelAndView("zzbconf/elfconflist");
		return model;
	}
	
	/**
	 * 精灵列表	
	 * @param para
	 * @param elfconf
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "initelfconflist", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> initELFConfList(@ModelAttribute PagingParams para, @ModelAttribute INSBElfconf elfconf) throws ControllerException{
		Map<String, Object> map = BeanUtils.toMap(elfconf,para);
		return service.initELFConfList(map);
	}
//	查询功能
	@RequestMapping(value = "queryautoconfig",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> queryautoconfig(
			@ModelAttribute PagingParams para,@ModelAttribute elfAbilityModel queryModel,
			@RequestParam(required=false) String elfid)throws ControllerException{
		Map<String, Object> map = BeanUtils.toMap(queryModel,para);
		System.out.println(map+"====="+elfid+"======");
		return service.abilityListByelfid(map,queryModel.getConfid());
	}
	/**
	 * 根据精灵id获取能力列表
	 * @param para
	 * @param elfconf
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "abilitylist", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> abilityList(@ModelAttribute PagingParams para, @ModelAttribute elfAbilityModel model,@RequestParam(required=false) String elfid) throws ControllerException{
		Map<String, Object> map = BeanUtils.toMap(model,para);
		return service.abilityListByelfid(map,elfid);
	}
	
	/**
	 * 根据id查看或修改精灵详细信息
	 * @param id
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "elfconfedit",method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView ELFConfEdit(String id)throws ControllerException{
		ModelAndView model = new ModelAndView("zzbconf/elfconfedit");
		List<INSCCode> capacityconf = inscCodeService.queryINSCCodeByCode("capacityconf","capacityconf");
		INSCCode inputcode = new INSCCode();
		inputcode.setParentcode("inputItem");
		List<INSCCode> input = inscCodeService.queryList(inputcode);
		INSCCode outputcode = new INSCCode();
		outputcode.setParentcode("outputItem");
		List<INSCCode> output = inscCodeService.queryList(outputcode);
		INSBElfconf elfconf = service.queryById(id);
		model.addObject("elfconf", elfconf);//精灵
		model.addObject("inputItem", input);//字典表输入项
		model.addObject("outputItem", output);//字典表输出项
		model.addObject("capacityconf",capacityconf);//字典表能力配置
		model.addObject("provider", insbProviderService.queryById((elfconf!=null?elfconf.getProid():"")!=null?elfconf.getProid():""));//供应商
		List<INSCCode> conftype = inscCodeService.queryINSCCodeByCode("conftype","conftype");
		model.addObject("conftype",conftype);//字典表配置类型（01：报价配置 :02：核保配置:03：续保配置.04:承保配置）
		List<INSBSkill> skillInputList =  insbSkillService.queryListByelfId(id,"in");
		List<INSBSkill> skillOutputList =  insbSkillService.queryListByelfId(id,"out");
		model.addObject("skillInputList",skillInputList);//技能表输入项
		model.addObject("skillOutputList",skillOutputList);//技能表输出项
		model.addObject("sbskill",insbSkillService.querySkillnameByelfid(id));
//		过滤掉右侧的技能输入项
		String filterin = insbSkillService.filter(id,"in");
		model.addObject("filterin",filterin);
//		过滤掉右侧的技能输出项
		String filterout = insbSkillService.filter(id,"out");
		model.addObject("filterout",filterout);
		return model;
	}
	/**
	 * 跳转到添加页面
	 * @return
	 */
	@RequestMapping(value = "addelfconf",method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView addELFConf(){
		ModelAndView model = new ModelAndView("zzbconf/elfconfsave");
		List<INSCCode> capacityconf = inscCodeService.queryINSCCodeByCode("capacityconf","capacityconf");
		model.addObject("capacityconf",capacityconf);
		INSCCode inputcode = new INSCCode();
		inputcode.setParentcode("inputItem");
		List<INSCCode> input = inscCodeService.queryList(inputcode);
		INSCCode outputcode = new INSCCode();
		outputcode.setParentcode("outputItem");
		List<INSCCode> output = inscCodeService.queryList(outputcode);
		model.addObject("inputItem", input);//字典表输入项
		model.addObject("outputItem", output);//字典表输出项
		List<INSCCode> conftype = inscCodeService.queryINSCCodeByCode("conftype","conftype");
		model.addObject("conftype",conftype);//字典表配置类型（01：报价配置 :02：核保配置:03：续保配置.04:承保配置）
		return model;
	}
	/**
	 * 添加或修改精灵配置
	 * @param session
	 * @param file
	 * @param filetype
	 * @param filedescribe
	 * @param elfconf
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "saveorupdateelfconf", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView saveOrUpdateELFConf(HttpSession session,
		@ModelAttribute INSBelfconfSkillModel  model) throws ControllerException{
		ModelAndView mav = new ModelAndView("zzbconf/elfconflist"); 
		String operator = ((INSCUser)session.getAttribute("insc_user")).getUsercode();
		Date now = new Date();
		int inlength = 0;
		if(model.getInputcode()!=null){
			inlength = model.getInputcode().split(",").length;
		}
		int outlength = 0;
		if(model.getOutputcode()!=null){
			outlength = model.getOutputcode().split(",").length;
		}
		if(StringUtil.isEmpty(model.getElfid())){
			INSBElfconf elfconf = new INSBElfconf();
			elfconf.setElfpath(model.getElfpath());
			elfconf.setCreatetime(now);
			elfconf.setOperator(operator);
			elfconf.setElftype(model.getElftype());
			elfconf.setElfname(model.getElfname());
			elfconf.setProid(model.getProid());
			elfconf.setCapacityconf(model.getCapacityconf());
			service.insert(elfconf);//精灵表
			String id = elfconf.getId();
			for (int i = 0; i < inlength; i++) {
				INSBSkill skill = new INSBSkill();
				skill.setCreatetime(now);
				skill.setSkillname(model.getSkillname());
				skill.setElfid(id);
				skill.setInputcode(model.getInputcode().split(",")[i]);
				skill.setOperator(operator);
				insbSkillService.insert(skill);//技能表 输入项
			}
			for (int i = 0; i < outlength; i++) {
				INSBSkill skill = new INSBSkill();
				skill.setCreatetime(now);
				skill.setSkillname(model.getSkillname());
				skill.setElfid(id);
				skill.setOutputcode(model.getOutputcode().split(",")[i]);
				skill.setOperator(operator);
				insbSkillService.insert(skill);//技能表 输出项
			}
		}else{
			INSBElfconf elfconf = new INSBElfconf();
			elfconf.setElfpath(model.getElfpath());
//			elfconf.setCreatetime(now);
			elfconf.setModifytime(now);
			elfconf.setOperator(operator);
			elfconf.setId(model.getElfid());
			elfconf.setElftype(model.getElftype());
			elfconf.setElfname(model.getElfname());
			elfconf.setCapacityconf(model.getCapacityconf());
			elfconf.setProid(model.getProid());
			service.updateById(elfconf);
			insbSkillService.deletebyelfid(model.getElfid());
			for (int i = 0; i < inlength; i++) {
				INSBSkill skill = new INSBSkill();
				skill.setCreatetime(now);
				skill.setElfid(model.getElfid());
				skill.setSkillname(model.getSkillname());
				skill.setInputcode(model.getInputcode().split(",")[i]);
				skill.setOperator(operator);
				insbSkillService.insert(skill); //输入项
			}
			for(int i = 0; i < outlength; i++){
				INSBSkill skill = new INSBSkill();
				skill.setCreatetime(now);
				skill.setElfid(model.getElfid());
				skill.setSkillname(model.getSkillname());
				skill.setOutputcode(model.getOutputcode().split(",")[i]);
				skill.setOperator(operator);
				insbSkillService.insert(skill); //输出项
			}
		}
		return mav;
	}
	/**
	 * 根据id删除一个精灵配置
	 * @param id
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "deletebyid",method = RequestMethod.GET)
	@ResponseBody
	public String deleteById(HttpSession session, @RequestParam(required=false) String id)throws ControllerException{
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("精灵配置删除id为%s,操作人:%s", id, operator.getUsercode());
		int count = service.deleteById(id);//删除一条精灵信息
		insbAutoconfigService.deleteByeElfId(id, "02");//根据精灵id删除相关的自动化配置表
		JSONObject jsonObject = new JSONObject();
		jsonObject.accumulate("count", count);
		return jsonObject.toString();
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
    @RequestMapping(value = "addautoconf", method = RequestMethod.GET)
    @ResponseBody
    public Map addautoconf(HttpSession session, @RequestParam(required = false) String providerid,
                              @RequestParam(required = false) String deptid, @RequestParam(required = false) String conftype, @RequestParam(required = false) String confid, @RequestParam(required = false) String elfid) throws ControllerException {
        Map<String, Object> result = new HashMap<>();
        INSCUser user = (INSCUser) session.getAttribute("insc_user");
        INSCDept dept = inscDeptService.queryById(deptid);
        INSBAutoconfigshow insbAutoConfigShow = new INSBAutoconfigshow(conftype, INSBAutoconfigshow.ELF_QUOTE_TYPE, null, elfid, providerid, deptid);

        if (StringUtils.isBlank(confid)) {
            createINSBAutoConfigShow(result, user, dept, insbAutoConfigShow);
        } else {
            updateINSBAutoConfigShow(providerid, confid, result, user, dept, insbAutoConfigShow);
        }

        return result;
    }

    private void updateINSBAutoConfigShow(@RequestParam(required = false) String providerid, @RequestParam(required = false) String confid, Map<String, Object> result, INSCUser user, INSCDept dept, INSBAutoconfigshow insbAutoconfigshow) {
        //修改信息到自动化配置表(show表)
        insbAutoconfigshow.setModifytime(new Date());
        insbAutoconfigshow.setOperator(user.getUsercode());
        insbAutoconfigshow.setId(confid);
        insbAutoConfigShowService.updateById(insbAutoconfigshow);
        // 删除自动化配置表
		LogUtil.info("精灵show表删除showid:%s,type:%s,操作人:%s", insbAutoconfigshow.getId(), INSBAutoconfigshow.ELF_QUOTE_TYPE, user.getUsercode());
		insbAutoConfigShowService.deleteAutoByShowId(insbAutoconfigshow.getId(), INSBAutoconfigshow.ELF_QUOTE_TYPE);
//			添加自动化配置表
        insbAutoConfigShowService.saveEdiConfig(INSBAutoconfigshow.ELF_QUOTE_TYPE, insbAutoconfigshow.getId(), insbAutoconfigshow.getProviderid(), insbAutoconfigshow.getDeptid(), insbAutoconfigshow.getContentid(), insbAutoconfigshow.getConftype(), user);
//			如果添加的show表中含有子节点，则删除
		LogUtil.info("精灵show表删除parentcodes:%s,providerid:%s,deptid:%s,操作人:%s", dept.getParentcodes(), providerid, dept.getId(), user.getUsercode());
		insbAutoConfigShowService.deleterepetitionautoshow(dept.getParentcodes(), providerid, dept.getId());
        result.put(MSG, "修改成功");
    }

    private void createINSBAutoConfigShow(Map<String, Object> result, INSCUser user, INSCDept dept, INSBAutoconfigshow insbAutoconfigshow) {
        List<INSBAutoconfigshow> list = insbAutoConfigShowService.queryList(insbAutoconfigshow);
        if (list != null && list.size() > 0) {
            INSBProvider p = insbProviderService.queryById(list.get(0).getProviderid());
            INSCDept d = inscDeptService.queryById(list.get(0).getDeptid());
            result.put(MSG, "所选:" + (p != null ? p.getPrvname() : "") + "(供应商)和" + (d != null ? d.getComname() : "") + "已存在");
        } else {
            insbAutoconfigshow.setCreatetime(new Date());
            insbAutoconfigshow.setOperator(user.getUsercode());
            insbAutoConfigShowService.insert(insbAutoconfigshow);//自动化配置表（show显示表）
			LogUtil.info("精灵show表删除parentcodes:%s,providerid:%s,deptid:%s,操作人:%s", dept.getParentcodes(), insbAutoconfigshow.getProviderid(), dept.getId(), user.getUsercode());
			insbAutoConfigShowService.deleterepetitionautoshow(dept.getParentcodes(), insbAutoconfigshow.getProviderid(), dept.getId());
            result.put(MSG, "添加成功");
        }
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
		LogUtil.info("精灵自动化配置删除id为%s,操作人:%s", id, operator.getUsercode());
		int count = insbAutoConfigShowService.deleteById(id);
//		删除自动化配置表信息
        insbAutoConfigShowService.deleteAutoByShowId(id, INSBAutoconfigshow.ELF_QUOTE_TYPE);
		JSONObject jsonObject = new JSONObject();
		jsonObject.accumulate("count", count);
		return jsonObject.toString();
	}
	
	/**
	 * 选择供应商
	 * @param parentcode
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="queryprotree",method=RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> queryProList(HttpSession session,@RequestParam(value="id",required=false) String parentcode, @RequestParam(value="type",required=false)String type,@RequestParam String proid) throws ControllerException{
		if(type!=null&&type.equals("stair")){
			return insbProviderService.queryTreeListStair();
		}else{
			if(proid!=null&&!"".equals(proid)){
				parentcode = proid;
				proid = "";
				return insbProviderService.queryTreeList(parentcode);
			}else{
				if(null != parentcode && !"".equals(parentcode)){
					return insbProviderService.queryTreeList(parentcode);
				}else{
					String userdept = ((INSCUser)session.getAttribute("insc_user")).getUserorganization();
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
	}
	
	/**
	 * 选择供应商 （选择能力）
	 * @param parentcode
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="queryprotreeOfablity",method=RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> queryProListOfAblity(HttpSession session,@RequestParam(value="id",required=false) String parentcode) throws ControllerException{
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
	
	/**
	 * 选择机构
	 * @param parentcode
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="querydepttree",method=RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> queryDeptList(HttpSession session,@RequestParam(value="id",required=false) String parentcode) throws ControllerException{
		/*return inscDeptService.queryTreeList(parentcode);*/
        INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		
		List<Map<String,String>> resultinstitutionList = inscDeptService.queryTreeList4Data(parentcode,operator.getUserorganization());
		return resultinstitutionList;
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
		INSBAutoconfigshow auto =  insbAutoConfigShowService.queryById(id);
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