package view.composites.listeners;

import controller.BusinessEvent;
import controller.IApplicationEvent;
import controller.IApplicationEventShooter;
import controller.IController;
import view.composites.OrderInspectionArea;
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
		return BusinessEvent.REMOVE_ORDER;
	}

}
