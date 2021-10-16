package model.filewriter;

import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemFinder;
import model.order.IOrderDataFactory;
import model.order.IOrderItemDataFactory;

public class StandardOrderFile extends OrderFile {
	public StandardOrderFile(String address, IDishMenuItemFinder finder, IOrderDataFactory orderDataFac,
			IDishMenuItemDataFactory menuItemDataFac) {
		super(address, new FileOrderSerialiser(), finder, orderDataFac, menuItemDataFac);
	}
}
