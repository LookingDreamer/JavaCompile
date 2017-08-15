package com.zzb.cm.service.impl;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSBFairyInsureErrorLogDao;
import com.zzb.cm.entity.INSBFairyInsureErrorLog;
import com.zzb.cm.service.INSBFairyInsureErrorLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * author: wz
 * date: 2017/3/25.
 */
@Service
public class INSBFairyInsureErrorLogServiceImpl extends BaseServiceImpl<INSBFairyInsureErrorLog> implements INSBFairyInsureErrorLogService {
    @Resource
    INSBFairyInsureErrorLogDao fairyInsureErrorLogDao;

    @Override
    protected BaseDao<INSBFairyInsureErrorLog> getBaseDao() {
        return fairyInsureErrorLogDao;
    }

    @Override
    public Page<Map<String, Object>> queryPageList(Map<String, Object> query, Pageable pageable) {
        return fairyInsureErrorLogDao.queryPageList(query, pageable);
    }
}
