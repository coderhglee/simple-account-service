package com.hglee.account.accounts.persistence.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.hglee.account.core.IDomainEvent;
import com.hglee.account.core.IEventPublisher;

@Component
public class EventPublisher implements IEventPublisher {
	private final ApplicationEventPublisher publisher;

	public EventPublisher(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}

	@TransactionalEventListener()
	@Override
	public void publish(IDomainEvent event) {
		this.publisher.publishEvent(event);
	}
}
