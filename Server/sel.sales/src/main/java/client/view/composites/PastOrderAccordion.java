package client.view.composites;

import controller.IController;
import model.order.IOrderData;
import view.repository.uiwrapper.UIComponentFactory;

public class PastOrderAccordion extends OrderAccordion {
	public PastOrderAccordion(IController controller, UIComponentFactory fac) {
		super(controller, fac);
	}
	
	protected OrderEntry createOrderEntry(IOrderData data) {
		return new PastOrderEntry(this.getController(), this.getUIFactory(), this, data);
	}
}