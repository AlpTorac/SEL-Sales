package model.filewriter;

import model.dish.IDishMenuData;
import model.dish.IDishMenuDataFactory;
import model.dish.IDishMenuItemDataFactory;
import model.dish.serialise.DishMenuParser;
import model.dish.serialise.FileDishMenuSerialiser;
import model.dish.serialise.IDishMenuParser;

public abstract class DishMenuFile extends FileAccess {
	private final static String defaultName = "menu";
	private FileDishMenuSerialiser serialiser;
	private IDishMenuParser parser;

	public DishMenuFile(String address, FileDishMenuSerialiser serialiser, IDishMenuDataFactory menuDataFac) {
		super(address);
		this.serialiser = serialiser;
		this.parser = new DishMenuParser(serialiser.getDishMenuFormat(), menuDataFac);
	}
	public String getDefaultFileName() {
		return defaultName;
	}
	public static String getDefaultFileNameForClass() {
		return defaultName;
	}
	protected FileDishMenuSerialiser getSerialiser() {
		return this.serialiser;
	}
	public boolean writeDishMenuData(IDishMenuData d) {
		return this.writeToFile(this.getSerialiser().serialise(d));
	}
	public IDishMenuData loadDishMenu() {
		String serialisedContent = this.readFile();
		if (serialisedContent != null && serialisedContent != "") {
			return this.parser.parseDishMenuData(serialisedContent);
		}
		return null;
	}
}
