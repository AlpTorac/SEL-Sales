package model.filewriter;

public class StandardDishMenuFileWriter extends DishMenuFileWriter {
	public StandardDishMenuFileWriter(String address) {
		super(address, new FileDishMenuItemSerialiser());
	}
}
