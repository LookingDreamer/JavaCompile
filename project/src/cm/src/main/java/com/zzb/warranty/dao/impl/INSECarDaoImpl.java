package com.zzb.warranty.dao.impl;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.warranty.dao.INSECarDao;
import com.zzb.warranty.model.INSECar;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/10.
 */
@Repository
public class INSECarDaoImpl extends BaseDaoImpl<INSECar> implements INSECarDao {
    @Override
    public void insertCar(INSECar inseCar) {
        this.sqlSessionTemplate.insert(getSqlName("insert"), inseCar);
    }

    @Override
    public void updateCarById(INSBCarinfo insbCarinfo) {
        this.sqlSessionTemplate.update(getSqlName("updateById"), insbCarinfo);
    }

    @Override
    public INSECar getCarById(String id) {
        return this.sqlSessionTemplate.selectOne(getSqlName("selectById"), id);

    }

    @Override
    public List<Map<String, Object>> selectByOwnernameAndPlateNumber(String agentCode, String key) {
        Map<String, String> params = new HashMap<>();
        params.put("agentCode", agentCode);
        params.put("key", key);

        return this.sqlSessionTemplate.selectList(getSqlName("selectByOwnernameAndPlateNumber"), params);
    }
}
