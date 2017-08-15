package com.zzb.mobile.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cninsure.core.utils.StringUtil;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.service.INSCDeptService;
import com.zzb.cm.dao.INSBQuoteinfoDao;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.entity.INSBCarowneinfo;
import com.zzb.cm.entity.INSBDeliveryaddress;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.entity.INSBPerson;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.cm.service.INSBCarowneinfoService;
import com.zzb.cm.service.INSBDeliveryaddressService;
import com.zzb.cm.service.INSBOrderService;
import com.zzb.cm.service.INSBPersonService;
import com.zzb.conf.dao.INSBDistributiontypeDao;
import com.zzb.conf.entity.INSBDistributiontype;
import com.zzb.conf.entity.INSBOrderdelivery;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.service.INSBOrderdeliveryService;
import com.zzb.conf.service.INSBProviderService;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.service.INSBDeliveryMethodService;

@Service
public class INSBDeliveryMethodServiceImpl extends
		BaseServiceImpl<INSBOrderdelivery> implements INSBDeliveryMethodService {

	@Resource
	private INSBOrderdeliveryService insbOrderdeliveryService;
	@Resource
	private INSCDeptService insbDeptService;
	@Resource
	private INSBCarowneinfoService insbCarowneinfoService;
	@Resource
	private INSBPersonService insbPersonService;
	@Resource
	private INSBDeliveryaddressService insbDeliveryaddressService;
	@Resource
	private INSBOrderService insbOrderService;
	@Resource
	private INSBProviderService insbProviderService;
	@Resource
	private INSBQuotetotalinfoDao insbQuotetotalinfoDao;
	@Resource
	private INSBQuoteinfoDao insbQuoteinfoDao;
	@Resource
	private INSBDistributiontypeDao insbDistributiontypeDao;
	/*
	 * 获取配送方式的信息，包括自取以及快递的方式
	 * 参数,订单 id  orderno  是order表中的id 字段
	 * 渠道跨区出单,增加渠道用户ID和渠道ID 20170526 hewc
	 */
	@Override
	public String showDeliveryInfo(String orderno,String channelId,String channelUserId) {
		Map<String,Object> body = new HashMap<String,Object>();
		Map<String,Object> result = new HashMap<String,Object>();
		Map<String,Object> byself = new HashMap<String,Object>();
		List<Map<String,Object>> express = new ArrayList<Map<String,Object>>();
		
		//根据订单表获取出单网点，及地址
		try{
			INSBOrder order = new INSBOrder();
			order.setOrderno(orderno);
			order = insbOrderService.queryOne(order);
			//查找到机构表
			INSCDept inscdept = new INSCDept();
			inscdept.setComcode(order.getDeptcode());
			inscdept = insbDeptService.queryOne(inscdept);
			//组织写数据
			byself.put("byselfaddress", inscdept==null? "":inscdept.getAddress());  //自取网点地址
			byself.put("deptname", inscdept==null? "":inscdept.getComname());       //网点名称
			byself.put("deptcode", inscdept==null? "":inscdept.getComcode());       //网点code
			
			String processInstanceId= order.getTaskid();

			//获得中间表INSBCarowneinfo中的personid
			//INSBCarowneinfo carowneinfo = new INSBCarowneinfo();
			//carowneinfo.setTaskid(order.getTaskid());
			//carowneinfo = insbCarowneinfoService.queryOne(carowneinfo);
			//INSBPerson person = new INSBPerson();
			//person.setId(carowneinfo.getPersonid());
			//person = insbPersonService.queryById(person.getId());//找到车主，获得车主的信息

			String operator = order.getAgentcode();
			if (StringUtil.isEmpty(operator)) {
				operator = order.getInputusercode();
			}

			if (StringUtil.isNotEmpty(operator)) {
				INSBDeliveryaddress deliveryaddress = new INSBDeliveryaddress();
				//deliveryaddress.setOwnername(person.getName());         //车主姓名
				//deliveryaddress.setOwneridcardno(person.getIdcardno());  //车主身份idcardno
				deliveryaddress.setOperator(operator);
				deliveryaddress.setChannelid(channelId);  //渠道ID
				deliveryaddress.setChanneluserid(channelUserId); //渠道用户ID
				
				if (StringUtil.isNotEmpty(channelId)) {
					deliveryaddress.setOperator(null);
				}

				//获取这个车主的所有地址的信息
				List<INSBDeliveryaddress> deladdresslist = insbDeliveryaddressService.queryList(deliveryaddress);
				for(INSBDeliveryaddress temp:deladdresslist){
					Map<String,Object> tempMap = new HashMap<String,Object>();
					tempMap.put("recipientname", temp.getRecipientname());    //获取收件人姓名
					tempMap.put("recipientmobilephone", temp.getRecipientmobilephone());  //获取收件人联系电话
					tempMap.put("recipientaddress", temp.getRecipientaddress()); //获取收件人详细地址
					tempMap.put("addressId", temp.getId());//获取地址存放地址信息的id
					tempMap.put("province", temp.getRecipientprovince());//获取省
					tempMap.put("city", temp.getRecipientcity());//获取市
					tempMap.put("area", temp.getRecipientarea());//获取区
					tempMap.put("zipCode", temp.getZip());//获取邮编
					tempMap.put("channelid", temp.getChannelid());//渠道ID
					tempMap.put("channeluserid", temp.getChanneluserid());//获取邮编
					express.add(tempMap);
				}
			} else {
				result.put("Inputusercode", "Inputusercode is null");
			}

			//组织任务id的数据
			result.put("taskid", processInstanceId);
			
			
			INSBQuotetotalinfo vo = new INSBQuotetotalinfo();
			vo.setTaskid(processInstanceId);
			INSBQuotetotalinfo totinfo = insbQuotetotalinfoDao.selectOne(vo);
			INSBQuoteinfo infovo = new INSBQuoteinfo();
			infovo.setQuotetotalinfoid(totinfo.getId());
			infovo.setInscomcode(order.getPrvid());
			INSBQuoteinfo info  =insbQuoteinfoDao.selectOne(infovo);
			INSBDistributiontype dbtvo = new INSBDistributiontype();
			dbtvo.setAgreementid(info.getAgreementid());
			List<INSBDistributiontype> insbDistributiontypes = insbDistributiontypeDao.selectList(dbtvo);
			/*
			 * 自取 、快递
			 */
//			if(!insbDistributiontypes.isEmpty()&&insbDistributiontypes.size()==2){
//				result.put("typebyself", "0");
//				result.put("typeexpress", "1");
//				result.put("byself", byself);
//				result.put("express", express);
//			}else if(!insbDistributiontypes.isEmpty()&&insbDistributiontypes.size()==1){
//				if("1".equals(insbDistributiontypes.get(0).getDistritype())){
//					result.put("typebyself", "0");
//					result.put("byself", byself);
//				}else{
//					result.put("typeexpress", "1");
//					result.put("express", express);
//				}
//			}else{
//				throw new Exception("协议" + info.getAgreementid() + "没有配置配送方式");
//			}

			if (!insbDistributiontypes.isEmpty()) {
				for (INSBDistributiontype t:insbDistributiontypes) {
					if (t == null || StringUtil.isEmpty(t.getDistritype())) continue;
					if ("3".equals(t.getDistritype())) {//电子保单 3
						result.put("electron", t.getDistritype());
					}
					if ("1".equals(t.getDistritype())) {//自取
						//result.put("typebyself", t.getDistritype());
						result.put("typebyself", "0");
						result.put("byself", byself);
					}
					if ("2".equals(t.getDistritype())) {//快递
						//result.put("typeexpress", t.getDistritype());
						result.put("typeexpress", "1");
						result.put("express", express);
					}
				}
			}else{
				throw new Exception("协议" + info.getAgreementid() + "没有配置配送方式");
			}

			body.put("body", result);
			body.put("message", "配送方式查询成功");
			body.put("status", "success");
			return JSONObject.fromObject(body).toString();
		}catch(Exception e){
			e.printStackTrace();
			body.put("message", "配送方式查询失败");
			body.put("status", "fail");
			return JSONObject.fromObject(body).toString();
		}
	}

	/*
	 * 添加新地址的实现
	 * 将新地址存入到deliveryaddress表中，根据任务id去查找信息
	 */
	@Override
	public String addNewAddress(String processInstanceId,String agentnum,String name, String phone,
			String province,String city,String area , String detailAddress, String zipCode,String channelId,String channelUserId) {
		CommonModel model = new CommonModel();
		try{
			INSBDeliveryaddress deliaddr = new INSBDeliveryaddress();
			deliaddr.setRecipientname(name);        //收件人姓名
			deliaddr.setRecipientmobilephone(phone);//联系电话
			deliaddr.setRecipientprovince(province); //省
			deliaddr.setRecipientcity(city);         //市
			deliaddr.setRecipientarea(area);         //区
			deliaddr.setRecipientaddress(detailAddress); //详细地址
			deliaddr.setZip(zipCode);               //邮编
			//获得中间表INSBCarowneinfo中的personid
			INSBCarowneinfo carowneinfo = new INSBCarowneinfo();
			carowneinfo.setTaskid(processInstanceId);
			carowneinfo = insbCarowneinfoService.queryOne(carowneinfo);
			INSBPerson person = new INSBPerson();
			person.setId(carowneinfo.getPersonid());
			person = insbPersonService.queryById(person.getId());//找到车主，获得车主的信息
			deliaddr.setCreatetime(new Date());            //创建时间
			deliaddr.setOperator(agentnum);                //操作员
			deliaddr.setOwnername(person.getName());         //车主姓名
			deliaddr.setOwneridcardno(person.getIdcardno());  //车主身份idcardno
			deliaddr.setChannelid(channelId);                 //渠道ID
			deliaddr.setChanneluserid(channelUserId);         //渠道用户ID
			insbDeliveryaddressService.insert(deliaddr);  //插入操作
			model.setMessage("保存新的配送地址成功");
			model.setStatus("success");
			return JSONObject.fromObject(model).toString();    //保存新地址成功
		}catch(Exception e){
			e.printStackTrace();
			model.setMessage("保存新的配送地址失败");
			model.setStatus("fail");
			return JSONObject.fromObject(model).toString();   //保存新地址失败
		}
	}

	/*
	 * 关于保存配送信息的数据，存入orderdelivery表中去 
	 */
	@Override
	public String saveOrderDelivery(String addressId, String agentnum,String orderno,String delivetytype) {
	    /*
		 * 此id 为选择的地址数据的id，是deliveryaddress数据表中的id 
		 */
		CommonModel result = new CommonModel();
		try{
			//获取地址信息后存入orderdelivery表中
			INSBOrderdelivery orderDelivery = new INSBOrderdelivery();
			INSBOrder order = new INSBOrder();
			order.setOrderno(orderno);               
			order = insbOrderService.queryOne(order);
			INSBOrderdelivery orderDeliveryTemp = new INSBOrderdelivery();
			//orderDeliveryTemp.setTaskid(order.getTaskid());   //订单表中的  任务id
			orderDeliveryTemp.setOrderid(order.getId());        //    
			orderDeliveryTemp = insbOrderdeliveryService.queryOne(orderDeliveryTemp);
			if(orderDeliveryTemp==null){
				orderDelivery.setProviderid(order.getPrvid());   //供应商id
				//供应商信息
				INSBProvider provider = new INSBProvider();
				provider = insbProviderService.queryByPrvcode(order.getPrvid());
				orderDelivery.setProvidername(provider.getPrvshotname());//供应商名称简写
				orderDelivery.setCreatetime(new Date());         //创建时间 
				orderDelivery.setOperator(agentnum);             //操作员
				orderDelivery.setTaskid(order.getTaskid());      //任务id
				orderDelivery.setOrderid(order.getId());      //订单orderno  id
				orderDelivery.setDeptcode(order.getDeptcode());  //出单网点
				orderDelivery.setDelivetype(delivetytype);       //配送方式，自取0或者 快递1
				orderDelivery.setDeliveryside("保网");           //默认保网         
				if(delivetytype.equals("1")){
					//获取deliveryaddress对象，并组织数据
					INSBDeliveryaddress delveryAddress = new INSBDeliveryaddress();
					delveryAddress = insbDeliveryaddressService.queryById(addressId);
					orderDelivery.setRecipientname(delveryAddress.getRecipientname());    //收件人姓名
					orderDelivery.setRecipientmobilephone(delveryAddress.getRecipientmobilephone()); //收件人联系电话
					orderDelivery.setRecipientprovince(delveryAddress.getRecipientprovince());      //省
					orderDelivery.setRecipientcity(delveryAddress.getRecipientcity());              //市
					orderDelivery.setRecipientarea(delveryAddress.getRecipientarea());              //区
					orderDelivery.setRecipientaddress(delveryAddress.getRecipientaddress());        //详细地址
					orderDelivery.setZip(delveryAddress.getZip());                                  //邮编
					orderDelivery.setDeliveryaddressid(addressId);                              //地址编号
				}
				insbOrderdeliveryService.insert(orderDelivery);
			}else{
				orderDeliveryTemp.setDelivetype(delivetytype);
				orderDeliveryTemp.setModifytime(new Date());
				orderDeliveryTemp.setOperator(agentnum);             //操作员
				if(delivetytype.equals("1")){
					//获取deliveryaddress对象，并组织数据
					INSBDeliveryaddress delveryAddress = new INSBDeliveryaddress();
					delveryAddress = insbDeliveryaddressService.queryById(addressId);
					orderDeliveryTemp.setRecipientname(delveryAddress.getRecipientname());    //收件人姓名
					orderDeliveryTemp.setRecipientmobilephone(delveryAddress.getRecipientmobilephone()); //收件人联系电话
					orderDeliveryTemp.setRecipientprovince(delveryAddress.getRecipientprovince());      //省
					orderDeliveryTemp.setRecipientcity(delveryAddress.getRecipientcity());              //市
					orderDeliveryTemp.setRecipientarea(delveryAddress.getRecipientarea());              //区
					orderDeliveryTemp.setRecipientaddress(delveryAddress.getRecipientaddress());        //详细地址
					orderDeliveryTemp.setZip(delveryAddress.getZip());                                  //邮编
					orderDeliveryTemp.setDeliveryaddressid(addressId);
					orderDeliveryTemp.setModifytime(new Date());
				}
				insbOrderdeliveryService.updateById(orderDeliveryTemp);                       //更新操作
			}
			result.setMessage("选择配送地址成功");
			result.setStatus("success");
			return JSONObject.fromObject(result).toString();
		}catch(Exception e){
			e.printStackTrace();
			result.setMessage("选择配送地址失败");
			result.setStatus("fail");
			return JSONObject.fromObject(result).toString();
		}
	}

	 /*
	  * 修改配送地址
	  * 从deliveryaddress表中找到对应的一条数据，进行修改
	  * 完成
	  */

	@Override
	public String editAddress(String addressId,String agentnum,String name, String phone,
			String province,String city,String area , String detailAddress, String zipCode,String orderno) {
		CommonModel model = new CommonModel();
		try{
			INSBDeliveryaddress delveryAddress = new INSBDeliveryaddress();
			delveryAddress = insbDeliveryaddressService.queryById(addressId);
			delveryAddress.setOperator(agentnum);      //操作员
			delveryAddress.setRecipientname(name);    //收件人姓名
			delveryAddress.setRecipientmobilephone(phone); //收件人联系电话
			delveryAddress.setRecipientprovince(province);      //省
			delveryAddress.setRecipientcity(city);              //市
			delveryAddress.setRecipientarea(area);              //区
			delveryAddress.setRecipientaddress(detailAddress);  //详细地址
			delveryAddress.setZip(zipCode);                     //邮编
			delveryAddress.setModifytime(new Date());           //修改时间
			insbDeliveryaddressService.updateById(delveryAddress);   //更新操作
			//如果改的地址是已经存放到orderdelivery中的地址，相应的自动更新该表
			INSBOrder order = new INSBOrder();
			order.setOrderno(orderno);               
			order = insbOrderService.queryOne(order);
			INSBOrderdelivery orderDeliveryTemp = new INSBOrderdelivery();
			orderDeliveryTemp.setOrderid(order.getId());         
			orderDeliveryTemp = insbOrderdeliveryService.queryOne(orderDeliveryTemp);
			if(orderDeliveryTemp!=null){
				if(addressId.equals(orderDeliveryTemp.getDeliveryaddressid())){
					//获取deliveryaddress对象，并组织数据
					orderDeliveryTemp.setRecipientname(delveryAddress.getRecipientname());    //收件人姓名
					orderDeliveryTemp.setRecipientmobilephone(delveryAddress.getRecipientmobilephone()); //收件人联系电话
					orderDeliveryTemp.setRecipientprovince(delveryAddress.getRecipientprovince());      //省
					orderDeliveryTemp.setRecipientcity(delveryAddress.getRecipientcity());              //市
					orderDeliveryTemp.setRecipientarea(delveryAddress.getRecipientarea());              //区
					orderDeliveryTemp.setRecipientaddress(delveryAddress.getRecipientaddress());        //详细地址
					orderDeliveryTemp.setZip(delveryAddress.getZip());                                  //邮编
					orderDeliveryTemp.setDeliveryaddressid(addressId);
					orderDeliveryTemp.setModifytime(new Date());
					insbOrderdeliveryService.updateById(orderDeliveryTemp);
				}
			}
			model.setMessage("修改地址成功");
			model.setStatus("success");
			return JSONObject.fromObject(model).toString();
		}catch(Exception e){
			e.printStackTrace();
			model.setMessage("修改地址失败");
			model.setStatus("fail");
			return JSONObject.fromObject(model).toString();
		}
	}

	/*
	 * 删除配送地址接口
	 */
	@Override
	public String deleteAddress(String addressId) {
		CommonModel model = new CommonModel();
		try{
			insbDeliveryaddressService.deleteById(addressId);
			model.setMessage("删除地址成功");
			model.setStatus("success");
			return JSONObject.fromObject(model).toString();
		}catch(Exception e){
			model.setMessage("删除地址失败");
			model.setStatus("fail");
			return JSONObject.fromObject(model).toString();
		}
		
	}
	
	@Override
	protected BaseDao<INSBOrderdelivery> getBaseDao() {
		return null;
	}

}
