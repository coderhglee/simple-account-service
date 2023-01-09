package com.hglee.account.common;

import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionalEventListener;

public abstract class EventHandler<T> {
	@Async
	@TransactionalEventListener
	public abstract void handle(T event);
}
