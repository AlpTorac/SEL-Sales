package model.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

import model.dish.IDishMenuItemDataFactory;

public interface IOrderDataFactory {
	default IOrderData constructData(Collection<IOrderItemData> orderData, LocalDateTime date, boolean cashOrCard, boolean hereOrToGo, BigDecimal orderDiscount, IOrderID id) {
		IOrderData data = this.constructData(orderData, date, cashOrCard, hereOrToGo, id);
		data.setOrderDiscount(orderDiscount);
		return data;
	}
	IOrderData constructData(Collection<IOrderItemData> orderData, LocalDateTime date, boolean cashOrCard, boolean hereOrToGo, IOrderID id);
	IOrderData orderToData(IOrder order, IOrderItemDataFactory orderItemDatafac, IDishMenuItemDataFactory dishMenuItemDataFac);
}
