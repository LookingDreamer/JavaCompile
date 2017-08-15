package com.zzb.conf.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.zzb.conf.controller.vo.InterfaceVo;
import com.zzb.conf.dao.INSBAgreementinterfaceDao;
import com.zzb.conf.dao.INSBChnchargeruleDao;
import com.zzb.conf.entity.INSBAgreementinterface;
import com.zzb.conf.entity.INSBChnchargerule;
import com.zzb.conf.service.INSBAgreementinterfaceService;

@Service
@Transactional
public class INSBAgreementinterfaceServiceImpl extends BaseServiceImpl<INSBAgreementinterface> implements
		INSBAgreementinterfaceService {
	@Resource
	private INSBAgreementinterfaceDao insbAgreementinterfaceDao;
	@Resource
	private INSBChnchargeruleDao insbChnchargeruleDao;

	@Override
	protected BaseDao<INSBAgreementinterface> getBaseDao() {
		return insbAgreementinterfaceDao;
	}

	@Override
	public InterfaceVo saveInterfaces(String agreementid, InterfaceVo interfaceVo) {
		InterfaceVo result = new InterfaceVo();
		if ("1".equals(interfaceVo.getCheck())) { //开启
			if (StringUtil.isEmpty(interfaceVo.getAgreementinterfaceid())){
				result = addInterfaceInfo(agreementid, interfaceVo);
			} else {
				result = updateInterfaceInfo(interfaceVo);
			}
		} else {
			if (StringUtil.isNotEmpty(interfaceVo.getAgreementinterfaceid())) {
				insbAgreementinterfaceDao.deleteById(interfaceVo.getAgreementinterfaceid());
				INSBChnchargerule insbChnchargerule = new INSBChnchargerule();
				insbChnchargerule.setAgreementinterfaceid(interfaceVo.getAgreementinterfaceid());
				insbChnchargeruleDao.delete(insbChnchargerule);
				result.setAgreementinterfaceid("");
			}
		}

		return result;
	}
	public void bashSaveInterface(List<InterfaceVo> inters) {
		
		//List<InterfaceVo> interfaceVos = new ArrayList<>();
		
		
		for(InterfaceVo interfaceVo : inters) {
			InterfaceVo inVo = new InterfaceVo();
			
			if("1".equals(interfaceVo.getCheck())) {//开启
				
				inVo = this.createInterface(interfaceVo);
				//interfaceVos.add(inVo);
				
			}else {
				
				if (StringUtil.isNotEmpty(interfaceVo.getAgreementinterfaceid())) {
					insbAgreementinterfaceDao.deleteById(interfaceVo.getAgreementinterfaceid());
					INSBChnchargerule insbChnchargerule = new INSBChnchargerule();
					insbChnchargerule.setAgreementinterfaceid(interfaceVo.getAgreementinterfaceid());
					insbChnchargeruleDao.delete(insbChnchargerule);
					inVo.setAgreementinterfaceid("");
					//interfaceVos.add(inVo);//????
					
				}
			}
			
			
		}
		
		//return "success";
	}
	private InterfaceVo updateInterfaceInfo(InterfaceVo interfaceVo) {
		INSBAgreementinterface tempInterface = insbAgreementinterfaceDao.selectById(interfaceVo.getAgreementinterfaceid());
		tempInterface.setIsfree(interfaceVo.getIsfree());
		tempInterface.setPv1(interfaceVo.getPv1());
		tempInterface.setPv2(interfaceVo.getPv2());
		tempInterface.setPv3(interfaceVo.getPv3());
		tempInterface.setPv4(interfaceVo.getPv4());
		tempInterface.setModifytime(new Date());
		String monthfree = interfaceVo.getMonthfree();
		if (StringUtil.isNotEmpty(monthfree)) {
			tempInterface.setMonthfree(Integer.valueOf(monthfree));
		}
//		tempInterface.setPerfee(Double.valueOf(interfaceVo.getPerfee()));
		tempInterface.setExtendspattern(interfaceVo.getExtendspattern());
		insbAgreementinterfaceDao.updateById(tempInterface);

		InterfaceVo result = new InterfaceVo();
		result.setAgreementinterfaceid(tempInterface.getId());
		return result;
	}

	private InterfaceVo addInterfaceInfo(String agreementid, InterfaceVo interfaceVo) {
		INSBAgreementinterface tempInterface = new INSBAgreementinterface();
		tempInterface.setId(UUIDUtils.create()) ;
		tempInterface.setAgreementid(agreementid);
		tempInterface.setInterfaceid(interfaceVo.getInterfaceid());
		tempInterface.setIsfree(interfaceVo.getIsfree());
		tempInterface.setChannelinnercode(interfaceVo.getChannelinnercode());
		tempInterface.setExtendspattern(interfaceVo.getExtendspattern());
		tempInterface.setCreatetime(new Date());
		tempInterface.setPv1(interfaceVo.getPv1());
		tempInterface.setPv2(interfaceVo.getPv2());
		tempInterface.setPv3(interfaceVo.getPv3());
		tempInterface.setPv4(interfaceVo.getPv4());
		String monthfree = interfaceVo.getMonthfree();
		if (StringUtil.isNotEmpty(monthfree)) {
			tempInterface.setMonthfree(Integer.valueOf(monthfree));
		}
		//tempInterface.setPerfee(Double.valueOf(interfaceVo.getPerfee()));
		insbAgreementinterfaceDao.insert(tempInterface);

		InterfaceVo result = new InterfaceVo();
		result.setAgreementinterfaceid(tempInterface.getId());
		return result;
	}
	private InterfaceVo createInterface(InterfaceVo interfaceVo) {
		INSBAgreementinterface tempInterface = new INSBAgreementinterface();
		tempInterface.setId(UUIDUtils.create()) ;
		//tempInterface.setAgreementid("");
		tempInterface.setInterfaceid(interfaceVo.getInterfaceid());
		tempInterface.setIsfree(interfaceVo.getIsfree());
		tempInterface.setChannelinnercode(interfaceVo.getChannelinnercode());
		tempInterface.setExtendspattern(interfaceVo.getExtendspattern());
		tempInterface.setCreatetime(new Date());
		String monthfree = interfaceVo.getMonthfree();
		if (StringUtil.isNotEmpty(monthfree)) {
			tempInterface.setMonthfree(Integer.valueOf(monthfree));
		}
		//tempInterface.setPerfee(Double.valueOf(interfaceVo.getPerfee()));
		insbAgreementinterfaceDao.insert(tempInterface);
		InterfaceVo result = new InterfaceVo();
		result.setAgreementinterfaceid(tempInterface.getId());
		return result;
	}
	

}