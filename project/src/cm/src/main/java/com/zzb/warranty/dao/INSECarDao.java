package com.zzb.warranty.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.warranty.model.INSECar;

import java.util.List;
import java.util.Map;

public interface INSECarDao extends BaseDao<INSECar> {

    void insertCar(INSECar inseCar);

    void updateCarById(INSBCarinfo insbCarinfo);

    INSECar getCarById(String id);

    List<Map<String, Object>> selectByOwnernameAndPlateNumber(String agentCode, String key);
}