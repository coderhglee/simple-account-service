package com.hglee.account.common.persistence;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.hglee.account.core.IDomainEvent;
import com.hglee.account.core.IEventPublisher;

@Component
public class DomainEventPublisher implements IEventPublisher {
	private final ApplicationEventPublisher publisher;

	public DomainEventPublisher(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}

	@Override
	public void publish(IDomainEvent event) {
		this.publisher.publishEvent(event);
	}
}
