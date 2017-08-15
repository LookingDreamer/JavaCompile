package com.zzb.conf.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cninsure.core.exception.DaoException;
import com.zzb.mobile.entity.ClientType;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.UUIDUtils;
import com.zzb.cm.controller.vo.CertificationVo;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.entity.INSBAgent;
import org.springframework.util.StringUtils;

@Repository
public class INSBAgentDaoImpl extends BaseDaoImpl<INSBAgent> implements
		INSBAgentDao {
	@Override
	public List<INSBAgent> selectListPage(Map<String,Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectPage"), map);
	}

	@Override
	public List<Map<String, Object>> selectListByCondition(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectVo"),map);

	}

	@Override
	public List<Map<String, Object>> selectListByUser(Map<String, Object> map)throws DaoException {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByUser"), map);
		
	}

	@Override
	public List<INSBAgent> selectListByLogicId(Map<String,Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectBylogicId"), map);
	}


	@Override
	public String insertReturnId(INSBAgent agent) {
		agent.setId(UUIDUtils.random());
		this.sqlSessionTemplate.insert(this.getSqlName("insert"), agent);
		return agent.getId();
	}



	@Override
	public long selectCountByMap(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectCount"), map);
	}
	@Override
	public long selectCountByMap_byAgentUser(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectCount_byAgentUser"), map);
	}

	@Override
	public List<INSBAgent> selectBySetId(String setId) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectBySetId"), setId);
	}

	@Override
	public INSBAgent selectAgentByAgentCode(String agentcode) {
		return this.sqlSessionTemplate.selectOne(
				this.getSqlName("selectByAgentCode"), agentcode);
	}

	@Override
	public List<INSBAgent> selectReferrer(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectReferrer"), map);
	}

	@Override
	public void updateSetIdById(INSBAgent agent) {
		this.sqlSessionTemplate.update(this.getSqlName("updateSetIdById"), agent);
	}

	@Override
	public INSBAgent selectByJobnum(String jobnum) {
		return this.sqlSessionTemplate.selectOne(
				this.getSqlName("selectByJobnum"), jobnum);
	}


	@Override
	public List<Map<String, String>> getpermissionsadd(String jobnum) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getpermissionsadd"), jobnum);
	}


	@Override
	public String selectIdByTempJobnum(String tempjobnum) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("seelctIdByTempJobNum"), tempjobnum);
	}


	@Override
	public INSBAgent selectCommNameById(String id) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectCommNameById"), id);
	}


	@Override
	public void updateSetIdIsNull(String setid) {
		this.sqlSessionTemplate.selectOne(this.getSqlName("updateSetIdBySetId"), setid);
	}


	@Override
	public List<Map<String, Object>> selectByCertificationStatus(Integer status) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByCertificationStatus"), status);
	}


	@Override
	public int selectCountByAgentCode(Map<String,String> map) {
		return this.sqlSessionTemplate.selectOne(
				this.getSqlName("selectcountByAgentCode"), map);
	}


	@Override
	public boolean getOnlyAgent(CertificationVo vo) {
		if(null==this.sqlSessionTemplate.selectList(this.getSqlName("getOnlyAgent"), BeanUtils.toMap(vo))){
			return true;
		}
		return false;
	}


	@Override
	public int updatePWDById(INSBAgent agent) {
		return this.sqlSessionTemplate.update(this.getSqlName("updateAgentPWDById"), agent);
	}

	@Override
	public List<INSBAgent> selectListPageByDeptIds(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectPageByDeptIds"), map);
	}
	@Override
	public List<INSBAgent> selectListPageByDeptIds_byAgentUser(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectPageByDeptIds_byAgentUser"), map);
	}


	@Override
	public INSBAgent selectAgent(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectAgentByJobnum"), map);
	}

	@Override
	public int selectCountByUser(Map<String, Object> map)throws DaoException {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectCounterByUser"), map);
	}


	@Override
	public List<Map<String, Object>> selectListByBindingUser(
			Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectListByBindingUser"), map);
	}


	@Override
	public Map<String, Object> selectBindingUserByAccountInfo(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectBindingUserByAccountInfo"), map);
	}



	@Override
	public List<String> selectAgentIdByDeptIds(List<String> list) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectAgentIdByDeptIds"), list);
	}


	@Override
	public void updateAgentLzgidById(Map<String, Object> map) {
		 this.sqlSessionTemplate.update(this.getSqlName("updateAgentLzgidById"), map);
	}
	
	@Override
	public void updateAgentLzgid(Map<String,Object> map){
		 this.sqlSessionTemplate.update(this.getSqlName("updateAgentLzgid"), map);
	}

	@Override
	public int phoneverify(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("phoneverify"), map);
	}


	@Override
	public Map<String, Object> selectBindingUserByJobnumInfo(
			Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectBindingUserByJobnumInfo"), map);
	}


	@Override
	public List<Map<String, String>> selectIdAnadPwd() {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectIdAndPwd"));
	}


	@Override
	public void updatePwdById(Map<String, String> map) {
		this.sqlSessionTemplate.update(this.getSqlName("updatePwdById"), map);
	}


	@Override
	public String getAgentProvince(String agentnum) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectProvinceByJobnum"), agentnum);
	}

	@Override
	public List<INSBAgent> queryAgentBydept(Map<String, String> param) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("queryAgentBydept"), param);
	}



	/**
	 * 查询代理人，去除Mini用户
	 *
	 * @param phone
	 * @return
	 */
	@Override
	public List<INSBAgent> selectNotminiAgent(String phone) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectNotminiAgent"), phone);
	}

	/**
	 * 查询代理人，去除Mini用户
	 *
	 * @param map
	 * @return
	 */
	@Override
	public List<INSBAgent> selectNotminiAgentByMap(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectNotminiAgent"), map);
	}

	/**
	 * 根据部门deptinnercode，查询代理人，去除Mini用户
	 *
	 * @param map
	 * @return
	 */
	@Override
	public List<INSBAgent> selectAgentByDept(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectAgentByDept"), map);
	}

	/**
	 * 代理人成功推荐的人数、被推荐人中的首单人数
	 *
	 * @param map {referrer 代理人工号，firstOderSuccess 是否首单成功}
	 * @return
	 */
	@Override
	public int countRecommend(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("countRecommend"), map);
	}

	/**
	 * 统计出该代理人成功推荐的人、这些人的姓名、所属平台、注册时间、首单时间
	 *
	 * @param map {referrer 代理人工号，firstOderSuccess 是否首单成功}
	 * @return
	 */
	@Override
	public List<Map<String, Object>> recommendPerson(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("recommendPerson"), map);
	}

	@Override
	public int updateByDeptinnercode(Map<String, Object> map) {
		return this.sqlSessionTemplate.update(this.getSqlName("updateByDeptinnercode"), map);
	}

	@Override
	public INSBAgent selectcountByDeptid(String deptid) {
		Map<String,Object> map = new HashMap<>();
		map.put("deptid", deptid);
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectOne"), map);
	}

	/**
	 * 计算代理人推荐成功的代理人中认证成功的代理人数
	 * */
	@Override
	public int getAuthenticatedCount(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("getAuthenticatedCount"), map);
	}

    /**
     * 根据推荐人更新当前代理人的推荐人编号为正式工号
     * @param map
     */
    @Override
    public int updateByReferrer(Map<String, Object> map) {
        return this.sqlSessionTemplate.update(this.getSqlName("updateByReferrer"),map);
    }

	/**
	 * 记录用户最后一次登陆的终端类型
	 * @param clientType 终端类型，记录用户最后一次登陆的终端类型
	 * @return
	 */
	@Override
	public int updateClientType(String id, String clientType) {
		if (StringUtils.isEmpty(id)) {
			throw new NullPointerException("id不能为空");
		}

		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("clienttype", clientType);
		return this.sqlSessionTemplate.update(getSqlName("updateClientType"), map);
	}
}