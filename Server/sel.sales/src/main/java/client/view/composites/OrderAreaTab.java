package client.view.composites;

import controller.IController;
import view.repository.IHBoxLayout;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIHBoxLayout;

public class OrderAreaTab extends UIHBoxLayout {

	protected IController controller;
	protected UIComponentFactory fac;

	public OrderAreaTab(IHBoxLayout component) {
		super(component);
	}

}