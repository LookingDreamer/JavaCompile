package com.zzb.extra.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.extra.model.INSBAgentUserQueryModel;

/**
 * Created by liwucai on 2016/8/5 12:00.
 */
public interface INSBAgentUserDao  extends BaseDao<INSBAgent> {

    public INSBAgent selectByOpenid(String openid);

    /**
     * 绑定手机号
     */
    public int bindingAgentIdentity(INSBAgent agent);

    /**
     * 更新代理人身份认证信息
     */
    public int auditAgentIdentity(INSBAgent agent);

    /**
     * 用户身份验证接口（检查用户提交的认证信息：openid 是否有绑定 手机号）
     * @param map 代理人信息
     * @return
     */
    public INSBAgent validateAgentIdentity(Map<String, Object> map);

    /**
     * 查询手机号是否 已经是 体系内代理人绑定手机
     * true是，false不是
     */
    public boolean checkIsAgentPhone(String phone);

    /**
     * 被推荐人 列表
     * @param referrerid 推荐人id
     */
    public List<Map<String,Object>> presenteeList(String referrerid);


    /**
     * 查询手机号是否 已经是 minizzb用户绑定手机
     * true是，false不是
     */
    public boolean checkIsMinizzbUserPhone(String phone);
    
    public List<INSBAgentUserQueryModel> selectListPageByDeptIds_byAgentUser(Map<String, Object> map);

}
