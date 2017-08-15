package com.zzb.cm.controller.vo;

/*
 * 保存新的未读信息的接口参数对象类型
 */
public class NotReadMessageVo {
	/*
	 * 发送人
	 */
	private String fromPeople;
	/*
	 * 标题
	 */
	private String title;
	/*
	 * 内容
	 */
	private String content;
	/*
	 * 时间
	 */
	private String dateTime;
	/*
	 * 操作
	 */
	private String operator;
	/*
	 * 接收人
	 */
	private String reciever;
	/*
	 * 消息类型
	 */
	private String msgtype;
	
	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public String getReciever() {
		return reciever;
	}

	public void setReciever(String reciever) {
		this.reciever = reciever;
	}

	public NotReadMessageVo(){}

	public String getFromPeople() {
		return fromPeople;
	}

	public void setFromPeople(String fromPeople) {
		this.fromPeople = fromPeople;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	
}
