package com.zzb.conf.component;

/**
 * 
 * 
 * @author Administrator
 *
 */
public enum TaskTypeNameEnum4Group {
	
	BAOJIA("报价任务",new String[]{"人工调整","人工规则报价","人工报价","人工回写","报价退回"}),
	HEBAO("核保任务",new String[]{"人工核保","核保退回"}),
	ZHIFU("支付任务",new String[]{"支付"}),
	ERZHI("二支任务",new String[]{"二次支付确认"}),
	CHENBAO("承保任务",new String[]{"人工承保","承保退回"}),
	DADAN("打单任务",new String[]{"打单"}),
	RENZHENG("认证任务",new String[]{"认证任务"});

	private final String type;
	private final String[] types;
	private TaskTypeNameEnum4Group(String type, String[] types) {
		this.type = type;
		this.types = types;
	}
	
	public static final String[] getTypesByType(String type){
		for (TaskTypeNameEnum4Group e:values()){
			if (e.getType().equals(type)){
				return e.getTypes();
			}
		}
		throw new IllegalArgumentException("no such nodeKey:"+type);
	}
	
	
	public String getType() {
		return type;
	}
	
	public String[] getTypes() {
		return types;
	}
	
}
