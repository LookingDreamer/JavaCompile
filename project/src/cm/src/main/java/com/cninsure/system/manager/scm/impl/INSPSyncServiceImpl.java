package com.cninsure.system.manager.scm.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.common.redis.Constants;
import com.common.redis.IRedisClient;
import org.springframework.stereotype.Service;

import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.manager.scm.INSCDeptorProviderSyncingService;
import com.cninsure.system.manager.scm.INSPSyncService;
import com.cninsure.system.manager.scr.INSCFdcomManager;
import com.zzb.conf.entity.INSBProvider;
@Service
public class INSPSyncServiceImpl implements INSPSyncService{
	@Resource
	INSCFdcomManager inscDeptManager;
	@Resource
	INSCDeptorProviderSyncingService inscDeptsyncingService;
	@Resource
	private IRedisClient redisClient;
	private static final String PROVIDER_SYNC_COUNT = "PROVIDER_SYNC_COUNT";
	private static final String PROVIDER_SYNC_PROCESS = "PROVIDER_SYNC_PROCESS";
	private static final int EXPIRED_TIME =60*60;
	
		//同步状态
//		private static long syncCount = -1L;
//		private static long syncProcess = -1L;

		public long getSyncCount() {
			if (redisClient.get(Constants.CM_SYNC, PROVIDER_SYNC_COUNT) == null) {
				return -1;
			}
			return Long.valueOf((String)redisClient.get(Constants.CM_SYNC, PROVIDER_SYNC_COUNT));
		}

		public long getSyncProcess() {
			if (redisClient.get(Constants.CM_SYNC, PROVIDER_SYNC_PROCESS) == null) {
				return -1;
			}
			return Long.valueOf((String)redisClient.get(Constants.CM_SYNC, PROVIDER_SYNC_PROCESS));
		}
		
