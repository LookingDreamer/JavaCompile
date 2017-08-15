package com.zzb.cm.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBPerson;

public interface INSBPersonDao extends BaseDao<INSBPerson> {
	public INSBPerson selectPersonByTaskId(String taskid);

	/**
	 * 车险任务其他信息修改页面对驾驶人信息的修改
	 * */
	public void updateSpecifydriverById(INSBPerson person);

	/**
	 *  人工报价 关系人信息修改 通过map查找person信息
	 * 
	 * @param map
	 * @return
	 */
	public INSBPerson selectPersonByMap(Map<String, Object> map);

    /**
     * 索赔权益人信息
     */
    public INSBPerson selectJoinLegalBytaskId(String taskid);

    /**
     * 被保人信息
     */
    public INSBPerson selectJoinBinBytaskId(String taskid);

    /**
     * 投保人信息
     */
    public INSBPerson selectJoinAppBytaskId(String taskid);

    /**
     * 关系人信息的修改
     * */
    public void updateRelationPersonById(INSBPerson person);

	public int updateById(INSBPerson person);

	public void deletebyID(String id);

	/**
	 * 如果数据库中有就更新，没有就保存
	 * 
	 * @param person
	 * @return
	 */
	public void saveOrUpdate(INSBPerson person);

	public List<INSBPerson> selectByAgent(Map<String, Object> map);

	public INSBPerson selectApplicantPersonByTaskId(String taskId);

	public INSBPerson selectInsuredPersonByTaskId(String taskId);

	public INSBPerson selectCarOwnerPersonByTaskId(String taskId);

	INSBPerson selectBeneficiaryByTaskId(String taskId);

	/**
	 * 根据保险公司编码和任务号查找单方投保人信息
	 * */
	public INSBPerson selectApplicantHisPerson(Map<String, Object> map);
	/**
	 * 根据保险公司编码和任务号查找单方被保人信息
	 * */
	public INSBPerson selectInsuredHisPerson(Map<String, Object> map);
	/**
	 * 获取需要删除insbPerson表无效记录id
	 * @param taskID 工作流号
	 * @return
	 */
	public List<String> getSelectDelId(String taskID);
}