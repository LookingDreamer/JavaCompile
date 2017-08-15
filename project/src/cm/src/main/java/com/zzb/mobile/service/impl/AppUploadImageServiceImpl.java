package com.zzb.mobile.service.impl;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cninsure.core.tools.util.ValidateUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.entity.INSCCode;
import com.zzb.cm.dao.INSBFilebusinessDao;
import com.zzb.cm.dao.INSBFilelibraryDao;
import com.zzb.cm.entity.INSBFilebusiness;
import com.zzb.cm.entity.INSBFilelibrary;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.service.AppUploadImageService;
@Service
@Transactional
public class AppUploadImageServiceImpl implements AppUploadImageService {

	@Resource
	private INSBFilelibraryDao insbFilelibraryDao;
	@Resource
	private INSCCodeDao inscCodeDao;
	@Resource
	private INSBFilebusinessDao insbFilebusinessDao;
	
	@Override
	public CommonModel imageUpload(MultipartFile file, String operator,String path,String filecodevalue,String processinstanceid) {
		CommonModel commonModel = new CommonModel();
        try {
        	if(StringUtil.isEmpty(processinstanceid)){
        		commonModel.setStatus("fail");
    			commonModel.setMessage("实例id不能为空");
    			return commonModel;
        	}
        	String fileName = file.getOriginalFilename();
            if(StringUtil.isEmpty(fileName)){
            	commonModel.setStatus("fail");
    			commonModel.setMessage("上传图片不存在");
    			return commonModel;
            }
            String newName = getImageName(fileName);
            File targetFile = new File(path, newName);  
            if(!targetFile.exists()){  
                targetFile.mkdirs();  
            }  
            //保存 
            file.transferTo(targetFile);
            //保存数据到数据库
            INSCCode inscCode = new INSCCode();
            inscCode.setCodevalue(filecodevalue);
            //影像code
            inscCode.setParentcode("insuranceimage");
            INSCCode code = inscCodeDao.selectOne(inscCode);
            if(null == code){
            	commonModel.setStatus("fail");
    			commonModel.setMessage("字典表没有对应的信息");
    			return commonModel;
            }
            //附件表保存
    		INSBFilelibrary insbFilelibrary = new INSBFilelibrary();
    		insbFilelibrary.setFilecodevalue(code.getCodevalue());
    		insbFilelibrary.setOperator(null == operator ? "" : operator);
    		insbFilelibrary.setCreatetime(new Date());
    		insbFilelibrary.setFiledescribe(code.getCodename());
    		insbFilelibrary.setFilename(newName);
    		insbFilelibrary.setFilepath("/static/upload/appimage/" + newName);
    		insbFilelibrary.setFiletype(code.getCodetype());
    		insbFilelibrary.setNoti(fileName);
    		insbFilelibraryDao.insert(insbFilelibrary);
    		//保存业务-附件关联表
    		INSBFilebusiness insbFilebusiness = new INSBFilebusiness();
    		insbFilebusiness.setCreatetime(new Date());
    		insbFilebusiness.setOperator(null == operator ? "" : operator);
    		insbFilebusiness.setType(inscCode.getParentcode());
    		insbFilebusiness.setFilelibraryid(insbFilelibrary.getId());
    		insbFilebusiness.setCode(processinstanceid);
    		insbFilebusinessDao.insert(insbFilebusiness);
    		commonModel.setMessage("操作成功");
			commonModel.setStatus("success");
    		Map<String, String> map = new HashMap<String, String>();
    		map.put("path", ValidateUtil.getConfigValue("uploadimage.url")+insbFilelibrary.getFilepath());
    		commonModel.setBody(map);
        } catch (Exception e) {  
            e.printStackTrace();
            commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
        }
        return commonModel; 
	}
	
	private String getImageName(String name){
		String extName = name.substring(name.lastIndexOf("."),name.length());
		return UUIDUtils.create() + extName;
	}
	
}
