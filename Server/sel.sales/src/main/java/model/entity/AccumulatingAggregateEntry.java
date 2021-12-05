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
	
	@Override
	public String toString() {
		return this.getItem().toString() + "=" + this.getAmount().toPlainString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof AccumulatingAggregateEntry)) {
			return false;
		}
		AccumulatingAggregateEntry<?> castedO = (AccumulatingAggregateEntry<?>) o;
		return this.getItem().equals(castedO.getItem()) && this.getAmount().compareTo(castedO.getAmount()) == 0;
	}
}