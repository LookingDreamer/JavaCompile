package com.zzb.cm.entity.entityutil;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;

import com.zzb.cm.entity.INSBApplicant;
import com.zzb.cm.entity.INSBApplicanthis;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBCarinfohis;
import com.zzb.cm.entity.INSBCarmodelinfo;
import com.zzb.cm.entity.INSBCarmodelinfohis;
import com.zzb.cm.entity.INSBInsured;
import com.zzb.cm.entity.INSBInsuredhis;
import com.zzb.cm.entity.INSBLegalrightclaim;
import com.zzb.cm.entity.INSBLegalrightclaimhis;
import com.zzb.cm.entity.INSBRelationperson;
import com.zzb.cm.entity.INSBRelationpersonhis;
import com.zzb.cm.entity.INSBSpecifydriver;
import com.zzb.cm.entity.INSBSpecifydriverhis;

public class EntityTransformUtil {

	/**
	 * INSBInsured转换为INSBInsuredhis
	 */
	public static INSBInsuredhis insured2Insuredhis(INSBInsured insured,String inscomcode){
		INSBInsuredhis insuredhis = new INSBInsuredhis();
		try {
			PropertyUtils.copyProperties(insuredhis, insured);
			insuredhis.setInscomcode(inscomcode);
			insuredhis.setId(null);
			return insuredhis;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * INSBInsuredhis转换为INSBInsured
	 */
	public static INSBInsured insuredhis2Insured(INSBInsuredhis insuredhis){
		INSBInsured insured = new INSBInsured();
		try {
			PropertyUtils.copyProperties(insured, insuredhis);
			insured.setId(null);
			return insured;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * INSBApplicant转换为INSBApplicanthis
	 */
	public static INSBApplicanthis applicant2Applicanthis(INSBApplicant applicant,String inscomcode){
		INSBApplicanthis applicanthis = new INSBApplicanthis();
		try {
			PropertyUtils.copyProperties(applicanthis, applicant);
			applicanthis.setInscomcode(inscomcode);
			applicanthis.setId(null);
			return applicanthis;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * INSBApplicanthis转换为INSBApplicant
	 */
	public static INSBApplicant applicanthis2Applicant(INSBApplicanthis applicanthis){
		INSBApplicant applicant = new INSBApplicant();
		try {
			PropertyUtils.copyProperties(applicant, applicanthis);
			applicant.setId(null);
			return applicant;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * INSBRelationperson转换为INSBRelationpersonhis
	 */
	public static INSBRelationpersonhis relationperson2Relationpersonhis(INSBRelationperson relationperson,String inscomcode){
		INSBRelationpersonhis relationpersonhis = new INSBRelationpersonhis();
		try {
			PropertyUtils.copyProperties(relationpersonhis, relationperson);
			relationpersonhis.setInscomcode(inscomcode);
			relationpersonhis.setId(null);
			return relationpersonhis;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * INSBRelationpersonhis转换为INSBRelationperson
	 */
	public static INSBRelationperson relationpersonhis2Relationperson(INSBRelationpersonhis relationpersonhis){
		INSBRelationperson relationperson = new INSBRelationperson();
		try {
			PropertyUtils.copyProperties(relationperson, relationpersonhis);
			relationperson.setId(null);
			return relationperson;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * INSBLegalrightclaim转换为INSBLegalrightclaimhis
	 */
	public static INSBLegalrightclaimhis legalrightclaim2Legalrightclaimhis(INSBLegalrightclaim legalrightclaim,String inscomcode){
		INSBLegalrightclaimhis legalrightclaimhis = new INSBLegalrightclaimhis();
		try {
			PropertyUtils.copyProperties(legalrightclaimhis, legalrightclaim);
			legalrightclaimhis.setInscomcode(inscomcode);
			legalrightclaimhis.setId(null);
			return legalrightclaimhis;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * INSBLegalrightclaimhis转换为INSBLegalrightclaim
	 */
	public static INSBLegalrightclaim legalrightclaimhis2Legalrightclaim(INSBLegalrightclaimhis legalrightclaimhis){
		INSBLegalrightclaim legalrightclaim = new INSBLegalrightclaim();
		try {
			PropertyUtils.copyProperties(legalrightclaim, legalrightclaimhis);
			legalrightclaim.setId(null);
			return legalrightclaim;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * INSBSpecifydriver转换为INSBSpecifydriverhis
	 */
	public static INSBSpecifydriverhis specifydriver2Specifydriverhis(INSBSpecifydriver specifydriver,String inscomcode){
		INSBSpecifydriverhis specifydriverhis = new INSBSpecifydriverhis();
		try {
			PropertyUtils.copyProperties(specifydriverhis, specifydriver);
			specifydriverhis.setInscomcode(inscomcode);
			specifydriverhis.setId(null);
			return specifydriverhis;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * INSBSpecifydriverhis转换为INSBSpecifydriver
	 */
	public static INSBSpecifydriver specifydriverhis2Specifydriver(INSBSpecifydriverhis specifydriverhis){
		INSBSpecifydriver specifydriver = new INSBSpecifydriver();
		try {
			PropertyUtils.copyProperties(specifydriver, specifydriverhis);
			specifydriver.setId(null);
			return specifydriver;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 车辆信息历史表对象转车辆信息对象
	 * */
	public static INSBCarinfo carinfohis2Carinfo(INSBCarinfohis carinfohis){
		INSBCarinfo carinfo = new INSBCarinfo();
		try {
			PropertyUtils.copyProperties(carinfo, carinfohis);
			carinfo.setId(null);
			return carinfo;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 车型信息历史表对象转车型信息对象
	 * */
	public static INSBCarmodelinfo carmodelinfohis2Carmodelinfo(INSBCarmodelinfohis carmodelinfohis){
		INSBCarmodelinfo carmodelinfo = new INSBCarmodelinfo();
		try {
			PropertyUtils.copyProperties(carmodelinfo, carmodelinfohis);
			carmodelinfo.setId(null);
			return carmodelinfo;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 车辆信息对象转车辆信息历史表对象
	 * */
	public static INSBCarinfohis carinfo2Carinfohis(INSBCarinfo carinfo, String inscomcode){
		INSBCarinfohis carinfohis = new INSBCarinfohis();
		try {
			PropertyUtils.copyProperties(carinfohis, carinfo);
			carinfohis.setInscomcode(inscomcode);
			carinfohis.setId(null);
			return carinfohis;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 车型信息对象转车型信息历史表对象
	 * */
	public static INSBCarmodelinfohis carmodelinfo2Carmodelinfohis(INSBCarmodelinfo carmodelinfo, String inscomcode){
		INSBCarmodelinfohis carmodelinfohis = new INSBCarmodelinfohis();
		try {
			PropertyUtils.copyProperties(carmodelinfohis, carmodelinfo);
			carmodelinfohis.setInscomcode(inscomcode);
			carmodelinfohis.setId(null);
			return carmodelinfohis;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
