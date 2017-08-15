package com.zzb.mobile.service.impl;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCDept;
import com.common.HttpClientUtil;
import com.common.HttpSender;
import com.zzb.chn.util.JsonUtils;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.dao.OFUserDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBRegion;
import com.zzb.conf.entity.OFUser;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.conf.service.INSBRegionService;
import com.zzb.mobile.model.usercenter.CXReturnModel;
import com.zzb.mobile.model.usercenter.UserCenterBank;
import com.zzb.mobile.model.usercenter.UserCenterDept;
import com.zzb.mobile.model.usercenter.UserCenterReturnModel;
import com.zzb.mobile.service.UserCenterService;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by Hwc on 2017/5/22.
 */
@Service
public class UserCenterServiceImpl implements UserCenterService {

    public static ResourceBundle resourceBundle = ResourceBundle.getBundle("config/config");

    public static String userCenterBaseURL = resourceBundle.getString("usercenter.interface.url");

    public static String loginCookieURL = userCenterBaseURL + "/apidata/logincookie";

    public static String successLoginURL = userCenterBaseURL + "/apidata/successlogin";

    public static String validateMobileNoURL = userCenterBaseURL + "/apidata/validatemobileno";

    public static String updateUserDetailURL = userCenterBaseURL + "/apidata/updateuserdetail";

    public static String updateUserAccountURL = userCenterBaseURL + "/apidata/updateuseraccount";

    public static String userInfoByAgentCodeURL = userCenterBaseURL + "/apidata/userinfobyagentcode";

    public static String FAIL = "fail";

    public static String SUCCESS = "success";

    public static String token = resourceBundle.getString("usercenter.zzb.token");;

    public static Map<String,String> httpHead = new HashMap<>();

    static {
        httpHead.put("Content-Type", "application/json;charset=utf-8");
        httpHead.put("token",token);
    }

    @Resource
    private INSCDeptDao inscDeptDao;

    @Resource
    private INSBAgentDao insbAgentDao;

    @Resource
    private OFUserDao ofUserDao;

    @Resource
    private INSBRegionService insbRegionService;

    @Resource
    private INSBAgentService insbAgentService;

    @Override
    public UserCenterReturnModel loginCookie(Map<String, String> params) {
        LogUtil.info("loginCookie params="+JsonUtils.serialize(params));
        UserCenterReturnModel userCenterReturnModel = new UserCenterReturnModel();
        try {
            String resp = HttpSender.doPost(loginCookieURL, JsonUtils.serialize(params), httpHead, "UTF-8");
            userCenterReturnModel = JsonUtils.deserialize(resp,UserCenterReturnModel.class);
        }catch (Exception e){
            userCenterReturnModel.setStatus(FAIL);
            userCenterReturnModel.setMessage("登录异常");
            e.printStackTrace();
        }
        LogUtil.info("loginCookie resp="+JsonUtils.serialize(userCenterReturnModel));
        return userCenterReturnModel;
    }

    @Override
    public CXReturnModel successLogin(Map<String ,String> params) {
        LogUtil.info("successLogin params="+JsonUtils.serialize(params));
        CXReturnModel cxReturnModel = new CXReturnModel();
        try {
            String resp = HttpClientUtil.doGetToken(successLoginURL, params, token);
            cxReturnModel = JsonUtils.deserialize(resp,CXReturnModel.class);
        }catch (Exception e){
            cxReturnModel.setStatus(FAIL);
            cxReturnModel.setMessage("接口异常");
            e.printStackTrace();
        }
        LogUtil.info("successLogin resp="+JsonUtils.serialize(cxReturnModel));
        return cxReturnModel;
    }

