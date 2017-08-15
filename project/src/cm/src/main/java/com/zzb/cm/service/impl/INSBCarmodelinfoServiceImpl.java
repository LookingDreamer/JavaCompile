package com.zzb.cm.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.system.dao.INSCCodeDao;
import com.common.CloudQueryUtil;
import com.common.PagingParams;
import com.zzb.cm.controller.vo.CarModelInfoVO;
import com.zzb.cm.dao.INSBCarinfoDao;
import com.zzb.cm.dao.INSBCarinfohisDao;
import com.zzb.cm.dao.INSBCarmodelinfoDao;
import com.zzb.cm.dao.INSBCarmodelinfohisDao;
import com.zzb.cm.dao.INSBOrderDao;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBCarinfohis;
import com.zzb.cm.entity.INSBCarmodelinfo;
import com.zzb.cm.entity.INSBCarmodelinfohis;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.entity.entityutil.EntityTransformUtil;
import com.zzb.cm.service.INSBCarmodelinfoService;

@Service
@Transactional 
public class INSBCarmodelinfoServiceImpl extends BaseServiceImpl<INSBCarmodelinfo> implements
		INSBCarmodelinfoService {
	
	private static String SEARCHCARURL = "";

	static {
		// 读取相关的配置
		ResourceBundle resourceBundle = ResourceBundle
				.getBundle("config/config");
		SEARCHCARURL = resourceBundle.getString("searchcar.url");
	}
	@Resource
	private INSBCarmodelinfoDao insbCarmodelinfoDao;
	@Resource
	private INSBOrderDao insbOrderDao;
	@Resource
	private INSBCarmodelinfohisDao insbCarmodelinfohisDao;
	@Resource
	private INSCCodeDao inscCodeDao;
	@Resource
	private INSBCarinfoDao insbCarinfoDao;
	@Resource
	private INSBCarinfohisDao insbCarinfohisDao;
	
	@Override
	protected BaseDao<INSBCarmodelinfo> getBaseDao() {
		return insbCarmodelinfoDao;
	}
	
	/**
	 * 通过车辆信息的id找到车型信息
	 */
	@Override
	public Map<String, Object> getCarmodelInfoByCarinfoId(String taskid, String carinfoid, String inscomcode, String useType) {
		Map<String, Object> temp = new HashMap<String, Object>();
		temp.put("taskid", taskid);
		temp.put("inscomcode", inscomcode);
		Map<String, String> carpriceparam = new HashMap<String, String>();
		carpriceparam.put("codetype", "rulepriceprovidetype");
		temp.put("carpriceList", inscCodeDao.selectINSCCodeByCode(carpriceparam));//车价选择列表
		INSBCarmodelinfo carmodelinfoTemp = new INSBCarmodelinfo();
		carmodelinfoTemp.setCarinfoid(carinfoid);
		carmodelinfoTemp = insbCarmodelinfoDao.selectOne(carmodelinfoTemp);
		//通过是否生成订单判断是否在人工报价节点
		INSBOrder order = new INSBOrder();
		order.setTaskid(taskid);
		order.setPrvid(inscomcode);
		order = insbOrderDao.selectOne(order);
//		if(order==null){
			//不能查到订单表明是人工报价节点
			//重新取得车型历史表信息
			INSBCarmodelinfohis carmodelinfohis = new INSBCarmodelinfohis();
			carmodelinfohis.setCarinfoid(carinfoid);
			carmodelinfohis.setInscomcode(inscomcode);
			carmodelinfohis = insbCarmodelinfohisDao.selectOne(carmodelinfohis);
			if(carmodelinfohis!=null){
				//历史表转换成原表类型
				carmodelinfoTemp = EntityTransformUtil.carmodelinfohis2Carmodelinfo(carmodelinfohis);
			}
//		}
		if(carmodelinfoTemp!=null){
			if(carmodelinfoTemp.getStandardfullname()!=null){
				temp.put("standardfullname", carmodelinfoTemp.getStandardfullname());//车辆信息描述
			}
			if(carmodelinfoTemp.getSyvehicletypename()!=null){
				temp.put("vehicletypename", carmodelinfoTemp.getSyvehicletypename());//车辆类型
			}
			if(carmodelinfoTemp.getStandardname()!=null){
				temp.put("standardname", carmodelinfoTemp.getStandardname());//车辆标准名称
			}
			if(carmodelinfoTemp.getFamilyname()!=null){
				temp.put("familyname", carmodelinfoTemp.getFamilyname());//车辆系列名称
			}
			if(carmodelinfoTemp.getBrandname()!=null){
				temp.put("brandname", carmodelinfoTemp.getBrandname());//品牌名称
			}
			if(carmodelinfoTemp.getId()!=null){
				temp.put("id", carmodelinfoTemp.getId());//车型表id
			}
			if(carmodelinfoTemp.getSeat()!=null){
				temp.put("seat", carmodelinfoTemp.getSeat());//承载人数
			}
			if(carmodelinfoTemp.getUnwrtweight()!=null){
				temp.put("unwrtweight", carmodelinfoTemp.getUnwrtweight());//核定载重量
			}
			if(carmodelinfoTemp.getDisplacement()!=null){
				temp.put("displacement", carmodelinfoTemp.getDisplacement().toString());//发动机排量
			}
			if(carmodelinfoTemp.getFullweight()!=null){
				temp.put("fullweight", carmodelinfoTemp.getFullweight());//整备质量
			}
			if(carmodelinfoTemp.getListedyear()!=null){
				temp.put("listedyear", carmodelinfoTemp.getListedyear());//上市年份
			}
			if(carmodelinfoTemp.getPrice()!=null){
				temp.put("price", carmodelinfoTemp.getPrice());//新车购置价
			}
			if(carmodelinfoTemp.getTaxprice()!=null){
				temp.put("addtaxprice", carmodelinfoTemp.getPrice()+
						carmodelinfoTemp.getTaxprice());//新车购置价含税
			}
			if(carmodelinfoTemp.getPolicycarprice()!=null){
				temp.put("policycarprice", carmodelinfoTemp.getPolicycarprice());//投保车价
			}
			if(carmodelinfoTemp.getIsstandardcar()!=null){
				temp.put("isstandardcar", carmodelinfoTemp.getIsstandardcar());//是否标准车型
			}
			if(carmodelinfoTemp.getCarprice()!=null){
				temp.put("carprice", carmodelinfoTemp.getCarprice());//车价选择
			}
			if(carmodelinfoTemp.getCardeploy()!=null){
				temp.put("cardeploy", carmodelinfoTemp.getCardeploy());//车型配置
			}
			if(carmodelinfoTemp.getTaxprice()!=null){
				temp.put("taxprice", carmodelinfoTemp.getTaxprice());//税额
			}
			if(carmodelinfoTemp.getAnalogyprice()!=null){
				temp.put("analogyprice", carmodelinfoTemp.getAnalogyprice());//类比价格
			}
			if(carmodelinfoTemp.getAnalogytaxprice()!=null){
				temp.put("analogytaxprice", carmodelinfoTemp.getAnalogytaxprice());//类比税额
			}
			if(carmodelinfoTemp.getAliasname()!=null){
				temp.put("aliasname", carmodelinfoTemp.getAliasname());//别名
			}
			if(carmodelinfoTemp.getGearbox()!=null){
				temp.put("gearbox", carmodelinfoTemp.getGearbox());//变速箱
			}
			if(carmodelinfoTemp.getLoads()!=null){
				temp.put("loads", carmodelinfoTemp.getLoads());//载荷
			}
		}
		return temp;
	}
	
	/**
	 * 重选车型信息
	 */
//	@Override
//	public String reselectCarModelInfo(Map<String, Object> paramMap) {
//		//得到重选车型分页信息并返回json数据
//		Map<String, Object> map = new HashMap<String, Object>();
//		List<Map<String, Object>> carModelInfoList = insbCarmodelinfoDao.reselectCarModelInfo(paramMap);
//		map.put("records", "10000");
//		map.put("page", 1);
//		map.put("total", carModelInfoList.size());
//		map.put("rows", carModelInfoList);
//		JSONObject jsonObject = JSONObject.fromObject(map);
//		return jsonObject.toString();
//	}
	
	/**
	 * 重选车型信息
	 */
	@Override
	public String reselectCarModelInfo(PagingParams pagingParams, String standardfullname) {
		System.out.println(" ====== standardfullname ====== " + standardfullname);
		//得到重选车型分页信息并返回json数据
		Map<String, Object> map = new HashMap<String, Object>();
//		if(pagingParams.getLimit()==null||pagingParams.getLimit()==0){
//			pagingParams.setLimit(10);
//		}
//		// 组织请求地址
//		//接口URL: /searchList/$key/$pMax?/$p? (?表示末尾参数可空缺)参数：$key=关键字，$pMax=页最大数(默认10)，$p=当前页
//		//URL举例：http://cx.baoxan.org/searchList/一汽奥迪
//		String requestURL = SEARCHCARURL+"/"+(standardfullname!=null?standardfullname:"大众")+
//				"/"+pagingParams.getLimit()+"/"+(pagingParams.getOffset()/pagingParams.getLimit()+1);
		Map<String,String> params=new HashMap<String,String>();
		if(standardfullname==null || standardfullname==""){
			params.put("vehicleName","大众");
		}else{
			params.put("vehicleName",standardfullname);
		}
		params.put("flag","2");
		params.put("pageSize", "10");
		params.put("pageNumber","1");
		String requestURL="http://119.29.53.84:8080/cif/output/getCarmodelList";
		System.out.println(" ====== requestURL ====== " + requestURL);
		//调取工作流终止工作流程接口	
		String result="";
		try {
			result = CloudQueryUtil.jingYouCarModelSearch(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(result);
		JSONObject resultJSONObj = JSONObject.fromObject(result);
		JSONArray ja = resultJSONObj.getJSONArray("List");
		List<Map<String, Object>> carModelInfoList = new ArrayList<Map<String,Object>>();
		for (Object obj : ja) {
			JSONObject jo = JSONObject.fromObject(obj);
			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put("standardname", jo.getString("vehiclename"));//标准名称
			temp.put("standardfullname", jo.getString("vehiclename"));//车辆全称
//			temp.put("aliasname", jo.getString("aliasName"));//车辆别名
			temp.put("gearbox", jo.getString("gearbox"));//变速器名称
			temp.put("familyname", jo.getString("familyname"));//车辆全称
			if( StringUtils.isNotBlank(jo.getString("seat")) )
				temp.put("seat", jo.getInt("seat"));//座位数
			temp.put("displacement", jo.getString("displacement"));//排量
			if( StringUtils.isNotBlank(jo.getString("modelLoads")) )
				temp.put("unwrtweight", jo.getDouble("modelLoads"));//核定载质量
//			temp.put("loads", jo.getDouble("loads"));//载荷
			temp.put("fullweight", jo.getString("fullweight"));//整备质量
			temp.put("isstandardcar", "是");//是否标准车型
			temp.put("purchaseprice", jo.getDouble("price")+"-"+jo.getDouble("taxprice"));//新车购置价和税额
			temp.put("taxprice", jo.getDouble("taxprice"));//税额
			temp.put("price", jo.getDouble("price"));//新车购置价
			temp.put("analogytaxprice", jo.getDouble("analogytaxprice"));//类比税额
			temp.put("analogyprice", jo.getDouble("analogyprice"));//类比价格
			temp.put("carprice", "0");//车价选择
			temp.put("listedyear", jo.getString("maketdate"));//年款
			temp.put("brandname", jo.getString("brandname"));//车辆品牌
            temp.put("vehicleid",jo.getString("vehicleId"));//车型id
			//bug-8760 车辆种类从cif带回并且保存到单方车信息表insbcarmodelinfohis
			temp.put("syvehicletypename",jo.getString("syvehicletypename"));//车辆种类
			carModelInfoList.add(temp);
		}
		map.put("records", "10000");
		map.put("page", 1);
		map.put("total", resultJSONObj.getLong("RecTotal"));
		map.put("rows", carModelInfoList);
		JSONObject jsonObject = JSONObject.fromObject(map);
		return jsonObject.toString();
	}
	
	@Override
	public String updateCarModelInfoByInstanceid(CarModelInfoVO carModelInfoVO) {
		//查询订单表判断是否报价阶段
		INSBOrder order = new INSBOrder();
		order.setTaskid(carModelInfoVO.getInstanceId());
		order.setPrvid(carModelInfoVO.getInscomcode());
		order = insbOrderDao.selectOne(order);
		boolean isQuote = false;
		if(order==null){
			isQuote = true;
		}
		//2016年2月17日新需求要求每个节点都要查询修改历史表数据
		isQuote = true;
		//查询车辆信息
		INSBCarinfo carinfo = new INSBCarinfo();
		carinfo.setTaskid(carModelInfoVO.getInstanceId());
		carinfo = insbCarinfoDao.selectOne(carinfo);
		carinfo.setOperator(carModelInfoVO.getOperator());
		carinfo.setCarproperty(carModelInfoVO.getCarproperty());
		carinfo.setStandardfullname(carModelInfoVO.getStandardfullname());
		//查询车型信息
		INSBCarmodelinfo carmodelinfo = new INSBCarmodelinfo();
		carmodelinfo.setCarinfoid(carinfo.getId());
		carmodelinfo = insbCarmodelinfoDao.selectOne(carmodelinfo);
		carmodelinfo.setOperator(carModelInfoVO.getOperator());
		carmodelinfo.setBrandname(carModelInfoVO.getBrandname());
		carmodelinfo.setStandardname(carModelInfoVO.getStandardname());
		carmodelinfo.setStandardfullname(carModelInfoVO.getStandardfullname());
		carmodelinfo.setFamilyname(carModelInfoVO.getFamilyname());
		carmodelinfo.setPrice(carModelInfoVO.getPrice());
		carmodelinfo.setTaxprice(carModelInfoVO.getTaxprice());
		carmodelinfo.setAnalogyprice(carModelInfoVO.getAnalogyprice());
		carmodelinfo.setAnalogytaxprice(carModelInfoVO.getAnalogytaxprice());
		carmodelinfo.setSeat(carModelInfoVO.getSeat());
		carmodelinfo.setDisplacement(carModelInfoVO.getDisplacement());
		carmodelinfo.setAliasname(carModelInfoVO.getAliasname());
		carmodelinfo.setGearbox(carModelInfoVO.getGearbox());
		carmodelinfo.setLoads(carModelInfoVO.getLoads());
		carmodelinfo.setUnwrtweight(carModelInfoVO.getUnwrtweight());
		carmodelinfo.setFullweight(carModelInfoVO.getFullweight());
		carmodelinfo.setListedyear(carModelInfoVO.getListedyear());
		carmodelinfo.setCardeploy(carModelInfoVO.getCardeploy());
		carmodelinfo.setPolicycarprice(carModelInfoVO.getPolicycarprice());
		Date date = new Date();
		//修改车辆和车型信息
		if(isQuote){//是报价阶段
			//查询车辆和车型信息历史表数据
			INSBCarinfohis carinfohis = new INSBCarinfohis();
			carinfohis.setTaskid(carModelInfoVO.getInstanceId());
			carinfohis.setInscomcode(carModelInfoVO.getInscomcode());
			carinfohis = insbCarinfohisDao.selectOne(carinfohis);
			INSBCarmodelinfohis carmodelinfohis = new INSBCarmodelinfohis();
			carmodelinfohis.setCarinfoid(carinfo.getId());
			carmodelinfohis.setInscomcode(carModelInfoVO.getInscomcode());
			carmodelinfohis = insbCarmodelinfohisDao.selectOne(carmodelinfohis);
			if(carinfohis==null){//车辆信息历史表没有数据，修改后新增一条数据
				carinfohis = EntityTransformUtil.carinfo2Carinfohis(carinfo, carModelInfoVO.getInscomcode());
				carinfohis.setCreatetime(date);
				carinfohis.setModifytime(null);
				insbCarinfohisDao.insert(carinfohis);
			}else{//车辆信息表中有历史记录，修改此数据
				carinfohis.setModifytime(date);
				carinfohis.setOperator(carModelInfoVO.getOperator());
				carinfohis.setCarproperty(carModelInfoVO.getCarproperty());
				carinfohis.setStandardfullname(carModelInfoVO.getStandardfullname());
				insbCarinfohisDao.updateById(carinfohis);
			}
			if(carmodelinfohis==null){//车型信息表中没有记录，修改后新增一条数据
				carmodelinfohis = EntityTransformUtil.carmodelinfo2Carmodelinfohis(carmodelinfo, carModelInfoVO.getInscomcode());
				carmodelinfohis.setCreatetime(date);
				carmodelinfohis.setModifytime(null);
				insbCarmodelinfohisDao.insert(carmodelinfohis);
			}else{//车型信息表中有记录，修改此数据
				carmodelinfohis.setModifytime(date);
				carmodelinfohis.setOperator(carModelInfoVO.getOperator());
				carmodelinfohis.setBrandname(carModelInfoVO.getBrandname());
				carmodelinfohis.setStandardname(carModelInfoVO.getStandardname());
				carmodelinfohis.setStandardfullname(carModelInfoVO.getStandardfullname());
				carmodelinfohis.setFamilyname(carModelInfoVO.getFamilyname());
				carmodelinfohis.setPrice(carModelInfoVO.getPrice());
				carmodelinfohis.setTaxprice(carModelInfoVO.getTaxprice());
				carmodelinfohis.setAnalogyprice(carModelInfoVO.getAnalogyprice());
				carmodelinfohis.setAnalogytaxprice(carModelInfoVO.getAnalogytaxprice());
				carmodelinfohis.setSeat(carModelInfoVO.getSeat());
				carmodelinfohis.setDisplacement(carModelInfoVO.getDisplacement());
				carmodelinfohis.setAliasname(carModelInfoVO.getAliasname());
				carmodelinfohis.setGearbox(carModelInfoVO.getGearbox());
				carmodelinfohis.setLoads(carModelInfoVO.getLoads());
				carmodelinfohis.setUnwrtweight(carModelInfoVO.getUnwrtweight());
				carmodelinfohis.setFullweight(carModelInfoVO.getFullweight());
				carmodelinfohis.setListedyear(carModelInfoVO.getListedyear());
				carmodelinfohis.setCardeploy(carModelInfoVO.getCardeploy());
				carmodelinfohis.setPolicycarprice(carModelInfoVO.getPolicycarprice());
				insbCarmodelinfohisDao.updateById(carmodelinfohis);
			}
		}else{//不是报价阶段
			//更新车辆信息
			carinfo.setModifytime(date);
			insbCarinfoDao.updateById(carinfo);
			LogUtil.info("INSBCarinfo|报表数据埋点|"+JSONObject.fromObject(carinfo).toString());
			//更新车型信息
			carmodelinfo.setModifytime(date);
			insbCarmodelinfoDao.updateById(carmodelinfo);
		}
		return "success";
	}

	@Override
	public List<INSBCarmodelinfo> queryAll(int offset,int limit) {
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("offset", offset);
		params.put("limit", limit);
		return insbCarmodelinfoDao.selectAll(params);
	}
	
}