package client.view.composites;

import controller.IController;
import view.composites.SettingsArea;
import view.repository.IRootComponent;
import view.repository.uiwrapper.UIComponentFactory;

public class ClientSettingsArea extends SettingsArea {
	public ClientSettingsArea(IController controller, UIComponentFactory fac, IRootComponent mainWindow) {
		super(controller, fac, mainWindow);
	}
}
