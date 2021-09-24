package model.dish.serialise;

import model.filewriter.FileDishMenuFormat;
import model.filewriter.FileDishMenuItemSerialiser;

public class FileDishMenuSerialiser extends DishMenuSerialiser {
	public FileDishMenuSerialiser() {
		super(new FileDishMenuItemSerialiser(), new FileDishMenuFormat(), true);
	}
}
