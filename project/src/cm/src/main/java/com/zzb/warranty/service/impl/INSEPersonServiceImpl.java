package com.zzb.warranty.service.impl;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.warranty.dao.INSEPersonDao;
import com.zzb.warranty.model.INSEPerson;
import com.zzb.warranty.service.INSEPersonService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/1/11.
 */
@Service
public class INSEPersonServiceImpl extends BaseServiceImpl<INSEPerson> implements INSEPersonService {
    @Resource
    INSEPersonDao personDao;



    @Override
    public void savePerson(INSEPerson person) {
        personDao.insert(person);

    }

    @Override
    public INSEPerson getPersonById(String id) {
        return personDao.selectById(id);
    }

    @Override
    protected BaseDao<INSEPerson> getBaseDao() {
        return personDao;
    }
}
