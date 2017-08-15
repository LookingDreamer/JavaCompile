package com.cninsure.jobpool;

import com.cninsure.system.entity.INSCUser;
/**
 * 派发信息
 * @author hxx
 *
 */
public class Dispatch{
	private INSCUser user;// 节点处理人；
	private String dispatchTime;//派发时间
	private INSCUser dispatchUser;//派发人
	private String recycleTime;//任务回收时间
	private INSCUser recycleUser;//回收人

	public INSCUser getDispatchUser() {
		return dispatchUser;
	}


	public String getDispatchTime() {
		return dispatchTime;
	}


	public void setDispatchTime(String dispatchTime) {
		this.dispatchTime = dispatchTime;
	}


	public String getRecycleTime() {
		return recycleTime;
	}


	public void setRecycleTime(String recycleTime) {
		this.recycleTime = recycleTime;
	}


	public void setDispatchUser(INSCUser dispatchUser) {
		this.dispatchUser = dispatchUser;
	}

	public INSCUser getUser() {
		return user;
	}

	public void setUser(INSCUser user) {
		this.user = user;
	}


	public INSCUser getRecycleUser() {
		return recycleUser;
	}

	public void setRecycleUser(INSCUser recycleUser) {
		this.recycleUser = recycleUser;
	}
	@Override
	public String toString() {
		return "TaskDispatch [dispatchTime=" + dispatchTime + ", dispatchUser="
				+ dispatchUser + ", user=" + user + ", recycleTime="
				+ recycleTime + ", recycleUser=" + recycleUser + "]";
	}
}
