package client.view.composites;

import controller.IController;
import view.repository.uiwrapper.AdvancedUIComponentFactory;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UILayout;

public class UnpaidOrdersArea extends UILayout {
	private IController controller;
	private UIComponentFactory fac;
	private AdvancedUIComponentFactory advFac;
	
	protected UnpaidOrdersArea(IController controller, UIComponentFactory fac, AdvancedUIComponentFactory advFac) {
		super(fac.createVBoxLayout().getComponent());
		this.controller = controller;
		this.fac = fac;
		this.advFac = advFac;
	}
}
