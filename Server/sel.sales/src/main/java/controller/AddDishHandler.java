package controller;

public class AddDishHandler extends BusinessEventHandler {

	AddDishHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleBusinessEvent(Object[] args) {
		this.getController().addMenuItem((String) args[0]);
	}
}
