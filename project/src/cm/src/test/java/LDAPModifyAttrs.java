import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPModification;

/**
 * 修改操作示例 blog http://www.micmiu.com
 *
 * @author Michael
 *
 */
public class LDAPModifyAttrs {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String ldapHost = "203.195.141.57";
		String loginDN = "cn=root,dc=baoxian,dc=com";
		String password = "111";
		String modifyDN = "uid=Michael,ou=Developer,dc=micmiu,dc=com";

		int ldapPort = LDAPConnection.DEFAULT_PORT;
		int ldapVersion = LDAPConnection.LDAP_V3;
		LDAPConnection lc = new LDAPConnection();
		List<LDAPModification> modList = new ArrayList<LDAPModification>();
		String desc = "This object was modified at " + new Date();
		LDAPAttribute attribute = new LDAPAttribute("description", desc);
		modList.add(new LDAPModification(LDAPModification.ADD, attribute));
		attribute = new LDAPAttribute("telephoneNumber", "180-8888-xxxx");
		modList.add(new LDAPModification(LDAPModification.ADD, attribute));
		// Replace the labeledURI address with a new value
		attribute = new LDAPAttribute("labeledURI", "www.micmiu.com");
		modList.add(new LDAPModification(LDAPModification.REPLACE, attribute));
		// delete the email attribute
		attribute = new LDAPAttribute("mail");
		modList.add(new LDAPModification(LDAPModification.DELETE, attribute));
		LDAPModification[] mods = new LDAPModification[modList.size()];
		mods = (LDAPModification[]) modList.toArray(mods);
		try {
			lc.connect(ldapHost, ldapPort);
			lc.bind(ldapVersion, loginDN, password.getBytes("UTF8"));
			lc.modify(modifyDN, mods);
			System.out
					.println("LDAPAttribute add、replace、delete all successful.");
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
