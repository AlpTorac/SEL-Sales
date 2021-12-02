package model.entity;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import model.datamapper.IAttribute;
import model.entity.id.EntityID;

public abstract class AccumulatingAggregate<A extends IAttribute, I extends Entity<A>> implements IAggregate<A, I> {
	private Map<I, BigDecimal> map;
	
	public AccumulatingAggregate() {
		this.map = this.initMap();
	}
	
	protected Map<I, BigDecimal> initMap() {
		return new ConcurrentHashMap<I, BigDecimal>();
	}
	
	/**
	 * Only returns the array of the entities
	 */
	@SuppressWarnings("unchecked")
	@Override
	public I[] getAllItems() {
		return (I[]) this.getElementToAmountMap().values().toArray(Object[]::new);
	}

	@Override
	public boolean isEmpty() {
		return this.getElementToAmountMap().isEmpty();
	}

	protected Map<I, BigDecimal> getElementToAmountMap() {
		return this.map;
	}
	
	protected Entry<I, BigDecimal> getEntry(EntityID id) {
		Optional<Entry<I, BigDecimal>> o = this.getElementToAmountMap().entrySet().stream()
				.filter(e -> e.getKey().getID().equals(id))
				.findFirst();
		
		return o.isPresent() ? o.get() : null;
	}
	
	public BigDecimal getElementAmount(EntityID id) {
		Entry<I, BigDecimal> e = this.getEntry(id);
		if (e != null) {
			return e.getValue();
		}
		return null;
	}
	
	@Override
	public I getElement(EntityID id) {
		Entry<I, BigDecimal> e = this.getEntry(id);
		if (e != null) {
			return e.getKey();
		}
		return null;
	}
	
	@Override
	public void addElement(I element) {
		BigDecimal pastAmount = this.getElementToAmountMap().put(element, BigDecimal.ONE);
		if (pastAmount != null) {
			this.getElementToAmountMap().put(element, pastAmount.add(BigDecimal.ONE));
		}
	}
	
	public void addElement(I element, BigDecimal amount) {
		BigDecimal pastAmount = this.getElementToAmountMap().put(element, amount);
		if (pastAmount != null) {
			this.getElementToAmountMap().put(element, amount.add(pastAmount));
		}
	}
	
	public boolean removeElementCompletely(EntityID id) {
		return this.getElementToAmountMap().keySet().removeIf(i -> i.getID().equals(id));
	}
	
	public void removeElement(EntityID id, BigDecimal amount) {
		Entry<I, BigDecimal> e = this.getEntry(id);
		if (e != null) {
			BigDecimal newAmount = e.getValue().subtract(amount);
			if (newAmount.compareTo(BigDecimal.ZERO) == 1) {
				e.setValue(newAmount);
			} else {
				this.getElementToAmountMap().remove(e.getKey());
			}
		}
	}
	
	@Override
	public abstract AccumulatingAggregate<A, I> getEmptyClone();
	
	public AccumulatingAggregate<A, I> combine(AccumulatingAggregate<A, I> anotherAggregate) {
		AccumulatingAggregate<A, I> result = this.getEmptyClone();
		this.getElementToAmountMap().entrySet().forEach(e -> result.addElement(e.getKey(), e.getValue()));
		anotherAggregate.getElementToAmountMap().entrySet().forEach(e -> result.addElement(e.getKey(), e.getValue()));
		return result;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null && !(o instanceof AccumulatingAggregate<?,?>)) {
			return false;
		}
		
		AccumulatingAggregate<?,?> castedO = (AccumulatingAggregate<?,?>) o;
		
		if (this.getElementToAmountMap().size() != castedO.getElementToAmountMap().size()) {
			return false;
		}
		
		for (Entry<I, BigDecimal> e : this.getElementToAmountMap().entrySet()) {
			if (castedO.getElementToAmountMap().entrySet().stream()
					.map(castedOE -> {return castedOE.getKey().equals(e.getKey()) &&
							castedOE.getValue().compareTo(e.getValue()) == 0;
					}).reduce(false, (b1,b2)->Boolean.logicalOr(b1, b2)).booleanValue()	== false) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean contains(I element) {
		return this.getElementToAmountMap().containsKey(element);
	}
	
	@Override
	public boolean contains(EntityID id) {
		return this.getElementToAmountMap().keySet().stream().anyMatch(i -> i.getID().equals(id));
	}
	
	public void addAll(AccumulatingAggregate<A, I> aggr) {
		for (Entry<I, BigDecimal> e : aggr.getElementToAmountMap().entrySet()) {
			this.getElementToAmountMap().merge(e.getKey(), e.getValue(),
					(v1,v2)->{
						return v1.add(v2);
					});
		}
	}
}
