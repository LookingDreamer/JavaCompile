package com.zzb.chn.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.common.ModelUtil;
import com.zzb.chn.bean.AgreementAreaBean;
import com.zzb.chn.bean.CityBean;
import com.zzb.chn.bean.QuoteBean;
import com.zzb.chn.dao.INSBChncarqryDao;
import com.zzb.chn.dao.INSBChncarqrycountDao;
import com.zzb.chn.entity.INSBChncarqry;
import com.zzb.chn.entity.INSBChncarqrycount;
import com.zzb.chn.service.CHNMerchantService;
import com.zzb.chn.util.JsonUtils;
import com.zzb.chn.util.StatusCodeMapperUtil;
import com.zzb.conf.dao.INSBAgreementareaDao;
import com.zzb.conf.dao.INSBAgreementinterfaceDao;
import com.zzb.conf.dao.INSBChannelDao;
import com.zzb.conf.dao.INSBChannelagreementDao;
import com.zzb.conf.dao.INSBRegionDao;
import com.zzb.conf.entity.INSBAgreementinterface;
import com.zzb.extra.entity.INSBCommission;
import com.zzb.mobile.model.CommonModel;

@Service
@Transactional
public class CHNMerchantServiceImpl implements CHNMerchantService {
	@Resource
	private INSBChannelagreementDao insbChannelagreementDao; 
	@Resource
    private INSBChncarqryDao insbChncarqryDao;
	@Resource
    private INSBChncarqrycountDao insbChncarqrycountDao;
	@Resource
    private INSBAgreementinterfaceDao insbAgreementinterfaceDao;
	@Resource
	private INSBAgreementareaDao insbAgreementareaDao ;
	@Resource
	private INSBRegionDao insbRegionDao;
	@Resource
	private INSBChannelDao insbChannelDao;

