package com.hglee.account.auth.application;

import com.hglee.account.auth.dto.AuthenticationResponse;
import com.hglee.account.auth.dto.RequestAccountVerificationMobileResponse;
import com.hglee.account.auth.dto.SignUpMobileRequest;

public interface IdentityProvider {
	AuthenticationResponse signInWithMobile(String mobile, String password);

	AuthenticationResponse signInWithEmail(String email, String password);

	RequestAccountVerificationMobileResponse requestAccountVerificationMobileForSignUp(String mobile);

	void verifyMobileForSignUp(String mobile, String code, String interactionId);

	AuthenticationResponse signUpWithMobile(SignUpMobileRequest request);
}