    @Override
    public UserCenterReturnModel validateMobileNo(Map<String ,String> params) {
        LogUtil.info("validateMobileNo params="+JsonUtils.serialize(params));
        UserCenterReturnModel userCenterReturnModel = new UserCenterReturnModel();
        try {
            String resp = HttpClientUtil.doGetToken(validateMobileNoURL, params, token);
            userCenterReturnModel = JsonUtils.deserialize(resp,UserCenterReturnModel.class);
        }catch (Exception e){
            userCenterReturnModel.setStatus("error");
            userCenterReturnModel.setMessage("接口异常");
            e.printStackTrace();
        }
        LogUtil.info("validateMobileNo resp="+JsonUtils.serialize(userCenterReturnModel));
        return userCenterReturnModel;
    }

    /**
     *
     * @param data
     * @return
     */
    @Override
    public UserCenterReturnModel updateUserDetail(CXReturnModel data) {
        LogUtil.info("updateUserDetail begin = "+JsonUtils.serialize(data));
        UserCenterReturnModel userCenterReturnModel = new UserCenterReturnModel();
        try {
            String resp = HttpSender.doPost(updateUserDetailURL, JsonUtils.serialize(data), httpHead, "UTF-8");
            LogUtil.info("updateUserDetail resp = "+resp);
            userCenterReturnModel = JsonUtils.deserialize(resp,UserCenterReturnModel.class);
        }catch (Exception e){
            userCenterReturnModel.setStatus(FAIL);
            userCenterReturnModel.setMessage("接口异常");
            e.printStackTrace();
        }
        LogUtil.info("updateUserDetail end = %s returnModel = %s ",data.getAgentCode(),JsonUtils.serialize(userCenterReturnModel));
        return userCenterReturnModel;
    }

    /**
     * 修改手机号或者密码推送到集团
     * @param params
     * @return
     */
    @Override
    public UserCenterReturnModel updateUserAccount(Map<String, String> params) {
        //System.out.println("updateUserAccount params = "+JsonUtils.serialize(params));
        LogUtil.info("updateUserAccount params = "+JsonUtils.serialize(params));
        UserCenterReturnModel userCenterReturnModel = new UserCenterReturnModel();
        try {
            String resp = HttpSender.doPost(updateUserAccountURL, JsonUtils.serialize(params), httpHead, "UTF-8");
            userCenterReturnModel = JsonUtils.deserialize(resp,UserCenterReturnModel.class);
        }catch (Exception e){
            userCenterReturnModel.setStatus(FAIL);
            userCenterReturnModel.setMessage("接口异常");
            e.printStackTrace();
        }
        //System.out.println("updateUserAccount userCenterReturnModel = "+JsonUtils.serialize(userCenterReturnModel));
        LogUtil.info("updateUserAccount userCenterReturnModel = "+JsonUtils.serialize(userCenterReturnModel));
        return userCenterReturnModel;
    }

    /**
     * 集团数据更新推送到掌中保
     * @param data
     * @return
     */
    @Override
    public UserCenterReturnModel updateUserDetailFromUserCenter(CXReturnModel data) {
        //
        LogUtil.info("updateUserDetailFromUserCenter data="+JsonUtils.serialize(data));
        UserCenterReturnModel userCenterReturnModel = new UserCenterReturnModel();
        try {
            INSBAgent agent = new INSBAgent();
            agent.setAgentcode(data.getAgentCode());
            List<INSBAgent> agents = insbAgentDao.selectList(agent);
            if (agents != null && agents.size()>0) {
                for (INSBAgent agt : agents) {
                    String oldPwd = agt.getPwd();
                    this.compAgent(agt, data);
//                    insbAgentDao.updateById(agt);
                    insbAgentService.updateAgentPwd(agt, oldPwd);//修改密码并发短信通知代理人
                    if(StringUtil.isNotEmpty(data.getPassword()) && !oldPwd.equals(data.getPassword())) {
                        //修改ofuser表中的密码
                        OFUser ou=new OFUser();
                        String username=agt.getJobnum();
                        LogUtil.info("updateUserDetailFromUserCenter 代理人密码成功：jobnum="+username+" oldPwd="+!oldPwd.equals(data.getPassword())+" newPwd="+data.getPassword());
                        if(username!=null){
                            ou.setUsername(username);
                            Long time=new Date().getTime();
                            ou.setModificationDate(time);	//修改时间
                            ou.setPlainPassword(data.getPassword()); //修改加密的密码
                            ofUserDao.updateByUserName(ou);
                        }
                    }
                }
            }else{
                //保存数据
                agent = saveAgent(data);
            }
            userCenterReturnModel.setMessage("更新代理人数据成功");
            userCenterReturnModel.setStatus(SUCCESS);
            LogUtil.info("updateUserDetailFromUserCenter SUCCESS="+JsonUtils.serialize(data));
        }catch (Exception e){
            LogUtil.info("updateUserDetailFromUserCenter 异常 data= %s errmsg=%s",JsonUtils.serialize(data),e.getMessage());
            e.printStackTrace();
            userCenterReturnModel.setMessage("更新代理人数据失败");
            userCenterReturnModel.setStatus(FAIL);
        }
        return userCenterReturnModel;
    }

