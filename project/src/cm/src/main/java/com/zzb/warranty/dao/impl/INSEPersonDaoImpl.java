package com.zzb.warranty.dao.impl;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.warranty.dao.INSEPersonDao;
import com.zzb.warranty.model.INSEPerson;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2017/1/10.
 */
@Repository
public class INSEPersonDaoImpl extends BaseDaoImpl<INSEPerson> implements INSEPersonDao{
    @Override
    public void insertPerson(INSEPerson person) {
        insert(person);
    }

    @Override
    public INSEPerson selectPersonById(String personId) {

        return selectById(personId);
    }
}
