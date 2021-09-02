package model.dish.serialise;

public class DishMenuFormat implements IDishMenuFormat {
	private String dishMenuItemDataFieldEnd = ";";
	private String dishMenuItemDataFieldSeperator = ",";
	
	@Override
	public String getDishMenuItemDataFieldEnd() {
		return this.dishMenuItemDataFieldEnd;
	}

	@Override
	public String getDishMenuItemDataFieldSeperator() {
		return this.dishMenuItemDataFieldSeperator;
	}

}
