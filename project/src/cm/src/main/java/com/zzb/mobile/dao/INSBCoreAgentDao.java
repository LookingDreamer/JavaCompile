package com.zzb.mobile.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.mobile.entity.INSBCoreAgent;

/**
 * Created by HaiJuan.Lei on 2016/10/19.
 *
 */
public interface INSBCoreAgentDao extends BaseDao<INSBCoreAgent> {
    int getAgentCnt(String agentCode);

}
