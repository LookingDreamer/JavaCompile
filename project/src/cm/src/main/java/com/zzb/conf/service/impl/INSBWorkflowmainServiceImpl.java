package com.zzb.conf.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.Resource;

import com.common.WorkflowFeedbackUtil;
import com.zzb.cm.service.INSBWorkflowDataService;
import com.zzb.conf.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.jobpool.DispatchService;
import com.cninsure.jobpool.Task;
import com.common.HttpClientUtil;
import com.common.HttpSender;
import com.common.TaskConst;
import com.common.redis.CMRedisClient;
import com.common.redis.Constants;
import com.common.redis.IRedisClient;
import com.zzb.app.service.AppQuotationService;
import com.zzb.chn.service.CHNChannelService;
import com.zzb.cm.controller.vo.MyTaskVo;
import com.zzb.cm.dao.INSBCarinfoDao;
import com.zzb.cm.dao.INSBCarmodelinfoDao;
import com.zzb.cm.dao.INSBLastyearinsurestatusDao;
import com.zzb.cm.dao.INSBQuoteinfoDao;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.dao.INSBRulequeryotherinfoDao;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBCarmodelinfo;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.cm.entity.INSBRulequeryotherinfo;
import com.zzb.cm.service.INSBAutomaticVerifyService;
import com.zzb.cm.service.INSBCommonQuoteinfoService;
import com.zzb.conf.component.MessageManager;
import com.zzb.conf.component.SupplementCache;
import com.zzb.conf.dao.INSBAutoconfigshowDao;
import com.zzb.conf.dao.INSBUsercommentDao;
import com.zzb.conf.dao.INSBWorkflowmainDao;
import com.zzb.conf.dao.INSBWorkflowmaintrackDao;
import com.zzb.conf.dao.INSBWorkflowmaintrackdetailDao;
import com.zzb.conf.dao.INSBWorkflowsubDao;
import com.zzb.conf.dao.INSBWorkflowsubtrackDao;
import com.zzb.conf.entity.INSBAgreement;
import com.zzb.conf.entity.INSBUsercomment;
import com.zzb.conf.entity.INSBWorkflowmain;
import com.zzb.conf.entity.INSBWorkflowmaintrackdetail;
import com.zzb.conf.entity.INSBWorkflowsub;
import com.zzb.conf.entity.INSBWorkflowsubtrack;
import com.zzb.mobile.service.AppInsuredQuoteService;
import com.zzb.mobile.service.AppOtherRequestService;
import com.zzb.model.WorkFlow4TaskModel;

import net.sf.json.JSONObject;

@Service
@Transactional
public class INSBWorkflowmainServiceImpl extends BaseServiceImpl<INSBWorkflowmain> implements INSBWorkflowmainService {

    @Resource
    private INSBCommonQuoteinfoService commonQuoteinfoService;
    @Resource
	private INSBAutomaticVerifyService automaticVerifyService;
	@Resource
	private INSBWorkflowmainDao insbWorkflowmainDao;
	@Resource
	private INSBWorkflowsubService workflowsubService;
	@Resource
	private INSBWorkflowsubDao insbWorkflowsubDao;
	@Resource
	private INSBQuoteinfoDao insbquoteinfoDao;
	@Resource
	private INSBAutoconfigshowDao autoconfigshowDao;
	@Resource
	private INSBWorkflowmaintrackDao workflowmaintrackDao;
	@Resource
	private DispatchService dispatchService;
	@Resource
	private INSBWorkflowmaintrackdetailDao workflowmaintrackdetailDao;
	@Resource
	private MessageManager messageManager;
	@Resource
	private INSBCarinfoDao insbCarinfoDao;
	@Resource
	private AppOtherRequestService appOtherRequestService;
	@Resource
	private INSBQuotetotalinfoDao insbQuotetotalinfoDao;
	@Resource
	private AppInsuredQuoteService appInsuredQuoteService;
	@Resource
	private INSBAutoconfigshowService autoconfigshowService;
	@Resource
	private CHNChannelService channelService;
	@Resource
	private ThreadPoolTaskExecutor taskthreadPool4workflow;
	@Resource
	private IRedisClient redisClient;
	@Resource
	private INSBQuoteinfoDao insbQuoteinfoDao;
	@Resource 
	private INSBWorkflowsubtrackDao insbWorkflowsubtrackDao;
	@Resource
	private INSBUsercommentDao insbUsercommentDao;
	@Resource
	private INSBAgreementService insbAgreementService;
	@Resource
	private INSBCarmodelinfoDao insbCarmodelinfoDao;
	@Resource
	private AppQuotationService appQuotationService;
	@Resource
	private INSBRealtimetaskService insbRealtimetaskService;
	@Resource
	private INSBWorkflowDataService workflowDataService;

	private static String WORKFLOW = "";
	private static int RULEQUERYKEY = 15*24*3600;
	
	static {
		// 读取相关的配置
		ResourceBundle resourceBundle = ResourceBundle.getBundle("config/config");
		WORKFLOW = resourceBundle.getString("workflow.url");
	}
	@Override
	protected BaseDao<INSBWorkflowmain> getBaseDao() {
		return insbWorkflowmainDao;
	}

