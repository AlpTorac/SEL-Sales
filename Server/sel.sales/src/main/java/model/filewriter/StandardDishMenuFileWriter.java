package model.filewriter;

import model.dish.serialise.FileDishMenuSerialiser;

public class StandardDishMenuFileWriter extends DishMenuFileWriter {
	public StandardDishMenuFileWriter(String address) {
		super(address, new FileDishMenuSerialiser());
	}
}
