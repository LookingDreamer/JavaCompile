package com.zzb.model;

import java.util.Date;
import java.util.List;

import com.zzb.conf.entity.INSBEdidescription;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.entity.INSBProviderandedi;


public class EDIConfModel {
	
	/**
	 *	EDI配置表id 
	 */
	private String ediconfid;
	
	/**
	 * EDI描述表id
	 */
	private String edidescid;
	
	/**
	 * 名称
	 */
	private String ediname;
	
	/**
	 * 说明
	 */
	private String explains;
	
	/**
	 * 操作人
	 */
	private String operator;
	
	/**
	 * 备注
	 */
	private String noti;
	
	/**
	 * 创建时间
	 */
	private Date createtime;
	
	/**
	 * 修改时间
	 */
	private Date modifytime;
	
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
	 * edi描述信息
	 */
	private List<INSBEdidescription> insbedidescription;
	
	/**
	 * 供应商和edi配置关系表
	 */
	private List<INSBProviderandedi> insbproviderandedi;
	/**
	 * 管理供应商表
	 */
	private INSBProvider insbprovider;
	
	/**
	 * edi配置路径
	 */
	private String edipath;
	
	public String getEdidescid() {
		return edidescid;
	}

	public void setEdidescid(String edidescid) {
		this.edidescid = edidescid;
	}

	public String getEdiconfid() {
		return ediconfid;
	}

	public void setEdiconfid(String ediconfid) {
		this.ediconfid = ediconfid;
	}

	public String getEdiname() {
		return ediname;
	}

	public void setEdiname(String ediname) {
		this.ediname = ediname;
	}


	public String getExplains() {
		return explains;
	}

	public void setExplains(String explains) {
		this.explains = explains;
	}

	public List<INSBEdidescription> getInsbedidescription() {
		return insbedidescription;
	}

	public void setInsbedidescription(List<INSBEdidescription> insbedidescription) {
		this.insbedidescription = insbedidescription;
	}

	public INSBProvider getInsbprovider() {
		return insbprovider;
	}

	public void setInsbprovider(INSBProvider insbprovider) {
		this.insbprovider = insbprovider;
	}


	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getNoti() {
		return noti;
	}

	public void setNoti(String noti) {
		this.noti = noti;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getModifytime() {
		return modifytime;
	}

	public void setModifytime(Date modifytime) {
		this.modifytime = modifytime;
	}

	public List<INSBProviderandedi> getInsbproviderandedi() {
		return insbproviderandedi;
	}

	public void setInsbproviderandedi(List<INSBProviderandedi> insbproviderandedi) {
		this.insbproviderandedi = insbproviderandedi;
	}

	public String getCapacityconf() {
		return capacityconf;
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

	public String getEdipath() {
		return edipath;
	}

	public void setEdipath(String edipath) {
		this.edipath = edipath;
	}
	
}
