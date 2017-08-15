package com.zzb.conf.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCDeptService;
import com.common.ConstUtil;
import com.common.ExcelUtil;
import com.common.ModelUtil;
import com.common.PagingParams;
import com.zzb.chn.util.JsonUtils;
import com.zzb.conf.controller.vo.BaseInfoVo;
import com.zzb.conf.controller.vo.BillTypeVo;
import com.zzb.conf.controller.vo.DeptPayTypeVo;
import com.zzb.conf.controller.vo.DistributionVo;
import com.zzb.conf.controller.vo.InterfaceVo;
import com.zzb.conf.dao.INSBAgreementinterfaceDao;
import com.zzb.conf.dao.INSBChannelDao;
import com.zzb.conf.entity.INSBAgreement;
import com.zzb.conf.entity.INSBAgreementarea;
import com.zzb.conf.entity.INSBAgreementdept;
import com.zzb.conf.entity.INSBAgreementpaymethod;
import com.zzb.conf.entity.INSBAgreementprovider;
import com.zzb.conf.entity.INSBChannel;
import com.zzb.conf.entity.INSBChnchargerule;
import com.zzb.conf.entity.INSBOutorderdept;
import com.zzb.conf.entity.INSBRegion;
import com.zzb.conf.service.INSBAgreementService;
import com.zzb.conf.service.INSBAgreementareaService;
import com.zzb.conf.service.INSBAgreementdeptService;
import com.zzb.conf.service.INSBAgreementinterfaceService;
import com.zzb.conf.service.INSBAgreementpaymethodService;
import com.zzb.conf.service.INSBAgreementproviderService;
import com.zzb.conf.service.INSBChannelService;
import com.zzb.conf.service.INSBChannelagreementService;
import com.zzb.conf.service.INSBChnchargeruleService;
import com.zzb.conf.service.INSBDistributionService;
import com.zzb.conf.service.INSBOutorderdeptService;
import com.zzb.conf.service.INSBPaychannelmanagerService;
import com.zzb.conf.service.INSBRegionService;

@Controller
@RequestMapping("/channel/*")
public class INSBChannelProtocolController extends BaseController {

	@Resource
	private INSBAgreementService INSBAgreementService;
	@Resource
	private INSBChannelService service;
	@Resource
	private INSBRegionService insbRegionService;
	@Resource
	private INSCDeptService deptservice;
	@Resource
	private INSBAgreementdeptService agreementdeptService;
	@Resource
	private INSBChannelagreementService insbChannelagreementService;
	@Resource
	private INSBAgreementareaService insbAgreementareaService;
	@Resource
	private INSBAgreementproviderService insbAgreementproviderService;
	@Resource
	private INSBAgreementpaymethodService agreementpaymethodService;
	@Resource
	private INSBOutorderdeptService outorderdeptService;
	@Resource
	private INSBDistributionService distributionService;
	@Resource
	private INSBAgreementinterfaceService insbAgreementinterfaceService;
	@Resource
	private INSBPaychannelmanagerService insbPaychannelmanagerService;
	@Resource
	private INSBChannelDao channelDao;
	@Resource
	private INSCDeptService inscDeptService;
	@Resource
	private INSBDistributionService insbDistributionService;
	@Resource
	private INSBAgreementService insbAgreementService;
	@Resource
	private INSBAgreementpaymethodService insbAgreementpaymethodService;
	@Resource
	private INSBChnchargeruleService insbChnchargeruleService;
	@Resource
	private INSBAgreementinterfaceDao insbagreementInterfaceDao;

	/**
	 * 跳转页面
	 *
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "preChannelProtocol", method = RequestMethod.GET)
	public ModelAndView preChannelProtocol() throws ControllerException {
		ModelAndView model = new ModelAndView("");
		return model;
	}

	/**
	 * 获取渠道协议信息 出单网点入参需要渠道ID 旧版本
	 */
	@RequestMapping(value = "getChannelProtocolInfo", method = RequestMethod.GET)
	public ModelAndView getChannelProtocolInfo(String id,
			@RequestParam(value = "offset", required = false) Integer offset,
			@RequestParam(value = "limit", required = false) Integer limit) throws ControllerException {
		INSBChannel insbChannel = new INSBChannel();
		insbChannel.setId(id);
		ModelAndView model = new ModelAndView("zzbconf/channelagreementsub");
		if (ModelUtil.isVoluation(offset, limit)) {
			offset = ConstUtil.OFFSET;
			limit = ConstUtil.LIMIT;
		}
		// 结算方式
		BillTypeVo billTypeVo = insbChannelagreementService.getBillTypeInfo(insbChannel, offset, limit);
		List<Map<String, String>> provider = agreementdeptService.getPrvider(insbChannel);
		List<INSBAgreementprovider> checkedProvider = agreementdeptService.checkedPrvider(insbChannel);
		model.addObject("billTypeVo", billTypeVo);
		model.addObject("provider", provider);
		model.addObject("checkedProvider", checkedProvider);
		return model;
	}

