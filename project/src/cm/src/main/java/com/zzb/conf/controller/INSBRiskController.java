package com.zzb.conf.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.system.dao.impl.INSBServiceUtil;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCCodeService;
import com.common.PagingParams;
import com.zzb.conf.component.INSBRiskUpdate4Cif;
import com.zzb.conf.controller.vo.BaseVo;
import com.zzb.conf.controller.vo.RiskAllVo;
import com.zzb.conf.dao.INSBRiskimgDao;
import com.zzb.conf.dao.INSBRiskkindconfigDao;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.entity.INSBRisk;
import com.zzb.conf.entity.INSBRiskimg;
import com.zzb.conf.entity.INSBRiskitem;
import com.zzb.conf.entity.INSBRiskkind;
import com.zzb.conf.entity.INSBRiskkindconfig;
import com.zzb.conf.entity.INSBRiskrenewalitem;
import com.zzb.conf.service.INSBProviderService;
import com.zzb.conf.service.INSBRiskService;
import com.zzb.conf.service.INSBRiskimgService;
import com.zzb.conf.service.INSBRiskitemService;
import com.zzb.conf.service.INSBRiskkindService;
import com.zzb.conf.service.INSBRiskkindconfigService;
import com.zzb.conf.service.INSBRiskrenewalitemService;
import com.zzb.model.CifRiskModel;

@Controller
@RequestMapping("/risk/*")
public class INSBRiskController extends BaseController {

	@Resource
	private INSBRiskService insbRiskService;
	@Resource
	private INSBRiskkindService insbRiskkindService;
	@Resource
	private INSCCodeService inscCodeService;
	@Resource
	private INSBRiskitemService insbRiskitemService;
	@Resource
	private INSBRiskrenewalitemService insbRiskrenewalitemService;
	@Resource
	private INSBRiskimgService insbRiskimgService;
	@Resource
	private INSBProviderService insbProviderService;
	@Resource
	private INSBRiskkindconfigService riskkindconfigService;
	@Resource
	private INSBRiskUpdate4Cif riskUpdate4Cif;	
	
	@Resource
	private INSBRiskimgDao insbRiskimgDao;	
	@Resource
	private INSBServiceUtil serviceUtil;
	@Resource
	private INSBRiskkindconfigDao insbRiskkindconfigDao;
    @Resource
    private INSBRiskkindconfigService insbRiskkindconfigService;
	
	private ModelAndView initCodetype(ModelAndView mav) {
		// 设置select 选项
		mav.addObject("risktype",inscCodeService.queryINSCCodeByCode("risktype", "risktype"));// 险种类型
		mav.addObject("riskstatus",inscCodeService.queryINSCCodeByCode("riskstatus", "riskstatus"));// 险种状态
		mav.addObject("kindisamount", inscCodeService.queryINSCCodeByCode("kindisamount", "kindisamount"));// 是否保全选项
		mav.addObject("amountselect", inscCodeService.queryINSCCodeByCode("amountselect", "amountselect"));// 保额选项
		mav.addObject("preriskkind", inscCodeService.queryINSCCodeByCode("preriskkind", "preriskkind"));//前置险别
		mav.addObject("notdeductible", inscCodeService.queryINSCCodeByCode("notdeductible", "notdeductible"));// 不计免赔
		mav.addObject("isusing",inscCodeService.queryINSCCodeByCode("isusing", "isusing"));// 是否启用
		mav.addObject("kindtype",inscCodeService.queryINSCCodeByCode("kindtype", "kindtype"));// 险别类型
		mav.addObject("itemtype",
				inscCodeService.queryINSCCodeByCode("itemtype", "itemtype"));// 要素类型
		List<INSCCode> insccs = inscCodeService.queryINSCCodeByCode("renewaltype", "renewaltype");
//		if(insccs!=null&&insccs.size()>0){
//			insccs.remove(2);
//		}
		mav.addObject("renewaltype",insccs);//字典表是否启用续保配置
		mav.addObject("iteminputtype", inscCodeService.queryINSCCodeByCode(
				"iteminputtype", "iteminputtype"));// 录入方式
		INSCCode inscCodevo = new INSCCode();
		inscCodevo.setParentcode("insuranceimage");
//		mav.addObject("riskimgtype", inscCodeService.queryList(inscCodevo));// 图片类型
		mav.addObject("provider", insbProviderService.queryTreeList(""));		
		mav.addObject("riskkind", insbRiskkindService.queryListByVo(new INSBRiskkind())); 
		mav.addObject("riskkindName", insbRiskService.getPreRiskKindName("")); 
		
		return mav;
	}

