package model.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import model.id.EntityID;

public interface IOrderDataFactory {
	default IOrderData constructData(IOrderItemData[] orderData, LocalDateTime date, boolean isCash, boolean isHere, BigDecimal orderDiscount, EntityID id) {
		return this.constructData(orderData, date, isCash, isHere, id);
	}
	IOrderData constructData(IOrderItemData[] orderData, LocalDateTime date, boolean isCash, boolean isHere, EntityID id);
	IOrderData constructData(IOrderItemData[] orderData, LocalDateTime date, boolean isCash, boolean isHere, Object... idParameters);
	IOrderData orderToData(IOrder order);
	IOrderItemDataFactory getItemDataFac();
}