	/**
	 * 渠道协议配置第三版
	 */
	@RequestMapping(value = "getNewChannelProtocolInfo", method = RequestMethod.GET)
	public ModelAndView getNewChannelProtocolInfo(String id) throws ControllerException {
		ModelAndView model = new ModelAndView("zzbconf/newchannelagreementsub");
		INSBChannel insbChannel = new INSBChannel();
		insbChannel.setId(id);
		model.addObject("channelinfo", service.getChannelAgreementInfo(id));
		INSBChannel Tchannel = channelDao.selectById(id);
		INSCDept dept = new INSCDept();
		dept.setComcode(Tchannel.getDeptid());
		dept = inscDeptService.queryOne(dept);
		if (dept != null) {
			model.addObject("dept", dept.getComname());
		} else {
			model.addObject("dept", "");
		}
		List<Map<String, String>> provider = agreementdeptService.getPrvider(insbChannel);
		List<INSBAgreementprovider> checkedProvider = agreementdeptService.checkedPrvider(insbChannel);
		List<String> providerStr = new ArrayList<String>();
		for (INSBAgreementprovider cp : checkedProvider) {
			providerStr.add(cp.getProviderid() + "#" + cp.getAgreeid());
		}
		model.addObject("checkedProvider", providerStr);
		model.addObject("provider", provider);
		return model;
	}

	@RequestMapping(value = "getChannelAgreementprovider", method = RequestMethod.GET)
	@ResponseBody
	public String getChannelAgreementprovider(@ModelAttribute PagingParams parm, String channelid) {
		Map<Object, Object> initMap = new HashMap<Object, Object>();
		initMap.put("rows", insbChannelagreementService.qChannelAgreementprovider(channelid, parm));
		initMap.put("total", insbChannelagreementService.getCountProvider(channelid));
		initMap.put("page", 1);
		initMap.put("records", 10000);
		System.out.println(JSONObject.toJSONString(initMap));
		return JSONObject.toJSONString(initMap);
	}

	/*
	 * 删除选中的供应商 agreementdept,agreementpaymethod表中的agreementid 都是协议表中的 agreeid
	 */
	@RequestMapping(value = "deletebyagrprvid", method = RequestMethod.GET)
	@ResponseBody
	public String deletebyagrprvid(@ModelAttribute PagingParams parm, String id, String agreeid) {
		// id是provider中间表id,agreeid 是协议id
		Map<Object, Object> initMap = new HashMap<Object, Object>();
		try {
			String str = insbAgreementproviderService.deletePrvDeptPay(id, agreeid);
			return str;
		} catch (Exception e) {
			e.printStackTrace();
			initMap.put("status", "fail");
			initMap.put("msg", "删除失败");
			return JSONObject.toJSONString(initMap);
		}
	}

