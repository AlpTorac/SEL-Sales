package server.view.composites;

import controller.IController;
import view.composites.ConnectionArea;
import view.repository.uiwrapper.AdvancedUIComponentFactory;
import view.repository.uiwrapper.UIComponentFactory;

public class ServerConnectionArea extends ConnectionArea {
	public ServerConnectionArea(IController controller, UIComponentFactory fac, AdvancedUIComponentFactory advFac) {
		super(controller, fac, advFac);
	}
}
