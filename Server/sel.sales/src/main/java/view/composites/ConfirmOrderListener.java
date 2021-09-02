package view.composites;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;

import controller.BusinessEvent;
import controller.IBusinessEventShooter;
import controller.IController;
import model.order.IOrderItemData;
import view.repository.HasText;
import view.repository.IRadioButton;
import view.repository.ITable;
import view.repository.uiwrapper.ClickEventListener;

public class ConfirmOrderListener extends ClickEventListener implements IBusinessEventShooter {
	private OrderInspectionArea oia;
	
	private IController controller;
	private HasText orderID;
	private ITable<IOrderItemData> orderItems;
	private IRadioButton isCash;
	private IRadioButton isHere;
	private HasText orderDiscount;
	
	public ConfirmOrderListener(IController controller, OrderInspectionArea oia) {
		super();
		this.controller = controller;
		this.oia = oia;
		this.orderID = this.oia.getOrderIDLabel();
		this.orderItems = this.oia.getOrderDetailsDisplay();
		this.isCash = this.oia.getCashRadioButton();
		this.isHere = this.oia.getHereRadioButton();
		this.orderDiscount = this.oia.getDiscountDisplay();
	}

	@Override
	protected void clickAction() {
		this.fireBusinessEvent(controller);
	}
	
	@Override
	public Object[] getArgs() {
		Collection<IOrderItemData> orderItemDataCollection = this.getOrderItems().getAllItems();		
		
		boolean isCash = this.getIsCash().isToggled();
		boolean isHere = this.getIsHere().isToggled();
		
		String orderID = this.getOrderID().getText();
		
		BigDecimal orderDiscount = BigDecimal.valueOf(Double.valueOf(this.getOrderDiscount().getText()));
		
		LocalDateTime date = this.oia.getDisplayedDate();
		
		String data = this.controller.getOrderSerialiser().serialiseOrderData(orderItemDataCollection.toArray(IOrderItemData[]::new), date, isCash, isHere, orderDiscount, orderID);
		
		this.oia.clearOrderDisplay();
		
		return new Object[] {data};
	}

	@Override
	public BusinessEvent getBusinessEvent() {
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

	private HasText getOrderDiscount() {
		return orderDiscount;
	}
}
