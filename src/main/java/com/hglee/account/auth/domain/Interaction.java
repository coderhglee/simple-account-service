package com.hglee.account.auth.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.hglee.account.common.BaseEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class Interaction extends BaseEntity {
	@Id
	private String id;
	private String prompt;
	private LocalDateTime expiresAt;

	public Interaction(String id, String prompt, LocalDateTime expiresAt) {
		this.id = id;
		this.prompt = prompt;
		this.expiresAt = expiresAt;
	}

	public boolean isExpired() {
		return this.expiresAt.isBefore(LocalDateTime.now());
	}

	public boolean isSamePrompt(String prompt) {
		return this.prompt.equals(prompt);
	}
}
