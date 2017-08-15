package com.zzb.warranty.service.impl;

import com.zzb.warranty.dao.INSECarDao;
import com.zzb.warranty.model.INSECar;
import com.zzb.warranty.service.CarService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2017/1/22.
 */
@Service
public class CarServiceImpl implements CarService {

    @Resource
    INSECarDao carDao;


    @Override
    public INSECar getCarById(String id) {
        return null;
    }

    @Override
    public List<INSECar> getCarsByAgentCode(String agentCode) {
        return null;
    }
}
