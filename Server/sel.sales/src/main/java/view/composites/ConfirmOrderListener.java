package view.composites;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;

import controller.BusinessEvent;
import controller.IBusinessEventShooter;
import controller.IController;
import model.order.IOrderData;
import model.order.IOrderDataFactory;
import model.order.IOrderIDFactory;
import model.order.IOrderItemData;
import view.repository.HasText;
import view.repository.IRadioButton;
import view.repository.ITable;
import view.repository.uiwrapper.ClickEventListener;

public class ConfirmOrderListener extends ClickEventListener implements IBusinessEventShooter {
	private OrderInspectionArea oia;
	
	private IController controller;
	private HasText orderID;
	private HasText orderTimeInDay;
	private HasText orderDateInYear;
	private ITable<IOrderItemData> orderItems;
	private IRadioButton isCash;
	private IRadioButton isHere;
	private HasText orderDiscount;
	private String dateInYearSeperator;
	private String timeInDaySeperator;
	
	public ConfirmOrderListener(IController controller, OrderInspectionArea oia) {
		super();
		this.controller = controller;
		this.oia = oia;
		this.orderID = this.oia.getOrderIDLabel();
		this.orderTimeInDay = this.oia.getOrderTimeInDayLabel();
		this.orderDateInYear = this.oia.getOrderDateLabel();
		this.orderItems = this.oia.getOrderDetailsDisplay();
		this.isCash = this.oia.getCashRadioButton();
		this.isHere = this.oia.getHereRadioButton();
		this.orderDiscount = this.oia.getDiscountDisplay();
		this.dateInYearSeperator = OrderInspectionArea.getDateInYearSeperator();
		this.timeInDaySeperator = OrderInspectionArea.getTimeInDaySeperator();
	}

	@Override
	protected void clickAction() {
		this.fireBusinessEvent(controller);
	}
	
	@Override
	public Object[] getArgs() {
		IOrderDataFactory orderDataFac = this.controller.getOrderDataCommunicationProtocoll();
		IOrderIDFactory orderIDFac = this.controller.getOrderItemDataCommunicationProtocoll();
		Collection<IOrderItemData> orderItemDataCollection = this.getOrderItems().getAllItems();		
		Calendar date = Calendar.getInstance();
		
		String[] timeInDay = this.getOrderTimeInDay().getText().split(timeInDaySeperator);
		String[] dateInYear = this.getOrderDateInYear().getText().split(dateInYearSeperator);
		
		String hour = timeInDay[0];
		String minute = timeInDay[1];
		String second = timeInDay[2];
		
		String year = dateInYear[0];
		String month = dateInYear[1];
		String day = dateInYear[2];
		
		date.set(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day), Integer.valueOf(hour), Integer.valueOf(minute), Integer.valueOf(second));
		
		boolean isCash = this.getIsCash().isToggled();
		boolean isHere = this.getIsHere().isToggled();
		
		String orderID = this.getOrderID().getText();
		
		BigDecimal orderDiscount = BigDecimal.valueOf(Double.valueOf(this.getOrderDiscount().getText()));
		
		IOrderData data = orderDataFac.constructData(orderItemDataCollection, date, isCash, isHere, orderIDFac.createOrderID(orderID));
		
		data.setOrderDiscount(orderDiscount);
		
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

	private HasText getOrderTimeInDay() {
		return orderTimeInDay;
	}

	private HasText getOrderDateInYear() {
		return orderDateInYear;
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
