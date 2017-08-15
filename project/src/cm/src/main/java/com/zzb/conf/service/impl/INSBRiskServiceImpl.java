package com.zzb.conf.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.system.entity.INSCUser;
import com.zzb.conf.dao.INSBRiskDao;
import com.zzb.conf.dao.INSBRiskkindDao;
import com.zzb.conf.dao.INSBRiskkindconfigDao;
import com.zzb.conf.entity.INSBRisk;
import com.zzb.conf.entity.INSBRiskkind;
import com.zzb.conf.entity.INSBRiskkindconfig;
import com.zzb.conf.service.INSBRiskService;

@Service
@Transactional
public class INSBRiskServiceImpl extends BaseServiceImpl<INSBRisk> implements
		INSBRiskService {
	@Resource
	private INSBRiskDao insbRiskDao;
	@Resource
	private INSBRiskkindDao riskkindDao;

	@Resource
	private INSBRiskkindconfigDao riskkindconfigDao;

	@Override
	protected BaseDao<INSBRisk> getBaseDao() {
		return insbRiskDao;
	}

	@Override
	public List<INSBRisk> queryListByVo(INSBRisk insbRisk) {
		return insbRiskDao.queryByRiskVo(insbRisk);
	}
	@Override
	public Map<String, Object>  queryListByVopage(Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<INSBRisk> riskList = insbRiskDao.queryByRiskVopage(map);
		result.put("total", insbRiskDao.queryByRiskVoCount(map));
		result.put("rows", riskList);
		return result;
	}

	@Override
	public Map<String, Object> queryKindListByVopage(Map<String, Object> map) {
		Map<String, Object> kindResult = new HashMap<String, Object>();
		List<INSBRiskkind> kindList = riskkindDao.queryByRiskkindVopage(map);
		kindResult.put("total", riskkindDao.queryListByVoCount(map));
		kindResult.put("rows", kindList);
		return kindResult;
	}

	@Override
//	public Object queryListByVoCount(INSBRisk risk) {
//		return insbRiskDao.queryByRiskVoCount(risk);
//	}
	public Object queryListByVoCount(Map<String, Object> map) {
		return insbRiskDao.queryByRiskVoCount(map);
	}
	@Override
	public List<INSBRisk> selectByModifyDate(String modifydate) {
		return insbRiskDao.selectByModifyDate(modifydate);
	}

	@Override
	public void updateRiskKindConfig(INSCUser operator, INSBRiskkind riskkind) {
		if (riskkind != null && riskkind.getKindtype() != null) {
			INSBRiskkindconfig model = riskkindconfigDao
					.selectByDatamp(riskkind.getKindtype());
			if (model == null) {
				INSBRiskkindconfig tempModel = new INSBRiskkindconfig();
				tempModel.setCreatetime(new Date());
				tempModel.setOperator(operator.getUsercode());
				tempModel.setRiskkindname(riskkind.getKindname());
				tempModel.setRiskkindcode(riskkind.getKindcode());
				tempModel.setPrekindcode(riskkind.getPreriskkind());
				// tempModel.setIsdeductible(riskkind.getis);
				tempModel.setDatatemp(riskkind.getKindtype());

				riskkindconfigDao.insert(tempModel);
			}
		}

	}

	@Override
	public int initData(String riskid) {
		List<INSBRiskkindconfig> riskkinds = riskkindconfigDao.selectNotAll();
		List<INSBRiskkind> riskKinds = new ArrayList<INSBRiskkind>();
		for (INSBRiskkindconfig risk : riskkinds) {
			
			//做一次查询如果 数据库中存在终止插入操作
			Map<String,String> map = new HashMap<String,String>();
			map.put("kindcode", risk.getRiskkindcode());
			map.put("riskid", riskid);
			long count= riskkindDao.selectCOuntByKindCode(map);
			if(count>0){
				break;
			}
			
//			INSBRiskkind oldKind = riskkindDao.
//			if(oldKind!=null){
//				break;
//			}
			
			INSBRiskkind kind = new INSBRiskkind();
			
			kind.setAmountselect(risk.getDatatemp());
			kind.setPreriskkind(risk.getPrekindcode());
			kind.setCreatetime(new Date());
			kind.setRiskid(riskid);
			kind.setKindcode(risk.getRiskkindcode());
			kind.setKindname(risk.getRiskkindname());
			if(risk.getType().equals("0")){
				kind.setKindtype("0");//0 商业  1 不及免赔 2 交强 3 车船税
			}else if(risk.getType().equals("1")){
				kind.setKindtype("1");//0 商业  1 不及免赔 2 交强 3 车船税
			}else if(risk.getType().equals("2")){
				kind.setKindtype("2");//0 商业  1 不及免赔 2 交强 3 车船税
			}else{
				kind.setKindtype("3");//0 商业  1 不及免赔 2 交强 3 车船税
			}
			if("1".equals(risk.getIsdeductible())){
					kind.setNotdeductible("1");;//1 有不计免赔 0无
			}else {
				kind.setNotdeductible("0");;//1 有不计免赔 0无
			}
			kind.setIsamount(risk.getOperator());//是否保额选项  1是  0否
			kind.setNotdeductible(risk.getIsdeductible());//是否不计免赔 1是 2 不是
			kind.setIsusing("1");
			
			riskKinds.add(kind);
			
		}
		if(!riskKinds.isEmpty()){
			riskkindDao.insertInBatch(riskKinds);
			return 1;
		}else{
			return 0;
		}
	}
	
	@Override
	public void deleteRiskByIds(List<String> riskIds) {
		insbRiskDao.deleteByIdInBatch(riskIds);
	}

	@Override
	public List<String> getPreRiskKindName(String riskkindId) {
		List<String> result = new ArrayList<String>();
		INSBRiskkind kindModel =  riskkindDao.selectById(riskkindId);
		//查所有的前置险别
		if(kindModel==null||kindModel.getPreriskkind()==null||kindModel.getPreriskkind()==""){
			List<String> prekindList = riskkindDao.selectAllPreKind();
			final int size =  prekindList.size();
			String[] arr = (String[])prekindList.toArray(new String[size]);
			for(int i = 0; i < arr.length; i++){
				StringBuffer kindName = new StringBuffer();
				System.out.println(arr[i]);				
				String[] preArray = arr[i].split(",");
				for(String str:preArray){
					String singelkindName = riskkindDao.selectKindNameByCode(str);
					kindName.append(singelkindName+",");
				}
				result.add(kindName.toString().substring(0,kindName.length()-1));
			}
		}else{//查单个险别的前置险别
			String preStr = kindModel.getPreriskkind();			
			String[] preArray = preStr.split(",");
			for(String str:preArray){
				String kindName = riskkindDao.selectKindNameByCode(str);
				result.add(kindName);
			}
		}
		return result;
	}

	@Override
	public String selectKindNameByCode(String code) {
		String prekindname = riskkindDao.selectKindNameByCode(code);
		return prekindname;
	}

	@Override
	public List<INSBRisk> queryAll() {
		// TODO Auto-generated method stub
		return insbRiskDao.selectAll();
	}

}