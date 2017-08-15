package com.zzb.conf.service.impl;
 
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.entity.INSCUser;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.controller.vo.AgentRelatePermissionVo;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.dao.INSBAgentpermissionDao;
import com.zzb.conf.dao.INSBPermissionDao;
import com.zzb.conf.dao.INSBPermissionallotDao;
import com.zzb.conf.dao.INSBPermissionsetDao;
import com.zzb.conf.dao.INSCDeptpermissionsetDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBAgentpermission;
import com.zzb.conf.entity.INSBPermission;
import com.zzb.conf.entity.INSBPermissionallot;
import com.zzb.conf.entity.INSBPermissionset;
import com.zzb.conf.entity.INSCDeptpermissionset;
import com.zzb.conf.service.INSBPermissionallotService;

@Service
@Transactional
public class INSBPermissionallotServiceImpl extends
		BaseServiceImpl<INSBPermissionallot> implements
		INSBPermissionallotService {
	@Resource
	private INSBPermissionallotDao insbPermissionallotDao;

	@Resource
	private INSBPermissionsetDao insbPermissionsetDao;
	
	@Resource
	private INSBAgentDao insbAgentDao;
	
	@Resource
	private INSBAgentpermissionDao insbAgentpermissionDao;
	
	@Resource
	private INSBPermissionDao insbPermissionDao;
	
	@Resource
	private INSCDeptpermissionsetDao inscDeptpermissionsetDao;
	
	@Override
	protected BaseDao<INSBPermissionallot> getBaseDao() {
		return insbPermissionallotDao;
	}

	@Override
	public String saveOrUpdate(INSBPermissionallot allot , INSCUser user) {
		String id = "";
		// 进行日期的转化
		try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//			if (allot.getValidtimeendstr() != null
//					&& !"".equals(allot.getValidtimeendstr())) {
//				try {
//					allot.setValidtimeend(format.parse(allot
//							.getValidtimeendstr().toString()));
//				} catch (ParseException e) {
//					e.printStackTrace();
//				}
//			}
//			if (allot.getValidtimestartstr() != null
//					&& !"".equals(allot.getValidtimestartstr())) {
//				try {
//					allot.setValidtimestart(format.parse(allot
//							.getValidtimestartstr().toString()));
//				} catch (ParseException e) {
//					e.printStackTrace();
//				}
//			}
			if (allot.getId().length() > 0) {
				allot.setOperator(user.getUsercode());
				allot.setModifytime(new Date());
				insbPermissionallotDao.updateById(allot);		
				id = "1";
			} else {
				allot.setOperator(user.getUsercode());
				allot.setCreatetime(new Date());				
				id = insbPermissionallotDao.insertDataReturnId(allot);
			}
		} catch (Exception e) {
			id="";
			e.printStackTrace();
		}
		return id;
	}

	/**
	 * 通过代理人id，获取代理相关的权限包下的权限
	 * @param agentId
	 * @return
	 */
	@Override
	public List<AgentRelatePermissionVo> getPermissionallotByAgentId(String agentId) {

		INSBPermissionset insbPermissionset = getPermissionsetByAgentId(agentId);
		if( insbPermissionset == null ) {
			return null;
		}
		List<INSBPermissionallot> insbPermissionallotList = insbPermissionallotDao.selectListBySetId(insbPermissionset.getId());

		List<AgentRelatePermissionVo> relateList = null;
		// 将已使用次数写到权限表的已使用次数中
		if( insbPermissionallotList != null && insbPermissionallotList.size() > 0 ) {
			relateList = new ArrayList<AgentRelatePermissionVo>();
			for( INSBPermissionallot insbPermissionallot : insbPermissionallotList ) {
				AgentRelatePermissionVo vo = new AgentRelatePermissionVo();				
				vo.setAgentId(agentId);
//				vo.setAgentJobnum(insbAgent.getJobnum());
//				vo.setAgentName(insbAgent.getName());
//				vo.setAgentPhone(insbAgent.getPhone());
				vo.setPermissionId(insbPermissionallot.getPermissionid());
				vo.setPermissionName(insbPermissionallot.getPermissionname());
				vo.setNum(insbPermissionallot.getNum());
				vo.setPermissionSetId(insbPermissionset.getId());
				vo.setPermissionSetName(insbPermissionset.getSetname());
				vo.setPermissonAllotId(insbPermissionallot.getId());
				vo.setWarningtimes(insbPermissionallot.getWarningtimes());
				vo.setFunctionstate(insbPermissionallot.getFunctionstate());
				List<INSBPermission> insbPermissionList = insbPermissionDao.selectPermissionByIstry();
				for( INSBPermission insbPermission : insbPermissionList ) {
					if( insbPermission.getId().equals(insbPermissionallot.getPermissionid()) ) {
						vo.setPermissionPath(insbPermission.getPermissionpath());
						break;
					}
				}
				List<INSBAgentpermission> insbAgentpermissionList = insbAgentpermissionDao.selectByAgentId(agentId);
				if( insbAgentpermissionList !=null && insbAgentpermissionList.size() > 0 ) {
					for( INSBAgentpermission insbAgentpermission : insbAgentpermissionList ) {
						// 如果权限id一致
						if( insbPermissionallot.getPermissionid().equals(insbAgentpermission.getPermissionid()) ) {
							vo.setSurplusnum(insbAgentpermission.getSurplusnum());
							vo.setAgentPermissionId(insbAgentpermission.getId());
							break;
						}
					}
				}
				relateList.add(vo);
			}
		}
		return relateList;
	}

	@Override
	public INSBPermissionset getPermissionsetByAgentId(String agentId) {
		INSBAgent insbAgent = insbAgentDao.selectById(agentId);
		
		if(insbAgent == null) {
			LogUtil.error("getPermissionsetByAgentId找不到代理人：agentId：" + agentId);
			return null;
		}
		INSBPermissionset insbPermissionset = null;
		// 先获取代理人关联的权限包
		if( insbAgent.getSetid() != null && !"".equals(insbAgent.getSetid()) ) {
			insbPermissionset = insbPermissionsetDao.selectById(insbAgent.getSetid());
			// 权限包不为空且权限包的状态为开启
			if( insbPermissionset != null && insbPermissionset.getStatus() == 2 ) {
				// 如果权限包和代理人的用户类型不一样，将权限包置空
				if( insbPermissionset.getAgentkind() != insbAgent.getAgentkind() ) {
					insbPermissionset = null;
				}
			} else {
				insbPermissionset = null;
			}
		}
		// 如果代理人未关联权限包，通过代理人的机构获取权限包
		if (StringUtil.isNotEmpty(insbAgent.getTeamcode())) {
			List<INSCDeptpermissionset> dpsetList = inscDeptpermissionsetDao.selectDeptByDeptid(insbAgent.getTeamcode());
			insbPermissionset = getPermissionset(insbPermissionset, insbAgent, dpsetList);
			if (insbPermissionset != null) return insbPermissionset;
		}
		if (StringUtil.isNotEmpty(insbAgent.getDeptid())) {
			List<INSCDeptpermissionset> dpsetList = inscDeptpermissionsetDao.selectDeptByDeptid(insbAgent.getDeptid());
			insbPermissionset = getPermissionset(insbPermissionset, insbAgent, dpsetList);
		}

		return insbPermissionset;
	}
	private INSBPermissionset getPermissionset(INSBPermissionset insbPermissionset, INSBAgent insbAgent, List<INSCDeptpermissionset> dpsetList) {
		if( insbPermissionset == null ) {
			// 获取第一条权限包数据
			if( dpsetList != null && dpsetList.size() > 0 ) {
				for (INSCDeptpermissionset deptpermissionset : dpsetList) {
					if (deptpermissionset == null) {
						continue;
					}
					//1-试用  2-正式  3-渠道
					if( insbAgent.getAgentkind() == 2 ) {
						if(StringUtil.isNotEmpty(deptpermissionset.getFormalsetid())) {
							INSBPermissionset temp = insbPermissionsetDao.selectById(deptpermissionset.getFormalsetid());
							if (temp == null || temp.getStatus() ==null || temp.getStatus() != 2) {
								continue;
							}
							if (temp.getAgentkind() != null && temp.getAgentkind() == insbAgent.getAgentkind()) {
								insbPermissionset =temp;
								break;
							}

						}
					} else if( insbAgent.getAgentkind() == 3 ) {
						if(StringUtil.isNotEmpty(deptpermissionset.getChannelsetid())) {
							INSBPermissionset temp = insbPermissionsetDao.selectById(deptpermissionset.getChannelsetid());
							if (temp == null || temp.getStatus() ==null || temp.getStatus() != 2) {
								continue;
							}
							if (temp.getAgentkind() != null && temp.getAgentkind() == insbAgent.getAgentkind()) {
								insbPermissionset =temp;
								break;
							}
						}
					} else {
						if(StringUtil.isNotEmpty(deptpermissionset.getTrysetid())) {
							INSBPermissionset temp = insbPermissionsetDao.selectById(deptpermissionset.getTrysetid());
							if (temp == null || temp.getStatus() ==null || temp.getStatus() != 2) {
								continue;
							}
							if (temp.getAgentkind() != null && temp.getAgentkind() == insbAgent.getAgentkind()) {
								insbPermissionset =temp;
								break;
							}
						}
					}
				}
			}
		}

		return insbPermissionset;
	}
}