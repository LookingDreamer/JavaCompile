package com.zzb.conf.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBWorkflowmaintrackdetailDao;
import com.zzb.conf.dao.INSBWorkflowsubDao;
import com.zzb.conf.dao.INSBWorkflowsubtrackdetailDao;
import com.zzb.conf.entity.INSBWorkflowmaintrackdetail;
import com.zzb.conf.entity.INSBWorkflowsubtrackdetail;
import com.zzb.conf.service.INSBWorkflowsubtrackdetailService;

@Service
@Transactional
public class INSBWorkflowsubtrackdetailServiceImpl extends BaseServiceImpl<INSBWorkflowsubtrackdetail> implements INSBWorkflowsubtrackdetailService {
	@Resource
	private INSBWorkflowsubtrackdetailDao insbWorkflowsubtrackdetailDao;
	@Resource
	private INSBWorkflowmaintrackdetailDao insbWorkflowmaintrackdetailDao;
	@Resource
	private INSBWorkflowsubDao  subDao;

	@Override
	protected BaseDao<INSBWorkflowsubtrackdetail> getBaseDao() {
		return insbWorkflowsubtrackdetailDao;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean addWorkFlowEndStatus(Map<String, String> param) {
		
		if(param==null || param.isEmpty()){
			return false;
		}

        if (StringUtil.isEmpty(param.get("operator"))) {
            LogUtil.error(param.get("mainTaskId")+","+param.get("subTaskId")+"更新trackdetail时操作人为空");
        }
        if (StringUtil.isNotEmpty(param.get("taskFeedBack")) && param.get("taskFeedBack").length() > 200) {
            param.put("taskFeedBack", param.get("taskFeedBack").substring(0, 197)+"...");
        }
		
		//判断当前是主流程还是子流程
		if(!StringUtils.isEmpty(param.get("subTaskId"))){
			String main="";
			//主流程为空查询
			if(StringUtils.isEmpty(param.get("mainTaskId"))){
				main = subDao.selectMainInstanceIdBySubInstanceId(param.get("subTaskId"));
			}else{
				main = param.get("mainTaskId");
			}

			INSBWorkflowsubtrackdetail subDetailModel = new INSBWorkflowsubtrackdetail();
			subDetailModel.setMaininstanceid(main);
			subDetailModel.setInstanceid(param.get("subTaskId"));
			subDetailModel.setTaskstate(param.get("taskState"));
			subDetailModel.setTaskcode(param.get("taskCode"));

            Map<String, Object> params = new HashMap<>(2);
            params.put("maininstanceid", subDetailModel.getMaininstanceid());
            params.put("instanceid", subDetailModel.getInstanceid());
			
			List<INSBWorkflowsubtrackdetail> dbModels = insbWorkflowsubtrackdetailDao.selectAllBy(params);
			
			if(null!=dbModels && !dbModels.isEmpty()){
				for(INSBWorkflowsubtrackdetail dbModel : dbModels){
                    //第一条就是该状态的数据才更新
                    if (dbModel.getTaskcode().equals(subDetailModel.getTaskcode()) && dbModel.getTaskstate().equals(subDetailModel.getTaskstate())) {
                        if (StringUtil.isNotEmpty(param.get("taskFeedBack"))) {
                            dbModel.setTaskfeedback(param.get("taskFeedBack"));
                        }

                        if (StringUtil.isNotEmpty(param.get("operator"))) {
                            dbModel.setOperator(param.get("operator"));
                        } else if (StringUtil.isEmpty(dbModel.getOperator())) {
                            dbModel.setOperator("admin");
                        }

                        insbWorkflowsubtrackdetailDao.updateById(dbModel);
                        //历史数据问题，只修改其中一个记录就返回
                        return true;
                    }

                    //不是第一条，说明中间有其他状态的流程轨迹，需要新增
                    break;
				}
			}

            subDetailModel.setCreatetime(new Date());
            subDetailModel.setTaskfeedback(param.get("taskFeedBack"));
            if (StringUtil.isNotEmpty(param.get("operator"))) {
                subDetailModel.setOperator(param.get("operator"));
            } else {
                subDetailModel.setOperator("admin");
            }

            insbWorkflowsubtrackdetailDao.insert(subDetailModel);
			return true;

		}else{
			if(StringUtils.isEmpty(param.get("mainTaskId"))){
				return false;
			}
			INSBWorkflowmaintrackdetail mainDetailModel = new INSBWorkflowmaintrackdetail();
			
			mainDetailModel.setInstanceid(param.get("mainTaskId"));
			mainDetailModel.setTaskstate(param.get("taskState"));
			mainDetailModel.setTaskcode(param.get("taskCode"));
			
			INSBWorkflowmaintrackdetail dbModel = insbWorkflowmaintrackdetailDao.selectOne(mainDetailModel);

			if(dbModel !=null){
                if (StringUtil.isNotEmpty(param.get("taskFeedBack"))) {
                    dbModel.setTaskfeedback(param.get("taskFeedBack"));
                }

                if (StringUtil.isNotEmpty(param.get("operator"))) {
                    dbModel.setOperator(param.get("operator"));
                } else if (StringUtil.isEmpty(dbModel.getOperator())) {
                    dbModel.setOperator("admin");
                }

                insbWorkflowmaintrackdetailDao.updateById(dbModel);
			}else{
				mainDetailModel.setCreatetime(new Date());
				mainDetailModel.setTaskfeedback(param.get("taskFeedBack"));
                if (StringUtil.isNotEmpty(param.get("operator"))) {
                    mainDetailModel.setOperator(param.get("operator"));
                } else {
                    mainDetailModel.setOperator("admin");
                }
				insbWorkflowmaintrackdetailDao.insert(mainDetailModel);
			}
			return true;
		}
	}

    public boolean addWorkFlowEndStatus(String mainTaskId, String subTaskId, String taskFeedBack, String taskCode, String taskState, String taskName, String operator) {
        Map<String, String> param = new HashMap<>();

        try {
            param.put("mainTaskId", mainTaskId);
            if(!taskName.contains("承保")){
                param.put("subTaskId", subTaskId);
            }
            param.put("taskFeedBack", taskFeedBack);
            param.put("taskState", taskState);
            param.put("taskCode", taskCode);
            param.put("operator", operator);

            return addWorkFlowEndStatus(param);
        } catch (Exception e) {
            LogUtil.error("更新"+mainTaskId+","+subTaskId+"trackdetail的taskFeedBack出错：" + e.getMessage());
            return false;
        }
    }
}