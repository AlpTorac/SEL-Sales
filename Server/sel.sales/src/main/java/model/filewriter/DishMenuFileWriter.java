package model.filewriter;

import model.dish.IDishMenuData;
import model.dish.serialise.FileDishMenuSerialiser;

public abstract class DishMenuFileWriter extends FileAccess {
	private FileDishMenuSerialiser serialiser;

	public DishMenuFileWriter(String address, FileDishMenuSerialiser serialiser) {
		super(address);
		this.serialiser = serialiser;
	}
	
	protected FileDishMenuSerialiser getSerialiser() {
		return this.serialiser;
	}
	public boolean writeDishMenuData(IDishMenuData d) {
		return this.writeToFile(this.getSerialiser().serialise(d));
	}
}
