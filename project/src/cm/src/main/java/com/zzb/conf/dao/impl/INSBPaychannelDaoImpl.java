package com.zzb.conf.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCDept;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBPaychannelDao;
import com.zzb.conf.entity.INSBPaychannel;

import javax.annotation.Resource;

@Repository
public class INSBPaychannelDaoImpl extends BaseDaoImpl<INSBPaychannel> implements
		INSBPaychannelDao {

	@Resource
	private INSCDeptDao deptDao;

	@Override
	public List<Map<Object, Object>> selectPayChannelListPaging(
			Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectpaychannel"),map);
	}

	@Override
	public List<Map<String, Object>> selectPayChannelList() {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectPayChannelList"));
	}

	@Override
	public List<INSBPaychannel> selectPayChannelListByAgreementId(String Depcode,String prvid) {
		String deptId = getPingTaiDeptId(Depcode);
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("ptdeptcode", deptId);
		map.put("deptcode", Depcode);
		map.put("providerid", prvid);
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectPayChannelListByAgreementId"),map);
	}
	private String getPingTaiDeptId(String deptId){
		String result="";
		INSCDept deptModel = deptDao.selectById(deptId);
		if(deptModel==null){
			return result;
		}

		String tempInnerCode = deptModel.getDeptinnercode();
		if(StringUtils.isEmpty(tempInnerCode)){
			return result;
		}
		if(tempInnerCode.length()==5){
			result = deptId;
		}else if(tempInnerCode.length()<5){
			result = deptId;
		}else if(tempInnerCode.length()>5){
			String pingTaiInnerCode = tempInnerCode.substring(0, 5);
			result =  deptDao.seleDeptIdByInnerCode(pingTaiInnerCode);
		}
		return result;
	}
	
}