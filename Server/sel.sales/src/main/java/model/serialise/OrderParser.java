package model.serialise;

import model.IDishMenuItemFinder;
import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemIDFactory;
import model.order.IOrderDataFactory;
import model.order.IOrderFactory;
import model.order.IOrderIDFactory;
import model.order.IOrderItemDataFactory;
import model.order.IOrderItemFactory;

public class OrderParser implements IOrderParser {
	
	private IDishMenuItemFinder finder;
	private IOrderFactory orderFac;
	private IOrderItemFactory itemFac;
	private IOrderIDFactory orderIDFac;
	private IDishMenuItemDataFactory dishMenuItemDataFac;
	private IDishMenuItemIDFactory dishMenuItemIDFac;
	private IOrderDataFactory orderDataFac;
	private IOrderItemDataFactory orderItemDataFac;
	private IOrderDateParser orderDateParser;
	
	OrderParser(
			IDishMenuItemFinder finder,
			IOrderFactory orderFac,
			IOrderDataFactory orderDataFac,
			IOrderItemFactory itemFac,
			IDishMenuItemDataFactory dishMenuItemDataFac,
			IOrderItemDataFactory orderItemDataFac,
			IOrderIDFactory orderIDFac,
			IDishMenuItemIDFactory dishMenuItemIDFac,
			IOrderDateParser orderDateParser) {
		
		this.finder = finder;
		this.orderFac = orderFac;
		this.orderDataFac = orderDataFac;
		this.itemFac = itemFac;
		this.dishMenuItemDataFac = dishMenuItemDataFac;
		this.orderItemDataFac = orderItemDataFac;
		this.orderIDFac = orderIDFac;
		this.dishMenuItemIDFac = dishMenuItemIDFac;
		this.orderDateParser = orderDateParser;
	}
	
	private String orderItemDataFieldSeperator = ",";
	private String orderItemDataNewLine = ";";
	private String orderDataFieldSeperator = "-";
	private String orderDataFieldEnd = ":";

	@Override
	public IDishMenuItemFinder getFinder() {
		return this.finder;
	}
	@Override
	public IOrderFactory getOrderFac() {
		return this.orderFac;
	}
	@Override
	public IOrderItemFactory getItemFac() {
		return this.itemFac;
	}
	@Override
	public IOrderIDFactory getOrderIDFac() {
		return this.orderIDFac;
	}
	@Override
	public IDishMenuItemIDFactory getDishMenuItemIDFac() {
		return this.dishMenuItemIDFac;
	}
	@Override
	public String getOrderItemDataFieldSeperator() {
		return this.orderItemDataFieldSeperator;
	}
	@Override
	public String getOrderItemDataNewLine() {
		return this.orderItemDataNewLine;
	}
	@Override
	public IDishMenuItemDataFactory getDishMenuItemDataFac() {
		return this.dishMenuItemDataFac;
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
	public String getOrderDataFieldSeperator() {
		return this.orderDataFieldSeperator;
	}
	@Override
	public String getOrderDataFieldEnd() {
		return this.orderDataFieldEnd;
	}
	@Override
	public IOrderDateParser getOrderDateParser() {
		return this.orderDateParser;
	}

}
