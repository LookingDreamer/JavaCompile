package com.zzb.conf.dao.impl;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBUsercommentDao;
import com.zzb.conf.dao.INSBUsercommentUploadFileDao;
import com.zzb.conf.entity.INSBUsercomment;
import com.zzb.conf.entity.INSBUsercommentUploadFile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class INSBUsercommentUploadFileDaoImpl extends BaseDaoImpl<INSBUsercommentUploadFile> implements
		INSBUsercommentUploadFileDao {

	@Override
	public List<INSBUsercommentUploadFile> selectUsercommentUploadFile_byCommId(Map<String, String> para) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("select"), para);
	}

	@Override
	public int deleteByUsercommentId(String userCommentId) {
		return this.sqlSessionTemplate.delete(this.getSqlName("deleteByUserCommentId"), userCommentId);
	}

	@Override
	public List<Map<String,String>> findFileIds(Map<String,Object> params) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectFileIds"), params);
	}
}