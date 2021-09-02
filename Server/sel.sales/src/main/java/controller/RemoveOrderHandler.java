package controller;

public class RemoveOrderHandler extends BusinessEventHandler {

	RemoveOrderHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleBusinessEvent(Object[] args) {
		this.getController().removeOrder((String) args[0]);
	}

}
