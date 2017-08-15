package com.zzb.mobile.model;

public class MsgPram {
		private String receiver;
		private String sender;
		private int offset;
		private int limit;
		private String messageID;
		private String messageStatus;

		
		public String getReceiver() {
			return receiver;
		}
		public void setReceiver(String receiver) {
			this.receiver = receiver;
		}
		public String getSender() {
			return sender;
		}
		public void setSender(String sender) {
			this.sender = sender;
		}
		public int getOffset() {
			return offset;
		}
		public void setOffset(int offset) {
			this.offset = offset;
		}
		public int getLimit() {
			return limit;
		}
		public void setLimit(int limit) {
			this.limit = limit;
		}
		public String getMessageID() {
			return messageID;
		}
		public void setMessageID(String messageID) {
			this.messageID = messageID;
		}
		public String getMessageStatus() {
			return messageStatus;
		}
		public void setMessageStatus(String messageStatus) {
			this.messageStatus = messageStatus;
		}
		
}
