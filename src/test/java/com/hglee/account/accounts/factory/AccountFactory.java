package com.hglee.account.accounts.factory;

import java.time.LocalDateTime;
import java.util.UUID;

import com.github.javafaker.Faker;
import com.hglee.account.accounts.domain.Account;
import com.hglee.account.accounts.domain.PasswordResetRequest;
import com.hglee.account.accounts.domain.Status;

public class AccountFactory {
	private String id;

	private String password;

	private String mobile;

	private String email;

	private String name;

	private String nickName;

	private PinCodeFactory pinCode;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	public String getId() {
		return id;
	}

	public String getMobile() {
		return mobile;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public String getNickName() {
		return nickName;
	}

	public String getPassword() {
		return password;
	}

	public PinCodeFactory getPinCode() {
		return pinCode;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public AccountFactory(String id, String password, String mobile, String email, String name, String nickName,
			PinCodeFactory pinCode, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.password = password;
		this.mobile = mobile;
		this.email = email;
		this.name = name;
		this.nickName = nickName;
		this.pinCode = pinCode;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public static AccountFactory build() {
		Faker faker = Faker.instance();

		String password = faker.internet().password(8, 14, false, true) + "1a";

		return new AccountFactory(UUID.randomUUID().toString(), password, faker.numerify("010########"),
				faker.internet().emailAddress(), faker.name().firstName(), faker.pokemon().name(),
				PinCodeFactory.build(), LocalDateTime.now(), LocalDateTime.now());
	}

	public static Account isSignedUpAccount() {
		AccountFactory accountFactory = AccountFactory.build();

		return new Account(accountFactory.getId(), accountFactory.getMobile(), build().getEmail(), Status.ACTIVATED,
				build().getName(), build().getNickName());
	}

	public static Account isSignedUpAccount(String mobile, String encodedPassword) {
		AccountFactory accountFactory = AccountFactory.build();

		return new Account(accountFactory.getId(), encodedPassword, mobile, build().getEmail(), Status.ACTIVATED,
				build().getName(), build().getNickName());
	}

	public static Account isSignedUpAccount(String mobile, String email, String encodedPassword) {
		AccountFactory accountFactory = AccountFactory.build();

		return new Account(accountFactory.getId(), encodedPassword, mobile, email, Status.ACTIVATED, build().getName(),
				build().getNickName());
	}

	public static Account isSignedUpAccount(String encodedPassword) {
		AccountFactory accountFactory = AccountFactory.build();

		return new Account(accountFactory.getId(), encodedPassword, accountFactory.getMobile(), build().getEmail(),
				Status.ACTIVATED, build().getName(), build().getNickName());
	}

	public static Account isSignedUpAccount(String encodedPassword, PasswordResetRequest resetRequest) {
		AccountFactory accountFactory = AccountFactory.build();

		return new Account(accountFactory.getId(), encodedPassword, accountFactory.getMobile(), build().getEmail(),
				Status.ACTIVATED, build().getName(), build().getNickName(), resetRequest);
	}

}
