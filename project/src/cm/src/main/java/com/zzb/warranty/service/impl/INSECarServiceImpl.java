package com.zzb.warranty.service.impl;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.warranty.dao.INSECarDao;
import com.zzb.warranty.model.INSECar;
import com.zzb.warranty.service.INSECarService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/11.
 */
@Service
public class INSECarServiceImpl extends BaseServiceImpl<INSECar> implements INSECarService {
    @Resource
    INSECarDao carDao;


    @Override
    protected BaseDao<INSECar> getBaseDao() {
        return carDao;
    }

    @Override
    public void insertCar(INSECar inseCar) {
        carDao.insertCar(inseCar);
    }

    @Override
    public void updateCarById(INSBCarinfo insbCarinfo) {
        carDao.updateCarById(insbCarinfo);
    }


    @Override
    public INSECar getCarById(String id) {
        return carDao.getCarById(id);
    }

    @Override
    public List<Map<String, Object>> selectByOwnernameAndPlateNumber(String agentCode, String key) {
        return carDao.selectByOwnernameAndPlateNumber(agentCode, key);
    }
}
