package client.view.composites;

import controller.IController;
import model.order.IOrderData;
import view.repository.uiwrapper.UIComponentFactory;

public class CookingOrderAccordion extends OrderAccordion {
	public CookingOrderAccordion(IController controller, UIComponentFactory fac) {
		super(controller, fac);
	}
	@Override
	protected CookingOrderEntry createOrderEntry(IOrderData data) {
		return new CookingOrderEntry(this.getController(), this.getUIFactory(), this, data);
	}
	public void setEditAvailability(boolean editEnabled) {
		this.getEntriesFromMap().forEach(e -> {
			((CookingOrderEntry) e).getEditButton().setEnabled(editEnabled);
		});
	}
}