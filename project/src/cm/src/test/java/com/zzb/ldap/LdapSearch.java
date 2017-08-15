package com.zzb.ldap;  
  
import java.util.ArrayList;  
import java.util.Enumeration;  
import java.util.HashMap;  
import java.util.List;  
import java.util.Map;  
import java.util.Properties;  
  





import javax.naming.Context;  
import javax.naming.NamingEnumeration;  
import javax.naming.NamingException;  
import javax.naming.directory.Attribute;  
import javax.naming.directory.Attributes;  
import javax.naming.directory.DirContext;  
import javax.naming.directory.SearchControls;  
import javax.naming.directory.SearchResult;  
import javax.naming.ldap.InitialLdapContext;  
  
/** 
 * LDAP分页查询,使用时需要异步统计返回结果集的总行数 
 * @author sxl 
 * 
 */  
public class LdapSearch {  
	public static void main(String[] args) {
		String p2 = "objectClass=pilotPerson";
		String[] p3 = { "DN","cn" };
		List l =LdapSearch.searchContextOneByPage("dc=baoxian,dc=com",p2,p3, 0,-1);
		for (Object object : l) {
			System.out.println(object);
		}
	}
	public static List<Map<String,Object>> getData(String param2,String[] param3,int from,int to){
		String[]  agent={"DN","description","displayName"}; 
		List<Map<String,Object>> l =LdapSearch.searchContextOneByPage("dc=baoxian,dc=com",param2,param3, from,to);
		return l;
	}
	
    /** 
     * 根据条件查找指定DN的条目下的一层所有属性 
     *   
     * @param dn 
     *            要查询的BaseDN名称 
     * @param filter 
     *            要查询的过滤字符串 
     * @param returnedAtts 
     *            要查找的属性 
     * @param start 
     *            开始行 
     * @param limit 
     *            分页大小,如果为-1,则返回结果集的总行数 
     * @return 符合查询结果的List 
     */  
    public static List searchContextOneByPage(String dn, String filter, String[] returnedAtts, int start, int limit){  
        DirContext context = getDirContext("ldap://203.195.141.57:389","cn=root,dc=baoxian,dc=com","111");  
        if(context == null) return null;  
        // 实例化一个搜索器  
        SearchControls constraints = new SearchControls();  
        // 设置搜索器的搜索范围为ONELEVEL  
        constraints.setSearchScope( SearchControls.SUBTREE_SCOPE);  
        // 设置返回的属性  
        if (returnedAtts != null) {  
            constraints.setReturningAttributes(returnedAtts);  
        }  
        try {  
            if (filter == null || filter.trim().equals("")) {  
                filter = "objectclass=*";  
            }  
            NamingEnumeration<SearchResult> results = context.search(dn, filter, constraints);  
            context.close();  
            return searchPage(results,start,limit);  
        } catch (NamingException ex) {  
            ex.printStackTrace();  
            return null;  
        }  
    }  
    /** 
     * 分页查询 
     * @param resultList 
     * @param results 
     * @throws NamingException 
     */  
    private static List searchPage(NamingEnumeration<SearchResult> results,int start,int limit) throws NamingException {  
        List resultList = new ArrayList();  
        int row = 0;  
        boolean flag = false;  
        while (results != null && results.hasMore()) {  
            SearchResult si = results.next();// 取一个条目  
            if(limit == -1)//如果limit==-1,只统计总行数  
                row++;  
            else {  
                if(row++ == start) flag = true;//从start行开始取数据  
                Attributes attrs = si.getAttributes();  
                if (attrs != null && flag) {  
                    Map resultRowMap = new HashMap();// 一行数据
                    resultRowMap.put("DN", si.getName());
                    for (NamingEnumeration ae = attrs.getAll(); ae.hasMoreElements();) {  
                        Attribute attr = (Attribute) ae.next();// 获取一个属性  
                        String attrId = attr.getID();  
                        Enumeration vals = attr.getAll();  
                        if (vals != null) {  
                            ArrayList<Object> valList = new ArrayList();  
                            while (vals.hasMoreElements()) {  
                                Object obj = vals.nextElement();  
                                if (obj instanceof String) {  
                                	System.out.println(obj);
                                    String _value = (String) obj;  
                                    valList.add(_value);  
                                } else if (obj instanceof byte[]) {  
                                    valList.add(new String((byte[])obj));  
                                } else {
                                	 valList.add(obj);  
                                }
                            }  
                            resultRowMap.put(attrId, valList);  
                        }  
                    }  
                    resultList.add(resultRowMap);  
                    if(resultList.size() == limit){  
                        break;  
                    }  
                }  
            }  
              
        }  
        results.close();  
        if(limit == -1)//如果limit为-1,则只返回总行数  
            resultList.add(row);  
        return resultList;  
    }  
    /** 
     * 从LDAP中取得一个连接 
     * @return DirContext 
     */  
    private static DirContext getDirContext(String url,String userdn,String password){  
        Properties mEnv = new Properties();  
        mEnv.put(Context.AUTHORITATIVE, "true");  
        mEnv.put("com.sun.jndi.ldap.connect.pool", "false");  
        mEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");  
        mEnv.put(Context.PROVIDER_URL, url);  
        mEnv.put("com.sun.jndi.ldap.connect.timeout","3000");  
        mEnv.put(Context.SECURITY_AUTHENTICATION, "simple");  
        mEnv.put(Context.SECURITY_PRINCIPAL, userdn);  
        mEnv.put(Context.SECURITY_CREDENTIALS, password);  
        DirContext ctx = null;  
        try {  
            ctx = new InitialLdapContext(mEnv, null);  
        } catch (NamingException ex) {  
            ex.printStackTrace();  
            if(ctx != null)  
                try {  
                    ctx.close();  
                } catch (NamingException e) {  
                    e.printStackTrace();  
                }  
        }  
        return ctx;  
  
    }  
}  
