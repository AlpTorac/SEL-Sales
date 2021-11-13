package client.view.composites.listener;

import client.controller.ClientSpecificEvent;
import client.view.composites.OrderEntry;
import controller.IApplicationEvent;
import controller.IApplicationEventShooter;
import controller.IController;
import view.repository.uiwrapper.ClickEventListener;

public class EditOrderListener extends ClickEventListener implements IApplicationEventShooter {
	
	private IController controller;
	private OrderEntry oe;
	
	public EditOrderListener(OrderEntry oe, IController controller) {
		super();
		this.controller = controller;
		this.oe = oe;
	}
	
	@Override
	public void clickAction() {
		this.fireApplicationEvent(this.controller);
	}
	
	@Override
	public Object[] getArgs() {
		return new Object[] {this.oe.getSerialisedOrderID()};
	}

	@Override
	public IApplicationEvent getApplicationEvent() {
		return ClientSpecificEvent.EDIT_ORDER;
	}
}
