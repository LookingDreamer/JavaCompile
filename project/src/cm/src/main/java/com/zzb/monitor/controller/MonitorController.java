package com.zzb.monitor.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.service.INSCCodeService;
import com.common.HttpClientUtil;
import com.common.PagingParams;
import com.common.redis.CMRedisClient;
import com.zzb.cm.dao.INSBMonitorDao;
import com.zzb.cm.entity.INSBFlowerror;
import com.zzb.cm.entity.INSBMonitor;
import com.zzb.cm.service.INSBFlowerrorService;
import com.zzb.cm.service.INSBMonitorService;
import com.zzb.conf.entity.INSBEdidescription;
import com.zzb.conf.entity.INSBElfconf;
import com.zzb.conf.entity.INSBSkill;
import com.zzb.conf.service.INSBEdiconfigurationService;
import com.zzb.conf.service.INSBElfconfService;
import com.zzb.conf.service.INSBProviderService;
import com.zzb.conf.service.INSBSkillService;
import com.zzb.monitor.model.Monitormodel;

@Controller
@RequestMapping("/monitor/*")
public class MonitorController extends BaseController {

	@Resource
	INSBMonitorService insbMonitorService;
	@Resource
	INSCCodeService inscCodeService;
	@Resource
	private INSBEdiconfigurationService ediService;
	@Resource
	INSBMonitorDao insbMonitorDao;
	@Resource
	INSBElfconfService service;
	@Resource
	INSBProviderService insbProviderService;
	@Resource
	INSBSkillService insbSkillService;

