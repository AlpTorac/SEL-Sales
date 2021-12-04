package server.view.composites.listeners;

import java.time.LocalDateTime;
import java.util.Collection;

import controller.IApplicationEvent;
import controller.IApplicationEventShooter;
import model.dish.DishMenuItemData;
import model.entity.AccumulatingAggregateEntry;
import model.order.OrderData;
import server.controller.IServerController;
import server.controller.ServerSpecificEvent;
import server.view.composites.OrderInspectionArea;
import view.repository.HasText;
import view.repository.IRadioButton;
import view.repository.ITable;
import view.repository.uiwrapper.ClickEventListener;

public class ConfirmOrderListener extends ClickEventListener implements IApplicationEventShooter {
	private OrderInspectionArea oia;
	
	private IServerController controller;
	private HasText orderID;
	private ITable<AccumulatingAggregateEntry<DishMenuItemData>> orderItems;
	private IRadioButton isCash;
	private IRadioButton isHere;
	private HasText totalOrderDiscount;
	
	public ConfirmOrderListener(IServerController controller, OrderInspectionArea oia) {
		super();
		this.controller = controller;
		this.oia = oia;
		this.orderID = this.oia.getOrderIDLabel();
		this.orderItems = this.oia.getOrderDetailsDisplay();
		this.isCash = this.oia.getCashRadioButton();
		this.isHere = this.oia.getHereRadioButton();
		this.totalOrderDiscount = this.oia.getDiscountDisplay();
	}

	@Override
	public void clickAction() {
		this.fireApplicationEvent(controller);
	}
	
	@Override
	public Object[] getArgs() {
		boolean isCash = this.getIsCash().isToggled();
		boolean isHere = this.getIsHere().isToggled();
		
		String orderID = this.getOrderID().getText();
		
//		BigDecimal menuItemDiscounts = BigDecimal.valueOf(Double.valueOf(this.oia.getDiscountDisplay().getText()));
//		BigDecimal orderDiscount = BigDecimal.valueOf(Double.valueOf(this.getTotalOrderDiscount().getText())).subtract(menuItemDiscounts);
		
		LocalDateTime date = this.oia.getDisplayedDate();
		
//		String data = this.controller.getModel().getOrderHelper().serialiseForApp(orderItemDataCollection.toArray(OrderItem[]::new), date, isCash, isHere, orderDiscount, orderID);
		OrderData data = this.controller.getModel().getOrderFactory().constructData(orderID, date, isCash, isHere);
		Collection<AccumulatingAggregateEntry<DishMenuItemData>> orderItemDataCollection = this.getOrderItems().getAllItems();
		data.addAllOrderItems(orderItemDataCollection);
		
		this.oia.clearOrderDisplay();
		
		return new Object[] {data};
	}

	@Override
	public IApplicationEvent getApplicationEvent() {
		return ServerSpecificEvent.CONFIRM_ORDER;
	}

	private HasText getOrderID() {
		return orderID;
	}

	private ITable<AccumulatingAggregateEntry<DishMenuItemData>> getOrderItems() {
		return orderItems;
	}

	private IRadioButton getIsCash() {
		return isCash;
	}

	private IRadioButton getIsHere() {
		return isHere;
	}

	private HasText getTotalOrderDiscount() {
		return totalOrderDiscount;
	}
}
