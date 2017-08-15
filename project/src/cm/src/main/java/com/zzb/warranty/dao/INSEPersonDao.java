package com.zzb.warranty.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.warranty.model.INSEPerson;

import java.util.List;
import java.util.Map;

public interface INSEPersonDao extends BaseDao<INSEPerson> {
    void insertPerson(INSEPerson person);

    INSEPerson selectPersonById(String personId);
}