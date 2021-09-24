package model.filewriter;

import model.dish.serialise.DishMenuFormat;

public class FileDishMenuFormat extends DishMenuFormat {
	public FileDishMenuFormat() {
		super(";\n", new FileDishMenuItemFormat());
	}
}
