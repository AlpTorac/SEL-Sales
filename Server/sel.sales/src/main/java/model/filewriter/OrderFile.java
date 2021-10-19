package model.filewriter;

import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemFinder;
import model.order.IOrderData;
import model.order.IOrderDataFactory;
import model.order.serialise.IOrderParser;
import model.order.serialise.OrderParser;

public abstract class OrderFile extends FileAccess {
	private final static String defaultName = "orders";
	private FileOrderSerialiser serialiser;
	private IOrderParser parser;
	
	public OrderFile(String address, FileOrderSerialiser serialiser, IDishMenuItemFinder finder, IOrderDataFactory orderDataFac,
			IDishMenuItemDataFactory menuItemDataFac) {
		super(address);
		this.serialiser = serialiser;
		this.parser = new OrderParser(finder, orderDataFac, orderDataFac.getItemDataFac(), serialiser.getOrderFormat(),
				menuItemDataFac);
	}
	public String getDefaultFileName() {
		return defaultName;
	}
	public static String getDefaultFileNameForClass() {
		return defaultName;
	}
	protected FileOrderSerialiser getSerialiser() {
		return this.serialiser;
	}
	public boolean writeOrderData(IOrderData[] ds) {
		return this.writeToFile(this.getSerialiser().serialiseOrderDatas(ds));
	}
	public boolean writeOrderData(IOrderData d) {
		return this.writeToFile(this.getSerialiser().serialiseOrderData(d));
	}
	public IOrderData[] loadOrders() {
		return this.parser.parseOrderDatas(this.readFile());
	}
}
