package com.hglee.account.core;

public interface IEventPublisher {
	void publish(IDomainEvent event);
}
