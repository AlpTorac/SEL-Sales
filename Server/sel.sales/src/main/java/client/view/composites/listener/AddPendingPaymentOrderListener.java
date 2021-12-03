package client.view.composites.listener;

import client.controller.ClientSpecificEvent;
import client.view.composites.OrderEntry;
import controller.IApplicationEvent;
import controller.IApplicationEventShooter;
import controller.IController;
import view.repository.uiwrapper.ClickEventListener;

public class AddPendingPaymentOrderListener extends ClickEventListener implements IApplicationEventShooter {
	
	private IController controller;
	private OrderEntry oe;
	
	public AddPendingPaymentOrderListener(OrderEntry oe, IController controller) {
		super();
		this.controller = controller;
		this.oe = oe;
	}
	
	@Override
	public void clickAction() {
		this.fireApplicationEvent(this.controller);
		this.oe.removeFromParent();
	}
	
	@Override
	public Object[] getArgs() {
		return new Object[] {this.oe.getSerialisedOrderID()};
	}

	@Override
	public IApplicationEvent getApplicationEvent() {
		return ClientSpecificEvent.ADD_PENDING_PAYMENT_ORDER;
	}

}
