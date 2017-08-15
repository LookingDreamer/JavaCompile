package com.zzb.conf.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.app.model.ImageManagerModel;
import com.zzb.cm.controller.vo.InsRecordNumberVO;
import com.zzb.conf.entity.INSBPolicyitem;

public interface INSBPolicyitemService extends BaseService<INSBPolicyitem> {
	/**
	 * 通过任务id查询保单信息
	 * @param taskId
	 */
	public Map<String, Object> getPolicyNumInfo(String taskid);
	 
	/**
	 * 修改投保单信息
	 * @param insRecordNumber
	 */
	public String editPolicyNumInfo(InsRecordNumberVO insRecordNumber);
	
	/**
	 * 获取当前代理人下所有的保单影像信息
	 * @param agentid
	 * @return
	 */
	public String getImageInfoList(String agentid);
	/**
	 * 获取影像信息
	 * @param policyid
	 * @return
	 */
	public String getImageInfo(String policyid);
	
	/**
	 * 添加影像信息
	 * @param model
	 * @return
	 */
	public String cos(ImageManagerModel model);

	/**
	 * 修改保单号用于公共页面弹出框
	 * @param ciPolicyNo
	 * @param biPolicyNo
	 * @param taskid
	 */
	public String updatePolicyNumInfo(String ciPolicyNo, String biPolicyNo,String taskid,String inscomcode);

	/**
	 * 修改投保单号用于公共页面弹出框
	 * @param ciproposalno
	 * @param biproposalno
	 * @param taskid
	 * @return
	 */
	public String updateProposalNumInfo(String ciproposalno, String biproposalno,String taskid,String inscomcode);

	/**
	 * 根据taskid和inscomcode查询报价节点后的保单信息
	 * @param map
	 * @return
	 */
	public List<INSBPolicyitem> selectByInscomTask(Map<String, Object> map);
	
	public Map<String, Object> getPolicyNumInfo2(String taskId,String inscomcode);

	public List<INSBPolicyitem> getListByParam(Map<String, Object> param);
	
	/**
	 * 根据代理人工id查询该代理人下的保单
	 * @param param
	 * @return
	 */
	public Map<String,Object> policcyitembyagentid(Map<String, Object> param);

	public String updatePolicyNumInfo1(String cipolicyno, String bipolicyno,
			String taskid, String inscomcode);

    /**
     * 保单号检验
     * @param ciPolicyNo 交强险单号
     * @param biPolicyNo 商业险单号
     * @param taskid 任务id
     * @param inscomcode 保险公司编码
     * @return map
     */
    public Map<String,Object> verifyPolicyno(String ciPolicyNo, String biPolicyNo,String taskid,String inscomcode);
    /**
     * 获取电子保单
     * @param taskId
     * @param type 0-商业，1-交强
     * @return
     */
    public String getElecPolicyFilePath(String taskId, String type);
}