	/**
	 * 跳转到列表页面
	 * 
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list() throws ControllerException {
		ModelAndView mav = new ModelAndView("zzbconf/risk");
		return initCodetype(mav);
	}

	@RequestMapping(value = "/querylist", method = RequestMethod.GET)
	@ResponseBody
	public  Map<String, Object> initRiskList(HttpSession session,
			@ModelAttribute PagingParams para,
			@ModelAttribute INSBRisk risk){
		Map<String, Object> initMap = BeanUtils.toMap(risk,para);
		return insbRiskService.queryListByVopage(initMap);
	}
//	public String initRiskList(HttpSession session,


//			@ModelAttribute PagingParams para,
//			@ModelAttribute INSBRisk risk){
////		Map<Object, Object> initMap = new HashMap<Object, Object>();		
////		initMap.put("records", "10000");
////		initMap.put("page", 1);
////		initMap.put("total", insbRiskService.queryListByVoCount(initMap));
////		initMap.put("rows", insbRiskService.queryListByVopage(initMap));
////		JSONObject jsonObject = JSONObject.fromObject(initMap);
////		return jsonObject.toString();
//	}

	@RequestMapping(value = "/addriskshow", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView addriskshow() {
		ModelAndView mav = new ModelAndView("zzbconf/riskdetail");
		return initCodetype(mav);
	}

	@RequestMapping(value = "/updateriskshow", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView updateriskshow(String riskId) {
		ModelAndView mav = new ModelAndView("zzbconf/riskdetail");
		INSCCode inscCodevo = new INSCCode();
		inscCodevo.setParentcode("insuranceimage");
		INSBRisk risk = new INSBRisk();
		risk = this.insbRiskService.queryById(riskId);
		mav.addObject("risk", risk);
		INSBProvider pvd = this.insbProviderService.queryProinfoById(risk.getProvideid());
		mav.addObject("prvname", pvd.getPrvname());
//		mav.addObject("riskId", riskId);
		mav.addObject("riskimgtype", inscCodeService.queryList(inscCodevo));// 图片类型
        mav.addObject("riskKinds", insbRiskkindconfigService.queryAll());
		return initCodetype(mav);
	}

	@RequestMapping(value = "/riskaddorupdate", method = RequestMethod.POST)
	@ResponseBody
	public INSBRisk riskAddorUpdate(HttpSession session,  INSBRisk risk) {
		Date newDate = new Date();
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		INSBRisk insbRisk = new INSBRisk();
		insbRisk.setProvideid(risk.getProvideid());
		insbRisk.setRisktype(risk.getRisktype());
		List<INSBRisk> list = insbRiskService.queryList(insbRisk);
//		if(list.size() > 0){
//			return  null;
//		}
		if(risk.getRenewaltype() == null || risk.getRenewaltype() == ""){
			risk.setRenewaltype("0");
		}
		if (risk.getId() == null || risk.getId().trim().equals("")) {
			risk.setCreatetime(newDate);
			risk.setOperator(operator.getUsercode());
			this.insbRiskService.insert(risk);
			
			System.out.println(risk);
			CifRiskModel model = new CifRiskModel();
			model.setInsbrisk(risk);
			model.setContend(CifRiskModel.CONTEND_INSBRISK);
			riskUpdate4Cif.addRiskData(model);
		} else {
			risk.setModifytime(newDate);
			risk.setOperator(operator.getUsercode());
			this.insbRiskService.updateById(risk);
			
			CifRiskModel model = new CifRiskModel();
			model.setInsbrisk(risk);
			model.setContend(CifRiskModel.CONTEND_INSBRISK);
			riskUpdate4Cif.updateRiskData(model);
		}
		return risk;
	}
	/**
	 * 批量删除险种信息
	 * @param session
	 * @param arrayid
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	@ResponseBody
	public String delete(HttpSession session,
			@RequestParam(value = "arrayid") List<String> arrayid)
			throws ControllerException {
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		CifRiskModel model = new CifRiskModel();
		for(int i = 0;i<arrayid.size();i++){
			model.setIds(arrayid.get(i));
			model.setContend(CifRiskModel.CONTEND_INSBRISK);
			riskUpdate4Cif.deleteRiskData(model);	
		}
		
		insbRiskService.deleteRiskByIds(arrayid);
		LogUtil.info("批量删除险种信息id为%s,操作人:%s", arrayid, operator.getUsercode());
		JSONObject jsonObject = new JSONObject();
		int count = arrayid.size();
		jsonObject.accumulate("count", count);
		return jsonObject.toString();
	}
//  只可删除单条数据
//	public int delete(String riskId) {
//		
//		CifRiskModel model = new CifRiskModel();
//		model.setIds(riskId);
//		model.setContend(CifRiskModel.CONTEND_INSBRISK);
//		riskUpdate4Cif.deleteRiskData(model);				
//		return this.insbRiskService.deleteById(riskId);
//	}
	

	@RequestMapping(value = "/riskkindlist", method = RequestMethod.GET)
	@ResponseBody
	public  Map<String, Object>  riskkindlist(HttpSession session,
			@ModelAttribute PagingParams para,
			@ModelAttribute INSBRiskkind riskkind) {	
		if(StringUtils.isEmpty(riskkind.getRiskid())){
			riskkind.setRiskid("null");
		}
		Map<String, Object> initMap = BeanUtils.toMap(para,riskkind);
		return insbRiskService.queryKindListByVopage(initMap);
	}

	@RequestMapping(value = "/riskkinddelete", method = RequestMethod.POST)
	@ResponseBody
	public int riskkinddelete(HttpSession session, String kindid) {
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("RiskKind删除id为%s,操作人:%s", kindid, operator.getUsercode());
		CifRiskModel model = new CifRiskModel();
		model.setIds(kindid);
		model.setContend(CifRiskModel.CONTEND_KINDSET);
		riskUpdate4Cif.deleteRiskData(model);


		return this.insbRiskkindService.deleteById(kindid);
	}

	@RequestMapping(value = "/getriskkind", method = RequestMethod.GET)
	@ResponseBody
	public INSBRiskkind getriskkind(String kindid) {
		INSBRiskkind riskkind = new INSBRiskkind();
		riskkind = this.insbRiskkindService.queryById(kindid);
		return riskkind;
	}

	@RequestMapping(value = "/riskkindaddorupdate", method = RequestMethod.POST)
	@ResponseBody
	public INSBRiskkind riskkindaddorupdate(HttpSession session,
			INSBRiskkind riskkind) {
		Date newDate = new Date();
		INSBRiskkindconfig insbRiskkindconfig = new INSBRiskkindconfig();
		insbRiskkindconfig.setRiskkindcode(riskkind.getKindcode());
		INSBRiskkindconfig riskkindconfig = insbRiskkindconfigDao.selectOne(insbRiskkindconfig);
		String kindtype = "";
		if(null != riskkindconfig){
			kindtype = riskkindconfig.getType();
		}
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		if (riskkind.getId() == null || riskkind.getId().trim().equals("")) {
			riskkind.setCreatetime(newDate);
			riskkind.setOperator(operator.getUsercode());	
			riskkind.setIsusing("1");//启用
			riskkind.setKindtype(kindtype);
			this.insbRiskkindService.insert(riskkind);
			
			CifRiskModel model = new CifRiskModel();
			model.setKindset(riskkind);
			model.setContend(CifRiskModel.CONTEND_KINDSET);
			riskUpdate4Cif.addRiskData(model);
			
		} else {
			riskkind.setOperator(operator.getUsercode());
			riskkind.setModifytime(newDate);
			riskkind.setKindtype(kindtype);
			this.insbRiskkindService.updateById(riskkind);
			
			CifRiskModel model = new CifRiskModel();
			model.setKindset(riskkind);
			model.setContend(CifRiskModel.CONTEND_KINDSET);
			riskUpdate4Cif.updateRiskData(model);
		}
		
//		if(null != riskkindconfig){
//			//更新riskkindconfig
//			insbRiskService.updateRiskKindConfig(operator,riskkind);
//		}
		
		return riskkind;
	}

	@RequestMapping(value = "/getriskitem", method = RequestMethod.GET)
	@ResponseBody
	public INSBRiskitem getriskitem(String itemid) {
		INSBRiskitem riskitem = new INSBRiskitem();
		riskitem = this.insbRiskitemService.queryById(itemid);
		return riskitem;
	}

	@RequestMapping(value = "/riskitemdelete", method = RequestMethod.POST)
	@ResponseBody
	public int riskitemdelete(String itemid) {
		
		CifRiskModel model = new CifRiskModel();
		model.setIds(itemid);
		model.setContend(CifRiskModel.CONTEND_ITEMSET);
		riskUpdate4Cif.deleteRiskData(model);
		
		return this.insbRiskitemService.deleteById(itemid);
	}

	@RequestMapping(value = "/riskitemlist", method = RequestMethod.GET)
	@ResponseBody
	public String riskitemlist(HttpSession session,
			@ModelAttribute INSBRiskitem riskitem) {
		Map<Object, Object> initMap = new HashMap<Object, Object>();
		initMap.put("records", "10000");
		initMap.put("page", 1);
		initMap.put("total", insbRiskitemService.queryCountVo(riskitem));
		initMap.put("rows", insbRiskitemService.queryListVo(riskitem));
		JSONObject jsonObject = JSONObject.fromObject(initMap);
		return jsonObject.toString();
	}

	@RequestMapping(value = "/riskitemaddorupdate", method = RequestMethod.POST)
	@ResponseBody
	public INSBRiskitem riskkindaddorupdate(HttpSession session,
			INSBRiskitem riskitem) {
		Date newDate = new Date();
		riskitem.setModifytime(newDate);
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		if (riskitem.getId() == null || riskitem.getId().trim().equals("")) {
			riskitem.setCreatetime(newDate);
			riskitem.setOperator(operator.getUsercode());
			this.insbRiskitemService.insert(riskitem);
		} else {
			riskitem.setModifytime(newDate);
			riskitem.setOperator(operator.getUsercode());
			this.insbRiskitemService.updateById(riskitem);
		}
		return riskitem;
	}

	/**
	 * 查询险种续保配置项
	 * 
	 * @param itemid
	 * @return
	 */
	@RequestMapping(value = "/getriskrenewalitem", method = RequestMethod.GET)
	@ResponseBody
	public INSBRiskrenewalitem getriskrenewalitem(String itemid) {
		INSBRiskrenewalitem riskitem = new INSBRiskrenewalitem();
		riskitem = this.insbRiskrenewalitemService.queryById(itemid);
		return riskitem;
	}

