package com.zzb.ocr.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by chengxt1202 on 15/11/21.
 */
@XmlRootElement(name = "root")
public class IDCardResult {

	private String error;
	private String status;
	private IDCardEntity data;

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

	public IDCardEntity getData() {
		return data;
	}

	public void setData(IDCardEntity data) {
		this.data = data;
	}

}
