package com.zzb.cm.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.tools.util.ValidateUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.service.INSCCodeService;
import com.zzb.cm.Interface.service.FlowInfo;
import com.zzb.cm.dao.INSBFlowerrorDao;
import com.zzb.cm.entity.INSBFlowerror;
import com.zzb.cm.service.INSBFlowerrorService;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.dao.INSBWorkflowsubDao;

@Service
@Transactional
public class INSBFlowerrorServiceImpl extends BaseServiceImpl<INSBFlowerror> implements
		INSBFlowerrorService {
	@Resource
	private INSBFlowerrorDao insbFlowerrorDao;
	@Resource 
	private INSBWorkflowsubDao insbWorkflowsubDao;
	@Resource
	private INSBPolicyitemDao insbPolicyitemDao;
	@Resource INSCCodeService inscCodeService;

	@Override
	protected BaseDao<INSBFlowerror> getBaseDao() {
		return insbFlowerrorDao;
	}

	@Override
	public Map<String,String> getErrorInfo(String taskid, String inscomcode) {
		Map<String,String> resultMap = new HashMap<String,String>();
		INSBFlowerror queryInsbFlowerror = new INSBFlowerror();
		queryInsbFlowerror.setTaskid(taskid);
		queryInsbFlowerror.setInscomcode(inscomcode);
		queryInsbFlowerror.setFlowname("报价失败");
		List<INSBFlowerror> dataINSBFlowerrorList = insbFlowerrorDao.selectList(queryInsbFlowerror);
		if(dataINSBFlowerrorList.size()<=0){
			return null;
		}
		String serverEnvrment = ValidateUtil.getConfigValue("server.envirment");
		if("dat".equals(serverEnvrment.trim())){
			INSBFlowerror insbFlowerror = new INSBFlowerror();
			insbFlowerror = dataINSBFlowerrorList.get(0);
			resultMap.put("errorcode", dataINSBFlowerrorList.get(0).getErrorcode());
			resultMap.put("errormsg", ("0".equals(insbFlowerror.getFiroredi())?"{报价失败}":"(报价失败)") + dataINSBFlowerrorList.get(0).getErrordesc());
		}else{//0 代表robot，1代表edi
			for(INSBFlowerror dataInsbFlowerror : dataINSBFlowerrorList){
				INSCCode query = new INSCCode();
				query.setCodetype("fronterrorinfo");
				query.setCodevalue(dataInsbFlowerror.getErrorcode());
				List<INSCCode> inscCodeList = inscCodeService.queryList(query);
				if(inscCodeList.size()>0){
					if("12".equals(dataInsbFlowerror.getErrorcode())){
						resultMap.put("errorcode", dataInsbFlowerror.getErrorcode());
						if(!StringUtil.isEmpty(dataInsbFlowerror.getErrordesc())&&(dataInsbFlowerror.getErrordesc().contains("商业险终保日期")||dataInsbFlowerror.getErrordesc().contains("交强险终保日期"))){
							resultMap.put("errormsg", inscCodeList.get(0).getCodename().replace("desc", dataInsbFlowerror.getErrordesc()));
						}else{
							resultMap.put("errormsg", inscCodeList.get(0).getCodename().replace("desc，", ""));
						}
						break;
					}else{
						resultMap.put("errorcode", dataInsbFlowerror.getErrorcode());
						resultMap.put("errormsg", inscCodeList.get(0).getCodename());
						break;
					}
				}else{
					resultMap.put("errorcode", "0");
					resultMap.put("errormsg", "系统网路开小差了  有专人为您服务");
					break;
				}
			}
		}
		if(StringUtil.isEmpty(resultMap.get("errorcode"))){
			return null;
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> initErrorList(Map<String, Object> data) {
		Map<String, Object> map = new HashMap<String, Object>();
		long total = insbFlowerrorDao.selectPagingCount(data);
		List<Map<Object, Object>> infoList = insbFlowerrorDao.selectErrorListPaging(data);
		if (infoList != null) {
			for (Map<Object, Object> mapModel : infoList) { 
				if ("1".equals(mapModel.get("firoredi"))) {
					mapModel.put("firoredi", "EDI");
				} else if ("0".equals(mapModel.get("firoredi"))) {
					mapModel.put("firoredi", "精灵");
				}else if("2".equals(mapModel.get("firoredi"))){
					mapModel.put("firoredi", "支付");
				}else if("4".equals(mapModel.get("firoredi"))){
					mapModel.put("firoredi", "规则");
				}else if("5".equals(mapModel.get("firoredi"))){
					mapModel.put("firoredi", "核心保单同步");
				}else if("6".equals(mapModel.get("firoredi"))){
					mapModel.put("firoredi", "保单校验");
				}
				mapModel.put("flowcode", this.getFlowInfoCode(mapModel.get("flowcode").toString()).get("codename"));
			}
		}

		map.put("total", total);
		map.put("rows", infoList);
		return map;
	}
	@Override
	public Map<String, Object> initPushtocoreErrorList(Map<String, Object> data) {
		Map<String, Object> map = new HashMap<String, Object>();
		long total = insbFlowerrorDao.selectPagingCount(data);
		List<Map<Object, Object>> infoList = insbFlowerrorDao.selectErrorListPaging(data);
		if (infoList != null) {
			for (Map<Object, Object> mapModel : infoList) { 
				mapModel.put("operating", "<a href=\"javascript:window.parent.openDialogForCM('business/ordermanage/preEditPolicyNumber1?taskid="+mapModel.get("taskid").toString()+"&inscomcode="+mapModel.get("inscomcode").toString()+"');\">修改保单号</a><br/><a href=\"javascript:pushtocore('taskid="+mapModel.get("taskid").toString()+"&companyid="+mapModel.get("inscomcode").toString()+"');\">推送到核心</a>");
			}
		} 
		
		map.put("total", total);
		map.put("rows", infoList);
		return map;
	}
	@Override
	public Map<String, Object> initClosstaskList(Map<String, Object> data) {
		Map<String, Object> map = new HashMap<String, Object>();
		long total = insbPolicyitemDao.selectClosstaskPagingCount(data);
		List<Map<Object, Object>> infoList = insbPolicyitemDao.selectClosstaskPaging(data);
		if (infoList != null) {
			for (Map<Object, Object> mapModel : infoList) { 
				mapModel.put("operating", "<a href=\"javascript:closstask('taskid="+mapModel.get("taskid").toString()+"&inscomcode="+mapModel.get("inscomcode").toString()+"');\">关闭任务</a>");
			}
		} 
		
		map.put("total", total);
		map.put("rows", infoList);
		return map;
	}
	
	private Map<String,Object> getFlowInfoCode(String taskStatus){
		
		Map<String,Object> map=new HashMap<String,Object>();
		try{
			int code=Integer.parseInt(taskStatus);
			 if(code<=7&&code>=0){
				 map.put("code", FlowInfo.values()[code].getCode());
				 map.put("codename", FlowInfo.values()[code].getDesc());
				 map.put("taskType", "quote");
			 }
			 if(code<=15&&code>=8){
				 map.put("code", FlowInfo.values()[code+2].getCode());
				 map.put("codename", FlowInfo.values()[code+2].getDesc());
				 map.put("taskType", "insure");
			 }
			 if(code<=18&&code>=16){
				 map.put("code", FlowInfo.values()[code+4].getCode());
				 map.put("codename", FlowInfo.values()[code+4].getDesc());
				 map.put("taskType", "pay");
			 }
			 if(code<=21&&code>=19){
				 map.put("code", FlowInfo.values()[code+6].getCode());
				 map.put("codename", FlowInfo.values()[code+6].getDesc());
				 map.put("taskType", "approved");
			 }
		
		}catch(Exception e){
			
			 if("A".equalsIgnoreCase(taskStatus)){
				 map.put("code",FlowInfo.quoteover.getCode()) ;
				 map.put("codename", FlowInfo.quoteover.getDesc());
				 map.put("taskType", "quote");
			 }
			 if("B".equalsIgnoreCase(taskStatus)){
				 map.put("code",FlowInfo.underwritingover.getCode()) ;
				 map.put("codename", FlowInfo.underwritingover.getDesc());
				 map.put("taskType", "insure");
			 }
			 if("C".equalsIgnoreCase(taskStatus)){
				 map.put("code",FlowInfo.payover.getCode()) ;
				 map.put("codename", FlowInfo.payover.getDesc());
				 map.put("taskType", "pay");
			 }
			 if("D".equalsIgnoreCase(taskStatus)){
				 map.put("code",FlowInfo.insover.getCode()) ;
				 map.put("codename", FlowInfo.insover.getDesc());
				 map.put("taskType", "approved");
			 }
			 if("A1".equalsIgnoreCase(taskStatus)){
				 map.put("code",FlowInfo.quotefiled.getCode()) ;
				 map.put("codename", FlowInfo.quotefiled.getDesc());
				 map.put("taskType", "quotefiled");
			 }
			 if("B1".equalsIgnoreCase(taskStatus)){
				 map.put("code",FlowInfo.underwritingoverfiled.getCode()) ;
				 map.put("codename", FlowInfo.underwritingoverfiled.getDesc());
				 map.put("taskType", "insurefiled");
			 }
			 if("C1".equalsIgnoreCase(taskStatus)){
				 map.put("code",FlowInfo.payfiled.getCode()) ;
				 map.put("codename", FlowInfo.payfiled.getDesc());
				 map.put("taskType", "payfiled");
			 }
			 if("D1".equalsIgnoreCase(taskStatus)){
				 map.put("code",FlowInfo.insfiled.getCode()) ;
				 map.put("codename", FlowInfo.insfiled.getDesc());
				 map.put("taskType", "approvedfiled");
			 }
			 if("D2".equalsIgnoreCase(taskStatus)){
				 map.put("code",FlowInfo.insdatanotover.getCode()) ;
				 map.put("codename", FlowInfo.insdatanotover.getDesc());
				 map.put("taskType", "approvedovernotdata");
			 }
		}
			
		return map;
	}

	@Override
	public List<INSBFlowerror> selectflowcode() {
		return insbFlowerrorDao.selectflowcode();
	}

	@Override
	public void insertInsbFlowerror(String taskId,String inscomcode,String flowcode,String flowName,String fairyorEdi,String taskStatus,String result,String errordesc,String operator) {
		INSBFlowerror dataInsbFlowerror = new INSBFlowerror();
		dataInsbFlowerror.setOperator(operator);
		dataInsbFlowerror.setTaskid(taskId);
		dataInsbFlowerror.setInscomcode(inscomcode);
		dataInsbFlowerror.setFlowcode(flowcode);
		dataInsbFlowerror.setFlowname(flowName);
		dataInsbFlowerror.setFiroredi(fairyorEdi);
		dataInsbFlowerror.setTaskstatus(taskStatus);
		dataInsbFlowerror.setResult(result);
		dataInsbFlowerror.setErrordesc(errordesc);
		dataInsbFlowerror.setCreatetime(new Date());
		this.insert(dataInsbFlowerror);
		
	}

}