package com.cninsure.system.dao.impl;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cninsure.system.dao.INSCCmLogsDao;
import com.cninsure.system.entity.INSCCmLogs;

@Repository
public class INSCCmLogsDaoImpl implements INSCCmLogsDao{

	@Autowired(required = true)
	private SqlSession sqlSessionTemplate;

	
	public void setSqlSession1(SqlSession sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}
	@Override
	public int insert(INSCCmLogs cmLogModel) {
		sqlSessionTemplate.insert("INSCCmLogs_insert",cmLogModel);
		return 0;
	}

	
}