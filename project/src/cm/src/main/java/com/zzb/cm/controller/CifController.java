package com.zzb.cm.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.zzb.cm.dao.*;
import com.zzb.cm.entity.*;
import com.zzb.mobile.model.ExtendCommonModel;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.system.entity.INSCDept;
import com.common.CloudQueryUtil;
import com.common.TaskConst;
import com.zzb.cm.controller.vo.FindLastClaimBackInfoVo;
import com.zzb.cm.service.INSBLastyearinsureinfoService;
import com.zzb.cm.service.RulePlatformQueryService;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.dao.INSBWorkflowsubDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBWorkflowsub;
import com.zzb.mobile.dao.AppInsuredQuoteDao;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.lastindanger.LastClaimBackInfo;

/**
 * cif 平查询及回调接口
 */
@Controller
@RequestMapping("/static/cif/*")
public class CifController extends BaseController {
	@Resource
	private AppInsuredQuoteDao appInsuredQuoteDao;
	@Resource
	private INSBAgentDao insbAgentDao;
	@Resource
	private INSBQuotetotalinfoDao insbQuotetotalinfoDao;
	@Resource
	private INSBWorkflowsubDao insbWorkflowsubDao;
	@Resource
	private INSBCarinfoDao insbCarinfoDao;
	@Resource
	private INSBCarmodelinfoDao insbCarmodelinfoDao;
	@Resource
	private INSBQuoteinfoDao insbQuoteinfoDao;
	@Resource
	private INSBLastyearinsureinfoService insbLastyearinsureinfoService;
	@Resource
	private RulePlatformQueryService rulePlatformQueryService;


