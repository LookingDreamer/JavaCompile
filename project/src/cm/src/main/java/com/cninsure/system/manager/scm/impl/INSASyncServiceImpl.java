package com.cninsure.system.manager.scm.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cninsure.core.utils.*;
import com.common.redis.Constants;
import com.common.redis.IRedisClient;
import org.springframework.stereotype.Service;

import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.manager.scm.INSASyncService;
import com.cninsure.system.manager.scm.INSCDeptorProviderSyncingService;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBBwagent;
import com.zzb.conf.entity.OFUser;
import com.zzb.conf.manager.scm.INSBAgentSyncService;
import com.zzb.conf.manager.scr.INSBBwagentManager;
@Service
public class INSASyncServiceImpl  implements INSASyncService {
	
	@Resource
	private INSBBwagentManager insbBwagentManager;
	
	@Resource
	private INSCDeptorProviderSyncingService inscDeptsyncingService;
	
	@Resource
    private INSBAgentSyncService insbAgentSyncService;
	private static final String AGENT_SYNC_COUNT = "AGENT_SYNC_COUNT";
	private static final String AGENT_SYNC_PROCESS = "AGENT_SYNC_PROCESS";
	private static final int EXPIRED_TIME =60*60;
	
	//同步状态
	//private static long syncCount = -1L;
	//private static long syncProcess = -1L;
	@Resource
	private IRedisClient redisClient;
		
	public long getSyncCount() {
		if (redisClient.get(Constants.CM_SYNC, AGENT_SYNC_COUNT) == null) {
			return -1;
		}
		return Long.valueOf((String)redisClient.get(Constants.CM_SYNC, AGENT_SYNC_COUNT));
	}

