package com.zzb.extra.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.extra.entity.INSBAccount;

import java.util.Map;

public interface INSBMiniPublicDao extends BaseDao<INSBAccount> {
   public Map<String,Object> queryUserComment(Map<String,Object> map);
}