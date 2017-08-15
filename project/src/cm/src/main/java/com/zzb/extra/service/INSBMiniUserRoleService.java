package com.zzb.extra.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.extra.entity.INSBMiniUserRole;

public interface INSBMiniUserRoleService extends BaseService<INSBMiniUserRole> {

    public void insert(INSBMiniUserRole insbMiniUserRole);

    
    
    public String selectUserAttr(String openid);
}