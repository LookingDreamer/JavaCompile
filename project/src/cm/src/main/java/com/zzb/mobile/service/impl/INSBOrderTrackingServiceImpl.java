package com.zzb.mobile.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.service.INSCDeptService;
import com.zzb.cm.dao.INSBCarinfoDao;
import com.zzb.cm.dao.INSBCarinfohisDao;
import com.zzb.cm.dao.INSBInsuredDao;
import com.zzb.cm.dao.INSBInsuredhisDao;
import com.zzb.cm.dao.INSBOrderDao;
import com.zzb.cm.dao.INSBPersonDao;
import com.zzb.cm.dao.INSBQuoteinfoDao;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBCarinfohis;
import com.zzb.cm.entity.INSBInsured;
import com.zzb.cm.entity.INSBInsuredhis;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.entity.INSBPerson;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.conf.dao.INSBProviderDao;
import com.zzb.conf.dao.INSBWorkflowmainDao;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.entity.INSBWorkflowmain;
import com.zzb.conf.service.INSBWorkflowmaintrackService;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.service.INSBOrderTrackingService;


@Service   
public class INSBOrderTrackingServiceImpl extends BaseServiceImpl<INSBOrder> implements INSBOrderTrackingService {
	
	
	@Resource 
	private INSBProviderDao insbProviderDao;
	@Resource 
	private INSBCarinfoDao insbCarinfoDao;
	@Resource
	private INSBPersonDao insbPersonDao;
	@Resource
	private INSBInsuredDao insbInsuredDao;
	@Resource
	private INSCDeptDao inscDeptDao;
	@Resource
	private INSBWorkflowmainDao insbWorkflowmainDao;
	@Resource
	private INSBWorkflowmaintrackService insbWorkflowmaintrackService;
	@Resource
	private INSBQuoteinfoDao insbQuoteinfoDao;
	@Resource
	private INSBInsuredhisDao insbInsuredhisDao;
	@Resource
	private INSBCarinfohisDao insbCarinfohisDao;
	
	
	/*
	 * 订单跟踪信息的接口服务的实现
	 * 这里跟踪信息需要显示的是有两个部分：
	 * 2，工作流中的状态，需要调用工作流的接口完成
	 * 第二部分的流程信息需要查询 工作流的接口完成。
	 */
	@Override
	public String showOrderTracking(String processInstanceId,String prvCode,String subInstanceId) {
		/*
		 * processInstanceId流程实例id，
		 * 保险公司的id，
		 * 子流程id，
		 */
		CommonModel model = new CommonModel();
		Map<String,Object> body = new HashMap<String,Object>();
		try{
			/*
			 * 获取logo,保险公司名称，网点，车牌号，被保人姓名5项信息
			 * 车辆信息，被保人信息需要先查询历史表进行数据选择
			 */
			//得到车牌号，被保人
			INSBCarinfohis carinfohis = new INSBCarinfohis();
			carinfohis.setTaskid(processInstanceId);
			carinfohis.setInscomcode(prvCode);
			carinfohis = insbCarinfohisDao.selectOne(carinfohis);
			if(carinfohis!=null){
				body.put("carlicenseno",carinfohis.getCarlicenseno());//设置车牌号
			}else{
                INSBCarinfo carinfo = insbCarinfoDao.selectCarinfoByTaskId(processInstanceId);
				body.put("carlicenseno",carinfo.getCarlicenseno());//设置车牌号
			}

            INSBPerson person = null;
			INSBInsuredhis insuredhis = new INSBInsuredhis();
			insuredhis.setTaskid(processInstanceId);
			insuredhis.setInscomcode(prvCode);
			insuredhis = insbInsuredhisDao.selectOne(insuredhis);
			if(insuredhis!=null){
				person = insbPersonDao.selectById(insuredhis.getPersonid());
			}else{
                INSBInsured insured = insbInsuredDao.selectInsuredByTaskId(processInstanceId);
                if (insured != null) {
                    person = insbPersonDao.selectById(insured.getPersonid());
                }
			}
			body.put("insuredname",person==null?"":person.getName());//设置被保人姓名

			//获取保险公司信息
			INSBProvider provider = new INSBProvider();
			provider.setPrvcode(prvCode);
			provider = insbProviderDao.selectOne(provider);
			//出单网点信息
			INSBQuoteinfo quoteinfonew = new INSBQuoteinfo();
			quoteinfonew.setInscomcode(prvCode);
			quoteinfonew.setWorkflowinstanceid(subInstanceId);
			quoteinfonew = insbQuoteinfoDao.selectOne(quoteinfonew);
			INSCDept dept = new INSCDept();
			if(quoteinfonew!=null){
				dept = inscDeptDao.selectByComcode(quoteinfonew.getDeptcode());
			}
			//任务状态
			INSBWorkflowmain workmain = insbWorkflowmainDao.selectINSBWorkflowmainByInstanceId(processInstanceId);
			body.put("orderStatus", workmain.getTaskcode());
			//组织数据
			body.put("inscomname", provider.getPrvname());//保险公司名称
			body.put("logo", provider.getLogo());       //logo
			body.put("deptcodeCN", dept==null?"":dept.getComname());//出单网点地址
			/*
			 * 调用提供的方法
			 */
            List<Map<String,String>> str = insbWorkflowmaintrackService.getWorkflowStatusByInstanceId(processInstanceId,subInstanceId);
            JSONArray workFlowJson = JSONArray.fromObject(str);
			body.put("workflowinfo", workFlowJson);   //接口返回的数据

			model.setBody(body);
			model.setMessage("订单跟踪信息查询成功");
			model.setStatus("success");

			JSONObject jsonObject = JSONObject.fromObject(model);
			return jsonObject.toString();

		}catch(Exception e){
			e.printStackTrace();
			model.setMessage("订单跟踪信息查询失败");
			model.setStatus("fail");
			return JSONObject.fromObject(model).toString();
		}
		
	}
	@Override
	protected BaseDao<INSBOrder> getBaseDao() {
		return null;
	}
}
