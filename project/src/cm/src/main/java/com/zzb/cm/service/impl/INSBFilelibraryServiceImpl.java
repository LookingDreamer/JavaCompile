package com.zzb.cm.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.zzb.cm.dao.INSBFilelibraryUploadCosFailDao;
import com.zzb.cm.entity.INSBFilelibraryUploadCosFail;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.tools.util.ValidateUtil;
import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.service.INSCCodeService;
import com.common.AffineTransImageUtil;
import com.common.CosUtils;
import com.zzb.cm.dao.INSBFilebusinessDao;
import com.zzb.cm.dao.INSBFilelibraryDao;
import com.zzb.cm.entity.INSBFilebusiness;
import com.zzb.cm.entity.INSBFilelibrary;
import com.zzb.cm.service.INSBFilelibraryService;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.dao.INSBProviderDao;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.mobile.model.FileUploadModel;
import com.zzb.mobile.util.DownloadWxFile;
import com.zzb.mobile.util.EncodeUtils;



@Service
@Transactional
public class INSBFilelibraryServiceImpl extends BaseServiceImpl<INSBFilelibrary> implements
		INSBFilelibraryService {
	@Resource
	private INSBFilelibraryDao insbFilelibraryDao;
	@Resource
	private INSCCodeService inscCodeService;
	@Resource
	private INSBPolicyitemDao insbPolicyitemDao;
	@Resource
	private INSBFilebusinessDao insbFilebusinessDao;
	@Resource
	private INSBProviderDao insbProviderDao;
	@Resource
	private INSBFilelibraryUploadCosFailDao filelibraryUploadCosFailDao;
	@Override
	protected BaseDao<INSBFilelibrary> getBaseDao() {
		return insbFilelibraryDao;
	}

	/**
	 * 只存数据，不上传文件
	 * @return
	 */
	@Override
	public String onlySaveNotUpload(String filePath,String fileName, 
			String fileType, String fileDesc, String taskId, String type){
		INSBFilelibrary insbFilelibrary = new INSBFilelibrary();
		insbFilelibrary.setOperator("fairywb");
		insbFilelibrary.setCreatetime(new Date());
		insbFilelibrary.setFiledescribe(fileDesc);
		insbFilelibrary.setFilename(fileName);
		insbFilelibrary.setFilepath(filePath);
		insbFilelibrary.setFiletype(fileType);
		insbFilelibraryDao.insert(insbFilelibrary);
		String filelibraryid = insbFilelibrary.getId();
		INSBFilebusiness business = new INSBFilebusiness();//业务表
		business.setOperator(insbFilelibrary.getOperator());
		business.setFilelibraryid(filelibraryid);
		business.setCode(taskId);
		business.setCreatetime(new Date());
		business.setType(type);
		insbFilebusinessDao.insert(business);
		return "success";
	}
	
	
	@Override
	public Map<String,Object> upload(HttpServletRequest request,MultipartFile file, String fileType,
			String fileDescribes, String userCode) {
		INSCCode inscCode = new INSCCode();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		if(inscCodeService.queryINSCCodeByCode("insuranceimage", fileType).size()>0){
			inscCode = inscCodeService.queryINSCCodeByCode("insuranceimage", fileType).get(0);
		}else{
			resultMap.put("status", "error");
			resultMap.put("msg", "查询字典表错误");
			return resultMap;
		}
		String filetype="";
		if(file.getOriginalFilename().contains("."))
			filetype = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1,file.getOriginalFilename().length());
		String uuid = UUIDUtils.random();
		String currentDate = DateUtil.getCurrentDate();
		File tempFile;
		if(StringUtil.isEmpty(ValidateUtil.getConfigValue("uploadimage.path"))){
			tempFile = new File(request.getSession().getServletContext().getRealPath("/") +"/static/upload/"+ currentDate + "/" + uuid +"."+ filetype);
		}else{
			tempFile = new File(ValidateUtil.getConfigValue("uploadimage.path") +"/static/upload/"+ currentDate + "/" + uuid +"."+ filetype);
		}
		if(!tempFile.getParentFile().exists()){
			tempFile.getParentFile().mkdirs();
		}
		try {
			FileCopyUtils.copy(file.getBytes(), tempFile);
		} catch (IOException e) {
			e.printStackTrace();
			LogUtil.info("保存文件出错");
			resultMap.put("status", "error");
			resultMap.put("msg", "保存文件出错");
			return resultMap;
		}
		
		INSBFilelibrary insbFilelibrary = new INSBFilelibrary();
		String fileName = file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf("."));
		insbFilelibrary.setFilecodevalue(inscCode.getCodevalue());
		insbFilelibrary.setOperator(userCode);
		insbFilelibrary.setCreatetime(new Date());
		insbFilelibrary.setFiledescribe(fileDescribes);
		insbFilelibrary.setFilename(fileName);
		insbFilelibrary.setFilepath(ValidateUtil.getConfigValue("uploadimage.url")+"/static/upload/" + currentDate + "/" + uuid +"."+ filetype);
		insbFilelibrary.setFiletype(fileType);
		if(StringUtil.isEmpty(ValidateUtil.getConfigValue("uploadimage.path"))){
			insbFilelibrary.setFilephysicalpath(request.getSession().getServletContext().getRealPath("/") +"/static/upload/"+ currentDate + "/" + uuid +"."+ filetype);
		}else{
			insbFilelibrary.setFilephysicalpath(ValidateUtil.getConfigValue("uploadimage.path") +"/static/upload/"+ currentDate + "/" + uuid +"."+ filetype);
		}
		//把http替换成https
		this.replaceHttpToHttps(insbFilelibrary);
		
		insbFilelibraryDao.insert(insbFilelibrary);
		resultMap.put("status", "success");
		resultMap.put("msg", "保存成功");
		resultMap.put("filetype", fileType);
		resultMap.put("filepath", insbFilelibrary.getFilepath());
		return resultMap;
	}
	
	/** 
	 * app文件上传通用接口
	 */
	@Override
	public Map<String, Object> appUpload(FileUploadModel model) {
		INSCCode inscCode = new INSCCode();
		Date date = new Date();
		String operator = "";
		INSBPolicyitem policyitem = null;
		if(!StringUtil.isEmpty(model)){
			policyitem = insbPolicyitemDao.selectPolicyitemByTaskId(model.getTaskid());
		}
		if(!StringUtil.isEmpty(policyitem)){
			operator =  policyitem.getOperator();
		}
		Map<String,Object> resultMap = new HashMap<String,Object>();
		if(inscCodeService.queryINSCCodeByCode("insuranceimage", model.getFiletype()).size()>0){
			inscCode = inscCodeService.queryINSCCodeByCode("insuranceimage", model.getFiletype()).get(0);
		}else{
			resultMap.put("status", "error");
			resultMap.put("msg", "查询字典表错误");
			return resultMap;
		}
		String currentDate = DateUtil.getCurrentDate();
		File tempFile;
		if(StringUtil.isEmpty(ValidateUtil.getConfigValue("uploadimage.path"))){
			tempFile = new File(ValidateUtil.getConfigValue("upload.file.directory") + "/static/upload/" + currentDate + "/" + model.getFile().getOriginalFilename());
		}else{
			tempFile = new File(ValidateUtil.getConfigValue("uploadimage.path") + "/static/upload/" + currentDate + "/" + model.getFile().getOriginalFilename());
		}
		if(!tempFile.getParentFile().exists()){
			tempFile.getParentFile().mkdirs();
		}
		try {
			FileCopyUtils.copy(model.getFile().getBytes(), tempFile);
		} catch (IOException e) {
			e.printStackTrace();
			LogUtil.info("保存文件出错");
			resultMap.put("status", "error");
			resultMap.put("msg", "保存文件出错");
			return resultMap;
		}
		try {
			INSBFilelibrary insbFilelibrary = new INSBFilelibrary();//附件表
			String fileName = model.getFile().getOriginalFilename().substring(0,model.getFile().getOriginalFilename().lastIndexOf("."));
			insbFilelibrary.setFilecodevalue(inscCode.getCodevalue());
			insbFilelibrary.setOperator(operator);
			insbFilelibrary.setCreatetime(date);
			insbFilelibrary.setFiledescribe(model.getFiledescribe());
			insbFilelibrary.setFilename(fileName);
			insbFilelibrary.setFilepath(ValidateUtil.getConfigValue("uploadimage.url") + "/static/upload/" + currentDate + "/" + model.getFile().getOriginalFilename());
			insbFilelibrary.setFiletype(model.getFiletype());
			
			//把http替换成https
			this.replaceHttpToHttps(insbFilelibrary);
			insbFilelibraryDao.insert(insbFilelibrary);
			String filelibraryid = insbFilelibrary.getId();
			INSBFilebusiness business = new INSBFilebusiness();//业务表
			business.setOperator(operator);
			business.setFilelibraryid(filelibraryid);
			business.setCode(model.getTaskid());
			business.setCreatetime(date);
			business.setType(model.getFiletype());
			insbFilebusinessDao.insert(business);
			resultMap.put("status", "success");
			resultMap.put("msg", "保存成功");
		} catch (Exception e) {
			resultMap.put("status", "error");
			resultMap.put("msg", "保存失败");
		}
		return resultMap;
	}
	
	/**
	 * cm通用接口
	 * @param file 文件
	 * @param fileType 对应insccode表codetype
	 * @param code 对应业务的唯一标识
	 * @param operator 操作人
	 * @see com.zzb.cm.service.INSBFilelibraryService#cmUpload(org.springframework.web.multipart.MultipartFile, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Map<String, Object> cmUpload(MultipartFile file, String fileType,
			String code, String operator) {
		INSCCode inscCode = new INSCCode();
		Date date = new Date();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		if(inscCodeService.queryINSCCodeByCode("insuranceimage", fileType).size()>0){
			inscCode = inscCodeService.queryINSCCodeByCode("insuranceimage", fileType).get(0);
		}else{
			resultMap.put("status", "error");
			resultMap.put("msg", "查询字典表错误");
			return resultMap;
		}
		String currentDate = DateUtil.getCurrentDate();
		File tempFile;
		if(StringUtil.isEmpty(ValidateUtil.getConfigValue("uploadimage.path"))){
			tempFile = new File(ValidateUtil.getConfigValue("upload.file.directory") + "/" + currentDate + "/" + file.getOriginalFilename());
		}else{
			tempFile = new File(ValidateUtil.getConfigValue("uploadimage.path") + "/" + currentDate + "/" + file.getOriginalFilename());
		}
		if(!tempFile.getParentFile().exists()){
			tempFile.getParentFile().mkdirs();
		}
		try {
			FileCopyUtils.copy(file.getBytes(), tempFile);
		} catch (IOException e) {
			e.printStackTrace();
			LogUtil.info("保存文件出错");
			resultMap.put("status", "error");
			resultMap.put("msg", "保存文件出错");
			return resultMap;
		}
		try {
			INSBFilelibrary insbFilelibrary = new INSBFilelibrary();//附件表
			String fileName = file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf("."));
			insbFilelibrary.setFilecodevalue(inscCode.getCodevalue());
			insbFilelibrary.setOperator(operator);
			insbFilelibrary.setCreatetime(date);
//			insbFilelibrary.setFiledescribe(model.getFiledescribe());描述
			insbFilelibrary.setFilename(fileName);
			insbFilelibrary.setFilepath(ValidateUtil.getConfigValue("uploadimage.url") + "/" + currentDate + "/" + file.getOriginalFilename());
			insbFilelibrary.setFiletype(fileType);
			//把http替换成https
			this.replaceHttpToHttps(insbFilelibrary);
			insbFilelibraryDao.insert(insbFilelibrary);
			String filelibraryid = insbFilelibrary.getId();
			INSBFilebusiness business = new INSBFilebusiness();//业务表
			business.setOperator(operator);
			business.setFilelibraryid(filelibraryid);
			business.setCode(code);
			business.setCreatetime(date);
			business.setType(fileType);
			insbFilebusinessDao.insert(business);
			resultMap.put("status", "success");
			resultMap.put("msg", "保存成功");
		} catch (Exception e) {
			resultMap.put("status", "error");
			resultMap.put("msg", "保存失败");
		}
		return resultMap;
	}
	@Override
	public boolean uploadFile(MultipartFile[] files, String filepath, String filetype,
			String userCode, String[] filenames,String[] filedescribes, String fileclassifytype) throws Exception{
		INSCCode inscCode = new INSCCode();
		inscCode.setParentcode("fileuploadtype");
		inscCode.setCodevalue(filetype);
		inscCode.setCodetype("fileuploadtype");
		if(files!=null&&files.length>0){  
            for(int i = 0;i<files.length;i++){  
                MultipartFile file = files[i];
                String fileName = "";
                String filedescribe = "";
                if (!file.isEmpty()) {
                	if(StringUtil.isEmpty(filenames[i])){
                		fileName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
                	}else{
                		fileName = filenames[i];
                	}
                	if(StringUtil.isEmpty(filedescribes[i])){
                		filedescribe = file.getOriginalFilename();
                	}else{
                		filedescribe = filedescribes[i];
                	}
            		String fileType="";
            		if(file.getOriginalFilename().contains("."))
            			fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1,file.getOriginalFilename().length());
            		String uuid = UUIDUtils.random();
            		String currentDate = DateUtil.getCurrentDate();
            		
            		File tempFile = new File(filepath + "/static/upload/" + filetype  + "/" + currentDate + "/" + uuid +"."+ fileType);
            		if(!tempFile.getParentFile().exists()){
            			tempFile.getParentFile().mkdirs();
            		}
            		FileCopyUtils.copy(file.getBytes(), tempFile);
            		
            		INSBFilelibrary insbFilelibrary = new INSBFilelibrary();
            		insbFilelibrary.setOperator(userCode);
            		insbFilelibrary.setCreatetime(new Date());
            		insbFilelibrary.setFiledescribe(filedescribe);
            		insbFilelibrary.setFilename(fileName);
            		insbFilelibrary.setFilepath(ValidateUtil.getConfigValue("uploadimage.url")+"/static/upload/" + filetype  + "/" + currentDate + "/" + uuid +"."+ fileType);
            		insbFilelibrary.setFiletype(filetype);
            		insbFilelibrary.setFilecodevalue(fileclassifytype);
            		insbFilelibrary.setFilephysicalpath(filepath + "/static/upload/" + filetype  + "/" + currentDate + "/" + uuid +"."+ fileType);
            		//把http替换成https
        			this.replaceHttpToHttps(insbFilelibrary);
            		insbFilelibraryDao.insert(insbFilelibrary);
                }  
            }  
        }  
		return false;
	}

	@Override
	public List<INSBFilelibrary> initFileLibraryData(String filetype, String filename, String filedescribe, String fileclassifytype) {
//		INSBFilelibrary insbFilelibrary = new INSBFilelibrary();
//		insbFilelibrary.setFiletype(filetype);
		Map<String,Object> map=new HashMap<String,Object>();
		if(!StringUtil.isEmpty(fileclassifytype)){
//			insbFilelibrary.setFilecodevalue(fileclassifytype);
			map.put("filecodevalue", fileclassifytype);
		}
		if(!StringUtil.isEmpty(filetype)){
			map.put("filetype", filetype);
//			insbFilelibrary.setFiletype(filetype);
		}
		if(!StringUtil.isEmpty(filename)){
//			insbFilelibrary.setFilename(filename);
			if(!filename.equals("undefined")){				
				map.put("filename", filename);	
			}
		}
		if(!StringUtil.isEmpty(filedescribe)){
//			insbFilelibrary.setFiledescribe(filedescribe);
			if(!filedescribe.equals("undefined")){
				map.put("filedescribe", filedescribe);
			}
		}
		
		return this.selectListByMap(map);
		//return insbFilelibraryDao.selectList(insbFilelibrary);
	}

	@Override
	public boolean deleteFileLibraryData(String[] ids) {
		List<String> idList = new ArrayList<String>();
		for (int i = 0; i < ids.length; i++) {
			idList.add(ids[i]);
			INSBFilelibrary tempInsbFilelibrary = new INSBFilelibrary();
			tempInsbFilelibrary = insbFilelibraryDao.selectById(ids[i]);
			if(tempInsbFilelibrary!=null&&!StringUtil.isEmpty(tempInsbFilelibrary.getFilephysicalpath()))DeleteFolder(tempInsbFilelibrary.getFilephysicalpath());
		}
		insbFilelibraryDao.deleteByIdInBatch(idList); //附件库

		insbFilebusinessDao.deleteByFilelibraryids(idList); // 业务-附件关联表
		return true;
	}
	
	private boolean DeleteFolder(String sPath) {  
	    boolean flag = false;  
	    File file = new File(sPath);  
	    if (!file.exists()) {
	        return flag;  
	    } else {  
	        if (file.isFile()) { 
	        	file.delete(); 
	        	flag = true;
	        } 
	    }
	    return flag;
	}

	@Override
	public boolean uploadOneFile(HttpServletRequest request,MultipartFile file, String userCode,
			String filename, String filedescribe, String id, String fileclassifytype) throws Exception {
		INSBFilelibrary temInsbFilelibrary = insbFilelibraryDao.selectById(id);
		temInsbFilelibrary.setFilename(filename);
		temInsbFilelibrary.setFiledescribe(filedescribe);
		temInsbFilelibrary.setFilecodevalue(fileclassifytype);
		if(StringUtil.isEmpty(file)||file.isEmpty()){
			insbFilelibraryDao.updateById(temInsbFilelibrary);
			return true;
		}else{
			INSCCode inscCode = new INSCCode();
			inscCode.setParentcode("img");
			inscCode.setCodevalue(fileclassifytype);
			inscCode.setCodetype("imgclassify");
			String oldFilePath = temInsbFilelibrary.getFilephysicalpath();
			if(!StringUtil.isEmpty(oldFilePath))DeleteFolder(oldFilePath);
			

			String filetype="";
			if(file.getOriginalFilename().contains("."))
				filetype = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1,file.getOriginalFilename().length());
			String uuid = UUIDUtils.random();
			String currentDate = DateUtil.getCurrentDate();
			String newFilePath ;
			if(StringUtil.isEmpty(ValidateUtil.getConfigValue("uploadimage.path"))){
				newFilePath = request.getSession().getServletContext().getRealPath("/") + "/static/upload/" + temInsbFilelibrary.getFiletype() + "/" + currentDate + "/" +uuid+"."+filetype;
			}else{
				newFilePath = ValidateUtil.getConfigValue("uploadimage.path") + "/static/upload/" + temInsbFilelibrary.getFiletype() + "/" + currentDate + "/" +uuid+"."+filetype;
			}
			File tempFile = new File(newFilePath);
			if(!tempFile.getParentFile().exists()){
    			tempFile.getParentFile().mkdirs();
    		}
    		FileCopyUtils.copy(file.getBytes(), tempFile);

    		temInsbFilelibrary.setFiledescribe(filedescribe);
    		temInsbFilelibrary.setFilename(filename);
    		temInsbFilelibrary.setFilepath(ValidateUtil.getConfigValue("uploadimage.url") + "/static/upload/" + temInsbFilelibrary.getFiletype() + "/" + currentDate + "/" +uuid+"."+filetype);
    		temInsbFilelibrary.setFilecodevalue(fileclassifytype);
    		temInsbFilelibrary.setFilephysicalpath(newFilePath);
    		temInsbFilelibrary.setModifytime(new Date());
    		insbFilelibraryDao.updateById(temInsbFilelibrary);
    		return true;
		}
	}

	@Override
	public List<Map<Object, Object>> initMenusList(String parentCode) {
		List<Map<Object, Object>> resultList = new ArrayList<>();
		if("".equals(parentCode)||"0".equals(parentCode)||"source".equals(parentCode)){
			Map<Object,Object> tempMap = new HashMap<Object,Object>();
			Map<Object,Object> dataAttributes = new HashMap<Object,Object>();
			Map<Object,Object> tempMap1 = new HashMap<Object,Object>();
			Map<Object,Object> dataAttributes1 = new HashMap<Object,Object>();
			Map<Object,Object> tempMap2 = new HashMap<Object,Object>();
			Map<Object,Object> dataAttributes2 = new HashMap<Object,Object>();
			Map<Object,Object> tempMap3 = new HashMap<Object,Object>();
			Map<Object,Object> dataAttributes3 = new HashMap<Object,Object>();
			dataAttributes.put("id", "img");
			dataAttributes.put("parentnodecode", "0");
			tempMap.put("name", "图片分类");
			tempMap.put("dataAttributes", dataAttributes);
			tempMap.put("type", "folder");
			dataAttributes1.put("id", "video");
			dataAttributes1.put("parentnodecode", "0");
			tempMap1.put("name", "视频分类");
			tempMap1.put("dataAttributes", dataAttributes1);
			tempMap1.put("type", "folder");
			dataAttributes2.put("id", "audio");
			dataAttributes2.put("parentnodecode", "0");
			tempMap2.put("name", "音频分类");
			tempMap2.put("dataAttributes", dataAttributes2);
			tempMap2.put("type", "folder");
			dataAttributes3.put("id", "other");
			dataAttributes3.put("parentnodecode", "0");
			tempMap3.put("name", "附件分类");
			tempMap3.put("dataAttributes", dataAttributes3);
			tempMap3.put("type", "folder");
			resultList.add(tempMap);
			resultList.add(tempMap1);
			resultList.add(tempMap2);
			resultList.add(tempMap3);
		}else{
			List<INSCCode> tempListInscCode = new ArrayList<INSCCode>();
			tempListInscCode = inscCodeService.queryINSCCodeByCode(parentCode,"");
			for(int i=0;i<tempListInscCode.size();i++){
				INSCCode tempInscCode = new INSCCode();
				Map<Object,Object> tempMap = new HashMap<Object,Object>();
				Map<Object,Object> dataAttributes = new HashMap<Object,Object>();
				tempInscCode = tempListInscCode.get(i);
				dataAttributes.put("id", tempInscCode.getId());
				dataAttributes.put("parentnodecode", tempInscCode.getParentcode());
				tempMap.put("name", tempInscCode.getCodename());
				tempMap.put("dataAttributes", dataAttributes);
				tempMap.put("type", "item");
				resultList.add(tempMap);
			}
		}
		return resultList;
	}
	@Override
	public Map<String, Object> uploadOneFile(HttpServletRequest request,MultipartFile file,
			 String fileType, String fileDescribes, String userCode) {
		LogUtil.info("file.getSize--"+file.getSize());
		Map<String,Object> resultMap = new HashMap<String,Object>();
		INSCCode inscCode = new INSCCode();

		if(inscCodeService.queryINSCCodeByCode("insuranceimage", fileType).size()>0){
			inscCode = inscCodeService.queryINSCCodeByCode("insuranceimage", fileType).get(0);
			if(inscCode==null){
				resultMap.put("status", "fail");
				resultMap.put("msg", "查询字典表错误");
				return resultMap;
			}
		}else{
			resultMap.put("status", "fail");
			resultMap.put("msg", "查询字典表错误");
			return resultMap;
		}

		String imgType="";
		if(file.getOriginalFilename().contains(".")) {
			imgType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1, file.getOriginalFilename().length());
		}
		String uuid = UUIDUtils.random();
		String currentDate = DateUtil.getCurrentDate();
		String uploadImagePath = ValidateUtil.getConfigValue("uploadimage.path");
		String path = "/upload/img/" + currentDate + "/" + uuid + ".";
		String filephysicalpath = "";

		if(StringUtil.isEmpty(uploadImagePath)){
			filephysicalpath = request.getSession().getServletContext().getRealPath("/") + path + imgType;
		}else{
			filephysicalpath = uploadImagePath + path + imgType;
			// 上传到cos失败时，绝对路径:http://xxxx:port/${filephysicalpath} ,这时要设置tomcat的上下文映射到物理路径: filephysicalpath,或在nginx配置映射
			// #nginx.conf
			/*location /data/staticfiles/upload {
				alias /data/staticfiles/upload;
			}*/
		}

		File tempFile = new File(filephysicalpath);
		if(!tempFile.getParentFile().exists()){
			tempFile.getParentFile().mkdirs();
		}
		try {
			FileCopyUtils.copy(file.getBytes(), tempFile);
			File smallfile = AffineTransImageUtil.getAffineTransImage(tempFile);

			Map<String,String> result = CosUtils.uploadCosFile(tempFile.getPath(), "cmbg");
			Map<String,String> result2 = CosUtils.uploadCosFile(smallfile.getPath(), "cmbg");

			String tempContextUrl = ValidateUtil.getConfigValue("uploadimage.server.url");
			if(!tempContextUrl.startsWith("https")){
				tempContextUrl = tempContextUrl.replace("http","https");
			}
			LogUtil.info("cm  userCode = " + userCode + " tempContextUrl = " + tempContextUrl);

			if (!"0".equals(result.get("code"))) {
				LogUtil.info("cm  userCode = " + userCode + " 保存文件到服务器网络硬盘成功，上传原图到cos出错, fileDescribes=" + fileDescribes);
				LogUtil.info("cm  userCode = " + userCode + " tempContextUrl + filephysicalpath = " +(tempContextUrl + filephysicalpath));
				// cos没上传成功，保存本地的地址
				result.put("url", tempContextUrl + filephysicalpath);
				LogUtil.info("cm  本地路径 原图片：" + tempFile.getPath());
			}
			if (!"0".equals(result2.get("code"))) {
				LogUtil.warn("cm  userCode = " + userCode + " 保存文件到服务器网络硬盘成功，上传小图到cos出错, fileDescribes=" + fileDescribes);
				result2.put("url",tempContextUrl + smallfile.getAbsolutePath());
				LogUtil.info("cm  本地路径 小图片：" + smallfile.getPath());
				LogUtil.info("cm  本地路径smallfile.getAbsolutePath()：" + smallfile.getAbsolutePath());
			}

			//只要上传到网络硬盘成功 或是上传cos成功都保存
			INSBFilelibrary insbFilelibrary = new INSBFilelibrary();
			insbFilelibrary.setFilecodevalue(inscCode.getCodevalue());
			insbFilelibrary.setOperator(userCode);
			insbFilelibrary.setCreatetime(new Date());
			insbFilelibrary.setFiledescribe(fileDescribes);
			insbFilelibrary.setFilename(uuid);
			insbFilelibrary.setFilepath(result.get("url"));
			insbFilelibrary.setSmallfilepath(result2.get("url"));
			insbFilelibrary.setFiletype(fileType);
			insbFilelibrary.setFilephysicalpath(filephysicalpath);
			//把http替换成https
			this.replaceHttpToHttps(insbFilelibrary);
			insbFilelibraryDao.insert(insbFilelibrary);

			if (!"0".equals(result.get("code"))) {
				LogUtil.warn("cm userCode = "+userCode+" 保存文件到服务器网络硬盘成功，上传原图到cos出错, fileDescribes=" + fileDescribes + " 增加日志");
				INSBFilelibraryUploadCosFail filelibraryUploadCosFail = new INSBFilelibraryUploadCosFail();
				filelibraryUploadCosFail.setCreatetime(new Date());
				filelibraryUploadCosFail.setOperator(userCode);
				filelibraryUploadCosFail.setFilelibraryid(insbFilelibrary.getId());
				filelibraryUploadCosFail.setImagetype("proto");
				filelibraryUploadCosFail.setFilephysicalpath(insbFilelibrary.getFilephysicalpath());
				filelibraryUploadCosFailDao.insert(filelibraryUploadCosFail);
			}
			if (!"0".equals(result2.get("code"))) {
				LogUtil.warn("cm userCode = "+userCode+" 保存文件到服务器网络硬盘成功，上传小图到cos出错, fileDescribes=" + fileDescribes + " 增加日志");
				INSBFilelibraryUploadCosFail filelibraryUploadCosFail = new INSBFilelibraryUploadCosFail();
				filelibraryUploadCosFail.setCreatetime(new Date());
				filelibraryUploadCosFail.setOperator(userCode);
				filelibraryUploadCosFail.setFilelibraryid(insbFilelibrary.getId());
				filelibraryUploadCosFail.setImagetype("small");
				filelibraryUploadCosFail.setFilephysicalpath(smallfile.getAbsolutePath());
				filelibraryUploadCosFailDao.insert(filelibraryUploadCosFail);
			}

			resultMap.put("status", "success");
			resultMap.put("msg", "保存成功");
			resultMap.put("filetype", fileType);
			resultMap.put("filepath", insbFilelibrary.getFilepath());
			resultMap.put("smallfilepath", insbFilelibrary.getSmallfilepath());
			resultMap.put("fileid", insbFilelibrary.getId());
			return resultMap;
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.info("保存文件出错");
			resultMap.put("status", "fail");
			resultMap.put("msg", "保存文件出错");
			return resultMap;
		}
	}
	@Override
	public Map<String, Object> uploadOneFile(HttpServletRequest request,String file,String fileName,
			String fileType, String fileDescribes,
			String userCode) {
		INSCCode inscCode = new INSCCode();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		if(inscCodeService.queryINSCCodeByCode("insuranceimage", fileType).size()>0){
			inscCode = inscCodeService.queryINSCCodeByCode("insuranceimage", fileType).get(0);
			if(inscCode==null||inscCode.equals("")){
				resultMap.put("status", "fail");
				resultMap.put("msg", "查询字典表错误");
				return resultMap;
			}
		}else{
			resultMap.put("status", "fail");
			resultMap.put("msg", "查询字典表错误");
			return resultMap;
		}
		String imgType="";
		if(fileName.contains("."))
			imgType = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
		String uuid = UUIDUtils.random();
		String currentDate = DateUtil.getCurrentDate();
		File tempFile ;
		if(StringUtil.isEmpty(ValidateUtil.getConfigValue("uploadimage.path"))){
			tempFile = new File(request.getSession().getServletContext().getRealPath("/") + "/upload/img/"+ currentDate + "/" + uuid+"."+imgType);
		}else{
			tempFile = new File(ValidateUtil.getConfigValue("uploadimage.path") + "/upload/img/"+ currentDate + "/" + uuid+"."+imgType);
		}
		if(!tempFile.getParentFile().exists()){
			tempFile.getParentFile().mkdirs();
		}
		try {
			FileCopyUtils.copy(EncodeUtils.decodeBase64(file), tempFile);
			File smallfile = AffineTransImageUtil.getAffineTransImage(tempFile);
			Map<String,String> result = CosUtils.uploadCosFile(tempFile.getPath(), "chn");
			Map<String,String> result2 = CosUtils.uploadCosFile(smallfile.getPath(), "chn");
			
    		if(!"0".equals(result.get("code"))){
    			LogUtil.info("====上传出错"+result.get("msg"));
    			throw new RuntimeException("zdzdzd====上传出错");
    		}else{
    			INSBFilelibrary insbFilelibrary = new INSBFilelibrary();
    			insbFilelibrary.setFilecodevalue(inscCode.getCodevalue());
    			insbFilelibrary.setOperator(userCode);
    			insbFilelibrary.setCreatetime(new Date());
    			insbFilelibrary.setFiledescribe(fileDescribes);
    			insbFilelibrary.setFilename(uuid);
    			insbFilelibrary.setFilepath(result.get("url"));
    			if("0".equals(result2.get("code"))){
					insbFilelibrary.setSmallfilepath(result2.get("url"));
				}else{
					LogUtil.info("zdzdzd====缩略图上传出错 "+result2.get("msg"));
				}
    			insbFilelibrary.setFiletype(fileType);
    			if(StringUtil.isEmpty(ValidateUtil.getConfigValue("uploadimage.path"))){
    				insbFilelibrary.setFilephysicalpath(request.getSession().getServletContext().getRealPath("/") + "/upload/img/"+ currentDate + "/" + uuid+"."+imgType);
    			}else{
    				insbFilelibrary.setFilephysicalpath(ValidateUtil.getConfigValue("uploadimage.path") + "/upload/img/"+ currentDate + "/" + uuid+"."+imgType);
    			}
    			//把http替换成https
    			this.replaceHttpToHttps(insbFilelibrary);
    			insbFilelibraryDao.insert(insbFilelibrary);
    			resultMap.put("status", "success");
    			resultMap.put("msg", "保存成功");
    			resultMap.put("filetype", fileType);
    			resultMap.put("filepath", insbFilelibrary.getFilepath());
				resultMap.put("smallfilepath", insbFilelibrary.getSmallfilepath());
    			resultMap.put("fileid", insbFilelibrary.getId());
    			return resultMap;
    		}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.info("保存文件出错");
			resultMap.put("status", "fail");
			resultMap.put("msg", "保存文件出错"+e.getStackTrace());
			return resultMap;
		} 
	}
	@Override
	public Map<String, Object> uploadOneFileByUrl(HttpServletRequest request,
			String urlStr, String fileName, String fileType, String fileDescribes,
			String userCode) {
		INSCCode inscCode = new INSCCode();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		if(inscCodeService.queryINSCCodeByCode("insuranceimage", fileType).size()>0){
			inscCode = inscCodeService.queryINSCCodeByCode("insuranceimage", fileType).get(0);
			if(inscCode==null||inscCode.equals("")){
				resultMap.put("status", "fail");
				resultMap.put("msg", "查询字典表错误");
				return resultMap;
			}
		}else{
			resultMap.put("status", "fail");
			resultMap.put("msg", "查询字典表错误");
			return resultMap;
		}
		try {
			String imgType="";
			if(fileName.contains("."))
				imgType = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
			String uuid = UUIDUtils.random();
			String currentDate = DateUtil.getCurrentDate();
			File tempFile ;
			if(StringUtil.isEmpty(ValidateUtil.getConfigValue("uploadimage.path"))){
				tempFile = new File(request.getSession().getServletContext().getRealPath("/") + "/upload/img/"+ currentDate + "/" + uuid+"."+imgType);
			}else{
				tempFile = new File(ValidateUtil.getConfigValue("uploadimage.path") + "/upload/img/"+ currentDate + "/" + uuid+"."+imgType);
			}
			if(!tempFile.getParentFile().exists()){
				tempFile.getParentFile().mkdirs();
			}
			URL url = new URL(urlStr);
			URLConnection urlConnection = url.openConnection();
			urlConnection.connect();
			InputStream inputStream = urlConnection.getInputStream();
			FileCopyUtils.copy(FileCopyUtils.copyToByteArray(inputStream), tempFile);
			inputStream.close();
			File smallfile = AffineTransImageUtil.getAffineTransImage(tempFile);

			Map<String,String> result = CosUtils.uploadCosFile(ValidateUtil.getConfigValue("uploadimage.path") + "/upload/img/"+ currentDate + "/" + uuid+"."+imgType, "chn");
			Map<String,String> result2 = CosUtils.uploadCosFile(smallfile.getPath(), "chn");
    		if(!"0".equals(result.get("code"))){
    			LogUtil.info("zdzdzd====上传出错");
    			throw new RuntimeException("zdzdzd====上传出错");
    		}else{
    			INSBFilelibrary insbFilelibrary = new INSBFilelibrary();
    			insbFilelibrary.setFilecodevalue(inscCode.getCodevalue());
    			insbFilelibrary.setOperator(userCode);
    			insbFilelibrary.setCreatetime(new Date());
    			insbFilelibrary.setFiledescribe(fileDescribes);
    			insbFilelibrary.setFilename(uuid);
    			insbFilelibrary.setFilepath(result.get("url"));
    			if("0".equals(result2.get("code"))){
					insbFilelibrary.setSmallfilepath(result2.get("url"));
				}else{
					LogUtil.info("zdzdzd====缩略图上传出错");
				}
    			insbFilelibrary.setFiletype(fileType);
    			if(StringUtil.isEmpty(ValidateUtil.getConfigValue("uploadimage.path"))){
    				insbFilelibrary.setFilephysicalpath(request.getSession().getServletContext().getRealPath("/") + "/upload/img/"+ currentDate + "/" + uuid+"."+imgType);
    			}else{
    				insbFilelibrary.setFilephysicalpath(ValidateUtil.getConfigValue("uploadimage.path") + "/upload/img/"+ currentDate + "/" + uuid+"."+imgType);
    			}
    			//把http替换成https
    			this.replaceHttpToHttps(insbFilelibrary);
    			insbFilelibraryDao.insert(insbFilelibrary);
    			resultMap.put("status", "success");
    			resultMap.put("msg", "保存成功");
    			resultMap.put("filetype", fileType);
    			resultMap.put("filepath", insbFilelibrary.getFilepath());
				resultMap.put("smallfilepath", insbFilelibrary.getSmallfilepath());
    			resultMap.put("fileid", insbFilelibrary.getId());
    			return resultMap;
    		}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.info("保存文件出错");
			resultMap.put("status", "fail");
			resultMap.put("msg", "保存文件出错");
			return resultMap;
		}
	}
	@Override
	public Map<String, Object> saveFileInfo(HttpServletRequest request,String mediaId,String fileName,
			String fileType, String fileDescribes,
			String userCode) {
		INSCCode inscCode = new INSCCode();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		if(inscCodeService.queryINSCCodeByCode("insuranceimage", fileType).size()>0){
			inscCode = inscCodeService.queryINSCCodeByCode("insuranceimage", fileType).get(0);
			if(inscCode==null||inscCode.equals("")){
				resultMap.put("status", "fail");
				resultMap.put("msg", "查询字典表错误");
				return resultMap;
			}
		}else{
			resultMap.put("status", "fail");
			resultMap.put("msg", "查询字典表错误");
			return resultMap;
		}
		String imgType="";
		if(fileName.contains("."))
			imgType = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
		String uuid = UUIDUtils.random();
		String currentDate = DateUtil.getCurrentDate();

		String filephysicalpath = "";
		if(StringUtil.isEmpty(ValidateUtil.getConfigValue("uploadimage.path"))){
			filephysicalpath = request.getSession().getServletContext().getRealPath("/") + "/upload/img/"+ currentDate + "/" + uuid+"."+imgType;
		}else{
			filephysicalpath = ValidateUtil.getConfigValue("uploadimage.path") + "/upload/img/"+ currentDate + "/" + uuid+"."+imgType;
			// 上传到cos失败时，绝对路径:http://xxxx:port/${filephysicalpath} ,这时要设置tomcat的上下文件映射到物理路径: filephysicalpath,或在nginx配置影射 ，
		}

		File tempFile = new File(filephysicalpath);
		if(!tempFile.getParentFile().exists()){
			tempFile.getParentFile().mkdirs();
		}
		try {
			DownloadWxFile.download(mediaId, ValidateUtil.getConfigValue("uploadimage.path") + "/upload/img/"+ currentDate , uuid+"."+imgType);
		} catch (Exception e) {
			LogUtil.info("保存文件出错");
			resultMap.put("status", "fail");
			resultMap.put("msg", "保存文件出错");
			e.printStackTrace();
			return resultMap;
		}
		
		Map<String, String> result;
		Map<String, String> result2;
		try {
			File smallfile = AffineTransImageUtil.getAffineTransImage(tempFile);
			result = CosUtils.uploadCosFile(ValidateUtil.getConfigValue("uploadimage.path") + "/upload/img/"+ currentDate + "/" + uuid+"."+imgType, "chn");
			result2 = CosUtils.uploadCosFile(smallfile.getPath(), "chn");

			String tempContextUrl = ValidateUtil.getConfigValue("uploadimage.server.url");
			if(!tempContextUrl.startsWith("https")){
				tempContextUrl = tempContextUrl.replace("http","https");
			}

			LogUtil.info("微信 userCode = " + userCode + " url = " + request.getRequestURL());
			LogUtil.info("微信 userCode = " + userCode + " uri = " + request.getRequestURI());
			if (!"0".equals(result.get("code"))) {
				LogUtil.info("微信 userCode = " + userCode + " 保存文件到服务器网络硬盘成功，上传到cos 出错,(上传原图操作) userCode=" + userCode + " fileName=" + fileName);
				LogUtil.info("微信 userCode = " + userCode + " tempContextUrl = " + tempContextUrl);
				LogUtil.info("微信 userCode = " + userCode + " tempContextUrl + filephysicalpath = " +(tempContextUrl + filephysicalpath));
				// cos没上传成功，保存本地的地址
				result.put("url", tempContextUrl + filephysicalpath);
				LogUtil.info("微信 本地路径 原图片：" + tempFile.getPath());
			}
			if (!"0".equals(result2.get("code"))) {
				LogUtil.warn("微信 userCode = "+userCode+" 保存文件到服务器网络硬盘成功，上传到cos 出错,(上传小图操作) userCode=" + userCode + " fileName=" + fileName);
				result2.put("url",tempContextUrl + smallfile.getAbsolutePath());
				LogUtil.info("微信 本地路径 小图片：" + smallfile.getPath());
				LogUtil.info("微信 本地路径smallfile.getPath()：" + smallfile.getPath());
				LogUtil.info("微信 本地路径smallfile.getAbsolutePath()：" + smallfile.getAbsolutePath());
			}
			//只要上传到网络硬盘成功 或是上传cos成功都保存
			INSBFilelibrary insbFilelibrary = new INSBFilelibrary();
			insbFilelibrary.setFilecodevalue(inscCode.getCodevalue());
			insbFilelibrary.setOperator(userCode);
			insbFilelibrary.setCreatetime(new Date());
			insbFilelibrary.setFiledescribe(fileDescribes);
			insbFilelibrary.setFilename(uuid);
			insbFilelibrary.setFilepath(result.get("url"));
			insbFilelibrary.setSmallfilepath(result2.get("url"));
			insbFilelibrary.setFiletype(fileType);
			insbFilelibrary.setFilephysicalpath(filephysicalpath);
			//把http替换成https
			this.replaceHttpToHttps(insbFilelibrary);
			insbFilelibraryDao.insert(insbFilelibrary);

			if (!"0".equals(result.get("code"))) {
				LogUtil.warn("微信 userCode = "+userCode+" 保存文件到服务器网络硬盘成功，上传到cos 出错,(上传原图操作) userCode=" + userCode + " fileName=" + fileName + " 增加日志");
				INSBFilelibraryUploadCosFail filelibraryUploadCosFail = new INSBFilelibraryUploadCosFail();
				filelibraryUploadCosFail.setCreatetime(new Date());
				filelibraryUploadCosFail.setOperator(userCode);
				filelibraryUploadCosFail.setFilelibraryid(insbFilelibrary.getId());
				filelibraryUploadCosFail.setImagetype("proto");
				filelibraryUploadCosFail.setFilephysicalpath(insbFilelibrary.getFilephysicalpath());
				filelibraryUploadCosFailDao.insert(filelibraryUploadCosFail);
			}
			if (!"0".equals(result2.get("code"))) {
				LogUtil.warn("微信 userCode = "+userCode+" 保存文件到服务器网络硬盘成功，上传到cos 出错,(上传小图操作) userCode=" + userCode + " fileName=" + fileName + " 增加日志");
				INSBFilelibraryUploadCosFail filelibraryUploadCosFail = new INSBFilelibraryUploadCosFail();
				filelibraryUploadCosFail.setCreatetime(new Date());
				filelibraryUploadCosFail.setOperator(userCode);
				filelibraryUploadCosFail.setFilelibraryid(insbFilelibrary.getId());
				filelibraryUploadCosFail.setImagetype("small");
				filelibraryUploadCosFail.setFilephysicalpath(smallfile.getAbsolutePath());
				filelibraryUploadCosFailDao.insert(filelibraryUploadCosFail);
			}

				resultMap.put("status", "success");
				resultMap.put("msg", "保存成功");
				resultMap.put("filetype", fileType);
				resultMap.put("filepath", insbFilelibrary.getFilepath());
				resultMap.put("smallfilepath", insbFilelibrary.getSmallfilepath());
				resultMap.put("fileid", insbFilelibrary.getId());
				return resultMap;
		} catch (Exception e) {
			LogUtil.info("保存文件出错");
			resultMap.put("status", "fail");
			resultMap.put("msg", "保存文件出错");
			return resultMap;
		}
	}

	@Override
	public Map<String, Object> uploadFiles(HttpServletRequest request,MultipartFile[] files,
			String filetype, String userCode,
			String[] filenames, String[] filedescribes, String[] fileclassifytype)
			throws Exception {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<String> fileidlist = new ArrayList<String>();
		List<String> filepathlist = new ArrayList<String>();
		List<String> filetypelist = new ArrayList<String>();
		List<String> statuslist = new ArrayList<String>();
		List<String> msglist = new ArrayList<String>();
		INSCCode inscCode = new INSCCode();
        MultipartFile file = null;
        String fileName = "";
        String filedescribe = "";
		if(files!=null&&files.length>0){  
            for(int i = 0;i<files.length;i++){ 
        		if(inscCodeService.queryINSCCodeByCode("insuranceimage", fileclassifytype[i]).size()>0){
        			inscCode = inscCodeService.queryINSCCodeByCode("insuranceimage", fileclassifytype[i]).get(0);
        			if(inscCode==null||inscCode.equals("")){
                		statuslist.add("fail");
                		msglist.add("查询字典表错误");
	            		fileidlist.add(null);
	            		filepathlist.add(null);
	            		filetypelist.add(null);
        				continue;
        			}
        		}else{
            		statuslist.add("fail");
            		msglist.add("查询字典表错误");
            		fileidlist.add(null);
            		filepathlist.add(null);
            		filetypelist.add(null);
            		continue;
        		}
        		String type=""; 
                file = files[i];
                if (!file.isEmpty()) {
                	if(StringUtil.isEmpty(filenames[i])){
                		fileName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
                	}else{
                		fileName = filenames[i];
                	}
                	if(StringUtil.isEmpty(filedescribes[i])){
                		filedescribe = file.getOriginalFilename();
                	}else{
                		filedescribe = filedescribes[i];
                	}
                	if(file.getOriginalFilename().contains("."))
                		type = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1,file.getOriginalFilename().length());
                	String currentDate = DateUtil.getCurrentDate();
                	File tempFile;
            		if(StringUtil.isEmpty(ValidateUtil.getConfigValue("uploadimage.path"))){
            			tempFile = new File(request.getSession().getServletContext().getRealPath("/") + "/upload/" + filetype + "/" + inscCode.getCodetype() + "/" + currentDate + "/" + fileName+"."+type);
            		}else{
            			tempFile = new File(ValidateUtil.getConfigValue("uploadimage.path") + "/upload/" + filetype + "/" + inscCode.getCodetype() + "/" + currentDate + "/" + fileName+"."+type);
            		}
            		if(!tempFile.getParentFile().exists()){
            			tempFile.getParentFile().mkdirs();
            		}
            		try {
						FileCopyUtils.copy(file.getBytes(), tempFile);
					} catch (Exception e) {
						e.printStackTrace();
						LogUtil.info("保存文件出错");
	            		statuslist.add("fail");
	            		msglist.add("保存失败");
	            		fileidlist.add(null);
	            		filepathlist.add(null);
	            		filetypelist.add(null);
	            		continue;
					}
            		
            		INSBFilelibrary insbFilelibrary = new INSBFilelibrary();
            		insbFilelibrary.setOperator(userCode);
            		insbFilelibrary.setCreatetime(new Date());
            		insbFilelibrary.setFiledescribe(filedescribe);
            		insbFilelibrary.setFilename(fileName);
            		insbFilelibrary.setFilepath(ValidateUtil.getConfigValue("uploadimage.url")+"/upload/" + filetype + "/" + inscCode.getCodetype() 
	            		+ "/" + currentDate + "/" + fileName+"."+type);
            		insbFilelibrary.setFiletype(fileclassifytype[i]);
            		if(StringUtil.isEmpty(ValidateUtil.getConfigValue("uploadimage.path"))){
            			insbFilelibrary.setFilephysicalpath(request.getSession().getServletContext().getRealPath("/") + "/upload/" + filetype + "/" + inscCode.getCodetype() + "/" + currentDate + "/" + fileName+"."+type);
            		}else{
            			insbFilelibrary.setFilephysicalpath(ValidateUtil.getConfigValue("uploadimage.path") + "/upload/" + filetype + "/" + inscCode.getCodetype() + "/" + currentDate + "/" + fileName+"."+type);
            		}
            		//把http替换成https
        			this.replaceHttpToHttps(insbFilelibrary);
            		insbFilelibraryDao.insert(insbFilelibrary);
            		fileidlist.add(insbFilelibrary.getId());
            		filepathlist.add(insbFilelibrary.getFilepath());
            		filetypelist.add(insbFilelibrary.getFiletype());
            		statuslist.add("success");
            		msglist.add("保存成功");
                }  
            }  
            resultMap.put("statuslist", statuslist);
            resultMap.put("msglist", msglist);
            resultMap.put("filetypelist", filetypelist);
            resultMap.put("filepathlist", filepathlist);
            resultMap.put("fileidlist", fileidlist);
        }  
		return resultMap;
	}

	@Override
	public List<INSBFilelibrary> queryByFilebusinessCode(String code) {
		List<INSBFilelibrary> list = insbFilelibraryDao.selectByFilebusinessCode(code);
		if( list != null && list.size() > 0 ) {
			for( INSBFilelibrary insbFilelibrary : list ) {
				if( insbFilelibrary != null && ( insbFilelibrary.getSmallfilepath() == null 
						|| "".equals(insbFilelibrary.getSmallfilepath().trim()) ) ) {
					insbFilelibrary.setSmallfilepath(insbFilelibrary.getFilepath());
				}
			}
		}
		return list;
	}




	@Override
	public List<INSBFilelibrary> selectListByMap(Map<String, Object> map) {
		return insbFilelibraryDao.selectListByMap(map);
	}




	@Override
	public boolean uploadProLogo(HttpServletRequest request,String filepath,MultipartFile file,String userCode, String proId,
			String fileclassifytype) throws Exception {
		INSBFilebusiness bu = new INSBFilebusiness();
		bu.setCode(proId);
		bu = insbFilebusinessDao.selectOne(bu);

		String uuid = UUIDUtils.random();
		String currentDate = DateUtil.getCurrentDate();
		INSBFilelibrary temInsbFilelibrary = new INSBFilelibrary();
		String uploadImageUrl = ValidateUtil.getConfigValue("uploadimage.url");
		String path = "/static/upload/img/" + currentDate + "/" + uuid + ".";

		if(bu!=null){
//			修改
			String filetype="";
			if(file.getOriginalFilename().contains(".")){
				filetype = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1,file.getOriginalFilename().length());
			}

			INSBProvider pro = insbProviderDao.selectById(proId);
    		if(pro!=null){
    			String newFilePath = filepath + path + filetype;
    			File tempFile = new File(newFilePath);
    			if(!tempFile.getParentFile().exists()){
        			tempFile.getParentFile().mkdirs();
        		}
        		FileCopyUtils.copy(file.getBytes(), tempFile);
        		pro.setModifytime(new Date());
    			pro.setLogo(uploadImageUrl + path + filetype);
    			insbProviderDao.updateById(pro);

				temInsbFilelibrary = insbFilelibraryDao.selectById(bu.getFilelibraryid());
    			temInsbFilelibrary.setFilepath(uploadImageUrl + path + filetype);
    			insbFilelibraryDao.updateById(temInsbFilelibrary);
    		}
    		return true;
		}else{
//			添加
			String filetype="";
			if(file.getOriginalFilename().contains(".")){
				filetype = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1,file.getOriginalFilename().length());
			}

			String newFilePath = filepath + path + filetype;;
			File tempFile = new File(newFilePath);
			if(!tempFile.getParentFile().exists()){
    			tempFile.getParentFile().mkdirs();
    		}
    		FileCopyUtils.copy(file.getBytes(), tempFile);

    		temInsbFilelibrary.setFilepath(uploadImageUrl + path + filetype);
    		temInsbFilelibrary.setFilecodevalue(fileclassifytype);
    		temInsbFilelibrary.setFilephysicalpath(filepath + path + filetype);
    		temInsbFilelibrary.setOperator(userCode);
    		temInsbFilelibrary.setCreatetime(new Date());
    		temInsbFilelibrary.setModifytime(new Date());
    		temInsbFilelibrary.setFiletype(filetype);
			temInsbFilelibrary.setFilename(file.getName());

			INSCCode inscCode = new INSCCode();
			inscCode.setParentcode("insuranceimage");
			inscCode.setCodetype(fileclassifytype);
			INSCCode inscCode2 = inscCodeService.queryOne(inscCode);
    		if(inscCode2!=null){
    			temInsbFilelibrary.setFilecodevalue(inscCode2.getCodevalue());
    		}

    		//把http替换成https
			this.replaceHttpToHttps(temInsbFilelibrary);
    		insbFilelibraryDao.insert(temInsbFilelibrary);

    		INSBFilebusiness sb = new INSBFilebusiness();
    		sb.setFilelibraryid(temInsbFilelibrary.getId());
    		sb.setCreatetime(new Date());
    		sb.setOperator(userCode);
    		sb.setType(fileclassifytype);
    		sb.setCode(proId);
    		insbFilebusinessDao.insert(sb);

    		INSBProvider pro = insbProviderDao.selectById(proId);
    		if(pro!=null){
    			pro.setLogo(uploadImageUrl + path + filetype);
    			insbProviderDao.updateById(pro);
    		}
    		return true;
		}
	}




	@Override
	public boolean uploadFileByCos(MultipartFile[] files, String filepath,
			String filetype, String userCode, String[] filenames,
			String[] filedescribes, String fileclassifytype) throws Exception {
		INSCCode inscCode = new INSCCode();
		inscCode.setParentcode("fileuploadtype");
		inscCode.setCodevalue(filetype);
		inscCode.setCodetype("fileuploadtype");
		if(files!=null&&files.length>0){  
            for(int i = 0;i<files.length;i++){  
                MultipartFile file = files[i];
                String fileName = "";
                String filedescribe = "";
                if (!file.isEmpty()) {
                	if(StringUtil.isEmpty(filenames[i])){
                		fileName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
                	}else{
                		fileName = filenames[i];
                	}
                	if(StringUtil.isEmpty(filedescribes[i])){
                		filedescribe = file.getOriginalFilename();
                	}else{
                		filedescribe = filedescribes[i];
                	}
            		String fileType="";
            		if(file.getOriginalFilename().contains("."))
            			fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1,file.getOriginalFilename().length());
            		String uuid = UUIDUtils.random();
            		String currentDate = DateUtil.getCurrentDate();
            		
            		File tempFile = new File(filepath + "/static/upload/" + filetype  + "/" + currentDate + "/" + uuid +"."+ fileType);
            		if(!tempFile.getParentFile().exists()){
            			tempFile.getParentFile().mkdirs();
            		}
            		FileCopyUtils.copy(file.getBytes(), tempFile);
            		
        			File smallfile = AffineTransImageUtil.getAffineTransImage(tempFile);

            		Map<String,String> result = CosUtils.uploadCosFile(filepath + "/static/upload/" + filetype  + "/" + currentDate + "/" + uuid +"."+ fileType, "cmbg");
            		Map<String,String> result2 = CosUtils.uploadCosFile(smallfile.getPath(), "cmbg");
        			
            		if(!"0".equals(result.get("code"))){
            			LogUtil.info("zdzdzd====上传出错");
            			throw new RuntimeException("zdzdzd====上传出错");
            		}else{
            			INSBFilelibrary insbFilelibrary = new INSBFilelibrary();
            			insbFilelibrary.setOperator(userCode);
            			insbFilelibrary.setCreatetime(new Date());
            			insbFilelibrary.setFiledescribe(filedescribe);
            			insbFilelibrary.setFilename(fileName);
            			insbFilelibrary.setFilepath(result.get("url"));
            			if("0".equals(result2.get("code"))){
        					insbFilelibrary.setSmallfilepath(result2.get("url"));
        				}else{
        					LogUtil.info("zdzdzd====缩略图上传出错");
        				}
            			insbFilelibrary.setFiletype(filetype);
            			insbFilelibrary.setFilecodevalue(fileclassifytype);
            			insbFilelibrary.setFilephysicalpath(filepath + "/static/upload/" + filetype  + "/" + currentDate + "/" + uuid +"."+ fileType);
            			//把http替换成https
            			this.replaceHttpToHttps(insbFilelibrary);
            			insbFilelibraryDao.insert(insbFilelibrary);
            		}
                }  
            }  
        }  
		return false;
	}




	@Override
	public Map<String, Object> uploadOneFileByCos(HttpServletRequest request,
			String file, String fileName, String fileType,
			String fileDescribes, String userCode,String taskId) {
		INSCCode inscCode = new INSCCode();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		if(inscCodeService.queryINSCCodeByCode("insuranceimage", fileType).size()>0){
			inscCode = inscCodeService.queryINSCCodeByCode("insuranceimage", fileType).get(0);
			if(inscCode==null||inscCode.equals("")){
				LogUtil.error("taskId = "+taskId+" zdzdzd====上传,查询字典表错误"+userCode +" fileName="+fileName);
				resultMap.put("status", "fail");
				resultMap.put("msg", "查询字典表错误");
				return resultMap;
			}
		}else{
			LogUtil.error("taskId = "+taskId+" zdzdzd====上传,查询字典表错误,图像类型不存在"+userCode+" fileType="+fileType+" fileName="+fileName);
			resultMap.put("status", "fail");
			resultMap.put("msg", "查询字典表错误");
			return resultMap;
		}
		String imgType="";
		if(fileName.contains(".")) {
			imgType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
		}
		String uuid = UUIDUtils.random();
		String currentDate = DateUtil.getCurrentDate();

		String filephysicalpath = "";
		if(StringUtil.isEmpty(ValidateUtil.getConfigValue("uploadimage.path"))){
			filephysicalpath = request.getSession().getServletContext().getRealPath("/") + "/upload/img/"+ currentDate + "/" + uuid+"."+imgType;
		}else{
			filephysicalpath = ValidateUtil.getConfigValue("uploadimage.path") + "/upload/img/"+ currentDate + "/" + uuid+"."+imgType;
			// 上传到cos失败时，绝对路径:http://xxxx:port/${filephysicalpath} ,这时要设置tomcat的上下文件映射到物理路径: filephysicalpath,或在nginx配置影射 ，
			/*location /data/staticfiles/upload { #nginx.conf
				alias /data/staticfiles/upload;
			}*/
		}

		File tempFile = new File(filephysicalpath);
		if(!tempFile.getParentFile().exists()){
			tempFile.getParentFile().mkdirs();
		}
		try {
			FileCopyUtils.copy(EncodeUtils.decodeBase64(file), tempFile);
			File smallfile = AffineTransImageUtil.getAffineTransImage(tempFile);

			Map<String, String> result = CosUtils.uploadCosFile(tempFile.getPath(), "cmbg");
			Map<String, String> result2 = CosUtils.uploadCosFile(smallfile.getPath(), "cmbg");

			// 获取带部署环境上下文的域名，如： http://www.iteye.com/admin/
			StringBuffer url = request.getRequestURL();
			// String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).append(request.getServletContext().getContextPath()).append("/").toString(); // https://develop.52zzb.com/cm//data/staticfiles/upload/upload/img/2017-02-16/6c5f1272ac09c1a7497973d4a5add3de.jpg
//			String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
			String tempContextUrl = ValidateUtil.getConfigValue("uploadimage.server.url");
			if(!tempContextUrl.startsWith("https")){
				tempContextUrl = tempContextUrl.replace("http","https");
			}
			LogUtil.info("taskId = " + taskId + " url = " + request.getRequestURL());
			LogUtil.info("taskId = " + taskId + " uri = " + request.getRequestURI());
			if (!"0".equals(result.get("code"))) {
				LogUtil.info("taskId = " + taskId + " 保存文件到服务器网络硬盘成功，上传到cos 出错,(上传原图操作) userCode=" + userCode + " fileName=" + fileName);
				LogUtil.info("taskId = " + taskId + " tempContextUrl = " + tempContextUrl);
				LogUtil.info("taskId = " + taskId + " tempContextUrl + filephysicalpath = " +(tempContextUrl + filephysicalpath));
				// cos没上传成功，保存本地的地址
				result.put("url", tempContextUrl + filephysicalpath);
				LogUtil.info("本地路径 原图片：" + tempFile.getPath());
			}
			if (!"0".equals(result2.get("code"))) {
				LogUtil.warn("taskId = "+taskId+" 保存文件到服务器网络硬盘成功，上传到cos 出错,(上传小图操作) userCode=" + userCode + " fileName=" + fileName);
				result2.put("url",tempContextUrl + smallfile.getAbsolutePath());
				LogUtil.info("本地路径 小图片：" + smallfile.getPath());
				LogUtil.info("本地路径smallfile.getPath()：" + smallfile.getPath());
				LogUtil.info("本地路径smallfile.getAbsolutePath()：" + smallfile.getAbsolutePath());
			}
			//只要上传到网络硬盘成功 或是上传cos成功都保存
			INSBFilelibrary insbFilelibrary = new INSBFilelibrary();
			insbFilelibrary.setFilecodevalue(inscCode.getCodevalue());
			insbFilelibrary.setOperator(userCode);
			insbFilelibrary.setCreatetime(new Date());
			insbFilelibrary.setFiledescribe(fileDescribes);
			insbFilelibrary.setFilename(uuid);
			insbFilelibrary.setFilepath(result.get("url"));
			insbFilelibrary.setSmallfilepath(result2.get("url"));
			insbFilelibrary.setFiletype(fileType);
			insbFilelibrary.setFilephysicalpath(filephysicalpath);
			//把http替换成https
			this.replaceHttpToHttps(insbFilelibrary);
			insbFilelibraryDao.insert(insbFilelibrary);

			if (!"0".equals(result.get("code"))) {
				LogUtil.warn("taskId = "+taskId+" 保存文件到服务器网络硬盘成功，上传到cos 出错,(上传原图操作) userCode=" + userCode + " fileName=" + fileName + " 增加日志");
				INSBFilelibraryUploadCosFail filelibraryUploadCosFail = new INSBFilelibraryUploadCosFail();
				filelibraryUploadCosFail.setCreatetime(new Date());
				filelibraryUploadCosFail.setOperator(userCode);
				filelibraryUploadCosFail.setFilelibraryid(insbFilelibrary.getId());
				filelibraryUploadCosFail.setImagetype("proto");
				filelibraryUploadCosFail.setFilephysicalpath(insbFilelibrary.getFilephysicalpath());
				filelibraryUploadCosFailDao.insert(filelibraryUploadCosFail);
			}
			if (!"0".equals(result2.get("code"))) {
				LogUtil.warn("taskId = "+taskId+" 保存文件到服务器网络硬盘成功，上传到cos 出错,(上传小图操作) userCode=" + userCode + " fileName=" + fileName + " 增加日志");
				INSBFilelibraryUploadCosFail filelibraryUploadCosFail = new INSBFilelibraryUploadCosFail();
				filelibraryUploadCosFail.setCreatetime(new Date());
				filelibraryUploadCosFail.setOperator(userCode);
				filelibraryUploadCosFail.setFilelibraryid(insbFilelibrary.getId());
				filelibraryUploadCosFail.setImagetype("small");
				filelibraryUploadCosFail.setFilephysicalpath(smallfile.getAbsolutePath());
				filelibraryUploadCosFailDao.insert(filelibraryUploadCosFail);
			}

			resultMap.put("status", "success");
			resultMap.put("msg", "保存成功");
			resultMap.put("filetype", fileType);
			resultMap.put("filepath", insbFilelibrary.getFilepath());
			resultMap.put("smallfilepath", insbFilelibrary.getSmallfilepath());
			resultMap.put("fileid", insbFilelibrary.getId());
			return resultMap;
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.info("taskId = "+taskId+" 保存文件出错,userCode="+userCode+" fileName="+fileName);
			resultMap.put("status", "fail");
			resultMap.put("msg", "保存文件服务器网络硬盘出错,返回异常");
			return resultMap;
		}
		
	}

	@Override
	public List<INSBFilelibrary> queryAll() {
		return insbFilelibraryDao.selectAll();
	}

	@Override
	public List<INSBFilelibrary> queryAll(int offset,int limit) {
		 Map<String, Object> Params=new HashMap<String, Object>();
         Params.put("offset", offset);
         Params.put("limit", limit);
         return insbFilelibraryDao.selectAll(Params);
	}



	public void replaceHttpToHttps(INSBFilelibrary insbFilelibrary){
		String filepath = insbFilelibrary.getFilepath();
		String smallfilepath = insbFilelibrary.getSmallfilepath();
		if( !StringUtil.isEmpty(filepath) ){
			if(!filepath.startsWith("https")){
				filepath = filepath.replace("http", "https");
				insbFilelibrary.setFilepath(filepath);
			}
		}
		if( !StringUtil.isEmpty(smallfilepath) ){
			if(!smallfilepath.startsWith("https")){
				smallfilepath = smallfilepath.replace("http", "https");
				insbFilelibrary.setSmallfilepath(smallfilepath);
			}
		}
	}


}