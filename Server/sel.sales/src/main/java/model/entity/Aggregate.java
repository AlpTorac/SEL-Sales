package model.entity;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import model.datamapper.IAttribute;
import model.entity.id.EntityID;

public abstract class Aggregate<A extends IAttribute, I extends IDOwner<A>> implements IAggregate<A, I> {
	private Collection<I> col;
	
	public Aggregate() {
		this.col = this.initCol();
	}
	
	protected Collection<I> initCol() {
		return new CopyOnWriteArrayList<I>();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public I[] getAllItems() {
		return (I[]) this.getElementCollection().toArray(Object[]::new);
	}

	@Override
	public boolean isEmpty() {
		return this.getElementCollection().isEmpty();
	}

	protected Collection<I> getElementCollection() {
		return this.col;
	}
	
	@Override
	public void addElement(I element) {
		this.getElementCollection().add(element);
	}
	
	@Override
	public I getElement(EntityID id) {
		Optional<I> o = this.getElementCollection().stream()
				.filter(i -> i.getID().equals(id))
				.findFirst();
		
		return o.isPresent() ? o.get() : null;
	}
	
	@Override
	public boolean removeElementCompletely(EntityID id) {
		return this.getElementCollection().removeIf(i -> i.getID().equals(id));
	}
	
	@Override
	public abstract Aggregate<A, I> getEmptyClone();
	
	public Aggregate<A, I> combine(Aggregate<A, I> anotherCombineable) {
		Aggregate<A, I> result = this.getEmptyClone();
		result.getElementCollection().addAll(this.getElementCollection());
		result.getElementCollection().addAll(anotherCombineable.getElementCollection());
		return result;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null && !(o instanceof Aggregate<?,?>)) {
			return false;
		}
		Aggregate<?,?> castedO = (Aggregate<?,?>) o;
		return this.getElementCollection().size() == castedO.getElementCollection().size() &&
				this.getElementCollection().containsAll(castedO.getElementCollection());
	}
	
	@Override
	public boolean contains(I element) {
		return this.getElementCollection().contains(element);
	}
	
	@Override
	public boolean contains(EntityID id) {
		return this.getElementCollection().stream().anyMatch(i -> i.getID().equals(id));
	}
	
	public void addAll(Aggregate<A, I> aggr) {
		this.getElementCollection().addAll(aggr.getElementCollection());
	}
}
