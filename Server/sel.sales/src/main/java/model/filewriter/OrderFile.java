package model.filewriter;

import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemFinder;
import model.order.IOrderData;
import model.order.IOrderDataFactory;
import model.order.serialise.IOrderParser;
import model.order.serialise.OrderParser;

public abstract class OrderFile extends FileAccess {
	private final static String defaultName = "orders";
	
	public OrderFile(String address) {
		super(address);
	}
	public String getDefaultFileName() {
		return defaultName;
	}
	public static String getDefaultFileNameForClass() {
		return defaultName;
	}
}
