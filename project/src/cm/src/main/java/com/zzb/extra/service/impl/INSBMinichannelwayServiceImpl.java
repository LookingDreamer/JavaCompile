package com.zzb.extra.service.impl;

import javax.annotation.Resource;

import com.zzb.chn.util.JsonUtils;
import com.zzb.extra.util.ParamUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.extra.dao.INSBMinichannelwayDao;
import com.zzb.extra.entity.INSBMinichannelway;
import com.zzb.extra.service.INSBMinichannelwayService;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class INSBMinichannelwayServiceImpl extends BaseServiceImpl<INSBMinichannelway> implements
		INSBMinichannelwayService {
	@Resource
	private INSBMinichannelwayDao insbMinichannelwayDao;

	@Override
	protected BaseDao<INSBMinichannelway> getBaseDao() {
		return insbMinichannelwayDao;
	}

	@Override
	public String queryChannelWayList(Map<String, Object> map) {
		INSBMinichannelway insbMinichannelway = new INSBMinichannelway();
		insbMinichannelway.setChannelcode(String.valueOf(map.get("channelcode")));
		Long total = insbMinichannelwayDao.selectCount(insbMinichannelway);
		List<Map<Object, Object>> channelList = insbMinichannelwayDao.selectChannelWayList(map);
		return ParamUtils.resultMap(total, channelList);
	}

	@Override
	public long selectMaxWayode(Map<String,Object> map) {
		return insbMinichannelwayDao.selectMaxWayCode(map);
	}
}