	/**
	 * 获取出单网点信息
	 */
	@RequestMapping(value = "getOutstore", method = RequestMethod.GET)
	@ResponseBody
	public String getOutstore(@RequestParam String channelid) throws ControllerException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("records", "10000");
		map.put("page", 1);
		map.put("rows", insbChannelagreementService.existChannelid(channelid));
		return JSONObject.toJSONString(map);
	}

	/**
	 * 出单网点页面关联按钮 保存
	 */
	@RequestMapping(value = "/savescope", method = RequestMethod.POST)
	@ResponseBody
	public String savescope(HttpSession session, INSBAgreementdept conndept) {
		try {
			String[] scopedept = { conndept.getDeptid1(), conndept.getDeptid2(), conndept.getDeptid3(),
					conndept.getDeptid4(), conndept.getDeptid5() };
			String deptid = scopedept[4];
			if (deptid != null && !"".equals(deptid) && !deptid.equals("0")) {
				Date newDate = new Date();
				INSCUser operator = (INSCUser) session.getAttribute("insc_user");
				conndept.setModifytime(newDate);
				conndept.setCreatetime(newDate);
				conndept.setDeptid1(conndept.getDeptid1());
				conndept.setDeptid2(conndept.getDeptid2());
				conndept.setDeptid3(conndept.getDeptid3());
				conndept.setDeptid4(conndept.getDeptid4());
				conndept.setDeptid5(conndept.getDeptid5());
				conndept.setOperator(operator.getName());
				conndept.setAgreementid(conndept.getAgreementid());
				this.agreementdeptService.insert(conndept);
			}
		} catch (Exception e) {
			return "fail";
		}
		// else{
		// String deptid2 = null;
		// for(int i = 0; i < scopedept.length; i++){
		// if(scopedept[i] != null && !"".equals(scopedept[i]) &&
		// !scopedept[i].equals("0")){
		// deptid2=scopedept[i];
		// }
		// }
		// if(deptid2 != null && !"".equals(deptid2) && !"0".equals(deptid2)){
		// List<String> deptWdids =
		// deptservice.selectByParentDeptCode4groups(deptid2);
		// if(deptWdids != null && deptWdids.size() > 0){
		// for(String id : deptWdids){
		// Date newDate = new Date();
		// INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		// conndept.setModifytime(newDate);
		// conndept.setCreatetime(newDate);
		// conndept.setDeptid1(conndept.getDeptid1());
		// conndept.setDeptid2(conndept.getDeptid2());
		// conndept.setDeptid3(conndept.getDeptid3());
		// conndept.setDeptid4(conndept.getDeptid4());
		// conndept.setDeptid5(conndept.getDeptid5());
		// conndept.setOperator(operator.getName());
		// conndept.setAgreementid(conndept.getAgreementid());
		//
		// this.agreementdeptService.insert(conndept);
		//
		//
		// }
		// }
		// }
		// }
		return "success";
	}

	/**
	 * 查询机构
	 *
	 * @param parentcode
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "querydepttree", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> queryDeptList(@RequestParam(value = "id", required = false) String parentcode)
			throws ControllerException {
		return deptservice.queryTreeListByAgr(parentcode, "02");
	}

	/**
	 * 出单网点页面设置按钮
	 */
	@RequestMapping(value = "updateAgreementdept", method = RequestMethod.POST)
	@ResponseBody
	public void updateAgreementdept(INSBAgreementdept agreementdept) throws ControllerException {
		agreementdeptService.updateById(agreementdept);
	}

	/**
	 * 出单网点页面取消关联按钮
	 */
	@RequestMapping(value = "deleteAgreementdept", method = RequestMethod.POST)
	@ResponseBody
	public String deleteAgreementdept(String deptList) throws ControllerException {
		try {
			JSONArray parseArray = JSONObject.parseArray(deptList);
			for (Object object : parseArray) {
				Map<String, Object> map = (Map<String, Object>) object;
				agreementdeptService.deleteById((String) map.get("id"));
			}
		} catch (Exception e) {
			return "fail";
		}
		return "success";
	}

	/**
	 * 供应商范围保存按钮(修改之前版本)
	 */
	@RequestMapping(value = "getUploadAndSaveProvider", method = RequestMethod.POST)
	@ResponseBody
	public String getUploadAndSaveProvider(HttpSession session, INSBAgreementprovider insbagreementprovider)
			throws ControllerException {
		String[] arrprvcode = insbagreementprovider.getProviderid().split(",");
		Date newDate = new Date();
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		INSBAgreementprovider Query_insbagreementprovider = new INSBAgreementprovider();
		Query_insbagreementprovider.setAgreementid(insbagreementprovider.getAgreementid());
		List<INSBAgreementprovider> listagreementprovider = insbAgreementproviderService
				.queryList(Query_insbagreementprovider);
		for (INSBAgreementprovider agreementprovider : listagreementprovider) {
			insbAgreementproviderService.deleteById(agreementprovider.getId());
		}
		String uuid = null;
		for (int i = 0; i < arrprvcode.length; i++) {
			uuid = UUIDUtils.random();
			INSBAgreementprovider IN_agreementprovider = new INSBAgreementprovider();
			IN_agreementprovider.setId(uuid);
			IN_agreementprovider.setCreatetime(newDate);
			IN_agreementprovider.setModifytime(newDate);
			IN_agreementprovider.setOperator(operator.getName());
			IN_agreementprovider.setAgreementid(insbagreementprovider.getAgreementid());
			IN_agreementprovider.setProviderid(arrprvcode[i]);
			insbAgreementproviderService.insert(IN_agreementprovider);
		}
		return "success";
	}

	/**
	 * 供应商范围保存按钮(修改后)
	 */
	@RequestMapping(value = "UploadAndSaveProvider", method = RequestMethod.POST)
	@ResponseBody
	public String uploadAndSaveProvider(HttpSession session, INSBAgreementprovider insbagreementprovider)
			throws ControllerException {
		String[] agreeids = insbagreementprovider.getAgreeid().split(",");
		Date newDate = new Date();
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		
		if ( StringUtil.isNotEmpty(insbagreementprovider.getAgreementid()) ) {
			INSBAgreementprovider insbAgreementprovider = new INSBAgreementprovider();
			insbAgreementprovider.setAgreementid(insbagreementprovider.getAgreementid());
			List<INSBAgreementprovider> insbAgreementproviders = insbAgreementproviderService.queryList(insbAgreementprovider);
			
			for (INSBAgreementprovider agreementprovider : insbAgreementproviders) {
				insbAgreementproviderService.deleteById(agreementprovider.getId());
				
				if ( StringUtil.isNotEmpty(agreementprovider.getProviderid()) ) {
					INSBAgreementpaymethod payDelCon = new INSBAgreementpaymethod();
					payDelCon.setAgreementid(insbagreementprovider.getAgreementid());
					payDelCon.setProviderid(agreementprovider.getProviderid());
					agreementpaymethodService.delete(payDelCon);
					
					INSBAgreementdept deptCon = new INSBAgreementdept();
					deptCon.setAgreementid(insbagreementprovider.getAgreementid()); 
					deptCon.setProviderid(agreementprovider.getProviderid()); 
					agreementdeptService.delete(deptCon);
				}
			}
		}
		
		for (String agreeid : agreeids) {
			INSBAgreement insbAgreement = insbAgreementService.queryById(agreeid);
			INSBAgreementprovider insbAgreementprovider = new INSBAgreementprovider();
			insbAgreementprovider.setCreatetime(newDate);
			insbAgreementprovider.setModifytime(newDate);
			insbAgreementprovider.setOperator(operator.getName());
			insbAgreementprovider.setAgreeid(agreeid);
			insbAgreementprovider.setAgreementid(insbagreementprovider.getAgreementid());
			insbAgreementprovider.setProviderid(insbAgreement.getProviderid());
			insbAgreementproviderService.insert(insbAgreementprovider);
		}
		return "success";
	}

	/**
	 * 修改结算方式
	 */
	@RequestMapping(value = "updateBillType", method = RequestMethod.POST)
	@ResponseBody
	public String updateChannel(BillTypeVo billTypeVo) throws ControllerException {
		String result = insbChannelagreementService.updateBillTypeInfo(billTypeVo);
		return result;
	}

	/**
	 * 已关联网点配置优先级等 弹出框
	 *
	 * @param agreementDeptid
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "preEditChannelDeptLevel", method = RequestMethod.GET)
	public ModelAndView preEditChannelDeptLevel(String agreementDeptid) throws ControllerException {
		ModelAndView model = new ModelAndView("zzbconf/editChannelDeptLevel");
		model.addObject("agreementDept", agreementdeptService.queryById(agreementDeptid));
		return model;
	}

	/**
	 * 已关联网点优先级 权限设置
	 *
	 * @param scaleflag
	 * @param permflag
	 * @param agreementDeptid
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "editChannelDeptLevel", method = RequestMethod.GET)
	@ResponseBody
	public String editChannelDeptLevel(String scaleflag, String permflag, String agreementDeptid)
			throws ControllerException {
		try {
			INSBAgreementdept temp = agreementdeptService.queryById(agreementDeptid);
			temp.setPermflag(permflag);
			temp.setScaleflag(scaleflag);
			agreementdeptService.updateById(temp);
		} catch (Exception e) {
			return "fail";
		}
		return "success";
	}

	@RequestMapping(value = "getSelectArea", method = RequestMethod.POST)
	@ResponseBody
	public String getSelectArea(String province, String agreementid) throws ControllerException {
		INSBRegion regionvo = new INSBRegion();
		if (StringUtil.isEmpty(province)) {
			regionvo.setParentid("110000");
		} else {
			regionvo.setParentid(province);
		}
		List<INSBRegion> list = insbRegionService.queryList(regionvo);
		// 初始化已选择的城市
		INSBAgreementarea temp = new INSBAgreementarea();
		temp.setAgreementid(agreementid);
		List<INSBAgreementarea> citys = insbAgreementareaService.queryList(temp);
		Map<String, Object> map = new HashMap<String, Object>();
		for (INSBAgreementarea insbAgreementarea : citys) {
			map.put(insbAgreementarea.getCity(), insbAgreementarea);
		}
		for (INSBRegion region : list) {
			if (map.containsKey(region.getComcode())) {
				region.setParentid("checked");
			}
		}
		return JSONObject.toJSONString(list);
	}
	/**
	 * 渠道管理页面供应商列表信息
	 */
	// @RequestMapping(value="getagreementbycomcode",method=RequestMethod.GET)
	// @ResponseBody
	// public String getAgreementByComcode(@ModelAttribute PagingParams
	// para,String agreementcode){
	// Map<Object, Object> initMap = new HashMap<Object, Object>();
	// Map<String, Object> map = BeanUtils.toMap(para);
	// map.put("comcode", "010");
	// map.put("agreementcode", agreementcode);
	// initMap.put("records", "10000");
	// initMap.put("page", 1);
	// initMap.put("rows", insbAgreementService.queryListAgreement(map));
	// initMap.put("total", insbAgreementService.queryCountAgreement(map));
	// JSONObject jsonObject = JSONObject.fromObject(initMap);
	// return jsonObject.toString();
	// }

	/**
	 * 点击新增供应商按钮查询出所有供应商
	 */
	@RequestMapping(value = "getAllprovider", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView getAllprovider(String id) throws ControllerException {
		ModelAndView view = new ModelAndView("zzbconf/newchannelagreementsub");
		INSBChannel insbChannel = new INSBChannel();
		insbChannel.setId(id);
		List<Map<String, String>> provider = agreementdeptService.getPrvider(insbChannel);
		view.addObject("provider", provider);
		view.addObject("channelid", id);
		return view;
	}

	/**
	 * 点击供应商管理中的编辑操作出现出单网店和支付方式和配送方式
	 */
	@RequestMapping(value = "getPayOutAndDistribut", method = RequestMethod.POST)
	@ResponseBody
	public List<Object> getPayOutAndDistribut(String deptid, String agreementid, String providerid)
			throws ControllerException {
		List<Object> payoutdistribut = new ArrayList<Object>();
		Map<String, Object> parm = new HashMap<String, Object>();
		// INSBAgreementpaymethod agreementpaymethod =
		// agreementpaymethodService.getDeptPaymethod(deptid, agreementid,
		// providerid);
		// parm.put("pay",agreementpaymethod);
		parm.put("out", outorderdeptService.getOutorderdept(agreementid, providerid));
		// parm.put("distribut",distributionService.getAllDistribution(deptid,
		// agreementid, providerid));
		payoutdistribut.add(parm);
		return payoutdistribut;
	}

	/**
	 * @param agreementID
	 * @return 获取出单网点名称
	 * @throws ControllerException
	 */
	@RequestMapping(value = "getOutdept", method = RequestMethod.GET)
	@ResponseBody
	public String getOutdept(String agreementid, String providerid) throws ControllerException {
		Map<String, Object> Query_agreementdept = new HashMap<String, Object>();
		List<Object> Listdeptname = agreementdeptService.getdeptname(agreementid, providerid);
		if (!"[null]".equals(Listdeptname.toString())) {
			Query_agreementdept.put("rows", Listdeptname);
		} else {
			Query_agreementdept.put("rows", new ArrayList<Object>());
		}
		// Query_agreementdept.put("page",1);
		// Query_agreementdept.put("total", 1);
		// Query_agreementdept.put("records", 10000);
		// System.out.println(JSONObject.toJSONString(Query_agreementdept));
		return JSONObject.toJSONString(Query_agreementdept);
	}

	@RequestMapping(value = "getBaseInfo", method = RequestMethod.GET)
	@ResponseBody
	public String getBaseInfo(@RequestParam String channelid) throws ControllerException {
		INSBChannel insbChannel = new INSBChannel();
		insbChannel.setId(channelid);
		return JSONObject.toJSONString(service.getChannelAgreementInfo(channelid));
	}

	@RequestMapping(value = "saveBaseInfo", method = RequestMethod.POST)
	@ResponseBody
	public String saveBaseInfo(BaseInfoVo baseInfoVo) throws ControllerException {
		try {
			return insbChannelagreementService.saveBaseInfo(baseInfoVo);
		} catch (Exception e) {
			return "fail";
		}
	}

	@RequestMapping(value = "getInterfaceInfo", method = RequestMethod.POST)
	@ResponseBody
	public String getInterfaceInfo(String agreementid, InterfaceVo interfaceVo,
			@RequestParam(value = "offset", required = false) Integer offset,
			@RequestParam(value = "limit", required = false) Integer limit) throws ControllerException {
		if (StringUtil.isEmpty(interfaceVo.getChannelinnercode())) {
			return null;
		}

		Map<String, Object> map = new HashMap<String, Object>();
		if (ModelUtil.isVoluation(offset, limit)) {
			offset = ConstUtil.OFFSET;
			limit = ConstUtil.LIMIT;
		}
		List<InterfaceVo> list = insbChannelagreementService.getInterfaceInfo(agreementid, interfaceVo, offset, limit);
		map.put("rows", list);
		return JSONObject.toJSONString(map);
	}

	@RequestMapping(value = "saveInterfaces", method = RequestMethod.POST)
	@ResponseBody
	public String saveInterfaces(String agreementid, InterfaceVo interfaceVo) throws ControllerException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "success";
		try {
			InterfaceVo resultVo = insbAgreementinterfaceService.saveInterfaces(agreementid, interfaceVo);
			resultMap.put("agreementinterfaceid", resultVo.getAgreementinterfaceid());
		} catch (Exception e) {
			e.printStackTrace();
			result = "fail";
		}
		resultMap.put("flag", result);
		return JSONObject.toJSONString(resultMap);
	}

	@RequestMapping(value = "getDeptPayType", method = RequestMethod.POST)
	@ResponseBody
	public String getDeptPayType(String providerid, String deptid, String agreeid) throws ControllerException {
		Map<String, Object> map = new HashMap<String, Object>();
		List<DeptPayTypeVo> list = insbAgreementpaymethodService.getDeptPaymethod(deptid, agreeid, providerid);
		// List<DeptPayTypeVo> list =
		// insbPaychannelmanagerService.getDeptPayType(providerid, deptid,
		// agreeid);
		map.put("rows", list);
		return JSONObject.toJSONString(map);
	}

	/**
	 * 批量开启
	 * 
	 * @param checkfag
	 * @param interfaceJson
	 * @param agreementid
	 * @return
	 */
	@RequestMapping(value = "bashSaveInterfaces", method = RequestMethod.POST)
	@ResponseBody
	public String bashSaveInterfaces(String channelinnercode, String interfaceJson) {
		List<InterfaceVo> interfaces = JSONArray.parseArray(interfaceJson, InterfaceVo.class);
		List<InterfaceVo> tmpInterfaces = new ArrayList<>();

		for (InterfaceVo tmpinface : interfaces) {
		//	if(tmpinface.getCheck().equals("1")) return "fail";
			tmpinface.setChannelinnercode(channelinnercode);

			tmpinface.setCheck("1");
			tmpinface.setIsfree("0");
			tmpinface.setExtendspattern("1");

			// 从数据库中查找到agreementinterfaceid

			tmpInterfaces.add(tmpinface);

		}
		String result = "success";
		try {

			insbAgreementinterfaceService.bashSaveInterface(tmpInterfaces);

			return result;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result = "fail";
			return result;
		}

	}

	@RequestMapping(value = "bashCloseInterfaces", method = RequestMethod.POST)
	@ResponseBody
	public String bashCloseInterfaces(String channelinnercode, String interfaceJson) {
		List<InterfaceVo> interfaces = JSONArray.parseArray(interfaceJson, InterfaceVo.class);
		List<InterfaceVo> tmpInterfaces = new ArrayList<>();
		
		for (InterfaceVo tmpinface : interfaces) {
			
		//	if(tmpinface.getCheck().equals("0")) return "fail";
			tmpinface.setChannelinnercode(channelinnercode);

			tmpinface.setCheck("0");
//			Map<String, Object> tmpMap = new HashMap<String, Object>();
//			tmpMap.put("channelinnercode", channelinnercode);
//			tmpMap.put("interfaceid", tmpinface.getInterfaceid());
//			String agreementinterfaceid = insbagreementInterfaceDao.getIdByChannelinnercode(tmpMap);
//			tmpinface.setAgreementinterfaceid(agreementinterfaceid);

			// 从数据库中查找到agreementinterfaceid

			tmpInterfaces.add(tmpinface);

		}
		String result = "success";
		try {

			insbAgreementinterfaceService.bashSaveInterface(tmpInterfaces);

			return result;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result = "fail";
			return result;
		}

	}

	@RequestMapping(value = "savePayType", method = RequestMethod.POST)
	@ResponseBody
	public String savePayType(String flag, String payTypeJson, String providerid, String agreementid)
			throws ControllerException {
		List<DeptPayTypeVo> payTypeList = JSONArray.parseArray(payTypeJson, DeptPayTypeVo.class);
		return insbPaychannelmanagerService.savePayType(payTypeList, flag, providerid, agreementid);
	}

	@RequestMapping(value = "getDistribution", method = RequestMethod.POST)
	@ResponseBody
	public String getDistribution(String deptid, String providerid, String agreementid) throws ControllerException {
		DistributionVo distributionVo = insbDistributionService.getDistribution(agreementid, providerid, deptid);
		return JSONObject.toJSONString(distributionVo);
	}

	@RequestMapping(value = "saveDistribution", method = RequestMethod.POST)
	@ResponseBody
	public String saveDistribution(DistributionVo distributionVo) throws ControllerException {
		try {
			insbDistributionService.saveDistribution(distributionVo);
		} catch (Exception e) {
			return "fail";
		}
		return "success";
	}

	/*
	 * 修改出单网店信息(修改前查询所有网点)
	 */
	@RequestMapping(value = "getAllDeptName", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getAllDeptName(String agreementid, String providerid, String soulagreeid)
			throws ControllerException {
		Map<String, Object> parma = null;
		Map<String, Object> result = new HashMap<String, Object>();
		INSBOutorderdept insbOutorderdept = new INSBOutorderdept();
		insbOutorderdept.setAgreementid(soulagreeid);
		List<INSBOutorderdept> allDeptlist = outorderdeptService.queryList(insbOutorderdept);
		List<Object> deptname = new ArrayList<Object>();
		// 翻译网点名称
		for (INSBOutorderdept outorderdept : allDeptlist) {
			parma = new HashMap<String, Object>();
			INSCDept Query_dept = deptservice.queryById(outorderdept.getDeptid5());
			parma.put("deptid", outorderdept.getDeptid5());
			parma.put("comname", Query_dept.getComname());
			deptname.add(parma);
		}
		List<Object> choosedeptname = agreementdeptService.getdeptname(agreementid, providerid);
		result.put("all", deptname);
		if (!"[null]".equals(choosedeptname.toString())) {
			// choosedeptname!=null&& !choosedeptname.isEmpty()
			result.put("chosed", choosedeptname);
		} else {
			result.put("chosed", choosedeptname.add(new HashMap<String, Object>()));
		}
		return result;
	}

	/**
	 * @param agreementid
	 * @param dept
	 * @修改出单网点
	 */
	@RequestMapping(value = "saveAgreementdept", method = RequestMethod.POST)
	@ResponseBody
	public String savedept(HttpSession session, String agreementid, String dept, String deptid5, String prvId,
			String chnagreementid) {
		if (StringUtil.isEmpty(chnagreementid) || StringUtil.isEmpty(prvId)) {
			return "fail";
		}
		Date newDate = new Date();
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		INSCDept Query_dept = deptservice.queryById(dept);
		String[] parentcodes = Query_dept.getParentcodes().toString().split("[+]");
		INSBAgreementdept agreementdept = new INSBAgreementdept();
		agreementdept.setAgreementid(chnagreementid);
		agreementdept.setProviderid(prvId);
		// agreementdept.setDeptid5(deptid5);
		List<INSBAgreementdept> List_agreementdept = agreementdeptService.queryList(agreementdept);
		for (INSBAgreementdept insbAgreementdept : List_agreementdept) {
			agreementdeptService.deleteById(insbAgreementdept.getId());
		}
		INSBAgreementdept INagreementdept = new INSBAgreementdept();
		INagreementdept.setDeptid1(parentcodes[2]);
		INagreementdept.setDeptid2(parentcodes[3]);
		INagreementdept.setDeptid3(parentcodes[4]);
		INagreementdept.setDeptid4(parentcodes[5]);
		INagreementdept.setDeptid5(dept);
		INagreementdept.setAgreementid(chnagreementid);
		INagreementdept.setProviderid(prvId);
		INagreementdept.setOperator(operator.getName());
		String uuid = UUIDUtils.random();
		INagreementdept.setId(uuid);
		INagreementdept.setCreatetime(newDate);
		INagreementdept.setModifytime(newDate);
		agreementdeptService.insert(INagreementdept);

		/*
		 * INSBAgreementpaymethod insbAgreementpaymethod = new
		 * INSBAgreementpaymethod();
		 * insbAgreementpaymethod.setAgreementid(chnagreementid);
		 * insbAgreementpaymethod.setProviderid(prvId);
		 * insbAgreementpaymethod.setDeptid(deptid5);
		 * List<INSBAgreementpaymethod> insbAgreementpaymethods =
		 * insbAgreementpaymethodService.queryList(insbAgreementpaymethod); if
		 * (insbAgreementpaymethods != null && insbAgreementpaymethods.size() >
		 * 0) { for (INSBAgreementpaymethod itMethod : insbAgreementpaymethods)
		 * { itMethod.setDeptid(dept);
		 * insbAgreementpaymethodService.updateById(itMethod); } }
		 */

		return "success";
	}

	@RequestMapping(value = "getInterfaceCharge", method = RequestMethod.POST)
	@ResponseBody
	public String getInterfaceCharge(String agreementinterfaceid) throws ControllerException {
		// if (StringUtil.isEmpty(agreementinterfaceid)) {
		// return null;
		// }
		INSBChnchargerule insbChnchargerule = new INSBChnchargerule();
		insbChnchargerule.setAgreementinterfaceid(agreementinterfaceid);
		List<INSBChnchargerule> insbChnchargerules = insbChnchargeruleService.queryList(insbChnchargerule);

		Map<String, Object> map = new HashMap<String, Object>();
		if (insbChnchargerules != null && insbChnchargerules.size() > 0) {
			for (INSBChnchargerule itRule : insbChnchargerules) {
				String conver = itRule.getConverstartcompar() + " " + itRule.getConverstart() + "%  并且  "
						+ itRule.getConverendcompar() + " " + itRule.getConverend() + "%，收费 " + itRule.getCharge()
						+ " 元/笔";
				itRule.setConver(conver);
			}
		}

		map.put("rows", insbChnchargerules);
		return JSONObject.toJSONString(map);
	}

	@RequestMapping(value = "delInterfaceCharge", method = RequestMethod.POST)
	@ResponseBody
	public String delInterfaceCharge(String id) throws ControllerException {
		if (StringUtil.isEmpty(id)) {
			return "fail";
		}
		INSBChnchargerule insbChnchargerule = new INSBChnchargerule();
		insbChnchargerule.setId(id);
		insbChnchargeruleService.delete(insbChnchargerule);
		return "success";
	}

	@RequestMapping(value = "saveInterfaceCharge", method = RequestMethod.POST)
	@ResponseBody
	public String saveInterfaceCharge(String agreeinterfaceid, INSBChnchargerule insbChnchargerule)
			throws ControllerException {
		insbChnchargerule.setAgreementinterfaceid(agreeinterfaceid);
		insbChnchargerule.setCreatetime(new Date());
		insbChnchargeruleService.insert(insbChnchargerule);
		return "success";
	}
	
	//批量导入供应商--.xlsx文件
	@RequestMapping(value = "importPrv", method = RequestMethod.POST)
	@ResponseBody
	public String importPrv(@RequestParam("prvFile") MultipartFile uploadfile,
			HttpServletRequest request, String agreementid, String channelid) throws ControllerException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		File newFile = null;
		try {
			if (StringUtil.isEmpty(agreementid) || StringUtil.isEmpty(channelid)) {
				throw new Exception("agreementid和channelid必传");
			}
			 
			resultMap.put("status", "0"); //success
			String path = request.getSession().getServletContext().getRealPath("/") + "/static/upload/channelprv/";
			File pathFile = new File(path);
			if (!pathFile.exists()) {
				pathFile.mkdirs();
			}
			LogUtil.info("文件类型：" + uploadfile.getContentType());
			LogUtil.info("文件名称：" + uploadfile.getOriginalFilename());
			LogUtil.info("文件大小:" + uploadfile.getSize());
			
			String strNewFile = path + UUIDUtils.random() + uploadfile.getOriginalFilename();
			newFile = new File(strNewFile);
		
			uploadfile.transferTo(newFile); //将文件copy上传到服务器指定位置
			
			ExcelUtil eu = new ExcelUtil();  
	        eu.setExcelPath(strNewFile);  
	        List<Row> rows = eu.readExcel();       
	        
	        if (rows == null || rows.size() <= 1) {
	        	resultMap.put("status", "1"); //文件没有数据
	        	return JSONObject.toJSONString(resultMap);
	        }
	        Row firstRow = rows.get(0);
	        if (firstRow == null || "".equals(firstRow)) {
	        	resultMap.put("status", "2"); //文件第一行有错，请下载模板文件
	        	return JSONObject.toJSONString(resultMap);
	        }
	        if ( !"供应商id".equals(firstRow.getCell(0).toString().trim()) || 
	        	 !"协议编码".equals(firstRow.getCell(1).toString().trim()) || 
	        	 !"出单网点".equals(firstRow.getCell(2).toString().trim()) || 
	        	 !"支付方式（英文;号隔开）".equals(firstRow.getCell(3).toString().trim()) ) {
	        	resultMap.put("status", "2"); //文件第一行有错，请下载模板文件
	        	return JSONObject.toJSONString(resultMap);
	        }
	        
	        INSCUser operator = (INSCUser)request.getSession().getAttribute("insc_user");
	        String errorPrvIds = insbAgreementproviderService.procImportPrvData(agreementid, rows, operator, channelid); 
	        resultMap.put("errorPrvIds", errorPrvIds);
	        return JSONObject.toJSONString(resultMap);
		} catch (Exception e) {
			LogUtil.error("批量导入供应商异常：" + e.getMessage());
			e.printStackTrace();
			resultMap.put("status", "3"); //系统异常
	        return JSONObject.toJSONString(resultMap);
		} finally {
			if (newFile != null) {
				newFile.delete();
			}
		}
	}
	
	//下载文件：渠道供应商导入模板.xlsx
	@RequestMapping("dlPrvTempFile")    
    public ResponseEntity<byte[]> dlPrvTempFile(HttpServletRequest request) throws Exception {    
        String path = request.getSession().getServletContext().getRealPath("/") + "/static/download/渠道供应商导入模板.xlsx";  
        File file = new File(path);  
        HttpHeaders headers = new HttpHeaders();    
        String fileName = new String("渠道供应商导入模板.xlsx".getBytes("UTF-8"), "iso-8859-1"); //为了解决中文名称乱码问题  
        headers.setContentDispositionFormData("attachment", fileName);   
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);   
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);    
    }

	@RequestMapping(value = "batchCopyChannelProvider", method = RequestMethod.POST)
	@ResponseBody
	public String batchCopyChannelProvider(HttpSession session,String fromChannelId,String toChannelIds){
		INSCUser user = (INSCUser) session.getAttribute("insc_user");
		String userName = user.getName();
		String result = "";
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if (StringUtil.isEmpty(fromChannelId) || StringUtil.isEmpty(toChannelIds)) {
				throw new Exception("fromChannelId和toChannelIds必传");
			}
			result = insbAgreementproviderService.batchCopyChannelProvider(fromChannelId, toChannelIds,userName);
		}catch (Exception e){
			LogUtil.error("batchCopyChannelProvider异常："+e.getMessage());
			e.printStackTrace();
			resultMap.put("msg","batchCopyChannelProvider异常："+e.getMessage());
			resultMap.put("status","0");
			result = JsonUtils.serialize(resultMap);
		}
		return result;
	}



}
