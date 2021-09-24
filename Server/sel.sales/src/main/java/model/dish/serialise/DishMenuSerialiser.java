package model.dish.serialise;

public abstract class DishMenuSerialiser implements IDishMenuSerialiser {
	private IDishMenuItemSerialiser itemSerialiser;
	private IDishMenuFormat format;
	private boolean addLastEnd;
	
	protected DishMenuSerialiser(IDishMenuItemSerialiser itemSerialiser, IDishMenuFormat format, boolean addLastEnd) {
		this.itemSerialiser = itemSerialiser;
		this.format = format;
		this.addLastEnd = addLastEnd;
	}
	
	@Override
	public boolean addLastEnd() {
		return this.addLastEnd;
	}
	
	@Override
	public IDishMenuItemSerialiser getDishMenuItemSerialiser() {
		return this.itemSerialiser;
	}

	@Override
	public IDishMenuFormat getDishMenuFormat() {
		return this.format;
	}

}
