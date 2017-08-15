package com.zzb.extra.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCUser;
import com.common.redis.Constants;
import com.common.redis.IRedisClient;
import com.zzb.cm.dao.INSBFilebusinessDao;
import com.zzb.cm.entity.INSBFilebusiness;
import com.zzb.cm.service.INSBFilelibraryService;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.dao.OFUserDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBCertification;
import com.zzb.conf.entity.OFUser;
import com.zzb.conf.service.INSBCertificationService;
import com.zzb.extra.dao.INSBAgentUserDao;
import com.zzb.extra.dao.INSBMiniUserRoleDao;
import com.zzb.extra.entity.INSBMiniUserRole;
import com.zzb.extra.model.INSBAgentUserQueryModel;
import com.zzb.extra.service.INSBAgentUserService;
import com.zzb.extra.util.AgentUtils;
import com.zzb.mobile.model.CommonModel;

@Service
@Transactional
public class INSBAgentUserServiceImpl  extends BaseServiceImpl<INSBAgent> implements INSBAgentUserService {
    public static final String TEMP_AGENT_JOB_NO = "temp_agent_job_no";
    @Resource
    private INSBAgentDao insbAgentDao;
    @Resource
    private INSBAgentUserDao insbAgentUserDao;
    @Resource
    private INSCCodeDao inscCodeDao;
    @Resource
    private INSCDeptDao inscDeptDao;
    @Resource
    private INSBMiniUserRoleDao insbMiniUserRoleDao;
    @Override
    protected BaseDao<INSBAgent> getBaseDao() {
        return insbAgentDao;
    }

    @Resource
    private INSBFilelibraryService insbFilelibraryService;

    @Resource
    private INSBFilebusinessDao insbFilebusinessDao;

    @Resource
    private INSBCertificationService insbCertificationService;
//    @Override
//    public List<Map<String, Object>> getQueryPageData() {
//        return null;
//    }

    @Resource
    private IRedisClient redisClient;

    @Override
    public List<Map<String, Object>> getQueryPageData() {
        return null;
    }

    @Resource
    private OFUserDao ofUserDao;

    @Override
    public Map<String, Object> getAgentUserListPage(Map<String, Object> map) {
        Map<String, Object> result = new HashMap<String, Object>();
		/* List<INSBAgent> agentList = insbAgentDao.selectListPage(map); */
        List<INSBAgentUserQueryModel> agentList = insbAgentUserDao.selectListPageByDeptIds_byAgentUser(map);
        // TODO 确定后加入字典表
        for (INSBAgentUserQueryModel model : agentList) {
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

            if (model.getRegistertime() != null) {
                model.setRegistertimestr(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(model.getRegistertime()));
            }

            if (model.getLsatlogintime() != null) {
                model.setLsatlogintimestr(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(model.getLsatlogintime()));
            }

            if (model.getApprovesstate() == null) {
                model.setApprovesstatestr("未认证 ");
            } else if (model.getApprovesstate() == 1) {
                model.setApprovesstatestr("未认证 ");
            } else if (model.getApprovesstate() == 2) {
                model.setApprovesstatestr("认证中");
            } else if (model.getApprovesstate() == 3) {
                model.setApprovesstatestr("认证通过");
            } else if (model.getApprovesstate() == 4) {
                model.setApprovesstatestr("认证失败");
            }

        }
        result.put("total", insbAgentDao.selectCountByMap_byAgentUser(map));
        result.put("rows", agentList);
        return result;
    }

    @Override
    public Map<String, Object> main2edit(String agentId) {
        Map<String, Object> result = new HashMap<String, Object>();

        // 查询所有功能包信息
        INSBAgent agent = new INSBAgent();

        // 存储常用网点名称
        List<String> commonUseBranchNames = new ArrayList<String>();

        // 证件类型
        List<Map<String, Object>> certKinds = inscCodeDao
                .selectByType("CertKinds");
        // 代理人级别
        List<Map<String, Object>> agentlevelvalue = inscCodeDao
                .selectByType("agentlevel");

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
            if(agent!=null){
                if (agent.getApprovesstate() == null) {
                    agent.setApprovesstatestr("未认证 ");
                } else if (agent.getApprovesstate() == 1) {
                    agent.setApprovesstatestr("未认证 ");
                } else if (agent.getApprovesstate() == 2) {
                    agent.setApprovesstatestr("认证中");
                } else if (agent.getApprovesstate() == 3) {
                    agent.setApprovesstatestr("认证通过");
                } else if (agent.getApprovesstate() == 4) {
                    agent.setApprovesstatestr("认证失败");
                }
            }
        } else {
            agent.setAgentcode(this.updateAgentTempJobNo() + "");
        }

