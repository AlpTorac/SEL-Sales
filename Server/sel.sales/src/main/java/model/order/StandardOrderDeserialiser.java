package model.order;

import model.IDishMenuItemFinder;
import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemIDFactory;

public class StandardOrderDeserialiser extends OrderDeserialiser {

	public StandardOrderDeserialiser(IDishMenuItemFinder finder, IDishMenuItemDataFactory dishMenuItemDataFac, IDishMenuItemIDFactory dishMenuItemIDFac) {
		super();
		
		IOrderDataFactory orderDataFac = new OrderDataFactory();
		IOrderItemFactory itemFac = new OrderItemFactory();
		IOrderItemDataFactory orderItemDataFac = new OrderItemDataFactory();
		IOrderIDFactory orderIDFac = new OrderIDFactory();
		IOrderFactory orderFac = new OrderFactory(itemFac);
		
		this.finder = finder;
		this.orderFac = orderFac;
		this.orderDataFac = orderDataFac;
		this.itemFac = itemFac;
		this.dishMenuItemDataFac = dishMenuItemDataFac;
		this.orderItemDataFac = orderItemDataFac;
		this.orderIDFac = orderIDFac;
		this.dishMenuItemIDFac = dishMenuItemIDFac;
		
		this.orderParser = new OrderParser(
				this.finder,
				this.orderFac,
				this.orderDataFac,
				this.itemFac,
				this.dishMenuItemDataFac,
				this.orderItemDataFac,
				this.orderIDFac,
				this.dishMenuItemIDFac);
	}
}
