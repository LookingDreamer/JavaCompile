package com.zzb.conf.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.service.INSCDeptService;
import com.common.PagingParams;
import com.zzb.conf.controller.vo.BaseInfoVo;
import com.zzb.conf.controller.vo.BillTypeVo;
import com.zzb.conf.controller.vo.InterfaceVo;
import com.zzb.conf.dao.INSBAgreementDao;
import com.zzb.conf.dao.INSBAgreementareaDao;
import com.zzb.conf.dao.INSBAgreementdeptDao;
import com.zzb.conf.dao.INSBAgreementinterfaceDao;
import com.zzb.conf.dao.INSBAgreementpaymethodDao;
import com.zzb.conf.dao.INSBAgreementproviderDao;
import com.zzb.conf.dao.INSBChannelDao;
import com.zzb.conf.dao.INSBChannelagreementDao;
import com.zzb.conf.dao.INSBInterfaceDao;
import com.zzb.conf.entity.INSBAgreement;
import com.zzb.conf.entity.INSBAgreementarea;
import com.zzb.conf.entity.INSBAgreementdept;
import com.zzb.conf.entity.INSBAgreementinterface;
import com.zzb.conf.entity.INSBAgreementpaymethod;
import com.zzb.conf.entity.INSBAgreementprovider;
import com.zzb.conf.entity.INSBChannel;
import com.zzb.conf.entity.INSBChannelagreement;
import com.zzb.conf.entity.INSBInterface;
import com.zzb.conf.entity.INSBOutorderdept;
import com.zzb.conf.service.INSBChannelagreementService;
import com.zzb.conf.service.INSBOutorderdeptService;

