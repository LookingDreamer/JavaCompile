package com.zzb.conf.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBUsercommentUploadFile;

import java.util.List;
import java.util.Map;

public interface INSBUsercommentUploadFileDao extends BaseDao<INSBUsercommentUploadFile> {

    /**
     *查询用户备注上传的文件
     * @param para 用户备注id
     * @return 备注上传文件列表
     */
    public List<INSBUsercommentUploadFile> selectUsercommentUploadFile_byCommId(Map<String, String> para);

    /**
     * 按备注id删除备注文件信息
     * @param userCommentId
     */
    public int deleteByUsercommentId(String userCommentId);

    public List<Map<String,String>> findFileIds(Map<String,Object> params);
}