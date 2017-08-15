package com.zzb.conf.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cninsure.core.utils.UUIDUtils;
import com.zzb.chn.util.JsonUtils;
import com.zzb.conf.dao.*;
import com.zzb.conf.entity.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCDeptService;
import com.zzb.conf.service.INSBAgreementService;
import com.zzb.conf.service.INSBAgreementdeptService;
import com.zzb.conf.service.INSBAgreementpaymethodService;
import com.zzb.conf.service.INSBAgreementproviderService;
import com.zzb.conf.service.INSBChannelService;
import com.zzb.conf.service.INSBOutorderdeptService;

@Service
@Transactional
public class INSBAgreementproviderServiceImpl extends BaseServiceImpl<INSBAgreementprovider> implements
		INSBAgreementproviderService {
	@Resource
	private INSBAgreementproviderDao insbAgreementproviderDao;
	@Resource
	private INSBAgreementdeptService agreementdeptService;
	@Resource
	private INSBAgreementpaymethodService agreementpaymethodService;
	@Resource
	private INSCDeptService inscDeptService;
	@Resource
	private INSBAgreementService insbAgreementService;
	@Resource
	private INSBChannelService insbChannelService;
	@Resource
	private INSBPaychannelDao insbPaychannelDao;
	@Resource
	private INSBOutorderdeptService insbOutorderdeptService;
	@Resource
	private INSBChannelDao insbChannelDao;
	@Resource
	private INSBChannelagreementDao insbChannelagreementDao;
	@Resource
	private INSBAgreementdeptDao insbAgreementdeptDao;
	@Resource
	private INSBAgreementpaymethodDao insbAgreementpaymethodDao;

	@Override
	protected BaseDao<INSBAgreementprovider> getBaseDao() {
		return insbAgreementproviderDao;
	}

	@Override
	public String deletePrvDeptPay(String id, String agreeid) throws Exception {
		Map<Object, Object> initMap = new HashMap<Object, Object>();
		try{
			INSBAgreementprovider ap = insbAgreementproviderDao.selectById(id);
			String agreementid = ap.getAgreementid();
			String prvid = ap.getProviderid();
			
			INSBAgreementpaymethod payDelCon = new INSBAgreementpaymethod();
			payDelCon.setAgreementid(agreementid);
			payDelCon.setProviderid(prvid);
			insbAgreementpaymethodDao.delete(payDelCon);
			
			INSBAgreementdept deptCon = new INSBAgreementdept();
			deptCon.setAgreementid(agreementid); 
			deptCon.setProviderid(prvid); 
			insbAgreementdeptDao.delete(deptCon);
			
			insbAgreementproviderDao.deleteById(id);
			//int a = agreementdeptService.delByAgreeid(agreeid);
			//int b = agreementpaymethodService.delByAgreeid(agreeid);
			//LogUtil.info("a,"+a+",b"+b);
			
			initMap.put("status", "success");
			initMap.put("msg", "删除成功");
			return JSONObject.toJSONString(initMap);
		}catch(Exception e){
			throw new RuntimeException();
		}
	}

	@Override
	public String procImportPrvData(String agreementid, List<Row> rows, INSCUser operator, String channelid) throws Exception {
		String errorPrvIds = "";
		List<INSBAgreementprovider> prvs = new ArrayList<INSBAgreementprovider>();
		List<INSBAgreementdept> depts = new ArrayList<INSBAgreementdept>();
		List<INSBAgreementpaymethod> pays = new ArrayList<INSBAgreementpaymethod>();

		INSBChannel insbChannel = insbChannelService.queryById(channelid);

		List<Map<String, Object>> insbPaychannels = insbPaychannelDao.selectPayChannelList();
		Map<String, Object> insbPaychannelMap = new HashMap<String, Object>();
		for (Map<String, Object> insbPaychannel : insbPaychannels) {
			insbPaychannelMap.put(insbPaychannel.get("id") + "", insbPaychannel.get("id"));
		}

		for (int i = 1; i < rows.size(); i++) {
			Row row = rows.get(i);
			String prvId = getCellStringValue(row.getCell(0));
			String agreeCode = getCellStringValue(row.getCell(1));
			String deptId = getCellStringValue(row.getCell(2));
			String payIds = getCellStringValue(row.getCell(3));

			LogUtil.info(String.format("channel-importPrv:prvId=%s,agreeCode=%s,deptId=%s,payIds=%s", prvId, agreeCode, deptId, payIds));

			boolean checkFlag = buildImportPrvData(prvs, depts, pays, prvId, agreeCode, deptId, payIds,
					agreementid, operator, insbChannel.getDeptid(), insbPaychannelMap);
			if (!checkFlag) {
				if (StringUtil.isEmpty(errorPrvIds)) {
					errorPrvIds = prvId;
				} else {
					errorPrvIds += ", " + prvId;
				}
			}
		}

		if (!prvs.isEmpty()) {
			INSBAgreementprovider prvDelCon = new INSBAgreementprovider();
			prvDelCon.setAgreementid(agreementid);
			insbAgreementproviderDao.delete(prvDelCon);

			INSBAgreementdept deptDelCon = new INSBAgreementdept();
			deptDelCon.setAgreementid(agreementid);
			agreementdeptService.delete(deptDelCon);

			INSBAgreementpaymethod payDelCon = new INSBAgreementpaymethod();
			payDelCon.setAgreementid(agreementid);
			agreementpaymethodService.delete(payDelCon);

			insbAgreementproviderDao.insertInBatch(prvs);
			agreementdeptService.insertInBatch(depts);
			agreementpaymethodService.insertInBatch(pays);
		}
		return errorPrvIds;
	}

	private boolean buildImportPrvData(List<INSBAgreementprovider> prvs, List<INSBAgreementdept> depts, List<INSBAgreementpaymethod> pays,
									   String prvId, String agreeCode, String deptId, String payIds, String agreementid,
									   INSCUser operator, String pDeptId, Map<String, Object> insbPaychannelMap) {
		if (StringUtil.isEmpty(prvId) || StringUtil.isEmpty(agreeCode)
				|| StringUtil.isEmpty(deptId) || StringUtil.isEmpty(payIds)) {
			LogUtil.info("channel-importPrv:存在字段为空");
			return false;
		}

		INSBAgreement insbAgreement = new INSBAgreement();
		insbAgreement.setAgreementcode(agreeCode);
		insbAgreement.setProviderid(prvId);
		insbAgreement.setDeptid(pDeptId);
		insbAgreement = insbAgreementService.queryOne(insbAgreement);
		if (insbAgreement == null) {
			LogUtil.info("channel-importPrv:insbAgreement匹配失败");
			return false;
		}

		INSBOutorderdept insbOutorderdept = new INSBOutorderdept();
		insbOutorderdept.setAgreementid(insbAgreement.getId());
		insbOutorderdept.setDeptid5(deptId);
		insbOutorderdept = insbOutorderdeptService.queryOne(insbOutorderdept);
		if (insbOutorderdept == null) {
			LogUtil.info("channel-importPrv:insbOutorderdept匹配失败");
			return false;
		}
		INSCDept inscDept = inscDeptService.queryById(deptId);
		if (inscDept == null) {
			LogUtil.info("channel-importPrv:inscDept匹配失败");
			return false;
		}

		String[] payIdArray = payIds.split(";");
		for (String payId : payIdArray) {
			if (StringUtil.isEmpty(payId) || insbPaychannelMap.get(payId) == null) {
				LogUtil.info("channel-importPrv:payId匹配失败" + payId);
				return false;
			}
		}

		INSBAgreementprovider prv = new INSBAgreementprovider();
		prv.setAgreeid(insbAgreement.getId());
		prv.setAgreementid(agreementid);
		prv.setCreatetime(new Date());
		prv.setOperator(operator.getName());
		prv.setProviderid(prvId);
		prvs.add(prv);

		String[] parentcodes = inscDept.getParentcodes().split("[+]");
		INSBAgreementdept dept = new INSBAgreementdept();
		dept.setAgreementid(agreementid);
		dept.setCreatetime(new Date());
		dept.setDeptid1(parentcodes[2]);
		dept.setDeptid2(parentcodes[3]);
		dept.setDeptid3(parentcodes[4]);
		dept.setDeptid4(parentcodes[5]);
		dept.setDeptid5(deptId);
		dept.setOperator(operator.getName());
		dept.setProviderid(prvId);
		depts.add(dept);

		for (String payId : payIdArray) {
			INSBAgreementpaymethod pay = new INSBAgreementpaymethod();
			pay.setAgreementid(agreementid);
			pay.setCreatetime(new Date());
			pay.setOperator(operator.getName());
			pay.setPaychannelid(payId);
			pay.setProviderid(prvId);
			pays.add(pay);
		}

		return true;
	}

	private String getCellStringValue(Cell cell) {
		String cellValue = "";
		if (null != cell) {
			switch (cell.getCellType()) {
				case HSSFCell.CELL_TYPE_NUMERIC: // 数字
					DecimalFormat df = new DecimalFormat("0");
					cellValue = df.format(cell.getNumericCellValue());
					break;

				case HSSFCell.CELL_TYPE_STRING: // 字符串
					cellValue = cell.getStringCellValue();
					break;

				case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
					cellValue = cell.getBooleanCellValue() + "";
					break;

				case HSSFCell.CELL_TYPE_FORMULA: // 公式
					cellValue = cell.getCellFormula() + "";
					break;

				case HSSFCell.CELL_TYPE_BLANK: // 空值
					cellValue = "";
					break;

				case HSSFCell.CELL_TYPE_ERROR: // 故障
					cellValue = "非法字符";
					break;

				default:
					cellValue = "未知类型";
					break;
			}
		}
		return cellValue;
	}

	public String batchCopyChannelProvider(String fromChannelId,String toChannelIds,String user){
		Map<String ,Object> resultMap = new HashMap<>();
		INSBChannel fromChannel = insbChannelDao.selectById(fromChannelId);
		String fromDeptId = fromChannel.getDeptid();
		String[] toIds = toChannelIds.split(",");
		boolean detpdiff = false;
		boolean channelt = false;
		StringBuffer errMsg1 = new StringBuffer();
		StringBuffer errMsg2 = new StringBuffer();
		//判断所属平台是否一致
		for(String toId : toIds ){
			INSBChannel toChannel = insbChannelDao.selectById(toId);
			if(!fromDeptId.equals(toChannel.getDeptid())){
				errMsg1.append(toChannel.getChannelname()).append("，");
				detpdiff = true;
			}
			if(fromChannelId.equals(toId)){
				errMsg2.append(toChannel.getChannelname()).append("，");
				channelt = true;
			}
		}
		if(detpdiff){
			resultMap.put("status","2");
			resultMap.put("msg",errMsg1.toString()+" 数据错误，所属平台不一致，请核对！");
			return JsonUtils.serialize(resultMap);
		}
		if(channelt){
			resultMap.put("status","2");
			resultMap.put("msg",errMsg2.toString()+" 数据错误，不能复制数据给同渠道，请核对！");
			return JsonUtils.serialize(resultMap);
		}

		List<INSBChannelagreement> fromChannelAgreement = insbChannelagreementDao.getChannelagreement(fromChannelId);


		for(INSBChannelagreement insbChannelagreement : fromChannelAgreement){
			String oldAgreementId = insbChannelagreement.getId();
			for(String toId : toIds ){
				//查找数据库中该渠道是否有供应商数据，有先删除。

				this.deleteData(toId);

				INSBChannelagreement toChannelAgreement = new INSBChannelagreement();
				toChannelAgreement.setChannelid(toId);
				List<INSBChannelagreement> toChannelAgreements = insbChannelagreementDao.selectList(toChannelAgreement);
				toChannelAgreement = toChannelAgreements.get(0);
				String newAgreementId = toChannelAgreement.getId();
				//
				INSBAgreementprovider providerParams = new INSBAgreementprovider();
				providerParams.setAgreementid(oldAgreementId);
				List<INSBAgreementprovider> insbAgreementproviders = insbAgreementproviderDao.selectList(providerParams);

				INSBAgreementdept deptParams = new INSBAgreementdept();
				deptParams.setAgreementid(oldAgreementId);
				List<INSBAgreementdept> insbAgreementdepts = insbAgreementdeptDao.selectList(deptParams);

				INSBAgreementpaymethod paymethodParams = new INSBAgreementpaymethod();
				paymethodParams.setAgreementid(oldAgreementId);
				List<INSBAgreementpaymethod> insbAgreementpaymethods = insbAgreementpaymethodDao.selectList(paymethodParams);

				/*//组装新数据begin insbChannelagreement不需要复制
				insbChannelagreement.setId(newAgreementId);
				insbChannelagreement.setChannelid(toId);
				insbChannelagreement.setCreatetime(new Date());
				insbChannelagreement.setModifytime(new Date());
				insbChannelagreement.setOperator(user);
				insbChannelagreementDao.insert(insbChannelagreement);
               */

				this.transferAgreementProvider(insbAgreementproviders,newAgreementId,user);
				insbAgreementproviderDao.insertInBatch(insbAgreementproviders);

				this.transferAgreementDept(insbAgreementdepts, newAgreementId, user);
				insbAgreementdeptDao.insertInBatch(insbAgreementdepts);

				this.transferAgreementPaymethod(insbAgreementpaymethods, newAgreementId, user);
				insbAgreementpaymethodDao.insertInBatch(insbAgreementpaymethods);

			}
		}
		resultMap.put("status","1");
		resultMap.put("msg","复制成功");

		return JsonUtils.serialize(resultMap);
	}

	private void transferAgreementProvider(List<INSBAgreementprovider> insbAgreementproviders,String newAgreementId,String user){
		if(null == insbAgreementproviders){
			return;
		}
		for(INSBAgreementprovider insbAgreementprovider : insbAgreementproviders){
			insbAgreementprovider.setAgreementid(newAgreementId);
			insbAgreementprovider.setId(UUIDUtils.random());
			insbAgreementprovider.setOperator(user);
			insbAgreementprovider.setCreatetime(new Date());
			insbAgreementprovider.setModifytime(null);
		}
	}
	private void transferAgreementDept(List<INSBAgreementdept> insbAgreementdepts,String newAgreementId,String user){
		if(null == insbAgreementdepts){
			return;
		}
		for(INSBAgreementdept insbAgreementDept : insbAgreementdepts){
			insbAgreementDept.setAgreementid(newAgreementId);
			insbAgreementDept.setId(UUIDUtils.random());
			insbAgreementDept.setOperator(user);
			insbAgreementDept.setCreatetime(new Date());
			insbAgreementDept.setModifytime(null);
		}
	}
	private void transferAgreementPaymethod(List<INSBAgreementpaymethod> insbAgreementpaymethods,String newAgreementId,String user){
		if(null == insbAgreementpaymethods){
			return;
		}
		for(INSBAgreementpaymethod insbAgreementPaymethod : insbAgreementpaymethods){
			insbAgreementPaymethod.setAgreementid(newAgreementId);
			insbAgreementPaymethod.setId(UUIDUtils.random());
			insbAgreementPaymethod.setOperator(user);
			insbAgreementPaymethod.setCreatetime(new Date());
			insbAgreementPaymethod.setModifytime(null);
		}
	}

	private void deleteData(String toId){
		List<INSBChannelagreement> toChannelAgreements = insbChannelagreementDao.getChannelagreement(toId);
		for(INSBChannelagreement toChannelAgreement : toChannelAgreements){
			String agreementid = toChannelAgreement.getId();
			INSBAgreementprovider prvDelCon = new INSBAgreementprovider();
			prvDelCon.setAgreementid(agreementid);
			insbAgreementproviderDao.delete(prvDelCon);

			INSBAgreementdept deptDelCon = new INSBAgreementdept();
			deptDelCon.setAgreementid(agreementid);
			agreementdeptService.delete(deptDelCon);

			INSBAgreementpaymethod payDelCon = new INSBAgreementpaymethod();
			payDelCon.setAgreementid(agreementid);
			agreementpaymethodService.delete(payDelCon);

			// insbChannelagreementDao.deleteById(agreementid);
		}

	}

}
