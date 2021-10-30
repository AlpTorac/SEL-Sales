package model.id;

public class FixIDFactory extends EntityIDFactory {

	@Override
	public FixID createID(Object... idObjects) {
		return new FixID((String) idObjects[0]);
	}

}
