package model.entity;

import model.datamapper.IAttribute;
import model.entity.id.EntityID;

public abstract class IDOwner<A extends IAttribute> extends AttributeOwner<A> implements Comparable<IDOwner<A>>{
	private final EntityID id;
	
	protected IDOwner(EntityID id) {
		super();
		this.id = id;
	}
	
	public EntityID getID() {
		return this.id;
	}
	
	public int compareTo(IDOwner<A> other) {
		return this.getID().compareTo(other.getID());
	}
}
