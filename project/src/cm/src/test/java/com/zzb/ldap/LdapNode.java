package com.zzb.ldap;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPSearchConstraints;
import com.novell.ldap.LDAPSearchResults;

public class LdapNode {
	static String ldapHost = "203.195.141.57";
	static String loginDN = "cn=root,dc=baoxian,dc=com";
	static String password = "111";
	static String searchBase = "dc=baoxian,dc=com";
	static String searchFilter_jg = "physicalDeliveryOfficeName=*";// 机构获取
	static String searchFilter_dlr = "objectClass=inetOrgPerson";// 代理人
	static String searchFilter_yg = "objectClass=pilotPerson";// 业管
	static String searchFilter_js = "objectClass=organizationalRole";// 角色
	static String searchFilter_group = "objectClass=group";// 角色
	public static void writer(String s,String filename){
		try {
//			File file = new File("D:/pilotPerson.txt");//业管
			File file = new File("D:/"+filename+".txt");//角色
			FileWriter w = new FileWriter(file,true);
			w.write(s+"\r");
			w.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static List<Map<String,String>> getLdapJg() {
		int ldapPort = LDAPConnection.DEFAULT_PORT;
		int searchScope = LDAPConnection.SCOPE_SUB;
		LDAPConnection lc = new LDAPConnection();
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		try {
			lc.connect(ldapHost, ldapPort);
			lc.bind(LDAPConnection.LDAP_V3, loginDN, password.getBytes("UTF8"));
			LDAPSearchConstraints cons = lc.getSearchConstraints();
			cons.setBatchSize(0);
			cons.setMaxResults(5);
			LDAPSearchResults searchResults = lc.search(searchBase,searchScope, searchFilter_jg, null, false);
			while (searchResults.hasMore()) {
				HashMap<String, String> entryMap = new HashMap<String, String>();
				LDAPEntry nextEntry = null;
				try {
					nextEntry = searchResults.next();
				} catch (LDAPException e) {
					System.out.println("Error: " + e.toString());
					if (e.getResultCode() == LDAPException.LDAP_TIMEOUT
							|| e.getResultCode() == LDAPException.CONNECT_ERROR) {
						break;
					} else {
						continue;
					}
				}
				/**
				 * DN =: o=8B9359508963488C88156F4406D4928A,o=B27606FBE3284A2CAF0B4CEBD0E0F03F,o=7EF8B43D23BE4E6CBEA9A5282AD98EF9,o=C203F2B43C67411E99E4E24A04FD410C,o=organizations,dc=baoxian,dc=com
					st = 410000  --省份ID
					description = 保网河南驻马店分公司 --机构名称
					destinationIndicator = 1291411049   --机构编码  及 核心机构编码
					businessCategory = 998
					physicalDeliveryOfficeName = 保网河南驻马店分公司
					l = 411700 --城市ID
					objectClass = organization
					o = 8B9359508963488C88156F4406D4928A
					telexNumber = 3 --传真
				 */
				entryMap.put("DN", nextEntry.getDN());
				entryMap.put("description",nextEntry.getAttribute("description").getStringValue()); 
				entryMap.put("destinationIndicator",nextEntry.getAttribute("destinationIndicator")==null?"":nextEntry.getAttribute("destinationIndicator").getStringValue());
				entryMap.put("businessCategory",nextEntry.getAttribute("businessCategory")==null?"":nextEntry.getAttribute("businessCategory").getStringValue());
				entryMap.put("physicalDeliveryOfficeName",nextEntry.getAttribute("physicalDeliveryOfficeName")==null?"":nextEntry.getAttribute("physicalDeliveryOfficeName").getStringValue());
				entryMap.put("st",nextEntry.getAttribute("st")==null?"":nextEntry.getAttribute("st").getStringValue());
				entryMap.put("l",nextEntry.getAttribute("l")==null?"":nextEntry.getAttribute("l").getStringValue());
				entryMap.put("telexNumber",nextEntry.getAttribute("telexNumber")==null?"":nextEntry.getAttribute("telexNumber").getStringValue());
				
				System.out.println(entryMap.get("DN"));
				list.add(entryMap);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 代理人
	 * @return
	 */
	public static List<Map<String,String>> getLdapDlr(String param) {
		int ldapPort = LDAPConnection.DEFAULT_PORT;
		int searchScope = LDAPConnection.SCOPE_SUB;
		LDAPConnection lc = new LDAPConnection();
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		LDAPSearchResults searchResults = null;
		try {
			lc.connect(ldapHost, ldapPort);
			lc.bind(LDAPConnection.LDAP_V3, loginDN, password.getBytes("UTF8"));
			LDAPSearchConstraints cons = lc.getSearchConstraints();
			cons.setBatchSize(0);
			cons.setMaxResults(5);
			if(param==""){
				searchResults = lc.search(searchBase,searchScope, searchFilter_dlr, null, false);
			}else{
				String searchparam = "(&("+searchFilter_dlr+")("+param+"))";
				searchResults = lc.search(searchBase,searchScope, searchparam, null, false);
			}
			System.out.println(searchResults.getCount());
			while (searchResults.hasMore()) {
				HashMap<String, String> entryMap = new HashMap<String, String>();
				LDAPEntry nextEntry = null;
				try {
					nextEntry = searchResults.next();
				} catch (LDAPException e) {
					System.out.println("Error: " + e.toString());
					if (e.getResultCode() == LDAPException.LDAP_TIMEOUT
							|| e.getResultCode() == LDAPException.CONNECT_ERROR) {
						break;
					} else {
						continue;
					}
				}
				/**
				 * DN =: uid=6D507D72E6884563981365353C7D8D62,ou=3A4790E99643430187CA3DC7E066CA56,o=477DFB6511EE4883977E14D65315EADE,o=67A070A34945445697223D1673A9D1D6,o=E107C5468E1B43028E4843BD15914067,o=C203F2B43C67411E99E4E24A04FD410C,o=organizations,dc=baoxian,dc=com
					displayName = 江晓文                              --名称
					initials = 2			  --vip 等级
					cn = 120100008			  --代理人账号 
					userPassword = {MD5}4QrcOUm6Wau+VuBX8g+IPg==  --密码
					employeeNumber = 120100008  --工号
					uid = 6D507D72E6884563981365353C7D8D62
					registeredAddress = 350105196501080040  --身份证号
					description = 2014-07-19 14:35:10.249
					destinationIndicator = 0
					businessCategory = 01
					title = PropertyIns
					sn = 江
					objectClass = inetOrgPerson
					givenName = 晓文
					mobile = 13705008364  --电话 最多2个
					labeledURI = serialNumber=F85LGAA1F19M,o=E107C5468E1B43028E4843BD15914067,o=C203F2B43C67411E99E4E24A04FD410C,o=organizations,dc=baoxian,dc=com  --代理人设备（可多个）
				 */
				System.out.println(nextEntry.getDN());
//				entryMap.put("DN", nextEntry.getDN());
//				entryMap.put("id", nextEntry.getAttribute("uid").getStringValue());
//				entryMap.put("displayName",nextEntry.getAttribute("displayName").getStringValue());//名称
//				entryMap.put("mobile",nextEntry.getAttribute("mobile")==null?"":nextEntry.getAttribute("mobile").getStringValue());//电话
//				entryMap.put("userPassword",nextEntry.getAttribute("userPassword").getStringValue());//密码
//				entryMap.put("employeeNumber",nextEntry.getAttribute("employeeNumber").getStringValue());//员工编号
//				entryMap.put("labeledURI",nextEntry.getAttribute("labeledURI")==null?"":nextEntry.getAttribute("labeledURI").getStringValue());
//				entryMap.put("description",nextEntry.getAttribute("description")==null?"":nextEntry.getAttribute("description").getStringValue());//创建时间
//				list.add(entryMap);
				entryMap.put("noti", nextEntry.getDN());
				//名称
				entryMap.put("name",nextEntry.getAttribute("displayName")==null?"":nextEntry.getAttribute("displayName").getStringValue());
				//性别 
				entryMap.put("sex",nextEntry.getAttribute("initials")==null?"":nextEntry.getAttribute("initials").getStringValue());
				//工号
				entryMap.put("jobnum",nextEntry.getAttribute("employeeNumber")==null?"":nextEntry.getAttribute("employeeNumber").getStringValue());
				//账号
				entryMap.put("agentcode",nextEntry.getAttribute("cn")==null?"":nextEntry.getAttribute("cn").getStringValue());
				//密码
				entryMap.put("pwd",nextEntry.getAttribute("userPassword")==null?"":nextEntry.getAttribute("userPassword").getStringValue());
				//证件号码
				entryMap.put("idno",nextEntry.getAttribute("registeredAddress")==null?"":nextEntry.getAttribute("registeredAddress").getStringValue());
				//最后登录时间
				entryMap.put("lsatlogintime",nextEntry.getAttribute("destinationIndicator")==null?"":nextEntry.getAttribute("destinationIndicator").getStringValue());
				//证件类型
				entryMap.put("idnotype",nextEntry.getAttribute("destinationIndicator")==null?"":nextEntry.getAttribute("destinationIndicator").getStringValue());
				//代理人类型
//				entryMap.put("agentkind",nextEntry.getAttribute("objectClass")==null?"":nextEntry.getAttribute("objectClass").getStringValue());
				//代理人等级
				entryMap.put("agentlevel",nextEntry.getAttribute("employeetype")==null?"":nextEntry.getAttribute("employeetype").getStringValue());
				//电话
				entryMap.put("mobile",nextEntry.getAttribute("mobile")==null?"":nextEntry.getAttribute("mobile").getStringValue());//电话
//	TODO			entryMap.put("labeledURI",nextEntry.getAttribute("labeledURI")==null?"":nextEntry.getAttribute("labeledURI").getStringValue());//手机型号
				list.add(entryMap);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 业管
	 * @return
	 */
	public static List<Map<String,Object>> getLdapYg() {
		int ldapPort = LDAPConnection.DEFAULT_PORT;
		int searchScope = LDAPConnection.SCOPE_SUB;
		LDAPConnection lc = new LDAPConnection();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		try {
			lc.connect(ldapHost, ldapPort);
			lc.bind(LDAPConnection.LDAP_V3, loginDN, password.getBytes("UTF8"));
			LDAPSearchConstraints cons = lc.getSearchConstraints();
			cons.setBatchSize(0);
			cons.setMaxResults(5);
			LDAPSearchResults searchResults = lc.search(searchBase,
					searchScope, searchFilter_yg, null, false);
			System.out.println(searchResults.getCount());
			while (searchResults.hasMore()) {
				HashMap<String, Object> entryMap = new HashMap<String, Object>();
				LDAPEntry nextEntry = null;
				try {
					nextEntry = searchResults.next();
				} catch (LDAPException e) {
					System.out.println("Error: " + e.toString());
					if (e.getResultCode() == LDAPException.LDAP_TIMEOUT
							|| e.getResultCode() == LDAPException.CONNECT_ERROR) {
						break;
					} else {
						continue;
					}
				}
				/**
				 * DN =: cn=wengsc,o=0C664EF7B39A47D997F985AB649E700F,o=C203F2B43C67411E99E4E24A04FD410C,o=organizations,dc=baoxian,dc=com
					userClass = ROLE_AMD_B  --角色	业管账号管理员
					userClass = ROLE_AMD_A    		代理人及设备管理员 
					userClass = ROLE_ATM_T			设备管理员
					userClass = ROLE_ATM_N			公告管理员
					userClass = ROLE_ATM_R			公告管理员
					userClass = ROLE_BOP_CARINS		车险业务操作员
					userClass = ROLE_CIP			车险供应商管理员
					userClass = ROLE_CALLCENTER		呼叫中心系统角色
					userClass = ROLE_PRODUCT_A		非车险保网产品管理员
					userClass = ROLE_PRODUCT_B 		非车险机构产品管理员
					userClass = ROLE_JFSC_M
					userClass = ROLE_ATM_V
					description = 2015-01-27 09:58:59.717
					businessCategory = group201310304090529     --所属业管群
					businessCategory = group20140258042548
					businessCategory = group20140258042743
					businessCategory = group20140363105652
					businessCategory = group20140380041328
					businessCategory = group201406163111340
					cn = wengsc									--账号		
					sn = 瓮士超									--名称
					drink = 2012-11-03 11:41:42.759				--创建时间
					objectClass = person
					objectClass = pilotPerson
					objectClass = top
					userPassword = wengsc1234					--密码
				 */
//				System.out.println(nextEntry.getAttribute("drink"));
				entryMap.put("noti", nextEntry.getDN());
				entryMap.put("userclass",nextEntry.getAttribute("userClass")==null?"":nextEntry.getAttribute("userClass").getStringValue());
				entryMap.put("objectclass",nextEntry.getAttribute("objectClass")==null?"":nextEntry.getAttribute("objectClass").getStringValue());
				entryMap.put("groupid",nextEntry.getAttribute("businessCategory")==null?"":nextEntry.getAttribute("businessCategory"));
				entryMap.put("usercode",nextEntry.getAttribute("cn")==null?"":nextEntry.getAttribute("cn").getStringValue());
				entryMap.put("name",nextEntry.getAttribute("sn")==null?"":nextEntry.getAttribute("sn").getStringValue());
				entryMap.put("createtime",nextEntry.getAttribute("drink")==null?"":nextEntry.getAttribute("drink").getStringValue());
				entryMap.put("password",nextEntry.getAttribute("userPassword")==null?"":nextEntry.getAttribute("userPassword").getStringValue());
				list.add(entryMap);
				System.out.println("======="+entryMap);
			}
			
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 角色
	 * @return
	 */
	public static List<Map<String,String>> getLdapRole() {
		int ldapPort = LDAPConnection.DEFAULT_PORT;
		int searchScope = LDAPConnection.SCOPE_SUB;
		LDAPConnection lc = new LDAPConnection();
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		try {
			lc.connect(ldapHost, ldapPort);
			lc.bind(LDAPConnection.LDAP_V3, loginDN, password.getBytes("UTF8"));
			LDAPSearchConstraints cons = lc.getSearchConstraints();
			cons.setBatchSize(0);
			cons.setMaxResults(5);
			LDAPSearchResults searchResults = lc.search(searchBase,
					searchScope, searchFilter_js, null, false);
			System.out.println(searchResults.getCount());
			while (searchResults.hasMore()) {
				HashMap<String, String> entryMap = new HashMap<String, String>();
				LDAPEntry nextEntry = null;
				try {
					nextEntry = searchResults.next();
				} catch (LDAPException e) {
					System.out.println("Error: " + e.toString());
					if (e.getResultCode() == LDAPException.LDAP_TIMEOUT
							|| e.getResultCode() == LDAPException.CONNECT_ERROR) {
						break;
					} else {
						continue;
					}
				}
				/**
				 * DN =: cn=A_CD12342,ou=role,o=user,dc=baoxian,dc=com
					description = 登录保网管理系统  --角色名称
					cn = A_CD12342
					l = 保网产品管理员
					objectClass = top
					objectClass = organizationalRole
				 */
				System.out.println(nextEntry.getDN());
				entryMap.put("DN", nextEntry.getDN());
				entryMap.put("description",nextEntry.getAttribute("description")==null?"":nextEntry.getAttribute("description").getStringValue());//创建时间
				list.add(entryMap);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 群组
	 * @param args
	 */
	public static List<Map<String,Object>> getLdapGroup(){
		int ldapPort = LDAPConnection.DEFAULT_PORT;
		int searchScope = LDAPConnection.SCOPE_SUB;
		LDAPConnection lc = new LDAPConnection();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		try {
			lc.connect(ldapHost, ldapPort);
			lc.bind(LDAPConnection.LDAP_V3, loginDN, password.getBytes("UTF8"));
			LDAPSearchConstraints cons = lc.getSearchConstraints();
			cons.setBatchSize(0);
			cons.setMaxResults(5);
			LDAPSearchResults searchResults = lc.search(searchBase,
					searchScope, searchFilter_group, null, false);
			System.out.println(searchResults.getCount());
			while (searchResults.hasMore()) {
				HashMap<String, Object> entryMap = new HashMap<String, Object>();
				LDAPEntry nextEntry = null;
				try {
					nextEntry = searchResults.next();
				} catch (LDAPException e) {
					System.out.println("Error: " + e.toString());
					if (e.getResultCode() == LDAPException.LDAP_TIMEOUT
							|| e.getResultCode() == LDAPException.CONNECT_ERROR) {
						break;
					} else {
						continue;
					}
				}
				/**
				 * DN =: cn=group201307193030354,o=groups,dc=baoxian,dc=com
						description = 演练集团管理组 --名称
						businessCategory = 管理组  --类型
						cn = group201307193030354
						objectClass = groupOfNames 
						member =  		 --群组参与人
						member = cn=testcm1,ou=356B14691A00436C84FA5AAB2B621D62,o=B2FB7C4CACA145CE81E672CE3CEE335F,o=39B0875324BC4B2896B88BF87C15DE69,o=B1BA99404E8A45F78A1062B3859DF560,o=DC66BD8BA06F47F49176B8C5930645F7,o=organizations,dc=baoxian,dc=com
						member = cn=fengjl,o=C203F2B43C67411E99E4E24A04FD410C,o=organizations,dc=baoxian,dc=com
						member = cn=008001,o=F86B9521F73F4311B778731FD7B5F2D9,o=organizations,dc=baoxian,dc=com
						member = cn=qusumei,o=F86B9521F73F4311B778731FD7B5F2D9,o=organizations,dc=baoxian,dc=com
				 */
				System.out.println(nextEntry.getDN());
				entryMap.put("DN", nextEntry.getDN());
				entryMap.put("description",nextEntry.getAttribute("description")==null?"":nextEntry.getAttribute("description").getStringValue());//创建时间
				entryMap.put("objectClass",nextEntry.getAttribute("objectClass")==null?"":nextEntry.getAttribute("objectClass").getStringValue());//创建时间
				entryMap.put("businessCategory",nextEntry.getAttribute("businessCategory")==null?"":nextEntry.getAttribute("businessCategory").getStringValue());//类型
				entryMap.put("member",nextEntry.getAttribute("member")==null?"":nextEntry.getAttribute("member").getStringValueArray());//群组参与人
				System.out.println(entryMap);
				list.add(entryMap);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static void main(String[] args) {
//		getLdapJg();
//		getLdapDlr();
		getLdapYg();
//		getLdapGroup();
	}
}
