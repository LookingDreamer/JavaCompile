package com.cninsure.system.dao.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCDept;

@Repository
public class INSCDeptDaoImpl extends BaseDaoImpl<INSCDept> implements
		INSCDeptDao {
	@Override
	public List<INSCDept> selectByParentDeptCode(String parentcode) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByParentDeptCode"),parentcode);
	}
	@Override
	public List<INSCDept> selectByParentDeptCodeAgr(Map<String, String> parm) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByParentDeptCodeAgr"),parm);
	}
	@Override
	public List<Map<String, String>> selectByParentDeptCode4Group(String parentcode) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByParentDeptCode4group"),parentcode);
	}

	@Override
	public List<String> selectByParentDeptCode4groups(String parentcode) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByParentDeptCode4groups"),parentcode);
	}
	
	@Override
	public int addDeptDatas(INSCDept org) {
		return this.sqlSessionTemplate.insert(this.getSqlName("insert"),org);
	}

	@Override
	public List<INSCDept> selectAllByDept(String fieldName) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByDept"),fieldName);
	}

	@Override
	public int updateDeptByid(String id) {
		return this.sqlSessionTemplate.update(this.getSqlName("updateDeptByid"),id);
	}

	@Override
	public int updateDeptByiddel(String id) {
		return this.sqlSessionTemplate.update(this.getSqlName("updateDeptByiddel"),id);
	}

	@Override
	public INSCDept selectByComcode(String code) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectByDeptCode"), code);
	}

	public List<INSCDept> selectAllByComcodes(List<String> codes) {
		if (codes == null || codes.isEmpty()) return Collections.emptyList();
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectAllByDeptCodes"), codes);
	}

	@Override
	public INSCDept queryByNoti(String noti) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("queryPid"),noti);
	}
	@Override
	public INSCDept selectBydeptcode(Map<String, Object> deptcode) {

		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectCom"), deptcode);
	}
	@Override
	public List<String> selectCodeByParentCode(String upcomcode) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectCodeByParentCode"), upcomcode);
	}
	@Override
	public List<String> selectAllChildren(String comcode) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectAllChildrenBycode"), comcode);
	}
	/**
	 * 查询某节点下所有网点id集合
	 * @param map
	 * @return
	 */
	@Override
	public List<String> queryWDidsByPatId(Map<String,String> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectWDidsByPatId"), map);
	}
	@Override
	public INSCDept selectParentCodeByCode(String comcode) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectParentCodeByCode"), comcode);
	}
	@Override
	public List<Map<String,String>> selectCodesByParentCodesIsNull() {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectCodesByParentCodesIsNull"));
	}
	@Override
	public String selectParentCodesByComcode(String comcode) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectParentCodesByComcode"), comcode);
	}
	@Override
	public void updateParentCodesByComcode(Map<String, String> map) {
		this.sqlSessionTemplate.update(this.getSqlName("updateParentCodesByComcode"), map);
	}
	@Override
	public List<String> selectComCodeByComtypeAndParentCodes4EDI(
			String comcode) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectComCodeByComtypeAndParentCodes4EDI"), comcode);
	}
	@Override
	public List<INSCDept> selectDeptlikeparentcodes(String parentcodes,String deptid) {
		Map<String, String> map  = new HashMap<String, String>();
		map.put("parentcodes", parentcodes);
		map.put("deptid", deptid);
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectDeptlikeparentcodes"), map);
	}
	@Override
	public List<INSCDept> selectDeptlikeparentcodes2(String parentcodes, String deptid) {
		Map<String, String> map  = new HashMap<String, String>();
		map.put("parentcodes", parentcodes);
		map.put("deptid", deptid);
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectDeptlikeparentcodes2"), map);
	}
	@Override
	public List<INSCDept> selectAgreedDeptByRegionCode(Map<String, String> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectAgreedDeptByRegionCode"), map);
	}
	
	/**
	 * 生成机构树使用  liuchao
	 */
	@Override
	public List<Map<String, Object>> selectDeptTreeByParentCode(
			String parentcode) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectDeptTreeByParentCode"), parentcode);
	}
	
	@Override
	public List<Map<String, Object>> selectPartTreeByParentCode(
			Map<String,String> params) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectPartTreeByParentCode"), params);
	}
	
	@Override
	public List<INSCDept> selectWdByCity(Map<String, Object> mapd) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectWdByCity"), mapd);
	}
	@Override
	public List<Map<String, String>> queryTreeListByCity(
			Map<String, String> mapdMap) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectWdByCityAndPlatform"), mapdMap);
	}
	
	@Override
	public List<Map<String, String>> list4Tree(String userorganization, String comgrade, String type){
		Map<String, String> map  = new HashMap<String, String>();
		map.put("userorganization", userorganization);
		map.put("comgrade", comgrade);
		map.put("comtype", type);
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectList4Tree"), map);
	}
	@Override
	public String seleDeptIdByInnerCode(String innerCode) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("seleDeptIdByInnerCode"), innerCode);
	}
	/**
	 * 通过depcode查询
	 */
	@Override
	public INSCDept selectByCode(String comcode) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectByCode"), comcode);
	}
	@Override
	public List<INSCDept> selectAll() {
		return this.sqlSessionTemplate.selectList(this.getSqlName("select"));
	}
}