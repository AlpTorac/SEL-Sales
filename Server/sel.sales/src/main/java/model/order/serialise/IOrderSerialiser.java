package model.order.serialise;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import model.dish.IDishMenuItemData;
import model.order.IOrderData;
import model.order.IOrderItemData;

public interface IOrderSerialiser {
	String serialiseOrderData(IOrderItemData[] orderItemsData, LocalDateTime date, boolean isCash, boolean isHere, BigDecimal orderDiscount, String orderID);
	
	default String serialiseOrderID(String orderID) {
		return orderID;
	}
	
	default String serialiseOrderDiscount(BigDecimal orderDiscount) {
		return this.serialiseBigDecimal(orderDiscount);
	}
	
	default String serialiseOrderItemDatas(IOrderItemData[] orderItemsData) {
		String result = "";
		for (IOrderItemData i : orderItemsData) {
			result += this.serialiseOrderItemData(i);
		}
		return result;
	}
	
	default String serialiseOrderDate(LocalDateTime date) {
		return this.getOrderFormat().formatDate(date);
	}
	
	default String serialiseIsCash(boolean isCash) {
		return this.serialiseBoolean(isCash);
	}
	
	default String serialiseIsHere(boolean isHere) {
		return this.serialiseBoolean(isHere);
	}
	
	default String getOrderDataFieldSeperator() {
		return this.getOrderFormat().getOrderDataFieldSeperator();
	}
	
	default String getOrderDataFieldEnd() {
		return this.getOrderFormat().getOrderDataFieldEnd();
	}
	
	default String serialiseOrderData(IOrderData orderData) {
		return this.serialiseOrderData(orderData.getOrderedItems(), orderData.getDate(), orderData.getIsCash(), orderData.getIsHere(), orderData.getOrderDiscount(), orderData.getID());
	}
	
	default String serialiseOrderItemData(IOrderItemData orderItemData) {
		String result = "";
		result += this.serialiseDishMenuItemID(orderItemData.getItemData()) + this.getOrderItemDataFieldSeperator() 
				+ this.serialiseOrderItemAmount(orderItemData) + this.getOrderItemDataFieldEnd();
		return result;
	}
	
	default String getOrderItemDataFieldSeperator() {
		return this.getOrderFormat().getOrderItemDataFieldSeperator();
	}
	
	default String getOrderItemDataFieldEnd() {
		return this.getOrderFormat().getOrderItemDataFieldEnd();
	}
	
	default String serialiseDishMenuItemID(IDishMenuItemData menuItemData) {
		return menuItemData.getId();
	}
	default String serialiseOrderItemAmount(IOrderItemData orderItemData) {
		return this.serialiseBigDecimal(orderItemData.getAmount());
	}
	
	default String serialiseBigDecimal(BigDecimal bd) {
		return bd.toPlainString();
	}
	
	default String serialiseBoolean(boolean b) {
		if (b) {
			return "1";
		} else {
			return "0";
		}
	}
	
	IOrderFormat getOrderFormat();
}