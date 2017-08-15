package com.cninsure.quartz.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zzb.cm.service.INSBManualPriceService;
import com.zzb.conf.dao.INSBWorkflowsubDao;
import com.zzb.conf.dao.INSBWorkflowsubtrackDao;
import com.zzb.conf.entity.INSBWorkflowsub;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.cninsure.core.utils.LogUtil;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class SimpleService  implements Serializable{
	private static final long serialVersionUID = 122323233244334343L;

    @Resource
    private INSBWorkflowsubDao insbWorkflowsubDao;

    @Resource
    private INSBWorkflowsubtrackDao insbWorkflowsubtrackDao;

    @Resource
    private INSBManualPriceService insbManualPriceService;

    private static Logger logger = LogUtil.getLogger();

    @Transactional(rollbackFor = Exception.class)
    public boolean expiredClose() {

        logger.info("开始执行超期关闭服务方法expiredClose！");
        List<Map> list = insbWorkflowsubDao.selectExpiredClose();

        List<INSBWorkflowsub> subList = new ArrayList<INSBWorkflowsub>();

        if (list == null || list.isEmpty()) {
            logger.info("无超期关闭任务！");
            return true;
        }
        for(Map map : list) {
            INSBWorkflowsub sub = new INSBWorkflowsub();
            sub.setId((String) map.get("id"));
            sub.setInstanceid((String) map.get("instanceid"));
            sub.setMaininstanceid((String) map.get("maininstanceid"));
            sub.setStarttaskid((String) map.get("starttaskid"));
            sub.setTaskid((String) map.get("taskid"));
            sub.setTaskcode("40");
            sub.setTaskstate((String) map.get("taskstate"));
            sub.setCreateby((String) map.get("createby"));
            sub.setCreatetime((Date) map.get("createtime"));
            sub.setModifytime((Date) map.get("modifytime"));
            sub.setOperator((String) map.get("operator"));
            sub.setNoti((String) map.get("noti"));
            sub.setGroupid((String) map.get("groupid"));
            sub.setOperatorstate((String) map.get("operatorstate"));

            subList.add(sub);

            //insbWorkflowsubDao.updateById(sub);
            //insbWorkflowsubtrackDao.insertBySubTable(sub);
            //insbManualPriceService.refuseUnderwrite(sub.getMaininstanceid(), sub.getInstanceid(),(String)map.get("inscomcode"), "main", "back");
        }

        insbWorkflowsubDao.updateBatch(subList);

        insbWorkflowsubtrackDao.insertBatchBySubTable(subList);

        logger.info("执行超期关闭服务方法expiredClose完成！！！");

        return true;
    }

    public void setInsbWorkflowsubDao(INSBWorkflowsubDao insbWorkflowsubDao) {
        this.insbWorkflowsubDao = insbWorkflowsubDao;
    }

    public void setInsbWorkflowsubtrackDao(INSBWorkflowsubtrackDao insbWorkflowsubtrackDao) {
        this.insbWorkflowsubtrackDao = insbWorkflowsubtrackDao;
    }

    public void setInsbManualPriceService(INSBManualPriceService insbManualPriceService) {
        this.insbManualPriceService = insbManualPriceService;
    }
}
