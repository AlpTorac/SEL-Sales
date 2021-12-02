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
import model.order.AccumulatingOrderItemAggregate;
import model.order.AccumulatingOrderItemAggregate;
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
		String[] serialisedOrderSpecificAndOrderItem = this.getSerialisedOrderSpecificAndOrderItem(serialisedOrderBody);
		String[] serialisedOrderDataFields = this.getSerialisedOrderSpecificData(
				this.getSerialisedOrderDataField(serialisedOrderSpecificAndOrderItem));
		
		String serialisedOrderID = this.getSerialisedOrderID(serialisedOrderDataFields);
		String serialisedDate = this.getSerialisedDate(serialisedOrderDataFields);
		String serialisedIsCash = this.getSerialisedIsCash(serialisedOrderDataFields);
		String serialisedIsHere = this.getSerialisedIsHere(serialisedOrderDataFields);
		
		LocalDateTime date = this.parseOrderDate(serialisedDate);
		boolean IsCash = this.parseBoolean(serialisedIsCash);
		boolean IsHere = this.parseBoolean(serialisedIsHere);
		
		String serialisedOrderItem = this.getSerialisedOrderItem(serialisedOrderSpecificAndOrderItem);
		
		AccumulatingOrderItemAggregate[] orderItemData = this.parseOrderItems(serialisedOrderItem);
		
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
	default String[] getSerialisedOrderSpecificAndOrderItem(String s) {
		return s.split(this.getOrderFormat().getOrderAttributeFieldEnd());
	}
	default String getSerialisedOrderDataField(String[] ss) {
		return ss[0];
	}
	default String getSerialisedOrderItem(String[] ss) {
		return ss[1];
	}
	default String[] getSerialisedOrderSpecificData(String s) {
		return s.split(this.getOrderFormat().getOrderAttributeFieldSeperator());
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
	default AccumulatingOrderItemAggregate[] parseOrderItems(String s) {
		Collection<AccumulatingOrderItemAggregate> orderItemData = new ArrayList<AccumulatingOrderItemAggregate>();
		
//		this.removeLastNewLine(s);
		String[] orderItems = s.split(this.getOrderFormat().getOrderItemFieldEnd());
		
		for (String item : orderItems) {
			orderItemData.add(this.parseOrderItem(item));
		}
		
		return orderItemData.toArray(AccumulatingOrderItemAggregate[]::new);
	}
	default AccumulatingOrderItemAggregate parseOrderItem(String s) {
		String[] orderItemDataParts = s.split(this.getOrderFormat().getOrderItemFieldSeperator());
		
		String menuItemID = orderItemDataParts[0];
		BigDecimal amount = this.parseAmount(orderItemDataParts[1]);
		
		IDishMenuItemData menuItemData = this.getDishMenuItemDataFactory().menuItemToData(this.getFinder().getDish(menuItemID));
		
		return this.getOrderItemFactory().constructData(menuItemData, amount);
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
	OrderItemFactory getOrderItemFactory();
	IOrderFormat getOrderFormat();
	
	void setFinder(IDishMenuItemFinder finder);
}
