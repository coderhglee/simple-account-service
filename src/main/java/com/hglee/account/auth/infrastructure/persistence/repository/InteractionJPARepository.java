package com.hglee.account.auth.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hglee.account.auth.domain.Interaction;

public interface InteractionJPARepository extends JpaRepository<Interaction, String> {
}
