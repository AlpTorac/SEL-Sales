package client.view.composites;

import controller.IController;
import view.composites.ConnectionArea;
import view.repository.uiwrapper.AdvancedUIComponentFactory;
import view.repository.uiwrapper.UIComponentFactory;

public class ClientConnectionArea extends ConnectionArea {
	public ClientConnectionArea(IController controller, UIComponentFactory fac, AdvancedUIComponentFactory advFac) {
		super(controller, fac, advFac);
	}
}
