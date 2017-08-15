package com.zzb.conf.controller;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCDeptService;
import com.common.PagingParams;
import com.zzb.cm.service.INSBFilebusinessService;
import com.zzb.cm.service.INSBFilelibraryService;
import com.zzb.conf.component.INSBRiskUpdate4Cif;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.entity.INSBRuleBase;
import com.zzb.conf.service.INSBProviderService;
import com.zzb.conf.service.INSBRulebseService;
import com.zzb.model.CifRiskModel;
import com.zzb.model.ProOrParModel;

/**
 * 供应商
 */
@Controller
@RequestMapping("/provider/*")
public class INSBProviderContorller extends BaseController{
	
	@Resource
	private INSBProviderService insbProviderService;
	
	@Resource
	private INSCDeptService service;
	
	@Resource
	private INSBFilelibraryService inscFilelibraryService;
	
	@Resource
	private INSBRiskUpdate4Cif riskUpdate4Cif;
	@Resource
	private INSBFilebusinessService insbFilebusinessService;
	@Resource
	private INSBRulebseService rulebseService;
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list() throws ControllerException{
		ModelAndView model = new ModelAndView("zzbconf/providerlist");
		return model;
	}
	
	/**
	 * 初始化保险公司列表树
	 * @param parentcode
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "inittreeprolist",method= RequestMethod.POST)
	@ResponseBody
	public String initTreeList(@RequestParam(value="root", required=false)   String parentcode)throws ControllerException{
		List<Map<Object,Object>> resultinstitutionList = new ArrayList<Map<Object,Object>>();
		resultinstitutionList = insbProviderService.queryProTreeList(parentcode);
		System.out.println(JSONArray.fromObject(resultinstitutionList).toString()+"供应商json");
		return JSONArray.fromObject(resultinstitutionList).toString();
	}
	
	
	/**
	 * 查询
	 * @param para
	 * @param queryModel
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "queryprotreelist",method= RequestMethod.GET)
	@ResponseBody
	public String queryProTreeList(@ModelAttribute PagingParams para,@ModelAttribute ProOrParModel queryModel)throws ControllerException{
		List<Map<Object,Object>> resultinstitutionList = new ArrayList<Map<Object,Object>>();
		resultinstitutionList = insbProviderService.queryProTreeList2(queryModel);
		System.out.println(JSONArray.fromObject(resultinstitutionList).toString()+"查询结果的json");
		return JSONArray.fromObject(resultinstitutionList).toString();
	}
	
	/**
	 * 根据id查看或修改供应商详细信息
	 * @param id
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "queryproinfobyid",method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView queryProinfoById(@RequestParam(value="id", required=false) String id,@RequestParam(value="root", required=false)   String parentcode)throws ControllerException{
		ModelAndView model = new ModelAndView("zzbconf/provideredit");
		INSBProvider pro = insbProviderService.queryProinfoById(id);
		model.addObject("pro", insbProviderService.queryByPrvcode((pro.getParentcode()!=null||!"".equals(pro.getParentcode()))?pro.getParentcode():"0"));
		model.addObject("dept", service.queryById(pro.getAffiliationorg()!=null?pro.getAffiliationorg():""));
		model.addObject("proobject", pro);
		model.addObject("rule",rulebseService.selectById(pro.getPermissionorg()!=null?pro.getPermissionorg():""));
		return model;
	}
	
	@RequestMapping(value = "queryid",method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView queryId(@RequestParam(value="id", required=false) String id,@RequestParam(value="root", required=false)   String parentcode)throws ControllerException{
		ModelAndView model = new ModelAndView("zzbconf/providerlist");
		INSBProvider pro = insbProviderService.queryProinfoById(id);
		model.addObject("proid", pro);
		return model;
	}
	
	@RequestMapping(value="queryprotree",method=RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> queryProList(@RequestParam(value="id",required=false) String parentcode) throws ControllerException{
		System.out.println("parentcode="+parentcode);
		return insbProviderService.queryTreeList(parentcode);
	}
	@RequestMapping(value="queryallprotree",method=RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> queryallprotree() throws ControllerException{
		return insbProviderService.queryAllProTreeList();
	}
	@RequestMapping(value="querydepttree",method=RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> queryDeptList(@RequestParam(value="id",required=false) String parentcode) throws ControllerException{
		System.out.println("parentcode="+parentcode);
		return service.queryTreeList(parentcode);
	}
	
	
	@RequestMapping(value="queryprotreefirst",method=RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> queryDeptListFirst(@RequestParam(value="id",required=false) String parentcode) throws ControllerException{
		System.out.println("parentcode="+parentcode);
		return insbProviderService.queryTreeListFirst(parentcode);
	}
	
	/**
	 * 初始化规则列表(查询)
	 * @param para
	 * @param rulebase
	 * @return
	 */
	@RequestMapping(value = "initrulelist", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> initrulelist(@ModelAttribute PagingParams para,@ModelAttribute INSBRuleBase rulebase) {
		Map<String, Object> map = BeanUtils.toMap(rulebase, para);
		return rulebseService.initRuleBase(map);
	}
	
	@RequestMapping(value = "jumpadd",method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView jump(){
		ModelAndView model = new ModelAndView("zzbconf/provideradd");
		return model;
	}
	/**
	 * 添加或修改供应商
	 * @param session
	 * @param pro
	 * @return
	 * @throws Exception 
	 */
/*	@RequestMapping(value = "saveorupdatepro",method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView saveOrUpdatePro(HttpSession session ,@ModelAttribute INSBProvider pro,@RequestParam(value="file", required=false) MultipartFile file,
		@RequestParam(value="filedescribe", required=false) String filedescribe)throws ControllerException{
		ModelAndView mav = new ModelAndView("zzbconf/providerlist"); 
		String operator = ((INSCUser)session.getAttribute("insc_user")).getUsercode();
		String path = ValidateUtil.getConfigValue("upload.file.directory")+ "/" + DateUtil.getCurrentDate()+ "/" + file.getOriginalFilename();
		try {
			String uuid = UUIDUtils.random();
			Date now = new Date();
			if(StringUtil.isEmpty(pro.getId())){
				pro.setId(uuid);
				pro.setPrvtype("01");
				pro.setPrvcode(uuid);
				pro.setCreatetime(now);
				pro.setModifytime(now);
				pro.setOperator(operator);
				pro.setChildflag("0");
				//附件表添加信息
				Map<String, Object> status = inscFilelibraryService.cmUpload(file, "logo", uuid, operator);
				if(status!=null&&status.get("status").equals("success")){
					pro.setLogo(path);
				}
				insbProviderService.addProData(pro);
				CifRiskModel model = new CifRiskModel();
				model.setInsbprovider(pro);
				model.setContend(CifRiskModel.CONTEND_INSBPROVIDER);
				riskUpdate4Cif.addRiskData(model);
	
				if(pro.getParentcode()!=null){
					insbProviderService.updateProById(pro.getParentcode());
				}
			}else{
				if(file!=null){
					pro.setLogo(path);
				}
				pro.setModifytime(now);
				pro.setOperator(operator);
				insbProviderService.updateById(pro);
//				inscFilelibraryService.upload(file, "logo", "logo", operator);//附件表添加信息
//				INSBFilebusiness business = new INSBFilebusiness(); //业务关系表添加信息
//				business.setFilelibraryid(uuid);
//				business.setType("1");
//				insbFilebusinessService.updateById(business);
				CifRiskModel model = new CifRiskModel();
				model.setInsbprovider(pro);
				model.setContend(CifRiskModel.CONTEND_INSBPROVIDER);
				riskUpdate4Cif.addRiskData(model);
			}
			mav.addObject("flag", "success");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
		
	}*/
	
	/**
	 * 添加或修改供应商 （去掉新增）
	 * @param session
	 * @param pro
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "saveorupdatepro",method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView saveOrUpdatePro(HttpSession session ,@ModelAttribute INSBProvider pro,@RequestParam(value="file", required=false) MultipartFile file,
			@RequestParam(value="filedescribe", required=false) String filedescribe)throws ControllerException{
		ModelAndView mav = new ModelAndView("zzbconf/providerlist"); 
		String operator = ((INSCUser)session.getAttribute("insc_user")).getUsercode();
		try {
			Date now = new Date();
			if(StringUtil.isEmpty(pro.getId())){
				mav.addObject("flag", "fail");
			}else{
				pro.setModifytime(now);
				pro.setOperator(operator);
				insbProviderService.updateById(pro);
				CifRiskModel model = new CifRiskModel();
				model.setInsbprovider(pro);
				model.setContend(CifRiskModel.CONTEND_INSBPROVIDER);
				riskUpdate4Cif.addRiskData(model);
				mav.addObject("flag", "success");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
		
	}
	
	/**
	 * 根据id删除一个供应商
	 * @param id
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "deletbyid",method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView deleteById(String id)throws ControllerException{
		ModelAndView model = new ModelAndView("zzbconf/providerlist");
		if(id!=null&&!"".equals(id)){
			String parentcode = insbProviderService.queryById(id).getParentcode();
			int count = insbProviderService.deleteById(id);
			model.addObject("count", count+"");
			
			CifRiskModel cifModel = new CifRiskModel();
			cifModel.setIds(id);
			cifModel.setContend(CifRiskModel.CONTEND_INSBPROVIDER);
			riskUpdate4Cif.deleteRiskData(cifModel);
			
			if(insbProviderService.queryProTreeList(parentcode).size()<1){
				insbProviderService.updateProByIddel(parentcode);
			}
//			删除图片业务表
			
			return model;
		}else{
			return model;
		}
	}
//	==================================================================================================================>>
	/**
	 * 获取列表信息(zanshibuyong)
	 * @param para
	 * @param provider
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "showproviderlist", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> testUserList(@ModelAttribute PagingParams para,@ModelAttribute INSBProvider provider) throws ControllerException{
		Map<String, Object> map = BeanUtils.toMap(para,provider);
		return insbProviderService.showProviderList(map);
	}
	
	/**
	 * 为cif提供供应商数据
	 * 
	 * @param synctype 01：d全部数据  02：代表按修改时间查询 精确到天
	 * @param modifydate yyyy-MM-dd
	 * @return
	 */
	@RequestMapping(value="getprovider4Cif",method=RequestMethod.POST)
	@ResponseBody
	public List<INSBProvider> getProvider4Cif(@RequestParam(required=true) String synctype, @RequestParam(required=false) String modifydate){
		
		List<INSBProvider> providerList = new ArrayList<INSBProvider>();
		try {
			if("01".equals(synctype)){
				providerList = insbProviderService.queryAll();
			}else if("02".equals(synctype)){
				providerList = insbProviderService.queryDataByModifyTime(modifydate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return providerList;
	}
	
	/**
	 * 获取顶级供应商
	 * @return
	 */
	@RequestMapping(value="getgroupprovidedata",method=RequestMethod.GET)
	@ResponseBody
	public String getGroupprovidedata(){
		Map<Object, Object> resultMap=new HashMap<Object, Object>();
		List<INSBProvider> providerList = new ArrayList<INSBProvider>();
		try {
			providerList = insbProviderService.getprovidedata();
		} catch (Exception e) {
			e.printStackTrace();
		}
		resultMap.put("providerList", providerList);
		return JSONObject.fromObject(resultMap).toString();
	}
		
}