		@Override
		public Map<String, Object> getSyncProviderData(String operator) {
			Date syncdate = new Date();
			Map<String, Object> map = new HashMap<String, Object>();
			redisClient.set(Constants.CM_SYNC, PROVIDER_SYNC_COUNT, 0, EXPIRED_TIME);
			try { 
				Date maxSyncdate = inscDeptsyncingService.getMaxSyncdate(3);//供应商同步
				String maxSyncdateStr = null;
				if (maxSyncdate != null) {
					maxSyncdateStr = DateUtil.toDateTimeString(maxSyncdate);
				}
				List<Map<String, Object>> list = inscDeptManager.getProviderData(maxSyncdateStr);
				if (list != null && list.size() > 0) {
					redisClient.set(Constants.CM_SYNC, PROVIDER_SYNC_COUNT, list.size(), EXPIRED_TIME);
					getSyncDataResultOfCm(list,operator);
					map.put("success", true);
					map.put("returnMsg", "成功同步了" + list.size() + "条供应商机构数据！");
				} else {
					map.put("success", false);
					map.put("returnMsg", "没有需要同步的供应商机构数据！");
				}
				inscDeptsyncingService.saveOrgproviderlog(operator, true, map, syncdate);
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.error(e);
				map.put("success", false);
				map.put("returnMsg", "供应商机构数据同步失败，数据同步时发生了异常.");
				inscDeptsyncingService.saveOrgproviderlog(operator, false, map, syncdate);
			} finally {
				redisClient.expire(Constants.CM_SYNC, PROVIDER_SYNC_COUNT, 0);
				redisClient.expire(Constants.CM_SYNC, PROVIDER_SYNC_PROCESS, 0);
			}
			return map;
		}
		/**
		 * 将供应商信息同步到表INSBProvider中
		 * 
		 * @param list
		 * @return
		 * @throws Exception
		 */
		private void getSyncDataResultOfCm(List<Map<String, Object>> list,String operator) throws Exception {
			INSBProvider pro;
			for (int i = 0; i < list.size(); i++) {
				redisClient.set(Constants.CM_SYNC, PROVIDER_SYNC_PROCESS, i + 1, EXPIRED_TIME);
				pro = new INSBProvider();
				getProvider(list.get(i),pro);
				if (StringUtil.isNotEmpty(pro.getPrvcode())) {
					INSBProvider p = inscDeptsyncingService.queryByPrvcode(pro.getPrvcode());
					if (p != null) {
						pro.setId(p.getId());
						inscDeptsyncingService.updateById(pro);
					} else {
						pro.setId(pro.getPrvcode());
						pro.setPrvtype("02");
						pro.setModifytime(new Date());
						pro.setOperator(operator);
						inscDeptsyncingService.addProData(pro);
					}
				}
			}
		}
		private INSBProvider getProvider(Map<String, Object> insbProvider,INSBProvider pro) {
			pro.setPrvcode(insbProvider.get("SUPPLIERCODE").toString());
			pro.setParentcode(null==insbProvider.get("MASTERINSCOM")?"":insbProvider.get("MASTERINSCOM").toString());
			pro.setProvince(null==insbProvider.get("PROVINCECODE")?"":insbProvider.get("PROVINCECODE").toString());
			pro.setCity(null==insbProvider.get("CITYCODE")?"":insbProvider.get("CITYCODE").toString());
			pro.setPrvshotname(null==insbProvider.get("SHORTNAME")?"":insbProvider.get("SHORTNAME").toString());
			pro.setPrvname(null==insbProvider.get("SUPPLIERNAME")?"":insbProvider.get("SUPPLIERNAME").toString());
			pro.setPrvtype("02");//默认02合作商
			pro.setBusinesstype("01");//默认传统
//			pro.setChanneltype("01");//默认01
//			pro.setPrvtype(null==insbProvider.get("INSTYPE")?"":insbProvider.get("INSTYPE").toString());
//			pro.setBusinesstype();
			pro.setPrvgrade(null==insbProvider.get("INSCLASS")?"":insbProvider.get("INSCLASS").toString());
			pro.setChildflag(null==insbProvider.get("MASTERINSCOM")?"1":"");
			//System.out.println(insbProvider.get("MAKEDATE").toString().substring(0, 11)+insbProvider.get("MAKETIME"));
			try{
				pro.setCreatetime(DateUtil.parse(insbProvider.get("MAKEDATE").toString().substring(0, 11)+insbProvider.get("MAKETIME"), "YYYY-MM-DD HH:mm:ss"));
				pro.setModifytime(DateUtil.parse(insbProvider.get("MODIFYDATE").toString().substring(0, 11)+insbProvider.get("MODIFYTIME"), "YYYY-MM-DD HH:mm:ss"));
			}catch(Exception e){
				e.printStackTrace();
				LogUtil.debug("同步供应商，创建或者修改时间转换错误");
			}
				   /*MASTERINSCOM=1045, 
					 STOPFLG=N, 
					 SUPPLIERCODE=104511, 
					 PEOPLES=0, 
					 MAKEDATE=2008-10-16 00:00:00.0, 
					 MODIFYDATE=2010-07-30 00:00:00.0, 
					 STATUS=1, 
					 SUPPLIERNAME=中荷人寿保险有限公司北京分公司, 
					 INSSUPERCODE=1045, 
					 PROVINCECODE=110000, 
					 INSCLASS=02, 
					 NETPROFITRATE=0, 
					 SHORTNAME=北分中荷, 
					 MAKETIME=09:47:25, 
					 SYNCHROFLAG=Y, 
					 CITYCODE=110100, 
					 ASSET=0, 
					 MODIFYTIME=17:13:43, 
					 AUTORENEWFLAG=1, 
					 INSTYPE=01, 01 寿险 02 财险 
					 PASSWORD=08E4A39F659E95F8, 
					 OPERATOR=001, 
					 RGTMONEY=0, 
					 CUSTOMERNO=291045110000, 
					 SUPPLIERSHOWCODE=104511*/
			return pro;
		}
}
