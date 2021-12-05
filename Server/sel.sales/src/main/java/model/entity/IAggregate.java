package model.entity;

import java.util.Collection;

import model.datamapper.IAttribute;
import model.entity.id.EntityID;

public interface IAggregate<A extends IAttribute, I extends IDOwner<A>> {
	Collection<I> getAllElements();
	boolean isEmpty();
	void addElement(I element);
	I getElement(EntityID id);
	IAggregate<A, I> getEmptyClone();
	boolean removeElementCompletely(EntityID id);
	boolean contains(I element);
	boolean contains(EntityID id);
	boolean equals(Object o);
}
