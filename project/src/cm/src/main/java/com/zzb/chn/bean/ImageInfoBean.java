package com.zzb.chn.bean;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 影像bean
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class ImageInfoBean {
	private String imageType;
	private String imageMode;
	private String imageUrl;
	private String imageContent;
	private String imageName; //影像名称
	private String upload; //是否已经上传 Y/N
	 
	public String getUpload() {
		return upload;
	}
	public void setUpload(String upload) {
		this.upload = upload;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public String getImageType() {
		return imageType;
	}
	public void setImageType(String imageType) {
		this.imageType = imageType;
	}
	public String getImageMode() {
		return imageMode;
	}
	public void setImageMode(String imageMode) {
		this.imageMode = imageMode;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getImageContent() {
		return imageContent;
	}
	public void setImageContent(String imageContent) {
		this.imageContent = imageContent;
	}
}
