package com.zzb.mobile.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.service.INSCDeptService;
import com.zzb.cm.dao.INSBCarinfoDao;
import com.zzb.cm.dao.INSBPersonDao;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBPerson;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.cm.service.INSBPersonService;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBUsercomment;
import com.zzb.conf.service.INSBUsercommentService;
import com.zzb.mobile.model.AppInsuredMyPolicyModel;
import com.zzb.mobile.model.AppInsuredMycustomerModel;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.CustomDetailModel;
import com.zzb.mobile.model.CustomInfoModel;
import com.zzb.mobile.model.PersonalInfoModel;
import com.zzb.mobile.service.AppInsuredMyService;

@Service
@Transactional
public class AppInsuredMyServiceImpl implements AppInsuredMyService {
    @Resource
	private INSBPolicyitemDao insbPolicyitemDao;
    @Resource
    private INSBCarinfoDao iNSBCarinfoDao;
    @Resource
    private INSBAgentDao iNSBAgentDao;
    @Resource
    private INSBPersonService personService;
    @Resource
    private INSCDeptDao iNSCDeptDao; 
    @Resource
    private INSCDeptService iNSCDeptService;
    @Resource
    private INSBPersonDao iNSBPersonDao;
    @Resource
    private INSBQuotetotalinfoDao iNSBQuotetotalinfoDao;
    @Resource
    private INSBUsercommentService insbUsercommentService;
   
