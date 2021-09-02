package model.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface IOrderDataFactory {
	default IOrderData constructData(IOrderItemData[] orderData, LocalDateTime date, boolean cashOrCard, boolean hereOrToGo, BigDecimal orderDiscount, String id) {
		IOrderData data = this.constructData(orderData, date, cashOrCard, hereOrToGo, id);
		data.setOrderDiscount(orderDiscount);
		return data;
	}
	IOrderData constructData(IOrderItemData[] orderData, LocalDateTime date, boolean cashOrCard, boolean hereOrToGo, String id);
	IOrderData orderToData(IOrder order);
}
