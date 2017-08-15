package com.zzb.conf.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBRealtimetask;
import com.zzb.conf.entity.INSBWorkflowmain;
import com.zzb.conf.entity.INSBWorkflowsub;

public interface INSBRealtimetaskService extends BaseService<INSBRealtimetask> {

    void addRealtimetask(INSBWorkflowmain workflowmain);
    void addRealtimetask(INSBWorkflowsub workflowsub);
    void deleteRealtimetask(INSBWorkflowmain workflowmain);
    void deleteRealtimetask(INSBWorkflowsub workflowsub);
}