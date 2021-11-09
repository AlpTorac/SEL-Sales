package client.view.composites;

import controller.IController;
import view.repository.uiwrapper.AdvancedUIComponentFactory;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UILayout;

public class ClientConnectionArea extends UILayout {
	public ClientConnectionArea(IController controller, UIComponentFactory fac, AdvancedUIComponentFactory advFac) {
		super(fac.createVBoxLayout().getComponent());
	}
}
