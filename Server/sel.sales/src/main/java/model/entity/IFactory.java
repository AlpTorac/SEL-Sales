package model.entity;

import model.datamapper.IAttribute;
import model.entity.id.EntityID;

public interface IFactory<A extends IAttribute, E extends Entity<A>, V extends ValueObject<A>> {
	V entityToValue(E entity);
	E valueToEntity(V valueObject);
	V constructMinimalValueObject(EntityID id);
}
