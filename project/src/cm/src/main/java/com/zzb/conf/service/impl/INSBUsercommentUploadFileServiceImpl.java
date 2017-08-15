package com.zzb.conf.service.impl;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBUsercommentUploadFileDao;
import com.zzb.conf.entity.INSBUsercommentUploadFile;
import com.zzb.conf.service.INSBUsercommentUploadFileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liwucai on 2016/11/17 11:39.
 */
@Service
@Transactional
public class INSBUsercommentUploadFileServiceImpl extends BaseServiceImpl<INSBUsercommentUploadFile> implements
        INSBUsercommentUploadFileService {
    @Resource
    private INSBUsercommentUploadFileDao insbUsercommentUploadFileDao;
    @Override
    protected BaseDao<INSBUsercommentUploadFile> getBaseDao() {
        return insbUsercommentUploadFileDao;
    }

    @Override
    public void deleteByUsercommentId(String commentId) {
        insbUsercommentUploadFileDao.deleteByUsercommentId(commentId);
    }
    @Override
    public List<Map<String,String>> findFileIds(String taskid,List<String> filetypes){
        Map<String,Object> params = new HashMap<>();
        params.put("taskid", taskid);
        params.put("filetypes", filetypes);
        return insbUsercommentUploadFileDao.findFileIds(params);
    }
}
