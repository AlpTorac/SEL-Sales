package client.view.composites.listener;

import client.view.composites.OrderEntry;
import controller.GeneralEvent;
import controller.IApplicationEvent;
import controller.IApplicationEventShooter;
import controller.IController;
import view.repository.uiwrapper.ClickEventListener;

public class AddCookingOrderListener extends ClickEventListener implements IApplicationEventShooter {
	
	private IController controller;
	private OrderEntry oe;
	
	public AddCookingOrderListener(OrderEntry oe, IController controller) {
		super();
		this.controller = controller;
		this.oe = oe;
	}
	
	@Override
	public void clickAction() {
		if (this.oe.getCurrentOrder().length > 0) {
			this.fireApplicationEvent(this.controller);
		}
		this.oe.orderSentToNextTab();
	}
	
	@Override
	public Object[] getArgs() {
		return new Object[] {this.oe.serialiseCurrentOrder()};
	}

	@Override
	public IApplicationEvent getApplicationEvent() {
		return GeneralEvent.ADD_ORDER;
	}

}
