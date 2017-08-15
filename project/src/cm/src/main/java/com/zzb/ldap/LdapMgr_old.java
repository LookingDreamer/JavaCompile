package com.zzb.ldap;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPAttributeSet;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPModification;
import com.novell.ldap.LDAPSearchConstraints;
import com.novell.ldap.LDAPSearchResults;
import com.zzb.mobile.model.LdapAgentModel;
import com.zzb.mobile.model.LdapBusmanagerModel;
import com.zzb.mobile.util.ConfigUtil;

public class LdapMgr_old {
	/**
	 * 查询用户
	 * 
	 * @param account
	 * @return
	 */
	private String LDAP_URL = ConfigUtil.get("ldap.host");// 连接地址
	private int LDAP_PORT = Integer.parseInt(ConfigUtil.get("ldap.port"));// 端口。
	private String MANAGER_USR = ConfigUtil.get("ldap.loginDN");// 用户名
	private String MANAGER_PASSWD = ConfigUtil.get("ldap.loginPasswd");// 密码
	public String baseDN = ConfigUtil.get("ldap.baseDN");

	public LdapAgentModel searchAgent(String account) {
		LdapAgentModel model = null;
		LDAPConnection lc = new LDAPConnection();
		try {
			lc.connect(LDAP_URL, LDAP_PORT);
			lc.bind(LDAPConnection.LDAP_V3, MANAGER_USR,
					MANAGER_PASSWD.getBytes("UTF8"));
			LDAPSearchConstraints cons = lc.getSearchConstraints();
			cons.setBatchSize(0);
			cons.setMaxResults(5);
			cons.setTimeLimit(20);//20s 返回
			LDAPSearchResults searchResults = lc.search(baseDN,
					LDAPConnection.SCOPE_SUB,
					"(&(objectClass=inetOrgPerson)(cn="+ account + "))",
					null, false);
			// if(!searchResults.hasMore()){
			// //用手机号登陆
			// String
			// filter="(&(objectClass=inetOrgPerson)(mobile="+account+"))";
			// searchResults = lc.search(baseDN,LDAPConnection.SCOPE_SUB,
			// filter,null, false);
			// if(!searchResults.hasMore()){
			// searchResults = lc.search(baseDN,LDAPConnection.SCOPE_SUB,
			// filter,null, false);
			// if(!searchResults.hasMore()){
			// searchResults = lc.search(baseDN,LDAPConnection.SCOPE_SUB,
			// filter,null, false);
			// }
			// }
			// }
			if (searchResults == null || !searchResults.hasMore())
				return null;

			if (searchResults.hasMore()) {
				model = new LdapAgentModel();
				LDAPEntry nextEntry = (LDAPEntry) searchResults.next();
				LDAPAttributeSet attributeSet = nextEntry.getAttributeSet();

				BeanInfo beanInfo = Introspector
						.getBeanInfo(LdapAgentModel.class);
				PropertyDescriptor[] proDescrtptors = beanInfo
						.getPropertyDescriptors();
				StringBuffer sb = new StringBuffer();
				String value = null;
				if (proDescrtptors != null && proDescrtptors.length > 0) {
					for (PropertyDescriptor propDesc : proDescrtptors) {
						sb = new StringBuffer();
						if (propDesc.getName().equals("class"))
							continue;
						if (propDesc.getName().equals("DN")) {
							model.setDN(nextEntry.getDN());
							continue;
						}
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
			// 增加密码验证 先执行LdapMd5.Md5Encode,然后跟表中的数据做比较

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (lc != null)
				try {
					lc.disconnect();
				} catch (LDAPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		}
		return model;
	}

	/**
	 * 向Ldap中添加用户
	 * 
	 * @param model
	 * @return
	 */
	public LdapAgentModel addAgent(LdapAgentModel model) throws Exception {
		// 密码加密开始

		// 密码加密结束
		LDAPConnection lc = new LDAPConnection();
		try {
			lc.connect(LDAP_URL, LDAP_PORT);
			lc.bind(LDAPConnection.LDAP_V3, MANAGER_USR,
					MANAGER_PASSWD.getBytes("UTF8"));
			LDAPSearchConstraints cons = lc.getSearchConstraints();
			cons.setBatchSize(0);
			cons.setMaxResults(5);
			cons.setTimeLimit(20);//20s 返回
			LDAPAttributeSet attrs = new LDAPAttributeSet();
			LDAPAttribute objclassSet = new LDAPAttribute("objectClass");
			objclassSet.addValue("inetOrgPerson");
			attrs.add(objclassSet);
			BeanInfo beanInfo = Introspector.getBeanInfo(LdapAgentModel.class);
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
					Method get = propDesc.getReadMethod();
					obj = (String) get.invoke(model);

					attr = new LDAPAttribute(propDesc.getName());
					if (obj != null && obj.indexOf("|") > 0) {
						for (String temp : obj.split("|")) {
							attr.addValue(temp);
						}
					} else {
						if (obj != null) {
							attr.addValue(obj);
						}
					}
					attrs.add(attr);
				}
			}
			if (model.getParentDN().contains(baseDN)) {
				model.setDN("uid=" + model.getCn() + "," + model.getParentDN());
			} else {
				model.setDN("uid=" + model.getCn() + "," + model.getParentDN()
						+ "," + baseDN);
			}
			LDAPEntry newEntry = new LDAPEntry(model.getDN(), attrs);
			lc.add(newEntry);
			return model;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (lc != null)
				lc.disconnect();
		}
	}

	/**
	 * 向Ldap中添加业管
	 * 
	 * @param model
	 * @return
	 */
	public LdapBusmanagerModel addBusmanager(LdapBusmanagerModel model)
			throws Exception {
		// 密码加密开始

		// 密码加密结束
		LDAPConnection lc = new LDAPConnection();
		try {
			lc.connect(LDAP_URL, LDAP_PORT);
			lc.bind(LDAPConnection.LDAP_V3, MANAGER_USR,
					MANAGER_PASSWD.getBytes("UTF8"));
			LDAPSearchConstraints cons = lc.getSearchConstraints();
			cons.setBatchSize(0);
			cons.setMaxResults(5);
			cons.setTimeLimit(20);//20s 返回
			LDAPAttributeSet attrs = new LDAPAttributeSet();
			LDAPAttribute objclassSet = new LDAPAttribute("objectClass");
			objclassSet.addValue("pilotPerson");
			objclassSet.addValue("person");
			objclassSet.addValue("top");
			attrs.add(objclassSet);
			BeanInfo beanInfo = Introspector
					.getBeanInfo(LdapBusmanagerModel.class);
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
					Method get = propDesc.getReadMethod();
					obj = (String) get.invoke(model);

					attr = new LDAPAttribute(propDesc.getName());
					if (obj != null && obj.indexOf("|") > 0) {
						for (String temp : obj.split("|")) {
							attr.addValue(temp);
						}
					} else {
						if (obj != null) {
							attr.addValue(obj);
						}
					}
					attrs.add(attr);
				}
			}
			String dn = "";
			if (model.getParentDN().contains(baseDN)) {
				dn = "cn=" + model.getCn() + "," + model.getParentDN();
				model.setDN(dn);
			} else {
				dn = "cn=" + model.getCn() + "," + model.getParentDN() + ","
						+ baseDN;
				model.setDN(dn);
			}
			LDAPEntry newEntry = new LDAPEntry(model.getDN(), attrs);
			lc.add(newEntry);
			return model;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (lc != null)
				lc.disconnect();
		}
	}

