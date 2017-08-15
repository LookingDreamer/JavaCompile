package com.lzgapi.yzt.bean;

import java.util.List;

public class UserBindingValidateBean {
	private String status;
	private String message;
	private List<UserValidateBean> accounts;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<UserValidateBean> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<UserValidateBean> accounts) {
		this.accounts = accounts;
	}

	public class UserValidateBean {
		private String account;
		private String username;
		private String userid;
		private String ismanager;
		private String agentcode;
		private String idno;
		private String organization;

		public String getAccount() {
			return account;
		}

		public void setAccount(String account) {
			this.account = account;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getUserid() {
			return userid;
		}

		public void setUserid(String userid) {
			this.userid = userid;
		}

		public String getIsmanager() {
			return ismanager;
		}

		public void setIsmanager(String ismanager) {
			this.ismanager = ismanager;
		}

		public String getAgentcode() {
			return agentcode;
		}

		public void setAgentcode(String agentcode) {
			this.agentcode = agentcode;
		}

		public String getIdno() {
			return idno;
		}

		public void setIdno(String idno) {
			this.idno = idno;
		}

		public String getOrganization() {
			return organization;
		}

		public void setOrganization(String organization) {
			this.organization = organization;
		}

	}

}
