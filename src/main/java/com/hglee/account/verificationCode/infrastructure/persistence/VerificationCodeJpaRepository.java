package com.hglee.account.verificationCode.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hglee.account.verificationCode.domain.VerificationCode;
import com.hglee.account.verificationCode.domain.VerificationCodeId;

public interface VerificationCodeJpaRepository extends JpaRepository<VerificationCode, VerificationCodeId> {
}
