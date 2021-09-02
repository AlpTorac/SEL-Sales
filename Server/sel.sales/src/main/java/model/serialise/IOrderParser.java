package model.serialise;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import model.IDishMenuItemFinder;
import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemID;
import model.dish.IDishMenuItemIDFactory;
import model.order.IOrder;
import model.order.IOrderData;
import model.order.IOrderDataFactory;
import model.order.IOrderFactory;
import model.order.IOrderID;
import model.order.IOrderIDFactory;
import model.order.IOrderItemData;
import model.order.IOrderItemDataFactory;
import model.order.IOrderItemFactory;

public interface IOrderParser {
	
	default IOrder parseOrder(String s) {
		return this.getOrderFac().createOrder(this.getFinder(), this.parseOrderData(s));
	}
	default IOrderData parseOrderData(String s) {
		String[] serialisedOrderSpecificAndOrderItemData = this.getSerialisedOrderSpecificAndOrderItemData(s);
		String[] serialisedOrderDataFields = this.getSerialisedOrderSpecificData(this.getSerialisedOrderDataField(serialisedOrderSpecificAndOrderItemData));
		
		String serialisedOrderID = this.getSerialisedOrderID(serialisedOrderDataFields);
		String serialisedDate = this.getSerialisedDate(serialisedOrderDataFields);
		String serialisedCashOrCard = this.getSerialisedCashOrCard(serialisedOrderDataFields);
		String serialisedHereOrToGo = this.getSerialisedHereOrToGo(serialisedOrderDataFields);
		
		IOrderID orderID = this.parseOrderID(serialisedOrderID);
		LocalDateTime date = this.parseOrderDate(serialisedDate);
		boolean cashOrCard = this.parseCashOrCard(serialisedCashOrCard);
		boolean hereOrToGo = this.parseHereOrToGo(serialisedHereOrToGo);
		
		String serialisedOrderItemData = this.getSerialisedOrderItemData(serialisedOrderSpecificAndOrderItemData);
		
		Collection<IOrderItemData> orderItemData = this.parseOrderItems(serialisedOrderItemData);
		
		return this.getOrderDataFactory().constructData(orderItemData, date, cashOrCard, hereOrToGo, orderID);
	}
	default String[] getSerialisedOrderSpecificAndOrderItemData(String s) {
		return s.split(this.getOrderDataFieldEnd());
	}
	default String getSerialisedOrderDataField(String[] ss) {
		return ss[0];
	}
	default String getSerialisedOrderItemData(String[] ss) {
		return ss[1];
	}
	default String[] getSerialisedOrderSpecificData(String s) {
		return s.split(this.getOrderDataFieldSeperator());
	}
	default String getSerialisedOrderID(String[] ss) {
		return ss[0];
	}
	default String getSerialisedDate(String[] ss) {
		return ss[1];
	}
	default String getSerialisedCashOrCard(String[] ss) {
		return ss[2];
	}
	default String getSerialisedHereOrToGo(String[] ss) {
		return ss[3];
	}
	default Collection<IOrderItemData> parseOrderItems(String s) {
		Collection<IOrderItemData> orderItemData = new ArrayList<IOrderItemData>();
		
		this.removeLastNewLine(s);
		String[] orderItems = s.split(this.getOrderItemDataNewLine());
		
		for (String item : orderItems) {
			orderItemData.add(this.parseOrderItemData(item));
		}
		
		return orderItemData;
	}
	default IOrderItemData parseOrderItemData(String s) {
		String[] orderItemDataParts = s.split(this.getOrderItemDataFieldSeperator());
		
		IDishMenuItemID menuItemID = 	this.parseDishMenuItemID(orderItemDataParts[0]);
		BigDecimal amount = 			this.parseAmount(orderItemDataParts[1]);
		
		IDishMenuItemData menuItemData = this.getFinder().getDish(menuItemID).getDishMenuItemData(this.getDishMenuItemDataFac());
		
		return this.getOrderItemDataFactory().constructData(menuItemData, amount);
	}
	default IOrderID parseOrderID(String s) {
		return this.getOrderIDFac().createOrderID(s);
	}
	default LocalDateTime parseOrderDate(String s) {
		LocalDateTime date = this.getOrderDateParser().parseDate(s);
		return date;
	}
	default IDishMenuItemID parseDishMenuItemID(String s) {
		return this.getDishMenuItemIDFac().createDishMenuItemID(s);
	}
	default BigDecimal parseAmount(String s) {
		return BigDecimal.valueOf(Double.valueOf(s));
	}
	default boolean parseCashOrCard(String s) {
		if (s.equals("0")) {
			return false;
		} else {
			return true;
		}
	}
	default boolean parseHereOrToGo(String s) {
		if (s.equals("0")) {
			return false;
		} else {
			return true;
		}
	}
	default void removeLastNewLine(String s) {
		if (s.endsWith(this.getOrderItemDataNewLine())) {
			s = s.substring(0, s.length() - 1);
		}
	}
	
	IDishMenuItemFinder getFinder();
	IOrderFactory getOrderFac();
	IOrderItemFactory getItemFac();
	IOrderIDFactory getOrderIDFac();
	IDishMenuItemIDFactory getDishMenuItemIDFac();
	IDishMenuItemDataFactory getDishMenuItemDataFac();
	IOrderDataFactory getOrderDataFactory();
	IOrderItemDataFactory getOrderItemDataFactory();
	IOrderDateParser getOrderDateParser();
	
	String getOrderItemDataFieldSeperator();
	String getOrderItemDataNewLine();
	String getOrderDataFieldSeperator();
	String getOrderDataFieldEnd();
}
