import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPSearchResults;

public class LDAPDeleAll {
	String ldapHost = "203.195.141.57";
	String loginDN = "cn=root,dc=baoxian,dc=com";
	String password = "111";
	String searchBase = "dc=baoxian,dc=com";
	String searchFilter = "objectClass=*";
	int ldapPort = LDAPConnection.DEFAULT_PORT;

	@SuppressWarnings("unchecked")
	public void deleAll() {
		ArrayList<String> dns = new ArrayList<String>();
		int searchScope = LDAPConnection.SCOPE_SUB;
		LDAPConnection lc = new LDAPConnection();
		try {
			lc.connect(ldapHost, ldapPort);
			lc.bind(LDAPConnection.LDAP_V3, loginDN, password.getBytes("UTF8"));
			LDAPSearchResults searchResults = lc.search(searchBase,searchScope, searchFilter, null, false);
			while (searchResults.hasMore()) {
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
				System.out.println("DN =: " + nextEntry.getDN());
				dns.add(nextEntry.getDN());
				
			}
			System.out.println(dns.size());
			Collections.sort(dns,new SortBylength());
			for (int i = dns.size() - 1; i >= 0; i--) {
				String dn = dns.get(i);
				System.out.println(" delete Entry: " + dn + " start.");
				lc.delete(dn);
			}

		} catch (LDAPException e) {
			e.printStackTrace();
			if (e.getResultCode() == LDAPException.NO_SUCH_OBJECT) {
				System.err.println("Error: No such entry");
			} else if (e.getResultCode() == LDAPException.NO_SUCH_ATTRIBUTE) {
				System.err.println("Error: No such attribute");
			} else {
				System.err.println("Error: " + e.toString());
			}
		} catch (UnsupportedEncodingException e) {
			System.err.println("Error: " + e.toString());
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
	class SortBylength implements Comparator<Object> {
		 public int compare(Object o1, Object o2) {
		  String s1 = (String) o1;
		  String s2 = (String) o2;
		  if (s1.length() > s2.length())
		   return 1;
		  return 0;
		 }
		}
	public static void main(String[] args) {
		LDAPDeleAll  n = new LDAPDeleAll();
		n.deleAll();
	}
}
