package model.order.serialise;

import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemFinder;
import model.order.IOrderDataFactory;
import model.order.IOrderItemDataFactory;

public class OrderParser implements IOrderParser {
	private IDishMenuItemFinder finder;
	private IDishMenuItemDataFactory menuItemDataFac;
	private IOrderDataFactory orderDataFac;
	private IOrderItemDataFactory orderItemDataFac;
	private IOrderFormat orderFormat;
	
	public OrderParser(
			IDishMenuItemFinder finder,
			IOrderDataFactory orderDataFac,
			IOrderItemDataFactory orderItemDataFac,
			IOrderFormat orderFormat,
			IDishMenuItemDataFactory menuItemDataFac) {
		
		this.finder = finder;
		this.orderDataFac = orderDataFac;
		this.orderItemDataFac = orderItemDataFac;
		this.orderFormat = orderFormat;
		this.menuItemDataFac = menuItemDataFac;
	}

	@Override
	public IDishMenuItemFinder getFinder() {
		return this.finder;
	}
	@Override
	public IOrderDataFactory getOrderDataFactory() {
		return this.orderDataFac;
	}
	@Override
	public IOrderItemDataFactory getOrderItemDataFactory() {
		return this.orderItemDataFac;
	}
	@Override
	public IOrderFormat getOrderFormat() {
		return this.orderFormat;
	}

	@Override
	public IDishMenuItemDataFactory getDishMenuItemDataFactory() {
		return this.menuItemDataFac;
	}

}
