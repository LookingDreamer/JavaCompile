package com.zzb.conf.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBUsercommentUploadFile;

import java.util.List;
import java.util.Map;

/**
 * Created by liwucai on 2016/11/17 11:37.
 */
public interface INSBUsercommentUploadFileService extends BaseService<INSBUsercommentUploadFile> {
    public void deleteByUsercommentId(String commentId);

    public List<Map<String,String>> findFileIds(String taskid, List<String> filetypes);
}
