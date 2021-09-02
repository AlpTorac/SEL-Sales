package controller;

public class ConfirmOrderHandler extends BusinessEventHandler {

	ConfirmOrderHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleBusinessEvent(Object[] args) {
		this.getController().confirmOrder((String) args[0]);
	}

}
