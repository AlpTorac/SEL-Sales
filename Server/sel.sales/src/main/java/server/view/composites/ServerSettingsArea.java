package server.view.composites;

import controller.IController;
import view.composites.SettingsArea;
import view.repository.IRootComponent;
import view.repository.uiwrapper.UIComponentFactory;

public class ServerSettingsArea extends SettingsArea {
	public ServerSettingsArea(IController controller, UIComponentFactory fac, IRootComponent mainWindow) {
		super(controller, fac, mainWindow);
	}
}
