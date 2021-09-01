package model.order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Collection;

import model.dish.IDishMenuItemDataFactory;

public class OrderDataFactory implements IOrderDataFactory {
	@Override
	public OrderData constructData(Collection<IOrderItemData> orderData, LocalDateTime date, boolean cashOrCard,
			boolean hereOrToGo, IOrderID id) {
		return new OrderData(orderData, date, cashOrCard, hereOrToGo, id);
	}

	@Override
	public OrderData orderToData(IOrder order, IOrderItemDataFactory orderItemDatafac, IDishMenuItemDataFactory dishMenuItemDataFac) {
		OrderData data = new OrderData(
				order.getOrderItemCollection().stream().map((o) -> {return orderItemDatafac.orderItemToData(o, dishMenuItemDataFac);}).toArray(IOrderItemData[]::new),
				order.getDate(),
				order.getCashOrCard(),
				order.getHereOrToGo(),
				order.getID());
		
		data.setOrderDiscount(order.getOrderDiscount());
		
		return data;
	}

}
