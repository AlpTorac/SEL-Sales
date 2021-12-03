package model.entity;

import java.math.BigDecimal;

public class AccumulatingAggregateEntry<T> {
	private T item;
	private BigDecimal amount;
	
	public AccumulatingAggregateEntry(T item, BigDecimal amount) {
		this.item = item;
		this.amount = amount;
	}
	
	public T getItem() {
		return this.item;
	}
	
	public BigDecimal getAmount() {
		return this.amount;
	}
}