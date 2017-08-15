package com.zzb.cm.Interface.entity.cif.model;


import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


@SuppressWarnings("serial")
public class PersonInfo extends BaseEntity implements Identifiable{
	String ownerrealname;
	String owneridtype;
	String owneridno;
	String ownermobile;
	String ownermail;
	String ownerwxcode;
	String ownerregistration;
	String owneradress;
	String contactorrealname;
	String contactormobile;
	String contactoridtype;
	String contactoridno;
	String contactormail;
	String contactorwxcode;
	String contactoradress;
	String vehicleno;
	String type;
	String personsource;
	public String getOwnerrealname() {
		return ownerrealname;
	}
	public void setOwnerrealname(String ownerrealname) {
		this.ownerrealname = ownerrealname;
	}
	public String getOwneridno() {
		return owneridno;
	}
	public void setOwneridno(String owneridno) {
		this.owneridno = owneridno;
	}
	public String getOwnermobile() {
		return ownermobile;
	}
	public void setOwnermobile(String ownermobile) {
		this.ownermobile = ownermobile;
	}
	public String getOwnermail() {
		return ownermail;
	}
	public void setOwnermail(String ownermail) {
		this.ownermail = ownermail;
	}
	public String getOwnerwxcode() {
		return ownerwxcode;
	}
	public void setOwnerwxcode(String ownerwxcode) {
		this.ownerwxcode = ownerwxcode;
	}
	public String getOwnerregistration() {
		return ownerregistration;
	}
	public void setOwnerregistration(String ownerregistration) {
		this.ownerregistration = ownerregistration;
	}
	public String getOwneradress() {
		return owneradress;
	}
	public void setOwneradress(String owneradress) {
		this.owneradress = owneradress;
	}
	public String getContactorrealname() {
		return contactorrealname;
	}
	public void setContactorrealname(String contactorrealname) {
		this.contactorrealname = contactorrealname;
	}
	public String getContactormobile() {
		return contactormobile;
	}
	public void setContactormobile(String contactormobile) {
		this.contactormobile = contactormobile;
	}
	public String getContactoridno() {
		return contactoridno;
	}
	public void setContactoridno(String contactoridno) {
		this.contactoridno = contactoridno;
	}
	public String getContactormail() {
		return contactormail;
	}
	public void setContactormail(String contactormail) {
		this.contactormail = contactormail;
	}
	public String getContactorwxcode() {
		return contactorwxcode;
	}
	public void setContactorwxcode(String contactorwxcode) {
		this.contactorwxcode = contactorwxcode;
	}
	public String getContactoradress() {
		return contactoradress;
	}
	public void setContactoradress(String contactoradress) {
		this.contactoradress = contactoradress;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOwneridtype() {
		return owneridtype;
	}
	public void setOwneridtype(String owneridtype) {
		this.owneridtype = owneridtype;
	}
	public String getContactoridtype() {
		return contactoridtype;
	}
	public void setContactoridtype(String contactoridtype) {
		this.contactoridtype = contactoridtype;
	}
	public String getVehicleno() {
		return vehicleno;
	}
	public void setVehicleno(String vehicleno) {
		this.vehicleno = vehicleno;
	}
	
	public String getPersonsource() {
		return personsource;
	}
	public void setPersonsource(String personsource) {
		this.personsource = personsource;
	}
	@Override
	public String toString() {
		return "PersonInfo [ ownerrealname="
				+ ownerrealname + ", owneridtype=" + owneridtype
				+ ", owneridno=" + owneridno + ", ownermobile=" + ownermobile
				+ ", ownermail=" + ownermail + ", ownerwxcode=" + ownerwxcode
				+ ", ownerregistration=" + ownerregistration + ", owneradress="
				+ owneradress + ", contactorrealname=" + contactorrealname
				+ ", contactormobile=" + contactormobile + ", contactoridtype="
				+ contactoridtype + ", contactoridno=" + contactoridno
				+ ", contactormail=" + contactormail + ", contactorwxcode="
				+ contactorwxcode + ", contactoradress=" + contactoradress
				+ ", vehicleno=" + vehicleno + ", type=" + type + "]";
	}
	
	
}
