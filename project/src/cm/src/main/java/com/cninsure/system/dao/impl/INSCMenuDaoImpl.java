package com.cninsure.system.dao.impl;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.system.dao.INSCMenuDao;
import com.cninsure.system.entity.INSCMenu;
import com.zzb.conf.controller.vo.MenuVo;


@Repository
public class INSCMenuDaoImpl extends BaseDaoImpl<INSCMenu> implements INSCMenuDao {

	@Override
	public List<INSCMenu> selectMenuByParentNodeCode(String parentnodecode) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByParentNodeCode"),parentnodecode);
	}
	
	@Override
	public List<INSCMenu> selectAll() {
		return this.sqlSessionTemplate.selectList(this.getSqlName("select"));
	}

	@Override
	public List<MenuVo> selectListMap(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectListMap"), map);
	}
	
	@Override
	public List<INSCMenu> selectAllByOrder(String fieldName) {
		return this.sqlSessionTemplate.selectList("com.cninsure.system.entity.INSCMenu_selectByOrder",fieldName);
	}
	
	@Override
	public INSCMenu selectByNodeCode(String nodecode) {
		return (INSCMenu) this.sqlSessionTemplate.selectList("com.cninsure.system.entity.INSCMenu_selectByNodeCode",nodecode);
	}

	@Override
	public List<String> selectCodeByIds4Menu(List<String> ids) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectCodeByIds4Menu"), ids);
	}

}
