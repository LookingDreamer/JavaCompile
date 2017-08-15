package com.common;

import com.cninsure.core.utils.LogUtil;
import com.common.redis.CMRedisClient;
import com.zzb.mobile.model.lastgetdanger.AccidentCarInfoBean;
import com.zzb.mobile.model.lastgetdanger.InParas;
import com.zzb.mobile.model.lastgetdanger.InsuranceBean;
import com.zzb.mobile.model.lastindanger.LastClaimBackInfo;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;
import net.sf.json.JSONObject;
import org.apache.http.client.ClientProtocolException;

/**
 * 调用云端查询接口
 * 
 * @author hejie
 *
 */
public class CloudQueryUtil {

	private static String CLOUDQUERY = "";
	private static String SEARCHCAR = "";
	private static final String CLAIM_REDIS_KEY = "cm:cloud_query:claim";

	static {
		// 读取相关的配置
		ResourceBundle resourceBundle = ResourceBundle
				.getBundle("config/config");
		CLOUDQUERY = resourceBundle.getString("cloudquery.url");
		SEARCHCAR = resourceBundle.getString("searchcar.url");
	}

	/**
	 * 查询上年投保信息
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String getLastYearInsureInfo(Map<String, String> map)
			throws ClientProtocolException, IOException {
		String path = CLOUDQUERY + "/rnpolicy/queryLastYearPolicy";
		//HttpClientUtil异步处理
		return HttpClientUtil.doPostJsonAsyncClient(path, map,"");
	}

	/**
	 * 模糊查询车辆信息
	 * 
	 * @param map
	 * @return
	 */
	public static String getCarInfo(Map<String, String> map, String modelName,
			String pageSize, String currentPage) {
		return HttpClientUtil.doGet(SEARCHCAR + "/" + modelName + "/"
				+ pageSize + "/" + currentPage, map);
	}

    /**
     * 续保查询：根据车牌号和车主姓名获取上年投保信息
     *
     * @param map
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static String getLastYearInsureInfoByCarNumberAndCarOwerForRenewal(Map<String, String> map) throws IOException {
        String path = CLOUDQUERY + "/quickpolicy/quickInsurancePolicy";
        //HttpClientUtil异步处理
        return HttpClientUtil.doPostJsonAsyncClient(path, map,"");
    }

	/**
	 * 根据车牌号和车主姓名获取上年保单信息
	 * 
	 * @param map
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String getLastYearInsureInfoByCarNumberAndCarOwer(
			Map<String, String> map) throws ClientProtocolException,
			IOException {
		String path = CLOUDQUERY + "/rnpolicy/queryLastYearPolicy";
		//HttpClientUtil异步处理
        return HttpClientUtil.doPostJsonAsyncClient(path, map,"");
	}

	/**
	 * 云查询上年保单信息，包含 精灵信息
	 * 
	 * @param json
	 * @return
	 * @throws IOException 
	 */
	public static String getLastYearInsurePolicy(String json) {
		String path = CLOUDQUERY + "/rnpolicy/queryLastYearPolicy";
		//HttpClientUtil异步处理
        return HttpClientUtil.doPostJsonAsyncClientJson(path, json,"");
	}

	/**
	 * 查询保单列表信息接口
	 * 
	 * @param json
	 * @return
	 */
	public static String queryAgentPolicydetail(String json) {
		String path = CLOUDQUERY + "/rnpolicy/queryAgentPolicydetail";
		//HttpClientUtil异步处理
        return HttpClientUtil.doPostJsonAsyncClientJson(path, json,"");
	}

	/**
	 * 查询保单列表信息接口liuchao
	 * 
	 * @param json
	 * @return
	 */
	public static String queryNotEnddateDetail(String json) {
		// String path = CLOUDQUERY + "/rnpolicy/queryNotEnddateDetail";
		// String path = "http://10.88.10.224:8080/cif" +
		// "/rnpolicy/queryAgentPolicydetail";
		String path = CLOUDQUERY + "/rnpolicy/queryAgentPolicydetail";
		//HttpClientUtil异步处理
        return HttpClientUtil.doPostJsonAsyncClientJson(path, json,"");
	}

