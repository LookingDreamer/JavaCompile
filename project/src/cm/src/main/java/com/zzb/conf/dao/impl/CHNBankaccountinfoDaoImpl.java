package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.core.utils.LogUtil;
import com.zzb.conf.dao.CHNBankaccountinfoDao;
import com.zzb.conf.entity.CHNBankaccountinfo;

/**
 * Created by HaiJuan.Lei on 2016/9/20.
 *
 */
@Repository
public class CHNBankaccountinfoDaoImpl extends BaseDaoImpl<CHNBankaccountinfo> implements CHNBankaccountinfoDao{

    public void saveOrUpdate(CHNBankaccountinfo account){
        LogUtil.info("保存银行卡信息：" + JSONObject.fromObject(account).toString());
        if(StringUtils.isNotBlank(account.getAccountno()))
        	insert(account);
    }


    public List<CHNBankaccountinfo> getByTaskIdAndAccountNo(Map<String, String> queryParam){
        return this.sqlSessionTemplate.selectList(this.getSqlName("selectByTaskIdAndAccountNo"), queryParam);
    }
}
