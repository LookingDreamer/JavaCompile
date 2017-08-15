package com.zzb.conf.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.cninsure.core.utils.DateUtil;
import com.zzb.mobile.service.SMSClientService;
import com.zzb.mobile.service.UserCenterService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.system.dao.INSBOrgagentlogDao;
import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.dao.impl.INSBServiceUtil;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.entity.LdapAgentBean;
import com.cninsure.system.entity.LdapOrgBean;
import com.cninsure.system.ldap.LdapAgentManager;
import com.cninsure.system.ldap.LdapOrgManager;
import com.cninsure.system.service.INSCCodeService;
import com.cninsure.system.service.INSCDeptService;
import com.common.redis.Constants;
import com.common.redis.IRedisClient;
import com.zzb.cm.controller.vo.CertificationVo;
import com.zzb.cm.dao.INSBCarinfoDao;
import com.zzb.cm.dao.INSBOrderDao;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.entity.INSBFilelibrary;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.cm.service.INSBFilelibraryService;
import com.zzb.conf.controller.vo.AgentRelatePermissionVo;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.dao.INSBAgentpermissionDao;
import com.zzb.conf.dao.INSBAgentproviderDao;
import com.zzb.conf.dao.INSBAgreementDao;
import com.zzb.conf.dao.INSBAgreementscopeDao;
import com.zzb.conf.dao.INSBCertificationDao;
import com.zzb.conf.dao.INSBChannelDao;
import com.zzb.conf.dao.INSBItemprovidestatusDao;
import com.zzb.conf.dao.INSBPermissionDao;
import com.zzb.conf.dao.INSBPermissionallotDao;
import com.zzb.conf.dao.INSBPermissionsetDao;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.dao.INSBProviderDao;
import com.zzb.conf.dao.OFUserDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBAgentpermission;
import com.zzb.conf.entity.INSBAgentprovider;
import com.zzb.conf.entity.INSBBwagent;
import com.zzb.conf.entity.INSBCertification;
import com.zzb.conf.entity.INSBPermission;
import com.zzb.conf.entity.INSBPermissionallot;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.entity.OFUser;
import com.zzb.conf.entity.INSBChannel;
import com.zzb.conf.manager.scm.INSBAgentSyncService;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.extra.service.INSBMarketActivityService;
import com.zzb.extra.service.WxMsgTemplateService;
import com.zzb.ldap.LdapMd5;
import com.zzb.mobile.entity.AgentUpdatePwdVo;

import net.sf.json.JSONObject;
 
