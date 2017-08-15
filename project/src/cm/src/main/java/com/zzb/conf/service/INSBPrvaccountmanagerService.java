package com.zzb.conf.service;

import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.cninsure.system.entity.INSCUser;
import com.zzb.conf.entity.INSBPrvaccountkey;
import com.zzb.conf.entity.INSBPrvaccountmanager;
 
public interface INSBPrvaccountmanagerService extends BaseService<INSBPrvaccountmanager> {
	/**
	 * 按条件查询key分页
	 * 
	 * @return
	 */
	public Map<String, Object> getKeyDataListPage(Map<String,Object> map);
	 
	/**
	 * 
	 * 查询内容
	 * @param map
	 * @return
	 */
	public Map<String, Object> getDataListPage(Map<String,Object> map);
	
	
	/**
	 * 转到新增/转到编辑
	 * 
	 * @param id
	 * @return
	 */
	public Map<String,Object> main2edit(String id,String deptid);
	
	/**
	 * 
	 * @param managerid
	 * @return
	 */
	public Map<String,Object> mian2keyEdit(String managerid);
	
	/**
	 * 保存/修改
	 * 
	 * @param user
	 * @param model
	 */
	public void saveOrUpdate(INSCUser user,INSBPrvaccountmanager model);
	
	/**
	 * 保存/修改
	 * 
	 * 1：通过选中的主表id 拿到主表数据
	 * 2：判断当前的主表数据是否是当前机构。
	 * 2.1：如果是，不再新建主表数据
	 * 2.2：如果主表数据和当前的机构不相同，新增主表数据，机构为当前机构
	 * 
	 * @param user
	 * @param model
	 */
	public void saveOrUpdateKey(INSCUser user,INSBPrvaccountkey model,String deptId,String manageridmain);
	

}