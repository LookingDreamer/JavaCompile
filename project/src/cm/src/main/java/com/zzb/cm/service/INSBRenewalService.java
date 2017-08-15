package com.zzb.cm.service;

import com.zzb.cm.controller.vo.RenewalItemVO;
import com.zzb.cm.entity.INSBRenewalquoteitem;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.RenewalQuoteinfoModel;
import com.zzb.mobile.model.RenewaltemSaveModel;

import java.util.List;

/**
 * Created by Dai on 2016/6/29.
 */
public interface INSBRenewalService {

    public List<INSBRenewalquoteitem> getRenewalQuoteItems(String taskid, String inscomcode);

    public List<RenewalItemVO> getRenewalConfigItems(String agreementid, String taskid, String inscomcode);

    public boolean saveRenewalQuoteitems(RenewaltemSaveModel model);
}
