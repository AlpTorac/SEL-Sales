package model.order.serialise;

import model.dish.DishMenuItemDataFactory;
import model.dish.IDishMenuItemDataFactory;
import model.entity.id.MinimalIDFactory;
import model.order.IOrderDataFactory;
import model.order.OrderItemFactory;
import model.order.OrderDataFactory;
import model.order.OrderItemFactory;

public class StandardOrderDeserialiser extends OrderDeserialiser {
	public StandardOrderDeserialiser() {
		super();
		
		OrderItemFactory orderItemDataFac = new OrderItemFactory();
		IOrderDataFactory orderDataFac = new OrderDataFactory(orderItemDataFac, new MinimalIDFactory());
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