    public INSBAgent loginUserCenter(Map<String ,String> params){
        LogUtil.info("loginUserCenter params="+JsonUtils.serialize(params));
        //System.out.println("loginUserCenter params="+JsonUtils.serialize(params));
        UserCenterReturnModel userCenterReturnModel = this.loginCookie(params);
        LogUtil.info("loginUserCenter userCenterReturnModel="+JsonUtils.serialize(userCenterReturnModel));
        //System.out.println("loginUserCenter userCenterReturnModel="+JsonUtils.serialize(userCenterReturnModel));
        String status = userCenterReturnModel.getStatus();
        if(FAIL.equals(status)){
            return null;
        }
        Map<String ,String> innerParam = new HashMap<>();
        String userCookie = userCenterReturnModel.getUsercookie();
        innerParam.put("usercookie",userCookie);
        CXReturnModel cxReturnModel = this.successLogin(innerParam);
        LogUtil.info("loginUserCenter successLogin cxReturnModel="+JsonUtils.serialize(cxReturnModel));
        //System.out.println("loginUserCenter successLogin cxReturnModel="+JsonUtils.serialize(cxReturnModel));
        if(StringUtil.isEmpty(cxReturnModel.getPassword())){
            cxReturnModel.setPassword(StringUtil.md5Base64(params.get("userpassword")));
        }
        INSBAgent agent = this.saveAgent(cxReturnModel,userCookie);
        LogUtil.info("loginUserCenter agent="+JsonUtils.serialize(agent));
        //System.out.println("loginUserCenter agent="+JsonUtils.serialize(agent));
        return  agent;
    }

    public INSBAgent getAgentByUserCookie(String userCookie){
        INSBAgent agent = null;
        try {
            Map<String, String> innerParam = new HashMap<>();
            innerParam.put("usercookie", userCookie);
            LogUtil.info("getAgentByUserCookie successLogin innerParam=" + JsonUtils.serialize(innerParam));
            CXReturnModel cxReturnModel = this.successLogin(innerParam);
            LogUtil.info("getAgentByUserCookie successLogin cxReturnModel=" + JsonUtils.serialize(cxReturnModel));
            //System.out.println("loginUserCenter successLogin cxReturnModel="+JsonUtils.serialize(cxReturnModel));
            agent = this.saveAgent(cxReturnModel, userCookie);
            LogUtil.info("getAgentByUserCookie agent=" + JsonUtils.serialize(agent));
            //System.out.println("loginUserCenter agent="+JsonUtils.serialize(agent));
        }catch (Exception e){
            LogUtil.info("getAgentByUserCookie error userCookie=" + userCookie+ " errMsg="+e.getMessage());
             agent = null;
             e.printStackTrace();
        }
        return  agent;
    }

