package com.zzb.cm.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.cm.entity.INSBFilelibrary;
import com.zzb.cm.entity.INSBFilelibraryUploadCosFail;
import com.zzb.mobile.model.FileUploadModel;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


public interface INSBFilelibraryUploadCosFailService extends BaseService<INSBFilelibraryUploadCosFail> {

	public boolean deleteFileLibraryData(String[] ids);

	public List<INSBFilelibraryUploadCosFail> selectListByMap(Map<String, Object> map);
	
	public void saveFileInfo(INSBFilelibraryUploadCosFail filelibraryUploadCosFail);

	public List<INSBFilelibraryUploadCosFail> queryAll(int offset, int limit);
	public List<INSBFilelibraryUploadCosFail> selectOneTimeReUploadCos();
	public void uploadCosTask();
}