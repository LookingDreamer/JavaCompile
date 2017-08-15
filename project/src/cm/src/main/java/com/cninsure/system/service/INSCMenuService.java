package com.cninsure.system.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.cninsure.system.entity.INSCMenu;
import com.zzb.conf.controller.vo.MenuVo;

public interface INSCMenuService extends BaseService<INSCMenu> {
	
//	public List<Map<Object, Object>> queryMenusByUserCode(String usercode, String parentnodecode);
	public String queryMenusFtl(String usercode, String parentnodecode);

	public List<Map<Object, Object>> queryMenusList(String id,String parentinnercode);

	INSCMenu queryByNodeCode(String nodecode);
	
	/**
	 * 通过业管得到任务管理菜单 信息
	 * @param userCode
	 * @return
	 */
	public Map<String,String> getTaskManageDataByUserCode(String userCode);
	
	/**
	 * 业管登录检索任务
	 * 
	 * 1：得到当前登陆人所属的群组id
	 * 2：检索工作量表 查看当前还未分配人的任务
	 * 3：调用认领任务接口 
	 * 4：修改数据库信息  （加事务 加锁）
	 * ）
	 * @param userCode
	 */
	public void loginUserDispatchWork(String userCode);
	@Deprecated
	public List<INSCMenu> queryAll();
	
	/**
	 * 获得资源和角色的对应关系信息
	 * @param map
	 * @return
	 */
	public List<MenuVo> selectListMap(int offset,int limit);
}
