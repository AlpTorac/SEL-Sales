package model.entity;

import model.datamapper.IAttribute;
import model.entity.id.EntityID;

public abstract class IDOwner<A extends IAttribute> extends AttributeOwner<A> {
	private final EntityID id;
	
	protected IDOwner(EntityID id) {
		super();
		this.id = id;
	}
	
	public EntityID getID() {
		return this.id;
	}
}