	/**
	 * 查询保单详情接口liuchao
	 * 
	 * @param json
	 * @return
	 */
	public static String queryPolicyDetail(String json) {
		// String path = "http://10.88.10.224:8080/cif" +
		// "/rnpolicy/queryPolicyDetail";
		String path = CLOUDQUERY + "/rnpolicy/queryPolicyDetail1";
		//HttpClientUtil异步处理
        return HttpClientUtil.doPostJsonAsyncClientJson(path, json,"");
	}

	/**
	 * 出险信息查询(平台精灵)推送
	 * 
	 * @param taskSequence
	 *            流程ID
	 * @param personName
	 *            车主 名称
	 * @param vin
	 *            车架号
	 * @param carBrandName
	 *            品牌型号
	 * @param engineNum
	 *            发动机号
	 * @param firstRegDate
	 *            初登日期（必传）
	 * @param plateNum
	 *            车牌号
	 * @return
	 */
	public static String pullLastClaimBackInfo(String taskSequence,
			String areaid, String vin, String carBrandName, String engineNum,
			String plateNum, String firstRegDate) {
		String path = CLOUDQUERY + "/rnpolicy/querydetaillastaccident";
		
		try {
			InsuranceBean insuranceBean = new InsuranceBean();
			insuranceBean.setAreaId(areaid);
			insuranceBean.setTaskSequence(taskSequence);
			InParas params = new InParas();
			AccidentCarInfoBean carInfo = new AccidentCarInfoBean();
			carInfo.setVin(vin);
			carInfo.setCarBrandName(carBrandName);
			carInfo.setEngineNum(engineNum);
			carInfo.setPlateNum(plateNum);
			carInfo.setFirstRegDate(firstRegDate);
			params.setCarInfo(carInfo);
			insuranceBean.setInParas(params);
			String ce = JSONObject.fromObject(insuranceBean).toString();
			LogUtil.info("平台查询参数：taskid="+taskSequence +"	params="+ce);
			String ret = HttpClientUtil.doPostJsonAsyncClientJson(path, ce,"");
			LogUtil.info("平台查询出参：taskid="+taskSequence +"	ret="+ret);
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 出险信息查询(平台精灵) 推送
	 * 
	 * @param multiQuoteId
	 *            taskId
	 * @param lastClaimBackInfo
	 * @return
	 */
	public static void setLastClaimBackInfo(String taskId,
			LastClaimBackInfo lastClaimBackInfo) {
		lastClaimBackInfo.setCurrenttime(new Date());
		LogUtil.info("claim_" + taskId + "======>>>>>>>平台回写");
		CMRedisClient.getInstance().set(CLAIM_REDIS_KEY,  taskId, lastClaimBackInfo);
		return;
	}

	/**
	 * reids get 平台查询结果
	 * 
	 * @param multiQuoteId
	 *            taskId
	 * @param lastClaimBackInfo
	 * @return
	 */
	public static LastClaimBackInfo getLastClaimBackInfo(String taskId) {
		return CMRedisClient.getInstance().get(CLAIM_REDIS_KEY,  taskId, LastClaimBackInfo.class);

	}

	public static String jingYouCarModelSearch(Map<String, String> map)
			throws ClientProtocolException, IOException {
		return HttpClientUtil.doPostJson(
				CLOUDQUERY + "/output/getCarmodelList", map);
	}

	public static String jingYouCarModelSearchVin(Map<String, String> map)
			throws ClientProtocolException, IOException {
		return HttpClientUtil.doPostJson(CLOUDQUERY
				+ "/output/getCarmodelListVIN", map);
	}

	/**
	 * 获取车辆信息 用于渠道 创建报价接口
	 * 
	 * @param map
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getCarInfo4Channel(String json) {
		return HttpClientUtil.doPostJsonString(CLOUDQUERY
				+ "/output/getCarInfoAndModel", json);
	}

	public static String getOneCarModel(String vehicleId) {
		JSONObject obj = new JSONObject();
		obj.element("vehicleId", vehicleId);
		LogUtil.info("查询车型列表请求:" + obj.toString());
		String result = HttpClientUtil.doPostJsonString(CLOUDQUERY
				+ "/output/getCarmodelOne", obj.toString());
		LogUtil.info("查询车型列表返回:" + result);
		return result;
	}

	public static void main(String[] args) {
		getOneCarModel("402880882727cbc701272d3d2dd40973");
	}
}
