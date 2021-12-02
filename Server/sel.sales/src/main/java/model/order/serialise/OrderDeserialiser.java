package model.order.serialise;

import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemFinder;
import model.order.IOrderData;
import model.order.IOrderDataFactory;
import model.order.OrderItemFactory;
import model.order.OrderItemFactory;

public abstract class OrderDeserialiser implements IOrderDeserialiser {
	protected IOrderParser orderParser;
	
	protected IDishMenuItemFinder finder;
	protected IOrderDataFactory orderDataFac;
	protected OrderItemFactory itemFac;
	protected IDishMenuItemDataFactory dishMenuItemDataFac;
	protected OrderItemFactory orderItemDataFac;
	
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
