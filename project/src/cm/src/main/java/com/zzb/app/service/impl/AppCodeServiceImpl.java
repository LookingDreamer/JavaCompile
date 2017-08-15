package com.zzb.app.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.*;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.entity.INSCCode;
import com.zzb.app.service.AppCodeService;
import com.zzb.model.AppCodeModel;

@Service
@Transactional
public class AppCodeServiceImpl extends BaseServiceImpl<INSCCode>  implements AppCodeService{

	@Resource
	private INSCCodeDao codeDao;
	
	@Override
	protected BaseDao<INSCCode> getBaseDao() {
		return codeDao;
	}

	@Override
	public List<AppCodeModel> queryByCodetype(String types) {
		if(types!=null&&!"".equals(types)){
			List<String> param = new ArrayList<String>();
			String[] typeArray = null;
			try {
				typeArray = types.split(",");
			} catch (Exception e) {
				typeArray[0] = types;
				e.printStackTrace();
			}
			
			for(String str:typeArray){
				param.add(str);
			}
			
			List<AppCodeModel> result = codeDao.selectByTypes(param);
			//bug 9284 前端证件类型去掉“其它证件”
			if(result!=null && result.size()>0){
				for(Iterator it = result.iterator();it.hasNext();){
					AppCodeModel appCodeModel = (AppCodeModel)it.next();
					if("其他证件".equalsIgnoreCase(appCodeModel.getCodeName()) && "CertKinds".equalsIgnoreCase(appCodeModel.getCodeType())) {
						it.remove();
					}
				}
			}
			return result;
		}else{
			return null;
		}
		
		
	}
	
	

}
