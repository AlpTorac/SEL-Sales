package server.view.composites;

import controller.IController;
import view.composites.ConnectionArea;
import view.repository.uiwrapper.UIComponentFactory;

public class ServerConnectionArea extends ConnectionArea {
	public ServerConnectionArea(IController controller, UIComponentFactory fac) {
		super(controller, fac);
	}
}
