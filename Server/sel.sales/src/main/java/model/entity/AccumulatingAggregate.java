package model.entity;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import model.datamapper.IAttribute;
import model.entity.id.EntityID;

public abstract class AccumulatingAggregate<A extends IAttribute, I extends IDOwner<A>> implements IAccumulatingAggregate<A, I> {
	private Map<I, BigDecimal> map;
	
	public AccumulatingAggregate() {
		this.map = this.initMap();
	}
	
	protected Map<I, BigDecimal> initMap() {
		return new ConcurrentHashMap<I, BigDecimal>();
	}
	
	@SuppressWarnings("unchecked")
	public AccumulatingAggregateEntry<I>[] getAllEntries() {
		int size = this.getElementToAmountMap().size();
		AccumulatingAggregateEntry<?>[] arr = new AccumulatingAggregateEntry<?>[size];
		int i = 0;
		for (Entry<I, BigDecimal> e : this.getElementToAmountMap().entrySet()) {
			arr[i] = new AccumulatingAggregateEntry<I>(e.getKey(), e.getValue());
			i++;
		}
		return (AccumulatingAggregateEntry<I>[]) arr;
	}
	
	public Collection<AccumulatingAggregateEntry<I>> getAllElements() {
		Collection<AccumulatingAggregateEntry<I>> col = new CopyOnWriteArrayList<AccumulatingAggregateEntry<I>>();
		for (Entry<I, BigDecimal> e : this.getElementToAmountMap().entrySet()) {
			col.add(new AccumulatingAggregateEntry<I>(e.getKey(), e.getValue()));
		}
		return col;
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
	
	public AccumulatingAggregateEntry<I> getElementEntry(EntityID id) {
		Entry<I, BigDecimal> e = this.getEntry(id);
		if (e != null) {
			return new AccumulatingAggregateEntry<I>(e.getKey(), e.getValue());
		}
		return null;
	}
	
	public void addElement(I element, BigDecimal amount) {
		if (element != null) {
			BigDecimal pastAmount = this.getElementToAmountMap().put(element, amount);
			if (pastAmount != null) {
				this.getElementToAmountMap().put(element, amount.add(pastAmount));
			}
		}
	}
	
	public boolean removeElementCompletely(EntityID id) {
		return this.getElementToAmountMap().keySet().removeIf(i -> i.getID().equals(id));
	}
	
	public void removeElement(I item, BigDecimal amount) {
		this.removeElement(item.getID(), amount);
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
	
	public void addAll(Iterable<AccumulatingAggregateEntry<I>> es) {
		for (AccumulatingAggregateEntry<I> e : es) {
			if (e != null) {
				this.addElement(e.getItem(), e.getAmount());
			}
		}
	}
	
	public void addAll(AccumulatingAggregateEntry<I>[] es) {
		for (AccumulatingAggregateEntry<I> e : es) {
			if (e != null) {
				this.addElement(e.getItem(), e.getAmount());
			}
		}
	}
	
	public void addAll(AccumulatingAggregate<A, I> aggr) {
		this.addAll(aggr.getAllElements());
	}
	
	@Override
	public String toString() {
		Optional<String> o = this.getElementToAmountMap().entrySet().stream()
				.map(e -> e.getKey().toString()+"="+e.getValue().toPlainString())
				.reduce((s1,s2)->s1+", "+s2);
		
		return o.isPresent() ? o.get() : "";
	}
}