    /**
     * 实名认证通过、绑定正式工号 向集团新增用户
     * @param agent  代理人信息
     * @param jobnum 正式工号
     * @param refer 推荐人工号
     * @return
     */
    public String updateUserDetail(INSBAgent agent ,String jobnum ,String refer){
        UserCenterReturnModel userCenterReturnModel = new UserCenterReturnModel();
        try {
            if (null == agent || StringUtil.isEmpty(jobnum)) {
                userCenterReturnModel.setStatus(FAIL);
                userCenterReturnModel.setMessage("agent or jobnum is null!");
                LogUtil.info("updateUserDetail agent %s or jobnum %s is null!",JsonUtils.serialize(agent),jobnum);
                return JsonUtils.serialize(userCenterReturnModel);
            }
            CXReturnModel cxReturnModel = new CXReturnModel();
            cxReturnModel.setAgentCode(jobnum);
            cxReturnModel.setAgentstate(String.valueOf(agent.getAgentstatus()));
            //cxReturnModel.setAge();
            //cxReturnModel.setBirthday();
            cxReturnModel.setBizType("02");
            cxReturnModel.setCardNumber(agent.getIdno());
            cxReturnModel.setCardType(agent.getIdnotype());
            cxReturnModel.setCertifNo(agent.getCgfns());//资格证
            cxReturnModel.setCertifNo2(agent.getLicenseno());//执业证
            cxReturnModel.setEmail(agent.getEmail());
            cxReturnModel.setIntroAgency(StringUtil.isEmpty(refer) ? agent.getReferrer() : refer);//推荐人
            cxReturnModel.setMessage(SUCCESS);
            cxReturnModel.setMobile(null);
            cxReturnModel.setMobile2(null);
            cxReturnModel.setName(agent.getName());
            cxReturnModel.setPassword(agent.getPwd());
            cxReturnModel.setStatus(String.valueOf(agent.getAgentstatus()));
            cxReturnModel.setSex(agent.getSex());

            List<UserCenterDept> depts = new ArrayList<>();
            UserCenterDept dept = new UserCenterDept();
            dept.setDeptType("02");//财险
            dept.setAgentDeptState("01");
            dept.setDeptCode4(agent.getTeamcode());//团队代码
            dept.setDeptName4(agent.getTeamname());//团队名称

            INSCDept dept3 = inscDeptDao.selectById(agent.getDeptid());
            dept.setDeptCode3(dept3.getId());//网点代码
            dept.setDeptName3(dept3.getComname());//网点名称

            INSCDept dept2 = inscDeptDao.selectById(dept3.getUpcomcode());
            dept.setDeptCode2(dept2.getId());//分公司代码
            dept.setDeptName2(dept2.getComname());//分公司名称

            INSCDept dept1 = inscDeptDao.selectById(dept2.getUpcomcode());
            dept.setDeptCode1(dept1.getId());//法人公司代码
            dept.setDeptName1(dept1.getComname());//法人公司名称

            INSCDept dept5 = inscDeptDao.selectById(dept1.getUpcomcode());
            dept.setDeptCode5(dept5.getId());//平台代码
            dept.setDeptName5(dept5.getComname());//平台名称

            depts.add(dept);
            cxReturnModel.setUserCenterDepts(depts);

            //添加银行卡信息
            List<UserCenterBank> banks = new ArrayList<>();
            UserCenterBank bank = new UserCenterBank();
            bank.setBankcarkNo(agent.getBankcard());
            bank.setBankCode(agent.getBelongs2bank());
            bank.setName(agent.getName());
            bank.setCardType(agent.getIdnotype());
            bank.setCardNo(agent.getIdno());
            bank.setPhoneNo(agent.getPhone() == null ? agent.getMobile() : agent.getPhone());
            banks.add(bank);
            cxReturnModel.setUserCenterBanks(banks);
            userCenterReturnModel = this.updateUserDetail(cxReturnModel);
        }catch (Exception e){
            LogUtil.info("updateUserDetail 接口异常! %s",e.getMessage());
            e.printStackTrace();
            userCenterReturnModel.setStatus(FAIL);
            userCenterReturnModel.setMessage("updateUserDetail接口异常");
        }
        return JsonUtils.serialize(userCenterReturnModel);
    }


