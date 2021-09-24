package model.dish.serialise;

public abstract class DishMenuItemFormat implements IDishMenuItemFormat {
	private String dishMenuItemDataFieldSeperator;
	
	protected DishMenuItemFormat(String dishMenuItemDataFieldSeperator) {
		this.dishMenuItemDataFieldSeperator = dishMenuItemDataFieldSeperator;
	}

	@Override
	public String getDishMenuItemDataFieldSeperator() {
		return this.dishMenuItemDataFieldSeperator;
	}
}
