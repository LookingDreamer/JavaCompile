package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSBCarinfoDao;
import com.zzb.cm.dao.INSBCarmodelinfoDao;
import com.zzb.cm.dao.INSBCarmodelinfohisDao;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBCarmodelinfo;
import com.zzb.cm.entity.INSBCarmodelinfohis;
import com.zzb.cm.entity.entityutil.EntityTransformUtil;
import com.zzb.cm.service.INSBCarmodelinfohisService;

@Service
@Transactional
public class INSBCarmodelinfohisServiceImpl extends BaseServiceImpl<INSBCarmodelinfohis> implements
		INSBCarmodelinfohisService {
	@Resource
	private INSBCarmodelinfohisDao insbCarmodelinfohisDao;
	@Resource
	private INSBCarinfoDao insbCarinfoDao;
	@Resource
	private INSBCarmodelinfoDao insbCarmodelinfoDao;
	
	@Override
	protected BaseDao<INSBCarmodelinfohis> getBaseDao() {
		return insbCarmodelinfohisDao;
	}

	@Override
	public INSBCarmodelinfohis selectCarmodelinfoByTaskIdAndCode(String taskid,
			String inscomcode) {
		INSBCarinfo insbCarinfo=insbCarinfoDao.selectCarinfoByTaskId(taskid);
		INSBCarmodelinfohis insbCarmodelinfohis=new INSBCarmodelinfohis();
		insbCarmodelinfohis.setCarinfoid(insbCarinfo.getId());
		insbCarmodelinfohis.setInscomcode(inscomcode);
		INSBCarmodelinfohis insbCarmodelinfohis1=insbCarmodelinfohisDao.selectOne(insbCarmodelinfohis);
		if (insbCarmodelinfohis1==null) {
			INSBCarmodelinfo insbCarmodelinfo=insbCarmodelinfoDao.selectByCarinfoId(insbCarinfo.getId());
					insbCarmodelinfohis1=EntityTransformUtil.carmodelinfo2Carmodelinfohis(insbCarmodelinfo, inscomcode);	
		}
		return insbCarmodelinfohis1;
	}

}