package com.zzb.mobile.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBOrderdelivery;

public interface INSBDeliveryMethodService extends BaseService<INSBOrderdelivery>{
	
	public String showDeliveryInfo(String orderno,String channelId,String channelUserId);
	/*
	 * 显示配送的两种方式的信息：自取以及快递，这两种方式的全部信息都放在一个接口中显示给界面
	 */

	public String addNewAddress(String processInstanceId,String agentnum ,String name, String phone,
			String province,String city,String area, String detailAddress, String zipCode ,String channelId ,String channelUserId);
	/*
	 * 添加新的快递配送地址，数据：
	 * 收件人姓名
	 * 联系电话
	 * 省市区三个字段
	 * 详细地址
	 * 邮编
	 */
	
	public String saveOrderDelivery(String addressId,String agentnum,String orderno,String delivetytype);
	/*
	 * id为 保存地址的id，deliveryaddress 中的id 
	 */
	
	public String editAddress(String addressId,String agentnum,String name, String phone,
			String province,String city,String area , String detailAddress, String zipCode,String orderno);
	/*
	 * 修改配送地址
	 * addressId,deliveryaddress表中存放的地址编号
	 */
	
	public String deleteAddress(String addressId);
	/*
	 * 删除配送地址
	 */
}
