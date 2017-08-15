package com.zzb.mobile.service.impl;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.service.INSCCodeService;
import com.common.ModelUtil;
import com.common.redis.Constants;
import com.common.redis.IRedisClient;
import com.zzb.cm.dao.INSBCarkindpriceDao;
import com.zzb.cm.dao.INSBOrderDao;
import com.zzb.cm.dao.INSBQuoteinfoDao;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.entity.INSBCarkindprice;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.conf.controller.vo.AgentRelatePermissionVo;
import com.zzb.conf.dao.*;
import com.zzb.conf.entity.*;
import com.zzb.conf.service.INSBPermissionallotService;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.lastinsured.LastYearPolicyInfoBean;
import com.zzb.mobile.service.APPBorderService;
import com.zzb.mobile.service.AppGeneralSettingService;
import com.zzb.mobile.service.AppPermissionService;
import com.zzb.mobile.service.INSBMyOrderService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 
 * @author Administrator
 * 权限包
 */
@Service
@Transactional
public class AppPermissionServiceImpl implements AppPermissionService{
	@Resource
	private INSBAgentDao insbAgentDao;
	@Resource
	private INSBQuotetotalinfoDao insbQuotetotalinfoDao;
	@Resource
	private INSBAgentpermissionDao iNSBAgentpermissionDao;
	@Resource
	private INSBPermissionallotService insbPermissionallotService;
	@Resource
	private IRedisClient redisClient;

