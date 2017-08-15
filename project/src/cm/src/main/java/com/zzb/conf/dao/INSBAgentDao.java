package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.controller.vo.CertificationVo;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.mobile.entity.ClientType;

public interface INSBAgentDao extends BaseDao<INSBAgent> {
	public List<INSBAgent> selectListPage(Map<String,Object> map);
	
	/**
	 * 通过逻辑主键查询代理人信息
	 *  所属机构、用户类型、测试账户
	 * 
	 * @param map
	 * @return
	 */
	public List<INSBAgent> selectListByLogicId(Map<String,Object> map);

	/**
	 * 新增代理人 返回id
	 * @return
	 */
	public String insertReturnId(INSBAgent agent);
	
	
	/**
	 * 根据参数得到所有数据条数
	 * 
	 * @param map
	 * @return
	 */
	public long selectCountByMap(Map<String, Object> map);
	public long selectCountByMap_byAgentUser(Map<String, Object> map);

	
	/**
	 * 通过权限包id 查询所有代理人
	 * 
	 * @param setId
	 * @return
	 */
	public List<INSBAgent> selectBySetId(String setId);
	
	
	/**
	 * 根据代理人代码获取代理人信息（INSBAgent对象）
	 * 
	 * @param agentcode
	 * @return
	 */
	public INSBAgent selectAgentByAgentCode(String agentcode);

	List<INSBAgent> selectReferrer(Map<String, Object> map);
	
	/**
	 * 通过传入map来查询代理人的信息
	 * param agent
	 * @return
	 */
	public List<Map<String, Object>> selectListByCondition(Map<String, Object> map);
	
	/**
	 * 根据主键更新功能包id
	 */
	public void updateSetIdById(INSBAgent agent);


	/**
	 * 根据工号查询代理人信息
	 */
	public INSBAgent selectByJobnum(String jobnum);

	/**
	 * 根据临时工号得到当前代理人id
	 * 
	 * @param tempJobnum
	 * @return
	 */
	public String selectIdByTempJobnum(String tempJobnum);
	
	/**
	 * 根据代理人的工号获得权限名和相应的code地址
	 * @param jobnum
	 * @return
	 */
	public List<Map<String, String>> getpermissionsadd(String jobnum);
	
	/**
	 * 得到组织机构信息
	 * 
	 * @param id
	 * @return
	 */
	public INSBAgent selectCommNameById(String id);
	
	
	/**
	 * 代理人功能包id值为空
	 * 
	 * @param setid
	 */
	public void updateSetIdIsNull(String setid);
	/**
	 * 根据认证状态查询代理人
	 * 
	 * @param status
	 * @return
	 */
	public List<Map<String,Object>> selectByCertificationStatus(Integer status);
	
	
	/**
	 *判断当前id下 是否存在相同的code 
	 * param agentcode
	 * @return
	 */
	public int selectCountByAgentCode(Map<String,String> map);


	/**
	 * 判断正式工号是否唯一
	 */
	public boolean getOnlyAgent(CertificationVo vo);
    
	/**
	 * 重置密码
	 * @param tempAgent
	 */
	public int updatePWDById(INSBAgent tempAgent);


	public List<INSBAgent> selectListPageByDeptIds(Map<String, Object> map);
	public List<INSBAgent> selectListPageByDeptIds_byAgentUser(Map<String, Object> map);

	/**
	 * 获得代理人
	 * param jobnum
	 * param deptid
	 * @return
	 */
	public INSBAgent selectAgent(Map<String, Object> map);

	/**
	 * 获得代理人
	 * param jobnum
	 * param deptid
	 * @return
	 */
	List<Map<String, Object>> selectListByUser(Map<String, Object> map);


	/**
	 * 获得代理人数量
	 * param jobnum
	 * param deptid
	 * @return
	 */
	public int selectCountByUser(Map<String, Object> map);
	
	/**
	 * 获得用于绑定的代理人
	 * param jobnum
	 * param deptid
	 * @return
	 */
	public List<Map<String, Object>> selectListByBindingUser(Map<String, Object> map);
	
	/**
	 * 获得用绑定的代理人
	 * param jobnum
	 * param deptid
	 * @return
	 */
	public Map<String, Object> selectBindingUserByAccountInfo(Map<String, Object> map);
	
	/**
	 * 获得用绑定的代理人
	 * param jobnum
	 * param deptid
	 * @return
	 */
	public Map<String, Object> selectBindingUserByJobnumInfo(Map<String, Object> map);
	
	/**
	 * 通过组织机构批量获取代理人
	 * 
	 * @param list
	 * @return
	 */
	public List<String> selectAgentIdByDeptIds(List<String>  list);
	
	/**
	 * 通过代理人ID更新lzgid
	 * @param map
	 */
	public void updateAgentLzgidById(Map<String,Object> map);
	
	public void updateAgentLzgid(Map<String,Object> map);

	/**
	 * 判断代理人手机号唯一性
	 * param phone
	 * @return
	 */
	public int phoneverify(Map<String, Object> map);
	
	/**
	 * @return
	 */
	public List<Map<String, String>> selectIdAnadPwd() ;
	
	/**
	 * @param map
	 */
	public void updatePwdById(Map<String, String> map);

	/**
	 * 获取代理人所在地区
	 * @param agentnum
	 */
	public String getAgentProvince(String agentnum);
	public List<INSBAgent> queryAgentBydept(Map<String, String> param);



	/**
	 * 查询代理人，去除Mini用户
	 * @param phone
	 * @return
     */
	public List<INSBAgent> selectNotminiAgent(String phone);

	/**
	 * 查询代理人，去除Mini用户
	 * @param map
	 * @return
	 */
	public List<INSBAgent> selectNotminiAgentByMap(Map<String, Object> map);

	/**
	 * 根据部门deptinnercode，查询代理人，去除Mini用户
	 * @param map
	 * @return
	 */
	public List<INSBAgent> selectAgentByDept(Map<String, Object> map);

	/**
	 * 代理人成功推荐的人数、被推荐人中的首单人数
	 * @param map {referrer 代理人工号，firstOderSuccess 是否首单成功}
	 * @return
     */
	int countRecommend(Map<String, Object> map);

	/**
	 * 统计出该代理人成功推荐的人、这些人的姓名、所属平台、注册时间、首单时间
	 * @param map {referrer 代理人工号，firstOderSuccess 是否首单成功}
	 * @return
     */
	List<Map<String,Object>> recommendPerson(Map<String, Object> map);

	public int updateByDeptinnercode(Map<String, Object> map);

	public INSBAgent selectcountByDeptid(String deptid);

	/**
	 * 计算代理人推荐成功的代理人中认证成功的代理人数
	 * */
	int getAuthenticatedCount(Map<String,Object> map);

    /**
     * 根据推荐人更新当前代理人的推荐人编号为正式工号
     * @param map
     */
    int updateByReferrer(Map<String,Object> map);

    int updateClientType(String id, String clientType);
}