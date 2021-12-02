package model.entity;

import model.datamapper.IAttribute;
import model.entity.id.EntityID;

public abstract class Entity<A extends IAttribute> extends IDOwner<A> implements Comparable<Entity<A>> {
	protected Entity(EntityID id) {
		super(id);
	}

	public int compareTo(Entity<A> o) {
		return this.getID().compareTo(o.getID());
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
