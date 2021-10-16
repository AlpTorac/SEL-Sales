package view.composites.listeners;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;

import controller.BusinessEvent;
import controller.IApplicationEvent;
import controller.IApplicationEventShooter;
import controller.IController;
import model.order.IOrderItemData;
import view.composites.OrderInspectionArea;
import view.repository.HasText;
import view.repository.IRadioButton;
import view.repository.ITable;
import view.repository.uiwrapper.ClickEventListener;

public class ConfirmOrderListener extends ClickEventListener implements IApplicationEventShooter {
	private OrderInspectionArea oia;
	
	private IController controller;
	private HasText orderID;
	private ITable<IOrderItemData> orderItems;
	private IRadioButton isCash;
	private IRadioButton isHere;
	private HasText totalOrderDiscount;
	
	public ConfirmOrderListener(IController controller, OrderInspectionArea oia) {
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
		Collection<IOrderItemData> orderItemDataCollection = this.getOrderItems().getAllItems();		
		
		boolean isCash = this.getIsCash().isToggled();
		boolean isHere = this.getIsHere().isToggled();
		
		String orderID = this.getOrderID().getText();
		
		BigDecimal menuItemDiscounts = orderItemDataCollection.stream().map(i -> i.getTotalDiscount()).reduce(BigDecimal.ZERO, (sum, i) -> sum = sum.add(i));
		BigDecimal orderDiscount = BigDecimal.valueOf(Double.valueOf(this.getTotalOrderDiscount().getText())).subtract(menuItemDiscounts);
		
		LocalDateTime date = this.oia.getDisplayedDate();
		
		String data = this.controller.getModel().getOrderSerialiser().serialiseOrderData(orderItemDataCollection.toArray(IOrderItemData[]::new), date, isCash, isHere, orderDiscount, orderID);
		
		this.oia.clearOrderDisplay();
		
		return new Object[] {data};
	}

	@Override
	public IApplicationEvent getApplicationEvent() {
		return BusinessEvent.CONFIRM_ORDER;
	}

	private HasText getOrderID() {
		return orderID;
	}

	private ITable<IOrderItemData> getOrderItems() {
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
