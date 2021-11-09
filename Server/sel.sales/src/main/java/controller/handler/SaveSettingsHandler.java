package controller.handler;

import controller.IController;

public class SaveSettingsHandler extends ApplicationEventHandler {

	public SaveSettingsHandler(IController controller) {
		super(controller);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().writeSettings();
	}

}