	/**
	 * 删除续保配置项
	 * 
	 * @param reitemid
	 * @return
	 */
	@RequestMapping(value = "/riskrenewalitemdelete", method = RequestMethod.POST)
	@ResponseBody
	public int riskrenewalitemdelete(HttpSession session, String reitemid) {
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("续保配置项删除id为%s,操作人:%s", reitemid, operator.getUsercode());
		return this.insbRiskrenewalitemService.deleteById(reitemid);
	}

	/**
	 * 续保配置项列表查询
	 * 
	 * @param session
	 * @param riskitem
	 * @return
	 */
	@RequestMapping(value = "/riskrenewalitemlist", method = RequestMethod.GET)
	@ResponseBody
	public String riskrenewalitemlist(HttpSession session,
			@ModelAttribute INSBRiskrenewalitem riskitem) {
		Map<Object, Object> initMap = new HashMap<Object, Object>();
		initMap.put("records", "10000");
		initMap.put("page", 1);
		initMap.put("total", insbRiskrenewalitemService.queryCountVo(riskitem));
		initMap.put("rows", insbRiskrenewalitemService.queryListVo(riskitem));
		JSONObject jsonObject = JSONObject.fromObject(initMap);
		return jsonObject.toString();
	}

	/**
	 * 续保配置项添加更新
	 * 
	 * @param session
	 * @param riskitem
	 * @return
	 */
	@RequestMapping(value = "/riskrenewalitemaddorupdate", method = RequestMethod.POST)
	@ResponseBody
	public INSBRiskrenewalitem riskrenewalitemaddorupdate(HttpSession session,
			INSBRiskrenewalitem riskitem) {
		Date newDate = new Date();
		riskitem.setModifytime(newDate);
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
//		riskitem.setOperator(operator.getName());
		if (riskitem.getId() == null || riskitem.getId().trim().equals("")) {
			riskitem.setCreatetime(newDate);
			riskitem.setOperator(operator.getUsercode());
			this.insbRiskrenewalitemService.insert(riskitem);
		} else {
			riskitem.setOperator(operator.getUsercode());
			this.insbRiskrenewalitemService.updateById(riskitem);
		}
		return riskitem;
	}

