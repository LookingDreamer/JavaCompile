package com.cninsure.system.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.service.INSCCodeService;
import com.googlecode.ehcache.annotations.Cacheable;

import net.sf.json.JSONObject;

@Service
@Transactional
public class INSCCodeServiceImpl extends BaseServiceImpl<INSCCode> implements
		INSCCodeService {
	@Resource
	private INSCCodeDao inscCodeDao;

	@Override
	protected BaseDao<INSCCode> getBaseDao() {
		return inscCodeDao;
	}

	@Cacheable(cacheName = "inscCodeCache")
	@Override
	public List<INSCCode> queryINSCCodeByCode(String parentcode, String codetype) {
		Map<String, String> para = new HashMap<String, String>();
		if(!StringUtil.isEmpty(codetype)){
			para.put("codetype", codetype);
		}
		if(!StringUtil.isEmpty(parentcode)){
			para.put("parentcode", parentcode);
		}
		para.put("order", "codeorder");
		return inscCodeDao.selectINSCCodeByCode(para);
	}

	@Override
	public List<INSCCode> queryMyTaskCode(String parentcode, String codetype) {
		Map<String, String> para = new HashMap<String, String>();
		if(!StringUtil.isEmpty(codetype)){
			para.put("codetype", codetype);
		}
		if(!StringUtil.isEmpty(parentcode)){
			para.put("parentcode", parentcode);
		}
		return inscCodeDao.selectMyTaskCode(para);
	}

	@Cacheable(cacheName = "inscCodeNameCache")
	@Override
	public String transferValueToName(String parentcode,String codetype,String codevalue) {
		Map<String, String> para = new HashMap<String, String>();
		if(!StringUtil.isEmpty(codetype)){
			para.put("codetype", codetype);
		}
		if(!StringUtil.isEmpty(parentcode)){
			para.put("parentcode", parentcode);
		}
		if(!StringUtil.isEmpty(codevalue)){
			para.put("codevalue", codevalue);
			INSCCode insccode =  inscCodeDao.transferCodeToName(para);
			if(insccode!=null){
				return insccode.getCodename();
			}else{
				return codevalue;
			}
		}else{
			return codevalue;
		}
	}

	/**
	 * 车险任务管理页面任务类型下拉框选项  liuchao
	 */
	@Override
	public List<Map<String, String>> getWorkFlowNodesForCarTaskQuery() {
		return inscCodeDao.getWorkFlowNodesForCarTaskQuery();
	}
	
	@Override
	public String selectAllErrorCodeInfo(Map<String, Object> map) {
		int total = inscCodeDao.queryAllErrorCodeCount();
		List<Map<String, Object>> rows = inscCodeDao.queryAllErrorCode(map);
		Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("success", true);
        resultMap.put("total", total);
        resultMap.put("rows", rows);
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        return jsonObject.toString();
	}
	
	@Override
	public String saveEditCode(INSCCode editcode) {
		int res = 0;
		boolean suc = false;
		String message = "修改失败!";
		
		if(editcode != null && editcode.getId() != null){
			INSCCode oneCode = inscCodeDao.selectById(editcode.getId());
			if(oneCode != null){
				oneCode.setOperator(editcode.getOperator());
				oneCode.setNoti(editcode.getNoti());
				oneCode.setProp2(editcode.getProp2());
				oneCode.setCodename(editcode.getCodename());
				oneCode.setModifytime(new Date());
				res = inscCodeDao.updateById(oneCode);
			} else {
				message = "未查到对应错误信息,修改失败!";
			}
		}
		if(res == 1){
			suc = true;
			message = "修改成功!";
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("success", suc);
        resultMap.put("message", message);
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        return jsonObject.toString();
	}
}