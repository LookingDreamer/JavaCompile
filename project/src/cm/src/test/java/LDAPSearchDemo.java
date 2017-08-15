import java.io.File;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Iterator;

import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPAttributeSet;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPSearchConstraints;
import com.novell.ldap.LDAPSearchResults;

/**
 * 查询条目示例 blog http://www.micmiu.com
 *
 * @author Michael
 *
 */
public class LDAPSearchDemo {

	public static void v(String s) {
		try {
//			File file = new File("D:/pilotPerson.txt");// 业管
			// File file = new File("D:/organizationalUnit.txt");//角色
			
			File file = new File("D:/pilotPerson.txt");// 业管
			FileWriter w = new FileWriter(file, true);
			w.write(s + "\r");
			w.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		String ldapHost = "203.195.141.57";
		String loginDN = "cn=root,dc=baoxian,dc=com";
		String password = "111";
		String searchBase = "o=837BE5BC18EB4E51B1682A9B3549B3A4,o=C203F2B43C67411E99E4E24A04FD410C,o=organizations,dc=baoxian,dc=com";
		// String searchFilter = "objectClass=groupOfNames";
		String searchFilter = "objectClass=inetOrgPerson";
		// String
		// searchFilter="(&(objectClass=inetOrgPerson)(employeeNumber=120101519))";
		int ldapPort = LDAPConnection.DEFAULT_PORT;
		// 查询范围
		// SCOPE_BASE、SCOPE_ONE、SCOPE_SUB、SCOPE_SUBORDINATESUBTREE
		int searchScope = LDAPConnection.SCOPE_SUB;
		LDAPConnection lc = new LDAPConnection();
		try {
			lc.connect(ldapHost, ldapPort);
			lc.bind(LDAPConnection.LDAP_V3, loginDN, password.getBytes("UTF8"));
			LDAPSearchConstraints cons = lc.getSearchConstraints();
			cons.setBatchSize(0);
			cons.setMaxResults(5);
			LDAPSearchResults searchResults = lc.search(searchBase,
					searchScope, searchFilter, null, false);
			searchResults.getCount();
			while (searchResults.hasMore()) {
				LDAPEntry nextEntry = null;
				try {
					nextEntry = searchResults.next();
				} catch (LDAPException e) {
					// System.out.println("Error: " + e.toString());
					if (e.getResultCode() == LDAPException.LDAP_TIMEOUT
							|| e.getResultCode() == LDAPException.CONNECT_ERROR) {
						break;
					} else {
						continue;
					}
				}
				v("\rDN =: " + nextEntry.getDN());
				 System.out.println("DN =: " + nextEntry.getDN());
				// System.out.println("|---- Attributes list: ");
				LDAPAttributeSet attributeSet = nextEntry.getAttributeSet();
				Iterator<LDAPAttribute> allAttributes = attributeSet.iterator();
				while (allAttributes.hasNext()) {
					LDAPAttribute attribute = allAttributes.next();
					String attributeName = attribute.getName();
					Enumeration<String> allValues = attribute.getStringValues();
					if (null == allValues) {
						continue;
					}
					while (allValues.hasMoreElements()) {
						String value = allValues.nextElement();
						v(attributeName + " = " + value);
//						 System.out.println("|---- ---- " + attributeName+
//						 " = " + value);
					}
				}
			}
		} catch (LDAPException e) {
			System.out.println("Error: " + e.toString());
		} catch (UnsupportedEncodingException e) {
			System.out.println("Error: " + e.toString());
		} finally {
			try {
				if (lc.isConnected()) {
					lc.disconnect();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
