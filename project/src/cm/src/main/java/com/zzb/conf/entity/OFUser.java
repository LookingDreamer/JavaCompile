package com.zzb.conf.entity;

import java.util.Date;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class OFUser extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;
	/**
	 * 用户名
	 */
	 private String username;
	

	/**
	 * 密码
	 */
	private String plainPassword;
	
	/**
	 * 十六进制密码
	 */
	private String encryptedPassword;

	/**
	 * 姓名
	 */
	private String name;
	
	/**
	 * 邮箱
	 */
	private String email;
	
	/**
	 * 创建日期
	 */
	private Long creationDate =0L;

	/**
	 * 修改日期
	 */
	private Long modificationDate =0L;
	
	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPlainPassword() {
		return plainPassword;
	}


	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}


	public String getEncryptedPassword() {
		return encryptedPassword;
	}


	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}



	public Long getCreationDate() {
		return creationDate;
	}


	public void setCreationDate(Long creationDate) {
		this.creationDate = creationDate;
	}


	public Long getModificationDate() {
		return modificationDate;
	}


	public void setModificationDate(Long modificationDate) {
		this.modificationDate = modificationDate;
	}


	@Override
	public String toString() {
		return "OFUser [username=" + username + ", plainPassword=" + plainPassword + ", encryptedPassword="
				+ encryptedPassword + ", name=" + name + ", email=" + email + ", creationDate=" + creationDate
				+ ", modificationDate=" + modificationDate + "]";
	}

}