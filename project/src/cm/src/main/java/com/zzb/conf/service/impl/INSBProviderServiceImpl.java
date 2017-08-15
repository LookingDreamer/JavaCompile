package com.zzb.conf.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.zzb.conf.dao.INSBProviderDao;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.service.INSBProviderService;
import com.zzb.model.ProOrParModel;

@Service
@Transactional
public class INSBProviderServiceImpl extends BaseServiceImpl<INSBProvider> implements
		INSBProviderService {
	@Resource
	private INSBProviderDao insbProviderDao;
	
	@Override
	protected BaseDao<INSBProvider> getBaseDao() {
		return insbProviderDao;
	}

	@Override
	public Map<String, Object> showProviderList(Map<String, Object> data) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<Object, Object>> infoList = insbProviderDao.selectProviderLitByMap(data);
		map.put("total", insbProviderDao.selectCount());
		map.put("rows", infoList);
	    return map;
	}

	/* 
	 * 初始化树
	 * (non-Javadoc)
	 * @see com.zzb.conf.service.INSBProviderService#queryProTreeList(java.lang.String)
	 */
	@Override
	public List<Map<Object, Object>> queryProTreeList(String parentcode) {
		LogUtil.info("init Pro tree parentinnercode= " + parentcode);
		List<Map<Object, Object>> resultList = new ArrayList<Map<Object, Object>>();
		List<INSBProvider> insbprolist = new ArrayList<INSBProvider>();
		if(StringUtil.isEmpty(parentcode) || "source".equalsIgnoreCase(parentcode)){
			parentcode = "";
		}
		insbprolist = insbProviderDao.selectByParentProTreeCode(parentcode);
		for(int i=0;i<insbprolist.size();i++){
			INSBProvider tempInsbPro = new INSBProvider();
			Map<Object,Object> tempMap = new HashMap<Object,Object>();
			tempInsbPro = insbprolist.get(i);
//			if((insbProviderDao.selectByParentProTreeCode(insbprolist.get(i).getPrvcode())).size()>0){
			
			/**
			 * 旧bootstrap-tree返回json格式
			 * */
			/*if(insbprolist.get(i).getChildflag()=="1"||"1".equals(insbprolist.get(i).getChildflag())){
				tempMap.put("type", "folder");
			}else{
				tempMap.put("type", "item");
			}
			tempMap.put("name", tempInsbPro.getPrvname());
			Map<Object, Object> attr = new HashMap<Object, Object>();
			attr.put("id", tempInsbPro.getId());
			attr.put("parentcode", tempInsbPro.getPrvcode());
			tempMap.put("dataAttributes", attr);*/
			
			/**
			 * zTree返回json格式
			 * */
			tempMap.put("name", tempInsbPro.getPrvname());
			tempMap.put("id", tempInsbPro.getId());
			tempMap.put("pid", tempInsbPro.getPrvcode());
			tempMap.put("isParent", "1".equals(tempInsbPro.getChildflag())? "true" : "false");
			
			resultList.add(tempMap);
		}
		return resultList;
	}
	
	@Override
	public List<Map<Object, Object>> queryProTreeList2(ProOrParModel parentcode) {
		List<Map<Object, Object>> resultList = new ArrayList<Map<Object, Object>>();
		List<INSBProvider> insbprolist = new ArrayList<INSBProvider>();
		if(parentcode.getParentcode()==null){
			parentcode.setParentcode(""); 
		}
		insbprolist = insbProviderDao.selectByParentProTreeCode2(parentcode);
		for(int i=0;i<insbprolist.size();i++){
			INSBProvider tempInsbPro = new INSBProvider();
			Map<Object,Object> tempMap = new HashMap<Object,Object>();
			tempInsbPro = insbprolist.get(i);
//			if((insbProviderDao.selectByParentProTreeCode(insbprolist.get(i).getPrvcode())).size()>0){
			if(insbprolist.get(i).getChildflag()=="1"||"1".equals(insbprolist.get(i).getChildflag())){
				tempMap.put("type", "folder");
			}else{
				tempMap.put("type", "item");
			}
			tempMap.put("name", tempInsbPro.getPrvname());
			Map<Object, Object> attr = new HashMap<Object, Object>();
			attr.put("id", tempInsbPro.getId());
			attr.put("parentcode", tempInsbPro.getPrvcode());
			tempMap.put("dataAttributes", attr);
			resultList.add(tempMap);
		}
		return resultList;
	}

	/* 
	 * 添加供应商
	 * (non-Javadoc)
	 * @see com.zzb.conf.service.INSBProviderService#addProData(com.zzb.conf.entity.INSBProvider)
	 */
	@Override
	public int addProData(INSBProvider pro) {
		return insbProviderDao.addProData(pro);
	}

	/* 
	 * 根据id查找供应商
	 * (non-Javadoc)
	 * @see com.zzb.conf.service.INSBProviderService#queryProinfoById(java.lang.String)
	 */
	@Override
	public INSBProvider queryProinfoById(String id) {
		return insbProviderDao.queryProinfoById(id);
	}

	
	@Override
	public List<Map<String, String>> queryTreeList(String parentcode) {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		List<INSBProvider> insbListPro = queryProListByPid(parentcode);
		for(INSBProvider pro : insbListPro){
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", pro.getId());
			map.put("prvcode", pro.getPrvcode());
			map.put("pId", pro.getParentcode());
			map.put("name",pro.getPrvname());
			map.put("isParent", "1".equals(pro.getChildflag())? "true" : "false");
			list.add(map);
		}
		return list;
	}
	@Override
	public List<Map<String, String>> queryTreeListFirst(String parentcode) {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		List<INSBProvider> insbListPro = queryProListByPid(parentcode);
		for(INSBProvider pro : insbListPro){
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", pro.getId());
			
			map.put("prvcode", pro.getPrvcode());
			map.put("pId", pro.getParentcode());
			map.put("name",pro.getPrvshotname());
			map.put("isParent", "false");
			list.add(map);
		}
		return list;
	}
	
	@Override
	public List<Map<String, String>> queryTreeListStair() {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		List<INSBProvider> insbListPro = insbProviderDao.selectByParentProTreeCodeStair();
		for(INSBProvider pro : insbListPro){
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", pro.getId());
			map.put("prvcode", pro.getPrvcode());
			map.put("pId", pro.getParentcode());
			map.put("name",pro.getPrvname());
			map.put("isParent", "false");
			list.add(map);
		}
		return list;
	}
	
	private List<INSBProvider> queryProListByPid(String parentcode){
		if(StringUtil.isEmpty(parentcode) || "source".equalsIgnoreCase(parentcode)){
			parentcode = "";
		}
		return insbProviderDao.selectByParentProTreeCode(parentcode);
	}

	@Override
	public INSBProvider queryByPrvcode(String prvcode) {
		return insbProviderDao.queryByPrvcode(prvcode);
	}

	@Override
	public int updateProById(String id) {
		return insbProviderDao.updateProById(id);
	}

	@Override
	public int updateProByIddel(String id) {
		return insbProviderDao.updateProByIddel(id);
	}

	@Override
	public List<INSBProvider> queryListPro(String ediid) {
		return insbProviderDao.queryListPro(ediid);
	}

	@Override
	public List<INSBProvider> queryDataByModifyTime(String modiftime) {
		return insbProviderDao.selectByModifytime(modiftime);
	}

	@Override
	public List<INSBProvider> getInscomNameList(List<String> insComCodeList) {
		List<INSBProvider> list = new ArrayList<INSBProvider>();
		for (String prvcode : insComCodeList) {
			INSBProvider provider = new INSBProvider();
			provider.setPrvcode(prvcode);
			INSBProvider temp = insbProviderDao.selectOne(provider);
			list.add(temp);
		}
		return list;
	}

	
	@Override
	public int getQuotationValidityById(String id) {
		return insbProviderDao.getQuotationValidityById(id);
	}

	@Override
	public INSBProvider getProvDept(String providerCode) {
		INSBProvider prov = insbProviderDao.queryByPrvcode(providerCode);
		if(prov.getPrvgrade().equals("01"))
			return prov;
		while(!prov.getPrvgrade().equals("01")){
			prov=insbProviderDao.queryByPrvcode(prov.getParentcode());
			if(prov.getPrvgrade().equals("01")){
				return prov;
			}
			
		}
		return null;
	}

	@Override
	public List<INSBProvider> getprovidedata() {
		// TODO Auto-generated method stub
		return insbProviderDao.selectProvider();
	}

	@Override
	public List<INSBProvider> queryProByInsureAreaCode(
			String insureAreaCode) {
		return insbProviderDao.selectByInsureAreaCode(insureAreaCode);
	}

	@Override
	public List<INSBProvider> queryProByInsureAreaCode2(Map<String, Object> map) {
		
		return insbProviderDao.selectByInsureAreaCode2(map);
	}

	@Override
	public List<Map<String, String>> queryAllProTreeList() {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		List<INSBProvider> insbListPro = insbProviderDao.selectAll();
		for(INSBProvider pro : insbListPro){
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", pro.getId());
			map.put("prvcode", pro.getPrvcode());
			map.put("pId", pro.getParentcode());
			map.put("name",pro.getPrvname());
			map.put("isParent", "1".equals(pro.getChildflag())? "true" : "false"); 
			list.add(map);
		}
		return list;
	}

	@Override
	public List<INSBProvider> queryAll() {
		return insbProviderDao.selectAll();
	}

}