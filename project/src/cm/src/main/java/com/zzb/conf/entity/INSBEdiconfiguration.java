package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


/**
 * @author qiu
 *
 */
public class INSBEdiconfiguration extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;
	/**
	 * 行号
	 */
	private String rownum;
	/**
	 * 名称
	 */
	private String ediname;

	/**
	 * 说明
	 */
	private String explains;

	/**
	 * 能力配置
	 */
	private String capacityconf;
	/**
	 * 部署地址
	 */
	private String deployaddress;
	
	/**
	 * 接口地址
	 */
	private String interfaceaddress;
	/**
	 * 运维负责人
	 */
	private String operationsdirector;
	/**
	 * 联系方式
	 */
	private String contactway;
	
	/**
	 * EDI配置路径
	 */
	private String edipath;
	public String getEdiname() {
		return ediname;
	}

	public void setEdiname(String ediname) {
		this.ediname = ediname;
	}

	public String getCapacityconf() {
		return capacityconf;
	}

	public String getExplains() {
		return explains;
	}

	public void setExplains(String explains) {
		this.explains = explains;
	}

	public void setCapacityconf(String capacityconf) {
		this.capacityconf = capacityconf;
	}

	public String getDeployaddress() {
		return deployaddress;
	}

	public void setDeployaddress(String deployaddress) {
		this.deployaddress = deployaddress;
	}

	public String getInterfaceaddress() {
		return interfaceaddress;
	}

	public void setInterfaceaddress(String interfaceaddress) {
		this.interfaceaddress = interfaceaddress;
	}

	public String getOperationsdirector() {
		return operationsdirector;
	}

	public void setOperationsdirector(String operationsdirector) {
		this.operationsdirector = operationsdirector;
	}

	public String getContactway() {
		return contactway;
	}

	public void setContactway(String contactway) {
		this.contactway = contactway;
	}

	public String getRownum() {
		return rownum;
	}

	public void setRownum(String rownum) {
		this.rownum = rownum;
	}

	public String getEdipath() {
		return edipath;
	}

	public void setEdipath(String edipath) {
		this.edipath = edipath;
	}

	@Override
	public String toString() {
		return "INSBEdiconfiguration [rownum=" + rownum + ", ediname="
				+ ediname + ", explains=" + explains + ", capacityconf="
				+ capacityconf + ", deployaddress=" + deployaddress
				+ ", interfaceaddress=" + interfaceaddress
				+ ", operationsdirector=" + operationsdirector
				+ ", contactway=" + contactway + ", edipath=" + edipath + "]";
	}
	
	
	
	
	
}