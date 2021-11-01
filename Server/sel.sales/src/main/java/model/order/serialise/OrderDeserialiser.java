package model.order.serialise;

import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemFinder;
import model.order.IOrderData;
import model.order.IOrderDataFactory;
import model.order.IOrderItemDataFactory;
import model.order.IOrderItemFactory;

public abstract class OrderDeserialiser implements IOrderDeserialiser {
	protected IOrderParser orderParser;
	
	protected IDishMenuItemFinder finder;
	protected IOrderDataFactory orderDataFac;
	protected IOrderItemFactory itemFac;
	protected IDishMenuItemDataFactory dishMenuItemDataFac;
	protected IOrderItemDataFactory orderItemDataFac;
	
	protected IOrderFormat orderFormat;

	public OrderDeserialiser() {
		
	}
	
	@Override
	public IOrderData deserialise(String s) {
		return this.orderParser.parseOrderData(s);
	}
	
	@Override
	public IOrderData[] deserialiseOrders(String s) {
		return this.orderParser.parseOrderDatas(s);
	}
	
	@Override
	public void setFinder(IDishMenuItemFinder finder) {
		this.finder = finder;
		this.orderParser.setFinder(this.finder);
	}
}
