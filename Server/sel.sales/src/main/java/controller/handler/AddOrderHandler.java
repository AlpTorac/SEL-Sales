package controller.handler;

import controller.IController;
import model.order.OrderData;

public class AddOrderHandler extends ApplicationEventHandler {

	public AddOrderHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		Object arg = args[0];
		if (arg instanceof OrderData) {
			this.getController().getModel().addOrder((OrderData) arg);
		} else if (arg instanceof String) {
			this.getController().getModel().addOrder((String) arg);
		} else {
			throw new IllegalArgumentException(arg.getClass().getSimpleName() + " is not a valid type for adding orders");
		}
	}

}
