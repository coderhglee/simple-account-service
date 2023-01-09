package com.hglee.account.accounts.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.hglee.account.common.BaseEntity;
import com.hglee.account.core.AggregateRoot;

@Entity
public class Account extends BaseEntity implements AggregateRoot {
	@Id
	private String id;

	private String password;

	@Column(unique = true)
	private String mobile;

	@Column(unique = true)
	private String email;

	private Status status;

	private String name;

	private String nickName;

	@Embedded
	private PinCode pinCode;

	@Embedded
	private PasswordResetRequest passwordResetRequest;

	protected Account() {
	}

	public Account(String id, String mobile, String email, Status status, String name, String nickName,
			PinCode pinCode) {
		this.id = id;
		this.mobile = mobile;
		this.email = email;
		this.status = status;
		this.name = name;
		this.nickName = nickName;
		this.pinCode = pinCode;
	}

	public Account(String mobile, Status status, PinCode pinCode) {
		this.id = generateId();
		this.mobile = mobile;
		this.status = status;
		this.pinCode = pinCode;
	}

	public Account(String id, String password, String mobile, String email, Status status, String name,
			String nickName) {
		this.id = id;
		this.password = password;
		this.mobile = mobile;
		this.email = email;
		this.status = status;
		this.name = name;
		this.nickName = nickName;
	}

	public Account(String id, String mobile, Status status, PasswordResetRequest passwordResetRequest) {
		this.id = id;
		this.mobile = mobile;
		this.status = status;
		this.passwordResetRequest = passwordResetRequest;
	}

	public Account(String mobile, Status status) {
		this.id = generateId();
		this.mobile = mobile;
		this.status = status;
	}

	public String getId() {
		return this.id;
	}

	public PinCode getPinCode() {
		return pinCode;
	}

	public Status getStatus() {
		return status;
	}

	public String getStatusAsString() {
		return status.name();
	}

	public String getMobile() {
		return mobile;
	}

	private String generateId() {
		return UUID.randomUUID().toString();
	}

	public boolean isExpiredPinCode() {
		return this.pinCode.isExpired();
	}

	public boolean isExpiredPasswordResetCode() {
		return this.passwordResetRequest.isExpired() || this.passwordResetRequest.isConfirmed();
	}

	public boolean isSignedUp() {
		return this.status == Status.ACTIVATED;
	}

	public boolean isVerified() {
		return this.status == Status.VERIFIED;
	}

	public static Account ofRequestVerificationByMobile(String mobile) {
		return new Account(mobile, Status.VERIFICATION_REQUESTED);
	}

	public void requestVerificationByMobile() {
		this.status = Status.VERIFICATION_REQUESTED;
		this.pinCode = PinCode.generateCode();
	}

	public void requestPasswordReset() {
		this.passwordResetRequest = PasswordResetRequest.generate();
	}

	public boolean isSamePinCode(String code) {
		return this.pinCode.isSameCode(code);
	}

	public void verify() {
		this.status = Status.VERIFIED;
	}

	public void signUpWithMobileAndEmail(String email, String password, String name, String nickName) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.nickName = nickName;
		this.status = Status.ACTIVATED;
	}

	public String getEmail() {
		return this.email;
	}

	public String getName() {
		return name;
	}

	public String getNickName() {
		return nickName;
	}

	public String getPassword() {
		return this.password;
	}

	public PasswordResetRequest getPasswordResetRequest() {
		return passwordResetRequest;
	}

	public boolean isSamePasswordResetCode(String code) {
		return this.passwordResetRequest.isSameCode(code);
	}

	public void confirmPasswordReset() {
		this.passwordResetRequest.confirm();
	}

	public void resetPassword(String newPassword) {
		this.password = newPassword;
		this.passwordResetRequest = null;
	}

	public boolean hasPasswordResetRequest() {
		return this.passwordResetRequest != null;
	}

	public boolean isConfirmedPasswordReset(String code) {
		return this.passwordResetRequest.isSameCode(code) && this.passwordResetRequest.isConfirmed();
	}
}
