package com.cninsure.system.dao;

import java.util.Map;

import com.cninsure.system.entity.INSCMethodCode;


public interface INSCMethodCodeDao{

	
	public int insert(INSCMethodCode methodCodeModel);
	
	public String selectMethodNameByMethod(Map<String,String> param);
}