package model.order.serialise;

import model.dish.DishMenuItemDataFactory;
import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemFinder;
import model.id.FixIDFactory;
import model.order.IOrderDataFactory;
import model.order.IOrderItemDataFactory;
import model.order.OrderDataFactory;
import model.order.OrderItemDataFactory;

public class StandardOrderDeserialiser extends OrderDeserialiser {
	public StandardOrderDeserialiser() {
		super();
		
		IOrderItemDataFactory orderItemDataFac = new OrderItemDataFactory();
		IOrderDataFactory orderDataFac = new OrderDataFactory(orderItemDataFac, new FixIDFactory());
		IOrderFormat orderDateParser = new IntraAppOrderFormat();
		IDishMenuItemDataFactory menuItemDataFac = new DishMenuItemDataFactory();
		
		this.orderDataFac = orderDataFac;
		this.orderItemDataFac = orderItemDataFac;
		this.orderFormat = orderDateParser;
		
		this.orderParser = new OrderParser(
				this.finder,
				this.orderDataFac,
				this.orderItemDataFac,
				this.orderFormat,
				menuItemDataFac);
	}
}
