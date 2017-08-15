package com.zzb.cm.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSBPersonDao;
import com.zzb.cm.entity.INSBPerson;

@Repository
public class INSBPersonDaoImpl extends BaseDaoImpl<INSBPerson> implements INSBPersonDao {

	@Override
	public INSBPerson selectPersonByTaskId(String taskid) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectByTaskId"), taskid);
	}

	/**
	 *  车险任务其他信息修改页面对驾驶人信息的修改
	 * */
	@Override
	public void updateSpecifydriverById(INSBPerson person) {
		this.sqlSessionTemplate.update(this.getSqlName("updateSpecifydriverById"), person);
	}

	@Override
	public INSBPerson selectPersonByMap(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("select"), map);

	}

	/**
	 * 关系人信息的修改
	 * */
	@Override
	public void updateRelationPersonById(INSBPerson person) {
		this.sqlSessionTemplate.update(this.getSqlName("updateRelationPersonById"), person);
	}

	@Override
	public int updateById(INSBPerson person) {
		return this.sqlSessionTemplate.update(this.getSqlName("updateById"), person);
	}

	@Override
	public void deletebyID(String id) {
		this.sqlSessionTemplate.delete(this.getSqlName("deleteById"), id);
	}

	/**
	 * 索赔权益人信息
	 * */
	@Override
	public INSBPerson selectJoinLegalBytaskId(String taskid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskid", taskid);
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectJoinLegalBytaskId"), map);
	}

	/**
	 * 被保人信息
	 * */
	@Override
	public INSBPerson selectJoinBinBytaskId(String taskid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskid", taskid);
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectJoinBinBytaskId"), map);
	}

	/**
	 * 投保人信息
	 * */
	@Override
	public INSBPerson selectJoinAppBytaskId(String taskid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskid", taskid);
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectJoinAppBytaskId"), map);
	}

	@Override
	public void saveOrUpdate(INSBPerson person) {
		INSBPerson temp = null;
		if (person.getId() != null) {
			temp = this.selectById(person.getId());
		}
		if (temp == null) {
			person.setCreatetime(new Date());
			person.setModifytime(null);
			this.insert(person);
		} else {
			person.setModifytime(new Date());
			this.updateRelationPersonById(person);
		}
	}

	@Override
	public List<INSBPerson> selectByAgent(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByAgent"), map);
	}

	@Override
	public INSBPerson selectApplicantPersonByTaskId(String taskId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskid", taskId);
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectApplicantPersonByTaskId"), map);
	}

	@Override
	public INSBPerson selectInsuredPersonByTaskId(String taskId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskid", taskId);
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectInsuredPersonByTaskId"), map);
	}

	@Override
	public INSBPerson selectCarOwnerPersonByTaskId(String taskId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskid", taskId);
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectCarOwnerPersonByTaskId"), map);
	}
	@Override
	public INSBPerson selectBeneficiaryByTaskId(String taskId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskid", taskId);
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectBeneficiaryByTaskId"), map);
	}


	@Override
	public INSBPerson selectApplicantHisPerson(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectApplicantHisPerson"), map);
	}

	@Override
	public INSBPerson selectInsuredHisPerson(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectInsuredHisPerson"), map);
	}
	
	@Override
	public List<String> getSelectDelId(String taskID) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectDelId"),taskID);
	}

}