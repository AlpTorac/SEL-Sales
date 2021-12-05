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
	
	@Override
	public boolean equals(Object o) {
		if (o == null | !(o instanceof IDOwner)) {
			return false;
		}
		
		IDOwner<?> castedO = (IDOwner<?>) o;
		return this.getID().equals(castedO.getID()) && super.equals(castedO);
	}
}
