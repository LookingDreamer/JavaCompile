package com.zzb.conf.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.constants.SqlId;
import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.core.exception.DaoException;
import com.zzb.conf.dao.OFUserDao;
import com.zzb.conf.entity.OFUser;

@Repository
public class OFUserDaoImpl extends BaseDaoImpl<OFUser>  implements OFUserDao {

	@Override
	public List<OFUser> queryOFUser(OFUser user) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectOne"),user);
	}

	@Override
	public OFUser queryOneOFUser(OFUser user) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("s	electOne"),user);
	}

	@Override
	public OFUser queryByUserName(String username) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectByUserName"),username);
	}
	
	@Override
	public void deleteByUserName(String username) {
		this.sqlSessionTemplate.delete(this.getSqlName("deleteByUserName"),username);
	}

	@Override
	public int updateByUserName(OFUser user) {
		return this.sqlSessionTemplate.update(this.getSqlName("updateByUserName"),user);
	}
	@Override
	public void insert(OFUser entity) {
		try {
			sqlSessionTemplate.insert(getSqlName(SqlId.SQL_INSERT), entity);
		} catch (Exception e) {
			throw new DaoException(String.format("添加对象出错！语句：%s", getSqlName(SqlId.SQL_INSERT)), e);
		}
	}
}