package com.zzb.cm.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBFairyInsureErrorLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * author: wz
 * date: 2017/3/25.
 */
public interface INSBFairyInsureErrorLogDao extends BaseDao<INSBFairyInsureErrorLog> {
    Page<Map<String, Object>> queryPageList(Map<String, Object> query, Pageable pageable);
}
