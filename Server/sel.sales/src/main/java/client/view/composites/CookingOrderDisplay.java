package client.view.composites;

import controller.IController;
import model.order.OrderData;
import view.repository.uiwrapper.UIComponentFactory;

public class CookingOrderDisplay extends OrderList {
	public CookingOrderDisplay(IController controller, UIComponentFactory fac) {
		super(controller, fac);
	}
	@Override
	protected CookingOrderEntry createOrderEntry(OrderData data) {
		return new CookingOrderEntry(this.getController(), this.getUIFactory(), this, data);
	}
	public void setEditAvailability(boolean editEnabled) {
		this.getEntryCollection().forEach(e -> {
			((CookingOrderEntry) e).getEditButton().setEnabled(editEnabled);
		});
	}
}