package model.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

import model.IDishMenuItemFinder;
import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemID;
import model.dish.IDishMenuItemIDFactory;

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
		Calendar date = this.parseOrderDate(serialisedDate);
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
	default int parseYear(String s) {
		return Integer.valueOf(s);
	}
	default int parseMonth(String s) {
		return Integer.valueOf(s);
	}
	default int parseDay(String s) {
		return Integer.valueOf(s);
	}
	default int parseHour(String s) {
		return Integer.valueOf(s);
	}
	default int parseMinute(String s) {
		return Integer.valueOf(s);
	}
	default int parseSecond(String s) {
		return Integer.valueOf(s);
	}
	default Calendar parseOrderDate(String s) {
		Calendar date = new GregorianCalendar();
		int year = this.parseYear(s.substring(0, 4));
		int month = this.parseMonth(s.substring(4, 6));
		int day = this.parseMonth(s.substring(6, 8));
		int hour = this.parseHour(s.substring(8, 10));
		int minute = this.parseHour(s.substring(10, 12));
		int second = this.parseHour(s.substring(12, 14));
		
		date.set(year, month, day, hour, minute, second);
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
	
	String getOrderItemDataFieldSeperator();
	String getOrderItemDataNewLine();
	String getOrderDataFieldSeperator();
	String getOrderDataFieldEnd();
}
