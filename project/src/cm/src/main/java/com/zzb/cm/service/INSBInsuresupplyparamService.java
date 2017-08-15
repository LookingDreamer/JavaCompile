package com.zzb.cm.service;

import com.cninsure.core.dao.BaseService;
import com.cninsure.system.entity.INSCCode;
import com.zzb.cm.entity.INSBInsuresupplyparam;

import java.util.List;
import java.util.Map;

public interface INSBInsuresupplyparamService extends BaseService<INSBInsuresupplyparam> {

    public int insertByTask(List<INSBInsuresupplyparam> insuresupplyparamList, String taskid, String inscomcode);

    public int updateByTask(List<String> insuresupplyparamList, String taskid, String inscomcode, String operator);

    public int saveByTask(List<INSBInsuresupplyparam> insuresupplyparamList, String taskid, String inscomcode, String operator);

    public Map<String, String> getParamsByTask(String taskid, String inscomcode, boolean editable);

    public List<INSCCode> getCertKinds();
}