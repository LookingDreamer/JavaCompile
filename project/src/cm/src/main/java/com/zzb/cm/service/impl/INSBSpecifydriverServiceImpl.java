package com.zzb.cm.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCDept;
import com.zzb.cm.controller.vo.INSBPersonVO;
import com.zzb.cm.controller.vo.OtherInformationVO;
import com.zzb.cm.dao.INSBCarinfoDao;
import com.zzb.cm.dao.INSBCarinfohisDao;
import com.zzb.cm.dao.INSBInvoiceinfoDao;
import com.zzb.cm.dao.INSBOrderDao;
import com.zzb.cm.dao.INSBPersonDao;
import com.zzb.cm.dao.INSBQuoteinfoDao;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.dao.INSBSpecifydriverDao;
import com.zzb.cm.dao.INSBSpecifydriverhisDao;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBCarinfohis;
import com.zzb.cm.entity.INSBInvoiceinfo;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.entity.INSBPerson;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.cm.entity.INSBSpecifydriver;
import com.zzb.cm.entity.INSBSpecifydriverhis;
import com.zzb.cm.entity.entityutil.EntityTransformUtil;
import com.zzb.cm.service.INSBSpecifydriverService;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.entity.INSBPolicyitem;

import net.sf.json.JSONObject;

@Service
@Transactional
public class INSBSpecifydriverServiceImpl   extends BaseServiceImpl<INSBSpecifydriver> implements
		INSBSpecifydriverService {
	@Resource
	private INSBSpecifydriverDao insbSpecifydriverDao;
	@Resource
	private INSBPersonDao insbPersonDao;
	@Resource
	private INSBCarinfoDao insbCarinfoDao;
	@Resource
	private INSBOrderDao insbOrderDao;
	@Resource
	private INSBSpecifydriverhisDao insbSpecifydriverhisDao;
	@Resource
	private INSBCarinfohisDao insbCarinfohisDao;
	@Resource
	private INSBPolicyitemDao insbPolicyitemDao;
	@Resource
	private INSCCodeDao inscCodeDao;
	@Resource
	private INSBQuotetotalinfoDao insbQuotetotalinfoDao;
	@Resource
	private INSBQuoteinfoDao insbQuoteinfoDao;
	@Resource
	private INSCDeptDao inscDeptDao;
	@Resource
	private INSBInvoiceinfoDao insbInvoiceinfoDao;
	@Override
	protected BaseDao<INSBSpecifydriver> getBaseDao() {
		return insbSpecifydriverDao;
	}
	/**
	 * 通过车辆信息id查询驾驶人信息
	 */
	@Override
	public List<Map<String, Object>> getSpecifydriversInfoByCarinfoId(String taskid, String inscomcode, String opeType) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//得到对应车辆驾驶人信息
		List<Map<String, Object>> driversInfo = new ArrayList<Map<String,Object>>();
		INSBCarinfo carinfo = insbCarinfoDao.selectCarinfoByTaskId(taskid);
		INSBSpecifydriver  specifyDriver = new INSBSpecifydriver();
		specifyDriver.setCarinfoid(carinfo.getId());
		List<INSBSpecifydriver> specifyDriverList = insbSpecifydriverDao.selectList(specifyDriver);
		//通过是否生成订单判断是否在人工报价节点
		INSBOrder order = new INSBOrder();
		order.setTaskid(taskid);
		order.setPrvid(inscomcode);    //保险公司id
		order = insbOrderDao.selectOne(order);
//		if(order==null){
			//不能查到订单表明是人工报价节点
			//重新取得驾驶人中间表信息
			INSBSpecifydriverhis  specifyDriverhis = new INSBSpecifydriverhis();
			specifyDriverhis.setCarinfoid(carinfo.getId());
			specifyDriverhis.setInscomcode(inscomcode);
			List<INSBSpecifydriverhis> specifyDriverhisList = insbSpecifydriverhisDao.selectList(specifyDriverhis);
			if(specifyDriverhisList!=null && specifyDriverhisList.size()>0){
				List<INSBSpecifydriver> specifyDriverListTemp = new ArrayList<INSBSpecifydriver>();
				for (int i = 0; i < specifyDriverhisList.size(); i++) {
					specifyDriverListTemp.add(EntityTransformUtil.specifydriverhis2Specifydriver(specifyDriverhisList.get(i)));
				}
				specifyDriverList = specifyDriverListTemp;
			}
//		}
		if("SHOW".equalsIgnoreCase(opeType)||"EDIT".equalsIgnoreCase(opeType)){
			if(specifyDriverList!=null && specifyDriverList.size()>0){
				for (int i = 0; i < specifyDriverList.size(); i++) {
					if(specifyDriverList.get(i)!=null){
						String personId = specifyDriverList.get(i).getPersonid();
						//得到驾驶人信息
						if(personId!=null){
							INSBPerson driverInfo= insbPersonDao.selectById(personId);
							if(driverInfo!=null){
								Map<String, Object> temp = new HashMap<String, Object>();
								if("EDIT".equalsIgnoreCase(opeType)){
									temp.put("driverId", personId);
									if(specifyDriverList.get(i).getId()!=null){
										temp.put("specifyDriverId", specifyDriverList.get(i).getId());
									}
								}
								if(driverInfo.getName()!=null){
									temp.put("driverName", driverInfo.getName());
								}
								if(driverInfo.getBirthday()!=null){
									temp.put("driverBirthday", sdf.format(driverInfo.getBirthday()).toString());
								}
								temp.put("driverGender", driverInfo.getGender());//性别code
								Map<String, String> param = new HashMap<String, String>();
								param.put("codetype", "Gender");
								param.put("parentcode", "Gender");
								param.put("codevalue", driverInfo.getGender().toString());
								temp.put("driverGenderValue", inscCodeDao.selectINSCCodeByCode(param).get(0).getCodename());//性别名称
								if(driverInfo.getLicensetype()!=null){
									temp.put("driverLicensetype", driverInfo.getLicensetype());//驾驶证类型code
									Map<String, String> param0 = new HashMap<String, String>();
									param0.put("codetype", "LicenseType");
									param0.put("parentcode", "LicenseType");
									param0.put("codevalue", driverInfo.getLicensetype());
									temp.put("driverLicensetypeValue", inscCodeDao.selectINSCCodeByCode(param0).get(0).getCodename());//驾驶证类型名称
								}
								if(driverInfo.getLicenseno()!=null){
									temp.put("driverLicenseno", driverInfo.getLicenseno());
								}
								if(driverInfo.getLicensedate()!=null){
									temp.put("driverLicensedate", sdf.format(driverInfo.getLicensedate()).toString());
								}
								driversInfo.add(temp);
							}
						}
					}
				}
			}
		}
		return driversInfo;
	}
	/**
	 * 修改其他信息弹出框提交修改处理
	 */
	@Override
	public String updateOtherInfo(OtherInformationVO otherInformation,INSBInvoiceinfo insbInvoiceinfo) {
		//通过是否生成订单判断是否在人工报价节点
		boolean noHasOrder = false;
		INSBOrder order = new INSBOrder();
		order.setTaskid(otherInformation.getTaskid());
		order.setPrvid(otherInformation.getInscomcode());   //保险公司id
		order = insbOrderDao.selectOne(order);
		if(order==null){
			//不能查到订单表明是人工报价节点
			noHasOrder = true;
		}else{
			// 人工报价的时候insbOrder是空，需要判断是否为空。
			if(null!=otherInformation.getCustomerinsurecode()&&!"".equals(otherInformation.getCustomerinsurecode())){//客户忠诚度有值，则修改order客户忠诚度的值
				order.setCustomerinsurecode(otherInformation.getCustomerinsurecode());
				insbOrderDao.updateById(order);
			}
			//修改出单网点
//			if(!StringUtils.isEmpty(otherInformation.getDeptCode())){
////				order.setDeptcode(otherInformation.getDeptCode());
//				order.setModifytime(new Date());
//				order.setOperator(otherInformation.getOperator());
//				insbOrderDao.updateById(order);
//			}
		}
		//2016年2月17日新需求要求每个节点都要查询修改历史表数据
		noHasOrder = true;
		//修改报价表里的出单网点
//		if(!StringUtils.isEmpty(otherInformation.getDeptCode())){
//			INSBQuotetotalinfo quotetotalinfo = new INSBQuotetotalinfo();
//			quotetotalinfo.setTaskid(otherInformation.getTaskid());
//			quotetotalinfo = insbQuotetotalinfoDao.selectOne(quotetotalinfo);
//			INSBQuoteinfo quoteinfo = null;
//			if(quotetotalinfo !=null){
//				quoteinfo = new INSBQuoteinfo();
//				quoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
//				quoteinfo.setInscomcode(otherInformation.getInscomcode());
//				quoteinfo = insbQuoteinfoDao.selectOne(quoteinfo);
//				if(quoteinfo!=null){
//					quoteinfo.setModifytime(new Date());
//					quoteinfo.setOperator(otherInformation.getOperator());
//					quoteinfo.setDeptcode(otherInformation.getDeptCode());
//					insbQuoteinfoDao.updateById(quoteinfo);
//				}
//			}
//		}
		//判断是否指定驾驶人历史表是否有此任务数据
		boolean hasHis = false;
		INSBSpecifydriverhis specifydriverhis = new INSBSpecifydriverhis();
		specifydriverhis.setCarinfoid(otherInformation.getCarinfoId());
		specifydriverhis.setInscomcode(otherInformation.getInscomcode());
		List<INSBSpecifydriverhis> specifydriverhisList = insbSpecifydriverhisDao.selectList(specifydriverhis);
		if(specifydriverhisList!=null && specifydriverhisList.size()>0){
			hasHis = true;
		}
		//处理其他信息弹出框传递的数据
		Date date = new Date();
		for (int i = 0; i < otherInformation.getDriversInfo().size(); i++) {
			INSBPersonVO person = otherInformation.getDriversInfo().get(i);
			if(person.getId()!=null && !("".equals(person.getId().trim()))){
				if(person.getName()!=null && !("".equals(person.getName().trim()))){
					//修改人员信息
					INSBPerson p = new INSBPerson();
					p.setId(person.getId().trim());
					p.setName(person.getName().trim());
					p.setBirthday(this.String2Date(person.getBirthday()));
					p.setGender(Integer.parseInt(person.getGender().trim()));
					p.setLicensetype(person.getLicensetype().trim());
					p.setLicenseno(person.getLicenseno().trim());
					p.setLicensedate(this.String2Date(person.getLicensedate()));
					p.setModifytime(date);
					p.setOperator(otherInformation.getOperator());
					//执行修改人员信息
					insbPersonDao.updateSpecifydriverById(p);
					//修改中间表信息
					INSBSpecifydriver sd = insbSpecifydriverDao.selectById(person.getSpecifyDriverId());
					INSBSpecifydriverhis sdh = insbSpecifydriverhisDao.selectById(person.getSpecifyDriverId());
					if(noHasOrder){//人工报价节点
						if(hasHis){//历史表里有此任务数据
							sdh.setModifytime(date);
							sdh.setOperator(otherInformation.getOperator());
							insbSpecifydriverhisDao.updateById(sdh);
						}else{//历史表里没有此任务数据
							INSBSpecifydriverhis sdhTemp = EntityTransformUtil.specifydriver2Specifydriverhis(sd, otherInformation.getInscomcode());
							sdhTemp.setModifytime(null);
							sdhTemp.setCreatetime(date);
							sdhTemp.setOperator(otherInformation.getOperator());
							insbSpecifydriverhisDao.insert(sdhTemp);
							person.setSpecifyDriverId(sdhTemp.getId());
						}
					}else{//不是人工报价节点
						sd.setModifytime(date);
						sd.setOperator(otherInformation.getOperator());
						insbSpecifydriverDao.updateById(sd);
					}
				}else{
					//删除人员信息及中间表信息
					if(noHasOrder){//人工报价节点
						if(hasHis){//历史表里有此任务数据
							insbSpecifydriverhisDao.deleteById(person.getSpecifyDriverId());
						}
					}else{//不是人工报价节点
						insbSpecifydriverDao.deleteById(person.getSpecifyDriverId().trim());
//							insbPersonDao.deleteById(person.getId().trim());
					}
				}
			}else{
				if(person.getName()!=null && !("".equals(person.getName().trim()))){
					//新建人员信息
					INSBPerson p = new INSBPerson();
					p.setName(person.getName().trim());
					p.setBirthday(this.String2Date(person.getBirthday()));
					p.setGender(Integer.parseInt(person.getGender().trim()));
					p.setLicensetype(person.getLicensetype().trim());
					p.setLicenseno(person.getLicenseno().trim());
					p.setLicensedate(this.String2Date(person.getLicensedate()));
					p.setTaskid(otherInformation.getTaskid().trim());
					p.setCreatetime(date);
					p.setOperator(otherInformation.getOperator());
					insbPersonDao.insert(p);
					//新建指定驾驶人中间表信息
					INSBSpecifydriver specifydriver = new INSBSpecifydriver();
					specifydriver.setCreatetime(date);
					specifydriver.setOperator(otherInformation.getOperator());
					specifydriver.setCarinfoid(otherInformation.getCarinfoId().trim());
					specifydriver.setTaskid(otherInformation.getTaskid().trim());
					specifydriver.setPersonid(p.getId());
					if(noHasOrder){//人工报价节点
						INSBSpecifydriverhis sdh = EntityTransformUtil.specifydriver2Specifydriverhis(specifydriver, otherInformation.getInscomcode());
						insbSpecifydriverhisDao.insert(sdh);
						person.setSpecifyDriverId(sdh.getId());
					}else{//不是人工报价节点
						insbSpecifydriverDao.insert(specifydriver);
						person.setSpecifyDriverId(specifydriver.getId());
					}
				}
			}
		}
		//修改发票信息
		INSBInvoiceinfo queryinsbInvoiceinfo=new INSBInvoiceinfo();
		Map<String,String> map=new HashMap<String,String>();
		map.put("taskid",otherInformation.getTaskid());
		//map.put("inscomcode",otherInformation.getInscomcode());
		map.put("inscomcode",null);
		INSBInvoiceinfo invoiceinfo=insbInvoiceinfoDao.selectByTaskidAndComcode(map);
		if(invoiceinfo==null){
				//没有发票信息,则执行插入语句
			queryinsbInvoiceinfo.setTaskid(otherInformation.getTaskid());
			queryinsbInvoiceinfo.setInscomcode(otherInformation.getInscomcode());
			queryinsbInvoiceinfo.setCreatetime(date);
			queryinsbInvoiceinfo.setNoti(null);
			queryinsbInvoiceinfo.setModifytime(null);
			queryinsbInvoiceinfo.setOperator(otherInformation.getOperator());
			queryinsbInvoiceinfo.setAccountnumber(insbInvoiceinfo.getAccountnumber());
			queryinsbInvoiceinfo.setBankname(insbInvoiceinfo.getBankname());
			queryinsbInvoiceinfo.setIdentifynumber(insbInvoiceinfo.getIdentifynumber());
			queryinsbInvoiceinfo.setRegisteraddress(insbInvoiceinfo.getRegisteraddress());
			queryinsbInvoiceinfo.setInvoicetype(insbInvoiceinfo.getInvoicetype());
			queryinsbInvoiceinfo.setRegisterphone(insbInvoiceinfo.getRegisterphone());
			queryinsbInvoiceinfo.setEmail(insbInvoiceinfo.getEmail());
			insbInvoiceinfoDao.insert(queryinsbInvoiceinfo);
		}else{
			if(insbInvoiceinfo.getAccountnumber()==null && insbInvoiceinfo.getBankname()==null &&
					insbInvoiceinfo.getIdentifynumber()==null && insbInvoiceinfo.getEmail()==null && insbInvoiceinfo.getRegisteraddress()==null
					&& insbInvoiceinfo.getRegisterphone()==null)
			{
				queryinsbInvoiceinfo.setInvoicetype(0);
			}else{
				queryinsbInvoiceinfo.setInvoicetype(insbInvoiceinfo.getInvoicetype());
			}
			queryinsbInvoiceinfo.setTaskid(otherInformation.getTaskid());
			queryinsbInvoiceinfo.setInscomcode(otherInformation.getInscomcode());
			queryinsbInvoiceinfo.setModifytime(date);
			queryinsbInvoiceinfo.setNoti(insbInvoiceinfo.getNoti());
			queryinsbInvoiceinfo.setOperator(otherInformation.getOperator());
			queryinsbInvoiceinfo.setAccountnumber(insbInvoiceinfo.getAccountnumber());
			queryinsbInvoiceinfo.setBankname(insbInvoiceinfo.getBankname());
			queryinsbInvoiceinfo.setIdentifynumber(insbInvoiceinfo.getIdentifynumber());
			queryinsbInvoiceinfo.setRegisteraddress(insbInvoiceinfo.getRegisteraddress());	
			queryinsbInvoiceinfo.setRegisterphone(insbInvoiceinfo.getRegisterphone());
			queryinsbInvoiceinfo.setEmail(insbInvoiceinfo.getEmail());
			insbInvoiceinfoDao.updateByTaskid(queryinsbInvoiceinfo);
		}
		//修改车辆信息
		INSBCarinfo carinfo = insbCarinfoDao.selectCarinfoByTaskId(otherInformation.getTaskid());
		INSBCarinfohis carinfohis = new INSBCarinfohis();
		carinfohis.setTaskid(otherInformation.getTaskid());
		carinfohis.setInscomcode(otherInformation.getInscomcode());
		carinfohis = insbCarinfohisDao.selectOne(carinfohis);
		boolean isInsert = false;
		if(noHasOrder){//人工报价节点
			if(carinfohis==null){
				carinfohis = EntityTransformUtil.carinfo2Carinfohis(carinfo,otherInformation.getInscomcode());
				isInsert = true;
			}
//				carinfohis.setBusinessstartdate(this.String2Date(otherInformation.getBusinessstartdate()));
//				carinfohis.setBusinessenddate(this.String2Date(otherInformation.getBusinessenddate()));
//				carinfohis.setStrongstartdate(this.String2Date(otherInformation.getCompulsorystartdate()));
//				carinfohis.setStrongenddate(this.String2Date(otherInformation.getCompulsoryenddate()));
//			carinfohis.setPreinscode(otherInformation.getPreinscode().trim());
			carinfohis.setOperator(otherInformation.getOperator());
			if("0".equals(otherInformation.getPointspecifydriver().trim())){//不指定驾驶人
				carinfohis.setSpecifydriver("");
			}else if("1".equals(otherInformation.getPointspecifydriver().trim())){//指定了驾驶人
				int index_ = otherInformation.getSpecifydriver().indexOf("_");
				int index = Integer.parseInt(otherInformation.getSpecifydriver().substring(0,index_).trim())-1;
				carinfohis.setSpecifydriver(otherInformation.getDriversInfo().get(index).getSpecifyDriverId());
			}
			//执行修改车辆信息
			if(isInsert){
				carinfohis.setCreatetime(date);
				carinfohis.setModifytime(null);
				insbCarinfohisDao.insert(carinfohis);
			}else{
				carinfohis.setModifytime(date);
				insbCarinfohisDao.updateById(carinfohis);
			}
		}else{//不是人工报价节点
//				carinfo.setBusinessstartdate(this.String2Date(otherInformation.getBusinessstartdate()));
//				carinfo.setBusinessenddate(this.String2Date(otherInformation.getBusinessenddate()));
//				carinfo.setStrongstartdate(this.String2Date(otherInformation.getCompulsorystartdate()));
//				carinfo.setStrongenddate(this.String2Date(otherInformation.getCompulsoryenddate()));
			carinfo.setPreinscode(otherInformation.getPreinscode().trim());
			carinfo.setModifytime(date);
			carinfo.setOperator(otherInformation.getOperator());
			if("0".equals(otherInformation.getPointspecifydriver().trim())){//不指定驾驶人
				carinfo.setSpecifydriver("");
			}else if("1".equals(otherInformation.getPointspecifydriver().trim())){//指定驾驶人
				int index_ = otherInformation.getSpecifydriver().indexOf("_");
				int index = Integer.parseInt(otherInformation.getSpecifydriver().substring(0,index_).trim())-1;
				carinfo.setSpecifydriver(otherInformation.getDriversInfo().get(index).getSpecifyDriverId());
			}
			//执行修改车辆信息
			insbCarinfoDao.updateById(carinfo);
			LogUtil.info("INSBCarinfo|报表数据埋点|"+JSONObject.fromObject(carinfo).toString());
		}
		INSBPolicyitem policyitem = new INSBPolicyitem();
		policyitem.setTaskid(otherInformation.getTaskid());
		policyitem.setInscomcode(otherInformation.getInscomcode());
		List<INSBPolicyitem> policyitemList = insbPolicyitemDao.selectList(policyitem);
		if(policyitemList!=null && policyitemList.size()>0){
			for (int i = 0; i < policyitemList.size(); i++) {
				policyitemList.get(i).setOperator(otherInformation.getOperator());
				policyitemList.get(i).setModifytime(date);
				if("0".equals(policyitemList.get(i).getRisktype())){//商业险保单
					if(StringUtils.isEmpty(otherInformation.getBusinessstartdate())){
						policyitemList.get(i).setStartdate(null);
						policyitemList.get(i).setEnddate(null);
					}else{
						policyitemList.get(i).setStartdate(this.String2Date(otherInformation.getBusinessstartdate()));
						policyitemList.get(i).setEnddate(this.String2Date02(otherInformation.getBusinessenddate()));
					}
					insbPolicyitemDao.updateById(policyitemList.get(i));
				}else if("1".equals(policyitemList.get(i).getRisktype())){//交强险保单
					if(StringUtils.isEmpty(otherInformation.getCompulsorystartdate())){
						policyitemList.get(i).setStartdate(null);
						policyitemList.get(i).setEnddate(null);
					}else{
						policyitemList.get(i).setStartdate(this.String2Date(otherInformation.getCompulsorystartdate()));
						policyitemList.get(i).setEnddate(this.String2Date02(otherInformation.getCompulsoryenddate()));
					}
					insbPolicyitemDao.updateById(policyitemList.get(i));
				}
			}
		}
		return "success";
	}
	/**
	 * String转Date
	 * */
	public Date String2Date(String dateStr){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = dateStr.trim();
		if(dateString.length()>10){
			dateString = dateString.substring(0, 10);
		}
		Date date = null;
		try {
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	/**
	 * String转Date
	 * */
	public Date String2Date02(String dateStr){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = dateStr.trim();
		if(dateString.length()>10){
			dateString = dateString.substring(0, 10);
		}
		dateString += " 23:59:59";
		Date date = null;
		try {
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * 获取性别和驾驶证类型字典提供其他信息弹出框使用
	 */
	@Override
	public Map<String, Object> getCodeOfGenderAndLicenseType() {
		Map<String, Object> temp = new HashMap<String, Object>();
		Map<String, String> param = new HashMap<String, String>();
		param.put("codetype", "Gender");
		param.put("parentcode", "Gender");
		temp.put("GenderList", inscCodeDao.selectINSCCodeByCode(param));//性别列表
		Map<String, String> param0 = new HashMap<String, String>();
		param0.put("codetype", "LicenseType");
		param0.put("parentcode", "LicenseType");
		temp.put("LicenseTypeList", inscCodeDao.selectINSCCodeByCode(param0));//驾驶证类型列表
		return temp;
	}

}