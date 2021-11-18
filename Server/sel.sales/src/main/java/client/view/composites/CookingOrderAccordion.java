package client.view.composites;

import controller.IController;
import model.order.IOrderData;
import view.repository.uiwrapper.UIComponentFactory;

public class CookingOrderAccordion extends OrderAccordion {
	public CookingOrderAccordion(IController controller, UIComponentFactory fac) {
		super(controller, fac);
	}
	
	protected OrderEntry createOrderEntry(IOrderData data) {
		return new CookingOrderEntry(this.getController(), this.getUIFactory(), this, data);
	}
}