package com.zzb.chn.service;

import com.zzb.chn.bean.QuoteBean;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.model.WorkFlow4TaskModel;

public interface INSBThirdCommissionService {
	void thirdCommission(WorkFlow4TaskModel dataModel, INSBQuotetotalinfo insbQuotetotalinfo) throws Exception;
	void thirdCommission(QuoteBean quoteBean, INSBQuotetotalinfo insbQuotetotalinfo);
}
