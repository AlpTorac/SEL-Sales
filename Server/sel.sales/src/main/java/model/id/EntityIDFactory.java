package model.id;

public abstract class EntityIDFactory {
	public EntityIDFactory() {
		
	}
	public abstract EntityID createID(Object... idObjects);
}
