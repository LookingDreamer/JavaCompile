package com.zzb.mobile.service;


import com.zzb.conf.entity.INSBAgent;
import com.zzb.mobile.model.usercenter.CXReturnModel;
import com.zzb.mobile.model.usercenter.UserCenterReturnModel;

import java.util.Map;

/**
 * Created by Hwc on 2017/5/22.
 */
public interface UserCenterService {
    /**
     *{
     "backurl": "string",
     "checkcode": "string",
     "logincode": "13212345678",
     "userpassword": "123456"
     }
     * @param params
     * @return
     */
    public UserCenterReturnModel loginCookie(Map<String ,String> params);

    /**
     * usercookie
     *根据cookie获取登录成功信息
     */
    public CXReturnModel successLogin(Map<String ,String> params);

    /**
     * 注册验证手机号在其他平台是否有账号
     * phoneno
     * @param params
     * @return
     */
    public UserCenterReturnModel validateMobileNo(Map<String ,String> params);

    /**
     * 新注册用户需要推送数据到数据中心
     * @param data
     * @return
     */
    public UserCenterReturnModel updateUserDetail(CXReturnModel data);

    /**
     *  omobile	String	旧得手机号码
     *  nmobile	String	新手机号码，没有修改为空
     *  cardno	String	身份证号码
     *  agentcode	String	代理人工号
     *  password	String	密码明文
     * 修改账户数据
     * @param params
     * @return
     */
    public UserCenterReturnModel updateUserAccount(Map<String ,String> params);

    /**
     * 用户中心修改了用户数据，更新平台用户数据
     * @param data
     * @return
     */
    public UserCenterReturnModel updateUserDetailFromUserCenter(CXReturnModel data);

    public INSBAgent loginUserCenter(Map<String ,String> params);

    /**
     * 从同一用户中心获取用户数据
     * @param userCookie
     * @return
     */
    public INSBAgent getAgentByUserCookie(String userCookie);

    /**
     * 实名认证通过、绑定正式工号 向集团新增用户
     * @param agent  代理人信息
     * @param jobnum 正式工号
     * @return
     */
    public String updateUserDetail(INSBAgent agent ,String jobnum,String reffer);

    /**
     * 统一中心调用掌中保同步数据
     * @param data
     * @return
     */
    public INSBAgent saveAgent(CXReturnModel data);


}
