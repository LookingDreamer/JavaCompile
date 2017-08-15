package com.zzb.model;

import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.entity.INSBRisk;
import com.zzb.conf.entity.INSBRiskitem;
import com.zzb.conf.entity.INSBRiskkind;




public class CifRiskModel {
	

	/**
	 * 类型  crud
	 */
	private String type;
	
	/**
	 * 确定操作那个表 insbrisk、insbprovider、itemset(INSBRiskkind数据项配置)、kindset(INSBRiskkind险别)
	 */
	public static final String CONTEND_INSBRISK="insbrisk";
	public static final String CONTEND_INSBPROVIDER="insbprovider";
	public static final String CONTEND_ITEMSET="itemset";
	public static final String CONTEND_KINDSET="kindset";
	
	private String contend;
	
	public String getContend() {
		return contend;
	}

	public void setContend(String contend) {
		this.contend = contend;
	}

	private INSBRisk insbrisk;
	
	private INSBProvider insbprovider;
	
	private INSBRiskitem itemset;
	
	private INSBRiskkind kindset;
	
	/**
	 * 批量删除ids
	 */
	private String ids;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public INSBRisk getInsbrisk() {
		return insbrisk;
	}

	public void setInsbrisk(INSBRisk insbrisk) {
		this.insbrisk = insbrisk;
	}

	public INSBProvider getInsbprovider() {
		return insbprovider;
	}

	public void setInsbprovider(INSBProvider insbprovider) {
		this.insbprovider = insbprovider;
	}

	public INSBRiskitem getItemset() {
		return itemset;
	}

	public void setItemset(INSBRiskitem itemset) {
		this.itemset = itemset;
	}

	public INSBRiskkind getKindset() {
		return kindset;
	}

	public void setKindset(INSBRiskkind kindset) {
		this.kindset = kindset;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	@Override
	public String toString() {
		return "CifRiskModel [type=" + type + ", contend=" + contend
				+ ", insbrisk=" + insbrisk + ", insbprovider=" + insbprovider
				+ ", itemset=" + itemset + ", kindset=" + kindset + ", ids="
				+ ids + "]";
	}


}
