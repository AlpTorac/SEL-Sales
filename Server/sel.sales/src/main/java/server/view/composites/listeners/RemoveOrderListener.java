package server.view.composites.listeners;

import controller.GeneralEvent;
import controller.IApplicationEvent;
import controller.IApplicationEventShooter;
import controller.IController;
import server.view.composites.OrderInspectionArea;
import view.repository.uiwrapper.ClickEventListener;

public class RemoveOrderListener extends ClickEventListener implements IApplicationEventShooter {
	private IController controller;
	private OrderInspectionArea oia;
	
	public RemoveOrderListener(IController controller, OrderInspectionArea oia) {
		this.controller = controller;
		this.oia = oia;
	}
	
	public void clickAction() {
		this.fireApplicationEvent(controller);
	}
	
	@Override
	public Object[] getArgs() {
		String id = this.oia.getOrderIDLabel().getText();
		this.oia.clearOrderDisplay();
		return new Object[] {id};
	}

	@Override
	public IApplicationEvent getApplicationEvent() {
		return GeneralEvent.REMOVE_ORDER;
	}

}
