package com.cninsure.system.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.cninsure.system.entity.INSCDept;

public interface INSCDeptService extends BaseService<INSCDept> {
	public List<Map<Object, Object>> queryDeptList(String parentcode);

	public int addDeptData(INSCDept model);

	public List<Map<String, String>> queryTreeList(String parentcode);
	
	/**
	 * 数据权限过滤
	 * @param parentcode
	 * @return
	 */
	@Deprecated
	public List<Map<String, String>> queryTreeList4Data(String parentcode,String deptId);

	/**
	 * 配置管理-->支付配置-->支付通道管理-->选择机构
	 * @param parentcode
	 * @param deptId
     * @return
     */
	public List<Map<String, String>> queryTreeList4Data2(String parentcode,String deptId);
	
	
	/**
	 * 
	 * 保险公司配置查询机构树过滤
	 * @param parentcode
	 * @param deptId
	 * @return
	 */
	public List<Map<String, String>> queryTreeList4PrvAccount(String parentcode,String deptId);

	public int updateDeptById(String id);

	public int updateDeptByIddel(String id);

	/**
	 * 查找当前节点所有子节点
	 * 
	 * @param parentcode
	 * @return
	 */
	public List<Map<String, String>> queryListByPcode4Group(String parentcode);

	public INSCDept getLegalPersonDept(String deptCode);
	
	public INSCDept getOrgDept(String deptCode);

	public String queryByComCode(String userorganization);

	public List<Map<String, String>> queryTreeListByAgr(String parentcode,String comtype);
	
	public INSCDept getOrgDeptByDeptCode(String deptCode);
	
	public List<String> selectByParentDeptCode4groups(String parentcode);
	
	public List<INSCDept> selectDeptlikeparentcodes(String parentcodes,String detpid);
	public List<INSCDept> selectDeptlikeparentcodes2(String parentcodes,String detpid);
	
	
	public List<String> selectWDidsByPatId(Map<String,String> detpid);
	
	/**
	 * 生成机构树使用  liuchao
	 */
	public List<Map<String,Object>> selectDeptTreeByParentCode(String parentcode);
	public List<Map<String,Object>> selectPartTreeByParentCode(Map<String,String> params);
	
	public List<Map<String, String>> queryTreeListByCity(String upcomcode,
			String city);

	public INSCDept getPlatformDept(String deptid);

	public List<Map<String, String>> getDeptListByCity(String id, String city,
			String platformcode);

	/**
	 * 机构树查询 
	 * @param userorganization 用户归属机构
	 * @param comgrade 查询层级（包含上级）
	 * @param type 机构类型 0-停业，1-营业
	 * @author yangteng
	 */
	public List<Map<String, String>> dept4Tree(String userorganization, String comgrade, String type);

	public List<Map<String, String>> dept4Tree2(String userorganization, String comgrade, String type);
	/**
	 * 查询平台机构
	 * @param deptId
	 * @return
	 */
	public String getPingTaiDeptId(String deptId);

	public List<Map<String, Object>> selectPartTreeByParentCodeCheckAll(Map<String, String> params);

	/**
	 * 根据机构内部编码获取该机构所属平台机构的平台内部编码
	 * @param deptInnercode
	 * @return
	 */
	public String getPlatformInnercode(String deptInnercode);
}