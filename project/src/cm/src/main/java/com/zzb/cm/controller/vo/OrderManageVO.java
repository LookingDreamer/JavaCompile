package com.zzb.cm.controller.vo;

public class OrderManageVO {

	private String orderstatus;
	private String qsimple;
	private String carlicenseno;
	private String comName;
	private String starTime;
	private String endTime;
	private String insuredname;
	private String usertype;
	private String photo;
	private String instanceid;
	private String providename;
	private String inscomcode;
	private String agentname;
	private String agentnum;
	private int currentpage;//当前页 
	private long totals;//总条数
	private int pagesize;//每页条数
	private int totalpage;//总页数
	
	public String getAgentname() {
		return agentname;
	}
	public void setAgentname(String agentname) {
		this.agentname = agentname;
	}
	public String getAgentnum() {
		return agentnum;
	}
	public void setAgentnum(String agentnum) {
		this.agentnum = agentnum;
	}
	public String getInscomcode() {
		return inscomcode;
	}
	public void setInscomcode(String inscomcode) {
		this.inscomcode = inscomcode;
	}
	/**
	 * @return the orderstatus
	 */
	public String getOrderstatus() {
		return orderstatus;
	}
	/**
	 * @param orderstatus the orderstatus to set
	 */
	public void setOrderstatus(String orderstatus) {
		this.orderstatus = orderstatus;
	}
	/**
	 * @return the comName
	 */
	public String getComName() {
		return comName;
	}
	/**
	 * @param comName the comName to set
	 */
	public void setComName(String comName) {
		this.comName = comName;
	}
	/**
	 * @return the starTime
	 */
	public String getStarTime() {
		return starTime;
	}
	/**
	 * @param starTime the starTime to set
	 */
	public void setStarTime(String starTime) {
		this.starTime = starTime;
	}
	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	/**
	 * @return the insuredname
	 */
	public String getInsuredname() {
		return insuredname;
	}
	/**
	 * @param insuredname the insuredname to set
	 */
	public void setInsuredname(String insuredname) {
		this.insuredname = insuredname;
	}
	/**
	 * @return the usertype
	 */
	public String getUsertype() {
		return usertype;
	}
	/**
	 * @param usertype the usertype to set
	 */
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	/**
	 * @return the photo
	 */
	public String getPhoto() {
		return photo;
	}
	/**
	 * @param photo the photo to set
	 */
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	/**
	 * @return the instanceid
	 */
	public String getInstanceid() {
		return instanceid;
	}
	/**
	 * @param instanceid the instanceid to set
	 */
	public void setInstanceid(String instanceid) {
		this.instanceid = instanceid;
	}
	/**
	 * @return the currentpage
	 */
	public int getCurrentpage() {
		return currentpage;
	}
	/**
	 * @param currentpage the currentpage to set
	 */
	public void setCurrentpage(int currentpage) {
		this.currentpage = currentpage;
	}
	/**
	 * @return the totals
	 */
	public long getTotals() {
		return totals;
	}
	/**
	 * @param totals the totals to set
	 */
	public void setTotals(long totals) {
		this.totals = totals;
	}
	/**
	 * @return the pagesize
	 */
	public int getPagesize() {
		return pagesize;
	}
	/**
	 * @param pagesize the pagesize to set
	 */
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}
	/**
	 * @return the qsimple
	 */
	public String getQsimple() {
		return qsimple;
	}
	/**
	 * @param qsimple the qsimple to set
	 */
	public void setQsimple(String qsimple) {
		this.qsimple = qsimple;
	}
	/**
	 * @return the totalpage
	 */
	public int getTotalpage() {
		return totalpage;
	}
	/**
	 * @param totalpage the totalpage to set
	 */
	public void setTotalpage(int totalpage) {
		this.totalpage = totalpage;
	}
	/**
	 * @return the carlicenseno
	 */
	public String getCarlicenseno() {
		return carlicenseno;
	}
	/**
	 * @param carlicenseno the carlicenseno to set
	 */
	public void setCarlicenseno(String carlicenseno) {
		this.carlicenseno = carlicenseno;
	}
	/**
	 * @return the providename
	 */
	public String getProvidename() {
		return providename;
	}
	/**
	 * @param providename the providename to set
	 */
	public void setProvidename(String providename) {
		this.providename = providename;
	}
	
}
