package client.view.composites;

import controller.IController;
import model.order.OrderData;
import view.repository.uiwrapper.UIComponentFactory;

public class PendingPaymentOrderDisplay extends OrderList {
	public PendingPaymentOrderDisplay(IController controller, UIComponentFactory fac) {
		super(controller, fac);
	}
	
	protected PendingPaymentOrderEntry createOrderEntry(OrderData data) {
		return new PendingPaymentOrderEntry(this.getController(), this.getUIFactory(), this, data);
	}
	
	@Override
	public PendingPaymentOrderEntry getEntry(String orderID) {
		return (PendingPaymentOrderEntry) super.getEntry(orderID);
	}
	public void setEditAvailability(boolean editEnabled) {
		this.getEntryCollection().forEach(e -> {
			((PendingPaymentOrderEntry) e).getEditButton().setEnabled(editEnabled);
		});
	}
}