    @Override
    public CommonModel delMyPolicies(List<String> ids){
    	CommonModel model=new CommonModel();
    	for (String id : ids) {
    		INSBCarinfo car=iNSBCarinfoDao.selectById(id);
    		Map<String,Object> map=new HashMap<String,Object>();
    		map.put("taskid", car.getTaskid());
    		map.put("insurancebooks", "1");
    		INSBQuotetotalinfo info=iNSBQuotetotalinfoDao.select(map);
    		if(info!=null){
    			info.setId(info.getId());
    			info.setInsurancebooks("0");
    			info.setModifytime(new Date());
    			iNSBQuotetotalinfoDao.updateById(info);
    		}
		}
    	model.setMessage("删除成功");    	
		model.setStatus("success");
    	return model;
    }
    
    
	@Override
	public CommonModel getMyPolicies(String jobnum,String keyword,int offset,int limit) {
		CommonModel model=new CommonModel();
		if(StringUtil.isEmpty(jobnum)){
			model.setMessage("代理人工号不能为空");
			model.setStatus("fail");
			return model;
		}
		HashMap<String,Object> reqmap =null;
		AppInsuredMyPolicyModel rmodel=null;
		List<AppInsuredMyPolicyModel> list=new ArrayList<AppInsuredMyPolicyModel>();
		if(!StringUtil.isEmpty(keyword)){  //关键字不为空查询
			reqmap =new HashMap<String,Object>();
			reqmap.put("agentnum", jobnum);
			reqmap.put("insurancebooks", "1");   //insbquotetotalinfo中是否保存投保书    0无投保书  1保存投保书
			reqmap.put("keyword", keyword);
			reqmap.put("offset", offset);
			reqmap.put("limit", limit);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("agentnum", jobnum);
			map.put("insurancebooks", "1");
			map.put("keyword", keyword);
			List<Map<String,Object>> policyitems = insbPolicyitemDao.getPolicyitems(map);
			//reqmap.put("count",policyitems.size());
			List<Map<String,Object>> policyitemList = insbPolicyitemDao.getPolicyitems(reqmap);
			if(policyitemList!=null&&policyitemList.size()>0){
				for(Map<String,Object> m:policyitemList){
					rmodel=new AppInsuredMyPolicyModel();
					rmodel.setId((String)m.get("id"));
					rmodel.setCarlicenseno((String)m.get("carlicenseno"));
					rmodel.setOwnername((String)m.get("ownername"));
					rmodel.setCreatetime((String)m.get("createtime"));
					rmodel.setWebpagekey((String)m.get("webpagekey"));
					rmodel.setProcessinstanceid((String)m.get("processinstanceid"));
					list.add(rmodel);
				}
			}
			reqmap = new HashMap<String,Object>();
			reqmap.put("list",list);
			reqmap.put("totalecount", policyitems.size());
			model.setBody(reqmap);
			model.setMessage("查询成功");
			model.setStatus("success");
		}else{     //关键字为空查询
			reqmap =new HashMap<String,Object>();
			reqmap.put("agentnum", jobnum);
			reqmap.put("insurancebooks", "1");
			reqmap.put("offset", offset);
			reqmap.put("limit", limit);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("agentnum", jobnum);
			map.put("insurancebooks", "1");
			List<Map<String,Object>> policyitems = insbPolicyitemDao.getPolicyitems(map);
			List<Map<String,Object>> policyitemList = insbPolicyitemDao.getPolicyitems(reqmap);
			if(policyitemList!=null&&policyitemList.size()>0){
				for(Map<String,Object> m:policyitemList){
					rmodel=new AppInsuredMyPolicyModel();
					rmodel.setId((String)m.get("id"));
					rmodel.setCarlicenseno((String)m.get("carlicenseno"));
					rmodel.setOwnername((String)m.get("ownername"));
					rmodel.setCreatetime((String)m.get("createtime"));
					rmodel.setWebpagekey((String)m.get("webpagekey"));
					rmodel.setProcessinstanceid((String)m.get("processinstanceid"));
					list.add(rmodel);
				}
			}
			reqmap = new HashMap<String,Object>();
			reqmap.put("list", list);
			reqmap.put("totalecount",policyitems.size());
			model.setBody(reqmap);
			model.setMessage("查询成功");
			model.setStatus("success");
					
		}

		return model;		
	}
	@Override
	public CommonModel getMyPoliciesByMinizzb(String channeluserid,String keyword,int offset,int limit) {
		CommonModel model=new CommonModel();
		HashMap<String,Object> reqmap =null;
		AppInsuredMyPolicyModel rmodel=null;
		List<AppInsuredMyPolicyModel> list=new ArrayList<AppInsuredMyPolicyModel>();
		if(!StringUtil.isEmpty(keyword)){  //关键字不为空查询
			reqmap =new HashMap<String,Object>();
//			reqmap.put("agentnum", jobnum);
			reqmap.put("agentid", channeluserid);
			reqmap.put("insurancebooks", "1");   //insbquotetotalinfo中是否保存投保书    0无投保书  1保存投保书
			reqmap.put("keyword", keyword);
			reqmap.put("offset", offset);
			reqmap.put("limit", limit);
			Map<String,Object> map = new HashMap<String,Object>();
//			map.put("agentnum", jobnum);
			map.put("agentid", channeluserid);
			map.put("insurancebooks", "1");
			map.put("keyword", keyword);
//			List<Map<String,Object>> policyitems = insbPolicyitemDao.getPolicyitems(map);
			List<Map<String,Object>> policyitems = insbPolicyitemDao.getPolicyitemsForMinizzb(map);
			//reqmap.put("count",policyitems.size());
//			List<Map<String,Object>> policyitemList = insbPolicyitemDao.getPolicyitems(reqmap);
			List<Map<String,Object>> policyitemList = insbPolicyitemDao.getPolicyitemsForMinizzb(reqmap);
			if(policyitemList!=null&&policyitemList.size()>0){
				for(Map<String,Object> m:policyitemList){
					rmodel=new AppInsuredMyPolicyModel();
					rmodel.setId((String)m.get("id"));
					rmodel.setCarlicenseno((String)m.get("carlicenseno"));
					rmodel.setOwnername((String)m.get("ownername"));
					rmodel.setCreatetime((String)m.get("createtime"));
					rmodel.setWebpagekey((String)m.get("webpagekey"));
					rmodel.setProcessinstanceid((String)m.get("processinstanceid"));
					list.add(rmodel);
				}
			}
			reqmap = new HashMap<String,Object>();
			reqmap.put("list",list);
			reqmap.put("totalecount", policyitems.size());
			model.setBody(reqmap);
			model.setMessage("查询成功");
			model.setStatus("success");
		}else{     //关键字为空查询
			reqmap =new HashMap<String,Object>();
//			reqmap.put("agentnum", jobnum);
			reqmap.put("agentid", channeluserid);
			reqmap.put("insurancebooks", "1");
			reqmap.put("offset", offset);
			reqmap.put("limit", limit);
			Map<String,Object> map = new HashMap<String,Object>();
//			map.put("agentnum", jobnum);
			map.put("agentid", channeluserid);
			map.put("insurancebooks", "1");
//			List<Map<String,Object>> policyitems = insbPolicyitemDao.getPolicyitems(map);
//			List<Map<String,Object>> policyitemList = insbPolicyitemDao.getPolicyitems(reqmap);
			List<Map<String,Object>> policyitems = insbPolicyitemDao.getPolicyitemsForMinizzb(map);
			List<Map<String,Object>> policyitemList = insbPolicyitemDao.getPolicyitemsForMinizzb(reqmap);
			if(policyitemList!=null&&policyitemList.size()>0){
				for(Map<String,Object> m:policyitemList){
					rmodel=new AppInsuredMyPolicyModel();
					rmodel.setId((String)m.get("id"));
					rmodel.setCarlicenseno((String)m.get("carlicenseno"));
					rmodel.setOwnername((String)m.get("ownername"));
					rmodel.setCreatetime((String)m.get("createtime"));
					rmodel.setWebpagekey((String)m.get("webpagekey"));
					rmodel.setProcessinstanceid((String)m.get("processinstanceid"));
					list.add(rmodel);
				}
			}
			reqmap = new HashMap<String,Object>();
			reqmap.put("list", list);
			reqmap.put("totalecount",policyitems.size());
			model.setBody(reqmap);
			model.setMessage("查询成功");
			model.setStatus("success");

		}

		return model;
	}
		