	@Override
	public void updateWorkFlowMainData(INSBWorkflowmain workflowModel,String fromOperator) {
		
		try {
			messageManager.sendMessage4Agent("admin", workflowModel.getInstanceid(),null, workflowModel.getTaskcode());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 判断当前是新增还是修改
		Map<String, String> oldMain = insbWorkflowmainDao.selectIdByInstanceId4Task(workflowModel.getInstanceid());
		if (oldMain != null && !oldMain.isEmpty()) {
			if ("Closed".equalsIgnoreCase(oldMain.get("taskstate"))) {
				LogUtil.error(workflowModel.getInstanceid()+"流程轨迹已关闭，本次不再处理");
				return;
			}
		}

		// 获取备注、操作人
		if (StringUtil.isEmpty(workflowModel.getTaskfeedback())) {
			Map<String, String> rdValue = WorkflowFeedbackUtil.getWorkflowFeedback(workflowModel.getInstanceid(), null,
					workflowModel.getTaskcode(), workflowModel.getTaskstate(), workflowModel.getTaskname());
			if (rdValue != null && !rdValue.isEmpty()) {
				if (StringUtil.isNotEmpty(rdValue.get("tfb"))) {
					workflowModel.setTaskfeedback(rdValue.get("tfb"));
				}
				if (StringUtil.isNotEmpty(rdValue.get("opt"))) {
					workflowModel.setOperator(rdValue.get("opt"));
				}
			}
		}

		//修改
		if (null != oldMain && !oldMain.isEmpty()) {
			if ("Completed".equals(workflowModel.getTaskstate())) {
				if (StringUtil.isEmpty(workflowModel.getOperator())) {
					workflowModel.setOperator(oldMain.get("operator"));
				}
				workflowModel.setGroupid(oldMain.get("groupid"));
			}

			String operator1 = workflowModel.getOperator();
			String groupid1 = workflowModel.getGroupid();

			//更新轨迹信息
			this.updateTrackTable(workflowModel);
			String operator2 = workflowModel.getOperator();
			String groupid2 = workflowModel.getGroupid();

			//人工任务设置操作人
			if (workflowDataService.isManWork(workflowModel.getTaskcode())) {
				workflowModel.setOperator(operator1);
				workflowModel.setGroupid(groupid1);
			}

			workflowModel.setId(oldMain.get("id"));
			//如果是完成节点，当前节点taskcode已不是要完成的节点的taskcode则不更新workflowmain表处理
			if ("Completed".equals(workflowModel.getTaskstate()) && !workflowModel.getTaskcode().equals(oldMain.get("taskcode"))) {
				LogUtil.error("Completed,taskid=%s,不更新insbWorkflowmain数据taskcode=%s不一致出错：当前是%s" ,
						workflowModel.getInstanceid(), workflowModel.getTaskcode(), oldMain.get("taskcode"));
			}else{
				if(TaskConst.QUOTING_2.equals(workflowModel.getTaskcode()) && "Completed".equals(workflowModel.getTaskstate())){//填写信息和报价完成不用重复修改insbWorkflowmain数据
				}else{
					insbWorkflowmainDao.updateById(workflowModel);
				}
			}

			workflowModel.setOperator(operator2);
			workflowModel.setGroupid(groupid2);

		} else {
			//新增
			workflowModel.setCreatetime(new Date());

			String operator1 = workflowModel.getOperator();
			String groupid1 = workflowModel.getGroupid();

			//更新轨迹信息
			this.updateTrackTable(workflowModel);
			String operator2 = workflowModel.getOperator();
			String groupid2 = workflowModel.getGroupid();

			//人工任务设置操作人
			if (workflowDataService.isManWork(workflowModel.getTaskcode())) {
				workflowModel.setOperator(operator1);
				workflowModel.setGroupid(groupid1);
			}

			workflowModel.setId(null);
			insbWorkflowmainDao.insert(workflowModel);

			workflowModel.setOperator(operator2);
			workflowModel.setGroupid(groupid2);
		}

		workflowModel.setFromoperator(fromOperator);
		if(workflowModel.getTaskcode().equals("33")){
			workflowModel.setTaskfeedback("成功");
		}

		if (StringUtil.isEmpty(workflowModel.getOperator())) {
			try {
				INSBWorkflowmain flow = new INSBWorkflowmain();
				flow.setInstanceid(workflowModel.getInstanceid());
				flow = insbWorkflowmainDao.selectOne(flow);
				if (flow != null && StringUtil.isNotEmpty(flow.getOperator())) {
					workflowModel.setOperator(flow.getOperator());
				}
			} catch (Exception e) {
				workflowModel.setOperator("admin");
				LogUtil.error("查询" + workflowModel.getInstanceid() + "的insbWorkflowmain数据出错：" + e.getMessage());
				e.printStackTrace();
			}
		}

		//若setGroup修改设置组信息不新增trackdetail数据
		if(!"setGroup".equals(workflowModel.getOperatorstate())) {
			workflowmaintrackdetailDao.myInsert(workflowModel);
		}
		WorkflowFeedbackUtil.delWorkflowFeedback(workflowModel.getInstanceid(), null,
				workflowModel.getTaskcode(), workflowModel.getTaskstate(), workflowModel.getTaskname());

		if("Completed".equals(workflowModel.getTaskstate())){
			//更新最上面的一条信息的modifytime
			try {
				Map<String,String> param1 = new HashMap<String, String>();
				param1.put("instanceid", workflowModel.getInstanceid());
				param1.put("taskcode", workflowModel.getTaskcode());
				INSBWorkflowmaintrackdetail detailModel = workflowmaintrackdetailDao.selectData4RunQian(param1);
				detailModel.setModifytime(new Date());
				workflowmaintrackdetailDao.updateById(detailModel);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		//更新车险任务查询实时表
		if (StringUtil.isNotEmpty(workflowModel.getTaskname())) {
			if ("Ready".equalsIgnoreCase(workflowModel.getTaskstate()) || "Reserved".equalsIgnoreCase(workflowModel.getTaskstate())) {
				insbRealtimetaskService.addRealtimetask(workflowModel);
			} else if ("Closed".equalsIgnoreCase(workflowModel.getTaskstate()) || "Completed".equalsIgnoreCase(workflowModel.getTaskstate()) || "End".equalsIgnoreCase(workflowModel.getTaskstate())) {
				insbRealtimetaskService.deleteRealtimetask(workflowModel);
			} else {
				LogUtil.info("INSBRealtimetask error Taskstate : " + workflowModel.getTaskstate());
			}
		}  else {
			LogUtil.info("INSBRealtimetask error Taskname : " + workflowModel.toString());
		}
		
		if(workflowModel.getTaskcode().equals("37")){
			closeAllSubTask(workflowModel.getInstanceid());

			WorkFlow4TaskModel dataModel = new WorkFlow4TaskModel();
			dataModel.setMainInstanceId(workflowModel.getInstanceid());
			dataModel.setTaskCode(workflowModel.getTaskcode());
			dataModel.setTaskName(workflowModel.getTaskname());
			dataModel.setTaskStatus(workflowModel.getTaskstate());
			dataModel.setDataFlag(2);
			
			taskthreadPool4workflow.execute(new Runnable() {
				@Override
				public void run() {
					try {
						channelService.callback(dataModel);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
	
	
	/**
	 * 关闭所有子流程
	 * @return
	 */
	private boolean closeAllSubTask(String mainInstanceId){
		List<INSBWorkflowsub> subList = insbWorkflowsubDao.selectSubInsIdExc(mainInstanceId);
		
		if(subList!=null&&subList.size()>0){
			for(INSBWorkflowsub subModel:subList){
				workflowsubService.updateWorkFlowSubData(subModel.getMaininstanceid(), subModel.getInstanceid(), subModel.getTaskcode(), "Completed", null, null, "admin");
				workflowsubService.updateWorkFlowSubData(subModel.getMaininstanceid(), subModel.getInstanceid(), "37", "Closed", null, null, "admin");
			}
		}

		return true;
	}
	
	/**
	 * 更新轨迹表信息
	 */
	private void updateTrackTable(INSBWorkflowmain mainModel){
		try {

            Map<String, String> map = new HashMap<String, String>();
            map.put("instanceid", mainModel.getInstanceid());
            map.put("taskcode", mainModel.getTaskcode());

            String id = workflowmaintrackDao.selectByInstanceIdTaskCode(map);

            //更新
            if (id != null) {
                mainModel.setModifytime(new Date());
                mainModel.setId(id);
                workflowmaintrackDao.updateByMainTable(mainModel);
            } else {
                mainModel.setCreatetime(new Date());
                mainModel.setId(null);
                if(StringUtil.isEmpty(mainModel.getOperator())){
                	mainModel.setOperator("admin");
                }
				if(StringUtil.isEmpty(mainModel.getFromoperator())){
					mainModel.setFromoperator("admin");
				}
                workflowmaintrackDao.insertByMainTable(mainModel);
            }
        } catch (Exception e) {
            LogUtil.error("更新轨迹表信息"+mainModel.getInstanceid()+"数据出错:"+e.getMessage());
            e.printStackTrace();
        }
	}

	/**
	 * 判断非费改报价路径
	 * @param taskid
	 * @param inscomcode
	 * @return true 人工规则录单  false 规则报价
	 */
	private boolean judjeRutuenWorkFlow(String taskid,String inscomcode){
		boolean result = false;
		//非费改判断是否非该区域
		INSBQuotetotalinfo quotetotalinfo = new INSBQuotetotalinfo();
		quotetotalinfo.setTaskid(taskid);
		INSBQuotetotalinfo insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(quotetotalinfo);
		boolean isnonfeereform = false;
		if(null != insbQuotetotalinfo){
			Map<String, Object> map = appInsuredQuoteService.getChangeFee(insbQuotetotalinfo.getAgentnum(), null);
			if(!map.isEmpty()){
				isnonfeereform = (boolean) map.get("isfeeflag");
			}
		}
		if(isnonfeereform && 0 == appOtherRequestService.selectWorkFlowRuleInfoFfg(taskid, inscomcode)){
			result = true;
		}
		return result;
	}

    /**
     * 获取报价途径
     * 1：EDI 、 2： 精灵 、 3：规则报价 、4：人工调整 、 5：人工规则录单 、6： 人工录单
     * @param taskid
     * @param providerid
     * @param quotecode
     * @return
     */
	@Override
	public String getQuoteType(String taskid, String providerid,String quotecode) {
		LogUtil.info("========获取报价途径---START--======== taskid"+taskid+"--providerid---"+providerid+"---quotecode--"+quotecode);
		Map<String, String> result = new HashMap<String, String>();

		try{
			if(StringUtil.isEmpty(taskid) || StringUtil.isEmpty(providerid)){
				LogUtil.error("传入实例id或供应商id为空");
				result.put("msg", "传入实例id和供应商id不能为空");
				return JSONObject.fromObject(result).toString();
			}
			if(StringUtil.isNotEmpty(quotecode) && Integer.valueOf(quotecode) >= 6){
				result.put("quotecode", "6");
			}

			//判断是否为渠道单子 如果是渠道 不转 规则
			INSBQuotetotalinfo vo   = new INSBQuotetotalinfo();
			vo.setTaskid(taskid);
			INSBQuotetotalinfo toq = insbQuotetotalinfoDao.selectOne(vo);
			boolean  isqud = false;
			if(!StringUtil.isEmpty(toq.getPurchaserChannel())){
				isqud= true; 
			}
			
			if(quotecode==null||"".equals(quotecode)||"0".equals(quotecode)||"4".equals(quotecode)||"5".equals(quotecode)||"6".equals(quotecode)){
				int i = appOtherRequestService.selectWorkFlowRuleInfoRG(taskid,providerid);
				if(i==0){
					LogUtil.info("---存在转人工标记---");
					//hxx 转人工将使用后的数据 变为历史不影响下次 跳转
					appOtherRequestService.updateClosedWorkFlowRuleInfoRG(taskid, providerid);
					result.put("quotecode", "6");
					return JSONObject.fromObject(result).toString();//直接去人工
				}
				Map<String, String> map2 = new HashMap<String, String>();
				map2.put("taskid", taskid);
				map2.put("providerid", providerid);
				INSBAgreement agreementtrule = insbquoteinfoDao.queryAgreementIdByTaksId(map2);
				if(agreementtrule!=null&&!"".equals(agreementtrule.getAgreementtrule())
						&&!"无".equals(agreementtrule.getAgreementrule())){
					LogUtil.info("---协议配置了规则---");
					int c = appOtherRequestService.selectWorkFlowRuleInfoGZ(taskid,providerid);
					if(c==0){
						LogUtil.info("---存在转规则标记---");
						if(isqud){
							LogUtil.info("---渠道单---");
							result.put("quotecode", "6");
							return JSONObject.fromObject(result).toString();//直接去人工
						}
						result.put("quotecode", "5");
						return JSONObject.fromObject(result).toString();//直接规则平台查询
					}
				}else{
					LogUtil.info("---协议没配置规则---");
					int c = appOtherRequestService.selectWorkFlowRuleInfoGZ(taskid,providerid);
					if(c==0){
						LogUtil.info("---存在转规则标记---");
						result.put("quotecode", "6");
						return JSONObject.fromObject(result).toString();//没有规则 转人工
					}
				}
				Map<String, String> map = new HashMap<String, String>();
				map.put("taskid", taskid);
				map.put("deptid", providerid);
				String deptid = insbquoteinfoDao.querydeptcode(map);
				Map<String, Object> deptmap = new HashMap<String, Object>();
				deptmap.put("providerid", providerid);
				deptmap.put("conftype", "01");
				List<String> tempDeptIds = autoconfigshowService.getParentDeptIds4Show(deptid);
				deptmap.put("deptList", tempDeptIds);
				
				List<String> quotetypes = autoconfigshowDao.queryByProId(deptmap);
				if(!quotetypes.isEmpty()){
					LogUtil.info("---供应商配置了自动报价能力---"+quotetypes);
					if(quotetypes.contains("01")){//EDI
						LogUtil.info("---EDI---");
						result.put("quotecode", "1");
						return JSONObject.fromObject(result).toString();//返回EDI
					}
					if(quotetypes.contains("02")){
						LogUtil.info("---精灵---");
						result.put("quotecode", "2");
						return JSONObject.fromObject(result).toString();//返回精灵
					}
				}
				LogUtil.info("---供应商没配置自动报价能力---");
				if(agreementtrule!=null&&!"".equals(agreementtrule.getAgreementtrule())
						&&!"0".equals(agreementtrule.getAgreementrule())
						&&!"无".equals(agreementtrule.getAgreementrule())){
					LogUtil.info("---协议配置了规则---");
					if(judjeRutuenWorkFlow(taskid, providerid)){
						LogUtil.info("---非费改单---");
						if(isqud){
							LogUtil.info("---渠道单---");
							result.put("quotecode", "6");
							return JSONObject.fromObject(result).toString();//直接去人工
						}
						result.put("quotecode", "5");
					}else{
						if(isqud){
							LogUtil.info("---渠道单---");
							result.put("quotecode", "6");
							return JSONObject.fromObject(result).toString();//直接去人工
						}
						result.put("quotecode", "3");
					}
					return JSONObject.fromObject(result).toString();//返回自动规则报价
				}else if("无".equals(agreementtrule.getAgreementrule())){
					LogUtil.info("---协议规则为：无---");
					result.put("quotecode", "6");//去人工 录单 
				}else{
					LogUtil.info("---协议没配置规则---");
					if(isqud){
						LogUtil.info("---渠道单---");
						result.put("quotecode", "6");
						return JSONObject.fromObject(result).toString();//直接去人工
					}
					result.put("quotecode", "5");//去人工规则
				}
			}else if("1".equals(quotecode)){//EDI 失败
				Map<String, String> map = new HashMap<String, String>();
				map.put("taskid", taskid);
				map.put("deptid", providerid);
				String deptid = insbquoteinfoDao.querydeptcode(map);
				Map<String, Object> deptmap = new HashMap<String, Object>();
				deptmap.put("providerid", providerid);
				deptmap.put("conftype", "01"); 

				List<String> tempDeptIds = autoconfigshowService.getParentDeptIds4Show(deptid);
				deptmap.put("deptList", tempDeptIds);
				List<String> quotetypes = autoconfigshowDao.queryByProId(deptmap);
				if(!quotetypes.isEmpty()){
					LogUtil.info("---供应商配置了自动报价能力---"+quotetypes);
					if(quotetypes.contains("02")){
						LogUtil.info("---精灵---");
						result.put("quotecode", "2");
						return JSONObject.fromObject(result).toString();//返回精灵
					}
				}

				LogUtil.info("---供应商没配置精灵自动报价能力---");
				Map<String, String> map2 = new HashMap<String, String>();
				map2.put("taskid", taskid);
				map2.put("providerid", providerid);
				INSBAgreement agreementtrule = insbquoteinfoDao.queryAgreementIdByTaksId(map2);
				if(agreementtrule!=null&&!"".equals(agreementtrule.getAgreementtrule())&&!"无".equals(agreementtrule.getAgreementrule())){
					LogUtil.info("---协议配置了规则---");
					if(judjeRutuenWorkFlow(taskid, providerid)){
						LogUtil.info("---非费改单---");
						if(isqud){
							LogUtil.info("---渠道单---");
							result.put("quotecode", "6");
							return JSONObject.fromObject(result).toString();//直接去人工
						}
						result.put("quotecode", "5");
					}else{
						if(isqud){
							LogUtil.info("---渠道单---");
							result.put("quotecode", "6");
							return JSONObject.fromObject(result).toString();//直接去人工
						}
						result.put("quotecode", "3");
					}
					return JSONObject.fromObject(result).toString();//返回自动规则报价
				}else if("无".equals(agreementtrule.getAgreementrule())){
					LogUtil.info("---协议规则为：无---");
					result.put("quotecode", "6");//去人工 录单 
				}else{
					LogUtil.info("---协议没配置规则---");
					if(isqud){
						LogUtil.info("---渠道单---");
						result.put("quotecode", "6");
						return JSONObject.fromObject(result).toString();//直接去人工
					}
					result.put("quotecode", "5");//去人工规则 
				}
			}else if("2".equals(quotecode)){//精灵 失败
				Map<String, String> map2 = new HashMap<String, String>();
				map2.put("taskid", taskid);
				map2.put("providerid", providerid);
				INSBAgreement agreementtrule = insbquoteinfoDao.queryAgreementIdByTaksId(map2);
				if(agreementtrule!=null&&!"".equals(agreementtrule.getAgreementtrule())&&!"0".equals(agreementtrule.getAgreementrule())&&!"无".equals(agreementtrule.getAgreementrule())){
					LogUtil.info("---协议配置了规则---");
					if(judjeRutuenWorkFlow(taskid, providerid)){
						LogUtil.info("---非费改单---");
						if(isqud){
							LogUtil.info("---渠道单---");
							result.put("quotecode", "6");
							return JSONObject.fromObject(result).toString();//直接去人工
						}
						result.put("quotecode", "5");
					}else{
						if(isqud){
							LogUtil.info("---渠道单---");
							result.put("quotecode", "6");
							return JSONObject.fromObject(result).toString();//直接去人工
						}
						result.put("quotecode", "3");
					}
					return JSONObject.fromObject(result).toString();//返回自动规则报价
				}else if("无".equals(agreementtrule.getAgreementrule())){
					LogUtil.info("---协议规则为：无---");
					result.put("quotecode", "6");//去人工 录单 
				}else{
					LogUtil.info("---协议没配置规则---");
					if(isqud){
						LogUtil.info("---渠道单---");
						result.put("quotecode", "6");
						return JSONObject.fromObject(result).toString();//直接去人工
					}
					result.put("quotecode", "5");//去人工规则 
				}
			}else if("3".equals(quotecode)){//自动规则失败
				if(isqud){
					LogUtil.info("---渠道单---");
					result.put("quotecode", "6");
					return JSONObject.fromObject(result).toString();//直接去人工
				}
				result.put("quotecode", "5");//去人工调整
				return JSONObject.fromObject(result).toString();//返回精灵
			}else{
				result.put("quotecode", "6");
				return JSONObject.fromObject(result).toString();//返回精灵
			}
			
		} catch (Exception e) {
			result.put("quotecode", "6");//去人工 录单 
			e.printStackTrace();
		}

		return JSONObject.fromObject(result).toString();
	}
	
	/** 
	 * 
	 * 获取承保方式
	 * 1 EDI  2精灵  3人工
	 * @see com.zzb.conf.service.INSBWorkflowmainService#getContracthbType(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String getContractcbType(String taskid, String providerid,String lasttype,String type) {
		LogUtil.info("========获取承保方式---START--======== taskid"+taskid+"--providerid---"+providerid+"---lasttype--"+lasttype+"--type---"+type);
		Map<String, String> result = new HashMap<String, String>();
		try{
            if(taskid == null||providerid == null){
                result.put("msg", "传入实例id和供应商id不能为空");
                return JSONObject.fromObject(result).toString();
            }
            //特种车 包含商业险直接转人工
    //		if(carsize(taskid)>0&polist(taskid)>0){
    //			result.put("quotecode", "3");
    //			return JSONObject.fromObject(result).toString();//直接去人工
    //		}

            //是否续保
            boolean isRenewal = false;
            INSBQuotetotalinfo quotetotalinfo = new INSBQuotetotalinfo();
            quotetotalinfo.setTaskid(taskid);
            quotetotalinfo = insbQuotetotalinfoDao.selectOne(quotetotalinfo);

            if (quotetotalinfo != null && "1".equals(quotetotalinfo.getIsrenewal())) {
                isRenewal = true;
            }

            //根据供应商id和实例id找到出单网点
            Map<String, String> map = new HashMap<String, String>();
            map.put("taskid", taskid);
            map.put("deptid", providerid);
            String deptid = insbquoteinfoDao.querydeptcode(map);
            LogUtil.info("========获取承保核保方式--实例ID:"+taskid+"---供应商ID:"+providerid+"-得到出单网点是："+deptid+"--========");

            List<String> tempDeptIds = autoconfigshowService.getParentDeptIds4Show(deptid);
            Map<String, Object> deptmap = new HashMap<String, Object>();
            deptmap.put("providerid", providerid);
            deptmap.put("deptList", tempDeptIds);

            //承保
            if(type.equals("contract")){
                deptmap.put("conftype", "04");
                LogUtil.info("========获取承保方式========");
            }
            //核保
            else if(type.equals("underwriting")){
                if (isRenewal) {
                    deptmap.put("conftype", "03");
                    LogUtil.info("========获取续保方式========");
                } else {
                    deptmap.put("conftype", "02");
                    LogUtil.info("========获取核保方式========");
                }
            }

            //根据能力、供应商id和出单网点查询自动化配置信息
            List<String> quotetypes = autoconfigshowDao.queryByProId(deptmap);
            LogUtil.info("========获取方式编码====="+quotetypes+"===");

            if(StringUtil.isEmpty(lasttype) || "0".equals(lasttype)){

                if(!quotetypes.isEmpty()){//EDI
                    if(quotetypes.contains("01")){
                        result.put("quotecode", "1");
                        LogUtil.info("========EDI=======");
                        return JSONObject.fromObject(result).toString();//返回EDI
                    }
                    else if(quotetypes.contains("02")){
                        result.put("quotecode", "2");
                        LogUtil.info("========精灵=======");
                        return JSONObject.fromObject(result).toString();//返回精灵
                    }
                    else{
                        result.put("quotecode", "3");
                        LogUtil.info("========人工=======");
                        return JSONObject.fromObject(result).toString();//人工
                    }
                }else{
                    result.put("quotecode", "3");
                    LogUtil.info("========人工=======");
                    return JSONObject.fromObject(result).toString();//人工
                }

            }else if("1".equals(lasttype)||"3".equals(lasttype)){

                if(!quotetypes.isEmpty()){//EDI
                    if(quotetypes.contains("02")){
                        result.put("quotecode", "2");
                        LogUtil.info("========精灵=======");
                        return JSONObject.fromObject(result).toString();//返回精灵
                    }else{
                        result.put("quotecode", "3");
                        LogUtil.info("========人工=======");
                        return JSONObject.fromObject(result).toString();//人工
                    }
                }else{
                    result.put("quotecode", "3");
                    LogUtil.info("========人工=======");
                    return JSONObject.fromObject(result).toString();//人工
                }

            }else{
                result.put("quotecode", "3");
                LogUtil.info("========人工=======");
                return JSONObject.fromObject(result).toString();//人工
            }
		
		}catch(Exception e){
			result.put("msg", "error");
			result.put("quotecode", "3");
            LogUtil.info("========人工=======");
            e.printStackTrace();
			return JSONObject.fromObject(result).toString();//人工
		}
	}

	
	/** 
	 * 如果是退回修改或支付则直接转人工
	 * 如果核保时有备注信息，则直接转人工
	 * 提交相关信息过规则，根据规则返回结果决定是否走自动核保
	 * 获取核保方式
	 * 1 EDI  2精灵  3人工
	 * @see com.zzb.conf.service.INSBWorkflowmainService#getContracthbType(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String getContracthbType(String taskid, String providerid,String lasttype,String type) {
		LogUtil.info("taskid="+taskid+",providerid="+providerid+",获取核保方式,lasttype="+lasttype+",type="+type);
		Map<String, String> result = new HashMap<String, String>();
		Map<String, Object> resultMap = null;
		try {
			if (StringUtils.isNotEmpty(lasttype) && ("2".equals(lasttype) || "3".equals(lasttype))) {
				result.put("quotecode", "3");
				LogUtil.info("taskid=" + taskid + ",providerid=" + providerid + ",退回修改和支付直接转人工");
				return JSONObject.fromObject(result).toString();
			}
			// 查询是否有提交备注
			INSBQuotetotalinfo quotetotalinfo = new INSBQuotetotalinfo();
			quotetotalinfo.setTaskid(taskid);
			quotetotalinfo = insbQuotetotalinfoDao.selectOne(quotetotalinfo);
			INSBQuoteinfo quoteinfo = new INSBQuoteinfo();
			quoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
			quoteinfo.setInscomcode(providerid);
			quoteinfo = insbQuoteinfoDao.selectOne(quoteinfo);
			INSBWorkflowsub workflowsub = null;
			if(!"renewal".equals(type)){//续保情况，不需要查询是否存在备注的情况
				workflowsub = new INSBWorkflowsub();
				workflowsub.setInstanceid(quoteinfo.getWorkflowinstanceid());
				workflowsub = insbWorkflowsubDao.selectOne(workflowsub);
			}

			// 查询核保能力
			boolean isRenewal = false;
			if (quotetotalinfo != null && "1".equals(quotetotalinfo.getIsrenewal())) {
				isRenewal = true;
			}

			Map<String, String> map = new HashMap<String, String>();
			map.put("taskid", taskid);
			map.put("deptid", providerid);
			String deptid = insbquoteinfoDao.querydeptcode(map);
			List<String> tempDeptIds = autoconfigshowService.getParentDeptIds4Show(deptid);
			Map<String, Object> deptmap = new HashMap<String, Object>();
			deptmap.put("providerid", providerid);
			deptmap.put("deptList", tempDeptIds);
			if (isRenewal) {
				deptmap.put("conftype", "03"); // 续保方式
				LogUtil.info("========获取续保方式========");
			} else {
				deptmap.put("conftype", "02"); // 核 保方式
				LogUtil.info("========获取核保方式========");
			}
			List<String> quotetypes = autoconfigshowDao.queryByProId(deptmap);
			LogUtil.info("========获取方式编码====="+quotetypes+"===");

			// 默认转人工
			result.put("quotecode", "3");
			if (!quotetypes.isEmpty()) {// EDI
				if (quotetypes.contains("01") && !"underwritingType".equals(type)) {
					result.put("quotecode", "1");
					LogUtil.info("========EDI=======");
				} else if (quotetypes.contains("02")) {
					result.put("quotecode", "2");
					LogUtil.info("========精灵=======");
				}
			}

			try {
				if ("1".equals(result.get("quotecode")) || "2".equals(result.get("quotecode"))) {
					//此处调用获取是否可以自核并且取特约信息
					resultMap = getAutoinsureOrSpecial(taskid, providerid, quoteinfo, quotetotalinfo);
					/*result={ruleItem.isAutoUnderwriting=true,
					ruleItem.specialAgreementList={22211|文字内容|***,carmodelname,agentname@##@22235|文字内容二|***,insurername,puttime},
					ruleItem.runningRecord=[136916, 151980, 186838]}*/
					if(StringUtil.isNotEmpty(String.valueOf(resultMap.get("ruleItem.specialAgreementList")))){
						CMRedisClient.getInstance().set(3, SupplementCache.MODULE_NAME, taskid+".ruleItem.specialAgreementList."+providerid, String.valueOf(resultMap.get("ruleItem.specialAgreementList")), 24*60*60);
					}
				}
			} catch (Exception e) {
				LogUtil.info("taskid=" + taskid + ",providerid=" + providerid + "规则获取是否自核标识和特约信息异常result="+resultMap);
				e.printStackTrace();
			}

			if (workflowsub != null) {
				//如果有备注直接根据配置转暂存
				//bug-7910、如果表insbWorkflowsubtrack里面存在14、并且insbUsercomment表有14的备注、直接走暂存
				INSBWorkflowsubtrack subtrack = new INSBWorkflowsubtrack();
				subtrack.setInstanceid(workflowsub.getInstanceid());
				subtrack.setTaskcode("14");        //提交核保流程
				subtrack = insbWorkflowsubtrackDao.selectOne(subtrack);//新方法获取备注表的ID
				INSBUsercomment userComment = new INSBUsercomment();
				if (null != subtrack && StringUtil.isNotEmpty(subtrack.getId())) {
					userComment.setTrackid(subtrack.getId());
					userComment.setTracktype(2);// 1：主流程 2：子流程
					userComment = insbUsercommentDao.selectOne(userComment);
				}
				if (userComment != null && null != userComment.getCommentcontenttype() && userComment.getCommentcontenttype() > 0) {
					LogUtil.info("taskid=" + taskid + ",providerid=" + providerid + ",用户提交有备注直接返回得到的核保途径不再判断是否自核");
					return JSONObject.fromObject(result).toString();
				}
			}

			try {
				if (!isRenewal && ("1".equals(result.get("quotecode")) || "2".equals(result.get("quotecode")))) {
					LogUtil.info("taskid=" + taskid + ",providerid=" + providerid + ",调用规则判断是否自核,result=" + resultMap);

					if ((Boolean) (resultMap.get("ruleItem.isAutoUnderwriting"))) {
						if ("1".equals(result.get("quotecode"))) {
							result.put("quotecode", "5");
						} else if ("2".equals(result.get("quotecode"))) {
							result.put("quotecode", "6");
						}
					}
				}
			} catch (Exception e) {
				LogUtil.info("taskid=" + taskid + ",providerid=" + providerid + "规则获取核保途径异常。直接返回之前赋的值给工作流result="+result);
				e.printStackTrace();
				return JSONObject.fromObject(result).toString();
			}
		} catch (Exception e) {
			LogUtil.info("taskid=" + taskid + ",providerid=" + providerid + "获取核保途径异常。直接返回转人工给工作流result="+result);
			e.printStackTrace();
			result.put("quotecode", "3");
			return JSONObject.fromObject(result).toString();
		}

		return JSONObject.fromObject(result).toString();// 人工
	}
	
	private static String RULE_SERVER_IP ="";
	private static String RULE_PARAM_HOST = "";
	static {
		// 读取相关的配置  
		ResourceBundle ruleResourceBundle = ResourceBundle.getBundle("config/rule");
		RULE_SERVER_IP = ruleResourceBundle.getString("rule.handler.server.ip");
		RULE_PARAM_HOST = ruleResourceBundle.getString("rule.handler.server.automateRule");
		//如果不是HTTP开头就拼接,是就不用拼接
		if(!RULE_PARAM_HOST.startsWith("http")){
			RULE_PARAM_HOST = RULE_SERVER_IP+RULE_PARAM_HOST;
		}
	}
	
	/**
	 * 调用规则判断是否有自动核保能力，或者获取特约信息
	 * @param taskid
	 * @param providerid
	 * @param quoteinfo
	 * @param quotetotalinfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getAutoinsureOrSpecial(String taskid,String providerid,INSBQuoteinfo quoteinfo,INSBQuotetotalinfo quotetotalinfo){
		// 调用规则判断是否有自动核保能力
		INSBCarinfo insbCarinfo = new INSBCarinfo();
		insbCarinfo.setTaskid(taskid);
		INSBCarinfo carinfo = insbCarinfoDao.selectOne(insbCarinfo);
		INSBCarmodelinfo insbCarmodelinfo = new INSBCarmodelinfo();
		insbCarmodelinfo.setCarinfoid(carinfo.getId());
		INSBCarmodelinfo carmodelinfo = insbCarmodelinfoDao.selectOne(insbCarmodelinfo);

		Map<String, Object> paramMap = new HashMap<>(88);
		if (StringUtils.isNotBlank(quoteinfo.getAgreementid())) {
			INSBAgreement insbAgreement = insbAgreementService.queryById(quoteinfo.getAgreementid());
			if (insbAgreement != null && StringUtils.isNotBlank(insbAgreement.getAgreementtrule())) {
				paramMap.put("ruleItem.ruleID", insbAgreement.getAgreementtrule());// 规则信息.规则集编号
				paramMap.put("ruleItem.repairDiscount", Double.valueOf(0));// 规则信息.专修保障费率
			}
		}

		if (carmodelinfo != null) {
			paramMap.put("car.specific.taxPrice", carmodelinfo.getTaxprice());
			paramMap.put("car.specific.price", carmodelinfo.getPrice());
			if (carmodelinfo.getAnalogytaxprice() != null)
				paramMap.put("car.specific.analogyTaxPrice", carmodelinfo.getAnalogytaxprice());
			if (carmodelinfo.getAnalogyprice() != null)
				paramMap.put("car.specific.analogyPrice", carmodelinfo.getAnalogyprice());
			paramMap.put("car.model.seats", carmodelinfo.getSeat());
		}

		paramMap.put("car.specific.regDate", carinfo.getRegistdate());
		paramMap.put("api.log.enquiry.id", taskid + "_" + providerid);// 任务ID
		// 调用报价组装给规则判断的参数。
		paramMap = appQuotationService.packetParamdata(paramMap, taskid, providerid, quotetotalinfo, quoteinfo);

		LogUtil.info("taskid=" + taskid + ",providerid=" + providerid + ",调用规则判断是否自核,params=" + paramMap);
		try {
			//HTTP请求规则返回信息
			LogUtil.info("RULE_PARAM_HOST=="+RULE_PARAM_HOST);
			String result = HttpSender.doPost(RULE_PARAM_HOST, JSONObject.fromObject(paramMap).toString());
			Map<String,Object> hashMap = new HashMap<>();
			hashMap = JSONObject.fromObject(result);
			LogUtil.info("自动核保规则或特约规则返回的信息"+result);
			return hashMap;
		} catch (Exception e) {
			LogUtil.error(e.getMessage(),e);
		}
		return null;
	}
	
	@Override
	public long getMyTaskTotals(MyTaskVo myTaskVo) {
		Map<String, Object> map = BeanUtils.toMap(myTaskVo);
		return insbWorkflowmainDao.getMyTaskTotals(map);
	}

	@Override
	public List<Map<String, Object>> getMyTaskInPage(Map<String, Object> map) {
		return insbWorkflowmainDao.getMyTaskInPage(map);
	}

	@Override
	public INSBWorkflowmain selectByInstanceId(String taskid) {
		return insbWorkflowmainDao.selectINSBWorkflowmainByInstanceId(taskid);
	}
	

	@Override
	public void deleteWorkFlowMainData(String instanceId) {
		insbWorkflowmainDao.deleteByInstanceId(instanceId);
	}
	//工作池申请按钮，工作流调用方法
	@Override
	public String interApplyForTask(String userid,String processinstanceid) {
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String> dateparams = new HashMap<String, String>();
		
		params.put("processinstanceid", "\""+processinstanceid+"\"");
		params.put("userid", "\""+userid+"\"");
		dateparams.put("datas", params.toString());
		//203.195.141.57:8080/workflow/process/skipTask
		String resultJSON = HttpClientUtil.doGet(WORKFLOW+"/process/claimTask",dateparams);
		if(resultJSON!=null){
			String urlcontent=JSONObject.fromObject(resultJSON).getString("message");
			return urlcontent;
		}else{
			return "faild";
		}
	}
	@Override
	public void addTask2Poll(Task task) {
	}
	
//	@Override
//	public String RequestWorkflow(String userid,String processinstanceid){
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("userid", userid);
//		params.put("processinstanceid", processinstanceid);
//		String resultJSON = HttpClientUtil.doGet(WORKFLOW+"/process/skipTask",params);
//		return null;
//	}

	@Override
	public void removeTaskFromPool(Task task) {
		dispatchService.removTaskFromPool(task);
	}

	@Override
	public String selectaskcode(String taskid) {
		return insbWorkflowmainDao.selectaskcode(taskid);
	}

	/**
	 * TIP:规则平台查询只会发起一次
	 * 1  需要启动   0 不需要启动
	 * taskid +"@rulequery"  KEY  放入redis中，有值就证明规则平台查询已经启动
	 */
	@Override
	public String getPTway(String taskid, String providerid) {
		LogUtil.info("getPTway taskid="+taskid +",providerid="+providerid);
		Map<String, String> result = new HashMap<String, String>();
		try {
			LogUtil.info("开始调用平台规则查询taskid="+ taskid +"=getPTway供应商id="+ providerid);
			//新车不启动规则平台查询
			if(isNewCar(taskid)){
				result.put("ptway", "0");
				LogUtil.info("开始调用平台规则查询taskid="+ taskid +"=新车不发起规则平台查询=");
			}else{
				int rulequerykey = redisClient.addOne("rulequerynum", taskid +"@rulequery");
				if(1 == rulequerykey){
					//设置自动超时
					redisClient.expire("rulequerynum", taskid +"@rulequery", RULEQUERYKEY);
					result.put("ptway", "1");
				}else{
					result.put("ptway", "0");
				}
				LogUtil.info("开始调用平台规则查询taskid="+ taskid +"=redisClient中取到的taskid="+ rulequerykey);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.info("开始调用平台规则查询taskid="+taskid+"抛异常了,当前时间："+new Date());
			result.put("ptway", "1");
		}
        LogUtil.info("转平台查询taskid="+taskid+",result="+result);
		return JSONObject.fromObject(result).toString();
	}
	/**
	 * TIP:规则平台查询只会发起一次
	 * 1  需要启动   0 不需要启动
	 * taskid +"@rulequery"  KEY  放入redis中，有值就证明规则平台查询已经启动
	 */
	@Override
	public String getGZPTway(String taskid, String providerid) {
		LogUtil.info("getGZPTway taskid="+taskid +",providerid="+providerid);
//		Map<String, String> result = new HashMap<String, String>();
		String result = "0";
		try {
			LogUtil.info("开始调用平台规则查询taskid="+ taskid +"=getPTway供应商id="+ providerid);
			//新车不启动规则平台查询--变更，新车也需要启动平台查询
//			if(isNewCar(taskid)){
//				result = "0";
//				LogUtil.info("开始调用平台规则查询taskid="+ taskid +"=新车不发起规则平台查询=");
//			}else{
				INSBRulequeryotherinfo insbqueryotherinfo = new INSBRulequeryotherinfo();
				insbqueryotherinfo.setTaskid(taskid);
				INSBRulequeryotherinfo queryotherinfo = insbRulequeryotherinfoDao.selectOne(insbqueryotherinfo);
				
				if(null==queryotherinfo || !"guizequery".equals(queryotherinfo.getOperator()))
					result = "1";
				else
					result = "0";
				
				//修改了保险配置，重新发起平台查询
				if(result.equals("0")){
					String status = String.valueOf(CMRedisClient.getInstance().get(Constants.CM_GLOBAL, taskid + "startedBakQuery"));
					if(!StringUtil.isEmpty(status)){
						LogUtil.info("taskid=" +taskid + ", getGZPTway, 是否发起平台查询status=" + status +"? 0发起 : 1不发起");
						if(status.equals("0")){
							result = "1";
						}
					}
				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.info("开始调用平台规则查询taskid="+taskid+"抛异常了,当前时间："+new Date());
			result = "0";
		}
        LogUtil.info("转平台查询taskid="+taskid+",result="+result);
		return result;
	}
	@Resource
	private INSBLastyearinsurestatusDao insbLastyearinsurestatusDao;
	@Resource
	private INSBRulequeryotherinfoDao insbRulequeryotherinfoDao;
	/**
	 * TIP:EDI平台查询只会发起一次
	 * 1  需要启动   0 不需要启动
	 * taskid +"@rulequery"  KEY  放入redis中，有值就证明规则平台查询已经启动
	 */
	@Override
	public String getEDIPTway(String taskid, String providerid) {
		LogUtil.info("getEDIPTway taskid="+taskid +",providerid="+providerid);
		String result = "0";
		try {
			LogUtil.info("开始是否调用EDI平台查询taskid="+ taskid +"=getPTway供应商id="+ providerid);
			//新车不启动平台查询--变更，新车也需要启动平台查询
//			if(isNewCar(taskid)){
//				result.put("ptediway", "0");
//				result = "0";
//				LogUtil.info("EDI平台查询taskid="+ taskid +"=新车不发起平台查询=");
//			}else{
					INSBRulequeryotherinfo insbRulequeryotherinfo = new INSBRulequeryotherinfo();
					insbRulequeryotherinfo.setTaskid(taskid);
					INSBRulequeryotherinfo queryotherinfo = insbRulequeryotherinfoDao.selectOne(insbRulequeryotherinfo);
						//设置自动超时
						if(null==queryotherinfo || !"guizequery".equals(queryotherinfo.getOperator()))
							result = "1";
						else
							result = "0";
					
					//修改了保险配置，重新发起平台查询
					if(result.equals("0")){
						String status = String.valueOf(CMRedisClient.getInstance().get(Constants.CM_GLOBAL, taskid + "startedBakQuery"));
						if(!StringUtil.isEmpty(status)){
							LogUtil.info("taskid=" +taskid + ", getEDIPTway, 是否发起平台查询status=" + status +"? 0发起 : 1不发起");
							if(status.equals("0")){
								result = "1";
							}
						}
					}
					
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.info("调用EDI平台查询taskid="+taskid+"抛异常了,当前时间："+new Date());
			result = "1";
		}
        LogUtil.info("EDI报价前转平台查询taskid="+taskid+",result="+result);
		return result;
	}
	/**
	 * 是否新车
	 * @param taskid
	 * @return true 新车
	 */
	private boolean isNewCar(String taskid){
		INSBCarinfo insbCarinfo = insbCarinfoDao.selectCarinfoByTaskId(taskid);
		return null!=insbCarinfo && ("新车未上牌".equals(insbCarinfo.getCarlicenseno()) || "1".equals(insbCarinfo.getIsNew()));
	}
	
	@Override
	public String getGZway(String taskid, String providerid) {
        LogUtil.info("getGZway taskid="+taskid +",providerid="+providerid);

		Map<String, String> result = new HashMap<String, String>();
        result.put("gzway", "0");

        //先检查规则校验结果
        int i = appOtherRequestService.selectWorkFlowRuleInfoGZ(taskid, providerid);

        //规则校验不通过
        if(i==0) {
            appOtherRequestService.updateClosedWorkFlowRuleInfoGZ(taskid, providerid);
            LogUtil.error("getGZway taskid="+taskid + ",providerid="+providerid+"--规则校验不通过");

        } else {
            int ic = commonQuoteinfoService.getInsureConfigType(taskid, providerid);

            //交强险+车船税
            if (ic == 4) {
                boolean verifyResult = automaticVerifyService.verifyTrafficTax(taskid, providerid);
                if (verifyResult) {
                    result.put("gzway", "1");
                }
            } else {
                LogUtil.error("getGZway taskid="+taskid + ",providerid="+providerid+"--投保险种类别不是 交强+车船税");
            }
        }

        String resultString = JSONObject.fromObject(result).toString();
        LogUtil.info("getGZway taskid="+taskid + ",providerid="+providerid+"--" + resultString);

		return resultString;
	}
	
}