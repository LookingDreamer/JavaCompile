package com.zzb.cm.dao.impl;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.core.utils.BeanUtils;
import com.zzb.cm.dao.INSBFairyInsureErrorLogDao;
import com.zzb.cm.entity.INSBFairyInsureErrorLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * author: wz
 * date: 2017/3/25.
 */
@Repository
public class INSBFairyInsureErrorLogDaoImpl extends BaseDaoImpl<INSBFairyInsureErrorLog> implements INSBFairyInsureErrorLogDao {
    @Override
    public Page<Map<String, Object>> queryPageList(Map<String, Object> query, Pageable pageable) {
        if (pageable != null) {
            Map<String, Object> params = BeanUtils.toMap(getRowBounds(pageable));
            query.putAll(params);
            if (pageable.getSort() != null) {
                query.put("sorting", pageable.getSort().toString());
            }
        }
        List<Map<String, Object>> result = sqlSessionTemplate.selectList(getSqlName("select"), query);
        return new PageImpl<>(result, pageable, selectCount(query));
    }

    public Long selectCount(Map<String, Object> query) {
        return sqlSessionTemplate.selectOne(getSqlName("selectCount"), query);
    }
}