	@Override
	public CommonModel getMyCustomers(String jobNum, String custmerName) {
		CommonModel retModel = new CommonModel();
		if(StringUtil.isEmpty(jobNum)){
			retModel.setStatus("fail");
			retModel.setMessage("代理人工号不能为空");
			return retModel;
		}
		Map<String, Object> Map = new HashMap<String, Object>();
		CustomInfoModel cmodel = new CustomInfoModel();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<CustomInfoModel> lis = new ArrayList<CustomInfoModel>();
		if (!StringUtil.isEmpty(custmerName)) {
			Map.put("agentnum", jobNum);
			Map.put("applicantname", custmerName);
			List<AppInsuredMycustomerModel> list = insbPolicyitemDao.queryCustomersInfo(Map);
			if (list.size() > 0) {
				/** 
				 * 对查询结果用Vincode和Idcardno过滤
				 */
				Map<String, String> map = new HashMap<String, String>();
				int number = 0;
				for (AppInsuredMycustomerModel info : list) {
					if (info == null) {
						continue;
					}
					if (!StringUtil.isEmpty(info.getVincode())
							&& !StringUtil.isEmpty(info.getIdcardno())) {
						if (map.containsKey(info.getVincode() + "_"+ info.getIdcardno())) {
							continue;
						} else {
							CustomInfoModel model = this.listContain(lis,info.getIdcardno());
							map.put(info.getVincode() + "_"+ info.getIdcardno(), "vincode+Idcardno");
							if (model == null) {
								number = number + 1;
								cmodel = new CustomInfoModel();
								cmodel.setCarNum(number);
								cmodel.setCarPate(info.getCarPate());
								cmodel.setCarModel(info.getCarModel());
								cmodel.setCustomerName(info.getApplicantname());
								cmodel.setCustomerIdCardTypeName(info.getIdcardtype());
								cmodel.setCustomerIdCardType(info.getIdcardcode());
								cmodel.setCustomerIdCardNo(info.getIdcardno());
								cmodel.setAddress(info.getAddress());
								cmodel.setAge(info.getAge());
								try {
									cmodel.setBirthday(sdf.format(info
											.getBirthday()));
								} catch (Exception e) {
								}
								cmodel.setCellphone(info.getCellphone());
								cmodel.setEmail(info.getEmail());
								cmodel.setGender(info.getGender());
								lis.add(cmodel);
							}

						}
					}
				}
			}

		} else {
			Map.put("agentnum", jobNum);
			List<AppInsuredMycustomerModel> list = insbPolicyitemDao.queryCustomersInfo(Map);
			Map<String, String> map = new HashMap<String, String>();
			if (list.size() > 0) {
				for (AppInsuredMycustomerModel info : list) {
					if (info == null) {
						continue;
					}
					if (!StringUtil.isEmpty(info.getVincode())&& !StringUtil.isEmpty(info.getIdcardno())) {
						int number = 0;
						if (map.containsKey(info.getVincode() + "_"+ info.getIdcardno())) {
							continue;
						} else {
							CustomInfoModel model = this.listContain(lis,info.getIdcardno());
							map.put(info.getVincode() + "_"+ info.getIdcardno(), "vincode+Idcardno");
							if (model == null) {
								number = number + 1;
								cmodel = new CustomInfoModel();
								cmodel.setCarNum(number);
								cmodel.setCarPate(info.getCarPate());
								cmodel.setCarModel(info.getCarModel());
								cmodel.setCustomerName(info.getApplicantname());
								cmodel.setCustomerIdCardTypeName(info.getIdcardtype());
								cmodel.setCustomerIdCardType(info.getIdcardcode());
								cmodel.setCustomerIdCardNo(info.getIdcardno());
								cmodel.setAddress(info.getAddress());
								cmodel.setAge(info.getAge());
								try {
									cmodel.setBirthday(sdf.format(info.getBirthday()));
								} catch (Exception e) {
								}
								cmodel.setCellphone(info.getCellphone());
								cmodel.setEmail(info.getEmail());
								cmodel.setGender(info.getGender());
								lis.add(cmodel);
							}

						}
					}
				}
			}
		}
		retModel.setStatus("success");
		retModel.setMessage("客户信息查找成功");
		retModel.setBody(lis);

		return retModel;
	}
	public CustomInfoModel listContain(List<CustomInfoModel> list,String user ){
		for(CustomInfoModel c :list){
			if(c.getCustomerIdCardNo().equals(user)){
				c.setCarNum(c.getCarNum()+1);
				return c;
			}
		}
		return null;
	}
	@Override
	public PersonalInfoModel getMyPersonalInfo(String jobNum) {
		PersonalInfoModel model=new PersonalInfoModel();
		INSBAgent agent=new INSBAgent();
		agent.setJobnum(jobNum);
		
		INSBAgent agent1=iNSBAgentDao.selectByJobnum(jobNum);
		if(agent1!=null){			
			INSCDept dep1=iNSCDeptDao.selectById(agent1.getDeptid());
			model.setJobnum(agent1.getJobnum());
			model.setPhone(agent1.getPhone());
			model.setName(agent1.getName());
			model.setApprovesstate(agent1.getApprovesstate());  //代理人认证状态			
			model.setSex(agent1.getSex());
			model.setIdno(agent1.getIdno());
			model.setCgfns(agent1.getCgfns());
			INSCDept legalPersonDept = iNSCDeptService.getLegalPersonDept(agent1.getDeptid());
			INSCDept dept = iNSCDeptService.queryById(legalPersonDept.getUpcomcode());
			model.setBranch(dept.getComname());
			model.setDeptName(dep1.getComname());
			model.setJobNumType(agent1.getAgentkind());
			// LINING bug-2913-认证任务选择退回时没有地方填写给前端用户的备注 20160707 START
			model.setCommentcontent("");
			INSBUsercomment insbUsercomment = insbUsercommentService.selectUserCommentByTrackid(agent1.getId(), 9);
			if( insbUsercomment != null && StringUtil.isNotEmpty(insbUsercomment.getCommentcontent()) ) {
				model.setCommentcontent(insbUsercomment.getCommentcontent());
			}
			// LINING bug-2913-认证任务选择退回时没有地方填写给前端用户的备注 20160707 END
		}
		
		return model;
	}

