package com.zzb.cm.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.cninsure.core.dao.BaseService;
import com.zzb.cm.entity.INSBFilelibrary;
import com.zzb.mobile.model.FileUploadModel;



public interface INSBFilelibraryService extends BaseService<INSBFilelibrary> {
	public String onlySaveNotUpload(String filePath,String fileName, String fileType, String fileDesc, String taskId, String type);
	
	public Map<String,Object> upload(HttpServletRequest request,MultipartFile file, String fileType, String fileDescribes, String userCode);
	public Map<String,Object> uploadOneFile(HttpServletRequest request,MultipartFile file, String fileType, String fileDescribes, String userCode);
	public Map<String,Object> uploadOneFile(HttpServletRequest request,String file,String fileName, String fileType, String fileDescribes, String userCode);
	public Map<String,Object> uploadOneFileByUrl(HttpServletRequest request,String url,String fileName, String fileType, String fileDescribes, String userCode);
	
	public Map<String, Object> appUpload(FileUploadModel model);
	public Map<String, Object> cmUpload(MultipartFile file,String fileType,String code,String operator);
	
	public Map<String, Object> uploadFiles(HttpServletRequest request,MultipartFile[] files, String filetype, String userCode, String[] filenames, String[] filedescribes, String[] fileclassifytype) throws Exception;
	public boolean uploadFile(MultipartFile[] files, String filepath, String filetype, String userCode, String[] filenames, String[] filedescribes, String fileclassifytype) throws Exception;
	
	public boolean uploadOneFile(HttpServletRequest request,MultipartFile file, String userCode, String filename, String filedescribe, String id, String fileclassifytype) throws Exception;

	public boolean uploadProLogo(HttpServletRequest request,String filepath,MultipartFile file,String userCode,String proId,String fileclassifytype)throws Exception;
	public List<INSBFilelibrary> initFileLibraryData(String filetype, String filename, String filedescribe,String fileclassifytype);
	
	public boolean deleteFileLibraryData(String[] ids);
	
	public List<Map<Object, Object>> initMenusList(String parentCode);
	/**
	 * 根据filebusinessCode查询
	 * @param code 
	 * @return
	 */
	public List<INSBFilelibrary> queryByFilebusinessCode(String code);
	
	public List<INSBFilelibrary> selectListByMap(Map<String, Object> map);
	
	public Map<String, Object> saveFileInfo(HttpServletRequest request,String mediaId,
			String fileName, String fileType, String fileDescribes,
			String userCode);
	
	public boolean uploadFileByCos(MultipartFile[] files, String filepath, String filetype, String userCode, String[] filenames, String[] filedescribes, String fileclassifytype) throws Exception;

	public Map<String,Object> uploadOneFileByCos(HttpServletRequest request,String file,String fileName, String fileType, String fileDescribes, String userCode,String taskId);
	@Deprecated
	public List<INSBFilelibrary> queryAll();
	public List<INSBFilelibrary> queryAll(int offset,int limit);


}