package com.zzb.conf.component;

import java.util.List;
import java.util.Map;

import com.zzb.conf.entity.INSBAgreement;

/**
 * 补充信息项Model
 * @author XiongNeng
 * @since 13-1-6
 * @version 1.0
 */
public class SupplementModel {
    // 补充项key
   private String key;
    // 是否基本数据类型
   private boolean isBasicItem;
    // 中文显示名称
   private String metadataName;
    // LHS/RHS
   private String metadataRange;
    // 是否有默认值
   private boolean isDefault;
    // 默认值
   private String defaultValue;
    // 旧的规则param传的key
   private String metadataSource;
    // 新的规则param传的key
   private String metadataDefine;
    // 该补充项被下列规则ID所使用
   private List<Long> validRuleIdList;
    // 函数/选择列表
   private String metadataValue;
    // 如果是选择列表，选择列表转换后的Map
   private Map<String, String> metadataValueMap;
    // 是否用户输入
   private boolean isUserInput;
    // 补充项的数据类型
   private String metadataType;
   private int order=999;
   public int getOrder() {
	   return order;
   }
   public void setOrder(int order) {
	   this.order = order;
   }
	/** 页面控件类型：*/
   private String type = TYPE_STRING;
    public static final String TYPE_LIST = "list";
    public static final String TYPE_BOOLEAN = "boolean";
    public static final String TYPE_DATE= "date";
    public static final String TYPE_NUMBER = "number";
    public static final String TYPE_STRING = "string";

    /**********************分割线*************************/
    /** 此补充项被那些供应商使用*/
    List<INSBAgreement> providerAgreementList;
    /** 此补充项的值*/
    private String value;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public boolean isBasicItem() {
		return isBasicItem;
	}
	public void setBasicItem(boolean isBasicItem) {
		this.isBasicItem = isBasicItem;
	}
	public String getMetadataName() {
		return metadataName;
	}
	public void setMetadataName(String metadataName) {
		this.metadataName = metadataName;
	}
	public String getMetadataRange() {
		return metadataRange;
	}
	public void setMetadataRange(String metadataRange) {
		this.metadataRange = metadataRange;
	}
	public boolean isDefault() {
		return isDefault;
	}
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getMetadataSource() {
		return metadataSource;
	}
	public void setMetadataSource(String metadataSource) {
		this.metadataSource = metadataSource;
	}
	public String getMetadataDefine() {
		return metadataDefine;
	}
	public void setMetadataDefine(String metadataDefine) {
		this.metadataDefine = metadataDefine;
	}
	public List<Long> getValidRuleIdList() {
		return validRuleIdList;
	}
	public void setValidRuleIdList(List<Long> validRuleIdList) {
		this.validRuleIdList = validRuleIdList;
	}
	public String getMetadataValue() {
		return metadataValue;
	}
	public void setMetadataValue(String metadataValue) {
		this.metadataValue = metadataValue;
	}
	public Map<String, String> getMetadataValueMap() {
		return metadataValueMap;
	}
	public void setMetadataValueMap(Map<String, String> metadataValueMap) {
		this.metadataValueMap = metadataValueMap;
	}
	public boolean isUserInput() {
		return isUserInput;
	}
	public void setUserInput(boolean isUserInput) {
		this.isUserInput = isUserInput;
	}
	public String getMetadataType() {
		return metadataType;
	}
	public void setMetadataType(String metadataType) {
		this.metadataType = metadataType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<INSBAgreement> getProviderAgreementList() {
		return providerAgreementList;
	}
	public void setProviderAgreementList(List<INSBAgreement> providerAgreementList) {
		this.providerAgreementList = providerAgreementList;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
    
}