	@Override
	public CommonModel getMyCustomerDetail(String jobNum,int idCardType, String idCardNo,String applicantname) {
		CommonModel commonModel = new CommonModel();
		CustomDetailModel cmodel =new CustomDetailModel();
		//根据类型和身份证号码去insbperson表中按照创建时间降序，获取第一条记录把信息封装到CustomDetailModel返回
		INSBPerson insbPerson = new INSBPerson();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("idCardType", idCardType);
		map.put("idCardNo", idCardNo);
		map.put("jobNum", jobNum);
		map.put("applicantname", applicantname);
		List<INSBPerson> personList = iNSBPersonDao.selectByAgent(map);
		insbPerson=personList.get(0);
		cmodel.setAddress(insbPerson.getAddress());
		cmodel.setId(insbPerson.getId());
		if(insbPerson.getBirthday()!=null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			cmodel.setBirthday(sdf.format(insbPerson.getBirthday()));
		}
		cmodel.setEmail(insbPerson.getEmail());
		cmodel.setIdCardNo(insbPerson.getIdcardno());
		cmodel.setIdCardType(insbPerson.getIdcardtype());
		cmodel.setMobile(insbPerson.getCellphone());
		cmodel.setName(insbPerson.getName());
		cmodel.setNoti(insbPerson.getNoti());
		cmodel.setSex(insbPerson.getGender()+"");

		commonModel.setStatus("success");
		commonModel.setMessage("获取客户详情成功");
		commonModel.setBody(cmodel);
		return commonModel;
	}
	@Override
	public CommonModel getMyCustomerCarInfo(String jobNum, int idCardType,
			String idCardNo,String applicantname) {
		CommonModel commonModel = new CommonModel();
		//根据类型和身份证号码去insbperson表中查找所有的taskid;
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("jobNum", jobNum);
		map.put("idCardType", idCardType);
		map.put("idCardNo", idCardNo);
		//map.put("applicantname", applicantname);
		List<Map<Object,Object>> selectList = iNSBCarinfoDao.getCarInfoByPerson(map);
		
		//获取车辆list<CarInfoModel>返回
		Map<String,Object> vincodeMap =new HashMap<String,Object>();
		List<Map> carInfoList =new ArrayList<Map>();
		for(Map tempMap:selectList){
			if(vincodeMap.containsKey((tempMap.get("vincode"))))
					continue;
			vincodeMap.put(tempMap.get("vincode")+"", tempMap.get("vincode"));
			carInfoList.add(tempMap);
		}
		commonModel.setStatus("success");
		commonModel.setMessage("获取客户车辆信息成功");
		commonModel.setBody(carInfoList);
		return commonModel;
	}


