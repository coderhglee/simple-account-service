package com.hglee.account.auth.domain.repository;

import java.util.Optional;

import com.hglee.account.auth.domain.Interaction;

public interface IInteractionRepository {
	Interaction save(Interaction interaction);

	Optional<Interaction> findOne(String id);

	void remove(String id);
}