    /**
     * 00 游客
     * 01 寿险获利
     * 02 车险获利
     * 03 车险出单
     * 04 寿险出单   bizType
     */
    private INSBAgent saveAgent(CXReturnModel cxReturnModel,String userCookie){
        INSBAgent agent = new INSBAgent();
        this.compAgent(agent,cxReturnModel);
        /**
         * 推荐人信息
         */
        String refferJobNum = cxReturnModel.getIntroAgency();
        /*if(StringUtil.isNotEmpty(refferJobNum)){
            agent.setReferrer(refferJobNum);
            INSBAgent referAgent = insbAgentDao.selectByJobnum(refferJobNum);
            //先检查本地推荐人是否有注册资料，没有就从集团统一用户中心获取资料保存到本地
            if(referAgent == null){
                CXReturnModel refCxRturnModel = this.getRefCxReturnModel(refferJobNum,userCookie);
                INSBAgent refAgent = this.saveAgent(refCxRturnModel,userCookie);
                if(StringUtil.isEmpty(refAgent.getId())){
                    //找不到推荐人信息
                    LogUtil.info("推荐人不存在！refferJobNum="+refferJobNum);
                    return null;
                }
            }
        }*/
        //agent.setFirstlogintime(new Date());
        //agent.setLsatlogintime(new Date());
        // 1插入到ofuser表中
        OFUser ofuser = new OFUser();
        ofuser.setUsername(agent.getJobnum());
        ofuser.setPlainPassword(agent.getPwd());  //密码加密
        ofuser.setName(agent.getName());
        ofuser.setCreationDate(new Date().getTime());
        ofuser.setModificationDate(new Date().getTime());

        // 2 保存注册信息
        try {
            if(StringUtil.isNotEmpty(agent.getJobnum()) && StringUtil.isNotEmpty(agent.getDeptid())) {
                ofUserDao.insert(ofuser);
                insbAgentDao.insertReturnId(agent);
            }else{
                LogUtil.info("jobnum 或者 deptId 为空 ！ agent="+JsonUtils.serialize(agent));
                agent = null;
            }
        } catch (Exception e) {
            LogUtil.info("saveAgent insertReturnId 异常="+JsonUtils.serialize(agent));
            agent = null;
            e.printStackTrace();
            ofUserDao.deleteByUserName(ofuser.getUsername());
        }

        return  agent;


    }

    /**
     * 组装Agent信息
     * @param agent
     * @param cxReturnModel
     */
    private void compAgent(INSBAgent agent ,CXReturnModel cxReturnModel){
        String bizType = cxReturnModel.getBizType();
        //List<UserCenterDept> depts = cxReturnModel.getUserCenterDepts();
        String jobnum = cxReturnModel.getAgentCode();
        String name = cxReturnModel.getName();

        setUserDept(agent,cxReturnModel);
        agent.setName(name);
        agent.setSex(cxReturnModel.getSex());
        agent.setJobnum(jobnum);
        agent.setTempjobnum(jobnum);
        agent.setJobnumtype("00".equals(bizType) ? 1 : 2);

        agent.setEmail(cxReturnModel.getEmail());
        agent.setAgentstatus(Integer.parseInt(StringUtil.isEmpty(cxReturnModel.getAgentstate())?"1":cxReturnModel.getAgentstate()));
        agent.setAgentcode(cxReturnModel.getAgentCode());
        agent.setAgentkind("00".equals(bizType) ? 1 : 2);

        if(StringUtil.isNotEmpty(cxReturnModel.getPassword())) {
            agent.setPwd(cxReturnModel.getPassword());
        }
        agent.setIstest(Integer.valueOf(2));
        agent.setCgfns(cxReturnModel.getCertifNo());//资格证
        agent.setLicenseno(cxReturnModel.getCertifNo2());//执业证
        agent.setIdno(cxReturnModel.getCardNumber());
        agent.setIdnotype(cxReturnModel.getCardType());
        agent.setApprovesstate(3);
        agent.setReferrer(cxReturnModel.getIntroAgency());
        agent.setCreatetime(agent.getCreatetime()==null? new Date() : agent.getCreatetime());
        agent.setModifytime(new Date());
    }

