package view.composites;

import controller.BusinessEvent;
import controller.IController;
import view.repository.uiwrapper.ClickEventListener;

public class ConfirmAllOrdersListener extends ClickEventListener {
	private IController controller;
	
	public ConfirmAllOrdersListener(IController controller) {
		super();
		this.controller = controller;
	}
	
	public void clickAction() {
		this.controller.handleApplicationEvent(BusinessEvent.CONFIRM_ALL_ORDERS, null);
	}
}
