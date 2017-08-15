package com.zzb.extra.dao.impl;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.extra.dao.INSBMiniPublicDao;
import com.zzb.extra.entity.INSBAccount;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by Administrator on 2016/10/26.
 */
@Repository
public class INSBMiniPublicDaoImpl extends BaseDaoImpl<INSBAccount> implements INSBMiniPublicDao {
    @Override
    public Map<String, Object> queryUserComment(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectOne("INSBMiniPublic_select",map);
    }
}
