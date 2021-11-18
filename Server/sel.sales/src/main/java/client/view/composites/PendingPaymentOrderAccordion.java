package client.view.composites;

import controller.IController;
import model.order.IOrderData;
import view.repository.uiwrapper.UIComponentFactory;

public class PendingPaymentOrderAccordion extends OrderAccordion {
	public PendingPaymentOrderAccordion(IController controller, UIComponentFactory fac) {
		super(controller, fac);
	}
	
	protected OrderEntry createOrderEntry(IOrderData data) {
		return new PendingPaymentOrderEntry(this.getController(), this.getUIFactory(), this, data);
	}
}