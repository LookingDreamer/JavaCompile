package com.cninsure.system.dao.impl;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cninsure.system.dao.INSCMethodCodeDao;
import com.cninsure.system.entity.INSCMethodCode;

@Repository
public class INSCMethodCodeDaoImpl implements INSCMethodCodeDao{

	@Autowired(required = true)
	private SqlSession sqlSessionTemplate;

	public void setSqlSession1(SqlSession sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@Override
	public int insert(INSCMethodCode methodCodeModel) {
		return sqlSessionTemplate.insert("INSCMethodCode_insert",methodCodeModel);
	}

	@Override
	public String selectMethodNameByMethod(Map<String,String> param) {
		return sqlSessionTemplate.selectOne("INSCMethodCode_selectMethodNameByMethod",param);
	}

	
}