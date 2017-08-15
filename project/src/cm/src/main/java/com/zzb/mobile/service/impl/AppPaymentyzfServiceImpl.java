package com.zzb.mobile.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.UUIDUtils;
import com.zzb.cm.dao.INSBOrderDao;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.dao.INSBOrderdeliveryDao;
import com.zzb.conf.dao.INSBOrderpaymentDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBOrderdelivery;
import com.zzb.conf.entity.INSBOrderpayment;
import com.zzb.mobile.dao.AppPaymentyzfDao;
import com.zzb.mobile.entity.AppPaymentyzf;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.service.AppPaymentyzfService;
import com.zzb.model.AppPaymentyzfRModel;

@Service
@Transactional
public class AppPaymentyzfServiceImpl extends BaseServiceImpl<AppPaymentyzf> implements
		AppPaymentyzfService {
	
	@Resource
	private AppPaymentyzfDao appPaymentyzfDao;  
	@Resource
	private INSBAgentDao iNSBAgentDao;
	@Resource
	private INSBOrderpaymentDao insbOrderpaymentDao;
	@Resource
	private INSBOrderdeliveryDao insbOrderdeliveryDao;
	
	@Resource
	private INSBOrderDao orderDao;
	
    //翼支付签约查询（“尾号”+卡后4位）
	@Override
	public CommonModel querySignInfos(String jobNum) {
		CommonModel model=new CommonModel();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("jobNum", jobNum);
		List<AppPaymentyzfRModel> querymap =appPaymentyzfDao.querySignInfos(map);
		Map<String,Object> modelmap=null;	
		List<Map> list=new ArrayList<Map>();
		if(querymap.size()>0){
			for(AppPaymentyzfRModel info:querymap){
				modelmap=new HashMap<String,Object>();
				String bank=info.getKhcomname()+""+info.getBankcardType();
				int length=info.getBankcardno().toString().length();
				String cardn="尾号"+info.getBankcardno().toString().substring(length-4, length); 
				modelmap.put("id", info.getId());
				modelmap.put("bank", bank);
				modelmap.put("cardn", cardn);	
				list.add(modelmap);
			}
			
		}
		model.setBody(list);
		model.setMessage("查询成功");
		model.setStatus("success");
		return model;
	}
	
	//翼支付签约成功信息（卡号后4位+开户行名称）
	@Override
	public CommonModel querySignInfos1(String jobNum) {
		CommonModel model=new CommonModel();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("jobNum", jobNum);  
		List<AppPaymentyzfRModel> querymap =appPaymentyzfDao.querySignInfos(map);
		Map<String,Object> modelmap=null;	
		List<Map> list=new ArrayList<Map>();
		if(querymap.size()>0){
			for(AppPaymentyzfRModel info:querymap){
				modelmap=new HashMap<String,Object>();
				String name=(String)info.getName();
				int length=info.getBankcardno().toString().length();
				String bankcard=info.getBankcardno().toString().substring(length-4, length)+"("+info.getKhcomname()+")";
		        modelmap.put("id", info.getId());
				modelmap.put("name", name);
				modelmap.put("bankcard", bankcard);	
				list.add(modelmap);
			}
			
		}
		model.setBody(list);
		model.setMessage("查询成功");
		model.setStatus("success");
		return model;
		
		
	}
	
    //翼支付确认签约
	@Override
	public CommonModel Signyzf(JSONObject params) {
		CommonModel model=new CommonModel();
		AppPaymentyzf yzf=new AppPaymentyzf();
		Map<String,Object> map=new HashMap<String,Object>();
		JSONObject obj=JSONObject.fromObject(params);	
		//LdapAgentModel agentModel = ldap.searchAgent(obj.getString("jobNum"));//100000031
		INSBAgent agent=iNSBAgentDao.selectByJobnum(obj.getString("jobNum"));
		String name=agent.getName();
		String jobNum=obj.getString("jobNum");
		map.put("jobNum", jobNum);
		List<AppPaymentyzfRModel> querymap =appPaymentyzfDao.querySignInfos(map);
		List list=new ArrayList();
		for(AppPaymentyzfRModel info:querymap){
			String bankcard=info.getBankcardno();
			list.add(bankcard);
		}	
			if(list.contains(obj.getString("bankcardno"))){	
				model.setMessage("您已签约");
			}else{
				
				yzf.setId(UUIDUtils.random());
				yzf.setName(name);
				yzf.setJobNum(jobNum);
				yzf.setBankcardno(obj.getString("bankcardno"));   
				yzf.setBankcardType(obj.getString("bankcardType"));
			
				Map<String, Object> codemap=new HashMap<String, Object>();	
		    	//根据发卡行code码将发卡行code码及名称写入数据库
				if(obj.containsKey("fkbankcode")){
		    		codemap.put("codevalue", obj.getString("fkbankcode"));
		    		codemap.put("codetype", "bankcard");
		    		Map<String, Object> rmap=appPaymentyzfDao.queryFromInsccodeByCode(codemap);
		    		yzf.setFkbankcode(obj.getString("fkbankcode"));
		    		yzf.setFkbankname((String) rmap.get("codename"));
		    	}
                yzf.setKhcomname(obj.getString("khcomname"));
                if("creditcardflag".equals(obj.getString("bankcardType"))){
                	yzf.setPeriodmonth(obj.getString("periodmonth"));
    				yzf.setPeriodyear(obj.getString("periodyear"));
    				yzf.setValiadatecode(obj.getString("valiadatecode"));
                }
				yzf.setKhprovincecode(obj.getString("provincecode"));
				yzf.setKhcitycode(obj.getString("citycode"));			
				yzf.setIdcradType(obj.getString("idcradType"));
				yzf.setIdcardno(obj.getString("idCardno"));
				yzf.setRelationaddress(obj.getString("relationaddress"));
				yzf.setPhone(obj.getString("phone"));
				yzf.setOperator(obj.getString("jobNum"));
				yzf.setCreatetime(new Date());
				appPaymentyzfDao.insert(yzf);
				if("cashcardflag".equals(yzf.getBankcardType()))yzf.setBankcardType("储蓄卡");
				if("creditcardflag".equals(yzf.getBankcardType()))yzf.setBankcardType("信用卡");
				String bank=yzf.getKhcomname()+""+yzf.getBankcardType();
				int length=yzf.getBankcardno().toString().length();
				HashMap<String,Object> modelmap=new HashMap<String,Object>();
				String cardn="尾号"+yzf.getBankcardno().toString().substring(length-4, length); 
				modelmap.put("id", yzf.getId());
				modelmap.put("bank", bank);
				modelmap.put("cardn", cardn);	
				model.setMessage("签约成功");
				model.setStatus("success");
				model.setBody(modelmap);			
			}		

		return model;
	}
	
	//翼支付信息确认
	@Override
	  public CommonModel Infosyzf(@RequestBody JSONObject params){
    	JSONObject obj=JSONObject.fromObject(params);
    	AppPaymentyzf yzf=new AppPaymentyzf();
    	Map<String,Object> map=new HashMap<String,Object>();
    	//根据发卡行code码返回对应名称
    	if(obj.containsKey("fkbankcode")){
    		map.put("codevalue", obj.getString("fkbankcode"));
    		map.put("codetype", "bankcard");
    		Map<String, Object> rmap=appPaymentyzfDao.queryFromInsccodeByCode(map);
    		obj.replace("fkbankcode", rmap.get("codename"));
    	}

    	//根据证件类型code码返回对应名称
      	if(obj.containsKey("idcradType")){
    		map.put("codevalue", obj.getString("idcradType"));
    		map.put("codetype", "CertKinds");
    		Map<String, Object> rmap=appPaymentyzfDao.queryFromInsccodeByCode(map);
    		obj.replace("idcradType", rmap.get("codename"));
    	}
      //根据省级code码返回对应名称
    	if(obj.containsKey("provincecode")){
    		map.put("comcode", obj.getString("provincecode"));
    		Map<String, Object> rmap=appPaymentyzfDao.queryFromInsbregionByCode(map);
    		obj.replace("provincecode", rmap.get("comcodename"));
    	}
    	 //根据市级code码返回对应名称
    	if(obj.containsKey("citycode")){
    		map.put("comcode", obj.getString("citycode"));
    		Map<String, Object> rmap=appPaymentyzfDao.queryFromInsbregionByCode(map);
    		obj.replace("citycode", rmap.get("comcodename"));
    	}
    	

    	CommonModel model=new CommonModel();
    	model.setMessage("信息确认完成");
    	model.setStatus("success");
    	model.setBody(obj);
    	return model;
    
		  
	  }
	
	
	//翼支付信息填写（获取省级列表，返回省级代码，名称）
	@Override
	public CommonModel ProvinceInfo() {
		CommonModel model=new CommonModel();
		List<Map<String, Object>> list=appPaymentyzfDao.getProvince();
        model.setBody(list);
		model.setMessage("获取省级列表成功");
		model.setStatus("success");
		return model;

	}
	//翼支付信息填写（获取市级列表，返回市级代码，名称）
	@Override
	public CommonModel CityInfo(String provinceID) {
		CommonModel model=new CommonModel();
		Map<String,Object> map=new HashMap<String,Object>();
		List<Map<String, Object>> list=appPaymentyzfDao.getCity(provinceID);
        model.setBody(list);
		model.setMessage("获取市级列表成功");
		model.setStatus("success");
		return model;
	}


	//翼支付信息填写（获取银行列表，返回银行代码，名称）
	@Override
	public CommonModel getBankType() {
		CommonModel model=new CommonModel();
		List<Map<String, Object>> list=appPaymentyzfDao.getBankType();
        model.setBody(list);
		model.setMessage("获取银行列表成功");
		model.setStatus("success");
		return model;
	}

	//翼支付信息填写（获取证件类型列表，返回证件类型代码，名称）
	@Override
	public CommonModel getidCardType() {
		CommonModel model=new CommonModel();
		List<Map<String, Object>> list=appPaymentyzfDao.getidCardType();
        model.setBody(list);
		model.setMessage("获取证件类型列表成功");
		model.setStatus("success");
		return model;
	}

	//翼支付信息填写（获取银行卡类型列表，返回卡类型代码，名称）
	@Override
	public CommonModel getCardType() {
		CommonModel model=new CommonModel();
		List<Map<String, Object>> list=appPaymentyzfDao.getBankCardType();
		model.setMessage("获取卡种成功");
		model.setStatus("success");
        model.setBody(list);
		return model;
	}


	@Override
	public CommonModel getPaymentAmount(String taskid,String prvid) {
		CommonModel model=new CommonModel();
		INSBOrder temp =new INSBOrder();
		temp.setTaskid(taskid);
		temp.setPrvid(prvid);
		INSBOrder order = orderDao.selectOne(temp);
		
		
		//用订单id查询保费和手续费
//		INSBOrderpayment insbOrderpayment = new INSBOrderpayment();
//		insbOrderpayment.setOrderid(order.getId());
//		insbOrderpayment = insbOrderpaymentDao.selectOne(insbOrderpayment);
//		if(insbOrderpayment==null){
//			model.setStatus("fail");
//			model.setMessage("订单查询失败");
//			return model;
//		}
		
		//用订单id查询快递费
		INSBOrderdelivery insbOrderdelivery = new INSBOrderdelivery();
		insbOrderdelivery.setOrderid(order.getId());
		insbOrderdelivery = insbOrderdeliveryDao.selectOne(insbOrderdelivery);
		if(insbOrderdelivery==null){
			model.setStatus("fail");
			model.setMessage("配送订单查询失败");
			return model;
		}
		Map<String,Object> map = new HashMap<String,Object>();
		//DecimalFormat decimalFormat = new DecimalFormat("#.00");
		//保费
		BigDecimal amount = new BigDecimal(order.getTotalpaymentamount()==null?0.00:order.getTotalpaymentamount());
		//map.put("amount", decimalFormat.format(amount.doubleValue()).equals(".00")?"0.00":decimalFormat.format(amount.doubleValue()));
		map.put("amount", String.valueOf(amount.setScale(2, RoundingMode.HALF_UP)));
		//手续费
		BigDecimal paypoundage = new BigDecimal(0.00);//insbOrderpayment.getPaypoundage()==null?0.00:insbOrderpayment.getPaypoundage());
		//map.put("paypoundage", decimalFormat.format(paypoundage.doubleValue()).equals(".00")?"0.00":decimalFormat.format(paypoundage.doubleValue()));
		map.put("paypoundage", String.valueOf(paypoundage.setScale(2, RoundingMode.HALF_UP)));
		map.put("orderid", order.getId());
		map.put("taskid", insbOrderdelivery.getTaskid());
		//配送费
		BigDecimal fee = new BigDecimal(insbOrderdelivery.getFee()==null?0.00:insbOrderdelivery.getFee());
		//map.put("fee", decimalFormat.format(fee.doubleValue()).equals(".00")?"0.00":decimalFormat.format(fee.doubleValue()));
		map.put("fee", String.valueOf(fee.setScale(2, RoundingMode.HALF_UP)));
		//避免精确度问题
		BigDecimal totalamount = amount.add(paypoundage).add(fee);
		//总支付金额
		//map.put("totalamount", decimalFormat.format(totalamount.doubleValue()).equals(".00")?"0.00":decimalFormat.format(totalamount.doubleValue()));
		map.put("totalamount", String.valueOf(totalamount.setScale(2, RoundingMode.HALF_UP)));
		model.setStatus("success");
		model.setMessage("支付金额查询成功");
		model.setBody(map);
		return model;
	}

	@Override
	protected BaseDao<AppPaymentyzf> getBaseDao() {
		return appPaymentyzfDao;
	}

}
