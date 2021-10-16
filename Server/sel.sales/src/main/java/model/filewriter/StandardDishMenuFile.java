package model.filewriter;

import model.dish.IDishMenuDataFactory;
import model.dish.IDishMenuItemDataFactory;
import model.dish.serialise.FileDishMenuSerialiser;

public class StandardDishMenuFile extends DishMenuFile {
	public StandardDishMenuFile(String address, IDishMenuDataFactory menuDataFac) {
		super(address, new FileDishMenuSerialiser(), menuDataFac);
	}
}
