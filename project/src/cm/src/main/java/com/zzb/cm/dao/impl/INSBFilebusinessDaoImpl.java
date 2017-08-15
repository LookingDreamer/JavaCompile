package com.zzb.cm.dao.impl;

import com.cninsure.core.exception.DaoException;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSBFilebusinessDao;
import com.zzb.cm.entity.INSBFilebusiness;

import java.util.List;
import java.util.Map;

@Repository
public class INSBFilebusinessDaoImpl extends BaseDaoImpl<INSBFilebusiness> implements
		INSBFilebusinessDao {

	@Override
	public void deleteByFilelibraryids(List<String> filelibraryids) {

		if(filelibraryids != null && !filelibraryids.isEmpty()) {
			filelibraryids.forEach(filelibraryid -> {
						try {
							this.sqlSessionTemplate.delete(this.getSqlName("deleteBy_filelibraryid"), filelibraryid);
						} catch (Exception var3) {
							throw new DaoException(String.format("根据 Filelibraryid 删除对象 INSBFilebusiness 出错！语句：%s", new Object[]{this.getSqlName("deleteBy_filelibraryid")}), var3);
						}
					}
			);
		}
	}

	@Override
	public void deleteByFilelibraryByTadkIdImageType(Map<String,String> params) {
		this.sqlSessionTemplate.delete(this.getSqlName("deleteBy_imagetype_taskId"), params);
	}

	/**
	 * 按id删除
	 */
	@Override
	public int deleteIn(List<String> list){
		return this.sqlSessionTemplate.delete(this.getSqlName("deleteInIds"), list);
	}
}