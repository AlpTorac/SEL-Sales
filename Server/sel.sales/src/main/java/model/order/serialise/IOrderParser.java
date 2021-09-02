package model.order.serialise;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import model.IDishMenuItemFinder;
import model.dish.IDishMenuItemData;
import model.order.IOrderData;
import model.order.IOrderDataFactory;
import model.order.IOrderItemData;
import model.order.IOrderItemDataFactory;

public interface IOrderParser {
	default IOrderData parseOrderData(String s) {
		String[] serialisedOrderSpecificAndOrderItemData = this.getSerialisedOrderSpecificAndOrderItemData(s);
		String[] serialisedOrderDataFields = this.getSerialisedOrderSpecificData(this.getSerialisedOrderDataField(serialisedOrderSpecificAndOrderItemData));
		
		String serialisedOrderID = this.getSerialisedOrderID(serialisedOrderDataFields);
		String serialisedDate = this.getSerialisedDate(serialisedOrderDataFields);
		String serialisedCashOrCard = this.getSerialisedCashOrCard(serialisedOrderDataFields);
		String serialisedHereOrToGo = this.getSerialisedHereOrToGo(serialisedOrderDataFields);
		
		LocalDateTime date = this.parseOrderDate(serialisedDate);
		boolean cashOrCard = this.parseCashOrCard(serialisedCashOrCard);
		boolean hereOrToGo = this.parseHereOrToGo(serialisedHereOrToGo);
		
		String serialisedOrderItemData = this.getSerialisedOrderItemData(serialisedOrderSpecificAndOrderItemData);
		
		IOrderItemData[] orderItemData = this.parseOrderItems(serialisedOrderItemData);
		
		return this.getOrderDataFactory().constructData(orderItemData, date, cashOrCard, hereOrToGo, serialisedOrderID);
	}
	default String[] getSerialisedOrderSpecificAndOrderItemData(String s) {
		return s.split(this.getOrderFormat().getOrderDataFieldEnd());
	}
	default String getSerialisedOrderDataField(String[] ss) {
		return ss[0];
	}
	default String getSerialisedOrderItemData(String[] ss) {
		return ss[1];
	}
	default String[] getSerialisedOrderSpecificData(String s) {
		return s.split(this.getOrderFormat().getOrderDataFieldSeperator());
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
	default IOrderItemData[] parseOrderItems(String s) {
		Collection<IOrderItemData> orderItemData = new ArrayList<IOrderItemData>();
		
		this.removeLastNewLine(s);
		String[] orderItems = s.split(this.getOrderFormat().getOrderItemDataNewLine());
		
		for (String item : orderItems) {
			orderItemData.add(this.parseOrderItemData(item));
		}
		
		return orderItemData.toArray(IOrderItemData[]::new);
	}
	default IOrderItemData parseOrderItemData(String s) {
		String[] orderItemDataParts = s.split(this.getOrderFormat().getOrderItemDataFieldSeperator());
		
		String menuItemID = 	orderItemDataParts[0];
		BigDecimal amount = 			this.parseAmount(orderItemDataParts[1]);
		
		IDishMenuItemData menuItemData = this.getFinder().getDish(menuItemID);
		
		return this.getOrderItemDataFactory().constructData(menuItemData, amount);
	}
	default LocalDateTime parseOrderDate(String s) {
		LocalDateTime date = this.getOrderFormat().parseDate(s);
		return date;
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
		if (s.endsWith(this.getOrderFormat().getOrderItemDataNewLine())) {
			s = s.substring(0, s.length() - 1);
		}
	}
	
	IDishMenuItemFinder getFinder();
	IOrderDataFactory getOrderDataFactory();
	IOrderItemDataFactory getOrderItemDataFactory();
	IOrderFormat getOrderFormat();
}
