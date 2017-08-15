package com.zzb.conf.controller;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.entity.INSCCode;
import com.zzb.conf.entity.*;
import com.zzb.conf.service.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCCodeService;
import com.cninsure.system.service.INSCDeptService;
import com.common.PagingParams;
import com.common.WorkFlowUtil;
import com.zzb.conf.component.SupplementCache;
import com.zzb.conf.controller.vo.PaychannelmanagerVo;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.InsbSaveScopeParam;
import com.zzb.mobile.model.InsbSaveTruleParms;

/**
 * 
 * 协议管理
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/agreement/*")
public class INSBAgreementController extends BaseController {
	@Resource
	private INSBAgreementService insbAgreementService;
	@Resource
	private INSCDeptService deptService;
	@Resource
	private INSBProviderService providerService;
	@Resource
	private INSBAgreementscopeService agreementscopeService;
	@Resource
	private INSCCodeService inscCodeService;
	@Resource
	private INSBOutorderdeptService outOrderDeptService;
	@Resource
	private INSBPaychannelmanagerService paychannelmanagerService;
	@Resource
	private INSBDistributiontypeService distributiontypeService;
	@Resource
	private INSBElfconfService insbEllfconfService;
	@Resource
	private INSBAutoconfigService insbAutoconfigService;
	@Resource
	private INSCDeptService inscDeptservice;
	@Resource
	private INSBRulebseService insbRulebseService;
	@Resource
	private INSBAgreementzoneService insbAgreementzoneService;
    @Resource
    private INSBRenewalitemService insbRenewalitemService;
    @Resource
    private INSBRenewalconfigitemService insbRenewalconfigitemService;
    @Resource
	private ThreadPoolTaskExecutor taskthreadPool4workflow;
    @Resource
	private SupplementCache supplementservice;

	/**
	 * 页面字典初始化
	 * 
	 * @param result
	 * @return
	 */
	public ModelAndView initCodeType(ModelAndView result) {
		result.addObject("underwritingitftype",
                inscCodeService.queryINSCCodeByCode("underwritingitftype", "underwritingitftype"));// 险种类型
		List<INSCCode> comList = inscCodeService.queryINSCCodeByCode("logisticscompany", "logisticscompany");
		result.addObject("comList", comList);//物流公司列表
		//规则初始化

		return result;
	}

	/**
	 * 查询协议
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/getagreement", method = RequestMethod.GET)
	@ResponseBody
	public INSBAgreement getagreement(String id) {
		try {
			INSBAgreement agreement = new INSBAgreement();
			agreement = this.insbAgreementService.queryById(id);
			return agreement;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 页面加载
	 * 
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result = new ModelAndView("zzbconf/agreement");
		return initCodeType(result);
	}

	/**
	 * 删除协议
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/deleteagreement", method = RequestMethod.POST)
	@ResponseBody
	public int deleteagreement(HttpSession session, String id) {
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("协议删除id为%s,操作人:%s", id, operator.getUsercode());
		return this.insbAgreementService.deleteById(id);
	}

	/**
	 * 协议列表查询
	 * 
	 * @param session
	 * @param agreement
	 * @return
	 */
	@RequestMapping(value = "/listagreement", method = RequestMethod.GET)
	@ResponseBody
	public String listagreement(HttpSession session,
			@ModelAttribute INSBAgreement agreement) {
		Map<Object, Object> initMap = new HashMap<Object, Object>();
		initMap.put("records", "10000");
		initMap.put("page", 1);
		initMap.put("total", insbAgreementService.queryCountVo(agreement));
		initMap.put("rows", insbAgreementService.queryListVo(agreement));
		JSONObject jsonObject = JSONObject.fromObject(initMap);
		return jsonObject.toString();
	}

	/**
	 * 协议添加更新
	 * 
	 * @param session
	 * @param agreement
	 * @return
	 */
	@RequestMapping(value = "/saveorupdateagreement", method = RequestMethod.POST)
	@ResponseBody
	public INSBAgreement saveorupdateagreement(HttpSession session,
			INSBAgreement agreement) {
		Date newDate = new Date();
		agreement.setModifytime(newDate);
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		//默认 使协议生效
		agreement.setAgreementstatus("1");
		agreement.setOperator(operator.getName());
		INSBAgreement agreementnewAgreement=new INSBAgreement();
		agreementnewAgreement.setAgreementcode(agreement.getAgreementcode());
		agreementnewAgreement=insbAgreementService.queryOne(agreementnewAgreement);
			if (agreement.getId() == null || agreement.getId().trim().equals("")) {
				if (agreementnewAgreement==null) {
					agreement.setCreatetime(newDate);
					agreement.setOperator(operator.getName());
					this.insbAgreementService.insert(agreement);
					
					if(StringUtil.isNotEmpty(agreement.getAgreementtrule())){
						taskthreadPool4workflow.execute(new Runnable() {
							@Override
							public void run() {
								try {
									supplementservice.flush(false);//刷新协议相关规则id的供应商补充项
								} catch (Exception e) {
									LogUtil.debug("刷新协议相关规则id对应的补充项异常", e);
								}
							}
						});
					}
						
				}else{
					agreement.setId("");
					return agreement;
				}
			} else {
				if (agreementnewAgreement==null) {
					INSBAgreement a = insbAgreementService.queryById(agreement.getId());
					agreement.setAgreementstatus(a.getAgreementstatus());
					insbAgreementService.updateById(agreement);
					if(!a.getAgreementtrule().equals(agreement.getAgreementtrule())){//如果修改前后的规则id不相同，则需更新
						taskthreadPool4workflow.execute(new Runnable() {
							@Override
							public void run() {
								try {
									supplementservice.flush(false);//刷新协议相关规则id的供应商补充项
								} catch (Exception e) {
									LogUtil.debug("刷新协议相关规则id对应的补充项异常", e);
								}
							}
						});
					}
				}else if (agreement.getId().equals(agreementnewAgreement.getId())) {
					INSBAgreement a = insbAgreementService.queryById(agreement.getId());
					agreement.setAgreementstatus(a.getAgreementstatus());
					insbAgreementService.updateById(agreement);
					if(!a.getAgreementtrule().equals(agreement.getAgreementtrule())){//如果修改前后的规则id不相同，则需更新
						taskthreadPool4workflow.execute(new Runnable() {
							@Override
							public void run() {
								try {
									supplementservice.flush(false);//刷新协议相关规则id的供应商补充项
								} catch (Exception e) {
									LogUtil.debug("刷新协议相关规则id对应的补充项异常", e);
								}
							}
						});
					}
				} else {
					agreement.setId("");
					return agreement;
				}
			}
			return agreement;
	}
	
	@RequestMapping(value = "/updateagreementsupplements", method = RequestMethod.POST)
	@ResponseBody
	public void updateagreementsupplements(HttpSession session) {
		supplementservice.flush(false);//刷新协议相关规则id的供应商补充项
	}
	/**
	 * 协议启动停止
	 * 
	 * @param session
	 * @param agreementVo
	 * @return
	 */
	@RequestMapping(value = "/startorendagreement", method = RequestMethod.POST)
	@ResponseBody
	public INSBAgreement startorendagreement(HttpSession session,
			INSBAgreement agreementVo) {
		INSBAgreement agreement = new INSBAgreement();
		if (agreementVo.getId() == null
				|| agreementVo.getId().trim().equals("")) {
			return null;
		} else {
			agreement = this.insbAgreementService
					.queryById(agreementVo.getId());
			agreement.setAgreementstatus(agreementVo.getAgreementstatus());
			Date newDate = new Date();
			agreement.setModifytime(newDate);
			INSCUser operator = (INSCUser) session.getAttribute("insc_user");
			agreement.setOperator(operator.getName());
			this.insbAgreementService.updateById(agreement);
		}
		return agreement;
	}

	/**
	 * 设置协议核保状态
	 *
	 * @param session
	 * @param ids
	 * @param underwritestatus
	 * @return
	 */
	@RequestMapping(value = "/setUnderwritestatus", method = RequestMethod.POST)
	@ResponseBody
	public INSBAgreement setUnderwritestatus(HttpSession session, String ids, Integer underwritestatus) {
		INSBAgreement agreement = new INSBAgreement();
		if (StringUtil.isEmpty(ids) || StringUtil.isEmpty(underwritestatus)) {
			return null;
		}
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		String[] idArrays = ids.split(",");
		for (String id : idArrays) {
			agreement = this.insbAgreementService.queryById(id);
			agreement.setUnderwritestatus(underwritestatus);
			agreement.setModifytime(new Date());
			agreement.setOperator(operator.getName());
			LogUtil.info("更新协议核保状态:"+ JSONObject.fromObject(agreement).toString());
			insbAgreementService.updateById(agreement);
		}
		return agreement;
	}

	/**
	 * 详情页面加载页面加载
	 * 
	 * @return
	 */
	@RequestMapping(value = "addagreementdetail", method = RequestMethod.GET)
	public ModelAndView addagreementdetail() {
		ModelAndView result = new ModelAndView("zzbconf/agreementdetail");
//		Map<String,Object> tempMap = insbAgreementService.initEditePageData(null);
//		result.addObject("ruleList2", tempMap.get("ruleList2"));
//		result.addObject("ruleList4", tempMap.get("ruleList4"));
		return initCodeType(result);
	}

	/**
	 * 详情页面加载页面加载
	 * 
	 * @return
	 */
	@RequestMapping(value = "updateagreementdetail", method = RequestMethod.GET)
	@ResponseBody
	public String updateagreementdetail(String agreementid) {

//		ModelAndView result = new ModelAndView("zzbconf/agreementdetail");
		Map< String, Object> result = new HashMap<String, Object>();
		INSBAgreement agreement = new INSBAgreement();
		agreement = this.insbAgreementService.queryById(agreementid);
		try {
			String providername = providerService.queryById(agreement.getProviderid()).getPrvname();
			INSCDept dept = deptService.queryById(agreement.getDeptid());
			String deptname = "";
			if(dept!=null){
				deptname = dept.getComname();
			}
			result.put("agreement", agreement);
			result.put("providername", providername);
			result.put("deptname", deptname);

			INSBAgreementscope queryAdr = new INSBAgreementscope();
			queryAdr.setAgreementid(agreementid);
			queryAdr.setScopetype("1");
			result.put("adrScope", this.listscope(queryAdr));

			INSBAgreementscope querydept = new INSBAgreementscope();
			querydept.setAgreementid(agreementid);
			querydept.setScopetype("2");
			result.put("deptScope", this.listscope(querydept));
			
			result.put("agreementid",agreementid);

			String agreementrule = agreement.getAgreementrule();
			String agreementrulename=insbRulebseService.selectByagreementrule(agreement.getAgreementtrule());
			result.put("agreementrule",agreementrule);
			result.put("agreementrulename",agreementrulename);

			//初始化规则下拉框
			Map<String,Object> tempMap = insbAgreementService.initEditePageData(agreementid);
			result.put("ruleList2", tempMap.get("ruleList2"));
			result.put("ruleList4", tempMap.get("ruleList4"));
			
		} catch (Exception e) {
			e.printStackTrace();
			result.put("agreement", null);
			result.put("providername", "");
			result.put("deptname", "");
			result.put("adrScope", null);
			result.put("deptScope", null);
		}

		result.put("underwritingitftype", inscCodeService.queryINSCCodeByCode("underwritingitftype", "underwritingitftype"));

		return JSONObject.fromObject(result).toString();
	}

	/**
	 * 覆盖范围查询
	 * 
	 * @return
	 */
	@RequestMapping(value = "/listscope", method = RequestMethod.GET)
	@ResponseBody
	public List<INSBAgreementscope> listscope(INSBAgreementscope query) {
		List<INSBAgreementscope> scopes = new ArrayList<INSBAgreementscope>();
		scopes = this.agreementscopeService.selectAdrAndDeptName(query);
		return scopes;
	}

	/**
	 * 覆盖范围添加
	 * 
	 * @return
	 */
	@RequestMapping(value = "/savescope", method = RequestMethod.POST)
	@ResponseBody
	public int savescope(HttpSession session, INSBAgreementscope scope,INSBOutorderdept conndept) {
		
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		
		try {
			agreementscopeService.saveAgreementScop(operator,scope,conndept);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 2;
	}
	/**
	 * 关联网点列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/listscopedept", method = RequestMethod.GET)
	@ResponseBody
	public List<INSBAgreementscope> listscopedept(String agreementid) {
		
		INSBAgreementscope query = new INSBAgreementscope();
		query.setAgreementid(agreementid);
		List<INSBAgreementscope> scopedepts = new ArrayList<INSBAgreementscope>();
		scopedepts = this.agreementscopeService.selectAdrAndDeptName(query);
		return scopedepts;
	}
	/**
	 * 覆盖范围删除
	 * 
	 * @return
	 */
	@RequestMapping(value = "/deletescope", method = RequestMethod.POST)
	@ResponseBody
	public int deletescope(HttpSession session, String scopeid) {
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("覆盖范围删除id为%s,操作人:%s", scopeid, operator.getUsercode());
		return this.agreementscopeService.deleteById(scopeid);
	}
	/**
	 * 删除关联网点并刷新页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/deletescopeids", method = RequestMethod.POST)
	@ResponseBody
	public int deletescopeids(HttpSession session, String ids) {
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("删除关联网点ids为%s,操作人:%s", ids, operator.getUsercode());
		for (String id : ids.split(",")) {
			this.agreementscopeService.deleteById(id);
		}
		return 1;
	}
	/**
	 * 出单网点列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/listoutdept", method = RequestMethod.GET)
	@ResponseBody
	public List<INSBOutorderdept> listoutdept(String agreementid) {
		INSBOutorderdept query = new INSBOutorderdept();
		query.setAgreementid(agreementid);
		List<INSBOutorderdept> outdepts = new ArrayList<INSBOutorderdept>();
		outdepts = this.outOrderDeptService.queryListVo(query);
		return outdepts;
	}

	/**
	 * 删除对象并刷新页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/deleteoutdept", method = RequestMethod.POST)
	@ResponseBody
	public int deleteoutdept(HttpSession session, String id) {
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("deleteoutdept删除id为%s,操作人:%s", id, operator.getUsercode());
		return this.outOrderDeptService.deleteById(id);
	}

	/**
	 * 删除对象并刷新页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/deleteoutdeptids", method = RequestMethod.POST)
	@ResponseBody
	public int deleteoutdeptids(HttpSession session, String ids) {
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("deleteoutdeptids删除id为%s,操作人:%s", ids, operator.getUsercode());
		for (String id : ids.split(",")) {
			this.outOrderDeptService.deleteById(id);
		}
		return 1;
	}

	/**
	 * 添加关联(出单网点)
	 * 
	 * @return
	 */
	@RequestMapping(value = "/saveorupdateoutdept", method = RequestMethod.POST)
	@ResponseBody
	public int saveorupdateoutdept(HttpSession session,
			INSBOutorderdept outdept) {
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		try {
			outOrderDeptService.saveDeptIds(outdept,operator);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 2;
	}

	/**
	 * 更新 优先级及权限
	 * 
	 * @return
	 */
	@RequestMapping(value = "/updateoutdept", method = RequestMethod.POST)
	@ResponseBody
	public INSBOutorderdept updateoutdept(HttpSession session, String id,
			String scaleflag, String permflag) {
		INSBOutorderdept outdept = outOrderDeptService.queryById(id);
		Date newDate = new Date();
		outdept.setModifytime(newDate);
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		outdept.setOperator(operator.getName());
		outdept.setPermflag(permflag);
		outdept.setScaleflag(scaleflag);
		this.outOrderDeptService.updateById(outdept);
		return outdept;
	}

	/**
	 * 支付通道配置初始化
	 * 
	 * @param agreementid
	 * @return
	 */
	@RequestMapping(value = "/initpcm", method = RequestMethod.POST)
	@ResponseBody
	public PaychannelmanagerVo initpcm(String agreementid) {
		PaychannelmanagerVo vo = new PaychannelmanagerVo();
		INSBPaychannelmanager query = new INSBPaychannelmanager();
		query.setAgreementid(agreementid);
		List<INSBPaychannelmanager> list = null;
		query.setPaychannelid("1");
		list = paychannelmanagerService.queryList(query);
		if (!list.isEmpty()) {
			vo.setPcm1(list.get(0));
		}
		query.setPaychannelid("2");
		list = paychannelmanagerService.queryList(query);
		if (!list.isEmpty()) {
			vo.setPcm2(list.get(0));
		}

		query.setPaychannelid("3");
		list = paychannelmanagerService.queryList(query);
		if (!list.isEmpty()) {
			vo.setPcm3(list.get(0));
		}

		query.setPaychannelid("4");
		list = paychannelmanagerService.queryList(query);
		if (!list.isEmpty()) {
			vo.setPcm4(list.get(0));
		}

		query.setPaychannelid("5");
		list = paychannelmanagerService.queryList(query);
		if (!list.isEmpty()) {
			vo.setPcm5(list.get(0));
		}

		query.setPaychannelid("6");
		list = paychannelmanagerService.queryList(query);
		if (!list.isEmpty()) {
			vo.setPcm6(list.get(0));
		}
		return vo;
	}

	/**
	 * 支付通道配置修改保存
	 * 
	 * @param session
	 * @param voStr
	 * @return
	 */
	@RequestMapping(value = "/savepcm", method = RequestMethod.POST)
	@ResponseBody
	public int savepcm(HttpSession session, String voStr) {
		try {
			JSONArray objs = JSONArray.fromObject(voStr);
			INSCUser operator = (INSCUser) session.getAttribute("insc_user");
			List<INSBPaychannelmanager> addlist = new ArrayList<INSBPaychannelmanager>();
			List<INSBPaychannelmanager> updatelist = new ArrayList<INSBPaychannelmanager>();
			String  agreementid= "";
			List<String> ids = new ArrayList<String>();
			Date newDate = new Date();
			for (Object jsob : objs) {
				INSBPaychannelmanager pcm = (INSBPaychannelmanager) JSONObject
						.toBean(JSONObject.fromObject(jsob),
								INSBPaychannelmanager.class);
				agreementid =pcm.getAgreementid();
				if (isadd(pcm)) {
					addlist.add(pcm);
				} else {
					ids.add(pcm.getId());
					updatelist.add(pcm);
				}
			}
			for (INSBPaychannelmanager pcm : addlist) {
				pcm.setModifytime(newDate);
				pcm.setCreatetime(newDate);
				pcm.setOperator(operator.getName());
			}
			for (INSBPaychannelmanager pcm : updatelist) {
				pcm.setModifytime(newDate);
				pcm.setOperator(operator.getName());
				paychannelmanagerService.updateById(pcm);
			}
			paychannelmanagerService.deleteByExceptIds(ids,agreementid);
			paychannelmanagerService.insertInBatch(addlist);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	private boolean isadd(BaseEntity e) {
		if (e.getId() == null || e.getId().trim().equals("")) {
			return true;
		}
		return false;
	}

	/**
	 * 配送方式初始化
	 * 
	 * @param agreementid
	 * @return
	 */
	@RequestMapping(value = "/initdbt", method = RequestMethod.POST)
	@ResponseBody
	public String initdbt(String agreementid) {
		INSBDistributiontype query = new INSBDistributiontype();
		query.setAgreementid(agreementid);
		List<INSBDistributiontype> list = distributiontypeService.queryList(query);
		return JSONArray.fromObject(list).toString();
	}

	/**
	 * 配送方式保存修改
	 * 
	 * @param session
	 * @param voStr
	 * @return
	 */
	@RequestMapping(value = "/savedbt", method = RequestMethod.POST)
	@ResponseBody
	public int savedbt(HttpSession session, String voStr) {
		try {
			JSONArray objs = JSONArray.fromObject(voStr);
			INSCUser operator = (INSCUser) session.getAttribute("insc_user");
			List<INSBDistributiontype> addlist = new ArrayList<INSBDistributiontype>();
			List<INSBDistributiontype> updatelist = new ArrayList<INSBDistributiontype>();
			String agreementid= "";
			List<String> ids = new ArrayList<String>();
			Date newDate = new Date();
            boolean addOnly = true;

			for (Object jsob : objs) {
				INSBDistributiontype dbt = (INSBDistributiontype) JSONObject.toBean(JSONObject.fromObject(jsob),INSBDistributiontype.class);
				agreementid =dbt.getAgreementid();
				if (isadd(dbt)) {
					addlist.add(dbt);
				} else {
					ids.add(dbt.getId());
					updatelist.add(dbt);
                    addOnly = false;
				}
			}
			for (INSBDistributiontype dbt : addlist) {
				dbt.setModifytime(newDate);
				dbt.setCreatetime(newDate);
				dbt.setOperator(operator.getName());
			}
			for (INSBDistributiontype dbt : updatelist) {
				dbt.setModifytime(newDate);
				dbt.setOperator(operator.getName());
				distributiontypeService.updateById(dbt);
			}
			if(ids.size()>0){
				distributiontypeService.deleteByExceptIds(ids,agreementid);
			}
            if (addOnly) {
                distributiontypeService.deleteByAgreementId(agreementid);
            }

			distributiontypeService.insertInBatch(addlist);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	/**
	 * 报价配置
	 * 
	 * 得到有精灵
	 * 
	 * @return
	 */
	@RequestMapping(value="initelf",method=RequestMethod.POST)
	@ResponseBody
	public List<INSBElfconf> initProvidersByElf(String providerid){
		return insbAgreementService.queryElfByProviderId(providerid);
	}
	
	/**
	 * 报价配置
	 * 
	 * 得到有EDI
	 * 
	 * @return
	 */
	@RequestMapping(value="initedis",method=RequestMethod.POST)
	@ResponseBody
	public List<Map<String,String>> initEDIs(String providerid){
		return insbAgreementService.queryEDIByProviderId(providerid);
	}
	
	/**
	 * 协议管理 流程自动化配置
	 * 
	 * @param session
	 * @param qc
	 * @return
	 */
//	@RequestMapping(value="saveorupdateautoconfig")
//	@ResponseBody
//	public BaseVo saveOrUpdateAutoConfig(HttpSession session,@ModelAttribute INSBAutoConfigModel model){
//		BaseVo bv = new BaseVo();
//		String quoteId=null;
//		Date date = new Date();
//		String operator = ((INSCUser) session.getAttribute("insc_user")).getUsercode();
//		List<INSBAutoconfig> conflisthb = model.getInsbAutoConfiglisthb();//核保
//		List<INSBAutoconfig> conflistcb = model.getInsbAutoConfiglistcb();//承保
//		List<INSBAutoconfig> conflistxb = model.getInsbAutoConfiglistxb();//续保
//		List<INSBAutoconfig> conflistbjfs = model.getInsbAutoConfiglistbjfs();//报价方式
//		try {
//			if(conflisthb!=null&&conflisthb.size()>0){ 	//核保
//				if(!StringUtil.isEmpty(model.getAgreementid())){
//					insbAutoconfigService.deleteByAgreementId(model.getAgreementid(),"02");
//					for (INSBAutoconfig list:conflisthb ) {
//						INSBAutoconfig auto = new INSBAutoconfig();
//						auto.setOperator(operator);
//						auto.setModifytime(date);
//						auto.setAgreementid(model.getAgreementid());//代理人id
//						auto.setCodevalue(list.getCodevalue());//code表codevalue
//						auto.setConftype(list.getConftype());//01：报价配置 :02：核保配置:03：续保配置04：承保配置
//						auto.setQuotetype(list.getQuotetype());//01:EDI  02:精灵 03：规则报价04：人工录单05：人工规则录单06：人工调整
//						auto.setContentid(list.getContentid());//edi或者精灵id
//						if(!"".equals(list.getCodevalue())&&list.getCodevalue()!=null){
//							quoteId = insbAgreementService.saveAutoConfig(auto);
//						}
//					}
//				}else{
//					for (INSBAutoconfig list:conflisthb ) {
//						INSBAutoconfig auto = new INSBAutoconfig();
//						auto.setOperator(operator);
//						auto.setModifytime(date);
//						auto.setAgreementid(model.getAgreementid());//代理人id
//						auto.setCodevalue(list.getCodevalue());//code表codevalue
//						auto.setConftype(list.getConftype());//01：报价配置 :02：核保配置:03：续保配置04：承保配置
//						auto.setQuotetype(list.getQuotetype());//01:EDI  02:精灵 03：规则报价04：人工录单05：人工规则录单06：人工调整
//						auto.setContentid(list.getContentid());//edi或者精灵id
//						if(!"".equals(list.getCodevalue())&&list.getCodevalue()!=null){
//							quoteId = insbAgreementService.saveAutoConfig(auto);
//						}
//					}
//				}
//			} 
//			if(conflistcb!=null&&conflistcb.size()>0){ 	//承保
//				if(!StringUtil.isEmpty(model.getAgreementid())){
//					insbAutoconfigService.deleteByAgreementId(model.getAgreementid(),"04");
//					for (INSBAutoconfig list:conflistcb ) {
//						INSBAutoconfig auto = new INSBAutoconfig();
//						auto.setOperator(operator);
//						auto.setModifytime(date);
//						auto.setAgreementid(model.getAgreementid());//代理人id
//						auto.setCodevalue(list.getCodevalue());//code表codevalue
//						auto.setConftype(list.getConftype());//01：报价配置 :02：核保配置:03：续保配置 04：承保配置
//						auto.setQuotetype(list.getQuotetype());//01:EDI  02:精灵 03：规则报价04：人工录单05：人工规则录单06：人工调整
//						auto.setContentid(list.getContentid());//edi或者精灵id
//						if(!"".equals(list.getCodevalue())&&list.getCodevalue()!=null){
//							quoteId = insbAgreementService.saveAutoConfig(auto);
//						}
//					}
//				}else{
//					for (INSBAutoconfig list:conflistcb ) {
//						INSBAutoconfig auto = new INSBAutoconfig();
//						auto.setId(UUIDUtils.random());//添加
//						auto.setAgreementid(model.getAgreementid());
//						auto.setCodevalue(list.getCodevalue());
//						auto.setConftype(list.getConftype());
//						auto.setQuotetype(list.getQuotetype());
//						auto.setContentid(list.getContentid());
//						auto.setOperator(operator);
//						auto.setCreatetime(date);
//						auto.setModifytime(date);
//						if(!"".equals(list.getCodevalue())&&list.getCodevalue()!=null&&list.getContentid()!=""){
//							quoteId = insbAgreementService.saveAutoConfig(auto);
//						}
//					}
//				}
//			} 
//			if(conflistxb!=null&&conflistxb.size()>0){	//续保
//				if(!StringUtil.isEmpty(model.getAgreementid())){
//					insbAutoconfigService.deleteByAgreementId(model.getAgreementid(),"03");
//					for (INSBAutoconfig list:conflistxb ) {
//						INSBAutoconfig auto = new INSBAutoconfig();
//						auto.setOperator(operator);
//						auto.setModifytime(date);
//						auto.setAgreementid(model.getAgreementid());//代理人id
//						auto.setCodevalue(list.getCodevalue());//code表codevalue
//						auto.setConftype(list.getConftype());//01：报价配置 :02：核保配置:03：续保配置 04：承保配置
//						auto.setQuotetype(list.getQuotetype());//01:EDI  02:精灵 03：规则报价04：人工录单05：人工规则录单06：人工调整
//						auto.setContentid(list.getContentid());//edi或者精灵id
//						if(!"".equals(list.getCodevalue())&&list.getCodevalue()!=null){
//							quoteId = insbAgreementService.saveAutoConfig(auto);
//						}
//					}
//				}else{
//					for (INSBAutoconfig list:conflistxb ) {
//						INSBAutoconfig auto = new INSBAutoconfig();
//						auto.setId(UUIDUtils.random());//添加
//						auto.setAgreementid(model.getAgreementid());
//						auto.setCodevalue(list.getCodevalue());
//						auto.setConftype(list.getConftype());
//						auto.setQuotetype(list.getQuotetype());
//						auto.setContentid(list.getContentid());
//						auto.setOperator(operator);
//						auto.setCreatetime(date);
//						auto.setModifytime(date);
//						if(!"".equals(list.getCodevalue())&&list.getCodevalue()!=null&&list.getContentid()!=""){
//							quoteId = insbAgreementService.saveAutoConfig(auto);
//						}
//					}
//				}
//			}
//			if(conflistbjfs!=null&&conflistbjfs.size()>0){//报价方式(报价配置)
//				if(!StringUtil.isEmpty(model.getAgreementid())){
//					insbAutoconfigService.deleteByAgreementId(model.getAgreementid(),"01");
//					for (INSBAutoconfig list:conflistbjfs ) {
//						INSBAutoconfig auto = new INSBAutoconfig();
//						auto.setOperator(operator);
//						auto.setModifytime(date);
//						auto.setAgreementid(model.getAgreementid());//代理人id
//						auto.setCodevalue(list.getCodevalue());//code表codevalue
//						auto.setConftype(list.getConftype());//01：报价配置 :02：核保配置:03：续保配置 04：承保配置
//						auto.setQuotetype(list.getQuotetype());//01:EDI  02:精灵 03：规则报价04：人工录单05：人工规则录单06：人工调整
//						auto.setContentid(list.getContentid());//edi或者精灵id
//						if(!"".equals(list.getCodevalue())&&list.getCodevalue()!=null){
//							quoteId = insbAgreementService.saveAutoConfig(auto);
//						}
//					}
//				}else{
//					for (INSBAutoconfig list:conflistbjfs ) {
//						INSBAutoconfig auto = new INSBAutoconfig();
//						auto.setId(UUIDUtils.random());//添加
//						auto.setAgreementid(model.getAgreementid());
//						auto.setCodevalue(list.getCodevalue());
//						auto.setConftype(list.getConftype());
//						auto.setQuotetype(list.getQuotetype());
//						auto.setContentid(list.getContentid());
//						auto.setOperator(operator);
//						auto.setCreatetime(date);
//						auto.setModifytime(date);
//						if(!"".equals(list.getCodevalue())&&list.getCodevalue()!=null&&list.getContentid()!=""){
//							quoteId = insbAgreementService.saveAutoConfig(auto);
//						}
//					}
//				}
//			}
//			bv.setStatus("1");
//			bv.setMessage("操作成功");
//			bv.setResult(quoteId);
//		} catch (Exception e) {
//			bv.setStatus("2");
//			bv.setMessage("操作失败");
//			e.printStackTrace();
//		}
//		return bv;
//	}
	
	/**
	 * 选择供应商 初始化规则下拉框
	 * 
	 * 规则和供应商下拉框联动
	 * 
	 * @param deptId
	 * @return
	 */
	@RequestMapping(value="initrulebyprovider",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> initrulebyprovider(String deptId){
		
		Map<String, Object> result = null;
		try {
			result = insbAgreementService.initRuleByProviderId(deptId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 根据公司code获取协议信息
	 * 
	 * @param comcode
	 * @return
	 */
	@RequestMapping(value="getagreementbycomcode",method=RequestMethod.GET)
	@ResponseBody
	public String getAgreementByComcode(@ModelAttribute PagingParams para,String comcode,String agreementcode
			,String queryagreementcode,String queryagreementname,String citys, String providerid, int agreementtype){
		Map<Object, Object> initMap = new HashMap<Object, Object>();
		Map<String, Object> map = BeanUtils.toMap(para);
		map.put("comcode", comcode);
		map.put("agreementcode", agreementcode);

		map.put("queryagreementcode", queryagreementcode);
		map.put("queryagreementname", queryagreementname);
		map.put("citys", citys);
		map.put("providerid", providerid);
		map.put("agreementtype", agreementtype);//协议类型
		initMap.put("records", "10000");
		initMap.put("page", 1);  
		initMap.put("rows", insbAgreementService.queryListAgreement(map));
		initMap.put("total", insbAgreementService.queryCountAgreement(map));
		JSONObject jsonObject = JSONObject.fromObject(initMap);
		return jsonObject.toString();
	}
	
	/**
	 * 查询机构
	 * @param parentcode
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="querydepttree",method=RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> queryDeptList(HttpSession session,@RequestParam(value="id",required=false) String parentcode) throws ControllerException{
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		return deptService.dept4Tree(operator.getDeptinnercode(), "01", null);
	}
	
	@RequestMapping(value = "inscdeptlist",method= RequestMethod.POST)
	@ResponseBody
	public List<Map<String,String>>  institutionTreeList(HttpSession session, @RequestParam(value="root", required=false)   String parentcode)throws ControllerException{
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		if (parentcode != null) {
			return deptService.queryTreeList4Data(parentcode,operator.getUserorganization());
		}
		return deptService.dept4Tree(operator.getDeptinnercode(), null, null);
	}

	@RequestMapping(value = "inscdeptlist2",method= RequestMethod.POST)
	@ResponseBody
	public List<Map<String,String>>  institutionTreeList2(HttpSession session, @RequestParam(value="root", required=false)   String parentcode)throws ControllerException{
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");

		return deptService.queryTreeList4Data(parentcode,operator.getUserorganization());

	}
	
	/**
	 * 一键关联网点
	 * 
	 * @return
	 */
	@RequestMapping(value = "/onekeysavescope", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel oneKeySaveScope(HttpSession session, @RequestBody InsbSaveScopeParam model) {
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		//先保存区域
		insbAgreementzoneService.savezones(operator,model);
		return agreementscopeService.saveAgreementScopByCity(operator,model);
	}
	/**
	 * 一键关联网点
	 * 
	 * @return
	 */
	@RequestMapping(value = "/gettrulebydeptid", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel getTruleByDeptid(@RequestBody InsbSaveTruleParms model) {
			return insbAgreementService.getTrules(model);
	}
	/**
	 * 根据协议查询区域
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getzonebyagreementid", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel getZoneByAgreementid(@RequestParam String agreementid) {
		return insbAgreementzoneService.getZone(agreementid);
	}

    /**
     * 续保数据项初始化
     *
     * @return
     */
    @RequestMapping(value = "/initrenewalitems", method = RequestMethod.GET)
    @ResponseBody
    public String initRenewalItems(String agreementid) {
        List<INSBRenewalitem> renewalitemList = insbRenewalitemService.queryAll();

        INSBRenewalconfigitem renewalconfigitem = new INSBRenewalconfigitem();
        renewalconfigitem.setAgreementid(agreementid);
        List<INSBRenewalconfigitem> renewalconfigitemList = insbRenewalconfigitemService.queryList(renewalconfigitem);

        int renewalEnable = 0;
        INSBAgreement agreement = insbAgreementService.queryById(agreementid);
        if (agreement != null) {
            if (agreement.getRenewalenable() != null) {
                renewalEnable = agreement.getRenewalenable();
            }
        }

        Map<String, Object> items = new HashMap<>(2);
        items.put("allItems", renewalitemList);
        items.put("configItems", renewalconfigitemList);
        items.put("renewalEnable", renewalEnable);

        return JSONObject.fromObject(items).toString();
    }

    /**
     * 获取协议续保数据项
     *
     * @param agreementid
     * @return
     */
    @RequestMapping(value = "/getrenewalitems", method = RequestMethod.GET)
    @ResponseBody
    public String getRenewalItems(String agreementid) {
        INSBRenewalconfigitem renewalconfigitem = new INSBRenewalconfigitem();
        renewalconfigitem.setAgreementid(agreementid);
        List<INSBRenewalconfigitem> list = insbRenewalconfigitemService.queryList(renewalconfigitem);
        return JSONArray.fromObject(list).toString();
    }

    /**
     * 保存协议续保数据项
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/saverenewalitems", method = RequestMethod.POST)
    @ResponseBody
    public String saveRenewalItems(HttpSession session,
                                   @RequestParam(value="renewalEnable", required=false) String renewalEnable,
                                   @RequestParam(value="renewalagreementid", required=true) String agreementid,
                                   @RequestParam(value="to", required=true) List<String> configRenewalItems) {
        INSCUser operator = (INSCUser) session.getAttribute("insc_user");

       try {
           if (StringUtil.isEmpty(agreementid)) {
               return "{\"status\":\"noid\"}";
           }
           if ("1".equals(renewalEnable)&&(configRenewalItems == null || configRenewalItems.isEmpty())) {
               return "{\"status\":\"noitem\"}";
           }

           insbRenewalconfigitemService.deleteByAgreementid(agreementid);

           Set<String> configItems = new HashSet<>();
           for (String item : configRenewalItems) {
               if (StringUtil.isNotEmpty(item)) {
                   configItems.add(item);
               }
           }

           configRenewalItems = new ArrayList<>(configItems.size());
           configRenewalItems.addAll(configItems);
           int result = insbRenewalconfigitemService.save(agreementid, configRenewalItems, operator);

           INSBAgreement agreement = insbAgreementService.queryById(agreementid);
           if (agreement != null) {
               if (StringUtil.isEmpty(renewalEnable)) {
                   renewalEnable = "0";
               }
               agreement.setRenewalenable(Integer.valueOf(renewalEnable));
			   agreement.setModifytime(new Date());
			   agreement.setOperator(operator.getUsercode());
               insbAgreementService.updateById(agreement);
           }
       }catch(Exception e){
           e.printStackTrace();
           LogUtil.error("saveRenewalItems occured error:" + e.toString());
           return "{\"status\":\"failed\"}";
       }
        return "{\"status\":\"ok\"}";
    }
}