	@Override
	public CommonModel getMyCustomerPolicyInfos(String jobNum, int idCardType,
			String idCardNo,int offset,int limit) {
		CommonModel model =new CommonModel();
		Map<String, Object> Params=new HashMap<String, Object>();
		Params.put("agentnum", jobNum);
		Params.put("idcardtype", idCardType);
		Params.put("idcardno", idCardNo);
		Params.put("offset", offset);
		Params.put("limit", limit);
		List<Map<String, Object>> querylist=insbPolicyitemDao.queryPolicyInfobyPerson(Params);
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		Map map=new HashMap();
		if(querylist.size()>0){
			for(Map<String, Object> query:querylist){
				if(query.get("policyno")!=null){
					if(map.containsKey(query.get("policyno")+"_"+query.get("taskid"))){
						continue;
					}
					map.put(query.get("policyno")+"_"+query.get("taskid"), "qqq");	
					list.add(query);
					
				}
			}
		}
		model.setMessage("查询成功");
		model.setStatus("success");
		model.setBody(list);
		return model;
	}


	@Override
	public CommonModel EditMyCustomerInfos(JSONObject jsonparams) {
		
		JSONObject params=JSONObject.fromObject(jsonparams);
		CommonModel model=new CommonModel();
		INSBPerson person=new INSBPerson();
		person.setName((String)params.get("name"));
		person.setIdcardtype((int)params.get("idCardType"));
		person.setIdcardno((String)params.get("idCardNo"));
		 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		   try {
			person.setBirthday(sdf.parse((String) params.get("birthday")));
		} catch (ParseException e) {
			
			e.printStackTrace();
		}

		person.setEmail((String)params.get("email"));
		person.setAddress((String)params.get("address"));
		person.setGender((int)params.get("sex"));
		person.setNoti((String)params.get("noti"));
		person.setCellphone((String)params.get("mobile"));
		Date date=new Date();
		person.setId((String)params.get("id"));
		person.setTaskid((String) params.get("taskid"));
		person.setOperator((String) params.get("operator"));
		person.setModifytime(date);
		iNSBPersonDao.updateById(person);
		model.setStatus("success");
		model.setMessage("保存成功");
		return model;
	}


	@Override
	public CommonModel delMyCustomerInfos(String id) {
		CommonModel model=new CommonModel();
		//iNSBPersonDao.deletebyID(id);
		model.setStatus("success");
		model.setMessage("删除成功");      	
		return model;
	
	}

	
}
