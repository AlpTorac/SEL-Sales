package controller.handler;

import controller.IController;

public abstract class StatusEventHandler extends ApplicationEventHandler {
	StatusEventHandler(IController controller) {
		super(controller);
	}
}
