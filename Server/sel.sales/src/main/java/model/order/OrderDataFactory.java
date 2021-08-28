package model.order;

import java.util.Calendar;
import java.util.Collection;

public class OrderDataFactory implements IOrderDataFactory {
	@Override
	public OrderData constructData(Collection<IOrderItemData> orderData, Calendar date, boolean cashOrCard,
			boolean hereOrToGo, IOrderID id) {
		return new OrderData(orderData, date, cashOrCard, hereOrToGo, id);
	}

}
