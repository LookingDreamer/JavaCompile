package com.zzb.mobile.service.impl;

import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.LogUtil;
import com.common.redis.Constants;
import com.common.redis.IRedisClient;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.zzb.extra.controller.vo.TempjobnumMap2Jobnum;
import com.zzb.mobile.entity.ClientType;
import com.zzb.mobile.model.usercenter.CXReturnModel;
import com.zzb.mobile.model.usercenter.UserCenterReturnModel;
import com.zzb.mobile.service.UserCenterService;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cninsure.core.tools.util.ValidateUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.service.INSCCodeService;
import com.common.ConfigUtil;
import com.common.HttpClientUtil;
import com.zzb.app.service.QuotewayService;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.dao.INSBRegionDao;
import com.zzb.conf.dao.OFUserDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBRegion;
import com.zzb.conf.entity.OFUser;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.CommonModelforlzglogin;
import com.zzb.mobile.service.AppLoginService;
import com.zzb.mobile.service.AppRegisteredService;
import com.zzb.mobile.service.SMSClientService;
import com.zzb.mobile.util.EncodeUtils;
import com.zzb.mobile.util.HttpClientUtilForLzg;
import com.zzb.mobile.util.TokenMgr;

@Service
@Transactional
public class AppLoginServiceImpl implements AppLoginService {

    public static final String USER_LOGIN_KEY = "cm:zzb:login:user_login_key";
    private static Logger logger = Logger.getLogger(AppLoginServiceImpl.class);
    @Resource
    private INSBAgentService insbAgentService;
    @Resource
    private QuotewayService quotewayService;
    @Resource
    private AppRegisteredService appRegisterService;
    @Resource
    private INSCDeptDao inscDeptDao;
    @Resource
    private INSBRegionDao insbRegionDao;
    @Resource
    private INSCCodeService inscCodeService;
    @Resource
    private SMSClientService smsClientService;
    @Resource
    private INSBAgentDao insbAgentDao;
    @Resource
    private OFUserDao ofUserDao;
    @Resource
    private AppRegisteredService appRegisteredService;
    @Resource
    private UserCenterService userCenterService;

    @Resource
    private IRedisClient redisClient;

    private static String zhangzburl;

    static {
        // 读取相关的配置
        ResourceBundle resourceBundle = ResourceBundle.getBundle("config/config");
        zhangzburl = resourceBundle.getString("cm.zhangzb.url");
    }

