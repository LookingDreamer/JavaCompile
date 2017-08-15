package com.zzb.conf.manager.scm.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCDept;
import com.zzb.conf.dao.OFUserDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.OFUser;
import com.zzb.conf.manager.scm.INSBAgentSyncService;
import com.zzb.conf.service.INSBAgentService;


@Service
public class INSBAgentSyncServiceImpl  implements INSBAgentSyncService {
	@Resource
	private INSBAgentService insbAgentService;
	@Resource
	private OFUserDao ofUserDao;
	@Resource
	private INSCDeptDao inscDeptDao;
	
	@Override
	public INSBAgent getAgent(String jobnum) {
		return insbAgentService.getAgent(jobnum);
	}

	@Override
	public void saveAgent(INSBAgent agent) {
		insbAgentService.insert(agent);
	}

	@Override
	public void updateAgent(INSBAgent agent) {
		insbAgentService.updateById(agent);
	}

	@Override
	 /**
	 * 设置代理人数据
	 * 
	 * @param agent
	 * @param bean
	 * @return
	 */
	public INSBAgent getAgent(INSBAgent agent, INSBAgent bean) {
		agent.setJobnumtype(bean.getJobnumtype());
		agent.setName(bean.getName());
		agent.setSex(bean.getSex());
		agent.setJobnum(bean.getJobnum());
		if (StringUtil.isEmpty(agent.getPhone())) {
			agent.setPhone(bean.getPhone());
		}
		if (StringUtil.isEmpty(agent.getMobile())) {
			agent.setMobile(bean.getMobile());
		}
		agent.setEmail(bean.getEmail());
		agent.setDeptid(bean.getDeptid());
		agent.setAgentlevel(bean.getAgentlevel());
		agent.setAgentcode(bean.getAgentcode());
		agent.setAgentstatus(bean.getAgentstatus());
		agent.setAgentkind(bean.getAgentkind());
		// agent.setPwd(bean.getPwd());
		agent.setIstest(bean.getIstest());
		agent.setReferrer(bean.getReferrer());
		agent.setCgfns(bean.getCgfns());
		agent.setLicenseno(bean.getLicenseno());
		agent.setIdno(bean.getIdno());
		agent.setIdnotype(bean.getIdnotype());
		agent.setCreatetime(bean.getCreatetime());
		agent.setModifytime(bean.getModifytime());
		agent.setAddress(bean.getAddress());
		agent.setTeamcode(bean.getTeamcode());
		agent.setTeamname(bean.getTeamname());
		agent.setPlatformcode(bean.getPlatformcode());
		agent.setPlatformname(bean.getPlatformname());
		agent.setNoti(bean.getNoti());
		return agent;
	}

	@Override
	public INSCDept selectByComcode(String agentgroup) {
		return inscDeptDao.selectByComcode(agentgroup);
	}

	@Override
	public OFUser queryByUserName(String agentcode) {
		return ofUserDao.queryByUserName(agentcode);
	}

	@Override
	public void updateByUserName(OFUser ofuserBean) {
		ofUserDao.updateByUserName(ofuserBean);
	}

	@Override
	public void insertOFUser(OFUser ofuser) {
		ofUserDao.insert(ofuser);
	}


}