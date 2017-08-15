package com.cninsure.system.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.system.entity.INSCMenu;
import com.zzb.conf.controller.vo.MenuVo;


public interface INSCMenuDao extends BaseDao<INSCMenu> {

	public List<INSCMenu> selectMenuByParentNodeCode(String parentnodecode);
	@Deprecated
	public List<INSCMenu> selectAll();
	
	public List<INSCMenu> selectAllByOrder(String fieldName);

	public INSCMenu selectByNodeCode(String nodecode);
	
	/**
	 * 初始化菜单
	 * @param id
	 * @return
	 */
	public List<String> selectCodeByIds4Menu(List<String> ids);
	/**
	 * 获得资源和角色的对应关系信息
	 * @param map
	 * @return
	 */
	public List<MenuVo> selectListMap(Map<String, Object> map);
	
}
