package com.zzb.warranty.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.warranty.model.INSECar;

import java.util.Map;

/**
 * Created by Administrator on 2017/1/17.
 */
public interface SearchCarDao extends BaseDao<INSECar> {
    Map<String, Object> searchINSBCar(String key);

    Map<String, Object> searchINSECar(String key);
}
