package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSBLoopunderwritingDao;
import com.zzb.cm.entity.INSBLoopunderwriting;
import com.zzb.cm.service.INSBLoopunderwritingService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class INSBLoopunderwritingServiceImpl extends BaseServiceImpl<INSBLoopunderwriting> implements INSBLoopunderwritingService {
	@Resource
	private INSBLoopunderwritingDao insbLoopunderwritingDao;

	@Override
	protected BaseDao<INSBLoopunderwriting> getBaseDao() {
		return insbLoopunderwritingDao;
	}

	public String searchList(Map<String, Object> paramMap) {
		List<Map<String, Object>> list = insbLoopunderwritingDao.searchList(paramMap);
		long total = insbLoopunderwritingDao.searchCount(paramMap);

		Map<String, Object> map = new HashMap<String, Object>();
		for (int i=0; i<list.size(); i+=1) {
			Map<String, Object> temp = list.get(i);
			String loopid = (String) temp.get("loopid");
			temp.put("detail", "<a href=\"javascript:window.parent.openLargeDialog(\'business/loop/showdetail?loopid="+loopid+
					"&platenumber="+temp.get("platenumber")+"&insuredname="+temp.get("insuredname")+"\');\">查看任务详情</a>");
		}

		map.put("records", "10000");
		map.put("page", 1);
		map.put("total",total);
		map.put("rows", list);

		JSONObject jsonObject = JSONObject.fromObject(map);
		return jsonObject.toString();
	}
}