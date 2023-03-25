package com.hglee.account.accounts.application.out.service;

import org.springframework.stereotype.Service;

import com.hglee.account.accounts.application.command.RequestAccountVerificationCommand;
import com.hglee.account.accounts.application.command.SignUpWithMobileAndEmailCommand;
import com.hglee.account.accounts.application.command.VerifyAccountCommand;
import com.hglee.account.accounts.application.usecase.RequestAccountVerificationUseCase;
import com.hglee.account.accounts.application.out.usecase.SignUpAccountUseCase;
import com.hglee.account.accounts.application.usecase.SignUpWithMobileAndEmailUseCase;
import com.hglee.account.accounts.application.usecase.VerifyAccountUseCase;
import com.hglee.account.accounts.dto.AccountResponseDto;
import com.hglee.account.accounts.dto.SignUpMobileRequest;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class SignUpAccountService implements SignUpAccountUseCase {
	private final RequestAccountVerificationUseCase requestAccountVerificationUseCase;
	private final VerifyAccountUseCase verifyAccountUseCase;
	private final SignUpWithMobileAndEmailUseCase signUpWithMobileAndEmailUseCase;

	@Override
	public void requestAccountVerificationMobile(String mobile) {
		requestAccountVerificationUseCase.execute(new RequestAccountVerificationCommand(mobile));
	}

	@Override
	public void verifyMobile(String mobile, String code) {
		verifyAccountUseCase.execute(new VerifyAccountCommand(mobile, code));
	}

	@Override
	public AccountResponseDto signUpWithMobile(SignUpMobileRequest request) {
		return signUpWithMobileAndEmailUseCase.execute(
				new SignUpWithMobileAndEmailCommand(request.getMobile(), request.getEmail(), request.getPassword(),
						request.getName(), request.getNickName(), request.getCode()));
	}
}
