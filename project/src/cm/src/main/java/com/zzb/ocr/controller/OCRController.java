package com.zzb.ocr.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zzb.cm.entity.INSBFilelibrary;
import com.zzb.cm.service.INSBFilelibraryService;
import com.zzb.ocr.bean.DrivingEntity;
import com.zzb.ocr.bean.IDCardEntity;
import com.zzb.ocr.controller.vo.ImgInfo;

@Controller
@RequestMapping("/ocr/*")
public class OCRController {
	@Resource
	private INSBFilelibraryService insbFilelibraryService;
	@Resource(name = "ocrClient")
	private com.zzb.ocr.client.OCRClient ocrClient;

	@RequestMapping(value = "dri", method = RequestMethod.POST)
	public DrivingEntity ocrDriving(@RequestBody ImgInfo vo) {
		try {
			// 图像id vo.getId()
			String id = vo.getId();
			INSBFilelibrary insbFilelibrary = insbFilelibraryService.queryById(id);
			String filephsicypath = insbFilelibrary.getFilephysicalpath();
			return ocrClient.ocrDriving(filephsicypath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new DrivingEntity();
	}

	@RequestMapping(value = "idc", method = RequestMethod.POST)
	public IDCardEntity ocrIdCard(@RequestBody ImgInfo vo) {
		try {
			String id = vo.getId();
			INSBFilelibrary insbFilelibrary = insbFilelibraryService.queryById(id);
			String filephsicypath = insbFilelibrary.getFilephysicalpath();
			return ocrClient.ocrIdCard(filephsicypath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new IDCardEntity();
	}

}
