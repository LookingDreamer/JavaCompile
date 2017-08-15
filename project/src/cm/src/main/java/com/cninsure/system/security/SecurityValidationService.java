package com.cninsure.system.security;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCRoleService;
import com.cninsure.system.service.INSCUserRoleService;
import com.cninsure.system.service.INSCUserService;

@Service(value = "securityValidationService")
public class SecurityValidationService implements UserDetailsService {

	@Resource
	private INSCUserService inscUserService;

	@Resource
	private INSCUserRoleService inscUserRoleService;

	@Resource
	private INSCRoleService inscRoleService;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		if (StringUtils.isBlank(username)) {
			throw new UsernameNotFoundException("用户名为空，请输入用户名!");
		}
		
		INSCUser inscUser = inscUserService.queryByUserCode(username);
		if (inscUser == null) {
			throw new UsernameNotFoundException("用户" + username + "不存在!");
		}
		if("0".equals(inscUser.getStatus())){
			throw new UsernameNotFoundException("用户" + username + "停用!");
		}

		Set<GrantedAuthority> grantedAuths = obtainGrantedAuthorities(inscUser);

		boolean enabled = true;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;
		UserDetails userDetails = new org.springframework.security.core.userdetails.User(
				username, inscUser.getPassword(), enabled,
				accountNonExpired, credentialsNonExpired, accountNonLocked,
				grantedAuths);
		return userDetails;
	}

	private Set<GrantedAuthority> obtainGrantedAuthorities(INSCUser user) {
		Set<GrantedAuthority> authSet = new HashSet<GrantedAuthority>();
		String userId = user.getId();
		if (StringUtils.isNotBlank(userId)) {
			List<String> roleidList = inscUserRoleService
					.selectRoleidByUserid(userId);
			if (roleidList != null && roleidList.size() > 0
					&& !roleidList.isEmpty()) {
				List<String> rolecodeList = inscRoleService
						.selectRolecodesByRoleids(roleidList);
				if (rolecodeList != null && rolecodeList.size() > 0
						&& !rolecodeList.isEmpty()) {
					for (String rolecode : rolecodeList) {
						authSet.add(new SimpleGrantedAuthority("ROLE_" + rolecode));
					}
				}
			}
		}
		authSet.add(new SimpleGrantedAuthority("ROLE_USER"));
		return authSet;
	}
}
