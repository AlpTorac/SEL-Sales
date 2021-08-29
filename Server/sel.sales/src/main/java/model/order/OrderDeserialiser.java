package model.order;

import model.IDishMenuItemFinder;
import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemIDFactory;

public abstract class OrderDeserialiser implements IOrderDeserialiser {
	protected IOrderParser orderParser;
	
	protected IDishMenuItemFinder finder;
	protected IOrderFactory orderFac;
	protected IOrderDataFactory orderDataFac;
	protected IOrderItemFactory itemFac;
	protected IDishMenuItemDataFactory dishMenuItemDataFac;
	protected IOrderItemDataFactory orderItemDataFac;
	protected IOrderIDFactory orderIDFac;
	protected IDishMenuItemIDFactory dishMenuItemIDFac;
	
	public OrderDeserialiser() {
		
	}
	
	@Override
	public IOrder deserialise(String s) {
		return this.orderParser.parseOrder(s);
	}

	protected void setOrderParser(IOrderParser orderParser) {
		this.orderParser = orderParser;
	}

	protected void setFinder(IDishMenuItemFinder finder) {
		this.finder = finder;
	}

	protected void setOrderFac(IOrderFactory orderFac) {
		this.orderFac = orderFac;
	}

	protected void setOrderDataFac(IOrderDataFactory orderDataFac) {
		this.orderDataFac = orderDataFac;
	}

	protected void setItemFac(IOrderItemFactory itemFac) {
		this.itemFac = itemFac;
	}

	protected void setDishMenuItemDataFac(IDishMenuItemDataFactory dishMenuItemDataFac) {
		this.dishMenuItemDataFac = dishMenuItemDataFac;
	}

	protected void setOrderItemDataFac(IOrderItemDataFactory orderItemDataFac) {
		this.orderItemDataFac = orderItemDataFac;
	}

	protected void setOrderIDFac(IOrderIDFactory orderIDFac) {
		this.orderIDFac = orderIDFac;
	}

	protected void setDishMenuItemIDFac(IDishMenuItemIDFactory dishMenuItemIDFac) {
		this.dishMenuItemIDFac = dishMenuItemIDFac;
	}

	protected IOrderParser getOrderParser() {
		return orderParser;
	}

	protected IDishMenuItemFinder getFinder() {
		return finder;
	}

	protected IOrderFactory getOrderFac() {
		return orderFac;
	}

	protected IOrderDataFactory getOrderDataFac() {
		return orderDataFac;
	}

	protected IOrderItemFactory getItemFac() {
		return itemFac;
	}

	protected IDishMenuItemDataFactory getDishMenuItemDataFac() {
		return dishMenuItemDataFac;
	}

	protected IOrderItemDataFactory getOrderItemDataFac() {
		return orderItemDataFac;
	}

	protected IOrderIDFactory getOrderIDFac() {
		return orderIDFac;
	}

	protected IDishMenuItemIDFactory getDishMenuItemIDFac() {
		return dishMenuItemIDFac;
	}
}
