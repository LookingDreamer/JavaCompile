package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.core.utils.StringUtil;
import com.zzb.conf.dao.INSBAgentpermissionDao;
import com.zzb.conf.entity.INSBAgentpermission;

@Repository
public class INSBAgentpermissionDaoImpl extends BaseDaoImpl<INSBAgentpermission> implements
		INSBAgentpermissionDao {

	@Override
	public INSBAgentpermission selectListByLogicId(Map<String, String> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectByLogicId"), map);
	}

	@Override
	public int deleteByAgentId(String agentId) {
		return this.sqlSessionTemplate.delete(this.getSqlName("deleteByAgentId"), agentId);
	}

	@Override
	public List<INSBAgentpermission> selectByAgentId(String agentId) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByAgentId"), agentId);
	}

	/**
	 * 查询代理人试用权限
	 *
	 * @param map
	 * @return
	 */
	@Override
	public INSBAgentpermission selectByAgentIdAndName(Map<String, String> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectByAgentIdAndName"), map);
	}

	@Override
	public long selectCountByAgentId(String agentId) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectCountByAgentId"), agentId);
	}

	/**
	 * 更新试用用户试用权限使用次数（剩余次数）
	 *
	 * @param agentpermission
	 * @return
	 */
	@Override
	public int updateSurplusnum(INSBAgentpermission agentpermission) {
		return this.sqlSessionTemplate.update(this.getSqlName("updateSurplusnum"), agentpermission);
	}

	/**
	 *
	 *
	 * @param list
	 */
	@Override
	public void insertBatch(List<INSBAgentpermission> list) {
		if (list == null || list.isEmpty()) {
			return;
		}

		int batchCount = 1000;// 每批commit的个数
		int batchLastIndex = batchCount;// 每批最后一个的下标
		for (int index = 0; index < list.size();) {
			if (batchLastIndex > list.size()) {
				batchLastIndex = list.size();
				this.sqlSessionTemplate.insert(this.getSqlName("insertBatch"), list.subList(index, batchLastIndex));
				break;// 数据插入完毕,退出循环

			} else {
				this.sqlSessionTemplate.insert(this.getSqlName("insertBatch"), list.subList(index, batchLastIndex));
				index = batchLastIndex;// 设置下一批下标
				batchLastIndex = index + batchCount;
			}
		}
	}

	@Override
	public int deleteByDeptinnercode(String deptinnercode) {
		if (StringUtil.isEmpty(deptinnercode)) return 0;
		return this.sqlSessionTemplate.delete(this.getSqlName("deleteByDeptinnercode"), deptinnercode);
	}

	@Override
	public void insertAll(List<INSBAgentpermission> list) {
		this.sqlSessionTemplate.insert(this.getSqlName("insertAll"),list);		
	}

}