package model.order.serialise;

import java.time.LocalDateTime;

import model.dish.IDishMenuItemData;
import model.id.EntityID;
import model.order.IOrderData;
import model.order.IOrderItemData;
import model.util.ISerialiser;

public interface IOrderSerialiser extends ISerialiser {
	String serialiseOrderDatas(IOrderData[] orderDatas);
	default String serialiseOrderData(IOrderData orderData) {
		return this.serialiseOrderData(orderData.getOrderedItems(), orderData.getDate(), orderData.getIsCash(), orderData.getIsHere(), orderData.getID());
	}
	String serialiseOrderData(IOrderItemData[] orderData, LocalDateTime date, boolean isCash, boolean isHere, EntityID orderID);
	default String serialiseOrderID(EntityID orderID) {
		return orderID.toString();
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
	
//	default String getOrderDataFieldSeperator() {
//		return this.getOrderFormat().getOrderDataFieldSeperator();
//	}
//	
//	default String getOrderDataFieldEnd() {
//		return this.getOrderFormat().getOrderDataFieldEnd();
//	}
	
	default String serialiseOrderItemData(IOrderItemData orderItemData) {
		String result = "";
		result += this.serialiseDishMenuItemID(orderItemData.getItemData()) + this.getOrderFormat().getOrderItemDataFieldSeperator()
				+ this.serialiseOrderItemAmount(orderItemData) + this.getOrderFormat().getOrderItemDataFieldEnd();
		return result;
	}
	
//	default String getOrderItemDataFieldSeperator() {
//		return this.getOrderFormat().getOrderItemDataFieldSeperator();
//	}
//	
//	default String getOrderItemDataFieldEnd() {
//		return this.getOrderFormat().getOrderItemDataFieldEnd();
//	}
	
	default String serialiseDishMenuItemID(IDishMenuItemData menuItemData) {
		return menuItemData.getID().toString();
	}
	default String serialiseOrderItemAmount(IOrderItemData orderItemData) {
		return this.serialiseBigDecimal(orderItemData.getAmount());
	}
	
	IOrderFormat getOrderFormat();
}
