package model.order.serialise;

import model.IDishMenuItemFinder;
import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemIDFactory;
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
	protected IDishMenuItemIDFactory dishMenuItemIDFac;
	
	protected IOrderFormat orderFormat;

	public OrderDeserialiser() {
		
	}
	
	@Override
	public IOrderData deserialise(String s) {
		return this.orderParser.parseOrderData(s);
	}
}