	/**
	 * 查询img
	 * 
	 * @param imgid
	 * @return
	 */
	@RequestMapping(value = "/getriskimg", method = RequestMethod.GET)
	@ResponseBody
	public INSBRiskimg getriskimg(String imgid) {
		INSBRiskimg riskimg = new INSBRiskimg();
		riskimg = this.insbRiskimgService.queryById(imgid);
		return riskimg;
	}

	/**
	 * 删除img
	 * 
	 * @param imgid
	 * @return
	 */
	@RequestMapping(value = "/riskimgdelete", method = RequestMethod.POST)
	@ResponseBody
	public int riskimgdelete(HttpSession session, String imgid) {
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("img删除id为%s,操作人:%s", imgid, operator.getUsercode());
		return this.insbRiskimgService.deleteById(imgid);
	}

	/**
	 * img列表查询
	 * 
	 * @param session
	 * @param riskimg
	 * @return
	 */
	@RequestMapping(value = "/riskimglist", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> riskimglist(HttpSession session,
			@ModelAttribute PagingParams para,
			@ModelAttribute INSBRiskimg riskimg) {
//		Map<Object, Object> initMap = new HashMap<Object, Object>();
//		initMap.put("records", "10");
//		initMap.put("page", 1);
//		initMap.put("total", insbRiskimgService.queryCountVo(riskimg));
//		initMap.put("rows", insbRiskimgService.queryListVo(riskimg));
//		JSONObject jsonObject = JSONObject.fromObject(initMap);
//		return jsonObject.toString();
		if(StringUtils.isEmpty(riskimg.getRiskid())){
			riskimg.setRiskid("null");
        }
        Map<String, Object> initMap = BeanUtils.toMap(para,riskimg);
        List<INSBRiskimg> imgList =  insbRiskimgService.queryListByVopage(initMap);
        Map<String, Object> imgResult = new HashMap<String, Object>();
        imgResult.put("total", insbRiskimgService.queryCountVo(riskimg));
		imgResult.put("rows", imgList);
		return imgResult;
	}

	/**
	 * img添加更新
	 * 
	 * @param session
	 * @param riskimg
	 * @return
	 */
	@RequestMapping(value = "/riskimgaddorupdate/{to}", method = RequestMethod.POST)
	@ResponseBody
	public INSBRiskimg riskimgaddorupdate(HttpSession session,
			INSBRiskimg riskimg, @PathVariable String to) {
		LogUtil.info(to + com.alibaba.fastjson.JSONObject.toJSON(riskimg));
		riskimg.setModifytime(new Date());
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		riskimg.setOperator(operator.getName());
		if (!"update".equals(to)) {			
//			riskimg.setOperator(operator.getName());
			insbRiskimgService.saveOrUpdate(riskimg, to);
		}else {
			this.insbRiskimgService.updateById(riskimg);
		}
		return riskimg;
		
	}
	
	/**
	 * img默认添加
	 * 
	 * @param session
	 * @param riskimg
	 * @return
	 */
	@RequestMapping(value = "/riskimgaddinit", method = RequestMethod.POST)
	@ResponseBody
	public int riskimgaddinit(HttpSession session,
			INSBRiskimg riskimg) {
		int result = 0;
        try {
        	Date newDate = new Date();
    		riskimg.setCreatetime(newDate);
    		riskimg.setModifytime(newDate);
    		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
    		riskimg.setOperator(operator.getName());
    		riskimg.setIsusing("1");
    		//删除之前的img
    		this.insbRiskimgService.deleteByRiskId(riskimg.getRiskid());
    		//获取默认的影像
    		List<INSCCode> codes = this.insbRiskimgService.getDefaultRiskImg();
    		for(INSCCode code : codes) {
    			riskimg.setId(UUID.randomUUID().toString().replace("-", ""));
    			riskimg.setRiskimgname(code.getCodename());
    			riskimg.setRiskimgtype(code.getCodevalue());
    			this.insbRiskimgService.insert(riskimg);
    		}
        } catch (Exception e) {
            result = 2;
            e.printStackTrace();
        }
        return result;
	}

	/**
	 * 大数据接口同步
	 * 
	 * @param synctype
	 * @param modifydate
	 * @return
	 */
	@SuppressWarnings("finally")
	@RequestMapping(value = "/getriskbydate", method = RequestMethod.POST)
	@ResponseBody
	public String getriskbydate(String synctype, String modifydate){
		BaseVo baseVo = new BaseVo();
		List<RiskAllVo> result = new ArrayList<RiskAllVo>();
		try {

			if (synctype.equals("01")) {// 查询全部
				List<INSBRisk> risklist = insbRiskService
						.queryListByVo(new INSBRisk());
				for (INSBRisk insbRisk : risklist) {
					RiskAllVo riskAllVo = new RiskAllVo();
					riskAllVo.setRisk(insbRisk);
					riskAllVo.setProvider(insbProviderService
							.queryById(insbRisk.getProvideid()));
					INSBRiskkind kindquery = new INSBRiskkind();
					kindquery.setRiskid(insbRisk.getId());
					List<INSBRiskkind> kindset = insbRiskkindService
							.queryList(kindquery);
					riskAllVo.setKindset(kindset);
					INSBRiskitem itemquery = new INSBRiskitem();
					itemquery.setRiskid(insbRisk.getId());
					List<INSBRiskitem> itemset = insbRiskitemService
							.queryList(itemquery);
					riskAllVo.setItemset(itemset);
					result.add(riskAllVo);
				}
			} else {// 查询当天
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				sdf.parse(modifydate);
				List<INSBRisk> risklist = insbRiskService
						.selectByModifyDate(modifydate);
				for (INSBRisk insbRisk : risklist) {
					RiskAllVo riskAllVo = new RiskAllVo();
					riskAllVo.setRisk(insbRisk);
					riskAllVo.setProvider(insbProviderService
							.queryById(insbRisk.getProvideid()));
					INSBRiskkind kindquery = new INSBRiskkind();
					kindquery.setRiskid(insbRisk.getId());
					List<INSBRiskkind> kindset = insbRiskkindService
							.queryList(kindquery);
					riskAllVo.setKindset(kindset);
					INSBRiskitem itemquery = new INSBRiskitem();
					itemquery.setRiskid(insbRisk.getId());
					List<INSBRiskitem> itemset = insbRiskitemService
							.queryList(itemquery);
					riskAllVo.setItemset(itemset);
					result.add(riskAllVo);
				}
			}
			baseVo.setStatus("OK");
			baseVo.setMessage("成功");
			baseVo.setResult(result);
		} catch (Exception e) {
			baseVo.setStatus("ERROR");
			baseVo.setMessage(e.getMessage());
		} finally {
			return baseVo.toString();
		}
	}
	
	/**
	 * 为 cif提供数据接口
	 * 
	 * @param synctype 查询类型
	 * @param modifydate 修改时间
	 * @return
	 */
	@RequestMapping(value="/getrisk4Cif",method=RequestMethod.POST)
	@ResponseBody
	public List<INSBRisk> getrisk4Cif(@RequestParam(required=true) String synctype, @RequestParam(required=false) String modifydate){
		List<INSBRisk> resultList = new ArrayList<INSBRisk>();
		try {
			if (synctype.equals("01")) {// 查询全部
				resultList =  insbRiskService.queryAll();
				System.out.println(resultList);
			}else if(synctype.equals("02")){
				resultList = insbRiskService
						.selectByModifyDate(modifydate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
		
	}
	
	/**
	 * 为 cif提供数据接口
	 * 
	 * @param synctype 查询类型
	 * @param modifydate 修改时间
	 * @return
	 */
	@RequestMapping(value="/getriskitem4Cif",method=RequestMethod.POST)
	@ResponseBody
	public List<INSBRiskitem> getriskitem4Cif(@RequestParam(required=true) String synctype, @RequestParam(required=false) String modifydate){
		List<INSBRiskitem> resultList = new ArrayList<INSBRiskitem>();
		try {
			if (synctype.equals("01")) {// 查询全部
				resultList =  insbRiskitemService.queryAll();
			}else if(synctype.equals("02")){
				resultList = insbRiskitemService
						.selectByModifyDate(modifydate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
		
	}
	
	/**
	 * 为 cif提供数据接口
	 * 
	 * @param synctype 查询类型 
	 * @param modifydate 修改时间
	 * @return
	 */
	@RequestMapping(value="/getriskkind4cif",method=RequestMethod.POST)
	@ResponseBody
	public List<INSBRiskkind> getriskkind4Cif(@RequestParam(required=true) String synctype, @RequestParam(required=false) String modifydate){
		List<INSBRiskkind> resultList = new ArrayList<INSBRiskkind>();
		try {
			if (synctype.equals("01")) {// 查询全部
				resultList =  insbRiskkindService.queryAll();
			}else if(synctype.equals("02")){
				resultList = insbRiskkindService
						.selectByModifyDate(modifydate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	@RequestMapping(value="initriskdata",method=RequestMethod.POST)
	@ResponseBody
	public int initRiskData(String riskId){
		int result=0;
		try {
			result = insbRiskService.initData(riskId);
		} catch (Exception e) {
			result=2;
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping(value = "/riskkindbycode", method = RequestMethod.GET)
	@ResponseBody
	public INSBRiskkindconfig selectKindByCode(String riskkindcode)throws ControllerException{
		INSBRiskkindconfig riskkindconfig = riskkindconfigService.selectKindByKindcode(riskkindcode);
		String prekindcodeList = riskkindconfig.getPrekindcode();
		String[] strArray = null;
		strArray = prekindcodeList.split(",");
		StringBuffer preKindName = new StringBuffer();
		for(int i=0;i<strArray.length;i++){
			String singelkindname = insbRiskService.selectKindNameByCode(strArray[i]);
			preKindName.append(singelkindname+",");
		}	
		riskkindconfig.setPrekindcode(preKindName.toString().substring(0,preKindName.length()-1));
		return riskkindconfig;
	}
	@RequestMapping(value = "/checkkindcode", method = RequestMethod.POST)
	@ResponseBody
	public boolean checkkindCode(String id,String kindcode,String riskid){
		
		int count = insbRiskkindService.selectcountByKindcode(id, kindcode, riskid);
		if(count == 0){
			return  true ;
		}else{
			return false;
		}		
	}
	@RequestMapping(value = "/checkhaskind", method = RequestMethod.POST)
	@ResponseBody
	public int checkIsHasKind(String riskid){
		int result = 0;
		try {
			result = insbRiskkindService.selectCountByRiskid(riskid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@RequestMapping(value = "/addriskimgshow", method = RequestMethod.GET)
	@ResponseBody
	public List<INSBRiskimg> addriskimgshow(String riskid) {
		return insbRiskimgService.selectRiskimgByRiskid(riskid);
	}
	
	@RequestMapping(value = "/notselectedriskimgshow", method = RequestMethod.GET)
	@ResponseBody
	public List<INSCCode> notselectedriskimgshow(String riskid) {
		return insbRiskimgService.selectNotSelectedRiskimgtypeByRiskid(riskid);
	}
}
