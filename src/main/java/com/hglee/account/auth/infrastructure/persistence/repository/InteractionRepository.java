package com.hglee.account.auth.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.hglee.account.auth.domain.Interaction;
import com.hglee.account.auth.domain.repository.IInteractionRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class InteractionRepository implements IInteractionRepository {
	private final InteractionJPARepository interactionJPARepository;

	@Override
	public Interaction save(Interaction interaction) {
		return this.interactionJPARepository.save(interaction);
	}

	@Override
	public Optional<Interaction> findOne(String id) {
		return this.interactionJPARepository.findById(id);
	}

	@Override
	public void remove(String id) {
		this.interactionJPARepository.deleteById(id);
	}
}
