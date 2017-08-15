package com.zzb.conf.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.Resource;

import com.common.JsonUtil;
import com.zzb.conf.entity.INSBBwagent;
import com.zzb.conf.manager.scr.INSBBwagentManager;
import com.zzb.mobile.service.AppMarketingActivitiesService;
import com.zzb.mobile.service.UserCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.jobpool.Task;
import com.cninsure.jobpool.dispatch.DispatchTaskService;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.manager.scm.INSASyncService;
import com.cninsure.system.service.INSCDeptService;
import com.common.HttpClientUtil;
import com.zzb.cm.controller.vo.CertificationVo;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.dao.INSBCertificationDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBCertification;
import com.zzb.conf.entity.INSBUsercomment;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.conf.service.INSBCertificationService;
import com.zzb.conf.service.INSBUsercommentService;

import net.sf.json.JSONObject;

@Service
@Transactional
public class INSBCertificationServiceImpl extends BaseServiceImpl<INSBCertification> implements
	INSBCertificationService {
	@Resource
	private INSBCertificationDao insbCertificationDao;
	@Resource
	private INSBAgentService agentService;
	@Resource
	private INSBAgentDao insbAgentDao;
	@Resource
	private INSCDeptService deptService;
	@Resource
	private INSBUsercommentService insbUsercommentService;

	@Resource
	private INSBQuotetotalinfoDao iNSBQuotetotalinfoDao;

	@Autowired
	private AppMarketingActivitiesService appMarketingActivitiesService;

	@Resource
	private INSBBwagentManager insbBwagentManager;
	@Resource
	private INSBAgentService insbAgentService;
	@Resource
	private UserCenterService userCenterService;

	@Override
	public CertificationVo getCertificationInfo(CertificationVo certificationVo) {
		certificationVo = insbCertificationDao.getCertificationInfo(certificationVo);
		if(certificationVo.getReferrer()!=null && certificationVo!=null && !"".equals(certificationVo.getReferrer())){
			INSBAgent insbagent = new INSBAgent();
			insbagent.setJobnum(certificationVo.getReferrer());
			insbagent = insbAgentDao.selectOne(insbagent);
			if(insbagent!=null ){
				certificationVo.setReferrername(insbagent.getName());
			}
		}else {//没有推荐人则显示代理人所属团队信息
            INSBAgent insbagent = new INSBAgent();
            insbagent.setJobnum(certificationVo.getJobnum());
            insbagent = insbAgentDao.selectOne(insbagent);
            if(insbagent!=null){
                certificationVo.setDeptid(insbagent.getTeamcode());
                //设置团队名称
                if(StringUtil.isNotEmpty(insbagent.getTeamcode())){
                    INSCDept dept = deptService.getOrgDeptByDeptCode(insbagent.getTeamcode());
                    certificationVo.setDeptname(dept!=null?dept.getComname():"");
                }
            }
        }
		certificationVo.setPic(agentService.getCertificationPhotoPath(certificationVo.getAgentnum()));
		return certificationVo;
	}

	@Override
	public CertificationVo getOneCertificationInfo(CertificationVo certificationVo) {
		if(certificationVo != null && certificationVo.getId() != null) {
			certificationVo = insbCertificationDao.getOneCertificationInfo(certificationVo);
		}else{
			LogUtil.debug("查询认证信息异常，代理人id为空");
		}
		if(certificationVo !=null) {
			certificationVo.setPic(agentService.getCertificationPhotoPath(certificationVo.getAgentnum()));
		}
		return certificationVo;
	}

	@Override
	public int updateCertificationInfo(CertificationVo certificationVo, String operator) {
		INSBAgent agent = new INSBAgent();
		List<INSBAgent> agentList = null;
		Task task = new Task();
		task.setProInstanceId(certificationVo.getId());
		task.setSonProInstanceId(certificationVo.getId());
		task.setPrvcode(certificationVo.getDeptid());
		task.setTaskTracks(certificationVo.getDesignatedoperator());
		task.setTaskName("认证任务");
		certificationVo.setDesignatedoperator(operator);
		/*else if(StringUtil.isEmpty(certificationVo.getReferrer())  && !StringUtil.isEmpty(certificationVo.getReferrername())){
					LogUtil.info("认证任务注册推荐人工号不能为空:" + certificationVo.getReferrer()  + "##name=" + certificationVo.getReferrername());
					return 107;//推荐人工号不能为空
		}*/
		// LINING bug-2913-认证任务选择退回时没有地方填写给前端用户的备注 20160707 START
		if( StringUtil.isNotEmpty(certificationVo.getAgentid()) ) {
            LogUtil.info(certificationVo.getId()+"根据agentid查询备注");
			INSBUsercomment insbUsercomment = insbUsercommentService.selectUserCommentByTrackid(certificationVo.getAgentid(), 9);
            LogUtil.info(certificationVo.getId()+"根据agentid查询备注结束");
			if( insbUsercomment != null ) {
				insbUsercomment.setOperator(operator);
				insbUsercomment.setModifytime(new Date());
				insbUsercomment.setCommentcontent(certificationVo.getCommentcontent());
				insbUsercommentService.updateById(insbUsercomment);
			} else {
				insbUsercomment = new INSBUsercomment();
				insbUsercomment.setOperator(operator);
				insbUsercomment.setCreatetime(new Date());
				insbUsercomment.setModifytime(new Date());
				insbUsercomment.setCommentcontent(certificationVo.getCommentcontent());
				insbUsercomment.setTrackid(certificationVo.getAgentid());
				insbUsercomment.setTracktype(9);
				insbUsercommentService.insert(insbUsercomment);
			}
            LogUtil.info(certificationVo.getId()+"更新备注结束");
		}
		// LINING bug-2913-认证任务选择退回时没有地方填写给前端用户的备注 20160707 END
		if(certificationVo.getStatus()==0){//保存
			updateCertification(certificationVo);
			return updateAgent(certificationVo);
		}else if(certificationVo.getStatus()==1){//通过 才验证
			//判断注册手机号是否重复
			if(!StringUtil.isEmpty(certificationVo.getMobile())){
	            LogUtil.info(certificationVo.getId()+"根据手机号查询");
	            agent.setPlatformcode(certificationVo.getDeptid().substring(0,4));//在平台内查看是否有此工号存在
				agent.setPhone(certificationVo.getMobile());
				agentList = insbAgentDao.selectList(agent);
	            LogUtil.info(certificationVo.getId()+"根据手机号查询结束");
				if(agentList!=null && agentList.size()>0){
					if(!(agentList.size()==1 && agentList.get(0).getId().equals(certificationVo.getAgentid()))){
						LogUtil.info("认证任务注册手机号重复:" + certificationVo.getMobile() + "##size=" + agentList.size()
								+ "##agentid=" + certificationVo.getAgentid() + "##agentList.id=" + agentList.get(0).getId());
						return 100;//手机号重复码
					}
				}
				agent.setPhone(null);
			}
			//判断身份证号是否重复
			if(!StringUtil.isEmpty(certificationVo.getIdcard())){
	            LogUtil.info(certificationVo.getId()+"根据身份证号查询");
				agent.setIdno(certificationVo.getIdcard());
				agent.setPlatformcode(certificationVo.getDeptid().substring(0,4));//在平台内查看是否有此工号存在
//				agent.setIdnotype("0");//身份证code为0
				agentList = insbAgentDao.selectList(agent);
	            LogUtil.info(certificationVo.getId()+"根据身份证号查询结束");
				if(agentList!=null && agentList.size()>0){
					if(!(agentList.size()==1 && agentList.get(0).getId().equals(certificationVo.getAgentid()))){
						LogUtil.info("认证任务注册身份证号重复:" + certificationVo.getIdcard() + "##size=" + agentList.size()
								+ "##agentid=" + certificationVo.getAgentid() + "##agentList.id=" + agentList.get(0).getId());
						return 101;//身份证重复码
					}
				}
				agent.setIdno(null);
			}
			//判断资格证号是否重复
			if(!StringUtil.isEmpty(certificationVo.getCgfns())){
	            LogUtil.info(certificationVo.getId()+"根据资格证号查询");
				agent.setCgfns(certificationVo.getCgfns());
				agent.setPlatformcode(certificationVo.getDeptid().substring(0,4));//在平台内查看是否有此工号存在
				agentList = insbAgentDao.selectList(agent);
	            LogUtil.info(certificationVo.getId()+"根据资格证号查询结束");
				if(agentList!=null && agentList.size()>0){
					if(!(agentList.size()==1 && agentList.get(0).getId().equals(certificationVo.getAgentid()))){
						LogUtil.info("认证任务注册资格证号重复:" + certificationVo.getCgfns() + "##size=" + agentList.size()
								+ "##agentid=" + certificationVo.getAgentid() + "##agentList.id=" + agentList.get(0).getId());
						return 102;//资格证号重复码
					}
				}
				agent.setCgfns(null);
			}
			//判断正式工号是否重复
			if(!StringUtil.isEmpty(certificationVo.getFormalnum())){
	            LogUtil.info(certificationVo.getId()+"根据正式工号查询");
				agent.setJobnum(certificationVo.getFormalnum());
				agent.setPlatformcode(certificationVo.getDeptid().substring(0,4));//在平台内查看是否有此工号存在
				agent.setAgentstatus(1);//启用的工号
				agentList = insbAgentDao.selectList(agent);
	            LogUtil.info(certificationVo.getId()+"根据正式工号查询结束");
				if(agentList!=null && agentList.size()>0){
					if(!(agentList.size()==1 && agentList.get(0).getId().equals(certificationVo.getAgentid()))){
						LogUtil.info("认证任务注册正式工号重复:" + certificationVo.getFormalnum() + "##size=" + agentList.size()
								+ "##agentid=" + certificationVo.getAgentid() + "##agentList.id=" + agentList.get(0).getId());
						return 103;//正式工号重复码
					}
				}
			}
			//判断推荐人工号与推荐人姓名是否相符
			if(!StringUtil.isEmpty(certificationVo.getReferrer())  && !StringUtil.isEmpty(certificationVo.getReferrername()) ){
	            LogUtil.info(certificationVo.getId()+"根据推荐人工号查询");
				agent.setJobnum(certificationVo.getReferrer());
				agentList = insbAgentDao.selectList(agent);
	            LogUtil.info(certificationVo.getId()+"根据推荐人工号查询结束");
				if(agentList!=null && agentList.size()>0){
					if(!(agentList.size()==1 && agentList.get(0).getName().equals(certificationVo.getReferrername()))){
						LogUtil.info("认证任务注册推荐人与推荐人工号不符:" + certificationVo.getReferrer() + "##size=" + agentList.size()
								+ "##name=" + certificationVo.getReferrername() + "##agentList.name=" + agentList.get(0).getName());
						return 104;//推荐人工号与推荐人姓名不符
					}
				}else{
						LogUtil.info("认证任务注册推荐人工号不存在:" + certificationVo.getReferrer() + "##name=" + certificationVo.getReferrername());
						return 105;//推荐人工号与推荐人工号不存在
				}
			}else if( !StringUtil.isEmpty(certificationVo.getReferrer())  && StringUtil.isEmpty(certificationVo.getReferrername())){
						LogUtil.info("认证任务注册推荐人姓名不能为空:" + certificationVo.getReferrer()  + "##name=" + certificationVo.getReferrername());
						return 106;//推荐人姓名不能为空
			}

			int flag = updateAgent(certificationVo);
			if(0==flag||-1==flag){
				return flag;
			}else{
				try {
					/*20170619 hwc 去除旧版懒掌柜相关代码
					LogUtil.info("===代理人升级审核通过---升级懒掌柜---代理人信息---agent="+certificationVo);
					userAPIService.up2LZGManager(certificationVo.getAgentid());
                    LogUtil.info("===代理人升级审核通过---升级懒掌柜---代理人信息---结束=");*/
					//试用用户通过认证升级为正式时，将他的试用权限包删除
                    LogUtil.info(certificationVo.getId()+"删除试用权限包");
					agent.setSetid("");
					insbAgentDao.updateSetIdById(agent);
                    LogUtil.info(certificationVo.getId()+"删除试用权限包结束");

					//临时代理人的单更新为正式代理人的单
                    LogUtil.info(certificationVo.getId()+"临时代理人的单更新为正式代理人的单"+certificationVo.getTempjobnum()+">"+certificationVo.getFormalnum());
					insbAgentService.updateOrderInsuranceInfo(certificationVo.getFormalnum(), certificationVo.getTempjobnum());
					//updateINSBQuotetotalinfo(certificationVo.getTempjobnum(), certificationVo.getFormalnum());
                    LogUtil.info(certificationVo.getId()+"临时代理人的单更新为正式代理人的单"+certificationVo.getTempjobnum()+">"+certificationVo.getFormalnum()+"结束");


				} catch (Exception e) {
					e.printStackTrace();
				}
				dispatchService.completeTask(task, "certificateDel");//通知调度删除任务

				String agentCode = certificationVo.getFormalnum(); //代理人的正式工号
				String referee = certificationVo.getReferrer(); //代理人的推荐人工号
				LogUtil.info("updateCertificationInfo绑定正式工号,代理人正式工号：" + agentCode + ", 推荐人工号：" + referee + ",参加营销活动 ");
				//INSBAgent agentNew = insbAgentDao.selectAgentByAgentCode(agentCode);
				//appMarketingActivitiesService.isCanParticipateMarketing(agentCode, referee , agentNew.getRegistertime());
				/*try {
					KafkaClientHelper.collect("insbcertification", JSONObject.fromObject(certificationVo).toString());
				} catch (Exception e) {
					LogUtil.error("收集激活用户数据错误"+certificationVo);
					e.printStackTrace();
				}*/
				LogUtil.info("insbcertification|报表数据埋点|"+JSONObject.fromObject(certificationVo).toString());

				return updateCertification(certificationVo);
			}
		}else if(certificationVo.getStatus()==2){//退回
			updateAgent(certificationVo);
			dispatchService.completeTask(task,"certificateDel");//通知调度删除任务
			return updateCertification(certificationVo);
		}else if(certificationVo.getStatus()==3){//认证失败
			updateAgent(certificationVo);
			dispatchService.completeTask(task,"certificateDel");//通知调度删除任务
			return updateCertification(certificationVo);
		}else{
			return 0;
		}
	}


	@Resource
	private DispatchTaskService dispatchService;
	private void updateINSBQuotetotalinfo(String tempjobnum, String formalnum) {
        if (StringUtil.isEmpty(tempjobnum) || StringUtil.isEmpty(formalnum) || tempjobnum.equals(formalnum)) return;

        INSBQuotetotalinfo query = new INSBQuotetotalinfo();

		query.setAgentnum(tempjobnum);
		List<INSBQuotetotalinfo> list = iNSBQuotetotalinfoDao.selectList(query);
		for (INSBQuotetotalinfo insbQuotetotalinfo : list) {
			insbQuotetotalinfo.setAgentnum(formalnum);
			iNSBQuotetotalinfoDao.updateById(insbQuotetotalinfo);
		}
	}
	private int updateCertification(CertificationVo certificationVo) {
        LogUtil.info(certificationVo.getId()+"更新认证状态"+certificationVo.getStatus());
		INSBCertification certification = new INSBCertification();
		certification.setId(certificationVo.getId());
		certification.setStatus(certificationVo.getStatus());
		certification.setRank(certificationVo.getRank());
		certification.setBelongs2bank(certificationVo.getBelongs2bank());
		certification.setBankcard(certificationVo.getBankcard());
		int result = insbCertificationDao.updateById(certification);
        LogUtil.info(certificationVo.getId()+"更新认证状态"+certificationVo.getStatus()+"结束:"+result);
        return result;
	}

	private int updateAgent(CertificationVo certificationVo) {
		//判断正式工号唯一
//		if(certificationVo.getStatus()==2|| StringUtil.isEmpty(certificationVo.getFormalnum()) || agentService.getOnlyAgent(certificationVo)){
            LogUtil.info(certificationVo.getId()+"根据agentid更新代理人信息");
			INSBAgent agent = new INSBAgent();
			agent.setId(certificationVo.getAgentid());
			//agent.setBankcard(certificationVo.getBankcard());
			//agent.setBelongs2bank(certificationVo.getBelongs2bank());
			agent.setMainbusiness(certificationVo.getMainbiz());
			//agent.setDeptid(certificationVo.getDeptid());
			agent.setChannelid(certificationVo.getChannelid());
			agent.setName(certificationVo.getName());
			agent.setLivingcityid(certificationVo.getRegion());
			agent.setMobile(certificationVo.getMobile());
			agent.setIdno(certificationVo.getIdcard());
			agent.setCgfns(certificationVo.getCgfns());
            agent.setTeamcode(certificationVo.getDeptid());
			if(certificationVo.getReferrer()==null){
				agent.setReferrer("");
			}else{
				agent.setReferrer(certificationVo.getReferrer());
			}
			/*if (StringUtil.isNotEmpty(certificationVo.getRank()) && certificationVo.getRank().length() > 1) {
				agent.setAgentlevel(Integer.valueOf(certificationVo.getRank()
						.trim().substring(1)));
			}*/
			if(1==certificationVo.getStatus()){//认证通过
				Date now = new Date();
				agent.setApprovesstate(3);
				agent.setJobnumtype(2);
				agent.setAgentkind(2);
				agent.setTempjobnum(agent.getJobnum());
				agent.setAgentcode(certificationVo.getFormalnum());
				agent.setJobnum(certificationVo.getFormalnum());
				agent.setActivetime(now);
				agent.setTesttime(now);//认证通过时间
				agentService.updateOrderInsuranceInfo(certificationVo.getFormalnum(), agent.getJobnum());
			}else if(2==certificationVo.getStatus()){//退回修改
				agent.setAgentkind(1);
				agent.setApprovesstate(5);//退回修改
				agent.setJobnumtype(1);
			}else if(3==certificationVo.getStatus()){//认证不通过
				agent.setAgentkind(1);
				agent.setApprovesstate(4);
				agent.setJobnumtype(1);
			}
			int i = agentService.updateById(agent);
//			if(i != 0 && 1==certificationVo.getStatus() && "fail".equals(updateAgentInLdap(certificationVo))){
//				return -1;
//			}
            LogUtil.info(certificationVo.getId()+"根据agentid更新代理人信息结束");
			return i;
//		}else{
//			return -1;
//		}
	}
	private String updateAgentInLdap(CertificationVo certificationVo) {
		//LdapMgr ldap = new LdapMgr();
		//if(ldap.modifyJobNumberAndMobile(certificationVo.getTempjobnum(),certificationVo.getFormalnum(), certificationVo.getMobile())){
			return "success";
		//}else{ 
		//	return "fail";
		//}
	}

	@Resource
	private INSASyncService agtService;
	@Override
	public Map<String, String> getFormalNum(CertificationVo certificationVo, String operator) {
		/*LDAP版本 申请工号
		INSBAgent temp = new INSBAgent();
		temp.setId(certificationVo.getAgentid());
		INSBAgent agent = agentService.getAgentInfo(temp);
		调接口
		CmLdapUtil ldapUtil = CmLdapUtil.getInstance();
		System.out.println("###########"+ldapUtil.getJobNumByParam(agent.getMobile(), agent.getIdnotype(), agent.getIdno()));
		return ldapUtil.getJobNumByParam(agent.getMobile(), agent.getIdnotype(), agent.getIdno());
		return "120989999";*/
		//接口path
		Map<String, String> result = new HashMap<String, String>();
		ResourceBundle resourceBundle = ResourceBundle.getBundle("config/config");
		String coreAddress= resourceBundle.getString("core.url");
		String path = coreAddress + "/AgentInfo/InfoQuery";
		//组织入参
		INSBAgent temp = new INSBAgent();
		temp.setId(certificationVo.getAgentid());
		INSBAgent agent = agentService.getAgentInfo(temp);
		INSBAgent referrer = null;
		if (StringUtil.isNotEmpty(agent.getReferrer())) {
			referrer = insbAgentDao.selectAgentByAgentCode(certificationVo.getReferrer());
            //推荐人必须为正式代理人（2016-12-06）
            if(referrer!=null&&referrer.getAgentkind()==1){
                result.put("status", "fail");
                result.put("agentcode", "推荐人还未认证");
                return result;
            }
			List<INSBBwagent> bwaList = insbBwagentManager.getBwagentData(null, agent.getReferrer(),null);
			if (bwaList != null && bwaList.size() > 0) {
				for (int i = 0; i < bwaList.size(); i++) {
					INSBBwagent bwagent = bwaList.get(i);
					//若是同一个代理人同步过来有多条数据则取在职状态的数据(例如在A地离职后在B地工作->工作地点转移)
					if (bwaList.size() > 1 && StringUtil.isNotEmpty(bwagent.getAgentstate()) && bwagent.getAgentstate().compareTo("02") > 0) {
						LogUtil.info(bwagent.getAgentcode() + "同步过来的数据有两条，该记录为离职状态");
						continue;
					}
					if (referrer != null) {
						referrer.setTeamcode(bwagent.getAgentgroup());
					}
					agent.setTeamcode(bwagent.getAgentgroup());
					agent.setDeptid(bwagent.getMgmtdivision());
				}
			}
		}else {//没有推荐人则校验代理人团队信息是否存在
            if(StringUtil.isEmpty(agent.getTeamcode())){
                result.put("status", "fail");
                result.put("agentcode", "请选择代理人团队");
                return result;
            }
        }
		INSCDept legalPersonDept = deptService.getLegalPersonDept(agent.getDeptid());//查询法人机构编码
		Map<String,String> map = new HashMap<String,String>();
		map.put("agentcode", "");//代理人工号
		map.put("agentname", StringUtil.trim(agent.getName()));//代理人真实姓名
		map.put("idno", agent.getIdno());//身份证号
		map.put("certifno", "");//资格证号

		INSBCertification certification = insbCertificationDao.selectById(certificationVo.getId());
		map.put("rank", certification == null ? "S01":certification.getRank());//业务员职级（编码）
		map.put("account", certification == null ? "":certification.getBankcard());//银行账号
		map.put("bank", certification == null ? "":certification.getBelongs2bank());//开户银行（编码）
		map.put("introagency", certificationVo == null ? "" : certificationVo.getReferrer());//推荐人编码
		map.put("agencyname", certificationVo == null ? "" : certificationVo.getReferrername());//推荐人姓名
		map.put("mobile", agent.getMobile());//手机号码
		map.put("biztype", "02");//财寿险标志
		map.put("managecom", legalPersonDept.getId());//法人机构编码
		map.put("branchoffice", "");//分公司编码
		map.put("mgmtdivision", agent.getDeptid());//网点（营销部）
		map.put("agentgroup",  referrer == null ? agent.getTeamcode() : referrer.getTeamcode());//团队
		//接口调用
		LogUtil.info("请求核心path" + path);
		LogUtil.info(agent.getIdno() + "idno请求核心map"+ JSONObject.fromObject(map).toString());
        String jsonString = HttpClientUtil.doPostJsonWithTimeout(path,map,null,30000);
		if (StringUtil.isEmpty(jsonString)) {
			result.put("status", "fail");
			result.put("agentcode", "核心返回错误");
			return result;
		}
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		LogUtil.info(agent.getIdno() + "idno请求核心返回结果：" + jsonObject.toString());
		if("0".equals(jsonObject.getString("resultcode"))){
			certificationVo.setFormalnum(jsonObject.getString("agentcode"));
			int updateStatu = updateCertificationInfo(certificationVo, operator);
			result.put("updateStatu", updateStatu+"");
			result.put("status", "success");
			result.put("agentcode", jsonObject.getString("agentcode"));
			result.put("result", jsonObject.getString("result"));

			agtService.getAgentData("admin", agent.getReferrer());

			if("Y".equals(jsonObject.getString("result")))//同步核心代理人数据到cm
				agtService.getAgentData("admin",jsonObject.getString("agentcode"));
            //更新推荐人为该临时工号对应的正式工号
            if (StringUtil.isNotEmpty(agent.getTempjobnum())){
                Map<String,Object> paramMap = new HashMap<>();
                paramMap.put("agentcode",jsonObject.getString("agentcode"));
                paramMap.put("tempjobnum",agent.getTempjobnum());
                insbAgentDao.updateByReferrer(paramMap);
            }
			//20170601 hwc 实名认证通过后通知集团统一中心
			LogUtil.info("实名认证通过调用updateUserDetail"+ JsonUtil.serialize(agent)+" agentcode="+jsonObject.getString("agentcode"));
			userCenterService.updateUserDetail(agent,jsonObject.getString("agentcode"),certificationVo.getReferrer());
			//20170601 hwc end
			return result;
		}else{
			result.put("status", "fail");
			result.put("agentcode", jsonObject.getString("errorinfo"));
			return result;
		}
	}

	@Override
	public int updateDesignatedoperator(INSBCertification certification) {
		return insbCertificationDao.updateDesignatedoperator(certification);
	}

	/**
	 * 分页认证任务列表
	 *
	 * @param query
	 * @return
	 */
	@Override
	public Map<String, Object> getCertificationPage(Map<String, Object> query) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total",insbCertificationDao.getCertificationCount(query));
		map.put("rows", insbCertificationDao.getCertificationPage(query));
		return map;
	}

	@Override
	protected BaseDao<INSBCertification> getBaseDao() {
		return insbCertificationDao;
	}

	

}