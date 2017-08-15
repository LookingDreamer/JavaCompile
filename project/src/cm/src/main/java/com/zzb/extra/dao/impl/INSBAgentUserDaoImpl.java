package com.zzb.extra.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.extra.dao.INSBAgentUserDao;
import com.zzb.extra.model.INSBAgentUserQueryModel;

/**
 * Created by liwucai on 2016/8/5 14:17.
 */
@Repository
public class INSBAgentUserDaoImpl extends BaseDaoImpl<INSBAgent> implements INSBAgentUserDao {

    @Override
    public int auditAgentIdentity(INSBAgent agent) {
        return this.sqlSessionTemplate.update(this.getSqlName("auditAgentIdentity"), agent);
    }

    @Override
    public int bindingAgentIdentity(INSBAgent agent) {
        return this.sqlSessionTemplate.update(this.getSqlName("bindingAgentIdentity"), agent);
    }

    /**
     * 被推荐人 列表
     * @param referrerid 推荐人id
     */
    @Override
    public List<Map<String,Object>> presenteeList(String referrerid){
        return this.sqlSessionTemplate.selectList(this.getSqlName("presenteeListQueryByReferrerId"), referrerid);
    }

    @Override
    public INSBAgent selectByOpenid(String openid) {
        return this.sqlSessionTemplate.selectOne(this.getSqlName("selectByOpenid"), openid);
    }

    /**
     * 用户身份验证接口（检查用户提交的认证信息：openid 是否有绑定 手机号）
     * @param map 代理人信息
     * @return
     */
    @Override
    public INSBAgent validateAgentIdentity(Map<String, Object> map){
        return this.sqlSessionTemplate.selectOne(this.getSqlName("selectOne"), map);
    }

    /**
     * 查询手机号是否 已经是 minizzb用户绑定手机
     * true是，false不是
     */
    @Override
    public boolean checkIsMinizzbUserPhone(String phone){
        List<INSBAgent> list = this.sqlSessionTemplate.selectList(this.getSqlName("checkIsMinizzbUserPhonesql"), phone);
        if(list!=null && list.size()>0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 查询手机号是否 已经是 体系内代理人绑定手机
     * true是，false不是
     */
    @Override
    public boolean checkIsAgentPhone(String phone){
        List<INSBAgent> list = this.sqlSessionTemplate.selectList(this.getSqlName("checkIsAgentPhonesql"), phone);
        if(list!=null && list.size()>0){
            return true;
        }else{
            return false;
        }
    }

	@Override
	public List<INSBAgentUserQueryModel> selectListPageByDeptIds_byAgentUser(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectPageByDeptIds_byAgentUser"), map);
	}
}
