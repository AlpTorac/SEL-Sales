package model.entity;

import java.math.BigDecimal;
import java.util.Collection;
import model.datamapper.IAttribute;
import model.entity.id.EntityID;

public interface IAccumulatingAggregate<A extends IAttribute, I extends IDOwner<A>> {
	Collection<AccumulatingAggregateEntry<I>> getAllElements();
	boolean isEmpty();
	default void addAllElements(Iterable<AccumulatingAggregateEntry<I>> elements) {
		for (AccumulatingAggregateEntry<I> e : elements) {
			this.addElement(e.getItem(), e.getAmount());
		}
	}
	void addElement(I element, BigDecimal amount);
	I getElement(EntityID id);
	BigDecimal getElementAmount(EntityID id);
	IAccumulatingAggregate<A, I> getEmptyClone();
	boolean removeElementCompletely(EntityID id);
	boolean contains(I element);
	boolean contains(EntityID id);
	boolean equals(Object o);
}
