package model.filewriter;

import model.dish.serialise.IDishMenuItemFormat;

public class FileDishMenuFormat implements IDishMenuItemFormat {
	private String dishMenuItemDataFieldEnd = ";\n";
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
