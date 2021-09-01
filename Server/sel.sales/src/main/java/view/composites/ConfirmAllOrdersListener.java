package view.composites;

import controller.BusinessEvent;
import controller.IBusinessEventShooter;
import controller.IController;
import model.order.IOrderData;
import view.repository.uiwrapper.ClickEventListener;

public class ConfirmAllOrdersListener extends ClickEventListener implements IBusinessEventShooter {
	private IController controller;
	private OrderTrackingArea ota;
	
	public ConfirmAllOrdersListener(IController controller, OrderTrackingArea ota) {
		super();
		this.controller = controller;
		this.ota = ota;
	}
	
	protected void clickAction() {
		for (int i = this.ota.getUnconfirmedOrderList().getSize(); i > 0; i--) {
			this.fireBusinessEvent(this.controller);
		}
	}

	@Override
	public Object[] getArgs() {
		IOrderData data = this.ota.getUnconfirmedOrderList().getAllItems().stream().findFirst().get();
		return new Object[] {data};
	}

	@Override
	public BusinessEvent getBusinessEvent() {
		return BusinessEvent.CONFIRM_ORDER;
	}
}