        // 初始化审核状态
        List<Map<String, Object>> approve = inscCodeDao
                .selectByType("approvestatus");

        result.put("agentlevelvalue", agentlevelvalue);// 代理人级别
        result.put("certKinds", certKinds);// 证件类型
        result.put("agent", agent);
        result.put("approve", approve);
        String commonUseBranch = commonUseBranchNames.toString();
        result.put("commonUseBranchNames",
                commonUseBranch.substring(1, commonUseBranch.length() - 1));
        return result;
    }

    //参考：com.zzb.mobile.service.AppRegisteredService.fileUpLoadBase64
    @Override
    public CommonModel fileUpLoadBase64(HttpServletRequest request, String file, String fileName, String fileType,
                                        String fileDescribes, String jobNum, String taskId) {
        CommonModel model = new CommonModel();
        INSBAgent insbAgent = new INSBAgent();
        //insbAgent.setJobnum(jobNum);
        insbAgent.setId(jobNum);
        insbAgent = insbAgentDao.selectOne(insbAgent);//insbAgentService.queryOne(insbAgent);
        if (insbAgent == null) {
            model.setStatus("fail");
            model.setMessage("代理人用户不存在");
            return model;
        }
        // 2015-11-25日修改,可以不用上传流程id
        // if(StringUtil.isEmpty(taskId)){
        // model.setStatus("fail");
        // model.setMessage("任务流水号不能为空");
        // return model;
        // }
        Map<String, Object> map = insbFilelibraryService.uploadOneFile(request, file, fileName, fileType, fileDescribes,
                insbAgent.getId());//insbAgent.getAgentcode()
        if (map.get("status").equals("success")) {
            model.setStatus("success");
            model.setMessage("上传成功");
            model.setBody(map);

            INSBFilebusiness insbFilebusiness = new INSBFilebusiness();
            insbFilebusiness.setCreatetime(new Date());
            insbFilebusiness.setOperator(null == jobNum ? "" : jobNum);
            insbFilebusiness.setType(fileType);
            insbFilebusiness.setFilelibraryid((String) map.get("fileid"));
            if (!StringUtils.isEmpty(taskId))
                insbFilebusiness.setCode(taskId);
            else
                insbFilebusiness.setCode("");
            insbFilebusinessDao.insert(insbFilebusiness);

            //用户认证表信息
            INSBCertification queryCertification = new INSBCertification();
            queryCertification.setAgentnum(jobNum);
            queryCertification = insbCertificationService.queryOne(queryCertification);
            INSBCertification insbCertification = null;
            if (StringUtil.isEmpty(queryCertification)) {
                insbCertification = new INSBCertification();
                insbCertification.setId(UUIDUtils.random());
                insbCertification.setCreatetime(new Date());
                insbCertification.setModifytime(new Date());
            } else {
                insbCertification = queryCertification;
            }
            insbCertification.setOperator(insbAgent.getJobnum());
            //insbCertification.setNoti(noti);
            //insbCertification.setIdcardopposite(idCardPhotoB);
            //insbCertification.setBankcardpositive(bankCardA);
            //insbCertification.setQualificationpositive(qualificationA);
            //insbCertification.setQualificationpage(qualificationPage);
            //insbCertification.setDeptid(insbAgent.getDeptid());
            insbCertification.setAgree(0);
            //insbCertification.setMainbiz(mainBiz);// 主营业务
            //insbCertification.setDesignatedoperator(null);
            insbCertification.setAgentnum(jobNum);
            insbCertification.setIdcardpositive((String) map.get("fileid"));
            insbCertification.setStatus(0);
            if (StringUtil.isEmpty(queryCertification)) {
                insbCertificationService.insert(insbCertification);
            } else {
                insbCertification.setModifytime(new Date());
                insbCertificationService.updateById(insbCertification);
                //insbCertificationService.updateDesignatedoperator(insbCertification);
            }

        } else {
            model.setStatus("fail");
            model.setMessage((String) map.get("msg"));
        }
        return model;
    }


    //  参考：com.zzb.mobile.service.AppRegisteredService.fileUpLoadWeChat
    public CommonModel fileUpLoadWeChat(HttpServletRequest request, String mediaId, String fileName, String fileType,
                                        String fileDescribes, String jobNum, String taskId) {
        CommonModel model = new CommonModel();
        INSBAgent insbAgent = new INSBAgent();
        //insbAgent.setJobnum(jobNum);
        insbAgent.setId(jobNum);
        insbAgent = insbAgentDao.selectOne(insbAgent);//insbAgentService.queryOne(insbAgent);
        if (insbAgent == null) {
            model.setStatus("fail");
            model.setMessage("代理人用户不存在");
            return model;
        }
        Map<String, Object> map = insbFilelibraryService.saveFileInfo(request, mediaId, fileName, fileType, fileDescribes,
                insbAgent.getId()); //insbAgent.getAgentcode()
        if (map.get("status").equals("success")) {
            model.setStatus("success");
            model.setMessage("上传成功");
            model.setBody(map);

            INSBFilebusiness insbFilebusiness = new INSBFilebusiness();
            insbFilebusiness.setCreatetime(new Date());
            insbFilebusiness.setOperator(null == jobNum ? "" : jobNum);
            insbFilebusiness.setType(fileType);
            insbFilebusiness.setFilelibraryid((String) map.get("fileid"));
            if (!StringUtils.isEmpty(taskId))
                insbFilebusiness.setCode(taskId);
            else
                insbFilebusiness.setCode("");
            insbFilebusinessDao.insert(insbFilebusiness);

            //用户认证表信息
            INSBCertification queryCertification = new INSBCertification();
            queryCertification.setAgentnum(jobNum);
            queryCertification = insbCertificationService.queryOne(queryCertification);
            INSBCertification insbCertification = null;
            if (StringUtil.isEmpty(queryCertification)) {
                insbCertification = new INSBCertification();
                insbCertification.setId(UUIDUtils.random());
                insbCertification.setCreatetime(new Date());
                insbCertification.setModifytime(new Date());
            } else {
                insbCertification = queryCertification;
            }
            insbCertification.setOperator(insbAgent.getJobnum());
            //insbCertification.setNoti(noti);
            //insbCertification.setIdcardopposite(idCardPhotoB);
            //insbCertification.setBankcardpositive(bankCardA);
            //insbCertification.setQualificationpositive(qualificationA);
            //insbCertification.setQualificationpage(qualificationPage);
            //insbCertification.setDeptid(insbAgent.getDeptid());
            insbCertification.setAgree(0);
            //insbCertification.setMainbiz(mainBiz);// 主营业务
            //insbCertification.setDesignatedoperator(null);
            insbCertification.setAgentnum(jobNum);
            insbCertification.setIdcardpositive((String) map.get("fileid"));
            insbCertification.setStatus(0);
            if (StringUtil.isEmpty(queryCertification)) {
                insbCertificationService.insert(insbCertification);
            } else {
                insbCertification.setModifytime(new Date());
                insbCertificationService.updateById(insbCertification);
                //insbCertificationService.updateDesignatedoperator(insbCertification);
            }

        } else {
            model.setStatus("fail");
            model.setMessage((String) map.get("msg"));
        }
        return model;
    }

    /**
     * 得到临时工号 并更新
     *
     * @return
     */
    public int updateAgentTempJobNo() {

        // redis中有数据
        long tempJobNo = redisClient.atomicIncr(Constants.CM_ZZB, TEMP_AGENT_JOB_NO);
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
                redisClient.set(Constants.CM_ZZB, TEMP_AGENT_JOB_NO, Integer.parseInt(codeModel.getCodevalue()) + 10 + "");
                tempJobNo = Integer.parseInt(codeModel.getCodevalue()) + 10;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return (int) tempJobNo;
    }


    /**
     * 参考：INSBAgentServiceImpl 的 saveOrUpdateAgent方法。
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public INSBAgent saveOrUpdateAgentWeiXin(INSBAgent agent) {
//		String nickName = agent.getNickname() != null ? agent.getNickname().replaceAll("\\\\x[A-Za-z0-9]{2}", "").replace("\\","") :null;
//        String nickName = agent.getNickname() != null ? agent.getNickname().replaceAll("[\\\\ud83c\\\\udc00-\\\\ud83c\\\\udfff]|[\\\\ud83d\\\\udc00-\\\\ud83d\\\\udfff]|[\\\\u2600-\\\\u27ff]", ""):null; //将emoji表情替换成空串
//        agent.setNickname(nickName); // 处理特殊字符
        INSBAgent insbAgent = insbAgentUserDao.selectByOpenid(agent.getOpenid());
        String id = "";
        // 修改
        if (insbAgent != null && insbAgent.getId() != null
                && !"".equals(insbAgent.getId())) {
            agent.setId(insbAgent.getId());
            AgentUtils.copySouceToTarget(agent, insbAgent);
            insbAgent.setModifytime(new Date());
            insbAgent.setLsatlogintime(new Date());
            // insbAgent.setOperator(user.getUsercode()); //微信请求没登录cm
            // insbAgent.setPhone(agent.getMobile());
            insbAgentDao.updateById(insbAgent);
            OFUser ofuser = ofUserDao.queryByUserName(insbAgent.getAgentcode());
            if (ofuser == null) {
                ofuser = new OFUser();
                ofuser.setUsername(insbAgent.getAgentcode());
//				ofuser.setPlainPassword("123456");
                ofuser.setPlainPassword(StringUtil.md5Base64("123456"));
                ofuser.setName(insbAgent.getName());
                ofuser.setCreationDate(new Date().getTime());
                ofuser.setModificationDate(new Date().getTime());
                ofUserDao.insert(ofuser);
            }
            return insbAgent;
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
            try {
                OFUser ofuser = new OFUser();
                ofuser.setUsername(tempJobNo);
//			ofuser.setPlainPassword("123456");
                ofuser.setPlainPassword(StringUtil.md5Base64("123456"));
                ofuser.setName(agent.getName());
                ofuser.setCreationDate(new Date().getTime());
                ofuser.setModificationDate(new Date().getTime());
                ofUserDao.insert(ofuser);
            }catch(Exception ex){
             ex.printStackTrace();
                }
            agent.setCreatetime(new Date());
            agent.setRegistertime(new Date());
            agent.setLsatlogintime(new Date());
            // agent.setOperator(user.getUsercode());
            // agent.setPhone(agent.getMobile());
            agent.setPwd(StringUtil.md5Base64("123456"));
            agent.setTempjobnum(tempJobNo);
            agent.setJobnum(tempJobNo); // 正式工号暂与试用工号相同
            agent.setJobnumtype(1);
            agent.setAgentstatus(1);
            agent.setApprovesstate(1);// 新建未上传身份证，状态1未认证
            agent.setAgentcode(tempJobNo);
            if (agent.getDeptid() == null) {
                agent.setDeptid("00000"); // 此处插入一条代理人记录，为非正式代理人的临时工号，没有deptid属性，以00000代替
            }
            id = insbAgentDao.insertReturnId(agent);
            agent.setId(id);
            /**
             * 设置权限管理
             */
            INSBMiniUserRole insbMiniUserRole = new INSBMiniUserRole();
            insbMiniUserRole.setMiniuserid(id);
            LogUtil.info("==>新注册用户默认为普通成员!!");
            insbMiniUserRole.setRoleid("9c979c2acf80ccb6ebde0ae0907f77e6");
            insbMiniUserRole.setCreatetime(new Date());
            insbMiniUserRole.setId(UUIDUtils.random());
            insbMiniUserRoleDao.saveObject(insbMiniUserRole);
            return agent;
        }
    }

    /**
     *
     * 绑定
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int bindingAgentIdentity(INSBAgent agent) {
        // 修改
        if (agent != null && agent.getOpenid() != null
                && !"".equals(agent.getOpenid().trim())
                && agent.getPhone() != null
                && !"".equals(agent.getPhone().trim())) {
            // agent.setModifytime(new Date());
            // agent.setOperator(user.getUsercode());
            // agent.setPhone(agent.getMobile());
            return insbAgentUserDao.bindingAgentIdentity(agent);
        } else {
            // 代理人id为空，绑定失败
            LogUtil.info("更新代理人绑定手机失败，openid 或 手机号 为空");
            return 0;
        }
    }

    /**
     *
     * 审核代理人身份信息
     *
     * @param user
     *            操作用户
     * @param agent
     *            代理人信息
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public String auditAgentIdentity(INSCUser user, INSBAgent agent) {
        String id = "";
        // 修改
        if (agent.getId() != null && !"".equals(agent.getId())) {
            agent.setModifytime(new Date());
            agent.setOperator(user.getUsercode());
            // agent.setPhone(agent.getMobile());
            insbAgentUserDao.auditAgentIdentity(agent);
            return agent.getId();
        } else {
            // 代理人id为空，审核失败
            LogUtil.info("更新代理人审核数据失败，id为空");
            return id;
        }
    }

    /**
     * 按微信openid查询用户信息
     *
     * @param openid
     * @return
     */
    @Override
    public INSBAgent selectByOpenid(String openid) {
        return insbAgentUserDao.selectByOpenid(openid);
    }

    /**
     * 用户身份验证接口（检查用户提交的认证信息：openid 是否有绑定 手机号）
     *
     * @param agent
     *            代理人信息
     * @return
     */
    public INSBAgent validateAgentIdentity(INSBAgent agent) {
        if (agent != null && agent.getOpenid() != null
                && !"".equals(agent.getOpenid().trim())) {
            Map<String, Object> map = BeanUtils.toMap(agent);
            return insbAgentUserDao.validateAgentIdentity(map);
        } else {
            LogUtil.info("openid为空，校验身份失败");
            return null;
        }
    }

    /**
     * 查询手机号是否 已经是 体系内代理人绑定手机 true是，false不是
     */
    public boolean checkIsAgentPhone(String phone) {
        return insbAgentUserDao.checkIsAgentPhone(phone);
    }

    /**
     * 被推荐人 列表
     *
     * @param referrerid
     *            推荐人id
     */
    public List<Map<String, Object>> presenteeList(String referrerid) {
        return insbAgentUserDao.presenteeList(referrerid);
    }

    /**
     * 查询手机号是否 已经是 minizzb用户绑定手机 true是，false不是
     */
    public boolean checkIsMinizzbUserPhone(String phone) {
        return insbAgentUserDao.checkIsMinizzbUserPhone(phone);
    }

	@Override
	public Map<String, Object> getAgentPhoneUserListPage(Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();
		/* List<INSBAgent> agentList = insbAgentDao.selectListPage(map); */
		List<INSBAgentUserQueryModel> agentResult = new ArrayList<>();
        List<INSBAgentUserQueryModel> agentList = insbAgentUserDao.selectListPageByDeptIds_byAgentUser(map);
        // TODO 确定后加入字典表
        for (INSBAgentUserQueryModel model : agentList) {
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

            if (model.getRegistertime() != null) {
                model.setRegistertimestr(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(model.getRegistertime()));
            }

            if (model.getLsatlogintime() != null) {
                model.setLsatlogintimestr(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(model.getLsatlogintime()));
            }

            if (model.getApprovesstate() == null) {
                model.setApprovesstatestr("未认证 ");
            } else if (model.getApprovesstate() == 1) {
                model.setApprovesstatestr("未认证 ");
            } else if (model.getApprovesstate() == 2) {
                model.setApprovesstatestr("认证中");
            } else if (model.getApprovesstate() == 3) {
                model.setApprovesstatestr("认证通过");
            } else if (model.getApprovesstate() == 4) {
                model.setApprovesstatestr("认证失败");
            }
            if(model.getPhone() != null) {
            	agentResult.add(model);
            }

        }
        result.put("total",insbAgentDao.selectCountByMap_byAgentUser(map));
        result.put("rows", agentResult);
        return result;
	}

}
