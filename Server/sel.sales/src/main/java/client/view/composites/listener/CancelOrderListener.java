package client.view.composites.listener;

import client.view.composites.OrderEntry;
import controller.GeneralEvent;
import controller.IApplicationEvent;
import controller.IApplicationEventShooter;
import controller.IController;
import view.repository.uiwrapper.ClickEventListener;

public class CancelOrderListener extends ClickEventListener implements IApplicationEventShooter {
	private IController controller;
	private OrderEntry oe;
	
	public CancelOrderListener(OrderEntry oe, IController controller) {
		super();
		this.controller = controller;
		this.oe = oe;
	}
	
	@Override
	public void clickAction() {
		if (this.oe.getActiveData() != null) {
			this.fireApplicationEvent(this.controller);
		}
		this.oe.resetUserInput();
	}
	
	@Override
	public Object[] getArgs() {
		return new Object[] {this.oe.getSerialisedOrderID()};
	}

	@Override
	public IApplicationEvent getApplicationEvent() {
		return GeneralEvent.REMOVE_ORDER;
	}

}
