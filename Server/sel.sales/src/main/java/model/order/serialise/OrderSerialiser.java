package model.order.serialise;

import model.dish.serialise.DishMenuItemSerialiser;
import model.dish.serialise.IDishMenuItemSerialiser;

public class OrderSerialiser implements IOrderSerialiser {
	private IOrderFormat format;
	private IDishMenuItemSerialiser menuItemSerialiser;
	
	public OrderSerialiser() {
		this.format = new OrderFormat();
		this.menuItemSerialiser = new DishMenuItemSerialiser();
	}
	
	@Override
	public IDishMenuItemSerialiser getDishMenuItemSerialiser() {
		return this.menuItemSerialiser;
	}

	@Override
	public IOrderFormat getOrderFormat() {
		return this.format;
	}

}
