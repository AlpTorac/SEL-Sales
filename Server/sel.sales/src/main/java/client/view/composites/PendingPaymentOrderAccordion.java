package client.view.composites;

import controller.IController;
import model.order.IOrderData;
import view.repository.uiwrapper.UIComponentFactory;

public class PendingPaymentOrderAccordion extends OrderAccordion {
	public PendingPaymentOrderAccordion(IController controller, UIComponentFactory fac) {
		super(controller, fac);
	}
	
	protected PendingPaymentOrderEntry createOrderEntry(IOrderData data) {
		return new PendingPaymentOrderEntry(this.getController(), this.getUIFactory(), this, data);
	}
	
	@Override
	public PendingPaymentOrderEntry getEntry(String orderID) {
		return (PendingPaymentOrderEntry) super.getEntry(orderID);
	}
	public void setEditAvailability(boolean editEnabled) {
		this.getEntriesFromMap().forEach(e -> {
			((PendingPaymentOrderEntry) e).getEditButton().setEnabled(editEnabled);
		});
	}
}