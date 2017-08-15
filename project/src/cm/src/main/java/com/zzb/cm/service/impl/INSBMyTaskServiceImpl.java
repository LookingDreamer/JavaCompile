package com.zzb.cm.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cninsure.core.utils.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.jobpool.DispatchService;
import com.cninsure.system.dao.INSCUserDao;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCCodeService;
import com.common.ModelUtil;
import com.common.RedisException;
import com.common.TaskConst;
import com.common.WorkFlowException;
import com.zzb.cm.controller.vo.MyTaskVo;
import com.zzb.cm.dao.INSBQuoteinfoDao;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.cm.service.INSBCommonWorkflowTrackservice;
import com.zzb.cm.service.INSBMyTaskService;
import com.zzb.conf.dao.INSBOrderpaymentDao;
import com.zzb.conf.dao.INSBWorkflowsubtrackDao;
import com.zzb.conf.entity.INSBOperatorcomment;
import com.zzb.conf.entity.INSBOrderpayment;
import com.zzb.conf.entity.INSBUsercomment;
import com.zzb.conf.entity.INSBWorkflowmain;
import com.zzb.conf.entity.INSBWorkflowsub;
import com.zzb.conf.service.INSBOperatorcommentService;
import com.zzb.conf.service.INSBUsercommentService;
import com.zzb.conf.service.INSBWorkflowmainService;
import com.zzb.conf.service.INSBWorkflowmaintrackService;
import com.zzb.conf.service.INSBWorkflowsubService;
import com.zzb.conf.service.INSBWorkflowsubtrackService;

