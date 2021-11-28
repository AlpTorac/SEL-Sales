package client.controller.handler;

import client.controller.IClientController;
import model.settings.SettingsField;

public class AvailableTableNumbersReceivedHandler extends ClientApplicationEventHandler {
	public AvailableTableNumbersReceivedHandler(IClientController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().addSetting(SettingsField.TABLE_NUMBERS, (String) args[0]);
	}
}
