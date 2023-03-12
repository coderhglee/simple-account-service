package com.hglee.account.accounts.application.service;

import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hglee.account.accounts.application.command.SignUpWithMobileAndEmailCommand;
import com.hglee.account.accounts.application.usecase.SignUpWithMobileAndEmailUseCase;
import com.hglee.account.accounts.domain.Account;
import com.hglee.account.accounts.domain.Status;
import com.hglee.account.accounts.domain.repository.IAccountRepository;
import com.hglee.account.accounts.dto.AccountResponseDto;
import com.hglee.account.accounts.exception.ConflictException;
import com.hglee.account.verificationCode.application.usecase.FindVerificationCodeUseCase;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SignUpWithMobileAndEmailService implements SignUpWithMobileAndEmailUseCase {
	private final IAccountRepository accountRepository;
	private final PasswordEncoder passwordEncoder;
	private final FindVerificationCodeUseCase findVerificationCodeUseCase;

	@Override
	@Transactional
	public AccountResponseDto execute(SignUpWithMobileAndEmailCommand command) {
		String email = command.getEmail();
		String mobile = command.getMobile();

		checkAlreadyExistAccountByMobileWithEmail(mobile, email);

		findVerificationCodeUseCase.findVerified(mobile, command.getCode())
				.orElseThrow(() -> new IllegalArgumentException("전화번호 인증이 완료되지 않았습니다."));

		String encodedPassword = passwordEncoder.encode(command.getPassword());

		Account account = new Account(encodedPassword, mobile, email, Status.ACTIVATED, command.getName(),
				command.getNickName());

		this.accountRepository.save(account);

		return new AccountResponseDto(account.getId(), account.getMobile(), account.getStatusAsString(),
				account.getEmail(), account.getName(), account.getNickName());
	}

	private void checkAlreadyExistAccountByMobileWithEmail(String mobile, String email) {
		this.accountRepository.findByEmail(email).ifPresent(account -> {
			if (account.isSignedUp()) {
				throw new ConflictException("이미 동일한 이메일로 가입된 계정입니다.");
			}
		});

		this.accountRepository.findByMobile(mobile).ifPresent(account -> {
			if (account.isSignedUp()) {
				throw new ConflictException("이미 동일한 전화번호로 가입된 계정입니다.");
			}
		});
	}
}
