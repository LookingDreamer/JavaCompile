package com.zzb.warranty.model;

import com.cninsure.core.dao.domain.Identifiable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.zzb.conf.entity.INSBAgent;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class INSEOrder extends BaseModel implements Identifiable {
	private static final long serialVersionUID = 1L;

	private String transactionId;
	/**
	 * 任务id
	 */
	private String taskid;

	/**
	 * 订单号
	 */
	private String orderno;

	/**
	 * 订单状态与insccode相关联
	 */
	private int orderstatus;

	/**
	 * 支付状态与insccode相关联
	 */
	private String paymentstatus;

	/**
	 * 配送状态与insccode相关联
	 */
	private String deliverystatus;

	/**
	 * 销售渠道与insccode相关联
	 */
	private String buyway;

    private INSBAgent agent;

	/**
	 * 该订单的代理人编码
	 */
	private String agentcode;

	/**
	 * 该订单的代理人姓名
	 */
	private String agentname;

	/**
	 * 录单人
	 */
	private String inputusercode;

	/**
	 * 币种编码
	 */
	private String currency;

	/**
	 * 配送费（总）
	 */
	private Double totaldeliveryamount;

	/**
	 * 实付金额：配送费 + 订单项实付总额
	 */
	private Double totalpaymentamount;

	/**
	 * 产品标价总金额
	 */
	private Double totalproductamount;

	/**
	 * 优惠金额：产品标价总金额 - (实付金额 - 配送费)
	 */
	private Double totalpromotionamount;

	/**
	 * 出单网点
	 */
	private String deptcode;

	/**
	 * 字典表
	 */
	private String paymentmethod;
	/**
	 * insbOrderDao.selectOrderByTaskId(param)---调用此方法才关联查询了此支付渠道名称
	 * 关联service方法com.zzb.cm.service.impl.INSBOrderServiceImpl.getPaymentinfo(String, String)
	 * paymentmethod对应的支付名称
	 */
	private String paychannelname;
	/**
	 * 01-续保,02-转保,03-新保,10-异地(客户忠诚度)
	 */
	private String customerinsurecode;

	/**
	 * 供应商
	 */
	private String prvid;
	private String deptshortname;// 订单所属机构名称
	private String delivetype;// 配送类型
	private String insorderno;  // 保险公司订单号

	private boolean needinvoice;
	private String invoiceemail;
	private int extendwarrantytype;

    private List<UserCoupon> userCoupons;

    private INSEPolicy policy;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public boolean isNeedinvoice() {
		return needinvoice;
	}

	public void setNeedinvoice(boolean needinvoice) {
		this.needinvoice = needinvoice;
	}

	public String getInvoiceemail() {
		return invoiceemail;
	}

	public void setInvoiceemail(String invoiceemail) {
		this.invoiceemail = invoiceemail;
	}

	public String getInsorderno() {
		return insorderno;
	}

	public void setInsorderno(String insorderno) {
		this.insorderno = insorderno;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public String getPaymentstatus() {
		return paymentstatus;
	}

	public void setPaymentstatus(String paymentstatus) {
		this.paymentstatus = paymentstatus;
	}

	public String getDeliverystatus() {
		return deliverystatus;
	}

	public void setDeliverystatus(String deliverystatus) {
		this.deliverystatus = deliverystatus;
	}

	public String getBuyway() {
		return buyway;
	}

	public void setBuyway(String buyway) {
		this.buyway = buyway;
	}

	public String getAgentcode() {
		return agentcode;
	}

	public void setAgentcode(String agentcode) {
		this.agentcode = agentcode;
	}

	public String getAgentname() {
		return agentname;
	}

	public void setAgentname(String agentname) {
		this.agentname = agentname;
	}

	public String getInputusercode() {
		return inputusercode;
	}

	public void setInputusercode(String inputusercode) {
		this.inputusercode = inputusercode;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Double getTotaldeliveryamount() {
		return totaldeliveryamount;
	}

	public String getDeptshortname() {
		return deptshortname;
	}

	public void setDeptshortname(String deptshortname) {
		this.deptshortname = deptshortname;
	}

	public void setTotaldeliveryamount(Double totaldeliveryamount) {
		this.totaldeliveryamount = totaldeliveryamount;
	}

	public Double getTotalpaymentamount() {
		return totalpaymentamount;
	}

	public void setTotalpaymentamount(Double totalpaymentamount) {
		this.totalpaymentamount = totalpaymentamount;
	}

	public Double getTotalproductamount() {
		return totalproductamount;
	}

	public void setTotalproductamount(Double totalproductamount) {
		this.totalproductamount = totalproductamount;
	}

	public Double getTotalpromotionamount() {
		return totalpromotionamount;
	}

	public void setTotalpromotionamount(Double totalpromotionamount) {
		this.totalpromotionamount = totalpromotionamount;
	}

	public String getDeptcode() {
		return deptcode;
	}

	public void setDeptcode(String deptcode) {
		this.deptcode = deptcode;
	}

	public String getPaymentmethod() {
		return paymentmethod;
	}

	public void setPaymentmethod(String paymentmethod) {
		this.paymentmethod = paymentmethod;
	}

	public String getPaychannelname() {
		return paychannelname;
	}

	public void setPaychannelname(String paychannelname) {
		this.paychannelname = paychannelname;
	}

	public String getCustomerinsurecode() {
		return customerinsurecode;
	}

	public void setCustomerinsurecode(String customerinsurecode) {
		this.customerinsurecode = customerinsurecode;
	}

	public String getPrvid() {
		return prvid;
	}

	public void setPrvid(String prvid) {
		this.prvid = prvid;
	}

	public String getDelivetype() {
		return delivetype;
	}

	public void setDelivetype(String delivetype) {
		this.delivetype = delivetype;
	}

	public int getOrderstatus() {
		return orderstatus;
	}

	public void setOrderstatus(int orderstatus) {
		this.orderstatus = orderstatus;
	}

	public int getExtendwarrantytype() {
		return extendwarrantytype;
	}

	public void setExtendwarrantytype(int extendwarrantytype) {
		this.extendwarrantytype = extendwarrantytype;
    }

    public List<UserCoupon> getUserCoupons() {
        return userCoupons;
    }

    public void setUserCoupons(List<UserCoupon> userCoupons) {
        this.userCoupons = userCoupons;
    }

    public INSEPolicy getPolicy() {
        return policy;
    }

    public void setPolicy(INSEPolicy policy) {
        this.policy = policy;
    }

    public INSBAgent getAgent() {
        return agent;
    }

    public void setAgent(INSBAgent agent) {
        this.agent = agent;
    }
}