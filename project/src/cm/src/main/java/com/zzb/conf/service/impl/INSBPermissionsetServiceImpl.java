package com.zzb.conf.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.system.entity.INSCDept;
import com.zzb.conf.controller.INSBPermissionsetController;
import com.zzb.conf.controller.vo.ExportPermissionInfoVo;
import com.zzb.conf.entity.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.system.dao.INSCDeptDao;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.dao.INSBAgentpermissionDao;
import com.zzb.conf.dao.INSBAgentproviderDao;
import com.zzb.conf.dao.INSBItemprovidestatusDao;
import com.zzb.conf.dao.INSBPermissionDao;
import com.zzb.conf.dao.INSBPermissionallotDao;
import com.zzb.conf.dao.INSBPermissionsetDao;
import com.zzb.conf.dao.INSBProviderDao;
import com.zzb.conf.service.INSBPermissionsetService;
import net.sf.json.JSONArray;

@Service
@Transactional
public class INSBPermissionsetServiceImpl extends
		BaseServiceImpl<INSBPermissionset> implements INSBPermissionsetService {
	
	@Override
	protected BaseDao<INSBPermissionset> getBaseDao() {
		return insbPermissionsetDao;
	}
	
	@Resource
	private INSBPermissionsetDao insbPermissionsetDao;
	@Resource
	private INSBAgentDao agentDao;
	@Resource
	private INSCDeptDao inscDeptDao;
	@Resource
	private INSBProviderDao insbProviderDao;
	@Resource
	private INSBPermissionDao insbPermissionDao;
	@Resource
	private INSBPermissionallotDao insbPermissionallotDao;
	@Resource
	private INSBItemprovidestatusDao itemprovidestatusDao;
	@Resource
	private INSBAgentpermissionDao agentpermissionDao;
	@Resource
	private INSBAgentproviderDao agentproviderDao;
	@Resource
	private INSBPermissionsetDao permissionsetDao;
	@Resource
	private INSBAgentDao insbAgentDao;
	
	@Override
	public Map<String, Object> getPermissionsetListByPage(
			Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<INSBPermissionset> resultList = insbPermissionsetDao
				.selectListPage(map);
		for (INSBPermissionset model : resultList) {
			if (1 == model.getAgentkind()) {
				model.setAgentkindstr("试用");
			} else if (2 == model.getAgentkind()) {
				model.setAgentkindstr("正式");
			} else if (3 == model.getAgentkind()) {
				model.setAgentkindstr("渠道");
			}
			if (1 == model.getStatus()) {
				model.setStatusstr("停用");
			} else {
				model.setStatusstr("启用");
			}
		}
		resultMap.put("rows", resultList);
		resultMap.put("total", insbPermissionsetDao.selectCountByParam(map));
		return resultMap;
	}

	@Override
	public Map<String, Object> getpermissionListByPage(Map<String, Object> map,
			String permissionsetId) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> permissionList = new ArrayList<Map<String, Object>>();

		// 新增功能呢个包 直接查询权限表
		if (permissionsetId == null || "".equals(permissionsetId.trim())) {
			// 得到权限信息
			permissionList = insbPermissionDao.selectListByPage4add(map);

			// 是修改 需要查询 关系表
		} else {
			map.put("permissionsetId", permissionsetId);
			permissionList = insbPermissionDao
					.selectListByPage4allotupdate(map);
		}

		for (Map<String, Object> mapTemp : permissionList) {
			if (mapTemp.get("functionstate") != null) {
				if ((int) mapTemp.get("functionstate") == 1) {
					mapTemp.put("functionstatestr", "启用");
				} else if ((int) mapTemp.get("functionstate") == 0) {
					mapTemp.put("functionstatestr", "停用");
				}
			}
			if (mapTemp.get("frontstate") != null) {
				if ((int) mapTemp.get("frontstate") == 1) {
					mapTemp.put("frontstatestr", "启用");
				} else if ((int) mapTemp.get("frontstate") == 0) {
					mapTemp.put("frontstatestr", "停用");
				}
			}
			//**
			if (mapTemp.get("abort") != null) {
				if ((int) mapTemp.get("abort") == 1) {
					mapTemp.put("abortstr", "开启");
				} else if ((int) mapTemp.get("abort") == 0) {
					mapTemp.put("abortstr", "关闭");
				}
			}
			//**

		}
		long count = insbPermissionDao.selectCountByIstry(map);
		result.put("total", count);
		result.put("rows", permissionList);
		return result;
	}

	@Override
	public Map<String, Object> getUserListByPage(Map<String, Object> map,
													   String permissionsetId) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> userList = new ArrayList<Map<String, Object>>();

		// 新增功能呢个包 直接查询权限表
		if (permissionsetId == null || "".equals(permissionsetId.trim())) {
			result.put("total", 0);
			result.put("rows", userList);
			return result;
		} else {
			map.put("setId", permissionsetId);
			userList = permissionsetDao.getUserByPage(map);
		}

		for (Map<String, Object> mapTemp : userList) {
			if (mapTemp.get("agentstatus") != null) {
				if ((int) mapTemp.get("agentstatus") == 1) {
					mapTemp.put("agentstatus", "启用");
				} else if ((int) mapTemp.get("agentstatus") == 2) {
					mapTemp.put("agentstatus", "停用");
				} else {
					mapTemp.put("agentstatus", "");
				}
			}
			if (mapTemp.get("agentkind") != null) {
				if ((int) mapTemp.get("agentkind") == 1) {
					mapTemp.put("agentkind", "试用");
				} else if ((int) mapTemp.get("agentkind") == 2) {
					mapTemp.put("agentkind", "正式");
				}else if ((int) mapTemp.get("agentkind") == 3) {
					mapTemp.put("agentkind", "渠道");
				} else {
					mapTemp.put("agentkind", "");
				}
			}


		}
		long count = permissionsetDao.getUserByPageCount(map);
		result.put("total", count);
		result.put("rows", userList);
		return result;
	}

	@Override
	public List<Map<String, String>> getProviderTreeList(String parentcode,
			String permissionSetId, String checked) {
		// 得到当前功能包所有供应商
		List<INSBItemprovidestatus> providerIds = itemprovidestatusDao
				.selectListBySetId(permissionSetId);

		// 存储关系表中所有供应商id
		List<String> TempItemProviderIdsList = new ArrayList<String>();

		for (INSBItemprovidestatus item : providerIds) {
			TempItemProviderIdsList.add(item.getProvideid());
		}

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if (parentcode == null) {
			parentcode = "";
		}
		List<INSBProvider> insbListPro = insbProviderDao
				.selectByParentProTreeCode(parentcode);
		for (INSBProvider pro : insbListPro) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", pro.getPrvcode());
			map.put("prvId", pro.getId());
			map.put("pId", pro.getParentcode());
			map.put("name", pro.getId() + "-" + pro.getPrvname());
			map.put("isParent", "1".equals(pro.getChildflag()) ? "true"
					: "false");

			// 供应商关系表中所有的 id
			for (INSBItemprovidestatus model : providerIds) {

				// 关系表中id 等于当前初始化id
				if (model.getProvideid().equals(pro.getId())) {

					// 得到当前初始化id所有子节点
					List<INSBProvider> tempInsbListPro = insbProviderDao
							.selectByParentProTreeCode(pro.getId());

					// 如果子节点不为空
					if (tempInsbListPro != null) {

						// 存储当前子节点子节点数据
						List<String> TempProviderIdsList = new ArrayList<String>();

						// 把子节点放到新list中
						for (INSBProvider pid : tempInsbListPro) {
							TempProviderIdsList.add(pid.getId());
						}
						// 存在 全选
						if (TempItemProviderIdsList
								.containsAll(TempProviderIdsList)) {
							map.put("checked", "true");
							map.put("openflag", "1");
							// 不全部存在 半选
						} else {
							map.put("checked", "true");
							map.put("halfCheck", "true");
							map.put("openflag", "0");
						}
					} else {
						map.put("checked", "true");
						map.put("openflag", "1");
					}

				} else {
					map.put("openflag", "0");
				}
			}
			list.add(map);

		}
		return list;
	}

	@Override
	public Map<String, String> savePermissionsetAllot(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> main2edit(String id) {

		Map<String, Object> result = new HashMap<String, Object>();
		// 功能包 文本信息
		INSBPermissionset set = insbPermissionsetDao.selectById(id);

		// 通过功能包id 查询权限分配信息（那些列表需要选中）
		List<INSBPermissionallot> permissionList = insbPermissionallotDao
				.selectListBySetId(id);

		// 查询功能包id 得到当前功能包绑定供应商
		List<INSBItemprovidestatus> providerList = itemprovidestatusDao
				.selectListBySetId(id);
		result.put("set", set);
		result.put("permission", permissionList);
		result.put("provider", providerList);

		return result;
	}

	@Override
	public String saveOrUpdateSetReturnId(INSBPermissionset set) {

		// 默认新建功能包 未开启（需要返回列表页面开启）

		if (StringUtil.isEmpty(set.getId())) {
			set.setStatus(1);
			set.setIsnew(1);
		}
		if (set.getId().toString().trim().length() > 0) {
			set.setIsnew(2);
			set.setModifytime(new Date());
			insbPermissionsetDao.updateById(set);
			return set.getId();
		} else {
			String setid = insbPermissionsetDao.insertReturnId(set);
			List<INSBPermission> list = insbPermissionDao.selectAll();
			for (INSBPermission p : list) {
				//(id,setid,permissionid,permissionname,functionstate,createtime,operator
				INSBPermissionallot a = new INSBPermissionallot();
				a.setSetid(setid);
				a.setPermissionid(p.getId());
				a.setPermissionname(p.getPermissionname());
				a.setFunctionstate(0);
				a.setCreatetime(new Date());
				a.setOperator(set.getOperator());
				insbPermissionallotDao.insert(a);
			}
			return setid;
		}

	}

	@Override
	public void saveSetProviderAllotData(String setId, String providerIds,
			String opFlag) {
		// 新供应商id（需要调整，部分子节点需要从数据库中获得。检查所有节点的子节点是否在数据库中存在，如果存在 加入到新选中集合中）

		List<String> newIds = new ArrayList<String>();
		// 原来供应商id
		List<String> oldIds = new ArrayList<String>();

		List<String> addIds = new ArrayList<String>();

		String[] providerIsArr = providerIds.split(",");
		for (String id : providerIsArr) {
			newIds.add(id);
			addIds.add(id);
		}

		// 拿到当前功能包id所有 供应商code
		List<INSBItemprovidestatus> itemList = itemprovidestatusDao
				.selectListBySetId(setId);
		for (INSBItemprovidestatus model : itemList) {
			oldIds.add(model.getProvideid());
		}
		addIds.removeAll(oldIds);

		// 进行新增操作
		//批量新增
		List<INSBItemprovidestatus> addList = new ArrayList<INSBItemprovidestatus>();
		for (String providerId : addIds) {
			INSBItemprovidestatus item = new INSBItemprovidestatus();
			item.setProvideid(providerId);
			item.setSetid(setId);
			item.setCreatetime(new Date());
			item.setOperator("1");
			addList.add(item);
			//itemprovidestatusDao.insert(item);
		}
		itemprovidestatusDao.insertInBatch(addList);
		// 进行删除操作
		oldIds.removeAll(newIds);
		for (String id : oldIds) {
			System.out.println("删除id==" + id);
			Map<String, String> ids = new HashMap<String, String>();
			ids.put("setId", setId);
			ids.put("provideId", id);
			itemprovidestatusDao.deleteBySetIdproviderId(ids);
		}

	}

	@Override
	public void updateAgentTable(String setId, String isNew) {

		// 当前功能包是新增的
		/*if ("1".equals(isNew)) {
			// 根据功能包逻辑主键查找代理人
			INSBPermissionset set = insbPermissionsetDao.selectById(setId);

			// 逻辑主键
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("agentkind", set.getAgentkind());
			map.put("deptid", set.getDeptid());
			map.put("istest", set.getIstest());

			// 得到功能包逻辑主键所有代理人信息
			List<INSBAgent> agentList = agentDao.selectListByLogicId(map);

			// 同步关系表
			for (INSBAgent model : agentList) {
				// 更新代理人功能包信息
				INSBAgent gaentTemp = new INSBAgent();
				gaentTemp.setSetid(setId);
				gaentTemp.setId(model.getId());

				System.out.println("id==" + gaentTemp.getId());
				// agentDao.updateById(gaentTemp);
				agentDao.updateSetIdById(gaentTemp);

				saveAgentProvider(model.getId(), setId);
				// saveAgentPermission(model.getId(), setId);

			}

			// 不是新功能包 只更新当前功能包代理人
		} else {
			// 更新权限关系表
			updateAgentPermission(setId);
			updateAgentProvider(setId);

		}*/

		// 更新功能包状态
		INSBPermissionset updateSet = new INSBPermissionset();
		updateSet.setId(setId);
		updateSet.setStatus(2);
		// 设置当前功能包不允许删除的标志
		updateSet.setIsnew(2);
		updateSet.setModifytime(new Date());
		insbPermissionsetDao.updateById(updateSet);
	}

	/**
	 * 更新代理人权限关系表
	 * 
	 * 由于涉及到 权限 其他字段的修改 采用删除以前所有此功能包权限。在新增的形式
	 */
	private void updateAgentPermission(String setId) {

		List<INSBAgent> agentList = agentDao.selectBySetId(setId);
		for (INSBAgent model : agentList) {
			// 删除旧的关系表数据
			agentpermissionDao.deleteByAgentId(model.getId());

			// 新增关系表数据
			saveAgentPermission(model.getId(), setId);
		}
	}

	/**
	 * 更新供应商关系表
	 * 
	 * @param setId
	 */
	private void updateAgentProvider(String setId) {
		Set<String> newProviderId = new HashSet<String>();
		Set<String> oldProviderId = new HashSet<String>();
		Set<String> addProviderId = new HashSet<String>();

		List<INSBItemprovidestatus> providerIdList = itemprovidestatusDao
				.selectListBySetId(setId);
		for (INSBItemprovidestatus providerModel : providerIdList) {
			newProviderId.add(providerModel.getProvideid());
			addProviderId.add(providerModel.getProvideid());
		}
		List<INSBAgent> agentList = agentDao.selectBySetId(setId);
		for (INSBAgent model : agentList) {
			List<INSBAgentprovider> apidList = agentproviderDao
					.selectListByAgentId(model.getId());
			for (INSBAgentprovider apModel : apidList) {
				oldProviderId.add(apModel.getProviderid());
			}
		}

		// 关系表需要新增的数据
		addProviderId.removeAll(oldProviderId);
		
		for (INSBAgent model : agentList) {
			//批量新增
			List<INSBAgentprovider> addList = new ArrayList<INSBAgentprovider>();
			for (String providerId : addProviderId) {
				INSBAgentprovider apTempAdd = new INSBAgentprovider();
				apTempAdd.setAgentid(model.getId());
				apTempAdd.setProviderid(providerId);
				apTempAdd.setCreatetime(new Date());
				apTempAdd.setOperator("1");
				addList.add(apTempAdd);
				//agentproviderDao.insert(apTempAdd);
			}
			agentproviderDao.insertInBatch(addList);
		}

		// 需要删除的关系表数据
		oldProviderId.removeAll(newProviderId);
		for (INSBAgent model : agentList) {
			for (String providerId : oldProviderId) {
				Map<String, String> ids = new HashMap<String, String>();
				ids.put("agentId", model.getId());
				ids.put("providerId", providerId);
				agentproviderDao.deleteByAgentIdproviderId(ids);
			}
		}
	}

	/**
	 * 
	 * 保存功能包信息 同时保存代理人权限关系表
	 * 
	 * TODO 解除权限 删除关系表
	 * 
	 * @param agentId
	 *            功能包id 根据功能包id 查出对应代理人
	 * @param setId
	 *            权限id
	 * @return
	 */
	private void saveAgentPermission(String agentId, String setId) {

		List<INSBPermissionallot> allotList = insbPermissionallotDao
				.selectListBySetId(setId);
		
		List<INSBAgentpermission> addList = new ArrayList<INSBAgentpermission>();
		for (INSBPermissionallot allot : allotList) {

			INSBAgentpermission ap = new INSBAgentpermission();
			ap.setAgentid(agentId);
			ap.setPermissionid(allot.getPermissionid());
			ap.setCreatetime(new Date());
			ap.setOperator("1");
			ap.setFrontstate(allot.getFrontstate());
			ap.setFunctionstate(allot.getFunctionstate());
			ap.setPermissionname(allot.getPermissionname());
			ap.setAbort(allot.getAbort());
			ap.setNoti(allot.getNoti());
			ap.setValidtimeendstr(allot.getValidtimeendstr());
			ap.setValidtimeend(allot.getValidtimeend());
			ap.setValidtimestart(allot.getValidtimestart());
			ap.setValidtimestartstr(allot.getValidtimestartstr());
			addList.add(ap);		
		}
		agentpermissionDao.insertInBatch(addList);
	}

	/**
	 * 根据功能包供应商设置 同步代理人供应商设置
	 * 
	 * @param setId
	 */
	private void saveAgentProvider(String agentId, String setId) {

		// 得到所有权限id
		List<INSBItemprovidestatus> itemList = itemprovidestatusDao
				.selectListBySetId(setId);
		
		List<INSBAgentprovider> addList = new ArrayList<INSBAgentprovider>();
		for (INSBItemprovidestatus model : itemList) {
			INSBAgentprovider ap = new INSBAgentprovider();
			ap.setAgentid(agentId);
			ap.setProviderid(model.getProvideid());
			ap.setCreatetime(new Date());
			ap.setOperator("1");
			// 向代理人权限关系表中新增数据
			addList.add(ap);
		}
		agentproviderDao.insertInBatch(addList);
		
		
		saveAgentPermission(agentId, setId);
	}

	@Override
	public int deleteJudge(String setId) {
		int result = 1;
		INSBPermissionset set = insbPermissionsetDao.selectById(setId);
//		if (set.getIsnew() == 2) {
//			result = 2;
//		} else if (set.getIsnew() == 1) {
			// 删除功能包基础信息
			insbPermissionsetDao.deleteById(setId);
			// 删除权限功能包关系表对应数据
			insbPermissionallotDao.deleteBySetId(setId);
			// 删除供应商功能包关系表对应数据
			itemprovidestatusDao.deleteBySetId(setId);

			//将代理人的setid更新为空
			agentDao.updateSetIdIsNull(setId);

		//}
		return result;
	}

	@Override
	public List<INSBPermissionset> queryOnUseSet() {
		return insbPermissionsetDao.selectByOnUseSet();
	}
	
	@Override
	public int initTestAgentPermission(String deptId, String jobNo) {
		return initPermission(deptId, jobNo, 1);
	}

	@Override
	public int initAgentPermission(String deptId, String jobNo) {
		return initPermission(deptId, jobNo, 2);

	}

	/**
	 * 设置代理人权限
	 * @param agent
	 */
	private void setAgentpermission(INSBAgent agent) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("permissionsetId",agent.getSetid());
		queryMap.put("istry",agent.getAgentkind());

		List<Map<String, Object>> permissionList = insbPermissionDao
				.selectSetPermission(queryMap);
		List<INSBAgentpermission> insbAgentpermissionList = agentpermissionDao.selectByAgentId(agent.getId());
		if(permissionList != null && !permissionList.isEmpty()) {
			for (Map<String, Object> m : permissionList) {
				if(m.get("allotid") == null) {
					continue;
				}
				INSBAgentpermission agentpermission = new INSBAgentpermission();
				agentpermission.setAgentid(agent.getId());
				agentpermission.setPermissionid((String) m.get("id"));
				agentpermission.setPermissionname((String) m.get("permissionpath"));
				Integer surplusnum = null;
				Date createTime = null;
				if( insbAgentpermissionList != null && insbAgentpermissionList.size() > 0 ) {
					for( INSBAgentpermission insbAgentpermission : insbAgentpermissionList ) {
						if( agentpermission.getAgentid().equals(insbAgentpermission.getAgentid())
								&& agentpermission.getPermissionid().equals(insbAgentpermission.getPermissionid())
								&& agentpermission.getPermissionname().equals(insbAgentpermission.getPermissionname()) ) {
							agentpermission.setId(insbAgentpermission.getId());
							surplusnum = insbAgentpermission.getSurplusnum();
							createTime = insbAgentpermission.getCreatetime();
							break;
						}
					}
				}
				Integer i = m.get("num") == null ? null: (Integer) m.get("num");
				agentpermission.setNum(i);
				if( createTime == null ) {
					agentpermission.setCreatetime(new Date());
				} else {
					agentpermission.setCreatetime(createTime);
				}
				agentpermission.setValidtimeend(m.get("validtimeend") == null ? null : (Date) m.get("validtimeend"));
				agentpermission.setValidtimestart(m.get("validtimestart") == null ? null : (Date) m.get("validtimestart"));
				agentpermission.setSurplusnum(surplusnum);
//				agentpermissionDao.insert(agentpermission);
				if( agentpermission.getId() != null ) {
					agentpermission.setModifytime(new Date());
					agentpermissionDao.updateById(agentpermission);
				} else {
					agentpermissionDao.insert(agentpermission);
				}
			}
		}
	}

	private int initPermission(String deptId, String jobNo, int istest) {


		String agentId = null;
		// 得到当前代理人id
		if (istest == 1) {

			INSBAgent agent = agentDao.selectByJobnum(jobNo);

			//用户注册时为试用用户  默认试用权限包 给该用户
			//获取试用权限逻辑 ，根据该用户所属机构向上递归 试用权限包（用户类型为试用的权限包）
			if (agent.getAgentkind() != null && agent.getAgentkind() == 1) {
				//List<INSBPermissionset> list = permissionsetDao.selectTrialSet(agent.getDeptid());
				//if(list != null && !list.isEmpty()) {
				//	agent.setSetid(list.get(0).getId());
				//	agentDao.updateSetIdById(agent);
				//} else {
					agent.setSetid("trial.permission.setid");
					agentDao.updateSetIdById(agent);
				//}
			}

			setAgentpermission(agent);

		}

		return 0;
/*
		// 得到当前代理人对应功能包，并且是已经启用的功能包
		List<String> resultSetList = insbPermissionsetDao.selectByDeptId(
				deptId, istest);

		// 当前代理人对应网点有功能包,当前默认取得最新功能包
		if (resultSetList != null && resultSetList.size() > 0) {
			String setId = resultSetList.get(0);

			// 得到权限id列表
			List<String> permissionIdList = insbPermissionallotDao
					.selectBySetId(setId);

			// 权限关联当前代理人
			//批量新增
			List<INSBAgentpermission> addList = new ArrayList<INSBAgentpermission>();
			for (String permissionId : permissionIdList) {
				INSBAgentpermission apModel = new INSBAgentpermission();
				apModel.setCreatetime(new Date());
				apModel.setOperator("system");
				apModel.setPermissionid(permissionId);
				apModel.setAgentid(agentId);
				addList.add(apModel);
				//agentpermissionDao.insert(apModel);
			}
			agentpermissionDao.insertInBatch(addList);
			
			// 得到供应商id列表
			List<String> providerIdList = itemprovidestatusDao.selectProviderIdBySetId(setId);
			
			// 供应商关联当前代理人
			//批量新增
			List<INSBAgentprovider> providerlist = new ArrayList<INSBAgentprovider>();
			for(String providerId : providerIdList) {
				INSBAgentprovider agentProvider = new INSBAgentprovider();
				agentProvider.setAgentid(agentId);
				agentProvider.setProviderid(providerId);
				agentProvider.setCreatetime(new Date());
				agentProvider.setOperator("system");
				providerlist.add(agentProvider);
			}
			agentproviderDao.insertInBatch(providerlist);
			
			INSBAgent tempModel = new INSBAgent();
			tempModel.setId(agentId);
			tempModel.setSetid(setId);
			agentDao.updateSetIdById(tempModel);
			
			return 1;
		} else {
			// 初始化默认代理人权限包
//			String setid = ConfigUtil.getPropString("default.permission.setid");
			List<String> resultList = insbPermissionsetDao.selectDefaultPermissionallot(istest);
			
			if(resultList != null && resultList.size() > 0){
				String setId = resultList.get(0);
				// 得到权限id列表
				List<String> permissionIdList = insbPermissionallotDao.selectBySetId(setId);
				
				// 权限关联当前代理人
				//批量新增
				List<INSBAgentpermission> addList = new ArrayList<INSBAgentpermission>();
				for (String permissionId : permissionIdList) {
					INSBAgentpermission apModel = new INSBAgentpermission();
					apModel.setCreatetime(new Date());
					apModel.setOperator("system");
					apModel.setPermissionid(permissionId);
					apModel.setAgentid(agentId);
					addList.add(apModel);
					//agentpermissionDao.insert(apModel);
				}
				agentpermissionDao.insertInBatch(addList);
				
				// 得到供应商id列表
				List<String> providerIdList = itemprovidestatusDao.selectProviderIdBySetId(setId);
				
				// 供应商关联当前代理人
				//批量新增
				List<INSBAgentprovider> providerlist = new ArrayList<INSBAgentprovider>();
				for(String providerId : providerIdList) {
					INSBAgentprovider agentProvider = new INSBAgentprovider();
					agentProvider.setAgentid(agentId);
					agentProvider.setProviderid(providerId);
					agentProvider.setCreatetime(new Date());
					agentProvider.setOperator("system");
					providerlist.add(agentProvider);
				}
				agentproviderDao.insertInBatch(providerlist);
				
				INSBAgent tempModel = new INSBAgent();
				tempModel.setId(agentId);
				tempModel.setSetid(setId);
				agentDao.updateSetIdById(tempModel);

				return 2;
			} else {
				return 0;
			}
		}
		*/
	}

	@Override
	public void releaseBandingByLogicId(INSBPermissionset set) {
		// 更新功能包状态为停用
		set.setStatus(1);
		if (INSBPermissionsetController.TRIAL_PERMISSION_SETID.equals(set.getId())) {
			return;
		} else {
			set.setModifytime(new Date());
			insbPermissionsetDao.updateById(set);
		}


		// 删除代理人权限关系表信息
		/*List<INSBAgent> tempAgentList = agentDao.selectBySetId(set.getId());
		for (INSBAgent model : tempAgentList) {
			agentpermissionDao.deleteByAgentId(model.getId());

		}

		// 解除功能包绑定
		if (set.getId() != null) {
			agentDao.updateSetIdIsNull(set.getId());
		}*/
	}

	@Override
	public List<INSBPermissionset> selectByDeptAgentkindAndOnUseSet(String deptid,
			Integer agentkind) {
		return insbPermissionsetDao.selectByDeptAgentkindAndOnUseSet(deptid, agentkind);
	}
	
	@Override
	public int selectCountBySetCode(String setcode,String id) {
		int num = 0;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("setcode", setcode);
		// 当前为修改
		if (id != null && !id.equals("")) {
			param.put("id", id);
		}
		num = insbPermissionsetDao.selectCountBySetCode(param);

		return num;
	}

	/**
	 * 当某机构或网点应用了某个功能包后，代表该机构或网点下的权限功能包指定用户类型（如：试用）的用户将赋予此权限包的功能权限。
	 * 例如：给广东南枫机构应用了一个权限包，该权限包对应的用户类型是试用，则广东南枫下所有用户类型为试用的用户，都应用了该权限包。
	 *
	 * @param setid
	 * @param deptid
	 * @return
	 */
	@Override
	public boolean saveFuncs(String setid, String deptid, String operator) {
		if (StringUtil.isEmpty(setid)) return false;
		if (StringUtil.isEmpty(deptid)) return false;
		INSCDept dept = inscDeptDao.selectById(deptid);
		if (dept == null) return false;
		String deptinnercode = dept.getDeptinnercode();
		if (StringUtil.isEmpty(deptinnercode)) return false;
		//试用代理人权限
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deptinnercode",deptinnercode);
		map.put("agentkind",1);
		LogUtil.info("试用代理人:" + map);
		List<INSBAgent> list = agentDao.selectAgentByDept(map);
		

		List<INSBAgentpermission> agentpermissionList = new ArrayList<INSBAgentpermission>();
		for (INSBAgent agent : list) {
			agentpermissionList.addAll(setAgentpermissionData(agent,setid , "1"));
		}

		//正式代理人权限
		map.put("agentkind",2);
		LogUtil.info("正式代理人:" + map);
		list = agentDao.selectAgentByDept(map);
		for (INSBAgent agent : list) {
			agentpermissionList.addAll(setAgentpermissionData(agent,setid , "2"));

		}

		updateByDeptinnercode(setid,deptinnercode,operator,1);//更新试用代理人
		updateByDeptinnercode(setid,deptinnercode,operator,2);//更新正式代理人
		agentpermissionDao.deleteByDeptinnercode(deptinnercode);
		agentpermissionDao.insertBatch(agentpermissionList);
		return true;
	}
	
	private int updateByDeptinnercode(String setid, String deptinnercode, String operator, int agentkind) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtil.isEmpty(deptinnercode)) {
			LogUtil.info("empty deptinnercode");
			return 0;
		}
		if (StringUtil.isEmpty(setid)) {
			LogUtil.info("empty setid");
			return 0;
		}
		if (StringUtil.isEmpty(operator)) {
			LogUtil.info("empty operator");
			return 0;
		}
		//用户种类 1-试用  2-正式  3-渠道
		if (agentkind == 0) {
			LogUtil.info("agentkind == 0");
			return 0;
		}
		
		map.put("deptinnercode",deptinnercode);
		map.put("agentkind",agentkind);
		map.put("setid",setid);
		map.put("operator",operator);
		return agentDao.updateByDeptinnercode(map);
	}

	/**
	 * 列出该部门下的权限包
	 * @param deptid
	 * @return
     */
	@Override
	public List<INSBPermissionset> selectPermissionset(String deptid) {
		if (StringUtil.isEmpty(deptid)) return null;
		INSCDept dept = inscDeptDao.selectById(deptid);
		if (dept == null) return null;
		String deptinnercode = dept.getDeptinnercode();
		if (StringUtil.isEmpty(deptinnercode)) return null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deptinnercode", deptinnercode);
		LogUtil.info(map);
		return insbPermissionsetDao.selectByDeptinnercode(map);
	}

	/**
	 * 设置代理人权限
	 * @param agent
	 * @param setid
	 * @param kind 1 试用 2 正式 3 渠道
	 */
	private void setAgentpermission(INSBAgent agent, String setid, String kind) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("permissionsetId",setid);
		queryMap.put("istry",kind);

		List<Map<String, Object>> permissionList = insbPermissionDao
				.selectSetPermission(queryMap);
		if(permissionList != null && !permissionList.isEmpty()) {
			for (Map<String, Object> m : permissionList) {
				if(m.get("allotid") == null) {
					continue;
				}
				INSBAgentpermission agentpermission = new INSBAgentpermission();
				agentpermission.setAgentid(agent.getId());
				agentpermission.setPermissionid((String) m.get("id"));
				agentpermission.setPermissionname((String) m.get("permissionpath"));
				Integer i = m.get("num") == null ? null: (Integer) m.get("num");
				agentpermission.setNum(i);
				agentpermission.setCreatetime(new Date());
				agentpermission.setValidtimeend(m.get("validtimeend") == null ? null : (Date) m.get("validtimeend"));
				agentpermission.setValidtimestart(m.get("validtimestart") == null ? null : (Date) m.get("validtimestart"));
				agentpermission.setSurplusnum(i);
				agentpermissionDao.insert(agentpermission);
			}
		}
	}

	/**
	 * 设置代理人权限数据
	 * @param agent
	 * @param setid
	 * @param kind 1 试用 2 正式 3 渠道
	 */
	private List<INSBAgentpermission> setAgentpermissionData(INSBAgent agent, String setid, String kind) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		List<INSBAgentpermission> addList = new ArrayList<INSBAgentpermission>();
		queryMap.put("permissionsetId",setid);
		queryMap.put("istry",kind);

		List<Map<String, Object>> permissionList = insbPermissionDao.selectSetPermission(queryMap);
		if(permissionList != null && !permissionList.isEmpty()) {
			for (Map<String, Object> m : permissionList) {
				if(m.get("allotid") == null) {
					continue;
				}
				INSBAgentpermission agentpermission = new INSBAgentpermission();
				agentpermission.setId(UUIDUtils.create());
				agentpermission.setAgentid(agent.getId());
				agentpermission.setPermissionid((String) m.get("id"));
				agentpermission.setPermissionname((String) m.get("permissionpath"));
				Integer i = m.get("num") == null ? null: (Integer) m.get("num");
				agentpermission.setNum(i);
				agentpermission.setCreatetime(new Date());
				agentpermission.setValidtimeend(m.get("validtimeend") == null ? null : (Date) m.get("validtimeend"));
				agentpermission.setValidtimestart(m.get("validtimestart") == null ? null : (Date) m.get("validtimestart"));
				agentpermission.setSurplusnum(null);
				addList.add(agentpermission);
			}
		}
		return addList;
	}
	