@Service
@Transactional
public class INSBAgentServiceImpl extends BaseServiceImpl<INSBAgent> implements
		INSBAgentService {
	private static Logger logger = Logger.getLogger(INSBAgentServiceImpl.class);
	@Resource
	private INSBAgentDao insbAgentDao;
	@Resource
	private OFUserDao ofUserDao;
	@Resource
	private INSBPermissionDao insbPermissionDao;
	@Resource
	private INSBProviderDao insbProviderDao;
	@Resource
	private INSBAgentpermissionDao agentpermissionDao;
	@Resource
	private INSBAgentproviderDao agentproviderDao;
	@Resource
	private INSBQuotetotalinfoDao insbQuotetotalinfoDao;
	@Resource
	private INSCCodeService inscCodeService;
	@Resource
	private INSCCodeDao inscCodeDao;
	@Resource
	private INSBAgreementDao agreementDao;
	@Resource
	private INSBAgreementscopeDao agreementscopeDao;
	@Resource
	private INSBPermissionallotDao permissionallotDao;
	@Resource
	private INSBItemprovidestatusDao itemprovidestatusDao;
	@Resource
	private INSBCertificationDao certificationDao;
	@Resource
	private INSBFilelibraryService filelibraryService;
	@Resource
	private INSBServiceUtil serviceUtil;
	@Resource
	private INSCDeptDao inscDeptDao;
	@Resource
	private INSBCarinfoDao insbCarinfoDao;
	@Resource
	private INSBPolicyitemDao policyitemDao;
	@Resource
	private INSBAgentSyncService insbAgentSyncService;

	@Resource
	private INSBMarketActivityService insbMarketActivityService;

	@Resource
	private WxMsgTemplateService wxMsgTemplateService;

	@Resource
	private INSCDeptService inscDeptService;

	@Resource
	private INSBOrgagentlogDao insbOrgagentlogDao;
	@Resource
	private INSBAgentDao agentDao;
	
	@Resource
	private INSBOrderDao orderDao;

	@Resource
	private INSBPermissionsetDao permissionsetDao;

	@Resource
	private INSBAgentpermissionDao insbAgentpermissionDao;
	
	@Resource
	private INSBChannelDao insbChannelDao;

	@Resource
	private UserCenterService userCenterService;
	
	@Resource
	private IRedisClient redisClient;

	@Resource
	private SMSClientService smsClientService;

	@Override
	protected BaseDao<INSBAgent> getBaseDao() {
		return insbAgentDao;
	}
	@Resource
	private INSBPermissionallotDao insbPermissionallotDao;
	
	// 同步状态
	private static long syncCount = -1L;
	private static long syncProcess = -1L;

	public static long getSyncCount() {
		return syncCount;
	}

	public static long getSyncProcess() {
		return syncProcess;
	}

	@Override
	public Map<String, Object> getAgentListPage(Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();
		/* List<INSBAgent> agentList = insbAgentDao.selectListPage(map); */
		List<INSBAgent> agentList = insbAgentDao.selectListPageByDeptIds(map);
		// TODO 确定后加入字典表
		for (INSBAgent model : agentList) {
			if (model.getAgentstatus() == null) {
				model.setAgentstatusstr("停用");
			} else if (model.getAgentstatus() == 1) {
				model.setAgentstatusstr("启用");
			} else if (model.getAgentstatus() == 2) {
				model.setAgentstatusstr("停用");
			}

			if (model.getAgentkind() == null) {
				model.setAgentkindstr("试用");
			} else if (model.getAgentkind() == 1) {
				model.setAgentkindstr("试用");
			} else if (model.getAgentkind() == 2) {
				model.setAgentkindstr("正式");
			} else if (model.getAgentkind() == 3) {
				model.setAgentkindstr("合作商");
			}
		}
		result.put("total", insbAgentDao.selectCountByMap(map));
		result.put("rows", agentList);
		return result;
	}

	/**
	 * 通过任务id查询车辆任务代理人信息
	 */
	@Override
	public Map<String, Object> getCarTaskAgentInfo(String taskId, String opType) {
		Map<String, Object> temp = new HashMap<String, Object>();
		temp.put("taskid", taskId);
		INSBAgent agent = new INSBAgent();
		// 取得报价信息总表信息
		INSBQuotetotalinfo quotetotalinfoTemp = new INSBQuotetotalinfo();
		quotetotalinfoTemp.setTaskid(taskId);
		quotetotalinfoTemp = insbQuotetotalinfoDao
				.selectOne(quotetotalinfoTemp);
		if (quotetotalinfoTemp != null) {
			if (quotetotalinfoTemp.getTeam() != null) {
				temp.put("team", quotetotalinfoTemp.getTeam());
			}
			if (quotetotalinfoTemp.getPurchaserChannel() != null) {
				temp.put("purchaserchannel", quotetotalinfoTemp.getPurchaserchannelName());// 加渠道来源
//				INSBChannel channel = insbChannelDao.selectByChannelcode(quotetotalinfoTemp.getPurchaserChannel());
//				temp.put("purchaserchannel", channel != null ? channel.getChannelname() : "");// 加渠道来源

				Map<String, Object> qllCon = new HashMap<String, Object>();
				qllCon.put("city", quotetotalinfoTemp.getInscitycode());
				qllCon.put("channelinnercode", quotetotalinfoTemp.getPurchaserChannel());
				Map<String, Object> qllResult = insbChannelDao.queryIllustration(qllCon);
				String channelinfo = "";
				
				if (qllResult != null) {
					Object cill = qllResult.get("cill");
					Object pcill = qllResult.get("pcill");
					if (cill != null) channelinfo += cill;
					if (pcill != null) channelinfo += pcill;
					
					if (channelinfo.length() > 200) {
						channelinfo = channelinfo.substring(0, 200);
					} 
				}
				
				temp.put("channelinfo", channelinfo); //渠道说明
			}			

			// 取得代理人信息
			if (quotetotalinfoTemp.getAgentnum() != null) {
				agent = insbAgentDao.selectByJobnum(quotetotalinfoTemp.getAgentnum());
				if (agent != null) {
					if (agent.getDeptid() != null) {
						INSCDept dept = inscDeptDao.selectById(agent.getDeptid());
						temp.put("deptCode", dept.getComcode());
						if (dept.getComname() != null) {
							temp.put("comname", dept.getComname());
						}
					}
					if (agent.getName() != null) {
						temp.put("agentname", agent.getName());
					}
					if (agent.getTeamname() != null) {
						temp.put("teamname", agent.getTeamname());
					}
					if (agent.getJobnum() != null) {
						temp.put("jobnum", agent.getJobnum());// 工号
					}
					if (agent.getCgfns() != null) {
						temp.put("cgfns", agent.getCgfns());// 资格证
					}
					if (agent.getIdnotype() != null && !("".equals(agent.getIdnotype()))) {
						temp.put("idnotype", inscCodeService.transferValueToName("CertKinds", "CertKinds", agent.getIdnotype() + ""));// 证件类型
					} else {
						temp.put("idnotype", "身份证");
					}
					if (agent.getIdno() != null) {
						temp.put("idno", agent.getIdno());
					}
					if (agent.getLicenseno() != null) {
						temp.put("licenseno", agent.getLicenseno());// 执业证展业证号
					}
					// if (agent.getMobile() != null) {
					// temp.put("mobile", agent.getMobile());
					// }
					if (agent.getPhone() != null) {
						temp.put("mobile", agent.getPhone());
					}
					if (!"DETAIL".equalsIgnoreCase(opType)) {
						if (agent.getNoti() != null) {
							temp.put("noti", agent.getNoti());// 备注
						}
					}
					// String sign = ",";
					// if ("DETAIL".equalsIgnoreCase(opType)) {
					// sign = "<br/>";
					// }
					// if (agent.getMobile() != null
					// && !("".equals(agent.getMobile()))) {
					// temp.put("mobile", agent.getMobile());
					// }
					// if (agent.getMobile2() != null
					// && !("".equals(agent.getMobile2()))) {
					// if (temp.get("mobile") != null) {
					// temp.replace("mobile", temp.get("mobile") + sign
					// + agent.getMobile2());
					// } else {
					// temp.put("mobile", agent.getMobile2());
					// }
					// }
					// if (agent.getTelnum() != null
					// && !("".equals(agent.getTelnum()))) {
					// if (temp.get("mobile") != null) {
					// temp.replace("mobile", temp.get("mobile") + sign
					// + agent.getTelnum());
					// } else {
					// temp.put("mobile", agent.getTelnum());
					// }
					// }
				}
			}
		}
		return temp;
	}

	@Override
	public Map<String, Object> main2edit(String agentId) {
		Map<String, Object> result = new HashMap<String, Object>();

		// 查询所有功能包信息
		INSBAgent agent = new INSBAgent();

		// 存储常用网点名称
		List<String> commonUseBranchNames = new ArrayList<String>();

		// 证件类型
		List<Map<String, Object>> certKinds = inscCodeDao.selectByType("CertKinds");
		// 代理人级别
		List<Map<String, Object>> agentlevelvalue = inscCodeDao.selectByType("agentlevel");

		if (!"".equals(agentId) && agentId != null) {
			agent = insbAgentDao.selectCommNameById(agentId);
			String commonUsebranch = agent.getCommonusebranch();
			if (commonUsebranch != null && !"".equals(commonUsebranch)) {
				String[] commonUsebranchArray = commonUsebranch.split(",");
				for (String str : commonUsebranchArray) {
					INSCDept deptModel = inscDeptDao.selectById(str);
					commonUseBranchNames.add(deptModel.getComname());
				}
			}
		} else {
			agent.setAgentcode(this.updateAgentTempJobNo() + "");
		}

		// 初始化审核状态
		List<Map<String, Object>> approve = inscCodeDao.selectByType("approvestatus");

		result.put("agentlevelvalue", agentlevelvalue);// 代理人级别
		result.put("certKinds", certKinds);// 证件类型
		result.put("agent", agent);
		result.put("approve", approve);
		String commonUseBranch = commonUseBranchNames.toString();
		result.put("commonUseBranchNames", commonUseBranch.substring(1, commonUseBranch.length() - 1));
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public String saveOrUpdateAgent(INSCUser user, INSBAgent agent) {
		String id = "";
		// 修改
		if (agent.getId() != null && !"".equals(agent.getId())) {
			agent.setModifytime(new Date());
			agent.setOperator(user.getUsercode());
			agent.setPhone(agent.getPhone());

			if (agent.getAgentkind() != null && agent.getAgentkind() == 2 ) {
				agent.setJobnumtype(2);
			}

			INSBAgent a = insbAgentDao.selectById(agent.getId());
			if (StringUtil.isNotEmpty(agent.getReferrer()) && !agent.getReferrer().equals(a.getReferrer())) {
				agent.setReferrer(a.getReferrer());
			}

			//task 745 【bug分支】代理人信息中的用户类型只能由正式改为渠道
			if(a.getAgentkind() != 2 ){//非正式用户不能修改
				if (a.getAgentkind() != agent.getAgentkind()) {
					return "changeAgentkinderror";
				}
			} else {
				if (agent.getAgentkind() == 2 || agent.getAgentkind() == 3) {//用户类型可以修改，但是只能允许由“正式”改为“渠道”
				} else {
					return "changeAgentkinderror";
				}
			}

			insbAgentDao.updateById(agent);

			if("2".equals(a.getAgentkind())){
				//20170601 hwc 实名认证通过后通知集团统一中心
				LogUtil.info("saveOrUpdateAgent调用updateUserDetail"+ com.common.JsonUtil.serialize(agent)+" agentcode="+agent.getJobnum());
				userCenterService.updateUserDetail(agent,agent.getJobnum(),agent.getReferrer());
				//20170601 hwc end
			}

			OFUser ofuser = ofUserDao.queryByUserName(agent.getAgentcode());
			if (ofuser == null) {
				ofuser = new OFUser();
				ofuser.setUsername(agent.getAgentcode());
//				ofuser.setPlainPassword("123456");
				ofuser.setPlainPassword(StringUtil.md5Base64("123456"));
				ofuser.setName(agent.getName());
				ofuser.setCreationDate(new Date().getTime());
				ofuser.setModificationDate(new Date().getTime());
				ofUserDao.insert(ofuser);
			}

			// 修改时为试用用户 默认试用权限包 给该用户
			if (StringUtil.isNotEmpty(agent.getSetid())) {
				List<INSBAgentpermission> list=agentpermissionDao.selectByAgentId(agent.getId());
				Map<String,Object> map=new HashMap<String,Object>();
				for(INSBAgentpermission permission:list){
					map.put(permission.getPermissionid(), permission.getSurplusnum());
				}
				agentpermissionDao.deleteByAgentId(agent.getId());
				List<INSBPermissionallot> allotList= insbPermissionallotDao.selectListBySetId(agent.getSetid());
				INSBAgentpermission agentmission=new INSBAgentpermission();
				for(INSBPermissionallot allot:allotList){
					if(map.containsKey(allot.getPermissionid())){
						System.out.println("已使用次数："+map.get(allot.getPermissionid()));
						agentmission.setSurplusnum((Integer)map.get(allot.getPermissionid()));
					}else{
						agentmission.setSurplusnum(0);
					}
					agentmission.setId(UUIDUtils.random());
					agentmission.setAgentid(agent.getId());
					agentmission.setPermissionid(allot.getPermissionid());
					agentmission.setPermissionname(allot.getPermissionname());
					agentmission.setFrontstate(allot.getFrontstate());
					agentmission.setFunctionstate(allot.getFunctionstate());
					agentmission.setValidtimestart(allot.getValidtimestart());
					agentmission.setNum(allot.getNum());
					agentmission.setValidtimeend(allot.getValidtimeend());
					agentmission.setAbort(allot.getAbort());
					agentmission.setStatus(null);
					agentmission.setCreatetime(new Date());
					agentmission.setModifytime(null);
					agentmission.setOperator(user.getName());
					agentmission.setNoti(null);
					agentpermissionDao.insert(agentmission);
				}
//				setAgentpermission(agent);
			} else {
//				agentpermissionDao.deleteByAgentId(agent.getId());
			}

			return agent.getId();
		} else { // 新增 初始化临时工号 类型为1
			// 生成临时工号
			String tempJobNo = "";
			if (StringUtil.isNotEmpty(agent.getAgentcode())) {
				tempJobNo = agent.getAgentcode();
			} else {
				tempJobNo = updateAgentTempJobNo() + "";
			}
			// 插入到LdapAgentModel
			// 向ofuser表中插入数据
			OFUser ofuser = new OFUser();
			ofuser.setUsername(tempJobNo);
//			ofuser.setPlainPassword("123456");
			ofuser.setPlainPassword(StringUtil.md5Base64("123456"));
			ofuser.setName(agent.getName());
			ofuser.setCreationDate(new Date().getTime());
			ofuser.setModificationDate(new Date().getTime());
			ofUserDao.insert(ofuser);

			agent.setCreatetime(new Date());
			agent.setOperator(user.getUsercode());
			agent.setPhone(agent.getMobile());
			agent.setPwd(StringUtil.md5Base64("123456"));
			agent.setTempjobnum(tempJobNo);
			agent.setJobnum(tempJobNo); // 正式工号暂与试用工号相同
			agent.setJobnumtype(1);
			agent.setRegistertime(new Date());
			agent.setAgentcode(tempJobNo);
			if (agent.getDeptid() == null) {
				agent.setDeptid("00000"); // 此处插入一条代理人记录，为非正式代理人的临时工号，没有deptid属性，以00000代替
			}

			// 用户注册时为试用用户 默认试用权限包 给该用户
			// 获取试用权限逻辑 ，根据该用户所属机构向上递归 试用权限包（用户类型为试用的权限包）
			/*if (agent.getAgentkind() != null && agent.getAgentkind() == 1) {
				List<INSBPermissionset> list = permissionsetDao
						.selectTrialSet(agent.getDeptid());
				if (list != null && !list.isEmpty()) {
					agent.setSetid(list.get(0).getId());
				} else {
					agent.setSetid("trial.permission.setid");
				}

			}*/

			id = insbAgentDao.insertReturnId(agent);
			if (agent.getAgentkind() != null && agent.getAgentkind() == 1) {
				agent.setId(id);
				setAgentpermission(agent);
			}

			return id;
		}
	}

	/**
	 * 设置代理人权限
	 * 
	 * @param agent
	 */
	private void setAgentpermission(INSBAgent agent) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("permissionsetId", agent.getSetid());
		queryMap.put("istry", agent.getAgentkind());

		List<Map<String, Object>> permissionList = insbPermissionDao
				.selectSetPermission(queryMap);
		List<INSBAgentpermission> insbAgentpermissionList = insbAgentpermissionDao.selectByAgentId(agent.getId());
		if (permissionList != null && !permissionList.isEmpty()) {
			for (Map<String, Object> m : permissionList) {
				if (m.get("allotid") == null) {
					continue;
				}
				INSBAgentpermission agentpermission = new INSBAgentpermission();
				agentpermission.setAgentid(agent.getId());
				agentpermission.setPermissionid((String) m.get("id"));
				agentpermission.setPermissionname((String) m
						.get("permissionpath"));
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
				Integer i = m.get("num") == null ? null : (Integer) m
						.get("num");
				agentpermission.setNum(i);
				agentpermission
						.setValidtimeend(m.get("validtimeend") == null ? null
								: (Date) m.get("validtimeend"));
				agentpermission
						.setValidtimestart(m.get("validtimestart") == null ? null
								: (Date) m.get("validtimestart"));
				agentpermission.setSurplusnum(surplusnum);
				if( createTime == null ) {
					agentpermission.setCreatetime(new Date());
				} else {
					agentpermission.setCreatetime(createTime);
				}
				if( agentpermission.getId() != null ) {
					agentpermission.setModifytime(new Date());
					agentpermissionDao.updateById(agentpermission);
				} else {
					agentpermissionDao.insert(agentpermission);
				}
			}
		}
	}




	/**
	 * 得到临时工号 并更新
	 * 
	 * @return
	 */
	public String updateAgentTempJobNo() {
		// redis中有数据
		long tempJobNo = redisClient.atomicIncr(Constants.CM_ZZB, "temp_agent_job_no");
		if (tempJobNo != 1) {
			INSCCode code1 = new INSCCode();
			code1.setId("3f916ea05da911e507ff2c65c6e94f2e");
			code1.setCreatetime(new Date());
			code1.setOperator("1");
			code1.setCodetype("agentTempJobNo");
			code1.setParentcode("agentTempJobNo");
			code1.setProp1((int) tempJobNo);
			code1.setCodevalue(tempJobNo + "");
			code1.setCodename("代理人临时工号生成 codeorder 记录步长");
			code1.setNoti("代理人临时工号生成 codeorder 记录步长");
			code1.setCodeorder(1);
			inscCodeDao.updateById(code1);
		}// redis中无数据
		else {
			try {
				INSCCode codeModel = inscCodeDao.selectById("3f916ea05da911e507ff2c65c6e94f2e");
				redisClient.set(Constants.CM_ZZB, "temp_agent_job_no", Integer.parseInt(codeModel.getCodevalue()) + 10 + "");
				tempJobNo = Integer.parseInt(codeModel.getCodevalue()) + 10;
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		String ret = String.valueOf(tempJobNo);
		int length = ret.length();
		for (int i = length; i < 9 ; i++) {
			ret ="0" + ret;
		}

		return "B" + ret;
	}

	@Override
	public String cleanSurplusnum(String agentPermissionId,String username) {
		try {
			INSBAgentpermission insbAgentpermission = new INSBAgentpermission();
			insbAgentpermission.setId(agentPermissionId);
			insbAgentpermission.setSurplusnum(new Integer("0"));
			Date date =new Date();
			SimpleDateFormat sfd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String datestr=sfd.format(date);
			LogUtil.info("业管-"+username+"在"+datestr+"使用次数进行了清零！");
			insbAgentpermissionDao.updateSurplusnum(insbAgentpermission);			
			return "success";
		}catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}
	
	@Override
	public Map<String, Object> getPermissionListByAgentidAndPermissionsetid(String permissionsetId, String agentId) {
		List<AgentRelatePermissionVo> voList = new ArrayList<AgentRelatePermissionVo>();
		Map<String, Object> result = new HashMap<String, Object>();
		INSBAgent agent = null;
		if (StringUtil.isNotEmpty(agentId)) {
			agent = agentDao.selectById(agentId);
		}
		if ( agent != null && permissionsetId != null && !"".equals(permissionsetId) ) {
			List<INSBPermissionallot> permissionallots = permissionallotDao.selectListBySetId(permissionsetId);
			List<INSBAgentpermission> insbAgentpermissionList = insbAgentpermissionDao.selectByAgentId(agent.getId());
			for (INSBPermissionallot pallotModel : permissionallots) {

				if (pallotModel == null || StringUtil.isEmpty(pallotModel.getPermissionid())) {
					continue;
				}
				INSBPermission permission= insbPermissionDao.selectById(pallotModel.getPermissionid());
				if (permission == null || permission.getIstry() == null) {
					continue;
				}
				if (agent == null || agent.getAgentkind() == null) {
					continue;
				}

				AgentRelatePermissionVo vo = new AgentRelatePermissionVo();
				vo.setAgentId(agent.getId());
				vo.setAgentJobnum(agent.getJobnum());
				vo.setPermissionId(pallotModel.getPermissionid());
				vo.setPermissionName(pallotModel.getPermissionname());
				if( pallotModel.getNum() == null ) {
					vo.setPermissionNum("无限制");
				} else if( pallotModel.getNum() < 0 ){
					vo.setPermissionNum("无限制");
				} else {
					vo.setPermissionNum(String.valueOf(pallotModel.getNum()));
				}
				vo.setWarningtimes(pallotModel.getWarningtimes());
				vo.setPermissionSurplusnum("-");

//				vo.setPermissionSetId(insbPermissionset.getId());
//				vo.setPermissionSetName(insbPermissionset.getSetname());
				vo.setPermissonAllotId(pallotModel.getId());
				if (1 == pallotModel.getFunctionstate()) {
					vo.setFunctionstatestr("启用");
				} else {
					vo.setFunctionstatestr("停用");
				}
				for( INSBAgentpermission insbAgentpermission : insbAgentpermissionList ) {
					// 如果权限id一致
					if( pallotModel.getPermissionid().equals(insbAgentpermission.getPermissionid()) ) {
						if( insbAgentpermission.getSurplusnum() != null ) {
							vo.setPermissionSurplusnum(String.valueOf(insbAgentpermission.getSurplusnum()));
						}
						vo.setAgentPermissionId(insbAgentpermission.getId());
						if( !"7".equals(pallotModel.getPermissionid()) && !"5".equals(pallotModel.getPermissionid()) ) {
//							vo.setPermissionPath("<a href=\"javascript:cleanSurplusnum(\'" + insbAgentpermission.getId() + "\');\">清空已使用次数</a>");
							vo.setPermissionPath("<button  onclick=\"cleanSurplusnum(\'" + insbAgentpermission.getId() + "\');\" type=\"button\" class=\"btn btn-primary\">清空已使用次数</button>");
						} else {
							vo.setPermissionPath("");
						}
						break;
					}
				}
				voList.add(vo);
			}
			result.put("total", voList.size());
			result.put("rows", voList);
		}
		return result;
	}
	
	@Override
	public Map<String, Object> getpermissionListByPage(Map<String, Object> map,
			String permissionsetId, String agentId) {
		Map<String, Object> result = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<INSBAgentpermission> permissionResult = new ArrayList<INSBAgentpermission>();
		List<INSBPermissionallot> permissionallotList = new ArrayList<INSBPermissionallot>();

		INSBAgent agent = null;
		if (StringUtil.isNotEmpty(agentId)) {
			agent = agentDao.selectById(agentId);
		}
		// 新增
		if (agentId == null || "".equals(agentId.trim())) {
			// 得到权限信息
			// permissionResult = insbPermissionDao.selectListByPage4add(map);
			// TODO
			// 修改(选择了功能包)
		} else if (!"".equals(permissionsetId) && permissionsetId != null) {
			List<INSBPermissionallot> permissionallots = permissionallotDao.selectListBySetId(permissionsetId);

			for (INSBPermissionallot pallotModel : permissionallots) {
				if (pallotModel == null || StringUtil.isEmpty(pallotModel.getPermissionid())) {
					continue;
				}
				INSBPermission permission= insbPermissionDao.selectById(pallotModel.getPermissionid());
				if (permission == null || permission.getIstry() == null) {
					continue;
				}
				if (agent == null || agent.getAgentkind() == null || !permission.getIstry().equals(agent.getAgentkind())) {
					continue;
				}
				permissionallotList.add(pallotModel);
				if (pallotModel.getValidtimestart() != null) {
					pallotModel.setValidtimestartstr(sdf.format(pallotModel
							.getValidtimestart()));
				} else {
					pallotModel.setValidtimestartstr(sdf.format(new Date()));
				}

				if (pallotModel.getValidtimeend() != null) {
					pallotModel.setValidtimeendstr(sdf.format(pallotModel
							.getValidtimeend()));
				} else {
					try {
						pallotModel.setValidtimeendstr(sdf.format(sdf
								.parse("2099-12-31")));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				if (1 == pallotModel.getFrontstate()) {
					pallotModel.setFrontstatestr("启用");
				} else {
					pallotModel.setFrontstatestr("停用");
				}
				if (1 == pallotModel.getFunctionstate()) {
					pallotModel.setFunctionstatestr("启用");
				} else {
					pallotModel.setFunctionstatestr("停用");
				}
				if (1 == pallotModel.getAbort()) {
					pallotModel.setAbortstr("启用");
				} else {
					pallotModel.setAbortstr("停用");
				}
			}
			result.put("total", permissionallotList.size());
			result.put("rows", permissionallotList);
			// 修改（没有选择功能包，会有默认权限）
		} else {
			/*
			 * List<INSBAgentpermission> agentPermissionList =
			 * agentpermissionDao .selectByAgentId(agentId);
			 * List<INSBPermission> tempPermissionList = insbPermissionDao
			 * .selectPermissionByIstry();
			 */
			/*
			 * if (agentPermissionList.isEmpty() || agentPermissionList == null)
			 * { List<INSBAgentpermission> insertList = new
			 * ArrayList<INSBAgentpermission>(); for (INSBPermission pModel :
			 * tempPermissionList) { INSBAgentpermission agentPermissionModel =
			 * new INSBAgentpermission();
			 * agentPermissionModel.setAgentid(agentId);
			 * agentPermissionModel.setFrontstate(1);
			 * agentPermissionModel.setFunctionstate(1); try {
			 * agentPermissionModel.setValidtimeend(sdf .parse("2099-12-31")); }
			 * catch (ParseException e) { e.printStackTrace(); }
			 * agentPermissionModel.setValidtimestart(new Date());
			 * agentPermissionModel.setAbort(1);
			 * agentPermissionModel.setOperator("admin");
			 * agentPermissionModel.setCreatetime(new Date());
			 * agentPermissionModel.setPermissionid(pModel.getId());
			 * agentPermissionModel.setPermissionname(pModel
			 * .getPermissionname());
			 * 
			 * insertList.add(agentPermissionModel); }
			 * agentpermissionDao.insertInBatch(insertList); } else {
			 * List<Map<String, String>> diffList = new ArrayList<Map<String,
			 * String>>(); for (INSBPermission pModel : tempPermissionList) {
			 * int i = 0; for (INSBAgentpermission apModel :
			 * agentPermissionList) { if (apModel.getValidtimeend() == null) {
			 * try { apModel.setValidtimeend(sdf.parse("2099-12-31"));
			 * apModel.setStatus(1); } catch (ParseException e) {
			 * e.printStackTrace(); }
			 * 
			 * } if (apModel.getFrontstate() == null) {
			 * apModel.setFrontstate(1); } if (apModel.getFunctionstate() ==
			 * null) { apModel.setFunctionstate(1); }
			 * agentpermissionDao.updateById(apModel); if
			 * (pModel.getId().equals(apModel.getPermissionid())) { i = 1;
			 * break; } } if (i == 0) { Map<String, String> tMap = new
			 * HashMap<String, String>(); tMap.put("id", pModel.getId());
			 * tMap.put("name", pModel.getPermissionname()); diffList.add(tMap);
			 * } }
			 * 
			 * if (!diffList.isEmpty()) { for (Map<String, String> tMap :
			 * diffList) { INSBAgentpermission agentPermissionModel = new
			 * INSBAgentpermission(); agentPermissionModel.setAgentid(agentId);
			 * agentPermissionModel.setFrontstate(1);
			 * agentPermissionModel.setFunctionstate(1); try {
			 * agentPermissionModel.setValidtimeend(sdf .parse("2099-12-31")); }
			 * catch (ParseException e) { e.printStackTrace(); }
			 * agentPermissionModel.setValidtimestart(new Date());
			 * agentPermissionModel.setAbort(1);
			 * agentPermissionModel.setOperator("admin");
			 * agentPermissionModel.setCreatetime(new Date());
			 * agentPermissionModel.setPermissionid(tMap.get("id"));
			 * agentPermissionModel .setPermissionname(tMap.get("name"));
			 * 
			 * agentpermissionDao.insert(agentPermissionModel); } } }
			 */

			// 查询代理人权限关系表
			/*
			 * permissionResult = agentpermissionDao.selectByAgentId(agentId);
			 * 
			 * if (permissionResult.isEmpty() || permissionResult == null) {
			 * return result; } for (INSBAgentpermission apModel :
			 * permissionResult) { if (apModel.getValidtimestart() != null) {
			 * apModel.setValidtimestartstr(sdf.format(apModel
			 * .getValidtimestart())); } if (apModel.getValidtimeend() != null)
			 * { apModel.setValidtimeendstr(sdf.format(apModel
			 * .getValidtimeend())); } if (1 == apModel.getFrontstate()) {
			 * apModel.setFrontstatestr("启用"); } else {
			 * apModel.setFrontstatestr("停用"); } if (1 ==
			 * apModel.getFunctionstate()) { apModel.setFunctionstatestr("启用");
			 * } else { apModel.setFunctionstatestr("停用"); } if
			 * (apModel.getAbort() != null && 1 == apModel.getAbort()) {
			 * apModel.setAbortstr("启用"); } else { apModel.setAbortstr("停用"); }
			 * }
			 */

			result.put("total", permissionResult.size());
			result.put("rows", permissionResult);
		}
		return result;
	}

	@Override
	public INSBAgentpermission agentMian2edit(String id) {

		// 全部都是修改
		INSBAgentpermission ap = agentpermissionDao.selectById(id);

		return ap;
	}

	@Override
	public void updateAgentPermission(INSBAgentpermission ap)
			throws ParseException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if (ap.getValidtimeendstr() != null
				&& !"".equals(ap.getValidtimeendstr())) {
			ap.setValidtimeend(format.parse(ap.getValidtimeendstr().toString()));
		}
		if (ap.getValidtimestartstr() != null
				&& !"".equals(ap.getValidtimestartstr())) {
			ap.setValidtimestart(format.parse(ap.getValidtimestartstr()
					.toString()));
		}

		if (ap.getId() == null || "".equals(ap.getId())) {
			ap.setCreatetime(new Date());
			agentpermissionDao.insert(ap);
		} else {
			agentpermissionDao.updateById(ap);
		}

	}

	@Override
	public void removeAgentPermission(INSBAgentpermission ap) {
		if (ap.getId() != null && !"".equals(ap.getId())) {
			agentpermissionDao.delete(ap);
		}
	}

	@Override
	public void saveAgentProvider(String agentId, String providerIds) {

		// 新供应商id（需要调整，部分子节点需要从数据库中获得。检查所有节点的子节点是否在数据库中存在，如果存在 加入到新选中集合中）

		List<String> newIds = new ArrayList<String>();
		// 原来供应商id
		List<String> oldIds = new ArrayList<String>();

		String[] providerIsArr = providerIds.split(",");
		for (String id : providerIsArr) {
			newIds.add(id);
		}

		// 拿到当前代理人id所有 供应商code
		List<INSBAgentprovider> apList = agentproviderDao
				.selectListByAgentId(agentId);
		for (INSBAgentprovider model : apList) {
			oldIds.add(model.getProviderid());
		}

		Map<String, List<String>> tempData = serviceUtil.updateUtil(newIds,
				oldIds);

		List<String> addList = tempData.get("add");

		List<INSBAgentprovider> addListParam = new ArrayList<INSBAgentprovider>();
		for (String providerId : addList) {
			INSBAgentprovider ap = new INSBAgentprovider();
			ap.setProviderid(providerId);
			ap.setAgentid(agentId);
			ap.setCreatetime(new Date());
			ap.setOperator("1");

			addListParam.add(ap);
			// agentproviderDao.insert(ap);
		}
		agentproviderDao.insertInBatch(addListParam);

		List<String> deleteList = tempData.get("delete");
		for (String providerId : deleteList) {
			Map<String, String> ids = new HashMap<String, String>();
			ids.put("agentId", agentId);
			ids.put("providerId", providerId);
			agentproviderDao.deleteByAgentIdproviderId(ids);
		}
	}

	@Override
	public List<Map<String, String>> getProviderTreeList(String agentId,
			String setId) {
		List<Map<String, String>> resultTreeList = new ArrayList<Map<String, String>>();

		if (agentId.length() > 0) {

			// 得到代理人信息
			INSBAgent agentModel = insbAgentDao.selectCommNameById(agentId);

			// 得到代理人供应商关联关系
			List<INSBAgentprovider> agentProviderIdsList = agentproviderDao
					.selectListByAgentId(agentId);

			// 代理人已经关联供应商
			List<String> agentProviderIds = new ArrayList<String>();
			for (INSBAgentprovider ap : agentProviderIdsList) {
				agentProviderIds.add(ap.getProviderid());
			}

			// 通过协议得到当前代理人 对应的供应商信息
			List<String> providerAgreementDataList = getAgreementProviderIds(agentModel
					.getDeptid());

			// 通过功能包得到当前代理人 对应的供应商信息
			List<String> providerSetDataList = new ArrayList<String>();
			if (!"".equals(setId) && setId != null) {
				providerSetDataList = getSetProviderIds(setId);
			}

			// 两次查出来的供应商做交集
			Set<String> providerData = new HashSet<String>();

			if (!providerAgreementDataList.isEmpty()) {
				for (String proviserId : providerAgreementDataList) {
					// providerData.add(proviserId);
				}
			}
			if (!providerSetDataList.isEmpty()) {
				for (String proviserId : providerSetDataList) {
					providerData.add(proviserId);
				}

			}
			if (agentProviderIds.size() > 0) {
				for (String proviserId : agentProviderIds) {
					// providerData.add(proviserId);
				}

			}
			providerData.remove(null);

			System.out.println("所有供应商-===" + providerData);
			// 得到所有供应商
			for (String pcode : providerData) {
				// 得到当前的供应商
				// resultTreeList = getProviders(resultTreeList, pcode);

				INSBProvider tempModel = insbProviderDao.queryByPrvcode(pcode);
				System.out.println(tempModel);

				if (tempModel == null) {
					continue;
				}
				Map<String, String> tempMap = new HashMap<String, String>();
				tempMap.put("id", tempModel.getPrvcode());
				tempMap.put("pId", tempModel.getParentcode());
				tempMap.put("name", tempModel.getPrvname());

				resultTreeList.add(tempMap);
			}

			// 存储关系表中所有供应商id

			if (!agentProviderIds.isEmpty()) {

				for (Map<String, String> treeModel : resultTreeList) {
					for (String id : agentProviderIds) {
						if (id.equals(treeModel.get("id"))) {
							treeModel.put("checked", "true");
						}
					}
				}
			}
		}
		return resultTreeList;
	}

	/**
	 * AB 得到功能包 供应商ids
	 * 
	 * @param setId
	 * @return
	 */
	private List<String> getSetProviderIds(String setId) {
		List<String> result = new ArrayList<String>();
		// 通过功能id得到所有权限信息
		List<String> itemModelIds = itemprovidestatusDao
				.selectProviderIdBySetId(setId);
		if (!itemModelIds.isEmpty()) {
			for (String str : itemModelIds) {
				result.add(str);
			}
		}

		return result;
	}

	/**
	 * CD
	 * 
	 * 通过 协议 用组织机构得到所有的出单网点
	 * 
	 * @param deptId
	 * @return
	 */
	private List<String> getAgreementProviderIds(String deptId) {
		List<String> result = new ArrayList<String>();
		List<String> agreementIds = agreementscopeDao
				.selectAgreementIdByDeptId(deptId);
		if (!agreementIds.isEmpty()) {
			for (String agreementId : agreementIds) {
				String pId = agreementDao
						.selectProviderIdByAgreementId(agreementId);
				result.add(pId);
			}
		}
		return result;
	}

	/**
	 * 
	 * @param resultList
	 * @param parentcode
	 * @return
	 */
	private List<Map<String, String>> getProviders(
			List<Map<String, String>> resultList, String parentcode) {

		List<Map<String, String>> tempList = insbProviderDao
				.selectByParentProTreeCode3(parentcode);
		if (tempList != null && tempList.size() > 0) {
			resultList.addAll(tempList);
			for (Map<String, String> treeModel : tempList) {
				getProviders(resultList, treeModel.get("id"));
			}
		}
		return resultList;
	}

	@Override
	public String deleteAgentBatch(String ids) {
		String result = "";
		if (!"".equals(ids) && ids != null) {
			String[] idArray = ids.split(",");

			INSBAgent agent = new INSBAgent();
			INSBPolicyitem po = new INSBPolicyitem();
			INSBQuotetotalinfo qu = new INSBQuotetotalinfo();
			String code = "";
			Long pocount;
			Long qucount;
			for (String id : idArray) {
				agent = insbAgentDao.selectById(id);
				code = agent.getAgentcode();
				po.setAgentnum(code);
				pocount = policyitemDao.selectCount(po);
				if (pocount > 0) {
					result = "编码为" + code + "的代理人有保单信息，不能删除。";
					continue;
				}
				qu.setAgentnum(code);
				qucount = insbQuotetotalinfoDao.selectCount(qu);
				if (qucount > 0) {
					result = "编码为" + code + "的代理人有报价信息，不能删除。";
					continue;
				}
				if (agent.getAgentkind() == 2) {
					System.out.println("agent.getAgentkind()是："
							+ agent.getAgentkind());
					result = "编码为" + code + "的代理人为正式用户，不能删除。";
					continue;
				}
				insbAgentDao.deleteById(id);
			}
		}
		return result;
	}

	/*
	 * @Override public void deleteAgentBatch(String ids) { if (!"".equals(ids)
	 * && ids != null) { String[] idArray = ids.split(","); for (String id :
	 * idArray) { insbAgentDao.deleteById(id); } } }
	 */

	@Override
	public String getAgentInfo(Map<String, Object> map) {
		if (map != null && !map.containsKey("limit")) {
			map.put("limit", 10);
		}
		List<Map<String, Object>> iNSBAgentList = insbAgentDao.selectListByUser(map);

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		// 用于展示代理人信息的序号使用
		int i = 1;
		for (Map<String, Object> agent : iNSBAgentList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("id", agent.get("id"));
			tempMap.put("licenseno", i);
			tempMap.put("jobnum", agent.get("jobnum"));
			tempMap.put("name", agent.get("name"));
			tempMap.put("phone", agent.get("phone"));
			tempMap.put("idno", agent.get("idno"));
			tempMap.put(
					"operating",
					"<a href=\"javascript:carInsurance(\'" + agent.get("id")
							+ "\');\" >车型投保</a>"
							+ "<a href=\"javascript:imageManagement(\'"
							+ agent.get("id") + "\');\" >影像管理</a>"
							+ "<a href=\"javascript:rapidRenewal(\'"
							+ agent.get("id") + "\');\" >快速续保</a>"
							+ "<a href=\"javascript:orderManagement(\'"
							+ agent.get("id") + "\');\" >订单管理</a>");

			i += 1;
			resultList.add(tempMap);
		}

		Map<Object, Object> initMap = new HashMap<Object, Object>();

		initMap.put("records", "10000");
		initMap.put("page", 1);
		initMap.put("total", insbAgentDao.selectCountByUser(map));
		initMap.put("rows", resultList);

		JSONObject jsonObj = JSONObject.fromObject(initMap);

		return jsonObj.toString();
	}

	@Override
	/**
	 * 查询代理人信息
	 * 
	 */
	public Map<String, Object> getAgentInfo(String taskId) {
		INSBAgent agent = insbAgentDao.selectById("10086");
		Map<String, Object> temp = new HashMap<String, Object>();
		if (agent != null) {
			if (agent.getId() != null) {
				temp.put("id", agent.getId());
			}
			if (agent.getName() != null) {
				temp.put("agentname", agent.getName());
			}
			if (agent.getJobnum() != null) {
				temp.put("jobnum", agent.getJobnum());
			}
			if (agent.getCgfns() != null) {
				temp.put("cgfns", agent.getCgfns());
			}
			if (agent.getIdno() != null) {
				temp.put("idno", agent.getIdno());
			}
			if (agent.getMainbusiness() != null) {
				temp.put("mainbusiness", agent.getMainbusiness());
			}
			if (agent.getDeptid() != null) {
				temp.put("deptid", agent.getDeptid());
			}
			if (agent.getMobile() != null) {
				temp.put("mobile", agent.getMobile());
			}
			if (agent.getBankcard() != null) {
				temp.put("bankcard", agent.getBankcard());
			}
		}
		return temp;
	}

	@Override
	/**
	 * 通过代理人id查询代理人工号（分正式工号和临时工号）
	 * 
	 */
	public String getAgentJobNumber(String id) {
		INSBAgent agent = insbAgentDao.selectById(id);
		String jobNumber = null;
		if (agent != null) {
			if (agent.getJobnumtype() == 1) {
				jobNumber = agent.getJobnum();
				logger.info("正式工号为" + jobNumber);
			}
			if (agent.getJobnumtype() == 2) {
				jobNumber = agent.getTempjobnum();
				logger.info("临时工号为" + jobNumber);
			}
		}
		;
		return jobNumber;
	}

	@Override
	public Map<String, String> getCertificationPhotoPath(String jobNum) {
		Map<String, String> map = new HashMap<String, String>();
		INSBCertification certification = new INSBCertification();
		certification.setAgentnum(jobNum);
		certification = certificationDao.selectOne(certification);
		// 判断认证信息是否存在
		if (certification == null)
			return null;
		// 查询图片信息
		INSBFilelibrary idcardpositive = null;
		INSBFilelibrary idcardopposite = null;
		INSBFilelibrary bankcardpositive = null;
		INSBFilelibrary qualificationpositive = null;
		INSBFilelibrary qualificationpage = null;
		if (certification.getIdcardpositive() != null
				&& !"".equals(certification.getIdcardpositive())) {
			idcardpositive = filelibraryService.queryById(certification
					.getIdcardpositive());
		}
		if (certification.getIdcardopposite() != null
				&& !"".equals(certification.getIdcardopposite())) {
			idcardopposite = filelibraryService.queryById(certification
					.getIdcardopposite());
		}
		if (certification.getBankcardpositive() != null
				&& !"".equals(certification.getBankcardpositive())) {
			bankcardpositive = filelibraryService.queryById(certification
					.getBankcardpositive());
		}
		if (certification.getQualificationpositive() != null
				&& !"".equals(certification.getQualificationpositive())) {
			qualificationpositive = filelibraryService.queryById(certification
					.getQualificationpositive());
		}
		if (certification.getQualificationpage() != null
				&& !"".equals(certification.getQualificationpage())) {
			qualificationpage = filelibraryService.queryById(certification
					.getQualificationpage());
		}

		if (idcardpositive != null)
			map.put(idcardpositive.getFiletype(),
					(idcardpositive == null ? null : idcardpositive
							.getFilepath()));
		if (idcardopposite != null)
			map.put(idcardopposite.getFiletype(),
					(idcardopposite == null ? null : idcardopposite
							.getFilepath()));
		if (bankcardpositive != null)
			map.put(bankcardpositive.getFiletype(),
					(bankcardpositive == null ? null : bankcardpositive
							.getFilepath()));
		if (qualificationpositive != null)
			map.put(qualificationpositive.getFiletype(),
					(qualificationpositive == null ? null
							: qualificationpositive.getFilepath()));
		if (qualificationpage != null)
			map.put(qualificationpage.getFiletype(),
					(qualificationpage == null ? null : qualificationpage
							.getFilepath()));
		map.put("deptid", certification.getDeptid());
		return map;
	}

	@Override
	public List<Map<String, Object>> getNotCertificationAgent() {

		return insbAgentDao.selectByCertificationStatus(0);
	}

	@Override
	public List<Map<String, Object>> getQueryPageData() {
		List<Map<String, Object>> result = inscCodeDao
				.selectByType("approvestatus");
		return result;
	}

	@Override
	public int selectcountByAgentCode(String agentcode, String id) {
		int num = 0;
		Map<String, String> param = new HashMap<String, String>();
		param.put("agentcode", agentcode);
		// 当前为新增
		if (id != null && !id.equals("")) {
			param.put("id", id);
		}
		num = insbAgentDao.selectCountByAgentCode(param);

		return num;
	}

	@Override
	public boolean getOnlyAgent(CertificationVo certificationVo) {
		return insbAgentDao.getOnlyAgent(certificationVo);
	}

	@Override
	public INSBAgent getAgentInfo(INSBAgent temp) {
		return insbAgentDao.selectOne(temp);
	}

	@Override
	public Map<String, String> updateResetPwd(String ids, String operator) {
		Map<String, String> resultMap = new HashMap<String, String>();
		try {
			String[] IdArray = ids.split(",");
			Date now = new Date();
			for (String userId : IdArray) {
				INSBAgent tempAgent = insbAgentDao.selectById(userId);
				String oldPwd = tempAgent.getPwd();
				tempAgent.setId(userId);
				tempAgent.setPwd(StringUtil.md5Base64("123456"));
				tempAgent.setModifytime(now);
				tempAgent.setOperator(operator);
				LogUtil.info("updateResetPwd 代理人：" + tempAgent.toString() + " 操作人:" + operator + " 操作时间:" + DateUtil.getCurrentDateTime());
//				insbAgentDao.updatePWDById(tempAgent);
				updateAgentPwd(tempAgent, oldPwd);//修改密码并发短信通知代理人
				// String result =
				// updatePassWord4Ldap(tempAgent.getJobnum(),"123456");
				// if("fail".equals(result)){
				// throw new Exception();
				// }
				OFUser user = new OFUser();
				user.setUsername(tempAgent.getJobnum());
//				user.setPlainPassword("123456");
				user.setPlainPassword(StringUtil.md5Base64("123456"));
				ofUserDao.updateByUserName(user);
				/**
				 * 通知集团统一用户中心 hwc 添加 20170710
				 */
				try {
					Map<String, String> params = new HashMap<>();
					params.put("omobile", tempAgent.getPhone());
					params.put("nmobile","");
					params.put("cardno", tempAgent.getIdno());
					params.put("agentcode", tempAgent.getAgentcode());
					params.put("password", tempAgent.getPwd());
					params.put("passwordflag","1");
					userCenterService.updateUserAccount(params);
				}catch (Exception e){
					LogUtil.info("修改密码通知集团统一用户中心失败"+e.getMessage());
					e.printStackTrace();
				}
			}
			resultMap.put("code", "0");
			resultMap.put("message", "操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("code", "1");
			resultMap.put("message", "操作失败，请稍候重试");
		}
		return resultMap;
	}

	// private String updatePassWord4Ldap(String jobNum, String newPassword) {
	// LdapMgr ldap = new LdapMgr();
	// LdapAgentModel agentModel = ldap.searchAgent(jobNum);
	// boolean ret
	// =ldap.modifyPassWord(agentModel.getEmployeeNumber(),LdapMd5.Md5Encode(newPassword));
	// if(ret){
	// return "success";
	// }else{
	// return "fail";
	// }
	// }

	/**
	 * 处理跟踪
	 */
	@Override
	public String getJSONOfCarTaskListByMap(Map<String, Object> paramMap,
			String taskstatussss) {
		List<Map<String, Object>> carTaskAllList = insbCarinfoDao
				.getCarTaskListByMap(paramMap);// 通过各种关联查询返回数据
		/*
		 * int limit = Integer.parseInt(paramMap.get("limit").toString()); int
		 * offset = Integer.parseInt(paramMap.get("offset").toString()); int
		 * endIndex =
		 * (limit+offset)>carTaskAllList.size()?carTaskAllList.size():
		 * (limit+offset); List<Map<String, Object>> carTaskList = new
		 * ArrayList<Map<String,Object>>(); for (int i = offset; i < endIndex;
		 * i++) { carTaskList.add(carTaskAllList.get(i)); } Map<String, String>
		 * taskcodeparams = new HashMap<String, String>();
		 * taskcodeparams.put("codetype", "workflowNodelName");
		 * taskcodeparams.put("parentcode", "workflowNodelName"); List<INSCCode>
		 * workflowNodelNameList =
		 * inscCodeDao.selectINSCCodeByCode(taskcodeparams); for (int i = 0; i <
		 * carTaskList.size(); i++) { Map<String, Object> temp =
		 * carTaskList.get(i); String maininstanceid = (String)
		 * temp.get("maininstanceId"); String subInstanceId = (String)
		 * temp.get("subInstanceId"); String taskcode = (String)
		 * temp.get("taskcode"); String inscomcode = (String)
		 * temp.get("inscomcode"); String operator = (String)
		 * temp.get("operator"); String groupid = (String) temp.get("groupid");
		 * if(groupid!=null && !("".equals(groupid))){ if(operator!=null &&
		 * !("".equals(operator))){ temp.put("dispatchstatus","已分配到人"); }else{
		 * temp.put("dispatchstatus","人分配中"); } }else{
		 * temp.put("dispatchstatus","组分配中"); } //添加查勘任务详细信息链接
		 * temp.put("operating",
		 * "<a href=\"javascript:window.parent.openLargeDialog(\'business/cartaskmanage/showcartaskdetail?maininstanceid="
		 * +
		 * maininstanceid+"&inscomcode="+inscomcode+"&subinstanceid="+subInstanceId
		 * +"&taskcode="+taskcode+"\');\">详细信息</a>"); for (INSCCode inscCode :
		 * workflowNodelNameList) {
		 * if(inscCode.getCodevalue().equals(taskcode)){ temp.put("tasktype",
		 * inscCode.getCodename()); break; } } }
		 */
		for (int i = 0; i < carTaskAllList.size(); i++) {
			Map<String, Object> temp = carTaskAllList.get(i);
			String maininstanceid = (String) temp.get("maininstanceId");
			String subInstanceId = (String) temp.get("subInstanceId");
			String taskcode = (String) temp.get("taskcode");
			String inscomcode = (String) temp.get("inscomcode");
			// 添加查勘任务详细信息链接
			temp.put(
					"operating",
					"<a href=\"javascript:window.parent.openLargeDialog(\'business/cartaskmanage/showcartaskdetail?maininstanceid="
							+ maininstanceid
							+ "&inscomcode="
							+ inscomcode
							+ "&subinstanceid="
							+ subInstanceId
							+ "&taskcode="
							+ taskcode + "\');\">详细信息</a>");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		// map.put("records", "10000");
		// map.put("page", 1);
		// map.put("total", insbCarinfoDao.getCarTaskCountByMap(paramMap));
		map.put("total", carTaskAllList.size());
		map.put("rows", carTaskAllList);
		// map.put("rows", carTaskList);
		JSONObject jsonObject = JSONObject.fromObject(map);
		return jsonObject.toString();
	}

	/**
	 * 查询某节点下所有网点id集合
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public List<String> queryWDidsByPatId(String id, String comtype) {

		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		map.put("comtype", comtype);
		return inscDeptDao.queryWDidsByPatId(map);
	}

	@Override
	public INSBAgent getAgent(String jobnum) {
		if (StringUtil.isNotEmpty(jobnum)) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("jobnum", jobnum);
			return insbAgentDao.selectAgent(map);
		}
		return null;
	}

	/**
	 * 将代理人数据同步到CM的INSBAGENT表中
	 * 
	 * @param bwaList
	 * @return
	 * @throws Exception
	 */
	// public boolean getResultOfAgentToCm(List<INSBBwagent> bwaList)
	// throws Exception {
	// boolean result = false;
	// if (bwaList != null && bwaList.size() > 0 && !bwaList.isEmpty()) {
	// INSBAgent agent = null;
	// INSBAgent selectAgent = null;
	// LdapAgentManager agentManager = new LdapAgentManager();
	// LdapAgentBean agentSerchBean = null;
	// LdapAgentBean searchAgent = null;
	// INSBBwagent bwagent = null;
	// for (int i = 0; i < bwaList.size(); i++) {
	// bwagent = bwaList.get(i);
	// System.out.println(bwagent);
	// if (bwagent != null) {
	// agentSerchBean = new LdapAgentBean();
	// agent = new INSBAgent();
	// agent.setId(UUIDUtils.random());
	// agent.setName(bwagent.getName());
	// agent.setSex(bwagent.getSex());
	// agent.setJobnum(bwagent.getAgentcode());
	// agent.setJobnumtype(Integer.valueOf(2));
	// agent.setPhone(bwagent.getMobile());
	// agent.setEmail(bwagent.getEmail());
	// agent.setDeptid(bwagent.getMgmtdivision());
	// if (StringUtil.isNotEmpty(bwagent.getAgentgrade())
	// && bwagent.getAgentgrade().trim().length() > 1) {
	// agent.setAgentlevel(Integer.valueOf(bwagent
	// .getAgentgrade().trim().substring(1)));
	// }
	// agent.setAgentcode(bwagent.getAgentcode());
	// agent.setAgentstatus(Integer.valueOf(bwagent
	// .getAgentstate()));
	// if(StringUtil.isNotEmpty(bwagent.getAgentkind())){
	// agent.setAgentkind(Integer.valueOf(bwagent.getAgentkind()));
	// }
	//
	// agent.setMobile(bwagent.getMobile());
	// if (StringUtil.isNotEmpty(bwagent.getIdno())
	// && bwagent.getIdno().trim().length() >= 6) {
	// agent.setPwd(StringUtil.md5Base64(bwagent
	// .getIdno()
	// .trim()
	// .substring(
	// bwagent.getIdno().trim().length() - 6)));
	// } else {
	// agent.setPwd(StringUtil.md5Base64("123456"));
	// }
	// agent.setIstest(Integer.valueOf(2));
	// agent.setReferrer(bwagent.getIntroagency());
	// agent.setCgfns(bwagent.getQuacertifno());
	// agent.setLicenseno(bwagent.getPracertifno());
	// agent.setIdno(bwagent.getIdno());
	// agent.setIdnotype(bwagent.getIdnotype());
	// agent.setCreatetime(bwagent.getMaketime());
	// agent.setModifytime(bwagent.getModifytime());
	// agent.setAddress(bwagent.getHomeaddress());
	// if (StringUtil.isNotEmpty(bwagent.getAgentgroup())) {
	// agent.setTeamcode(bwagent.getAgentgroup());
	// INSCDept teamDept = inscDeptDao.selectByComcode(bwagent
	// .getAgentgroup());
	// if (teamDept != null
	// && StringUtil.isNotEmpty(teamDept.getComname())) {
	// agent.setTeamname(teamDept.getComname());
	// }
	// if (bwagent.getAgentgroup().trim().length() > 4) {
	// String platformcode = bwagent.getAgentgroup()
	// .trim().substring(0, 4);
	// for (int j = 0; j < 6; j++) {
	// platformcode += "0";
	// }
	// agent.setPlatformcode(platformcode);
	//
	// INSCDept platDept = inscDeptDao
	// .selectByComcode(platformcode);
	// if (platDept != null
	// && StringUtil.isNotEmpty(platDept
	// .getComname())) {
	// agent.setPlatformname(platDept.getComname());
	// }
	// }
	// }
	// agentSerchBean.setObjectClass(LdapAgentManager.LDAP_AGENT);
	// agentSerchBean.setCn(agent.getPhone());
	// agentSerchBean.setEmployeeNumber(agent.getJobnum());
	// searchAgent = agentManager.searchAgent(agentSerchBean);
	// if (searchAgent != null) {
	// agent.setNoti(searchAgent.getDN());
	// }
	//
	// if (StringUtil.isNotEmpty(agent.getJobnum())
	// && StringUtil.isNotEmpty(agent.getDeptid())) {
	// selectAgent = insbAgentSyncService.getAgent(
	// agent.getJobnum(), agent.getDeptid());
	// }
	// if (selectAgent != null) {
	// insbAgentSyncService.updateAgent(getAgent(selectAgent,
	// agent));
	// } else {
	// insbAgentSyncService.saveAgent(agent);
	// }
	// }
	// }
	// result = true;
	// }
	// return result;
	// }

	

	/**
	 * 设置代理人数据
	 * 
	 * @param searchAgent
	 * @param bean
	 * @return
	 */
	private LdapAgentBean getLdapAgentBean(LdapAgentBean searchAgent,
			LdapAgentBean bean) {
		// searchAgent.setEmployeeNumber(bean.getEmployeeNumber());
		searchAgent.setDisplayName(bean.getDisplayName());
		searchAgent.setSn(bean.getSn());
		searchAgent.setGivenName(bean.getGivenName());
		// searchAgent.setUserPassword(bean.getUserPassword());
		// searchAgent.setObjectClass(bean.getObjectClass());
		searchAgent.setSuccessFlag(bean.getSuccessFlag());
		// searchAgent.setParentDN(bean.getParentDN());
		// searchAgent.setCn(bean.getCn());
		if (StringUtil.isEmpty(searchAgent.getMobile())) {
			searchAgent.setMobile(bean.getMobile());
		}
		searchAgent.setMail(bean.getMail());
		searchAgent.setInitials(bean.getInitials());
		searchAgent.setBusinessCategory(bean.getBusinessCategory());
		searchAgent.setRegisteredAddress(bean.getRegisteredAddress());
		searchAgent.setDestinationIndicator(bean.getDestinationIndicator());
		searchAgent.setTitle(bean.getTitle());
		searchAgent.setEmployeeType(bean.getEmployeeType());
		return searchAgent;
	}

	/**
	 * 将代理人数据同步到LDAP
	 * 
	 * @param bwaList
	 * @return
	 * @throws Exception
	 */
	public boolean getResultOfAgentToLdap(List<INSBBwagent> bwaList)
			throws Exception {
		boolean result = false;
		if (bwaList != null && bwaList.size() > 0 && !bwaList.isEmpty()) {
			LdapAgentManager manager = new LdapAgentManager();
			LdapOrgManager orgManager = new LdapOrgManager();
			LdapOrgBean orgSearchBean = null;
			LdapAgentBean searchAgent = null;
			LdapAgentBean addAgentResult = null;
			boolean modifyAgentResult = false;
			INSBBwagent bwagent = null;
			for (int i = 0; i < bwaList.size(); i++) {
				bwagent = bwaList.get(i);
				orgSearchBean = new LdapOrgBean();
				if (bwagent != null) {
					LdapAgentBean bean = new LdapAgentBean();
					bean.setEmployeeNumber(bwagent.getAgentcode());
					bean.setDisplayName(bwagent.getName());

					if (StringUtil.isNotEmpty(bwagent.getName())) {
						if (bwagent.getName().indexOf("工作室") != -1) {
							bean.setSn(bwagent.getName().substring(0,
									bwagent.getName().indexOf("工作室")));
							bean.setGivenName(bwagent.getName().substring(
									bwagent.getName().indexOf("工作室")));
						} else {
							if (bwagent.getName().length() > 0
									&& bwagent.getName().length() < 2) {
								bean.setSn(bwagent.getName());
								bean.setGivenName("**");
							} else if (bwagent.getName().length() >= 2
									&& bwagent.getName().length() < 4) {
								bean.setSn(bwagent.getName().substring(0, 1));
								bean.setGivenName(bwagent.getName()
										.substring(1));
							} else if (bwagent.getName().length() >= 4) {
								bean.setSn(bwagent.getName().substring(0, 2));
								bean.setGivenName(bwagent.getName()
										.substring(2));
							} else {
								bean.setSn("*");
								bean.setGivenName("**");
							}
						}
					}

					if (StringUtil.isNotEmpty(bwagent.getIdno())
							&& bwagent.getIdno().trim().length() >= 6) {
						bean.setUserPassword(LdapMd5.Md5Encode(bwagent
								.getIdno()
								.trim()
								.substring(
										bwagent.getIdno().trim().length() - 6)));
					} else {
						bean.setUserPassword(LdapMd5.Md5Encode("123456"));
					}
					bean.setObjectClass(LdapAgentManager.LDAP_AGENT);
					bean.setSuccessFlag("false");
					if (StringUtil.isNotEmpty(bwagent.getMgmtdivision())) {
						orgSearchBean
								.setObjectClass(LdapOrgManager.LDAP_ORG_UNIT);
						orgSearchBean.setDestinationIndicator(bwagent
								.getMgmtdivision());
						LdapOrgBean searchOrg = orgManager
								.searchOrganization(orgSearchBean);
						if (searchOrg != null
								&& StringUtil.isNotEmpty(searchOrg.getDN()
										.trim())) {
							bean.setParentDN(searchOrg
									.getDN()
									.trim()
									.substring(
											0,
											searchOrg
													.getDN()
													.trim()
													.indexOf(
															LdapOrgManager.baseDN) - 1));
						} else {
							throw new Exception("工号为:" + bwagent.getAgentcode()
									+ "的代理人" + bwagent.getName()
									+ "在LDAP中找不到相应的网点，请同步机构后再试。");
						}
					} else {
						throw new Exception("工号为:" + bwagent.getAgentcode()
								+ "的代理人" + bwagent.getName() + "网点编码有问题。");
					}
					bean.setUid(UUIDUtils.random());
					bean.setCn(bwagent.getAgentcode());
					bean.setMobile(bwagent.getMobile());
					bean.setMail(bwagent.getEmail());
					bean.setInitials(bwagent.getSex());
					bean.setBusinessCategory(bwagent.getIdnotype());
					bean.setRegisteredAddress(bwagent.getIdno());
					bean.setDestinationIndicator("1");
					bean.setTitle("PropertyIns");
					if (StringUtil.isNotEmpty(bwagent.getAgentgrade())
							&& bwagent.getAgentgrade().trim().length() > 1) {
						bean.setEmployeeType(bwagent.getAgentgrade().trim()
								.substring(1));
					}
					bean.setMgmtDivision(bwagent.getMgmtdivision());
					bean.setAgentGroup(bwagent.getAgentgroup());

					searchAgent = manager.searchAgent(bean);
					if (searchAgent == null) {
						addAgentResult = manager.addAgent(bean);
						if (addAgentResult != null
								&& Boolean.parseBoolean(addAgentResult
										.getSuccessFlag())) {
							result = true;
						} else {
							result = false;
							break;
						}
					} else {
						modifyAgentResult = manager
								.modifyAgent(getLdapAgentBean(searchAgent, bean));
						if (modifyAgentResult) {
							result = true;
						} else {
							result = false;
							break;
						}
					}
				}
			}
		}
		return result;
	}

	@Override
	public Map<String, Object> getInsuranceInfo() {
		Map<String, Object> result = new HashMap<String, Object>();
		// 证件类型
		List<Map<String, Object>> certKinds = inscCodeDao
				.selectByType("CertKinds");
		// 行驶区域
		List<Map<String, Object>> carDrivingArea = inscCodeDao
				.selectByType("CarDrivingArea");
		result.put("certKinds", certKinds);
		result.put("carDrivingArea", carDrivingArea);
		return result;
	}
	@Override
	public void updateOrderInsuranceInfo(String jobno, String tempjobno) {
		try{
			if (StringUtil.isEmpty(jobno) || StringUtil.isEmpty(tempjobno)) {
				return;
			}
			INSBOrder orderquery = new INSBOrder();
			orderquery.setAgentcode(tempjobno);
			List<INSBOrder> orders = orderDao.selectList(orderquery);
			for(INSBOrder order:orders){
				order.setAgentcode(jobno);
				order.setModifytime(new Date());
				orderDao.updateById(order);
			}
			INSBPolicyitem prolicequery = new INSBPolicyitem();
			prolicequery.setAgentnum(tempjobno);
			List<INSBPolicyitem> policyitems = policyitemDao.selectList(prolicequery);
			for(INSBPolicyitem policyitem:policyitems){
				policyitem.setAgentnum(jobno);
				policyitem.setModifytime(new Date());
				policyitemDao.updateById(policyitem);
			}
			INSBQuotetotalinfo query = new INSBQuotetotalinfo();
			query.setAgentnum(tempjobno);
			List<INSBQuotetotalinfo> quoteinfos = insbQuotetotalinfoDao.selectList(query);
			for(INSBQuotetotalinfo quoteinfo:quoteinfos){
				quoteinfo.setAgentnum(jobno);
				quoteinfo.setModifytime(new Date());
				insbQuotetotalinfoDao.updateById(quoteinfo);
				LogUtil.info("INSBQuotetotalinfo|报表数据埋点|" + JSONObject.fromObject(quoteinfo).toString());
			}
		}catch(Exception e){
			LogUtil.info("绑定工号，更行订单保单数据异常！"+e.getMessage());
		}
	}
	@Override
	public int phoneverify(String phone, String agentid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("phone", phone);
		map.put("agentid", agentid);

		return insbAgentDao.phoneverify(map);
	}

	@Override
	public void updatePwdById() {
		List<Map<String, String>> list = insbAgentDao.selectIdAnadPwd();
		LogUtil.info("====size:" + list.size());
		System.out.println("====size:" + list.size());
		Map<String, String> vMap = null;
		int i = 1;
		for (Map<String, String> map : list) {
			vMap = new HashMap<String, String>();
			vMap.put("id", map.get("ID"));
			vMap.put("pwd", StringUtil.md5Base64(map.get("PWD")));
			insbAgentDao.updatePwdById(vMap);
			System.out.println(map.get("ID") + ":" + map.get("PWD") + ":"
					+ vMap.get("pwd"));
			vMap.clear();
			vMap = null;
			System.out.println("====i:" + (i++));
		}
		LogUtil.info("====success modify:" + list.size());
	}

	@Override
	public String getAgentProvince(String agentnum) {
		return insbAgentDao.getAgentProvince(agentnum);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Map<String, Object> changeAgentPwd(String token, String param) {
		AgentUpdatePwdVo pwdModel = JSON.parseObject(param,
				AgentUpdatePwdVo.class);

		Map<String, Object> result = new HashMap<String, Object>();

		if (pwdModel == null) {
			result.put("status", "fail");
			result.put("message", "网络异常，修改密码失败，请重试");
			return result;
		}
		if (StringUtils.isEmpty(pwdModel.getAgentId())) {
			result.put("status", "fail");
			result.put("message", "网络异常，修改密码失败，请重试");
			return result;
		}
		if (StringUtils.isEmpty(pwdModel.getOldPwd())) {
			result.put("status", "fail");
			result.put("message", "网络异常，修改密码失败，请重试");
			return result;
		}
		if (StringUtils.isEmpty(pwdModel.getNewPwd())) {
			result.put("status", "fail");
			result.put("message", "网络异常，修改密码失败，请重试");
			return result;
		}

		// 得到代理人信息
		INSBAgent agentModel = insbAgentDao.selectById(pwdModel.getAgentId());
		if (agentModel == null) {
			result.put("status", "fail");
			result.put("message", "网络异常，修改密码失败，请重试");
			return result;
		}
		String oldMd5Pwd = StringUtil.md5Base64(pwdModel.getOldPwd());
		if (!StringUtils.isEmpty(oldMd5Pwd) && agentModel.getPwd() != null) {
			if (oldMd5Pwd.equals(agentModel.getPwd())) {
				String oldPwd = agentModel.getPwd();
				String newMd5Pwd = StringUtil.md5Base64(pwdModel.getNewPwd());
				agentModel.setPwd(newMd5Pwd);
				//修改ofuser表中的密码     
				OFUser ou=new OFUser();
				String username=agentModel.getJobnum();
				LogUtil.info("代理人工号：jobnum="+username);
				if(username!=null){
					ou.setUsername(username);
					Long time=new Date().getTime();
					ou.setModificationDate(time);	//修改时间
					ou.setPlainPassword(newMd5Pwd);	//修改加密的密码
				}else{
					result.put("status","fail");
					result.put("message","系统异常，修改密码失败，请重试");
					return result;
				}
				ofUserDao.updateByUserName(ou);
				LogUtil.info("changeAgentPwd 代理人修改密码 代理人："+agentModel.toString()+" 操作人:"+username+" 操作时间:"+ DateUtil.getCurrentDateTime());
//				insbAgentDao.updatePWDById(agentModel);
				updateAgentPwd(agentModel, oldPwd);//修改密码并发短信通知代理人
				redisClient.del(Constants.TOKEN, token);
				LogUtil.info("前端修改用户密码删除token成功，token=" + token);
				result.put("status", "success");
				result.put("message", "登录密码修改成功");
				/**
				 * 通知集团统一用户中心 hwc 添加 20170710
				 */
				try {
					Map<String, String> params = new HashMap<>();
					params.put("omobile", agentModel.getPhone());
					params.put("nmobile","");
					params.put("cardno", agentModel.getIdno());
					params.put("agentcode", agentModel.getAgentcode());
					params.put("password", agentModel.getPwd());
					params.put("passwordflag","1");
					userCenterService.updateUserAccount(params);
				}catch (Exception e){
					LogUtil.info("修改密码通知集团统一用户中心失败"+e.getMessage());
					e.printStackTrace();
				}
				return result;
			} else {
				result.put("status", "fail");
				result.put("message", "原登录密码错误，请重新输入");
				return result;
			}
		} else {
			result.put("status", "fail");
			result.put("message", "网络异常，修改密码失败，请重试");
			return result;
		}
	}

	public static void main(String[] args) {
		// AgentUpdatePwdVo pwdModel = new AgentUpdatePwdVo();
		// pwdModel.setAgentId("11111111111111111");
		// pwdModel.setNewPwd("123456");
		// pwdModel.setOldPwd("654321");
		// String aa = JSON.toJSONString(pwdModel);
		// System.out.println(aa);
		//INSBAgentServiceImpl b = new INSBAgentServiceImpl();
		// System.out.println(b.getIdnoTypebyHxType("02"));
		//System.out.println(b.getSexTypebyHxType("9"));
	}

	// 设置代理人 正式、启用、普通、权限
	/**
	 * 
	 * INSERT INTO `INSBAgentpermission` SELECT UUID(), a.id, '1','续保', '1','1',
	 * '2016-04-09 17:11:10',NULL,'2099-12-31 00:00:00','1',NULL,'2016-04-09
	 * 17:11:10', NULL,'admin_hxx',NULL,NULL FROM insbagent a, inscdept b WHERE
	 * NOT EXISTS (SELECT 1 FROM insbagentpermission s WHERE s.agentid = a.id
	 * AND s.permissionid ='1') AND a.deptid=b.id AND a.jobnum LIKE '6%' AND
	 * (b.parentcodes LIKE 'p+1000000000+1200000000+1244300000%' OR
	 * b.parentcodes LIKE 'p+1000000000+1200000000+1244200000%' OR b.parentcodes
	 * LIKE 'p+1000000000+1200000000+1244000000%' ) ;
	 */
	/**
	 * UPDATE insbagent SET agentkind = '2',agentstatus = '1' ,istest = '2'
	 * WHERE id in( SELECT * FROM ( SELECT a.id FROM insbagent a LEFT JOIN
	 * inscdept b ON a.deptid=b.id WHERE a.jobnum LIKE '6%' AND (b.parentcodes
	 * like CONCAT('p+1000000000+1200000000+1233000000','%') OR b.id =
	 * '1233000000' ) AND (a.agentkind <> '2' or a.agentstatus <> '1' or
	 * a.istest <> '2' ))v)
	 */
	public Map<String, Object> setAgentAttribute(String parentcodes,
			String deptid) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> param = new HashMap<String, String>();
		param.put("parentcodes", parentcodes);
		param.put("deptid", deptid);
		List<INSBAgent> agentlist = insbAgentDao.queryAgentBydept(param);
		List<String> error = new ArrayList<String>();
		int sum = 0;
		for (INSBAgent agent : agentlist) {
			try {
				agent.setAgentkind(2);// 正式
				agent.setAgentstatus(1);// 启用
				agent.setIstest(2);// 普通
				insbAgentDao.updateById(agent);
				sum++;
				result.put("status", "success");
				result.put("msg", sum);
			} catch (Exception e) {
				error.add(agent.getId());
				result.put("status", "fail");
				result.put("msg", sum);
			}
		}
		for (String str : error) {
			System.out.println(str);
		}
		return result;
	}

	@Override
	public int updateAgentPwd(INSBAgent agent, String oldPwd) {
		String newPwd = agent.getPwd();//代理人新密码
		String phone = agent.getPhone();
		int n = updateById(agent);
		if (n > 0 && oldPwd != null && newPwd != null && StringUtil.isNotEmpty(phone) && !newPwd.equals(oldPwd)) {
			LogUtil.info("工号%s密码发生改变，发送短信至手机号码为%s通知代理人", agent.getAgentcode(), phone);
			try {
				smsClientService.sendUpdateSuccessMessage(phone, agent.getAgentcode());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return n;
	}
}
