package com.zzb.conf.controller;


import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCCodeService;
import com.cninsure.system.service.INSCDeptService;
import com.common.PagingParams;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBCarinfohis;
import com.zzb.cm.entity.INSBCarmodelinfo;
import com.zzb.cm.entity.INSBCarmodelinfohis;
import com.zzb.cm.entity.INSBFilebusiness;
import com.zzb.cm.entity.INSBFilelibrary;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.entity.INSBPerson;
import com.zzb.cm.service.INSBCarinfohisService;
import com.zzb.cm.service.INSBCarkindpriceService;
import com.zzb.cm.service.INSBCarmodelinfoService;
import com.zzb.cm.service.INSBCarmodelinfohisService;
import com.zzb.cm.service.INSBCommonWorkflowTrackservice;
import com.zzb.cm.service.INSBOrderService;
import com.zzb.conf.entity.INSBOperatorcomment;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.conf.entity.INSBUsercomment;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.conf.service.INSBOperatorcommentService;
import com.zzb.conf.service.INSBPolicyManageService;
import com.zzb.conf.service.INSBPolicyitemService;
import com.zzb.conf.service.INSBProviderService;
import com.zzb.conf.service.INSBUsercommentService;
import com.zzb.model.PolicyDetailedModel;
import com.zzb.model.PolicyQueryModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/policyitem/*")
public class INSBPolicyItemController extends BaseController{

    public static final String USE_PROPS = "UseProps";
    public static final String USER_TYPE = "UserType";
    @Resource
	private INSBPolicyManageService insbPolicyManageService;
	@Resource
	private INSCDeptService deptService;
	@Resource
	private INSCCodeService codeService;
	@Resource
	private INSBProviderService providerService;
	@Resource
	private INSBCarmodelinfoService insbCarmodelinfoService;
	@Resource
	private INSBPolicyitemService insbPolicyitemService;
	@Resource
	private INSBOrderService insbOrderservice;
	@Resource
	private INSBCarkindpriceService insbCarkindpriceService;
	@Resource
	private INSBAgentService service;
	@Resource
	private INSBUsercommentService insbUsercommentService;
	@Resource
	private INSBOperatorcommentService insbOperatorcommentService;
	@Resource
	private INSBCommonWorkflowTrackservice insbCommonWorkflowTrackservice;
	@Resource
	private INSBCarmodelinfohisService insbCarmodelinfohisService;
	@Resource
	private INSBCarinfohisService insbCarinfohisService;
	
	@RequestMapping(value="/query",method=RequestMethod.GET)
	@ResponseBody
	public ModelAndView query() throws ControllerException{
		ModelAndView mav = new ModelAndView("zzbconf/insbPolicyManagement");
		//保单状态
		List<INSCCode> codeList = codeService.queryINSCCodeByCode("policystatus", "policystatus");
		mav.addObject("codeList", codeList);
		return mav;
	}
	/**
	 * 查询保单列表
	 * @param para
	 * @param queryModel
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/querylist",method=RequestMethod.GET)
	@ResponseBody
	public String queryList(HttpSession session,@ModelAttribute PagingParams para,@ModelAttribute PolicyQueryModel queryModel) throws ControllerException{
		/**
		 * 查询获得机构树某节点下所有的id
		 */
		String deptid= "";
		if(queryModel != null){
			deptid = queryModel.getDeptid();
		}
		List<String> deptids = new ArrayList<String>();
		if(!"".equals(deptid)&&null!=deptid){
			INSCDept parentdept = deptService.queryById(deptid);	
			//deptids = service.queryWDidsByPatId(parentdept.getId(),"");
			//queryModel.setDeptids(deptids);

			//部门id转换成deptinnercode
			queryModel.setDeptid(parentdept == null ? "" : parentdept.getDeptinnercode());
		}
		Map<String, Object> map = BeanUtils.toMap(queryModel,para);
		INSCUser user = (INSCUser) session.getAttribute("insc_user");
		String comcode = user.getUserorganization();
		return insbPolicyManageService.queryPagingList(map,comcode);
	}
	/**
	 * 机构数
	 * @param parentcode
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/querydepttree",method=RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> queryList(@RequestParam(value="id",required=false) String parentcode) throws ControllerException{
		return deptService.queryTreeList(parentcode);
	}
	
	/**
	 * 供应商树
	 * @param parentcode
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/queryprovidertree",method=RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> queryprovidertree(@RequestParam(value="id",required=false) String parentcode) throws ControllerException{
		return providerService.queryTreeList(parentcode);
	}
	/**
	 * 保单详细信息
	 * @param id 保单id
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/querydetail",method=RequestMethod.POST)
	@ResponseBody
	public ModelAndView queryDetail(@RequestParam(value="id") String id,@RequestParam(value="risktype",required=false) String risktype) throws ControllerException{
		LogUtil.info("id-->>"+id+",risktype-->>"+risktype);
		ModelAndView mav = new ModelAndView("zzbconf/insbPolicyDetailed");
		PolicyDetailedModel detailedModel = insbPolicyManageService.queryPolictDetailedByPId(id);
		mav.addObject("detailedModel", detailedModel);
		mav.addObject("risktype",risktype);
		INSBPolicyitem po = insbPolicyitemService.queryById(id);
		String orderid = ""; 
		String taskid ="";
		String prvid ="";
		String orderstatus="";
		if(po!=null){
			taskid = po.getTaskid();
			orderid = po.getOrderid();
			INSBOrder order = insbOrderservice.queryById(orderid);
			if(order!=null){
				prvid = order.getPrvid();
				orderstatus = order.getOrderstatus();
			}
		}
		Map<String,Object> insConfigInfo = insbCarkindpriceService.getCarInsConfigByInscomcode(prvid,taskid);
		Map<String,Object> carInsTaskInfo = new HashMap<String, Object>();
		carInsTaskInfo.put("insConfigInfo",insConfigInfo);
		mav.addObject("carInsTaskInfo", carInsTaskInfo);
		//影像信息
		List<PolicyDetailedModel> imginfo = insbPolicyManageService.queryImgInfo(orderid);
		//给用户备注信息
		List<INSBUsercomment> usercomment  = new ArrayList<INSBUsercomment>();
		//给操作员备注信息
		List<INSBOperatorcomment> operatorcommentList = new ArrayList<INSBOperatorcomment>();
		List<String> node =  Arrays.asList(new String[]{"3","4"});
		if(node.contains(orderstatus)){
			operatorcommentList = insbOperatorcommentService.getOperatorCommentByMaininstanceid(taskid, prvid);
		}else{
			operatorcommentList = insbOperatorcommentService.getOperatorCommentByMaininstanceid(taskid, prvid);
		}
		//当前节点之前的备注信息（入参： 主流程实例taskid ; prvid 保险公司code ; dqtaskcode 当前节点code）
	    usercomment = insbUsercommentService.getNearestUserComment(taskid, prvid, null);
		mav.addObject("usercomment", usercomment);
		mav.addObject("operatorcommentList", operatorcommentList);
		mav.addObject("imginfo", imginfo);
		mav.addObject("taskid", taskid);
		
		//电子保单
		mav.addObject("jqElecPolicyFilePath", insbPolicyitemService.getElecPolicyFilePath(taskid, "1"));
		mav.addObject("syElecPolicyFilePath", insbPolicyitemService.getElecPolicyFilePath(taskid, "0"));
		return mav;
	}
	/**
	 * 车辆信息
	 * @param taskid
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/querycarinfo",method=RequestMethod.GET)
	@ResponseBody
	public ModelAndView queryCarInfoByTaskid(@RequestParam(value="taskid") String taskid,@RequestParam(value="inscomcode",required=false)String inscomcode) throws ControllerException{
		ModelAndView mav = new ModelAndView("zzbconf/showComInfo");
		INSBCarinfohis carinfohis = new INSBCarinfohis();
		INSBCarinfohis carhis = null;
		carinfohis.setTaskid(taskid);
		carinfohis.setInscomcode(inscomcode);
		List<INSBCarinfohis> carhislist = insbCarinfohisService.queryList(carinfohis);
		if(carhislist!=null&&carhislist.size()>0){
			carhis = carhislist.get(0);
		}
		INSBCarinfo carinfo = insbPolicyManageService.queryCarInfoByTaskid(taskid);
        Map<String, Object> carInfoMap = BeanUtils.toMap(carhis != null ? carhis : carinfo);
        getProperty(carInfoMap);
        mav.addObject("carinfo", carInfoMap);
        //车辆信息
		mav.addObject("flag", "1");
		//车型信息表
		if(carinfohis!=null){
			INSBCarmodelinfohis carmodelinfohis = new INSBCarmodelinfohis();
			INSBCarmodelinfohis his = null;
			carmodelinfohis.setCarinfoid(carinfo.getId());
			carmodelinfohis.setInscomcode(inscomcode);
			List<INSBCarmodelinfohis> hislist = insbCarmodelinfohisService.queryList(carmodelinfohis);
			if (hislist!=null&&hislist.size()>0) {
				his = hislist.get(0);
			}
			if(his!=null){
				mav.addObject("carmodelinfo",his);
			}else{
				INSBCarmodelinfo carmodelinfo = new INSBCarmodelinfo();
				carmodelinfo.setCarinfoid(carinfo.getId());
				mav.addObject("carmodelinfo",insbCarmodelinfoService.queryOne(carmodelinfo));
			}
		}
		return mav;
	}

    private void getProperty(Map<String, Object> carInfoMap) {
        INSCCode t = new INSCCode();
        t.setCodetype(USE_PROPS);
        t.setParentcode(USE_PROPS);
        Object carProperty = carInfoMap.get("carproperty");
        if (carProperty != null) {
            t.setCodevalue(carProperty.toString());
            t = codeService.queryOne(t);
            carInfoMap.put("carPropertyName", t != null ? t.getCodename() : null);
        }

        t = new INSCCode();
        t.setCodetype(USER_TYPE);
        t.setParentcode(USER_TYPE);
        Object property = carInfoMap.get("property");
        if (property != null) {
            t.setCodevalue(property.toString());
            t = codeService.queryOne(t);
            carInfoMap.put("propertyName", t != null ? t.getCodename() : null);
        }
    }

    /**
	 * 关系人信息
	 * @param taskid, flag insure 被保险人 policy 投保人  carower 车主
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/querypeopleinfo",method=RequestMethod.GET)
	@ResponseBody
	public ModelAndView queryPersonInfoByTaskid(@RequestParam(value="taskid") String taskid,@RequestParam(value="show",required=false) String flag,@RequestParam(value="inscomcode",required=false)String inscomcode) throws ControllerException{
		ModelAndView mav = new ModelAndView("zzbconf/showComInfo");
		INSBPerson insbPerson = insbPolicyManageService.queryPersonInfoByTaskid(taskid,flag,inscomcode);
		String idcardtype = insbPerson.getIdcardtype() == null ? "0" : insbPerson.getIdcardtype().toString();
		String codeName = codeService.transferValueToName("CertKinds", "CertKinds", idcardtype);
		mav.addObject("insbPerson", insbPerson);
		mav.addObject("codeName", codeName);
		//人员信息
		mav.addObject("flag", "2");
		return mav;
	}
	/**
	 * 车辆驾驶人信息
	 * @param taskid
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/querydriverinfo",method=RequestMethod.GET)
	@ResponseBody
	public ModelAndView queryDriverInfoByTaskid(@RequestParam(value="taskid") String taskid) throws ControllerException{
		ModelAndView mav = new ModelAndView("zzbconf/showComInfo");
		List<INSBPerson> driverList = insbPolicyManageService.queryDriverPersonByTaskid(taskid);
		mav.addObject("driverList", driverList);
		//驾驶人
		mav.addObject("flag", "3");
		return mav;
	}
}
