package com.zzb.mobile.service;

import org.springframework.web.multipart.MultipartFile;

import com.zzb.mobile.model.CommonModel;

public interface AppUploadImageService {

	CommonModel imageUpload(MultipartFile file, String operator,String path,String filecodevalue,String processinstanceid);

}
