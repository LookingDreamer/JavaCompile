package com.cninsure.system.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.system.entity.INSCDept;

public interface INSCDeptDao extends BaseDao<INSCDept> {
	public List<INSCDept> selectByParentDeptCode(String parentcode);
	
	public List<INSCDept> selectByParentDeptCodeAgr(Map<String, String> parm);
	/**
	 * 新增
	 * @param org
	 * @return
	 */
	public int addDeptDatas(INSCDept org);

	public List<INSCDept> selectAllByDept(String fieldName);

	public int updateDeptByid(String id);

	public int updateDeptByiddel(String id);

	/**
	 * 通过code查找dept
	 * 
	 * @param code
	 * @return
	 */
	public INSCDept selectByComcode(String code);

	public List<INSCDept> selectAllByComcodes(List<String> codes);

	public INSCDept queryByNoti(String noti);

	/**
	 * 通过父节点查询子节点
	 * 
	 * @param parentcode
	 * @return
	 */
	public List<Map<String, String>> selectByParentDeptCode4Group(
			String parentcode);
	
	/**
	 * 通过父code得到所有子节点
	 * 
	 * @param upcomcode
	 * @return
	 */
	public List<String> selectCodeByParentCode(String upcomcode);
	
	/**
	 * 得到当前组织机构所有子节点
	 * @param comcode
	 * @return
	 */
	public List<String> selectAllChildren(String comcode);
	
	
	/**
	 * 根据订单配送表信息找到相应机构名称（手机端订单详情页面）
	 */
	public INSCDept selectBydeptcode(Map<String,Object> deptcode);

	/**
	 * 查询某节点下所有网点id集合
	 * @param id
	 * @return
	 */
	public List<String> queryWDidsByPatId(Map<String,String> map);
	
	/**
	 * 通过comcode查找父节点code
	 * 
	 * @param comcode
	 * @return
	 */
	public INSCDept selectParentCodeByCode(String comcode);
	public INSCDept selectByCode(String comcode);
	
	/**
	 * 查询得到parentCodes为null的 数据
	 * 
	 * @return
	 */
	public List<Map<String,String>> selectCodesByParentCodesIsNull();
	
	/**
	 * 
	 * @param comcode
	 * @return
	 */
	public String selectParentCodesByComcode(String comcode);
	
	
	/**
	 * 更新parentCode字段
	 * @param map
	 */
	public void updateParentCodesByComcode(Map<String,String> map);
	
	/**
	 * 根据上级节点查询下级所有网点的code
	 * @param parentcode
	 * @return
	 */
	public List<String> selectByParentDeptCode4groups(String parentcode);
	
	/**
	 * 通过当前机构得到所有网点级子机构
	 * @param param
	 * @return
	 */
	public List<String> selectComCodeByComtypeAndParentCodes4EDI(String param);
	public List<INSCDept> selectDeptlikeparentcodes(String parentcodes, String deptid);
	public List<INSCDept> selectDeptlikeparentcodes2(String parentcodes, String deptid);
	
	public List<INSCDept> selectAgreedDeptByRegionCode(Map<String,String> map);
	
	/**
	 * 生成机构树使用  liuchao
	 */
	public List<Map<String,Object>> selectDeptTreeByParentCode(String parentcode);
	public List<Map<String,Object>> selectPartTreeByParentCode(Map<String,String> params);
	
	public List<INSCDept> selectWdByCity(Map<String, Object> mapd);

	public List<Map<String, String>> queryTreeListByCity(
			Map<String, String> mapdMap);

	/**
	 * 机构树查询 
	 * @param userorganization 用户归属机构
	 * @param comgrade 查询层级（包含上级）
	 * @param type 机构类型 0-停业，1-营业
	 * @author yangteng
	 */
	public List<Map<String, String>> list4Tree(String userorganization, String comgrade, String type);
	
	public String seleDeptIdByInnerCode(String innerCode);
	@Deprecated
	public List<INSCDept> selectAll();
}