	public long getSyncProcess() {
		if (redisClient.get(Constants.CM_SYNC, AGENT_SYNC_PROCESS) == null) {
			return -1;
		}
		return Long.valueOf((String)redisClient.get(Constants.CM_SYNC, AGENT_SYNC_PROCESS));
	}
	// TODO 新增同步 按代理人工号
	@Override
	public Map<String, Object> getAgentData(String operator, String agentcode) {
		Map<String, Object> map = new HashMap<String, Object>();
		redisClient.set(Constants.CM_SYNC, AGENT_SYNC_COUNT,0, EXPIRED_TIME);
		try {
			List<INSBBwagent> bwaList = insbBwagentManager.getBwagentData(null, agentcode,null);
			if (bwaList != null && bwaList.size() > 0) {
				redisClient.set(Constants.CM_SYNC, AGENT_SYNC_COUNT,bwaList.size(), EXPIRED_TIME);
				getResultOfAgentToCm(bwaList, "byCode");
				map.put("success", true);
				map.put("returnMsg", "同步成功,请点击同步状态查询按钮,查看是否机构也已经同步到最新的时间。");
			} else {
				map.put("success", false);
				map.put("returnMsg", "没有需要同步的代理人数据！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(e);
			map.put("success", false);
			map.put("returnMsg", "代理人数据同步失败，数据同步时发生了异常.");
		} finally {
			redisClient.set(Constants.CM_SYNC, AGENT_SYNC_COUNT,-1);
			redisClient.set(Constants.CM_SYNC, AGENT_SYNC_PROCESS,-1);
		}
		return map;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zzb.conf.service.INSBAgentService#getAgentData(java.lang.String)
	 */
	@Override
	public Map<String, Object> getAgentDataorOrg(String operator,String orgCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		Date syncdate = new Date();
		redisClient.set(Constants.CM_SYNC, AGENT_SYNC_COUNT,0,EXPIRED_TIME);
		try {
			Date maxSyncdate = inscDeptsyncingService.getMaxSyncdate(Integer.valueOf(2));
			String maxSyncdateStr = null;
			if (maxSyncdate != null) {
				maxSyncdateStr = DateUtil.toDateTimeString(maxSyncdate);
			}
			List<INSBBwagent> bwaList = null;
			if(!StringUtil.isEmpty(orgCode)){//根据orgCode查询
				bwaList = insbBwagentManager.getBwagentData( maxSyncdateStr, null,orgCode);
			}else{
				bwaList = insbBwagentManager.getBwagentData( maxSyncdateStr, null,null);
			}
			if (bwaList != null && bwaList.size() > 0) {
				redisClient.set(Constants.CM_SYNC, AGENT_SYNC_COUNT,bwaList.size(), EXPIRED_TIME);
				getResultOfAgentToCm(bwaList, "byOrg");
				map.put("success", true);
				map.put("returnMsg", "恭喜您成功同步了" + bwaList.size() + "条代理人数据！");
			} else {
				map.put("success", false);
				map.put("returnMsg", "没有需要同步的代理人数据！");
			}
			if(StringUtil.isEmpty(orgCode)){//同步单个机构代理人或者根据工号同步代理人不更新同步数据时间，否则会丢失同步时间之前未同步的数据
				inscDeptsyncingService.saveOrgagentlog2(operator, true, map, syncdate);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(e);
			map.put("success", false);
			map.put("returnMsg", "代理人数据同步失败，数据同步时发生了异常.");
			inscDeptsyncingService.saveOrgagentlog2(operator, false, map, syncdate);
		} finally {
			redisClient.expire(Constants.CM_SYNC, AGENT_SYNC_COUNT,0);
			redisClient.expire(Constants.CM_SYNC, AGENT_SYNC_PROCESS,0);
		}
		return map;
	}
	
	
	/**
	 * 将代理人数据同步到CM的INSBAGENT表中
	 * 
	 * @param bwaList
	 * @return
	 * @throws Exception
	 */
	private boolean getResultOfAgentToCm(List<INSBBwagent> bwaList, String flag)
			throws Exception {
		boolean result = false;
		INSBAgent agent = null;
		INSBAgent selectAgent = null;
		INSBBwagent bwagent = null;
		OFUser ofuser;
		for (int i = 0; i < bwaList.size(); i++) {
			redisClient.set(Constants.CM_SYNC, AGENT_SYNC_PROCESS,i+1, EXPIRED_TIME);
			bwagent = bwaList.get(i);
			LogUtil.info("同步的核心代理人信息：" + JsonUtil.getJsonString(bwagent));
			//若是同一个代理人同步过来有多条数据则取在职状态的数据(例如在A地离职后在B地工作->工作地点转移)
			if ("byCode".equals(flag) && bwaList.size() > 1 && StringUtil.isNotEmpty(bwagent.getAgentstate()) && bwagent.getAgentstate().compareTo("02") > 0) {
				LogUtil.info(bwagent.getAgentcode() + "同步过来的数据有两条，该记录为离职状态不做更新操作");
				continue;
			}
			agent = new INSBAgent();
			ofuser = new OFUser();
			agent.setNoti("");
			agent.setId(UUIDUtils.random());
			agent.setName(bwagent.getName());
			agent.setSex(getSexTypebyHxType(bwagent.getSex().trim()));
			agent.setJobnum(bwagent.getAgentcode());
			agent.setJobnumtype(Integer.valueOf(2));
			//agent.setPhone(bwagent.getMobile());
			agent.setEmail(bwagent.getEmail());
			agent.setDeptid(bwagent.getMgmtdivision());
			if(StringUtil.isEmpty(bwagent.getMgmtdivision())){//如果这个字段为空，则跳过
				LogUtil.info(bwagent.getAgentcode()+"=code代理人部门字段为空，则跳过此代理人同步");
				continue;
			}
			/*if (StringUtil.isNotEmpty(bwagent.getAgentgrade())
					&& bwagent.getAgentgrade().trim().length() > 1) {
				agent.setAgentlevel(Integer.valueOf(bwagent.getAgentgrade()
						.trim().substring(1)));
			}*/
			agent.setAgentcode(bwagent.getAgentcode());
			//agent.setAgentstatus(1);//直接修改成启用，同步时都是有效的代理人数据，无效停用的数据不会被同步过来
			//agentstate<=‘02’为在职用户，agentstate>‘02’为离职用户
			if (StringUtil.isNotEmpty(bwagent.getAgentstate()) && bwagent.getAgentstate().compareTo("02") > 0) {
				agent.setAgentstatus(2);
			} else {
				agent.setAgentstatus(1);
			}
			agent.setApprovesstate(3);
			// if(StringUtil.isNotEmpty(bwagent.getAgentkind())){
			// agent.setAgentkind(Integer.valueOf(bwagent.getAgentkind()));
			// }
			agent.setAgentkind(2);
			//agent.setMobile(bwagent.getMobile());
			String pwd = "123456";
			if (StringUtil.isNotEmpty(bwagent.getIdno())
					&& bwagent.getIdno().trim().length() >= 6) {
				pwd = bwagent.getIdno().trim()
						.substring(bwagent.getIdno().trim().length() - 6);
			}
			agent.setPwd(StringUtil.md5Base64(pwd));
			ofuser.setPlainPassword(StringUtil.md5Base64(pwd));
			agent.setIstest(Integer.valueOf(2));
			//agent.setReferrer(bwagent.getIntroagency());
			agent.setCgfns(bwagent.getQuacertifno());
			agent.setLicenseno(bwagent.getPracertifno());
			agent.setIdno(bwagent.getIdno());
			agent.setIdnotype(getIdnoTypebyHxType(bwagent.getIdnotype().trim()));

			agent.setCreatetime(bwagent.getMaketime());
			agent.setRegistertime(bwagent.getMaketime());
			agent.setModifytime(bwagent.getModifytime());
			agent.setAddress(bwagent.getHomeaddress());
			if (StringUtil.isNotEmpty(bwagent.getAgentgroup())) {
				agent.setTeamcode(bwagent.getAgentgroup());
				INSCDept teamDept = insbAgentSyncService.selectByComcode(bwagent
						.getAgentgroup());
				if (teamDept != null
						&& StringUtil.isNotEmpty(teamDept.getComname())) {
					agent.setTeamname(teamDept.getComname());
				}
				if (bwagent.getAgentgroup().trim().length() > 4) {
					String platformcode = bwagent.getAgentgroup().trim()
							.substring(0, 4);
					for (int j = 0; j < 6; j++) {
						platformcode += "0";
					}
					agent.setPlatformcode(platformcode);

					INSCDept platDept = insbAgentSyncService.selectByComcode(platformcode);
					if (platDept != null
							&& StringUtil.isNotEmpty(platDept.getComname())) {
						agent.setPlatformname(platDept.getComname());
					}
				}
			}
			selectAgent = null;
			System.out.println("====" + agent.getJobnum() + ":"
					+ agent.getDeptid());
			/*
			 * if (StringUtil.isNotEmpty(agent.getJobnum()) &&
			 * StringUtil.isNotEmpty(agent.getDeptid())) { selectAgent =
			 * insbAgentSyncService.getAgent( agent.getJobnum(),
			 * agent.getDeptid()); if (selectAgent != null) {
			 * insbAgentSyncService.updateAgent(getAgent(selectAgent, agent)); }
			 * else { insbAgentSyncService.saveAgent(agent); } }
			 */

			if (StringUtil.isNotEmpty(agent.getJobnum())) {
				selectAgent = insbAgentSyncService.getAgent(agent.getJobnum());
				if (selectAgent != null) {
					insbAgentSyncService.updateAgent(insbAgentSyncService.getAgent(selectAgent, agent));
					// 添加默认权限
					// getpermissionListByPage(null, null, getAgent(selectAgent,
					// agent).getId());
				} else {
					insbAgentSyncService.saveAgent(agent);
					// getpermissionListByPage(null, null, agent.getId());
					// 添加默认权限
				}
			}

			if (agent != null && StringUtil.isNotEmpty(agent.getAgentcode())) {
				OFUser ofuserBean = insbAgentSyncService.queryByUserName(agent
						.getAgentcode());
				if (ofuserBean != null) {
					ofuserBean.setName(agent.getName());
					ofuserBean.setModificationDate(new Date().getTime());
					insbAgentSyncService.updateByUserName(ofuserBean);
				} else {
					ofuser.setUsername(agent.getAgentcode());
					ofuser.setName(agent.getName());
					ofuser.setCreationDate(new Date().getTime());
					ofuser.setModificationDate(new Date().getTime());
					insbAgentSyncService.insertOFUser(ofuser);
				}
			}

		}
		result = true;
		return result;
	}
	/**
	 * cm: 0 男 1 女 核心： 1 男性 2 女性 9 未说明的性别
	 * 
	 * @param hxType
	 * @return
	 */
	public String getSexTypebyHxType(String hxType) {
		if (StringUtil.isEmpty(hxType)) {
			return "0";
		} else {
			switch (hxType) {
			case "1":
				return "0";
			case "2":
				return "1";
			default:
				return "0";
			}
		}
	}
	/**
	 * 核心证件类型需要转换 CM: 0 身份证 1 户口本 2 驾照 3 军官证/士兵证 4 护照 5 港澳回乡证/台胞证 6 组织代码证 7 其他证件
	 * 8 社会信用代码证 核心： 01 居民身份证 02 居民户口簿 03 驾驶证 04 军官证 05 士兵证 06 军官离退休证 07 中国护照 08
	 * 异常身份证 09 港澳通行证 10 台湾通行证 11 回乡证 12 出生证 13 家长证件 51 外国护照 52 旅行证 53 居留证件 71
	 * 组织机构代码证 72 税务登记证 73 营业执照 98 车牌号 99 其他证件
	 */
	public String getIdnoTypebyHxType(String hxType) {
		if (StringUtil.isEmpty(hxType)) {
			return "7";
		} else {
			switch (hxType) {
			case "01":
				return "0";
			case "02":
				return "1";
			case "03":
				return "2";
			case "04":
				return "3";
			case "05":
				return "3";
			case "06":
				return "3";
			case "07":
				return "4";
			case "08":
				return "0";
			case "09":
				return "5";
			case "10":
				return "5";
			case "11":
				return "5";
			case "12":
				return "7";
			case "13":
				return "7";
			case "51":
				return "7";
			case "52":
				return "7";
			case "53":
				return "7";
			case "71":
				return "6";
			case "72":
				return "6";
			case "73":
				return "6";
			case "98":
				return "7";
			case "99":
				return "7";
			default:
				return "7";
			}
		}
	}
	
	public static void main(String[] args) {
		String getIdno = "231026197502156128";
		String pwd = "";
		if (StringUtil.isNotEmpty(getIdno)
				&& getIdno.trim().length() >= 6) {
			pwd = getIdno.trim()
					.substring(getIdno.trim().length() - 6,getIdno.trim().length());
			System.out.println(pwd);
		}
		System.out.println(StringUtil.md5Base64(pwd));
		System.out.println(pwd);
		System.out.println(StringUtil.md5Base64("123456"));
	}
}
