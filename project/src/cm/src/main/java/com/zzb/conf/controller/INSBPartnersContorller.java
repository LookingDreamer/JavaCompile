package com.zzb.conf.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

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
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCDeptService;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.service.INSBProviderService;

/**
 * 合作商
 */
@Controller
@RequestMapping("/partners/*")
public class INSBPartnersContorller extends BaseController{
	
	@Resource
	private INSBProviderService insbProviderService;
	
	@Resource
	private INSCDeptService service;
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list() throws ControllerException{
		ModelAndView model = new ModelAndView("zzbconf/partnerslist");
		return model;
	}
	
	/**
	 * 初始化合作商表树
	 * @param parentcode
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "inittreeparlist",method= RequestMethod.POST)
	@ResponseBody
	public String initTreeList(@RequestParam(value="root", required=false)   String parentcode)throws ControllerException{
		List<Map<Object,Object>> resultinstitutionList = new ArrayList<Map<Object,Object>>();
		resultinstitutionList = insbProviderService.queryProTreeList(parentcode);
		System.out.println(JSONArray.fromObject(resultinstitutionList).toString()+"合作商json");
		return JSONArray.fromObject(resultinstitutionList).toString();
	}
	
	/**
	 * 根据id查看或修改合作商详细信息
	 * @param id
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "queryparinfobyid",method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView queryProinfoById(String id,@RequestParam(value="root", required=false)   String parentcode)throws ControllerException{
		ModelAndView model = new ModelAndView("zzbconf/partnersedit");
		INSBProvider par = insbProviderService.queryProinfoById(id);
		model.addObject("par", insbProviderService.queryByPrvcode((par.getParentcode()!=null||!"".equals(par.getParentcode()))?par.getParentcode():"0"));
		model.addObject("dept", service.queryById(par.getAffiliationorg()!=null?par.getAffiliationorg():""));
		model.addObject("parobject", par);
		return model;
	}
	
	@RequestMapping(value = "queryid",method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView queryId(String id,@RequestParam(value="root", required=false)   String parentcode)throws ControllerException{
		ModelAndView model = new ModelAndView();
		INSBProvider par = insbProviderService.queryProinfoById(id);
		model.addObject("parid", par);
		return model;
	}
	
	@RequestMapping(value="querypartree",method=RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> queryProList(@RequestParam(value="id",required=false) String parentcode) throws ControllerException{
		System.out.println("parentcode="+parentcode);
		return insbProviderService.queryTreeList(parentcode);
	}
	
	@RequestMapping(value="querydepttree",method=RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> queryDeptList(@RequestParam(value="id",required=false) String parentcode) throws ControllerException{
		System.out.println("parentcode="+parentcode);
		return service.queryTreeList(parentcode);
	}
	
	@RequestMapping(value = "jumpadd",method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView jump(){
		ModelAndView model = new ModelAndView("zzbconf/partnersadd");
		return model;
	}
	/**
	 * 添加或修合作商
	 * @param session
	 * @param pro
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "saveorupdatepar",method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView saveOrUpdatePar(HttpSession session ,@ModelAttribute INSBProvider par,@RequestParam(value="file", required=false) MultipartFile file)throws ControllerException{
		ModelAndView mav = new ModelAndView("zzbconf/partnerslist"); 
		String operator = ((INSCUser)session.getAttribute("insc_user")).getUsername();
		String uuid = UUIDUtils.random();
		Date now = new Date();
		if(StringUtil.isEmpty(par.getId())){
			par.setId(uuid);
			par.setPrvcode(uuid);
			par.setPrvtype("02");
			par.setCreatetime(now);
			par.setModifytime(now);
			par.setOperator(operator);
			par.setChildflag("0");
			insbProviderService.addProData(par);
			if(par.getParentcode()!=null){
				insbProviderService.updateProById(par.getParentcode());
			}
		}else{
			par.setModifytime(now);
			par.setOperator(operator);
			insbProviderService.updateById(par);
		}
		mav.addObject("flag", "success");
		return mav;
	}
	
	/**
	 * 根据id删除一个合作商
	 * @param id
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "deletbyid",method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView deleteById(String id)throws ControllerException{
		ModelAndView model = new ModelAndView("zzbconf/partnerslist");
		if(id!=null&&!"".equals(id)){
			String parentcode = insbProviderService.queryById(id).getParentcode();
			int count = insbProviderService.deleteById(id);
			model.addObject("count", count+"");
			if(insbProviderService.queryProTreeList(parentcode).size()<1){
				insbProviderService.updateProByIddel(parentcode);
			}
			return model;
		}else{
			return model;
		}
	}
	
}