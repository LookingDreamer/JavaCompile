package com.zzb.conf.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCUser;
import com.zzb.conf.dao.INSBOutorderdeptDao;
import com.zzb.conf.entity.INSBOutorderdept;
import com.zzb.conf.service.INSBOutorderdeptService;

@Service
@Transactional
public class INSBOutorderdeptServiceImpl extends BaseServiceImpl<INSBOutorderdept> implements
		INSBOutorderdeptService {
	@Resource 
	private INSBOutorderdeptDao insbOutorderdeptDao;
	@Resource
	private INSCDeptDao deptDao;

	@Override
	protected BaseDao<INSBOutorderdept> getBaseDao() {
		return insbOutorderdeptDao;
	}

	@Override
	public List<INSBOutorderdept> queryListVo(INSBOutorderdept query) {
		return insbOutorderdeptDao.queryListVo(query);
	}

	@Override
	public void saveDeptIds(INSBOutorderdept outdept, INSCUser operator) {
		String[] scopedept = {outdept.getDeptid1(),outdept.getDeptid2(),outdept.getDeptid3(),outdept.getDeptid4(),outdept.getDeptid5()};
		List<String> oldDeptIds = insbOutorderdeptDao.selectDeptIdsByAgreementId(outdept.getAgreementid());
		
		String deptid = scopedept[4];
		if (deptid != null && !"".equals(deptid)&& !deptid.equals("0")) {
			if(!oldDeptIds.contains(deptid)){
				Date newDate = new Date();
				outdept.setModifytime(newDate);
				outdept.setDeptid5(deptid);
				outdept.setId(null);
				outdept.setCreatetime(newDate);
				outdept.setOperator(operator.getName());
				insbOutorderdeptDao.insert(outdept);	
			}
		}else{
			String deptid2 = null;
			for(int i = scopedept.length-1; i>=0; i--){
		        if(scopedept[i] != null && !"".equals(scopedept[i]) && !scopedept[i].equals("0")){
		        	deptid2=scopedept[i];
		        	break;
		        }
		    }
			if(deptid2 != null && !"".equals(deptid2) && !"0".equals(deptid2)){
				List<String> deptWdids = deptDao.selectByParentDeptCode4groups(deptid2);
				deptWdids.removeAll(oldDeptIds);
				
				if(deptWdids != null && deptWdids.size() > 0){
					for(String id : deptWdids){
						Date newDate = new Date();
						outdept.setModifytime(newDate);
						outdept.setDeptid5(id);
						outdept.setId(UUIDUtils.random());
						outdept.setCreatetime(newDate);
						outdept.setOperator(operator.getName());
						insbOutorderdeptDao.insert(outdept);
						}
					}
				}
			}		
		}

	@Override
	public List<String> getDeptByAgreementId(String agreementId) {
		return insbOutorderdeptDao.selectDeptIdsByAgreementId(agreementId);
	}

	@Override
	public INSBOutorderdept getOutorderdept(String agreementid,String deptid5) {
		INSBOutorderdept insbOutorderdept=new INSBOutorderdept();
		insbOutorderdept.setDeptid5(deptid5);
		insbOutorderdept.setAgreementid(agreementid);
		//insbOutorderdept.setOperator(operator);
		INSBOutorderdept Query_insbOutorderdept=insbOutorderdeptDao.selectOne(insbOutorderdept);
		return Query_insbOutorderdept;
	} 
	
	
}