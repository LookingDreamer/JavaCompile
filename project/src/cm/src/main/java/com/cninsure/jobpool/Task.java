package com.cninsure.jobpool;

import java.io.Serializable;
import java.util.List;


/**
 * 工作对象 进入 工作池封装的对象
 * 
 * @author hxx
 *
 */
public class Task implements Serializable{

	private String proInstanceId; // 主实例ID
	private String sonProInstanceId; // 子实例ID
	private String prvcode;//供应商编码
	private String taskName;// 节点名称
	private String taskcode;// 节点编码
	private String group;//任务组
	private String dispatchUser;// 处理人
	private int weight;// 权重
	private String taskTracks;// 订单处理人轨迹
	private List<String> taskRelated;// 相关子任务（人工规则报价使用）
	
	public List<String> getTaskRelated() {
		return taskRelated;
	}
	public void setTaskRelated(List<String> taskRelated) {
		this.taskRelated = taskRelated;
	}
	/**
	 * 调度标记 
	 * 	 true 可以调度
	 */
	private boolean dispatchflag =true;
	public String getProInstanceId() {
		return proInstanceId;
	}
	public void setProInstanceId(String proInstanceId) {
		this.proInstanceId = proInstanceId;
	}
	public String getSonProInstanceId() {
		return sonProInstanceId;
	}
	public void setSonProInstanceId(String sonProInstanceId) {
		this.sonProInstanceId = sonProInstanceId;
	}
	public String getPrvcode() {
		return prvcode;
	}
	public void setPrvcode(String prvcode) {
		this.prvcode = prvcode;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getTaskcode() {
		return taskcode;
	}
	public void setTaskcode(String taskcode) {
		this.taskcode = taskcode;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getDispatchUser() {
		return dispatchUser;
	}
	public void setDispatchUser(String dispatchUser) {
		this.dispatchUser = dispatchUser;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public String getTaskTracks() {
		return taskTracks;
	}
	public void setTaskTracks(String taskTracks) {
		this.taskTracks = taskTracks;
	}
	public boolean isDispatchflag() {
		return dispatchflag;
	}
	public void setDispatchflag(boolean dispatchflag) {
		this.dispatchflag = dispatchflag;
	}
	@Override
	public String toString() {
		return "Task [proInstanceId=" + proInstanceId + ", sonProInstanceId="
				+ sonProInstanceId + ", prvcode=" + prvcode + ", taskName="
				+ taskName + ", taskcode=" + taskcode + ", group=" + group + ", dispatchUser=" + dispatchUser
				+ ", weight=" + weight + ", taskTracks=" + taskTracks
				+ ", dispatchflag=" + dispatchflag + "]";
	}
}
