package com.zzb.warranty.dao;

import com.zzb.warranty.model.INSECar;

import java.util.List;

/**
 * Created by Administrator on 2017/1/22.
 */
public interface INSECarMapper {

    INSECar getCarById(String id);

    List<INSECar> getCarsByAgentCode(String agentCode);
}
