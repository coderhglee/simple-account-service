package com.hglee.account.accounts.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.hglee.account.common.BaseEntity;
import com.hglee.account.core.AggregateRoot;

import lombok.Getter;

@Getter
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

	protected Account() {
	}

	public Account(String password, String mobile, String email, Status status, String name, String nickName) {
		this.id = generateId();
		this.password = password;
		this.mobile = mobile;
		this.email = email;
		this.status = status;
		this.name = name;
		this.nickName = nickName;
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

	public String getStatusAsString() {
		return status.name();
	}

	private String generateId() {
		return UUID.randomUUID().toString();
	}

	public boolean isSignedUp() {
		return this.status == Status.ACTIVATED;
	}

	public void resetPassword(String newPassword) {
		this.password = newPassword;
	}
}
