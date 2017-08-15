package com.zzb.conf.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCUser;
import com.zzb.conf.dao.INSBAgreementscopeDao;
import com.zzb.conf.entity.INSBAgreementscope;
import com.zzb.conf.entity.INSBOutorderdept;
import com.zzb.conf.service.INSBAgreementscopeService;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.InsbSaveScopeParam;

@Service
@Transactional
public class INSBAgreementscopeServiceImpl extends BaseServiceImpl<INSBAgreementscope> implements
		INSBAgreementscopeService {
	@Resource
	private INSBAgreementscopeDao insbAgreementscopeDao;
	@Resource
	private INSCDeptDao deptDao;

	@Override
	protected BaseDao<INSBAgreementscope> getBaseDao() {
		return insbAgreementscopeDao;
	}

	@Override
	public List<INSBAgreementscope> selectAdrAndDeptName(
			INSBAgreementscope query) {
		return insbAgreementscopeDao.selectAdrAndDeptName(query);
	}


	@Override
	public List<String> selectAreaDeptid(String city) {		
		return insbAgreementscopeDao.selectAreaDeptid(city);
	}

	@Override
	public void saveAgreementScop(INSCUser operator, INSBAgreementscope scope,INSBOutorderdept conndept) {
		String[] scopedept = {conndept.getDeptid1(),conndept.getDeptid2(),conndept.getDeptid3(),conndept.getDeptid4(),conndept.getDeptid5()};
		List<String> oldDeptId =  insbAgreementscopeDao.selectDeptIdByAgreementId(scope.getAgreementid());

		String deptid = scopedept[4];
		if (deptid != null && !"".equals(deptid)&& !deptid.equals("0")) {
			if(!oldDeptId.contains(deptid)){
				INSCDept inscDept=deptDao.selectByComcode(deptid);
				Date newDate = new Date();
				scope.setModifytime(newDate);
				scope.setScopetype("2");
				scope.setDeptid(deptid);
				scope.setProvince(inscDept.getProvince());
				scope.setCity(inscDept.getCity());
				scope.setId(UUIDUtils.random());
				scope.setCreatetime(newDate);
				scope.setOperator(operator.getName());
				this.insbAgreementscopeDao.insert(scope);	
			}
		}else{
			String deptid2 = null;
			for(int i = scopedept.length-1; i >= 0; i--){
		        if(scopedept[i] != null && !"".equals(scopedept[i]) && !scopedept[i].equals("0")){
		        	deptid2=scopedept[i];
		        	break;
		        }
		    }
			if(deptid2 != null && !"".equals(deptid2) && !"0".equals(deptid2)){
				List<String> deptWdids = deptDao.selectByParentDeptCode4groups(deptid2);
				deptWdids.removeAll(oldDeptId);
				if(deptWdids != null && deptWdids.size() > 0){
					List<INSBAgreementscope> scopModelList = new ArrayList<INSBAgreementscope>();
					for(String id : deptWdids){
						INSCDept inscDep=deptDao.selectByComcode(id);
						Date newDate = new Date();
						scope.setModifytime(newDate);
						scope.setScopetype("2");
						scope.setDeptid(id);
						scope.setProvince(inscDep.getProvince());
						scope.setCity(inscDep.getCity());
						scope.setId(UUIDUtils.random());
						scope.setCreatetime(newDate);
						scope.setOperator(operator.getName());
						scopModelList.add(scope);
						insbAgreementscopeDao.insert(scope);
					}
				}
			}		
		} 
	}

	@Override
	public CommonModel saveAgreementScopByCity(INSCUser operator,
			InsbSaveScopeParam model) {
		CommonModel modeld=new CommonModel();
		try {
//			for (String cityid : model.getCitys()) {
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("operator", operator.getName());
				map.put("scopetype", "2");
				map.put("agreementid", model.getAgreementid());
				map.put("cityids",model.getCitys());
				
//				map.put("id",UUIDUtils.random());
				map.put("createtime",new Date());
				map.put("modifytime",new Date());
				//对网点批量关联
				insbAgreementscopeDao.insertscopes(map);
//				List<INSBAgreementscope>list=insbAgreementscopeDao.insertscope(map);
//				insbAgreementscopeDao.insertInBatch(list);
//			}
			modeld.setStatus("success");
			modeld.setMessage("一键关联成功");
			return modeld;
		} catch (Exception e) {
			e.printStackTrace();
			modeld.setStatus("fail");
			modeld.setMessage("一键关联失败，请重新操作！");
			return modeld;
		}
	} 

}