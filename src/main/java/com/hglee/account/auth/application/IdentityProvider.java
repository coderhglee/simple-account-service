package com.hglee.account.auth.application;

import com.hglee.account.auth.dto.AuthenticationResponse;

public interface IdentityProvider {
	AuthenticationResponse signInWithMobile(String mobile, String password);
}
