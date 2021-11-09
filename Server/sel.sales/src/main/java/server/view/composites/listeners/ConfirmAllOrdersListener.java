package server.view.composites.listeners;

import controller.IController;
import server.controller.ServerSpecificEvent;
import view.repository.uiwrapper.ClickEventListener;

public class ConfirmAllOrdersListener extends ClickEventListener {
	private IController controller;
	
	public ConfirmAllOrdersListener(IController controller) {
		super();
		this.controller = controller;
	}
	
	public void clickAction() {
		this.controller.handleApplicationEvent(ServerSpecificEvent.CONFIRM_ALL_ORDERS, null);
	}
}
