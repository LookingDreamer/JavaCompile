package com.zzb.extra.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.QueryBean;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.manager.scm.INSASyncService;
import com.cninsure.system.service.INSCCodeService;
import com.cninsure.system.service.INSCDeptService;
import com.common.ModelUtil;
import com.common.PagingParams;
import com.zzb.chn.util.JsonUtils;
import com.zzb.cm.controller.vo.CertificationVo;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.cm.service.INSBCarinfoService;
import com.zzb.cm.service.INSBCarkindpriceService;
import com.zzb.cm.service.INSBOrderService;
import com.zzb.cm.service.INSBQuoteinfoService;
import com.zzb.cm.service.INSBQuotetotalinfoService;
import com.zzb.conf.controller.vo.BaseVo;
import com.zzb.conf.controller.vo.TreeVo;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBOperatorcomment;
import com.zzb.conf.entity.INSBOrderpayment;
import com.zzb.conf.entity.INSBPaychannel;
import com.zzb.conf.entity.INSBPermission;
import com.zzb.conf.entity.INSBPermissionset;
import com.zzb.conf.entity.INSBUsercomment;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.conf.service.INSBCertificationService;
import com.zzb.conf.service.INSBChannelService;
import com.zzb.conf.service.INSBOperatorcommentService;
import com.zzb.conf.service.INSBOrderpaymentService;
import com.zzb.conf.service.INSBPaychannelService;
import com.zzb.conf.service.INSBPermissionsetService;
import com.zzb.conf.service.INSBPolicyManageService;
import com.zzb.conf.service.INSBProviderService;
import com.zzb.conf.service.INSBUsercommentService;
import com.zzb.extra.controller.vo.ActivityPrizesVo;
import com.zzb.extra.controller.vo.TempjobnumMap2Jobnum;
import com.zzb.extra.dao.INSBAgentPrizeDao;
import com.zzb.extra.dao.INSBMiniRoleDao;
import com.zzb.extra.entity.INSBAccountDetails;
import com.zzb.extra.entity.INSBAgentRecommend;
import com.zzb.extra.entity.INSBAgentUser;
import com.zzb.extra.entity.INSBMiniRole;
import com.zzb.extra.model.INSBAgentUserQueryModel;
import com.zzb.extra.model.MiniOrderQueryModel;
import com.zzb.extra.model.WxMsgTemplateModel;
import com.zzb.extra.service.INSBAccountDetailsService;
import com.zzb.extra.service.INSBAgentPrizeService;
import com.zzb.extra.service.INSBAgentUserService;
import com.zzb.extra.service.INSBMarketActivityService;
import com.zzb.extra.service.INSBMiniOrderManageService;
import com.zzb.extra.service.WxMsgTemplateService;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.FileUploadBase64ParamIdentify;
import com.zzb.mobile.service.AppLoginService;
import com.zzb.model.PolicyDetailedModel;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/agentuser/*")
public class INSBAgentUserController extends BaseController {

    @Resource
    private INSBAgentUserService insbAgentUserService;
    @Resource
    private INSBAgentService insbAgentService;
    @Resource
    private INSASyncService agtService;
    @Resource
    private INSBPermissionsetService insbPermissionsetService;
    //
    @Resource
    private INSCDeptService deptService;

    @Resource
    private INSBChannelService insbChannelService;

    @Resource
    private INSBCertificationService certificationService;

    @Resource
    private INSBChannelService channelService;

    @Resource
    private AppLoginService appLoginService;

    @Resource
    private INSBMarketActivityService insbMarketActivityService;

    @Resource
    private WxMsgTemplateService wxMsgTemplateService;

    @Resource
    private INSBAgentPrizeService insbAgentPrizeService;

    @Resource
    private INSBAccountDetailsService insbAccountDetailsService;

    @Resource
    private INSBCarinfoService insbCarinfoService;
    @Resource
    private INSBMiniOrderManageService insbOrderManageService;
    @Resource
    private INSCCodeService codeService;
    @Resource
    private INSBProviderService providerService;
    @Resource
    private INSBPolicyManageService insbPolicyManageService;
    @Resource
    private INSBCarkindpriceService insbCarkindpriceService;
    @Resource
    private INSBOrderService insbOrderService;
    @Resource
    private INSBUsercommentService insbUsercommentService;
    @Resource
    private INSBOperatorcommentService insbOperatorcommentService;
    @Resource
    private INSBQuotetotalinfoService insbQuotetotalinfoService;
    @Resource
    private INSBQuoteinfoService insbQuoteinfoService;
    @Resource
    private INSBPaychannelService isnbPaychannelService;
    @Resource
    private INSBOrderpaymentService insbOrderpaymentService;

    @Resource
    private INSBAgentPrizeDao insbAgentPrizeDao;

    @Resource
    private HttpServletRequest request;

    @Resource
    private HttpServletResponse response;
    
    @Resource
    private INSBMiniRoleDao insbMiniRoleDao;

//	@Resource
//	private INSBPolicyitemService insbPolicyitemService;

    //	@Resource
//	private INSBCarinfoService insbCarinfoService;
    private String agentid = "";

    /**
     * 初始化代理人状�??
     *
     * @return
     */
    @RequestMapping(value = "menu2list", method = RequestMethod.GET)
    public ModelAndView menu2List() {
        ModelAndView result = new ModelAndView("extra/agentuserlist");
//		List<Map<String,Object>> approve =  insbAgentUserServiceImpl.getQueryPageData();
        List<Map<String, Object>> approve = insbAgentService.getQueryPageData();
        List<INSBMiniRole> listRole = insbMiniRoleDao.queryMiniRole(new HashMap<String,Object>());
        result.addObject("approveData", approve);
        result.addObject("listRole", listRole);
        return result;
    }

    @RequestMapping(value = "initagentlistpage", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> initAgentUserListPage(HttpSession session,
                                                     @ModelAttribute PagingParams para,
                                                     @ModelAttribute INSBAgentUserQueryModel qm,
                                                     @ModelAttribute INSCDept dept, @ModelAttribute QueryBean querybean)
            throws ParseException {
        if (para.getSort() != null) {
            if ("agentuserkindstr".equals(para.getSort())) {
                para.setSort("agentuserkind");
            }
            if ("agentuserstatusstr".equals(para.getSort())) {
                para.setSort("agentuserstatus");
            }
        }
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // 处理时间字段
        // 1:为空不处�?
        if (!"".equals(qm.getTesttimeendstr())
                && qm.getTesttimeendstr() != null) {
            qm.setTesttimeend(format.parse(qm.getTesttimeendstr().toString()));
        }
        if (!"".equals(qm.getTesttimestr()) && qm.getTesttimestr() != null) {
            qm.setTesttime(format.parse(qm.getTesttimestr().toString()));
        }
        if (!"".equals(qm.getRegistertimestr())
                && qm.getRegistertimestr() != null) {
            qm.setRegistertime(format.parse(qm.getRegistertimestr().toString()));
        }
        if (!"".equals(qm.getRegistertimeendstr())
                && qm.getRegistertimeendstr() != null) {
            qm.setRegistertimeend(format.parse(qm.getRegistertimeendstr()
                    .toString()));
        }

        /**
         * 仅查询获得机构树某节点下�?有网点的id
         */
        String deptid = qm.getDeptid();
        if (deptid == null || "".equals(deptid)) {
            INSCUser user = (INSCUser) session.getAttribute("insc_user");
            if(user!=null) deptid = user.getUserorganization();
        }
        List<String> deptWDids = new ArrayList<String>();
        if (!"".equals(deptid) && deptid != null) {
            INSCDept parentdept = deptService.queryById(deptid);
            if (parentdept.getComtype().equals("05")) {
                qm.setDeptid(deptid);
                qm.setDeptWDids(null);
            } else if (!parentdept.getComtype().equals("05") && !parentdept.getComtype().equals("") && null != parentdept.getComtype()) {
//				deptWDids = insbAgentUserServiceImpl.queryWDidsByPatId(parentdept.getId(),"05");
                deptWDids = insbAgentService.queryWDidsByPatId(parentdept.getId(), "05");
                qm.setDeptWDids(deptWDids);
                qm.setDeptid(null);
                dept.setId(null);
            }
        }

        Map<String, Object> map = BeanUtils.toMap(qm, dept, querybean, para);
        if (null != deptWDids  && deptWDids.size() > 0) {
            map.put("deptid", null);
        }
//        map.put("usersource", "minizzb");
        return insbAgentUserService.getAgentUserListPage(map);
    }
    @RequestMapping(value = "initagentlistPhoepage", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> initagentlistPhoepage(HttpSession session,
    		@ModelAttribute PagingParams para,
    		@ModelAttribute INSBAgentUserQueryModel qm,
    		@ModelAttribute INSCDept dept, @ModelAttribute QueryBean querybean)
    				throws ParseException {
    	if (para.getSort() != null) {
    		if ("agentuserkindstr".equals(para.getSort())) {
    			para.setSort("agentuserkind");
    		}
    		if ("agentuserstatusstr".equals(para.getSort())) {
    			para.setSort("agentuserstatus");
    		}
    	}
    	DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	// 处理时间字段
    	// 1:为空不处�?
    	if (!"".equals(qm.getTesttimeendstr())
    			&& qm.getTesttimeendstr() != null) {
    		qm.setTesttimeend(format.parse(qm.getTesttimeendstr().toString()));
    	}
    	if (!"".equals(qm.getTesttimestr()) && qm.getTesttimestr() != null) {
    		qm.setTesttime(format.parse(qm.getTesttimestr().toString()));
    	}
    	if (!"".equals(qm.getRegistertimestr())
    			&& qm.getRegistertimestr() != null) {
    		qm.setRegistertime(format.parse(qm.getRegistertimestr().toString()));
    	}
    	if (!"".equals(qm.getRegistertimeendstr())
    			&& qm.getRegistertimeendstr() != null) {
    		qm.setRegistertimeend(format.parse(qm.getRegistertimeendstr()
    				.toString()));
    	}
    	
    	/**
    	 * 仅查询获得机构树某节点下�?有网点的id
    	 */
    	String deptid = qm.getDeptid();
    	if (deptid == null || "".equals(deptid)) {
    		INSCUser user = (INSCUser) session.getAttribute("insc_user");
    		if(user!=null) deptid = user.getUserorganization();
    	}
    	List<String> deptWDids = new ArrayList<String>();
    	if (!"".equals(deptid) && deptid != null) {
    		INSCDept parentdept = deptService.queryById(deptid);
    		if (parentdept.getComtype().equals("05")) {
    			qm.setDeptid(deptid);
    			qm.setDeptWDids(null);
    		} else if (!parentdept.getComtype().equals("05") && !parentdept.getComtype().equals("") && null != parentdept.getComtype()) {
//				deptWDids = insbAgentUserServiceImpl.queryWDidsByPatId(parentdept.getId(),"05");
    			deptWDids = insbAgentService.queryWDidsByPatId(parentdept.getId(), "05");
    			qm.setDeptWDids(deptWDids);
    			qm.setDeptid(null);
    			dept.setId(null);
    		}
    	}
    	
    	Map<String, Object> map = BeanUtils.toMap(qm, dept, querybean, para);
    	if (null != deptWDids  && deptWDids.size() > 0) {
    		map.put("deptid", null);
    	}
    	map.put("usersource", "minizzb");
    	return insbAgentUserService.getAgentPhoneUserListPage(map);
    }


//	/**
//	 * 跳转保单列表页面
//	 *
//	 * @param agentUserId
//	 * @return
//	 */
//	@RequestMapping(value = "querybuttonlist", method = RequestMethod.GET)
//	public ModelAndView querybuttonlist(@RequestParam(required = false, defaultValue = "") String agentUserId) {
//		agentUserid = agentUserId;
//		return new ModelAndView("extra/policyitemlist");
//	}
//	/**
//	 * 根据代理人id查询该代理人id下面�?有的保单
//	 * @param para
//	 * @param edi
//	 * @return
//	 * @throws ControllerException
//	 */
//	@RequestMapping(value = "policcyitembyagentuserid", method = RequestMethod.GET)
//	@ResponseBody
//	public Map<String, Object> policcyitembyagentuserid(@ModelAttribute PagingParams para, @ModelAttribute INSBPolicyitem policyitem) throws ControllerException{
//		policyitem.setId(agentuserid);
//		Map<String, Object> map = BeanUtils.toMap(policyitem,para);
//		return insbPolicyitemService.policcyitembyagentuserid(map);
//	}

    @RequestMapping(value = "getAgentData", method = RequestMethod.POST)
    @ResponseBody
    public String getAgentUserData(HttpSession session) throws ControllerException {
        String operator = ((INSCUser) session.getAttribute("insc_user")).getUsercode();
//		Map<String, Object> mapResult = insbAgentUserServiceImpl.getAgentUserData(operator);
        Map<String, Object> mapResult = agtService.getAgentDataorOrg(operator,null);
        return com.alibaba.fastjson.JSONObject.toJSONString(mapResult);
    }

    /**
     * 转到编辑页面
     * <p/>
     * 初始化编辑页面信�?
     *
     * @param agentId
     * @return
     */
    @RequestMapping(value = "mian2edit", method = RequestMethod.GET)
    public ModelAndView mian2edit(HttpSession session,@RequestParam(required = false, defaultValue = "") String agentId) {

        ModelAndView result = new ModelAndView("extra/agentuseredit");
        List<INSBPermissionset> setList = new ArrayList<INSBPermissionset>();
//				insbPermissionsetService.queryOnUseSet();
        INSBAgent insbAgent = insbAgentUserService.queryById(agentId);
        if (insbAgent != null) {
            String deptid = insbAgent.getDeptid();
            Integer agentuserkind = insbAgent.getAgentkind();
            if (agentuserkind == null) {
                agentuserkind = 1;//1为试用类�?
            }
            //根据�?属机构�?�用户类型�?�功能启用来显示功能�?
            setList = insbPermissionsetService.selectByDeptAgentkindAndOnUseSet(deptid, agentuserkind);
        }

        CertificationVo certificationVo = new CertificationVo();
        certificationVo.setAgentnum(insbAgent != null ? insbAgent.getId() : "");  //insbAgent.getAgentcode()
        certificationVo = certificationService.getOneCertificationInfo(certificationVo);

        INSBAgent myReferrer = null; //我的推荐人
        if (insbAgent != null && insbAgent.getReferrerid() != null && !"".equals(insbAgent.getReferrerid().trim())) {
            myReferrer = insbAgentUserService.queryById(insbAgent.getReferrerid());
        }
        INSBAgent queryMyRecommendPersons = new INSBAgent();
        queryMyRecommendPersons.setReferrerid(insbAgent != null?insbAgent.getId():"");
        List<INSBAgent> myRecommendPersons = insbAgentUserService.queryList(queryMyRecommendPersons); //我推荐的人

        /*========================my order begin==================================*/
        MiniOrderQueryModel queryModel = new MiniOrderQueryModel();
        //System.out.println(JsonUtils.serialize(queryModel));
        //保单状态
        List<INSCCode> statusList = codeService.queryINSCCodeByCode("minizzbOrder", "orderStatus");
        result.addObject("statusList", statusList);
        INSCUser user = (INSCUser) session.getAttribute("insc_user");
        //String deptid = user.getUserorganization();
        String deptid = "999";//不需要用部门这个权限筛数据，临时设置成999
        //queryModel.setUserCode(user.getUsercode());
        queryModel.setAgentid(agentId);
        queryModel.setChannelUserId(agentId);
        Map<String, Object> rowList = insbOrderManageService.queryOrderList(queryModel,deptid);
        result.addObject("allData", rowList);
        result.addObject("queryModel", queryModel);
        /*========================my order end==================================*/

        Map<String, Object> resultTemp = insbAgentUserService.main2edit(agentId);
        result.addObject("agent", resultTemp.get("agent"));
        result.addObject("setlist", setList);
        result.addObject("approve", resultTemp.get("approve"));
        result.addObject("commonUseBranchNames", resultTemp.get("commonUseBranchNames"));
        result.addObject("certKinds", resultTemp.get("certKinds"));//证件类型
        result.addObject("agentlevelvalue", resultTemp.get("agentlevelvalue"));//代理人级�?
        result.addObject("certificationVo", certificationVo);
        result.addObject("myReferrer", myReferrer);
        result.addObject("myRecommendPersons", myRecommendPersons);
        return result;
    }

    /**
     *
     */
    /**
     * 查看订单列表
     * @param queryModel
     * @return
     * @throws com.cninsure.core.exception.ControllerException
     */
    @RequestMapping(value="/queryorderlist",method=RequestMethod.POST)
    @ResponseBody
    public ModelAndView queryOrderList(HttpSession session,@ModelAttribute MiniOrderQueryModel queryModel) throws ControllerException{
        String agentId = queryModel.getAgentid();
        ModelAndView result = new ModelAndView("extra/agentuseredit");
        List<INSBPermissionset> setList = new ArrayList<INSBPermissionset>();
//				insbPermissionsetService.queryOnUseSet();
        INSBAgent insbAgent = insbAgentUserService.queryById(agentId);
        if (insbAgent != null) {
            String deptid = insbAgent.getDeptid();
            Integer agentuserkind = insbAgent.getAgentkind();
            if (agentuserkind == null) {
                agentuserkind = 1;//1为试用类�?
            }
            //根据�?属机构�?�用户类型�?�功能启用来显示功能�?
            setList = insbPermissionsetService.selectByDeptAgentkindAndOnUseSet(deptid, agentuserkind);
        }

        CertificationVo certificationVo = new CertificationVo();
        certificationVo.setAgentnum(insbAgent != null ? insbAgent.getId() : "");  //insbAgent.getAgentcode()
        certificationVo = certificationService.getOneCertificationInfo(certificationVo);

        INSBAgent myReferrer = null; //我的推荐人
        if (insbAgent != null && insbAgent.getReferrerid() != null && !"".equals(insbAgent.getReferrerid().trim())) {
            myReferrer = insbAgentUserService.queryById(insbAgent.getReferrerid());
        }
        INSBAgent queryMyRecommendPersons = new INSBAgent();
        queryMyRecommendPersons.setReferrerid(insbAgent != null?insbAgent.getId():"");
        List<INSBAgent> myRecommendPersons = insbAgentUserService.queryList(queryMyRecommendPersons); //我推荐的人

        /*========================my order begin==================================*/
        //System.out.println(JsonUtils.serialize(queryModel));
        //保单状态
        List<INSCCode> statusList = codeService.queryINSCCodeByCode("minizzbOrder", "orderStatus");
        result.addObject("statusList", statusList);
        INSCUser user = (INSCUser) session.getAttribute("insc_user");
        //String deptid = user.getUserorganization();
        String deptid = "999";//不需要用部门这个权限筛数据，临时设置成999
        //queryModel.setUserCode(user.getUsercode());
        Map<String, Object> rowList = insbOrderManageService.queryOrderList(queryModel,deptid);
        result.addObject("allData", rowList);
        result.addObject("queryModel", queryModel);
        /*========================my order end==================================*/

        Map<String, Object> resultTemp = insbAgentUserService.main2edit(agentId);
        result.addObject("agent", resultTemp.get("agent"));
        result.addObject("setlist", setList);
        result.addObject("approve", resultTemp.get("approve"));
        result.addObject("commonUseBranchNames", resultTemp.get("commonUseBranchNames"));
        result.addObject("certKinds", resultTemp.get("certKinds"));//证件类型
        result.addObject("agentlevelvalue", resultTemp.get("agentlevelvalue"));//代理人级�?
        result.addObject("certificationVo", certificationVo);
        result.addObject("myReferrer", myReferrer);
        result.addObject("myRecommendPersons", myRecommendPersons);
        result.addObject("myOrderActive", "true");
        return result;

    }

    @RequestMapping(value="/queryprovidertree",method=RequestMethod.POST)
    @ResponseBody
    public List<Map<String, String>> queryprovidertree(@RequestParam(value="id",required=false) String parentcode) throws ControllerException{
        return providerService.queryTreeList(parentcode);
    }

    @RequestMapping(value="/queryorderdetail",method= RequestMethod.POST)
    @ResponseBody
    public ModelAndView queryDetail(@RequestParam(value="agentid",required=true) String agentid,@RequestParam(value="taskId",required=false) String taskId,@RequestParam(value="codename",required=false)String codename) throws ControllerException{
         ModelAndView mav = new ModelAndView("extra/insbMiniOrderDetailed");

        mav.addObject("codename",codename);
        INSBOrder order = new INSBOrder();
        order.setTaskid(taskId);
        //INSBOrder order = insbOrderService.queryById(taskId);
        order = insbOrderService.queryOne(order);
        String prvid = "";
        String id = "";
        if(order!=null){
            prvid = order.getPrvid();
            id = order.getId();
        }
        PolicyDetailedModel detailedModel = null;
        if(order!=null && order.getPaymentmethod()!=null && !"".equals(order.getPaymentmethod()) ){
            INSBPaychannel pc = isnbPaychannelService.queryById(order.getPaymentmethod());
            if(pc!=null){
                mav.addObject("paytypes",pc.getPaychannelname());
            }
        }
        if(id!=null && !"".equals(id)){
            detailedModel = insbPolicyManageService.queryOrderDetailedByOId(id,prvid,"orderid");
        }/*else{
            detailedModel = insbPolicyManageService.queryOrderDetailedByOId(pid,prvid,"");
        }*/

        try {
            INSBOrderpayment insbOrderpayment = new INSBOrderpayment();
            insbOrderpayment.setTaskid(taskId);
            insbOrderpayment.setPayresult("02");
            insbOrderpayment = insbOrderpaymentService.queryOne(insbOrderpayment);
            mav.addObject("insbOrderpayment", insbOrderpayment);
        }catch(Exception e){
            LogUtil.info(taskId+" queryorderdetail.insbOrderpayment errmsg="+e.getMessage());
        }

        Map<String, Object> otherInfo = insbCarinfoService.getCarTaskOtherInfo(taskId, prvid, "SHOW");//其他信息
        mav.addObject("otherInfo", otherInfo);
        mav.addObject("detailedModel", detailedModel);
        String taskid ="";
        String orderstatus="";
        String subInstanceId = "";
        if(order!=null){
            taskid = order.getTaskid();
            prvid = order.getPrvid();
            orderstatus = order.getOrderstatus();
            INSBQuotetotalinfo totalinfo = new INSBQuotetotalinfo();
            totalinfo.setTaskid(taskid);
            totalinfo = insbQuotetotalinfoService.queryOne(totalinfo);
            INSBQuoteinfo quote = new INSBQuoteinfo();
            quote.setQuotetotalinfoid(totalinfo.getId());
            quote.setInscomcode(prvid);
            quote = insbQuoteinfoService.queryOne(quote);
            subInstanceId = quote.getWorkflowinstanceid();
        }
        Map<String,Object> insConfigInfo = insbCarkindpriceService.getCarInsConfigByInscomcode(prvid,taskid);
        Map<String,Object> carInsTaskInfo = new HashMap<String, Object>();
        carInsTaskInfo.put("insConfigInfo",insConfigInfo);
        mav.addObject("carInsTaskInfo", carInsTaskInfo);
        //查询影像信息
        List<PolicyDetailedModel> imginfo = insbPolicyManageService.queryImgInfo(id);
        //给用户的备注信息
        List<INSBUsercomment> usercomment  = new ArrayList<INSBUsercomment>();
        //给操作员的备注信息
        List<INSBOperatorcomment> operatorcommentList = new ArrayList<INSBOperatorcomment>();
        List<String> node =  Arrays.asList(new String[]{"3","4"});
        if(node.contains(orderstatus)){
            operatorcommentList = insbOperatorcommentService.getOperatorCommentByMaininstanceid(taskid, prvid);
        }else{
            operatorcommentList = insbOperatorcommentService.getOperatorCommentByMaininstanceid(taskid, prvid);
        }
        //当前节点之前的备注信息（入参 ： 主流程实例taskid ; prvid 保险公司code ; dqtaskcode 当前节点code）
        usercomment = insbUsercommentService.getNearestUserComment(taskid, prvid, null);
        mav.addObject("agentid",agentid);
        mav.addObject("usercomment", usercomment);
        mav.addObject("taskid", taskid);
        mav.addObject("subInstanceId", subInstanceId);
        mav.addObject("operatorcommentList", operatorcommentList);
        mav.addObject("imginfo", imginfo);
        try {
            Map<String, Object> commissionInfo = new HashMap<String, Object>();
            Map<String, Object> usePrizeInfo = new HashMap<String, Object>();
            Map<String, Object> getCashInfo = new HashMap<String, Object>();
            //Map<String, Object> getPrizeInfoList = new HashMap<String, Object>();
            INSBAccountDetails accountDetailsQuery = new INSBAccountDetails();
            accountDetailsQuery.setTaskid(taskid);
            List<INSBAccountDetails> accountDetailsList = insbAccountDetailsService.queryList(accountDetailsQuery);
            List<Map<String, Object>> commissionList = new ArrayList<Map<String, Object>>();
            for(INSBAccountDetails accountDetails : accountDetailsList) {
                if("101".equals(accountDetails.getFundsource())) {
                    Map<String, Object> acctNoti = JsonUtils.deserialize(accountDetails.getNoti(), Map.class);
                    if (acctNoti.get("商业险金额") != null) {
                        Double rate = Double.parseDouble(String.valueOf(acctNoti.get("商业险酬金率")).replace("%",""))*0.01;
                        Double amount = Double.parseDouble(String.valueOf(acctNoti.get("商业险金额")));
                        commissionInfo = new HashMap<String, Object>();
                        commissionInfo.put("rate",String.valueOf(acctNoti.get("商业险酬金率")));
                        commissionInfo.put("amount", amount);
                        commissionInfo.put("type","商业险交易佣金");
                        commissionInfo.put("commission",((Double)(amount*rate)).intValue());
                        commissionList.add(commissionInfo);
                    }
                    if (acctNoti.get("交强险金额") != null) {
                        Double rate = Double.parseDouble(String.valueOf(acctNoti.get("交强险酬金率")).replace("%",""))*0.01;
                        Double amount = Double.parseDouble(String.valueOf(acctNoti.get("交强险金额")));
                        commissionInfo = new HashMap<String, Object>();
                        commissionInfo.put("rate",String.valueOf(acctNoti.get("交强险酬金率")));
                        commissionInfo.put("amount", amount);
                        commissionInfo.put("type","交强险交易佣金");
                        commissionInfo.put("commission",((Double)(amount*rate)).intValue());
                        commissionList.add(commissionInfo);
                    }
                }else if("104".equals(accountDetails.getFundsource())){
                    commissionInfo = new HashMap<String, Object>();
                    Map<String, Object> acctNoti = JsonUtils.deserialize(accountDetails.getNoti(), Map.class);
                    Double amount = accountDetails.getAmount();
                    commissionInfo.put("rate",String.valueOf(acctNoti.get("赏金率")));
                    commissionInfo.put("amount", amount);
                    commissionInfo.put("type","推荐赏金");
                    commissionInfo.put("commission",amount);
                    commissionList.add(commissionInfo);
                }
                /*
                else if("102".equals(accountDetails.getFundsource())){
                    usePrizeInfo.put("amount", accountDetails.getAmount());
                    mav.addObject("usePrizeInfo", usePrizeInfo);
                }else if("103".equals(accountDetails.getFundsource())){
                    Map<String, Object> acctNoti = JsonUtils.deserialize(accountDetails.getNoti(), Map.class);
                    getCashInfo.put("amount", accountDetails.getAmount());
                    if (acctNoti.get("活动名称") != null) {
                        getCashInfo.put("activityName", acctNoti.get("活动名称"));
                    }
                    mav.addObject("getCashInfo", getCashInfo);
                }
                */
            }
            mav.addObject("commissionList", commissionList);

            //获取奖励
            Map getParamsMap = new HashMap<String,Object>();
            getParamsMap.put("agentid",agentid);
            getParamsMap.put("prizenoti",taskid);
            //map.put("prizetype","01");
            List<Map<Object, Object>> getPrizeList = insbAgentPrizeDao.queryPagingList(getParamsMap);

            //使用奖券
            Map useParamsMap = new HashMap<String,Object>();
            useParamsMap.put("agentid",agentid);
            useParamsMap.put("useTaskId",taskid);
            //map.put("prizetype","01");
            List<Map<Object, Object>> usePrizeList = insbAgentPrizeDao.queryPagingList(useParamsMap);

            List<Map<Object, Object>> prizeList = new ArrayList<Map<Object, Object>>();
            prizeList.addAll(getPrizeList);
            prizeList.addAll(usePrizeList);
            mav.addObject("prizeInfoList", prizeList);

        }catch (Exception e){
            LogUtil.info(e.getMessage());
        }
        return mav;
    }

    /**
     * 1：有功能包（新增后�?�择了功能包。修改时有功能包�? 1.1�? 初始�? 修改 保存 走allot
     * <p/>
     * 2:没有功能�? 2.1：新增修改走代理人权限关系表
     *
     * @param session
     * @param permissionsetId 当前功能包id 新增时为�?
     * @param para            分页信息
     * @param permission      （列表查�? 留得参数�?
     * @param dept            组织机构
     * @param querybean       （没有源�? 暂且不用�?
     * @return
     */
    @RequestMapping(value = "initpermissionlistpage", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> initpermissionListPage(HttpSession session,
                                                      String permissionsetId, String agentId,
                                                      @ModelAttribute PagingParams para,
                                                      @ModelAttribute INSBPermission permission,
                                                      @ModelAttribute INSCDept dept, @ModelAttribute QueryBean querybean) {

        Map<String, Object> map = BeanUtils.toMap(permission, dept, querybean,
                para);
//		return insbAgentUserServiceImpl.getpermissionListByPage(map, permissionsetId, agentUserId);
        return insbAgentService.getpermissionListByPage(map, permissionsetId, agentId);
    }

    @RequestMapping(value = "phoneverify", method = RequestMethod.GET)
    @ResponseBody
    public String phoneverify(String phonenum, String agentUserid) {

//		int num = insbAgentUserServiceImpl.phoneverify(phonenum,agentUserid);
        int num = insbAgentService.phoneverify(phonenum, agentUserid);
        if (num > 0) {
            return JSONObject.fromObject("{'msg':'该手机号已存?'}").toString();
        } else {
            return JSONObject.fromObject("{'msg':'手机号可以使?'}").toString();
        }
    }

    @RequestMapping(value = "deptverify", method = RequestMethod.GET)
    @ResponseBody
    public String deptverify(String deptid) {
        INSCDept dept = deptService.getOrgDeptByDeptCode(deptid);
        if (dept != null) {
            if ("05".equals(dept.getComtype())) {
                return JSONObject.fromObject("{'msg':'true'}").toString();
            } else {
                return JSONObject.fromObject("{'msg':'false'}").toString();
            }
        } else {
            return JSONObject.fromObject("{'msg':'false'}").toString();
        }
    }

    /**
     * 初始化当前功能包供应
     * <p/>
     * A：代理人没有选择功能
     * B：代理人选择功能
     * C：代理人关联协议
     * D：代理人没有关联协议
     * <p/>
     * 下面组合方式否能够编辑，有功能包编辑时默认修改代理人基本信息功能包字段置为空
     * <p/>
     * AD:不显示供应商结构
     * AC:显示协议供应
     * <p/>
     * BD:显示功能包供应商
     * BC:显示功能包协议
     *
     * @param session
     * @param agentId
     * @param setId
     * @return
     */
    @RequestMapping(value = "initprovidertree", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, String>> initProviderTree(HttpSession session, String agentId, String setId) {
//		return insbAgentUserServiceImpl.getProviderTreeList(agentUserId,setId);
        return insbAgentService.getProviderTreeList(agentId, setId);
    }

    /**
     * 查询条件 机构树（异步)
     *
     * @return
     */
    @RequestMapping(value = "initdepttree", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, String>> initDeptTree(HttpSession session,
                                                  @RequestParam(value = "id", required = false) String parentcode) {
        return deptService.queryTreeList(parentcode);
    }

    /**
     * 根据登陆代理人所在机构查询条机构树（异步)
     *
     * @return
     */
    @RequestMapping(value = "initDeptTreeByAgentUser", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, String>> initDeptTreeByAgentUser(HttpSession session,
                                                             @RequestParam(value = "id", required = false) String parentcode) {
        INSCUser user = (INSCUser) session.getAttribute("insc_user");
        if (parentcode == null || "".equals(parentcode)) {
            parentcode = user.getUserorganization();
            INSCDept dept = deptService.getOrgDept(parentcode);
            if (dept != null) {
                parentcode = dept.getUpcomcode();
            }
        }
        return deptService.queryTreeList4Data(parentcode, user.getUserorganization());
    }

    /**
     * 查询渠道
     *
     * @param  session
     * @param parentcode
     * @return
     */
    @RequestMapping(value = "initchanneltree", method = RequestMethod.POST)
    @ResponseBody
    public List<TreeVo> initChannelTree(HttpSession session,
                                        @RequestParam(value = "id", required = false) String parentcode) {
        return insbChannelService.queryTreeListByPid(parentcode);
    }

    /**
     * 保存或�?�修改代理人基本信息
     *
     * @return
     */
//    @RequestMapping(value = "saveorupdateagent", method = RequestMethod.POST)
//    @ResponseBody
//    public String saveOrUpdateAgent(HttpSession session, @ModelAttribute INSBAgent agent) {
//        INSCUser user = (INSCUser) session.getAttribute("insc_user");
////		return insbAgentUserServiceImpl.saveOrUpdateAgentUser(user, agent);
//        return insbAgentService.saveOrUpdateAgent(user, agent);
//    }

    /**
     * 接口：
     * 保存或修改代理人基本信息，来源于微信的 MINI掌中保
     * 必需项：openid、usersource
     *
     * @return
     */
    @RequestMapping(value = "saveOrUpdateAgentWeiXin", method = RequestMethod.POST)
    @ResponseBody
    public BaseVo saveOrUpdateAgentWeiXin(HttpSession session, @ModelAttribute INSBAgent agent) {
        //INSCUser user = (INSCUser) session.getAttribute("insc_user");
        BaseVo bv = new BaseVo();
        if (agent != null && agent.getOpenid() != null && !"".equals(agent.getOpenid().trim()) && "minizzb".equals(agent.getUsersource())) {
            try {
                INSBAgent agentInfo = insbAgentUserService.saveOrUpdateAgentWeiXin(agent);
                bv.setStatus("1");
                bv.setMessage("操作成功");
                bv.setResult(agentInfo);
            } catch (Exception e) {
                bv.setStatus("2");
                bv.setMessage("操作失败,发生异常openid、usersource");
                e.printStackTrace();
            }
        } else {
            bv.setStatus("2");
            bv.setMessage("操作失败,检查提交openid、usersource");
        }
        return bv;
    }

    //关注消息
    private void sendMessage(INSBAgent agent) {
        LogUtil.info("关注中 openid:"+agent.getOpenid() +",开始发消息 时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        new Thread(() -> {
            try {
                String agentid = agent.getId();
                //关注新增加用户成功，发消息
                WxMsgTemplateModel wxMsgTemplateModel = new WxMsgTemplateModel();
                wxMsgTemplateModel.setTouser(agent.getOpenid()); // 用户的openid
                wxMsgTemplateModel.setUrl(wxMsgTemplateService.getBaseUrl("WeChat", "minizzburl", "01", agent.getOpenid(), "mycenter"));
                wxMsgTemplateModel.setTemplatetype("08"); // 08：关注成功通知--主动关注公众号进入公众号即触发
                wxMsgTemplateModel.setFirst("尊敬的用户，欢迎您关注mini掌中保	");
                wxMsgTemplateModel.setKeyword1(agent.getNickname());
                wxMsgTemplateModel.setKeyword2(new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date()));

                //agentid 注册用户UUID，operate=save保存，activitytype=02 注册活动
                ActivityPrizesVo activityPrizesVo = new ActivityPrizesVo();
                //agentid 注册ID，referrer 推荐人的ID，operate=save保存，activitytype=02 注册活动
                activityPrizesVo.setAgentid(agentid);
                activityPrizesVo.setActivitytype("02");
                //查询不设置 activityPrizesVo.setOperate("save");
                activityPrizesVo.setGettotal(true);
                String jsonStr = insbMarketActivityService.queryTaskPrizes(activityPrizesVo); //记录推荐奖励
                LogUtil.info("用户uuid=" + agentid + " 关注公众号消息 ,奖金信息json 串" + jsonStr);
                Map<String, Object> map = JSONObject.fromObject(jsonStr);
                int account_1 = 0;
                int account_2 = 0;
                try {
                    account_1 = (Integer) map.get("奖券");
                } catch (Exception ex) {
                    //ex.printStackTrace();
                }
                try {
                    account_2 = (Integer) map.get("奖金");
                } catch (Exception ex) {
                    //ex.printStackTrace();
                }
                String remark = "感谢您的支持，绑定手机号就能领取 " + (account_1 + account_2) + "元 奖励，快去吧！";

                wxMsgTemplateModel.setRemark(remark);

                wxMsgTemplateService.sendMsgSimplified(wxMsgTemplateModel);
                LogUtil.info("用户uuid=" + agentid + " ,openid= " + agent.getOpenid() + " 发关注消息成功");
            } catch (Exception ex) {
                ex.printStackTrace();
                LogUtil.info("用户uuid=" + agent.getId() + " ,openid= " + agent.getOpenid() + " 发关注消息, 发生异常");
            }
            LogUtil.info("关注中 openid:"+agent.getOpenid() +",结束发消息 时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        }).start();
    }

    /**
     * 接口：关注后调用的登录接口。可以是多次关注（即关注 -> 取消关注 -> 关注）
     * 登录，来源于微信的 MINI掌中保
     * 返回：用户基本信息
     * 必需项：openid、usersource
     *
     * @return
     */
    @RequestMapping(value = "focusminzzb", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel focusminzzb(HttpSession session, @ModelAttribute INSBAgentUser agent) {
        //INSCUser user = (INSCUser) session.getAttribute("insc_user");s
        agent.setPhone(null); //登录入口：新增加用户或修改用户，都不设置绑定手机号
        LogUtil.info("关注开始 openid:"+agent.getOpenid() +" 时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        CommonModel cm = loginminizzb(agent,agent.getCity());

        LogUtil.info("关注中 openid:" + agent.getOpenid() + " 时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        //主动关注，或推荐关注的，前端都从这个接口，进入注册，就发消息，
        //08：关注成功通知--主动关注公众号进入公众号即触发
        this.sendMessage(agent);
        //绑定手机号消息,如果是推荐成功之后，再关注要发 绑定手机 奖券消息(发消息里判断)
        wxMsgTemplateService.resendRegisterMsg(agent.getId());

        LogUtil.info("关注成功 openid:" + agent.getOpenid() + " 时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        return cm;
    }

    /**
     * 接口：登录或注册(注册功能deprecated, 改为关注注册接口: focusminzzb ）。
     * 登录，来源于微信的 MINI掌中保
     * 返回：用户基本信息
     * 必需项：openid、usersource
     *
     * @return
     */
    @RequestMapping(value = "loginminzzb", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel loginminzzb(HttpSession session, @ModelAttribute INSBAgentUser agent) {
        //INSCUser user = (INSCUser) session.getAttribute("insc_user");s
        agent.setPhone(null); //登录入口：新增加用户或修改用户，都不设置绑定手机号
        return loginminizzb(agent,agent.getCity());
    }

    @Deprecated
    //一个是关注登录，一个是从接受推荐之后登录
    private CommonModel loginminizzb(INSBAgentUser agent) {
        CommonModel cm = new CommonModel();
        if (agent != null && agent.getOpenid() != null && !"".equals(agent.getOpenid().trim()) && "minizzb".equals(agent.getUsersource())) {
            try {
                insbAgentUserService.saveOrUpdateAgentWeiXin(agent);
                INSBAgent insbAgent = insbAgentUserService.selectByOpenid(agent.getOpenid());
                if (insbAgent != null && insbAgent.getId() != null && !"".equals(insbAgent.getId())) {
                    String city = (agent.getCity() != null && agent.getCity().trim().length() > 0) ? (agent.getCity().trim()) //登录提交的投保地区
                            : (insbAgent.getLivingcityid() != null && insbAgent.getLivingcityid().trim().length() > 0) ? (insbAgent.getLivingcityid().trim()) //用户注册的地区
                            : "440100"; //默认地区
                    //String channelinnercode = agent.getUsersource(); // usersource="minizzb" 支付前置，渠道配置变更为nqd_minizzb2016
                    String channelinnercode = "nqd_minizzb2016";
                    String agentcode = channelService.queryChannelAgentCode(city, channelinnercode);
                    insbAgent.setJobnum(agentcode);
                    if (agentcode != null) {
                        //使用渠道代理人登录
                        TempjobnumMap2Jobnum tempjobnumMap2Jobnum = new TempjobnumMap2Jobnum();
                        tempjobnumMap2Jobnum.setChanneluserid(insbAgent.getId());
                        tempjobnumMap2Jobnum.setTempjobnum(insbAgent.getTempjobnum());
                        tempjobnumMap2Jobnum.setJobnum(agentcode);
                        //tempjobnumVo.setToken();
                        tempjobnumMap2Jobnum.setCity(city);
                        tempjobnumMap2Jobnum.setUsersource(agent.getUsersource());
                        tempjobnumMap2Jobnum.setOpenid(agent.getOpenid());

                        // 临时工号生成token信息，出单工号按之前代理登录查询信息一致。
                        CommonModel model = appLoginService.loginByChannelTempjobnum(tempjobnumMap2Jobnum);
                        if (model.getStatus().equals("success")) {
                            JSONObject jsonObject = JSONObject.fromObject(model.getBody());
                            response.addHeader("token", jsonObject.getJSONObject("tempjobnumMap2Jobnum").getString("token"));
                        }
                        return model;
                    } else {
                        cm.setStatus("fail");
                        cm.setMessage("登录失败,地区:" + city + " 没有配置渠道代理人");
                    }
                } else {
                    cm.setStatus("fail");
                    cm.setMessage("登录失败,增加渠道用户失败");
                }
            } catch (Exception e) {
                cm.setStatus("fail");
                cm.setMessage("登录失败,发生异常");
                e.printStackTrace();
            }
        } else {
            cm.setStatus("fail");
            cm.setMessage("登录失败,检查提交openid、usersource");
        }
        return cm;
    }

    //一个是关注登录，一个是从接受推荐之后登录
    private CommonModel loginminizzb(INSBAgent agent,String ecity) {
        CommonModel cm = new CommonModel();
        if (agent != null && agent.getOpenid() != null && !"".equals(agent.getOpenid().trim()) && "minizzb".equals(agent.getUsersource())) {
            try {
                insbAgentUserService.saveOrUpdateAgentWeiXin(agent);
                INSBAgent insbAgent = insbAgentUserService.selectByOpenid(agent.getOpenid());
                if (insbAgent != null && insbAgent.getId() != null && !"".equals(insbAgent.getId())) {
                    String city = (ecity != null && ecity.trim().length() > 0) ? (ecity.trim()) //登录提交的投保地区
                            : (insbAgent.getLivingcityid() != null && insbAgent.getLivingcityid().trim().length() > 0) ? (insbAgent.getLivingcityid().trim()) //用户注册的地区
                            : "440100"; //默认地区
                    //String channelinnercode = agent.getUsersource(); // usersource="minizzb" 支付前置，渠道配置变更为nqd_minizzb2016
                    String channelinnercode = "nqd_minizzb2016";
                    String agentcode = channelService.queryChannelAgentCode(city, channelinnercode);
                    insbAgent.setJobnum(agentcode);
                    if (agentcode != null) {
                        //使用渠道代理人登录
                        TempjobnumMap2Jobnum tempjobnumMap2Jobnum = new TempjobnumMap2Jobnum();
                        tempjobnumMap2Jobnum.setChanneluserid(insbAgent.getId());
                        tempjobnumMap2Jobnum.setTempjobnum(insbAgent.getTempjobnum());
                        tempjobnumMap2Jobnum.setJobnum(agentcode);
                        //tempjobnumVo.setToken();
                        tempjobnumMap2Jobnum.setCity(city);
                        tempjobnumMap2Jobnum.setUsersource(agent.getUsersource());
                        tempjobnumMap2Jobnum.setOpenid(agent.getOpenid());

                        // 临时工号生成token信息，出单工号按之前代理登录查询信息一致。
                        CommonModel model = appLoginService.loginByChannelTempjobnum(tempjobnumMap2Jobnum);
                        if (model.getStatus().equals("success")) {
                            JSONObject jsonObject = JSONObject.fromObject(model.getBody());
                            response.addHeader("token", jsonObject.getJSONObject("tempjobnumMap2Jobnum").getString("token"));
                        }
                        return model;
                    } else {
                        cm.setStatus("fail");
                        cm.setMessage("登录失败,地区:" + city + " 没有配置渠道代理人");
                    }
                } else {
                    cm.setStatus("fail");
                    cm.setMessage("登录失败,增加渠道用户失败");
                }
            } catch (Exception e) {
                cm.setStatus("fail");
                cm.setMessage("登录失败,发生异常");
                e.printStackTrace();
            }
        } else {
            cm.setStatus("fail");
            cm.setMessage("登录失败,检查提交openid、usersource");
        }
        return cm;
    }

    /**
     * 审核代理代理人身份证信息
     *
     * @return
     */
    @RequestMapping(value = "auditAgentIdentity", method = RequestMethod.POST)
    @ResponseBody
    public BaseVo auditAgentIdentity(HttpSession session, @ModelAttribute INSBAgent agent) {
        INSCUser user = (INSCUser) session.getAttribute("insc_user"); //操作用户(cm用户登录了，可以取到用户信息)
        BaseVo bv = new BaseVo();
        if (agent.getApprovesstate() == null) {
            bv.setStatus("2");
            bv.setMessage("审核失败，提交的审核状态为空");
            return bv;
        }
        try {
            insbAgentUserService.auditAgentIdentity(user, agent);
            if (agent.getApprovesstate() == 3) {
                INSBAgent qAgent = insbAgentService.queryById(agent.getId());
                //更新性别
                Integer sex = ModelUtil.getGenderByIdCard(qAgent.getIdno());
                qAgent.setSex(sex.toString());
                /**
                 * 认证时间
                 */
				qAgent.setTesttime(new Date());
                insbAgentService.updateById(qAgent);
                LogUtil.info("用户uuid=" + agent.getId() + " ,证件号= " + agent.getIdno() + " ,性别 = " + sex.toString());
                //实名认证审核通过通知消息
                this.sendMessage_forPassingAuth(qAgent);
            }
            if (agent.getApprovesstate() == 4) {
                INSBAgent qAgent = insbAgentService.queryById(agent.getId());
                //认证失败通知消息
                this.sendMessage_forNotPassingAuth(qAgent);
            }
            bv.setStatus("1");
            bv.setMessage("操作成功");
        } catch (Exception e) {
            bv.setStatus("2");
            bv.setMessage("操作失败");
            e.printStackTrace();
        }
        return bv;
    }

    // 实名认证审核通过通知消息
    public void sendMessage_forPassingAuth(INSBAgent agent) {
        try {
            String agentid = agent.getId();
            //10：实名认证审核通知
            WxMsgTemplateModel wxMsgTemplateModel = new WxMsgTemplateModel();
            wxMsgTemplateModel.setTouser(agent.getOpenid()); // 用户的openid
            wxMsgTemplateModel.setUrl(wxMsgTemplateService.getBaseUrl("WeChat", "minizzburl", "01", agent.getOpenid(), "mycenter"));
            wxMsgTemplateModel.setTemplatetype("10"); //10：绑定手机成功通知
            wxMsgTemplateModel.setFirst("尊敬的" + agent.getName() + "，您的实名认证已通过！");
            wxMsgTemplateModel.setKeyword1("认证成功");
            wxMsgTemplateModel.setKeyword2(new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date()));
            wxMsgTemplateModel.setRemark("您现在可以去提现了。感谢您的支持！");

            wxMsgTemplateService.sendMsgSimplified(wxMsgTemplateModel);
            LogUtil.info("用户uuid=" + agentid + " ,openid= " + agent.getOpenid() + " ,实名认证审核通过通知消息");
        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtil.info("用户uuid=" + agent.getId() + " ,openid= " + agent.getOpenid() + " ,实名认证审核通过通知消息, 发生异常");
        }
    }

    // 认证失败通知消息
    public void sendMessage_forNotPassingAuth(INSBAgent agent) {
        try {
            String agentid = agent.getId();
            //11：认证失败通知
            WxMsgTemplateModel wxMsgTemplateModel = new WxMsgTemplateModel();
            wxMsgTemplateModel.setTouser(agent.getOpenid()); // 用户的openid
            wxMsgTemplateModel.setUrl(wxMsgTemplateService.getBaseUrl("WeChat", "minizzburl", "01", agent.getOpenid(), "mycenter"));
            wxMsgTemplateModel.setTemplatetype("11"); //11：认证失败通知消息
            wxMsgTemplateModel.setFirst("尊敬的" + agent.getName() + "，您申请的实名认证没有通过。");
            wxMsgTemplateModel.setKeyword1("实名认证");
            wxMsgTemplateModel.setKeyword2("认证失败");
            wxMsgTemplateModel.setKeyword3(new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date()));
            wxMsgTemplateModel.setKeyword4(agent.getNoti());
            wxMsgTemplateModel.setRemark("请重新上传资料，务必保证身份信息清晰完整。");

            wxMsgTemplateService.sendMsgSimplified(wxMsgTemplateModel);
            LogUtil.info("用户uuid=" + agentid + " ,openid= " + agent.getOpenid() + " ,认证失败通知消息");
        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtil.info("用户uuid=" + agent.getId() + " ,openid= " + agent.getOpenid() + " ,认证失败通知消息, 发生异常");
        }
    }

    /**
     * 接口：
     * 用户身份验证接口（检查用户提交的认证信息：openid 是否有绑定 手机号）
     */
    @RequestMapping(value = "validateAgentIdentity", method = RequestMethod.GET)
    @ResponseBody
    public BaseVo validateAgentIdentity(@RequestParam(value = "openid", required = true) String openid) {
        //INSCUser user = (INSCUser) session.getAttribute("insc_user"); //操作用户
        BaseVo bv = new BaseVo();
        try {
            INSBAgent agent = new INSBAgent();
            agent.setOpenid(openid);
            INSBAgent agentinfo = insbAgentUserService.validateAgentIdentity(agent);
            if (agentinfo != null && agentinfo.getPhone() != null && !"".equals(agentinfo.getPhone().trim())) {
                bv.setStatus("1");
                bv.setMessage("校验成功，openid 已绑定手机号:" + agentinfo.getPhone());
            } else {
                bv.setStatus("2");
                bv.setMessage("校验失败，openid 未绑定手机号");
            }
        } catch (Exception e) {
            bv.setStatus("2");
            bv.setMessage("校验发生异常");
            e.printStackTrace();
        }
        return bv;
    }

    /**
     * 接口:
     * 用户手机绑定接口（保存用户提交的绑定信息：openid 对应的 手机号）
     */
    @RequestMapping(value = "bindingAgentIdentity", method = RequestMethod.GET)
    @ResponseBody
    public BaseVo bindingAgentIdentity(@RequestParam(value = "openid", required = true) String openid, @RequestParam(value = "phone", required = true) String phone) {
        //INSCUser user = (INSCUser) session.getAttribute("insc_user"); //操作用户(前端用户未登录cm了，取不到用户信息)
        BaseVo bv = new BaseVo();
        try {
            INSBAgent agentQuery = new INSBAgent();
            agentQuery.setPhone(phone); //todo 如果 == 传过来的手机号 AND !=传过来的openid 有数据，说明已注册，不给注册。如是用户重绑，或是没有绑定过的号码，可以绑定。
            List<INSBAgent> list = insbAgentService.queryList(agentQuery);
            if (list != null && list.size() > 0) {
                bv.setStatus("2");
                bv.setMessage("手机绑定失败，手机号已绑定 " + list.get(0).getJobnum());
                return bv;
            }
            INSBAgent agent = new INSBAgent();
            agent.setOpenid(openid);
            agent.setPhone(phone);
            int updateCount = insbAgentUserService.bindingAgentIdentity(agent);
            if (updateCount > 0) {
                //发注册红包
                String id = "";
                try {
                    INSBAgent agentq = new INSBAgent();
                    agentq.setOpenid(openid);
                    agentq.setPhone(phone);
                    INSBAgent agentRs = insbAgentService.queryOne(agentq);
                    if (agentRs == null) {
                        LogUtil.error("用户uuid=" + id + " ,openid= " + openid + " ,绑定手机号成功，记录注册奖励失败，在数据库查询用户信息失败");
                    } else {
                        id = agentRs.getId();
                        //agentid 注册用户UUID，operate=save保存，activitytype=02 注册活动
                        ActivityPrizesVo activityPrizesVo = new ActivityPrizesVo();
                        //agentid 注册ID，referrer 推荐人的ID，operate=save保存，activitytype=02 注册活动
                        activityPrizesVo.setAgentid(id);
                        activityPrizesVo.setOperate("save");
                        activityPrizesVo.setActivitytype("02");
                        LogUtil.info("用户uuid=" + id + " ,openid= " + openid + " ,记录注册绑定奖励");
                        insbMarketActivityService.queryTaskPrizes(activityPrizesVo); //记录推荐奖励
                    }
                } catch (Exception ex) {
                    LogUtil.error("用户uuid=" + id + " ,openid= " + openid + " ,记录注册绑定奖励,发生异常：" + ex.getStackTrace());
                    ex.printStackTrace();
                }

                bv.setStatus("1");
                bv.setMessage("手机绑定成功，openid:" + agent.getOpenid() + " 绑定手机号:" + agent.getPhone());
                // 09：绑定手机成功通知
                this.sendMessage_forBinding(agent);
            } else {
                bv.setStatus("2");
                bv.setMessage("手机绑定失败，openid:" + agent.getOpenid() + "  无记录");
            }
        } catch (Exception e) {
            bv.setStatus("2");
            bv.setMessage("手机绑定发生异常, openid:" + openid + "  未绑定手机号" + phone);
            e.printStackTrace();
        }
        return bv;
    }

    // 绑定手机消息
    private void sendMessage_forBinding(INSBAgent agent) {
        try {
            String agentid = agent.getId();
            String nickName = agent.getNickname() != null ? agent.getNickname() : "";
            //09：绑定手机成功通知
            WxMsgTemplateModel wxMsgTemplateModel = new WxMsgTemplateModel();
            wxMsgTemplateModel.setTouser(agent.getOpenid()); // 用户的openid
            wxMsgTemplateModel.setUrl(wxMsgTemplateService.getBaseUrl("WeChat", "minizzburl", "01", agent.getOpenid(), "mycenter"));
            wxMsgTemplateModel.setTemplatetype("09"); //09：绑定手机成功通知
            wxMsgTemplateModel.setFirst("尊敬的用户" + nickName + "，您的手机号已与微信成功绑定！");
            wxMsgTemplateModel.setKeyword1(ModelUtil.hiddenPhoneSubNum(agent.getPhone()));
            wxMsgTemplateModel.setKeyword2(new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date()));

            //agentid 注册用户UUID，operate=save保存，activitytype=02 注册活动
            ActivityPrizesVo activityPrizesVo = new ActivityPrizesVo();
            //agentid 注册ID，referrer 推荐人的ID，operate=save保存，activitytype=02 注册活动
            activityPrizesVo.setAgentid(agentid);
            activityPrizesVo.setActivitytype("02"); //绑定也是02
            //查询不设置 activityPrizesVo.setOperate("save");
            activityPrizesVo.setGettotal(true);
            String jsonStr = insbMarketActivityService.queryTaskPrizes(activityPrizesVo); //记录推荐奖励
            LogUtil.info("用户uuid=" + agentid + " ,绑定手机消息 ,奖金信息json 串" + jsonStr);
            Map<String, Object> map = JSONObject.fromObject(jsonStr);
            int account_1 = 0;
            int account_2 = 0;
            try {
                account_1 = (Integer) map.get("奖券");
            } catch (Exception ex) {
                //ex.printStackTrace();
            }
            try {
                account_2 = (Integer) map.get("奖金");
            } catch (Exception ex) {
                //ex.printStackTrace();
            }
            String remark = "感谢您的支持，您已成功获得注册奖励 " + (account_1 + account_2) + "元！点击该条消息查看已获奖励。";

            wxMsgTemplateModel.setRemark(remark);

            wxMsgTemplateService.sendMsgSimplified(wxMsgTemplateModel);
            LogUtil.info("用户uuid=" + agentid + " ,openid= " + agent.getOpenid() + " ,绑定手机消息");
        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtil.info("用户uuid=" + agent.getId() + " ,openid= " + agent.getOpenid() + " ,绑定手机消息, 发生异常");
        }
    }

    @RequestMapping(value = "checkusercode", method = RequestMethod.POST)
    @ResponseBody
    public boolean checkUserCode(String agentusercode, String id) {
//		int agentUserCount =  insbAgentUserServiceImpl.selectcountByAgentCode(agentusercode,id);
        int agentUserCount = insbAgentService.selectcountByAgentCode(agentusercode, id);
        if (agentUserCount == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 修改关系�?
     * @param id
     * @return
     */
//	@RequestMapping(value = "agent2agentpermission", method = RequestMethod.GET)
//	public ModelAndView agent2AgentPermission(String id) {
//		INSBAgentpermission ap = insbAgentUserServiceImpl.agentMian2edit(id);
//		ModelAndView result = new ModelAndView("extra/agentpermissionedit");
//		result.addObject("apData", ap);
//		return result;
//	}

    /**
     * 新增或�?�修改代理人权限关系�?
     *
     * @param ap
     * @return
     */
//	@RequestMapping(value = "updateagentpermission", method = RequestMethod.POST)
//	@ResponseBody
//	public BaseVo updateAgentPermission(HttpSession session,@ModelAttribute INSBAgentpermission ap) {
//		BaseVo bv = new BaseVo();
//		INSCUser user = (INSCUser) session.getAttribute("insc_user");
//		try {
//			ap.setOperator(user.getUsercode());
//			ap.setModifytime(new Date());
//			insbAgentUserServiceImpl.updateAgentPermission(ap);
//
//			bv.setStatus("1");
//			bv.setMessage("操作成功�?");
//		} catch (ParseException e) {
//			bv.setStatus("2");
//			bv.setMessage("操作失败�?");
//			e.printStackTrace();
//		}
//
//		return bv;
//	}

    /**
     * 删除代理人权限关�?
     *
     * @param ap
     * @return
     */
//	@RequestMapping(value = "deleteagentpermission", method = RequestMethod.POST)
//	@ResponseBody
//	public BaseVo deleteagentpermission(@ModelAttribute INSBAgentpermission ap) {
//		BaseVo bv = new BaseVo();
//		try {
//
//			insbAgentUserServiceImpl.removeAgentPermission(ap);
//			bv.setStatus("1");
//			bv.setMessage("操作成功�?");
//		} catch (Exception e) {
//			bv.setStatus("2");
//			bv.setMessage("操作失败�?");
//			e.printStackTrace();
//		}
//
//		return bv;
//	}

//	@RequestMapping(value = "saveagentproviderdata", method = RequestMethod.POST)
//	@ResponseBody
//	public BaseVo saveagentproviderdata(HttpSession session,String providerIds,
//										String agentId) {
//		BaseVo bv = new BaseVo();
//		try {
////			INSCUser user = (INSCUser) session.getAttribute("insc_user");
//			insbAgentUserServiceImpl.saveAgentProvider(agentId, providerIds);
//			bv.setStatus("1");
//			bv.setMessage("操作成功�?");
//		} catch (Exception e) {
//			bv.setStatus("2");
//			bv.setMessage("操作失败�?");
//			e.printStackTrace();
//		}
//		return bv;
//	}

    /**
     * @param ids
     * @return
     */
    @RequestMapping(value = "batchdeleteagentbyid", method = RequestMethod.POST)
    @ResponseBody
    public BaseVo batchDeleteAgentUserById(String ids) {
        BaseVo bv = new BaseVo();
        try {
//			String str = insbAgentUserServiceImpl.deleteAgentUserBatch(ids);
            String str = insbAgentService.deleteAgentBatch(ids);
            if (!"".equals(str) && str != null) {
                bv.setStatus("2");
                bv.setMessage(str);
            } else {
                bv.setStatus("1");
                bv.setMessage("操作成功�?");
            }
        } catch (Exception e) {
            bv.setStatus("2");
            bv.setMessage("操作失败�?");
            e.printStackTrace();
        }
        return bv;
    }
    /*@RequestMapping(value = "batchdeleteagentbyid", method = RequestMethod.POST)
    @ResponseBody
	public BaseVo batchDeleteAgentById(String ids) {
		BaseVo bv = new BaseVo();
		try {
			insbAgentUserServiceImpl.deleteAgentBatch(ids);
			bv.setStatus("1");
			bv.setMessage("操作成功�?");
		} catch (Exception e) {
			bv.setStatus("2");
			bv.setMessage("操作失败�?");
			e.printStackTrace();
		}
		return bv;
	}*/

    @RequestMapping(value = "main2detail", method = RequestMethod.GET)
    public ModelAndView main2detail(String id) {
        ModelAndView result = new ModelAndView("extra/agentuserdetailinfo");
        INSBAgent agentuser = insbAgentUserService.queryById(id);
        String stationdeptname = "";
        if (!"".equals(agentuser.getStationid()) && agentuser.getStationid() != null) {
            INSCDept dept = deptService.queryById(agentuser.getStationid());
            stationdeptname = dept.getShortname();
            result.addObject("stationdeptname", stationdeptname);
        }
        result.addObject("agentuser", agentuser);
        return result;
    }

    /**
     * 接口：
     * 用户信息查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "agentuserdetail", method = RequestMethod.GET)
    @ResponseBody
    public BaseVo agentuserdetail(String id) {
        BaseVo bv = new BaseVo();
        INSBAgent agentuser = null;
        try {
            agentuser = insbAgentUserService.queryById(id);
            bv.setResult(agentuser);
            bv.setStatus("1");
            bv.setMessage("查询成功");
        } catch (Exception ex) {
            ex.printStackTrace();
            bv.setStatus("2");
            bv.setMessage("查询失败，发生异常");
        }
        return bv;
    }

    /**
     * 接口描述：文件上传 用户认证身份证正面图片
     * 参照：	AppRegisteredController /mobile/registered/fileUpLoadBase64
     * 参数：FileUploadBase64ParamIdentify.jobNum 用户uuid
     *
     * @param
     */
    @RequestMapping(value = "/agentUserIdentityFileUpLoadBase64", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel agentUserIdentityFileUpLoadBase64(@RequestBody FileUploadBase64ParamIdentify param)
            throws ControllerException {
        CommonModel model = new CommonModel();
        if (param.getJobNum() == null || "".equals(param.getJobNum().trim())) {
            model.setStatus("fail");
            model.setMessage("提交代用户id为空");
            return model;
        } else {

        	model = insbAgentUserService.fileUpLoadBase64(request, param.getFile(), param.getFileName(), param.getFileType(), param.getFileDescribes(), param.getJobNum(), param.getTaskId());
        	if(model.getStatus().equals("success")) {
        		
        		param.setIdnotype("0");
        		INSBAgent agent = new INSBAgent();
        		agent.setId(param.getJobNum());
        		agent.setName(param.getName());
        		agent.setIdno(param.getIdno());
        		agent.setIdnotype(param.getIdnotype());
        		agent.setApprovesstate(2);//上传身份证，状态2认证中
        		insbAgentUserService.updateById(agent);
        	}
        	return model;
        }
    }

    /**
     * 接口描述：文件上传 用户认证身份证正面图片(处理微信上传图片)
     * 参照：	AppRegisteredController /mobile/registered/fileUpLoadWeChat
     * 参数：FileUploadBase64ParamIdentify.jobNum 用户uuid
     *
     * @param
     */
    @RequestMapping(value = "/agentUserIdentityFileUpLoadWeChat", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel agentUserIdentityFileUpLoadWeChat(@RequestBody FileUploadBase64ParamIdentify param)
            throws ControllerException {
        CommonModel model = new CommonModel();
        if (param.getJobNum() == null || "".equals(param.getJobNum().trim())) {
            model.setStatus("fail");
            model.setMessage("提交代用户id为空");
            return model;
        } else {

        	model = insbAgentUserService.fileUpLoadWeChat(request, param.getMediaid(), param.getFileName(), param.getFileType(), param.getFileDescribes(), param.getJobNum(), param.getTaskId());
        	if(model.getStatus().equals("success")) {
        		
        		param.setIdnotype("0");
        		INSBAgent agent = new INSBAgent();
        		agent.setId(param.getJobNum());
        		agent.setName(param.getName());
        		agent.setIdno(param.getIdno());
        		agent.setIdnotype(param.getIdnotype());
        		agent.setApprovesstate(2);//上传身份证，状态2认证中
        		insbAgentUserService.updateById(agent);
        	}
        	return model;
        }

    }

    @RequestMapping(value = "queryAgentAuthInfo", method = RequestMethod.GET)
    @ResponseBody
    public BaseVo queryAgentAuthInfo(String id) {
        BaseVo bv = new BaseVo();
        INSBAgent agentuser = insbAgentUserService.queryById(id);
        bv.setResult(agentuser);
        if (agentuser == null) {
            //approvesstate 认证状态  1-未认证  2-认证中  3-认证通过  4-认证失败
            bv.setStatus("1");
            bv.setMessage("未认证");
        } else if (agentuser.getApprovesstate() == null || agentuser.getApprovesstate() == 1) {
            bv.setStatus("1");
            bv.setMessage("未认证");
        } else if (agentuser.getApprovesstate() == 2) {
            bv.setStatus("2");
            bv.setMessage("认证中");
        } else if (agentuser.getApprovesstate() == 3) {
            bv.setStatus("3");
            bv.setMessage("认证通过");
        } else if (agentuser.getApprovesstate() == 4) {
            bv.setStatus("4");
            bv.setMessage("认证失败");
        }
        return bv;
    }


    /**
     * 查询手机号 是否已在系统内注册了
     * true 系统已经有绑定提交的手机
     * false 系统还没有绑定提交的手机
     *
     * @param phone 手机号
     */
    @RequestMapping(value = "/checkphone", method = RequestMethod.GET)
    @ResponseBody
    public CommonModel checkphone(@RequestParam("phone") String phone) {
        CommonModel cm = new CommonModel();
        if (phone == null || phone.equals("null") || phone.trim().equals("")) {
            cm.setStatus("fail");
            cm.setMessage("提交的手机号为空");
            return cm;
        } else {
            // INSBAgent agent = new INSBAgent();
            //agent.setPhone(phone);
            //List<INSBAgent> list = insbAgentService.queryList(agent);
            boolean isAgentPhone = insbAgentUserService.checkIsAgentPhone(phone.trim());
            if (isAgentPhone) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("status", "1");
                map.put("message", "手机号:" + phone + "已经是体系内代理人绑定手机");
                cm.setStatus("success");
                cm.setMessage("查询成功");
                cm.setBody(map);
                return cm;
            }
            boolean isMinizzbUserPhone = insbAgentUserService.checkIsMinizzbUserPhone(phone.trim());
            if (isMinizzbUserPhone) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("status", "2");
                map.put("message", "手机号:" + phone + "已经是minizzb用户绑定手机");
                cm.setStatus("success");
                cm.setMessage("查询成功");
                cm.setBody(map);
                return cm;
            } else {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("status", "0");
                map.put("message", "手机号非体系内代理人和minizzb用户绑定手机号");
                cm.setStatus("success");
                cm.setMessage("查询成功");
                cm.setBody(map);
                return cm;
            }
        }
    }


    /**
     * 接受推荐
     *
     * @param recommend
     * @return
     */
    @RequestMapping(value = "/acceptRecommend", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel acceptRecommend(@ModelAttribute INSBAgentRecommend recommend) {
        LogUtil.info("接口/acceptRecommend，接收推荐，接收到的数据是："+ com.alibaba.fastjson.JSONObject.toJSONString(recommend));
        CommonModel cm = new CommonModel();
        String message = "";
        String referreropenid = recommend.getReferreropenid();
        String presenteeopenid = recommend.getPresenteeopenid();
        String recommendtype = recommend.getRecommendtype();
        String phone = recommend.getPhone();
        String weixinheadphotourl = recommend.getWeixinheadphotourl();
        String nickname = recommend.getNickname();
        if (referreropenid == null || referreropenid.trim().equals("null") || referreropenid.trim().equals("")) {
            message = message + " 推荐人openid为空 ";
        }
        if (presenteeopenid == null || presenteeopenid.trim().equals("null") || presenteeopenid.trim().equals("")) {
            message = message + " 被推荐人openid为空 ";
        }
        if (recommendtype == null || recommendtype.trim().equals("null") || recommendtype.trim().equals("")) {
            message = message + " 推方式为空,(1-微信分享，2-QQ分享) ";
        }
        if (phone == null || phone.trim().equals("null") || phone.trim().equals("")) {
            message = message + " 推方手机号为空";
        }
        if (message.length() > 0) {
            message = "接受推荐,提交的" + message;
            cm.setMessage(message);
            cm.setStatus("fail");
            return cm;
        } else {
            INSBAgent agent_1 = new INSBAgent();
            agent_1.setOpenid(presenteeopenid);
            INSBAgent agentQueryResult_1 = insbAgentService.queryOne(agent_1);//查询被推荐人openid，是否已经是注册用户
            INSBAgent agent_2 = new INSBAgent();
            agent_2.setPhone(phone);
            INSBAgent agentQueryResult_2 = insbAgentService.queryOne(agent_2);//查询被推荐人手机号，是否已经是注册用户
            String message_query = "";
            if (agentQueryResult_1 != null && agentQueryResult_1.getPhone() != null && !"".equals(agentQueryResult_1.getPhone().trim())) {
                LogUtil.info(" 被推荐人微信(openid)已经是注册用户,且已经绑定手机号，推荐关系不成立。被推荐openid= " + presenteeopenid + " 已绑定手机号= " + agentQueryResult_1.getPhone());
                message_query = message_query + " 被推荐人微信(openid)已经是注册用户，且已绑定手机号: " + agentQueryResult_1.getPhone();
            } else {
                LogUtil.info("  被推荐人微信(openid)还不是注册用户，或 被推荐人微信(openid)已经是注册用户,但未绑定手机号，可以建立推荐关系。被推荐openid= " + presenteeopenid + " 待绑定手机号= " + phone);
            }
            if (agentQueryResult_2 != null) {
                LogUtil.info(" 被推荐人的手机号已经是注册用户，推荐关系不成立。被推荐openid= " + presenteeopenid +" 手机号= " + phone);
                message_query = message_query + " 被推荐人的手机号已经是注册用户，推荐关系不成立。被推荐openid= " + presenteeopenid +" 手机号= " + phone;
            }
            if (message_query.length() > 0) {
                message_query = "接受推荐,提交的" + message_query;
                cm.setMessage(message_query);
                cm.setStatus("fail");
                return cm;
            }

            INSBAgent agent_recommend = new INSBAgent();
            agent_recommend.setOpenid(referreropenid);
            INSBAgent agent_recommend_re = insbAgentService.queryOne(agent_recommend);//按推荐人openid，查询推荐人信息，取推荐人id
            String recommendId = "";
            if(agent_recommend_re != null)  {recommendId = agent_recommend_re.getId() ; }else{
                LogUtil.error("推荐人openid = " + referreropenid + " ，被推荐openid= " + presenteeopenid + "，根据推荐人openid 查询推荐uuid信息出错，无法建立推荐关系。");
                recommendId = "";
            }

            //注册、记录推荐、处理推荐奖励   登录(如果渠道在用户登录地区没配置代理人或供应商登录失败)
            INSBAgent agentobj = (INSBAgent) recommend;
            agentobj.setOpenid(presenteeopenid);   //修改的记录是被推荐人的INSBAgent
            agentobj.setReferrerid(recommendId);
            agentobj.setUsersource("minizzb");
            agentobj.setPhone(phone); //前端必须是已校检的手机号
            agentobj.setWeixinheadphotourl(weixinheadphotourl);
            agentobj.setNickname(nickname);
            LogUtil.info("推荐入库数据: " + com.alibaba.fastjson.JSONObject.toJSONString(agentobj));
            INSBAgent agentInfo = insbAgentUserService.saveOrUpdateAgentWeiXin(agentobj);//注册、记录推荐、

            //前端已校验推荐过来的被推荐人手机号，这里直接绑定手机号。手机号不在修改用户信息的时候修改，防止前端修改用户信息的时候，随意提交手机号。
            INSBAgent agentBind = new INSBAgent();
            agentBind.setOpenid(presenteeopenid);//被推荐人openid
            agentBind.setPhone(phone);//被推荐人手机号
            int updateCount = insbAgentUserService.bindingAgentIdentity(agentBind);
            LogUtil.error("用户uuid=" + recommendId + " ,推荐人openid = " + referreropenid + " ，被推荐openid= " + presenteeopenid + " , 被推荐人手机号 = " + phone + " ,绑定手机号修改记录数：" + updateCount);

            //agentid 注册用户UUID，referrer 推荐人的UUID，operate=save保存，activitytype=02 注册活动
            ActivityPrizesVo activityPrizesVo = new ActivityPrizesVo();
            //agentid 注册ID，referrer 推荐人的ID，operate=save保存，activitytype=02 注册活动
            activityPrizesVo.setAgentid(agentInfo.getId());
            activityPrizesVo.setReferrerid(agent_recommend_re != null ? agent_recommend_re.getId() : "");
            activityPrizesVo.setOperate("save");
            activityPrizesVo.setActivitytype("02");
            try {
                LogUtil.info("用户uuid=" + recommendId + " ,记录推荐奖励 agentid = " + activityPrizesVo.getAgentid() + " , referrerid = " + activityPrizesVo.getReferrerid());
                insbMarketActivityService.queryTaskPrizes(activityPrizesVo); //记录推荐奖励
            } catch (Exception ex) {
                LogUtil.error("用户uuid=" + recommendId + " ,记录推荐奖励 agentid = " + activityPrizesVo.getAgentid() + " , referrerid = " + activityPrizesVo.getReferrerid() + " 发生异常：" + ex.getStackTrace());
                ex.printStackTrace();
            }
            LogUtil.info(" 推荐完成。推荐人openid = "+referreropenid+" 被推荐openid= " + presenteeopenid +" 手机号= " + phone);

            //INSBAgentUser au = (INSBAgentUser) agentInfo;
            return this.loginminizzb(agentInfo,recommend.getCity());//add city
        }
    }

    /**
     * 接口：
     * 被推荐用户信息查询
     *
     * @param presenteeId
     * @return
     */
    @RequestMapping(value = "presenteedetail", method = RequestMethod.GET)
    @ResponseBody
    public BaseVo presenteedetail(@RequestParam("presenteeId")String presenteeId) {
        BaseVo bv = new BaseVo();
        if (presenteeId == null || presenteeId.equals("null") || presenteeId.trim().equals("")) {
            bv.setStatus("2");
            bv.setMessage("提交的被推荐人id为空");
            return bv;
        } else {
            Map<String, Object> reMap = new HashMap<String, Object>();
            INSBAgent resultAgent = insbAgentService.queryById(presenteeId);
            if(resultAgent== null){
                bv.setStatus("2");
                bv.setMessage("数据库不存在被推荐人信息，userid = "+presenteeId);
                return bv;
            }else{
                try {
                    resultAgent.setPhone(ModelUtil.hiddenPhoneSubNum(resultAgent.getPhone()));
                    resultAgent.setRegistertimestr(DateUtil.toDateTimeString(resultAgent.getRegistertime()));
                    //查询有赏金的保单、赏金。（目前只有商业险有赏金）
                    Map<String,Object> gotPrizesPolicy = insbAgentPrizeService.queryGotPrizesPolicy(presenteeId);

                    reMap.put("presentee", resultAgent);
                    reMap.put("gotPrizesPolicy", gotPrizesPolicy);

                    bv.setStatus("1");
                    bv.setMessage("查询成功");
                    bv.setResult(reMap);
                    return bv;
                }catch (Exception ex){
                    ex.printStackTrace();
                    bv.setStatus("2");
                    bv.setMessage("查询发生异常，userid = " + presenteeId);
                    return bv;
                }
            }
        }

    }

    /**
     * 接口：
     * openid 取 uuid
     *
     * @param openid
     * @return
     */
    @RequestMapping(value = "openidQueryUserid", method = RequestMethod.GET)
    @ResponseBody
    public BaseVo openidQueryUserid(@RequestParam("openid")String openid) {
        BaseVo bv = new BaseVo();
        if (openid == null || openid.equals("null") || openid.trim().equals("")) {
            bv.setStatus("2");
            bv.setMessage("提交的openid为空");
            return bv;
        } else {
            Map<String, Object> reMap = new HashMap<String, Object>();
            INSBAgent agent = insbAgentUserService.selectByOpenid(openid);
            if(agent != null) {
                reMap.put("userId", agent.getId());
                bv.setStatus("1");
                bv.setMessage("查询成功");
                bv.setResult(reMap);
                return bv;
            }else {
                bv.setStatus("2");
                bv.setMessage("数据库没保存到openid = "+openid +" 的记录。");
                return bv;
            }
        }
    }


    /**
     * 被推荐人 列表
     *
     * @param referrerid 推荐人uuid
     */
    @RequestMapping(value = "/presenteeList", method = RequestMethod.GET)
    @ResponseBody
    public BaseVo presenteeList(@RequestParam("referrerid") String referrerid) {
        BaseVo bv = new BaseVo();
        if (referrerid == null || referrerid.equals("null") || referrerid.trim().equals("")) {
            bv.setStatus("2");
            bv.setMessage("提交的推荐人id为空");
            return bv;
        } else {
            List<Map<String, Object>> recommendlist = insbAgentUserService.presenteeList(referrerid);
            INSBAgent resultAgent = insbAgentService.queryById(referrerid); //按推荐人id查询推荐人信息

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("activitytype", "04");//"04"推荐活动
            param.put("agentid", resultAgent != null ? resultAgent.getId() : ""); //用户uuid
            param.put("gettotal", true); //获取总额
            //param.put("status",); //不传值返回全部状态的资料
            int totalnum = 0;
            try {
                String totalnumStr = insbAgentPrizeService.queryPrizes(param); //查询总金额
                totalnum = Integer.parseInt(totalnumStr != null ? totalnumStr.trim() : "0");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            Map<String, Object> reMap = new HashMap<String, Object>();
            reMap.put("totalnum", totalnum); //总金额
            reMap.put("recommendlist", recommendlist);
            reMap.put("recommendAmount", recommendlist != null ? recommendlist.size() : 0);
            bv.setStatus("1");
            bv.setMessage("查询成功");
            bv.setResult(reMap);
            return bv;
        }
    }
    
}

