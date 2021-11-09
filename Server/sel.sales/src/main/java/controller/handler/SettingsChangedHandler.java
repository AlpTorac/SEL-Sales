package controller.handler;

import controller.IController;

public class SettingsChangedHandler extends ApplicationEventHandler {

	public SettingsChangedHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().setSettings((String[][]) args[0]);
	}

}
