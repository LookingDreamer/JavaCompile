package com.zzb.conf.dao;

import java.util.List;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBSkill;

public interface INSBSkillDao extends BaseDao<INSBSkill> {
	
	/**
	 * 根据精灵id找技能输入。输出项
	 * @param id
	 * @param type
	 * @return
	 */
	public List<INSBSkill> queryListByelfId(String id,String type);
	
	/**
	 * 根据精灵id删除技能信息
	 * @param elfid
	 * @return
	 */
	public int deletebyelfid(String elfid);
	
	/**
	 * 获取现有的输入输出项
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