	/**
	 * 精灵监控跳转到页面
	 * 
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "robotindex", method = RequestMethod.GET)
	public ModelAndView list() throws ControllerException {
		ModelAndView mav = new ModelAndView("cm/monitor/robot_index");
		mav.addObject("quotetype", "02");
		return mav;
	}

	/**
	 * edi监控跳转到页面
	 * 
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "ediindex", method = RequestMethod.GET)
	public ModelAndView edilist() throws ControllerException {
		ModelAndView mav = new ModelAndView("cm/monitor/edi_index");
		mav.addObject("quotetype", "01");
		return mav;
	}

	/**
	 * 任务监控跳转到页面
	 * 
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "taskindex", method = RequestMethod.GET)
	public ModelAndView tasklist() throws ControllerException {
		ModelAndView model = new ModelAndView("cm/monitor/task_index");
		List<Map<String, Object>> resultAllList = insbMonitorDao.getAllMonitorInfo(new HashMap<>());
		String currentDate = DateUtil.getCurrentDate();
		int todaytotal1 = 0;
		int todaysuc1 = 0;
		int todayfail1 = 0;
		int todaytotalorder1 = 0;
		int todayqouteorder = 0;
		int todayinsureorder = 0;
		int todayxborder1 = 0;
		int todayappovedorder = 0;
		for (Map<String, Object> tempMap : resultAllList) {
			String tempMonitorId = String.valueOf(tempMap.get("monitorid"));
			String tempProcessType = String.valueOf(tempMap.get("quotetype"));

			String CM_MONITOR_TEMP = "newmonitor:" + ("01".equals(tempProcessType) ? "edi" : "robot") + ":"+ tempMonitorId + ":";
			String CM_MONITOR_TEMP_CURRENTDATE = CM_MONITOR_TEMP + "currentdate:" + currentDate + ":";
			String CM_MONITOR_TEMP_CURRENTDATE_TOTAL = CM_MONITOR_TEMP_CURRENTDATE + "total";
			String CM_MONITOR_TEMP_CURRENTDATE_SUCTOTAL = CM_MONITOR_TEMP_CURRENTDATE + "suctotal";
			String CM_MONITOR_TEMP_CURRENTDATE_FAILTOTAL = CM_MONITOR_TEMP_CURRENTDATE + "failtotal";
			String CM_MONITOR_TEMP_CURRENTDATE_ORDER = CM_MONITOR_TEMP_CURRENTDATE + "inorder:";
			String CM_MONITOR_TEMP_CURRENTDATE_ORDER_TOTAL = CM_MONITOR_TEMP_CURRENTDATE_ORDER + "total";

			String todaytotal = String.valueOf(CMRedisClient.getInstance().get("cm", CM_MONITOR_TEMP_CURRENTDATE_TOTAL));
			String todaysuc = String.valueOf(CMRedisClient.getInstance().get("cm", CM_MONITOR_TEMP_CURRENTDATE_SUCTOTAL));
			String todayfail = String.valueOf(CMRedisClient.getInstance().get("cm", CM_MONITOR_TEMP_CURRENTDATE_FAILTOTAL));
			String todaytotalorder = String.valueOf(CMRedisClient.getInstance().get("cm", CM_MONITOR_TEMP_CURRENTDATE_ORDER_TOTAL));
			String todayqouteorder1 = String.valueOf(CMRedisClient.getInstance().get("cm", CM_MONITOR_TEMP_CURRENTDATE_ORDER + "quote"));
			String todayqouteorder2 = String.valueOf(CMRedisClient.getInstance().get("cm", CM_MONITOR_TEMP_CURRENTDATE_ORDER + "quotequery"));
			String todayinsureorder1 = String.valueOf(CMRedisClient.getInstance().get("cm", CM_MONITOR_TEMP_CURRENTDATE_ORDER + "insure"));
			String todayinsureorder2 = String.valueOf(CMRedisClient.getInstance().get("cm", CM_MONITOR_TEMP_CURRENTDATE_ORDER + "insurequery"));
			String todayxborder = String.valueOf(CMRedisClient.getInstance().get("cm", CM_MONITOR_TEMP_CURRENTDATE_ORDER + "xb"));
			String todayappovedorder1 = String.valueOf(CMRedisClient.getInstance().get("cm", CM_MONITOR_TEMP_CURRENTDATE_ORDER + "approved"));
			String todayappovedorder2 = String.valueOf(CMRedisClient.getInstance().get("cm", CM_MONITOR_TEMP_CURRENTDATE_ORDER + "approvedquery"));
			// String todayplateseracchorder =String.valueOf(CMRedisClient.getInstance().get("cm",CM_MONITOR_ROBOT_ERRORCOUNT));

			todaytotal1 = todaytotal1 + Integer.parseInt(StringUtil.isEmpty(todaytotal) ? "0" : todaytotal);
			todaysuc1 = todaysuc1 + Integer.parseInt(StringUtil.isEmpty(todaysuc) ? "0" : todaysuc);
			todayfail1 = todayfail1 + Integer.parseInt(StringUtil.isEmpty(todayfail) ? "0" : todayfail);
			todaytotalorder1 = todaytotalorder1+ Integer.parseInt(StringUtil.isEmpty(todaytotalorder) ? "0" : todaytotalorder);
			todayqouteorder = todayqouteorder+ Integer.parseInt(StringUtil.isEmpty(todayqouteorder1) ? "0" : todayqouteorder1)+ Integer.parseInt(StringUtil.isEmpty(todayqouteorder2) ? "0" : todayqouteorder2);
			todayinsureorder = todayinsureorder+ Integer.parseInt(StringUtil.isNotEmpty(todayinsureorder1) ? todayinsureorder1 : "0")+ Integer.parseInt(StringUtil.isNotEmpty(todayinsureorder2) ? todayinsureorder2 : "0");
			todayxborder1 = todayxborder1 + Integer.parseInt(StringUtil.isNotEmpty(todayxborder) ? todayxborder : "0");
			todayappovedorder = todayappovedorder+ Integer.parseInt(StringUtil.isNotEmpty(todayappovedorder1) ? todayappovedorder1 : "0")+ Integer.parseInt(StringUtil.isNotEmpty(todayappovedorder2) ? todayappovedorder2 : "0");
		}

		model.addObject("todaytotal", todaytotal1);
		model.addObject("todaysuc", todaysuc1);
		model.addObject("todayfail", todayfail1);
		model.addObject("todaytotalorder", todaytotalorder1);
		model.addObject("todayqouteorder", todayqouteorder);
		model.addObject("todayinsureorder", todayinsureorder);
		model.addObject("todayxborder", todayxborder1);
		model.addObject("todayappovedorder", todayappovedorder);

		return model;
	}

	/**
	 * 所属机构选择init
	 * 
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "toporginfo", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> orgList() throws ControllerException {
		return insbMonitorService.queryOrgInfo();
	}
	
	/**
	 * 精灵监控-查询
	 * @param parp
	 * @param monitorModel
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "query", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> queryList(@ModelAttribute PagingParams parp, @ModelAttribute Monitormodel monitorModel)
			throws ControllerException {
		return insbMonitorService.queryList(BeanUtils.toMap(monitorModel, parp));
	}

	/**
	 * edi监控-查询
	 * @param parp
	 * @param monitorModel
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "queryedi", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> queryediList(@ModelAttribute PagingParams parp,
			@ModelAttribute Monitormodel monitorModel) throws ControllerException {
		return insbMonitorService.queryEdiList(BeanUtils.toMap(monitorModel, parp));
	}

	/**
	 * 任务监控-查询
	 * @param parp
	 * @param monitorModel
	 * @param insbMonitor
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "querytask", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> querytaskList(@ModelAttribute PagingParams parp,
			@ModelAttribute Monitormodel monitorModel, @ModelAttribute INSBMonitor insbMonitor)
			throws ControllerException {
		return insbMonitorService.querytaskinfoList(BeanUtils.toMap(monitorModel, parp, insbMonitor));
	}

	/**
	 * 清空错误数
	 * 
	 * @param parp
	 * @param monitorModel
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "zeroerrorcount", method = RequestMethod.GET)
	@ResponseBody
	public String zeroerrorcount(String monitorid, String quotetype) throws ControllerException {
		String CM_MONITOR_EDI = "newmonitor:" + ("02".equals(quotetype) ? "robot" : "edi") + ":" + monitorid + ":";
		String CM_MONITOR_EDI_ERRORCOUNT = CM_MONITOR_EDI + "errorcount";
		CMRedisClient.getInstance().set("cm", CM_MONITOR_EDI_ERRORCOUNT, "0");
		return String.valueOf(CMRedisClient.getInstance().get("cm", CM_MONITOR_EDI_ERRORCOUNT));
	}

	/**
	 * 精灵监控跳转到详细信息页面
	 * 
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "robotinfo", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView robotInfo(String id, String orgcode) throws ControllerException {
		ModelAndView model = new ModelAndView("cm/monitor/robot_info");
		List<INSCCode> capacityconf = inscCodeService.queryINSCCodeByCode("capacityconf", "capacityconf");
		INSCCode inputcode = new INSCCode();
		inputcode.setParentcode("inputItem");
		List<INSCCode> input = inscCodeService.queryList(inputcode);
		INSCCode outputcode = new INSCCode();
		outputcode.setParentcode("outputItem");
		List<INSCCode> output = inscCodeService.queryList(outputcode);
		INSBElfconf elfconf = service.queryById(id);
		model.addObject("elfconf", elfconf);// 精灵
		model.addObject("inputItem", input);// 字典表输入项
		model.addObject("outputItem", output);// 字典表输出项
		model.addObject("capacityconf", capacityconf);// 字典表能力配置
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", id);
		data.put("orgcode", orgcode);
		model.addObject("provider", insbMonitorService.queryPrvNames(data));// 供应商
		List<INSCCode> conftype = inscCodeService.queryINSCCodeByCode("conftype", "conftype");
		model.addObject("conftype", conftype);// 字典表配置类型（01：报价配置// :02：核保配置:03：续保配置.04:承保配置）
		List<INSBSkill> skillInputList = insbSkillService.queryListByelfId(id, "in");
		List<INSBSkill> skillOutputList = insbSkillService.queryListByelfId(id, "out");
		model.addObject("skillInputList", skillInputList);// 技能表输入项
		model.addObject("skillOutputList", skillOutputList);// 技能表输出项
		model.addObject("sbskill", insbSkillService.querySkillnameByelfid(id));

		String currentDate = DateUtil.getCurrentDate();
		String CM_MONITOR_ROBOT = "newmonitor:robot:" + id + "_" + orgcode + ":";
		String CM_MONITOR_ROBOT_ERRORCOUNT = CM_MONITOR_ROBOT + "errorcount";
		String CM_MONITOR_ROBOT_CURRENTDATE = CM_MONITOR_ROBOT + "currentdate:" + currentDate + ":";
		String CM_MONITOR_ROBOT_CURRENTDATE_TOTAL = CM_MONITOR_ROBOT_CURRENTDATE + "total";
		String CM_MONITOR_ROBOT_CURRENTDATE_SUCTOTAL = CM_MONITOR_ROBOT_CURRENTDATE + "suctotal";
		String CM_MONITOR_ROBOT_CURRENTDATE_FAILTOTAL = CM_MONITOR_ROBOT_CURRENTDATE + "failtotal";
		String CM_MONITOR_ROBOT_CURRENTDATE_ORDER = CM_MONITOR_ROBOT_CURRENTDATE + "inorder:";
		String CM_MONITOR_ROBOT_CURRENTDATE_ORDER_TOTAL = CM_MONITOR_ROBOT_CURRENTDATE_ORDER + "total";
		// 当天任务处理数:${todaytotal},成功任务数:${todaysuc},失败任务数:${todayfail} <br>
		// 当前排队任务数:${todaytotalorder},报价排队:${todayqouteorder},核保排队:${todayinsureorder},续保排队:${todayxborder},承保排队:${todayappovedorder},平台排队:${todayplateseracchorder}
		String tempErrorcount = String.valueOf(CMRedisClient.getInstance().get("cm", CM_MONITOR_ROBOT_ERRORCOUNT));
		String todaytotal = String.valueOf(CMRedisClient.getInstance().get("cm", CM_MONITOR_ROBOT_CURRENTDATE_TOTAL));
		String todaysuc = String.valueOf(CMRedisClient.getInstance().get("cm", CM_MONITOR_ROBOT_CURRENTDATE_SUCTOTAL));
		String todayfail = String.valueOf(CMRedisClient.getInstance().get("cm", CM_MONITOR_ROBOT_CURRENTDATE_FAILTOTAL));
		String todaytotalorder = String.valueOf(CMRedisClient.getInstance().get("cm", CM_MONITOR_ROBOT_CURRENTDATE_ORDER_TOTAL));
		String todayqouteorder1 = String.valueOf(CMRedisClient.getInstance().get("cm", CM_MONITOR_ROBOT_CURRENTDATE_ORDER + "quote"));
		String todayqouteorder2 = String.valueOf(CMRedisClient.getInstance().get("cm", CM_MONITOR_ROBOT_CURRENTDATE_ORDER + "quotequery"));
		String todayinsureorder1 = String.valueOf(CMRedisClient.getInstance().get("cm", CM_MONITOR_ROBOT_CURRENTDATE_ORDER + "insure"));
		String todayinsureorder2 = String.valueOf(CMRedisClient.getInstance().get("cm", CM_MONITOR_ROBOT_CURRENTDATE_ORDER + "insurequery"));
		String todayxborder = String.valueOf(CMRedisClient.getInstance().get("cm", CM_MONITOR_ROBOT_CURRENTDATE_ORDER + "xb"));
		String todayappovedorder1 = String.valueOf(CMRedisClient.getInstance().get("cm", CM_MONITOR_ROBOT_CURRENTDATE_ORDER + "approved"));
		String todayappovedorder2 = String.valueOf(CMRedisClient.getInstance().get("cm", CM_MONITOR_ROBOT_CURRENTDATE_ORDER + "approvedquery"));
		// String todayplateseracchorder =String.valueOf(CMRedisClient.getInstance().get("cm",CM_MONITOR_ROBOT_ERRORCOUNT));
		model.addObject("todaytotal", StringUtil.isNotEmpty(todaytotal) ? todaytotal : "0");
		model.addObject("todaysuc", StringUtil.isNotEmpty(todaysuc) ? todaysuc : "0");
		model.addObject("todayfail", StringUtil.isNotEmpty(todayfail) ? todayfail : "0");
		model.addObject("todaytotalorder", StringUtil.isNotEmpty(todaytotalorder) ? todaytotalorder : "0");
		model.addObject("todayqouteorder",Integer.parseInt(StringUtil.isNotEmpty(todayqouteorder1) ? todayqouteorder1 : "0")+ Integer.parseInt(StringUtil.isNotEmpty(todayqouteorder2) ? todayqouteorder2 : "0"));
		model.addObject("todayinsureorder",Integer.parseInt(StringUtil.isNotEmpty(todayinsureorder1) ? todayinsureorder1 : "0")+ Integer.parseInt(StringUtil.isNotEmpty(todayinsureorder2) ? todayinsureorder2 : "0"));
		model.addObject("todayxborder", StringUtil.isNotEmpty(todayxborder) ? todayxborder : "0");
		model.addObject("todayappovedorder",Integer.parseInt(StringUtil.isNotEmpty(todayappovedorder1) ? todayappovedorder1 : "0")+ Integer.parseInt(StringUtil.isNotEmpty(todayappovedorder2) ? todayappovedorder2 : "0"));
		// model.addObject("todayplateseracchorder",StringUtil.isNotEmpty(todayplateseracchorder)?todayplateseracchorder:"0");
		if (StringUtil.isNotEmpty(tempErrorcount) && Integer.parseInt(tempErrorcount) >= 10) {
			model.addObject("status", "<font style=\"color: red\">异常</font>");
		} else {
			model.addObject("status", "健康");
		}
		// 过滤掉右侧的技能输入项
		String filterin = insbSkillService.filter(id, "in");
		model.addObject("filterin", filterin);
		// 过滤掉右侧的技能输出项
		String filterout = insbSkillService.filter(id, "out");
		model.addObject("filterout", filterout);
		model.addObject("keyone", id + "_" + orgcode);
		model.addObject("quotetype", "02");
		return model;
	}

	/**
	 * 精灵监控-详情页-查询
	 * @param parp
	 * @param insbMonitor
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "robottaskinfo", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> robottaskinfoList(@ModelAttribute PagingParams parp,
			@ModelAttribute INSBMonitor insbMonitor) throws ControllerException {
		return insbMonitorService.queryTaskList((BeanUtils.toMap(insbMonitor, parp)));
	}

	/**
	 * edi监控跳转到详细信息页面
	 * 
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "ediinfo", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView ediInfo(String id, String orgcode) throws ControllerException {
		ModelAndView model = new ModelAndView("cm/monitor/edi_info");
		List<INSCCode> capacityconf = inscCodeService.queryINSCCodeByCode("capacityconf", "capacityconf");
		INSCCode inputcode = new INSCCode();
		inputcode.setParentcode("inputItem");
		List<INSCCode> input = inscCodeService.queryList(inputcode);
		INSCCode outputcode = new INSCCode();
		outputcode.setParentcode("outputItem");
		List<INSCCode> output = inscCodeService.queryList(outputcode);
		List<INSBEdidescription> edidesc = ediService.queryediinfoByid(id);
		model.addObject("edidesc", edidesc);
		model.addObject("ediconf", ediService.queryById(id));
		model.addObject("prolist", insbProviderService.queryListPro(id));
		List<INSCCode> conftype = inscCodeService.queryINSCCodeByCode("conftype", "conftype");
		model.addObject("conftype", conftype);// 字典表配置类型（01：报价配置:02：核保配置:03：续保配置.04:承保配置）
		model.addObject("inputItem", input);// 字典表输入项
		model.addObject("outputItem", output);// 字典表输出项
		model.addObject("capacityconf", capacityconf);// 字典表能力配置
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", id);
		data.put("orgcode", orgcode);
		model.addObject("provider", insbMonitorService.queryPrvNames(data));// 供应商
		List<INSBSkill> skillInputList = insbSkillService.queryListByelfId(id, "in");
		List<INSBSkill> skillOutputList = insbSkillService.queryListByelfId(id, "out");
		model.addObject("skillInputList", skillInputList);// 技能表输入项
		model.addObject("skillOutputList", skillOutputList);// 技能表输出项
		model.addObject("sbskill", insbSkillService.querySkillnameByelfid(id));

		String currentDate = DateUtil.getCurrentDate();
		String CM_MONITOR_EDI = "newmonitor:edi:" + id + "_" + orgcode + ":";
		String CM_MONITOR_EDI_ERRORCOUNT = CM_MONITOR_EDI + "errorcount";
		String CM_MONITOR_EDI_CURRENTDATE = CM_MONITOR_EDI + "currentdate:" + currentDate + ":";
		String CM_MONITOR_EDI_CURRENTDATE_TOTAL = CM_MONITOR_EDI_CURRENTDATE + "total";
		String CM_MONITOR_EDI_CURRENTDATE_SUCTOTAL = CM_MONITOR_EDI_CURRENTDATE + "suctotal";
		String CM_MONITOR_EDI_CURRENTDATE_FAILTOTAL = CM_MONITOR_EDI_CURRENTDATE + "failtotal";
		String CM_MONITOR_EDI_CURRENTDATE_ORDER = CM_MONITOR_EDI_CURRENTDATE + "inorder:";
		String CM_MONITOR_EDI_CURRENTDATE_ORDER_TOTAL = CM_MONITOR_EDI_CURRENTDATE_ORDER + "total";
		// 当天任务处理数:${todaytotal},成功任务数:${todaysuc},失败任务数:${todayfail} <br>
		// 当前排队任务数:${todaytotalorder},报价排队:${todayqouteorder},核保排队:${todayinsureorder},续保排队:${todayxborder},承保排队:${todayappovedorder},平台排队:${todayplateseracchorder}
		String tempErrorcount = String.valueOf(CMRedisClient.getInstance().get("cm", CM_MONITOR_EDI_ERRORCOUNT));
		String todaytotal = String.valueOf(CMRedisClient.getInstance().get("cm", CM_MONITOR_EDI_CURRENTDATE_TOTAL));
		String todaysuc = String.valueOf(CMRedisClient.getInstance().get("cm", CM_MONITOR_EDI_CURRENTDATE_SUCTOTAL));
		String todayfail = String.valueOf(CMRedisClient.getInstance().get("cm", CM_MONITOR_EDI_CURRENTDATE_FAILTOTAL));
		String todaytotalorder = String.valueOf(CMRedisClient.getInstance().get("cm", CM_MONITOR_EDI_CURRENTDATE_ORDER_TOTAL));
		String todayqouteorder1 = String.valueOf(CMRedisClient.getInstance().get("cm", CM_MONITOR_EDI_CURRENTDATE_ORDER + "quote"));
		String todayqouteorder2 = String.valueOf(CMRedisClient.getInstance().get("cm", CM_MONITOR_EDI_CURRENTDATE_ORDER + "quotequery"));
		String todayinsureorder1 = String.valueOf(CMRedisClient.getInstance().get("cm", CM_MONITOR_EDI_CURRENTDATE_ORDER + "insure"));
		String todayinsureorder2 = String.valueOf(CMRedisClient.getInstance().get("cm", CM_MONITOR_EDI_CURRENTDATE_ORDER + "insurequery"));
		String todayxborder = String.valueOf(CMRedisClient.getInstance().get("cm", CM_MONITOR_EDI_CURRENTDATE_ORDER + "xb"));
		String todayappovedorder1 = String.valueOf(CMRedisClient.getInstance().get("cm", CM_MONITOR_EDI_CURRENTDATE_ORDER + "approved"));
		String todayappovedorder2 = String.valueOf(CMRedisClient.getInstance().get("cm", CM_MONITOR_EDI_CURRENTDATE_ORDER + "approvedquery"));
		// String todayplateseracchorder =String.valueOf(CMRedisClient.getInstance().get("cm",CM_MONITOR_EDI_ERRORCOUNT));
		model.addObject("todaytotal", StringUtil.isNotEmpty(todaytotal) ? todaytotal : "0");
		model.addObject("todaysuc", StringUtil.isNotEmpty(todaysuc) ? todaysuc : "0");
		model.addObject("todayfail", StringUtil.isNotEmpty(todayfail) ? todayfail : "0");
		model.addObject("todaytotalorder", StringUtil.isNotEmpty(todaytotalorder) ? todaytotalorder : "0");
		model.addObject("todayqouteorder",Integer.parseInt(StringUtil.isNotEmpty(todayqouteorder1) ? todayqouteorder1 : "0")+ Integer.parseInt(StringUtil.isNotEmpty(todayqouteorder2) ? todayqouteorder2 : "0"));
		model.addObject("todayinsureorder",Integer.parseInt(StringUtil.isNotEmpty(todayinsureorder1) ? todayinsureorder1 : "0")+ Integer.parseInt(StringUtil.isNotEmpty(todayinsureorder2) ? todayinsureorder2 : "0"));
		model.addObject("todayxborder", StringUtil.isNotEmpty(todayxborder) ? todayxborder : "0");
		model.addObject("todayappovedorder",Integer.parseInt(StringUtil.isNotEmpty(todayappovedorder1) ? todayappovedorder1 : "0")+ Integer.parseInt(StringUtil.isNotEmpty(todayappovedorder2) ? todayappovedorder2 : "0"));
		// model.addObject("todayplateseracchorder",StringUtil.isNotEmpty(todayplateseracchorder)?todayplateseracchorder:"0");
		if (StringUtil.isNotEmpty(tempErrorcount) && Integer.parseInt(tempErrorcount) >= 10) {
			model.addObject("status", "<font style=\"color: red\">异常</font>");
		} else {
			model.addObject("status", "健康");
		}
		// 过滤掉右侧的技能输入项
		String filterin = insbSkillService.filter(id, "in");
		model.addObject("filterin", filterin);
		// 过滤掉右侧的技能输出项
		String filterout = insbSkillService.filter(id, "out");
		model.addObject("filterout", filterout);
		model.addObject("keyone", id + "_" + orgcode);
		model.addObject("quotetype", "01");
		return model;
	}

	/**
	 * edi监控-详情页-查询
	 * @param parp
	 * @param insbMonitor
	 * @param monitormodel
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "editaskinfo", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> editaskinfoList(@ModelAttribute PagingParams parp,
			@ModelAttribute INSBMonitor insbMonitor, @ModelAttribute Monitormodel monitormodel)
			throws ControllerException {
		return insbMonitorService.queryTaskList((BeanUtils.toMap(insbMonitor, parp, monitormodel)));
	}

	@Resource
	INSBFlowerrorService insbFlowerrorService;

	/**
	 * 任务监控-查看错误信息
	 * @param maininstanceid
	 * @param inscomcode
	 * @param inscomName
	 * @return
	 */
	@RequestMapping(value = "showflowerror4monitor", method = RequestMethod.GET)
	public ModelAndView showFlowError(String maininstanceid, String inscomcode, String inscomName) {
		ModelAndView mav = new ModelAndView("cm/cartaskmanage/flowErrorDetail");
		INSBFlowerror query = new INSBFlowerror();
		query.setTaskid(maininstanceid);
		query.setInscomcode(inscomcode);
		// String inscompanyName =null;
		// try {
		// inscompanyName= new String(inscomName.getBytes("ISO-8859-1"),
		// "UTF-8"); //URLDecoder.decode(inscomName, "UTF-8");
		// }catch (Exception e){
		// LogUtil.info("inscomName is encode error"+ e.getMessage());
		// }
		mav.addObject("errorList", insbFlowerrorService.queryList(query));
		mav.addObject("inscomName", inscomName);
		mav.addObject("maininstanceid", maininstanceid);
		return mav;
	}

	@RequestMapping(value = "getJaonInfo", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String getJsonInfo(String url) throws ControllerException {
		return HttpClientUtil.doGet(url, null);
	}
}
