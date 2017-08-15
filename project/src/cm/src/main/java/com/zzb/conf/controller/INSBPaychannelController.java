package com.zzb.conf.controller;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.cninsure.core.utils.LogUtil;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
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
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCCodeService;
import com.cninsure.system.service.INSCDeptService;
import com.common.PagingParams;
import com.zzb.conf.dao.INSBAgreementDao;
import com.zzb.conf.dao.INSBPaychannelDao;
import com.zzb.conf.dao.INSBPaychannelmanagerDao;
import com.zzb.conf.dao.INSBProviderDao;
import com.zzb.conf.entity.INSBAgreement;
import com.zzb.conf.entity.INSBBankcardconf;
import com.zzb.conf.entity.INSBOperatingsystem;
import com.zzb.conf.entity.INSBPaychannel;
import com.zzb.conf.entity.INSBPaychannelmanager;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.service.INSBBankcardconfService;
import com.zzb.conf.service.INSBOperatingsystemService;
import com.zzb.conf.service.INSBOutorderdeptService;
import com.zzb.conf.service.INSBPaychannelService;
import com.zzb.conf.service.INSBPaychannelmanagerService;
import com.zzb.conf.service.INSBProviderService;
import com.zzb.model.BankCardConfModel;

@Controller
@RequestMapping("/paychannel/*")
public class INSBPaychannelController extends BaseController{
	@Resource
	private INSBPaychannelService insbPaychannelService;
	@Resource
	private INSCCodeService inscCodeService;
	@Resource
	private INSBOperatingsystemService insbOperatingsystemService;
	@Resource
	private INSBBankcardconfService insbBankcardconfService;
	@Resource
	private INSBPaychannelmanagerService insbpaychannelmanagerService;
	@Resource
	private INSCDeptService inscDeptService;
	@Resource
	private INSBProviderService insbProviderService;
	@Resource
	private INSBAgreementDao agreementDao;
	@Resource
	private INSBOutorderdeptService outorderdeptService;
	@Resource
	private INSCDeptDao deptDao;
	@Resource
	private INSBProviderDao providerDao;

