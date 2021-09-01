package view.composites;

import controller.BusinessEvent;
import controller.IBusinessEventShooter;
import controller.IController;
import model.order.IOrderID;
import model.order.IOrderIDFactory;
import view.repository.uiwrapper.ClickEventListener;

public class RemoveOrderListener extends ClickEventListener implements IBusinessEventShooter {
	private IController controller;
	private OrderInspectionArea oia;
	
	public RemoveOrderListener(IController controller, OrderInspectionArea oia) {
		this.controller = controller;
		this.oia = oia;
	}
	
	public void clickAction() {
		this.fireBusinessEvent(controller);
	}
	
	@Override
	public Object[] getArgs() {
		IOrderIDFactory orderIDFac = this.controller.getOrderItemDataCommunicationProtocoll();
		IOrderID id = orderIDFac.createOrderID(this.oia.getOrderIDLabel().getText());
		
		this.oia.clearOrderDisplay();
		
		return new Object[] {id};
	}

	@Override
	public BusinessEvent getBusinessEvent() {
		return BusinessEvent.REMOVE_ORDER;
	}

}
