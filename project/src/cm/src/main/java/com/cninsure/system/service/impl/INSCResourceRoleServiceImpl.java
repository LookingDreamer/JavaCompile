package com.cninsure.system.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.system.entity.INSCMenu;
import com.cninsure.system.entity.INSCRole;
import com.cninsure.system.entity.INSCRoleMenu;
import com.cninsure.system.service.INSCMenuService;
import com.cninsure.system.service.INSCResourceRoleService;
import com.cninsure.system.service.INSCRoleMenuService;
import com.cninsure.system.service.INSCRoleService;
import com.common.ConstUtil;
import com.zzb.conf.controller.vo.MenuVo;

/**
 * @author hlj
 * @date 14:40 2015/6/12
 *
 */
@Service
@Transactional
public class INSCResourceRoleServiceImpl implements INSCResourceRoleService {
	@Autowired
	public INSCMenuService inscMenuServiceImpl;
	@Autowired
	public INSCRoleService inscRoleServiceImpl;
	@Autowired
	public INSCRoleMenuService inscRoleMenuServiceImpl;

	/*
	 * 获得资源和角色的对应关系信息
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cninsure.system.service.INSCResourceRoleService#getResourceRolesMap()
	 */
	@Override
	public Map<String, List<String>> getResourceRolesMap() {
		Map<String, List<String>> map = new HashMap<String, List<String>>(); 
		String[] arr=null;
		List<String> sList=null;
		int offset=ConstUtil.OFFSET+1;
		int limit=ConstUtil.LIMIT;
		while(true){
			List<MenuVo> list = inscMenuServiceImpl.selectListMap((offset-1)*limit, limit);
			for(MenuVo menuVo : list){
				sList = new ArrayList<String>();
				if(menuVo.getRoleCode()!=null&&!menuVo.getRoleCode().equals("")){
					arr=menuVo.getRoleCode().split(",");
					sList= Arrays.asList(arr);
				}
				map.put(menuVo.getActiveUrl(), sList);
			}
			if(list.size()!=limit){
				break;
			}
			offset++;
			//System.out.println(offset);
		}
		return map;
	}
}
