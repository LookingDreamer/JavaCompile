
 
import java.io.UnsupportedEncodingException;
 
import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPAttributeSet;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;
 
/**
 * 添加新条目的示例
 * blog http://www.micmiu.com
 *
 * @author Michael
 *
 */
public class LDAPAddEntry {
 
	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) {
 
		String ldapHost = "localhost";
		String loginDN = "cn=Manager,dc=micmiu,dc=com";
		String password = "secret";
		String containerName = "dc=micmiu,dc=com";
 
		int ldapPort = LDAPConnection.DEFAULT_PORT;
		int ldapVersion = LDAPConnection.LDAP_V3;
		LDAPConnection lc = new LDAPConnection();
		LDAPAttributeSet attributeSet = new LDAPAttributeSet();
 
		attributeSet.add(new LDAPAttribute("objectclass", new String(
				"inetOrgPerson")));
		attributeSet.add(new LDAPAttribute("cn", "Wukong Sun"));
		attributeSet.add(new LDAPAttribute("sn", "Sun"));
		attributeSet.add(new LDAPAttribute("mail", "sjsky007@gmail.com"));
		attributeSet.add(new LDAPAttribute("labeledURI",	"http://www.micmiu.com"));
		attributeSet.add(new LDAPAttribute("userPassword", "111111"));
		attributeSet.add(new LDAPAttribute("uid", "addnew"));
		String dn = "uid=addnew,ou=Developer,"+containerName;
		LDAPEntry newEntry = new LDAPEntry(dn, attributeSet);
		try {
			lc.connect(ldapHost, ldapPort);
			lc.bind(ldapVersion, loginDN, password.getBytes("UTF8"));
			System.out.println("login ldap server successfully.");
			lc.add(newEntry);
			System.out.println("Added object: " + dn + " successfully.");
		} catch (LDAPException e) {
			e.printStackTrace();
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

