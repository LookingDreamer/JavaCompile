package com.zzb.conf.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zzb.conf.dao.INSBAgreementDao;
import com.zzb.conf.entity.INSBAgreement;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCDept;
import com.zzb.conf.dao.INSBPaychannelDao;
import com.zzb.conf.dao.INSBPaychannelmanagerDao;
import com.zzb.conf.dao.INSBProviderDao;
import com.zzb.conf.entity.INSBPaychannel;
import com.zzb.conf.entity.INSBPaychannelmanager;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.service.INSBPaychannelService;

@Service
@Transactional
public class INSBPaychannelServiceImpl extends BaseServiceImpl<INSBPaychannel> implements
		INSBPaychannelService {
	@Resource
	private INSBPaychannelDao insbPaychannelDao;
	@Resource
	private INSBPaychannelmanagerDao insbPaychannelmanagerDao;
	@Resource
	private INSCDeptDao inscDeptDao;
	@Resource
	private INSBProviderDao insbProviderDao;

	@Resource
	private INSBAgreementDao iNSBAgreementDao;

	@Override
	protected BaseDao<INSBPaychannel> getBaseDao() {
		return insbPaychannelDao;
	}

	@Override
	public Map<String, Object> initPayChannelList(Map<String, Object> data) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<Object, Object>> infoList = insbPaychannelDao.selectPayChannelListPaging(data);
		map.put("total", insbPaychannelDao.selectCount());
		map.put("rows", infoList);
		return map;
	}
	
	@Override
	public Map<String, Object> initPayWayList(Map<String, Object> data , String paychannelid) {
		Map<String, Object> resultmap = new HashMap<String, Object>();
		/*INSBPaychannelmanager param = new INSBPaychannelmanager();
		param.setPaychannelid(id);*/
		
		List<Map<Object, Object>> infoList = new ArrayList<Map<Object, Object>>();
//		List<INSBPaychannelmanager> paywayList = insbPaychannelmanagerDao.selectList(param);
		List<INSBPaychannelmanager> paywayList = (List<INSBPaychannelmanager>) insbPaychannelmanagerDao.selectPagePaywayList(data);
		INSBPaychannelmanager pycount = new INSBPaychannelmanager();
		pycount.setPaychannelid(String.valueOf((data.get("paychannelid"))));
		pycount.setUserDept((String) data.get("userDept"));
		Long pyListCount = insbPaychannelmanagerDao.selectCount(pycount);
		for(INSBPaychannelmanager paymge : paywayList){
			/*if(paymge.getDeptid()!=null || !StringUtil.isEmpty(paymge.getDeptid())){*/
				Map<Object, Object> parammap = new HashMap<Object, Object>();
				INSBPaychannel paychannel = insbPaychannelDao.selectById(paymge.getPaychannelid());
				if(paymge.getDeptid()!=null || !StringUtil.isEmpty(paymge.getDeptid())){
					INSCDept dept = inscDeptDao.selectById(paymge.getDeptid());
					if(dept!=null && !"".equals(dept)){
						parammap.put("deptname", dept.getShortname());
					}
				}
				if(paymge.getProviderid()!=null || !StringUtil.isEmpty(paymge.getProviderid())){
					INSBProvider insbprovider = insbProviderDao.selectById(paymge.getProviderid());
					if(insbprovider != null && !"".equals(insbprovider)){
						parammap.put("providename", insbprovider.getPrvshotname());
					}
				}
				//协议名称
				if (StringUtil.isNotEmpty(paymge.getAgreementid())) {
					INSBAgreement iNSBAgreement = iNSBAgreementDao.selectById(paymge.getAgreementid());
					if (iNSBAgreement != null) {
						parammap.put("agreementname", iNSBAgreement.getAgreementname() == null ? "" : iNSBAgreement.getAgreementname());

					}
				}
				parammap.put("id", paymge.getId());
				parammap.put("paychannelid", paymge.getPaychannelid());
				parammap.put("paychannelname", paychannel.getPaychannelname());
				infoList.add(parammap);
			/*}*/
		}
//		int count=infoList.size();
		resultmap.put("total", pyListCount);
		resultmap.put("rows", infoList);
		
		return resultmap;
	}

}