package com.hglee.account.common;

import org.springframework.transaction.event.TransactionalEventListener;

public abstract class EventHandler<T> {
	@TransactionalEventListener
	public abstract void handle(T event);
}