	/**
	 * 跳转到列表页面
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list() throws ControllerException{
		ModelAndView mav = new ModelAndView("zzbconf/paychannellist");
		return mav;
	}
	/**
	 * 支付通道列表
	 * @param para
	 * @param paychannel
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "initpaychannellist", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> initPayChannelList(@ModelAttribute PagingParams para, @ModelAttribute INSBPaychannel paychannel) throws ControllerException{
		Map<String, Object> map = BeanUtils.toMap(paychannel,para);
		return insbPaychannelService.initPayChannelList(map);
	}

	/**
	 * 支付方式 和机构、供应商关联列表
	 * @param para
	 * @param paychannel
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "initpaywaylist", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> initPayWayList(HttpSession session,@RequestParam("id") String id,@ModelAttribute PagingParams para, @ModelAttribute INSBPaychannelmanager paychannelmanager) throws ControllerException{
		paychannelmanager.setPaychannelid(id);
		Map<String, Object> map = BeanUtils.toMap(paychannelmanager,para);
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		if (operator== null || StringUtils.isEmpty(operator.getUserorganization())) {
			return new HashMap<String, Object>();
		}
		map.put("userDept", operator.getUserorganization());
		return insbPaychannelService.initPayWayList(map,id);
	}

	/**
	 * 支付方式配置-新增(弹窗跳转)
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/addpcm", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView addpcm(HttpSession session,@RequestParam("paychannelmageid") String id)throws ControllerException {
		ModelAndView mv = new ModelAndView("zzbconf/paychanneleditmodel");
		INSBPaychannelmanager paychannelmanager = new INSBPaychannelmanager();
		paychannelmanager.setPaychannelid(id);
		paychannelmanager.setId(null);
		mv.addObject("paychannelmanager", paychannelmanager);
		mv.addObject("collectiontypes",inscCodeService.queryINSCCodeByCode("tradingobject3","tradingobject3"));
		return mv;
	}

	/**
	 * 根据平台查询协议
	 * @param pladeptid
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/getpladept", method = RequestMethod.POST)
	@ResponseBody
	public List<INSBAgreement> getpladept(String  pladeptid)throws ControllerException {
		List<INSBAgreement> agreementlist = new ArrayList<INSBAgreement>();
		if(StringUtils.isEmpty(pladeptid)) {
			return agreementlist;
		}
		INSCDept dept = inscDeptService.queryById(pladeptid);
		if("5".equals(dept.getTreelevel())) {//网点协议
			Map<String,Object> query = new HashMap<String,Object>();
			query.put("deptid5", pladeptid);
			agreementlist = agreementDao.selectByOutorderdept(query);
		} else {//平台协议
			Map<String,String> p1 = new HashMap<String,String>();
			p1.put("id", pladeptid);
			p1.put("comtype", "02");

			//拿到当前登陆人所有子机构
			List<String>  deptIdList =  inscDeptService.selectWDidsByPatId(p1);

//			INSBAgreement aget = new INSBAgreement();
//			aget.setDeptid(pladeptid);
			deptIdList.add(pladeptid);

			if(deptIdList.isEmpty()){
//				agreementlist = agreementDao.selectList(aget);
				agreementlist = agreementDao.selectAllSubAgreement(deptIdList);
			}else{
//				deptIdList.add(pladeptid);
				agreementlist = agreementDao.selectAllSubAgreement(deptIdList);

			}
		}

		//汉字排序
		Comparator<INSBAgreement> cmp = new Comparator<INSBAgreement>() {
			Comparator<Object> cmp = Collator.getInstance(java.util.Locale.CHINA);
			@Override
			public int compare(INSBAgreement o1, INSBAgreement o2) {
				if(o1 == null || o2 == null || o1.getAgreementname() == null || o2.getAgreementname() == null) {
					return 0;
				}
				if(cmp.compare(o1.getAgreementname(), o2.getAgreementname())>0){
					return 1;
				}else if(cmp.compare(o1.getAgreementname(), o2.getAgreementname())<0){
					return -1;
				}
				return 0;
			}
		};
		Collections.sort(agreementlist, cmp);
		return agreementlist;
	}
	/**
	 * 支付方式配置-删除
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/deletepcmbyid", method = RequestMethod.GET)
	@ResponseBody
	public String deletepcmbyid(HttpSession session, @RequestParam("paychannelmageid") String id)throws ControllerException {
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("支付方式配置删除id为%s,操作人:%s", id, operator.getUsercode());
		int count = insbpaychannelmanagerService.deleteById(id);
		JSONObject jsonObject = new JSONObject();
		jsonObject.accumulate("count", count);
		return jsonObject.toString();
	}


	/**
	 * 协议网点下拉框
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/getwangdianbyagreement", method = RequestMethod.POST)
	@ResponseBody
	public List<INSCDept> getwangdianbyagreement(String  agreementid)throws ControllerException {

		List<INSCDept> result = new ArrayList<INSCDept>();
		List<String> deptList = outorderdeptService.getDeptByAgreementId(agreementid);
		for(String deptId:deptList){
			if (StringUtils.isEmpty(deptId)) {
				continue;
			}
			INSCDept model = inscDeptService.getOrgDeptByDeptCode(deptId);
			result.add(model);
		}
		return result;
	}

	/**
	 * 支付方式(弹窗显示详情)
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/initpcm", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView initpcm(HttpSession session,@RequestParam("paychannelmageid") String id)throws ControllerException {
		ModelAndView mv = new ModelAndView("zzbconf/paychanneleditmodel");
		INSBPaychannelmanager paychannelmanager = insbpaychannelmanagerService.queryById(id);

		String userdept = ((INSCUser)session.getAttribute("insc_user")).getUserorganization();

		Map<String,String> p1 = new HashMap<String,String>();
		p1.put("id", userdept);
		p1.put("comtype", "02");

		//拿到当前登陆人所有子机构
		List<String>  deptIdList =  inscDeptService.selectWDidsByPatId(p1);

		INSBAgreement aget = new INSBAgreement();
		aget.setDeptid(userdept);
		List<INSBAgreement> agreementlist = new ArrayList<INSBAgreement>();
		if(deptIdList.isEmpty()){
			agreementlist = agreementDao.selectList(aget);
		}else{
			deptIdList.add(userdept);
			agreementlist = agreementDao.selectAllSubAgreement(deptIdList);

		}
		if (paychannelmanager!=null) {
            if (StringUtil.isNotEmpty(paychannelmanager.getDeptid())) {
                INSCDept dept = inscDeptService.queryById(paychannelmanager.getDeptid());
                mv.addObject("dept", dept);
            }
            if (StringUtil.isNotEmpty(paychannelmanager.getProviderid())) {
                INSBProvider provider = insbProviderService.queryById(paychannelmanager.getProviderid());
                mv.addObject("provider", provider);
            }
			mv.addObject("paychannelmanager", paychannelmanager);
		}
		mv.addObject("agreementlist", agreementlist);
		mv.addObject("collectiontypes",inscCodeService.queryINSCCodeByCode("tradingobject3","tradingobject3"));
		return mv;
	}


	/**
	 * 支付方式配置修改保存+新增 (弹窗保存)
	 *
	 * @param
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/savepcm", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView savepcm(HttpSession session,@ModelAttribute INSBPaychannelmanager paychlmge)throws ControllerException {
		INSCUser user = (INSCUser) session.getAttribute("insc_user");
		List<String> agreementIds = paychlmge.getAgreementids();

		//选择协议不为空时
        if (agreementIds != null && !agreementIds.isEmpty()) {
            for (String agreementId : agreementIds) {
                if (agreementId != null && !agreementId.equals("")) {
                    //得到协议的供应商
                    INSBAgreement agreementModel = agreementDao.selectById(agreementId);

                    String providerId = agreementModel.getProviderid();
                    List<INSCDept> depts = this.getwangdianbyagreement(agreementId);
					if (depts != null && !depts.isEmpty()) {
						Map<String, String> paramMap = new HashMap<String, String>();
						paramMap.put("providerid", providerId);
						paramMap.put("deptid", paychlmge.getDeptid());
						paramMap.put("channelid", paychlmge.getPaychannelid());

						//先删除
						insbpaychannelmanagerService.delteDataBylogicId(paramMap);

						//paychlmge.setDeptid(dept.getComcode());
						paychlmge.setPaychannelid(paychlmge.getPaychannelid());
						paychlmge.setProviderid(providerId);
						paychlmge.setCreatetime(new Date());
						paychlmge.setOperator(user.getUsercode());
						paychlmge.setAgreementid(agreementId);
						paychlmge.setId(null);

						insbpaychannelmanagerService.insert(paychlmge);
					}
                }
            }
        } else {
            INSBPaychannelmanager temp = insbpaychannelmanagerService.queryById(paychlmge.getId());
            if (temp != null) {
                temp.setModifytime(new Date());
                temp.setOperator(user.getUsercode());
                insbpaychannelmanagerService.copySubmitData(paychlmge, temp);
                insbpaychannelmanagerService.updateById(temp);
            }
        }

		ModelAndView mv = new ModelAndView("redirect:paychanneledit?id="+paychlmge.getPaychannelid());
		return mv;
	}


	/**
	 * 保存并跳转到支付通道列表
	 * @param session
	 * @param bankcardmodel
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView  saveorupdate(HttpSession session,@ModelAttribute BankCardConfModel bankcardmodel)throws ControllerException{
		ModelAndView model = new ModelAndView("zzbconf/paychannellist");
		String operator = ((INSCUser)session.getAttribute("insc_user")).getUsername();
		Date date = new Date();
		INSBPaychannel paybean = new INSBPaychannel();//支付通道
//		INSBOperatingsystem operatingbean = new INSBOperatingsystem();//系统配置
		INSBBankcardconf bankcardbean = new INSBBankcardconf();//银行卡配置表
		if(!StringUtil.isEmpty(bankcardmodel.getPaychannelid())){
//			基本信息
			paybean.setId(bankcardmodel.getPaychannelid());
			paybean.setPaychannelname(bankcardmodel.getPaychannelname());
			paybean.setStateflag(bankcardmodel.getStateflag());
			paybean.setPaytype(bankcardmodel.getPaytype());
			paybean.setSupportterminal(bankcardmodel.getSupportterminal());
			paybean.setPaychannellimit(bankcardmodel.getPaychannellimit());
			paybean.setSort(bankcardmodel.getSort());
			paybean.setClientpayflag(bankcardmodel.getClientpayflag());
			paybean.setChanneldescribe(bankcardmodel.getChanneldescribe());
			paybean.setCostratedescribe(bankcardmodel.getCostratedescribe());
			paybean.setShowpoundageflag(bankcardmodel.getShowpoundageflag());
			paybean.setChargepoundagefalg(bankcardmodel.getChargepoundagefalg());
			paybean.setRatio(bankcardmodel.getRatio());
			paybean.setPoundageflag(bankcardmodel.getPoundageflag());
			paybean.setPoundageratio(bankcardmodel.getPoundageratio());
			paybean.setFixedfee(bankcardmodel.getFixedfee());
			paybean.setCreditpoundageratio(bankcardmodel.getCreditpoundageratio());
			paybean.setCreditfixedfee(bankcardmodel.getCreditfixedfee());
			paybean.setCashpoundageratio(bankcardmodel.getCashpoundageratio());
			paybean.setCashfixedfee(bankcardmodel.getCashfixedfee());
			paybean.setOperator(operator);
			paybean.setModifytime(date);
			paybean.setClienttypes(joinClientTypes(bankcardmodel));
			paybean.setOnlyedionlineunderwriting(bankcardmodel.isOnlyedionlineunderwriting());

			insbPaychannelService.updateById(paybean);
//			操作系统配置
//			String ios [] = bankcardmodel.getIossystem();
//			String iosid [] = insbOperatingsystemService.queryTypeId(bankcardmodel.getPaychannelid(),"01").split(",");
//			publicupdate(ios,iosid,operatingbean,operator,date);
//			String android [] = bankcardmodel.getAndroidsystem();
//			String androidid [] = insbOperatingsystemService.queryTypeId(bankcardmodel.getPaychannelid(),"02").split(",");
//			publicupdate(android,androidid,operatingbean,operator,date);
//			String winphone [] = bankcardmodel.getWinphonesystem();
//			String winphoneid [] = insbOperatingsystemService.queryTypeId(bankcardmodel.getPaychannelid(),"03").split(",");
//			publicupdate(winphone,winphoneid,operatingbean,operator,date);
//			String win [] = bankcardmodel.getWinsystem();
//			String winid [] = insbOperatingsystemService.queryTypeId(bankcardmodel.getPaychannelid(),"04").split(",");
//			publicupdate(win,winid,operatingbean,operator,date);
//			String os [] = bankcardmodel.getOssystem();
//			String osid [] = insbOperatingsystemService.queryTypeId(bankcardmodel.getPaychannelid(),"05").split(",");
//			publicupdate(os,osid,operatingbean,operator,date);
//			银行卡配置
			List<INSBBankcardconf>  bankcardconflist = bankcardmodel.getBankcardconflist();
			for(INSBBankcardconf list :bankcardconflist){
				if(StringUtil.isEmpty(list.getId())){
					bankcardbean.setId(UUIDUtils.random());
					bankcardbean.setInsbcodevalue(list.getInsbcodevalue());
					bankcardbean.setPaychannelid(bankcardmodel.getPaychannelid());
					bankcardbean.setCommonlyusedbankflag(list.getCommonlyusedbankflag());
					bankcardbean.setCashcardflag(list.getCashcardflag());
					bankcardbean.setCreditcardflag(list.getCreditcardflag());
					bankcardbean.setCashlimitfalg(list.getCashlimitfalg());
					bankcardbean.setCashlimit(list.getCashlimit());
					bankcardbean.setCashlimitdayfalg(list.getCashlimitdayfalg());
					bankcardbean.setCashdaylimit(list.getCashdaylimit());
					bankcardbean.setCreditlimitfalg(list.getCreditlimitfalg());
					bankcardbean.setCreditlimit(list.getCreditlimit());
					bankcardbean.setCreditlimitdayfalg(list.getCreditlimitdayfalg());
					bankcardbean.setCreditdaylimit(list.getCreditdaylimit());
					bankcardbean.setOperator(operator);
					bankcardbean.setCreatetime(date);
					bankcardbean.setModifytime(date);
					insbBankcardconfService.addBankCardConf(bankcardbean);
				}else{
					bankcardbean.setId(list.getId());
					bankcardbean.setInsbcodevalue(list.getInsbcodevalue());
					bankcardbean.setPaychannelid(bankcardmodel.getPaychannelid());
					bankcardbean.setCommonlyusedbankflag(list.getCommonlyusedbankflag());
					bankcardbean.setCashcardflag(list.getCashcardflag());
					bankcardbean.setCreditcardflag(list.getCreditcardflag());
					bankcardbean.setCashlimitfalg(list.getCashlimitfalg());
					bankcardbean.setCashlimit(list.getCashlimit());
					bankcardbean.setCashlimitdayfalg(list.getCashlimitdayfalg());
					bankcardbean.setCashdaylimit(list.getCashdaylimit());
					bankcardbean.setCreditlimitfalg(list.getCreditlimitfalg());
					bankcardbean.setCreditlimit(list.getCreditlimit());
					bankcardbean.setCreditlimitdayfalg(list.getCreditlimitdayfalg());
					bankcardbean.setCreditdaylimit(list.getCreditdaylimit());
					bankcardbean.setOperator(operator);
					bankcardbean.setModifytime(date);
					insbBankcardconfService.updateById(bankcardbean);
				}
			}
		}
		return model;
	}

	private String joinClientTypes(BankCardConfModel bankcardmodel) {
		if (bankcardmodel.getClienttypes() == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder();

        for (String s : bankcardmodel.getClienttypes()) {
            sb.append(s);
            sb.append(",");
        }
        return sb.substring(0, sb.length()-1);
	}

	/**
	 * 查看并修改支付通道信息
	 * @param id
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "paychanneledit", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView  bankCardedit( @RequestParam("id") String id)throws ControllerException{
		ModelAndView model = new ModelAndView("zzbconf/paychanneledit");
		INSBPaychannel paychannel = insbPaychannelService.queryById(id);
		model.addObject("paychannel", paychannel);//通道主表信息
//		List<INSCCode> systemios = inscCodeService.queryINSCCodeByCode("systemtype", "ios");//ios
//		List<INSCCode> systemandroid = inscCodeService.queryINSCCodeByCode("systemtype", "android");//android
//		List<INSCCode> systemwinphone = inscCodeService.queryINSCCodeByCode("systemtype", "winphone");//windowsphone
//		List<INSCCode> systemwindows = inscCodeService.queryINSCCodeByCode("systemtype", "windows");//windows
//		List<INSCCode> systemos = inscCodeService.queryINSCCodeByCode("systemtype", "os");//os
//		model.addObject("systemios",systemios);//ios
//		model.addObject("systemandroid",systemandroid);//android
//		model.addObject("systemwinphone",systemwinphone);//windowsphone
//		model.addObject("systemwindows",systemwindows);//windows
//		model.addObject("systemos",systemos);//os
//		model.addObject("ioscheck",insbOperatingsystemService.queryOperatingSystemlist(id,"01"));//ios
//		model.addObject("androidcheck",insbOperatingsystemService.queryOperatingSystemlist(id,"02"));//android
//		model.addObject("winphonecheck",insbOperatingsystemService.queryOperatingSystemlist(id,"03"));//winphone
//		model.addObject("windowscheck",insbOperatingsystemService.queryOperatingSystemlist(id,"04"));//windows
//		model.addObject("oscheck",insbOperatingsystemService.queryOperatingSystemlist(id,"05"));//os
		model.addObject("paytype",inscCodeService.queryINSCCodeByCode("paytype","paytype"));
		model.addObject("bankcardlist",inscCodeService.queryINSCCodeByCode("bankcard","bankcard"));
		model.addObject("bankconfinfo",insbBankcardconfService.queryBankcardConfInfo(id));

		// TODO 增加支持的客户端集合

		return model;
	}
	@RequestMapping(value = "delbankcardconfbyid", method = RequestMethod.GET)
	@ResponseBody
	public String delBankcardconfByid(HttpSession session, String cardconfids)throws ControllerException{
//		System.out.println(cardconfids);
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("银行卡信息删除id为%s,操作人:%s", cardconfids, operator.getUsercode());
		int count = 0;
		if(!cardconfids.isEmpty()){
			String [] ids = cardconfids.split(",");
			for (int i = 0; i < ids.length; i++) {
				insbBankcardconfService.deleteById(ids[i]);
				count++;
			}
		}
		return JSONObject.fromObject("{msg:"+String.valueOf(count)+"}").toString();
	}

	public void publicupdate(String [] type,String [] id,INSBOperatingsystem operatingbean,String operator,Date date){
		for(int i = 0;i<id.length ;i++){
			operatingbean.setId(id[i]);
			if(type!=null&&i<type.length){
				operatingbean.setInsbcodevalue(type[i]);
			}else{
				operatingbean.setInsbcodevalue("");
			}
			operatingbean.setOperator(operator);
			operatingbean.setModifytime(date);
			insbOperatingsystemService.updateById(operatingbean);
		}
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

		List<Map<String,String>> resultinstitutionList = inscDeptService.queryTreeList4Data2(parentcode,operator.getUserorganization());
		return resultinstitutionList;
	}

	/**
	 * 查询
	 * @param para
	 * @param deptid
	 * @param providerid
	 * @param paychannelid
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "querypcm",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> querypcm(HttpSession session,@ModelAttribute PagingParams para,
			@RequestParam(value="deptid",required=false) String deptid,
			@RequestParam(value="providerid",required=false) String providerid,
			@RequestParam(value="paychannelid",required=false) String paychannelid)throws ControllerException{
		Map<String, Object> resultmap = new HashMap<String,Object>();
		List<Map<Object, Object>> infoList = new ArrayList<Map<Object, Object>>();

		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		if (operator== null || StringUtils.isEmpty(operator.getUserorganization())) {
			return resultmap;
		}

		if(null != deptid && !"".equals(deptid) || null != providerid && !"".equals(providerid)){
			if("".equals(deptid)){
				deptid = null;
			}
			if("".equals(providerid)){
				providerid = null;
			}
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("paychannelid", paychannelid);
			param.put("providerid", providerid);
			param.put("offset", para.getOffset());
			param.put("limit", para.getLimit());
			param.put("userDept", operator.getUserorganization());
			if(deptid!=null&&!"".equals(deptid)){
				INSCDept dept = inscDeptService.queryById(deptid);
				if(dept!=null){
					if("05".equals(dept.getComtype())){
						param.put("deptid", dept.getId());
					}else{
						param.put("parentcodes", dept.getDeptinnercode());
					}
				}
			}else{
				param.put("parentcodes", null);
			}
			List<INSBPaychannelmanager>  paywayList = insbpaychannelmanagerService.queryListByParam(param);
			Long totalsize = insbpaychannelmanagerService.queryListByParamSize(param);
			for(INSBPaychannelmanager pcm : paywayList){
				Map<Object, Object> parammap = new HashMap<Object, Object>();
				INSBPaychannel paychannel = insbPaychannelService.queryById(pcm.getPaychannelid());
				parammap.put("id", pcm.getId());
				parammap.put("paychannelid", pcm.getPaychannelid());
				parammap.put("paychannelname", paychannel.getPaychannelname());

                if (StringUtil.isNotEmpty(pcm.getDeptid())) {
                    INSCDept dept = deptDao.selectById(pcm.getDeptid());
                    if (dept != null && !"".equals(dept)) {
                        parammap.put("deptname", dept.getShortname());
                    }
                }
                if (StringUtil.isNotEmpty(pcm.getProviderid())) {
                    INSBProvider insbprovider = providerDao.selectById(pcm.getProviderid());
                    if (insbprovider != null && !"".equals(insbprovider)) {
                        parammap.put("providename", insbprovider.getPrvshotname());
                    }
                }

				if (StringUtil.isNotEmpty(pcm.getAgreementid())) {
					INSBAgreement insbprovider = agreementDao.selectById(pcm.getAgreementid());
					if (insbprovider != null && !"".equals(insbprovider)) {
						parammap.put("agreementname", insbprovider.getAgreementname());
					}
				}
				infoList.add(parammap);
			}
			resultmap.put("total", totalsize);
		}else if((null == deptid || "".equals(deptid)) && (null == providerid || "".equals(providerid))){
			Map<String, Object> map = BeanUtils.toMap(para);
			map.put("offset", para.getOffset());
			map.put("limit", para.getLimit());
			map.put("paychannelid", paychannelid);
			map.put("userDept", operator.getUserorganization());
			return insbPaychannelService.initPayWayList(map,paychannelid);
		}
		resultmap.put("rows", infoList);

		return resultmap;
	}

	/**
	 * 根据id查询pcm
	 * @param id
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "selectpcmbyid", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> selectpcmbyid(@RequestParam(required=false) String id) throws ControllerException{
		Map<String,Object> resultmap = new HashMap<String,Object>();
		INSBPaychannelmanager pcm =  insbpaychannelmanagerService.queryById(id);
		if(pcm!=null){
            if (StringUtil.isNotEmpty(pcm.getProviderid())) {
                INSBProvider pro = insbProviderService.queryById(pcm.getProviderid());
                if(pro!=null){
                    resultmap.put("providername", pro.getPrvname());
                    resultmap.put("providerid", pro.getId());
                }
            }

            if (StringUtil.isNotEmpty(pcm.getDeptid())) {
                INSCDept dept = inscDeptService.queryById(pcm.getDeptid());
                if (dept != null) {
                    resultmap.put("deptname", dept.getShortname());
                    resultmap.put("deptid", dept.getId());
                }
            }
		}
		return resultmap;
	}

}