	@Override
	public CommonModel getProviders(QuoteBean quoteBeanIn) throws Exception {
		CommonModel resultOut = new CommonModel();
		resultOut.setStatus(CommonModel.STATUS_SUCCESS);
		String chnId = quoteBeanIn.getChannelId();
		String city = quoteBeanIn.getInsureAreaCode();
		if (StringUtil.isEmpty(chnId) || StringUtil.isEmpty(city)) {
			resultOut.setStatus(CommonModel.STATUS_FAIL);
			resultOut.setMessage("渠道ID和地区编码必传");
			return resultOut;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("channelinnercode", chnId);
		map.put("city", city);
		
		List<Map<String, Object>> channelagreements = insbChannelagreementDao.getChannelAgreementProviderMerchant(map);	
		List<Map<String, Object>> providers = new ArrayList<Map<String, Object>>();
		for(Map<String, Object> channelagreement : channelagreements){
			Map<String, Object> itPrv = new HashMap<String, Object>();
			String agreementid = (String)channelagreement.get("agreementid");
			String deptid5 = null; //(String)channelagreement.get("deptid5");
			String providerid = (String)channelagreement.get("providerid");
			
			Map<String, Object> pcnMap = insbChannelagreementDao.getPayChannelNames(agreementid, deptid5, providerid);
			if ( pcnMap == null ) {
				itPrv.put("paychannelnames", null);
			} else {
				itPrv.put("paychannelnames", pcnMap.get("paychannelnames"));
			}
			
			itPrv.put("agreementname", (String)channelagreement.get("agreementname"));
			itPrv.put("agreementcode", (String)channelagreement.get("agreementcode"));
			itPrv.put("prvshotname", (String)channelagreement.get("prvshotname"));
			itPrv.put("topPrvName", (String)channelagreement.get("topPrvName"));
			providers.add(itPrv);
		}
		
		resultOut.setBody(providers);
		return resultOut;
	}

	@Override
	public CommonModel getInterCallDetail(Map<String, Object> mapIn) throws Exception {
		CommonModel resultOut = new CommonModel();
		resultOut.setStatus(CommonModel.STATUS_SUCCESS);
		
		String channelinnercode = (String)mapIn.get("channelinnercode");
		String createtimestart = (String)mapIn.get("createtimestart");
		mapIn.put("createtimestart", createtimestart + " 00:00:00");
		String createtimeend = (String)mapIn.get("createtimeend");
		mapIn.put("createtimeend", createtimeend + " 23:59:59");
		//String carlicenseno = (String)mapIn.get("carlicenseno");
		//String carowner = (String)mapIn.get("carowner");
		//mapIn.put("limit", mapIn.get("limit"));
		//mapIn.put("offset", mapIn.get("offset"));
		
		if (StringUtil.isEmpty(channelinnercode)) {
			resultOut.setStatus(CommonModel.STATUS_FAIL);
			resultOut.setMessage("渠道ID必传");
			return resultOut;
		}
		
		INSBAgreementinterface interfaceIn = new INSBAgreementinterface();
		interfaceIn.setChannelinnercode(channelinnercode);
		interfaceIn.setInterfaceid("10");
		INSBAgreementinterface insbAgreementinterface = insbAgreementinterfaceDao.selectOne(interfaceIn);
		int freeTimes = 0;
		if (insbAgreementinterface != null && insbAgreementinterface.getMonthfree() != null) {
			freeTimes = insbAgreementinterface.getMonthfree();
		}
		
		List<Map<String, Object>> interCallDetails = insbChncarqryDao.queryDetail(mapIn);
		long totalSize = insbChncarqryDao.queryDetailSize(mapIn);
		
		for (Map<String, Object> interCallDetail : interCallDetails) {			
			String createtime = (String)interCallDetail.get("createtime");
			String carlicenseno = (String)interCallDetail.get("carlicenseno");
			String id = (String)interCallDetail.get("id");
			
			Map<String, Object> map = new HashMap<String, Object>();
	        map.put("channelinnercode", channelinnercode);
	        map.put("carlicenseno", carlicenseno);
	        map.put("betweentime", createtime);	        
			List<INSBChncarqrycount> countDatas = insbChncarqrycountDao.selectCountByCidAndDay(map);
			INSBChncarqrycount countData = countDatas.get(0);
			if (!countData.getId().equals(id)) { //同一车牌一个自然年之内重复的车牌查询免费，只有第一次才收费
				interCallDetail.put("isCharge", "否");
			} else {
				map = new HashMap<String, Object>();
				map.put("channelinnercode", channelinnercode);
				map.put("createtimeend", countData.getCreatetime());	
				long recSize = insbChncarqrycountDao.countData(map);
				
				if (recSize > freeTimes) {
					interCallDetail.put("isCharge", "是");
				} else {
					interCallDetail.put("isCharge", "否");
				}
			}	
		}
						
		Map<String, Object> reultMap = new HashMap<String, Object>();
		reultMap.put("total", totalSize);
		reultMap.put("rows", interCallDetails);
		
		resultOut.setBody(reultMap);
		return resultOut;
	}
    
	@Override
	public CommonModel getInterCallDayDetail(Map<String, Object> mapIn) throws Exception {
		CommonModel resultOut = new CommonModel();
		resultOut.setStatus(CommonModel.STATUS_SUCCESS);
		
		String channelinnercode = (String)mapIn.get("channelinnercode");
		String createtimestart = (String)mapIn.get("createtimestart");
		mapIn.put("createtimestart", createtimestart + " 00:00:00");
		String createtimeend = (String)mapIn.get("createtimeend");
		mapIn.put("createtimeend", createtimeend + " 23:59:59");
		
		if (StringUtil.isEmpty(channelinnercode)) {
			resultOut.setStatus(CommonModel.STATUS_FAIL);
			resultOut.setMessage("渠道ID必传");
			return resultOut;
		}
		
		INSBAgreementinterface interfaceIn = new INSBAgreementinterface();
		interfaceIn.setChannelinnercode(channelinnercode);
		interfaceIn.setInterfaceid("10");
		INSBAgreementinterface insbAgreementinterface = insbAgreementinterfaceDao.selectOne(interfaceIn);
		int freeTimescg = 0;
		if (insbAgreementinterface != null && insbAgreementinterface.getMonthfree() != null) {
			freeTimescg = insbAgreementinterface.getMonthfree();
		}
		
		mapIn.put("strformat", "%Y-%m-%d");
		List<Map<String, Object>> interCallDetails = insbChncarqryDao.queryGroupRecCount(mapIn);
		long totalSize = insbChncarqryDao.queryGroupRecCountSize(mapIn);
		
		for (Map<String, Object> interCallDetail : interCallDetails) {
			String createtime = (String)interCallDetail.get("createtime");
			long dayreccount = (long)interCallDetail.get("reccount");
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("channelinnercode", channelinnercode); 
			map.put("createtimeendex", createtime + " 00:00:00");	
			long forwardCount = insbChncarqrycountDao.countData(map);
			map.put("createtimeend", createtime + " 23:59:59");
			long backCount = insbChncarqrycountDao.countData(map);
			long dayCount = backCount - forwardCount;
			
			if (backCount <= freeTimescg) {
				interCallDetail.put("chargetimes", 0);
				interCallDetail.put("freetimes", dayreccount);
			} else {
				if (freeTimescg <= forwardCount) {
					interCallDetail.put("chargetimes", dayCount);
					interCallDetail.put("freetimes", dayreccount - dayCount);
				} else {
					long chargetimes = backCount - freeTimescg;
					interCallDetail.put("chargetimes", chargetimes);
					interCallDetail.put("freetimes", dayreccount - chargetimes);
				}
			}
						
		}
		
		Map<String, Object> reultMap = new HashMap<String, Object>();
		reultMap.put("total", totalSize);
		reultMap.put("rows", interCallDetails);
		
		resultOut.setBody(reultMap);
		return resultOut;
	}
	
	@Override
	public CommonModel querySummaryCount(Map<String, Object> mapIn) throws Exception {
		CommonModel resultOut = new CommonModel();
		resultOut.setStatus(CommonModel.STATUS_SUCCESS);
		
		//String channelinnercode = (String)mapIn.get("channelinnercode");
		
//		if (StringUtils.isEmpty(channelinnercode)) {
//			resultOut.setStatus(CommonModel.STATUS_FAIL);
//			resultOut.setMessage("渠道ID必传");
//			return resultOut;
//		}
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String year = sdf.format(date);
		
		List<Map<String, Object>> interCallDetails = insbChncarqryDao.querySummaryCount(mapIn);
		for (Map<String, Object> interCallDetail : interCallDetails) {
			int freetimes = 0;
			Object objFreeTimes = interCallDetail.get("freetimes");
			if (objFreeTimes != null) {
				freetimes = Integer.parseInt(objFreeTimes.toString());
			}
			
			mapIn.put("createtimestart", year + "-01-01 00:00:00");
			mapIn.put("createtimeend", year + "-12-31 23:59:59");
			mapIn.put("channelinnercode", (String)interCallDetail.get("channelinnercode"));
			long yearRecCount = 0;
			long yearChargeRecCount = 0;
			long monthRecCount = 0;
			String maxtime = "-";
			try {
				yearRecCount = insbChncarqryDao.queryDetailSize(mapIn);
				//yearChargeRecCount = insbChncarqryDao.queryClnGroupCount(mapIn);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("channelinnercode", mapIn.get("channelinnercode")); 
				//map.put("modifytimeend", year + "-12-31 23:59:59");	
				yearChargeRecCount = insbChncarqrycountDao.countData(map);
				
				Map<String, Object> maxTimeMap = insbChncarqryDao.queryMaxTimeRec(mapIn);
				String maxTimeStr = (String)maxTimeMap.get("maxtime");
				String[] maxTimeArr = maxTimeStr.split("-");
				mapIn.put("createtimestart", maxTimeArr[0] + "-" + maxTimeArr[1] + "-01 00:00:00");
				mapIn.put("createtimeend", maxTimeStr);
				monthRecCount = insbChncarqryDao.queryDetailSize(mapIn);
				maxtime = maxTimeStr.split(" ")[0];
			} catch (Exception exception) {
				if (exception.getMessage().contains("doesn't exist")) {
					LogUtil.info("渠道：" + interCallDetail.get("channelinnercode") + "没有使用过a接口");
				} else {
					throw exception;
				}
			}
			interCallDetail.put("maxtime", maxtime);
			interCallDetail.put("yearRecCount", yearRecCount);	
			interCallDetail.put("monthRecCount", monthRecCount);
			long leftFreeTimes = freetimes - yearChargeRecCount;
			if (leftFreeTimes < 0 ) {
				leftFreeTimes = 0;
			}
			interCallDetail.put("leftFreeTimes", leftFreeTimes);
			if ("关闭".equals(interCallDetail.get("isopenname"))) {
				interCallDetail.remove("yearRecCount");
				interCallDetail.remove("monthRecCount");
				interCallDetail.remove("leftFreeTimes");
			}
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("rows", interCallDetails);
		resultMap.put("total", insbChncarqryDao.queryCount(mapIn));
		resultOut.setBody(resultMap);
		return resultOut;
	}

	@Override
	public CommonModel queryAgreementArea(Map<String, Object> mapIn) throws Exception {
		CommonModel model = new CommonModel();
		if(mapIn.get("channelId") == null) {
			model.setStatus(CommonModel.STATUS_FAIL);
			model.setMessage("channelId不能为空! ");
			return model ;
		}
		String channelId = (String) mapIn.get("channelId");
		try {
			HashMap<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("channelinnercode", channelId);
			List<Map<String, Object>> areaDatas = insbAgreementareaDao.getAgreeAreaByChninnercode(tempMap);
			List<AgreementAreaBean> agreementAreas = new ArrayList<AgreementAreaBean>();

			for (Map<String, Object> areaData : areaDatas) {
				String province = (String)areaData.get("province");
				String provinceName = (String)areaData.get("provincename");
				String city = (String)areaData.get("city");
				String cityName = (String)areaData.get("cityname");

				AgreementAreaBean agreementAreaMatch = null;
				for (AgreementAreaBean agreementArea : agreementAreas) {
					if (province.equals(agreementArea.getProvince())) {
						agreementAreaMatch = agreementArea;
						break;
					}
				}

				if (agreementAreaMatch == null) {
					agreementAreaMatch = new AgreementAreaBean();
					agreementAreaMatch.setProvince(province);
					agreementAreaMatch.setProvinceName(provinceName);

					List<CityBean> cityBeans = new ArrayList<CityBean>();
					CityBean cityBean = new CityBean();
					cityBean.setCity(city);
					cityBean.setCityName(cityName);
					cityBeans.add(cityBean);

					agreementAreaMatch.setCitys(cityBeans);
					agreementAreas.add(agreementAreaMatch);
				} else {
					List<CityBean> cityBeans = agreementAreaMatch.getCitys();

					CityBean cityBean = new CityBean();
					cityBean.setCity(city);
					cityBean.setCityName(cityName);
					cityBeans.add(cityBean);
				}
			}

			model.setStatus(CommonModel.STATUS_SUCCESS);
			if (agreementAreas.size() > 0) {
				model.setBody(agreementAreas);
			} else {
				model.setBody(new String[]{}) ;
				model.setMessage("暂没有地区支持车险业务");
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.setStatus(CommonModel.STATUS_FAIL);
			model.setMessage("获取投保地区失败！");
		}
		return model;
	}
	
	@Override
	public CommonModel getChannels(Map<String, Object> mapIn) throws Exception {
		CommonModel resultOut = new CommonModel();
		resultOut.setStatus(CommonModel.STATUS_SUCCESS);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("limit", mapIn.get("limit"));
		map.put("offset", mapIn.get("offset"));
		
		long totalSize = insbChannelDao.countForMerchant(map);
		List<Map<String, Object>> rows = insbChannelDao.selectForMerchant(map);
		
		Map<String, Object> reultMap = new HashMap<String, Object>();
		reultMap.put("total", totalSize);
		reultMap.put("rows", rows);
		
		resultOut.setBody(reultMap);
		return resultOut;
	}

	@Override
	public CommonModel getOldCarTaskList(Map<String, Object> mapIn) {
		CommonModel resultOut = new CommonModel();
		resultOut.setStatus(CommonModel.STATUS_SUCCESS);
		long totalSize = insbChannelDao.getQuoteTotalInfoCount(mapIn);
		List<Map<String, Object>> rows = insbChannelDao.getQuoteTotalInfo(mapIn);
		for(int i = 0; rows != null && i < rows.size(); i++){
			Map<String, Object> row = rows.get(i);
			String taskcode = StatusCodeMapperUtil.states.get(row.get("taskcode"));
			row.put("taskcode",taskcode);
			row.put("tasktype", StatusCodeMapperUtil.stateDescription.get(row.get("taskcode")));
//			Map<String, Object> params = new HashMap<String, Object>();
//			params.put("taskid",row.get("taskid"));
//			params.put("providercode",row.get("inscomcode"));
//			if("14".equals(taskcode)){
//				params.put("commissionFlag", "quote");
//				this.queryCommissionInfo(params,row);
//
//			}else if("14".equals(taskcode) || "23".equals(taskcode) || "25".equals(taskcode) || "33".equals(taskcode)){
//				params.put("commissionFlag","insured");
//				this.queryCommissionInfo(params,row);
//			}
		}

		Map<String, Object> reultMap = new HashMap<String, Object>();
		reultMap.put("total", totalSize);
		reultMap.put("rows", rows);

		resultOut.setBody(reultMap);
		return resultOut;
	}

	public void queryCommissionInfo(Map<String, Object> map,Map<String, Object> row){
		List< INSBCommission > commissionInfoList = insbChannelDao.queryCommissionList(map);
		for(INSBCommission insbCommission : commissionInfoList){
			Double counts = insbCommission.getCounts();
			if("01".equals(insbCommission.getCommissiontype())){
				row.put("bisCounts", counts);
			}
			if("03".equals(insbCommission.getCommissiontype())){
				row.put("comCounts", counts);
			}
		}
	}
	
	@Override
	public CommonModel buildCountData(Map<String, Object> mapIn) throws Exception {
		CommonModel resultOut = new CommonModel();
		resultOut.setStatus(CommonModel.STATUS_SUCCESS);
		String channelinnercodes = (String)mapIn.get("channelinnercodes");
		if (StringUtil.isEmpty(channelinnercodes)) {
			resultOut.setStatus(CommonModel.STATUS_FAIL);
			resultOut.setMessage("channelinnercodes必传");
			return resultOut;
		}
		
		String[] ccArray = channelinnercodes.split(";");
		for (String channelinnercode : ccArray) {
			INSBChncarqrycount carqrycountcon = new INSBChncarqrycount();
			carqrycountcon.setChannelinnercode(channelinnercode); 
			insbChncarqrycountDao.delete(carqrycountcon);
			
			INSBChncarqry insbChncarqry = new INSBChncarqry();
			insbChncarqry.setChannelinnercode(channelinnercode); 
			List<INSBChncarqry> insbChncarqrys = insbChncarqryDao.selectList(insbChncarqry);
			List<String> carnos = new ArrayList<String>();
			for (INSBChncarqry insbChncarqryIt : insbChncarqrys) {
				String itCarNo = insbChncarqryIt.getCarlicenseno();
				boolean findCarNo = false;
				for (String carno : carnos) {
					if ( carno.equalsIgnoreCase(itCarNo) ) { 
						findCarNo = true;
						break;
					}
				}
				if (!findCarNo) carnos.add(itCarNo);
			}
			LogUtil.info("channel-buildCountData-carnos:" + channelinnercode + " " + JsonUtils.serialize(carnos));
			
			for (String carno : carnos) {
				INSBChncarqry carqrycon = new INSBChncarqry();
				carqrycon.setChannelinnercode(channelinnercode); 
				carqrycon.setCarlicenseno(carno);
				List<INSBChncarqry> carqrys = insbChncarqryDao.selectList(carqrycon);
				
				INSBChncarqry carqryStart = carqrys.get(0); 
				INSBChncarqry carqryEnd = carqryStart;
				Date groupYearEndTime = getNextYearDate(carqryStart.getCreatetime());
				int callCout = 0;
				
				for (int i = 0; i < carqrys.size(); i++) {
					INSBChncarqry carqryIt = carqrys.get(i);
					if (carqryIt.getCreatetime().getTime() <= groupYearEndTime.getTime()) {
						carqryEnd = carqryIt;
						callCout++;
						if (i >= carqrys.size() - 1) {
							insertCountData(carno, carqryStart, carqryEnd, callCout, channelinnercode);
						}
					} else {
						insertCountData(carno, carqryStart, carqryEnd, callCout, channelinnercode);
						
						carqryStart = carqryIt;
						carqryEnd = carqryStart;
						groupYearEndTime = getNextYearDate(carqryStart.getCreatetime());
						callCout = 0;
						i--;
					}
				}
			}
		}
		
		resultOut.setBody(null);
		return resultOut;
	}
	
	private void insertCountData(String carno, INSBChncarqry carqryStart, INSBChncarqry carqryEnd, int callCout, String channelinnercode) {
		INSBChncarqrycount carqrycountdata = new INSBChncarqrycount();
		carqrycountdata.setId(carqryStart.getId()); 
		carqrycountdata.setCallcount(callCout);
		carqrycountdata.setCarlicenseno(carno);
		carqrycountdata.setCarowner(carqryEnd.getCarowner());
		carqrycountdata.setChannelinnercode(channelinnercode);
		carqrycountdata.setCreatetime(carqryStart.getCreatetime());
		carqrycountdata.setIdcardno(carqryEnd.getIdcardno());
		carqrycountdata.setModifytime(carqryEnd.getCreatetime());
		insbChncarqrycountDao.insert(carqrycountdata);
		LogUtil.info("channel-buildCountData-carqrycountdata:" + channelinnercode + " " + JsonUtils.serialize(carqrycountdata));
	}
	
	private Date getNextYearDate(Date oldDate) {
    	Calendar calendar = Calendar.getInstance();
		calendar.setTime(oldDate);
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
		Date newDate = calendar.getTime();
    	
    	String strNewDate = ModelUtil.conbertToString(newDate) + " 00:00:00";
    	return ModelUtil.conbertStringToDate(strNewDate);
    }
}
