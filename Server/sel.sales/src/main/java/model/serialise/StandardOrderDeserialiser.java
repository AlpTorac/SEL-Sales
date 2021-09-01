package model.serialise;

import model.IDishMenuItemFinder;
import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemIDFactory;
import model.order.IOrderDataFactory;
import model.order.IOrderFactory;
import model.order.IOrderIDFactory;
import model.order.IOrderItemDataFactory;
import model.order.IOrderItemFactory;
import model.order.OrderDataFactory;
import model.order.OrderFactory;
import model.order.OrderIDFactory;
import model.order.OrderItemDataFactory;
import model.order.OrderItemFactory;

public class StandardOrderDeserialiser extends OrderDeserialiser {

	public StandardOrderDeserialiser(IDishMenuItemFinder finder, IDishMenuItemDataFactory dishMenuItemDataFac, IDishMenuItemIDFactory dishMenuItemIDFac) {
		super();
		
		IOrderDataFactory orderDataFac = new OrderDataFactory();
		IOrderItemFactory itemFac = new OrderItemFactory();
		IOrderItemDataFactory orderItemDataFac = new OrderItemDataFactory();
		IOrderIDFactory orderIDFac = new OrderIDFactory();
		IOrderFactory orderFac = new OrderFactory(itemFac);
		IOrderDateParser orderDateParser = new OrderDateParser();
		
		this.finder = finder;
		this.orderFac = orderFac;
		this.orderDataFac = orderDataFac;
		this.itemFac = itemFac;
		this.dishMenuItemDataFac = dishMenuItemDataFac;
		this.orderItemDataFac = orderItemDataFac;
		this.orderIDFac = orderIDFac;
		this.dishMenuItemIDFac = dishMenuItemIDFac;
		this.orderDateParser = orderDateParser;
		
		this.orderParser = new OrderParser(
				this.finder,
				this.orderFac,
				this.orderDataFac,
				this.itemFac,
				this.dishMenuItemDataFac,
				this.orderItemDataFac,
				this.orderIDFac,
				this.dishMenuItemIDFac,
				this.orderDateParser);
	}
}
