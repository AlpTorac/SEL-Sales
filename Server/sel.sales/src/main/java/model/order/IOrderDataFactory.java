package model.order;

import java.util.Calendar;
import java.util.Collection;

import model.dish.IDishMenuItemDataFactory;

public interface IOrderDataFactory {
	IOrderData constructData(Collection<IOrderItemData> orderData, Calendar date, boolean cashOrCard, boolean hereOrToGo, IOrderID id);
	IOrderData orderToData(IOrder order, IOrderItemDataFactory orderItemDatafac, IDishMenuItemDataFactory dishMenuItemDataFac);
}