	/**
	 *
	 * @param jobnum
	 * @param taskid
	 * @param permission
	 * @return  权限结果  0 有权限，1 预警，2 无权限，-1 错误
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Map<String,Object> checkPermission(String jobnum, String taskid, String permission) {

		LogUtil.info("checkPermission jobnum:"+jobnum+" taskid:" + taskid + " permission:" + permission);
		if (StringUtil.isNotEmpty(taskid) && StringUtil.isEmpty(jobnum)) {
			Map<String, Object> map = insbQuotetotalinfoDao.getAgentInfoByTaskId(taskid);
			if(map != null && !map.isEmpty() && map.get("jobnum") != null) {
				jobnum = (String) map.get("jobnum");
			}
		}

		INSBAgent agent = insbAgentDao.selectByJobnum(jobnum);
		if (agent == null) {
			agent = insbAgentDao.selectById(jobnum);
		}
		if (agent == null) {
			return setMessage(-1, null, null, null);
		}
		List<AgentRelatePermissionVo> permissionList = insbPermissionallotService.getPermissionallotByAgentId(agent.getId());
		if (permissionList == null) {
			LogUtil.info("checkPermission jobnum:"+agent.getJobnum()+" can not find permissionset.");
			return setMessage(-1, null, null, null);
		}
		//使用次数：（1）使用次数为负数时，表示无限制次数。为0时表示该功能不能使用。大于等于1（n）时，表示用户可使用次数为n  （2）输入框只能输入数字，不能为小数
		//人工报价使用次数如何计算：提交1次人工报价请求，则该用户此功能的已使用次数+1.
		for (AgentRelatePermissionVo item : permissionList) {
			if (permission.equals(item.getPermissionPath())) {
				//功能状态
				if (item.getFunctionstate() == null || item.getFunctionstate() == 0) {
					return setMessage(2, agent.getAgentkind(), item.getPermissionName(), null);
				}
				if ("pay".equals(permission) || "query".equals(permission)) {
					return setMessage(0, null, null, null);
				}
				if (item.getNum() == null || item.getNum() < 0) {//使用次数无限制时。直接返回0，并记录使用次数
					if (item.getSurplusnum() == null || item.getSurplusnum() <= 0) {
						if (StringUtil.isEmpty(item.getAgentPermissionId())) {
							insertAgentpermission (item);
							LogUtil.info("checkPermission jobnum:"+agent.getJobnum()+" 开始使用该权限计数 permission:" + permission);
							return setMessage(0, null, null, null);
						} else {
							INSBAgentpermission agentpermission = new INSBAgentpermission();
							agentpermission.setId(item.getAgentPermissionId());
							if (item.getSurplusnum() == null || item.getSurplusnum() <= 0) {
								agentpermission.setSurplusnum(1);
							} else {
								agentpermission.setSurplusnum(item.getSurplusnum() + 1);
							}

							iNSBAgentpermissionDao.updateSurplusnum(agentpermission);
							LogUtil.info("checkPermission jobnum:"+agent.getJobnum()+" 使用权限加1： permission:" + permission);
							return setMessage(0, null, null, null);
						}

					} else {
						INSBAgentpermission agentpermission = new INSBAgentpermission();
						agentpermission.setId(item.getAgentPermissionId());
						agentpermission.setSurplusnum(item.getSurplusnum() + 1);
						iNSBAgentpermissionDao.updateSurplusnum(agentpermission);
						LogUtil.info("checkPermission jobnum:"+agent.getJobnum()+" 使用权限加1： permission:" + permission);
						return setMessage(0, null, null, null);
					}
				}
				if (item.getNum() == 0) {
					return setMessage(2, agent.getAgentkind(), item.getPermissionName(), null);
				}
				if (item.getSurplusnum() == null || item.getSurplusnum() <= 0) {
					if (StringUtil.isEmpty(item.getAgentPermissionId())) {
						insertAgentpermission (item);
						LogUtil.info("checkPermission jobnum:"+agent.getJobnum()+" 开始使用该权限计数 permission:" + permission);
						return setMessage(0, null, null, null);
					} else {
						INSBAgentpermission agentpermission = new INSBAgentpermission();
						agentpermission.setId(item.getAgentPermissionId());

						if (item.getSurplusnum() == null || item.getSurplusnum() <= 0) {
							agentpermission.setSurplusnum(1);
						} else {
							agentpermission.setSurplusnum(item.getSurplusnum() + 1);
						}
						iNSBAgentpermissionDao.updateSurplusnum(agentpermission);
						LogUtil.info("checkPermission jobnum:"+agent.getJobnum()+" 使用权限加1： permission:" + permission);
						return setMessage(0, null, null, null);
					}

				}
				if (item.getSurplusnum() < item.getNum()) {
					INSBAgentpermission agentpermission = new INSBAgentpermission();
					agentpermission.setId(item.getAgentPermissionId());
					agentpermission.setSurplusnum(item.getSurplusnum() + 1);
					iNSBAgentpermissionDao.updateSurplusnum(agentpermission);
					LogUtil.info("checkPermission jobnum:"+agent.getJobnum()+" 使用权限加1： permission:" + permission);
					if ("insured".equals(permission) || "quote".equals(permission) || "renewal".equals(permission) || "underwriting".equals(permission)) {
						if (item.getWarningtimes() != null && (item.getNum()-item.getSurplusnum() < item.getWarningtimes())) {
							return setMessage(1, agent.getAgentkind(), item.getPermissionName(), item.getWarningtimes());
						}
					}
					return setMessage(0, null, null, null);
				} else {
					return setMessage(2, agent.getAgentkind(), item.getPermissionName(), null);
				}
			}
		}
		return setMessage(-1, null, null, null);
	}

	/**
	 *
	 * @param jobnum
	 * @param taskid
	 * @param permission
	 * @return  权限结果  0 有权限，1 预警，2 无权限，-1 错误
	 */
	@Override
	public Map<String,Object> findPermission(String jobnum, String taskid, String permission) {
		LogUtil.info("findPermission jobnum:"+jobnum+" taskid:" + taskid + " permission:" + permission);
		if (StringUtil.isNotEmpty(taskid) && StringUtil.isEmpty(jobnum)) {
			Map<String, Object> map = insbQuotetotalinfoDao.getAgentInfoByTaskId(taskid);
			if(map != null || map.get("jobnum") != null) {
				jobnum = (String) map.get("jobnum");
			}
		}

		INSBAgent agent = insbAgentDao.selectByJobnum(jobnum);
		if (agent == null) {
			agent = insbAgentDao.selectById(jobnum);
		}
		if (agent == null) {
			return setMessage(-1, null, null, null);
		}
		List<AgentRelatePermissionVo> permissionList = insbPermissionallotService.getPermissionallotByAgentId(agent.getId());
		if (permissionList == null) {
			LogUtil.info("findPermission jobnum:"+agent.getJobnum()+" can not find permissionset.");
			return setMessage(-1, null, null, null);
		}
		//使用次数：（1）使用次数为负数时，表示无限制次数。为0时表示该功能不能使用。大于等于1（n）时，表示用户可使用次数为n  （2）输入框只能输入数字，不能为小数
		//人工报价使用次数如何计算：提交1次人工报价请求，则该用户此功能的已使用次数+1.
		for (AgentRelatePermissionVo item : permissionList) {
			if (permission.equals(item.getPermissionPath())) {
				//功能状态
				if (item.getFunctionstate() == null || item.getFunctionstate() == 0) {
					return setMessage(2, agent.getAgentkind(), item.getPermissionName(), null);
				}
				if ("pay".equals(permission) || "query".equals(permission)) {
					return setMessage(0, null, null, null);
				}
				if (item.getNum() == null || item.getNum() < 0) {//使用次数无限制时。直接返回0，并记录使用次数
					return setMessage(0, null, null, null,item);
				}
				if (item.getNum() == 0) {
					return setMessage(2, agent.getAgentkind(), item.getPermissionName(), null);
				}
				if (item.getSurplusnum() == null || item.getSurplusnum() <= 0) {
					return setMessage(0, null, null, null,item);
				}
				if (item.getSurplusnum() < item.getNum()) {
					if ("insured".equals(permission) || "quote".equals(permission) || "renewal".equals(permission) || "underwriting".equals(permission)) {
						if (item.getWarningtimes() != null && (item.getNum()-item.getSurplusnum() < item.getWarningtimes())) {
							return setMessage(1, agent.getAgentkind(), item.getPermissionName(), item.getWarningtimes());
						}
					}
					return setMessage(0, null, null, null,item);
				} else {
					return setMessage(2, agent.getAgentkind(), item.getPermissionName(), null);
				}
			}
		}
		return setMessage(-1, null, null, null);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean resetPermission(String jobnum) {
		LogUtil.info("resetPermission jobnum" + jobnum);
		INSBAgent agent = insbAgentDao.selectByJobnum(jobnum);
		if (agent == null) {
			agent = insbAgentDao.selectById(jobnum);
		}
		if (agent == null) {
			LogUtil.info("resetPermission jobnum" + jobnum + "找不到该代理人。");
			return true;
		}
		List<INSBAgentpermission> list = iNSBAgentpermissionDao.selectByAgentId(agent.getId());
		if (list == null) {
			LogUtil.info("resetPermission jobnum" + jobnum + "代理人权限不存在。");
			return true;
		}
		for (INSBAgentpermission agentpermission : list) {

			if ("pay".equals(agentpermission.getPermissionname()) || "query".equals(agentpermission.getPermissionname())) {
				continue;
			}
			agentpermission.setSurplusnum(0);
			LogUtil.info("resetPermission jobnum" + jobnum + "重置代理人权限" + agentpermission.getPermissionname());
			iNSBAgentpermissionDao.updateSurplusnum(agentpermission);
		}
		return true;
	}

	private Map<String, Object> setMessage(Integer result, Integer agentkind, String permissionName, Integer warningtimes,AgentRelatePermissionVo item) {
		Map<String, Object> retMap = setMessage(result, agentkind, permissionName, warningtimes);
		retMap.put("AgentRelatePermissionVo", item);
		return retMap;
	}

	/**
	 *
	 * @param result 权限结果  0 有权限，1 预警，2 无权限，-1 错误
	 * @param agentkind 代理人类型 1 试用用户, 2 正式用户， 3 渠道用户
	 * @param permissionName 权限名称
	 * @param warningtimes 预警次数
     * @return
     */
	private Map<String, Object> setMessage(Integer result, Integer agentkind, String permissionName, Integer warningtimes) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("status", result);
		if (result == 0) {

		} else if (result == 1) {
			if (agentkind == null) {
				retMap.put("message", "权限包，非法代理人类型。");
				return retMap;
			}
			if (agentkind == 1) {
				retMap.put("message", permissionName+"功能剩余可使用次数已不足"+warningtimes+"次，请及时升级为正式用户。");
			} else if (agentkind == 2) {
				retMap.put("message", permissionName+"功能剩余可使用次数已不足"+warningtimes+"次，待次数用完时将会被停用，请及时出单。");
			}else if (agentkind == 3) {
				retMap.put("message", permissionName+"功能剩余可使用次数已不足"+warningtimes+"次，待次数用完时将会被停用，请及时出单。");
			} else {
				retMap.put("message", "权限包，非法代理人类型。");
			}
		} else if (result == 2) {
			if (agentkind == null) {
				retMap.put("message", "权限包，非法代理人类型。");
				return retMap;
			}
			if (agentkind == 1) {
				retMap.put("message", "抱歉，" + permissionName + "功能不对试用用户开放或试用次数已用完。马上成为正式用户，享受无限制的掌中保功能。");
			} else if (agentkind == 2) {
				retMap.put("message", "抱歉，" + permissionName + "功能已被停用或使用次数已用完，请联系当地机构进行处理。");
			}else if (agentkind == 3) {
				retMap.put("message", "抱歉，" + permissionName + "功能已被停用或使用次数已用完。");
			} else {
				retMap.put("message", "权限包，非法代理人类型。");
			}

		} else {
			retMap.put("message", "权限包错误");
		}
		return retMap;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updatePermission(LastYearPolicyInfoBean lastYearPolicyInfoBean,List<AgentRelatePermissionVo> permissionVoList, String taskid) {

		if (lastYearPolicyInfoBean == null || permissionVoList == null || permissionVoList.isEmpty()) return;

		for (AgentRelatePermissionVo item : permissionVoList) {
			if (item == null) continue;
			if (!StringUtil.isEmpty(redisClient.get(Constants.TASK, item.getPermissionPath() + taskid))) continue;
			if (("cif_own".equals(item.getPermissionPath()) && lastYearPolicyInfoBean.getCif_owncount() == 1)
				|| ("cif_renewal".equals(item.getPermissionPath()) && lastYearPolicyInfoBean.getCif_renewalcount() == 1)
				|| ("cif_social".equals(item.getPermissionPath()) && lastYearPolicyInfoBean.getCif_socialcount() == 1)) {
				if (StringUtil.isEmpty(item.getAgentPermissionId())) {
					insertAgentpermission (item);
					LogUtil.info("updatePermission jobnum:"+item.getAgentId()+" 开始使用该权限计数 permission:" + item.getPermissionPath());
					redisClient.set(Constants.TASK, item.getPermissionPath() + taskid,taskid,60*60);
				} else {
					INSBAgentpermission agentpermission = new INSBAgentpermission();
					agentpermission.setId(item.getAgentPermissionId());

					if (item.getSurplusnum() == null || item.getSurplusnum() <= 0) {
						agentpermission.setSurplusnum(1);
					} else {
						agentpermission.setSurplusnum(item.getSurplusnum() + 1);
					}
					iNSBAgentpermissionDao.updateSurplusnum(agentpermission);
					LogUtil.info("updatePermission jobnum:"+item.getAgentId()+" 使用权限加1： permission:" + item.getPermissionPath());
					redisClient.set(Constants.TASK, item.getPermissionPath() + taskid,taskid,60*60);
				}
			}
		}
	}

	private void insertAgentpermission (AgentRelatePermissionVo item) {
		if (item == null) return;
		INSBAgentpermission ap = new INSBAgentpermission();
		ap.setAgentid(item.getAgentId());
		ap.setPermissionid(item.getPermissionId());
		ap.setCreatetime(new Date());
		ap.setOperator("1");
		ap.setFunctionstate(item.getFunctionstate());
		ap.setPermissionname(item.getPermissionPath());
		ap.setNoti("开始使用该权限计数");
		ap.setStatus(item.getFunctionstate());
		ap.setNum(item.getNum());
		ap.setSurplusnum(1);
		iNSBAgentpermissionDao.insert(ap);
	}
}
