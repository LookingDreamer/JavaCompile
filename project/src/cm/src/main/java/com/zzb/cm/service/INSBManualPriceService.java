package com.zzb.cm.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.cninsure.core.dao.BaseService;
import com.cninsure.system.entity.INSCUser;
import com.zzb.cm.controller.vo.SupplementInfoVO;
import com.zzb.cm.entity.INSBSupplement;
import com.zzb.conf.entity.INSBWorkflowmain;
import com.zzb.mobile.model.lastinsured.CarModel;



public interface INSBManualPriceService extends BaseService<INSBWorkflowmain> {

	/**
	 * 报价通过或报价退回修改按钮调用接口
	 */
	public String quotePricePassOrBackForEdit(String instanceId, String userid, String quoteResult);
	
	/**
	 * 退回修改按钮调用接口
	 */
//	public String quotePriceBackForEdit(String instanceId, String inscomcode, String userid);
	
	/**
	 * 转人工处理按钮调用接口
	 */
//	public String quotePriceToManual(String instanceId, String inscomcode);
	
	/**
	 * 拒绝承保按钮调用接口
	 */
	public String refuseUnderwrite(String maininstanceId, String subinstanceId, String inscomcode, String mainorsub, String from, String operator);
	
	/**
	 * 拒绝承保按钮调用接口，前端调用wy
	 */
	public String refuseUnderwrite2(String maininstanceId, String subinstanceId, String inscomcode, String mainorsub, String from);
	
	/**
	 * 打回任务按钮调用接口
	 * 如果不存在子流程，只传递主流程实例id,不用传递保险公司code
	 */
	public String releaseTask(String instanceId, String inscomcode, int instanceType, INSCUser loginUser);
	
	/**
	 * 报价打回任务按钮调用接口
	 * 如果不存在子流程，只传递主流程实例id,不用传递保险公司code
	 */
	public String quoteReleaseTask(String instanceId, String inscomcode, int instanceType, INSCUser loginUser);
	/**
	 * 影像上传
	 * @param file 上传的文件
	 * @param fileType 文件类型
	 * @param fileDescribes 文件描述
	 * @param userCode 用户代码
	 * @param processinstanceid 实例id
	 * @return
	 */
	public Map<String, Object> fileUpLoad(HttpServletRequest request,MultipartFile file, String fileType,
			String fileDescribes, String userCode,String processinstanceid,String taskid);

	/**
	 * 规则试算调用接口
	 */
	public String ruleCalculation(String instanceId, String subInstanceId, String inscomcode, boolean priceLimitFlag);
	
	/**
	 * 获取补充信息中车型上面部分信息
	 */
	public Map<String, Object> getReplenishInfo(String instanceId);
	
	/**
	 * 获取平台车型信息
	 */
	public CarModel getPlatCarModelMessage(String instanceId);
	
	/**
	 * 获取补充信息中车型下面部分信息
	 */
	public Map<String, Object> getLocaldbReplenishInfo(String instanceId, String inscomcode, String deptCode);
	
	/**
	 * 修改补充信息
	 */
	public String editLocaldbReplenishInfo(String instanceId, String inscomcode, INSBSupplement supplementInfoVO);
	
	/**
	 * 查询补充信息
	 */
	public SupplementInfoVO getReplenishInfo(String instanceId, String inscomcode);
	
	/**
	 * 获取补充信息中车型下面部分信息
	 */
	public List<Map<String, Object>> getReplenishSelectItemsInfo(String instanceId, String inscomcode, String deptCode);
	public String quoteRefuseUnderwrite(String maininstanceId, String subinstanceId, String inscomcode, String mainorsub, String from, String operator);
	public String quoteTotailPricePassOrBackForEdit(String instanceId, String userid, String quoteResult, String incoms);
	
	
	/**
	 * 结束二次支付任务
	 * @param instanceId
	 * @return
	 */
	public Map<String,String> endPayTask(String instanceId,String usercode);
	/**
	 * 保存修改补充信息
	 */
	public String editLocaldbSupplementInfo(String inscoms, List<INSBSupplement> supplements);

}