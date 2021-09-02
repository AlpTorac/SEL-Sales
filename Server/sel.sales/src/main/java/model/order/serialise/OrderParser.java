package model.order.serialise;

import model.IDishMenuItemFinder;
import model.order.IOrderDataFactory;
import model.order.IOrderItemDataFactory;

public class OrderParser implements IOrderParser {
	private IDishMenuItemFinder finder;
	private IOrderDataFactory orderDataFac;
	private IOrderItemDataFactory orderItemDataFac;
	private IOrderFormat orderFormat;
	
	OrderParser(
			IDishMenuItemFinder finder,
			IOrderDataFactory orderDataFac,
			IOrderItemDataFactory orderItemDataFac,
			IOrderFormat orderFormat) {
		
		this.finder = finder;
		this.orderDataFac = orderDataFac;
		this.orderItemDataFac = orderItemDataFac;
		this.orderFormat = orderFormat;
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

}
