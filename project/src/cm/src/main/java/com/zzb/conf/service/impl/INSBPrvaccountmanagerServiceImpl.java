package com.zzb.conf.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCUser;
import com.zzb.conf.dao.INSBProviderDao;
import com.zzb.conf.dao.INSBPrvaccountkeyDao;
import com.zzb.conf.dao.INSBPrvaccountmanagerDao;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.entity.INSBPrvaccountkey;
import com.zzb.conf.entity.INSBPrvaccountmanager;
import com.zzb.conf.service.INSBPrvaccountmanagerService;

@Service
@Transactional  
public class INSBPrvaccountmanagerServiceImpl extends
		BaseServiceImpl<INSBPrvaccountmanager> implements
		INSBPrvaccountmanagerService {
	@Resource
	private INSBPrvaccountmanagerDao insbPrvaccountmanagerDao;
	@Resource
	private INSBPrvaccountkeyDao prvaccountkeyDao;
	@Resource
	private INSCDeptDao deptDao;
	@Resource
	private INSBProviderDao providerDao;

	@Override
	protected BaseDao<INSBPrvaccountmanager> getBaseDao() {
		return insbPrvaccountmanagerDao;
	}
	

	@Override
	public Map<String, Object> getDataListPage(Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();
		if ("".equals(map.get("deptid")) || map.get("deptid") == null) {
			return result;
		}
		// //递归查询当前deptid及其所有上级机构
		// List<String> deptIds = getAllUpDeptIds(map.get("deptid").toString());
		// map.put("deptIds", deptIds);
		// List<INSBPrvaccountmanager> dataList =
		// insbPrvaccountmanagerDao.selectListPageNew(map);
		// for(INSBPrvaccountmanager model:dataList){
		// if("1".equals(model.getUsetype())){
		// model.setUsetypeStr("精灵");
		// }else if("2".equals(model.getUsetype())){
		// model.setUsetypeStr("EDI");
		// }
		// }
		// result.put("total",
		// insbPrvaccountmanagerDao.selectPageCountByMapNew(map));
		// result.put("rows", dataList);
		List<INSBPrvaccountmanager> dataList = insbPrvaccountmanagerDao
				.extendsPage(map);
		result.put("total", insbPrvaccountmanagerDao.extendscount(map));
		result.put("rows", dataList);
		return result;
	}

	@Override
	public Map<String, Object> getKeyDataListPage(Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<INSBPrvaccountkey> dataList = prvaccountkeyDao.extendsPage(map);
		int count = prvaccountkeyDao.extendscount(map);
		result.put("total", count);
		result.put("rows", dataList);
		return result;
	}

	@Override
	public Map<String, Object> main2edit(String id, String deptid) {
		Map<String, Object> result = new HashMap<String, Object>();

		INSCDept deptModel = deptDao.selectByComcode(deptid);
		result.put("deptid", deptid);
		if (null != deptModel) {
			result.put("deptname", deptModel.getComname());
		}
		if (id == null || "".equals(id)) {
			return result;
		}
		INSBPrvaccountmanager model = insbPrvaccountmanagerDao.selectById(id);
		if (model == null) {
			result.put("deptid", deptid);
			if (null != deptModel) {
				result.put("deptname", deptModel.getComname());
			}
			return result;
		}

		if (null != model.getProviderid() && !"".equals(model.getProviderid())) {
			INSBProvider providerModel = providerDao.selectById(model
					.getProviderid());
			result.put("prvName", providerModel.getPrvshotname());
			result.put("prvId", providerModel.getId());

			INSCDept deptModel1 = deptDao.selectByComcode(model.getDeptid());
			result.put("deptid", model.getDeptid());
			if (null != deptModel) {
				result.put("deptname", deptModel1.getComname());
			}
		} else {
			result.put("prvName", null);
			result.put("prvId", null);
		}

		result.put("model", model);

		return result;
	}

	@Override
	public Map<String, Object> mian2keyEdit(String id) {
		Map<String, Object> result = new HashMap<String, Object>();
		if ("".equals(id)) {
			return result;
		}
		INSBPrvaccountkey model = prvaccountkeyDao.selectById(id);
		result.put("model", model);
		return result;
	}

	@Override
	public void saveOrUpdate(INSCUser user, INSBPrvaccountmanager model) {
		// 通过id判断当前这条是修改 还是新增
		if (model != null) {
			if (StringUtils.isEmpty(model.getId())) {
				int hasData = accountManagerDataExists(model);
				if (hasData==0) {
					model.setCreatetime(new Date());
					model.setOperator(user.getUsercode());
					LogUtil.debug("===新增配置模块主表信息==="+model.toString());
					insbPrvaccountmanagerDao.insert(model);
				}
			} else {
				// 修改
				model.setModifytime(new Date());
				model.setOperator(user.getUsercode());
				LogUtil.debug("===修改配置模块主表信息==="+model.toString());
				insbPrvaccountmanagerDao.updateById(model);
			}
		}
	}

	/**
	 * 判断当前新增数据逻辑逐渐是否存在
	 * 
	 * @param model
	 * @return
	 */
	private int accountManagerDataExists(INSBPrvaccountmanager model) {
		INSBPrvaccountmanager queryModel = new INSBPrvaccountmanager();
		queryModel.setDeptid(model.getDeptid());
		queryModel.setProviderid(model.getProviderid());
		queryModel.setUsetype(model.getUsetype());

		// 加一个查询判断当前逻辑主键是否存在
		long mycount = insbPrvaccountmanagerDao.selectCount(queryModel);
		LogUtil.debug("===判断配置模块主表信息是否存在===count="+mycount+"---model="+model.toString());

		return (int) mycount;
	}

	@Override
	public void saveOrUpdateKey(INSCUser user, INSBPrvaccountkey model,String deptId,String manageridmain) {
		if (StringUtils.isEmpty(model.getId())) {
			LogUtil.debug("===新增配置模块子表信息是否存在---model="+model.toString());
			addData(user, model, deptId,manageridmain);
		} else {
			updateAddKeyData(user, model, deptId,manageridmain);
		}
	}

	/**
	 * 修改子表数据
	 * 
	 * @param user
	 * @param model
	 * @param deptId
	 * @param manageridmain
	 * @return
	 */
	private boolean updateAddKeyData(INSCUser user, INSBPrvaccountkey model,String deptId,String manageridmain) {
		
		//已经选中主表数据
		if (manageridmain != null) {
			
//			INSBPrvaccountmanager managerModel = insbPrvaccountmanagerDao.selectById(manageridmain);
			
			
			INSBPrvaccountmanager managerModelTemp = insbPrvaccountmanagerDao.selectById(manageridmain);
			INSBPrvaccountmanager managerModel = accountManagerNameExists(deptId,managerModelTemp.getProviderid(),managerModelTemp.getUsetype());
			
			
			String oldDataId= accountKeyNameExists(model, deptId, managerModel.getProviderid(), managerModel.getUsetype());

			if (managerModel.getDeptid().equals(deptId)) {
				
				if(StringUtils.isEmpty(oldDataId)){
					model.setOperator(user.getUsercode());
					model.setManagerid(managerModel.getId());
					model.setId(UUIDUtils.random());
					model.setCreatetime(new Date());
					LogUtil.debug("===新增配置模块子表信息---model="+model.toString());

					prvaccountkeyDao.insert(model);
				}else{
					if(managerModel.getId().equals(model.getManagerid())){
						model.setModifytime(new Date());
						model.setOperator(user.getUsercode());
						model.setManagerid(managerModel.getId());
						model.setId(oldDataId);
						LogUtil.debug("===修改配置模块主表信息---model="+model.toString());
						prvaccountkeyDao.updateById(model);
					}else{
						model.setCreatetime(new Date());
						model.setOperator(user.getUsercode());
						model.setManagerid(managerModel.getId());
						model.setId(UUIDUtils.random());
						LogUtil.debug("===修改配置模块主表信息---model="+model.toString());
						prvaccountkeyDao.insert(model);
					}
					
				}
			}//新增主表和子表
			else{
				String tempId = UUIDUtils.random();
				managerModel.setDeptid(deptId);
				managerModel.setId(tempId);
				managerModel.setNoti(null);
				managerModel.setCreatetime(new Date());
				LogUtil.debug("===新增配置模块主表信息---model="+managerModel.toString());

				insbPrvaccountmanagerDao.insert(managerModel);
				
				model.setCreatetime(new Date());
				model.setOperator(user.getUsercode());
				model.setManagerid(tempId);
				model.setId(UUIDUtils.random());
				LogUtil.debug("===新增配置模块子表信息---model="+model.toString());

				prvaccountkeyDao.insert(model);
			}
		}
		return true;
	}

	/**
	 * @param user
	 * @param model
	 * @param deptId 当前选中机构
	 * @param manageridmain 主表选中行id
	 * @return
	 */
	private boolean addData(INSCUser user, INSBPrvaccountkey model,String deptId,String manageridmain) {
		
		
		if (manageridmain!= null) {
//			INSBPrvaccountmanager managerModel = insbPrvaccountmanagerDao.selectById(manageridmain);
			
			INSBPrvaccountmanager managerModelTemp = insbPrvaccountmanagerDao.selectById(manageridmain);
			INSBPrvaccountmanager managerModel = accountManagerNameExists(deptId,managerModelTemp.getProviderid(),managerModelTemp.getUsetype());
			
			
			String oldDataId= accountKeyNameExists(model, deptId, managerModel.getProviderid(), managerModel.getUsetype());
			
			
			//当前主表数据 不是当前机构数
			if(!deptId.equals(managerModel.getDeptid())){
				String tempId = UUIDUtils.random();
				managerModel.setDeptid(deptId);
				managerModel.setId(tempId);
				managerModel.setNoti(null);
				managerModel.setCreatetime(new Date());
				LogUtil.debug("===新增配置模块主表信息---model="+managerModel.toString());
				insbPrvaccountmanagerDao.insert(managerModel);
				if(StringUtils.isEmpty(oldDataId)){
					model.setCreatetime(new Date());
					model.setOperator(user.getUsercode());
					model.setManagerid(tempId);
					LogUtil.debug("===新增配置模块子表信息---model="+model.toString());
					prvaccountkeyDao.insert(model);
				}else{
					model.setModifytime(new Date());
					model.setOperator(user.getUsercode());
					model.setManagerid(tempId);
					model.setId(oldDataId);
					LogUtil.debug("===修改配置模块子表信息---model="+model.toString());
					prvaccountkeyDao.updateById(model);
				}
				
			}//有数据必然是当前选中的主表数据
			else{
				if(StringUtils.isEmpty(oldDataId)){
					model.setCreatetime(new Date());
					model.setOperator(user.getUsercode());
					model.setManagerid(managerModel.getId());
					LogUtil.debug("===新增配置模块子表信息---model="+model.toString());
					prvaccountkeyDao.insert(model);
				}else{
					model.setModifytime(new Date());
					model.setOperator(user.getUsercode());
					model.setManagerid(managerModel.getId());
					model.setId(oldDataId);
					LogUtil.debug("===修改配置模块子表信息---model="+model.toString());
					prvaccountkeyDao.updateById(model);
				}
			}
		}
		return true;
	}

	private String accountKeyNameExists(INSBPrvaccountkey model, String logicDeptId,String logicProviderId,String logicUseType) {
		if (!StringUtils.isEmpty(logicProviderId)) {
			// 判断当前的key是否存在
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("limit", 1000);
			paramMap.put("managerid", "1");
			paramMap.put("offset", 0);
			paramMap.put("order", "asc");
			paramMap.put("deptId", logicDeptId);
			paramMap.put("usetype", logicUseType);
			paramMap.put("providerid", logicProviderId);
			List<INSBPrvaccountkey> dataList = prvaccountkeyDao.extendsPage(paramMap);
			for (INSBPrvaccountkey oldModel : dataList) {
				if (model.getParamname().equals(oldModel.getParamname())) {
					LogUtil.debug("===判断配置模块子表信息存在---model="+model.toString());
					return oldModel.getId();
				}
			}
		}
		return "";
	}
	private INSBPrvaccountmanager accountManagerNameExists(String logicDeptId,
			String logicProviderId, String logicUseType) {
		if (StringUtils.isEmpty(logicUseType)) {
			return null;
		}
		if (StringUtils.isEmpty(logicProviderId)) {
			return null;
		}
		if (StringUtils.isEmpty(logicUseType)) {
			return null;
		}
		// 判断当前的key是否存在
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("limit", 1000);
		paramMap.put("offset", 0);
		paramMap.put("order", "asc");
		paramMap.put("deptid", logicDeptId);
		paramMap.put("usetype", logicUseType);
		paramMap.put("providerid", logicProviderId);
		List<INSBPrvaccountmanager> dataList = insbPrvaccountmanagerDao
				.extendsPage(paramMap);
		if (dataList != null && dataList.size() == 1) {
			return dataList.get(0);
		}
		return null;
	}
}