    @Override
    public CommonModel login(String account, String password, String openid, String clienttype) {
        CommonModel model = new CommonModel();
        INSBAgent agent = null;
        if (!StringUtil.isEmpty(openid) && StringUtil.isEmpty(account)) { //只有微信号的
            INSBAgent userq = new INSBAgent();//
            userq.setOpenid(openid);
            agent = insbAgentService.queryOne(userq);
            if (agent == null) {
                model.setStatus("fail");
                model.setMessage("微信号不存在！");
                return model;
            }
        } else {
            //*****************************密码验证************************************
            //本地cm库校验
            agent = insbAgentDao.selectAgentByAgentCode(account);
            if (null == agent) {
                INSBAgent insbAgent = new INSBAgent();
                insbAgent.setMobile(account);
                List<INSBAgent> agentList = insbAgentDao.selectNotminiAgent(account);
                if (agentList.size() > 1) {
                    model.setStatus("fail");
                    model.setMessage("存在重复手机号，请使用工号登陆");
                    return model;
                } else if (agentList.size() == 1) {
                    agent = agentList.get(0);
                } else {
                    // agent = null;
                    /**add by hwc
                     * 调用集团统一用户中心接口验证
                     */
                    try {
                        Map<String, String> params = new HashMap<>();
                        params.put("logincode", account);
                        params.put("userpassword", password);
                        agent = userCenterService.loginUserCenter(params);
                    }catch (Exception e){
                        LogUtil.info("调用集团统一用户中心接口验证异常"+e.getMessage());
                        e.printStackTrace();
                        agent = null;
                    }
                }
            }else{
                //add by hwc 20170605 若本地数据库insbagent密码为空(集团统一用户中心返回的密码为空)，则登录统一中心验证，验证成功更新密码
                try {
                    if (StringUtil.isEmpty(agent.getPwd())) {
                        Map<String, String> params = new HashMap<>();
                        params.put("logincode", account);
                        params.put("userpassword", password);
                        UserCenterReturnModel userCenterReturnModel = userCenterService.loginCookie(params);
                        if ("success".equals(userCenterReturnModel.getStatus())) {
                            agent.setPwd(StringUtil.md5Base64(password));
                            insbAgentDao.updateById(agent);
                            OFUser ofuser = new OFUser();
                            ofuser = ofUserDao.queryByUserName(agent.getJobnum());
                            ofuser.setPlainPassword(agent.getPwd());
                            ofUserDao.updateByUserName(ofuser);
                        }
                    }
                }catch (Exception e){
                    LogUtil.info("调用集团统一用户中心接口返回密码异常" + e.getMessage());
                    e.printStackTrace();
                }
            }
            if (null == agent) {
                model.setStatus("fail");
                model.setMessage("用户账号不存在！");
                return model;
            }
            if (StringUtil.isEmpty(agent.getJobnum())) {
                model.setStatus("fail");
                model.setMessage("工号为空");
                return model;
            }
            if (!StringUtil.md5Base64(password).equals(agent.getPwd())) {
                model.setStatus("fail");
                model.setMessage("密码错误！");
                return model;
            }
            //*****************************密码验证结束************************************

            //*****************************如果有微信号，绑定微信号************************************
            if (!StringUtils.isEmpty(openid)) {
                INSBAgent userq = new INSBAgent();//
                userq.setOpenid(openid);
                List<INSBAgent> openAgents = insbAgentService.queryList(userq);
                for (INSBAgent temp : openAgents) {
                    temp.setOpenid("");
                    insbAgentService.updateById(temp);
                }
                agent.setOpenid(openid);
                insbAgentService.updateById(agent);
            }
            //*****************************如果有微信号，绑定微信号************************************
        }
        if (StringUtil.isEmpty(agent.getAgentstatus()) || agent.getAgentstatus().equals(2)) {
            model.setStatus("fail");
            model.setMessage("用户未启用！");
            return model;
        }
        //**************************获取机构信息*******************************************
        INSCDept dept = null;
        if (agent.getDeptid() != null) {
            dept = inscDeptDao.selectById(agent.getDeptid());
        }
        //**************************获取机构信息*******************************************
        if (dept == null || StringUtil.isEmpty(dept.getStatus()) || "1".equals(dept.getStatus())) {
            model.setStatus("fail");
            model.setMessage("用户所在区域未启用！");
            return model;
        }
        //**************************权限管理*******************************************
        // 认证通过的 未配置权限包 可忽略
        String jobNumber = agent.getJobnum();
        String permissionsJson = this.quotewayService.getpermissionsadd(jobNumber, agent.getApprovesstate() == 3);
        //**************************权限管理*******************************************


        String token = TokenMgr.generateToken(jobNumber);
        Map<Object, Object> responseMap = new HashMap<Object, Object>();
        responseMap.put("agentId", agent.getId());
        responseMap.put("jobNum", jobNumber);
        responseMap.put("name", agent.getName());
        responseMap.put("deptid", agent.getDeptid());
        responseMap.put("deptinnercode", dept == null ? "" : dept.getDeptinnercode());
        if (StringUtil.isEmpty(agent.getPlatformcode()) && StringUtil.isNotEmpty(agent.getTeamcode()) && agent.getTeamcode().trim().length() > 4) {
            String platformcode = agent.getTeamcode().trim()
                    .substring(0, 4);
            for (int j = 0; j < 6; j++) {
                platformcode += "0";
            }
            agent.setPlatformcode(platformcode);
        }

        responseMap.put("platformcode", agent.getPlatformcode());
        responseMap.put("mobile", agent.getMobile());
        responseMap.put("phone", agent.getPhone() == null ? "" : agent.getPhone());
        responseMap.put("comname", agent.getComname());
        responseMap.put("jobNumType", agent.getJobnumtype());
        responseMap.put("agentKind", agent.getAgentkind());
        responseMap.put("idno", agent.getIdno());
        responseMap.put("sex", agent.getSex());
        responseMap.put("permissions", JSONObject.fromObject(permissionsJson));
        responseMap.put("lastActiveTime", System.currentTimeMillis());
        responseMap.put("token", token);
        responseMap.put("defaultsite", agent.getCommonusebranch());//常用出单网点 20160407
        responseMap.put("approvesstate", agent.getApprovesstate());//认证状态  1-未认证  2-认证中  3-认证通过  4-认证失败
        responseMap.put("taskid", agent.getTaskid());//首单任务号
        responseMap.put("firstOderSuccesstime", agent.getFirstOderSuccesstime());//首单时间
        responseMap.put("firstlogintime", agent.getFirstlogintime());//首单时间

        if (dept != null) {
            responseMap.put("province", dept.getProvince());
            responseMap.put("provinceName", this.getAreaNameByCode(dept.getProvince()));
            responseMap.put("city", dept.getCity());
            responseMap.put("cityName", this.getAreaNameByCode(dept.getCity()));
            responseMap.put("county", dept.getCounty());
            responseMap.put("countyName", this.getAreaNameByCode(dept.getCounty()));
        }
        //登陆成功之前判断是否是首次登陆和是否注册时间为空
        if (null == agent.getFirstlogintime() && (agent.getAgentkind() != null && agent.getAgentkind() == 3)) {//如果首次登陆时间为空，则设置其首次登陆时间。
            agent.setFirstlogintime(new Date());
        }
        if (null == agent.getRegistertime()) {//如果注册时间为空，则设置其为首次登陆时间。
            agent.setRegistertime(new Date());
        }
        if (null != agent.getFirstlogintime()) {
             agent.setLsatlogintime(new Date());
         }
        agent.setClientType(clienttype == null ? "" : clienttype);
        insbAgentDao.updateById(agent);
        model.setStatus("success");
        model.setMessage("登陆成功！");
        model.setBody(responseMap);
        try {
            redisClient.set(Constants.TOKEN, token, JSONObject.fromObject(responseMap).toString(), 24 * 60 * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    @Override
    public CommonModel loginFromLzg(String account, String password, String openid, String lzgid, String requirementid) {
        CommonModel model = new CommonModel();
        INSBAgent agent = null;
        if (!StringUtil.isEmpty(openid) && StringUtil.isEmpty(account)) { //只有微信号的
            INSBAgent userq = new INSBAgent();//
            userq.setOpenid(openid);
            agent = insbAgentService.queryOne(userq);
            if (agent == null) {
                model.setStatus("fail");
                model.setMessage("微信号不存在！");
                return model;
            }
        } else {
            //账号密码登陆
//			LdapMgr ldap = new LdapMgr();
//			LdapAgentModel agentModel=null;
            INSBAgent agentModel = insbAgentDao.selectByJobnum(account);
//			agentModel = ldap.searchAgent(account);
            if (agentModel == null) {
                model.setStatus("fail");
                model.setMessage("用户账号不存在！");
                return model;
            }
            //*****************************密码验证************************************
            //本地cm库校验
//			INSBAgent insbAgent = insbAgentDao.selectAgentByAgentCode(account);
//			if(!StringUtil.md5Base64(password).equals(insbAgent.getPwd())){
//				model.setStatus("fail");
//				model.setMessage("密码错误！");
//				return model;
//			}
            if (!StringUtil.md5Base64(password).equals(agentModel.getPwd())) {
                model.setStatus("fail");
                model.setMessage("密码错误！");
                return model;
            }
            //*****************************密码验证结束************************************
            INSBAgent user = new INSBAgent();// 工号登陆 查询参数
            user.setJobnum(agentModel.getJobnum());
            user.setApprovesstate(null);
            agent = insbAgentService.queryOne(user);// 手机号登陆 查询
            if (agent == null) {
                model.setStatus("fail");
                model.setMessage("CM中用户不存在！");
                return model;
            }
            if (StringUtil.isEmpty(agent.getAgentstatus()) || agent.getAgentstatus().equals(2)) {
                model.setStatus("fail");
                model.setMessage("用户未启用！");
                return model;
            }
            INSCDept inscDept = inscDeptDao.selectById(agent.getDeptid());
            if ("1".equals(inscDept.getStatus())) {
                model.setStatus("fail");
                model.setMessage("用户所在区域未启用！");
                return model;
            }
            //*****************************如果有微信号，绑定微信号************************************
            if (!StringUtils.isEmpty(openid)) {
                INSBAgent userq = new INSBAgent();//
                userq.setOpenid(openid);
                List<INSBAgent> openAgents = insbAgentService.queryList(userq);
                for (INSBAgent temp : openAgents) {
                    temp.setOpenid("");
                    insbAgentService.updateById(temp);
                }
                agent.setOpenid(openid);
                insbAgentService.updateById(agent);
            }
            //*****************************如果有微信号，绑定微信号************************************
        }
        //**************************权限管理*******************************************
        String jobNumber = agent.getJobnum();
        String permissionsJson = this.quotewayService.getpermissionsadd(jobNumber, agent.getApprovesstate() == 3);
        //**************************权限管理*******************************************

//		//**************************获取机构信息*******************************************
        INSCDept dept = null;
        if (agent.getDeptid() != null) {
            dept = inscDeptDao.selectById(agent.getDeptid());
        }
        //**************************获取机构信息*******************************************
        String token = TokenMgr.generateToken(jobNumber);
        Map<Object, Object> responseMap = new HashMap<Object, Object>();
        responseMap.put("agentId", agent.getId());
        responseMap.put("jobNum", jobNumber);
        responseMap.put("name", agent.getName());
        responseMap.put("deptid", agent.getDeptid());
        responseMap.put("mobile", agent.getMobile());
        responseMap.put("comname", agent.getComname());
        responseMap.put("jobNumType", agent.getJobnumtype());
        responseMap.put("agentKind", agent.getAgentkind());
        responseMap.put("idno", agent.getIdno());
        responseMap.put("sex", agent.getSex());
        responseMap.put("permissions", JSONObject.fromObject(permissionsJson));
        responseMap.put("lastActiveTime", System.currentTimeMillis());
        responseMap.put("token", token);

        //TODO 写client type到redis并保存到数据库

        if (dept != null) {
            responseMap.put("province", dept.getProvince());
            responseMap.put("provinceName", this.getAreaNameByCode(dept.getProvince()));
            responseMap.put("city", dept.getCity());
            responseMap.put("cityName", this.getAreaNameByCode(dept.getCity()));
            responseMap.put("county", dept.getCounty());
            responseMap.put("countyName", this.getAreaNameByCode(dept.getCounty()));
        }
        model.setStatus("success");
        model.setMessage("登陆成功！");
        model.setBody(responseMap);
        try {
            redisClient.set(Constants.TOKEN, token, JSONObject.fromObject(responseMap).toString(), 24 * 60 * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //调用懒掌柜绑定一账通接口
        String ismanager = null;
        if ("2".equals(agent.getAgentkind())) {
            ismanager = "1";
        } else {
            ismanager = "0";
        }
        if (lzgid == null) {
            lzgid = "";
        }
        if (requirementid == null) {
            requirementid = "";
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", agent.getId());
        map.put("managerid", lzgid);
        map.put("requirementid", requirementid);
        map.put("account", agent.getMobile());
        map.put("username", agent.getName());
        map.put("ismanager", ismanager);
        map.put("agentcode", agent.getAgentcode());
        map.put("idno", agent.getIdno());
        map.put("organization", agent.getIdno());
        appRegisterService.bindingLzg(map);
        return model;
    }

    private String getAreaNameByCode(String code) {

        INSBRegion area = new INSBRegion();
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        area.setComcode(code);
        area = insbRegionDao.selectOne(area);
        if (area != null)
            return area.getComcodename();
        return null;
    }


    @Override
    public CommonModel findPassWord(String account) {
        CommonModel model = new CommonModel();
//		LdapMgr ldap = new LdapMgr();
//		LdapAgentModel agentModel = ldap.searchAgent(account);
        INSBAgent agentModel = insbAgentDao.selectByJobnum(account);
        String phoneNo = null;
        if (agentModel != null) {
            phoneNo = agentModel.getPhone();
        } else {
            model.setStatus("fail");
            model.setMessage("用户不存在！");
        }
        if (phoneNo == null) {
            model.setStatus("fail");
            model.setMessage("用户手机号不存在，请联系管理员！");
        } else {
            // 生成验证码 随机生成分六位数字
            String validatecode = EncodeUtils.generateValidateCode(6);
            // 将手机号、验证码、验证码生成时间保存到数据库
            try {
                redisClient.set(Constants.PHONE, phoneNo, validatecode + "_" + String.valueOf(System.currentTimeMillis()), 5 * 60); // 验证码放入redis
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                // 给phone发送验证码
                smsClientService.sendMobileValidateCode(phoneNo, validatecode);

                Map<Object, Object> responseMap = new HashMap<Object, Object>();
                responseMap.put("code", validatecode);
                responseMap.put("phone", phoneNo);
                model.setStatus("success");
                model.setMessage("验证码发送成功！");
                model.setBody(responseMap);
            } catch (Exception e) {
                e.printStackTrace();
                model.setStatus("fail");
                model.setMessage("手机短信验证码发送失败");
                return model;
            }

        }
        return model;
    }

    @Override
    public CommonModel validationCode(HttpServletRequest request, String phone, String vercode, String uuid) {
        CommonModel model = new CommonModel();
        String[] codeStr = null; // 从redis中取出验证码
        try {
            String codeStrs = (String) redisClient.get(Constants.PHONE, phone);
            codeStr = codeStrs.split("_");
        } catch (Exception e) {
            model.setStatus("fail");
            model.setMessage("验证码失效！");
            return model;
        }
        long timeMills = Long.parseLong(codeStr[1]);
        long time = (System.currentTimeMillis() - timeMills) / 1000;
        INSCCode code = new INSCCode();
        code.setCodetype("validateCodeFailureTime");
        code.setCodename("验证码失效时间");
        long ditime = Long.parseLong(inscCodeService.queryOne(code).getCodevalue());
        if (time > ditime) {
            model.setStatus("fail");
            model.setMessage("验证码失效！");
        } else {
            if (vercode.equals(codeStr[0])) {
                redisClient.del(Constants.PHONE, phone);
                request.getSession().removeAttribute("ImgValidateCode");//删除session中的图片验证码
                model.setStatus("success");
                model.setMessage("验证通过！");
            } else {
                model.setStatus("fail");
                model.setMessage("请输入正确的6位验证码！");
            }
        }
        return model;
    }

    @Override
    public CommonModel updatePassWord(String phoneNo, String newPassword) {
        CommonModel model = new CommonModel();
//		LdapMgr ldap = new LdapMgr();
//		LdapAgentModel agentModel = ldap.searchAgent(phoneNo);
        INSBAgent insbAgent2 = new INSBAgent();
        insbAgent2.setPhone(phoneNo);
        INSBAgent agentModel = insbAgentDao.selectOne(insbAgent2);
//		boolean ret =ldap.modifyPassWord(agentModel.getEmployeeNumber(),LdapMd5.Md5Encode(newPassword));
        OFUser user = new OFUser();
        user.setUsername(agentModel.getJobnum());
//		user.setPlainPassword(newPassword);
        user.setPlainPassword(StringUtil.md5Base64("123456"));
        int ret = ofUserDao.updateByUserName(user);
        if (ret > 0) {
            INSBAgent insbAgent = insbAgentDao.selectByJobnum(agentModel.getJobnum());
            String oldPwd = insbAgent.getPwd();
            insbAgent.setPwd(StringUtil.md5Base64(newPassword));
            LogUtil.info("updatePassWord 修改代理人密码 代理人："+insbAgent.toString()+" 操作人:"+phoneNo+" 操作时间:"+ DateUtil.getCurrentDateTime());
            //insbAgentDao.updateById(insbAgent);
            insbAgentService.updateAgentPwd(insbAgent, oldPwd);//修改密码并发短信通知代理人
            model.setStatus("success");
            model.setMessage("密码修改成功！");
            /**
             * 通知集团统一用户中心 hwc 添加 20170602
             */
            try {
                Map<String, String> params = new HashMap<>();
                params.put("omobile", phoneNo);
                params.put("nmobile","");
                params.put("cardno", insbAgent.getIdno());
                params.put("agentcode", insbAgent.getAgentcode());
                params.put("password", insbAgent.getPwd());
                params.put("passwordflag","1");
                userCenterService.updateUserAccount(params);
            }catch (Exception e){
                LogUtil.info("修改密码通知集团统一用户中心失败"+e.getMessage());
                e.printStackTrace();
            }
        } else {
            model.setStatus("fail");
            model.setMessage("密码修改失败！");
        }
        return model;
    }

    @Override
    public CommonModel logout(String token) {
        // Map<Object, Object> responseMap = new HashMap<Object, Object>();
        CommonModel model = new CommonModel();
        redisClient.del(Constants.TOKEN, token);
        model.setStatus("success");
        model.setMessage("退出成功");
        return model;
    }

    @Override
    public CommonModel loginByOpenId(String openid) {
        /**
         * 通过openid去库里查，如果有该用户，则登陆，如果没有该用户，则返回没有该用户
         */
        CommonModel model = new CommonModel();
        INSBAgent agent = null;
        INSBAgent user = new INSBAgent();// 工号登陆 查询参数
        user.setOpenid(openid);
        agent = insbAgentService.queryOne(user);// 手机号登陆 查询
        if (agent == null) {
            model.setStatus("fail");
            model.setMessage("CM中用户不存在！");
            return model;
        }


//		LdapMgr ldap = new LdapMgr();
//		LdapAgentModel agentModel = ldap.searchAgent(agent.getAgentcode());
        OFUser agentModel = ofUserDao.queryByUserName(agent.getAgentcode());
        if (agentModel != null) {
            String jobNumber = null;
            String permissionsJson = null;
            permissionsJson = this.quotewayService.getpermissionsadd(agent.getJobnum(), agent.getApprovesstate() == 3);
            jobNumber = agent.getJobnum();

            INSCDept dept = null;
            if (agent.getDeptid() != null) {
                dept = inscDeptDao.selectById(agent.getDeptid());
            }
            String token = TokenMgr.generateToken(jobNumber);
            Map<Object, Object> responseMap = new HashMap<Object, Object>();
            responseMap.put("agentId", agent.getId());
            responseMap.put("jobNum", jobNumber);
            responseMap.put("name", agent.getName());
            responseMap.put("deptid", agent.getDeptid());
            responseMap.put("mobile", agent.getMobile());
            responseMap.put("comname", agent.getComname());
            responseMap.put("jobNumType", agent.getJobnumtype());
            responseMap.put("agentKind", agent.getAgentkind());
            responseMap.put("idno", agent.getIdno());
            responseMap.put("sex", agent.getSex());
            responseMap.put("permissions", JSONObject.fromObject(permissionsJson));
            responseMap.put("lastActiveTime", System.currentTimeMillis());
            responseMap.put("token", token);

            //TODO 写client type到redis并保存到数据库


            if (dept != null) {
                responseMap.put("province", dept.getProvince());
                responseMap.put("provinceName", this.getAreaNameByCode(dept.getProvince()));
                responseMap.put("city", dept.getCity());
                responseMap.put("cityName", this.getAreaNameByCode(dept.getCity()));
                responseMap.put("county", dept.getCounty());
                responseMap.put("countyName", this.getAreaNameByCode(dept.getCounty()));
            }
            model.setStatus("success");
            model.setMessage("登陆成功！");
            model.setBody(responseMap);
            try {
                redisClient.set(Constants.TOKEN, token, JSONObject.fromObject(responseMap).toString(), 24 * 60 * 60);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            model.setStatus("fail");
            model.setMessage("登陆失败！");
        }

        return model;
    }

    @Override
    public CommonModel loginByChannelTempjobnum(TempjobnumMap2Jobnum tempjobnumMap2Jobnum) {
        /**
         * 参照：AppLoginServiceImpl 的 loginByOpenId(String openid)方法
         * 通过渠道用户的临时代理人，去库里查，如果有该用户，则登陆，如果没有该用户，则返回没有该用户
         */
        CommonModel model = new CommonModel();
        String tempjobnum = tempjobnumMap2Jobnum.getTempjobnum();
        String jobnum = tempjobnumMap2Jobnum.getJobnum();
        INSBAgent user_temp = new INSBAgent();// 临时工号登陆
        user_temp.setTempjobnum(tempjobnum);
        INSBAgent agent_temp = insbAgentService.queryOne(user_temp);
        if (agent_temp == null) {
            model.setStatus("fail");
            model.setMessage("CM中用户不存在临时工号：" + tempjobnum);
            return model;
        }
        INSBAgent agent = null;
        INSBAgent user = new INSBAgent();// 工号登陆 查询参数
        //user.setOpenid(openid);
        user.setJobnum(jobnum);
        agent = insbAgentService.queryOne(user);// 手机号登陆 查询
        if (agent == null) {
            model.setStatus("fail");
            model.setMessage("CM中用户不存在渠道出单工号：" + jobnum);
            return model;
        }


//		LdapMgr ldap = new LdapMgr();
//		LdapAgentModel agentModel = ldap.searchAgent(agent.getAgentcode());
        OFUser agentModel = ofUserDao.queryByUserName(agent.getAgentcode());
        if (agentModel != null) {
            String jobNumber = null;
            String permissionsJson = null;
            permissionsJson = this.quotewayService.getpermissionsadd(agent.getJobnum(), agent.getApprovesstate() == 3);
            jobNumber = agent.getJobnum();

//			String tokens = (String)redisClient.get("zzbOnlineUsers");
//			JSONObject obj =null;
//
//
//			if(tokens!=null){
//				obj = JSONObject.fromObject(tokens);
//				if(obj.size()!=0){
////					if(obj.keySet().contains("jobNumber")){
////						if(obj.getString(jobNumber)!=null){
////							this.logout(obj.getString(jobNumber));
////							obj.remove(jobNumber);
////						}
////					}
//					try {
//						//渠道配置的出单工号不直接登录。
//						// 如果渠道配置的出单工号出直接登录了，临时工号的退出，渠道配置的出单工号也退出了；可重写logout方法。但理论上渠道配置的出单工号不直接登录掌中保界面。
//						this.logout(obj.getString(tempjobnum));
//					}catch(Exception ex){
//						//ex.printStackTrace();
//						logger.debug("临时工号:"+tempjobnum+" 登录之前没有保存token值");
//					}
//					obj.remove(tempjobnum);
//				}
//			}else{
//				obj = new JSONObject();
//			}
            INSCDept dept = null;
            if (agent.getDeptid() != null) {
                dept = inscDeptDao.selectById(agent.getDeptid());
            }
            //String token = TokenMgr.generateToken(jobNumber);
            String token = TokenMgr.generateToken(tempjobnum);
            Map<Object, Object> responseMap = new HashMap<Object, Object>();
            responseMap.put("agentId", agent.getId());
            responseMap.put("jobNum", jobNumber);
            responseMap.put("name", agent.getName());
            responseMap.put("deptid", agent.getDeptid());
            responseMap.put("mobile", agent.getMobile());
            responseMap.put("comname", agent.getComname());
            responseMap.put("jobNumType", agent.getJobnumtype());
            responseMap.put("agentKind", agent.getAgentkind());
            responseMap.put("idno", agent.getIdno());
            responseMap.put("sex", agent.getSex());
            responseMap.put("permissions", JSONObject.fromObject(permissionsJson));
            responseMap.put("lastActiveTime", System.currentTimeMillis());
            responseMap.put("token", token);

            //TODO 写client type到redis并保存到数据库


            if (dept != null) {
                responseMap.put("province", dept.getProvince());
                responseMap.put("provinceName", this.getAreaNameByCode(dept.getProvince()));
                responseMap.put("city", dept.getCity());
                responseMap.put("cityName", this.getAreaNameByCode(dept.getCity()));
                responseMap.put("county", dept.getCounty());
                responseMap.put("countyName", this.getAreaNameByCode(dept.getCounty()));
            }
            tempjobnumMap2Jobnum.setToken(token);
            tempjobnumMap2Jobnum.setAgentId(agent.getId());
            responseMap.put("tempjobnumMap2Jobnum", tempjobnumMap2Jobnum);
            model.setStatus("success");
            model.setMessage("登陆成功！");
            model.setBody(responseMap);
            try {
                redisClient.set(Constants.TOKEN, token, JSONObject.fromObject(responseMap).toString(), 24 * 60 * 60);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            model.setStatus("fail");
            model.setMessage("登陆失败,渠道出单代理人不存在");
        }

        return model;
    }

    @Override
    public CommonModelforlzglogin lzgLogin(String token, String account) {
        //懒掌柜验证登陆
        String url = ValidateUtil.getConfigValue("lzg.api.service.url") + "/lm/otherconfig/checklogon";
        String platform = ConfigUtil.getPropString("lzg.zzb.platform.code");//平台编号
        CommonModelforlzglogin model = new CommonModelforlzglogin();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("platform", StringUtil.isEmpty(platform) ? platform : Integer.valueOf(platform));
        map.put("token", token);
        map.put("clientid", StringUtil.isEmpty(account) ? null : account);
        try {
            String result = HttpClientUtil.doPostJsonString(url, JSONObject.fromObject(map).toString());
            logger.info("请求懒掌柜检查登陆接口===param:" + JSONObject.fromObject(map).toString() + ",url:" + url + ",结果：" + result);
            JSONObject resultJSN = JSONObject.fromObject(result);
            if ("1".equals(resultJSN.getString("status"))) {//成功
                if (!StringUtil.isEmpty(account)) {//账号不为空  登陆
                    INSBAgent selectById = insbAgentDao.selectById(account);
                    if (selectById == null) {
                        model.setStatus("fail");
                        model.setMessage("代理人不存在");
                    } else {
                        CommonModel loginWithOutPwd = loginWithOutPwd(selectById.getJobnum());
                        model.setStatus(loginWithOutPwd.getStatus());
                        model.setMessage(loginWithOutPwd.getMessage());
                        model.setBody(loginWithOutPwd.getBody());
                        model.setValidateData(resultJSN);
                    }
                } else {
                    model.setStatus("success");
                    model.setMessage("验证成功");
                    model.setValidateData(resultJSN);
                }
            }
            if ("2".equals(resultJSN.getString("status"))) {
                model.setStatus("fail");
                model.setMessage("验证失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.setStatus("fail");
            model.setMessage("验证失败");
        }
        return model;
    }

    @Override
    public CommonModel oauthLogin(String code) {
        CommonModel model = new CommonModel();
        String clientSecret = ValidateUtil.getConfigValue("oauth.clientSecret");
        String clientId = ValidateUtil.getConfigValue("oauth.clientId");
        String redirectUri = ValidateUtil.getConfigValue("oauth.redirectUri");
        String applyTokenUrl = ValidateUtil.getConfigValue("oauth.applyToken.url");
        String grantType = ValidateUtil.getConfigValue("oauth.grantType");
        Map<String, String> map = new HashMap<String, String>();
        map.put("client_secret", clientSecret);
        map.put("grant_type", grantType);
        map.put("client_id", clientId);
        map.put("redirect_uri", redirectUri);
        map.put("code", code);
        String result = null;
        String accessToken = null;
        try {
            result = HttpClientUtilForLzg.post(applyTokenUrl, map);
        } catch (Exception e) {
            model.setStatus("fail");
            model.setMessage("申请令牌失败");
            model.setBody(result);
            return model;
        }
        logger.info("请求申请令牌接口===:code=" + code + ",client_secret=" + clientSecret + ",grant_type=" + grantType + ",client_id=" + clientId + ",redirect_uri=" + redirectUri + ",url=" + applyTokenUrl + ",结果=" + result);
        try {
            JSONObject resJson = JSONObject.fromObject(result);
            accessToken = resJson.getString("accessToken");
        } catch (Exception e1) {
            model.setStatus("fail");
            model.setMessage("申请令牌失败,令牌为空");
            model.setBody(result);
            return model;
        }

        String getUserInfoUrl = ValidateUtil.getConfigValue("oauth.getUserInfo.url");
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("access_token", accessToken);
        map2.put("redirect_uri", redirectUri);
        String result1 = null;
        try {
            result1 = HttpClientUtilForLzg.get(getUserInfoUrl, map2);
        } catch (Exception e) {
            model.setStatus("fail");
            model.setMessage("获取用户信息失败");
            model.setBody(result);
            return model;
        }
        JSONObject resJson1 = JSONObject.fromObject(result1);
        logger.info("请求获取用户信息接口===:access_token=" + accessToken + ",redirect_uri=" + redirectUri + ",url=" + getUserInfoUrl + ",结果=" + result1);
        if (StringUtil.isEmpty(result1)) {
            model.setStatus("fail");
            model.setMessage("获取用户信息失败,返回结果为空");
            model.setBody(result);
            return model;
        }
        String imgUrl = resJson1.containsKey("imgUrl") ? resJson1.getString("imgUrl") : null;
        String lzgid = resJson1.getString("id");
        String birthday = resJson1.containsKey("birthday") ? resJson1.getString("birthday") : null;
        String sex = resJson1.containsKey("sex") ? resJson1.getString("sex") : null;
        String bindPhone = resJson1.containsKey("bindPhone") ? resJson1.getString("bindPhone") : null;
        String uEmail = resJson1.containsKey("uEmail") ? resJson1.getString("uEmail") : null;
        String uRealName = resJson1.containsKey("uRealName") ? resJson1.getString("uRealName") : "zzbUser";
        String uNickName = resJson1.containsKey("uNickName") ? resJson1.getString("uNickName") : null;
        String county = resJson1.containsKey("county") ? resJson1.getString("county") : null;
        String city = resJson1.containsKey("city") ? resJson1.getString("city") : null;
        String province = resJson1.containsKey("province") ? resJson1.getString("province") : null;
        INSBAgent insbAgent = new INSBAgent();
        insbAgent.setLzgid(lzgid);
        List<INSBAgent> selectList = insbAgentDao.selectList(insbAgent);
        if (selectList.size() == 1) {//如果用户存在，则直接登陆
            CommonModel loginModel = loginWithOutPwd(selectList.get(0).getJobnum());
            model.setBody(loginModel.getBody());
            model.setStatus(loginModel.getStatus());
            model.setMessage(loginModel.getMessage());
            return model;
        } else if (selectList.size() == 1) {
            model.setStatus("fail");
            model.setMessage("登陆失败，懒掌柜账号被多个用户绑定");
            logger.info("Oauth2.0登陆接口，登陆失败=== lzgid:" + lzgid + ",被多个用户绑定");
            return model;
        }
        if (bindPhone != null) {
            INSBAgent agentModel = new INSBAgent();
            agentModel.setPhone(bindPhone);
            List<INSBAgent> agentList = insbAgentDao.selectList(agentModel);
            agentModel = new INSBAgent();
            agentModel.setMobile(bindPhone);
            List<INSBAgent> tempAgentList = insbAgentService.queryList(agentModel);
            if (agentList.size() > 0 || tempAgentList.size() > 0) {//如果手机号存在，则绑定，登陆，给用户提示
                //绑定
                agentModel = new INSBAgent();
                agentModel.setPhone(bindPhone);
                agentModel = insbAgentDao.selectOne(agentModel);
                Map<String, String> map_binding = new HashMap<String, String>();
                map_binding.put("userid", agentModel.getId());
                map_binding.put("managerid", lzgid);
                map_binding.put("requirementid", "");
                map_binding.put("account", bindPhone);
                map_binding.put("username", uRealName);
                map_binding.put("ismanager", "0");
                map_binding.put("agentcode", agentModel.getAgentcode());
                map_binding.put("idno", agentModel.getIdno());
                map_binding.put("organization", agentModel.getIdno());
                appRegisteredService.bindingLzg(map_binding);

                CommonModel loginModel = loginWithOutPwd(bindPhone);
                model.setBody(loginModel.getBody());
                model.setStatus(loginModel.getStatus());
                if (loginModel.getStatus().equals("success"))
                    model.setMessage("手机号已存在，自动绑定到账户");
                else
                    model.setMessage(loginModel.getMessage());
            } else {//注册
                Map<String, String> map3 = new HashMap<String, String>();
                map3.put("provinceCode", province);
                map3.put("cityCode", city);
                map3.put("countyCode", county);
                map3.put("phone", bindPhone);
                map3.put("name", uRealName);
                model = appRegisteredService.oauthRegist(JSONObject.fromObject(map3).toString());
            }
        } else {//注册
            Map<String, String> map3 = new HashMap<String, String>();
            map3.put("provinceCode", province);
            map3.put("cityCode", city);
            map3.put("countyCode", county);
            map3.put("phone", bindPhone);
            map3.put("name", uRealName);
            model = appRegisteredService.oauthRegist(JSONObject.fromObject(map3).toString());
        }
        return model;
    }

    @Override
    public CommonModel loginWithOutPwd(String account) {
        CommonModel model = new CommonModel();
        INSBAgent agent = null;
        //本地cm库校验
        agent = insbAgentDao.selectAgentByAgentCode(account);
        if (null == agent) {
            INSBAgent insbAgent = new INSBAgent();
            insbAgent.setMobile(account);
            List<INSBAgent> agentList = insbAgentDao.selectList(insbAgent);
            if (agentList.size() > 1) {
                for (INSBAgent insbAgent2 : agentList) {
                    if (insbAgent2.getTempjobnum() != null) {
                        agent = insbAgent2;
                        break;
                    }
                }
            } else
                agent = agentList.get(0);
        }
        if (null == agent) {
            model.setStatus("fail");
            model.setMessage("用户账号不存在！");
            return model;
        }
        if (StringUtil.isEmpty(agent.getJobnum())) {
            model.setStatus("fail");
            model.setMessage("工号为空");
            return model;
        }
        if (StringUtil.isEmpty(agent.getAgentstatus()) || agent.getAgentstatus().equals(2)) {
            model.setStatus("fail");
            model.setMessage("用户未启用！");
            return model;
        }
        if (!StringUtil.isEmpty(agent.getDeptid())) {
            INSCDept inscDept = inscDeptDao.selectById(agent.getDeptid());
            if ("1".equals(inscDept.getStatus())) {
                model.setStatus("fail");
                model.setMessage("用户所在区域未启用！");
                return model;
            }
        }
        //**************************权限管理*******************************************
        // 认证通过的 未配置权限包 可忽略
        String jobNumber = agent.getJobnum();
        String permissionsJson = this.quotewayService.getpermissionsadd(jobNumber, agent.getApprovesstate() == 3);
        //**************************权限管理*******************************************

        //**************************获取机构信息*******************************************
        INSCDept dept = null;
        if (agent.getDeptid() != null) {
            dept = inscDeptDao.selectById(agent.getDeptid());
        }
        //**************************获取机构信息*******************************************
        String token = TokenMgr.generateToken(jobNumber);
        Map<Object, Object> responseMap = new HashMap<Object, Object>();
        responseMap.put("agentId", agent.getId());
        responseMap.put("jobNum", jobNumber);
        responseMap.put("name", agent.getName());
        responseMap.put("deptid", agent.getDeptid());
        responseMap.put("mobile", agent.getMobile());
        responseMap.put("comname", agent.getComname());
        responseMap.put("jobNumType", agent.getJobnumtype());
        responseMap.put("agentKind", agent.getAgentkind());
        responseMap.put("idno", agent.getIdno());
        responseMap.put("sex", agent.getSex());
        responseMap.put("permissions", JSONObject.fromObject(permissionsJson));
        responseMap.put("lastActiveTime", System.currentTimeMillis());
        responseMap.put("token", token);
        responseMap.put("defaultsite", agent.getCommonusebranch());//常用出单网点 20160407
        if (dept != null) {
            responseMap.put("province", dept.getProvince());
            responseMap.put("provinceName", this.getAreaNameByCode(dept.getProvince()));
            responseMap.put("city", dept.getCity());
            responseMap.put("cityName", this.getAreaNameByCode(dept.getCity()));
            responseMap.put("county", dept.getCounty());
            responseMap.put("countyName", this.getAreaNameByCode(dept.getCounty()));
        }
        model.setStatus("success");
        model.setMessage("登陆成功！");
        model.setBody(responseMap);
        try {
            redisClient.del(USER_LOGIN_KEY, account);
            redisClient.set(Constants.TOKEN, token, JSONObject.fromObject(responseMap).toString(), 24 * 60 * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    @Override
    public CommonModel userLoginGetKey(String account, String userid) {
        CommonModel model = new CommonModel();
        String token = TokenMgr.generateToken(account);
        long invalidTime = System.currentTimeMillis();
        try {
            redisClient.set(USER_LOGIN_KEY, account, token + "_" + invalidTime, 7200);
            LogUtil.info("userLoginGetKey:" + account + ":" + redisClient.get(USER_LOGIN_KEY, account));
        } catch (Exception e) {
            e.printStackTrace();
            model.setStatus("fail");
            return model;
        }
        Map<String, String> body = new HashMap<String, String>();
        body.put("token", token);
        body.put("userid", userid);
        body.put("zhangzburl", zhangzburl);
        model.setStatus("success");
        model.setBody(body);
        return model;
    }

    @Override
    public CommonModel userLogin(String account, String token) {
        CommonModel model = new CommonModel();
        if (StringUtil.isEmpty(account)) {
            model.setStatus("fail");
            model.setMessage("登陆失败，代理人账号不可为空");
            return model;
        }
        String accessTokens = (String) redisClient.get(USER_LOGIN_KEY, account);
        LogUtil.info("userLogin:" + account + ":" + accessTokens);
        if (StringUtil.isEmpty(accessTokens)) {
            model.setStatus("fail");
            model.setMessage("登陆失败，令牌不存在或已超时");
            return model;
        }

        String[] datas = accessTokens.split("_");
        String accessToken = datas[0];
        /*long invalideTime =Long.parseLong(datas[1]);
		
		if((System.currentTimeMillis()-invalideTime)>7200*1000){
			model.setStatus("fail");
			model.setMessage("登陆超时！");
			return model;
		}*/
        if (!accessToken.equals(token)) {
            model.setStatus("fail");
            model.setMessage("登陆失败，令牌不正确！");
            return model;
        }
        return this.loginWithOutPwd(account);
    }

    /**
     * 保存用户最后一次登陆相关信息
     */
    private void updateUserLoginInfo(String id, String clientType) {
        insbAgentDao.updateClientType(id, clientType);
    }

    /**
     *AgentCode免密码登录
     */
    @Override
    public CommonModel loginByAgentCodeForChn(String agentCode, String clienttype) {
        CommonModel model = new CommonModel();
        INSBAgent agent = null;
            //*****************************************************************
            //本地cm库校验
            agent = insbAgentDao.selectAgentByAgentCode(agentCode);
            if (null == agent) {
                model.setStatus("fail");
                model.setMessage("用户账号不存在！");
                return model;
            }
            if (StringUtil.isEmpty(agent.getJobnum())) {
                model.setStatus("fail");
                model.setMessage("工号为空");
                return model;
            }
            //*****************************密码验证结束************************************
            if (StringUtil.isEmpty(agent.getAgentstatus()) || agent.getAgentstatus().equals(2)) {
                model.setStatus("fail");
                model.setMessage("用户未启用！");
                return model;
            }
            INSCDept inscDept = inscDeptDao.selectById(agent.getDeptid());
            if ("1".equals(inscDept.getStatus())) {
                model.setStatus("fail");
                model.setMessage("用户所在区域未启用！");
                return model;
            }

        //**************************权限管理*******************************************
        // 认证通过的 未配置权限包 可忽略
        String jobNumber = agent.getJobnum();
        String permissionsJson = this.quotewayService.getpermissionsadd(jobNumber, agent.getApprovesstate() == 3);
        //**************************权限管理*******************************************

//		//**************************获取机构信息*******************************************
        INSCDept dept = null;
        if (agent.getDeptid() != null) {
            dept = inscDeptDao.selectById(agent.getDeptid());
        }
        //**************************获取机构信息*******************************************
        String token = TokenMgr.generateToken(jobNumber);
        Map<Object, Object> responseMap = new HashMap<Object, Object>();
        responseMap.put("agentId", agent.getId());
        responseMap.put("jobNum", jobNumber);
        responseMap.put("name", agent.getName());
        responseMap.put("deptid", agent.getDeptid());
        responseMap.put("deptinnercode", dept == null ? "" : dept.getDeptinnercode());
        if (StringUtil.isEmpty(agent.getPlatformcode()) && StringUtil.isNotEmpty(agent.getTeamcode()) && agent.getTeamcode().trim().length() > 4) {
            String platformcode = agent.getTeamcode().trim()
                    .substring(0, 4);
            for (int j = 0; j < 6; j++) {
                platformcode += "0";
            }
            agent.setPlatformcode(platformcode);
        }

        responseMap.put("platformcode", agent.getPlatformcode());
        responseMap.put("mobile", agent.getMobile());
        responseMap.put("phone", agent.getPhone() == null ? "" : agent.getPhone());
        responseMap.put("comname", agent.getComname());
        responseMap.put("jobNumType", agent.getJobnumtype());
        responseMap.put("agentKind", agent.getAgentkind());
        responseMap.put("idno", agent.getIdno());
        responseMap.put("sex", agent.getSex());
        responseMap.put("permissions", JSONObject.fromObject(permissionsJson));
        responseMap.put("lastActiveTime", System.currentTimeMillis());
        responseMap.put("token", token);
        responseMap.put("defaultsite", agent.getCommonusebranch());//常用出单网点 20160407
        responseMap.put("approvesstate", agent.getApprovesstate());//认证状态  1-未认证  2-认证中  3-认证通过  4-认证失败
        responseMap.put("taskid", agent.getTaskid());//首单任务号
        responseMap.put("firstOderSuccesstime", agent.getFirstOderSuccesstime());//首单时间
        responseMap.put("firstlogintime", agent.getFirstlogintime());//首单时间

        if (dept != null) {
            responseMap.put("province", dept.getProvince());
            responseMap.put("provinceName", this.getAreaNameByCode(dept.getProvince()));
            responseMap.put("city", dept.getCity());
            responseMap.put("cityName", this.getAreaNameByCode(dept.getCity()));
            responseMap.put("county", dept.getCounty());
            responseMap.put("countyName", this.getAreaNameByCode(dept.getCounty()));
        }
        //登陆成功之前判断是否是首次登陆和是否注册时间为空
        if (null == agent.getFirstlogintime() && (agent.getAgentkind() != null && agent.getAgentkind() == 3)) {//如果首次登陆时间为空，则设置其首次登陆时间。
            agent.setFirstlogintime(new Date());
        }
        if (null == agent.getRegistertime()) {//如果注册时间为空，则设置其为首次登陆时间。
            agent.setRegistertime(new Date());
        }
        if (null != agent.getFirstlogintime()) {
            agent.setLsatlogintime(new Date());
        }
        agent.setClientType(clienttype == null ? "" : clienttype);
        insbAgentDao.updateById(agent);
        model.setStatus("success");
        model.setMessage("登陆成功！");
        model.setBody(responseMap);
        try {
            redisClient.set(Constants.TOKEN, token, JSONObject.fromObject(responseMap).toString(), 24 * 60 * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    /**
     *AgentCode免密码登录
     */
    @Override
    public CommonModel loginByAgentCodeForLzg(String agentCode ,String userCookie, String clienttype) {
        CommonModel model = new CommonModel();
        INSBAgent agent = null;
        //*****************************************************************
        //本地cm库校验
        agent = insbAgentDao.selectAgentByAgentCode(agentCode);
        if (null == agent) {
            if(StringUtil.isNotEmpty(userCookie)){
                agent = userCenterService.getAgentByUserCookie(userCookie);
            }
            if(null == agent) {
                model.setStatus("fail");
                model.setMessage("用户账号不存在！");
                return model;
            }
        }
        if (StringUtil.isEmpty(agent.getJobnum())) {
            model.setStatus("fail");
            model.setMessage("工号为空");
            return model;
        }
        //*****************************密码验证结束************************************
        if (StringUtil.isEmpty(agent.getAgentstatus()) || agent.getAgentstatus().equals(2)) {
            model.setStatus("fail");
            model.setMessage("用户未启用！");
            return model;
        }
        INSCDept inscDept = inscDeptDao.selectById(agent.getDeptid());
        if ("1".equals(inscDept.getStatus())) {
            model.setStatus("fail");
            model.setMessage("用户所在区域未启用！");
            return model;
        }

        //**************************权限管理*******************************************
        // 认证通过的 未配置权限包 可忽略
        String jobNumber = agent.getJobnum();
        String permissionsJson = this.quotewayService.getpermissionsadd(jobNumber, agent.getApprovesstate() == 3);
        //**************************权限管理*******************************************

//		//**************************获取机构信息*******************************************
        INSCDept dept = null;
        if (agent.getDeptid() != null) {
            dept = inscDeptDao.selectById(agent.getDeptid());
        }
        //**************************获取机构信息*******************************************
        String token = TokenMgr.generateToken(jobNumber);
        Map<Object, Object> responseMap = new HashMap<Object, Object>();
        responseMap.put("agentId", agent.getId());
        responseMap.put("jobNum", jobNumber);
        responseMap.put("name", agent.getName());
        responseMap.put("deptid", agent.getDeptid());
        responseMap.put("deptinnercode", dept == null ? "" : dept.getDeptinnercode());
        if (StringUtil.isEmpty(agent.getPlatformcode()) && StringUtil.isNotEmpty(agent.getTeamcode()) && agent.getTeamcode().trim().length() > 4) {
            String platformcode = agent.getTeamcode().trim()
                    .substring(0, 4);
            for (int j = 0; j < 6; j++) {
                platformcode += "0";
            }
            agent.setPlatformcode(platformcode);
        }

        responseMap.put("platformcode", agent.getPlatformcode());
        responseMap.put("mobile", agent.getMobile());
        responseMap.put("phone", agent.getPhone() == null ? "" : agent.getPhone());
        responseMap.put("comname", agent.getComname());
        responseMap.put("jobNumType", agent.getJobnumtype());
        responseMap.put("agentKind", agent.getAgentkind());
        responseMap.put("idno", agent.getIdno());
        responseMap.put("sex", agent.getSex());
        responseMap.put("permissions", JSONObject.fromObject(permissionsJson));
        responseMap.put("lastActiveTime", System.currentTimeMillis());
        responseMap.put("token", token);
        responseMap.put("defaultsite", agent.getCommonusebranch());//常用出单网点 20160407
        responseMap.put("approvesstate", agent.getApprovesstate());//认证状态  1-未认证  2-认证中  3-认证通过  4-认证失败
        responseMap.put("taskid", agent.getTaskid());//首单任务号
        responseMap.put("firstOderSuccesstime", agent.getFirstOderSuccesstime());//首单时间
        responseMap.put("firstlogintime", agent.getFirstlogintime());//首单时间

        if (dept != null) {
            responseMap.put("province", dept.getProvince());
            responseMap.put("provinceName", this.getAreaNameByCode(dept.getProvince()));
            responseMap.put("city", dept.getCity());
            responseMap.put("cityName", this.getAreaNameByCode(dept.getCity()));
            responseMap.put("county", dept.getCounty());
            responseMap.put("countyName", this.getAreaNameByCode(dept.getCounty()));
        }
        //登陆成功之前判断是否是首次登陆和是否注册时间为空
        if (null == agent.getFirstlogintime() && (agent.getAgentkind() != null && agent.getAgentkind() == 3)) {//如果首次登陆时间为空，则设置其首次登陆时间。
            agent.setFirstlogintime(new Date());
        }
        if (null == agent.getRegistertime()) {//如果注册时间为空，则设置其为首次登陆时间。
            agent.setRegistertime(new Date());
        }
        if (null != agent.getFirstlogintime()) {
            agent.setLsatlogintime(new Date());
        }
        agent.setClientType(clienttype == null ? "" : clienttype);
        insbAgentDao.updateById(agent);
        model.setStatus("success");
        model.setMessage("登陆成功！");
        model.setBody(responseMap);
        try {
            redisClient.set(Constants.TOKEN, token, JSONObject.fromObject(responseMap).toString(), 24 * 60 * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    /**
     * 外部用户（寿险、C站用户）登录掌中保
     * @param cxReturnModel
     * @return
     */
    @Override
    public CommonModel loginForUserCenter(CXReturnModel cxReturnModel) {
        CommonModel model = new CommonModel();
        INSBAgent agent = null;
        String account = cxReturnModel.getAgentCode() == null ? cxReturnModel.getMobile() : cxReturnModel.getAgentCode();
        String password = cxReturnModel.getPassword();
        String bizType = cxReturnModel.getBizType();
            //*****************************密码验证************************************
            //本地cm库校验
            agent = insbAgentDao.selectAgentByAgentCode(account);
            if (null == agent) {
                INSBAgent insbAgent = new INSBAgent();
                insbAgent.setMobile(account);
                List<INSBAgent> agentList = insbAgentDao.selectNotminiAgent(account);
                if (agentList.size() > 1) {
                    model.setStatus("fail");
                    model.setMessage("存在重复手机号，请使用工号登陆");
                    return model;
                } else if (agentList.size() == 1) {
                    agent = agentList.get(0);
                } else {
                    agent = null;
                    //用户数据同步
                    agent = userCenterService.saveAgent(cxReturnModel);

                }
            }
            if (null == agent) {
                model.setStatus("fail");
                model.setMessage("用户账号不存在！");
                return model;
            }
            if("00".equals(bizType) && 2 == agent.getAgentkind()){
                model.setStatus("fail");
                model.setMessage("需使用身份证号进行验证！");
                return model;
            }
            if (StringUtil.isEmpty(agent.getJobnum())) {
                model.setStatus("fail");
                model.setMessage("工号为空");
                return model;
            }
            if (!password.equals(agent.getPwd())) {
                model.setStatus("fail");
                model.setMessage("密码错误！");
                return model;
            }
            //*****************************密码验证结束************************************


        if (StringUtil.isEmpty(agent.getAgentstatus()) || agent.getAgentstatus().equals(2)) {
            model.setStatus("fail");
            model.setMessage("用户未启用！");
            return model;
        }
        //**************************获取机构信息*******************************************
        INSCDept dept = null;
        if (agent.getDeptid() != null) {
            dept = inscDeptDao.selectById(agent.getDeptid());
        }
        //**************************获取机构信息*******************************************
        if (dept == null || StringUtil.isEmpty(dept.getStatus()) || "1".equals(dept.getStatus())) {
            model.setStatus("fail");
            model.setMessage("用户所在区域未启用！");
            return model;
        }
        //**************************权限管理*******************************************
        // 认证通过的 未配置权限包 可忽略
        String jobNumber = agent.getJobnum();
        String permissionsJson = this.quotewayService.getpermissionsadd(jobNumber, agent.getApprovesstate() == 3);
        //**************************权限管理*******************************************


        String token = TokenMgr.generateToken(jobNumber);
        Map<Object, Object> responseMap = new HashMap<Object, Object>();
        responseMap.put("agentId", agent.getId());
        responseMap.put("jobNum", jobNumber);
        responseMap.put("name", agent.getName());
        responseMap.put("deptid", agent.getDeptid());
        responseMap.put("deptinnercode", dept == null ? "" : dept.getDeptinnercode());
        if (StringUtil.isEmpty(agent.getPlatformcode()) && StringUtil.isNotEmpty(agent.getTeamcode()) && agent.getTeamcode().trim().length() > 4) {
            String platformcode = agent.getTeamcode().trim()
                    .substring(0, 4);
            for (int j = 0; j < 6; j++) {
                platformcode += "0";
            }
            agent.setPlatformcode(platformcode);
        }

        responseMap.put("platformcode", agent.getPlatformcode());
        responseMap.put("mobile", agent.getMobile());
        responseMap.put("phone", agent.getPhone() == null ? "" : agent.getPhone());
        responseMap.put("comname", agent.getComname());
        responseMap.put("jobNumType", agent.getJobnumtype());
        responseMap.put("agentKind", agent.getAgentkind());
        responseMap.put("idno", agent.getIdno());
        responseMap.put("sex", agent.getSex());
        responseMap.put("permissions", JSONObject.fromObject(permissionsJson));
        responseMap.put("lastActiveTime", System.currentTimeMillis());
        responseMap.put("token", token);
        responseMap.put("defaultsite", agent.getCommonusebranch());//常用出单网点 20160407
        responseMap.put("approvesstate", agent.getApprovesstate());//认证状态  1-未认证  2-认证中  3-认证通过  4-认证失败
        responseMap.put("taskid", agent.getTaskid());//首单任务号
        responseMap.put("firstOderSuccesstime", agent.getFirstOderSuccesstime());//首单时间
        responseMap.put("firstlogintime", agent.getFirstlogintime());//首单时间

        if (dept != null) {
            responseMap.put("province", dept.getProvince());
            responseMap.put("provinceName", this.getAreaNameByCode(dept.getProvince()));
            responseMap.put("city", dept.getCity());
            responseMap.put("cityName", this.getAreaNameByCode(dept.getCity()));
            responseMap.put("county", dept.getCounty());
            responseMap.put("countyName", this.getAreaNameByCode(dept.getCounty()));
        }
        //登陆成功之前判断是否是首次登陆和是否注册时间为空
        if (null == agent.getFirstlogintime() && (agent.getAgentkind() != null && agent.getAgentkind() == 3)) {//如果首次登陆时间为空，则设置其首次登陆时间。
            agent.setFirstlogintime(new Date());
        }
        if (null == agent.getRegistertime()) {//如果注册时间为空，则设置其为首次登陆时间。
            agent.setRegistertime(new Date());
        }
        if (null != agent.getFirstlogintime()) {
            agent.setLsatlogintime(new Date());
        }
        agent.setClientType("");
        insbAgentDao.updateById(agent);
        model.setStatus("success");
        model.setMessage("登陆成功！");
        model.setBody(responseMap);
        try {
            redisClient.set(Constants.TOKEN, token, JSONObject.fromObject(responseMap).toString(), 24 * 60 * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }
}
