package com.zzb.conf.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.common.ModelUtil;
import com.zzb.conf.controller.vo.DeptPayTypeVo;
import com.zzb.conf.dao.INSBAgreementpaymethodDao;
import com.zzb.conf.dao.INSBPaychannelmanagerDao;
import com.zzb.conf.entity.INSBAgreementpaymethod;
import com.zzb.conf.entity.INSBPaychannelmanager;
import com.zzb.conf.service.INSBPaychannelmanagerService;

@Service
@Transactional 
public class INSBPaychannelmanagerServiceImpl extends BaseServiceImpl<INSBPaychannelmanager> implements
		INSBPaychannelmanagerService {
	@Resource
	private INSBPaychannelmanagerDao insbPaychannelmanagerDao;
	@Resource
	private INSBAgreementpaymethodDao insbAgreementpaymethodDao;

	@Override
	protected BaseDao<INSBPaychannelmanager> getBaseDao() {
		return insbPaychannelmanagerDao;
	}

	@Override
	public Long deleteByExceptIds(List<String> ids,String agreementid){
		return insbPaychannelmanagerDao.deleteByExceptIds(ids,agreementid);
	}
	
	@Override
	public void delteDataBylogicId(Map<String, String> param) {
		insbPaychannelmanagerDao.deleteByLogicId(param);
	}

	@Override
	public List<DeptPayTypeVo> getDeptPayType(String providerid, String deptid, String agreeid) {
		INSBAgreementpaymethod temp = new INSBAgreementpaymethod();
		temp.setAgreementid(agreeid);
		temp.setDeptid(deptid);
		temp.setProviderid(providerid);
		List<String> selectedPaymethod = new ArrayList<String>();
		List<INSBAgreementpaymethod> selectedPaymethods = insbAgreementpaymethodDao.selectList(temp);
		for (INSBAgreementpaymethod paymethod : selectedPaymethods) {
			selectedPaymethod.add(paymethod.getPaychannelid());
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("agreeid", agreeid);
		map.put("deptid", deptid);
		map.put("providerid", providerid);
		List<DeptPayTypeVo> list = insbPaychannelmanagerDao.getDeptPayType(map);
		for (DeptPayTypeVo deptPayTypeVo : list) {
			if(selectedPaymethod.contains(deptPayTypeVo.getId())){
				deptPayTypeVo.setState(true);
				deptPayTypeVo.setModifytime(ModelUtil.conbertToString(new Date()));
				deptPayTypeVo.setCheck("开启");
			}else{
				deptPayTypeVo.setState(false);
				deptPayTypeVo.setCheck("关闭");
			}
		}
		return list;
	}

	@Override
	public List<INSBPaychannelmanager> queryListByParam(
			Map<String, Object> param) {
		return insbPaychannelmanagerDao.queryListByParam(param);
	}

	@Override
	public Long queryListByParamSize(Map<String, Object> param) {
		return insbPaychannelmanagerDao.selectDataSize(param);
	}

	//保存支付方式
	@Override
	public String savePayType(List<DeptPayTypeVo> deptPayTypeVolist,String flag, String providerid, String agreementid) {
		try {
			if (StringUtil.isEmpty(providerid) || StringUtil.isEmpty(agreementid)) {
				throw new Exception("providerid和agreementid必传");
			}
			
			INSBAgreementpaymethod methodCond = new INSBAgreementpaymethod();
			methodCond.setAgreementid(agreementid);
			methodCond.setProviderid(providerid);
			List<INSBAgreementpaymethod> methods = insbAgreementpaymethodDao.selectList(methodCond);
			
			if (methods != null && !methods.isEmpty()) {
				for (INSBAgreementpaymethod method : methods) {
					insbAgreementpaymethodDao.deleteById(method.getId());
				}
			}
			
			for (DeptPayTypeVo deptPayTypeVo : deptPayTypeVolist) {
				INSBAgreementpaymethod tempPaymethod = new INSBAgreementpaymethod();
				tempPaymethod.setAgreementid(agreementid);
				tempPaymethod.setProviderid(providerid);
				tempPaymethod.setPaychannelid(deptPayTypeVo.getId());
				tempPaymethod.setId(UUIDUtils.create());
				tempPaymethod.setCreatetime(new Date());
				insbAgreementpaymethodDao.insert(tempPaymethod);	
			}
			
			/* for (DeptPayTypeVo deptPayTypeVo : deptPayTypeVolist) {
				INSBAgreementpaymethod tempPaymethod = new INSBAgreementpaymethod();
				tempPaymethod.setAgreementid(deptPayTypeVo.getAgreementid());
				tempPaymethod.setDeptid(deptPayTypeVo.getDeptid());
				tempPaymethod.setProviderid(deptPayTypeVo.getProviderid());
				tempPaymethod.setPaychannelid(deptPayTypeVo.getId());
				INSBAgreementpaymethod selectOne = insbAgreementpaymethodDao.selectOne(tempPaymethod);
				if("1".equals(flag)){//插入
					if(selectOne ==null){
						tempPaymethod.setId(UUIDUtils.create());
						tempPaymethod.setCreatetime(new Date());
						insbAgreementpaymethodDao.insert(tempPaymethod);
					}
				}else{//删除
					if(selectOne!=null){
						insbAgreementpaymethodDao.deleteById(selectOne.getId());
					}
				}
			} */
		} catch (Exception e) {
			return "fail";
		}
		return "success";
	}

    public void copySubmitData(INSBPaychannelmanager src, INSBPaychannelmanager dest) {
        if (src == null || dest == null) return;

        dest.setCollectiontype(src.getCollectiontype());
        dest.setPaytarget(src.getPaytarget());
        dest.setSort(src.getSort());
        dest.setFavorabledescribe(src.getFavorabledescribe());
        dest.setSettlementno(src.getSettlementno());
        dest.setSettlementnoname(src.getSettlementnoname());
        dest.setTerraceno(src.getTerraceno());
        dest.setTerracenoname(src.getTerracenoname());
        dest.setTransferdesc(src.getTransferdesc());
    }
}