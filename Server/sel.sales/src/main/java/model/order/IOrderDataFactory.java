package model.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface IOrderDataFactory {
	default IOrderData constructData(IOrderItemData[] orderData, LocalDateTime date, boolean isCash, boolean isHere, BigDecimal orderDiscount, String id) {
		IOrderData data = this.constructData(orderData, date, isCash, isHere, id);
		data.setOrderDiscount(orderDiscount);
		return data;
	}
	IOrderData constructData(IOrderItemData[] orderData, LocalDateTime date, boolean isCash, boolean isHere, String id);
	IOrderData orderToData(IOrder order);
	IOrderItemDataFactory getItemDataFac();
}
