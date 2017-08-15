package com.cninsure.system.ldap;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.entity.LdapOrgBean;
import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPAttributeSet;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPModification;
import com.novell.ldap.LDAPSearchResults;
import com.zzb.mobile.util.ConfigUtil;

public class LdapOrgManager {
	private static final String LDAP_URL = ConfigUtil.get("ldap.host");// 连接地址
	private static final int LDAP_PORT = Integer.parseInt(ConfigUtil
			.get("ldap.port"));// 端口
	private static final String MANAGER_USR = ConfigUtil.get("ldap.loginDN");// 用户名
	private static final String MANAGER_PASSWD = ConfigUtil
			.get("ldap.loginPasswd");// 密码
	public static final String baseDN = ConfigUtil.get("ldap.baseDN");
	public static final String LDAP_ORG = "organization";//机构
	public static final String LDAP_ORG_UNIT = "organizationalUnit";

	@SuppressWarnings("unchecked")
	public LdapOrgBean searchOrganization(LdapOrgBean bean) throws Exception {
		if (bean == null || StringUtil.isEmpty(bean.getObjectClass())
				|| StringUtil.isEmpty(bean.getDestinationIndicator()))
			return null;
		LdapOrgBean model = null;
		LDAPConnection lc = getConnection();
		try {
			LDAPSearchResults searchResults = lc.search(
					baseDN,
					LDAPConnection.SCOPE_SUB,
					"(&(objectClass=" + bean.getObjectClass()
							+ ")(destinationIndicator="
							+ bean.getDestinationIndicator() + "))", null,
					false);

			if (searchResults == null || !searchResults.hasMore())
				return null;
			if (searchResults.hasMore()) {
				model = new LdapOrgBean();
				LDAPEntry nextEntry = (LDAPEntry) searchResults.next();
				LDAPAttributeSet attributeSet = nextEntry.getAttributeSet();

				BeanInfo beanInfo = Introspector.getBeanInfo(LdapOrgBean.class);
				PropertyDescriptor[] proDescrtptors = beanInfo
						.getPropertyDescriptors();
				StringBuffer sb = new StringBuffer();
				if (proDescrtptors != null && proDescrtptors.length > 0) {
					for (PropertyDescriptor propDesc : proDescrtptors) {
						sb = new StringBuffer();
						if (propDesc.getName().equals("class"))
							continue;
						if (propDesc.getName().equals("DN")) {
							model.setDN(nextEntry.getDN());
							continue;
						}
						if (propDesc.getName().equals("cn"))
							continue;
						if (propDesc.getName().equals("successFlag"))
							continue;
						if (propDesc.getName().equals("parentDN")) {
							model.setParentDN(nextEntry.getDN().substring(
									nextEntry.getDN().indexOf(",") + 1));
							continue;
						}
						LDAPAttribute temp = (LDAPAttribute) attributeSet
								.getAttribute(propDesc.getName());
						if (temp == null)
							continue;
						Enumeration<String> values = temp.getStringValues();
						if (values == null)
							continue;
						while (values.hasMoreElements()) {
							if (sb.length() > 0) {
								sb.append("|");
							}
							sb.append(values.nextElement());
						}
						Method methodSetUserName = propDesc.getWriteMethod();
						methodSetUserName.invoke(model, sb.toString());
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			closeConnection(lc);
		}
		return model;
	}

	
	public LDAPConnection getConnection() throws Exception {
		LDAPConnection lc = new LDAPConnection();
		if (lc != null) {
			try {
				if(!lc.isConnected()){
					lc.connect(LDAP_URL, LDAP_PORT);
				}
				if(!lc.isBound()){
					lc.bind(LDAPConnection.LDAP_V3, MANAGER_USR,
							MANAGER_PASSWD.getBytes("UTF8"));
				}
			} catch (LDAPException e) {
				e.printStackTrace();
				throw new Exception(e);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				throw new Exception(e);
			}
		}
		return lc;
	}

	public void closeConnection(LDAPConnection lc) throws Exception {
		if (lc != null) {
			try {
				lc.disconnect();
			} catch (LDAPException e) {
				e.printStackTrace();
				throw new Exception(e);
			}
		}
	}

	/**
	 * 向Ldap中添加机构
	 * 
	 * @param bean
	 * @return
	 * @throws Exception 
	 */
	public LdapOrgBean addOrganization(LdapOrgBean bean) throws Exception {
		LDAPConnection lc = getConnection();
		try {
			LDAPAttributeSet attrs = new LDAPAttributeSet();
			/*
			 * LDAPAttribute objclassSet = new LDAPAttribute("objectClass");
			 * objclassSet.addValue("cninsureOrginfo"); attrs.add(objclassSet);
			 */
			BeanInfo beanInfo = Introspector.getBeanInfo(LdapOrgBean.class);
			PropertyDescriptor[] proDescrtptors = beanInfo
					.getPropertyDescriptors();
			String obj = null;
			LDAPAttribute attr;
			if (proDescrtptors != null && proDescrtptors.length > 0) {
				for (PropertyDescriptor propDesc : proDescrtptors) {
					if (propDesc.getName().equals("class"))
						continue;
					if (propDesc.getName().equals("DN"))
						continue;
					if (propDesc.getName().equals("parentDN"))
						continue;
					if (propDesc.getName().equals("cn"))
						continue;
					if (propDesc.getName().equals("successFlag"))
						continue;
					
					Method get = propDesc.getReadMethod();
					obj = (String) get.invoke(bean);

					if (obj != null && obj.indexOf("|") != -1) {
						for (String val : obj.split("\\|")) {
							if (val != null) {
								attr = new LDAPAttribute(propDesc.getName());
								attr.addValue(val);
								attrs.add(attr);
							}
						}

					} else {
						if (obj != null) {
							attr = new LDAPAttribute(propDesc.getName());
							attr.addValue(obj);
							attrs.add(attr);
						}
					}

				}
			}
			if (bean.getParentDN() == null || "".equals(bean.getParentDN())) {
				bean.setDN(bean.getCn() + "," + baseDN);
			} else {
				bean.setDN(bean.getCn() + "," + bean.getParentDN() + ","
						+ baseDN);
			}
			System.out.println("ldap add bean:"+bean);
			LDAPEntry newEntry = new LDAPEntry(bean.getDN(), attrs);
			lc.add(newEntry);
			bean.setSuccessFlag("true");
		} catch (Exception e) {
			bean.setSuccessFlag("false");
			e.printStackTrace();
			throw e;
		} finally {
			closeConnection(lc);
		}
		return bean;
	}

	/**
	 * 
	 * @param comcode
	 * @return
	 * @throws Exception 
	 */
	public boolean deleteEntity(LdapOrgBean bean) throws Exception {
		LDAPConnection lc = getConnection();
		try {
			/*
			 * LDAPSearchConstraints cons = lc.getSearchConstraints();
			 * cons.setBatchSize(0); cons.setMaxResults(5);
			 */
			LdapOrgBean model = this.searchOrganization(bean);
			if (model != null)
				lc.delete(model.getDN());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			closeConnection(lc);
		}
	}

	public boolean modifyOrg(LdapOrgBean bean) throws Exception {
		LDAPConnection lc = getConnection();
		try {
			List<LDAPModification> list = new ArrayList<LDAPModification>();
			LDAPAttribute attribute = null;
			BeanInfo beanInfo = Introspector.getBeanInfo(LdapOrgBean.class);
			PropertyDescriptor[] proDescrtptors = beanInfo
					.getPropertyDescriptors();
			String obj = null;
			if (proDescrtptors != null && proDescrtptors.length > 0) {
				for (PropertyDescriptor propDesc : proDescrtptors) {
					if (propDesc.getName().equals("class"))
						continue;
					if (propDesc.getName().equals("DN"))
						continue;
					if (propDesc.getName().equals("parentDN"))
						continue;
					if (propDesc.getName().equals("cn"))
						continue;
					if (propDesc.getName().equals("objectClass"))
						continue;
					if (propDesc.getName().equals("o"))
						continue;
					if (propDesc.getName().equals("ou"))
						continue;
					if (propDesc.getName().equals("successFlag"))
						continue;
					Method get = propDesc.getReadMethod();
					obj = (String) get.invoke(bean);

					if (obj != null && obj.indexOf("|") != -1) {
						for (String val : obj.split("\\|")) {
							if (val != null) {
								attribute = new LDAPAttribute(
										propDesc.getName(), val);
								list.add(new LDAPModification(
										LDAPModification.REPLACE, attribute));
							}
						}

					} else {
						if (obj != null) {
							attribute = new LDAPAttribute(propDesc.getName(),
									obj);
							list.add(new LDAPModification(
									LDAPModification.REPLACE, attribute));
						}
					}

					attribute = null;
				}
			}

			LDAPModification[] mods = new LDAPModification[list.size()];
			mods = (LDAPModification[]) list.toArray(mods);
			System.out.println("ldap modify bean:"+bean);
			lc.modify(bean.getDN(), mods);
			return true;
		} catch (Exception ne) {
			ne.printStackTrace();
			throw ne;
		} finally {
			closeConnection(lc);
		}
		
	}
	public static void main(String[] args) throws Exception {
		LdapOrgBean bean = new LdapOrgBean();
		// bean.setObjectClass("inetOrgPerson");
		// bean.setParentDN("");
		// bean.setCn("ttsts");
		bean.setObjectClass("organization"); //
		bean.setCn("o=orgs"); // cn
		bean.setO("orgs");// 机构
		/*
		 * bean.setCn("o=xyzz0122"); // cn
		 * bean.setParentDN("o=organizations");// parentDN
		 * bean.setO("xyzz0122");// 机构
		 */
		// bean.setOu("");// 网点
		bean.setBusinessCategory("001");// 机构编码
		bean.setDestinationIndicator("001");// 机构编码
		bean.setDescription("机构名称1");// 机构名称
		bean.setPhysicalDeliveryOfficeName("机构1");// 短名称
		bean.setTelexNumber("1");// 机构级别
		bean.setSt("00300");// 所在省
		bean.setL("00301");// 所在市
		bean.setPostalAddress("地址...");// 机构地址
		bean.setPostalCode("10086");// 机构邮编
		bean.setTelephoneNumber("010-110");// 机构电话
		bean.setFacsimileTelephoneNumber("010-111");// 机构传真
		bean.setStreet("米亚");// 机构负责人姓名
		bean.setPostOfficeBox("123@cninsure.net");//EMail
		bean.setRegisteredAddress("www.cninsure.net");//网址

		LdapOrgManager lm = new LdapOrgManager();
		/*
		 * LdapOrgBean addOrganization = lm.addOrganization(bean);
		 * System.out.println(addOrganization);
		 */
		LdapOrgBean delBean = new LdapOrgBean();
		//LdapManager.LDAP_TEST_PREFIX_tt;
		String[] ouCodes=new String[]{
				"1211191003",
				"1211191002",
				"1211191001"
		};
		for(String ouCode:ouCodes){
			delBean.setObjectClass(LdapOrgManager.LDAP_ORG_UNIT);
			delBean.setDestinationIndicator(ouCode);
			boolean deleteEntity = lm.deleteEntity(delBean);// orgs
			System.out.println(deleteEntity);
		}
		
		String[] oCodes=new String[]{
				"1211191000",
				"1211190300",
				"1211190200",
				"1211190100",
				"1211000001",
				"1211000000",
				"1200000000"
		};
		for(String oCode:oCodes){
			delBean.setObjectClass(LdapOrgManager.LDAP_ORG);
			delBean.setDestinationIndicator(oCode);
			boolean deleteEntity = lm.deleteEntity(delBean);// orgs
			System.out.println("deleteEntity---Org:"+deleteEntity);
		}
		
	}
	
}
