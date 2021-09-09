package model.filewriter;

import model.dish.IDishMenuData;
import model.dish.IDishMenuItemData;

public abstract class DishMenuFileWriter extends FileAccess {
	private FileDishMenuItemSerialiser serialiser;

	public DishMenuFileWriter(String address, FileDishMenuItemSerialiser serialiser) {
		super(address);
		this.serialiser = serialiser;
	}
	
	protected FileDishMenuItemSerialiser getSerialiser() {
		return this.serialiser;
	}
	public boolean writeDishMenuData(IDishMenuData d) {
		boolean b = true;
		for (IDishMenuItemData idata : d.getAllDishMenuItems()) {
			b = b && this.writeToFile(this.getSerialiser().serialise(idata));
		}
		return b;
	}
}
