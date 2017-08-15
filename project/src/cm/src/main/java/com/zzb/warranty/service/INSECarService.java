package com.zzb.warranty.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.warranty.model.INSECar;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/9.
 */
public interface INSECarService extends BaseService<INSECar>{

    void insertCar(INSECar insbCarinfo);

    void updateCarById(INSBCarinfo insbCarinfo);


    INSECar getCarById(String id);

    List<Map<String, Object>> selectByOwnernameAndPlateNumber(String agentCode, String key);
}
