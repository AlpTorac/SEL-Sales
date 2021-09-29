package controller.handler;

import controller.IController;

public abstract class BusinessEventHandler extends ApplicationEventHandler {
	BusinessEventHandler(IController controller) {
		super(controller);
	}
}
