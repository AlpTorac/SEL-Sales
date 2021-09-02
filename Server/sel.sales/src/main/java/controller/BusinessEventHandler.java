package controller;

public abstract class BusinessEventHandler implements IBusinessEventHandler {
	private IController controller;
	
	BusinessEventHandler(IController controller) {
		this.controller = controller;
	}
	
	IController getController() {
		return this.controller;
	}
}