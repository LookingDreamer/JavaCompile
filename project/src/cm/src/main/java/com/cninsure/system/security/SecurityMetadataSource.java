package com.cninsure.system.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.cninsure.system.service.INSCResourceRoleService;

public class SecurityMetadataSource implements
		FilterInvocationSecurityMetadataSource {
	private static Map<RequestMatcher, Collection<ConfigAttribute>> resourceMap = null;
	private INSCResourceRoleService inscResourceRoleService;
	
	public SecurityMetadataSource(INSCResourceRoleService inscResourceRoleService) {
		this.inscResourceRoleService = inscResourceRoleService;
		loadResourceDefine();
	}

	private void loadResourceDefine() {
		if(resourceMap == null){
			resourceMap = new HashMap<RequestMatcher, Collection<ConfigAttribute>>();
			
			Map<String, List<String>> map = this.inscResourceRoleService.getResourceRolesMap();
			if(map != null && map.size() > 0 && ! map.isEmpty()){
				Collection<ConfigAttribute> atts = null;
				ConfigAttribute ca = null;
				for(String key : map.keySet()){
					List<String> list = map.get(key);
					if(list != null && list.size() > 0 && ! list.isEmpty()){
						atts = new ArrayList<ConfigAttribute>();
						for(String str : list){
							ca = new SecurityConfig("ROLE_" + str);
							atts.add(ca);
							ca = null;
						}
						resourceMap.put(new AntPathRequestMatcher(key), atts);
						atts = null;
					}
					
				}
				
			}
			
		}
	}

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object)
			throws IllegalArgumentException {
		final HttpServletRequest request = ((FilterInvocation) object).getRequest();
		for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : resourceMap
				.entrySet()) {
			if (entry.getKey().matches(request)) {
				return entry.getValue();
			}
		}
		return null;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return new ArrayList<ConfigAttribute>();  
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

}
