package view.repository.uiwrapper;

import view.repository.IVBoxLayout;

public class UIVBoxLayout extends UILayout implements IVBoxLayout {
	public UIVBoxLayout(IVBoxLayout component) {
		super(component);
	}
	
	public IVBoxLayout getComponent() {
		return (IVBoxLayout) super.getComponent();
	}
}
