package com.common;

/**
 * 常量
 * @author Administrator
 *
 */
public class ConstUtil {

	
	/**分页起始记录数**/
	public static Integer OFFSET=0;
	/**分页获取记录总数**/
	public static Integer LIMIT=1000;
	/**分页获取记录总数，不得大于此变量值**/
	public static Integer COUNT=2000;
	/**添加操作**/
	public static final String OPER_ADD = "add";
	/**更新操作**/
	public static final String OPER_UPDATE = "update";
	/**操作人，admin为系统管理员**/
	public static final String OPER_ADMIN = "admin";
	/***1:投保人，2：被保人，3：权益人，4：关系人***/
	public static final int STATUS_1=1;
	/***1:投保人，2：被保人，3：权益人，4：关系人***/
	public static final int STATUS_2=2;
	/***1:投保人，2：被保人，3：权益人，4：关系人***/
	public static final int STATUS_3=3;
	/***1:投保人，2：被保人，3：权益人，4：关系人***/
	public static final int STATUS_4=4;
	
	// 平台
	/** 北京平台deptcode **/
	public static final String PLATFORM_BEIJING_DEPTCODE = "1211000000";

	// 北京流程申请验证码
	public static final String TASKTYPE_APPLY_PIN_BJ = "applypinbj";
	// 申请验证码的状态
	/** 申请验证码中... **/
	public static final String PIN_APPLING = "0";
	/** 申请验证码成功 **/
	public static final String PIN_APPLY_SUCCESS = "1";
	/** 姓名或者身份证号错误 **/
	public static final String PIN_ERR_NAMEORCARDNUM = "2";
	/** 手机号绑定超限 **/
	public static final String PIN_ERR_OUTOF_PHONE = "3";
	/** 身份证绑定超限 **/
	public static final String PIN_ERR_OUTOF_IDCARD = "4";
	/** 代码异常 **/
	public static final String PIN_APPLY_CODEERR = "5";
	// 提交验证码的状态
	/** 提交验证码中... **/
	public static final String PIN_COMMITTING = "0";
	/** 提交-验证码提交成功 **/
	public static final String PIN_COMMIT_SUCCESS = "1";
	/** 提交-验证码有误,重新输入 **/
	public static final String PIN_COMMIT_ERROR = "2";
	/** 提交-验证码提交失败 **/
	public static final String PIN_COMMIT_FAIL = "3";
	/** 提交-验证码超时,重新申请 **/
	public static final String PIN_COMMIT_TIMEOUT = "4";
	/** 代码异常 **/
	public static final String PIN_COMMIT_CODEERR = "5";
	
	//文件类型
	/** 文件类型: 电子保单 **/
	public static final String FILE_TYPE_ELECPOLICY = "electricpolicy";

	//从规则获取的折扣系数的key
	public static final String[] DiscountKeys = new String[] {
			"geniusItem.policyDiscount", //整单折扣
			"geniusItem.insureDiscount", //自主核保系数
			"geniusItem.channelDiscount", //自主渠道系数
			"geniusItem.repairDiscount", //指定专修厂费率
			"geniusItem.vehicleDemageIns.coverageDiscount", //车损保额调整系数
			"geniusItem.theftIns.coverageDiscount", //盗抢保额调整系数
			"geniusItem.combustionIns.coverageDiscount" //自燃保额调整系数
	};
}
