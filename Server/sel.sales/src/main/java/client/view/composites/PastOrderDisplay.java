package client.view.composites;

import controller.IController;
import model.order.OrderData;
import view.repository.uiwrapper.UIComponentFactory;

public class PastOrderDisplay extends OrderList {
	public PastOrderDisplay(IController controller, UIComponentFactory fac) {
		super(controller, fac);
	}
	@Override
	protected PastOrderEntry createOrderEntry(OrderData data) {
		return new PastOrderEntry(this.getController(), this.getUIFactory(), this, data);
	}
}