package model.dish.serialise;

public abstract class DishMenuFormat implements IDishMenuFormat {
	private String dishMenuItemDataFieldEnd;
	private IDishMenuItemFormat itemFormat;
	
	protected DishMenuFormat(String dishMenuItemDataFieldEnd, IDishMenuItemFormat itemFormat) {
		this.dishMenuItemDataFieldEnd = dishMenuItemDataFieldEnd;
		this.itemFormat = itemFormat;
	}
	
	@Override
	public String getDishMenuItemDataFieldEnd() {
		return this.dishMenuItemDataFieldEnd;
	}

	@Override
	public IDishMenuItemFormat getDishMenuItemFormat() {
		return this.itemFormat;
	}

}
