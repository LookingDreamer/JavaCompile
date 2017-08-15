package com.zzb.extra.service.impl;

import javax.annotation.Resource;

import com.zzb.chn.util.JsonUtils;
import com.zzb.extra.util.ParamUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.extra.dao.INSBMinichannelDao;
import com.zzb.extra.entity.INSBMinichannel;
import com.zzb.extra.service.INSBMinichannelService;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class INSBMinichannelServiceImpl extends BaseServiceImpl<INSBMinichannel> implements
		INSBMinichannelService {
	@Resource
	private INSBMinichannelDao insbMinichannelDao;

	@Override
	protected BaseDao<INSBMinichannel> getBaseDao() {
		return insbMinichannelDao;
	}

	@Override
	public String queryChannelList(Map<String,Object> map) {
		Long total = insbMinichannelDao.selectChannelCount(map);
		List<Map<Object, Object>> channelList = insbMinichannelDao.selectChannelList(map);
		return ParamUtils.resultMap(total, channelList);
	}

	@Override
	public long selectMaxTempcode(){
		return insbMinichannelDao.selectMaxTempcode();
	}

	@Override
	public int updateWayNum(Map<String, Object> map) {
		return insbMinichannelDao.updateWayNum(map);
	}
}