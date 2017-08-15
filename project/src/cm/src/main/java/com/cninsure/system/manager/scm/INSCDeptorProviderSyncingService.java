package com.cninsure.system.manager.scm;

import java.util.Date;
import java.util.Map;

import com.cninsure.system.entity.INSCDept;
import com.zzb.conf.entity.INSBProvider;

public interface INSCDeptorProviderSyncingService {

		/**
		 * @param comcode
		 * @return
		 */
		public INSCDept getDept(String comcode);
		
		/**
		 * @param dept
		 */
		public void saveDept(INSCDept dept);
		
		/**
		 * @param dept
		 */
		public void updateDept(INSCDept dept);
		
		/**
		 * 保存日志信息org
		 * 
		 * @param operator
		 * @param result
		 * @param map
		 * @param syncdate
		 */
		public void saveOrgagentlog(String operator, boolean result, Map<String, Object> map, Date syncdate);
		/**
		 * 保存日志信息agent
		 * 
		 * @param operator
		 * @param result
		 * @param map
		 * @param syncdate
		 */
		public void saveOrgagentlog2(String operator, boolean result, Map<String, Object> map, Date syncdate);
		/**
		 * 保存日志信息provider
		 * 
		 * @param operator
		 * @param result
		 * @param map
		 * @param syncdate
		 */
		public void saveOrgproviderlog(String operator, boolean b, Map<String, Object> map, Date syncdate);
		public Date getMaxSyncdate(Integer value);

		public INSBProvider queryByPrvcode(String prvcode);

		public void updateById(INSBProvider pro);

		public void addProData(INSBProvider pro);

}
