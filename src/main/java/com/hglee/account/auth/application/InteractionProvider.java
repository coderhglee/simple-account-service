package com.hglee.account.auth.application;

import com.hglee.account.auth.dto.InteractionResponse;

public interface InteractionProvider {
	InteractionResponse create(String prompt);

	InteractionResponse verify(String interactionId, String prompt);

	void finish(String interactionId);
}
