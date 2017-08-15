package com.zzb.mobile.service;

import java.util.List;

import net.sf.json.JSONObject;

import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.PersonalInfoModel;

public interface AppInsuredMyService {
	
	/**
	 * 删除我的投保书
	 * @param agentid
	 * @return
	 */
	public CommonModel delMyPolicies(List<String> ids);

	/**
	 * 获取我的投保书
	 * @param agentid
	 * @return
	 */
	public CommonModel getMyPolicies(String jobNum,String keyword,int offset,int limit);
	/**
	 * 获取我的投保书
	 * @param agentid
	 * @return
	 */
	public CommonModel getMyPoliciesByMinizzb(String taskid,String keyword,int offset,int limit);
	/**
	 * 获取我的客户
	 * @param agentid -代理人id
	 * @param customerName -客户名称
	 * @return
	 * {
	 * 客户基本信息：{customerName:李四，customerNo:1212,mobile:13800000000,idCard:111111111111111111,address:北京市xxx,sex:男,birthday:11111111,description:新客户},
	 * 车辆信息：{{plateNumber：京A11111，engineNo：LSG1212，ChassisNo：12132312，carCrop：奥迪,carSeries：A4L，carType：2013A4L,amount:280000},
	 * {}
	 * }
	 */
	public CommonModel getMyCustomers(String agentid,String customerName);
	/**
	 * 获取代理人个人信息
	 * @param agentid -代理人id
	 * @return
	 * "{workNo:1234，mobile:13800000000,status：0
	 * ，realName:张三，sex：男，idcard:1111111111111,agentQualificationNo:12312312,
	 * teamName:xxx,branchName:xxxx,platform：广东南风平台}
	 */
	public PersonalInfoModel getMyPersonalInfo(String agentid);
	
	/**
	 * 获取客户详细信息
	 * @param idCardType -证件类型
	 * @param idCardNo   -证件号码
	 * @return
	 */
	public CommonModel getMyCustomerDetail(String jobNum,int idCardType, String idCardNo,String applicantname);
	/**
	 * 获取客户车辆信息
	 * @param jobNum -工号
	 * @param idCardType -证件类型
	 * @param idCardNo   -证件号码
	 * @return
	 */
	public CommonModel getMyCustomerCarInfo(String jobNum,int idCardType,String idCardNo,String applicantname);
	
	/**
	 * 获取客户保单信息
	 * @param jobNum
	 * @param idCardType
	 * @param idCardNo
	 * @return
	 */
	public CommonModel getMyCustomerPolicyInfos(String jobNum,int idCardType,String idCardNo,int offset,int limit);
	/**
	 * 客户信息编辑
	 * @param json
	 * @return
	 */
	public CommonModel EditMyCustomerInfos(JSONObject jsonparams);
	/**
	 * 删除客户信息
	 * @param json
	 * @return
	 */
	public CommonModel delMyCustomerInfos(String id);
}
