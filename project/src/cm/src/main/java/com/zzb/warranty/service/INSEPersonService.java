package com.zzb.warranty.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.warranty.model.INSEPerson;

/**
 * Created by Administrator on 2017/1/9.
 */
public interface INSEPersonService extends BaseService<INSEPerson>{

    void savePerson(INSEPerson person);

    INSEPerson getPersonById(String id);
}
