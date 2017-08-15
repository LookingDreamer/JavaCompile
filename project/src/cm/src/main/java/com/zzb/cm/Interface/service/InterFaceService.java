package com.zzb.cm.Interface.service;

import java.util.Map;

import org.jivesoftware.smack.SmackException.NotConnectedException;

import com.common.ModelUtil;
import com.zzb.cm.Interface.model.CarModelInfoBean;
import com.zzb.cm.entity.INSBCarmodelinfohis;
import com.zzb.cm.entity.INSBFlowinfo;

import net.sf.json.JSONObject;
public interface InterFaceService {
	String DADIKEY = "cm:zzb:interface:dadikey";


	/**
	 * 发送消息到精灵，通知接口
	 * @throws NotConnectedException 
	 */ 
	public String goToFairyQuote(String processinstanceid,String incoid, String touserid, String taskType) throws Exception;
	/**
	 * 工作流调用edi任务接口
	 */
	public String goToEdiQuote(String processinstanceid,String incoid, String ediid, String taskType) throws Exception;
	/**
	 * 获取报文信息
	 * @param taskId
	 * @return
	 */
	public Map<String,Object> getPacket(String taskId, String insurancecompanyid, String processType,String monitorid,String taskType);
	/**
	 * 保存报文信息
	 * @param json
	 * @return
	 */
	public Map<String,String> savePacket(String json);
	/**
	 *任务处理完成调用工作流接口	
	 * 
	 */	
	public void TaskResultWriteBack(String taskid,String inscomcode,String userid ,String result, String quotename, String taskTyep, String skipStep, String taskName, String subTaskId);
	/**
	 * 任务状态变更
	 * @param taskId
	 * @return
	 */
	public Map<String,Object> updateTaskStatus(String taskId,String taskStatus,String processType);
	/**
	 * 消息
	 * @param taskId
	 * @return
	 */
	public Map<String,Object> getMessage(String taskId);
	
	/**
	 * 任务改派接口
	 * @return
	 */
	public String forwardTask(String taskid,String insurancecompanyid,String fromuser,String touser);

	/**
	 *任务锁定接口
	 * 
	 */	
	public String lockTask(String taskId,String isLocked,String insurancecompanyid);
	/**
	 *精灵任务回收接口
	 * 
	 */	
	public String recoverytask(String taskId,String taskType);

	/**
	 * 推送到cif接口
	 * @param taskid
	 * @param companyid
	 * @return
	 */
	public String pushtocif(String taskid, String companyid);
	
	/**
	 * 
	 * @param subtaskId 子流程（必传）
	 * @param taskId 主流程（必传）
	 * @param inscomcode 供应商（保险公司）（必传）
	 * @param taxPrice 含税价 （必传）
	 * @param price 不含税价 （必传）
	 * @param analogyTaxPrice 含税类比价 （null）
	 * @param analogyPrice 不含税类比价（null）
	 * @param regDate 初等日期 2015-12-10（必传）
	 * @param seats 座位数（必传）
	 * @param modelName 车型名称（必传）
	 * @param pointPrice 用户指定车价（null）
	 * @return 车型对象（null）
	 */
	public CarModelInfoBean getCarModelInfo(String vinCode, String subtaskId, String taskId,String inscomcode, INSBCarmodelinfohis carmodelinfo, String regDate, String pointPrice);
	
	/**
	 * 
	 * @param inscomcode 保险公司id（4位）
	 * @param type robot-精灵，edi-EDI
	 * @param singlesite 网点id
	 * @param key 加密信息
	 * @return
	 */
	public String getConfigInfo(String inscomcode,String type, String singlesite,String key);
	/**
	 * 供张伟调用的精灵通知接口
	 * @param processinstanceid
	 * @param incoid
	 * @param touserid
	 * @param taskType
	 * @param childTaskId
	 * @return
	 */
	public String goRobot(String processinstanceid, String incoid,
			String touserid, String taskType,String childTaskId) throws Exception;
	
	/**
	 * 供张伟调用的edi通知接口
	 * @param processinstanceid
	 * @param incoid
	 * @param ediid
	 * @param taskType
	 * @param childTaskId
	 * @return
	 * @throws Exception
	 */
	public String goEDi(String processinstanceid, String incoid, String ediid, String taskType,String childTaskId) throws Exception;
	
	/**
	 *
	 * @param subTaskId 子流程id
	 * @param processType  类型，edi，robot，person
	 * @param taskStatus  A-报价完成，B-核保完成，C-承保完成
	 * @return
	 */
	public Boolean saveHisInfo(String subTaskId, String processType, String taskStatus);
	
	public String valiedatePremiu(JSONObject savePack);
	
	public void insertOrUpdateFlowError(INSBFlowinfo flowinfo,String errorInfoJson);
	/**
	 * 规则平台查询获取初始数据
	 * @param taskId
	 * @param insurancecompanyid
	 * @param processType
	 * @return
	 */
	public Map<String,Object> getGzQueryData(String taskId, String insurancecompanyid, String processType,String taskType,String monitorid);

    /**
     * 获取车辆对应车型code
     * @param vehicleid 车型id
     * @param inscome 保险公司编码
     * @param taskid 任务id
     * @return
     */
    public String getRbCode(String vehicleid, String inscome, String taskid);

	/**
	 * 平安二维码支付、核保查询回写保存报文信息
	 * @param json
	 * @return
	 */
	public Map<String,String> saveQRCodePacket(String json);

	/**
	 * 支付平台发送消息到精灵，通知接口去核保查询（北京二维码流程）
	 * @throws NotConnectedException
	 */

	public String goToFairyQuote(String processinstanceid, String incoid, String touserid, String taskType, String callBackUrl, String orderPaymentId, String sid) throws Exception;
	
}