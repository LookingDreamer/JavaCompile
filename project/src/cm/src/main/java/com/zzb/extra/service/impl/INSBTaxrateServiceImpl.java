package com.zzb.extra.service.impl;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.runqian.report4.model.expression.function.datetime.DaysInYear;
import com.zzb.conf.dao.INSBChannelDao;
import com.zzb.extra.dao.INSBTaxrateDao;
import com.zzb.extra.entity.INSBChnTaxrate;
import com.zzb.extra.service.INSBTaxrateService;
import com.zzb.extra.util.ParamUtils;

@Service
@Transactional
public class INSBTaxrateServiceImpl extends BaseServiceImpl<INSBChnTaxrate> implements
		INSBTaxrateService {
	@Resource
	private INSBTaxrateDao insbTaxrateDao;

	@Resource
	private INSBChannelDao insbChannelDao;
	
	@Override
	protected BaseDao<INSBChnTaxrate> getBaseDao() {
		return insbTaxrateDao;
	}
	/**
	 * 此方法被废弃调了
	 */
	@Deprecated
	@Override
	public Map<String, Object> getTaxrateList(Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rows", insbTaxrateDao.queryTaxrateList(map));
		result.put("total", insbTaxrateDao.queryTaxrate(map));
		return result;
	}
	/**
	 * 点击编辑，显示 税率设置界面，注意：应用渠道只显示该渠道，并且不允许修改
	 * 待审核状态下的税率，可以修改生效时间，失效时间，税率
	 * 已启用状态下的税率，可以修改失效时间
	 * 关闭状态下的税率，都不允许修改
	 */
	@Override
	public String saveOrUpdateTaxrate(INSBChnTaxrate taxrate, String operator, String channelids) {
		// TODO Auto-generated method stub
		if(StringUtil.isEmpty(taxrate.getId()) && StringUtil.isNotEmpty(channelids)) {
			String[] channelidsArray = channelids.split(",");
			for(String channelid : channelidsArray) {
				taxrate.setId(UUIDUtils.random());
//				taxrate.setCreatetime(new Date());
				taxrate.setOperator(operator);
				taxrate.setChannelid(channelid);
				if(StringUtil.isEmpty(taxrate.getStatus())) taxrate.setStatus("3");
				String effectivetimeStr = this.getDateStr(taxrate.getEffectivetime());
				String createtimeStr = this.getDateStr(taxrate.getCreatetime());
				if(StringUtil.isNotEmpty(createtimeStr) && StringUtil.isNotEmpty(effectivetimeStr) && effectivetimeStr.equals(createtimeStr))
					taxrate.setEffectivetime(taxrate.getCreatetime());
				insbTaxrateDao.insert(taxrate);
			}
			
		}else {
			if("2".equals(taxrate.getStatus())) {
//				关闭状态
				taxrate.setModifytime(new Date());
				taxrate.setOperator(operator);
				insbTaxrateDao.updateById(taxrate);
			}else if ("3".equals(taxrate.getStatus())) {
//				审核状态
				taxrate.setModifytime(new Date());
				taxrate.setOperator(operator);
				insbTaxrateDao.updateById(taxrate);
			}else {
//				开启状态
				INSBChnTaxrate usingTaxrate = insbTaxrateDao.queryTaxrateByChannelid(taxrate.getChannelid());
//				如果有其他记录开启着，则设置关闭，并赋予失效时间
				if(usingTaxrate != null && usingTaxrate.getStatus().equals("1")) {
					usingTaxrate.setTerminaltime(new Date());
					usingTaxrate.setStatus("2");
					usingTaxrate.setModifytime(new Date());
					insbTaxrateDao.updateStatusById(BeanUtils.toMap(usingTaxrate));
				}
				taxrate.setModifytime(new Date());
				taxrate.setOperator(operator);
				insbTaxrateDao.updateById(taxrate);
			}
		}
		return ParamUtils.resultMap(true, "操作成功");
	}
	/**
	 * 批量删除税率
	 */
	@Override
	public String batchDeleteOrDeleteTaxrate(String taxrateIds) {
		// TODO Auto-generated method stub
		List<String> taxids = JSONArray.parseArray(taxrateIds,String.class);
		for(String id : taxids) {
			INSBChnTaxrate taxrate = insbTaxrateDao.selectById(id);
			if(taxrate.getStatus().equals("1")) {
				return ParamUtils.resultMap(false, "操作失败");
			}
			insbTaxrateDao.deleteById(id);
		}
		return ParamUtils.resultMap(true, "操作成功");
	}
	/**
	 * 查询税率列表
	 */
	@Override
	public Map<String, Object> queryTaxrateList(Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();
		String channelid = map.getOrDefault("channelid", "").toString();
		if (!"".equals(channelid))
			insbTaxrateDao.cleanTaxrateStatus(channelid);
		result.put("rows", insbTaxrateDao.queryRateLsitVo(map));
		result.put("total", insbTaxrateDao.queryRateLsitVoCount(map));
		return result;
	}
	/**
	 * 通过id更新税率状态
	 */
	@Override
	public String updateStatusByIds(String operator, INSBChnTaxrate taxrate) {
		String result = null;
		String status = taxrate.getStatus();
		if(status != null && status.equals("1")) {
			result = this.updateUsingStatus(operator, taxrate); 
		}else {
			result = this.updateCloseStatus(operator, taxrate);
		}
		return result;
	}
	/**
	 * 更新状态
	 * @param operator
	 * @param status
	 * @param ids
	 * @return
	 */
	private String updateUsingStatus(String operator,INSBChnTaxrate taxrate) {
		
		Date currtime = new Date();
		INSBChnTaxrate taxrateTem = insbTaxrateDao.selectById(taxrate.getId());
//			如果有其他记录开启着，则设置关闭，并赋予失效时间
		INSBChnTaxrate usingTaxrate = insbTaxrateDao.queryTaxrateByChannelid(taxrate.getChannelid());
		if(usingTaxrate != null && usingTaxrate.getStatus().equals("1")) {
			usingTaxrate.setTerminaltime(new Date());
			usingTaxrate.setStatus("2");
			usingTaxrate.setModifytime(new Date());
			insbTaxrateDao.updateStatusById(BeanUtils.toMap(usingTaxrate));
		}
		if(taxrate == null || !taxrateTem.getStatus().equals("3")) ParamUtils.resultMap(true, "操作失败");
		Date terminaltime = taxrateTem.getTerminaltime();
		Date effectivetime = taxrateTem.getEffectivetime();
		if(terminaltime != null && terminaltime.getTime() < currtime.getTime()) {
			return ParamUtils.resultMap(false, "弹窗提醒：失效时间设置有误，请重新核对！");
		}else if (effectivetime.getTime() < currtime.getTime()) {
			taxrateTem.setEffectivetime(currtime);
		}
		taxrateTem.setModifytime(new Date());
		taxrateTem.setOperator(operator);
		taxrateTem.setStatus(taxrate.getStatus());
		insbTaxrateDao.updateById(taxrateTem);
		
		return ParamUtils.resultMap(true, "操作成功");
	}
	/**
	 * 更新关闭状态
	 * @param operator
	 * @param status
	 * @param ids
	 * @return
	 */
	private String updateCloseStatus(String operator,INSBChnTaxrate taxrate) {
		
		INSBChnTaxrate taxrateTemp = insbTaxrateDao.selectById(taxrate.getId());
		Date terminaltime = taxrate.getTerminaltime();
		if(taxrateTemp == null || !taxrateTemp.getStatus().equals("1")) return ParamUtils.resultMap(true, "操作失败");
		if(terminaltime != null) {
			taxrateTemp.setTerminaltime(terminaltime);
		}else {
			taxrateTemp.setStatus(taxrate.getStatus());
			taxrateTemp.setTerminaltime(new Date());
		}
		taxrateTemp.setModifytime(new Date());
		taxrateTemp.setOperator(operator);
		
		insbTaxrateDao.updateById(taxrateTemp);
		return ParamUtils.resultMap(true, "操作成功");
	}
	/**
	 * 批量复制税率
	 */
	@Override
	public String batchCopyTaxrate(String operator, String rateIds, String channelIds, Date currtime) {
		// TODO Auto-generated method stub
		String[] rateIdsList = rateIds.split(",");
		String[] channelIdsList = channelIds.split(",");
		for(String rateId : rateIdsList) {
			INSBChnTaxrate taxrate = insbTaxrateDao.selectById(rateId);
			if(taxrate != null) {
				for(String channelid : channelIdsList) {
					taxrate.setId(UUIDUtils.random());
					taxrate.setChannelid(channelid);
					taxrate.setEffectivetime(this.getNextday());
					taxrate.setOperator(operator);
					taxrate.setModifytime(null);
					taxrate.setCreatetime(currtime);
					taxrate.setStatus("3");
					taxrate.setNoti(null);
					taxrate.setTerminaltime(null);
					insbTaxrateDao.insert(taxrate);
				}
			}
		}
		return ParamUtils.resultMap(true, "操作成功");
	}
	/**
	 * 获取下一天的零点
	 * @return
	 */
	private Date getNextday() {
		//生效时间默认为下一天0点
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());   //设置当前日期
		calendar.add(Calendar.DATE, 1); //日期加1天
		Date effectivetime =calendar.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(effectivetime);
		ParsePosition parsePosition = new ParsePosition(0);
		return formatter.parse(dateString,parsePosition);
	}
	private String getDateStr(Date date) {
		//生效时间默认为下一天0点
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());   //设置当前日期
		calendar.add(Calendar.DATE, 1); //日期加1天
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(date);
		return dateString;
	}


	
}