	/**
	 * cif 平台查询回调接口
	 * 
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "saveLastClaimBackInfo", method = RequestMethod.POST)
	public CommonModel saveLastClaimBackInfo(@RequestBody LastClaimBackInfo vo) {
		// 入库
		LogUtil.info(vo.getTaskSequence() + ":saveLastClaimBackInfo:" + JSONObject.fromObject(vo).toString());
		CommonModel result = insbLastyearinsureinfoService.saveLastYearClaimsInfo(vo.getTaskSequence(),vo);

		//并发事务重复数据处理
        if (result != null && result instanceof ExtendCommonModel) {
            ExtendCommonModel extendCommonModel = (ExtendCommonModel) result;
            if (extendCommonModel.getExtend() != null && ((Boolean)extendCommonModel.getExtend()).booleanValue()) {
//                try {
//                    INSBLastyearinsureinfo insbLastyearinsureinfo = new INSBLastyearinsureinfo();
//                    insbLastyearinsureinfo.setTaskid(vo.getTaskSequence());
//                    List<INSBLastyearinsureinfo> lastyearinsureinfoList = insbLastyearinsureinfoDao.selectList(insbLastyearinsureinfo);
//
//                    if (lastyearinsureinfoList != null && !lastyearinsureinfoList.isEmpty()) {
//                        for (INSBLastyearinsureinfo lastyearinsureinfo : lastyearinsureinfoList) {
//                            if (!"2".equals(lastyearinsureinfo.getSflag()) || !"cifback".equalsIgnoreCase(lastyearinsureinfo.getOperator())) {
//                                lastyearinsureinfo.setTaskid(lastyearinsureinfo.getTaskid() + "_cifbak");
//                                insbLastyearinsureinfoDao.updateById(lastyearinsureinfo);
//                            }
//                        }
//                    }
//                } catch (Exception e) {
//                    LogUtil.error(vo.getTaskSequence() + "普通平台查询回调保存后检查重复数据时出错：" + e.getMessage());
//                }
            }
        }

		return result;
	}
	
	/**
	 * 重新发起规则平台查询
	 * 
	 * taskId
	 *            主流程ID
	 * jobnum
	 *            代理人工号
	 * @return
	 */
	@RequestMapping(value = "findrulequeryBackInfo", method = RequestMethod.POST)
	public CommonModel findrulequeryBackInfo(@RequestBody FindLastClaimBackInfoVo claimvo) {
		CommonModel comm = new CommonModel();
		String taskId = claimvo.getTaskId();
		try {
				INSBWorkflowsub workflowsub = new INSBWorkflowsub();
				workflowsub.setMaininstanceid(taskId);
				workflowsub.setTaskcode(TaskConst.QUOTING_7);//只需要在人工规则报价的时候重新发起规则平台查询
				List<INSBWorkflowsub> workflowsubList = insbWorkflowsubDao.selectList(workflowsub);
				//得到报价信息总表id
				INSBQuotetotalinfo quotetotalinfo = new INSBQuotetotalinfo();
				quotetotalinfo.setTaskid(taskId);
				quotetotalinfo = insbQuotetotalinfoDao.selectOne(quotetotalinfo);
				//循环子流程通过子流程id查询报价表的到保险公司code
				if(workflowsubList!=null && workflowsubList.size()>0){
					for (int i = 0; i < workflowsubList.size(); i++) {
						INSBQuoteinfo quoteinfo = new INSBQuoteinfo();
						quoteinfo.setWorkflowinstanceid(workflowsubList.get(i).getInstanceid());
						quoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
						quoteinfo = insbQuoteinfoDao.selectOne(quoteinfo);
						if(quoteinfo!=null&&null!=quoteinfo.getInscomcode()){
							rulePlatformQueryService.startRuleQuery("{\"taskid\":\""+taskId+"\",\"inscomcode\":\""+quoteinfo.getInscomcode()+"\"}"); //发起规则平台查询
							break;//只需要发起一次查询就结束
						}
					}
				}
				comm.setStatus("success");
				return comm;
		} catch (Exception e) {
			comm.setStatus("false");
			e.printStackTrace();
			return comm;
		}
	}
	/**
	 * 发起平台查询
	 * 
	 * taskId
	 *            主流程ID
	 * jobnum
	 *            代理人工号
	 * @return
	 */
	@RequestMapping(value = "findLastClaimBackInfo", method = RequestMethod.POST)
	public CommonModel findLastClaimBackInfo(@RequestBody FindLastClaimBackInfoVo claimvo) {
		CommonModel comm = new CommonModel();
		String taskId = claimvo.getTaskId();
		String jobnum = claimvo.getJobnum();
		
		try {
			try{
				INSBWorkflowsub workflowsub = new INSBWorkflowsub();
				workflowsub.setMaininstanceid(taskId);
				workflowsub.setTaskcode(TaskConst.QUOTING_7);//只需要在人工规则报价的时候重新发起规则平台查询
				List<INSBWorkflowsub> workflowsubList = insbWorkflowsubDao.selectList(workflowsub);
				//得到报价信息总表id
				INSBQuotetotalinfo quotetotalinfo = new INSBQuotetotalinfo();
				quotetotalinfo.setTaskid(taskId);
				quotetotalinfo = insbQuotetotalinfoDao.selectOne(quotetotalinfo);
				//循环子流程通过子流程id查询报价表的到保险公司code
				if(workflowsubList!=null && workflowsubList.size()>0){
					for (int i = 0; i < workflowsubList.size(); i++) {
						INSBQuoteinfo quoteinfo = new INSBQuoteinfo();
						quoteinfo.setWorkflowinstanceid(workflowsubList.get(i).getInstanceid());
						quoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
						quoteinfo = insbQuoteinfoDao.selectOne(quoteinfo);
						if(quoteinfo!=null&&null!=quoteinfo.getInscomcode()){
							rulePlatformQueryService.startRuleQuery("{\"taskid\":\""+taskId+"\",\"inscomcode\":\""+quoteinfo.getInscomcode()+"\"}"); //发起规则平台查询
						}
					}
				}
			}catch(Exception e){
				LogUtil.error("规则平台查询异常", e);
			}
			String taskSequence = taskId;
			String areaId = null;
			String vin = null;
			String carBrandName = null;
			String engineNum = null;
			String firstRegDate = null;
			String plateNum = null;
			INSBAgent vo = new INSBAgent();
			vo.setJobnum(jobnum);
			INSBAgent agent = insbAgentDao.selectOne(vo);
			INSCDept inscDept = appInsuredQuoteDao.sellectCityAreaByAgreeid(agent.getId());// 区域
			areaId = inscDept.getCity();
			INSBCarinfo insbCarinfo = insbCarinfoDao.selectCarinfoByTaskId(taskSequence);
			if(insbCarinfo!=null){
				plateNum = insbCarinfo.getCarlicenseno();
				firstRegDate = dateToString(insbCarinfo.getRegistdate());
				engineNum = insbCarinfo.getEngineno();
				vin = insbCarinfo.getVincode();
			}
			INSBCarmodelinfo cmvo = new INSBCarmodelinfo();
			cmvo.setCarinfoid(insbCarinfo!=null?insbCarinfo.getId():"");
			INSBCarmodelinfo insbcarmodelinfo = insbCarmodelinfoDao.selectOne(cmvo);
			if(insbcarmodelinfo!=null){
				carBrandName = insbcarmodelinfo.getBrandname();
			}
			CloudQueryUtil.pullLastClaimBackInfo(taskSequence, areaId, vin, carBrandName, engineNum, plateNum, firstRegDate);
			comm.setStatus("success");
			return comm;
		} catch (Exception e) {
			comm.setStatus("false");
			e.printStackTrace();
			return comm;
		}
	}
	public String dateToString(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
		return df.format(date);
	}
}
