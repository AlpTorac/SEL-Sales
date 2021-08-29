package controller;

public class AddOrderHandler extends BusinessEventHandler {

	AddOrderHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleBusinessEvent(Object[] args) {
		this.getController().addOrder((String) args[0]);
	}

}
