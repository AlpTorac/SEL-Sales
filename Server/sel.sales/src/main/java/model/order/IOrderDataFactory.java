package model.order;

import java.util.Calendar;
import java.util.Collection;

public interface IOrderDataFactory {
	IOrderData constructData(Collection<IOrderItemData> orderData, Calendar date, boolean cashOrCard, boolean hereOrToGo, IOrderID id);
	default IOrderData orderToData(IOrder order) {
		return order.getOrderData(this);
	}
}