    private CXReturnModel getRefCxReturnModel(String refJobNum,String userCookie){
        CXReturnModel cxReturnModel = new CXReturnModel();
        //从集团用户中心获取推荐人信息保存
        try {
            Map<String,String> params = new HashMap<>();
            params.put("usercookie",userCookie);
            params.put("agentcode",refJobNum);
            String resp = HttpClientUtil.doGetToken(userInfoByAgentCodeURL, params, token);
            cxReturnModel = JsonUtils.deserialize(resp,CXReturnModel.class);
        }catch (Exception e){
            cxReturnModel.setStatus(FAIL);
            cxReturnModel.setMessage("接口异常");
            LogUtil.info("getRefCxReturnModel异常 refJobNum="+refJobNum);
            e.printStackTrace();
        }

        return cxReturnModel;
    }

    public INSBAgent saveAgent(CXReturnModel data){
        INSBAgent agent = new INSBAgent();
        this.compAgent(agent,data);
        //agent.setFirstlogintime(new Date());
        //agent.setLsatlogintime(new Date());


        try {
            // 1插入到ofuser表中
            OFUser ofuser = new OFUser();
            ofuser.setUsername(agent.getJobnum());
            ofuser.setPlainPassword(agent.getPwd());  //密码加密
            ofuser.setName(agent.getName());
            ofuser.setCreationDate(new Date().getTime());
            ofuser.setModificationDate(new Date().getTime());
            if(StringUtil.isNotEmpty(agent.getJobnum()) && StringUtil.isNotEmpty(agent.getDeptid())) {
                ofUserDao.insert(ofuser);
                // 2 保存注册信息
                insbAgentDao.insertReturnId(agent);
            }else{
                LogUtil.info("jobnum 或者 deptId 为空 ！ agent="+JsonUtils.serialize(agent));
                agent = null;
            }
        } catch (Exception e) {
            LogUtil.info("saveAgent OFUser insertReturnId 异常="+JsonUtils.serialize(agent));
            e.printStackTrace();
            ofUserDao.deleteByUserName(agent.getJobnum());
            agent = null;
        }

        return  agent;
    }

    private void setUserDept(INSBAgent user, CXReturnModel cxReturnModel) {
        INSBRegion insbRegion = new INSBRegion();
        String cityCode = cxReturnModel.getCitycode();
        String provinceCode = cxReturnModel.getProvincecode();
        if (StringUtil.isNotEmpty(cityCode) && StringUtil.isEmpty(user.getDeptid())) {
            insbRegion.setComcode(cityCode);
            INSBRegion region = insbRegionService.queryOne(insbRegion);
            user.setDeptid(region == null ? "" : region.getDeptid());
        }
        if (StringUtil.isNotEmpty(provinceCode) && StringUtil.isEmpty(user.getDeptid())) {
            insbRegion.setComcode(provinceCode);
            INSBRegion region = insbRegionService.queryOne(insbRegion);
            user.setDeptid(region == null ? "" : region.getDeptid());
        }
        if(StringUtil.isNotEmpty(user.getDeptid())) {
            INSCDept dept = inscDeptDao.selectByComcode(user.getDeptid());
            String platForm = dept.getParentcodes().substring(24, 34);
            user.setPlatformcode(platForm);
            INSCDept platFormDept = inscDeptDao.selectByComcode(platForm);
            user.setPlatformname(platFormDept.getComname());
        }

    }
}