//	@Override
	
	/**
	 * 导入批量代理人
	 * @param permissionsetid
	 * @param agentInfo
	 * @return
	 * @throws Exception 
	 */
	public List<ExportPermissionInfoVo> importAgentRelationPermission(String permissionsetid, List<String> agentInfoList,String user) throws Exception {
		List<ExportPermissionInfoVo> exportList = new ArrayList<ExportPermissionInfoVo>();
		if( agentInfoList == null || agentInfoList.size() < 1) {
			throw new Exception("关联的工号或手机号为空");
		} else if( agentInfoList.size() > 200 ) {
			throw new Exception("关联的工号或手机号大于200条");
		}
		INSBPermissionset insbPermissionset = insbPermissionsetDao.selectById(permissionsetid);
		if( insbPermissionset == null ) {
			throw new Exception("权限包信息未找到");
		}
		int id = 0;
		for( String param : agentInfoList ) {
			id++;
			ExportPermissionInfoVo exportVo = new ExportPermissionInfoVo();
			exportVo.setId(id);
			exportVo.setResult(1);
			exportVo.setReason("成功");
			try{
				INSBAgent insbAgent = insbAgentDao.selectAgentByAgentCode(param);
				// 验证代理人是否能关联权限包
				if( insbAgent == null ) {
					exportVo.setJobnum(param);
					exportVo.setMobile(param);
					exportVo.setResult(0);
					exportVo.setReason("找不到该账号");
					exportList.add(exportVo);
					continue;
				}
				String checkResult = checkDept(insbPermissionset, insbAgent);
				if( "1".equals(checkResult) ) {
					checkResult = checkUserType(insbPermissionset, insbAgent);
					if( !"1".equals(checkResult) ) {
						exportVo.setResult(0);
						exportVo.setReason(checkResult);
					}
				} else {
					exportVo.setResult(0);
					exportVo.setReason(checkResult);
				}
				exportVo.setJobnum(insbAgent.getJobnum());
				exportVo.setMobile(insbAgent.getPhone());
				if( exportVo.getResult() == 1 ) {
					try {
						// 将代理人与权限包进行关联
						insbAgent.setSetid(permissionsetid);
						insbAgent.setOperator(user);
						insbAgent.setModifytime(new Date());
						agentDao.updateById(insbAgent);
						List<INSBPermissionallot> allotList=insbPermissionallotDao.selectListBySetId(permissionsetid);
						List<INSBAgentpermission> list=agentpermissionDao.selectByAgentId(insbAgent.getId());
						List<String> permissionId=new ArrayList<String>();
						for(INSBAgentpermission agentmission:list){
							permissionId.add(agentmission.getPermissionid());
						}
						List<INSBPermissionallot> waitUpdate=new ArrayList<INSBPermissionallot>();
						List<INSBPermissionallot> waitInsert=new ArrayList<INSBPermissionallot>();
						for(INSBPermissionallot p:allotList){
							if(permissionId.contains(p.getPermissionid())){
								waitUpdate.add(p);
							}else{
								waitInsert.add(p);
							}
						}
						List<INSBAgentpermission> toInsert=new ArrayList<INSBAgentpermission>();
						for(INSBPermissionallot p:waitInsert){
							INSBAgentpermission insbagentpermission=new INSBAgentpermission();
							insbagentpermission.setId(UUIDUtils.create());
							insbagentpermission.setAbort(p.getAbort());
							insbagentpermission.setAgentid(insbAgent.getId());
							insbagentpermission.setPermissionid(p.getPermissionid());
							INSBPermission insbPermission=insbPermissionDao.selectById(p.getPermissionid());
							insbagentpermission.setPermissionname(insbPermission.getPermissionpath());
							insbagentpermission.setFrontstate(p.getFrontstate());
							insbagentpermission.setFunctionstate(p.getFunctionstate());
							insbagentpermission.setValidtimeend(p.getValidtimeend());
							insbagentpermission.setValidtimestart(p.getValidtimestart());
							insbagentpermission.setNum(p.getNum());
							insbagentpermission.setStatus(insbPermissionset.getStatus());
							insbagentpermission.setCreatetime(new Date());
							insbagentpermission.setModifytime(new Date());
							insbagentpermission.setOperator(user);
							insbagentpermission.setNoti(null);
							insbagentpermission.setSurplusnum(0);
							toInsert.add(insbagentpermission);
						}
						if(toInsert.size()>1){
							agentpermissionDao.insertAll(toInsert);
						}
						List<INSBAgentpermission> toUpdate=new ArrayList<INSBAgentpermission>();
						if(waitUpdate.size()>1){
						for(INSBPermissionallot p:waitUpdate){
							for(INSBAgentpermission agentper:list){
								if(p.getPermissionid().equals(agentper.getPermissionid())){
									agentper.setFrontstate(p.getFrontstate());
									agentper.setFunctionstate(p.getFunctionstate());
									agentper.setValidtimeend(p.getValidtimeend());
									agentper.setValidtimestart(p.getValidtimestart());
									agentper.setNum(p.getNum());
									agentper.setStatus(insbPermissionset.getStatus());
									agentper.setCreatetime(new Date());
									agentper.setModifytime(new Date());
									agentper.setOperator(user);
									agentper.setNoti(null);				
									agentpermissionDao.updateById(agentper);
								}
							}
						}
						}
					} catch(Exception e) {
						LogUtil.error("更新代理人权限包出现异常：" + e.getStackTrace());
						exportVo.setResult(0);
						exportVo.setReason("其他");
					}
				}
				exportList.add(exportVo);
			} catch(Exception ex) {
				LogUtil.error("查询代理人信息出现异常：" + ex.getStackTrace());
				exportVo.setJobnum(param);
				exportVo.setMobile(param);
				exportVo.setResult(0);
				exportVo.setReason("其他");
				exportList.add(exportVo);
			}
		}
		return exportList;
	}
	
	/**
	 * 判断代理人机构是否可以使用当前权限
	 * @param insbPermissionset
	 * @param insbAgent
	 * @return
	 */
	private String checkDept(INSBPermissionset insbPermissionset, INSBAgent insbAgent) {
		String checkResult = "1";
		if( insbPermissionset.getDeptid() == null ) {
			checkResult = "权限包对应的机构为空";
		} else if( insbAgent.getDeptid() == null ) {
			checkResult = "代理人对应的机构为空";
		} else {
			// 比较机构id是否一致
			if( !insbPermissionset.getDeptid().equals(insbAgent.getDeptid()) ) {
				// 如果不一致，获取inscDept的上级机构，在与权限的机构id进行比较
				INSCDept inscDept = inscDeptDao.selectParentCodeByCode(insbAgent.getDeptid());
				if( inscDept.getParentcodes().indexOf(insbPermissionset.getDeptid()) <= 0 ) {
					checkResult = "代理人所属机构与权限包的所属机构不一致或不是下属机构";
				}
			}
		}
		return checkResult;
	}
	
	/**
	 * 判断代理人用户类型与权限用户类型一致
	 * @param insbPermissionset
	 * @param insbAgent
	 * @return
	 */
	private String checkUserType(INSBPermissionset insbPermissionset, INSBAgent insbAgent) {
		String checkResult = "1";
		if( insbPermissionset.getAgentkind() == null ) {
			checkResult = "权限包信息中用户类型为空";
		} else if( insbAgent.getAgentkind() == null ) {
			checkResult = "代理人信息中用户类型为空";
		} else {
			if( insbPermissionset.getAgentkind() != insbAgent.getAgentkind() ) {
				checkResult = "代理人用户类型与权限包的用户类型不一致";
			}
		}
		return checkResult;
	}
	
	/**
	 * 导出关联结果
	 */
	public ResponseEntity<byte[]> exPortResult(String str){
		//将json型字符串转换为ExportPermissionInfoVo对象集合
		JSONArray json=JSONArray.fromObject(str);
		List<ExportPermissionInfoVo> vos=(ArrayList<ExportPermissionInfoVo>)JSONArray.toCollection(json,ExportPermissionInfoVo.class);
		LogUtil.info("导出数据的条数："+vos.size());
		//excel表，列名
		String[] arr={"序号","工号","手机号","关联结果","原因"};
		//创建一个工作簿
		HSSFWorkbook workbook=new HSSFWorkbook();
		//创建一个工作表
		HSSFSheet sheet=workbook.createSheet();
		//创建第一行
		HSSFRow row=sheet.createRow(0);
		//创建第一行的列
		HSSFCell cell=null;
		for(int i=0;i<arr.length;i++){
			cell=row.createCell(i);
			cell.setCellValue(arr[i]);
		}
		//追加数据
		for(int i=1;i<=vos.size();i++){
			HSSFRow nextRow=sheet.createRow(i);
			cell=nextRow.createCell(0);
			cell.setCellValue(vos.get(i-1).getId());
			cell=nextRow.createCell(1);
			cell.setCellValue(vos.get(i-1).getJobnum());
			cell=nextRow.createCell(2);
			cell.setCellValue(vos.get(i-1).getMobile());
			cell=nextRow.createCell(3);
			cell.setCellValue(vos.get(i-1).getResult());
			cell=nextRow.createCell(4);
			cell.setCellValue(vos.get(i-1).getReason());
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();  
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		HttpHeaders headers=new HttpHeaders();
		try {
			String fileName = new String("关联结果.xls".getBytes("UTF-8"), "iso-8859-1");
			headers.setContentDispositionFormData("attachment", fileName);  
	        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);  
	        ResponseEntity<byte[]> filebyte = new ResponseEntity<byte[]>(out.toByteArray(),headers, HttpStatus.CREATED);  
	        return filebyte;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<INSBPermissionset> querybyparentcodes(String deptid,String agentkind) {
		return insbPermissionsetDao.selectSetByKindAndDeptid(deptid, agentkind);
	}

}