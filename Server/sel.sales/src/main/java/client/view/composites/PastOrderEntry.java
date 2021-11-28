package client.view.composites;

import controller.IController;
import model.order.IOrderData;
import view.repository.uiwrapper.UIComponentFactory;

public class PastOrderEntry extends OrderEntry {
	public PastOrderEntry(IController controller, UIComponentFactory fac, PriceUpdateTarget<OrderEntry> notifyTarget,
			IOrderData data) {
		super(controller, fac, notifyTarget, data);
	}
	
	@Override
	protected void noEntryAction() {
		this.removeFromParent();
	}
}