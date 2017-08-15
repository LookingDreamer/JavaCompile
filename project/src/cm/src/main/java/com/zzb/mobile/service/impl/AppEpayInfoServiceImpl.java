package com.zzb.mobile.service.impl;

import com.common.redis.IRedisClient;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.UUIDUtils;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.mobile.dao.AppEpayInfoDao;
import com.zzb.mobile.dao.AppPaymentyzfDao;
import com.zzb.mobile.entity.AppEpayInfo;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.service.AppEpayInfoService;
import com.zzb.model.AppPaymentyzfRModel;


@Service
@Transactional
public class AppEpayInfoServiceImpl extends BaseServiceImpl<AppEpayInfo> implements
AppEpayInfoService {

    public static final String EPAY = "epay";
    public static final String MODULE = "cm:zzb:epay";
    @Resource
	private AppEpayInfoDao appEpayInfoDao;
	@Resource
	private AppPaymentyzfDao appPaymentyzfDao;
	@Resource
    private   INSBAgentDao  iNSBAgentDao; 

	@Resource
	private IRedisClient redisClient;

//易连支付信息查询
	@Override
	public CommonModel queryEpayInfos(String jobNum) {
		CommonModel model=new CommonModel();
		List<AppEpayInfo> list=appEpayInfoDao.querySignInfos(jobNum);
		model.setBody(list);
		model.setMessage("查询成功");
		model.setStatus("success");
		
		return model;
	}
	
	@Override
	public CommonModel writebackName(String jobNum) {
		CommonModel model=new CommonModel();
		INSBAgent agent=iNSBAgentDao.selectByJobnum(jobNum);
		Map<String,String> map=new HashMap<String,String>();
		if(agent!=null){
			String name=agent.getName();
			map.put("name", name);
		}
		 model.setBody(map);
		 model.setMessage("查询代理人姓名成功");
		 model.setStatus("success");
		return model;
	}
	

	//易连支付信息填写(卡密支付)
	@Override
	public CommonModel EpayInfos(JSONObject params) {
		CommonModel model=new CommonModel();
		AppEpayInfo epay=new AppEpayInfo();
		JSONObject obj=JSONObject.fromObject(params);

		epay.setName(obj.getString("name"));	
		epay.setJobNum(obj.getString("jobNum"));
		epay.setIdcardno(obj.getString("idCardno"));
		epay.setPhone(obj.getString("phone"));
		epay.setBankcardno(obj.getString("bankcardno"));
		epay.setTotalamount(new BigDecimal(obj.getString("totalamount")));
		redisClient.set(MODULE, EPAY, epay,24*60*60);   //写入缓存

		model.setMessage("提交成功");
		model.setStatus("success");
		return model;
	}


	
	//易连支付信息填写并提交
	@Override
	public CommonModel submitEpayInfos(JSONObject params) {
		CommonModel model=new CommonModel();  
		AppEpayInfo epay=null;
		try{
			//从缓存中获取上级页面填写信息
			String idCardno=(String) JSONObject.fromObject(redisClient.get(MODULE, EPAY)).get("idcardno");
			String name=(String) JSONObject.fromObject(redisClient.get(MODULE, EPAY)).get("name");
			String jobNum=(String) JSONObject.fromObject(redisClient.get(MODULE, EPAY)).get("jobNum");
			String phone=(String) JSONObject.fromObject(redisClient.get(MODULE, EPAY)).get("phone");
			String bankcardno=(String) JSONObject.fromObject(redisClient.get(MODULE, EPAY)).get("bankcardno");
			BigDecimal totalamount=BigDecimal.valueOf((Double) JSONObject.fromObject(redisClient.get(MODULE, EPAY)).get("totalamount"));
			JSONObject obj=JSONObject.fromObject(params);

			epay=new AppEpayInfo();
			epay.setKhprovince((String)obj.getString("provincecode"));
			epay.setKhcity((String) obj.getString("citycode"));
			epay.setIdcardaddress(obj.getString("idcardaddress"));
			epay.setName(name);
			epay.setJobNum(jobNum);
			epay.setIdcardno(idCardno);
			epay.setPhone(phone);
			epay.setBankcardno(bankcardno);
			epay.setId(UUIDUtils.random());
			epay.setCreatetime(new Date());
			epay.setOperator(epay.getJobNum());
			epay.setTotalamount(totalamount);
			
		}catch(Exception e){e.printStackTrace();}
		
		appEpayInfoDao.insert(epay);          //将支付信息写入数据库
		redisClient.del(MODULE, EPAY);   //删除缓存
		model.setMessage("信息确认完成");
		model.setStatus("success");
		
		return model;
	}

	//获取省级列表
	@Override
	public CommonModel getProvinceInfo() {
		CommonModel model=new CommonModel();
		List<Map<String, Object>> list=appPaymentyzfDao.getProvince();
	    model.setBody(list);
		model.setMessage("获取省级列表成功");
		model.setStatus("success");
		return model;
	}
	//获取市级列表
	@Override
	public CommonModel getCityInfo(String provinceID) {
		CommonModel model=new CommonModel();
		List<Map<String, Object>> list=appPaymentyzfDao.getCity(provinceID);
	    model.setBody(list);
		model.setMessage("获取市级列表成功");
		model.setStatus("success");
		return model;
	}

	@Override
	protected BaseDao<AppEpayInfo> getBaseDao() {
		// TODO Auto-generated method stub
		return null;
	}



}
