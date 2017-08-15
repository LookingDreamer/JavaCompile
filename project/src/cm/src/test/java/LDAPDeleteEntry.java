
 
import java.io.UnsupportedEncodingException;
 

import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPException;
import com.zzb.mobile.util.EncodeUtils.Md5Encodes;
 
/**
 * 删除条目的示例
 * blog http://www.micmiu.com
 *
 * @author Michael
 *
 */
public class LDAPDeleteEntry {
 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
 
//		String ldapHost = "localhost";
//		String loginDN = "cn=Manager,dc=micmiu,dc=com";
//		String password = "secret";
//		String deleteDN = "uid=addnew,ou=Developer,dc=micmiu,dc=com";
// 
//		int ldapPort = LDAPConnection.DEFAULT_PORT;
//		int ldapVersion = LDAPConnection.LDAP_V3;
//		LDAPConnection lc = new LDAPConnection();
//		try {
//			lc.connect(ldapHost, ldapPort);
//			lc.bind(ldapVersion, loginDN, password.getBytes("UTF8"));
// 
//			lc.delete(deleteDN);
//			System.out.println(" delete Entry: " + deleteDN + " success.");
//			lc.disconnect();
//		} catch (LDAPException e) {
//			if (e.getResultCode() == LDAPException.NO_SUCH_OBJECT) {
//				System.err.println("Error: No such object");
//			} else if (e.getResultCode() == LDAPException.INSUFFICIENT_ACCESS_RIGHTS) {
//				System.err.println("Error: Insufficient rights");
//			} else {
//				System.err.println("Error: " + e.toString());
//			}
//		} catch (UnsupportedEncodingException e) {
//			System.out.println("Error: " + e.toString());
//		} finally {
//			try {
//				if (lc.isConnected()) {
//					lc.disconnect();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
		System.out.println(Md5Encodes.encodeMd5("a1234567"));
 
	}
 
}

