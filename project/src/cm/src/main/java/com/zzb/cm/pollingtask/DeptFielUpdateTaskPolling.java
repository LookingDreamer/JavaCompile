package com.zzb.cm.pollingtask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cninsure.core.utils.SpringContextHandle;
import com.cninsure.system.dao.INSCDeptDao;
@Service
public class DeptFielUpdateTaskPolling {
	@Resource INSCDeptDao deptDao=SpringContextHandle.getBean("INSCDeptDaoImpl");

	public void updateFiel(){
		List<Map<String,String>> datas = deptDao.selectCodesByParentCodesIsNull();
		for(Map<String,String> map:datas){
			
			//查询父类字段是否存在
			String parentCodeStr = deptDao.selectParentCodesByComcode(map.get("upcomcode"));
			
			//存在更新当前机构
			if(parentCodeStr!=null){
				Map<String,String> paramMap = new HashMap<String,String>();
				paramMap.put("parentcodes", parentCodeStr);
				paramMap.put("comcode", map.get("comcode"));
				deptDao.updateParentCodesByComcode(paramMap);
			}
		}
	}
}
