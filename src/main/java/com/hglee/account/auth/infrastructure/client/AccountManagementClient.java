package com.hglee.account.auth.infrastructure.client;

import org.springframework.stereotype.Service;

import com.hglee.account.accounts.application.out.usecase.IAccountService;
import com.hglee.account.accounts.application.out.usecase.SignUpAccountUseCase;
import com.hglee.account.auth.dto.AccountResponseDto;
import com.hglee.account.auth.dto.SignUpMobileRequest;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AccountManagementClient implements IAccountManagementClient {
	private final SignUpAccountUseCase signUpAccountUseCase;
	private final IAccountService accountService;

	@Override
	public AccountResponseDto signInWithMobile(String mobile, String password) {
		com.hglee.account.accounts.dto.AccountResponseDto accountResponseDto = this.accountService.signInWithMobile(
				mobile, password);

		return AccountResponseDto.of(accountResponseDto.getId(), accountResponseDto.getMobile(),
				accountResponseDto.getStatus(), accountResponseDto.getEmail(), accountResponseDto.getName(),
				accountResponseDto.getNickName());
	}

	@Override
	public AccountResponseDto signInWithEmail(String email, String password) {
		com.hglee.account.accounts.dto.AccountResponseDto accountResponseDto = this.accountService.signInWithEmail(
				email, password);

		return AccountResponseDto.of(accountResponseDto.getId(), accountResponseDto.getMobile(),
				accountResponseDto.getStatus(), accountResponseDto.getEmail(), accountResponseDto.getName(),
				accountResponseDto.getNickName());
	}

	@Override
	public AccountResponseDto findById(String id) {
		com.hglee.account.accounts.dto.AccountResponseDto accountResponseDto = accountService.findById(id);

		return AccountResponseDto.of(accountResponseDto.getId(), accountResponseDto.getMobile(),
				accountResponseDto.getStatus(), accountResponseDto.getEmail(), accountResponseDto.getName(),
				accountResponseDto.getNickName());

	}

	@Override
	public void requestAccountVerificationMobile(String mobile) {
		signUpAccountUseCase.requestAccountVerificationMobile(mobile);

	}

	@Override
	public void verifyMobile(String mobile, String code) {
		signUpAccountUseCase.verifyMobile(mobile, code);
	}

	@Override
	public AccountResponseDto signUpWithMobile(SignUpMobileRequest request) {
		com.hglee.account.accounts.dto.AccountResponseDto accountResponseDto = signUpAccountUseCase.signUpWithMobile(
				new com.hglee.account.accounts.dto.SignUpMobileRequest(request.getMobile(), request.getEmail(),
						request.getPassword(), request.getName(), request.getNickName(), request.getCode(),
						request.getInteractionId()));

		return AccountResponseDto.of(accountResponseDto.getId(), accountResponseDto.getMobile(),
				accountResponseDto.getStatus(), accountResponseDto.getEmail(), accountResponseDto.getName(),
				accountResponseDto.getNickName());
	}
}