	/**
	 * 查询业管
	 * 
	 * @param account
	 * @return
	 */
	public LdapBusmanagerModel searchUser(String account) {
		// LdapAgentModel model2 =null;
		LdapBusmanagerModel model = null;
		LDAPConnection lc = new LDAPConnection();
		try {
			lc.connect(LDAP_URL, LDAP_PORT);
			lc.bind(LDAPConnection.LDAP_V3, MANAGER_USR,
					MANAGER_PASSWD.getBytes("UTF8"));
			LDAPSearchConstraints cons = lc.getSearchConstraints();
			cons.setBatchSize(0);
			cons.setMaxResults(5);
			cons.setTimeLimit(20);//20s 返回
			LDAPSearchResults searchResults = lc.search(baseDN,
					LDAPConnection.SCOPE_SUB,
					"(&(objectClass=pilotPerson)(cn=" + account+ "))", null, false);
			if (searchResults == null || !searchResults.hasMore())
				return null;

			if (searchResults.hasMore()) {
				model = new LdapBusmanagerModel();
				LDAPEntry nextEntry = (LDAPEntry) searchResults.next();
				LDAPAttributeSet attributeSet = nextEntry.getAttributeSet();

				BeanInfo beanInfo = Introspector
						.getBeanInfo(LdapBusmanagerModel.class);
				PropertyDescriptor[] proDescrtptors = beanInfo
						.getPropertyDescriptors();
				StringBuffer sb = new StringBuffer();
				String value = null;
				if (proDescrtptors != null && proDescrtptors.length > 0) {
					for (PropertyDescriptor propDesc : proDescrtptors) {
						sb = new StringBuffer();
						if (propDesc.getName().equals("class"))
							continue;
						if (propDesc.getName().equals("DN")) {
							model.setDN(nextEntry.getDN());
							continue;
						}
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
			// 增加密码验证 先执行LdapMd5.Md5Encode,然后跟表中的数据做比较

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (lc != null)
				try {
					lc.disconnect();
				} catch (LDAPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		}
		return model;
	}

	/**
	 * 修改密码
	 * 
	 * @param jobNumber
	 * @param password
	 * @return
	 */
	public boolean modifyPassWord(String jobNumber, String password) {
		// password加密

		// password加密
		return modifyAttrbute(jobNumber, "userPassword", password);
	}

	/**
	 * 修改业管密码
	 * 
	 * @param usercode
	 * @param password
	 * @return
	 */
	public boolean updatePassWord(String usercode, String password) {
		return updatepwd(usercode, "userPassword", password);
	}

	/**
	 * 用于试用用户转为正式用户
	 * 
	 * @param jobNumber
	 * @param moblie
	 * @return
	 */
	public boolean modifyJobNumberAndMobile(String tempjobNum, String jobNum,
			String moblie) {
		if (modifyAttrbute(tempjobNum, "employeeNumber", jobNum)) {
			return modifyAttrbute(tempjobNum, "mobile", moblie);
		}
		return false;
	}

	private boolean modifyAttrbute(String jobNumber, String attr, String value) {
		LDAPConnection lc = new LDAPConnection();
		try {
			lc.connect(LDAP_URL, LDAP_PORT);
			lc.bind(LDAPConnection.LDAP_V3, MANAGER_USR,
					MANAGER_PASSWD.getBytes("UTF8"));
			LDAPSearchConstraints cons = lc.getSearchConstraints();
			cons.setBatchSize(0);
			cons.setMaxResults(5);
			cons.setTimeLimit(20);//20s 返回
			List<LDAPModification> list = new ArrayList<LDAPModification>();
			LdapAgentModel model = this.searchAgent(jobNumber);
			if (model == null)
				return false;
			LDAPAttribute attribute = new LDAPAttribute(attr, value);
			list.add(new LDAPModification(LDAPModification.REPLACE, attribute));
			LDAPModification[] mods = new LDAPModification[list.size()];
			mods = (LDAPModification[]) list.toArray(mods);
			lc.modify(model.getDN(), mods);
			return true;
		} catch (Exception ne) {
			ne.printStackTrace();
			System.err.println("Error: " + ne.getMessage());
		} finally {
			if (lc != null)
				try {
					lc.disconnect();
				} catch (LDAPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return false;
	}

	private boolean updatepwd(String usercode, String attr, String value) {
		LDAPConnection lc = new LDAPConnection();
		try {
			lc.connect(LDAP_URL, LDAP_PORT);
			lc.bind(LDAPConnection.LDAP_V3, MANAGER_USR,
					MANAGER_PASSWD.getBytes("UTF8"));
			LDAPSearchConstraints cons = lc.getSearchConstraints();
			cons.setBatchSize(0);
			cons.setMaxResults(5);
			cons.setTimeLimit(20);//20s 返回
			List<LDAPModification> list = new ArrayList<LDAPModification>();
			LdapBusmanagerModel model = this.searchUser(usercode);
			if (model == null)
				return false;
			LDAPAttribute attribute = new LDAPAttribute(attr, value);
			list.add(new LDAPModification(LDAPModification.REPLACE, attribute));
			LDAPModification[] mods = new LDAPModification[list.size()];
			mods = (LDAPModification[]) list.toArray(mods);
			lc.modify(model.getDN(), mods);
			return true;
		} catch (Exception ne) {
			ne.printStackTrace();
			System.err.println("Error: " + ne.getMessage());
		} finally {
			if (lc != null)
				try {
					lc.disconnect();
				} catch (LDAPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return false;
	}

	/**
	 * 
	 * @param jobNumber
	 * @param newMobile
	 * @return
	 */
	public boolean modifyMobile(String jobNumber, String newMobile) {
		return modifyAttrbute(jobNumber, "mobile", newMobile);
	}

	/**
	 * 
	 * @param jobNumber
	 * @return
	 */
	public boolean deleteEntity(String jobNumber) {
		LDAPConnection lc = new LDAPConnection();
		try {
			lc.connect(LDAP_URL, LDAP_PORT);
			lc.bind(LDAPConnection.LDAP_V3, MANAGER_USR,
					MANAGER_PASSWD.getBytes("UTF8"));
			LDAPSearchConstraints cons = lc.getSearchConstraints();
			cons.setBatchSize(0);
			cons.setMaxResults(5);
			cons.setTimeLimit(20);//20s 返回
			LdapAgentModel model = this.searchAgent(jobNumber);
			if (model != null)
				lc.delete(model.getDN());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception in delete():" + e);
		} finally {
			if (lc != null)
				try {
					lc.disconnect();
				} catch (LDAPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		}
		return false;
	}

	// 删除业管
	public boolean deleteUser(String usercode) {
		LDAPConnection lc = new LDAPConnection();
		try {
			lc.connect(LDAP_URL, LDAP_PORT);
			lc.bind(LDAPConnection.LDAP_V3, MANAGER_USR,
					MANAGER_PASSWD.getBytes("UTF8"));
			LDAPSearchConstraints cons = lc.getSearchConstraints();
			cons.setBatchSize(0);
			cons.setMaxResults(5);
			LdapBusmanagerModel model = this.searchUser(usercode);
			if (model != null)
				lc.delete(model.getDN());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception in delete():" + e);
		} finally {
			if (lc != null)
				try {
					lc.disconnect();
				} catch (LDAPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		}
		return false;
	}

	public static void main(String[] args) {
		//LdapMgr ldap = new LdapMgr();
		// LdapAgentModel model =new LdapAgentModel();
		// model.setBusinessCategory("01");
		//
		// model.setCn("80111111");
		// model.setDescription("绑定测试");
		// model.setMobile("13811111115");
		// model.setEmployeeNumber("80111111");
		// model.setUserPassword("asdfasdf");
		// model.setObjectClass("inetOrgPerson");
		// model.setInitials("1");
		// model.setDisplayName("绑定测试");
		// model.setGivenName("绑定测试");
		// model.setLabeledURI("aa");
		// model.setRegisteredAddress("111111111111111111");
		// model.setSn("绑定测试");
		// model.setTitle("绑定测试");
		// model.setUid("11231231231211131231");
		// model.setParentDN("o=95C07590F27B41F997C61B1A4DD2B5BE, o=3DCA853203FA4E55B6A93A7CBF4344EA, o=697DCEE58641449BB2627F24A58D90C4, o=C203F2B43C67411E99E4E24A04FD410C, o=organizations");
		// //ldap.modifyPassWord("aaa", "2323");
		//
		try {
			// ldap.addAgent(model);
			// System.out.println("12131");
			// for(int i=0;i<20;i++){
			// model = ldap.searchAgent("120101519");
			// System.out.println(model);
			//
			// //ldap.modifyMobile("80111111", "13000000000");
			// for(int i=0;i<100;i++){
			// LdapMgr ldap =new LdapMgr();
			// LdapAgentModel model =new LdapAgentModel();
			// ldap.modifyMobile("13800138002","13800138003");
			// model=ldap.searchAgent("13800138002");

			// LdapBusmanagerModel model = new LdapBusmanagerModel();
			// //生成临时工号
			// String tempJobNo = updateAgentTempJobNo() + "";
			// //插入到LdapAgentModel
			// model.setBusinessCategory("01");
			// model.setCn(tempJobNo);
			// model.setMobile(agent.getMobile());
			// model.setEmployeeNumber(tempJobNo);
			// model.setUserPassword(LdapMd5.Md5Encode(""));
			// model.setObjectClass("inetOrgPerson");
			// model.setDisplayName(agent.getName());
			// model.setGivenName(agent.getName());
			// model.setSn(agent.getName());
			// model.setUid(UUIDUtils.random());
			// INSCDept dept = inscDeptService.queryById(agent.getDeptid());
			// if(dept != null){
			// model.setParentDN(dept.getNoti());
			// }else{
			// model.setParentDN("");
			// }
			// model.setCn("80112333");//id
			// model.setSn("yeguan");//name
			// model.setUserPassword(LdapMd5.Md5Encode("123456"));
			// model.setDescription("2015-01-10");
			// model.setObjectClass("pilotPerson");
			// model.setObjectClass("person");
			// model.setParentDN("o=organizations");
			// ldap.addBusmanager(model);
			// ldap.searchBusmanager("80112");
			// System.out.println(model);
			// LdapAgentModel mo = ldap.searchAgent("55555");
			// System.out.println(mo.getEmployeeNumber()+"======");
			// LdapBusmanagerModel model = ldap.searchUser("huzhike");
			// System.out.println(model.getSn()+"======");
			// Boolean b = ldap.deleteUser("80112");
			// System.out.println(b+"==>>>>");
			// Boolean b =
//			 ldap.updatepwd("huangxingxing","userPassword",LdapMd5.Md5Encode("123456"));
			
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
