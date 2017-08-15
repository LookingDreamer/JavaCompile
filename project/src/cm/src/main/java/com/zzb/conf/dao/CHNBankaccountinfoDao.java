package com.zzb.conf.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.CHNBankaccountinfo;

import java.util.List;
import java.util.Map;

/**
 * Created by HaiJuan.Lei on 2016/9/20.
 *
 */
public interface CHNBankaccountinfoDao extends BaseDao<CHNBankaccountinfo> {

    /**
     * 根据任务号和银行卡号查询是否已经有相同的记录
     * */
    public List<CHNBankaccountinfo> getByTaskIdAndAccountNo(Map<String,String> map);

    /**
     * 根据任务号和银行卡号查询是否已经有记录，有则更新，无则插入一条新的记录*/
    public void saveOrUpdate(CHNBankaccountinfo account);



 }
