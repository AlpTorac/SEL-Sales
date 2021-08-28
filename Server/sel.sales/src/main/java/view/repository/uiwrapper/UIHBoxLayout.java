package view.repository.uiwrapper;

import view.repository.IHBoxLayout;

public class UIHBoxLayout extends UILayout implements IHBoxLayout {
	public UIHBoxLayout(IHBoxLayout component) {
		super(component);
	}
	
	public IHBoxLayout getComponent() {
		return (IHBoxLayout) super.getComponent();
	}
}
