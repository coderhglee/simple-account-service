package com.hglee.account.auth.application;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

import org.springframework.stereotype.Service;

import com.hglee.account.auth.domain.Interaction;
import com.hglee.account.auth.domain.repository.IInteractionRepository;
import com.hglee.account.auth.dto.InteractionResponse;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class InteractionService implements InteractionProvider {
	private final IInteractionRepository interactionRepository;
	private static final SecureRandom secureRandom = new SecureRandom();
	private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

	@Override
	public InteractionResponse create(String prompt) {
		byte[] randomBytes = new byte[24];
		secureRandom.nextBytes(randomBytes);
		String id = base64Encoder.encodeToString(randomBytes);

		Interaction createdInteraction = new Interaction(id, prompt, LocalDateTime.now().plusMinutes(30));

		this.interactionRepository.save(createdInteraction);

		return new InteractionResponse(createdInteraction.getId());
	}

	@Override
	public InteractionResponse verify(String interactionId, String prompt) {
		Interaction interaction = this.interactionRepository.findOne(interactionId)
				.orElseThrow(() -> new IllegalArgumentException("요청 정보가 없습니다."));

		if (!interaction.isSamePrompt(prompt)) {
			throw new IllegalArgumentException("요청이 올바르지 않습니다.");
		}

		if (interaction.isExpired()) {
			throw new IllegalArgumentException("요청이 만료되었습니다.");
		}

		return new InteractionResponse(interaction.getId());
	}

	@Override
	public void finish(String interactionId) {
		this.interactionRepository.remove(interactionId);
	}
}
