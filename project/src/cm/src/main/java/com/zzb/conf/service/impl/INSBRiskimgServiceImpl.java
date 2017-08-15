package com.zzb.conf.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.system.dao.impl.INSBServiceUtil;
import com.cninsure.system.entity.INSCCode;
import com.zzb.conf.dao.INSBRiskimgDao;
import com.zzb.conf.entity.INSBRiskimg;
import com.zzb.conf.service.INSBRiskimgService;

@Service
@Transactional
public class INSBRiskimgServiceImpl extends BaseServiceImpl<INSBRiskimg> implements
		INSBRiskimgService {
	@Resource
	private INSBRiskimgDao insbRiskimgDao;
	@Resource
	private INSBServiceUtil serviceUtil;

	@Override
	protected BaseDao<INSBRiskimg> getBaseDao() {
		return insbRiskimgDao;
	}

	@Override
	public Long queryCountVo(INSBRiskimg riskimg) {
		return insbRiskimgDao.queryCountVo(riskimg);
	}

	@Override
	public List<INSBRiskimg> queryListVo(INSBRiskimg riskimg) {
		return insbRiskimgDao.queryListVo(riskimg);
	}

	@Override
	public void deleteByRiskId(String riskid) {
		insbRiskimgDao.deleteByRiskId(riskid);
	}

	@Override
	public List<INSCCode> getDefaultRiskImg() {
		return insbRiskimgDao.getDefaultRiskImg();
	}

	@Override
	public List<INSBRiskimg> queryListByVopage(Map<String, Object> initMap) {
		return insbRiskimgDao.queryListByVopage(initMap);
	}

	@Override
	public List<INSBRiskimg> selectRiskimgByRiskid(String riskid) {
		return insbRiskimgDao.selectRiskimgByRiskid(riskid);
	}

	@Override
	public void saveOrUpdate(INSBRiskimg riskimg, String riskimgtypes) {
		//拆分字符串
		List<String> imgTypeList = null;
		if(riskimgtypes != null && !"".equals(riskimgtypes)){
			imgTypeList = Arrays.asList(riskimgtypes.split(","));
		}else{
			throw new RuntimeException("险种影像类型列表为空！");
		}
		//查詢數據庫現有的影響配置信息
		List<INSBRiskimg> riskimgList = insbRiskimgDao.selectRiskimgByRiskid(riskimg.getRiskid());
		//循環根據比較判斷刪除多餘的配置，添加新的配置
		if(imgTypeList.size()>0){
			for (int i = 0; i < riskimgList.size(); i++) {
				for (int j = 0; j < imgTypeList.size(); j++) {
					if(imgTypeList.get(j).split("_").length>1){
						if(riskimgList.get(i).getRiskimgtype().equals(imgTypeList.get(j).split("_")[0])){
							break;
						}else if(j == (imgTypeList.size()-1)){//需要删除的配置信息
							LogUtil.info("删除供应商影像配置"+"--Riskid="+riskimg.getRiskid()+
									"--影像类型="+riskimgList.get(i).getRiskimgtype()+
									"--影像名称="+riskimgList.get(i).getRiskimgname());
							insbRiskimgDao.deleteById(riskimgList.get(i).getId());
						}
					}
				}
			}
		}else{
			for (int i = 0; i < riskimgList.size(); i++) {
				insbRiskimgDao.deleteById(riskimgList.get(i).getId());
			}
		}
		INSBRiskimg temp = null;
		if(riskimgList.size()>0){
			for (int i = 0; i < imgTypeList.size(); i++) {
				if(imgTypeList.get(i).split("_").length>1){
					for (int j = 0; j < riskimgList.size(); j++) {
						if(riskimgList.get(j).getRiskimgtype().equals(imgTypeList.get(i).split("_")[0])){
							break;
						}else if(j == (riskimgList.size()-1)){//需要添加的配置信息
							temp = new INSBRiskimg();
							temp.setCreatetime(riskimg.getModifytime());
							temp.setRiskid(riskimg.getRiskid());
							temp.setRiskimgtype(imgTypeList.get(i).split("_")[0]);
							temp.setRiskimgname(imgTypeList.get(i).split("_")[1]);
							temp.setIsusing(riskimg.getIsusing());
							LogUtil.info("添加供应商影像配置"+"--Riskid="+riskimg.getRiskid()+
									"--影像类型="+temp.getRiskimgtype()+"--影像名称="+temp.getRiskimgname());
							insbRiskimgDao.insert(temp);
						}
					}
				}
			}
		}else{
			for (int i = 0; i < imgTypeList.size(); i++) {
				if(imgTypeList.get(i).split("_").length>1){
					temp = new INSBRiskimg();
					temp.setCreatetime(riskimg.getModifytime());
					temp.setRiskid(riskimg.getRiskid());
					temp.setRiskimgtype(imgTypeList.get(i).split("_")[0]);
					temp.setRiskimgname(imgTypeList.get(i).split("_")[1]);
					temp.setIsusing(riskimg.getIsusing());
					LogUtil.info("添加供应商影像配置"+"--Riskid="+riskimg.getRiskid()+
							"--影像类型="+temp.getRiskimgtype()+"--影像名称="+temp.getRiskimgname());
					insbRiskimgDao.insert(temp);
				}
			}
		}
	}

	@Override
	public List<INSCCode> selectNotSelectedRiskimgtypeByRiskid(String riskid) {
		return insbRiskimgDao.selectNotSelectedRiskimgtypeByRiskid(riskid);
	}
}