package com.zzb.conf.dao;

import java.util.List;

import com.zzb.conf.entity.INSBBaseData;


public interface INSBBaseDataDao  {

	/**
	 * 按条件查询 
	 * @param model 查询条件
	 * @param page 当前页
	 * @param rows 每页多少条
	 * @return
	 */
	public List<INSBBaseData> selectBaseDatasByModel(INSBBaseData model,int page,int rows);
	/**
	 * 初始化列表页
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<INSBBaseData> selectBaseDatas(int page,int rows);
	/**
	 * 新增
	 * @param model
	 * @return
	 */
	public int insertBaseDatas(INSBBaseData model);
	/**
	 * TODO 删除（按主键删除   按业务主键删除？） 当前id删除
	 * @param model
	 * @return
	 */
	public int deleteBaseDatas(String id);
	/**
	 * 修改
	 * @param model
	 * @return
	 */
	public int updateBaseDatas(INSBBaseData model);
	
}
