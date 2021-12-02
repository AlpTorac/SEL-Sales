package model.entity;

import model.datamapper.IAttribute;

public interface IFactory<A extends IAttribute, E extends Entity<A>, V extends ValueObject<A>> {
	V entityToValue(E entity);
}
