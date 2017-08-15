package com.common;

public class CMMethod {
	/**
	 * 根据任务类型获取能力链接的类型
	 * @param taskType
	 * @return
	 */
	public static String getConfTypeByTaskType(String taskType){
		switch (taskType) {
			case "quote":
				return "01";
			case "quotequery":
				return "02";
			case "insure":
			case "autoinsure":
				return "02";
			case "insurequery":
				return "02";
			case "pay":
				return "02";
			case "approved":
				return "04";
			case "approvedquery":
				return "04";
			case "applypinbj":
				return "02";
			default:
				return "01";
		}
	}
}