@Service
@Transactional
public class INSBChannelagreementServiceImpl extends BaseServiceImpl<INSBChannelagreement> implements
		INSBChannelagreementService {
	@Resource
	private INSBChannelagreementDao insbChannelagreementDao; 
	@Resource 
	private INSBChannelDao insbChannelDao;
	@Resource
	private INSBAgreementinterfaceDao insbAgreementinterfaceDao;
	@Resource
	private INSBInterfaceDao insbInterfaceDao;
	@Resource
	private INSBAgreementdeptDao agreementdeptDao;
	@Resource
	private INSBAgreementareaDao insbAgreementareaDao;
	@Resource
	private INSCDeptDao deptDao; 
	@Resource
	private INSBAgreementpaymethodDao agreementpaymethodDao;
	@Resource
	private INSBAgreementproviderDao insbAgreementproviderDao;
	@Resource
	private INSBAgreementDao insbAgreementDao;
	@Resource
	private INSCDeptService inscDeptService;
	@Resource
	private INSBOutorderdeptService outorderdeptService;
	
	@Override
	protected BaseDao<INSBChannelagreement> getBaseDao() {
		return insbChannelagreementDao;
	}

	@Override
	public BillTypeVo getBillTypeInfo(INSBChannel insbChannel,int offset,int limit) {
		BillTypeVo typeVo = new BillTypeVo();
		//查询基本信息
		Map<String,Object> map = insbChannelDao.getBillTypeInfo(insbChannel);
		typeVo.setChannelid((String) map.get("id"));
		typeVo.setDeptid((String) map.get("deptid"));
		typeVo.setChannelname((String) map.get("channelname"));
		typeVo.setDeptname((String) map.get("comname"));
		typeVo.setPayperiod((String) map.get("payperiod"));
		typeVo.setPaytype((String) map.get("paytype"));
		typeVo.setPayaccount((String) map.get("payaccount"));
		typeVo.setInterfacetype((String) map.get("interfacetype"));
		typeVo.setAgreementid((String) map.get("agreementid"));
		//查询已选择的接口,将接口id作为map的key
		List<Map<String,Object>> tempList = insbAgreementinterfaceDao.getInterfaceInfo(typeVo.getAgreementid());
		Map<String,Object> interCode = new HashMap<String,Object>();
		for (Map<String, Object> checkedInterface : tempList) {
			interCode.put((String) checkedInterface.get("interfaceid"), checkedInterface);
		}
		//查出所有的接口,标记已选择的接口check属性为1,未选择为0
		List<InterfaceVo> allInterList = new ArrayList<InterfaceVo>();
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("offset", offset);
		params.put("limit", limit);
		for (INSBInterface insbInterface : insbInterfaceDao.selectAll(params)) {
			InterfaceVo temp = new InterfaceVo();
			temp.setInterfacename(insbInterface.getName());
			temp.setInterfaceid(insbInterface.getId());
			//判断该接口是否被选中
			if(interCode.containsKey(insbInterface.getId())){
				Map<String,Object> tempMap = (Map<String,Object>) interCode.get(insbInterface.getId());
				temp.setCheck("1");
				temp.setIsfree(tempMap.get("isfree").toString());
				temp.setMonthfree(tempMap.get("monthfree").toString());
				temp.setPerfee(tempMap.get("perfee").toString());
				temp.setAgreementinterfaceid(tempMap.get("id").toString());
			}else{
				temp.setCheck("0");
			}
			allInterList.add(temp);
		}
		typeVo.setAgreementInterfaceList(allInterList);
		return typeVo;
	}
	/**
	 * 此方法用于根据渠道协议表查询协议出单网点表信息 
	 */
	@Override
	public List<INSBAgreementdept> existChannelid(String channelid) {
		List<INSBChannelagreement> channelagreementList=insbChannelagreementDao.getChannelagreement(channelid);
		List<INSBAgreementdept> agreementdeptList=new ArrayList<INSBAgreementdept>();
		INSBAgreementdept agreementdept=null;
		for(INSBChannelagreement channelagreement:channelagreementList){
			agreementdept=new INSBAgreementdept();
//			channelagreement.getId()
			agreementdept.setAgreementid(channelagreement.getId());
			agreementdeptList=agreementdeptDao.selectList(agreementdept);
			for (INSBAgreementdept insbAgreementdept : agreementdeptList) {
				insbAgreementdept.setDeptid1(deptDao.selectById(insbAgreementdept.getDeptid1()).getComname());
				insbAgreementdept.setDeptid2(deptDao.selectById(insbAgreementdept.getDeptid2()).getComname());
				insbAgreementdept.setDeptid3(deptDao.selectById(insbAgreementdept.getDeptid3()).getComname());
				insbAgreementdept.setDeptid4(deptDao.selectById(insbAgreementdept.getDeptid4()).getComname());
				insbAgreementdept.setDeptid5(deptDao.selectById(insbAgreementdept.getDeptid5()).getComname());
			}
			//deptDao.selectById(agreementdept.getDeptid1());
//			agreementdeptList.add(agreementdept);
		}
		
		return agreementdeptList;
	}

	@Override
	public String updateBillTypeInfo(BillTypeVo billTypeVo) {
		if(billTypeVo.getAgreementid()==null||"".equals(billTypeVo.getAgreementid())){
			INSBChannelagreement temp = new INSBChannelagreement();
			temp.setId(UUIDUtils.create());
			temp.setChannelid(billTypeVo.getChannelid());
			temp.setDeptid(billTypeVo.getDeptid());
			temp.setCreatetime(new Date());
			temp.setPayperiod(billTypeVo.getPayperiod());
			temp.setPayaccount(billTypeVo.getPayaccount());
			temp.setPaytype(billTypeVo.getPaytype());
			temp.setInterfacetype(billTypeVo.getInterfacetype());
			insbChannelagreementDao.insert(temp);
			billTypeVo.setAgreementid(temp.getId());
		}else{
			INSBChannelagreement temp = insbChannelagreementDao.selectById(billTypeVo.getAgreementid());
			temp.setPayperiod(billTypeVo.getPayperiod());
			temp.setPayaccount(billTypeVo.getPayaccount());
			temp.setPaytype(billTypeVo.getPaytype());
			temp.setInterfacetype(billTypeVo.getInterfacetype());
			insbChannelagreementDao.updateById(temp);
		}
		List<InterfaceVo> interfaceList = billTypeVo.getAgreementInterfaceList();
		for (InterfaceVo interfaceVo : interfaceList) {
			if("1".equals(interfaceVo.getCheck())){
				if(!"1".equals(interfaceVo.getCheckflag())){
					//add
					addInterfaceInfo(billTypeVo, interfaceVo);
				}else{
					//update
					updateInterfaceInfo(interfaceVo);
				}
			}else{
				if("1".equals(interfaceVo.getCheckflag())){
					//delete
					insbAgreementinterfaceDao.deleteById(interfaceVo.getAgreementinterfaceid());
				}
			}
		}
		return "success";
	}

	private void addInterfaceInfo(BillTypeVo billTypeVo, InterfaceVo interfaceVo) {
		INSBAgreementinterface tempInterface = new INSBAgreementinterface();
		tempInterface.setId(UUIDUtils.create());
		tempInterface.setAgreementid(billTypeVo.getAgreementid());
		tempInterface.setInterfaceid(interfaceVo.getInterfaceid());
		if(interfaceVo.getIsfree()!=null)
		tempInterface.setIsfree(interfaceVo.getIsfree().toString());
		tempInterface.setMonthfree(Integer.valueOf(interfaceVo.getMonthfree()));
		tempInterface.setPerfee(Double.valueOf(interfaceVo.getPerfee()));
		insbAgreementinterfaceDao.insert(tempInterface);
	}

	private void updateInterfaceInfo(InterfaceVo interfaceVo) {
		INSBAgreementinterface tempInterface = insbAgreementinterfaceDao.selectById(interfaceVo.getAgreementinterfaceid());
		tempInterface.setIsfree(interfaceVo.getIsfree());
		tempInterface.setMonthfree(Integer.valueOf(interfaceVo.getMonthfree()));
		tempInterface.setPerfee(Double.valueOf(interfaceVo.getPerfee()));
		insbAgreementinterfaceDao.updateById(tempInterface);
	}
	/**
	 * 初始化供应商列表
	 */
	@Override
	public List<Map<String,Object>> qChannelAgreementprovider(String channelid,PagingParams para) {
		List<Map<String,Object>> channelagreements=insbChannelagreementDao.getChannelAgreementProvider(channelid,para);
		
		for(Map<String, Object> channelagreement : channelagreements){
			String agreementid = (String)channelagreement.get("agreementid");
			String deptid5 = null; //(String)channelagreement.get("deptid5");
			String providerid = (String)channelagreement.get("providerid");
			
			Map<String, Object> pcnMap = insbChannelagreementDao.getPayChannelNames(agreementid, deptid5, providerid);
			if ( pcnMap == null ) {
				channelagreement.put("paychannelnames", null);
			} else {
				channelagreement.put("paychannelnames", pcnMap.get("paychannelnames"));
			}
		}
		
		return channelagreements;
	}

	//保存基础信息
	@Override
	public String saveBaseInfo(BaseInfoVo baseInfoVo) {
		String agreementid = baseInfoVo.getAgreementid();
		//当前状态存储
		if(StringUtil.isEmpty(baseInfoVo.getAgreementid())){
			INSBChannelagreement temp = new INSBChannelagreement();
			temp.setId(UUIDUtils.create());
			temp.setCreatetime(new Date());
			temp.setChannelid(baseInfoVo.getChannelid());
			temp.setDeptid(baseInfoVo.getDeptid());
			temp.setAgreementstatus(baseInfoVo.getAgreementstatus());
			insbChannelagreementDao.insert(temp);
			baseInfoVo.setAgreementid(temp.getId());
			agreementid = temp.getId();
		}else{
			INSBChannelagreement tempagree = insbChannelagreementDao.selectById(baseInfoVo.getAgreementid());
			tempagree.setAgreementstatus(baseInfoVo.getAgreementstatus());
			insbChannelagreementDao.updateById(tempagree);
		}
		//应用地区存储
		INSBAgreementarea temparea = new INSBAgreementarea();
		temparea.setAgreementid(baseInfoVo.getAgreementid());
		List<INSBAgreementarea> list = insbAgreementareaDao.selectList(temparea);
		if(list!=null && list.size()>0){
			updateAreaInfo(list,baseInfoVo.getCitys(),baseInfoVo.getProvince(),baseInfoVo.getAgreementid());
		}else{
			saveNewCitys(baseInfoVo.getCitys(),baseInfoVo.getProvince(),baseInfoVo.getAgreementid());
		}
		return agreementid;
	}

	private void updateAreaInfo(List<INSBAgreementarea> list,List<String> citys,String province,String agreementid) {
		List<String> oldcitys = new ArrayList<String>();
		for (INSBAgreementarea temp : list) {
			//if(temp.getCity()!=null)
			oldcitys.add(temp.getCity());
		}
		//保存新选择的城市
		saveNewCitys((List<String>)CollectionUtils.subtract(citys, oldcitys),province,agreementid);
		//删除去除的城市
		List<String> removecitys =(List<String>) CollectionUtils.subtract(oldcitys, citys);
		for (INSBAgreementarea temp : list) {
			if(removecitys.contains(temp.getCity())){
				insbAgreementareaDao.deleteById(temp.getId());
			}
		}
	}
	
	private void saveNewCitys(List<String> citys,String province,String agreementid){
		if(citys!=null && citys.size()>0){
			for (String city : citys) {
				if(city!=null){
					INSBAgreementarea temp = new INSBAgreementarea();
					temp.setCity(city);
					temp.setId(UUIDUtils.create());
					temp.setCreatetime(new Date());
					temp.setProvince(province);
					temp.setAgreementid(agreementid);
					insbAgreementareaDao.insert(temp);
				}
			}
		}/*else{
			INSBAgreementarea temp = new INSBAgreementarea();
			temp.setId(UUIDUtils.create());
			temp.setCreatetime(new Date());
			temp.setProvince(province);
			temp.setAgreementid(agreementid);
			insbAgreementareaDao.insert(temp);
		}*/
	}

	@Override
	public List<InterfaceVo> getInterfaceInfo(String agreementid, InterfaceVo interfaceVo,
			int offset,int limit) {
		// 查询已选择的接口,将接口id作为map的key
		INSBAgreementinterface condition = new INSBAgreementinterface();
		condition.setChannelinnercode(interfaceVo.getChannelinnercode());
		List<INSBAgreementinterface> tempList = insbAgreementinterfaceDao.selectList(condition);
		//List<Map<String, Object>> tempList = insbAgreementinterfaceDao.getInterfaceInfo(agreementid);
		Map<String, Object> interCode = new HashMap<String, Object>();
		for (INSBAgreementinterface checkedInterface : tempList) {
			interCode.put(checkedInterface.getInterfaceid(), checkedInterface);
		}
		// 查出所有的接口,标记已选择的接口check属性为1,未选择为0
		List<InterfaceVo> allInterList = new ArrayList<InterfaceVo>();
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("offset", offset);
		params.put("limit", limit);
		for (INSBInterface insbInterface : insbInterfaceDao.selectAll(params)) {
			InterfaceVo temp = new InterfaceVo();
			temp.setInterfacename(insbInterface.getName());
			temp.setInterfaceid(insbInterface.getId());
			// 判断该接口是否被选中
			if (interCode.containsKey(insbInterface.getId())) {
				INSBAgreementinterface tempInterface = (INSBAgreementinterface) interCode.get(insbInterface.getId());
				temp.setCheck("1");
				temp.setCheckflag("开启");
				String isFree = tempInterface.getIsfree();
				temp.setIsfree(isFree);
				
				if ("1".equals(isFree)) {
					temp.setIsfreename("收费");
				} else {
					temp.setIsfreename("免费");
				}
				
				Integer monthfree = tempInterface.getMonthfree();
				if (monthfree == null) {
					temp.setMonthfree("");
				} else {
					temp.setMonthfree(String.valueOf(monthfree));
				}
				temp.setPv1(tempInterface.getPv1());
				temp.setPv2(tempInterface.getPv2());
				temp.setPv3(tempInterface.getPv3());
				temp.setPv4(tempInterface.getPv4());
				
//				temp.setPerfee(tempMap.get("perfee").toString());
				temp.setAgreementinterfaceid(tempInterface.getId());
			} else {
				temp.setCheck("0");
				temp.setCheckflag("关闭");
				temp.setIsfree("0");
			}
			allInterList.add(temp);
		}
		return allInterList;
	}

	@Override
	public Long getCountProvider(String channelid) {
		Long providerCount=insbChannelagreementDao.getqueryProviderCount(channelid);
		return providerCount;
	}
	
	@Override
	public Map<String, String> getDeptIdByChannelinnercodeAndPrvcode(String channelinnercode, String city) {
		List<Map<String, Object>> list = insbChannelagreementDao.getChannelDeptId(channelinnercode, city);
		if( list != null && list.size() > 0 ) {
			Map<String, String> map = new HashMap<String, String>();
			for(Map<String, Object> tmap : list){
				map.put((String) tmap.get("providerid"), (String) tmap.get("deptid"));
			}
			map.put("jobnum", (String) list.get(0).get("jobnum"));
			return map;
		}
		return null;
	}
	
	@Override
	public String getAgreementByArea(String channelinnercode, String prvcode, String city) {
		List<Map<String, Object>> list = insbChannelagreementDao.getAgreementByArea(channelinnercode, prvcode, city);
		if( list != null && list.size() > 0 ) {
			Map<String, Object> map = list.get(0);
			return (String) map.get("agreementid");
		}
		return "";
	}

	@Override
	public Map<String, Object> aglist(Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("total", insbChannelagreementDao.queryAgreementCount(map));
		List<Map<String, Object>> rows = insbChannelagreementDao.queryAgreement(map);
		
		for (Map<String, Object> row : rows) {
			Integer underwritestatus = (Integer)row.get("underwritestatus");
			String agreementstatus = String.valueOf(row.get("agreementstatus"));
			if (underwritestatus == 1) {
				row.put("underwritestatusname", "已生效");
			} else {
				row.put("underwritestatusname", "未生效");
			}
			if ("1".equals(agreementstatus)) {
				row.put("agreementstatusname", "已生效");
			} else {
				row.put("agreementstatusname", "未生效");
			}
		}
		
		resultMap.put("rows", rows); 
		return resultMap;
	}
	
	@Override
	public Map<String, Object> agchnlist(Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> rows = insbChannelagreementDao.queryAgreementChn(map);
		resultMap.put("rows", rows); 
		return resultMap;
	}

	@Override
	public String delchn(String ids) {
		String[] idArr = ids.split(",");
		for (String id : idArr) {
			INSBAgreementprovider ap = insbAgreementproviderDao.selectById(id);
			String agreementid = ap.getAgreementid();
			String prvid = ap.getProviderid();
			
			INSBAgreementpaymethod payDelCon = new INSBAgreementpaymethod();
			payDelCon.setAgreementid(agreementid);
			payDelCon.setProviderid(prvid);
			agreementpaymethodDao.delete(payDelCon);
			
			INSBAgreementdept deptCon = new INSBAgreementdept();
			deptCon.setAgreementid(agreementid); 
			deptCon.setProviderid(prvid); 
			agreementdeptDao.delete(deptCon);
			
			insbAgreementproviderDao.deleteById(id);
		}
		return "success";
	}

	@Override
	public String addchn(String operator, String channelIds, String agreeId, String deptId, String payIds) {
		INSBAgreement insbAgreement = insbAgreementDao.selectById(agreeId);
		//String agreeDeptId = insbAgreement.getDeptid();
		
		String[] channelIdArr = channelIds.split(",");
		for (String channelId : channelIdArr) {
			//INSBChannel insbChannel = insbChannelDao.selectById(channelId);
			//if (!agreeDeptId.equals(insbChannel.getDeptid())) {
			//	return insbChannel.getChannelname() + "：不应跨平台增加协议";
			//}
			
			List<INSBChannelagreement> insbChannelagreements = insbChannelagreementDao.getChannelagreement(channelId);
			
			if (insbChannelagreements.size() >= 0 ) {
				String agreementid = insbChannelagreements.get(0).getId();
				
				INSBAgreementprovider apCon = new INSBAgreementprovider();
				apCon.setAgreementid(agreementid);
				apCon.setAgreeid(agreeId);
				List<INSBAgreementprovider> aps = insbAgreementproviderDao.selectList(apCon);
				
				if ( aps.isEmpty() ) {
					INSBAgreementpaymethod payDelCon = new INSBAgreementpaymethod();
					payDelCon.setAgreementid(agreementid);
					payDelCon.setProviderid(insbAgreement.getProviderid());
					agreementpaymethodDao.delete(payDelCon);
					
					String[] payIdArr = payIds.split(",");
					for (String payId : payIdArr) {
						INSBAgreementpaymethod pay = new INSBAgreementpaymethod();
						pay.setAgreementid(agreementid);
						pay.setCreatetime(new Date());
						pay.setOperator(operator);
						pay.setPaychannelid(payId);
						pay.setProviderid(insbAgreement.getProviderid());
						agreementpaymethodDao.insert(pay); 
					}
					
					INSBAgreementdept deptCon = new INSBAgreementdept();
					deptCon.setAgreementid(agreementid);
					deptCon.setProviderid(insbAgreement.getProviderid()); 
					agreementdeptDao.delete(deptCon);
					
					INSCDept inscDept = inscDeptService.queryById(deptId);
					String[] parentcodes = inscDept.getParentcodes().split("[+]");
					INSBAgreementdept dept = new INSBAgreementdept();
					dept.setAgreementid(agreementid);
					dept.setCreatetime(new Date());
					dept.setDeptid1(parentcodes[2]);
					dept.setDeptid2(parentcodes[3]);
					dept.setDeptid3(parentcodes[4]);
					dept.setDeptid4(parentcodes[5]);
					dept.setDeptid5(deptId);
					dept.setOperator(operator);
					dept.setProviderid(insbAgreement.getProviderid());
					agreementdeptDao.insert(dept); 
					
					INSBAgreementprovider apData = new INSBAgreementprovider();
					apData.setAgreeid(agreeId);
					apData.setAgreementid(agreementid);
					apData.setCreatetime(new Date());
					apData.setModifytime(null);
					apData.setOperator(operator);
					apData.setProviderid(insbAgreement.getProviderid()); 
					insbAgreementproviderDao.insert(apData); 
				}
			}
		}
		return "success";
	}

	@Override
	public String swagchn(String operator, String channelIds, String agreeId, String toAgreeId, String payIds,
			String deptId) {
		INSBAgreement insbAgreement = insbAgreementDao.selectById(agreeId);
		INSBAgreement insbAgreementTo = insbAgreementDao.selectById(toAgreeId);
		
		/* String deptIdFr = insbAgreement.getDeptid();
		String deptIdTo = insbAgreementTo.getDeptid();
		if ( !deptIdFr.equals(deptIdTo) ) {
			return "协议不应跨平台切换";
		}
		String prvId = insbAgreement.getProviderid();
		String prvIdTo = insbAgreementTo.getProviderid();
		if ( !prvId.equals(prvIdTo) ) {
			return "不同供应商之间的协议不能相互切换";
		} */
		String agcode = insbAgreement.getAgreementcode();
		String agcodeTo = insbAgreementTo.getAgreementcode();
		if ( agcode.equals(agcodeTo) ) {
			return "同一个协议不能切换";
		}
		
		String[] channelIdArr = channelIds.split(",");
		for (String channelId : channelIdArr) {
			List<INSBChannelagreement> insbChannelagreements = insbChannelagreementDao.getChannelagreement(channelId);
			if (insbChannelagreements.isEmpty()) continue;
			String agreementid = insbChannelagreements.get(0).getId();
			
			INSBAgreementprovider apCon = new INSBAgreementprovider();
			apCon.setAgreementid(agreementid);
			apCon.setAgreeid(agreeId);
			List<INSBAgreementprovider> aps = insbAgreementproviderDao.selectList(apCon);
			if ( aps.isEmpty() ) continue;
			
			INSBAgreementprovider apData = aps.get(0); 
			apData.setAgreeid(toAgreeId);
			apData.setModifytime(new Date());
			apData.setOperator(operator);
			apData.setProviderid(insbAgreementTo.getProviderid()); 
			insbAgreementproviderDao.updateById(apData);
			
			INSBAgreementpaymethod payDelCon = new INSBAgreementpaymethod();
			payDelCon.setAgreementid(agreementid);
			payDelCon.setProviderid(insbAgreement.getProviderid());
			agreementpaymethodDao.delete(payDelCon);
			
			payDelCon = new INSBAgreementpaymethod();
			payDelCon.setAgreementid(agreementid);
			payDelCon.setProviderid(insbAgreementTo.getProviderid());
			agreementpaymethodDao.delete(payDelCon);
			
			if ( StringUtil.isNotEmpty(payIds) ) {
				String[] payIdArr = payIds.split(",");
				for (String payId : payIdArr) {
					INSBAgreementpaymethod pay = new INSBAgreementpaymethod();
					pay.setAgreementid(agreementid);
					pay.setCreatetime(new Date());
					pay.setOperator(operator);
					pay.setPaychannelid(payId);
					pay.setProviderid(insbAgreementTo.getProviderid());
					agreementpaymethodDao.insert(pay); 
				}
			}
			
			INSBAgreementdept deptCon = new INSBAgreementdept();
			deptCon.setAgreementid(agreementid);
			deptCon.setProviderid(insbAgreement.getProviderid()); 
			agreementdeptDao.delete(deptCon);
			
			deptCon = new INSBAgreementdept();
			deptCon.setAgreementid(agreementid);
			deptCon.setProviderid(insbAgreementTo.getProviderid()); 
			agreementdeptDao.delete(deptCon);
			
			if ( StringUtil.isNotEmpty(deptId) ) {
				INSCDept inscDept = inscDeptService.queryById(deptId);
				String[] parentcodes = inscDept.getParentcodes().split("[+]");
				INSBAgreementdept dept = new INSBAgreementdept();
				dept.setAgreementid(agreementid);
				dept.setCreatetime(new Date());
				dept.setDeptid1(parentcodes[2]);
				dept.setDeptid2(parentcodes[3]);
				dept.setDeptid3(parentcodes[4]);
				dept.setDeptid4(parentcodes[5]);
				dept.setDeptid5(deptId);
				dept.setOperator(operator);
				dept.setProviderid(insbAgreementTo.getProviderid());
				agreementdeptDao.insert(dept); 
			}
		}
		return "success";
	}

	@Override
	public Map<String, Object> agdeptlist(Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		INSBOutorderdept insbOutorderdept = new INSBOutorderdept();
		insbOutorderdept.setAgreementid(map.get("agreeid") + "");
		List<INSBOutorderdept> allDeptlist = outorderdeptService.queryList(insbOutorderdept);
		
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		for (INSBOutorderdept outorderdept : allDeptlist) {
			Map<String, Object> mapData = new HashMap<String, Object>();
			
			INSCDept inscDept = inscDeptService.queryById(outorderdept.getDeptid5());
			mapData.put("deptid5", outorderdept.getDeptid5());
			mapData.put("d5name", inscDept.getComname());
			
			String[] parentcodes = inscDept.getParentcodes().split("[+]");
			inscDept = inscDeptService.queryById(parentcodes[2]);
			mapData.put("d1name", inscDept.getComname());
			inscDept = inscDeptService.queryById(parentcodes[3]);
			mapData.put("d2name", inscDept.getComname());
			inscDept = inscDeptService.queryById(parentcodes[4]);
			mapData.put("d3name", inscDept.getComname());
			inscDept = inscDeptService.queryById(parentcodes[5]);
			mapData.put("d4name", inscDept.getComname());
			
			rows.add(mapData);
		}
		
		resultMap.put("rows", rows); 
		return resultMap;
	}
	
}
