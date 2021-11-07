package model.order.serialise;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemFinder;
import model.order.IOrderData;
import model.order.IOrderDataFactory;
import model.order.IOrderItemData;
import model.order.IOrderItemDataFactory;
import model.util.IParser;

public interface IOrderParser extends IParser {
	default IOrderData[] parseOrderDatas(String s) {
		String serialisedOrders = this.removeStartAndEnd(s);
		String[] serialisedSeparatedOrders = serialisedOrders.split(this.getOrderFormat().getOrderEnd());
		Collection<IOrderData> orderDatas = new ArrayList<IOrderData>();
		for (String so : serialisedSeparatedOrders) {
			orderDatas.add(this.parseOrderData(so));
		}
		Map<String, IOrderData> newOrderDatas = new HashMap<String, IOrderData>();
		orderDatas.forEach(od -> {
			String id;
			if (newOrderDatas.containsKey(id = od.getID().toString())) {
				newOrderDatas.put(id, newOrderDatas.get(id).combine(od));
			} else {
				newOrderDatas.put(id, od);
			}
		});
		return newOrderDatas.values().toArray(IOrderData[]::new);
	}
	default IOrderData parseOrderData(String s) {
		String serialisedOrderBody = this.removeStartAndEnd(s);
		String[] serialisedOrderSpecificAndOrderItemData = this.getSerialisedOrderSpecificAndOrderItemData(serialisedOrderBody);
		String[] serialisedOrderDataFields = this.getSerialisedOrderSpecificData(
				this.getSerialisedOrderDataField(serialisedOrderSpecificAndOrderItemData));
		
		String serialisedOrderID = this.getSerialisedOrderID(serialisedOrderDataFields);
		String serialisedDate = this.getSerialisedDate(serialisedOrderDataFields);
		String serialisedIsCash = this.getSerialisedIsCash(serialisedOrderDataFields);
		String serialisedIsHere = this.getSerialisedIsHere(serialisedOrderDataFields);
		
		LocalDateTime date = this.parseOrderDate(serialisedDate);
		boolean IsCash = this.parseBoolean(serialisedIsCash);
		boolean IsHere = this.parseBoolean(serialisedIsHere);
		
		String serialisedOrderItemData = this.getSerialisedOrderItemData(serialisedOrderSpecificAndOrderItemData);
		
		IOrderItemData[] orderItemData = this.parseOrderItems(serialisedOrderItemData);
		
		return this.getOrderDataFactory().constructData(orderItemData, date, IsCash, IsHere, serialisedOrderID);
	}
	default String removeStartAndEnd(String s) {
		int begin = 0;
		int end = s.length();
		
		if (s.startsWith(this.getOrderFormat().getOrderStart())) {
			begin += this.getOrderFormat().getOrderStart().length();
		}
		if (s.endsWith(this.getOrderFormat().getOrderEnd())) {
			end -= this.getOrderFormat().getOrderEnd().length();
		}
		
		return s.substring(begin, end);
	}
	/**
	 * @return {orderData, orderedItems}
	 */
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
	default String getSerialisedIsCash(String[] ss) {
		return ss[2];
	}
	default String getSerialisedIsHere(String[] ss) {
		return ss[3];
	}
	default IOrderItemData[] parseOrderItems(String s) {
		Collection<IOrderItemData> orderItemData = new ArrayList<IOrderItemData>();
		
//		this.removeLastNewLine(s);
		String[] orderItems = s.split(this.getOrderFormat().getOrderItemDataFieldEnd());
		
		for (String item : orderItems) {
			orderItemData.add(this.parseOrderItemData(item));
		}
		
		return orderItemData.toArray(IOrderItemData[]::new);
	}
	default IOrderItemData parseOrderItemData(String s) {
		String[] orderItemDataParts = s.split(this.getOrderFormat().getOrderItemDataFieldSeperator());
		
		String menuItemID = orderItemDataParts[0];
		BigDecimal amount = this.parseAmount(orderItemDataParts[1]);
		
		IDishMenuItemData menuItemData = this.getDishMenuItemDataFactory().menuItemToData(this.getFinder().getDish(menuItemID));
		
		return this.getOrderItemDataFactory().constructData(menuItemData, amount);
	}
	default LocalDateTime parseOrderDate(String s) {
		LocalDateTime date = this.getOrderFormat().parseDate(s);
		return date;
	}

	default BigDecimal parseAmount(String s) {
		return this.parseBigDecimal(s);
	}
	
	IDishMenuItemDataFactory getDishMenuItemDataFactory();
	IDishMenuItemFinder getFinder();
	IOrderDataFactory getOrderDataFactory();
	IOrderItemDataFactory getOrderItemDataFactory();
	IOrderFormat getOrderFormat();
	
	void setFinder(IDishMenuItemFinder finder);
}
