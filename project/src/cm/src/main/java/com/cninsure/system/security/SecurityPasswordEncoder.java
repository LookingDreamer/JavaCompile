package com.cninsure.system.security;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.cninsure.core.utils.StringUtil;

public class SecurityPasswordEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		if (rawPassword != null) {
			return StringUtil.md5Base64(rawPassword.toString());
		}
		return null;

	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		if ((encodedPassword == null || encodedPassword.length() == 0) 
				|| (rawPassword == null || rawPassword.length() == 0)) {
			return false;
		} else {
			String rawEncodedPassword = StringUtil.md5Base64(rawPassword
					.toString());
			if (rawEncodedPassword != null && rawEncodedPassword.length() > 0
					&& rawEncodedPassword.equals(encodedPassword)) {
				return true;
			}
		}

		return false;
	}
}
