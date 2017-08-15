package com.zzb.conf.service;

import java.util.List;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBSkill;

public interface INSBSkillService extends BaseService<INSBSkill> {
	
	/**
	 * 根据精灵id找到技能表的输入，输出项
	 * @param id
	 * @param type
	 * @return
	 */
	public List<INSBSkill> queryListByelfId(String id,String type);
	
	/**
	 * 根据精灵id删除多条技能表信息
	 * @param elfid
	 * @return
	 */
	public int deletebyelfid(String elfid);
	
	/**
	 * 获取数据库中现有的技能输入输出项
	 * @param id
	 * @param type
	 * @return
	 */
	public String filter(String id,String type);
	
	/**
	 * 根据精灵id查找技能名称
	 * @param elfid
	 * @return
	 */
	public String querySkillnameByelfid(String elfid);
}