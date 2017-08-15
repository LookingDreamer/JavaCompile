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
import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.system.entity.LdapAgentBean;
import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPAttributeSet;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPModification;
import com.novell.ldap.LDAPSearchResults;
import com.zzb.mobile.util.ConfigUtil;

public class LdapAgentManager {
	private static final String LDAP_URL = ConfigUtil.get("ldap.host");// 连接地址
	private static final int LDAP_PORT = Integer.parseInt(ConfigUtil
			.get("ldap.port"));// 端口
	private static final String MANAGER_USR = ConfigUtil.get("ldap.loginDN");// 用户名
	private static final String MANAGER_PASSWD = ConfigUtil
			.get("ldap.loginPasswd");// 密码
	public static final String baseDN = ConfigUtil.get("ldap.baseDN");

	public static final String LDAP_AGENT = "inetOrgPerson";// 机构

	@SuppressWarnings("unchecked")
	public LdapAgentBean searchAgent(LdapAgentBean bean) throws Exception {
		if (bean == null || StringUtil.isEmpty(bean.getObjectClass())
				|| StringUtil.isEmpty(bean.getEmployeeNumber())
				|| StringUtil.isEmpty(bean.getMgmtDivision()))
			return null;
		LdapAgentBean model = null;
		LDAPConnection lc = getConnection();
		try {
			LDAPSearchResults searchResults = lc
					.search(baseDN,
							LDAPConnection.SCOPE_SUB,
							"(&(objectClass=" + bean.getObjectClass()
									+ ")(employeeNumber="
									+ bean.getEmployeeNumber()
									+ ")(agentGroup=" + bean.getAgentGroup()
									+ ")(mgmtDivision="
									+ bean.getMgmtDivision() + "))", null,
							false);

			if (searchResults == null || !searchResults.hasMore())
				return null;
			if (searchResults.hasMore()) {
				model = new LdapAgentBean();
				LDAPEntry nextEntry = (LDAPEntry) searchResults.next();
				LDAPAttributeSet attributeSet = nextEntry.getAttributeSet();

				BeanInfo beanInfo = Introspector
						.getBeanInfo(LdapAgentBean.class);
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
				lc.connect(LDAP_URL, LDAP_PORT);
				lc.bind(LDAPConnection.LDAP_V3, MANAGER_USR,
						MANAGER_PASSWD.getBytes("UTF8"));
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
	public LdapAgentBean addAgent(LdapAgentBean bean) throws Exception {
		LDAPConnection lc = getConnection();
		try {
			LDAPAttributeSet attrs = new LDAPAttributeSet();
			BeanInfo beanInfo = Introspector.getBeanInfo(LdapAgentBean.class);
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
				bean.setDN("uid=" + bean.getUid() + "," + baseDN);
			} else {
				bean.setDN("uid=" + bean.getUid() + "," + bean.getParentDN()
						+ "," + baseDN);
			}
			System.out.println("ldap add bean:" + bean);
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
	public boolean deleteEntity(LdapAgentBean bean) throws Exception {
		LDAPConnection lc = getConnection();
		try {
			LdapAgentBean model = this.searchAgent(bean);
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

	public boolean modifyAgent(LdapAgentBean bean) throws Exception {
		LDAPConnection lc = getConnection();
		try {
			List<LDAPModification> list = new ArrayList<LDAPModification>();
			LDAPAttribute attribute = null;
			BeanInfo beanInfo = Introspector.getBeanInfo(LdapAgentBean.class);
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
					if (propDesc.getName().equals("objectClass"))
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
			System.out.println("ldap modify bean:" + bean);
			LDAPModification[] mods = new LDAPModification[list.size()];
			mods = (LDAPModification[]) list.toArray(mods);
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
		LdapAgentBean bean = new LdapAgentBean();
		bean.setSn("张");// 姓
		bean.setGivenName("三");// 名
		bean.setDisplayName("张三");// 姓名
	//	bean.setUserPassword(LdapMd5.Md5Encode("123456"));// 密码 
		bean.setSuccessFlag("false");
		bean.setParentDN("o=organizations");// parentdn
		bean.setUid(UUIDUtils.random());//
		bean.setMobile("13500000000");// 手机号码
		bean.setMail("1@qq.com");// 邮箱
		bean.setInitials("1");// 性别
		bean.setBusinessCategory("01");// 证件类型
		bean.setRegisteredAddress("152630198909090909");// 证件号码
		bean.setDestinationIndicator("1");// 是否允许通过任何移动设备登录
		// bean.setLabeleduri();// 绑定设备
		bean.setTitle("PropertyIns");// 用户权限
		bean.setObjectClass(LDAP_AGENT);
		bean.setEmployeeNumber("620034338");
		// bean.setCn("620034338");
		// bean.setL;// 禁用/启用移动登录
		// bean.setSt;// 禁用/启用网站登录
		// bean.setDescription;// 最近登录时间
		bean.setEmployeeType("1");// VIP等级

		LdapAgentManager manager = new LdapAgentManager();
		// manager.addAgent(bean);
		String[] employeeNumbers = new String[] { "620032821", "620032779",
				"620032792", "921400164", "620035247", "620032810",
				"620036410", "620034349", "620034338", "620032785" };

		for (String employeeNumber : employeeNumbers) {
			bean.setEmployeeNumber(employeeNumber);
			LdapAgentBean searchAgent = manager.searchAgent(bean);
			if (searchAgent != null) {
				// searchAgent.setMobile("139900000000");// 手机号码
				// searchAgent.setCn("13900000000");// 手机号码
				// searchAgent.setDisplayName("张三");// 姓名
				// manager.modifyAgent(searchAgent);

				boolean deleteEntity = manager.deleteEntity(bean);
				System.out.println("deleteEntity---Agent:" + deleteEntity);
			}
		}

	}

}
