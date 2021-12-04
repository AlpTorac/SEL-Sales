package model.entity;

import model.datamapper.IAttribute;
import model.entity.id.EntityID;

public abstract class Entity<A extends IAttribute> extends IDOwner<A> {
	protected Entity(EntityID id) {
		super(id);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Entity<?>) || !super.equals(o)) {
			return false;
		}
		Entity<?> castedO = (Entity<?>) o;
		return this.getID().equals(castedO.getID());
	}
}
