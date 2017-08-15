package com.zzb.chn.service.impl;

import javax.annotation.Resource;

import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.common.JsonUtil;
import com.zzb.chn.util.JsonUtils;
import com.zzb.extra.entity.INSBCommission;
import com.zzb.extra.entity.INSBCommissionRate;
import com.zzb.extra.model.AgentTaskModel;
import com.zzb.extra.service.INSBCommissionService;
import com.zzb.extra.service.INSBConditionsService;
import com.zzb.extra.util.ParamUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.chn.dao.INSBCommissionratioDao;
import com.zzb.chn.entity.INSBCommissionratio;
import com.zzb.chn.service.CHNCommissionRatioService;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class CHNCommissionRatioServiceImpl extends BaseServiceImpl<INSBCommissionratio> implements
		CHNCommissionRatioService {
	@Resource
	private INSBCommissionratioDao insbCommissionratioDao;

	@Override
	protected BaseDao<INSBCommissionratio> getBaseDao() {
		return insbCommissionratioDao;
	} 

	@Resource
	private INSBConditionsService insbConditionsService;

	@Resource
	private INSBCommissionService insbCommissionService;
 

	@Override
	public String queryPagingList(Map<String, Object> map) {
		String channelid = map.getOrDefault("channelid", "").toString();
		if (!channelid.equals(""))
			insbCommissionratioDao.cleanCommissionRatioStatus(channelid);

		long total = insbCommissionratioDao.queryPagingListCount(map);
		List<Map<Object, Object>> infoList = insbCommissionratioDao.queryPagingList(map);
		return ParamUtils.resultMap(total, infoList);
	}

	@Override
	public List<Map<String, String>> initChannelList() {
		return insbCommissionratioDao.initChannelList();
	}

	@Override
	public String queryTaskCommission(AgentTaskModel agentTaskModel) {
		String commercialcommissionRatioId = "";
		String compulsorycommissionRatioId = "";
		String adCommercialcommissionRatioId = "";
		String adCompulsorycommissionRatioId = "";

		Double commercialCommission = 0D;
		Double compulsoryCommission = 0D;
		Double adCommercialCommission = 0D;
		Double adCompulsoryCommission = 0D;

		Double commercialcommissionRatio = 1D;
		Double compulsorycommissionRatio = 1D;
/*
		agentTaskModel.setAgreementid(insbConditionsService.queryAgreementIdByTask(agentTaskModel));
		if (agentTaskModel.getChannelid() == null || agentTaskModel.getChannelid().equals(""))
			return ParamUtils.resultMap(false, "任务信息不正确");

		if (insbCommissionService.isLocked(agentTaskModel)) {
			List<INSBCommission> commissions = insbCommissionService.queryCommissions(agentTaskModel);
			for (INSBCommission commission : commissions) {
				Double counts = commission.getCounts();
				switch (commission.getCommissiontype()) {
					case "01":
						commercialCommission = counts;
						break;
					case "02":
						adCommercialCommission = counts;
						break;
					case "03":
						compulsoryCommission = counts;
						break;
				}
			}
		} else {

			Map<String, Object> params = insbConditionsService.queryParams(agentTaskModel);

			Double commercialPremium = Double.parseDouble(params.getOrDefault("commercialPremium", "0").toString());
			Double compulsoryPremium = Double.parseDouble(params.getOrDefault("compulsoryPremium", "0").toString());
			List<INSBCommissionratio> commissionRatioList = insbCommissionratioDao.queryCommissionByAgreement(agentTaskModel.getChannelid(), "channel");
			for (INSBCommissionratio commissionRatio : commissionRatioList) {
				if (insbConditionsService.execute("02", commissionRatio.getId(), params)) {
					Double rate = commissionRatio.getRate();
					if (commissionRatio.getCommissiontype().equals("01") && rate <= commercialcommissionRatio) {
						commercialcommissionRatioId = commissionRatio.getId();
						commercialcommissionRatio = rate;
						commercialCommission = commercialPremium * rate;
					}
					if (commissionRatio.getCommissiontype().equals("03") && rate <= compulsorycommissionRatio) {
						compulsorycommissionRatioId = commissionRatio.getId();
						compulsorycommissionRatio = rate;
						compulsoryCommission = compulsoryPremium * rate;
					}
				}
			}

			if (commercialCommission > 0)
				adCommercialCommission = commercialPremium * 0.03;

			insbCommissionService.deleteCommissionByTask(agentTaskModel);
			if (!commercialcommissionRatioId.equals(""))
				updateCommission(agentTaskModel, "01", commercialcommissionRatioId, (double) commercialCommission.intValue());
			if (!compulsorycommissionRatioId.equals(""))
				updateCommission(agentTaskModel, "03", compulsorycommissionRatioId, (double) compulsoryCommission.intValue());

			if (commercialCommission > 0)
				updateCommission(agentTaskModel, "02", adCommercialcommissionRatioId, (double) adCommercialCommission.intValue());
		}*/
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("commercialCommission", (double) commercialCommission.intValue());
		map.put("compulsoryCommission", (double) compulsoryCommission.intValue());
		map.put("adCommercialCommission", (double) adCommercialCommission.intValue());
		map.put("adCompulsoryCommission", (double) adCompulsoryCommission.intValue());
		return ParamUtils.resultMap(true, map);
	}

	@Override
	public String addCommissionRatio(INSBCommissionratio commissionRatio) {
		if (commissionRatio.getChannelid() == null || commissionRatio.getChannelid().equals(""))
			return ParamUtils.resultMap(false, "参数不正确");

		commissionRatio.setId(UUIDUtils.random());
		commissionRatio.setCommissiontype(StringUtil.isEmpty(commissionRatio.getCommissiontype())?"01":commissionRatio.getCommissiontype());
		commissionRatio.setStatus("0");
		commissionRatio.setEffectivetime(getNextDate());
		commissionRatio.setCreatetime(new Date());
		int result = insbCommissionratioDao.insertCommissionRatio(commissionRatio);

		if (result > 0) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", commissionRatio.getId());
			map.put("status", commissionRatio.getStatus());
			map.put("commissiontype", commissionRatio.getCommissiontype());
			map.put("ratio", commissionRatio.getRatio());
			map.put("effectivetime", dateFormat.format(commissionRatio.getEffectivetime()));
			map.put("channeleffectivetime", dateFormat.format(commissionRatio.getEffectivetime()));
			map.put("taxrate",commissionRatio.getTaxrate() );
			return ParamUtils.resultMap(true, map);
		} else
			return ParamUtils.resultMap(false, "操作失败");
	}

	private Date getNextDate(){
		//生效时间默认为下一天0点
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());   //设置当前日期
		calendar.add(Calendar.DATE, 1); //日期加1天
		Date effectivetime =calendar.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(effectivetime);
		ParsePosition parsePosition = new ParsePosition(0);
		Date effectivetime_2 = formatter.parse(dateString,parsePosition);
		return effectivetime_2;
	}

	@Override
	public String copyCommissionRatio(String commissionRatioId, String operator) {
		INSBCommissionratio commissionRatio = insbCommissionratioDao.queryCommissionRatioById(commissionRatioId);
		commissionRatio.setId(UUIDUtils.random());
		commissionRatio.setStatus("0");
		commissionRatio.setEffectivetime(getNextDate());
		commissionRatio.setCreatetime(new Date());
		commissionRatio.setModifytime(null);
		commissionRatio.setTerminaltime(null);
		commissionRatio.setNoti(null);
		commissionRatio.setOperator(operator);
		int result = insbCommissionratioDao.insertCommissionRatio(commissionRatio);
		if (result > 0) {
			if (insbConditionsService.copyConditions("04", commissionRatio.getId(), commissionRatioId)) {
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", commissionRatio.getId());
				map.put("status", commissionRatio.getStatus());
				map.put("commissiontype", commissionRatio.getCommissiontype());
				map.put("ratio", commissionRatio.getRatio());
				map.put("effectivetime", dateFormat.format(commissionRatio.getEffectivetime()));
				return ParamUtils.resultMap(true, map);
			}
		}
		return ParamUtils.resultMap(false, "操作失败");
	}

	@Override
	public String updateCommissionRatio(INSBCommissionratio commissionRatio) {
		int result = insbCommissionratioDao.updateCommissionRatio(commissionRatio);
		if (result > 0)
			return ParamUtils.resultMap(true, "操作成功");
		else
			return ParamUtils.resultMap(false, "操作失败");
	}

	@Override
	public String updateCommissionRatioStatus(INSBCommissionratio commissionRatio) {
		if (commissionRatio.getStatus().equals("1")) {
			String conditionsText = insbConditionsService.queryConditionsText(commissionRatio.getId());
			commissionRatio.setNoti(conditionsText);
			INSBCommissionratio temp = insbCommissionratioDao.queryCommissionRatioById(commissionRatio.getId());
			if(StringUtil.isEmpty(temp.getCalculatetype())){
				return ParamUtils.resultMap(false, "计算方式不能为空！");
			}
			//System.out.println("DateUtil.getCurrentDate(DateUtil.Format_Date)="+DateUtil.getCurrentDate(DateUtil.Format_Date));
			//System.out.println("DateUtil.toString(temp.getEffectivetime(), DateUtil.Format_Date))="+DateUtil.toString(temp.getEffectivetime(), DateUtil.Format_Date));
			INSBCommissionratio tempInsbCommissionRatio = new INSBCommissionratio();
			tempInsbCommissionRatio.setId(commissionRatio.getId());
			tempInsbCommissionRatio.setChannelid(commissionRatio.getChannelid());
			tempInsbCommissionRatio.setCommissiontype(commissionRatio.getCommissiontype());

			tempInsbCommissionRatio.setStatus(commissionRatio.getStatus());
			tempInsbCommissionRatio.setNoti(commissionRatio.getNoti());
			tempInsbCommissionRatio.setModifytime(new Date());
			tempInsbCommissionRatio.setOperator(commissionRatio.getOperator());
			if(DateUtil.getCurrentDate(DateUtil.Format_Date).equals(DateUtil.toString(temp.getEffectivetime(), DateUtil.Format_Date))
					||(DateUtil.getCurrentDate(DateUtil.Format_Date).compareTo(DateUtil.toString(temp.getEffectivetime(), DateUtil.Format_Date)))>0){
				commissionRatio.setEffectivetime(new Date());
				tempInsbCommissionRatio.setTerminaltime(commissionRatio.getEffectivetime());
			}else{
				commissionRatio.setEffectivetime(temp.getEffectivetime());
				tempInsbCommissionRatio.setTerminaltime(this.getLastDay(temp.getEffectivetime()));
			}
			tempInsbCommissionRatio.setEffectivetime(commissionRatio.getEffectivetime());
			insbCommissionratioDao.updateTerminalTimeByNoti(tempInsbCommissionRatio);

		}else{
			INSBCommissionratio temp = insbCommissionratioDao.queryCommissionRatioById(commissionRatio.getId());
			commissionRatio.setEffectivetime(temp.getEffectivetime());
			commissionRatio.setTerminaltime(new Date());
		}
		commissionRatio.setModifytime(new Date());
		int result = insbCommissionratioDao.updateCommissionRatioStatus(commissionRatio);
		if (result > 0)
			return ParamUtils.resultMap(true, "操作成功");
		else
			return ParamUtils.resultMap(false, "操作失败");
	}

	private Date getLastDay(Date day){
		//生效时间默认为下一天0点
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(day);   //设置当前日期
		calendar.add(Calendar.DATE, -1); //日期加1天
		Date time =calendar.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(time);
		dateString +=" 23:59:59";
		ParsePosition parsePosition = new ParsePosition(0);
		Date time2 = formatter2.parse(dateString,parsePosition);
		return time2;
	}

	@Override
	public String delCommissionRatio(String id) {
		Map<String ,Object> params = new HashMap<>();
		params.put("commissionratioid",id);
		if (insbCommissionService.existRate(params))
			return ParamUtils.resultMap(false, "佣金系数配置已生效生成过佣金，不可删除");

		INSBCommissionratio commissionRatio = insbCommissionratioDao.queryCommissionRatioById(id);
		if (commissionRatio.getStatus().equals("1"))
			return ParamUtils.resultMap(false, "佣金系数配置需关闭后才可删除");

		int result = insbCommissionratioDao.delCommissionRatio(id);

		insbConditionsService.deleteConditionsBySourceId(id);

		if (result > 0)
			return ParamUtils.resultMap(true, "操作成功");
		else
			return ParamUtils.resultMap(false, "操作失败");
	}

	/**
	 * 更新任务佣金
	 *
	 * @param agentTaskModel
	 * @return
	 */
	private void updateCommission(AgentTaskModel agentTaskModel, String commissionType, String commissionRateId, Double counts) {
		INSBCommission commission = new INSBCommission();
		commission.setTaskid(agentTaskModel.getTaskid());
		commission.setAgreementid(agentTaskModel.getChannelid());
		commission.setCommissiontype(commissionType);
		commission.setCommissionrateid(commissionRateId);
		commission.setCounts(counts);
		commission.setStatus("0");
		commission.setOperator("admin");
		insbCommissionService.saveCommission(commission);
	}

	@Override
	public String testCommissionRule(Map<String, Object> params) {
		Double commercialCommission = 0D;
		Double compulsoryCommission = 0D;
		Double adCommercialCommission = 0D;
		Double adCompulsoryCommission = 0D;

		Double commercialcommissionRatio = 1D;
		Double compulsorycommissionRatio = 1D;

		String commercialConditionParams = "";
		String compulsoryConditionParams = "";

		String channelid = params.getOrDefault("channelid", "").toString();

		Double commercialPremium = Double.parseDouble(params.getOrDefault("commercialPremium", "0").toString());
		Double compulsoryPremium = Double.parseDouble(params.getOrDefault("compulsoryPremium", "0").toString());
		List<INSBCommissionratio> commissionRatioList = insbCommissionratioDao.queryCommissionRatioByChannel(channelid);
		for (INSBCommissionratio commissionRatio : commissionRatioList) {
			if (insbConditionsService.execute("04", commissionRatio.getId(), params)) {
				Double rate = commissionRatio.getRatio();
				if (commissionRatio.getCommissiontype().equals("01") && rate <= commercialcommissionRatio) {
					commercialcommissionRatio = rate;
					commercialCommission = commercialPremium * rate;
					commercialConditionParams = commissionRatio.getNoti();
				}
				if (commissionRatio.getCommissiontype().equals("03") && rate <= compulsorycommissionRatio) {
					compulsorycommissionRatio = rate;
					compulsoryCommission = compulsoryPremium * rate;
					compulsoryConditionParams = commissionRatio.getNoti();
				}
			}
		}

		if (commercialCommission > 0)
			adCommercialCommission = commercialPremium * 0.03;

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("commercialCommission", (double) commercialCommission.intValue());
		map.put("compulsoryCommission", (double) compulsoryCommission.intValue());
		map.put("adCommercialCommission", (double) adCommercialCommission.intValue());
		map.put("adCompulsoryCommission", (double) adCompulsoryCommission.intValue());
		map.put("commercialcommissionRatio", commercialcommissionRatio);
		map.put("compulsorycommissionRatio", compulsorycommissionRatio);
		map.put("commercialConditionParams", commercialConditionParams);
		map.put("compulsoryConditionParams", compulsoryConditionParams);
		return ParamUtils.resultMap(true, map);
	}

	@Override
	public String queryCommissionRatioChannel() {
		List<Map<Object, Object>> channelList = insbCommissionratioDao.queryCommissionRatioChannel();
		return ParamUtils.resultMap(channelList.size(), channelList);
	}

	@Override
	public Map<String, String> batchUpdateCommissionRatioStatus(String ratioIds,String status,String userCode){
		Map<String, String> resultMap = new HashMap<String, String>();
		try {
			String[] ratioIdArray = ratioIds.split(",");
			Map<String, Object> tempMap = new HashMap<String, Object>();
			ArrayList<Object> tempArr = new ArrayList<Object>();
			for (String ratioId : ratioIdArray) {
				INSBCommissionratio commissionRatio = insbCommissionratioDao.selectById(ratioId);
				commissionRatio.setId(ratioId);
				commissionRatio.setOperator(userCode);
				commissionRatio.setModifytime(new Date());
				commissionRatio.setStatus(status);
				tempMap = JsonUtils.deserialize(this.updateCommissionRatioStatus(commissionRatio),Map.class);
				if(!(Boolean)tempMap.get("success")){
					tempArr.add(tempMap.get("result"));
				}
			}
			if(!tempArr.isEmpty()){
				resultMap.put("code", "1");
				resultMap.put("message", "计算方式为空配置未启用！");
			}else {
				resultMap.put("code", "0");
				resultMap.put("message", "操作成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("code", "1");
			resultMap.put("message", "操作失败，请稍候重试");
		}
		return resultMap;
	}

	@Override
	public String batchCopyCommissionRatio(String ratioIds,String channelIds,String userCode){
		String[] ratioIdArray = ratioIds.split(",");
		String[] channelIdArray = channelIds.split(",");
		//生效时间默认为下一天0点
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());   //设置当前日期
		calendar.add(Calendar.DATE, 1); //日期加1天
		Date effectivetime =calendar.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(effectivetime);
		ParsePosition parsePosition = new ParsePosition(0);
		Date effectivetime_2 = formatter.parse(dateString,parsePosition);
		for (String rateId : ratioIdArray) {
			for(String channelId : channelIdArray){
				INSBCommissionratio commissionRatio = insbCommissionratioDao.queryCommissionRatioById(rateId);
				commissionRatio.setId(UUIDUtils.random());
				commissionRatio.setStatus("0");
				commissionRatio.setEffectivetime(effectivetime_2);
				commissionRatio.setCreatetime(new Date());
				commissionRatio.setModifytime(null);
				commissionRatio.setTerminaltime(null);
				commissionRatio.setNoti(null);
				commissionRatio.setOperator(userCode);
				commissionRatio.setChannelid(channelId);
				int result = insbCommissionratioDao.insertCommissionRatio(commissionRatio);
				if (result > 0) {
					if (insbConditionsService.copyConditions("04", commissionRatio.getId(), rateId)) {
						DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("id", commissionRatio.getId());
						map.put("status", commissionRatio.getStatus());
						map.put("commissiontype", commissionRatio.getCommissiontype());
						map.put("ratio", commissionRatio.getRatio());
						map.put("effectivetime", dateFormat.format(commissionRatio.getEffectivetime()));
					}
				}
			}
		}
		return ParamUtils.resultMap(true, "操作成功！");
	}

	@Override
	public String updateCommissionRatioTerminalTime(INSBCommissionratio commissionRatio){
		INSBCommissionratio insbCommissionRatio = insbCommissionratioDao.selectById(commissionRatio.getId());
		insbCommissionRatio.setTerminaltime(commissionRatio.getTerminaltime());
		insbCommissionRatio.setModifytime(new Date());
		return this.updateCommissionRatio(insbCommissionRatio);
	}

	@Override
	public String batchUpdateCommissionRatioTerminalTime(String ratioIds,Date terminalTime,String userCode){
		String[] ratioIdArray = ratioIds.split(",");
		INSBCommissionratio insbCommissionRatio = new INSBCommissionratio();
		for (String rateId : ratioIdArray) {
			insbCommissionRatio = insbCommissionratioDao.selectById(rateId);
			insbCommissionRatio.setTerminaltime(terminalTime);
			insbCommissionRatio.setModifytime(new Date());
			insbCommissionRatio.setOperator(userCode);
			this.updateCommissionRatio(insbCommissionRatio);
		}
		return ParamUtils.resultMap(true, "操作成功！");
	}

	@Override
	public String batchDeleteCommissionRatio(String ratioIds,String userCode){
		Map<String, Object> resultMap = new HashMap<>();
		try {
			String[] ratioIdArray = ratioIds.split(",");
			StringBuffer msg = new StringBuffer();
			msg.append("注意：");
			Map<String ,Object> params = new HashMap<>();
			boolean eff = false;
			for (String ratioId : ratioIdArray) {
				params.put("commissionratioid",ratioId);
				if (insbCommissionService.existRate(params)) {
					eff = true;
					msg.append("ratioId=");
					msg.append(ratioId);
					msg.append(";");
				}else{
					delCommissionRatio(ratioId);
				}
			}
			resultMap.put("code", "0");
			resultMap.put("msg", (eff?msg.append(" 佣金系数配置已生成过佣金没有删除，其他配置删除完成!").toString():"删除成功！"));
		}catch (Exception e){
			resultMap.put("code", "1");
			resultMap.put("msg", "删除异常！");
			LogUtil.info("删除异常！" + e.getMessage());
		}
		return JsonUtils.serialize(resultMap);
	}

}