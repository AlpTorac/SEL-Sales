package model.entity.id;

public class MinimalIDFactory extends EntityIDFactory {

	@Override
	public EntityID createID(Object... idObjects) {
		if (idObjects != null && idObjects.length > 0) {
			return new MinimalID((String) idObjects[0]);
		}
		return null;
	}

}
