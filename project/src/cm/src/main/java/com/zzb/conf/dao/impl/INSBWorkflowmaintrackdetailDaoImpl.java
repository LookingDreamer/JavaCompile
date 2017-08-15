package com.zzb.conf.dao.impl;

import java.util.Date;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.core.utils.StringUtil;
import com.zzb.conf.dao.INSBWorkflowmaintrackdetailDao;
import com.zzb.conf.entity.INSBWorkflowmain;
import com.zzb.conf.entity.INSBWorkflowmaintrackdetail;

@Repository
public class INSBWorkflowmaintrackdetailDaoImpl extends BaseDaoImpl<INSBWorkflowmaintrackdetail> implements INSBWorkflowmaintrackdetailDao {

	@Override
	public void myInsert(INSBWorkflowmain model) {
		String feedback = model.getTaskfeedback();
		if (StringUtil.isNotEmpty(feedback) && feedback.contains("#")) {
			int idx = feedback.indexOf("#");
			String t1 = feedback.substring(0, idx);
			String t2 = feedback.substring(idx+1);
			model.setTaskfeedback(t1);
			model.setNoti(t2);
		}
		model.setCreatetime(new Date());
		if(StringUtil.isEmpty(model.getOperator())){
			model.setOperator("admin");
		}
		if(StringUtil.isEmpty(model.getFromoperator())){
			model.setFromoperator("admin");
		}

		if ("Completed".equals(model.getTaskstate()) || "Closed".equals(model.getTaskstate())) {
			saveOrUpdate(model);
		} else {
			INSBWorkflowmaintrackdetail detailModel = new INSBWorkflowmaintrackdetail();

			try {
				PropertyUtils.copyProperties(detailModel, model);
			} catch (Exception e) {
				e.printStackTrace();
			}
			detailModel.setId("");
			this.insert(detailModel);
		}
	}

	private void saveOrUpdate(INSBWorkflowmain model) {
		// 对于逻辑主键包括 Completed/Closed 只更新
		INSBWorkflowmaintrackdetail mainDetailModel = new INSBWorkflowmaintrackdetail();

		mainDetailModel.setInstanceid(model.getInstanceid());
		mainDetailModel.setTaskstate(model.getTaskstate());
		mainDetailModel.setTaskcode(model.getTaskcode());

		INSBWorkflowmaintrackdetail dbModel = this.selectOne(mainDetailModel);
		if (dbModel != null) {
			if (StringUtil.isEmpty(dbModel.getNoti())) {
				dbModel.setNoti(model.getNoti());
			}
			if (StringUtil.isNotEmpty(model.getTaskfeedback())) {
				dbModel.setTaskfeedback(model.getTaskfeedback());
			}
			if (StringUtil.isNotEmpty(model.getOperator())) {
				dbModel.setOperator(model.getOperator());
			}
			dbModel.setModifytime(null);
			this.updateById(dbModel);
		} else {
			INSBWorkflowmaintrackdetail detailModel = new INSBWorkflowmaintrackdetail();

			try {
				PropertyUtils.copyProperties(detailModel, model);
			} catch (Exception e) {
				e.printStackTrace();
			}
			detailModel.setId("");
			detailModel.setModifytime(null);
			this.insert(detailModel);
		}
	}

	@Override
	public INSBWorkflowmaintrackdetail selectData4RunQian(Map<String, String> param) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectData4RunQian"), param);
	}

	@Override
	public INSBWorkflowmaintrackdetail copyTaskFeed4CompletedState(String instanceId) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("copyTaskFeed4CompletedState"),instanceId);
	}
}