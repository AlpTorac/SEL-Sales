package client.view.composites;

import controller.IController;
import view.repository.uiwrapper.AdvancedUIComponentFactory;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UILayout;

public class ClientSettingsArea extends UILayout {
	public ClientSettingsArea(IController controller, UIComponentFactory fac, AdvancedUIComponentFactory advFac) {
		super(fac.createVBoxLayout().getComponent());
	}
}
