package com.zzb.cm.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSBQuoteinfoDao;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.service.INSBQuoteinfoService;

@Service
@Transactional
public class INSBQuoteinfoServiceImpl extends BaseServiceImpl<INSBQuoteinfo>
		implements INSBQuoteinfoService {
	@Resource
	private INSBQuoteinfoDao insbQuoteinfoDao;

	@Override
	protected BaseDao<INSBQuoteinfo> getBaseDao() {
		return insbQuoteinfoDao;
	}

	public INSBQuoteinfo getQuoteinfoByWorkflowinstanceid(
			String workflowinstanceid) {
		return insbQuoteinfoDao
				.queryQuoteinfoByWorkflowinstanceid(workflowinstanceid);
	}

	@Override
	public INSBQuoteinfo getQuoteinfo(String quotetotalinfoid, String inscomcode) {
		if(StringUtils.isNoneBlank(quotetotalinfoid,inscomcode)){
			Map<String, String> param = new HashMap<String, String>();
			param.put("quotetotalinfoid", quotetotalinfoid);
			param.put("inscomcode", inscomcode);
			return insbQuoteinfoDao.selectQuoteinfoByQuotetotalinfoidAndinscomcode(
					param);
		}
		return null;
	}

	@Override
	public INSBQuoteinfo getByTaskidAndCompanyid(String taskid, String companyid) {
		Map<String, String> map = new HashMap<String,String>();
		map.put("taskid", taskid);
		map.put("companyid", companyid);
		return insbQuoteinfoDao
				.getByTaskidAndCompanyid(map);
	}

	@Override
	public List<INSBQuoteinfo> getQuoteinfosByInsbQuotetotalinfoid(String quotetotalinfoid) {
		// TODO Auto-generated method stub
		INSBQuoteinfo param = new INSBQuoteinfo();
		param.setQuotetotalinfoid(quotetotalinfoid);
		return insbQuoteinfoDao.selectList(param);
	}
}