@Service
@Transactional
public class INSBMyTaskServiceImpl extends BaseServiceImpl implements
		INSBMyTaskService {
	@Resource
	private INSBWorkflowmainService workflowmainService;
	@Resource
	private INSBWorkflowsubService workflowsubService;
	@Resource
	private INSBWorkflowmaintrackService workflowmaintrackService;
	@Resource
	private INSBWorkflowsubtrackService workflowsubtrackService;
	@Resource
	private INSCCodeService codeService;
	@Resource
	private DispatchService dispatchService;
	@Resource
	private INSBOrderpaymentDao orderpaymentDao;
	@Resource
	private INSCUserDao userDao;
	@Resource
	private INSBWorkflowsubtrackDao workflowsubtrackDao;
	@Resource
	private INSBCommonWorkflowTrackservice insbCommonWorkflowTrackservice;
	@Resource
	private INSBUsercommentService insbUsercommentService;
	@Resource
	private INSBQuotetotalinfoDao quotetotalinfoDao;
	@Resource
	private INSBQuoteinfoDao quoteinfoDao;
	@Resource
	INSBOperatorcommentService insbOperatorcommentService;

	@Override
	public Map<String, Object> queryTaskList(MyTaskVo myTaskVo) {
		List<Map<String, Object>> rowList = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		rowList = workflowmainService.getMyTaskInPage(BeanUtils.toMap(myTaskVo));
		//组织查询结果数据
		Map<String, Integer> indexMap = new HashMap<String, Integer>();
		List<Integer> delIndex = new ArrayList<Integer>();
//		for (Map<String, Object> taskMap: rowList) {
		for(int i = 0; i < rowList.size(); i++){
			if(indexMap.get((String)rowList.get(i).get("maininstanceid")+"-"+(String)rowList.get(i).get("taskcode")) != null){
				int index = indexMap.get((String)rowList.get(i).get("maininstanceid")+"-"+(String)rowList.get(i).get("taskcode"));
				rowList.get(index).put("paymentransaction", rowList.get(index).get("paymentransaction") + " " + rowList.get(i).get("paymentransaction"));
				delIndex.add(i);
				continue;
			}
			indexMap.put((String)rowList.get(i).get("maininstanceid")+"-"+(String)rowList.get(i).get("taskcode"), i);
			//二次支付查询保单信息
			if("21".equals((String)rowList.get(i).get("taskcode"))){
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("inscomcode", rowList.get(i).get("inscomcode"));
				map.put("maininstanceid", rowList.get(i).get("maininstanceid"));
                map.put("taskcode", "21");
				Map<String, Object> payInfo = workflowsubService.getMediumPayInfo(map);
				rowList.get(i).putAll(payInfo);
			}
			//非认证任务展示流程图
			if(!"999".equals((String) rowList.get(i).get("taskcode"))){
				boolean flag = isThereSecondPayment(rowList.get(i));
				rowList.get(i).put("workFlowIndex",OrganizeWorkFlowInfo((String)rowList.get(i).get("taskcode"),flag));
				if("15".equals((String) rowList.get(i).get("mtaskcode"))){
					rowList.get(i).put("workflowinfoList",GetWorkFlowInfosXB(flag));
				}else{
					rowList.get(i).put("workflowinfoList",GetWorkFlowInfos(flag));
				}
			}
//			//任务摘要的用户备注取最新的一条
//			List<INSBUsercomment> userComment = insbUsercommentService.getNearestUserComment((String)taskMap.get("maininstanceid"),(String) taskMap.get("inscomcode"),(String) taskMap.get("taskcode"));
//			if(userComment!=null && userComment.size()>0)
//			taskMap.put("commentcontent",userComment);
			//修改为显示所有的备注信息
			//查询给操作员备注信息
			String maininstanceid = (String)rowList.get(i).get("maininstanceid");
			String inscomcode = (String) rowList.get(i).get("inscomcode");
			List<String> operatorNotiList = insbOperatorcommentService.getOperCommentByMaininstanceid(maininstanceid, inscomcode);
			if(operatorNotiList!=null && operatorNotiList.size() > 0){
				String noti = ModelUtil.replaceHtml(operatorNotiList.get(0));
				//如果备注超过二十五个字符,则点击更多按钮,显示全部的备注	
				noti=noti.length()>25?noti.substring(0,25)+"...":noti;
				rowList.get(i).put("operatorcommentList",noti+"<a href=\"javascript:openNoti('" + (String)rowList.get(i).get("maininstanceid") +"','" + (String) rowList.get(i).get("inscomcode") + "', '2');\">更多</a>");
			}
			//查询给用户备注信息
			/*List<String> notiList = insbUsercommentService.getUserComment(maininstanceid, inscomcode);
			//判断所有给用户备注的集合是否为空
			if(notiList!=null && notiList.size() > 0){
				String usercommentlist = notiList.get(0);
				//如果备注超过二十五个字符,则点击更多按钮,显示全部的备注
				usercommentlist=usercommentlist.length()>25?usercommentlist.substring(0,25)+"...":usercommentlist;
				rowList.get(i).put("usercommentlist",usercommentlist+"<a href=\"javascript:openNoti('" + (String)rowList.get(i).get("maininstanceid") +"','" + (String) rowList.get(i).get("inscomcode") + "', '1');\">更多</a>");
			}*/
			//查询给用户备注信息
			List<Map<String, Object>> notiList = insbUsercommentService.getUserCommentAndType(maininstanceid, inscomcode);
			if (notiList != null && notiList.size() > 0) {
				INSBUsercomment usc = (INSBUsercomment) notiList.get(0).get("userComment");
				String content = usc.getCommentcontent();
				//如果备注超过二十五个字符,则点击更多按钮,显示全部的备注
				content = content.length() > 25 ? content.substring(0, 25) + "..." : content;
				rowList.get(i).put("usercommentlist", content + "<a href=\"javascript:openNoti('" + (String) rowList.get(i).get("maininstanceid") + "','" + (String) rowList.get(i).get("inscomcode") + "', '1');\">更多</a>");
			}

			//查询用户备注信息
			List<Map<String, Object>> commentList = insbUsercommentService.getNearestUserCommentAndType(maininstanceid, inscomcode);
			if (commentList != null && commentList.size() > 0) {
				INSBUsercomment usc = (INSBUsercomment) commentList.get(0).get("userComment");
				String content = usc.getCommentcontent();
				//如果备注超过二十五个字符,则点击更多按钮,显示全部的备注
				content = content.length() > 25 ? content.substring(0, 25) + "..." : content;
				rowList.get(i).put("uscs", content + "<a href=\"javascript:openNoti('" + (String) rowList.get(i).get("maininstanceid") + "','" + (String) rowList.get(i).get("inscomcode") + "', '3');\">更多</a>");
			}
		}
		for(int i = delIndex.size() - 1; i >= 0; i--)
			rowList.remove(delIndex.get(i).intValue());
		resultMap.put("rowList", rowList);
		resultMap.put("myTaskVo",myTaskVo);
		return resultMap;
	}

	private boolean isThereSecondPayment(Map<String, Object> taskMap) {
		String instanceid = (String) taskMap.get("instanceid");
		String maininstanceid = (String) taskMap.get("maininstanceid");
		String taskcode = (String) taskMap.get("taskcode");
		String inscomcode = (String) taskMap.get("inscomcode");
		if(instanceid.equals(maininstanceid) && (taskcode.equals("27")||taskcode.equals("24"))){
//			instanceid = workflowsubService.getInstanceidByMaininstanceId(maininstanceid);
			INSBQuotetotalinfo temp = new INSBQuotetotalinfo();
			temp.setTaskid(maininstanceid);
			temp = quotetotalinfoDao.selectOne(temp);
			INSBQuoteinfo quoteinfo = new INSBQuoteinfo();
			quoteinfo.setInscomcode(inscomcode);
			quoteinfo.setQuotetotalinfoid(temp.getId());
			quoteinfo = quoteinfoDao.selectOne(quoteinfo);
			instanceid = quoteinfo.getWorkflowinstanceid();
		}
		return workflowsubtrackDao.isThereSecondPayment(maininstanceid,instanceid);
	}

	public boolean checkCloseTask(String instanceId, String providerid, String operator) {
		//检查是否已经关闭
		if (StringUtil.isEmpty(instanceId) || StringUtil.isEmpty(providerid)) return false;
		Map<String, String> params = new HashMap<String, String>();
		params.put("maininstanceid",instanceId);
		params.put("inscomcode",providerid);
		int i = quoteinfoDao.closeTask(params);
		//return i>0 ? true : false;
		if(i>0){
			return true;
		}else{
			//检查任务是否还属于该operator处理
			INSBWorkflowmain mainwork = workflowmainService.selectByInstanceId(instanceId);
			if(TaskConst.QUOTING_2.equals(mainwork.getTaskcode())||TaskConst.QUOTING_CONTINUE_15.equals(mainwork.getTaskcode())){
				INSBQuotetotalinfo temp = new INSBQuotetotalinfo();
				temp.setTaskid(instanceId);
				temp = quotetotalinfoDao.selectOne(temp);
				INSBQuoteinfo quoteinfo = new INSBQuoteinfo();
				quoteinfo.setInscomcode(providerid);
				quoteinfo.setQuotetotalinfoid(temp.getId());
				quoteinfo = quoteinfoDao.selectOne(quoteinfo);
				INSBWorkflowsub query = new INSBWorkflowsub();
				query.setInstanceid(quoteinfo.getWorkflowinstanceid());
				query.setOperator(operator);
				List<INSBWorkflowsub> subs = workflowsubService.queryList(query);
				if(null==subs||subs.isEmpty()){//任务不属于当前操作人
					return true;
				}else{
					return false;
				}
			}else{
				if(operator.equals(mainwork.getOperator())){
					return false;
				}else{
					return true;
				}
			}
		}
		
	}

	private List<Map<String,String>> GetWorkFlowInfos(boolean flag) {
		List<Map<String,String>> temp = new ArrayList<Map<String,String>>();
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> map2 = new HashMap<String, String>();
		Map<String, String> map3 = new HashMap<String, String>();
		Map<String, String> map4 = new HashMap<String, String>();
		Map<String, String> map6 = new HashMap<String, String>();
		Map<String, String> map7 = new HashMap<String, String>();
		Map<String, String> map8 = new HashMap<String, String>();
		map.put("taskCode", "1");
		map.put("taskName", "信息录入");
		map2.put("taskCode", "7,8");
		map2.put("taskName", "报价");
		map3.put("taskCode", "18");
		map3.put("taskName", "核保");
		map4.put("taskCode", "20");
		map4.put("taskName", "支付");
		map6.put("taskCode", "27");
		map6.put("taskName", "承保");
		map7.put("taskCode", "23");
		map7.put("taskName", "打单");
		map8.put("taskCode", "24");
		map8.put("taskName", "配送");
		temp.add(map);
		temp.add(map2);
		temp.add(map3);
		temp.add(map4);
		if(flag){
			Map<String, String> map5 = new HashMap<String, String>();
			map5.put("taskCode", "21");
			map5.put("taskName", "二次支付");
			temp.add(map5);
		}
		temp.add(map6);
		temp.add(map7);
		temp.add(map8);
//		temp.add(map9);
		return temp;
	}
	private List<Map<String,String>> GetWorkFlowInfosXB(boolean flag) {
		List<Map<String,String>> temp = new ArrayList<Map<String,String>>();
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> map2 = new HashMap<String, String>();
		Map<String, String> map3 = new HashMap<String, String>();
		Map<String, String> map4 = new HashMap<String, String>();
		Map<String, String> map6 = new HashMap<String, String>();
		Map<String, String> map7 = new HashMap<String, String>();
		Map<String, String> map8 = new HashMap<String, String>();
		map.put("taskCode", "1");
		map.put("taskName", "信息录入");
		map2.put("taskCode", "15");
		map2.put("taskName", "快速续保");
		map3.put("taskCode", "18");
		map3.put("taskName", "核保");
		map4.put("taskCode", "20");
		map4.put("taskName", "支付");
		map6.put("taskCode", "27");
		map6.put("taskName", "承保");
		map7.put("taskCode", "23");
		map7.put("taskName", "打单");
		map8.put("taskCode", "24");
		map8.put("taskName", "配送");
		temp.add(map);
		temp.add(map2);
		temp.add(map3);
		temp.add(map4);
		if(flag){
			Map<String, String> map5 = new HashMap<String, String>();
			map5.put("taskCode", "21");
			map5.put("taskName", "二次支付");
			temp.add(map5);
		}
		temp.add(map6);
		temp.add(map7);
		temp.add(map8);
//		temp.add(map9);
		return temp;
	}

	private int OrganizeWorkFlowInfo(String taskcode,boolean secflag) {
		List<Map<String, String>> temp = GetWorkFlowInfos(secflag);
		int flag = 0;
		for (int i=0;i<temp.size();i++) {
			if(temp.get(i).get("taskCode").contains(taskcode)){
				flag = i;
				break;
			}
		}
		return flag;
	}

	@Override
	public String getPayResult(String taskid) {
		String result = null;
		INSBOrderpayment orderpayment = new INSBOrderpayment();
		orderpayment.setTaskid(taskid);
		orderpayment = orderpaymentDao.selectOne(orderpayment);
		if(orderpayment != null){
			result = orderpayment.getId()+" "+orderpayment.getPayresult();
		}
		return result;
	}
	
	private Map<String,Object> getLastNode(String instanceid,String maininstanceid) {
		Map<String,Object> lastNode = null;
		//实例id相同说明是主流程否则子流程
		if(instanceid.equals(maininstanceid)){
			lastNode = workflowmaintrackService.getMyTaskLastNode(instanceid);
		}else {
			lastNode = workflowsubtrackService.getMyTaskLastNode(maininstanceid,instanceid);
		}
		return lastNode;
	}

	private Map<String,Object> getLimitAndPageSize(MyTaskVo myTaskVo){
		//组织分页信息
		long total = myTaskVo.getTotals();
		int currentPage = myTaskVo.getCurrentpage()==0?1:myTaskVo.getCurrentpage();
		int pageSize = myTaskVo.getPagesize()==0?5:myTaskVo.getPagesize();
		int limit = (currentPage - 1) * pageSize;
		int totalpage = (int) (total / pageSize);// 总页数
		int mod = (int) (total % pageSize);// 最后一页的条数
		if (mod != 0){
			totalpage++;
		}
		myTaskVo.setTotalpage(totalpage);
		myTaskVo.setCurrentpage(currentPage);
		//组织分页查询条件
		Map<String, Object> map = BeanUtils.toMap(myTaskVo);
		map.put("pageSize", pageSize);
		map.put("limit", limit);
		return map;
	}
	
	@Override
	public long countDayTask(MyTaskVo taskVo) {
		Map<String, Object> map = BeanUtils.toMap(taskVo);
		map.put("modifytime",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		return workflowmaintrackService.countDayTask(map);
	}
	
	@Override
	public long countMonthTask(MyTaskVo taskVo) {
		Map<String, Object> map = BeanUtils.toMap(taskVo);
		map.put("modifytime",new SimpleDateFormat("yyyy-MM").format(new Date()));
		return workflowmaintrackService.countMonthTask(map);
	}

	@Override
	public void transformTask(String instanceId, String providerid,
			int instanceType, INSCUser fromUser, String tousercode) throws WorkFlowException, RedisException {
		INSCUser toUser = userDao.selectByUserCode(tousercode);
		dispatchService.reassignment(instanceId, providerid, instanceType, fromUser,toUser);
	}

	@Override
	protected BaseDao getBaseDao() {
		return null;
	}

}