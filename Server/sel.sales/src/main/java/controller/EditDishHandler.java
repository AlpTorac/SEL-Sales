package controller;

public class EditDishHandler extends BusinessEventHandler {
	
	EditDishHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleBusinessEvent(Object[] args) {
		this.getController().editMenuItem((String) args[0]);
	}
}
