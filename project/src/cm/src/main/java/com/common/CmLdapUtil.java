package com.common;

import java.io.UnsupportedEncodingException;

import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPAttributeSet;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPSearchConstraints;
import com.novell.ldap.LDAPSearchResults;
import com.zzb.mobile.util.ConfigUtil;

public class CmLdapUtil {
	private String LDAP_URL = ConfigUtil.get("ldap.host");// 连接地址
	private int LDAP_PORT = Integer.parseInt(ConfigUtil.get("ldap.port"));// 端口。
	private String MANAGER_USR = ConfigUtil.get("ldap.loginDN");// 用户名
	private String MANAGER_PASSWD = ConfigUtil.get("ldap.loginPasswd");// 密码
	private String baseDN = ConfigUtil.get("ldap.baseDN");

	public LDAPConnection ldapConnection = new LDAPConnection();;
	private static final CmLdapUtil instance = new CmLdapUtil();

	private CmLdapUtil() {
		System.out.println(LDAP_URL);
		try {
			ldapConnection.connect(LDAP_URL, LDAP_PORT);
			ldapConnection.bind(LDAPConnection.LDAP_V3, MANAGER_USR, MANAGER_PASSWD.getBytes("UTF8"));
   			LDAPSearchConstraints cons = ldapConnection.getSearchConstraints();           
   			cons.setBatchSize(0);           
   			cons.setMaxResults(5);
		} catch (LDAPException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	public static  CmLdapUtil getInstance() {
		return instance;
	}

	/**
	 * @param mobile
	 *            电话号
	 * @param papersType
	 *            证件类型
	 * @param papersNum
	 *            证件号
	 * @return 工号
	 */
	public String getJobNumByParam(String mobile, String papersType,
			String papersNum) {
		String employeeNumberStr = null;
		try {
			LDAPSearchResults searchResults = ldapConnection
					.search(baseDN,
							LDAPConnection.SCOPE_SUB,
							"(|(&(objectClass=inetOrgPerson)(businessCategory="
									+ papersType
									+ "))(&(objectClass=inetOrgPerson)(mobile="
									+ mobile
									+ "))(&(objectClass=inetOrgPerson)(registeredAddress="
									+ papersNum + ")))", null, false);
			if (searchResults == null || !searchResults.hasMore()) {
				return null;
			} else {
				LDAPEntry nextEntry = (LDAPEntry) searchResults.next();
				LDAPAttributeSet attributeSet = nextEntry.getAttributeSet();
				LDAPAttribute employeeNumber = attributeSet
						.getAttribute("employeeNumber");
				employeeNumberStr = employeeNumber.getStringValue();
			}
		} catch (LDAPException e) {
			e.printStackTrace();
		}
		return employeeNumberStr;
	}

	public static void main(String[] args) {
		CmLdapUtil aa = CmLdapUtil.getInstance();
		System.out.println(aa);
		String str = aa.getJobNumByParam("13800138002", "01", "test");
		System.out.println(str);

	}

}