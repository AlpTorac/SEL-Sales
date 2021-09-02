package model.order.serialise;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import model.dish.IDishMenuItemData;
import model.dish.serialise.IDishMenuItemSerialiser;
import model.order.IOrderData;
import model.order.IOrderItemData;

public interface IOrderSerialiser {
	default String serialiseOrderData(IOrderItemData[] orderItemsData, LocalDateTime date, boolean isCash, boolean isHere, BigDecimal discount, String orderID) {
		String result = "";
		result += orderID + this.getOrderFormat().getOrderDataFieldSeperator();
		result += this.getOrderFormat().formatDate(date) + this.getOrderFormat().getOrderDataFieldSeperator();
		result += discount.toPlainString() + this.getOrderFormat().getOrderDataFieldSeperator();
		result += this.serialiseBoolean(isCash) + this.getOrderFormat().getOrderDataFieldSeperator();
		result += this.serialiseBoolean(isHere) + this.getOrderFormat().getOrderDataFieldEnd();
		
		for (IOrderItemData i : orderItemsData) {
			result += this.serialiseOrderItemData(i);
		}
		
		return result;
	}
	
	default String serialiseOrderData(IOrderData orderData) {
		return this.serialiseOrderData(orderData.getOrderedItems(), orderData.getDate(), orderData.getCashOrCard(), orderData.getHereOrToGo(), orderData.getOrderDiscount(), orderData.getID());
	}
	
	default String serialiseOrderItemData(IOrderItemData orderItemData) {
		String result = "";
		result += this.serialiseDishMenuItemID(orderItemData.getItemData()) + this.getOrderFormat().getOrderItemDataFieldSeperator() 
				+ this.serialiseOrderItemAmount(orderItemData) + this.getOrderFormat().getOrderItemDataNewLine();
		return result;
	}
	
	default String serialiseOrderDate(IOrderData orderData) {
		return this.getOrderFormat().getDateFormatter().format(orderData.getDate());
	}
	
	default String serialiseDishMenuItemID(IDishMenuItemData menuItemData) {
		return menuItemData.getId();
	}
	default String serialiseOrderItemAmount(IOrderItemData orderItemData) {
		return orderItemData.getAmount().toPlainString();
	}
	
	default String serialiseOrderID(IOrderData orderData) {
		return orderData.getID();
	}
	
	default String serialiseCashOrCard(IOrderData orderData) {
		return this.serialiseBoolean(orderData.getCashOrCard());
	}
	
	default String serialiseHereOrToGo(IOrderData orderData) {
		return this.serialiseBoolean(orderData.getHereOrToGo());
	}
	
	default String serialiseBoolean(boolean b) {
		if (b) {
			return "1";
		} else {
			return "0";
		}
	}
	
	IDishMenuItemSerialiser getDishMenuItemSerialiser();
	IOrderFormat getOrderFormat();
}
