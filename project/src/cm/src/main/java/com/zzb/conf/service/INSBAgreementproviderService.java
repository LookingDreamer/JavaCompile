package com.zzb.conf.service;

import java.util.List;

import org.apache.poi.ss.usermodel.Row;

import com.cninsure.core.dao.BaseService;
import com.cninsure.system.entity.INSCUser;
import com.zzb.conf.entity.INSBAgreementprovider;

public interface INSBAgreementproviderService extends BaseService<INSBAgreementprovider> {
	
	public String deletePrvDeptPay(String id,String agreeid) throws Exception;
	
	public String procImportPrvData(String agreementid, List<Row> rows, INSCUser operator, String channelid) throws Exception;

	public String batchCopyChannelProvider(String fromChannelId,String toChannelIds,String user);
	
}