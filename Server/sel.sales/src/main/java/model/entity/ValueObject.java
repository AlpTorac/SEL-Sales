package model.entity;

import model.datamapper.IAttribute;
import model.entity.id.EntityID;

public abstract class ValueObject<A extends IAttribute> extends IDOwner<A> {
	protected ValueObject(EntityID id) {
		super(id);
	}
}
