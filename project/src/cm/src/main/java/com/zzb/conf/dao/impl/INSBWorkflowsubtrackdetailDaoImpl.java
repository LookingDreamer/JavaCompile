package com.zzb.conf.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.LogUtil;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.core.utils.StringUtil;
import com.zzb.conf.dao.INSBWorkflowsubtrackdetailDao;
import com.zzb.conf.entity.INSBWorkflowsub;
import com.zzb.conf.entity.INSBWorkflowsubtrackdetail;

@Repository
public class INSBWorkflowsubtrackdetailDaoImpl extends
		BaseDaoImpl<INSBWorkflowsubtrackdetail> implements
		INSBWorkflowsubtrackdetailDao {

	@Override
	public void myInsert(INSBWorkflowsub model) {
	    String feedback = model.getTaskfeedback();
	    if (StringUtil.isNotEmpty(feedback) && feedback.contains("#")) {
	        int idx = feedback.indexOf("#");
            String t1 = feedback.substring(0, idx);
            String t2 = feedback.substring(idx+1);
            model.setTaskfeedback(t1);
            model.setNoti(t2);
        }
        model.setCreatetime(new Date());
        INSBWorkflowsubtrackdetail detailModel = new INSBWorkflowsubtrackdetail();

        try {
            PropertyUtils.copyProperties(detailModel, model);
        } catch (Exception e) {
            e.printStackTrace();
        }

        detailModel.setId(null);
        detailModel.setModifytime(null);

        if(StringUtil.isEmpty(detailModel.getOperator())){//如果操作人为空，则设置为系统
            detailModel.setOperator("admin");
        }
        if(StringUtil.isEmpty(detailModel.getFromoperator())){//如果为空，则设置为系统
            detailModel.setFromoperator("admin");
        }

        List<INSBWorkflowsubtrackdetail> detailTempModels = copyTaskFeed4CompletedState(model.getInstanceid());

        if (detailTempModels != null && !detailTempModels.isEmpty()) {
            INSBWorkflowsubtrackdetail completedDetail = detailTempModels.get(0);

            if (completedDetail.getTaskcode().equals(model.getTaskcode()) && completedDetail.getTaskstate().equals(model.getTaskstate()) &&
                    ("Completed".equals(model.getTaskstate()) || "Closed".equals(model.getTaskstate()) ||
                            (StringUtil.isEmpty(completedDetail.getOperatorstate()) && detailModel.getOperator().equals(completedDetail.getOperator())) ||
                            (StringUtil.isNotEmpty(completedDetail.getOperatorstate()) && completedDetail.getOperatorstate().equals(model.getOperatorstate()) &&
                                    detailModel.getOperator().equals(completedDetail.getOperator())))) {

                boolean updatable = false;

                if (StringUtil.isNotEmpty(model.getTaskfeedback())) {
                    completedDetail.setTaskfeedback(model.getTaskfeedback());
                    updatable = true;
                }
                if (StringUtil.isNotEmpty(model.getOperator())) {
                    completedDetail.setOperator(model.getOperator());
                    updatable = true;
                }
                if (updatable) {
                    if ("Completed".equals(model.getTaskstate())) {
                        completedDetail.setModifytime(null);
                    }

                    LogUtil.info(model.getMaininstanceid()+","+model.getInstanceid()+","+model.getTaskcode()+","+model.getTaskstate()+
                            "更新subtrackdetail:"+model.getTaskfeedback()+","+model.getOperator());

                    updateById(completedDetail);
                }

                return;
            }
		}

        LogUtil.info(model.getMaininstanceid()+","+model.getInstanceid()+","+model.getTaskcode()+","+model.getTaskstate()+
                "新增subtrackdetail:"+model.getTaskfeedback()+","+model.getOperator());

        insert(detailModel);
	}

	@Override
	public INSBWorkflowsubtrackdetail selectLastModelBySubInstanceId(Map<String, String> param) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectLastModelBySubInstanceId"), param);
	}

    public List<INSBWorkflowsubtrackdetail> selectUpdatableData4RunQian(Map<String, String> param) {
        return this.sqlSessionTemplate.selectList(this.getSqlName("selectUpdatableData4RunQian"), param);
    }

    @Override
	public List<INSBWorkflowsubtrackdetail> selectData4RunQian(Map<String, String> param) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectData4RunQian"), param);
	}

    @Override
    public List<INSBWorkflowsubtrackdetail> selectData4RunCompleted(Map<String, String> param) {
        return this.sqlSessionTemplate.selectList(this.getSqlName("selectData4RunCompleted"),param);

    }

	@Override
	public List<INSBWorkflowsubtrackdetail> copyTaskFeed4CompletedState(String instanceId) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("copyTaskFeed4CompletedState"),instanceId);
		
	}

    public List<INSBWorkflowsubtrackdetail> selectLockConf(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectList(this.getSqlName("selectLockConf"), map);
    }

    public INSBWorkflowsubtrackdetail findPrevWorkFlow(String instanceid, String taskcode) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("instanceid",instanceid);
        param.put("taskcode",taskcode);
        List<INSBWorkflowsubtrackdetail> list = this.sqlSessionTemplate.selectList(this.getSqlName("findPrevWorkFlow"), param);
        for (INSBWorkflowsubtrackdetail workflowsubtrackdetail : list) {
            String code = workflowsubtrackdetail.getTaskcode();
            if("3".equals(code)||"4".equals(code)||"32".equals(code)
                    ||"6".equals(code)||"7".equals(code)||"8".equals(code)){
                return workflowsubtrackdetail;
            }
        }
        return null;
    }

    public List<INSBWorkflowsubtrackdetail> selectAllBy(Map<String, Object> params) {
        if (params == null || params.isEmpty()) return null;
        if (!params.containsKey("sort")) {
            params.put("sort", "createtime");
        }
        if (!params.containsKey("order")) {
            params.put("order", "DESC");
        }
        return this.sqlSessionTemplate.selectList(getSqlName("select"), params);
    }
}