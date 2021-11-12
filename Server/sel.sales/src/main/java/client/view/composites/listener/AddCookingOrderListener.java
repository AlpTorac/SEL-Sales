package client.view.composites.listener;

import client.view.composites.OrderTakingArea;
import controller.GeneralEvent;
import controller.IApplicationEvent;
import controller.IApplicationEventShooter;
import controller.IController;
import view.repository.uiwrapper.ClickEventListener;

public class AddCookingOrderListener extends ClickEventListener implements IApplicationEventShooter {
	
	private IController controller;
	private OrderTakingArea ota;
	
	public AddCookingOrderListener(OrderTakingArea ota, IController controller) {
		super();
		this.controller = controller;
		this.ota = ota;
	}
	
	@Override
	public void clickAction() {
		this.fireApplicationEvent(this.controller);
		this.ota.resetUserInput();
	}
	
	@Override
	public Object[] getArgs() {
		return new Object[] {this.ota.serialiseCurrentOrder()};
	}

	@Override
	public IApplicationEvent getApplicationEvent() {
		return GeneralEvent.ADD_ORDER;
	}

}
