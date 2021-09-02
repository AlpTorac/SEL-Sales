package controller;

public class RemoveDishHandler extends BusinessEventHandler {

	RemoveDishHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleBusinessEvent(Object[] args) {
		this.getController().removeMenuItem((String) args[0]);
	}
}
