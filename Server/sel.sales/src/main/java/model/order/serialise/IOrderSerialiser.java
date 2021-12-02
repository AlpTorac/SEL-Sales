package model.order.serialise;

import java.time.LocalDateTime;

import model.dish.IDishMenuItemData;
import model.entity.id.EntityID;
import model.order.IOrderData;
import model.order.AccumulatingOrderItemAggregate;
import model.util.ISerialiser;

public interface IOrderSerialiser extends ISerialiser {
	String serialiseOrderDatas(IOrderData[] orderDatas);
	default String serialiseOrderData(IOrderData orderData) {
		return this.serialiseOrderData(orderData.getOrderedItems(), orderData.getDate(), orderData.getIsCash(), orderData.getIsHere(), orderData.getID());
	}
	String serialiseOrderData(AccumulatingOrderItemAggregate[] orderData, LocalDateTime date, boolean isCash, boolean isHere, EntityID orderID);
	default String serialiseOrderID(EntityID orderID) {
		return orderID.toString();
	}
	
	default String serialiseOrderItems(AccumulatingOrderItemAggregate[] orderItemsData) {
		String result = "";
		for (AccumulatingOrderItemAggregate i : orderItemsData) {
			result += this.serialiseOrderItem(i);
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
	
	default String serialiseOrderItem(AccumulatingOrderItemAggregate orderItemData) {
		String result = "";
		result += this.serialiseDishMenuItemID(orderItemData.getItemData()) + this.getOrderFormat().getOrderItemFieldSeperator()
				+ this.serialiseOrderItemAmount(orderItemData) + this.getOrderFormat().getOrderItemFieldEnd();
		return result;
	}
	
//	default String getOrderItemFieldSeperator() {
//		return this.getOrderFormat().getOrderItemFieldSeperator();
//	}
//	
//	default String getOrderItemFieldEnd() {
//		return this.getOrderFormat().getOrderItemFieldEnd();
//	}
	
	default String serialiseDishMenuItemID(IDishMenuItemData menuItemData) {
		return menuItemData.getID().toString();
	}
	default String serialiseOrderItemAmount(AccumulatingOrderItemAggregate orderItemData) {
		return this.serialiseBigDecimal(orderItemData.getAmount());
	}
	
	IOrderFormat getOrderFormat();
}
