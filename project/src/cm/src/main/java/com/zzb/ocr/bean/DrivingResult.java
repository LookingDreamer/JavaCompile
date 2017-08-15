package com.zzb.ocr.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by chengxt1202 on 15/11/21.
 */
@XmlRootElement(name = "root")
public class DrivingResult {

	private String error;
	private String status;
	private DrivingEntity data;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public DrivingEntity getData() {
		return data;
	}

	public void setData(DrivingEntity data) {
		this.data = data;
	}

}
